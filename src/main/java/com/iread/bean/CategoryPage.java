package com.iread.bean;

/**
 * Created by liu on 16/9/26.
 */
public class CategoryPage extends Storable {
    private int page;
    private Category category;
    public void setPage(int page) {
        this.page = page;
    }

    public int getPage() {
        return page;
    }

    public CategoryPage(Category category, int page) {
        this.category = category;
        this.page = page;
    }

    public String getStoreFilename() {
        return category.getStoreFilename() + "_page-" + page;
    }

    public String getUrl() {
        return category.getUrl() + "&page=" + page;
    }

    public Species getSpecies() {
        return category.getSpecies();
    }

}
