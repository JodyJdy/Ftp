package ftp.enums;

import ftp.util.ByteUtil;

/**
 * 数据传输状态
 */
public enum DataTransStatus {
    /**
     * 00 默认     * 01 传输中     * 10 上次传输成功     * 11 传输完成
     */
    DEFAULT(new byte[]{(byte) 0, (byte) 0}), TRANSING(new byte[]{(byte) 0, (byte) 1}), LAST_SUCC(new byte[]{(byte) 1, (byte) 0}), TRANS_DONE(new byte[]{(byte) 1, (byte) 1});
    byte[] bytes;

    DataTransStatus(byte[] bytes) {
        this.bytes = bytes;
    }

    public static DataTransStatus getStatus(byte[] status) {
        for (DataTransStatus statu : DataTransStatus.values()) {
            if (ByteUtil.equals(status, statu.bytes)) {
                return statu;
            }
        }
        return DEFAULT;
    }

    public byte[] getBytes() {
        return bytes;
    }
}
