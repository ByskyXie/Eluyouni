package com.github.byskyxie.eluyouni;

import android.content.ContentValues;
import android.util.Log;

public class Patient {
    private long pid;
    private String pwd;
    private String pname;
    private String picon;
    private long ecoin;
    private int pscore;

    public long getPid() {
        return pid;
    }

    public void setPid(long pid) {
        this.pid = pid;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getPicon() {
        return picon;
    }

    public void setPicon(String picon) {
        this.picon = picon;
    }

    public long getEcoin() {
        return ecoin;
    }

    public void setEcoin(long ecoin) {
        this.ecoin = ecoin;
    }

    public int getPscore() {
        return pscore;
    }

    public void setPscore(int pscore) {
        this.pscore = pscore;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public Patient(){;}

    public Patient(long pid, String pname, String pwd, String picon, long ecoin, int pscore) {
        super();
        this.pid = pid;
        this.pname = pname;
        this.pwd = pwd;
        this.picon = picon;
        this.ecoin = ecoin;
        this.pscore = pscore;
    }

    public void save(){
        //TODO:to database
        ContentValues content = new ContentValues();
        BaseActivity.userDatabasewrit.delete("patient",null,null);

        content.clear();
        content.put("PID",pid);
        content.put("PNAME",pname);
        content.put("PPWD",pwd);
        content.put("PICON",picon);
        content.put("ECOIN",ecoin);
        content.put("PSCORE",pscore);
        Log.w("Patient","saved");
        BaseActivity.userDatabasewrit.insert("patient",null,content);
    }
}
