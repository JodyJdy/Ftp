import ftp.ftpexchanger.FtpExchanger;

public class TestExchanger {
    public static void main(String[] args) {
        new FtpExchanger(8888, 9999, "localhost").start();
    }
}
