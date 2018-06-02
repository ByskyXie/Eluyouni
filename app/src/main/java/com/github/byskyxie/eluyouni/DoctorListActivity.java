package com.github.byskyxie.eluyouni;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class DoctorListActivity extends BaseActivity {

    private static final int ACCEPT_DOCTOR_LIST = 0x1025;

    private Illness ill;
    private DoctorListHandler handler = new DoctorListHandler(new WeakReference<>(this));
    private RecyclerView recyclerList;
    private DoctorListAdapter adapter = new DoctorListAdapter(this, null);

    class DoctorListHandler extends Handler{
        private WeakReference<DoctorListActivity> activity;

        public DoctorListHandler(WeakReference<DoctorListActivity> activity) {
            this.activity = activity;
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case ACCEPT_DOCTOR_LIST:
                    if(activity.get() == null || activity.get().adapter.getItemCount()!=0)
                        return; //只接受一次，为0时
                    activity.get().adapter.addData((ArrayList<Doctor>) msg.obj);
                    try{
                        while(activity.get().recyclerList.isComputingLayout())
                            Thread.sleep(200);
                    }catch (InterruptedException ie){
                        ie.printStackTrace();
                    }
                    activity.get().adapter.notifyDataSetChanged();
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_list);
        Toolbar toolbar = findViewById(R.id.toolbar_doc_list);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initialIntent(getIntent());
        recyclerList = findViewById(R.id.recycler_doctor_list);
        recyclerList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerList.setAdapter(adapter);
    }

    protected void initialIntent(Intent intent){
        if(intent == null || intent.getSerializableExtra("ILLNESS") == null)
            return;
        ill = (Illness) intent.getSerializableExtra("ILLNESS");
        getSupportActionBar().setTitle(ill.getName());
        //下载列表
        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<Doctor> list = null;
                try{
                    list = downloadIllnessDoctor(ill);
                }catch (InterruptedException ie){
                    ie.printStackTrace();
                }
                if(list != null && list.size()>0){
                    Message msg = new Message();
                    msg.what = ACCEPT_DOCTOR_LIST;
                    msg.obj = list;
                    handler.sendMessage(msg);
                }
            }
        }).start();
    }

    protected ArrayList<Doctor> downloadIllnessDoctor(Illness ill) throws InterruptedException{
        ArrayList<Doctor> list = new ArrayList<>();
        String request = "http://"+ IP_SERVER+":8080/"+"eluyouni/doctorlist";
        try{
            URL url = new URL(request);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");  //POST方法
            //输出疾病名
            BufferedWriter bw = new BufferedWriter( new OutputStreamWriter( conn.getOutputStream() ));
            bw.write(URLEncoder.encode("ill="+ill.getName(), "UTF-8")); //字符编码转换
            bw.write(URLEncoder.encode("&id="+userInfo.getPid(), "UTF-8"));
            bw.flush();
            bw.close();
            //等待响应
            if(conn.getResponseCode() != HttpURLConnection.HTTP_OK){
                Log.w("ShowDoctor","ResponseCode!=OK, ="+conn.getResponseCode());
                return null;
            }
            BufferedReader br = new BufferedReader( new InputStreamReader( conn.getInputStream() ) );
            String line = br.readLine();
            if(line == null){
                Log.e("MedicinFragment","connect medicine failed");
                return list;
            }
            int num = Integer.parseInt( line.substring(line.indexOf('=')+1) );
            for(int i=0;i<num;i++){
                Doctor doctor = downloadOneDoctorBaseInfo(br);
                if(doctor != null)
                    list.add( doctor );
            }
            //插入数据库
            this.writeDoctorBaseInfo(list);
            br.close();
        }catch (IOException ioe){
            Log.e("showdoctorlist","IOException:"+ioe.getMessage());
            return list;
        }
        return list;
    }

}
