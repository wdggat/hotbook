package com.iread.parser;

import com.iread.bean.*;
import com.iread.spider.AmazonSpider;
import org.apache.commons.beanutils.converters.IntegerArrayConverter;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by liu on 16/10/1.
 */
public class AmazonBookParser extends SpiderParser {
    public static Calendar getDate(Element titleEl) {
        String dateStr = titleEl.getElementsContainingText("年").last().text();
        return parseDate(dateStr);
    }

    public static Calendar parseDate(String dateStr) {
        Calendar date = null;
        Pattern pattern = Pattern.compile("([0-9]+)年([0-9]+)月([0-9]+)");
        Matcher matcher = pattern.matcher(dateStr);
        if(matcher.matches()) {
            date = Calendar.getInstance();
            int year = Integer.parseInt(matcher.group(1));
            int month = Integer.parseInt(matcher.group(2));
            int day = Integer.parseInt(matcher.group(3));
            date.set(year, month, day, 0, 0, 0);
        }
        return date;
    }

    public static ArrayList<String> getAuthors(Document document, String flag) {
        ArrayList<String> authors = new ArrayList<String>();
        Elements authorEls = document.getElementsByClass("author");
        for(Element authorEl : authorEls) {
            String author = authorEl.select("a").first().text();
            Element contributionEl = authorEl.getElementsByClass("contribution").first();
            if(contributionEl.toString().contains(flag)) {
                authors.add(author);
            }
        }
        return authors;
    }

    public static double getPrice(Document document) {
        Element priceEl = document.getElementsByClass("a-color-price").first();
        String priceStr = priceEl.text().replace("￥", "");
        return Double.parseDouble(priceStr.trim());
    }

    public static String getSeller(Document document) {
        Elements sellerEls = document.getElementById("ddmMerchantMessage").select("a");
        if (!sellerEls.isEmpty()) {
            return sellerEls.first().text();
        }
        return "亚马逊";
    }

    public static String getBookDesc(Document document) {
        Element bookDescEl = document.getElementById("bookDesc_iframe");
        Element contentEl = bookDescEl.getElementById("iframeContent");
        return contentEl.text();
    }

    public static String getImgUrl(Document document) {
        Element imgEl = document.getElementById("img-canvas");
        Element srcEl = imgEl.select("img").first();
        return srcEl.attr("src");
    }

    public static String getHrefInElement(Element el) {
        String html = el.toString();
        String url = StringUtils.substringBetween(html, "href=\"", "\"");
        return url.replaceAll("amp;", "");
    }

    /**
     * 购买此商品的顾客也同时购买，此处有星级打分
     * @param document
     * @return
     */
    public static ArrayList<Suggest> getAlsoBuy(Document document) {
        ArrayList<Suggest> alsoBuySuggests = new ArrayList<Suggest>();
        Elements alsoBuyEls = document.getElementsByClass("a-carousel-card");
        for (Element el : alsoBuyEls) {
            Suggest alsoBuySuggest = new Suggest();
            alsoBuySuggest.setSuggestType(SuggestType.ALSOBUY);
            String url = AmazonSpider.HOST + getHrefInElement(el.select("a").first());
            Element imgEl = el.select("img").first();
            alsoBuySuggest.setTitle(imgEl.attr("alt"));
            alsoBuySuggest.setUrl(url);
            alsoBuySuggest.setImgUrl(imgEl.attr("src"));
            Elements spans = el.select("span");
            alsoBuySuggest.setAuthor(spans.first().text());
            String wrap = spans.get(spans.size() - 2).text();
            alsoBuySuggest.setWrapType(WrapType.getFromName(wrap));
            String price = spans.last().text();
            alsoBuySuggest.setPrice(priceCast(price));

            Elements iconEls = el.getElementsByClass("a-icon-row");
            if(!iconEls.isEmpty()) {
                String starStr = iconEls.first().select("span").first().text();
                alsoBuySuggest.setStar(starClean(starStr));
                Element lastA = iconEls.first().select("a").last();
                alsoBuySuggest.setCommentNum(Integer.parseInt(lastA.text()));
            }
            alsoBuySuggests.add(alsoBuySuggest);
        }
        return alsoBuySuggests;
    }

    /**
     * 经常一起买,此处不会有星级打分
     */
    public static ArrayList<Suggest> getBuyTogether(Document document) {
        //经常一起买
        ArrayList<Suggest> buyTogether = new ArrayList<Suggest>();
        Element imageUl = document.getElementsByClass("sims-fbt-image-box").first();
        Elements imgEls = imageUl.select("img");
        for(int i = 1; i< imgEls.size(); i ++) {
            Suggest suggest = new Suggest();
            suggest.setSuggestType(SuggestType.BUYTOGETHER);
            Element imgEl = imgEls.get(i);
            suggest.setTitle(imgEl.attr("alt"));
            suggest.setImgUrl(imgEl.attr("src"));
            buyTogether.add(suggest);
        }

        Elements labelEls = document.getElementsByClass("sims-fbt-checkbox-label");
        for(int i = 1; i < labelEls.size(); i ++) {
            Element labelEl = labelEls.get(i);
            Element hrefEl = labelEl.select("a").first();
            String url = AmazonSpider.HOST + getHrefInElement(hrefEl);
            buyTogether.get(i - 1).setUrl(url);
            Elements spanEls = labelEl.select("span");
            buyTogether.get(i - 1).setPrice(priceCast(spanEls.last().text()));
            buyTogether.get(i - 1).setWrapType(WrapType.getFromName(spanEls.get(spanEls.size() - 2).text()));
            buyTogether.get(i - 1).setAuthor(authorClean(spanEls.get(spanEls.size() - 3).text()));
        }
        return buyTogether;
    }

    /**
     * 看过此商品后顾客买的其它商品？
     * @param document
     * @return 先给空，感觉用处不大..
     */
    public static ArrayList<Suggest> getVisitorBuy(Document document) {
        return new ArrayList<Suggest>();
    }

    private static double starClean(String starStr) {
        String clean = starStr.replace("平均", "").replace("星", "");
        return Double.parseDouble(clean.trim());
    }


    public static String getSquareSortUrlFromDoc(Document document) {
        Element squareSortEl = document.getElementsByAttributeValue("title", "图像视图").first();
        String squareSortUrl = getHrefInElement(squareSortEl);
        return squareSortUrl;
    }

    public static int getRankAll(Document document) {
        Element rankEl = document.getElementById("SalesRank");
        String randStr = StringUtils.substringBetween(rankEl.text(), "图书商品里排第", "名");
        return Integer.parseInt(randStr);
    }

    public static HashMap<Category, Integer> getRankCats(Element detailBulletsEl) {
        HashMap<Category, Integer> rankCats = new HashMap<Category, Integer>();
        Elements rankCatEls = detailBulletsEl.getElementsByClass("zg_hrsr_item");
        for(Element rankCatEl : rankCatEls) {
            String rankText = rankCatEl.select("span").first().text();
            String rankStr = StringUtils.substringBetween(rankText, "第", "位");
            Integer rank = Integer.parseInt(rankStr);
            Elements aEls = rankCatEl.select("a");
            String cat1 = aEls.get(1).text();
            String cat2 = aEls.get(2).text();
            String cat3 = aEls.get(3).text();
            String url = AmazonBookParser.getHrefInElement(aEls.get(3));
            Category category = new Category();
            category.setSpecies(Species.AMAZON);
            category.setCat1name(cat1);
            category.setCat2name(cat2);
            category.setCat3name(cat3);
            category.setUrl(url);
            category.setType(Category.TYPE_NORMAL);
            rankCats.put(category, rank);
        }
        return rankCats;
    }

    public static ArrayList<Integer> getStarGroups(Document document) {
        ArrayList<Integer> starGroups = new ArrayList<Integer>();
        Element histogramEl = document.getElementById("histogramTable");
        for(Element histogramRow : histogramEl.getElementsByClass("a-histogram-row")) {
            Element commentNumEl = histogramRow.getElementsByClass("a-nowrap").last();
            starGroups.add(Integer.parseInt(commentNumEl.text()));
        }
        return starGroups;
    }

    public static ArrayList<Comment> getComments(Document document) {
        ArrayList<Comment> comments = new ArrayList<Comment>();
        Element parentEl = document.getElementById("revMH");
        for(Element commentEl : parentEl.getElementsByAttributeValueStarting("id", "rev-")) {
            Comment comment = new Comment();
            comment.setId(commentEl.id());
            Element iconEl = commentEl.getElementsByClass("a-icon-star").first();
            String starS = StringUtils.substringBetween(iconEl.text(), "平均", ".0");
            comment.setStar(Integer.parseInt(starS));
            Element titleEl = iconEl.parent().nextElementSibling();
            comment.setTitle(titleEl.text());
            Element commentatorEl = titleEl.parent().nextElementSibling();
            String commentator = StringUtils.substringBetween(commentatorEl.toString(), "noTextDecoration\">", "</a>");
            comment.setAuthor(commentator);
            String dateStr = StringUtils.substringBetween(commentatorEl.toString(), "于", "</span>");
            comment.setDate(parseDate(dateStr.trim()));
            Element revDataEl = commentEl.getElementsByAttributeValueStarting("id", "revData-").first();
            String content = revDataEl.getElementsByClass("a-section").last().text();
            comment.setContent(content);
            Elements praiseEls = commentEl.getElementsByClass("cr-vote-buttons");
            if(!praiseEls.isEmpty()) {
                String praiseS = StringUtils.substringBefore(praiseEls.text(), "个人发现此评论有用");
                comment.setPraise(Integer.parseInt(praiseS.trim()));
            }
            comments.add(comment);
        }
        return comments;
    }

}
