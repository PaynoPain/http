package com.touchiteasy.http.cache;

import com.touchiteasy.http.BaseResponse;
import com.touchiteasy.http.Request;

import java.io.*;
import java.util.Date;

public class CacheStorageInDirectory implements CacheStorage {
    private File baseDirectory;

    public CacheStorageInDirectory(File baseDirectory){
        this.baseDirectory = baseDirectory;
    }

    @Override
    public boolean contains(Request req) {
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
    public CacheEntry read(Request req) {
        if (!contains(req))
            throw new IllegalStateException("There is no cached entry for the resource: " + req.getResource());

        final File file = getFile(req);
        try {
            return parse(readFile(file));
        } catch (Throwable t) {
            throw new RuntimeException(
                    "Can't read the stored cache entry at " + file.getAbsolutePath(),
                    t
            );
        }
    }

    private String readFile(File file) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        byte[] data = new byte[(int)file.length()];
        fis.read(data);
        fis.close();

        return new String(data, "UTF-8");
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
    public void write(Request req, CacheEntry entry) {
        final File file = getFile(req);
        try {
            BufferedWriter fileWriter = new BufferedWriter(new FileWriter(file));

            String expiration = String.valueOf(entry.expiration.getTime());
            String deadline = String.valueOf(entry.deadline.getTime());
            String statusCode = String.valueOf(entry.response.getStatusCode());
            String body = entry.response.getBody();

            fileWriter.write(expiration + "\n" + deadline + "\n" + statusCode + "\n" + body);
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(
                    "Can't write a cache entry for " + req.getResource() + " at " + file.getAbsolutePath(),
                    e
            );
        }
    }

    private File getFile(Request req) {
        return new File(this.baseDirectory, String.valueOf(new HashedRequest(req).hashCode()));
    }
}

