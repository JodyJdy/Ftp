package ftp.ftpclient.trans;

import ftp.ftpclient.ClientContext;
import ftp.instruction.Instruction;
import ftp.instruction.InstructionResolver;
import ftp.status.ClientStatus;
import ftp.util.StructTransUtil;

/**
 * 传输任务执行器
 **/
public class TaskExecutor extends Thread {
    private FileTransTaskQueue queue;
    private ChannelPool channelPool;

    public TaskExecutor(FileTransTaskQueue queue, ChannelPool pool) {
        this.queue = queue;
        this.channelPool = pool;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                FileTransTask task = queue.getFileTransTask();
                if (task == null) {
                    continue;
                }
                ChannelStatus c = channelPool.getIdleChannel();
                if (c == null) {
                    queue.addFileTransTask(task);
                    continue;
                }
                ClientStatus workStatus = c.getClientStatus();
                workStatus.setTask(task.getFutureTask());
                String ins = task.getIns();
                //设置工作信道使用的路径
                workStatus.setDir(ClientContext.getClientStatus().getDir());
                Instruction instruction = InstructionResolver.resolver(ins, workStatus);
                // 执行传输任务
                instruction.process();
                c.getChannel().writeAndFlush(StructTransUtil.generateInsStruct(ins)).sync();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
