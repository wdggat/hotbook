package com.iread.util;

import com.iread.bean.Category;
import com.iread.bean.Species;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Created by liuxiaolong on 16/10/11.
 */
@Ignore
public class WBListHelperTest {
    @BeforeClass
    public static void setUp() {
        WBListHelper.init();
    }

    @Test
    public void testCheckCategoryTrue() {
        String url = "https://www.amazon.cn/s/ref=lp_658495051_il_ti_stripbooks?rh=n%3A658390051%2Cn%3A%21658391051%2Cn%3A658393051%2Cn%3A658495051&ie=UTF8&qid=1474476399&lo=stripbooks";
        Category category = new Category(Species.AMAZON, Category.TYPE_NORMAL, "推理小说", "小说", url, 32422, 2);
        category.setCat1name("小说");
        Assert.assertTrue(WBListHelper.checkCategory(category));
    }

    @Test
    public void testCheckCategoryFalse() {
        String url = "https://www.amazon.cn/s/ref=lp_658495051_il_ti_stripbooks?rh=n%3A658390051%2Cn%3A%21658391051%2Cn%3A658393051%2Cn%3A658495051&ie=UTF8&qid=1474476399&lo=stripbooks";
        Category category = new Category(Species.AMAZON, Category.TYPE_NORMAL, "推理小说", "小说", url, 32422, 2);
        category.setCat1name("娱乐");
        Assert.assertFalse(WBListHelper.checkCategory(category));
    }
}
