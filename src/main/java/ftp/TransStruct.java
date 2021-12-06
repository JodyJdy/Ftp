package ftp;

/**
 * 传输的帧的结构体
 */
public class TransStruct {
    /**
     * 帧的大小（仅仅代表内容的大小）
     */
    private Integer size;
    /**
     * 标识 指令传输 还是 数据传输
     */
    private Byte type;
    /**
     * 数据传输中有效，两bit位     * 00 默认     * 01 传输中     * 10 上次传输成功     * 11 传输完毕
     */
    private byte[] status = new byte[]{(byte) 0, (byte) 0};
    /**
     * 除了 size 外的header的长度
     */
    public final static int HEADER_LEN = 3;
    /**
     * 传输内容
     */
    private byte[] contents;

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public byte[] getStatus() {
        return status;
    }

    public void setStatus(byte[] status) {
        this.status = status;
    }

    public byte[] getContents() {
        return contents;
    }

    public void setContents(byte[] contents) {
        this.contents = contents;
    }
}
