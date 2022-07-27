package ftp.instruction.availableinstruciton;/**
 * * @date 2021/12/2
 **/

import ftp.TransStruct;
import ftp.instruction.Instruction;
import ftp.status.Status;
import ftp.util.TransStructUtil;

/** * 当前目录指令  client -- > server */
public class PwdInstruction extends Instruction {
    public PwdInstruction(String ins, Status status) {
        super(ins, status);
    }

    @Override
    public TransStruct execute() {
        return TransStructUtil.generateResponse(status.getDir() + "\n");
    }

    @Override
    public void preProcess() {
        super.preProcess();
    }
}
