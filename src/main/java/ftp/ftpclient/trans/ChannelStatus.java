package ftp.ftpclient.trans;

import ftp.status.ClientStatus;
import io.netty.channel.Channel;

/**
 * * 记录channel以及channel的状态
 **/
public class ChannelStatus {
    private final ClientStatus clientStatus;
    private final Channel channel;
    private ChannelPool channelPool;

    void setChannelPool(ChannelPool channelPool) {
        this.channelPool = channelPool;
    }

    public ChannelStatus(ClientStatus clientStatus, Channel channel) {
        this.clientStatus = clientStatus;
        this.channel = channel;
        this.clientStatus.setChannelStatus(this);
    }

    ClientStatus getClientStatus() {
        return clientStatus;
    }

    public void backChannel() {
        channelPool.backChannel(channel);
    }

    Channel getChannel() {
        return channel;
    }

    @Override
    public int hashCode() {
        return channel.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof ChannelStatus)) {
            return false;
        }
        ChannelStatus o = (ChannelStatus) obj;
        return channel.equals(o.channel);
    }
}
