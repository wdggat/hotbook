package com.iread.bean;

/**
 * Created by liu on 16/10/4.
 */
public class BookDescription extends Storable {
    private String editorSuggest;
    private String celebritySuggest;
    private String mediaSuggest;
    private String authorIntro;
    private String catalog;
    private String preface;
    private String digest;
    private String url;
    private Book book;

    public BookDescription(Book book, String url) {
        this.book = book;
        this.url = url;
    }

    public String getEditorSuggest() {
        return editorSuggest;
    }

    public void setEditorSuggest(String editorSuggest) {
        this.editorSuggest = editorSuggest;
    }

    public String getCelebritySuggest() {
        return celebritySuggest;
    }

    public void setCelebritySuggest(String celebritySuggest) {
        this.celebritySuggest = celebritySuggest;
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

    public String getStoreFilename() {
        return "bookdesc_" + book.getSpecies() + "_" + book.getTitle() + "_" + book.getAsin();
    }

    public String getUrl() {
        return url;
    }

    public Species getSpecies() {
        return book.getSpecies();
    }

    @Override
    public int getMinSize() {
        return 2000;
    }

    @Override
    public String toString() {
        return "BookDescription{" +
                "editorSuggest='" + editorSuggest + '\'' + "\n" +
                ", celebritySuggest='" + celebritySuggest + '\'' + "\n" +
                ", mediaSuggest='" + mediaSuggest + '\'' + "\n" +
                ", authorIntro='" + authorIntro + '\'' + "\n" +
                ", catalog='" + catalog + '\'' + "\n" +
                ", preface='" + preface + '\'' + "\n" +
                ", digest='" + digest + '\'' + "\n" +
                ", url='" + url + '\'' + "\n" +
                ", book=" + book + "\n" +
                '}';
    }
}
