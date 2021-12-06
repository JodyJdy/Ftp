package ftp.ftpexchanger;

import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *  客户端与服务端之间(或者client与exchange，exchange与exchange,exchange与server)channel的映射
 **/
public class ChannelExchanger {
    Map<Channel, Channel> exchangerMap = new ConcurrentHashMap<>(16);

    public void addChannel(Channel a, Channel b) {
        exchangerMap.put(a, b);
        exchangerMap.put(b, a);
    }

    public Channel getChannel(Channel a) {
        return exchangerMap.get(a);
    }

    public boolean contains(Channel a) {
        return exchangerMap.containsKey(a);
    }
}
