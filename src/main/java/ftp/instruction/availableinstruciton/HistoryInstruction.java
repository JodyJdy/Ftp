package ftp.instruction.availableinstruciton;

import ftp.TransStruct;
import ftp.instruction.Instruction;
import ftp.status.ClientStatus;
import ftp.status.Status;

import java.util.Stack;

/**
 * 打印客户端历史记录指令
 **/
public class HistoryInstruction extends Instruction {
    public HistoryInstruction(String ins, Status status) {
        super(ins, status);
    }

    @Override
    public TransStruct execute() {
        ClientStatus clientStatus = (ClientStatus) status;
        Stack<String> stringStack = clientStatus.getHistory();
        while (!stringStack.isEmpty()) {
            System.out.println(stringStack.pop());
        }
        return null;
    }

    @Override
    public void preProcess() {
        //history指令不记录
    }
}
