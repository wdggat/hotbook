package com.iread.bean;

/**
 * Created by liu on 16/9/30.
 */
public enum WrapType {
    /**
     * 平装，硬装，kindle
     */
    PAPERBACK, HARDBACK, KINDLE, LEATHER;

    public static WrapType getFromName(String name) {
        if (name.contains("平装") || name.contains("简装")) {
            return WrapType.PAPERBACK;
        } else if (name.contains("精装")) {
            return WrapType.HARDBACK;
        } else if (name.contains("Kindle")) {
            return WrapType.KINDLE;
        } else if (name.contains("皮质")) {
            return WrapType.LEATHER;
        }
        return null;
    }
}
