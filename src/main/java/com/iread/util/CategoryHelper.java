package com.iread.util;

import com.iread.bean.Category;
import com.iread.bean.Species;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuxiaolong on 16/10/15.
 */
public class CategoryHelper {
    private static List<Category> categories;

    public static boolean init(Species species) {
        categories = Importer.readCategorys(species);
        return true;
    }

    public static Category getCategoryByName(String fullName) {
        for (Category category : categories) {
            if (category.getCatFullName().equals(fullName)) {
                return category;
            }
        }
        return null;
    }
}
