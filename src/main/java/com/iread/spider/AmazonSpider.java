package com.iread.spider;

import com.iread.bean.*;
import com.iread.conf.ConfMan;
import com.iread.parser.AmazonBookParser;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by liu on 16/9/18.
 */
public class AmazonSpider extends Spider {
    private static final Logger logger = Logger.getLogger(AmazonSpider.class);
    public static final String HOST = "https://www.amazon.cn";
//    private static final int MAX_RET_PER_CAT = 1200;
    private static final String CATEGORY_FIRST = "https://www.amazon.cn/%E5%9B%BE%E4%B9%A6/b/ref=sa_menu_top_books_l1?ie=UTF8&node=658390051";
    private ConfMan conf;

    public AmazonSpider(ConfMan conf) {
        this.conf = conf;
    }

    public ArrayList<Category> fetchCategorys() {
        ArrayList<Category> allCats = new ArrayList<Category>();
        try {
            ArrayList<Category> catsLevel1 = fetchCategorysLevel1();
            allCats.addAll(catsLevel1);
            for (Category category : catsLevel1) {
                category.setLeaf(false);
                category.setCat1name(category.getName());
                if (category.getType() == Category.TYPE_NORMAL) {
                    // 2级子类目
                    ArrayList<Category> catsLevel2 = fetchSubCategorys(category);
                    allCats.addAll(catsLevel2);
                    for (Category catLevel2 : catsLevel2) {
                        catLevel2.setCat1name(category.getCat1name());
                        catLevel2.setCat2name(catLevel2.getName());
                        ArrayList<Category> catsLevel3 = fetchSubCategorys(category);
                        allCats.addAll(catsLevel3);
                        if (catsLevel3.isEmpty()) {
                            catLevel2.setLeaf(true);
                        } else {
                            catLevel2.setLeaf(false);
                            for (Category catLevel3 : catsLevel3) {
                                catLevel3.setLeaf(true);
                                catLevel3.setCat1name(catLevel2.getCat1name());
                                catLevel3.setCat2name(catLevel2.getCat2name());
                                catLevel3.setCat3name(catLevel3.getName());
                                Document level3Doc = fetchDocument(catLevel3);
                                String squareSortUrl = AmazonBookParser.getSquareSortUrlFromDoc(level3Doc);
                                catLevel3.setUrl(squareSortUrl);
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            logger.error("fetch categorys failed..", e);
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
            String url = AmazonBookParser.getHrefInElement(el);
            String name = el.text();
            Category cat = new Category(Species.AMAZON, Category.TYPE_HOT, name, "", HOST + url, 0, 1);
            categories.add(cat);
            logger.info("Got category: " + cat.toString());
        }

        //图书
        ArrayList<Category> normalCats = parseLeftCategorySections(document, "图书", 1);
        categories.addAll(normalCats);
        return categories;
    }

    public ArrayList<Category> fetchSubCategorys(Category rootCat) throws IOException {
        ArrayList<Category> subCategorys = new ArrayList<Category>();
        if (rootCat.getType() == Category.TYPE_NORMAL) {
            logger.info("SubCat's url: " + rootCat.getName() + ", " + rootCat.getUrl());
            Document document = fetchDocument(rootCat);
            String squareSortUrl = AmazonBookParser.getSquareSortUrlFromDoc(document);
            rootCat.setUrl(squareSortUrl);
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
            String url = AmazonBookParser.getHrefInElement(el);
            String name = el.getElementsByAttributeValue("class", "refinementLink").text();
            String amountStr = el.getElementsByAttributeValue("class", "narrowValue").text();
            int amount = Integer.parseInt(StringUtils.substringBetween(amountStr, "(", ")").replace(",", ""));
            Category cat = new Category(Species.AMAZON, Category.TYPE_NORMAL, name, parentCategoryName, HOST + url, amount, catLevel);
            categories.add(cat);
            logger.info("Got category: " + cat.toString());
        }
        return categories;
    }

    /**
     * 类目下所有的bookPreview
     * @param category
     * @return
     */
    public ArrayList<BookPreview> fetchBookPreviews(Category category) {
        int pageNum = (category.getAmount() > conf.getAmazonBooknumPerCat()) ?
                (conf.getAmazonBooknumPerCat() / conf.getAmazonBooknumPerSquareSortPage()) :
                (category.getAmount() / conf.getAmazonBooknumPerSquareSortPage() + 1);
        ArrayList<BookPreview> allPres = new ArrayList<BookPreview>();
        for(int page=1; page <= pageNum; page ++) {
            ArrayList<BookPreview> bookPreviews = null;
            try {
                bookPreviews = fetchBookPreviews(category, page);
            } catch (IOException e) {
                logger.error("failed to fetch book previews for " + category.getCatFullName() + " on page " + page, e);
                break;
            }
            allPres.addAll(bookPreviews);
        }
        logger.info("Got " + allPres.size() + " book previews of category1: " + category.getCat1name() + ", category2: " + category.getCat2name() + ", category3: " + category.getCat3name());
        return allPres;
    }

    /**
     *
     * @param category
     * @param page 页数
     * @return 类目下某一页的book previews
     * @throws IOException
     */
    public ArrayList<BookPreview> fetchBookPreviews(Category category, int page) throws IOException {
        ArrayList<BookPreview> bookPreviews = new ArrayList<BookPreview>();
        CategoryPage categoryPage = new CategoryPage(category, page);
        Document document = fetchDocument(categoryPage);
        Elements bookPreviewEls = document.getElementsByAttributeValueStarting("id", "result_");
        int order = (page - 1) * conf.getAmazonBooknumPerSquareSortPage();
        for(Element bookPreviewEl : bookPreviewEls) {
            String asin = bookPreviewEl.attr("data-asin");
            Element titleEl = bookPreviewEl.getElementsByClass("s-access-detail-page").first();
            String title = titleEl.attr("title");
            String onloadTime = bookPreviewEl.getElementsByClass("a-size-small").first().text();
            Element priceEl = bookPreviewEl.getElementsByClass("a-color-price").first();
            String price = priceEl.text();
            Element linkEl = bookPreviewEl.getElementsByClass("a-spacing-mini").last();
            String kindleUrl = null,paperbackUrl = null,hardbackUrl = null;
            for(Element el : linkEl.select("a")) {
                if(el.text().contains("电子书")) {
                    kindleUrl = AmazonBookParser.getHrefInElement(el);
                } else if(el.text().contains("平装")) {
                    paperbackUrl = AmazonBookParser.getHrefInElement(el);
                } else if(el.text().contains("精装")) {
                    hardbackUrl = AmazonBookParser.getHrefInElement(el);
                }
            }
            float star = 0;
            Elements starEls = bookPreviewEl.getElementsByClass("a-icon-star");
            if(starEls.size() > 0) {
                String starStr = starEls.first().text();
                String trimStar = starStr.replace("平均", "").replace("星", "").trim();
                star = Float.parseFloat(trimStar);
            }
            int voteNum = 0;
            Elements reviewEls = bookPreviewEl.getElementsByAttributeValueEnding("href", "customerReviews");
            if(reviewEls.size() > 0) {
                voteNum = Integer.parseInt(reviewEls.last().text().replace(",", ""));
            }
            BookPreview bookPreview = new BookPreview(Species.AMAZON, order, asin, title, onloadTime, kindleUrl, paperbackUrl, hardbackUrl, price, star, voteNum);
            bookPreview.setCategory(category);
            bookPreviews.add(bookPreview);
            order ++;
            logger.debug("Got book preview: " + bookPreview.toString());
        }
        return bookPreviews;
    }

    public Book fetchBook(BookPreview bookPreview) throws IOException {
        Document document = fetchDocument(bookPreview);
        Book book = new Book();
        book.setSpecies(Species.AMAZON);
        book.setTitle(bookPreview.getTitle());
        book.setWrapType(bookPreview.getTopWrapType());
        Element titleEl = document.getElementById("title");
        book.setOnloadDate(AmazonBookParser.getDate(titleEl));
        ArrayList<String> authors = AmazonBookParser.getAuthors(document, "作者");
        ArrayList<String> translators = AmazonBookParser.getAuthors(document, "译");
        book.setAuthor(authors);
        book.setTranslator(translators);
        book.setStar(bookPreview.getStar());
        book.setCommentNum(bookPreview.getVoteNum());
        book.setCategory(bookPreview.getCategory());
        book.setPrice(AmazonBookParser.getPrice(document));
        book.setSeller(AmazonBookParser.getSeller(document));
        book.setDescription(AmazonBookParser.getBookDesc(document));
        book.setBuyTogether(AmazonBookParser.getBuyTogether(document));
        book.setAlsoBuy(AmazonBookParser.getAlsoBuy(document));

        // 基本信息
        Element bucketEl = document.getElementById("detail_bullets_id");
        for(Element liEl : bucketEl.select("li")) {
            String text = liEl.text().trim();
            if (text.startsWith("出版社:")) {
                book.setPublisher(text.replaceFirst("出版社:", "").trim());
            } else if (text.startsWith("平装:") || text.startsWith("精装:")) {
                book.setWrapType(WrapType.getFromName(text.substring(0, 3)));
            } else if (text.startsWith("语种")) {
                String language = text.replaceFirst("语种", "").replace(":", "").replace("：", "");
                book.setLanguage(language);
            } else if (text.startsWith("开本:")) {
                String size = text.replaceFirst("开本:", "").trim();
                book.setSize(Integer.parseInt(size));
            } else if (text.startsWith("ISBN:")) {
                book.setIsbn(text.replaceFirst("ISBN:", "").trim());
            } else if (text.startsWith("条形码:")) {
                book.setBarcode(text.replaceFirst("条形码:", "").trim());
            } else if (text.startsWith("商品尺寸: ")) {
                String sizeStr = text.replaceFirst("商品尺寸: ", "").replace("cm", "").trim();
                String cms[] = sizeStr.split("x");
                book.setLength(Double.parseDouble(cms[0].trim()));
                book.setWidth(Double.parseDouble(cms[1].trim()));
                book.setHeight(Double.parseDouble(cms[2].trim()));
            } else if (text.startsWith("商品重量:")) {
                String weight = text.replaceFirst("商品重量:", "").replace("Kg", "").trim();
                book.setWeight(Double.parseDouble(weight.trim()));
            } else if (text.startsWith("品牌:")) {
                book.setBrand(text.replaceFirst("品牌:", "").trim());
            } else if (text.startsWith("ASIN:")) {
                book.setAsin(text.replace("ASIN:", "").trim());
            }
        }
        book.setRankAll(AmazonBookParser.getRankAll(document));
        book.setRankCats(AmazonBookParser.getRankCats(bucketEl));

        //商品描述
        Element contents = document.getElementById("s_contents");
        for (Element contentEl : contents.getElementsByClass("s-content")) {
            String h3 = contentEl.child(0).text();
            String content = contentEl.child(1).text().trim();
            if(h3.startsWith("编辑推荐")) {
                book.setEditorSuggest(content);
            } else if (h3.startsWith("媒体推荐")) {
                book.setMediaSuggest(content);
            } else if (h3.startsWith("作者简介")) {
                book.setAuthorIntro(content);
            } else if (h3.startsWith("目录")) {
                book.setCatalog(content);
            } else if (h3.startsWith("序言")) {
                book.setPreface(content);
            } else if (h3.startsWith("文摘")) {
                book.setDigest(content);
            }
        }
        book.setStarGroups(AmazonBookParser.getStarGroups(document));
        book.setComments(AmazonBookParser.getComments(document));
        return book;
    }
}
