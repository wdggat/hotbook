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
        Document document = Spider.fetchDocument(desc, false);
        Element contents = document.getElementById("s_contents");
        for (Element contentEl : contents.getElementsByClass("s-content")) {
            String h3 = contentEl.child(0).text();
            String content = contentEl.text().trim();
            if(h3.startsWith("编辑推荐")) {
                content = content.substring(4);
                desc.setEditorSuggest(content.trim());
            } else if (h3.startsWith("媒体推荐")) {
                content = content.substring(4);
                desc.setMediaSuggest(content.trim());
            } else if (h3.startsWith("名人推荐")) {
                content = content.substring(4);
                desc.setCelebritySuggest(content.trim());
            } else if (h3.startsWith("作者简介")) {
                content = content.substring(4);
                desc.setAuthorIntro(content.trim());
            } else if (h3.startsWith("目录")) {
                content = content.substring(2);
                desc.setCatalog(content.trim());
            } else if (h3.startsWith("序言")) {
                content = content.substring(2);
                desc.setPreface(content.trim());
            } else if (h3.startsWith("文摘")) {
                content = content.substring(2);
                desc.setDigest(content.trim());
            }
        }
    }
}
