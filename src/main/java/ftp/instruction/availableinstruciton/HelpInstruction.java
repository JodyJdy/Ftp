package ftp.instruction.availableinstruciton;

import ftp.TransStruct;
import ftp.instruction.Instruction;
import ftp.status.Status;

/**
 *  help指令
 **/
public class HelpInstruction extends Instruction {
    public HelpInstruction(String ins, Status status) {
        super(ins, status);
    }

    @Override
    public TransStruct execute() {

        System.out.println( "数据操作的命令加上 local，操作的是本地文件夹，否则是ftpServer的文件夹，上传下载命令不支持加上local\n" +
                "\n" +
                "* ls\n" +
                " 列出当前目录内容  \n" +
                "  ls dir 列出指定目录内容         \n" +
                "* rm\n" +
                "  rm file 删除文件\n" +
                "  rm dir  删除目录\n" +
                "* cp\n" +
                "  cp file dir 拷贝文件到指定目录\n" +
                "* pwd 显示当前所在目录\n" +
                "* down  下载文件\n" +
                "   down srcFile dstDir 下载文件到本地指定目录\n" +
                "   down srcFile   下载文件到程序执行目录\n" +
                "* up 上传文件\n" +
                "   up srcFile dstDir 上传文件到server指定目录\n" +
                "   up srcFile  上传文件到server程序执行目录\n" +
                "* cd  切换目录\n" +
                "   cd .. 回到上一层目录\n" +
                "   cd dir 到指定的目录\n" +
                "* updir 上传目录\n" +
                "   updir srcDir dstDir 上传目录到server指定目录\n" +
                "   updir srcDir  上传目录到server程序执行目录\n" +
                "* downdir 下载目录\n" +
                "   downdir srdDir dstDir 下载目录到本地指定目录\n" +
                "   downdir srcDir 下载目录到程序执行目录\n" +
                "   \n" +
                "* history  指令历史\n" +
                "* help 打印帮助信息");
        return null;
    }
}
