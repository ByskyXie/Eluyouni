package com.github.byskyxie.eluyouni;

import java.io.Serializable;

public class Doctor implements Serializable{
    private long did;
    private int dsex;
    private String dname;
    private String dpwd;
    private String dicon;
    private String dillness;
    private int dgrade;
    private String dprofess;
    private String dcareer;
    private float dmarking;
    private float d24hreply ;
    private int dpatient_num;
    private float dhot_level;

    Doctor(){}

    public Doctor(long did, int dsex, String dname, String dpwd, String dicon, String dillness, int dgrade, String dprofess, String dcareer, float dmarking, float d24hreply, int dpatient_num, float dhot_level) {
        this.did = did;
        this.dsex = dsex;
        this.dname = dname;
        this.dpwd = dpwd;
        this.dicon = dicon;
        this.dillness = dillness;
        this.dgrade = dgrade;
        this.dprofess = dprofess;
        this.dcareer = dcareer;
        this.dmarking = dmarking;
        this.d24hreply = d24hreply;
        this.dpatient_num = dpatient_num;
        this.dhot_level = dhot_level;
    }

    public int getDsex() {
        return dsex;
    }

    public void setDsex(int dsex) {
        this.dsex = dsex;
    }

    public long getDid() {
        return did;
    }
    public void setDid(long did) {
        this.did = did;
    }
    public String getDname() {
        return dname;
    }
    public void setDname(String dname) {
        this.dname = dname;
    }
    public String getDpwd() {
        return dpwd;
    }
    public void setDpwd(String dpwd) {
        this.dpwd = dpwd;
    }
    public String getDicon() {
        return dicon;
    }
    public void setDicon(String dicon) {
        this.dicon = dicon;
    }
    public String getDillness() {
        return dillness;
    }
    public void setDillness(String dillness) {
        this.dillness = dillness;
    }
    public int getDgrade() {
        return dgrade;
    }
    public void setDgrade(int dgrade) {
        this.dgrade = dgrade;
    }
    public String getDprofess() {
        return dprofess;
    }
    public void setDprofess(String dprofess) {
        this.dprofess = dprofess;
    }
    public String getDcareer() {
        return dcareer;
    }
    public void setDcareer(String dcareer) {
        this.dcareer = dcareer;
    }
    public float getDmarking() {
        return dmarking;
    }
    public void setDmarking(float dmarking) {
        this.dmarking = dmarking;
    }
    public float getD24hreply() {
        return d24hreply;
    }
    public void setD24hreply(float d24hreply) {
        this.d24hreply = d24hreply;
    }
    public int getDpatient_num() {
        return dpatient_num;
    }
    public void setDpatient_num(int dpatient_num) {
        this.dpatient_num = dpatient_num;
    }
    public float getDhot_level() {
        return dhot_level;
    }
    public void setDhot_level(float dhot_level) {
        this.dhot_level = dhot_level;
    }

    public String getGradeName(){
        switch (dgrade){
            case 1:
                return "青铜";
            case 2:
                return "白银";
            case 3:
                return "黄金";
            case 4:
                return "铂金";
            case 5:
                return "钻石";
            default:
                return "null";
        }
    }
}
