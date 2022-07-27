package ftp.ftpclient;/**
 * * @date 2021/12/6
 **/

import ftp.ftpclient.trans.FileTransTaskQueue;
import ftp.status.ClientStatus;
import io.netty.channel.Channel;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;

/** * 存储客户端对应的 上下文信息，
 *只有一份
 *
 * */
public class ClientContext {
    /**
     * 只进行指令的传输， 不传输文件
     */
    private static Channel communicateChannel;
    /**
     * 指令传输的Channel对应的 ClientStatus
     */
    private static ClientStatus clientStatus;
    private static FileTransTaskQueue taskQueue;
    /**     客户端id     */
    private static String id = UUID.randomUUID().toString();


    /**
     * 传输文件用的ClientStatus
     */
    private static List<ClientStatus> transClientStatus;
    /**
     * 空闲的传输客户端
     */
    private static LinkedBlockingQueue<Integer> idleClient = new LinkedBlockingQueue<>();

   static void setTransClientStatus(List<ClientStatus> transClientStatus){
      ClientContext.transClientStatus = transClientStatus;
      for(int i=0;i<transClientStatus.size();i++){
          idleClient.add(i);
      }
   }

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
    /**
     * 获取一个空闲的客户端
     */
    public static ClientStatus getIdleClient() throws InterruptedException {
        //阻塞式获取一个空闲Channel
        int id = idleClient.take();
        return transClientStatus.get(id);
    }

    /**
     * 归还一个客户端到池子里面
     */
    public static void backClient(ClientStatus clientStatus) {
        idleClient.add(transClientStatus.indexOf(clientStatus));
    }
}
