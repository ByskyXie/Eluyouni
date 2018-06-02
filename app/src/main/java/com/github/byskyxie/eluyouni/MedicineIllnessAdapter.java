package com.github.byskyxie.eluyouni;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class MedicineIllnessAdapter extends RecyclerView.Adapter<MedicineIllnessAdapter.MedicineIllnessHolder>
            implements View.OnClickListener{

    private Context context;
    private ArrayList<Illness> illList = new ArrayList<>();

    static class MedicineIllnessHolder extends RecyclerView.ViewHolder{
        private View view;
        private ImageView image;
        private TextView name;
        MedicineIllnessHolder(View itemView) {
            super(itemView);
            view = itemView;
            image = itemView.findViewById(R.id.image_view_medicine_illness_pic);
            name = itemView.findViewById(R.id.text_view_medicine_illness_text);
        }
    }

    MedicineIllnessAdapter(Context context, ArrayList<Illness> secList) {
        this.context = context;
        if(secList == null || secList.isEmpty())
            return;
        this.illList.addAll( secList );
    }

    protected void addData(ArrayList<Illness> list){
        if(list == null)
            return;
        this.illList.addAll(list);
    }

    protected ArrayList<Illness> getData(){
        return illList;
    }

    protected boolean compareDataSetSame(ArrayList<Illness> list){
        if(list == null)
            return true;
        if(this.illList.isEmpty())
            return false;
        return illList.get(0).getName().equals( list.get(0).getName() );
    }

    @NonNull
    @Override
    public MedicineIllnessHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_medicine_illness_item, parent, false);
        return new MedicineIllnessHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicineIllnessHolder holder, int position) {
        int actPos = holder.getAdapterPosition();
        holder.view.setTag(actPos);
        holder.view.setOnClickListener(this);
        holder.name.setText(illList.get(actPos).getName());
        //TODO:设置疾病图片
    }

    @Override
    public int getItemCount() {
        return illList.size();
    }

    @Override
    public void onClick(View v) {
        int pos = (int)v.getTag();
        Intent intent = new Intent(context, DoctorListActivity.class);
        intent.putExtra("ILLNESS",illList.get(pos));
        context.startActivity(intent);
    }
}
