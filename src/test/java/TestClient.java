import ftp.ftpclient.FtpClient;

public class TestClient {
    public static void main(String[] args) {
        new FtpClient(9900, "localhost", 5).start();
    }
}
