package com.touchiteasy.http.queue;

import com.touchiteasy.http.BaseRequest;
import com.touchiteasy.http.BaseResponse;
import com.touchiteasy.http.Request;
import com.touchiteasy.http.ServerMock;
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
        assertThat(queueStorage.isEmpty(), is(true));
    }
}
