package com.jp.daichi.designer.editor;

import com.jp.daichi.designer.interfaces.Point;
import com.jp.daichi.designer.interfaces.SignedDimension;
import com.jp.daichi.designer.interfaces.Tool;
import com.jp.daichi.designer.interfaces.UpdateAction;
import com.jp.daichi.designer.interfaces.editor.EditorDesignerObject;
import com.jp.daichi.designer.simple.SimpleObservedObject;

import java.awt.*;
import java.awt.event.MouseAdapter;

/**
 * 選択、移動を行うツール
 */
public class SelectAndMoveTool extends SimpleObservedObject implements Tool {

    private static final Color selectRectColor = new Color(65, 105, 255, 100);
    private final MouseAdapter mouseAdapter;
    private final EditorCanvas canvas;

    /**
     * 新しいインスタンスを作成する
     *
     * @param canvas キャンバス
     */
    public SelectAndMoveTool(EditorCanvas canvas) {
        this.canvas = canvas;
        this.mouseAdapter = new SelectToolMouseAdapter(this);
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

    public void setRectangle(Rectangle rectangle) {
        this.rectangle = rectangle;
        sendUpdate(UpdateAction.CHANGE_RECTANGLE);
    }

    public void startDrag() {
        canvas.getFrame().getSelected().forEach(designerObject -> ((EditorDesignerObject) designerObject).setSaveHistory(false));
    }

    public void endDrag(Point startPoint, SignedDimension startDimension, boolean moveOnly) {
        if (moveOnly) {
            canvas.getFrame().setPosition(startPoint);
        } else {
            canvas.getFrame().setPositionAndDimension(startPoint, startDimension);
        }
        canvas.getFrame().getSelected().forEach(designerObject -> ((EditorDesignerObject) designerObject).setSaveHistory(true));
        if (moveOnly) {
            canvas.getHistory().startCompress("Move");
        } else {
            canvas.getHistory().startCompress("Resize");
        }
    }

    public EditorCanvas getCanvas() {
        return canvas;
    }
}
