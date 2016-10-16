package com.iread.util;

import com.iread.bean.Category;
import com.iread.bean.Species;
import com.iread.conf.ConfMan;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Created by liuxiaolong on 16/10/15.
 */
public class CategoryHelperTest {
    @BeforeClass
    public static void setUp() {
        PropertyConfigurator.configure(new ConfMan().DEFAULT_CONF_PATH);
        CategoryHelper.init(Species.AMAZON);
    }

    @Test
    public void testGetCategoryByFullname() {
        String fullname = "经济管理|经济学理论与读物|经济学文集与刊物";
        Category category = CategoryHelper.getCategoryByName(fullname);
        System.out.println(category);
        Assert.assertNotNull(category);
    }

    @Test
    public void testGetCategoryByFullname_null() {
        String fullname = "经济管理|经济学理论与读物|经济学文集与刊物_test";
        Category category = CategoryHelper.getCategoryByName(fullname);
        System.out.println(category);
        Assert.assertNull(category);
    }
}
