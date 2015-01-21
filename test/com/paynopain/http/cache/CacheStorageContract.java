package com.paynopain.http.cache;

import com.paynopain.http.BaseResponse;
import com.paynopain.http.Request;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public abstract class CacheStorageContract {
    public abstract MapStorage<Request, CacheEntry> createCacheStorage();

    protected MapStorage<Request, CacheEntry> storage;
    protected Request request;
    protected CacheEntry entry;

    @Before
    public void setUp(){
        storage = createCacheStorage();
        request = createRequest();
        entry = createEntry();
    }

    private CacheEntry createEntry() {
        return new CacheEntry(new BaseResponse(200, "body"), new Date(0), new Date(0));
    }

    private Request createRequest() {
        return new Request() {
            @Override
            public String getResource() {
                return "resource";
            }

            @Override
            public Map<String, String> getParameters() {
                return new HashMap<String, String>();
            }
        };
    }

    @Test
    public void BeforeStoringARequest_ShouldNotContainTheRequest(){
        assertThat(storage.contains(request), is(false));
    }

    @Test(expected = IllegalStateException.class)
    public void BeforeStoringARequest_WhenReadingIt_ThrowsException() {
        storage.read(createRequest());
    }

    @Test
    public void AfterStoringAnEntry_ShouldContainTheAssociatedRequest(){
        storage.write(request, entry);
        assertThat(storage.contains(request), is(true));
    }

    @Test
    public void AfterStoringAnEntry_ShouldContainTheAssociatedRequest_EvenIfIsAnotherInstance(){
        storage.write(request, entry);
        assertThat(storage.contains(createRequest()), is(true));
    }

    @Test
    public void AfterStoringAnEntry_ShouldBeAbleToReadIt(){
        storage.write(request, entry);
        assertThat(storage.read(request), is(entry));
    }

    @Test
    public void AfterStoringAnEntry_ShouldBeAbleToReadIt_EvenIfTheyAreDifferentInstances(){
        storage.write(request, entry);
        assertThat(storage.read(createRequest()), is(createEntry()));
    }
}

