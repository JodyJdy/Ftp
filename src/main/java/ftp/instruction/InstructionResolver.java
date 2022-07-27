package ftp.instruction;

import static ftp.enums.InstructionEnum.*;

import ftp.enums.InstructionEnum;
import ftp.status.Status;
import ftp.util.FileUtil;
import ftp.util.RegexUtil;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;

/**
 * * @date 2021/12/3 * * 指令解析
 **/
public class InstructionResolver {
    /**
     * 将指令的字符串形式解析为 Instruction对象
     *
     * @param ins    指令
     * @param status 信道状态
     */
    public static Instruction resolver(String ins, Status status) {
        ins = ins.trim();
        for (InstructionEnum instructionEnum : InstructionEnum.values()) {
            if (ins.startsWith(instructionEnum.getIns()) && (ins.length() == instructionEnum.getIns().length() || ins.charAt(instructionEnum.getIns().length()) == FileUtil.SPLIT.charAt(0))) {
                return resolver(instructionEnum,status,ins);
            }
        }
        return new DefaultInstruction(ins, status);
    }

    public static Instruction resolver(InstructionEnum insEnum, Status status, String ins) {
        Class<? extends Instruction> clazz = insEnum.getClazz();
        try {
            Constructor<? extends Instruction> constructor = clazz.getConstructor(String.class, Status.class);
            return constructor.newInstance(ins, status);
        } catch (Exception ignored) {
        }
        return new ftp.instruction.DefaultInstruction(null, status);
    }

    /**
     * 检查指令是否存在
     */
    public static InstructionEnum checkExist(String ins) {
        ins = RegexUtil.splitInstruciton(ins.trim())[0];
        for (InstructionEnum instructionEnum : InstructionEnum.values()) {
            if (ins.equals(instructionEnum.getIns())) {
                return instructionEnum;
            }
        }
        return null;
    }


    /**
     * 检查指令是否应该在客户端本地执行
     */
    public static boolean localExec(InstructionEnum ins) {
        return ins == LOCAL || ins == HISTORY || ins == HELP;
    }

    private static List<InstructionEnum> TRANS = Arrays.asList(DOWN, UP, DOWN_DIR, UP_DIR);

    /**
     * 检查是否是文件传输指令
     */
    public static boolean isTrans(InstructionEnum ins) {
        return TRANS.contains(ins);
    }

}
