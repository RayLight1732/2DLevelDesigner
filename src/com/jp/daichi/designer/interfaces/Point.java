package com.jp.daichi.designer.interfaces;

import java.io.Serializable;

public record Point(int x, int y) implements Serializable {

    public static final Point ZERO = new Point(0, 0);

}
