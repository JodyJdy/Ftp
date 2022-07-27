package ftp.instruction.availableinstruciton;

import ftp.TransStruct;
import ftp.instruction.Instruction;
import ftp.status.Status;
import ftp.util.FileUtil;
import ftp.util.RegexUtil;
import ftp.util.TransStructUtil;

import java.io.IOException;

/**
 * 文件拷贝指令
 **/
public class CpInstruction extends Instruction {
    public CpInstruction(String ins, Status status) {
        super(ins, status);
    }

    @Override
    public TransStruct execute() {
        // cp fromfile tofile
        String[] splists = RegexUtil.splitInstruciton(ins);
        boolean result;
        try {
            result = FileUtil.cpFile(splists[1], splists[2], status);
        } catch (IOException e) {
            result = false;
        }
        if (result) {
            return TransStructUtil.generateResponse("拷贝成功\n");
        }
        return TransStructUtil.generateResponse("拷贝失败\n");
    }

    @Override
    public void preProcess() {
        super.preProcess();
    }
}
