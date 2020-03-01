package com.example.externalsort;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Callable;

public class FileReader  implements Callable<Boolean> {
    private QueueHandle fileHandler;
    private String filename;

    public FileReader(QueueHandle fileHandler, String filename) {
        this.fileHandler = fileHandler;
        this.filename = filename;
    }

    @Override
    public Boolean call() {
        return execute();
    }


    public Boolean execute() {
        Path path = Paths.get(filename);
        try (BufferedReader br = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            String line;
            while ((line = br.readLine()) != null ) {
                fileHandler.addLineToQueue(line);
            }
        } catch (Exception e) {
            throw new  RuntimeException("Unexpected error occured!", e);
        }
        return Boolean.TRUE;
    }
}