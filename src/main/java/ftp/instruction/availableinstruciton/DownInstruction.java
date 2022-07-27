package ftp.instruction.availableinstruciton;/**
 * * @date 2021/12/3
 **/

import ftp.TransStruct;
import ftp.enums.InstructionEnum;
import ftp.instruction.Instruction;
import ftp.status.ClientStatus;
import ftp.status.ServerClientStatus;
import ftp.status.Status;
import ftp.util.FileUtil;
import ftp.util.RegexUtil;
import ftp.util.TransStructUtil;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 客户端向服务端发起下载文件的指令
 * 下载指令  client -- > server     指令格式:  down fileName
 */
public class DownInstruction extends Instruction {
    public DownInstruction(String ins, Status status) {
        super(ins, status);
    }

    @Override
    public TransStruct execute() {
        ServerClientStatus status = (ServerClientStatus) super.status;
        //获取下载的文件名称
        String fileName = RegexUtil.splitInstruciton(ins)[1];
        //相对路径  down   a/b.txt
        if (!FileUtil.isAbsolutePath(fileName)) {
            fileName = status.getDir() + File.separator + fileName;
        }
        File file = new File(fileName);
        if (file.isFile() && file.exists()) {
            //可以下载,记录下文件名称
            status.setName(fileName);
            try {
                InputStream inputStream = new FileInputStream(file);
                status.setEnd(inputStream.available());
                status.setInputStream(inputStream);
            } catch (IOException e) {
                return null;
            }
            return TransStructUtil.generateInsStruct(InstructionEnum.COULD_DOWN.getIns());
        }
        return null;
    }

    @Override
    public void preProcess() {
        super.preProcess();
        ClientStatus status = (ClientStatus) super.status;
        //获取下载后保存为的文件名称， 例如  down  a.txt /b/c, 那么下载后的文件名称为 /b/c/a/txt
        String[] splits = RegexUtil.splitInstruciton(ins);
        String fileName;
        //未指定文件下载后的路径，保存在默认路径
        if (splits.length == 2) {
            fileName = status.getDir() + File.separator + new File(splits[1]).getName();
        } else {
            //指定了下载路径
            if (FileUtil.isAbsolutePath(splits[2])) {
                fileName = splits[2] + File.separator + new File(splits[1]).getName();
            } else {
                fileName = status.getDir() + File.separator + splits[2] + File.separator + new File(splits[1]).getName();
            }
        }
        try {
            FileUtils.touch(new File(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        status.setName(fileName);
    }
}
