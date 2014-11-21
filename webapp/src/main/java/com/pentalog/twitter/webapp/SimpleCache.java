package com.pentalog.twitter.webapp;

import java.util.HashMap;
import java.util.Map;

/**
 * User: mcsere
 * Date: 11/21/2014
 * Time: 11:04 PM
 */
public class SimpleCache {
    private Map<String, Object> storage = new HashMap<String, Object>();

    public void put(String key, Object value) {
        this.storage.put(key, value);
    }

    public Object get(String key) {
        return storage.get(key);
    }

    public void clear() {
        this.storage.clear();
    }

}
