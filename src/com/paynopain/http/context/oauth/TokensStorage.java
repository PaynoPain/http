package com.paynopain.http.context.oauth;

public interface TokensStorage {
    public boolean isEmpty();
    public Tokens get();
    public void set(Tokens t);
}
