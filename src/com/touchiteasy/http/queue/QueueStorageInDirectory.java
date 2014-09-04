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
    public boolean isEmpty() {
        return getSmallerName() == null;
    }

    @Override
    public void add(T element) {
        try {
            BufferedWriter fileWriter = new BufferedWriter(new FileWriter(getNextFile()));

            fileWriter.write(composer.apply(element));

            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public T peek() {
        String fileContent = readFileContent(getFirstFile());
        return parser.apply(fileContent);
    }

    private String readFileContent(File file) {
        try {
            FileReader reader = new FileReader(file);
            StringBuilder sb = new StringBuilder((int) file.length());
            while (reader.ready()){
                sb.append((char) reader.read());
            }
            return sb.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void dequeue() {
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
