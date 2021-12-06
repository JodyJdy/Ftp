package ftp.instruction.availableinstruciton;/**
 * * @date 2021/12/3
 **/

import ftp.TransStruct;
import ftp.instruction.Instruction;
import ftp.status.ClientStatus;
import ftp.status.Status;
import ftp.util.StructTransUtil;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * 对于下载的确认，server - > client， 执行的是客户端
 */
public class CouldDownInstruction extends Instruction {
    public CouldDownInstruction(String ins, Status status) {
        super(ins, status);
    }

    @Override
    public TransStruct execute() {
        ClientStatus clientStatus = (ClientStatus) super.status;
        //设置状态为开始传输文件
        OutputStream outputStream;
        try {
            outputStream = new FileOutputStream(clientStatus.getName());
            clientStatus.setOutputStream(outputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //开始传输，告诉服务端上次的请求已经收到
        return StructTransUtil.generateLastSucc();
    }

    @Override
    public void process() {
        super.process();
    }
}

