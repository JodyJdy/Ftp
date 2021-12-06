package ftp.ftpclient;/**
 * * @date 2021/12/6
 **/

import ftp.ftpclient.trans.FileTransTaskQueue;
import ftp.status.ClientStatus;
import io.netty.channel.Channel;

import java.util.UUID;

/** * 存储客户端对应的 上下文信息，只有一份 */
public class ClientContext {
    private static Channel communicate;
    private static ClientStatus clientStatus;
    private static FileTransTaskQueue taskQueue;
    /**     * 客户端id     */
    private static String id = UUID.randomUUID().toString();

    static Channel getCommunicate() {
        return communicate;
    }

    static void setCommunicate(Channel communicate) {
        if (ClientContext.communicate != null) {
            return;
        }
        ClientContext.communicate = communicate;
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
