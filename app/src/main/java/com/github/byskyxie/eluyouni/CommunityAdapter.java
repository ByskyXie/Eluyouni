package com.github.byskyxie.eluyouni;

import android.content.Context;
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


public class CommunityAdapter extends RecyclerView.Adapter<CommunityAdapter.CommunityHolder> {

    private Context context;
    private IndexFragment.IndexHandler handler;
    private ArrayList<PatientCommunity> list = new ArrayList<>();

    static class CommunityHolder extends RecyclerView.ViewHolder{
        private ImageView icon;
        private TextView assent;
        private TextView erName;
        private TextView tag;
        private TextView time;
        private TextView content;

        CommunityHolder(View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.image_view_item_community_icon);
            erName = itemView.findViewById(R.id.text_view_item_community_ername);
            tag = itemView.findViewById(R.id.text_view_item_community_tag);
            time = itemView.findViewById(R.id.text_view_item_community_time);
            content = itemView.findViewById(R.id.text_view_item_community_content);
            assent = itemView.findViewById(R.id.text_view_item_community_assent);
        }
    }

    CommunityAdapter(Context context, ArrayList<PatientCommunity> list, IndexFragment.IndexHandler handler) {
        this.context = context;
        this.handler = handler;
        if(list != null)
            this.list.addAll(list);
    }

    protected void addData(ArrayList<PatientCommunity> list){
        if(list != null)
            this.list.addAll(list);
    }

    protected ArrayList<PatientCommunity> getData(){
        return list;
    }

    protected boolean compareDataSetSame(ArrayList<PatientCommunity> list){
        if(list == null)
            return true;
        if(this.list.isEmpty())
            return false;
        return this.list.get(0).getCcontent().equals( list.get(0).getCcontent() );
    }

    @NonNull
    @Override
    public CommunityHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_community, parent , false);
        return new CommunityHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommunityHolder holder, final int position) {
        int actPos = holder.getAdapterPosition();
        holder.content.setText(list.get(actPos).getCcontent());
        if(list.get(actPos).getErType() == 1){
            holder.tag.setText("患者");
            //获取患者姓名
            Cursor cursor = BaseActivity.userDatabaseRead.query("PATIENT_BASE_INFO",new String[]{"*"}
                    , "PID=? ",new String[]{""+list.get(actPos).getErId() },null,null,null,null);
            if(cursor.moveToFirst()){
                //设置姓名
                holder.erName.setText( cursor.getString( cursor.getColumnIndex("PNAME") ) );
                String icon = cursor.getString( cursor.getColumnIndex("PICON") );
                //默认头像
                holder.icon.setImageDrawable(ContextCompat.getDrawable(context,R.mipmap.patient));
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
        }else if(list.get(actPos).getErType() == 2){
            holder.tag.setText("医生");
            //获得医生姓名
            Cursor cursor = BaseActivity.userDatabaseRead.query("DOCTOR_BASE_INFO",new String[]{"*"}
                    , "DID=? ",new String[]{""+list.get(actPos).getErId() },null,null,null,null);
            if(cursor.moveToFirst()){
                //设置姓名
                holder.erName.setText( cursor.getString( cursor.getColumnIndex("DNAME") ) );
                String icon = cursor.getString( cursor.getColumnIndex("DICON") );
                //默认头像
                if( cursor.getInt(cursor.getColumnIndex("DSEX"))==2 )
                    holder.icon.setImageDrawable(ContextCompat.getDrawable(context,R.mipmap.doctor_woman));//女默认
                else
                    holder.icon.setImageDrawable(ContextCompat.getDrawable(context,R.mipmap.doctor_man));   //男默认
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
            Log.e("CommunityAdapter","Get error er_type!!");
        }
        holder.time.setText( list.get(actPos).getTime());
        holder.content.setText( list.get(actPos).getCcontent());
        holder.assent.setText( list.get(actPos).getAssentNum()+" ");
    }

    private void downloadDicon(final String dicon, final int position){
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(! ((BaseActivity)context).downloadDicon(dicon) )
                    return; //下载失败就算了
                Message msg = new Message();
                msg.what = IndexPagerAdapter.COMMUNITY_ICON_ACCEPT;
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
                msg.what = IndexPagerAdapter.COMMUNITY_ICON_ACCEPT;
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
