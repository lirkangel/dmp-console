import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FileMerge {
    private static Integer NR_WRITER_THREADS = Integer.valueOf(ConfigProperties.NR_WRITER_THREADS.value());

    public List<String> doMerge(List<String> sortedFiles, Map<String, Set<File>> filesToMerge) {
        List<String> toRemove = new ArrayList<>();

        ExecutorService mergerPool = Executors.newFixedThreadPool(NR_WRITER_THREADS);
        List<Future> futures = new ArrayList();

        for (String filename : sortedFiles) {
            Set<File> fragments = filesToMerge.get(filename);
            if (fragments != null) {
                if (fragments.size() > 1) {
                    futures.add(mergerPool.submit(new FileMergerTask(fragments, filename)));
                }
            } else {
                toRemove.add(filename);
            }
        }
        mergerPool.shutdown();
        AsyncHelper.waitExecution(futures);
        sortedFiles.removeAll(toRemove);
        return sortedFiles;
    }
}
