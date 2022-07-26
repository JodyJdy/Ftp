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
     * 指令的执行,一般在服务端
     */
    public abstract TransStruct execute();

    /**
     * 指令的处理 一般在客户端
     */
    public void process() {
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
