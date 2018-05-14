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

public class BaseActivity extends AppCompatActivity {
    public static final String IP_SERVER = "192.168.137.1";  //"192.168.137.1"  "119.23.62.71"

    protected static Patient userInfo = null;
    protected static SQLiteDatabase userDatabaseRead;
    protected static SQLiteDatabase userDatabasewrit;
    private static EluDatabaseOpenHelper eluDatabaseOpenHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applyConnectNet();
        if(eluDatabaseOpenHelper == null){
            eluDatabaseOpenHelper = new EluDatabaseOpenHelper(this,"Elu.db",null,1);
            userDatabaseRead = eluDatabaseOpenHelper.getReadableDatabase();
            userDatabasewrit = eluDatabaseOpenHelper.getWritableDatabase();
        }
        if(isLogin())
            readUserInfo();
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

    private void readUserInfo(){
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
            requestPermissions(new String[]{Manifest.permission.INTERNET}, 10086);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 10086:
                if(grantResults[0] != PackageManager.PERMISSION_GRANTED){
                    //不允许联网, 再次请求
                    applyConnectNet();
                }
                break;
        }
    }
    

}
