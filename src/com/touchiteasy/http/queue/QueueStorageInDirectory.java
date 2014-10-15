package com.touchiteasy.http.queue;

import com.touchiteasy.commons.Function;

import java.io.*;
import java.util.NoSuchElementException;

public class QueueStorageInDirectory<T> implements QueueStorage<T> {
    private final File rootDirectory;
    private final Function<T, String> composer;
    private final Function<String, T> parser;

    public QueueStorageInDirectory(File rootDirectory, Function<T, String> composer, Function<String, T> parser) {
        this.rootDirectory = rootDirectory;
        this.composer = composer;
        this.parser = parser;
    }

    @Override
    public synchronized boolean isEmpty() {
        return getSmallerName() == null;
    }

    @Override
    public synchronized void add(T element) {
        BufferedWriter fileWriter = null;
        try {
            fileWriter = new BufferedWriter(new FileWriter(getNextFile()));
            fileWriter.write(composer.apply(element));
            fileWriter.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException ignored) {}
            }
        }
    }

    @Override
    public synchronized T peek() {
        String fileContent = readFileContent(getFirstFile());
        return parser.apply(fileContent);
    }

    private String readFileContent(File file) {
        StringBuilder sb = new StringBuilder((int) file.length());
        FileReader reader = null;
        try {
            reader = new FileReader(file);
            while (reader.ready()){
                sb.append((char) reader.read());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ignored) {}
            }
        }
        return sb.toString();
    }

    @Override
    public synchronized void dequeue() {
        getFirstFile().delete();
    }

    private File getNextFile() {
        Integer biggerName = getBiggerName();
        Integer fileNumber = biggerName == null? 0 : biggerName +1;

        String fileName = String.valueOf(fileNumber);
        return new File(this.rootDirectory, fileName);
    }

    private File getFirstFile() {
        Integer smallerName = getSmallerName();

        if (smallerName == null)
            throw new NoSuchElementException("There is no valid file remaining!");

        String fileName = String.valueOf(smallerName);
        return new File(this.rootDirectory, fileName);
    }

    private Integer getBiggerName() {
        Integer n = null;

        for (String s : this.rootDirectory.list()){
            try {
                Integer num = Integer.valueOf(s);
                if (n == null || num > n) n = num;
            } catch (NumberFormatException ignored){}
        }

        return n;
    }

    private Integer getSmallerName() {
        Integer n = null;

        for (String s : this.rootDirectory.list()){
            try {
                Integer num = Integer.valueOf(s);
                if (n == null || num < n) n = num;
            } catch (NumberFormatException ignored){}
        }

        return n;
    }
}
