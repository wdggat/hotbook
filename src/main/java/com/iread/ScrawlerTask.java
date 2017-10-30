package com.iread;

import com.iread.bean.Book;
import com.iread.bean.BookPreview;
import com.iread.bean.Category;
import com.iread.bean.Species;
import com.iread.conf.ConfMan;
import com.iread.spider.Spider;
import com.iread.util.CategoryHelper;
import com.iread.util.Exporter;
import com.iread.util.Importer;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class ScrawlerTask implements Runnable {
    private Logger logger = Logger.getLogger(ScrawlerTask.class);
    private int searchRounds;

    private ConfMan conf;
    private Spider spider;
    private Species species;

    public ScrawlerTask(ConfMan conf, Spider spider, Species species) {
        this.conf = conf;
        this.searchRounds = 1;
        this.spider = spider;
        this.species = species;
    }

    public void run() {
        try {
            logger.info("start round " + searchRounds);
            // Reload configurations that changes at runtime
            conf.reload();
            CategoryHelper.init(species);
            List<Category> categories = Importer.readCategorys(species);
            logger.info("Read " + categories.size() + " categories");
            for (Category category : categories) {
                logger.info("begin category : " + category);
                ArrayList<Book> books = new ArrayList<Book>();
                ArrayList<BookPreview> previews = spider.fetchBookPreviews(category);
                for (BookPreview bookPreview : previews) {
                    logger.debug("begin to get book of : " + bookPreview.toJsonStr());
                    Book book = spider.fetchBook(bookPreview);
                    book.setCategory(category);
                    books.add(book);
                    logger.info(book.getSpecies() + " " + category.getCatFullName() + " fetched book : " + book.getTitle());
                    if (books.size() == 200) {
                        Exporter.exportBooks(books);
                        logger.info(category.getSpecies() + " " + category.getCatFullName() + " exported books num : " + books.size());
                        books.clear();
                    }
                }
                Exporter.exportBooks(books);
                logger.info(category.getSpecies() + " " + category.getCatFullName() + " exported books num : " + books.size());
                return;
            }
        } catch (Throwable e) {
            logger.error("Something wrong happens during spider running:",
                    e);
        }
    }
}
