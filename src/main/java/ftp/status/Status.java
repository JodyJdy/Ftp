package ftp.status;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * * @date 2021/12/3
 **/
public class Status {
    /**
     * 客户端id
     */
    private String id;
    /**
     * 如果用户在下载文件，会持有一个 InutStream
     */
    private InputStream inputStream;
    /**
     * 如果用户在上传文件，会只有一个OutputStream
     */
    private OutputStream outputStream;
    /**
     * 上传或下载的文件的名称
     */
    private String name;
    /**
     * 如果客户端下载文件，存储的是当前已经下载的内容
     */
    private long pos;
    /**
     * 文件的结尾位置
     */
    private long end;
    /**
     * 默认目录
     */
    String dir;

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }

    public void setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPos() {
        return pos;
    }

    public void setPos(long pos) {
        this.pos = pos;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
