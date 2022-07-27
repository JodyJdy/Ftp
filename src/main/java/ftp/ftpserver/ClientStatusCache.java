package ftp.ftpserver;/**
 * * @date 2021/12/6
 **/

import ftp.status.ServerClientStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/** * 客户端状态缓存，每个客户端进程都要一个唯一的标识 */
public class ClientStatusCache {
    private static Map<String, List<ServerClientStatus>> statusMap = new ConcurrentHashMap<>(16);

    public static synchronized void addStatus(String key, ServerClientStatus serverClientStatus) {
        List<ServerClientStatus> statusList;
        if (!statusMap.containsKey(key)) {
            statusList = new ArrayList<>();
            statusMap.put(key, statusList);
        } else {
            statusList = statusMap.get(key);
        }
        statusList.add(serverClientStatus);
    }

    public static List<ServerClientStatus> getStatus(String key) {
        return statusMap.get(key);
    }
}
