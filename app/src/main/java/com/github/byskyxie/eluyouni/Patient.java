package com.github.byskyxie.eluyouni;

import android.content.ContentValues;
import android.util.Log;

import java.io.Serializable;

public class Patient implements Serializable{
    private long pid;
    private int psex;
    private String pwd;
    private String pname;
    private String picon;
    private long ecoin;
    private int pscore;

    public int getPsex() {
        return psex;
    }

    public void setPsex(int psex) {
        this.psex = psex;
    }

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

    Patient(){}

    public Patient(long pid, int psex, String pwd, String pname, String picon, long ecoin, int pscore) {
        this.pid = pid;
        this.psex = psex;
        this.pwd = pwd;
        this.pname = pname;
        this.picon = picon;
        this.ecoin = ecoin;
        this.pscore = pscore;
    }

    public String getGradeName(){
        if(pscore>120)
            return "铂金";
        if(pscore>90)
            return "黄金";
        if(pscore>60)
            return "白银";
        return "青铜";
    }

    public int getGrade(){
        if(pscore>120)
            return 4;
        if(pscore>90)
            return 3;
        if(pscore>60)
            return 2;
        return 1;
    }

    public void save(){
        ContentValues content = new ContentValues();
        BaseActivity.userDatabasewrit.delete("patient",null,null);

        content.clear();
        content.put("PID",pid);
        content.put("PSEX",psex);
        content.put("PNAME",pname);
        content.put("PPWD",pwd);
        content.put("PICON",picon);
        content.put("ECOIN",ecoin);
        content.put("PSCORE",pscore);
        Log.w("Patient","saved");
        BaseActivity.userDatabasewrit.insert("patient",null,content);
    }
}
