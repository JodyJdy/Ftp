package ftp.ftpclient.trans;

import io.netty.channel.Channel;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * * channel的池
 **/
public class ChannelPool {
    /**
     * 空闲channel的id
     */
    private LinkedBlockingQueue<Integer> idleChannel = new LinkedBlockingQueue<>();
    /**
     * Channel 到 id的映射
     */
    private Map<Channel, Integer> channelToId = new ConcurrentHashMap<>();
    /**
     * id 到 ChannelStatus的映射
     */
    private Map<Integer, ChannelStatus> idToChannelStatus = new ConcurrentHashMap<>();
    private AtomicInteger integer = new AtomicInteger(0);

    public ChannelPool(List<ChannelStatus> statusList) {
        for (ChannelStatus status : statusList) {
            int i = integer.incrementAndGet();
            channelToId.put(status.getChannel(), i);
            idleChannel.add(i);
            idToChannelStatus.put(i, status);
            status.setChannelPool(this);
        }
    }

    /**
     * 获取一个空闲的channelStatus
     */
    ChannelStatus getIdleChannel() throws InterruptedException {
       //阻塞式获取一个空闲Channel
        int id = idleChannel.take();
        return idToChannelStatus.get(id);
    }

    /**
     * 归还一个channel到池子里去
     */
    void backChannel(Channel channel) {
        idleChannel.add(channelToId.get(channel));
    }
}
