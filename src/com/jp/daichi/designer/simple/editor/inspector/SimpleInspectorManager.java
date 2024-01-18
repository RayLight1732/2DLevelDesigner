package com.jp.daichi.designer.simple.editor.inspector;

import com.jp.daichi.designer.interfaces.DesignerObject;
import com.jp.daichi.designer.interfaces.ImageObject;
import com.jp.daichi.designer.interfaces.Material;
import com.jp.daichi.designer.interfaces.editor.InspectorManager;
import com.jp.daichi.designer.simple.editor.EditorMaterial;
import com.jp.daichi.designer.simple.editor.WindowManager;

import javax.swing.*;

/**
 * Inspector画面で表示されるコンポーネントの生成を行う
 */
public class SimpleInspectorManager implements InspectorManager {
    private WindowManager windowManager;
    public void setWindowManager(WindowManager windowManager) {
        this.windowManager = windowManager;
    }
    @Override
    public JComponent createInspectorView(Object object) {
        if (object instanceof EditorMaterial material) {
            return new MaterialInspectorView(material,windowManager);
        } else if (object instanceof ImageObject imageObject) {
            return new ImageObjectInspectorView(imageObject);
        } else if (object instanceof DesignerObject designerObject) {
            return new DesignerObjectInspectorView(designerObject);
        } else {
            return null;
        }
    }
}
