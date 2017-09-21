package org.inchain.queue.manager;

import net.apexes.fqueue.exception.FileFormatException;
import org.inchain.queue.PersistentQueue;
import org.inchain.queue.exception.QueueException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 持久化队列管理器
 * Created by Niels on 2017/9/20.
 */
public abstract class QueueManager {
    private static Logger log = LoggerFactory.getLogger(QueueManager.class);
    private static final Map<String, PersistentQueue> queuesMap = new HashMap<>();
    private static final Map<String, Lock> lockMap = new HashMap<>();

    /**
     * 将队列加入管理中
     *
     * @param queueName 队列名称
     * @param queue     队列实例
     */
    public static void initQueue(String queueName, PersistentQueue queue) {
        if (queuesMap.containsKey(queueName)) {
            throw new QueueException("队列名称已存在");
        }
        log.debug("队列初始化，名称：{}，单个文件最大大小：{}", queue.getQueueName(), queue.getMaxSize());
        queuesMap.put(queueName, queue);
        lockMap.put(queueName, new ReentrantLock());
    }

    public static void destroyQueue(String queueName) {
        PersistentQueue queue = queuesMap.get(queueName);
        if (null == queue) {
            throw new QueueException("队列不存在");
        }
        queue.distroy();
        queuesMap.remove(queueName);
        log.debug("队列销毁，名称：{}。", queueName);
    }

    public static Object take(String queueName) {
        //TODO 待实现
//        final Lock lock = lockMap.get(queueName);
//        lock.lock();
//        try {
//            E x;
//            while ( (x = unlinkFirst()) == null)
//                notEmpty.await();
//            return x;
//        } finally {
//            lock.unlock();
//        }
        return null;
    }

    public static Object poll(String queueName) {
        PersistentQueue queue = queuesMap.get(queueName);
        if (null == queue) {
            throw new QueueException("队列不存在");
        }
        try {
            return queue.poll();
        } finally {
            log.debug("从队列中取出数据，名称：{}，当前长度：{}。", queueName, queue.size());
        }
    }

    public static void offer(String queueName, Object item) {
        PersistentQueue queue = queuesMap.get(queueName);
        if (null == queue) {
            throw new QueueException("队列不存在");
        }

        queue.offer(item);
        log.debug("向队列中加入数据，名称：{}，当前长度：{}。", queueName, queue.size());
    }

    public static void clear(String queueName) {
        PersistentQueue queue = queuesMap.get(queueName);
        if (null == queue) {
            throw new QueueException("队列不存在");
        }

        log.debug("清空队列数据，名称：{}，当前长度：{}。", queueName, queue.size());
        queue.clear();
    }

    public static void close(String queueName) throws IOException, FileFormatException {
        PersistentQueue queue = queuesMap.get(queueName);
        if (null == queue) {
            throw new QueueException("队列不存在");
        }

        queue.close();
        log.debug("关闭队列实例，名称：{}，当前长度：{}。", queueName, queue.size());
    }

    public static long size(String queueName) {
        PersistentQueue queue = queuesMap.get(queueName);
        if (null == queue) {
            throw new QueueException("队列不存在");
        }
        return queue.size();
    }

    public static long getMaxSize(String queueName) {
        PersistentQueue queue = queuesMap.get(queueName);
        if (null == queue) {
            throw new QueueException("队列不存在");
        }
        return queue.getMaxSize();
    }
}
