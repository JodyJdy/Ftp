package ftp.enums;

/**
 * 传输内容的类型
 */
public enum TransType {
    /**
     * 指令传输，数据传输
     */
    INSTRUCTION((byte) 0), DATA((byte) 1);
    byte value;

    TransType(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }
}
