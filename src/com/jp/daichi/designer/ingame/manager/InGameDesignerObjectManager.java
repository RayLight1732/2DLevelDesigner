package com.jp.daichi.designer.ingame.manager;

import com.jp.daichi.designer.editor.EditorImageObject;
import com.jp.daichi.designer.ingame.InGameImageObject;
import com.jp.daichi.designer.interfaces.*;
import com.jp.daichi.designer.simple.manager.SimpleDesignerObjectManager;

import java.util.Map;
import java.util.UUID;

/**
 * ゲーム用のデザイナーオブジェクトマネージャーの実装
 */
public class InGameDesignerObjectManager extends SimpleDesignerObjectManager {

    private Canvas canvas;

    /**
     * 対象となるキャンバスを設定
     * @param canvas キャンバス
     */
    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }

    @Override
    public <T extends DesignerObject> T createInstance(String name, DesignerObjectType type) {
        String resolvedName = resolveName(null,name);
        DesignerObject designerObject = null;
        if (type == DesignerObjectType.IMAGE) {
            designerObject = new InGameImageObject(resolvedName, UUID.randomUUID(),canvas,new Point(0,0),new SignedDimension(0,0));
        } else {
            //TODO
        }

        if (designerObject != null) {
            addInstance(designerObject);
            return (T) designerObject;
        } else {
            return null;
        }
    }

    @Override
    public DesignerObject deserializeManagedObject(Map<String, Object> map) {
        return null;
    }
}
