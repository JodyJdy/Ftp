package ftp.instruction.availableinstruciton;

import ftp.TransStruct;
import ftp.instruction.Instruction;
import ftp.status.Status;
import ftp.util.FileUtil;
import ftp.util.RegexUtil;
import ftp.util.TransStructUtil;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.Collection;

import static ftp.util.FileUtil.CUR_DIR;

/**
 * * 下载目录指令
 **/
public class DownDirInstruction extends Instruction {
    public DownDirInstruction(String ins, Status status) {
        super(ins, status);
    }

    @Override
    public TransStruct execute() {
        ins = ins.trim();
        String[] splits = RegexUtil.splitInstruciton(ins);
        //要下载的路径
        String dirPath;
        if (FileUtil.isAbsolutePath(splits[1])) {
            dirPath = splits[1];
        } else {
            dirPath = status.getDir() + File.separator + splits[1];
        }
        File dir = new File(dirPath);
        if (!dir.exists() || dir.isFile()) {
            return null;
        }
        Collection<File> fileCollection = FileUtils.listFiles(dir, null, true);
        StringBuilder sb = new StringBuilder();
        //客户端下载后放到默认路径
        if (splits.length == 2) {
            sb.append(CUR_DIR).append(" ");
        } else {
            //放到指定路径
            sb.append(splits[2]).append(" ");
        }
        fileCollection.forEach(file -> sb.append(file.getPath()).append(" "));
        return TransStructUtil.generateDownDirContent(sb.toString());
    }

    @Override
    public void preProcess() {
        super.preProcess();
    }
}
