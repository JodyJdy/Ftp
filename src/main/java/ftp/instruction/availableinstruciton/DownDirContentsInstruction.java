package ftp.instruction.availableinstruciton;/**
 * * @date 2021/12/6
 **/

import ftp.TransStruct;
import ftp.enums.InstructionEnum;
import ftp.ftpclient.ClientContext;
import ftp.instruction.Instruction;
import ftp.status.Status;
import ftp.util.FileUtil;
import ftp.util.RegexUtil;

import static ftp.util.FileUtil.CUR_DIR;

/**
 * 服务端向客户端返回目录里面哪些文件是可以下载的
 */
public class DownDirContentsInstruction extends Instruction {
    @Override
    public TransStruct execute() {
        super.preProcess();
        String contents = ins.substring(InstructionEnum.DOWN_DIR_CONTENTS.getIns().length() + 1);
        contents = contents.trim();
        String[] files = RegexUtil.splitInstruciton(contents);
        //下载后放置的目录
        String targetDir = "";
        if (!CUR_DIR.equals(files[0])) {
            targetDir = files[0];
        }
        new DownDirThread(files, targetDir).start();
        return null;
    }

    public DownDirContentsInstruction(String ins, Status status) {
        super(ins, status);
    }

    static class DownDirThread extends Thread {
        /**
         * 下标从1开始，下标为0的地方存放的是文件放置目录
         */
        private String[] files;
        private String targetDir;

        DownDirThread(String[] files, String targetDir) {
            this.files = files;
            this.targetDir = targetDir;
        }

        @Override
        public void run() {
            for (int i = 1; i < files.length; i++) {
                ClientContext.getTaskQueue().addFileTransTask(
                        InstructionEnum.DOWN.getIns() + FileUtil.SPLIT + files[i]
                                + FileUtil.SPLIT + targetDir);
            }
        }
    }
}
