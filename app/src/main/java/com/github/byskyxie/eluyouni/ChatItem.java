package com.github.byskyxie.eluyouni;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ChatItem {
    public static final int CHAT_TYPE_SELF = 0X001;
    public static final int CHAT_TYPE_OTHER_SIDE = 0X002;
    public static final int CHAT_TYPE_SYS= 0X003;

    private String content;
    private int chatType;
    private Date time;
    private long erid;
    private int ertype;

    ChatItem(){}

    ChatItem(String content, int chatType, Date time, long erid, int ertype) {
        this.content = content;
        this.chatType = chatType;
        this.time = time;
        this.erid = erid;
        this.ertype = ertype;
    }

    public String getDayTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.CHINA);
        return sdf.format(time);
    }

    public String getMonthTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("M-d HH:mm", Locale.CHINA);
        return sdf.format(time);
    }

    public String getFormatTime(){
        SimpleDateFormat sdf = new SimpleDateFormat(BaseActivity.DATE_FORMAT, Locale.CHINA);
        return sdf.format(time);
    }

    public long getRealTime(){ return time.getTime();}

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getChatType() {
        return chatType;
    }

    public void setChatType(int chatType) {
        this.chatType = chatType;
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
}
