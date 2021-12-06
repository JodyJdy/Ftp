package ftp.instruction;

import ftp.enums.InstructionEnum;
import ftp.status.Status;
import ftp.util.FileUtil;

import java.lang.reflect.Constructor;

/**
 * * @date 2021/12/3 * * 指令解析
 **/
public class InstructionResolver {
    /**
     * 将指令的字符串形式解析为 Instruction对象     * @param ins 指令     * @param status 信道状态
     */
    @SuppressWarnings("unchecked")
    public static Instruction resolver(String ins, Status status) {
        ins = ins.trim();
        for (InstructionEnum instructionEnum : InstructionEnum.values()) {
            if (ins.startsWith(instructionEnum.getIns()) && (ins.length() == instructionEnum.getIns().length() || ins.charAt(instructionEnum.getIns().length()) == FileUtil.SPLIT.charAt(0))) {
                Class clazz = instructionEnum.getClazz();
                try {
                    Constructor constructor = clazz.getConstructor(String.class, Status.class);
                    return (Instruction) constructor.newInstance(ins, status);
                } catch (Exception ignored) {
                }
            }
        }
        return new ftp.instruction.DefaultInstruction(ins, status);
    }

    /**
     * 检查指令是否存在
     */
    public static boolean checkExist(String ins) {
        ins = ins.trim();
        for (InstructionEnum instructionEnum : InstructionEnum.values()) {
            if (ins.startsWith(instructionEnum.getIns())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检查指令是否应该在客户端本地执行
     */
    public static boolean localExec(String ins) {
        ins = ins.trim();
        return ins.startsWith(InstructionEnum.LOCAL.getIns()) || ins.startsWith(InstructionEnum.HISTORY.getIns()) || ins.startsWith(InstructionEnum.HELP.getIns());
    }

    /**
     * 检查是否是文件传输指令
     */
    public static boolean isTrans(String ins) {
        ins = ins.trim();
        return ins.startsWith(InstructionEnum.DOWN.getIns()) || ins.startsWith(InstructionEnum.UP.getIns()) || ins.startsWith(InstructionEnum.UP_DIR.getIns()) || ins.startsWith(InstructionEnum.DOWN_DIR.getIns());
    }

    /**
     * 是 文件传输指令
     */
    public static boolean isFileTrans(String ins) {
        return ins.startsWith(InstructionEnum.DOWN.getIns()) || ins.startsWith(InstructionEnum.UP.getIns());
    }
}
