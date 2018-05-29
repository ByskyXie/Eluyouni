package com.github.byskyxie.eluyouni;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatHolder> {

    private Context context;
    private ArrayList<ChatItem> list = new ArrayList<>();

    static class ChatHolder extends RecyclerView.ViewHolder{
        private int viewType;
        private View view;
        private ImageView icon;
        private TextView time;
        private TextView content;
        ChatHolder(View itemView, int viewType) {
            super(itemView);
            this.viewType = viewType;
            view = itemView;
            if(viewType == ChatItem.CHAT_TYPE_SELF){
                icon = itemView.findViewById(R.id.image_view_chat_icon_self);
                time = itemView.findViewById(R.id.text_view_chat_time_self);
                content = itemView.findViewById(R.id.text_view_chat_content_self);
            }else if(viewType == ChatItem.CHAT_TYPE_OTHER_SIDE){
                icon = itemView.findViewById(R.id.image_view_chat_icon_other);
                time = itemView.findViewById(R.id.text_view_chat_time_other);
                content = itemView.findViewById(R.id.text_view_chat_content_other);
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
        }else{
            Log.e("ChatAdapter","error view type");
        }
        return new ChatHolder(view,viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatHolder holder, int position) {
        int actPos = holder.getAdapterPosition();
        holder.content.setText(list.get(actPos).getContent());
        holder.icon.setImageURI(Uri.parse("http://wx2.sinaimg.cn/large/006ajDf8ly1frq8d37sfxj30sh0shgpa.jpg"));
        if(actPos>0 && (list.get(actPos).getRealTime()-list.get(actPos-1).getRealTime()) < 1000*60*2 )
            holder.time.setVisibility(View.GONE);  //小于2分钟不显示时间
        else{
            holder.time.setVisibility(View.VISIBLE);
            holder.time.setText(list.get(actPos).getDayTime());
        }
        if(list.get(actPos).getChatType() == ChatItem.CHAT_TYPE_SELF || list.get(actPos).getErtype()==1 )
            holder.icon.setImageBitmap(BitmapFactory.decodeFile(context //用户头像
                .getFilesDir().getAbsolutePath()+"/icon/picon/"+BaseActivity.userInfo.getPicon()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
