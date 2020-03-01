import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FileSort {
    private static Integer NR_WRITER_THREADS = Integer.valueOf(ConfigProperties.NR_WRITER_THREADS.value());

    public void sort(Set<File> files) {
        ExecutorService writerPool = Executors.newFixedThreadPool(NR_WRITER_THREADS);
        List<Future> futures = new ArrayList();
        for (File file : files) {
            futures.add(writerPool.submit(new FileSorterTask(file)));
        }
        writerPool.shutdown();
        AsyncHelper.waitExecution(futures);
    }
}
