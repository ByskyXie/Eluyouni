package com.github.byskyxie.eluyouni;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ConsultDoctorAdapter extends RecyclerView.Adapter<ConsultDoctorAdapter.ConsultDoctorHolder> {

    private Context context;
    private ArrayList<Doctor> list = new ArrayList<>();

    static class ConsultDoctorHolder extends RecyclerView.ViewHolder{
        View view;
        ConsultDoctorHolder(View itemView) {
            super(itemView);
            view = itemView;
        }
    }

    public ConsultDoctorAdapter(Context context, ArrayList<Doctor> list) {
        this.context = context;
        if(list!=null)
            this.list.addAll( list);
    }

    public void addData(ArrayList<Doctor> list){
        if(list==null)
            return;
        this.list.addAll(list);
    }

    public void addData(Doctor doctor){
        if(doctor==null)
            return;
        list.add(doctor);
    }

    @Override
    public int getItemViewType(int position) {
        if(list.size()==0){
            //无选择医生
            return 1;
        }
        return 2;
    }

    @NonNull
    @Override
    public ConsultDoctorHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(viewType == 2)
            view = LayoutInflater.from(context).inflate(R.layout.item_consult_doc_list,parent,false);
        else
            view = LayoutInflater.from(context).inflate(R.layout.item_consult_none_doctor,parent,false);
        return new ConsultDoctorHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ConsultDoctorHolder holder, int position) {
        if(list.size() == 0){
            //说明当前列表为空 TODO:设置点击事件

            return;
        }
        int actPos = holder.getAdapterPosition();
        ((TextView)holder.view.findViewById(R.id.text_view_consult_doc_name)).setText( list.get(actPos).getDname() );
    }

    @Override
    public int getItemCount() {
        if(list.size() == 0)
            return 1;
        return list.size();
    }
}