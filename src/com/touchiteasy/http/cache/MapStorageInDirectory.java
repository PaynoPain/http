package com.touchiteasy.http.cache;

import com.touchiteasy.http.BaseResponse;
import com.touchiteasy.http.IdentifiableRequest;
import com.touchiteasy.http.Request;

import java.io.*;
import java.util.Date;

public class MapStorageInDirectory implements MapStorage<Request, CacheEntry> {
    private File baseDirectory;

    public MapStorageInDirectory(File baseDirectory){
        this.baseDirectory = baseDirectory;
    }

    @Override
    public synchronized boolean contains(Request req) {
        final File file = getFile(req);

        if (!isValid(file)){
            file.delete();
        }

        return file.exists();
    }

    private boolean isValid(File file) {
        try {
            parse(readFile(file));
            return true;
        } catch (Throwable t){
            return false;
        }
    }

    @Override
    public synchronized CacheEntry read(Request req) {
        if (!contains(req))
            throw new IllegalStateException("There is no cached entry for the resource: " + req.getResource());

        final File file = getFile(req);
        return parse(readFile(file));
    }

    private String readFile(File file) {
        FileInputStream fis = null;

        try {
            fis = new FileInputStream(file);
            byte[] data = new byte[(int)file.length()];
            fis.read(data);

            return new String(data, "UTF-8");
        } catch (IOException e) {
            throw new RuntimeException(
                    "Can't read the stored cache entry at " + file.getAbsolutePath(),
                    e
            );
        } finally {
            if (fis != null){
                try {
                    fis.close();
                } catch (IOException ignored) {}
            }
        }
    }

    private CacheEntry parse(String data) {
        String[] lines = data.split(System.getProperty("line.separator"));

        try {
            Date expiration = new Date(Long.valueOf(lines[0]));
            Date deadline = new Date(Long.valueOf(lines[1]));
            Integer statusCode = Integer.valueOf(lines[2]);
            String body = lines[3];

            return new CacheEntry(new BaseResponse(statusCode, body), expiration, deadline);
        } catch (Throwable t){
            throw new IllegalArgumentException("The following data is not a valid CacheEntry: \n\"" + data + "\"", t);
        }
    }

    @Override
    public synchronized void write(Request req, CacheEntry entry) {
        final File file = getFile(req);
        BufferedWriter fileWriter = null;
        try {
            final File parentFile = file.getParentFile();
            if (!parentFile.exists()) {
                parentFile.mkdirs();
            }

            fileWriter = new BufferedWriter(new FileWriter(file));

            String expiration = String.valueOf(entry.expiration.getTime());
            String deadline = String.valueOf(entry.deadline.getTime());
            String statusCode = String.valueOf(entry.response.getStatusCode());
            String body = entry.response.getBody();

            fileWriter.write(expiration + "\n" + deadline + "\n" + statusCode + "\n" + body);
        } catch (IOException e) {
            throw new RuntimeException(
                    "Can't write a cache entry for " + req.getResource() + " at " + file.getAbsolutePath(),
                    e
            );
        } finally {
            if (fileWriter != null){
                try {
                    fileWriter.close();
                } catch (IOException ignored) {}
            }
        }
    }

    private File getFile(Request req) {
        return new File(this.baseDirectory, String.valueOf(new IdentifiableRequest(req).hashCode()));
    }
}

