package com.iread.bean;

import java.util.*;

/**
 * Created by liu on 16/9/22.
 */
public class Book {
    private Species species;
    private String title;
    private WrapType wrapType;
    private Calendar onloadDate;
    private ArrayList<String> author;
    private ArrayList<String> translator;
    private double star;
    private int commentNum;
    private Category category;
    private String seller;
    private double price;
    private String description;
    private String imgUrl;
    private List<Suggest> buyTogether;
    private List<Suggest> alsoBuy;
    private List<Suggest> visitorBuy;
    private String publisher;
    private int pageNum;
    private String language;
    private int size; //开本
    private String isbn;
    private String barcode;
    private double length;
    private double width;
    private double height;
    private double weight;
    private String brand;
    private String asin;
    private int rankAll;
    private Map<Category, Integer> rankCats;
    private String editorSuggest;
    private String mediaSuggest;
    private String authorIntro;
    private String catalog;
    private String preface;
    private String digest;
    private ArrayList<Integer> starGroups; //5星，4星，...
    private List<Comment> comments;

    public Species getSpecies() {
        return species;
    }

    public void setSpecies(Species species) {
        this.species = species;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public WrapType getWrapType() {
        return wrapType;
    }

    public void setWrapType(WrapType wrapType) {
        this.wrapType = wrapType;
    }

    public Calendar getOnloadDate() {
        return onloadDate;
    }

    public void setOnloadDate(Calendar onloadDate) {
        this.onloadDate = onloadDate;
    }

    public ArrayList<String> getAuthor() {
        return author;
    }

    public void setAuthor(ArrayList<String> author) {
        this.author = author;
    }

    public ArrayList<String> getTranslator() {
        return translator;
    }

    public void setTranslator(ArrayList<String> translator) {
        this.translator = translator;
    }

    public double getStar() {
        return star;
    }

    public void setStar(double star) {
        this.star = star;
    }

    public int getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(int commentNum) {
        this.commentNum = commentNum;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public List<Suggest> getBuyTogether() {
        return buyTogether;
    }

    public void setBuyTogether(List<Suggest> buyTogether) {
        this.buyTogether = buyTogether;
    }

    public List<Suggest> getAlsoBuy() {
        return alsoBuy;
    }

    public void setAlsoBuy(List<Suggest> alsoBuy) {
        this.alsoBuy = alsoBuy;
    }

    public List<Suggest> getVisitorBuy() {
        return visitorBuy;
    }

    public void setVisitorBuy(List<Suggest> visitorBuy) {
        this.visitorBuy = visitorBuy;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getAsin() {
        return asin;
    }

    public void setAsin(String asin) {
        this.asin = asin;
    }

    public int getRankAll() {
        return rankAll;
    }

    public void setRankAll(int rankAll) {
        this.rankAll = rankAll;
    }

    public Map<Category, Integer> getRankCats() {
        return rankCats;
    }

    public void setRankCats(Map<Category, Integer> rankCats) {
        this.rankCats = rankCats;
    }

    public String getEditorSuggest() {
        return editorSuggest;
    }

    public void setEditorSuggest(String editorSuggest) {
        this.editorSuggest = editorSuggest;
    }

    public String getMediaSuggest() {
        return mediaSuggest;
    }

    public void setMediaSuggest(String mediaSuggest) {
        this.mediaSuggest = mediaSuggest;
    }

    public String getAuthorIntro() {
        return authorIntro;
    }

    public void setAuthorIntro(String authorIntro) {
        this.authorIntro = authorIntro;
    }

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public String getPreface() {
        return preface;
    }

    public void setPreface(String preface) {
        this.preface = preface;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public ArrayList<Integer> getStarGroups() {
        return starGroups;
    }

    public void setStarGroups(ArrayList<Integer> starGroups) {
        this.starGroups = starGroups;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
