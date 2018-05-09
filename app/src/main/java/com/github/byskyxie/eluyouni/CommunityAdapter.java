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
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class CommunityAdapter extends RecyclerView.Adapter<CommunityAdapter.CommunityHolder> {

    private Context context;
    private ArrayList<PatientCommunity> list;

    static class CommunityHolder extends RecyclerView.ViewHolder{
        private ImageView icon;
        private TextView assent;
        private TextView erName;
        private TextView tag;
        private TextView time;
        private TextView content;

        CommunityHolder(View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.image_view_icon);
            erName = itemView.findViewById(R.id.text_view_item_community_ername);
            tag = itemView.findViewById(R.id.text_view_item_community_tag);
            time = itemView.findViewById(R.id.text_view_item_community_time);
            content = itemView.findViewById(R.id.text_view_item_community_content);
            assent = itemView.findViewById(R.id.text_view_item_community_assent);
        }
    }

    CommunityAdapter(Context context, ArrayList<PatientCommunity> list) {
        this.context = context;
        this.list = list;
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
            }
            cursor.close();
        }else{
            Log.e("CommunityAdapter","Get error er_type!!");
        }
        holder.time.setText( list.get(actPos).getTime());
        holder.content.setText( list.get(actPos).getCcontent());
        holder.assent.setText( list.get(actPos).getAssentNum()+" ");
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

}
