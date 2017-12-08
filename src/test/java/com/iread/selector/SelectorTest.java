package com.iread.selector;

import com.iread.bean.Book;
import com.iread.bean.Species;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by liuxiaolong on 17/12/6.
 */
public class SelectorTest {
    @Test
    public void testSelectAbook() {
        Selector selector = new Selector();
        Book book = selector.selectABook(Species.AMAZON);
        Assert.assertNotNull(book);
        System.out.println(book);
    }
}
