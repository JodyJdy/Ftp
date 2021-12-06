package ftp.instruction.availableinstruciton;
import ftp.TransStruct;
import ftp.instruction.Instruction;
import ftp.status.Status;

/** * 对于 ls，pwd,,,这种非文件传输相关指令的响应，将响应本身也作为一个指令 */
public class InstructionResponse extends Instruction {
    public InstructionResponse(String ins, Status status) {
        super(ins, status);
    }

    @Override
    public TransStruct execute() {
        return null;
    }
}
