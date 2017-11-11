package com.iread.spider;

import com.iread.Main;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Created by liuxiaolong on 17/10/31.
 */
public class MainTest {
    @Ignore
    public void AmazonCategory() {
        String args[] = new String[]{"AMAZON", "category"};
        Main main = new Main();
        main.main(args);
    }
}
