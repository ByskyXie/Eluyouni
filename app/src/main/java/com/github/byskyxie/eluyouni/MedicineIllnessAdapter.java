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

public class MedicineIllnessAdapter extends RecyclerView.Adapter<MedicineIllnessAdapter.MedicineIllnessHolder> {

    private Context context;
    private ArrayList<Illness> illList = new ArrayList<>();

    static class MedicineIllnessHolder extends RecyclerView.ViewHolder{
        private ImageView image;
        private TextView name;
        MedicineIllnessHolder(View itemView) {
            super(itemView);
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
        holder.name.setText(illList.get(actPos).getName());

    }

    @Override
    public int getItemCount() {
        return illList.size();
    }

}
