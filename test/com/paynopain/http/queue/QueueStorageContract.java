package com.paynopain.http.queue;

import org.junit.Before;
import org.junit.Test;

import java.util.NoSuchElementException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;

public abstract class QueueStorageContract {
    public abstract QueueStorage<String> createInstance();

    QueueStorage<String> storage;

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
        storage.dequeue();
    }

    @Test(expected = NoSuchElementException.class)
    public void WithNoData_WhenGetting_ShouldTrowException(){
        storage.peek();
    }

    @Test
    public void WithAnElement_ShouldNotBeEmpty(){
        storage.add("::element1::");

        assertThat(storage.isEmpty(), is(false));
    }

    @Test
    public void WithAnElement_AndThenRemovedIt_ShouldBeEmpty(){
        storage.add("::element1::");
        storage.dequeue();

        assertThat(storage.isEmpty(), is(true));
    }

    @Test
    public void WithAnElement_ShouldReturnThatElement(){
        String element = "::element1::";

        storage.add(element);

        assertThat(storage.peek(), is(element));
    }

    @Test
    public void WithTwoElements_AndThenRemovedTheFirst_ShouldReturnTheSecond(){
        String element1 = "::element1::";
        String element2 = "::element2::";

        storage.add(element1);
        storage.add(element2);
        storage.dequeue();

        assertThat(storage.peek(), is(not(element1)));
        assertThat(storage.peek(), is(element2));
    }

    @Test
    public void WithSomeElements_AndRemovingAllButOne_ShouldNotBeEmpty(){
        for (int i = 0; i < 10; i++){
            storage.add(String.format("::element%s::", i));
        }

        for (int i = 0; i < 9; i++){
            storage.dequeue();
        }

        assertThat(storage.isEmpty(), is(false));
    }
}
