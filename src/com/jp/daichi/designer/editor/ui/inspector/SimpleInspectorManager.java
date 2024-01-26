package com.jp.daichi.designer.editor.ui.inspector;

import com.jp.daichi.designer.editor.EditorCanvas;
import com.jp.daichi.designer.editor.EditorMaterial;
import com.jp.daichi.designer.editor.ui.WindowManager;
import com.jp.daichi.designer.interfaces.DesignerObject;
import com.jp.daichi.designer.interfaces.ImageObject;
import com.jp.daichi.designer.interfaces.editor.InspectorManager;

import javax.swing.*;

/**
 * Inspector画面に表示されるコンポーネントの生成を行う
 */
public class SimpleInspectorManager implements InspectorManager {
    private WindowManager windowManager;
    private final EditorCanvas editorCanvas;

    /**
     * 新しいインスペクターマネージャーのインスタンスを作成する
     *
     * @param canvas キャンバス
     */
    public SimpleInspectorManager(EditorCanvas canvas) {
        this.editorCanvas = canvas;
    }

    /**
     * このインスタンスが参照するウィンドウマネージャーを設定する
     *
     * @param windowManager ウィンドウマネージャー
     */
    public void setWindowManager(WindowManager windowManager) {
        this.windowManager = windowManager;
    }

    @Override
    public JComponent createInspectorView(Object object) {
        if (object instanceof EditorMaterial material) {
            return new MaterialInspectorView(material, windowManager, editorCanvas);
        } else if (object instanceof ImageObject imageObject) {
            return new ImageObjectInspectorView(imageObject);
        } else if (object instanceof DesignerObject designerObject) {
            return new DesignerObjectInspectorView(designerObject);
        } else if (object instanceof EditorCanvas editorCanvas) {
            return new CanvasInspectorView(editorCanvas, windowManager);
        } else {
            return null;
        }
    }

    /**
     * 対象のオブジェクトが現在インスペクターで表示されているか
     *
     * @param object 対象のオブジェクト
     * @return 表示されているならtrue
     */
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
