package com.touchiteasy.http.cache;

import com.touchiteasy.http.*;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class PolicyFactoryMock implements PolicyFactory {

    @Override
    public Policy construct() {
        return null;
    }
}

class ResourceRequesterMock implements ResourceRequester {

    private String responseBody;
    private int responseCode;

    ResourceRequesterMock( int responseCode, String responseBody) {
        this.responseBody = responseBody;
        this.responseCode = responseCode;
    }

    @Override
    public Response run(Request request) {
        return new BaseResponse(responseCode, responseBody);
    }
}

public class CacheRequesterTest {
    public class GivenACacheRequester {
        CacheRequester cacheRequester;

        public class AndResourceRequesterMock {
            ResourceRequesterMock requesterMock;

            @Before
            public void setUp() {
                requesterMock = new ResourceRequesterMock(200, "{\"is_blocked\": true,\"block_description\":\"Full capacity\"}");
            }
            public class AndPolicyFactoryMock {
                PolicyFactoryMock factoryMock;

                @Before
                public void setUp() {
                    factoryMock = new PolicyFactoryMock();
                    cacheRequester = new CacheRequester(requesterMock, factoryMock);
                }
                public class WhenCacheListIsEmpty{
                    @Before
                    public void setUp(){
                        cacheRequester.run(new BaseRequest("http://dev.api.touchaccess.com/zones/info/VIP.json"));
                    }

                    @Test
                    public void CacheEntryListShouldBeEmpty(){
                        assertThat(cacheRequester.cacheEntryList.isEmpty(), is(true));
                    }
                }

            }
        }
    }
}
