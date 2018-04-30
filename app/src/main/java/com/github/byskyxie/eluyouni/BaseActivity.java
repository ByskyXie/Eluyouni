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
import android.util.Log;

import java.security.Permission;

public class BaseActivity extends AppCompatActivity {
    public static final String IP_SERVER = "192.168.137.1";  //"192.168.137.1"  "119.23.62.71"
    private static final String SQL_LOGIN = "SELECT COUNT(*) FROM PATIENT;";

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
        if(cursor == null || cursor.getCount() == 0)
            return false;
        return true;
    }

    private void readUserInfo(){
        if(userInfo!=null)
            return;
        //TODO:从数据库读取资料

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
