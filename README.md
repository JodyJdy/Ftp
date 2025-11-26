# Ftp

 项目包含的功能有：
 * FtpClient  连接FtpServer，上传/下载文件或目录,支持以命令行的形式访问FtpServer上的文件，例如 ls,cd,rm,cp等等
 * FtpExchanger 不用于存储任何文件可以用来中转数据，对于FtpClient与FptServer之间不能直接访问时，可以使用FtpExchanger中转
   FtpClient <--> FtpExchanger <--> FtpExchanger <--> FtpServer
 * FtpServer  接受多个FtpClient的连接，FtpClient可以将文件上传到FtpServer



   
## 启动

### server启动
```java

public class TestServer {
    public static void main(String[] args) {
        new FtpServer(9900).start();
    }
}

```
### client启动
```java

public class TestClient {
    public static void main(String[] args) {
        //指定服务端ip:port,(也可以是exchanger的ip:port) 以及用于传输文件的信道的数量
        new FtpClient(9900, "localhost", 5).start();
    }
}
```
### exchanger启动
```java

public class TestExchanger {
    public static void main(String[] args) {
        // 指定当前服务监听的端口: 8888, 服务端端口: 9999  服务端ip: localhost
        new FtpExchanger(8888, 9999, "localhost").start();
    }
}
```
   
## 客户端支持命令

数据操作的命令加上 local，操作的是本地文件夹，否则是ftpServer的文件夹，上传下载命令不支持加上local

* ls
 列出当前目录内容  
  ls dir 列出指定目录内容         
* rm
  rm file 删除文件
  rm dir  删除目录
* cp
  cp file dir 拷贝文件到指定目录
* pwd 显示当前所在目录
* down  下载文件
   down srcFile dstDir 下载文件到本地指定目录
   down srcFile   下载文件到程序执行目录
* up 上传文件
   up srcFile dstDir 上传文件到server指定目录
   up srcFile  上传文件到server程序执行目录
* cd  切换目录
   cd .. 回到上一层目录
   cd dir 到指定的目录
* updir 上传目录
   updir srcDir dstDir 上传目录到server指定目录
   updir srcDir  上传目录到server程序执行目录
* downdir 下载目录
   downdir srdDir dstDir 下载目录到本地指定目录
   downdir srcDir 下载目录到程序执行目录
   
* history  指令历史
* help 打印帮助信息
                                            
