package com.example.externalsort;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class FileWritering {
    private static final Boolean append = Boolean.TRUE;

    public void writeLine(String line, File file, Boolean append) {
        writeLines(Arrays.asList(line), file, append);
    }

    public void appendLine(String line, File file) {
        writeLine(line, file, append);
    }

    public void writeLines(List<String> lines, File file, Boolean append) {
        try (BufferedWriter bw = new BufferedWriter(new java.io.FileWriter(file, append))) {
            for (String line : lines) {
                bw.write(line);
                bw.newLine();
            }
            bw.flush();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public void mergeFiles(List<File> files) {
        File output = Paths.get(ConfigProperties.OUTPUT.value()).toFile();
        try (BufferedWriter bw = new BufferedWriter(new java.io.FileWriter(output, false))) {
            for (File file : files) {
                try (BufferedReader br = Files.newBufferedReader(file.toPath(), StandardCharsets.UTF_8)) {
                    String line;
                    while ((line = br.readLine()) != null ) {
                        bw.write(line);
                        bw.newLine();
                    }
                } catch (Exception e) {
                    continue;
                }
                bw.flush();
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public void move(File source, File destine) {
        try {
            Files.move(source.toPath(), destine.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public void delete(Set<File> files) {
        for (File file : files) {
            file.delete();
        }
    }
}
