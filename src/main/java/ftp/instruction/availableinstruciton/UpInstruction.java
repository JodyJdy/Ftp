package ftp.instruction.availableinstruciton;

import ftp.TransStruct;
import ftp.instruction.Instruction;
import ftp.status.ClientStatus;
import ftp.status.ServerClientStatus;
import ftp.status.Status;
import ftp.util.FileUtil;
import ftp.util.RegexUtil;
import ftp.util.TransStructUtil;
import org.apache.commons.io.FileUtils;

import java.io.*;

import static ftp.util.FileUtil.isAbsolutePath;

/**
 * 上传指令  client -- > server
 */
public class UpInstruction extends Instruction {
    /**
     * 由 服务端执行
     */
    @Override
    public TransStruct execute() {
        ServerClientStatus status = (ServerClientStatus) super.status;
        //获取文件名称
        String[] splits = RegexUtil.splitInstruciton(ins);
        //未指定上传路径，放到默认目录
        String fileName;
        //上传的文件
        File upFile = new File(splits[1]);
        if (splits.length == 2) {
            fileName = status.getDir() + File.separator + upFile.getName();
        } else {
            //指定了路径，绝对路径
            if (isAbsolutePath(splits[2])) {
                fileName = splits[2] + File.separator + upFile.getName();
            } else {
                fileName = status.getDir() + File.separator + splits[2] + File.separator + upFile.getName();
            }
        }
        //保存在本地的文件
        File file = new File(fileName);
        status.setName(fileName);
        try {
            FileUtils.touch(file);
            OutputStream outputStream = new FileOutputStream(file);
            status.setOutputStream(outputStream);
            return TransStructUtil.generateLastSucc();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 由客户端执行
     */
    @Override
    public void preProcess() {
        super.preProcess();
        ClientStatus status = (ClientStatus) super.status;
        status.getHistory().add(ins);
        // up xxx.txt 或者 up xxx.txt xxxx
        String fileName = RegexUtil.splitInstruciton(ins)[1];
        if (!FileUtil.isAbsolutePath(fileName)) {
            fileName = status.getDir() + File.separator + fileName;
        }
        File file = new File(fileName);
        status.setName(fileName);
        try {
            InputStream inputStream = new FileInputStream(file);
            status.setInputStream(inputStream);
            status.setPos(0);
            status.setEnd(inputStream.available());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public UpInstruction(String ins, Status status) {
        super(ins, status);
    }
}

