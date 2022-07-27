package ftp.enums;

import ftp.instruction.Instruction;
import ftp.instruction.availableinstruciton.*;

/**
 * * @date 2021/12/3
 **/
public enum InstructionEnum {
    /**
     * 支持指令集合
     */
    PWD("pwd", PwdInstruction.class),
    LS("ls", LsInstruction.class), RM("rm", RmInstruction.class),
    UP("up", UpInstruction.class), DOWN("down", DownInstruction.class),
    COULD_DOWN("could_down", CouldDownInstruction.class),
    HISTORY("history", HistoryInstruction.class),
    INSTRUCTION_RESPONSE("response", InstructionResponse.class),
    CD("cd", CdInstruction.class), CP("cp", CpInstruction.class),
    LOCAL("local", LocalInstruction.class), HELP("help", HelpInstruction.class),
    UP_DIR("updir", UpDirInstruction.class), DOWN_DIR("downdir", DownDirInstruction.class),
    DOWN_DIR_CONTENTS("downdircontents", DownDirContentsInstruction.class),
    EXIT("exit",ExitInstruction.class),
    ID("id", IdInstruction.class);
    String ins;
    Class<? extends Instruction> clazz;

    InstructionEnum(String ins, Class<? extends Instruction> clazz) {
        this.ins = ins;
        this.clazz = clazz;
    }

    public String getIns() {
        return ins;
    }

    public Class<? extends Instruction> getClazz() {
        return clazz;
    }
}
