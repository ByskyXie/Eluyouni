package com.github.byskyxie.eluyouni;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Handler;

import static com.github.byskyxie.eluyouni.BaseActivity.IP_SERVER;
import static com.github.byskyxie.eluyouni.BaseActivity.userInfo;

public class PriDocAdapter extends RecyclerView.Adapter<PriDocAdapter.PriDocHolder>
        implements View.OnClickListener{

    private Context context;
    private PriDocFragment.PriHandler handler;
    private ArrayList<Doctor> list = new ArrayList<>();

    static class PriDocHolder extends RecyclerView.ViewHolder{
        private View view;
        private ImageView icon;
        private TextView name;
        private TextView grade;
        private TextView talk;
        PriDocHolder(View itemView) {
            super(itemView);
            view = itemView;
            icon = itemView.findViewById(R.id.image_view_pri_icon);
            name = itemView.findViewById(R.id.text_view_pri_name);
            grade = itemView.findViewById(R.id.text_view_pri_grade);
            talk = itemView.findViewById(R.id.text_view_talk);
        }
    }

    PriDocAdapter(Context context, ArrayList<Doctor> list,@NonNull PriDocFragment.PriHandler handler) {
        this.context = context;
        this.handler = handler;
        if(list !=null)
            this.list.addAll(list);
    }

    protected void addData(ArrayList<Doctor> list){
        if(list != null)
            this.list.addAll(list);
    }

    protected ArrayList<Doctor> getData(){
        return list;
    }

    protected boolean compareDataSetSame(ArrayList<Doctor> list){
        if(list == null)
            return true;
        if(this.list.isEmpty())
            return false;
        return this.list.get(0).getDid() == list.get(0).getDid();
    }

    @NonNull
    @Override
    public PriDocHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_pri_doc_list, parent, false);
        return new PriDocHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final PriDocHolder holder, int position) {
        //bind data
        final int actPos = holder.getAdapterPosition();
        holder.view.setTag(actPos); //记录位置
        holder.view.setOnClickListener(this);
        holder.name.setText( list.get(actPos).getDname() );
        holder.grade.setText( list.get(actPos).getGradeName() );
        //设置默认头像
        if(list.get(actPos).getDsex()==2 )
            holder.icon.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.doctor_woman));
        else
            holder.icon.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.doctor_man));
        //头像
        if(list.get(actPos).getDicon()!=null && !list.get(actPos).getDicon().isEmpty()){
            String iconPath =  context.getFilesDir().getAbsolutePath()+"/icon/dicon/"+list.get(actPos).getDicon();
            File file = new File(iconPath);
            if(file.exists()){
                holder.icon.setImageBitmap( BitmapFactory.decodeFile(iconPath) );
            }else{
                downloadDicon(list.get(actPos), actPos);
            }

        }
        //更新聊天记录
        if(BaseActivity.mapEridToPosition.containsKey(list.get(actPos).getDid()) ){
            ArrayList<ChatItem> cis = BaseActivity.chatRecordList.get(BaseActivity.mapEridToPosition.get(list.get(actPos).getDid())).getList();
            if(cis.size() > 0){
                holder.talk.setText( cis.get( cis.size()-1 ).getContent() );
            }else
                holder.talk.setText("暂无聊天记录");
        }else
            holder.talk.setText("暂无聊天记录");
    }

    private void downloadDicon(final Doctor doctor, final int position){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String request = "http://"+ IP_SERVER+":8080/"+"eluyouni/pic?id="+userInfo.getPid()+"&pic="+doctor.getDicon()
                        +"&pictype=dicon";
                File file = new File( context.getFilesDir()+"/icon/dicon/"+doctor.getDicon() );
                try{
                    createFolder();
                    file.createNewFile();
                    URL url = new URL(request);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    FileOutputStream fos = new FileOutputStream(file);
                    BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
                    do{
                        byte[] bytes = new byte[2000];
                        int i = bis.read(bytes);
                        if(i == -1){
                            bis.close();
                            fos.close();
                            break;
                        }
                        fos.write(bytes, 0, i);
                    }while(true);
                    if(file.length()==0) {    //图片接收失败
                        file.delete();
                        return;
                    }
                }catch (IOException ioe){
                    ioe.printStackTrace();
                }
                Message msg = new Message();
                msg.what = PriDocFragment.DOCTOR_ICON_ACCEPT;
                msg.obj = position;
                handler.sendMessage(msg);
            }
        }).start();
    }

    private void createFolder(){
        File file = new File( context.getFilesDir()+"/icon/dicon/");
        if(!file.exists())
            file.mkdirs();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onClick(View v) {
        int pos = (int)v.getTag();
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra("TargetType",ChatActivity.TARGET_TYPE_DOCTOR);
        intent.putExtra("Target",list.get(pos));
        intent.putExtra("ClickedPos",pos);
        ((BaseActivity)context).startActivityForResult(intent, ChatActivity.CHAT_ACTIVITY_CODE);
    }
}
