package ftp.status;

import ftp.ftpclient.trans.ChannelStatus;

import java.io.File;
import java.io.IOException;
import java.util.Stack;
import java.util.concurrent.FutureTask;

/** * 用于在记录客户端自身的状态 */
public class ClientStatus extends Status {
    public ClientStatus() {
        System.out.println("初始化ClientStatus");
        try {
            super.dir = new File(".").getCanonicalPath();
        } catch (IOException e) {
            super.dir = System.getProperty("user.dir");
        }
    }

    /**     * 历史命令     */
    private Stack<String> history = new Stack<>();
    /**     * 当前执行任务对应的 FutureTask     */
    private FutureTask<Void> task;
    /**     * 当前任务对应的 ChannelStatus     */
    private ChannelStatus channelStatus;

    public ChannelStatus getChannelStatus() {
        return channelStatus;
    }

    public void setChannelStatus(ChannelStatus channelStatus) {
        this.channelStatus = channelStatus;
    }

    public Stack<String> getHistory() {
        return history;
    }

    public FutureTask<Void> getTask() {
        return task;
    }

    public void setTask(FutureTask<Void> task) {
        this.task = task;
    }
}
