package com.github.byskyxie.eluyouni;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.MedicineHolder> {
    //名医达人  科室分类
    private Context context;
    private ArrayList<Doctor> docList = new ArrayList<>();
    private ArrayList<Section> sectionList = new ArrayList<>();


    static class MedicineHolder extends RecyclerView.ViewHolder{
        private View view;
        MedicineHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            //TODO: 传context进入子recycler view
        }
    }

    public MedicineAdapter(Context context, ArrayList<Doctor> docList, ArrayList<Section> sectionList) {
        this.context = context;
        if(docList != null)
            this.docList.addAll(docList);
        if(sectionList != null)
            this.sectionList.addAll(sectionList);
    }

    protected void addFamousDoc(ArrayList<Doctor> list){
        if(list != null)
            this.docList.addAll(list);
    }

    protected ArrayList<Doctor> getDocList(){
        return docList;
    }

    protected boolean compareDocSetSame(ArrayList<Doctor> list){
        if(list == null)
            return true;
        if(this.docList.isEmpty())
            return false;
        return this.docList.get(0).getDid() == list.get(0).getDid();
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
        if(viewType ==1)
            view = LayoutInflater.from(context).inflate(R.layout.item_medicine_famous_doc, parent,false);
        else if(viewType == 2)
            view = LayoutInflater.from(context).inflate(R.layout.item_medicine_section, parent,false);
        else
            Log.e("medicineAdapter","Error view type");
        return new MedicineHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicineHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
