package ftp.instruction.availableinstruciton;/**
 * * @date 2021/12/3
 **/

import ftp.TransStruct;
import ftp.instruction.Instruction;
import ftp.status.Status;
import ftp.util.FileUtil;
import ftp.util.RegexUtil;
import ftp.util.TransStructUtil;

import java.io.IOException;

/** * 删除指令  client -- > server */
public class RmInstruction extends Instruction {
    public RmInstruction(String ins, Status status) {
        super(ins, status);
    }

    @Override
    public TransStruct execute() {
        String[] splits = RegexUtil.splitInstruciton(ins);
        boolean result;
        try {
            result = FileUtil.rmFile(splits[1], status);
        } catch (IOException e) {
            result = false;
        }
        if (result) {
            return TransStructUtil.generateResponse("删除成功\n");
        }
        return TransStructUtil.generateResponse("删除失败\n");
    }

    @Override
    public void preProcess() {
        super.preProcess();
    }
}
