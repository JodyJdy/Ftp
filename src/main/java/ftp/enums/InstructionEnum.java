package ftp.enums;

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
    ID("id", IdInstruction.class);
    String ins;
    Class clazz;

    InstructionEnum(String ins, Class clazz) {
        this.ins = ins;
        this.clazz = clazz;
    }

    public String getIns() {
        return ins;
    }

    public Class getClazz() {
        return clazz;
    }
}
