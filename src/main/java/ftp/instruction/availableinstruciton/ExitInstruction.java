package ftp.instruction.availableinstruciton;

import ftp.TransStruct;
import ftp.instruction.Instruction;
import ftp.status.Status;

/**
 * 退出客户端
 */
public class ExitInstruction extends Instruction {

    public ExitInstruction(String ins, Status status) {
        super(null,null);
    }

    @Override
    public void preProcess() {
       System.exit(0);
    }

    @Override
    public TransStruct execute() {
        return null;
    }
}
