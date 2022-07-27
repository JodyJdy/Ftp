package ftp.instruction;

import ftp.TransStruct;
import ftp.status.ClientStatus;
import ftp.status.Status;

/**
 * 指令
 */
public abstract class Instruction {
    public String ins;
    public Status status;

    public Instruction(String ins, Status status) {
        this.ins = ins;
        this.status = status;
    }

    /**
     * 指令的执行, 服务端执行 （如果是local类的指令，在客户端执行）
     */
    public abstract TransStruct execute();

    /**
     * 指令的预处理 客户端执行，例如客户端会将指令加入到 history里面
     */
    public void preProcess() {
        if (status instanceof ClientStatus) {
            ClientStatus clientStatus = (ClientStatus) status;
            clientStatus.getHistory().add(ins);
        }
    }

    public String getIns() {
        return ins;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }
}
