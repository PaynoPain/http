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
        return getFile(req).exists();
    }

    @Override
    public CacheEntry read(Request req) {
        final File file = getFile(req);
        try {
            BufferedReader fileReader = new BufferedReader(new FileReader(file));

            Date expiration = new Date(Long.valueOf(fileReader.readLine()));
            Date deadline = new Date(Long.valueOf(fileReader.readLine()));
            Integer statusCode = Integer.valueOf(fileReader.readLine());
            String body = fileReader.readLine();

            return new CacheEntry(new BaseResponse(statusCode, body), expiration, deadline);
        } catch (IOException e) {
            throw new RuntimeException(
                    "Can't read the stored cache for " + req.getResource() + " at " + file.getAbsolutePath(),
                    e
            );
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

