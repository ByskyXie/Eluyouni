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

public class ConsultDoctorAdapter extends RecyclerView.Adapter<ConsultDoctorAdapter.ConsultDoctorHolder>
        implements View.OnClickListener{

    private int docNumLimit = 5;
    private Context context;
    private ArrayList<Doctor> list = new ArrayList<>();

    static class ConsultDoctorHolder extends RecyclerView.ViewHolder{
        View view;
        ConsultDoctorHolder(View itemView) {
            super(itemView);
            view = itemView;
        }
    }

    ConsultDoctorAdapter(Context context, ArrayList<Doctor> list) {
        this.context = context;
        if(list==null)
            return;
        this.list.addAll( list);
        while(list.size()>docNumLimit)
            list.remove(list.size()-1 );
    }

    public void addData(ArrayList<Doctor> list){
        if(list==null)
            return;
        for(Doctor d: list){
            if(list.size()>=docNumLimit)
                break;
            addData(d);
        }
    }

    public void addData(Doctor doctor){
        if(doctor==null || list.size()>docNumLimit)
            return;
        int i;
        for(i=0; i<list.size(); i++)
            if(list.get(i).getDid()==doctor.getDid())
                break;
        if( i>=list.size() )
            list.add(doctor);
    }

    @Override
    public int getItemViewType(int position) {
        if(list.size() == position && list.size()!=docNumLimit){
            //添加医生
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
        int actPos = holder.getAdapterPosition();
        if(list.size() == position && list.size()!=docNumLimit){
            //说明当前列表为空
            ViewGroup.LayoutParams param = holder.view.getLayoutParams();
            if(list.size()==0){
                //如果列表为空，改变宽度
                param.width = ViewGroup.LayoutParams.MATCH_PARENT;
            }else{
                param.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            }
            holder.view.findViewById(R.id.text_view_consult_add_doc).setOnClickListener(this);
            return;
        }

        ((TextView)holder.view.findViewById(R.id.text_view_consult_doc_name)).setText( list.get(actPos).getDname() );
    }

    @Override
    public int getItemCount() {
        if(list.size() == 0)
            return 1;
        return list.size();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.text_view_consult_add_doc:
                ((MainActivity)context).setRadioButtonChecked(2);
            break;
        }
    }
}
