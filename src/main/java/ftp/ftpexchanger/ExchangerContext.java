package ftp.ftpexchanger;

import io.netty.bootstrap.Bootstrap;

/**
 *  记录FtpExchange相关的环境信息
 **/
public class ExchangerContext {
    private static Bootstrap bootstrap;

    public static Bootstrap getBootstrap() {
        return bootstrap;
    }

    public static void setBootstrap(Bootstrap bootstrap) {
        if (ExchangerContext.bootstrap != null) {
            return;
        }
        ExchangerContext.bootstrap = bootstrap;
    }
}
