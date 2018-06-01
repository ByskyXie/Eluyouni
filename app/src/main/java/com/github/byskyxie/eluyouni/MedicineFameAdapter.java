package com.github.byskyxie.eluyouni;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class MedicineFameAdapter extends RecyclerView.Adapter<MedicineFameAdapter.MedicineFameHolder>
            implements View.OnClickListener{

    private MedicineFragment.MedicineHandler handler;
    private Context context;
    private ArrayList<Doctor> docList = new ArrayList<>();

    static class MedicineFameHolder extends RecyclerView.ViewHolder{
        private View view;
        private ImageView icon;
        private TextView name;
        private TextView ill;
        MedicineFameHolder(View itemView) {
            super(itemView);
            view = itemView;
            ill = itemView.findViewById(R.id.text_view_medicine_famous_ill);
            icon = itemView.findViewById(R.id.image_view_medicine_famous_icon);
            name = itemView.findViewById(R.id.text_view_medicine_famous_name);
        }
    }

    MedicineFameAdapter(Context context, ArrayList<Doctor> docList, MedicineFragment.MedicineHandler handler) {
        this.context = context;
        this.handler = handler;
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
        holder.view.setTag(actPos);
        holder.name.setText( docList.get(actPos).getDname() );
        holder.ill.setText( "主治:"+docList.get(actPos).getDillness() );
        holder.view.setOnClickListener(this);
        //设置默认头像
        if(docList.get(actPos).getDsex()==2 )
            holder.icon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_doctor_female));
        else
            holder.icon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_doctor_male));
        //头像
        if(docList.get(actPos).getDicon()!=null && !docList.get(actPos).getDicon().isEmpty()){
            String iconPath =  context.getFilesDir().getAbsolutePath()+"/icon/dicon/"+docList.get(actPos).getDicon();
            File file = new File(iconPath);
            if(file.exists()){
                holder.icon.setImageBitmap( BitmapFactory.decodeFile(iconPath) );
            }else{
                downloadDicon(docList.get(actPos), actPos);
            }
        }
    }

    @Override
    public int getItemCount() {
        return docList.size();
    }

    private void downloadDicon(final Doctor doctor, final int position){
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(! ((BaseActivity)context).downloadDicon(doctor) )
                    return; //下载失败就算了
                Message msg = new Message();
                msg.what = MedicineFragment.ACCEPT_FAME_ICON;
                msg.obj = position;
                handler.sendMessage(msg);
            }
        }).start();
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(context, ShowDoctorActivity.class);
        intent.putExtra("DOCTOR", docList.get((int)v.getTag()) );
        ((BaseActivity)context).startActivityForResult(intent, ShowDoctorActivity.SHOW_DOCTOR_ACTIVITY_CODE);
    }
}
