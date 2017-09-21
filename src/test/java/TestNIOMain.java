import java.io.File;
import java.io.RandomAccessFile;
import java.lang.reflect.Method;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by Niels on 2017/9/21.
 * inchain.org
 */
public class TestNIOMain {

    public static void main(String[] args) throws Exception {
        File f = new File("test.f");
        RandomAccessFile raf = new RandomAccessFile(f, "rwd");
        FileChannel fc = raf.getChannel();
        MappedByteBuffer mappedByteBuffer = fc.map(FileChannel.MapMode.READ_WRITE, 0, 1024);
        mappedByteBuffer.put("Niels Wang".getBytes());
        mappedByteBuffer.force();
        mappedByteBuffer.clear();
        clean(mappedByteBuffer);
        fc.close();
        raf.close();
        mappedByteBuffer = null;
        fc = null;
        raf = null;
        Thread.sleep(10000l);
        f.deleteOnExit();
    }

    public static void clean(final Object buffer) throws Exception {
        try {
            Method getCleanerMethod = buffer.getClass().getMethod("cleaner", new Class[0]);
            getCleanerMethod.setAccessible(true);
            sun.misc.Cleaner cleaner = (sun.misc.Cleaner) getCleanerMethod.invoke(buffer, new Object[0]);
            cleaner.clean();
            getCleanerMethod.setAccessible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
