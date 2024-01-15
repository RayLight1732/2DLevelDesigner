package com.jp.daichi.designer.test;

import com.jp.daichi.designer.Utils;
import com.jp.daichi.designer.interfaces.Canvas;
import com.jp.daichi.designer.interfaces.Point;
import com.jp.daichi.designer.interfaces.SignedDimension;
import com.jp.daichi.designer.simple.SimpleDesignerObject;

import java.awt.*;
import java.awt.event.MouseAdapter;

public class MockDesignerObject extends SimpleDesignerObject {
    /**
     * デザイナーオブジェクトのインスタンスを作成する
     *
     * @param name このオブジェクトの名前
     * @param canvas    キャンバス
     * @param center    中心座標
     * @param dimension 表示領域
     */
    public MockDesignerObject(String name,Canvas canvas, Point center, SignedDimension dimension) {
        super(name,canvas, center, dimension);
    }

    @Override
    public MouseAdapter getMouseAdapter() {
        return null;
    }

    @Override
    public void draw(Graphics2D g) {
        if (getZ() == 0) {
            g.setColor(Color.RED);
        } else {
            g.setColor(Color.WHITE);
        }
        g.fill(Utils.getRectangleOnScreen(getCanvas(),this));
    }
}
