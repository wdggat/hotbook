package com.iread.spider;

import com.iread.bean.Category;
import com.iread.util.HttpClientVM;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by liu on 16/9/18.
 */
public abstract class Spider {
    public static HttpClientVM clientVM = new HttpClientVM();

    public abstract ArrayList<Category> fetchCategorys();

    protected static Document fetchDocument(String url) {
        String content = clientVM.get(url);
        return Jsoup.parse(content);
    }
}
