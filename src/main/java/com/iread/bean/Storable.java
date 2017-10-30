package com.iread.bean;

/**
 * Created by liu on 16/9/25.
 */
public abstract class Storable {
    public abstract String getStoreFilename();
    public abstract String getUrl();
    public abstract Species getSpecies();
    public abstract int getMinSize();
}
