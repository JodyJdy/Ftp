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
     * 文件传输完毕后执行的任务，在其他线程可以调用 task.get()，阻塞直到任务完成
     */
    private final FutureTask<Void> transDoneTask;

    FileTransTask(String instruction, FutureTask<Void> transDoneTask) {
        this.ins = instruction;
        this.transDoneTask = transDoneTask;
    }

    public String getIns() {
        return ins;
    }

    FutureTask<Void> getTransDoneTask() {
        return transDoneTask;
    }
}
