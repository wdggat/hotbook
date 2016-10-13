package com.iread.util;

import com.alibaba.fastjson.JSON;
import com.iread.bean.Category;
import com.iread.bean.Species;
import com.iread.conf.ConfMan;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

/**
 * Created by liuxiaolong on 16/10/13.
 */
@Ignore
public class ImporterTest {
    @BeforeClass
    public static void setUp() {
        PropertyConfigurator.configure(new ConfMan().DEFAULT_CONF_PATH);
    }
    @Test
    public void testReadCategorys() {
        Species species = Species.AMAZON;
        List<Category> categories = Importer.readCategorys(species);
        for(Category category : categories) {
            System.out.println(JSON.toJSONString(category));
        }
        Assert.assertTrue(categories.size() > 100);
    }
}
