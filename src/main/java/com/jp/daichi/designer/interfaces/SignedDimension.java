package com.jp.daichi.designer.interfaces;

import java.io.Serial;
import java.io.Serializable;

/**
 * 符号付き領域
 * マイナス符号の時反転を表す
 */
public record SignedDimension(double width, double height) implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public double getAbsWidth() {
        return Math.abs(width);
    }

    public double getAbsHeight() {
        return Math.abs(height);
    }


}
