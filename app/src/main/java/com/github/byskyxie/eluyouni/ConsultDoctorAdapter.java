package com.github.byskyxie.eluyouni;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class ConsultDoctorAdapter extends RecyclerView.Adapter<ConsultDoctorAdapter.ConsultDoctorHolder>
        implements View.OnClickListener{

    private int docNumLimit;
    private BaseActivity activity;
    private ArrayList<Doctor> list;

    static class ConsultDoctorHolder extends RecyclerView.ViewHolder{
        View view;
        ConsultDoctorHolder(View itemView) {
            super(itemView);
            view = itemView;
        }
    }

    ConsultDoctorAdapter(BaseActivity activity) {
        this.activity = activity;
        list = activity.getConsultList();
        docNumLimit = BaseActivity.DOC_NUM_LIMIT;
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
            view = LayoutInflater.from(activity).inflate(R.layout.item_consult_doc_list,parent,false);
        else
            view = LayoutInflater.from(activity).inflate(R.layout.item_consult_none_doctor,parent,false);
        return new ConsultDoctorHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ConsultDoctorHolder holder, int position) {
        final int actPos = holder.getAdapterPosition();
        if(list.size() == position && list.size()!=docNumLimit){
            //添加选项
            ViewGroup.LayoutParams param = holder.view.getLayoutParams();
            if(list.size()==0){
                //如果列表为空，改变宽度高度
                param.width = ViewGroup.LayoutParams.MATCH_PARENT;
                param.height = 360;
            }else{
                param.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                param.height = ViewGroup.LayoutParams.MATCH_PARENT;
            }
            holder.view.findViewById(R.id.text_view_consult_add_doc).setOnClickListener(this);
            return;
        }//医生选项
        ((TextView)holder.view.findViewById(R.id.text_view_consult_doc_name)).setText( list.get(actPos).getDname() );
        ((TextView)holder.view.findViewById(R.id.text_view_consult_doc_grade)).setText( list.get(actPos).getGradeName() );
        holder.view.findViewById(R.id.image_button_consult_doc_delete).setOnClickListener(this);//删除
        holder.view.findViewById(R.id.image_button_consult_doc_delete).setTag(actPos);  //位置
        //头像
        if(list.get(actPos).getDicon()!=null && !list.get(actPos).getDicon().isEmpty()){
            String iconPath =  activity.getFilesDir().getAbsolutePath()+"/icon/dicon/"+list.get(actPos).getDicon();
            File file = new File(iconPath);
            if(file.exists()){
                ((ImageView)holder.view.findViewById(R.id.image_view_consult_doc_icon))
                        .setImageBitmap( BitmapFactory.decodeFile(iconPath) );
            }else{
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        activity.downloadDicon(list.get(actPos));
                    }
                }).start();
            }
        }
    }

    @Override
    public int getItemCount() {
        if(list.size()<docNumLimit)
            return list.size()+1;
        return docNumLimit;
    }

    public int getDoctorCount(){
        return list.size();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.text_view_consult_add_doc:
                ((MainActivity)activity).setRadioButtonChecked(2);
                break;
            case R.id.image_button_consult_doc_delete:
                int pos = (int)v.getTag();
                list.remove(pos);
                notifyDataSetChanged();;
                break;
        }
    }
}
