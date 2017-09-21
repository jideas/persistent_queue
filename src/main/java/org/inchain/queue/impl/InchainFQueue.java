package org.inchain.queue.impl;

import net.apexes.fqueue.FQueue;
import net.apexes.fqueue.exception.FileFormatException;
import org.inchain.queue.PersistentQueue;
import org.inchain.queue.util.SerializeUtils;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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
        if (size() > 0)
            signalNotEmpty();
    }

    @Override
    public T poll() {
        byte[] obj = this.queue.poll();
        if (null == obj) {
            return null;
        }
        Object value = SerializeUtils.ByteToObject(obj);
        try {
            return (T) value;
        } finally {
            if (size() > 0)
                signalNotEmpty();
        }

    }

    /**
     * Lock held by take, poll, etc
     */
    private final ReentrantLock takeLock = new ReentrantLock();

    /**
     * Wait queue for waiting takes
     */
    private final Condition notEmpty = takeLock.newCondition();

    public T take() throws InterruptedException {
        T x;
        final ReentrantLock takeLock = this.takeLock;
        takeLock.lockInterruptibly();
        try {
            while (size() == 0) {
                notEmpty.await();
            }
            x = poll();
            if (null == x) {
                return take();
            }
            if (size() > 0)
                notEmpty.signal();
        } finally {
            takeLock.unlock();
        }
        return x;
    }

    private void signalNotEmpty() {
        final ReentrantLock takeLock = this.takeLock;
        takeLock.lock();
        try {
            notEmpty.signal();
        } finally {
            takeLock.unlock();
        }
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
            //TODO 删除文件
            File file = new File(this.queueName);
            this.deleteFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FileFormatException e) {
            e.printStackTrace();
        }
    }

    private void deleteFile(File file) {
        if (null != file && file.exists()) {//判断文件是否存在
            if (file.isFile()) {//判断是否是文件
                boolean b = file.delete();//删除文件
                System.out.println("删除文件:" + file.getPath() + "，结果：" + b);
            } else if (file.isDirectory()) {//否则如果它是一个目录
                File[] files = file.listFiles();//声明目录下所有的文件 files[];
                for (File f : files) {//遍历目录下所有的文件
                    this.deleteFile(f);//把每个文件用这个方法进行迭代
                }
                boolean b = file.delete();//删除文件夹
                System.out.println("删除文件:" + file.getPath() + "，结果：" + b);
            }
        }
    }

}
