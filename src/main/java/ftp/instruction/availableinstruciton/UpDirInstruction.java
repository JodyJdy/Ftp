package ftp.instruction.availableinstruciton;

import ftp.TransStruct;
import ftp.enums.InstructionEnum;
import ftp.ftpclient.ClientContext;
import ftp.instruction.Instruction;
import ftp.status.Status;
import ftp.util.RegexUtil;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.Collection;

import static ftp.util.FileUtil.SPLIT;
import static ftp.util.FileUtil.isAbsolutePath;

/**
 * *上传文件夹指令， 客户端执行
 **/
public class UpDirInstruction extends Instruction {
    public UpDirInstruction(String ins, Status status) {
        super(ins, status);
    }

    @Override
    public TransStruct execute() {
        return null;
    }

    @Override
    public void process() {
        super.process();
        String[] splits = RegexUtil.splitInstruciton(ins);
        // 上传目录
        String sourceDir;
        //上传的目录
        if (isAbsolutePath(splits[1])) {
            sourceDir = splits[1];
        } else {
            sourceDir = status.getDir() + File.separator + splits[1];
        }
        if (splits.length == 2) {
            new UpDirThread(sourceDir, "").start();
        } else {
            new UpDirThread(sourceDir, splits[2]).start();
        }
    }

    static class UpDirThread extends Thread {
        /**
         * 要上传的目录
         */
        private final String sourceDir;
        /**
         * 上传的目录放置的位置
         */
        private final String targetDir;

        UpDirThread(String sourceDir, String targetDir) {
            this.sourceDir = sourceDir;
            this.targetDir = targetDir;
        }

        @Override
        public void run() {
            File dir = new File(sourceDir);
            if (!dir.exists()) {
                return;
            }
            Collection<File> files = FileUtils.listFiles(dir, null, true);
            files.forEach(file -> {
                String task = InstructionEnum.UP.getIns() + SPLIT + file.getPath() + SPLIT + targetDir;
                ClientContext.getTaskQueue().addFileTransTask(task);
            });
        }
    }
}
