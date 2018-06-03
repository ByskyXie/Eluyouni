package com.github.byskyxie.eluyouni;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class ArticleDoctorAdapter extends RecyclerView.Adapter<ArticleDoctorAdapter.DoctorArticleHolder>
            implements View.OnClickListener{

    private Context context;
    private IndexFragment.IndexHandler handler;
    private ArrayList<ArticleDoctor> list = new ArrayList<>();

    static class DoctorArticleHolder extends RecyclerView.ViewHolder{
        private View view;
        private TextView title;
        private TextView name;
        private ImageView icon;
        private ImageView pic;
        DoctorArticleHolder(View itemView) {
            super(itemView);
            view = itemView;
            title = itemView.findViewById(R.id.text_view_article_doctor_title);
            name = itemView.findViewById(R.id.text_view_article_doctor_name);
            icon = itemView.findViewById(R.id.image_view_article_doctor_icon);
            pic = itemView.findViewById(R.id.image_view_article_doctor_pic);
        }
    }

    ArticleDoctorAdapter(Context context, ArrayList<ArticleDoctor> list, IndexFragment.IndexHandler handler) {
        this.context = context;
        this.handler = handler;
        if(list != null)
            this.list.addAll(list);
    }

    protected void addData(ArrayList<ArticleDoctor> list){
        if(list != null)
            this.list.addAll(list);
    }

    protected ArrayList<ArticleDoctor> getData(){
        return list;
    }

    protected boolean compareDataSetSame(ArrayList<ArticleDoctor> list){
        if(list == null)
            return true;
        if(this.list.isEmpty())
            return false;
        return this.list.get(0).getTitle().equals( list.get(0).getTitle() );
    }

    @NonNull
    @Override
    public DoctorArticleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_article_doctor,parent,false);
        return new DoctorArticleHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorArticleHolder holder, int position) {
        int actPos = holder.getAdapterPosition();
        holder.view.setTag(actPos);
        holder.view.setOnClickListener( this );
        holder.title.setText( list.get(actPos).getTitle() );
        //获取医生姓名
        Cursor cursor = BaseActivity.userDatabaseRead.query("DOCTOR_BASE_INFO",new String[]{"*"}
                , "DID=? ",new String[]{""+list.get(actPos).getDid() },null,null,null,null);
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
    }

    private void downloadDicon(final String dicon, final int position){
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(! ((BaseActivity)context).downloadDicon(dicon) )
                    return; //下载失败就算了
                Message msg = new Message();
                msg.what = IndexPagerAdapter.DOCTOR_ICON_ACCEPT;
                msg.obj = position;
                handler.sendMessage(msg);
            }
        }).start();
    }

    @Override
    public void onClick(View v) {
        int pos = (int)v.getTag();
        Intent intent = new Intent(context, ShowArticleActivity.class);
        intent.putExtra("TITLE",context.getString(R.string.pager_recommend));
        intent.putExtra("ARTICLE", list.get(pos) );
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
