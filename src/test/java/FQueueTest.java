import junit.framework.TestCase;
import org.inchain.queue.service.QueueService;
import org.inchain.queue.service.QueueServiceFactory;
import org.inchain.queue.service.impl.FQueueService;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Niels on 2017/9/21.
 */
public class FQueueTest extends TestCase {

    protected Logger log = LoggerFactory.getLogger(getClass());
    private final String queueName = "test1";
    private QueueService<Long> service = QueueServiceFactory.createQueueService();


    @Test
    public void test() {
        //创建
        boolean b = service.createQueue(queueName, 1000);
        assertTrue(b);

        //写入
        int count = 10;
        long start = System.currentTimeMillis();
        for (; count >= 0; count--) {
            service.offer(queueName, count - 1l);
        }
        log.info("offer count=" + count + ",use time(ms):" + (System.currentTimeMillis() - start));
        assertTrue(true);

        //取出
        while (true) {
            log.info("start poll....");

            Long data = null;
//            try {
//                data = service.take(queueName);
//            } catch (InterruptedException e) {
//                log.error("", e);
//            }
            data = service.poll(queueName);
            if (data == null) {
                break;
            }
            log.info("poll data:" + data);
        }


        service.destroyQueue(queueName);
        assertTrue(true);
    }


}
