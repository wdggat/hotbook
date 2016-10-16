package com.iread.parser;

import com.iread.bean.*;
import com.iread.spider.AmazonSpider;
import com.iread.util.CategoryHelper;
import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.*;
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
//        System.out.println(document.toString());
        Element contentEl = document.getElementById("bookDescription_feature_div");
        return contentEl.text();
    }

    public static List<String> getImgUrls(Document document) {
        Element imgEl = document.getElementById("imgThumbs");
        List<String> imgUrls = new ArrayList<String>();
        for(Element el : imgEl.select("img")) {
            imgUrls.add(el.attr("src"));
        }
        return imgUrls;
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
            String asin = getAsinFromUrl(url);
            alsoBuySuggest.setAsin(asin);
            alsoBuySuggest.setImgUrl(imgEl.attr("src"));
            Elements spans = el.select("span");
            alsoBuySuggest.setAuthor(spans.first().text());
            Element wrapEl = el.getElementsByClass("a-size-small").last();
            String wrap = wrapEl.text();
            alsoBuySuggest.setWrapType(WrapType.getFromName(wrap));
            Element priceEl = el.getElementsByClass("a-color-price").first();
            if (priceEl != null) {
                String price = priceEl.text();
                alsoBuySuggest.setPrice(priceCast(price));
            }

            Elements iconEls = el.getElementsByClass("a-icon-row");
            if(!iconEls.isEmpty()) {
                String starStr = iconEls.first().select("span").first().text();
                alsoBuySuggest.setStar(starClean(starStr));
                Element lastA = iconEls.first().select("a").last();
                alsoBuySuggest.setCommentNum(parseInt(lastA.text()));
            }
            alsoBuySuggests.add(alsoBuySuggest);
        }
        return alsoBuySuggests;
    }

    /**
     * 经常一起买,此处不会有星级打分
     */
    public static ArrayList<Suggest> getBuyTogether(Document document) {
        ArrayList<Suggest> buyTogether = new ArrayList<Suggest>();
        Element imageUl = document.getElementsByClass("sims-fbt-image-box").first();
        if(imageUl == null) {
            return buyTogether;
        }
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
            String asin = getAsinFromUrl(url);
            buyTogether.get(i - 1).setAsin(asin);
            buyTogether.get(i - 1).setUrl(url);
            Elements spanEls = labelEl.select("span");
            buyTogether.get(i - 1).setPrice(priceCast(spanEls.get(2).text()));
            buyTogether.get(i - 1).setWrapType(WrapType.getFromName(spanEls.get(1).text()));
            buyTogether.get(i - 1).setAuthor(authorClean(spanEls.get(0).text()));
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
        if (squareSortEl == null) {
            return null;
        }
        String squareSortUrl = getHrefInElement(squareSortEl);
        return squareSortUrl;
    }

    public static int getRankAll(Document document) {
        Element rankEl = document.getElementById("SalesRank");
        String rankStr = StringUtils.substringBetween(rankEl.text(), "商品里排第", "名");
        rankStr = rankStr.replace(",", "");
        return parseInt(rankStr);
    }

    public static HashMap<Category, Integer> getRankCats(Element detailBulletsEl) {
        HashMap<Category, Integer> rankCats = new HashMap<Category, Integer>();
        Elements rankCatEls = detailBulletsEl.getElementsByClass("zg_hrsr_item");
        for(Element rankCatEl : rankCatEls) {
            String rankText = rankCatEl.select("span").first().text();
            String rankStr = StringUtils.substringBetween(rankText, "第", "位");
            Integer rank = parseInt(rankStr);
            Elements aEls = rankCatEl.select("a");
            for (int i = 0; i< aEls.size(); i++) {
                String text = aEls.get(i).text();
                if (text.contains("Kindle") || text.contains("kindle") || text.equals("图书")) {
                    aEls.remove(i);
                }
            }
            String cat1 = aEls.get(0).text();
            String cat2 = (aEls.size() >= 2) ? aEls.get(1).text() : null;
            String cat3 = (aEls.size() >= 3) ? aEls.get(2).text() : null;
            String url = AmazonBookParser.getHrefInElement(aEls.last());
            Category category = new Category();
            category.setSpecies(Species.AMAZON);
            category.setCat1name(cat1);
            category.setCat2name(cat2);
            category.setCat3name(cat3);
            category.setUrl(url);
            category.setType(Category.TYPE_NORMAL);
            Category catInDb = CategoryHelper.getCategoryByName(category.getCatFullName());
            rankCats.put(catInDb, rank);
        }
        return rankCats;
    }

    public static ArrayList<Integer> getStarGroups(Document document) {
        ArrayList<Integer> starGroups = new ArrayList<Integer>();
        Element histogramEl = document.getElementById("histogramTable");
        for(Element histogramRow : histogramEl.getElementsByClass("a-histogram-row")) {
            Element commentNumEl = histogramRow.getElementsByClass("a-nowrap").last();
            String star = StringUtils.isBlank(commentNumEl.text()) ? "0" : commentNumEl.text();
            starGroups.add(parseInt(star));
        }
        return starGroups;
    }

    public static ArrayList<Comment> getComments(Document document) {
        ArrayList<Comment> comments = new ArrayList<Comment>();
        Element parentEl = document.getElementById("revMH");
        if (parentEl == null) {
            return comments;
        }
        for(Element commentEl : parentEl.getElementsByAttributeValueStarting("id", "rev-")) {
            Comment comment = new Comment();
            comment.setId(commentEl.id());
            Element iconEl = commentEl.getElementsByClass("a-icon-star").first();
            String starS = StringUtils.substringBetween(iconEl.text(), "平均", ".0");
            comment.setStar(parseInt(starS));
            Element titleEl = commentEl.getElementsByClass("a-icon-row").select("span").last();
            comment.setTitle(titleEl.text());
            Element commentatorEl = commentEl.getElementsByAttributeValueContaining("href", "profile.amazon").first();
            String commentator = commentatorEl.text();
            comment.setAuthor(commentator);
            Element dateEl = commentEl.getElementsMatchingOwnText(Pattern.compile("([0-9]+)年([0-9]+)月([0-9]+)日")).first();
            String dateStr = StringUtils.substringBetween(dateEl.toString(), "于", "</span>");
            comment.setDate(parseDate(dateStr.trim()));
            Element revDataEl = commentEl.getElementsByAttributeValueStarting("id", "revData-").first();
            String content = revDataEl.getElementsByClass("a-section").last().text();
            comment.setContent(content);
            Elements praiseEls = commentEl.getElementsByClass("cr-vote-buttons");
            if(!praiseEls.isEmpty()) {
                String praiseS = StringUtils.substringBetween(praiseEls.text(), "", "个人发现此评论有用");
                comment.setPraise(praiseS == null ? 0 : parseInt(praiseS));
            }
            comments.add(comment);
        }
        return comments;
    }

    public static String getPosterUrl(Document document) {
        Element descEl = document.getElementById("bookDescription_feature_div");
        if (descEl == null) {
            return null;
        }
        Element imgEl = descEl.select("img").first();
        if (imgEl == null) {
            return null;
        }
        return imgEl.attr("src");
    }

    public static String getAsinFromUrl(String url) {
        return StringUtils.substringBetween(url, "/dp/", "/");
    }

    public static double getWeight(String text) {
        if (text.contains("Kg")) {
            String weight = text.replaceFirst("商品重量:", "").replace("Kg", "").trim();
            return Double.parseDouble(weight);
        } else if (text.contains("g")) {
            String weight = text.replaceFirst("商品重量:", "").replace("g", "").trim();
            return Double.parseDouble("0." + weight);
        }
        return 0;
    }

    /*public static String getImgUrl(Document document) {
        Element imgEl = document.getElementById("imgBlkFront");
        return imgEl.attr("src");
    }*/

}
