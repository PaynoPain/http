package com.touchiteasy.http.queue;

import com.touchiteasy.http.BaseRequest;
import com.touchiteasy.http.IdentifiableRequest;
import com.touchiteasy.http.Request;
import org.junit.Before;
import org.junit.Test;

import java.util.NoSuchElementException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNot.not;

public abstract class QueueStorageContract {
    public abstract QueueStorage createInstance();

    private Request createElement(String resource) {
        return new IdentifiableRequest(new BaseRequest(resource));
    }

    QueueStorage storage;

    @Before
    public void setUp(){
        storage = createInstance();
    }

    @Test
    public void WithNoData_ShouldBeEmpty(){
        assertThat(storage.isEmpty(), is(true));
    }

    @Test(expected = NoSuchElementException.class)
    public void WithNoData_WhenRemoving_ShouldTrowException(){
        storage.removeFirst();
    }

    @Test(expected = NoSuchElementException.class)
    public void WithNoData_WhenGetting_ShouldTrowException(){
        storage.getFirst();
    }

    @Test
    public void WithAMovement_ShouldNotBeEmpty(){
        storage.add(createElement("::resource::"));

        assertThat(storage.isEmpty(), is(false));
    }

    @Test
    public void WithAMovement_AndThenRemovedIt_ShouldBeEmpty(){
        storage.add(createElement("::resource::"));
        storage.removeFirst();

        assertThat(storage.isEmpty(), is(true));
    }

    @Test
    public void WithAMovement_ShouldReturnThatMovement(){
        Request element = createElement("::resource::");

        storage.add(element);

        assertThat(storage.getFirst(), is(element));
    }


    @Test
    public void WithTwoMovements_AndThenRemovedTheFirst_ShouldReturnTheSecond(){
        Request element1 = createElement("::resource1::");
        Request element2 = createElement("::resource2::");

        storage.add(element1);
        storage.add(element2);
        storage.removeFirst();

        assertThat(storage.getFirst(), is(not(element1)));
        assertThat(storage.getFirst(), is(element2));
    }

    @Test
    public void WithSomeMovements_AndRemovingAllButOne_ShouldNotBeEmpty(){
        for (int i = 0; i < 10; i++){
            storage.add(createElement("::resource" + i + "::"));
        }

        for (int i = 0; i < 9; i++){
            storage.removeFirst();
        }

        assertThat(storage.isEmpty(), is(false));
    }
}
