package com.paynopain.http;

import java.util.Map;

public interface Request {
    public String getResource();
    public Map<String, String> getParameters();
}
