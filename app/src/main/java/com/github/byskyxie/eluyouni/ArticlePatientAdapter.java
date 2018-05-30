package com.github.byskyxie.eluyouni;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
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

public class ArticlePatientAdapter extends RecyclerView.Adapter<ArticlePatientAdapter.ArticlePatientHolder> {

    private ArrayList<ArticlePatient> list = new ArrayList<>();
    private Context context;
    private IndexFragment.IndexHandler handler;

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

    ArticlePatientAdapter(Context context, ArrayList<ArticlePatient> list, IndexFragment.IndexHandler handler) {
        this.context = context;
        this.handler = handler;
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
            String icon = cursor.getString( cursor.getColumnIndex("PICON") );
            int sex = cursor.getInt( cursor.getColumnIndex("PSEX") );
            //默认头像
            if(sex == 2)
                holder.icon.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_patient_female));
            else
                holder.icon.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_patient_male));
            if(icon != null && !icon.isEmpty() && !icon.equalsIgnoreCase("null")){
                //查看picon有没有下载
                if( new File(context.getFilesDir().getAbsolutePath()+"/icon/picon/"+icon).exists() ){
                    holder.icon.setImageBitmap(BitmapFactory.decodeFile(context //用户头像
                            .getFilesDir().getAbsolutePath()+"/icon/picon/"+icon) );
                }else{//下载并notify
                    downloadPicon(icon,actPos);
                }
            }
        }
        cursor.close();
    }

    private void downloadPicon(final String picon, final int position){
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(! ((BaseActivity)context).downloadPicon(picon) ) {
                    return; //下载失败就算了
                }
                Message msg = new Message();
                msg.what = IndexPagerAdapter.PATIENT_ICON_ACCEPT;
                msg.obj = position;
                handler.sendMessage(msg);
            }
        }).start();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
