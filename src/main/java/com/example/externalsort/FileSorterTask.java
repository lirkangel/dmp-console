package com.example.externalsort;

import java.io.File;
import java.util.LinkedList;
import java.util.concurrent.Callable;

public class FileSorterTask implements Callable<Boolean>, QueueHandle {

    private LinkedList<String> queue = new LinkedList<>();
    private File file;


    public FileSorterTask(File file) {
        this.file = file;
    }

    @Override
    public Boolean call() throws Exception {
        new FileReader(this, file.getAbsolutePath()).execute();

        MergeSort sorter = new MergeSort();
        queue = sorter.sort(queue);
        new FileWritering().writeLines(queue, file, false);
        return Boolean.TRUE;
    }

    @Override
    public void addLineToQueue(String line) throws InterruptedException {
        queue.add(line);
    }
}
