package com.paynopain.http.queue;

import org.junit.Test;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class FlushableCollectionTest {
    private static class FlushableSpy implements Flushable {
        public boolean canFlush = false;
        public int flushCount = 0;

        @Override
        public boolean canFlush() {
            return canFlush;
        }

        @Override
        public void flush() {
            flushCount++;
        }
    }

    @Test
    public void GivenAnEmptyCollection_CantFlush() {
        final FlushableCollection collection = new FlushableCollection(Arrays.<Flushable>asList());
        assertThat(collection.canFlush(), is(false));
    }

    @Test (expected = RuntimeException.class)
    public void GivenAnEmptyCollection_WhenFlushing_ThrowsException() {
        final FlushableCollection collection = new FlushableCollection(Arrays.<Flushable>asList());
        collection.flush();
    }

    @Test
    public void GivenACollectionWithOneElement_CanFlushIfTheElementCanFlush() {
        final FlushableSpy element = new FlushableSpy();
        final FlushableCollection collection = new FlushableCollection(Arrays.<Flushable>asList(element));

        element.canFlush = true;
        assertThat(collection.canFlush(), is(true));

        element.canFlush = false;
        assertThat(collection.canFlush(), is(false));
    }

    @Test
    public void GivenACollectionWithOneElementThatCanFlush_FlushShouldSendHimToFlush() {
        final FlushableSpy element = new FlushableSpy();
        final FlushableCollection collection = new FlushableCollection(Arrays.<Flushable>asList(element));

        element.canFlush = true;
        collection.flush();

        assertThat(element.flushCount, is(1));
    }

    @Test (expected = RuntimeException.class)
    public void GivenACollectionWithOneElementThatCantFlush_WhenFlushing_ShouldThrowException() {
        final FlushableSpy element = new FlushableSpy();
        final FlushableCollection collection = new FlushableCollection(Arrays.<Flushable>asList(element));

        element.canFlush = false;
        collection.flush();
    }

    @Test
    public void GivenACollectionWithThreeElements_CanFlushIfOneOfThemCanFlush() {
        final FlushableSpy element1 = new FlushableSpy();
        final FlushableSpy element2 = new FlushableSpy();
        final FlushableSpy element3 = new FlushableSpy();
        final FlushableCollection collection = new FlushableCollection(Arrays.<Flushable>asList(element1, element2, element3));

        element1.canFlush = false;
        element2.canFlush = false;
        element3.canFlush = false;
        assertThat(collection.canFlush(), is(false));

        element1.canFlush = false;
        element2.canFlush = false;
        element3.canFlush = true;
        assertThat(collection.canFlush(), is(true));
    }

    @Test
    public void GivenACollectionWithThreeElements_WhenFlushing_ShouldFlushOnlyTheOneThatCanFlush() {
        final FlushableSpy element1 = new FlushableSpy();
        final FlushableSpy element2 = new FlushableSpy();
        final FlushableSpy element3 = new FlushableSpy();
        final FlushableCollection collection = new FlushableCollection(Arrays.<Flushable>asList(element1, element2, element3));

        element1.canFlush = false;
        element2.canFlush = true;
        element3.canFlush = true;
        collection.flush();

        assertThat(element1.flushCount, is(0));
        assertThat(element2.flushCount, is(1));
        assertThat(element3.flushCount, is(1));
    }
}
