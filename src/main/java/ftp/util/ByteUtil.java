package ftp.util;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.text.DecimalFormat;

/**
 * * @date 2021/12/3
 **/
public class ByteUtil {
    /**
     * 判断两个字节数组是否相同
     */
    public static boolean equals(byte[] a, byte[] b) {
        if (a == b) {
            return true;
        }
        if (a == null || b == null) {
            return false;
        }
        if (a.length != b.length) {
            return false;
        }
        for (int i = 0; i < a.length; i++) {
            if (a[i] != b[i]) {
                return false;
            }
        }
        return true;
    }

    private static final int GB = 1024 * 1024 * 1024;
    public static final int MB = 1024 * 1024;
    private static final int KB = 1024;

    public static String getFileSize(File file) {
        long size = file.isDirectory() ? FileUtils.sizeOf(file) : file.length();
        DecimalFormat df = new DecimalFormat("0.00");
        String resultSize;
        //如果当前Byte的值大于等于1GB
        if (size / GB >= 1) {
            resultSize = df.format(size / (float) GB) + "GB";
        }
        //如果当前Byte的值大于等于1MB
        else if (size / MB >= 1) {
            resultSize = df.format(size / (float) MB) + "MB";
        }
        //如果当前Byte的值大于等于1KB
        else if (size / KB >= 1) {
            resultSize = df.format(size / (float) KB) + "KB";
        } else {
            resultSize = size + "B";
        }
        return resultSize;
    }
}

