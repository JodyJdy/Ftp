package ftp.instruction;

import ftp.TransStruct;
import ftp.status.Status;

/**
 * *
 **/
public class DefaultInstruction extends Instruction {
    DefaultInstruction(String ins, Status status) {
        super(ins, status);
    }

    @Override
    public TransStruct execute() {
        System.out.println("指令不存在");
        return null;
    }
}
