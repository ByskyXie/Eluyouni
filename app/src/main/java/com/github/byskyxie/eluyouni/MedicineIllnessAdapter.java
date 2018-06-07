package com.github.byskyxie.eluyouni;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
        holder.image.setImageDrawable( ContextCompat.getDrawable(context, getMipmapId( illList.get(actPos).getName() ))  );
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

    private int getMipmapId(String name){
        if(name.equals("冠心病"))
            return R.mipmap.gxb;
        if(name.equals("哮喘病"))
            return R.mipmap.xcb;
        if(name.equals("慢性支气管炎"))
            return R.mipmap.mxzqgy;
        if(name.equals("慢性肝炎"))
            return R.mipmap.mxgy;
        if(name.equals("甲亢"))
            return R.mipmap.jk;
        if(name.equals("痛风性关节炎"))
            return R.mipmap.tfxgjy;
        if(name.equals("精神病"))
            return R.mipmap.jsb;
        if(name.equals("糖尿病"))
            return R.mipmap.tnb;
        if(name.equals("老年痴呆"))
            return R.mipmap.lncd;
        if(name.equals("肝硬化"))
            return R.mipmap.gyh;
        if(name.equals("肩周炎"))
            return R.mipmap.jzy;
        if(name.equals("脑卒中"))
            return R.mipmap.nzc;
        if(name.equals("脑梗塞"))
            return R.mipmap.ngs;
        if(name.equals("阻塞性气肺"))
            return R.mipmap.zsxfqz;
        if(name.equals("颈椎病"))
            return R.mipmap.jzb;
        if(name.equals("骨质疏松"))
            return R.mipmap.gzss;
        if(name.equals("高血压"))
            return R.mipmap.gxy;
        if(name.equals("高血脂"))
            return R.mipmap.gxz;
        return R.drawable.ic_illness;
    }
}
