package org.inchain.queue.impl;

import net.apexes.fqueue.FQueue;
import net.apexes.fqueue.exception.FileFormatException;
import org.inchain.queue.PersistentQueue;
import org.inchain.queue.util.SerializeUtils;

import java.io.IOException;

/**
 * 用Fqueue实现的持久化队列
 * Created by Niels on 2017/9/20.
 */
public class InchainFQueue<T> extends PersistentQueue<T> {

    private FQueue queue = null;

    /**
     * 创建队列
     *
     * @param queueName 队列名称
     * @param maxSize   单个文件最大大小fileLimitLength
     * @throws IOException
     * @throws FileFormatException
     */
    public InchainFQueue(String queueName, long maxSize) throws IOException, FileFormatException {
        this.queueName = "queue/" + queueName;
        this.maxSize = maxSize;
        this.queue = new FQueue(this.queueName, maxSize);
    }

    @Override
    public void offer(T t) {
        if (null == t || this.queue == null) {
            return;
        }
        byte[] obj = SerializeUtils.ObjectToByte(t);
        this.queue.offer(obj);
    }

    @Override
    public T poll() {
        byte[] obj = this.queue.poll();
        if (null == obj) {
            return null;
        }
        Object value = SerializeUtils.ByteToObject(obj);
        return (T) value;
    }

    @Override
    public long size() {
        return this.queue.size();
    }

    public void close() throws IOException, FileFormatException {
        this.queue.close();
    }

    public void clear() {
        this.queue.clear();
    }

    @Override
    public void distroy() {
        if (null == queue) {
            return;
        }
        this.queue.clear();
        try {
            this.queue.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FileFormatException e) {
            e.printStackTrace();
        }
        //TODO 删除磁盘文件
    }

}
