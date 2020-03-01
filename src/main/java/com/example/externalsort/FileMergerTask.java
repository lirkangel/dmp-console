package com.example.externalsort;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;
import java.util.concurrent.Callable;


public class FileMergerTask implements Callable<Boolean> {

    private FileWritering fileWritering;
    private Set<File> fragments;
    private String filename;

    public FileMergerTask(Set<File> fragments, String filename) {
        this.fileWritering = new FileWritering();
        this.fragments = fragments;
        this.filename = filename;
    }


    @Override
    public Boolean call() throws Exception {
        executeKWayMerge(fragments, filename);
        return Boolean.TRUE;
    }


    public void executeKWayMerge(Set<File> files, String outputfile) {
        //create a temporary file
        File dir = Paths.get(ConfigProperties.TEMP_FILES_DIR.value(), "k-way").toFile();
        dir.mkdirs();
        File output = new File(dir, outputfile);

        try (BufferedWriter bw = new BufferedWriter(new java.io.FileWriter(output, false))) {
            SortList<CacheReader> autoOrderedList = getSortedList(files);
            mergeSortedLines(bw, autoOrderedList);
            bw.flush();
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error occured!", e);
        }
        fileWritering.delete(files);

        //move to outputfile
        File destine = Paths.get(ConfigProperties.TEMP_FILES_DIR.value(), outputfile+".txt").toFile();
        fileWritering.move(output, destine);
        dir.delete();
    }

    private void mergeSortedLines( BufferedWriter bw, SortList<CacheReader> autoOrderedList) throws IOException {
        Integer rowCount = 0;
        while (autoOrderedList.size() > 0) {
            CacheReader bfw = autoOrderedList.poll();
            String r = bfw.poll();
            bw.write(r);
            bw.newLine();
            rowCount++;
            if (bfw.isEmpty()) {
                bfw.close();
            } else {
                // add it back
                autoOrderedList.add(bfw);
            }
            if (rowCount % 1000 == 0) {
                bw.flush();
            }
        }
    }

    /**
     * Creates the sorted list based on files to be merged
     * 
     * @param files
     * @return
     * @throws IOException
     */
    private SortList<CacheReader> getSortedList(Set<File> files) throws IOException {
        SortList<CacheReader> autoOrderedList = new SortList();
        for (File file : files) {
            BufferedReader br = Files.newBufferedReader(file.toPath(), StandardCharsets.UTF_8);
            autoOrderedList.add(new CacheReader(br));
        }
        return autoOrderedList;
    }
}
