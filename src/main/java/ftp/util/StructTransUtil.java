package ftp.util;

import ftp.TransStruct;
import ftp.enums.DataTransStatus;
import ftp.enums.InstructionEnum;
import ftp.enums.TransType;
import ftp.ftpclient.ClientContext;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;

import static ftp.util.FileUtil.SPLIT;

/**
 * * @date 2021/12/2
 **/
public class StructTransUtil {
    /**
     * transStruct 转 byteBuf
     */
    public static ByteBuf transToByteBuf(TransStruct struct) {
        // 返回结果包含了 struct内容的大小 和 整型size占用的大小
        ByteBuf buf = Unpooled.buffer();
        buf.writeInt(struct.getSize());
        buf.writeByte(struct.getType());
        buf.writeBytes(struct.getStatus());
        if (struct.getContents() != null) {
            buf.writeBytes(struct.getContents());
        }
        return buf;
    }

    /**
     * byteBuf转transStruct
     */
    public static TransStruct transToStruct(ByteBuf buf) {
        TransStruct struct = new TransStruct();
        struct.setSize(buf.readInt());
        struct.setType(buf.readByte());
        for (int i = 0; i < struct.getStatus().length; i++) {
            struct.getStatus()[i] = buf.readByte();
        }
        if (struct.getSize() - TransStruct.HEADER_LEN != 0) {
            byte[] contents = new byte[struct.getSize() - TransStruct.HEADER_LEN];
            for (int i = 0; i < contents.length; i++) {
                contents[i] = buf.readByte();
            }
            struct.setContents(contents);
        }
        return struct;
    }

    /**
     * 设置 struct的大小
     */
    private static void setSize(TransStruct struct) {
        int size = TransStruct.HEADER_LEN + (struct.getContents() == null ? 0 : struct.getContents().length);
        struct.setSize(size);
    }

    /**
     * 生成指令的TransStruct
     * @param content 字符串内容
     */
    public static TransStruct generateInsStruct(String content) {
        TransStruct t = new TransStruct();
        t.setType(TransType.INSTRUCTION.getValue());
        t.setContents(content.getBytes(CharsetUtil.UTF_8));
        StructTransUtil.setSize(t);
        return t;
    }

    /**
     * 生成上次传输成功的 TransStruct
     */
    public static TransStruct generateLastSucc() {
        TransStruct t = new TransStruct();
        t.setType(TransType.DATA.getValue());
        t.setStatus(DataTransStatus.LAST_SUCC.getBytes());
        StructTransUtil.setSize(t);
        return t;
    }

    /**
     * 生成传输完毕的TransStuct
     */
    public static TransStruct generateTransDone() {
        TransStruct t = new TransStruct();
        t.setType(TransType.DATA.getValue());
        t.setStatus(DataTransStatus.TRANS_DONE.getBytes());
        StructTransUtil.setSize(t);
        return t;
    }

    /**
     * 生成传输完毕的TransStuct
     */
    public static TransStruct generateTransDone(byte[] contents) {
        TransStruct t = new TransStruct();
        t.setType(TransType.DATA.getValue());
        t.setStatus(DataTransStatus.TRANS_DONE.getBytes());
        t.setContents(contents);
        StructTransUtil.setSize(t);
        return t;
    }

    /**
     * 生成传输文件用的 TransStruct
     */
    public static TransStruct generateTransing(byte[] bytes) {
        TransStruct t = new TransStruct();
        t.setType(TransType.DATA.getValue());
        t.setStatus(DataTransStatus.TRANSING.getBytes());
        t.setContents(bytes);
        StructTransUtil.setSize(t);
        return t;
    }

    /**
     * 生成包含response内容的 TransStruct
     */
    public static TransStruct generateResponse(String content) {
        TransStruct t = new TransStruct();
        t.setType(TransType.INSTRUCTION.getValue());
        t.setStatus(DataTransStatus.TRANSING.getBytes());
        t.setContents((InstructionEnum.INSTRUCTION_RESPONSE.getIns() + SPLIT + content).getBytes(CharsetUtil.UTF_8));
        StructTransUtil.setSize(t);
        return t;
    }

    /**
     * 生成包含下载内容的TransStruct
     */
    public static TransStruct generateDownDirContent(String content) {
        TransStruct t = new TransStruct();
        t.setType(TransType.INSTRUCTION.getValue());
        t.setStatus(DataTransStatus.TRANSING.getBytes());
        t.setContents((InstructionEnum.DOWN_DIR_CONTENTS.getIns() + SPLIT + content).getBytes(CharsetUtil.UTF_8));
        StructTransUtil.setSize(t);
        return t;
    }

    /**
     * 生成包含id的Transstruct
     */
    public static TransStruct generateIdStruct() {
        TransStruct t = new TransStruct();
        t.setType(TransType.INSTRUCTION.getValue());
        t.setContents((InstructionEnum.ID.getIns() + SPLIT + ClientContext.getId()).getBytes(CharsetUtil.UTF_8));
        StructTransUtil.setSize(t);
        return t;
    }
}
