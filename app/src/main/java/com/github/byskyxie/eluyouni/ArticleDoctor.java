package com.github.byskyxie.eluyouni;

public class ArticleDoctor {
    private long adid;
    private long did;
    private String title;
    private String time;
    private String content;

    public ArticleDoctor(){}

    public ArticleDoctor(int adid, long did, String title, String time, String content) {
        this.adid = adid;
        this.did = did;
        this.title = title;
        this.time = time;
        this.content = content;
    }

    public long getAdid() {
        return adid;
    }

    public void setAdid(long adid) {
        this.adid = adid;
    }

    public long getDid() {
        return did;
    }

    public void setDid(long did) {
        this.did = did;
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
