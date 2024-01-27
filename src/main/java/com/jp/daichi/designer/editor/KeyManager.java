package com.jp.daichi.designer.editor;

import java.util.HashMap;
import java.util.Map;

public class KeyManager {

    private static final Map<Integer,Boolean> map = new HashMap<>();
    public static boolean isPressed(int id) {
        //TODO
        return map.getOrDefault(id,false);
    }

    static void onPressed(int id) {
        map.put(id,true);
    }

    static void onReleased(int id) {
        map.put(id,false);
    }

}
