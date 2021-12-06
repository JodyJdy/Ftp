package ftp.status;/**
 * * @date 2021/12/3
 **/

/** * 用于在服务端记录客户端的状态， 每个Channel有一个ClientStatus与之对应 */
public class ServerClientStatus extends Status {
    public ServerClientStatus() {
        super.dir = "D:\\test\\server";
    }
}
