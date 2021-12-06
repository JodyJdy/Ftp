package ftp.ftpclient.trans;

import java.util.concurrent.FutureTask;

/**
 * 文件传输任务包装类
 **/
public class FileTransTask {
    /**
     * 文件传输的指令
     */
    private final String ins;
    /**
     * 文件传输对应的 Future
     */
    private final FutureTask<Void> futureTask;

    FileTransTask(String instruction, FutureTask<Void> futureTask) {
        this.ins = instruction;
        this.futureTask = futureTask;
    }

    public String getIns() {
        return ins;
    }

    FutureTask<Void> getFutureTask() {
        return futureTask;
    }
}
