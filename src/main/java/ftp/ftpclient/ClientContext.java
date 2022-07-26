package ftp.ftpclient;/**
 * * @date 2021/12/6
 **/

import ftp.ftpclient.trans.FileTransTaskQueue;
import ftp.status.ClientStatus;
import io.netty.channel.Channel;

import java.util.UUID;

/** * 存储客户端对应的 上下文信息，
 *只有一份
 *
 * @// TODO: 2022/7/26   期望使用 ClientContext 解决 工作Channel与ClientStatus的映射关系
 * */
public class ClientContext {
    /**
     * 只进行指令的传输， 不传输文件
     */
    private static Channel communicateChannel;
    private static ClientStatus clientStatus;
    private static FileTransTaskQueue taskQueue;
    /**     * 客户端id     */
    private static String id = UUID.randomUUID().toString();

    static Channel getCommunicateChannel() {
        return communicateChannel;
    }

    static void setCommunicateChannel(Channel communicateChannel) {
        if (ClientContext.communicateChannel != null) {
            return;
        }
        ClientContext.communicateChannel = communicateChannel;
    }

    public static ClientStatus getClientStatus() {
        return clientStatus;
    }

    static void setClientStatus(ClientStatus clientStatus) {
        if (ClientContext.clientStatus != null) {
            return;
        }
        ClientContext.clientStatus = clientStatus;
    }

    public static FileTransTaskQueue getTaskQueue() {
        return taskQueue;
    }

    static void setTaskQueue(FileTransTaskQueue taskQueue) {
        if (ClientContext.taskQueue != null) {
            return;
        }
        ClientContext.taskQueue = taskQueue;
    }

    public static String getId() {
        return id;
    }
}
