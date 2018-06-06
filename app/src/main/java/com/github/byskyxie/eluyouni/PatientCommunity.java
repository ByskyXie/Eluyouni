package com.github.byskyxie.eluyouni;

public class PatientCommunity {
    private long cid;
    private long erId;
    private int erType;
    private String time;
    private String ccontent;
    private int assentNum;
    private boolean assented ;

    protected PatientCommunity(){ assented = false;}
    protected PatientCommunity(long cid, long erId, int erType, String time, String ccontent, int assentNum) {
        this.cid = cid;
        this.erId = erId;
        this.erType = erType;
        this.time = time;
        this.ccontent = ccontent;
        this.assentNum = assentNum;
        assented = false;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public long getCid() {
        return cid;
    }

    public void setCid(long cid) {
        this.cid = cid;
    }

    public long getErId() {
        return erId;
    }

    public void setErId(long erId) {
        this.erId = erId;
    }

    public int getErType() {
        return erType;
    }

    public void setErType(int erType) {
        this.erType = erType;
    }

    public String getCcontent() {
        return ccontent;
    }

    public void setCcontent(String ccontent) {
        this.ccontent = ccontent;
    }

    public int getAssentNum() {
        return assentNum;
    }

    public void setAssentNum(int assentNum) {
        this.assentNum = assentNum;
    }

    public boolean isAssented() {
        return assented;
    }

    public void setAssented(boolean assented) {
        this.assented = assented;
    }

    @Override
    public String toString() {
        return "time:"+getTime();
    }
}
