import org.inchain.queue.service.QueueService;
import org.inchain.queue.service.QueueServiceFactory;

/**
 * Created by Niels on 2017/9/21.
 * inchain.org
 */
public class TestMain {

    public static void main(String[] args) {
        final QueueService<String> service = QueueServiceFactory.createQueueService();
        service.createQueue("test", 10 * 1024 * 1024);
        new Thread(new Runnable() {
            @Override
            public void run() {
                int index = 0;
                while (true) {
                    index++;
                    service.offer("test", "" + index);
//                    if (index % 4 == 0) {
//                        try {
//                            Thread.sleep(1000l);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        String val = service.poll("test");
                        if(null==val){
                            Thread.sleep(1l);
                            continue;
                        }
//                        System.out.println("1======" + val);
                    } catch ( Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                {
                    while (true) {
                        try {
                            String val = service.take("test");
//                            System.out.println("2======" + val);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                }

            }
        }).start();
    }
}
