package com.iread.bean;

import com.alibaba.fastjson.JSON;

/**
 * Created by liu on 16/9/19.
 */
public class Category extends Storable{
    private Species species;
    /**
     * 类目种类，普通图书分类:0 畅销榜: 1
     */
    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_HOT = 1;
    private int type;
    /**
     * 类目名称
     */
    private String name;
    /**
     * 父类目
     */
    private String parent;
    private String url;
    /**
     * 类目下书目总数
     */
    private int amount;
    /**
     * 类目级别
     */
    private int level;
    private boolean isLeaf;
    private String cat1name;
    private String cat2name;
    private String cat3name;
    private int categoryid;

    public Category() {}

    public Category(Species species, int type, String name, String parent, String url, int amount, int level) {
        this.species = species;
        this.type = type;
        this.name = name;
        this.parent = parent;
        this.url = url;
        this.amount = amount;
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Species getSpecies() {
        return species;
    }

    public void setSpecies(Species species) {
        this.species = species;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isLeaf() {
        return isLeaf;
    }

    public void setLeaf(boolean leaf) {
        isLeaf = leaf;
    }

    public String getCat1name() {
        return cat1name;
    }

    public void setCat1name(String cat1name) {
        this.cat1name = cat1name;
    }

    public String getCat2name() {
        return cat2name;
    }

    public void setCat2name(String cat2name) {
        this.cat2name = cat2name;
    }

    public String getCat3name() {
        return cat3name;
    }

    public void setCat3name(String cat3name) {
        this.cat3name = cat3name;
    }

    public String getCatFullName() {
        return cat1name + "|" + cat2name + "|" + (cat3name == null ? "" : cat3name);
    }

    public int getCategoryid() {
        return categoryid;
    }

    public void setCategoryid(int categoryid) {
        this.categoryid = categoryid;
    }

    @Override
    public int getMinSize() {
        return 1000;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    public String getStoreFilename() {
        String filename = null;
        if (cat2name == null) {
            filename = "cat_" + species + "_" + cat1name;
        } else if (cat3name == null) {
            filename = "cat_" + species + "_" + cat1name + "_" + cat2name;
        } else {
            filename = "cat_" + species + "_" + cat1name + "_" + cat2name + "_" + cat3name;
        }
        return filename;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Category category = (Category) o;

        if (type != category.type) return false;
        if (species != category.species) return false;
        if (!cat1name.equals(category.cat1name)) return false;
        if (cat2name != null ? !cat2name.equals(category.cat2name) : category.cat2name != null) return false;
        return cat3name != null ? cat3name.equals(category.cat3name) : category.cat3name == null;

    }

    @Override
    public int hashCode() {
        int result = species.hashCode();
        result = 31 * result + type;
        result = 31 * result + cat1name.hashCode();
        result = 31 * result + (cat2name != null ? cat2name.hashCode() : 0);
        result = 31 * result + (cat3name != null ? cat3name.hashCode() : 0);
        return result;
    }
}
