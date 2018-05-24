package com.github.byskyxie.eluyouni;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class ConsultDataAdapter extends RecyclerView.Adapter<ConsultDataAdapter.ConsultDataHolder> {
    private Context context;
    private ArrayList<String> list = new ArrayList<>();

    static class ConsultDataHolder extends RecyclerView.ViewHolder{
        View view;
        ConsultDataHolder(View itemView) {
            super(itemView);
            view = itemView;
        }
    }

    public ConsultDataAdapter(Context context, ArrayList<String> list) {
        this.context = context;
        if(list!=null)
            this.list.addAll( list);
    }

    public void addData(ArrayList<String> list){
        if(list==null)
            return;
        this.list.addAll(list);
    }

    public void addData(String s){
        if(s==null)
            return;
        list.add(s);
    }

    @Override
    public int getItemViewType(int position) {
        if(list.size() == position){//最后选择资料
            return 1;
        }
        return 2;
    }

    @NonNull
    @Override
    public ConsultDataAdapter.ConsultDataHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(viewType == 2)
            view = LayoutInflater.from(context).inflate(R.layout.item_consult_data_list,parent,false);
        else
            view = LayoutInflater.from(context).inflate(R.layout.item_consult_none_data,parent,false);
        return new ConsultDataAdapter.ConsultDataHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ConsultDataAdapter.ConsultDataHolder holder, int position) {
        if(list.size() == 0){
            //说明当前列表为空 TODO:设置添加图片事件
            return;
        }
        int actPos = holder.getAdapterPosition();
        //TODO:载入图片

    }

    @Override
    public int getItemCount() {
        if(list.size() == 0)
            return 1;
        return list.size()+1;
    }
}
