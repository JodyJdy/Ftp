package ftp.instruction.availableinstruciton;
import ftp.TransStruct;
import ftp.instruction.Instruction;
import ftp.status.Status;
import ftp.util.ByteUtil;
import ftp.util.FileUtil;
import ftp.util.RegexUtil;
import ftp.util.StructTransUtil;

import java.io.File;
import java.util.List;

import static ftp.util.FileUtil.SPLIT;

/**
 * 目录文件指令  client -- > server
 */
public class LsInstruction extends Instruction {
    public LsInstruction(String ins, Status status) {
        super(ins, status);
    }

    @Override
    public TransStruct execute() {
        String[] splits = RegexUtil.splitInstruciton(ins);
        List<File> fileList;
        // ls的形式
        if (splits.length == 1) {
            fileList = FileUtil.lsFile(null, status);

        } else {
            // ls xxx的形式
            fileList = FileUtil.lsFile(splits[1], status);
        }
        if (fileList == null) {
            return StructTransUtil.generateResponse("目录不存在");
        }
        return StructTransUtil.generateResponse(transToResult(fileList));
    }

    private static String transToResult(List<File> files) {
        StringBuilder stringBuilder = new StringBuilder();
        for (File file : files) {
            if (file.isFile()) {
                stringBuilder.append("file:");
            } else {
                stringBuilder.append("dir:");
            }
            stringBuilder.append(file.getName()).append(SPLIT);
            if (file.isFile()) {
                stringBuilder.append(ByteUtil.getFileSize(file));
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    @Override
    public void preProcess() {
        super.preProcess();
    }
}
