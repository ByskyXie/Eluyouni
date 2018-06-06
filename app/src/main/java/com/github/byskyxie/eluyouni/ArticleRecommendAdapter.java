package com.github.byskyxie.eluyouni;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class ArticleRecommendAdapter extends RecyclerView.Adapter<ArticleRecommendAdapter.ArticleRecommendHolder>
                implements View.OnClickListener{
    private ArrayList<ArticleRecommend> list = new ArrayList<>();
    private Context context;
    private IndexFragment.IndexHandler handler;

    static class ArticleRecommendHolder extends RecyclerView.ViewHolder{
        private View view;
        private TextView content;
        private TextView title;
        private TextView name;
        private ImageView icon;
        private ImageView pic;
        ArticleRecommendHolder(View itemView) {
            super(itemView);
            view = itemView;
            content = itemView.findViewById(R.id.text_view_article_recom_content);
            title = itemView.findViewById(R.id.text_view_article_recom_title);
            name = itemView.findViewById(R.id.text_view_article_recom_name);
            icon = itemView.findViewById(R.id.image_view_article_recom_icon);
            pic = itemView.findViewById(R.id.image_view_article_recom_pic);
        }
    }

    ArticleRecommendAdapter(Context context, ArrayList<ArticleRecommend> list, IndexFragment.IndexHandler handler) {
        this.context = context;
        this.handler = handler;
        if(list != null)
            this.list.addAll(list);
    }

    protected void addData(ArrayList<ArticleRecommend> list){
        if(list != null)
            this.list.addAll(list);
    }

    protected ArrayList<ArticleRecommend> getData(){
        return list;
    }

    protected boolean compareDataSetSame(ArrayList<ArticleRecommend> list){
        if(list == null)
            return true;
        if(this.list.isEmpty())
            return false;
        return this.list.get(0).getTitle().equals( list.get(0).getTitle() );
    }

    @NonNull
    @Override
    public ArticleRecommendAdapter.ArticleRecommendHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_article_recommend,parent,false);
        return new ArticleRecommendAdapter.ArticleRecommendHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleRecommendAdapter. ArticleRecommendHolder holder, int position) {
        int actPos = holder.getAdapterPosition();
        holder.view.setTag(actPos);
        holder.view.setOnClickListener(this);
        holder.title.setText( list.get(actPos).getTitle() );
        holder.content.setText( list.get(actPos).getContent() );
        if( list.get(actPos).getErtype() == 1 ){
            //获取患者姓名
            Cursor cursor = BaseActivity.userDatabaseRead.query("PATIENT_BASE_INFO",new String[]{"*"}
                    , "PID=? ",new String[]{""+list.get(actPos).getErid() },null,null,null,null);
            if(cursor.moveToFirst()){
                //设置姓名
                holder.name.setText( cursor.getString( cursor.getColumnIndex("PNAME") ) );
                String icon = cursor.getString( cursor.getColumnIndex("PICON") );
                int sex = cursor.getInt( cursor.getColumnIndex("PSEX") );
                //默认头像
                if(sex == 2)
                    holder.icon.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_patient_female));
                else
                    holder.icon.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_patient_male));
                if(icon != null && !icon.isEmpty() && !icon.equalsIgnoreCase("null")){
                    //查看picon有没有下载
                    if(new File(context.getFilesDir().getAbsolutePath()+"/icon/picon/"+icon).exists() ){
                        holder.icon.setImageBitmap(BitmapFactory.decodeFile(context //用户头像
                                .getFilesDir().getAbsolutePath()+"/icon/picon/"+icon) );
                    }else{//下载并notify
                        downloadPicon(icon,actPos);
                    }
                }
            }
            cursor.close();
        }else if( list.get(actPos).getErtype() == 2 ){
            //获取医生姓名
            Cursor cursor = BaseActivity.userDatabaseRead.query("DOCTOR_BASE_INFO",new String[]{"*"}
                    , "DID=? ",new String[]{""+list.get(actPos).getErid() },null,null,null,null);
            if(cursor.moveToFirst()){
                //设置姓名
                holder.name.setText( cursor.getString( cursor.getColumnIndex("DNAME") ) );
                String icon = cursor.getString( cursor.getColumnIndex("DICON") );
                //默认头像
                if( cursor.getInt(cursor.getColumnIndex("DSEX"))==2 )
                    holder.icon.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_doctor_female));//女默认
                else
                    holder.icon.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_doctor_male));   //男默认
                if(icon != null && !icon.isEmpty() && !icon.equalsIgnoreCase("null")){
                    //查看dicon有没有下载
                    if(new File(context.getFilesDir().getAbsolutePath()+"/icon/dicon/"+icon).exists() ){
                        holder.icon.setImageBitmap(BitmapFactory.decodeFile(context //用户头像
                                .getFilesDir().getAbsolutePath()+"/icon/dicon/"+icon) );
                    }else{//下载并notify
                        downloadDicon(icon,actPos);
                    }
                }
            }
            cursor.close();
        }else{
            Log.e("ArticleRecomAdapter","Error er type!");
        }
    }

    private void downloadDicon(final String dicon, final int position){
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(! ((BaseActivity)context).downloadDicon(dicon) )
                    return; //下载失败就算了
                Message msg = new Message();
                msg.what = IndexPagerAdapter.RECOMMEND_ICON_ACCEPT;
                msg.obj = position;
                handler.sendMessage(msg);
            }
        }).start();
    }

    private void downloadPicon(final String picon, final int position){
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(! ((BaseActivity)context).downloadPicon(picon) )
                    return; //下载失败就算了
                Message msg = new Message();
                msg.what = IndexPagerAdapter.RECOMMEND_ICON_ACCEPT;
                msg.obj = position;
                handler.sendMessage(msg);
            }
        }).start();
    }

    @Override
    public void onClick(View v) {
        int pos = (int)v.getTag();
        ArticleRecommend ar = list.get(pos);
        Intent intent = new Intent(context, ShowArticleActivity.class);
        intent.putExtra("TITLE",context.getString(R.string.pager_recommend));
        if(ar.getErtype() == 1)
            intent.putExtra("ARTICLE", new ArticlePatient(ar.getArid(), ar.getErid(), ar.getTitle(), ar.getTime(), ar.getContent()) );
        else
            intent.putExtra("ARTICLE", new ArticleDoctor( ar.getArid(), ar.getErid(), ar.getTitle(), ar.getTime(), ar.getContent()) );
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
