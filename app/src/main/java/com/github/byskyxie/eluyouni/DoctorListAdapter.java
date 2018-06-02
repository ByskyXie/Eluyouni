package com.github.byskyxie.eluyouni;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;

public class DoctorListAdapter extends RecyclerView.Adapter<DoctorListAdapter.DoctorListHolder>
        implements View.OnClickListener{

    private boolean isShowFame = false;
    private Context context;
    private ArrayList<Doctor> list = new ArrayList<>();

    static class DoctorListHolder extends RecyclerView.ViewHolder {
        private View view;
        private ImageView icon;
        private TextView name;
        private TextView hot;
        private TextView marking;
        private TextView hospital;
        private TextView grade;
        private TextView ill;
        DoctorListHolder(View itemView) {
            super(itemView);
            view = itemView;
            icon = itemView.findViewById(R.id.image_view_doc_list_icon);
            name = itemView.findViewById(R.id.text_view_doc_list_name);
            hot = itemView.findViewById(R.id.text_view_doc_list_hot);
            marking = itemView.findViewById(R.id.text_view_doc_list_marking);
            hospital = itemView.findViewById(R.id.text_view_doc_list_hospital);
            grade = itemView.findViewById(R.id.text_view_doc_list_grade);
            ill = itemView.findViewById(R.id.text_view_doc_list_ill);
        }
    }

    public DoctorListAdapter(Context context, ArrayList<Doctor> list) {
        this.context = context;
        if(list != null)
            this.list.addAll( list );
    }

    public void addData(ArrayList<Doctor> list){
        if(list == null || list.isEmpty())
            return;
        this.list.addAll(list);
    }

    public void addData(Doctor doctor){
        if(doctor == null)
            return;
        list.add(doctor);
    }

    public boolean isShowFame() {
        return isShowFame;
    }

    public void setShowFame(boolean showFame) {
        isShowFame = showFame;
    }

    @NonNull
    @Override
    public DoctorListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_doctor_list, parent, false);
        return new DoctorListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorListHolder holder, int position) {
        final int actPos = holder.getAdapterPosition();
        holder.view.setTag(actPos);
        holder.view.setOnClickListener(this);
        holder.name.setText(list.get(actPos).getDname());
        holder.hospital.setText( list.get(actPos).getDhospital());
        holder.grade.setText( list.get(actPos).getGradeName() );
        holder.hot.setText( "推荐热度（综合）："+list.get(actPos).getDhot_level() );
        holder.marking.setText( "评分："+list.get(actPos).getDmarking() );
        //名医要显示擅长领域
        if(isShowFame)
            holder.ill.setText("主治："+list.get(actPos).getDillness());
        //设置默认头像
        if(list.get(actPos).getDsex()==2 )
            holder.icon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_doctor_female));
        else
            holder.icon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_doctor_male));
        //头像
        if(list.get(actPos).getDicon()!=null && !list.get(actPos).getDicon().isEmpty()){
            String iconPath =  context.getFilesDir().getAbsolutePath()+"/icon/dicon/"+list.get(actPos).getDicon();
            File file = new File(iconPath);
            if(file.exists()){
                holder.icon.setImageBitmap( BitmapFactory.decodeFile(iconPath) );
            }else{
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ((BaseActivity)context).downloadDicon(list.get(actPos));
                    }
                }).start();
            }
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onClick(View v) {
        int pos = (int)v.getTag();
        Intent intent = new Intent(context, ShowDoctorActivity.class);
        intent.putExtra("DOCTOR", list.get(pos));
        context.startActivity(intent);
    }
}
