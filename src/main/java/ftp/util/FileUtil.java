package ftp.util;

import ftp.status.Status;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * * @date 2021/12/4
 **/
public class FileUtil {
    /**
     * 当前目录
     */
    public static final String CUR_DIR = ".";
    /**
     * 上一层目录
     */
    private static final String PRE_DIR = "..";
    /**
     * 指令中的分隔符
     */
    public static final String SPLIT = " ";

    /**
     * ls 命令
     */
    public static List<File> lsFile(String path, Status status) {
        String curDir = status.getDir();
        File file;
        if (path != null) {
            //绝对路径
            if (isAbsolutePath(path)) {
                file = new File(path);
            } else {
                //相对路径
                file = new File(curDir + File.separator + path);
            }
        } else {
            file = new File(curDir);
        }
        // 目录不存在，或路径不是个目录，返回null
        if (!file.exists() || file.isFile()) {
            return null;
        }
        return searchFile(file, false, false);
    }

    /**
     * cd 命令
     */
    public static File cdFile(String path, Status status) {
        File curDir = new File(status.getDir());
        if (CUR_DIR.equals(path)) {
            return curDir;
        }
        if (path.equals(PRE_DIR)) {
            File parentFile = curDir.getParentFile();
            if (parentFile != null && parentFile.exists()) {
                status.setDir(parentFile.getPath());
                return parentFile;
            }
        }
        //绝对路径
        if (isAbsolutePath(path)) {
            File temp = new File(path);
            if (temp.exists()) {
                status.setDir(temp.getPath());
                return temp;
            }
        } else {
            //相对路径
            File temp = new File(status.getDir() + File.separator + path);
            if (temp.exists()) {
                status.setDir(temp.getPath());
                return temp;
            }
        }
        return null;
    }

    /**
     * 扫描路径下的文件
     * @param dir 目录
     * @param recurision 是否递归的查询
     * @param onlyFile  是否只要文件
     */
    private static List<File> searchFile(File dir, boolean recurision, boolean onlyFile) {
        List<File> result = new ArrayList<>();
        if (!dir.exists() || dir.isFile()) {
            return result;
        }
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (!onlyFile || file.isFile()) {
                    result.add(file);
                }
                if (file.isDirectory() && recurision) {
                    result.addAll(searchFile(file, recurision, onlyFile));
                }
            }
        }
        return result;
    }

    private static final boolean IS_WINDOWS = System.getProperty("os.name").toLowerCase().contains("windows");

    /**
     * 判断是否是绝对路径
     */
    public static boolean isAbsolutePath(String path) {
        if (path == null) {
            return false;
        }
        path = path.toLowerCase();
        if (IS_WINDOWS) {
            return RegexUtil.isWindowsAbsolutePath(path);
        }
        //认为是linux， 路径为 /a/b这种
        return path.startsWith(File.separator);
    }

    public static boolean rmFile(String path, Status status) throws IOException {
        File file;
        //绝对路径
        if (isAbsolutePath(path)) {
            file = new File(path);
        } else {
            //相对路径
            file = new File(status.getDir() + File.separator + path);
        }
        if (!file.exists()) {
            return true;
        }
        if (file.isFile()) {
            FileUtils.delete(file);
        } else {
            FileUtils.deleteDirectory(file);
        }
        return true;
    }

    public static boolean cpFile(String from, String to, Status status) throws IOException {
        File fromFile;
        if (isAbsolutePath(from)) {
            fromFile = new File(from);
        } else {
            fromFile = new File(status.getDir() + File.separator + from);
        }
        File toFile;
        if (isAbsolutePath(to)) {
            toFile = new File(to);
        } else {
            toFile = new File(status.getDir() + File.separator + to);
        }
        if (fromFile.isFile()) {
            FileUtils.copyFile(fromFile, toFile);
        } else {
            FileUtils.copyDirectory(fromFile, toFile);
        }
        return true;
    }
}
