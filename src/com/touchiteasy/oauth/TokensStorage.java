package com.touchiteasy.oauth;

import com.touchiteasy.oauth.Tokens;

public interface TokensStorage {
    public boolean isEmpty();
    public Tokens get();
    public void set(Tokens t);
}
