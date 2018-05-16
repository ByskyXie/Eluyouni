package com.github.byskyxie.eluyouni;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ArticleRecommendAdapter extends RecyclerView.Adapter<ArticleRecommendAdapter.ArticleRecommendHolder> {
    private ArrayList<ArticleRecommend> list = new ArrayList<>();
    private Context context;

    static class ArticleRecommendHolder extends RecyclerView.ViewHolder{
        private TextView title;
        private TextView name;
        private ImageView icon;
        private ImageView pic;
        ArticleRecommendHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.text_view_article_recom_title);
            name = itemView.findViewById(R.id.text_view_article_recom_name);
            icon = itemView.findViewById(R.id.image_view_article_recom_icon);
            pic = itemView.findViewById(R.id.image_view_article_recom_pic);
        }
    }

    ArticleRecommendAdapter(Context context, ArrayList<ArticleRecommend> list) {
        this.context = context;
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
        holder.title.setText( list.get(actPos).getTitle() );
        if( list.get(actPos).getErtype() == 1 ){
            //获取患者姓名
            Cursor cursor = BaseActivity.userDatabaseRead.query("PATIENT_BASE_INFO",new String[]{"*"}
                    , "PID=? ",new String[]{""+list.get(actPos).getErid() },null,null,null,null);
            if(cursor.moveToFirst()){
                //设置姓名
                holder.name.setText( cursor.getString( cursor.getColumnIndex("PNAME") ) );
                String icon = cursor.getString( cursor.getColumnIndex("PICON") );
                if(icon == null || icon.equalsIgnoreCase("null")){
                    //默认头像
                    holder.icon.setImageDrawable(ContextCompat.getDrawable(context,R.mipmap.patient));
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
                if(icon == null || icon.equalsIgnoreCase("null")){
                    //默认头像
                    if( cursor.getInt(cursor.getColumnIndex("DSEX"))==2 )
                        holder.icon.setImageDrawable(ContextCompat.getDrawable(context,R.mipmap.doctor_woman));
                    else
                        holder.icon.setImageDrawable(ContextCompat.getDrawable(context,R.mipmap.doctor_man));
                }
            }
            cursor.close();
        }else{
            Log.e("ArticleRecomAdapter","Error er type!");
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
