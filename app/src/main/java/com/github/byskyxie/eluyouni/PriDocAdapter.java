package com.github.byskyxie.eluyouni;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class PriDocAdapter extends RecyclerView.Adapter<PriDocAdapter.PriDocHolder>
        implements View.OnClickListener{

    private Context context;
    private ArrayList<Doctor> list = new ArrayList<>();

    static class PriDocHolder extends RecyclerView.ViewHolder{
        private View view;
        private ImageView icon;
        private TextView name;
        private TextView grade;
        private TextView talk;
        PriDocHolder(View itemView) {
            super(itemView);
            view = itemView;
            icon = itemView.findViewById(R.id.image_view_pri_icon);
            name = itemView.findViewById(R.id.text_view_pri_name);
            grade = itemView.findViewById(R.id.text_view_pri_grade);
            talk = itemView.findViewById(R.id.text_view_talk);
        }
    }

    PriDocAdapter(Context context, ArrayList<Doctor> list) {
        this.context = context;
        if(list !=null)
            this.list.addAll(list);
    }

    protected void addData(ArrayList<Doctor> list){
        if(list != null)
            this.list.addAll(list);
    }

    protected ArrayList<Doctor> getData(){
        return list;
    }

    protected boolean compareDataSetSame(ArrayList<Doctor> list){
        if(list == null)
            return true;
        if(this.list.isEmpty())
            return false;
        return this.list.get(0).getDid() == list.get(0).getDid();
    }

    @NonNull
    @Override
    public PriDocHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_pri_doc_list, parent, false);
        return new PriDocHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PriDocHolder holder, int position) {
        //bind data
        int actPos = holder.getAdapterPosition();
        holder.view.setTag(actPos); //记录位置
        holder.view.setOnClickListener(this);
        holder.name.setText( list.get(actPos).getDname() );
        holder.grade.setText( list.get(actPos).getGradeName() );
        if(list.get(actPos).getDicon()==null || list.get(actPos).getDicon().isEmpty()){
            if(list.get(actPos).getDsex()==2 )
                holder.icon.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.doctor_woman));
        }
        //更新聊天记录
        if(BaseActivity.mapEridToPosition.containsKey(list.get(actPos).getDid()) ){
            ArrayList<ChatItem> cis = BaseActivity.chatRecordList.get(BaseActivity.mapEridToPosition.get(list.get(actPos).getDid())).getList();
            if(cis.size() > 0){
                holder.talk.setText( cis.get( cis.size()-1 ).getContent() );
            }else
                holder.talk.setText("暂无聊天记录");
        }else
            holder.talk.setText("暂无聊天记录");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onClick(View v) {
        int pos = (int)v.getTag();
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra("TargetType",ChatActivity.TARGET_TYPE_DOCTOR);
        intent.putExtra("Target",list.get(pos));
        intent.putExtra("ClickedPos",pos);
        ((BaseActivity)context).startActivityForResult(intent, ChatActivity.CHAT_ACTIVITY_CODE);
    }
}
