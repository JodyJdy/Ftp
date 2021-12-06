package ftp.status;/**
 * * @date 2021/12/3
 **/

import java.io.File;
import java.io.IOException;

/** * 用于在服务端记录客户端的状态， 每个Channel有一个ClientStatus与之对应 */
public class ServerClientStatus extends Status {
    public ServerClientStatus() {
        try {
            super.dir = new File(".").getCanonicalPath();
        } catch (IOException e) {
            super.dir = System.getProperty("user.dir");
        }
    }
}
