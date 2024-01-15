package com.jp.daichi.designer.interfaces;

/**
 * 符号付き領域
 * マイナス符号の時反転を表す
 */
public record SignedDimension(double width, double height) {

    public double getAbsWidth() {
        return Math.abs(width);
    }

    public double getAbsHeight() {
        return Math.abs(height);
    }


}
