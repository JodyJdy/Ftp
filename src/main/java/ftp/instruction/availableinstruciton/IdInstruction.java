package ftp.instruction.availableinstruciton;/**
 * * @date 2021/12/6
 **/

import ftp.TransStruct;
import ftp.ftpserver.ClientStatusCache;
import ftp.instruction.Instruction;
import ftp.status.ServerClientStatus;
import ftp.status.Status;
import ftp.util.RegexUtil;

/**
 * 客户端向服务端发送自己的标识
 */
public class IdInstruction extends Instruction {
    public IdInstruction(String ins, Status status) {
        super(ins, status);
    }

    @Override
    public TransStruct execute() {
        String[] splits = RegexUtil.splitInstruciton(ins.trim());
        String id = splits[1];
        status.setId(id);
        ClientStatusCache.addStatus(id, (ServerClientStatus) status);
        return null;
    }
}
