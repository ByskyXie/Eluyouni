package com.github.byskyxie.eluyouni;

import android.Manifest;
import android.content.ContentValues;
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
import java.io.BufferedReader;
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
    protected static final int DOC_NUM_LIMIT = 5; //会诊室医生最大容量
    private static final int REQUEST_INTERNET = 10086;
    private static final int REQUEST_FILE_READ = 10010;

    protected static ArrayList<Doctor> consultList = new ArrayList<>();
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
        Cursor cursor = userDatabaseRead.query("PATIENT",new String[]{"*"}
            , null, null,null,null,null);
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
        Cursor cursor = userDatabaseRead.query("PATIENT", new String[]{"*"}
            , null, null, null, null, null);
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

    protected Patient downloadOnePatientBaseInfo(BufferedReader br) throws IOException{
        Patient patient = new Patient();
        String line;
        line = br.readLine();//PID
        patient.setPid(Long.parseLong( line.substring(line.indexOf('=')+1) ));
        line = br.readLine();//PSEX
        patient.setPsex( Integer.parseInt( line.substring(line.indexOf('=')+1 ) ));
        line = br.readLine();//PNAME
        patient.setPname( line.substring(line.indexOf('=')+1 ));
        line = br.readLine();//PICON
        patient.setPicon( line.substring(line.indexOf('=')+1 ) );
        line = br.readLine();//PSCORE
        patient.setPscore( Integer.parseInt(line.substring(line.indexOf('=')+1 )) );
        return patient;
    }

    protected int writePatientBaseInfo(ArrayList<Patient> list){
        int num = 0;
        if(list == null)
            return num;
        for(Patient p: list){
            if(writePatientBaseInfo(p))
                num++;
        }
        return num;
    }

    protected boolean writePatientBaseInfo(Patient patient){
        if(patient == null)
            return false;
        ContentValues content = new ContentValues();
        Cursor cursor = userDatabaseRead.query("PATIENT_BASE_INFO", new String[]{"*"}
                , "PID=? ", new String[]{""+patient.getPid()}, null, null, null);
        if(cursor.getCount()!=0){
            cursor.close();
            return false;
        }
        cursor.close();
        //写入
        content.clear();
        content.put("PID",patient.getPid());
        content.put("PSEX",patient.getPsex());
        content.put("PNAME",patient.getPname());
        content.put("PICON",patient.getPicon());
        content.put("PSCORE",patient.getPscore());
        userDatabasewrit.insert("PATIENT_BASE_INFO", null, content);
        return true;
    }

    protected Patient readPatientBaseInfo(long pid){
        Cursor cursor = userDatabaseRead.query("PATIENT_BASE_INFO",new String[]{"*"}
                , "PID=?",new String[]{""+pid},null,null,null,null);
        if( !cursor.moveToFirst()){
            return null;
        }
        Patient patient = new Patient();
        patient.setPid(pid);
        patient.setPname( cursor.getString( cursor.getColumnIndex("PNAME") ) );
        patient.setPsex( cursor.getInt( cursor.getColumnIndex("PSEX") ) );
        patient.setPicon( cursor.getString( cursor.getColumnIndex("PICON") ) );
        patient.setPscore( cursor.getInt( cursor.getColumnIndex("PSCORE") ) );
        cursor.close();
        return patient;
    }

    protected Doctor downloadOneDoctorBaseInfo(BufferedReader br) throws IOException{
        Doctor doctor = new Doctor();
        String line;
        StringBuilder temp = new StringBuilder();
        line = br.readLine();//did
        doctor.setDid( Long.parseLong( line.substring(line.indexOf('=')+1) ) );
        line = br.readLine();//dsex
        doctor.setDsex( Integer.parseInt( line.substring(line.indexOf('=')+1)) );
        line = br.readLine();//dname
        doctor.setDname( line.substring(line.indexOf('=')+1) );
        line = br.readLine();//dicon
        doctor.setDicon( line.substring(line.indexOf('=')+1) );
        line = br.readLine();//dillness
        doctor.setDillness( line.substring(line.indexOf('=')+1) );
        line = br.readLine();//dgrade
        doctor.setDgrade( Integer.parseInt( line.substring(line.indexOf('=')+1) ) );
        line = br.readLine();//dprofess
        temp.append( line.substring( line.indexOf('=')+1 ) );
        do{
            line = br.readLine();
            if(line == null)
                return null;
            if(line.matches("dcareer=.*")){
                doctor.setDprofess(temp.toString());
                break;
            }
            temp.append(line);
        }while(true);

        //dcareer
        temp.delete(0, temp.length());
        temp.append( line.substring( line.indexOf('=')+1 ) );
        do{
            line = br.readLine();
            if(line == null)
                return null;
            if(line.matches("dhospital=.*")){
                doctor.setDcareer(temp.toString());
                break;
            }
            temp.append(line);
        }while(true);
        doctor.setDhospital( line.substring(line.indexOf('=')+1) );//dhospital
        line = br.readLine();//dmarking
        doctor.setDmarking( Float.parseFloat( line.substring(line.indexOf('=')+1) ) );
        line = br.readLine();//d24hreply
        doctor.setD24hreply( Float.parseFloat( line.substring(line.indexOf('=')+1) ) );
        line = br.readLine();//dpatient_num
        doctor.setDpatient_num( Integer.parseInt( line.substring(line.indexOf('=')+1) ) );
        line = br.readLine();//dhot_level
        doctor.setDhot_level( Float.parseFloat( line.substring(line.indexOf('=')+1) ) );
        return doctor;
    }

    protected int writeDoctorBaseInfo(ArrayList<Doctor> list){
        int num = 0;
        if(list == null)
            return num;
        for(Doctor d: list){
            if(writeDoctorBaseInfo(d))
                num++;
        }
        return num;
    }


    protected boolean writeDoctorBaseInfo(Doctor doctor){
        if(doctor == null)
            return false;
        ContentValues content = new ContentValues();
        Cursor cursor = userDatabaseRead.query("DOCTOR_BASE_INFO", new String[]{"*"}
                , "DID=? ", new String[]{""+doctor.getDid()}, null, null, null);
        if(cursor.getCount()!=0){
            cursor.close();
            return false;
        }
        cursor.close();
        //写入
        content.clear();
        content.put("DID",doctor.getDid());
        content.put("DSEX",doctor.getDsex());
        content.put("DNAME",doctor.getDname());
        content.put("DICON",doctor.getDicon());
        content.put("DILLNESS",doctor.getDillness());
        content.put("DGRADE",doctor.getDgrade());
        content.put("DPROFESS",doctor.getDprofess());
        content.put("DCAREER",doctor.getDcareer());
        content.put("DHOSPITAL",doctor.getDhospital());
        content.put("DMARKING",doctor.getDmarking());
        content.put("D24HREPLY",doctor.getD24hreply());
        content.put("DPATIENT_NUM",doctor.getDpatient_num());
        content.put("DHOT_LEVEL",doctor.getDhot_level());
        userDatabasewrit.insert("DOCTOR_BASE_INFO", null, content);
        return true;
    }

    protected Doctor readDoctorBaseInfo(long did){
        Cursor cursor = userDatabaseRead.query("DOCTOR_BASE_INFO",new String[]{"*"}
                , "DID=?",new String[]{""+did},null,null,null,null);
        if( !cursor.moveToFirst() ){
            return null;
        }
        Doctor doctor = new Doctor();
        doctor.setDid(did);
        doctor.setDname( cursor.getString( cursor.getColumnIndex("DNAME") ) );
        doctor.setDsex( cursor.getInt( cursor.getColumnIndex("DSEX") ) );
        doctor.setDicon( cursor.getString( cursor.getColumnIndex("DICON") ) );
        doctor.setDillness( cursor.getString( cursor.getColumnIndex("DILLNESS") ) );
        doctor.setDgrade( cursor.getInt( cursor.getColumnIndex("DGRADE") ) );
        doctor.setDprofess( cursor.getString( cursor.getColumnIndex("DPROFESS") ) );
        doctor.setDcareer( cursor.getString( cursor.getColumnIndex("DCAREER") ) );
        doctor.setDhospital( cursor.getString( cursor.getColumnIndex("DHOSPITAL") ) );
        doctor.setDmarking( cursor.getFloat( cursor.getColumnIndex("DMARKING") ) );
        doctor.setD24hreply( cursor.getFloat( cursor.getColumnIndex("D24HREPLY") ) );
        doctor.setDpatient_num( cursor.getInt( cursor.getColumnIndex("DPATIENT_NUM") ) );
        doctor.setDhot_level( cursor.getFloat( cursor.getColumnIndex("DHOT_LEVEL") ) );
        cursor.close();
        return doctor;
    }

    protected ArticleRecommend downloadOneArticleRecommend(BufferedReader br) throws IOException{
        String line;
        StringBuilder temp = new StringBuilder();
        ArticleRecommend ar = new ArticleRecommend();
        //arid
        line = br.readLine();
        ar.setArid( Long.parseLong( line.substring(line.indexOf('=')+1) ) );
        //erid
        line = br.readLine();
        ar.setErid( Long.parseLong( line.substring(line.indexOf('=')+1) ) );
        //ertype
        line = br.readLine();
        ar.setErtype( Integer.parseInt( line.substring(line.indexOf('=')+1) ) );
        //title
        line = br.readLine();
        ar.setTitle( line.substring(line.indexOf('=')+1) );
        //time
        line = br.readLine();
        ar.setTime( line.substring(line.indexOf('=')+1) );
        //content
        line = br.readLine();
        temp.append( line.substring(line.indexOf('=')+1) );
        do{
            line = br.readLine();
            if(line.matches("end=.+"))
                break;
            temp.append( line );
            temp.append('\n');
        }while(true);
        ar.setContent( temp.toString() );
        return ar;
    }

    protected ArticlePatient downloadOneArticlePatient(BufferedReader br) throws IOException{
        ArticlePatient ap = new ArticlePatient();
        StringBuilder temp = new StringBuilder();
        String line;
        //apid
        line = br.readLine();
        ap.setApid( Long.parseLong( line.substring(line.indexOf('=')+1) ) );
        //pid
        line = br.readLine();
        ap.setPid( Long.parseLong( line.substring(line.indexOf('=')+1) ) );
        //title
        line = br.readLine();
        ap.setTitle( line.substring(line.indexOf('=')+1) );
        //time
        line = br.readLine();
        ap.setTime( line.substring(line.indexOf('=')+1) );
        //content
        line = br.readLine();
        temp.append( line.substring(line.indexOf('=')+1) );
        do{
            line = br.readLine();
            if(line.matches("end=.+"))
                break;
            temp.append( line );
            temp.append('\n');
        }while(true);
        ap.setContent( temp.toString() );
        return ap;
    }

    protected ArticleDoctor downloadOneArticleDoctor(BufferedReader br) throws IOException{
        ArticleDoctor ad = new ArticleDoctor();
        StringBuilder temp = new StringBuilder();
        String line;
        //adid
        line = br.readLine();
        ad.setAdid( Long.parseLong( line.substring(line.indexOf('=')+1) ) );
        //did
        line = br.readLine();
        ad.setDid( Long.parseLong( line.substring(line.indexOf('=')+1) ) );
        //title
        line = br.readLine();
        ad.setTitle( line.substring(line.indexOf('=')+1) );
        //time
        line = br.readLine();
        ad.setTime( line.substring(line.indexOf('=')+1) );
        //content
        line = br.readLine();
        temp.append( line.substring(line.indexOf('=')+1) );
        do{
            line = br.readLine();
            if(line.matches("end=.+"))
                break;
            temp.append( line );
            temp.append('\n');
        }while(true);
        ad.setContent( temp.toString() );
        return ad;
    }

    protected PatientCommunity downloadOnePatientCommunity(BufferedReader br) throws IOException{
        String line;
        StringBuilder temp = new StringBuilder();
        PatientCommunity pc = new PatientCommunity();
        //cid
        line = br.readLine();
        pc.setCid( Long.parseLong( line.substring(line.indexOf('=')+1) ) );
        //erid
        line = br.readLine();
        pc.setErId( Long.parseLong( line.substring(line.indexOf('=')+1) ) );
        //ertype
        line = br.readLine();
        pc.setErType( Integer.parseInt( line.substring(line.indexOf('=')+1) ) );
        //time
        line = br.readLine();
        pc.setTime( line.substring(line.indexOf('=')+1) );
        //content
        line = br.readLine();
        temp.append( line.substring(line.indexOf('=')+1) );
        do{
            line = br.readLine();
            if(line.matches("assent=.+"))
                break;
            temp.append( line );
            temp.append('\n');
        }while(true);
        pc.setCcontent( temp.toString() );
        //assent 直到遇见assent
        pc.setAssentNum( Integer.parseInt( line.substring(line.indexOf('=')+1) ) );
        return pc;
    }

    protected boolean addConsultDoctor(Doctor doctor){
        if(doctor == null || consultList.size()>=DOC_NUM_LIMIT || isConsultContain(doctor.getDid()))
            return false;
        consultList.add(doctor);
        return true;
    }

    protected ArrayList<Doctor> getConsultList(){
        return consultList;
    }

    protected boolean isConsultContain(long did){
        for(Doctor d: consultList)
            if(d.getDid() == did)
                return true;
        return false;
    }

}
