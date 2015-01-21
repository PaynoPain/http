package com.paynopain.http.context.oauth;

public class TokensStorageInMemory implements TokensStorage {
    private Tokens t;

    @Override
    public boolean isEmpty() {
        return t == null;
    }

    @Override
    public Tokens get() {
        return t;
    }

    @Override
    public void set(Tokens t) {
        this.t = t;
    }
}
