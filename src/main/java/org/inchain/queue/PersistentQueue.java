package org.inchain.queue;

import net.apexes.fqueue.exception.FileFormatException;
import org.inchain.queue.util.stat.StatInfo;

import java.io.IOException;

/**
 * 持久化队列定义
 * Created by Niels on 2017/9/20.
 */
public abstract class PersistentQueue<T> {

    protected String queueName;
    protected long maxSize;
    private StatInfo statInfo;

    public final String getQueueName() {
        return queueName;
    }

    /**
     * @return 初始化时设置的最大长度
     */
    public final long getMaxSize() {
        return maxSize;
    }


    /**
     * 向队列中加入数据
     *
     * @param t 数据对象
     */
    public abstract void offer(T t);

    /**
     * 从队列中取出数据，若没有数据则阻塞
     *
     * @return 数据对象
     */
    public abstract T poll();

    public abstract T take() throws InterruptedException;

    /**
     * @return 队列当前长度
     */
    public abstract long size();

    /**
     * 销毁改队列，包括删除磁盘文件
     */
    public abstract void distroy() throws IOException, FileFormatException;

    /**
     * 关闭队列
     *
     * @throws IOException
     * @throws FileFormatException
     */
    public abstract void close() throws IOException, FileFormatException;

    /**
     * 清空队列
     */
    public abstract void clear();

    public void setStatInfo(StatInfo statInfo) {
        this.statInfo = statInfo;
    }

    public StatInfo getStatInfo() {

        return statInfo;
    }
}
