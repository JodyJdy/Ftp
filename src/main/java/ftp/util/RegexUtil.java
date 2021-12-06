package ftp.util;

import java.util.regex.Pattern;

/**
 * * @date 2021/12/4
 **/
public class RegexUtil {
    public static String[] splitInstruciton(String ins) {
        return ins.split(" +");
    }

    private static final Pattern WINDOWS_ABSOLUTE_PATH = Pattern.compile("[a-zA-Z]:.*");

    static boolean isWindowsAbsolutePath(String path) {
        return WINDOWS_ABSOLUTE_PATH.matcher(path).matches();
    }
}