import ftp.ftpserver.FtpServer;

public class TestServer {
    public static void main(String[] args)  {
        new FtpServer(9900).start();
    }
}
