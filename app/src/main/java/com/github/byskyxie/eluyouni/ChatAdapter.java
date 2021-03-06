package com.github.byskyxie.eluyouni;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
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

import static com.github.byskyxie.eluyouni.BaseActivity.userInfo;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatHolder> {

    private Context context;
    private ArrayList<ChatItem> list = new ArrayList<>();

    static class ChatHolder extends RecyclerView.ViewHolder{
        private int viewType;
        private ImageView icon;
        private TextView time;
        private TextView content;
        ChatHolder(View itemView, int viewType) {
            super(itemView);
            this.viewType = viewType;
            if(viewType == ChatItem.CHAT_TYPE_SELF){
                icon = itemView.findViewById(R.id.image_view_chat_icon_self);
                time = itemView.findViewById(R.id.text_view_chat_time_self);
                content = itemView.findViewById(R.id.text_view_chat_content_self);
            }else if(viewType == ChatItem.CHAT_TYPE_OTHER_SIDE){
                icon = itemView.findViewById(R.id.image_view_chat_icon_other);
                time = itemView.findViewById(R.id.text_view_chat_time_other);
                content = itemView.findViewById(R.id.text_view_chat_content_other);
            }else if(viewType == ChatItem.CHAT_TYPE_SYS){
                icon = itemView.findViewById(R.id.image_view_chat_icon_sys);
                time = itemView.findViewById(R.id.text_view_chat_time_sys);
                content = itemView.findViewById(R.id.text_view_chat_content_sys);
            }
        }
    }

    ChatAdapter(Context context, ArrayList<ChatItem> list) {
        this.context = context;
        if(list != null)
            this.list.addAll(list);
    }

    public void addData(ArrayList<ChatItem> list){
        if(list != null)
            this.list.addAll(list);
    }

    public void addData(ChatItem item){
        if(item == null)
            return;
        list.add(item);
        notifyDataSetChanged();
    }

    public ArrayList<ChatItem> getList() {
        return list;
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).getChatType();
    }

    @NonNull
    @Override
    public ChatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        if(viewType == ChatItem.CHAT_TYPE_SELF){
            view = LayoutInflater.from(context).inflate(R.layout.item_chat_self, parent,false);
        }else if(viewType == ChatItem.CHAT_TYPE_OTHER_SIDE){
            view = LayoutInflater.from(context).inflate(R.layout.item_chat_other_side, parent,false);
        }else if(viewType == ChatItem.CHAT_TYPE_SYS){
            view = LayoutInflater.from(context).inflate(R.layout.item_chat_system, parent,false);
        }else{
            Log.e("ChatAdapter","error view type");
        }
        return new ChatHolder(view,viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatHolder holder, int position) {
        int actPos = holder.getAdapterPosition();
        if(holder.viewType == ChatItem.CHAT_TYPE_SYS){
            //系统消息
            holder.content.setText( list.get(actPos).getContent() );
            return;
        }
        holder.content.setText(list.get(actPos).getContent());
        //要保证前一条信息
        if(actPos>0 && (list.get(actPos).getRealTime()-list.get(actPos-1).getRealTime()) < 1000*60*2 )
            holder.time.setVisibility(View.GONE);  //小于2分钟不显示时间
        else{
            holder.time.setVisibility(View.VISIBLE);
            holder.time.setText(list.get(actPos).getDayTime());
        }
        holder.icon.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_patient_male));
        if(list.get(actPos).getChatType() == ChatItem.CHAT_TYPE_SELF || list.get(actPos).getErtype()==1 ){
            if( !new File(this. context.getFilesDir().getAbsolutePath()+"/icon/picon/"+userInfo.getPicon()).exists() )
                holder.icon.setImageDrawable(ContextCompat.getDrawable(context,R.mipmap.patient)  );
            else
                holder.icon.setImageBitmap(BitmapFactory.decodeFile(context //用户头像
                    .getFilesDir().getAbsolutePath()+"/icon/picon/"+ userInfo.getPicon()));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
