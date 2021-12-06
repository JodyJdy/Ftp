package ftp.ftpclient.trans;

import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * * 文件传输任务队列
 **/
public class FileTransTaskQueue {
    private LinkedBlockingQueue<FileTransTask> instructionQueue = new LinkedBlockingQueue<>();

    /**
     * 添加一个文件传输的任务到队列里面去，返回一个Future，任务是异步执行的，可以通过     * future.get()阻塞直到任务执行完毕
     */
    public Future<Void> addFileTransTask(final String ins) {
        //任务完成后，打印内容
        FutureTask<Void> futureTask = new FutureTask<>(() ->System.out.println(ins + " done"), null);
        instructionQueue.add(new FileTransTask(ins, futureTask));
        return futureTask;
    }

    void addFileTransTask(final FileTransTask fileTransTask) {
        instructionQueue.add(fileTransTask);
    }

    /**
     * 取一个文件传输任务
     */
    FileTransTask getFileTransTask() throws InterruptedException {
        return instructionQueue.take();
    }
}
