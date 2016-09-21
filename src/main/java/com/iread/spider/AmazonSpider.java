package com.iread.spider;

import com.iread.bean.Category;
import com.iread.bean.Species;
import com.iread.util.HttpClientVM;
import org.apache.commons.beanutils.locale.LocaleBeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by liu on 16/9/18.
 */
public class AmazonSpider extends Spider {
    private static final Logger logger = Logger.getLogger(AmazonSpider.class);
    public static final String HOST = "https://www.amazon.cn";
    private static final int MAX_RET_PER_CAT = 1200;
    private static final String CATEGORY_FIRST = "https://www.amazon.cn/%E5%9B%BE%E4%B9%A6/b/ref=sa_menu_top_books_l1?ie=UTF8&node=658390051";

    public ArrayList<Category> fetchCategorys() {
        ArrayList<Category> allCats = new ArrayList<Category>();
        ArrayList<Category> catsLevel1 = fetchCategorysLevel1();
        allCats.addAll(catsLevel1);
        for(Category category : catsLevel1) {
            category.setLeaf(false);
            category.setCat1name(category.getName());
            if(category.getType() == Category.TYPE_NORMAL) {
                // 2级子类目
                ArrayList<Category> catsLevel2 = fetchSubCategorys(category);
                allCats.addAll(catsLevel2);
                for(Category catLevel2 : catsLevel2) {
                    catLevel2.setCat1name(category.getCat1name());
                    catLevel2.setCat2name(catLevel2.getName());
                    ArrayList<Category> catsLevel3 = fetchSubCategorys(category);
                    allCats.addAll(catsLevel3);
                    if(catsLevel3.isEmpty()) {
                        catLevel2.setLeaf(true);
                    } else {
                        catLevel2.setLeaf(false);
                        for(Category catLevel3 : catsLevel3) {
                            catLevel3.setLeaf(true);
                            catLevel3.setCat1name(catLevel2.getCat1name());
                            catLevel3.setCat2name(catLevel2.getCat2name());
                            catLevel3.setCat3name(catLevel3.getName());
                        }
                    }
                }
            }
        }
        return allCats;
    }

    public ArrayList<Category> fetchCategorysLevel1() {
        Document document = fetchDocument(CATEGORY_FIRST);
        ArrayList<Category> categories = new ArrayList<Category>();
        // 特色书店, 图书特色排行榜
        Element specialCategoryEl = document.getElementsByAttributeValue("role", "directory").first();
//        String homeCat = StringUtils.substringBetween(specialCategoryEl.html(), "<h2 style=\\\"\\\">", "</h2>");
        for(Element el : specialCategoryEl.getElementsByTag("li")) {
            String url = StringUtils.substringBetween(el.html(), "href=\"", "\"");
            url = url.replaceAll("amp;", "");
            String name = el.text();
            Category cat = new Category(Species.AMAZON, Category.TYPE_HOT, name, "", url, 0, 1);
            categories.add(cat);
            logger.info("Got category: " + cat.toString());
        }

        //图书
        ArrayList<Category> normalCats = parseLeftCategorySections(document, "图书", 1);
        categories.addAll(normalCats);
        return categories;
    }

    public ArrayList<Category> fetchSubCategorys(Category rootCat) {
        ArrayList<Category> subCategorys = new ArrayList<Category>();
        if (rootCat.getType() == Category.TYPE_NORMAL) {
            String url = HOST + rootCat.getUrl();
            logger.info("SubCat's url: " + rootCat.getName() + ", " + url);
            Document document = fetchDocument(url);
//            logger.debug("---------------------------- documents --------------------------------- \n" + document.html());
            ArrayList<Category> subLevels = parseLeftCategorySections(document, rootCat.getName(), rootCat.getLevel() + 1);
            subCategorys.addAll(subLevels);
        }
        return subCategorys;
    }


    /**
     * 解析亚马逊左侧"显示搜索结果"里的类目
     * @param document
     * @param parentCategoryName
     * @return
     */
    public ArrayList<Category> parseLeftCategorySections(Document document, String parentCategoryName, int catLevel) {
        logger.info("Begin to fetch subCategorys of " + parentCategoryName + ", leval " + catLevel);
//        logger.debug("---------------------------- documents --------------------------------- \n" + document.html());
        ArrayList<Category> categories = new ArrayList<Category>();
        Element normalEl = document.getElementsByClass("categoryRefinementsSection").first();
        Elements normalCatEls = normalEl.getElementsByTag("li");
        for(int i=0; i< normalCatEls.size(); i ++) {
            Element el = normalCatEls.get(i);
            if(!el.html().contains("narrowValue")) {
                continue;
            }
            String url = StringUtils.substringBetween(el.html(), "href=\"", "\"");
            url = url.replaceAll("amp;", "");
            String name = el.getElementsByAttributeValue("class", "refinementLink").text();
            String amountStr = el.getElementsByAttributeValue("class", "narrowValue").text();
            int amount = Integer.parseInt(StringUtils.substringBetween(amountStr, "(", ")").replace(",", ""));
            Category cat = new Category(Species.AMAZON, Category.TYPE_NORMAL, name, parentCategoryName, url, amount, catLevel);
            categories.add(cat);
            logger.info("Got category: " + cat.toString());
        }
        return categories;
    }
}
