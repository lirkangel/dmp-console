import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

public class FileSplitter implements QueueHandle {
    private BlockingQueue<String> linesQueue = new ArrayBlockingQueue<>(30);
    private Map<String, File> tempFiles = new ConcurrentHashMap<>();
    private final Set<String> exhaustedTempFiles = ConcurrentHashMap.newKeySet();
    private final Map<String, Set<File>> sameFirstCharFilename = new ConcurrentHashMap<>();
    private boolean isReaderDone = false;
    private static AtomicLong count = new AtomicLong(0);
    private static Integer NR_WRITER_THREADS = Integer.valueOf(ConfigProperties.NR_WRITER_THREADS.value());


    public Map<String, File> split() {
        createTempDir();
        String filename = ConfigProperties.FILENAME.value();
        ExecutorService readerPool = Executors.newFixedThreadPool(1);
        Future<Boolean> readerFuture = readerPool.submit(getFileReader(filename));
        readerPool.shutdown();

        ExecutorService writerPool = Executors.newFixedThreadPool(NR_WRITER_THREADS);
        List<Future> futures = new ArrayList();
        for (int i = 0; i < NR_WRITER_THREADS; i++) {
            futures.add(writerPool.submit(getFileSplitterWriter()));
        }
        writerPool.shutdown();

        setIsReaderDone(AsyncHelper.waitExecution(readerFuture));
        AsyncHelper.waitExecution(futures);
        print();
        return tempFiles;
    }


    protected synchronized void checkSameFirstCharFilename(File file) {
        String firstChar = ""+file.getName().charAt(0);
        Set<File> temp = sameFirstCharFilename.get(firstChar);
        if (temp == null) {
            temp = ConcurrentHashMap.newKeySet();
        }
        temp.add(file);
        sameFirstCharFilename.put(firstChar, temp);
    }

   
    public void addTempFile(String filename, File file) {
        if (!tempFiles.containsKey(filename)) {
            tempFiles.put(filename, file);
        }
    }

    private void createTempDir() {
        File tempDir = Paths.get(ConfigProperties.TEMP_FILES_DIR.value()).toFile();
        if (!tempDir.exists()) {
            tempDir.mkdirs();
        }
    }

    private void print() {
        System.out.println("Lines processed: " + getCount().get());
        System.out.println("Number of temp files: " + tempFiles.size());
    }

    private FileReader getFileReader(String filename) {
        return new FileReader(this, filename);
    }

    private FileSplitterWritering getFileSplitterWriter() {
        return new FileSplitterWritering(this);
    }

    public BlockingQueue<String> getLinesQueue() {
        return linesQueue;
    }

    public void setIsReaderDone(boolean isReaderDone) {
        this.isReaderDone = isReaderDone;
    }

    public boolean isReaderDone() {
        return isReaderDone;
    }

    public void increment() {
        count.incrementAndGet();
    }

    public static AtomicLong getCount() {
        return count;
    }


    public Map<String, File> getTempFiles() {
        return tempFiles;
    }

    @Override
    public void addLineToQueue(String line) throws InterruptedException {
        linesQueue.put(line);
    }

    public void addExhaustedFile(String filename) {
        exhaustedTempFiles.add(filename);
    }

    public boolean isFileExhausted(String filename) {
        return exhaustedTempFiles.contains(filename);
    }

    public Map<String, Set<File>> getSameFirstCharFilename() {
        return sameFirstCharFilename;
    }
}

