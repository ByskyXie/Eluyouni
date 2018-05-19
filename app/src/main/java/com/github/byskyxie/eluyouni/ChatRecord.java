package com.github.byskyxie.eluyouni;

import java.util.ArrayList;
import java.util.Date;

public class ChatRecord {
    private long pid;
    private long erid;
    private int ertype;
    private ArrayList<ChatItem> list;

    public ChatRecord() {
    }

    public ChatRecord(long pid, long erid, int ertype, ArrayList<ChatItem> list) {
        this.pid = pid;
        this.erid = erid;
        this.ertype = ertype;
        this.list = list;
    }

    public void addChatItem(ChatItem item){
        if(list == null)
            list = new ArrayList<>();
        if(item == null)
            return;
        list.add(item);
    }

    public void addChatItem(ArrayList<ChatItem> list){
        if(this.list == null )
            this.list = new ArrayList<>();
        if(list == null)
            return;
        this.list.addAll(list);
    }

    public void clearChatItem(){
        if(list == null)
            return;
        list.clear();
    }

    public long getPid() {
        return pid;
    }

    public void setPid(long pid) {
        this.pid = pid;
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

    public ArrayList<ChatItem> getList() {
        return list;
    }

    public void setList(ArrayList<ChatItem> list) {
        this.list = list;
    }
}
