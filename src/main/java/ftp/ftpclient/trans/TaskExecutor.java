package ftp.ftpclient.trans;

import ftp.ftpclient.ClientContext;
import ftp.instruction.Instruction;
import ftp.instruction.InstructionResolver;
import ftp.status.ClientStatus;
import ftp.util.TransStructUtil;

/**
 * 传输任务执行器
 **/
public class TaskExecutor extends Thread {
    private FileTransTaskQueue queue;

    public TaskExecutor(FileTransTaskQueue queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                FileTransTask task = queue.getFileTransTask();
                if (task == null) {
                    continue;
                }
                //获取空闲的客户端
                ClientStatus client = ClientContext.getIdleClient();
                if (client == null) {
                    queue.addFileTransTask(task);
                    continue;
                }
                //设置客户端的任务
                client.setTask(task.getFutureTask());
                String ins = task.getIns();
                //设置工作信道使用的路径
                client.setDir(ClientContext.getClientStatus().getDir());
                Instruction instruction = InstructionResolver.resolver(ins, client);
                // 执行传输任务
                instruction.preProcess();
                client.getChannel().writeAndFlush(TransStructUtil.generateInsStruct(ins)).sync();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
