package com.touchiteasy.oauth;

public class Tokens {
    public final String access;
    public final String refresh;

    public Tokens(String access, String refresh){
        this.access = access;
        this.refresh = refresh;
    }
}
