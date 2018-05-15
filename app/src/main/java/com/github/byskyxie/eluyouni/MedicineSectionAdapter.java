package com.github.byskyxie.eluyouni;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MedicineSectionAdapter extends RecyclerView.Adapter<MedicineSectionAdapter.MedicineSectionHolder> {

    private Context context;
    private ArrayList<Section> secList;

    static class MedicineSectionHolder extends RecyclerView.ViewHolder{
        private ImageView imageSection;
        private TextView nameSection;
        MedicineSectionHolder(View itemView) {
            super(itemView);
            imageSection = itemView.findViewById(R.id.image_view_medicine_section_pic);
            nameSection = itemView.findViewById(R.id.text_view_medicine_section_text);
        }
    }

    MedicineSectionAdapter(Context context, ArrayList<Section> secList) {
        this.context = context;
        this.secList = secList;
    }

    protected void addData(ArrayList<Section> list){
        if(list != null)
            this.secList.addAll(list);
    }

    protected ArrayList<Section> getData(){
        return secList;
    }

    protected boolean compareDataSetSame(ArrayList<Section> list){
        if(list == null)
            return true;
        if(this.secList.isEmpty())
            return false;
        //TODO:return section CMP
        return true;
    }

    @NonNull
    @Override
    public MedicineSectionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_medicine_section_item, parent, false);
        return new MedicineSectionHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicineSectionHolder holder, int position) {
        //TODO:add actual section info and OnClick
    }

    @Override
    public int getItemCount() {
        return secList.size();
    }

}
