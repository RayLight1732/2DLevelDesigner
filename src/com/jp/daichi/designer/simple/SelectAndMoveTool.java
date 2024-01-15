package com.jp.daichi.designer.simple;

import com.jp.daichi.designer.interfaces.Canvas;
import com.jp.daichi.designer.interfaces.Tool;
import com.jp.daichi.designer.interfaces.UpdateObserver;
import com.jp.daichi.designer.simple.editor.UpdateAction;

import java.awt.*;
import java.awt.event.MouseAdapter;

/**
 * 選択、移動を行うツール
 */
public class SelectAndMoveTool implements Tool  {

    private static final Color selectRectColor = new Color(65,105,255,100);
    private final MouseAdapter mouseAdapter;

    private UpdateObserver updateObserver;
    /**
     * 新しいインスタンスを作成する
     * @param canvas キャンバス
     */
    public SelectAndMoveTool(Canvas canvas) {
        this.mouseAdapter = new SelectToolMouseAdapter(this,canvas);
    }
    private Rectangle rectangle;

    @Override
    public MouseAdapter getMouseAdapter() {
        return mouseAdapter;
    }

    @Override
    public void draw(Graphics2D g) {
        if (rectangle != null) {
            g.setColor(selectRectColor);
            g.fill(rectangle);
        }
    }

    @Override
    public UpdateObserver getUpdateObserver() {
        return updateObserver;
    }

    @Override
    public void setUpdateObserver(UpdateObserver updateObserver) {
        this.updateObserver = updateObserver;
    }

    public void setRectangle(Rectangle rectangle) {
        this.rectangle = rectangle;
        updateObserver.update(this, UpdateAction.CHANGE_RECTANGLE);
    }
}
