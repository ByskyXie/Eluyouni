package com.github.byskyxie.eluyouni;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
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

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;

public class FocusAdapter extends RecyclerView.Adapter<FocusAdapter.FocusHolder> implements View.OnClickListener{
    private static final int VIEW_TYPE_ARTICLE_PATIENT = 0X0101;
    private static final int VIEW_TYPE_ARTICLE_DOCTOR = 0X0102;
    private static final int VIEW_TYPE_COMMUNITY = 0X0103;

    private Context context;
    private IndexFragment.IndexHandler handler;
    private ArrayList<Object> list = new ArrayList<>();

    static class FocusHolder extends RecyclerView.ViewHolder{
        private View view;
        FocusHolder(View itemView) {
            super(itemView);
            view = itemView;
        }
    }

    FocusAdapter(Context context, ArrayList<Object> list, IndexFragment.IndexHandler handler) {
        this.context = context;
        this.handler = handler;
        if(list == null)
            return;
        addData( list );
    }

    protected void addData(ArrayList<Object> list){
        if(this.list.size()>0)
            return;//TODO:只下载一次
        if(list == null)
            return;
        for(Object obj: list)
            addData(obj);
    }

    protected void addData(Object obj){
        if(obj == null || (!(obj instanceof ArticlePatient) &&
                !(obj instanceof ArticleDoctor) && !(obj instanceof PatientCommunity) ))
            return;
        for(Object item: list){
            if(item.toString().equals(obj))
                return;
        }
        list.add(obj);
    }

    protected ArrayList getData(){
        return list;
    }

    protected boolean compareDataSetSame(ArrayList<Object> list){
        //TODO:对比函数
        return false;
    }

    @Override
    public int getItemViewType(int position) {
        Object obj = list.get(position);
        if(obj instanceof ArticlePatient)
            return VIEW_TYPE_ARTICLE_PATIENT;
        if(obj instanceof ArticleDoctor)
            return VIEW_TYPE_ARTICLE_DOCTOR;
        if(obj instanceof PatientCommunity)
            return VIEW_TYPE_COMMUNITY;
        return -1;
    }

    @NonNull
    @Override
    public FocusHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType){
            case VIEW_TYPE_ARTICLE_DOCTOR:
                view = LayoutInflater.from(context).inflate(R.layout.item_article_doctor, parent, false);
                break;
            case VIEW_TYPE_ARTICLE_PATIENT:
                view = LayoutInflater.from(context).inflate(R.layout.item_article_patient, parent, false);
                break;
            case VIEW_TYPE_COMMUNITY:
                view = LayoutInflater.from(context).inflate(R.layout.item_community, parent, false);
                break;
            default:
                view = null;
                break;
        }
        return new FocusHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FocusHolder holder, int position) {
        switch ( holder.getItemViewType() ){
            case VIEW_TYPE_ARTICLE_PATIENT:
                bindArticlePatientData(holder, position);
                break;
            case VIEW_TYPE_ARTICLE_DOCTOR:
                bindArticleDoctorData(holder, position);
                break;
            case VIEW_TYPE_COMMUNITY:
                bindCommunityData(holder, position);
                break;
        }
        ////
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

    private void bindArticleDoctorData(FocusHolder holder, final int position){
        int actPos = holder.getAdapterPosition();
        ArticleDoctor ad = (ArticleDoctor) list.get(actPos);
        holder.view.setOnClickListener(this);
        holder.view.setTag(actPos);
        ((TextView)holder.view.findViewById(R.id.text_view_article_doctor_title)).setText( ad.getTitle() );
        ((TextView)holder.view.findViewById(R.id.text_view_article_doctor_time)).setText( ad.getMDTime() );
        ((TextView)holder.view.findViewById(R.id.text_view_article_doctor_content)).setText( ad.getContent() );
        //获取医生姓名
        Cursor cursor = BaseActivity.userDatabaseRead.query("DOCTOR_BASE_INFO",new String[]{"*"}
                , "DID=? ",new String[]{""+ad.getDid() },null,null,null,null);
        if(cursor.moveToFirst()){
            //设置姓名
            ((TextView)holder.view.findViewById(R.id.text_view_article_doctor_name))
                    .setText( cursor.getString( cursor.getColumnIndex("DNAME") ) );
            String icon = cursor.getString( cursor.getColumnIndex("DICON") );
            ImageView img = holder.view.findViewById(R.id.image_view_article_doctor_icon);
            //默认头像
            if( cursor.getInt(cursor.getColumnIndex("DSEX"))==2 )
                img.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_doctor_female));//女默认
            else
                img.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_doctor_male));   //男默认
            if(icon != null && !icon.isEmpty() && !icon.equalsIgnoreCase("null")){
                //查看dicon有没有下载
                if(new File(context.getFilesDir().getAbsolutePath()+"/icon/dicon/"+icon).exists() ){
                    img.setImageBitmap(BitmapFactory.decodeFile(context //用户头像
                            .getFilesDir().getAbsolutePath()+"/icon/dicon/"+icon) );
                }else{//下载并notify
                    downloadDicon(icon,actPos);
                }
            }
        }
        cursor.close();
    }

    private void bindArticlePatientData(FocusHolder holder, final int position){
        int actPos = holder.getAdapterPosition();
        ArticlePatient ap = (ArticlePatient)list.get(actPos);
        holder.view.setOnClickListener(this);
        holder.view.setTag(actPos);
        ((TextView)holder.view.findViewById(R.id.text_view_article_patient_title)).setText( ap.getTitle() );
        ((TextView)holder.view.findViewById(R.id.text_view_article_patient_time)).setText( ap.getMDTime() );
        ((TextView)holder.view.findViewById(R.id.text_view_article_patient_content)).setText( ap.getContent() );
        //获取姓名
        Cursor cursor = BaseActivity.userDatabaseRead.query("PATIENT_BASE_INFO",new String[]{"*"}
                , "PID=? ",new String[]{""+ap.getPid() },null,null,null,null);
        if(cursor.moveToFirst()){
            //设置姓名
            ((TextView)holder.view.findViewById(R.id.text_view_article_patient_name))
                    .setText( cursor.getString( cursor.getColumnIndex("PNAME") ) );
            String icon = cursor.getString( cursor.getColumnIndex("PICON") );
            int sex = cursor.getInt( cursor.getColumnIndex("PSEX") );
            ImageView img = holder.view.findViewById(R.id.image_view_article_patient_icon);
            //默认头像
            if(sex == 2)
                img.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_patient_female));
            else
                img.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_patient_male));
            if(icon != null && !icon.isEmpty() && !icon.equalsIgnoreCase("null")){
                //查看picon有没有下载
                if( new File(context.getFilesDir().getAbsolutePath()+"/icon/picon/"+icon).exists() ){
                    img.setImageBitmap(BitmapFactory.decodeFile(context //用户头像
                            .getFilesDir().getAbsolutePath()+"/icon/picon/"+icon) );
                }else{//下载并notify
                    downloadPicon(icon,actPos);
                }
            }
        }
        cursor.close();
    }

    private void bindCommunityData(FocusHolder holder, final int position){
        int actPos = holder.getAdapterPosition();
        TextView tag = holder.view.findViewById(R.id.text_view_item_community_tag);
        TextView erName = holder.view.findViewById(R.id.text_view_item_community_ername);
        ImageView imgIcon = holder.view.findViewById(R.id.image_view_item_community_icon);
        PatientCommunity com = (PatientCommunity)list.get(actPos);

        if(com.getErType() == 1){
            tag.setText("患者");
            //获取患者姓名
            Cursor cursor = BaseActivity.userDatabaseRead.query("PATIENT_BASE_INFO",new String[]{"*"}
                    , "PID=? ",new String[]{""+com.getErId() },null,null,null,null);
            if(cursor.moveToFirst()){
                //设置姓名
                erName.setText( cursor.getString( cursor.getColumnIndex("PNAME") ) );
                String icon = cursor.getString( cursor.getColumnIndex("PICON") );
                int sex = cursor.getInt( cursor.getColumnIndex("PSEX") );
                //默认头像
                if(sex == 2)
                    imgIcon.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_patient_female));
                else
                    imgIcon.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_patient_male));
                if(icon != null && !icon.isEmpty() && !icon.equalsIgnoreCase("null")){
                    //查看picon有没有下载
                    if(new File(context.getFilesDir().getAbsolutePath()+"/icon/picon/"+icon).exists() ){
                        imgIcon.setImageBitmap(BitmapFactory.decodeFile(context //用户头像
                                .getFilesDir().getAbsolutePath()+"/icon/picon/"+icon) );
                    }else{//下载并notify
                        downloadPicon(icon,actPos);
                    }
                }
            }
            cursor.close();
        }else if(com.getErType() == 2){
            tag.setText("医生");
            //获得医生姓名
            Cursor cursor = BaseActivity.userDatabaseRead.query("DOCTOR_BASE_INFO",new String[]{"*"}
                    , "DID=? ",new String[]{""+com.getErId() },null,null,null,null);
            if(cursor.moveToFirst()){
                //设置姓名
                erName.setText( cursor.getString( cursor.getColumnIndex("DNAME") ) );
                String icon = cursor.getString( cursor.getColumnIndex("DICON") );
                //默认头像
                if( cursor.getInt(cursor.getColumnIndex("DSEX"))==2 )
                    imgIcon.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_doctor_female));//女默认
                else
                    imgIcon.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_doctor_male));   //男默认
                if(icon != null && !icon.isEmpty() && !icon.equalsIgnoreCase("null")){
                    //查看dicon有没有下载
                    if(new File(context.getFilesDir().getAbsolutePath()+"/icon/dicon/"+icon).exists() ){
                        imgIcon.setImageBitmap(BitmapFactory.decodeFile(context //用户头像
                                .getFilesDir().getAbsolutePath()+"/icon/dicon/"+icon) );
                    }else{//下载并notify
                        downloadDicon(icon,actPos);
                    }
                }
            }
            cursor.close();
        }else{
            Log.e("CommunityAdapter","Get error er_type!!");
        }
        ((TextView)holder.view.findViewById(R.id.text_view_item_community_time)).setText( com.getTime());
        ((TextView)holder.view.findViewById(R.id.text_view_item_community_content)).setText( com.getCcontent());
        ((TextView)holder.view.findViewById(R.id.text_view_item_community_assent)).setText( com.getAssentNum()+" ");
        holder.view.findViewById(R.id.text_view_item_community_assent).setOnClickListener(this);
        holder.view.findViewById(R.id.text_view_item_community_assent).setTag( actPos );
        setIsAssent(((TextView)holder.view.findViewById(R.id.text_view_item_community_assent))
                ,((PatientCommunity)list.get(actPos)).isAssented());
    }

    protected void setIsAssent(TextView view, boolean isAssent){
        Drawable d = null;
        if( isAssent ){
            d = ContextCompat.getDrawable(context, R.drawable.ic_zan_checked);
        }else{
            d = ContextCompat.getDrawable(context, R.drawable.ic_zan);
        }
        if(d != null)
            d.setBounds(0,0,d.getMinimumWidth(),d.getMinimumHeight());
        else
            Log.e("Assent set","get icon failed");
        view.setCompoundDrawables(null, null, d,null);
    }

    @Override
    public void onClick(View v) {
        int pos = (int)v.getTag();
        switch (v.getId()){
            case R.id.text_view_item_community_assent:
                //点赞
                PatientCommunity pm = (PatientCommunity) list.get(pos);
                int i = pm.isAssented()? pm.getAssentNum()-1: pm.getAssentNum()+1 ;
                pm.setAssentNum( i );
                setIsAssent((TextView) v, pm.isAssented());
                pm.setAssented( !pm.isAssented() );
                break;
            default:
                //点击文章
                Object obj = list.get(pos);
                Intent intent = new Intent(context, ShowArticleActivity.class);
                intent.putExtra("TITLE",context.getString(R.string.pager_focus));
                intent.putExtra("ARTICLE", (obj instanceof ArticlePatient)? (ArticlePatient)obj: (ArticleDoctor)obj );
                context.startActivity(intent);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
