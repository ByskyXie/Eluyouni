package com.github.byskyxie.eluyouni;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ArticlePatientAdapter extends RecyclerView.Adapter<ArticlePatientAdapter.ArticlePatientHolder> {

    private ArrayList<ArticlePatient> list = new ArrayList<>();
    private Context context;

    static class ArticlePatientHolder extends RecyclerView.ViewHolder{
        private TextView title;
        private TextView name;
        private ImageView icon;
        private ImageView pic;
        ArticlePatientHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.text_view_article_patient_title);
            name = itemView.findViewById(R.id.text_view_article_patient_name);
            icon = itemView.findViewById(R.id.image_view_article_patient_icon);
            pic = itemView.findViewById(R.id.image_view_article_patient_pic);
        }
    }

    ArticlePatientAdapter(Context context, ArrayList<ArticlePatient> list) {
        this.context = context;
        if(list != null)
            this.list.addAll(list);
    }

    protected void addData(ArrayList<ArticlePatient> list){
        if(list != null)
            this.list.addAll(list);
    }

    protected ArrayList<ArticlePatient> getData(){
        return list;
    }

    protected boolean compareDataSetSame(ArrayList<ArticlePatient> list){
        if(list == null)
            return true;
        if(this.list.isEmpty())
            return false;
        return this.list.get(0).getTitle().equals( list.get(0).getTitle() );
    }

    @NonNull
    @Override
    public ArticlePatientHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_article_patient,parent,false);
        return new ArticlePatientHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticlePatientHolder holder, int position) {
        int actPos = holder.getAdapterPosition();
        holder.title.setText( list.get(actPos).getTitle() );
        //获取姓名
        Cursor cursor = BaseActivity.userDatabaseRead.query("PATIENT_BASE_INFO",new String[]{"*"}
                , "PID=? ",new String[]{""+list.get(actPos).getPid() },null,null,null,null);
        if(cursor.moveToFirst()){
            //设置姓名
            holder.name.setText( cursor.getString( cursor.getColumnIndex("PNAME") ) );
        }
        cursor.close();

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
