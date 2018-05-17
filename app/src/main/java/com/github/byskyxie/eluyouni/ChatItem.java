package com.github.byskyxie.eluyouni;

public class ChatItem {
    public static final int CHAT_TYPE_SELF = 0X001;
    public static final int CHAT_TYPE_OTHER_SIDE = 0X002;

    private String content;
    private int chatType;
    private String icon;
}
