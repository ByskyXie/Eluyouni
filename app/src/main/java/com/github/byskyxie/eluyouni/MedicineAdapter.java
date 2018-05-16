package com.github.byskyxie.eluyouni;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.MedicineHolder> {
    //名医达人  科室分类
    private static final int FAME_COLUMN_NUM = 4;
    private static final int SECTION_COLUMN_NUM = 4;

    private Context context;
    private ArrayList<Doctor> docList;
    private ArrayList<Illness> illList;
    private MedicineFameAdapter fameAdapter;
    private MedicineIllnessAdapter sectionAdapter;

    static class MedicineHolder extends RecyclerView.ViewHolder{
        private View view;
        MedicineHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            //TODO: 传context进入子recycler view
        }
    }

    MedicineAdapter(Context context, ArrayList<Doctor> docList, ArrayList<Illness> illnessList) {
        this.context = context;
        if(docList != null)
            this.docList = docList;
        if(illnessList != null)
            this.illList = illnessList;
    }

    public MedicineFameAdapter getFameAdapter() {
        return fameAdapter;
    }

    public MedicineIllnessAdapter getSectionAdapter() {
        return sectionAdapter;
    }

    @Override
    public int getItemViewType(int position) {
        //return type
        switch (position){
            case 0:
                //famous doc
                return 1;
            case 1:
                //section
                return 2;
            default:
                return -1;
        }
    }

    @NonNull
    @Override
    public MedicineHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        if(viewType ==1){
            if(fameAdapter == null)
                fameAdapter = new MedicineFameAdapter(context,docList); //如果docList有数据就添加进去，没有为null
            view = LayoutInflater.from(context).inflate(R.layout.item_medicine_famous, parent,false);
            GridLayoutManager glm = new GridLayoutManager(context, FAME_COLUMN_NUM);
            ((RecyclerView)view.findViewById(R.id.recycler_medicine_famous)).setLayoutManager(glm);
            ((RecyclerView)view.findViewById(R.id.recycler_medicine_famous)).setAdapter(fameAdapter);
        }
        else if(viewType == 2){
            if(sectionAdapter == null)
               sectionAdapter = new MedicineIllnessAdapter(context, illList);
            view = LayoutInflater.from(context).inflate(R.layout.item_medicine_illness, parent,false);
            GridLayoutManager glm = new GridLayoutManager(context, SECTION_COLUMN_NUM);
            ((RecyclerView)view.findViewById(R.id.recycler_medicine_illness)).setLayoutManager(glm);
            ((RecyclerView)view.findViewById(R.id.recycler_medicine_illness)).setAdapter(sectionAdapter);
        }
        else
            Log.e("medicineAdapter","Error view type");
        return new MedicineHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicineHolder holder, int position) {
        if(holder.getAdapterPosition() == 0){
            //fame
        }else if(holder.getAdapterPosition() == 1){
            //section
        }else
            Log.e("medicineAdapter","Error position");
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
