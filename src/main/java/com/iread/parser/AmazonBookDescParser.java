package com.iread.parser;

import com.iread.bean.BookDescription;
import com.iread.spider.AmazonSpider;
import com.iread.spider.Spider;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

/**
 * Created by liu on 16/10/4.
 */
public class AmazonBookDescParser {
    public static void parseBookDesc(BookDescription desc) throws IOException {
        Document document = Spider.fetchDocument(desc);
        Element contents = document.getElementById("s_contents");
        for (Element contentEl : contents.getElementsByClass("s-content")) {
            String h3 = contentEl.child(0).text();
            String content = contentEl.child(1).text().trim();
            if(h3.startsWith("编辑推荐")) {
                desc.setEditorSuggest(content);
            } else if (h3.startsWith("媒体推荐")) {
                desc.setMediaSuggest(content);
            } else if (h3.startsWith("名人推荐")) {
                desc.setCelebritySuggest(content);
            } else if (h3.startsWith("作者简介")) {
                desc.setAuthorIntro(content);
            } else if (h3.startsWith("目录")) {
                desc.setCatalog(content);
            } else if (h3.startsWith("序言")) {
                desc.setPreface(content);
            } else if (h3.startsWith("文摘")) {
                desc.setDigest(content);
            }
        }
    }
}
