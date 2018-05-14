package com.github.byskyxie.eluyouni;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ArticleDoctorAdapter extends RecyclerView.Adapter<ArticleDoctorAdapter.DoctorArticleHolder> {

    private Context context;
    private ArrayList<ArticleDoctor> list = new ArrayList<>();

    static class DoctorArticleHolder extends RecyclerView.ViewHolder{
        private TextView title;
        private TextView name;
        private ImageView icon;
        private ImageView pic;
        DoctorArticleHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.text_view_article_doctor_title);
            name = itemView.findViewById(R.id.text_view_article_doctor_name);
            icon = itemView.findViewById(R.id.image_view_article_doctor_icon);
            pic = itemView.findViewById(R.id.image_view_article_doctor_pic);
        }
    }

    ArticleDoctorAdapter(Context context, ArrayList<ArticleDoctor> list) {
        this.context = context;
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
        holder.title.setText( list.get(actPos).getTitle() );
        //获取医生姓名
        Cursor cursor = BaseActivity.userDatabaseRead.query("DOCTOR_BASE_INFO",new String[]{"*"}
                , "DID=? ",new String[]{""+list.get(actPos).getDid() },null,null,null,null);
        if(cursor.moveToFirst()){
            //设置姓名
            holder.name.setText( cursor.getString( cursor.getColumnIndex("DNAME") ) );
            String icon = cursor.getString( cursor.getColumnIndex("DICON") );
            if(icon == null || icon.equalsIgnoreCase("null")){
                //默认头像
                holder.icon.setImageDrawable(ContextCompat.getDrawable(context,R.mipmap.doctor));
            }
        }
        cursor.close();
        //icon pic

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
