package com.github.byskyxie.eluyouni;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class EluDatabaseOpenHelper extends SQLiteOpenHelper {
    private static final String SQL_CREATE = "" +
            "CREATE TABLE PATIENT(" +
            "PID INT PRIMARY KEY NOT NULL," +
            "PNAME VARCHAR(8) NOT NULL," +
            "PPWD VARCHAR(18) NOT NULL," +  //密码
            "PICON TEXT," +
            "ECOIN BIGINT NOT NULL," +
            "PSCORE INT NOT NULL" +
            ");";

    public EluDatabaseOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
