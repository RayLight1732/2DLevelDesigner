package com.jp.daichi.designer.editor.inspector;

import com.jp.daichi.designer.editor.EditorCanvas;
import com.jp.daichi.designer.interfaces.DesignerObject;
import com.jp.daichi.designer.interfaces.ImageObject;
import com.jp.daichi.designer.interfaces.editor.InspectorManager;
import com.jp.daichi.designer.editor.EditorMaterial;
import com.jp.daichi.designer.editor.WindowManager;

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
        } else if (object instanceof EditorCanvas editorCanvas) {
            return new CanvasInspectorView(editorCanvas,windowManager);
        } else {
            return null;
        }
    }

    public boolean isShowed(Object object) {
        if (object instanceof EditorMaterial) {
            if (windowManager.inspectorView().getView() instanceof MaterialInspectorView materialInspectorView) {
                return materialInspectorView.getMaterial() == object;
            }
        } else if (object instanceof ImageObject) {
            if (windowManager.inspectorView().getView() instanceof ImageObjectInspectorView imageObjectInspectorView) {
                return imageObjectInspectorView.getDesignerObject() == object;
            }
        } else if (object instanceof DesignerObject) {
            if (windowManager.inspectorView().getView() instanceof DesignerObjectInspectorView designerObjectInspectorView) {
                return designerObjectInspectorView.getDesignerObject() == object;
            }
        }
        return false;
    }
}
