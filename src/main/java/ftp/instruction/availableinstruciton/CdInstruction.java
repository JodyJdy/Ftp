package ftp.instruction.availableinstruciton;

import ftp.TransStruct;
import ftp.ftpserver.ClientStatusCache;
import ftp.instruction.Instruction;
import ftp.status.ServerClientStatus;
import ftp.status.Status;
import ftp.util.FileUtil;
import ftp.util.RegexUtil;
import ftp.util.TransStructUtil;

import java.io.File;
import java.util.List;

/**
 * 切换目录指令
 **/
public class CdInstruction extends Instruction {
    public CdInstruction(String ins, Status status) {
        super(ins, status);
    }



    @Override
    public TransStruct execute() {
        String[] splits = RegexUtil.splitInstruciton(ins);
        File file = FileUtil.cdFile(splits[1], status);
        String result;
        if (file == null) {
            result = "目录不存在\n";
        } else {
            //修改同一client的channel的当前目录信息，目前只有cd命令需要这样做
            if (status instanceof ServerClientStatus) {
                List<ServerClientStatus> statusList = ClientStatusCache.getStatus(status.getId());
                synchronized (status) {
                    for (ServerClientStatus s : statusList) {
                        s.setDir(status.getDir());
                    }
                }
            }
            result = "";
        }
        return TransStructUtil.generateResponse(result);
    }

    @Override
    public void preProcess() {
        super.preProcess();
    }
}
