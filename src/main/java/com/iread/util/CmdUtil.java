package com.iread.util;

import java.io.IOException;

/**
 * Created by liuxiaolong on 17/12/8.
 */
public class CmdUtil {
    public static int callShell(String command) throws IOException, InterruptedException {
        Process process = Runtime.getRuntime().exec(command);
        return process.waitFor();
    }
}
