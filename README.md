# Ftp
a project about FtpClient,FtpExchanger,FtpServer

FtpClient可以从FtpServer上传或下载文件以及目录。
  支持以命令行的形式访问FtpServer上的文件，例如 ls,cd,rm,cp等等。
  
FtpExchanger可以用来中转数据，对于FtpClient与FptServer之间不能直接访问时，可以使用FtpExchanger中转
   FtpClient <--> FtpExchanger <--> FtpExchanger <--> FtpServer
