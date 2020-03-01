package com.example;
import com.example.externalsort.*;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class MainApplication {
    public static void main(String[] args) {

            MainApplication app = new MainApplication();
            app.execute();



    }

    protected void execute() {
        System.out.println("Running External Sorting!");
        System.out.println(Runtime.getRuntime().totalMemory());
        FileSplitter fileSplitter = new FileSplitter();
        Map<String, File> tempFiles = fileSplitter.split();

        // multithread read, sort in alphabetic order and override file
        FileSort fileSorter = new FileSort();
        fileSorter.sort(new HashSet<>(tempFiles.values()));

        MergeSort sorter = new MergeSort();
        List<String> orderedFiles = sorter.sort(new LinkedList<>(tempFiles.keySet()));
        orderedFiles = new FileMerge().doMerge(orderedFiles, fileSplitter.getSameFirstCharFilename());

        // read and merge
        List<File> files = getOrderedFileList(orderedFiles, tempFiles);
        FileWritering fileWritering = new FileWritering();
        fileWritering.mergeFiles(files);

        System.out.println("External Sorting successfully executed!");
    }

    protected List<File> getOrderedFileList(List<String> filenames, Map<String, File> tempFiles) {
        return filenames.stream().map(tempFiles::get).collect(Collectors.toCollection(() -> new LinkedList<>()));
    }
}


