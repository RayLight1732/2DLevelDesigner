package com.jp.daichi.designer;

import java.util.HashMap;
import java.util.Map;

public class Settings {
    private static final Map<Integer, Object> map = new HashMap<>();

    public static final int SNAP = 0;

    public static <T> T get(int id) {
        try {
            return (T) map.get(id);
        } catch (ClassCastException e) {
            return null;
        }
    }

    public static <T> void set(int id, T value) {
        map.put(id, value);
    }
}
