package com.github.byskyxie.eluyouni;

public class ArticleRecommend {
    private long arid;
    private long erid;
    private int ertype;
    private String title;
    private String time;
    private String content;

    ArticleRecommend(){}

    ArticleRecommend(long arid, long erid, int ertype, String title, String time, String content) {
        this.arid = arid;
        this.erid = erid;
        this.ertype = ertype;
        this.title = title;
        this.time = time;
        this.content = content;
    }

    public long getArid() {
        return arid;
    }

    public void setArid(long arid) {
        this.arid = arid;
    }

    public long getErid() {
        return erid;
    }

    public void setErid(long erid) {
        this.erid = erid;
    }

    public int getErtype() {
        return ertype;
    }

    public void setErtype(int ertype) {
        this.ertype = ertype;
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
}
