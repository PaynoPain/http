package com.paynopain.http.queue;

import com.paynopain.commons.Factory;
import com.paynopain.http.*;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class QueueFlusherTest {
    ServerMock serverRequester;
    QueueFlusher queueFlusher;
    QueueStorageInMemory<Request> queueStorage;

    @Before
    public void setUp() {
        serverRequester = new ServerMock();
        queueStorage = new QueueStorageInMemory<Request>();
        queueFlusher = new QueueFlusher(serverRequester, queueStorage);
    }

    @Test
    public void GivenAnEmptyQueue_ShouldDoNothing() {
        queueFlusher.flush();
        assertThat(serverRequester.requests.size(), is(0));
    }

    @Test
    public void GivenAQueueWithOneRequest_ShouldSendItToTheServer_AndDequeueIt() {
        serverRequester.addResponse(new BaseResponse(200, "ok"));
        queueStorage.add(new BaseRequest("::request1::"));
        queueFlusher.flush();
        assertThat(serverRequester.requests.size(), is(1));
        assertThat(serverRequester.requests.get(0).getResource(), is("::request1::"));
        assertThat(queueFlusher.canFlush(), is(false));
    }

    @Test
    public void GivenARequestersException_ShouldNotDequeueTheElement() {
        serverRequester.addResponseFactory(new Factory<Response>() {
            @Override
            public Response get() {
                throw new RuntimeException("Can't communicate with the server!");
            }
        });
        queueStorage.add(new BaseRequest("::request1::"));
        queueFlusher.flush();
        assertThat(serverRequester.requests.size(), is(1));
        assertThat(serverRequester.requests.get(0).getResource(), is("::request1::"));
        assertThat(queueFlusher.canFlush(), is(true));
    }

    @Test
    public void GivenAQueueWithOneRequest_AndTheServerThrowsExceptionOnlyTheFirstTime_FlushingTwoTimesShouldEmptyTheQueue() {
        serverRequester.addResponseFactory(new Factory<Response>() {
            @Override
            public Response get() {
                throw new RuntimeException("Can't communicate with the server!");
            }
        });
        serverRequester.addResponse(new BaseResponse(200, "ok"));

        queueStorage.add(new BaseRequest("::request1::"));

        queueFlusher.flush();
        queueFlusher.flush();

        assertThat(serverRequester.requests.size(), is(2));
        assertThat(serverRequester.requests.get(1).getResource(), is("::request1::"));
        assertThat(queueFlusher.canFlush(), is(false));
    }

    @Test
    public void GivenAQueueWithThreeRequests_ShouldSendThemAll() {
        final BaseResponse ok = new BaseResponse(200, "ok");
        serverRequester.addResponse(ok);
        serverRequester.addResponse(ok);
        serverRequester.addResponse(ok);

        queueStorage.add(new BaseRequest("::request1::"));
        queueStorage.add(new BaseRequest("::request2::"));
        queueStorage.add(new BaseRequest("::request3::"));

        queueFlusher.flush();

        assertThat(serverRequester.requests.size(), is(3));
        assertThat(queueFlusher.canFlush(), is(false));
    }

    @Test
    public void GivenAQueueWithThreeRequests_WhenOneOfThemThrowsException_ShouldStopSendingThem() {
        final BaseResponse ok = new BaseResponse(200, "ok");
        serverRequester.addResponse(ok);
        serverRequester.addResponseFactory(new Factory<Response>() {
            @Override
            public Response get() {
                throw new RuntimeException("Can't communicate with the server!");
            }
        });
        serverRequester.addResponse(ok);

        queueStorage.add(new BaseRequest("::request1::"));
        queueStorage.add(new BaseRequest("::request2::"));
        queueStorage.add(new BaseRequest("::request3::"));

        queueFlusher.flush();

        assertThat(serverRequester.requests.size(), is(2));
        assertThat(serverRequester.requests.get(1).getResource(), is("::request2::"));
    }

    private static class FailingPeekStorage implements QueueStorage<Request>{
        public static class StorageFailure extends RuntimeException {}

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public void add(Request request) {}

        @Override
        public Request peek() {
            throw new StorageFailure();
        }

        @Override
        public void dequeue() {}
    }

    @Test(expected = FailingPeekStorage.StorageFailure.class)
    public void GivenAPeekFailure_ShouldBubbleUp() {
        queueFlusher = new QueueFlusher(serverRequester, new FailingPeekStorage());
        queueFlusher.flush();
    }

    private static class FailingDequeueStorage implements QueueStorage<Request>{
        public static class StorageFailure extends RuntimeException {}

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public void add(Request request) {}

        @Override
        public Request peek() {
            return new BaseRequest("");
        }

        @Override
        public void dequeue() {
            throw new StorageFailure();
        }
    }

    @Test(expected = FailingDequeueStorage.StorageFailure.class)
    public void GivenADequeueFailure_ShouldBubbleUp() {
        serverRequester.addResponse(new BaseResponse(200, "ok"));
        queueFlusher = new QueueFlusher(serverRequester, new FailingDequeueStorage());
        queueFlusher.flush();
    }
}
