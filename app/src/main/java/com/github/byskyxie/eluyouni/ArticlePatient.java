package com.github.byskyxie.eluyouni;

import java.io.Serializable;

public class ArticlePatient implements Serializable{
    private long apid;
    private long pid;
    private String title;
    private String time;
    private String content;

    ArticlePatient(){}
    
    public ArticlePatient(long apid, long pid, String title, String time, String content) {
        this.apid = apid;
        this.pid = pid;
        this.title = title;
        this.time = time;
        this.content = content;
    }

    public long getApid() {
        return apid;
    }

    public void setApid(long apid) {
        this.apid = apid;
    }

    public long getPid() {
        return pid;
    }

    public void setPid(long pid) {
        this.pid = pid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "time:"+getTime();
    }
}
