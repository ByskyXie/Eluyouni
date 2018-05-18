package com.github.byskyxie.eluyouni;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ChatItem {
    public static final int CHAT_TYPE_SELF = 0X001;
    public static final int CHAT_TYPE_OTHER_SIDE = 0X002;

    private String content;
    private int chatType;
    private Date time;
    private String icon;

    ChatItem(){}

    public ChatItem(String content, int chatType, Date time, String icon) {
        this.content = content;
        this.chatType = chatType;
        this.time = time;
        this.icon = icon;
    }

    public String getDayTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.CHINA);
        return sdf.format(time);
    }

    public String getMonthTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("M-d HH:mm", Locale.CHINA);
        return sdf.format(time);
    }

    public String getFullTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d HH:mm", Locale.CHINA);
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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
