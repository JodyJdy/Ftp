package ftp.instruction.availableinstruciton;

import ftp.TransStruct;
import ftp.enums.InstructionEnum;
import ftp.instruction.Instruction;
import ftp.instruction.InstructionResolver;
import ftp.status.Status;
import io.netty.util.CharsetUtil;

/**
 * 本地执行的指令
 **/
public class LocalInstruction extends Instruction {
    public LocalInstruction(String ins, Status status) {
        super(ins, status);
    }

    @Override
    public TransStruct execute() {
        //将指令转成本地指令
        ins = ins.substring(InstructionEnum.LOCAL.getIns().length() + 1).trim();
        Instruction instruction = InstructionResolver.resolver(ins, status);
        TransStruct transStruct = instruction.execute();
        if (transStruct != null) {
            String content = new String(transStruct.getContents(), CharsetUtil.UTF_8);
            System.out.print(content.substring(InstructionEnum.INSTRUCTION_RESPONSE.getIns().length() + 1));
        }
        return null;
    }
}
