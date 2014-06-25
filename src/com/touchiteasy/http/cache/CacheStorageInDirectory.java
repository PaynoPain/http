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
        try {
            BufferedReader fileReader = new BufferedReader(new FileReader(getFile(req)));

            Date expiration = new Date(Long.valueOf(fileReader.readLine()));
            Integer statusCode = Integer.valueOf(fileReader.readLine());
            String body = fileReader.readLine();

            return new CacheEntry(expiration, new BaseResponse(statusCode, body));
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    @Override
    public void write(Request req, CacheEntry entry) {
        try {
            BufferedWriter fileWriter = new BufferedWriter(new FileWriter(getFile(req)));

            String expiration = String.valueOf(entry.expiration.getTime());
            String statusCode = String.valueOf(entry.response.getStatusCode());
            String body = entry.response.getBody();

            fileWriter.write(expiration + "\n" + statusCode + "\n" + body);
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private File getFile(Request req) {
        return new File(this.baseDirectory, String.valueOf(new HashedRequest(req).hashCode()));
    }
}

