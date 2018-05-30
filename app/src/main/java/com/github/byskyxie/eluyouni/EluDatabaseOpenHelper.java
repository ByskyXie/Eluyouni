package com.github.byskyxie.eluyouni;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class EluDatabaseOpenHelper extends SQLiteOpenHelper {
    private static final String SQL_CREATE = "" +
            "CREATE TABLE PATIENT(" +
            "PID LONG PRIMARY KEY NOT NULL," +
            "PSEX INT," +
            "PNAME VARCHAR(8) NOT NULL," +
            "PPWD VARCHAR(18) NOT NULL," +  //密码
            "PICON TEXT," +
            "ECOIN BIGINT NOT NULL," +
            "PSCORE INT NOT NULL" +
            ");";
    private static final String SQL_PATIENT_BASE_INFO = "" +
            "CREATE TABLE PATIENT_BASE_INFO(" +
            "PID LONG PRIMARY KEY," +
            "PSEX INT," +
            "PNAME VARCHAR(8) NOT NULL," +
            "PICON TEXT," +
            "PSCORE INT NOT NULL " +
            ");";
    private static final String SQL_DOCTOR_BASE_INFO = "" +
            "CREATE TABLE DOCTOR_BASE_INFO(" +
            "DID LONG PRIMARY KEY," +
            "DSEX INT," +
            "DNAME VARCHAR(8) NOT NULL," +
            "DICON TEXT," +
            "DILLNESS TEXT," +
            "DGRADE INT," +
            "DPROFESS TEXT," +
            "DCAREER TEXT," +
            "DHOSPITAL TINYTEXT," +
            "DMARKING REAL," +
            "D24HREPLY REAL," +
            "DPATIENT_NUM REAL," +
            "DHOT_LEVEL REAL" +
            ");";
    private static final String SQL_CHAT_RECORD = "" +
            "CREATE TABLE CHAT_RECORD(" +
            "ID LONG NOT NULL," +
            "ERID LONG NOT NULL," +
            "ERTYPE INT," +
            "TIME DATE," +
            "CHATTYPE INT," +
            "CONTENT TEXT," +
            "PRIMARY KEY(ID,ERID,TIME)" +
            ");";

    EluDatabaseOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE);
        db.execSQL(SQL_PATIENT_BASE_INFO);
        db.execSQL(SQL_DOCTOR_BASE_INFO);
        db.execSQL(SQL_CHAT_RECORD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
