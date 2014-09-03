package com.touchiteasy.http.queue;

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

    @Test
    public void GivenACollectionWithOneElement_SameAsTheElementAlone() {
        final FlushableSpy element = new FlushableSpy();
        final FlushableCollection collection = new FlushableCollection(Arrays.<Flushable>asList(element));

        element.canFlush = true;
        assertThat(collection.canFlush(), is(true));

        element.canFlush = false;
        assertThat(collection.canFlush(), is(false));
    }
}
