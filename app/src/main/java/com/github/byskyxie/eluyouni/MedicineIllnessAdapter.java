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

public class MedicineIllnessAdapter extends RecyclerView.Adapter<MedicineIllnessAdapter.MedicineSectionHolder> {

    private Context context;
    private ArrayList<Illness> illList;

    static class MedicineSectionHolder extends RecyclerView.ViewHolder{
        private ImageView imageSection;
        private TextView nameSection;
        MedicineSectionHolder(View itemView) {
            super(itemView);
            imageSection = itemView.findViewById(R.id.image_view_medicine_illness_pic);
            nameSection = itemView.findViewById(R.id.text_view_medicine_illness_text);
        }
    }

    MedicineIllnessAdapter(Context context, ArrayList<Illness> secList) {
        this.context = context;
        this.illList = secList;
    }

    protected void addData(ArrayList<Illness> list){
        if(list != null)
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
        //TODO:return section CMP
        return true;
    }

    @NonNull
    @Override
    public MedicineSectionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_medicine_illness_item, parent, false);
        return new MedicineSectionHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicineSectionHolder holder, int position) {
        //TODO:add actual section info and OnClick
    }

    @Override
    public int getItemCount() {
        return illList.size();
    }

}
