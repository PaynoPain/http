package com.paynopain.http.cache;

import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class LayeredMapStorageTest {
    private static class StorageDouble<K, V> implements MapStorage<K, V> {
        public HashMap<K, V> m = new HashMap<K, V>();
        public int readCalls = 0;
        public int writeCalls = 0;

        @Override
        public boolean contains(K k) {
            return m.containsKey(k);
        }

        @Override
        public V read(K k) {
            readCalls++;
            return m.get(k);
        }

        @Override
        public void write(final K k, final V v) {
            writeCalls++;
            m.put(k, v);
        }
    }

    StorageDouble<String, String> fast;
    StorageDouble<String, String> persistent;
    MapStorage<String, String> layered;

    @Before
    public void setUp() {
        fast = new StorageDouble<String, String>();
        persistent = new StorageDouble<String, String>();
        layered = new LayeredMapStorage<String, String>(fast, persistent);
    }

    @Test
    public void afterWriting_ShouldBeReadable() {
        layered.write("::key::", "::value::");
        assertThat(layered.contains("::key::"), is(true));
        assertThat(layered.read("::key::"), is("::value::"));
    }

    @Test
    public void beforeWriting_ShouldNotContainIt() {
        assertThat(layered.contains("::key::"), is(false));
    }

    @Test
    public void givenNotCachedKey_ShouldStoreItInBothStorages() {
        layered.write("::key::", "::value::");
        assertThat(fast.contains("::key::"), is(true));
        assertThat(persistent.contains("::key::"), is(true));
    }

    @Test
    public void givenData_ShouldReadFromFastStorage() {
        layered.write("::key::", "::value::");

        String value = layered.read("::key::");

        assertThat(value, is("::value::"));
        assertThat(fast.readCalls, is(1));
        assertThat(persistent.readCalls, is(0));
    }

    @Test
    public void givenPersistentDataButNotFast_ShouldReadFromPersistentAndPutItInFast() {
        persistent.write("::key::", "::value::");

        String value = layered.read("::key::");

        assertThat(persistent.readCalls, is(1));
        assertThat(fast.readCalls, is(0));
        assertThat(fast.writeCalls, is(1));
        assertThat(value, is("::value::"));
    }

    @Test
    public void givenPersistentDataButNotFast_ShouldContainIt() {
        persistent.write("::key::", "::value::");
        assertThat(layered.contains("::key::"), is(true));
    }

    @Test
    public void givenOnlyFastData_ShouldNotContainIt_BecauseThisShouldNeverHappen() {
        fast.write("::key::", "::value::");
        assertThat(layered.contains("::key::"), is(false));
    }
}
