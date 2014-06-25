package com.touchiteasy.http.cache;

import com.touchiteasy.http.BaseResponse;
import com.touchiteasy.http.Request;
import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(HierarchicalContextRunner.class)
public abstract class CacheStorageContract {
    public abstract CacheStorage createCacheStorage();

    private CacheStorage storage;
    private Request request;
    private CacheEntry entry;

    @Before
    public void setUp(){
        storage = createCacheStorage();
        request = createRequest();
        entry = createEntry();
    }

    private CacheEntry createEntry() {
        return new CacheEntry(new Date(0), new BaseResponse(200, "body"));
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

