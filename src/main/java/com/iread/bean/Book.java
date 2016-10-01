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
    private String poster;
    private List<Suggest> suggests;
    private String publisher;
    private int pageNum;
    private String language;
    private int size; //开本
    private String isbn;
    private String barcode;
    private double length;
    private double width;
    private double thickness;
    private double weight;
    private String brand;
    private String asin;
    private int orderAll;
    private Map<Category, Integer> orderCats;
    private String editorSuggest;
    private String mediaSuggest;
    private String authorIntro;
    private String catalog;
    private String preface;
    private String digest;
    private Map<Integer, Integer> starGroups;
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

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public List<Suggest> getSuggests() {
        return suggests;
    }

    public void setSuggests(List<Suggest> suggests) {
        this.suggests = suggests;
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

    public double getThickness() {
        return thickness;
    }

    public void setThickness(double thickness) {
        this.thickness = thickness;
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

    public int getOrderAll() {
        return orderAll;
    }

    public void setOrderAll(int orderAll) {
        this.orderAll = orderAll;
    }

    public Map<Category, Integer> getOrderCats() {
        return orderCats;
    }

    public void setOrderCats(Map<Category, Integer> orderCats) {
        this.orderCats = orderCats;
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

    public Map<Integer, Integer> getStarGroups() {
        return starGroups;
    }

    public void setStarGroups(Map<Integer, Integer> starGroups) {
        this.starGroups = starGroups;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
