# Ftp
a project about FtpClient,FtpExchanger,FtpServer

FtpClient可以从FtpServer上传或下载文件以及目录。
  支持以命令行的形式访问FtpServer上的文件，例如 ls,cd,rm,cp等等。
  
FtpExchanger可以用来中转数据，对于FtpClient与FptServer之间不能直接访问时，可以使用FtpExchanger中转
   FtpClient <--> FtpExchanger <--> FtpExchanger <--> FtpServer
   
   
## 支持命令

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
cd  切换目录
   cd .. 回到上一层目录
   cd dir 到指定的目录
updir 上传目录
   updir srcDir dstDir 上传目录到server指定目录
   updir srcDir  上传目录到server程序执行目录
downdir 下载目录
   downdir srdDir dstDir 下载目录到本地指定目录
   downdir srcDir 下载目录到程序执行目录
                                            
