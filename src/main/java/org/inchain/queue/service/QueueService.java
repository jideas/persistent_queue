package org.inchain.queue.service;

import net.apexes.fqueue.exception.FileFormatException;

import java.io.IOException;

/**
 * 提供给外部使用的接口
 * Created by Niels on 2017/9/20.
 */
public interface QueueService<T> {
    /**
     * 创建一个持久化队列
     *
     * @param queueName 队列名称
     * @param maxSize   单个文件最大大小fileLimitLength
     * @return 是否创建成功
     */
    public boolean createQueue(String queueName, long maxSize);

    /**
     * 销毁该队列，并删除磁盘文件
     *
     * @param queueName 队列名称
     * @return
     */
    public boolean destroyQueue(String queueName);

    /**
     * 从队列中取出一个数据
     *
     * @param queueName 队列名称
     * @return
     */
    T poll(String queueName);

    /**
     * 向队列中加入一条数据
     *
     * @param queueName 队列名称
     * @param item      数据对象
     */
    void offer(String queueName, T item);

    /**
     * @return 当前长度
     */
    public long size(String queueName);

    /**
     * @return 初始化时设置的单个文件最大大小
     */
    public long getMaxSize(String queueName);

    void clear(String queueName);

    void close(String queueName) throws IOException, FileFormatException;
}
