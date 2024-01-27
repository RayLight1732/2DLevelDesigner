package com.jp.daichi.designer.editor;

import com.jp.daichi.designer.Util;
import com.jp.daichi.designer.interfaces.DesignerObject;
import com.jp.daichi.designer.interfaces.Point;
import com.jp.daichi.designer.interfaces.editor.EditorDesignerObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;

public class CopyPasteHandler {
    private final EditorCanvas canvas;
    private List<Map<String,Object>> copiedObjects;

    public CopyPasteHandler(EditorCanvas canvas) {
        this.canvas = canvas;
    }

    public void onCopied() {
        Set<DesignerObject> designerObjects = canvas.getFrame().getSelected();
        if (designerObjects.size() > 0) {
            copiedObjects = new ArrayList<>();
            for (DesignerObject designerObject:designerObjects) {
                copiedObjects.add(((EditorDesignerObject)designerObject).serialize());
            }
        }
    }

    public void onPasted() {
        if (copiedObjects != null) {
            canvas.getHistory().startCompress("Paste");
            canvas.getFrame().clearSelectedObject();
            for (Map<String, Object> map : copiedObjects) {
                DesignerObject designerObject = canvas.getDesignerObjectManager().deserializeManagedObject(map,true);
                canvas.getFrame().addSelectedObject(designerObject);
                canvas.getLayerManager().getLayer(designerObject.getType()).add(designerObject.getUUID());
            }
            canvas.getHistory().finishCompress();
        }
    }

}
