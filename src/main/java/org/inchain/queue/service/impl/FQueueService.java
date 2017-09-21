package org.inchain.queue.service.impl;

import net.apexes.fqueue.exception.FileFormatException;
import org.inchain.queue.impl.InchainFQueue;
import org.inchain.queue.manager.QueueManager;
import org.inchain.queue.service.QueueService;
import org.inchain.queue.util.stat.StatInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * 队列服务类
 * Created by Niels on 2017/9/20.
 */
@Service
public class FQueueService<T> implements QueueService<T> {
    private static Logger log = LoggerFactory.getLogger(FQueueService.class);

    /**
     * 创建一个持久化队列
     *
     * @param queueName 队列名称
     * @param maxSize   单个文件最大大小fileLimitLength
     * @return 是否创建成功
     */
    public boolean createQueue(String queueName, long maxSize) {
        return createQueue(queueName, maxSize, 0);
    }

    /**
     * 创建一个持久化队列
     *
     * @param queueName 队列名称
     * @param maxSize   单个文件最大大小fileLimitLength
     * @param latelySecond 统计日志打印时间段
     * @return 是否创建成功
     */
    public boolean createQueue(String queueName, long maxSize, int latelySecond) {
        try {
            InchainFQueue queue = new InchainFQueue(queueName, maxSize);
            QueueManager.initQueue(queueName, queue, latelySecond);
            return true;
        } catch (Exception e) {
            log.error("", e);
            return false;
        }
    }

    @Override
    public boolean destroyQueue(String queueName) {
        try {
            QueueManager.destroyQueue(queueName);
        } catch (Exception e) {
            log.error("", e);
            return false;
        }
        return true;
    }

    @Override
    public T poll(String queueName) {
        Object data = QueueManager.poll(queueName);
        return (T) data;
    }

    @Override
    public T take(String queueName) throws InterruptedException {
        Object data = QueueManager.take(queueName);
        return (T) data;
    }

    @Override
    public void offer(String queueName, T item) {
        QueueManager.offer(queueName, item);
    }

    @Override
    public long size(String queueName) {
        return QueueManager.size(queueName);
    }

    @Override
    public long getMaxSize(String queueName) {
        return QueueManager.getMaxSize(queueName);
    }

    @Override
    public void clear(String queueName) {
        QueueManager.clear(queueName);
    }

    @Override
    public void close(String queueName) throws IOException, FileFormatException {
        QueueManager.close(queueName);
    }

    @Override
    public StatInfo getStatInfo(String queueName) {
        return QueueManager.getStatInfo(queueName);
    }
}
