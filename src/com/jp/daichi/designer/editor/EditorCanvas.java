package com.jp.daichi.designer.editor;

import com.jp.daichi.designer.editor.history.SimpleHistoryStaff;
import com.jp.daichi.designer.interfaces.Canvas;
import com.jp.daichi.designer.interfaces.UpdateObserver;
import com.jp.daichi.designer.interfaces.editor.History;
import com.jp.daichi.designer.interfaces.editor.PermanentObject;
import com.jp.daichi.designer.interfaces.manager.DesignerObjectManager;
import com.jp.daichi.designer.interfaces.manager.LayerManager;
import com.jp.daichi.designer.interfaces.manager.MaterialManager;
import com.jp.daichi.designer.simple.SimpleCanvas;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static com.jp.daichi.designer.simple.DeserializeUtil.*;

/**
 * エディタ用のキャンバスの実装
 */
public class EditorCanvas extends SimpleCanvas implements PermanentObject {

    /**
     * デシリアライズを行う
     *
     * @param history               履歴
     * @param materialManager       マテリアルマネージャー
     * @param layerManager          レイヤーマネージャー
     * @param designerObjectManager デザイナーオブジェクトマネージャー
     * @param serialized            シリアライズされたデータ
     * @return デシリアライズの結果
     */
    public static Canvas deserialize(History history, MaterialManager materialManager, LayerManager layerManager, DesignerObjectManager designerObjectManager, Map<String, Object> serialized) {
        EditorCanvas canvas = new EditorCanvas(history, materialManager, layerManager, designerObjectManager);
        try {
            canvas.setSaveHistory(false);
            Double pov = (Double) serialized.get(POV);
            if (pov != null) {
                canvas.setPov(pov);
            }
            Rectangle viewPort = (Rectangle) serialized.get(VIEWPORT);
            Objects.requireNonNull(viewPort);
            canvas.setViewport(viewPort);
            Double fogStrength = (Double) serialized.get(FOG_STRENGTH);
            if (fogStrength != null) {
                canvas.setFogStrength(fogStrength);
            }
            Color color = (Color) serialized.get(FOG_COLOR);
            canvas.setFogColor(color);
            UUID materialUUID = (UUID) serialized.get(MATERIAL_UUID);
            canvas.setMaterialUUID(materialUUID);
            canvas.setSaveHistory(true);
            return canvas;
        } catch (ClassCastException | NullPointerException e) {
            e.printStackTrace();
            return null;
        }
    }

    private final History history;
    private boolean saveHistory = true;
    private final Frame frame = new SimpleFrame(this);


    /**
     * 新しいキャンバスのインスタンスを作成する
     *
     * @param history               履歴
     * @param materialManager       マテリアルマネージャー
     * @param layerManager          レイヤーマネージャー
     * @param designerObjectManager デザイナーオブジェクトマネージャー
     */
    public EditorCanvas(History history, MaterialManager materialManager, LayerManager layerManager, DesignerObjectManager designerObjectManager) {
        super(materialManager, layerManager, designerObjectManager);
        this.history = history;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> result = new HashMap<>();
        result.put(POV, getPov());
        result.put(VIEWPORT, getViewport());
        result.put(FOG_STRENGTH, getFogStrength());
        result.put(FOG_COLOR, getFogColor());
        result.put(MATERIAL_UUID, getMaterialUUID());
        return result;
    }

    @Override
    public void setUpdateObserver(UpdateObserver updateObserver) {
        super.setUpdateObserver(updateObserver);
        frame.setUpdateObserver(updateObserver);
    }

    /**
     * 選択用の枠線を取得する
     *
     * @return 枠線
     */
    public Frame getFrame() {
        return frame;
    }

    @Override
    public void draw(Graphics2D g, int width, int height) {
        super.draw(g, width, height);
        frame.draw(g);
    }

    @Override
    public void setMaterialUUID(UUID uuid) {
        UUID oldValue = getMaterialUUID();
        super.setMaterialUUID(uuid);
        if (saveHistory) {
            history.add(new SetMaterialUUID(null, oldValue, getMaterialUUID()));
        }
    }

    @Override
    public void setPov(double angle) {
        double oldValue = getPov();
        super.setPov(angle);
        if (saveHistory) {
            history.add(new SetPov(null, oldValue, getPov()));
        }
    }


    @Override
    public void setFogColor(Color color) {
        Color old = getFogColor();
        super.setFogColor(color);
        if (saveHistory) {
            history.add(new SetFogColor(null, old, getFogColor()));
        }
    }

    @Override
    public void setFogStrength(double fogStrength) {
        double old = getFogStrength();
        super.setFogStrength(fogStrength);
        if (saveHistory) {
            history.add(new SetFogStrength(null, old, getFogStrength()));
        }
    }


    @Override
    public boolean saveHistory() {
        return saveHistory;
    }

    @Override
    public void setSaveHistory(boolean saveHistory) {
        this.saveHistory = saveHistory;
    }

    /**
     * 履歴を取得する
     *
     * @return 履歴
     */
    public History getHistory() {
        return history;
    }

    private class SetMaterialUUID extends SimpleHistoryStaff<EditorCanvas, UUID> {

        public SetMaterialUUID(UUID uuid, UUID oldValue, UUID newValue) {
            super(uuid, oldValue, newValue);
        }

        @Override
        public void setValue(EditorCanvas target, UUID value) {
            target.setMaterialUUID(value);
        }

        @Override
        public EditorCanvas getTarget(Canvas canvas) {
            return (EditorCanvas) canvas;
        }

        @Override
        public String description() {
            return "Set background image";
        }
    }

    private static class SetPov extends SimpleHistoryStaff<EditorCanvas, Double> {

        public SetPov(UUID uuid, Double oldValue, Double newValue) {
            super(uuid, oldValue, newValue);
        }

        @Override
        public void setValue(EditorCanvas target, Double value) {
            target.setPov(value);
        }

        @Override
        public EditorCanvas getTarget(Canvas canvas) {
            if (canvas instanceof EditorCanvas editorCanvas) {
                return editorCanvas;
            } else {
                return null;
            }
        }

        @Override
        public String description() {
            return "set pov:" + newValue;
        }
    }

    private static class SetFogColor extends SimpleHistoryStaff<EditorCanvas, Color> {

        public SetFogColor(UUID uuid, Color oldValue, Color newValue) {
            super(uuid, oldValue, newValue);
        }

        @Override
        public void setValue(EditorCanvas target, Color value) {
            target.setFogColor(value);
        }

        @Override
        public EditorCanvas getTarget(Canvas canvas) {
            if (canvas instanceof EditorCanvas editorCanvas) {
                return editorCanvas;
            } else {
                return null;
            }
        }

        @Override
        public String description() {
            return "set fog color:" + newValue;
        }
    }

    private static class SetFogStrength extends SimpleHistoryStaff<EditorCanvas, Double> {

        public SetFogStrength(UUID uuid, Double oldValue, Double newValue) {
            super(uuid, oldValue, newValue);
        }

        @Override
        public void setValue(EditorCanvas target, Double value) {
            target.setFogStrength(value);
        }

        @Override
        public EditorCanvas getTarget(Canvas canvas) {
            if (canvas instanceof EditorCanvas editorCanvas) {
                return editorCanvas;
            } else {
                return null;
            }
        }

        @Override
        public String description() {
            return "set fog strength:" + newValue;
        }
    }
}
