package com.iread.bean;

import com.alibaba.fastjson.JSON;

/**
 * Created by liu on 16/9/23.
 */
public class BookPreview extends Storable{
    private Species species;
    private String asin;
    private String title;
    private String onloadTime;
    private String kindleUrl;
    private String paperbackUrl;
    private String hardbackUrl;
    private String price;
    private Float star;
    private int voteNum;
    private int order;

    public BookPreview(Species species, int order, String asin, String title, String onloadTime, String kindleUrl, String paperbackUrl, String hardbackUrl, String price, Float star, int voteNum) {
        this.species = species;
        this.order = order;
        this.asin = asin;
        this.title = title;
        this.onloadTime = onloadTime;
        this.kindleUrl = kindleUrl;
        this.paperbackUrl = paperbackUrl;
        this.hardbackUrl = hardbackUrl;
        this.price = price;
        this.star = star;
        this.voteNum = voteNum;
    }

    public Species getSpecies() {
        return species;
    }

    public void setSpecies(Species species) {
        this.species = species;
    }

    public String getAsin() {
        return asin;
    }

    public void setAsin(String asin) {
        this.asin = asin;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOnloadTime() {
        return onloadTime;
    }

    public void setOnloadTime(String onloadTime) {
        this.onloadTime = onloadTime;
    }

    public String getKindleUrl() {
        return kindleUrl;
    }

    public void setKindleUrl(String kindleUrl) {
        this.kindleUrl = kindleUrl;
    }

    public Float getStar() {
        return star;
    }

    public void setStar(Float star) {
        this.star = star;
    }

    public int getVoteNum() {
        return voteNum;
    }

    public void setVoteNum(int voteNum) {
        this.voteNum = voteNum;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getPaperbackUrl() {
        return paperbackUrl;
    }

    public void setPaperbackUrl(String paperbackUrl) {
        this.paperbackUrl = paperbackUrl;
    }

    public String getHardbackUrl() {
        return hardbackUrl;
    }

    public void setHardbackUrl(String hardbackUrl) {
        this.hardbackUrl = hardbackUrl;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    @Override
    public String getStoreFilename() {
        return "bp_" + species + "_" + title + "_" + asin;
    }

    /**
     * 接优先级取一个url, 精装，平装，kindle
     * @return
     */
    @Override
    public String getUrl() {
        if(hardbackUrl != null) {
            return hardbackUrl;
        } else if(paperbackUrl != null) {
            return paperbackUrl;
        } else if(kindleUrl != null) {
            return kindleUrl;
        }
        return null;
    }
}
