package org.inchain.queue.service;

import org.inchain.queue.service.impl.FQueueService;

/**
 * 队列服务类工厂
 * Created by Niels on 2017/9/21.
 * inchain.org
 */
public class QueueServiceFactory {


    /**
     * 创建QueueService实例
     *
     * @return
     */
    public static final QueueService createQueueService() {
        return createQueueService("FQueue");
    }

    /**
     * 创建QueueService实例
     *
     * @param type 目前只支持FQueue类型
     * @return
     */
    public static final QueueService createQueueService(String type) {
        switch (type) {
            case "FQueue":
                return new FQueueService();
            default:
                return new FQueueService();
        }
    }
}
