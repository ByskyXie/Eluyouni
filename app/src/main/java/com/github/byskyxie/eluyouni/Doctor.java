package com.github.byskyxie.eluyouni;

public class Doctor {
    private long did;
    private String dname;
    private String dpwd;
    private String dicon;
    private String dsection;
    private int dgrade;
    private String dprofess;
    private String dcareer;
    private float dmarking;
    private float d24hreply ;
    private int dpatient_num;
    private float dhot_level;

    public Doctor(){}
    public Doctor(long did, String dname, String dpwd, String dicon, String dsection, int dgrade,
                  String dprofess, String dcareer, float dmarking, float d24hreply, int dpatient_num, float dhot_level) {
        this.did = did;
        this.dname = dname;
        this.dpwd = dpwd;
        this.dicon = dicon;
        this.dsection = dsection;
        this.dgrade = dgrade;
        this.dprofess = dprofess;
        this.dcareer = dcareer;
        this.dmarking = dmarking;
        this.d24hreply = d24hreply;
        this.dpatient_num = dpatient_num;
        this.dhot_level = dhot_level;
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
    public String getDsection() {
        return dsection;
    }
    public void setDsection(String dsection) {
        this.dsection = dsection;
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
}