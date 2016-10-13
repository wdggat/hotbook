package com.iread.bean;

/**
 * Created by liuxiaolong on 16/10/11.
 */
public class BlackWhiteListType {
    public static final int TYPE_CATEGORY = 1;
    public static final int TYPE_BOOK = 2;
    public static final int BLACK = 0;
    public static final int WHITE = 1;

    // 1:CATEGORY,2:BOOK
    private int type;
    //0:black,1:white
    private int blackorwhite;
    private String value;

    public BlackWhiteListType(int type, int blackorwhite, String value) {
        this.type = type;
        this.blackorwhite = blackorwhite;
        this.value = value;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getBlackorwhite() {
        return blackorwhite;
    }

    public void setBlackorwhite(int blackorwhite) {
        this.blackorwhite = blackorwhite;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
