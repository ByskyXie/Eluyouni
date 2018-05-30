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

public class MedicineFameAdapter extends RecyclerView.Adapter<MedicineFameAdapter.MedicineFameHolder>
            implements View.OnClickListener{

    private Context context;
    private ArrayList<Doctor> docList = new ArrayList<>();

    static class MedicineFameHolder extends RecyclerView.ViewHolder{
        private View view;
        private ImageView icon;
        private TextView name;
        MedicineFameHolder(View itemView) {
            super(itemView);
            view = itemView;
            icon = itemView.findViewById(R.id.image_view_medicine_famous_icon);
            name = itemView.findViewById(R.id.text_view_medicine_famous_name);
        }
    }

    MedicineFameAdapter(Context context, ArrayList<Doctor> docList) {
        this.context = context;
        if(docList == null || docList.isEmpty())
            return;
        this.docList.addAll(docList);
    }

    protected void addData(ArrayList<Doctor> list){
        if(list == null)
            return;
        this.docList.addAll(list);
    }

    protected ArrayList<Doctor> getData(){
        return docList;
    }

    protected boolean compareDataSetSame(ArrayList<Doctor> list){
        if(list == null)
            return true;
        if(this.docList.isEmpty())
            return false;
        return docList.get(0).getDid() == list.get(0).getDid();
    }

    @NonNull
    @Override
    public MedicineFameHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_medicine_famous_item, parent, false);
        return new MedicineFameHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicineFameHolder holder, int position) {
        int actPos = holder.getAdapterPosition();
        holder.name.setText( docList.get(actPos).getDname() );
        holder.view.setOnClickListener(this);
        //设置头像

    }

    @Override
    public int getItemCount() {
        return docList.size();
    }

    @Override
    public void onClick(View v) {
        //TODO:跳转医生详情页
    }
}
