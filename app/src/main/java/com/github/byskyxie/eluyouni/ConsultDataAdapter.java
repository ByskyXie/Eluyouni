package com.github.byskyxie.eluyouni;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
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

public class ConsultDataAdapter extends RecyclerView.Adapter<ConsultDataAdapter.ConsultDataHolder>
            implements View.OnClickListener{
    public static final int CONSULT_DATA_ADAPTER = 0x0101;

    private int dataNumLimit = 5;
    private Context context;
    private ArrayList<String> list = new ArrayList<>();

    static class ConsultDataHolder extends RecyclerView.ViewHolder{
        View view;
        ConsultDataHolder(View itemView) {
            super(itemView);
            view = itemView;
        }
    }

    ConsultDataAdapter(Context context, ArrayList<String> list) {
        this.context = context;
        if(list==null)
            return;
        this.list.addAll( list);
        while(list.size()>dataNumLimit)
            list.remove(list.size()-1 );
    }

    public void addData(ArrayList<String> list){
        if(list==null)
            return;
        this.list.addAll(list);
        while(list.size()>dataNumLimit)
            list.remove(list.size()-1 );
    }

    public void addData(String uri){
        if(uri==null || list.size()>dataNumLimit)
            return;
        list.add(uri);
    }

    @Override
    public int getItemViewType(int position) {
        if(list.size() == position && list.size()!=dataNumLimit){
            //没选满，最后选择资料
            return 1;
        }
        return 2;
    }

    @NonNull
    @Override
    public ConsultDataAdapter.ConsultDataHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(viewType == 2)
            view = LayoutInflater.from(context).inflate(R.layout.item_consult_data_list,parent,false);
        else
            view = LayoutInflater.from(context).inflate(R.layout.item_consult_none_data,parent,false);
        return new ConsultDataAdapter.ConsultDataHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ConsultDataAdapter.ConsultDataHolder holder, int position) {
        int actPos = holder.getAdapterPosition();
        if(actPos == list.size() && list.size()!=dataNumLimit){
            ViewGroup.LayoutParams param = holder.view.getLayoutParams();
            if(list.size()==0){
                param.width = ViewGroup.LayoutParams.MATCH_PARENT;
            }else{
                param.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            }
            holder.view.findViewById(R.id.text_view_consult_add_data).setOnClickListener(this);
            return;
        }
        //图片太大进行压缩
        holder.view.findViewById(R.id.image_button_consult_data_delete).setTag(actPos); //点击事件备用
        holder.view.findViewById(R.id.image_button_consult_data_delete).setOnClickListener(this);
        ((ImageView)holder.view.findViewById(R.id.image_view_consult_data_pic)).setImageBitmap( getCompressPic(list.get(actPos)) );
        int temp = list.get(actPos).lastIndexOf('/');
        ((TextView)holder.view.findViewById(R.id.text_view_consult_data_name)).setText( list.get(actPos).substring(temp+1) );
    }

    private Bitmap getCompressPic(String path){
        BitmapFactory.Options op = new BitmapFactory.Options();
        op.inSampleSize=4;
        Bitmap b = BitmapFactory.decodeFile(path);
        if(b == null)
            return null;
        int h=b.getHeight();    b.recycle();
        op.inSampleSize = h/400;
        if(op.inSampleSize < 1)
            op.inSampleSize = 1;
        return BitmapFactory.decodeFile(path, op);
    }

    @Override
    public int getItemCount() {
        if(list.size() < dataNumLimit)
            return list.size() + 1;
        return list.size();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.text_view_consult_add_data:
                ((BaseActivity)context).applyReadFile();
                if(ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    ((MainActivity)context).startActivityForResult(intent, CONSULT_DATA_ADAPTER);
                }
                break;
            case R.id.image_button_consult_data_delete:
                Log.e("++++","get "+v.getTag());
                if(v.getTag()==null ) {
                    return;
                }
                list.remove((int)v.getTag());
                notifyDataSetChanged();
                break;
        }
    }
}
