package com.github.byskyxie.eluyouni;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class BaseActivity extends AppCompatActivity {

    static{
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    public static final String IP_SERVER = "192.168.137.1";  //"192.168.137.1"  "119.23.62.71"
    public static final String DATE_FORMAT = "yyyy-M-d HH:mm:ss";
    private static final int REQUEST_INTERNET = 10086;
    private static final int REQUEST_FILE_READ = 10010;

    protected static Patient userInfo = null;
    protected static SQLiteDatabase userDatabaseRead;
    protected static SQLiteDatabase userDatabasewrit;
    private static EluDatabaseOpenHelper eluDatabaseOpenHelper;

    protected static HashMap<Long,Integer> mapEridToPosition = new HashMap<>();
    protected static ArrayList<ChatRecord> chatRecordList ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applyConnectNet();
        if(eluDatabaseOpenHelper == null){
            eluDatabaseOpenHelper = new EluDatabaseOpenHelper(this,"Elu.db",null,1);
            userDatabaseRead = eluDatabaseOpenHelper.getReadableDatabase();
            userDatabasewrit = eluDatabaseOpenHelper.getWritableDatabase();
        }
    }

    protected void createFolder(){
        File file = new File( getFilesDir()+"/icon/dicon/");
        if(!file.exists())
            file.mkdirs();
        file = new File(getFilesDir()+"/icon/picon/");
        if(!file.exists())
            file.mkdirs();
    }

    protected boolean isPiconExists(String name){
        return new File(getFilesDir()+"/icon/picon/"+name).exists();
    }

    protected boolean isDiconExists(String name){
        return new File(getFilesDir()+"/icon/dicon/"+name).exists();
    }

    protected boolean downloadDicon(Doctor doctor){
        String request = "http://"+ IP_SERVER+":8080/"+"eluyouni/pic?id="+userInfo.getPid()+"&pic="+doctor.getDicon()
                +"&pictype=dicon";
        File file = new File( getFilesDir()+"/icon/dicon/"+doctor.getDicon() );
        try{
            if(file.exists() && file.length()!=0){
                Log.e("BaseActivity",file.getAbsolutePath()+" exists!");
                return false;
            }
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
                Log.e("BaseActivity","download dicon failed "+doctor.getDicon());
                return false;
            }
        }catch (IOException ioe){
            ioe.printStackTrace();
            return false;
        }
        return true;
    }

    protected boolean downloadDicon(String dicon){
        String request = "http://"+ IP_SERVER+":8080/"+"eluyouni/pic?id="+userInfo.getPid()+"&pic="+dicon
                +"&pictype=dicon";
        File file = new File( getFilesDir()+"/icon/dicon/"+dicon );
        try{
            if(file.exists() && file.length()!=0){
                Log.e("BaseActivity",file.getAbsolutePath()+" exists!");
                return false;
            }
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
                Log.e("BaseActivity","download dicon failed "+dicon);
                return false;
            }
        }catch (IOException ioe){
            ioe.printStackTrace();
            return false;
        }
        return true;
    }

    protected boolean downloadPicon(Patient patient){
        String request = "http://"+ IP_SERVER+":8080/"+"eluyouni/pic?id="+userInfo.getPid()+"&pic="+patient.getPicon()
                +"&pictype=picon";
        File file = new File( getFilesDir()+"/icon/picon/"+patient.getPicon() );
        try{
            if(file.exists() && file.length()!=0){
                Log.e("BaseActivity",file.getAbsolutePath()+" exists!");
                return false;
            }
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
                Log.e("BaseActivity","download picon failed "+patient.getPicon());
                return false;
            }
        }catch (IOException ioe){
            ioe.printStackTrace();
            return false;
        }
        return true;
    }

    protected boolean downloadPicon(String picon){
        String request = "http://"+ IP_SERVER+":8080/"+"eluyouni/pic?id="+userInfo.getPid()+"&pic="+picon
                +"&pictype=picon";
        File file = new File( getFilesDir()+"/icon/picon/"+picon );
        try{
            if(file.exists() && file.length()!=0){
                Log.e("BaseActivity",file.getAbsolutePath()+" exists!");
                return false;
            }
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
                Log.e("BaseActivity","download picon failed "+picon);
                return false;
            }
        }catch (IOException ioe){
            ioe.printStackTrace();
            return false;
        }
        return true;
    }

    protected boolean isLogin(){
        Cursor cursor = userDatabaseRead.query("PATIENT",new String[]{"*"}, null, null,null,null,null);
        if(cursor == null )
            return false;
        else if(cursor.getCount() == 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    protected void readUserInfo(){
        if(userInfo!=null)
            return;
        userInfo = new Patient();
        Cursor cursor = userDatabaseRead.query("PATIENT", new String[]{"*"}, null, null, null, null, null);
        cursor.moveToFirst();
        userInfo.setPid(cursor.getLong(cursor.getColumnIndex("PID")));
        userInfo.setPsex(cursor.getInt(cursor.getColumnIndex("PSEX")));
        userInfo.setPname(cursor.getString(cursor.getColumnIndex("PNAME")));
        userInfo.setPwd(cursor.getString(cursor.getColumnIndex("PPWD")));
        userInfo.setPicon(cursor.getString(cursor.getColumnIndex("PICON")));
        userInfo.setEcoin(cursor.getLong(cursor.getColumnIndex("ECOIN")));
        userInfo.setPscore(cursor.getInt(cursor.getColumnIndex("PSCORE")));
        cursor.close();
    }

    /**
     * function: to apply internet permission
     * */
    protected void applyConnectNet(){
        int code = ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET);
        if(code == PackageManager.PERMISSION_GRANTED)
            return;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            requestPermissions(new String[]{Manifest.permission.INTERNET}, REQUEST_INTERNET);
        }
    }

    /**
     * function: to apply read file data
     * */
    protected void applyReadFile(){
        int code = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if(code == PackageManager.PERMISSION_GRANTED)
            return;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_INTERNET);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_INTERNET:
                if(grantResults[0] != PackageManager.PERMISSION_GRANTED){
                    //不允许联网, 再次请求
                    applyConnectNet();
                }
                break;
            case REQUEST_FILE_READ:
                if(grantResults[0] != PackageManager.PERMISSION_GRANTED){
                    //不允许读取文件, 再次请求
                    applyReadFile();
                }
                break;
        }
    }

    protected void getChatRecord(){
        chatRecordList = new ArrayList<>();
        Cursor cursor = userDatabaseRead.query("CHAT_RECORD",new String[]{"*"}, null,
                null, null,null,"ID ASC,ERID ASC, TIME ASC");
        if(!cursor.moveToFirst()){
            cursor.close();
            return;
        }
        //begin
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.CHINA);
        long id,erid;
        int ertype,chattype;
        Date time;
        String content;
        do{
            // id erid ertype time chattype content
            id = cursor.getLong(cursor.getColumnIndex("ID"));
            if(userInfo == null || id != userInfo.getPid())
                continue;   //非当前用户的记录不读取
            erid = cursor.getLong(cursor.getColumnIndex("ERID"));
            ertype = cursor.getInt(cursor.getColumnIndex("ERTYPE"));
            try{
                time = sdf.parse(cursor.getString(cursor.getColumnIndex("TIME")) );
            }catch (ParseException pe){
                Log.e(".BaseActivity","Time parse error:"+pe);
                continue;//跳过该记录
            }
            chattype = cursor.getInt(cursor.getColumnIndex("CHATTYPE"));
            content = cursor.getString(cursor.getColumnIndex("CONTENT"));
            if(!mapEridToPosition.containsKey(erid)){
                //没有该聊天方的记录集，加入新ChatRecord
                mapEridToPosition.put(erid,chatRecordList.size());
                chatRecordList.add(new ChatRecord(userInfo.getPid(), erid, ertype, null));
            }
            if(chattype == ChatItem.CHAT_TYPE_OTHER_SIDE)   //是对方的发言
                chatRecordList.get(mapEridToPosition.get(erid)).addChatItem( new ChatItem(content,chattype,time,erid,ertype) );
            else if(chattype == ChatItem.CHAT_TYPE_SELF)    //用户的发言
                chatRecordList.get(mapEridToPosition.get(erid)).addChatItem( new ChatItem(content,chattype,time,userInfo.getPid(),1) );
        }while(cursor.moveToNext());
        cursor.close();
    }


}
