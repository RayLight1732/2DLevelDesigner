package com.jp.daichi.designer.editor;

import com.jp.daichi.designer.editor.history.SimpleHistoryStaff;
import com.jp.daichi.designer.interfaces.Canvas;
import com.jp.daichi.designer.interfaces.UpdateAction;
import com.jp.daichi.designer.interfaces.editor.History;
import com.jp.daichi.designer.interfaces.editor.HistoryStaff;
import com.jp.daichi.designer.interfaces.editor.PermanentObject;
import com.jp.daichi.designer.interfaces.manager.DesignerObjectManager;
import com.jp.daichi.designer.interfaces.manager.LayerManager;
import com.jp.daichi.designer.interfaces.manager.MaterialManager;
import com.jp.daichi.designer.simple.SimpleCanvas;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class EditorCanvas extends SimpleCanvas implements PermanentObject {

    private static final String POV = "Pov";
    private static final String VIEWPORT = "ViewPort";
    private static final String FOG_STRENGTH = "FogStrength";
    private static final String FOG_COLOR = "FogColor";
    private static final String PATH = "Path";

    public static Canvas deserialize(History history,MaterialManager materialManager,LayerManager layerManager,DesignerObjectManager designerObjectManager,Map<String,Object> serialized) {
        EditorCanvas canvas = new EditorCanvas(history, materialManager, layerManager, designerObjectManager);
        canvas.setSaveHistory(false);
        try {
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
            String path = (String) serialized.get(PATH);
            if (path != null) {
                canvas.setFile(new File(path));
            }

            return canvas;
        } catch (ClassCastException|NullPointerException e) {
            e.printStackTrace();
            return null;
        }
    }

    private final History history;
    private File file;
    private boolean saveHistory = true;


    public EditorCanvas(History history,MaterialManager materialManager, LayerManager layerManager, DesignerObjectManager designerObjectManager) {
        super(materialManager, layerManager, designerObjectManager);
        this.history = history;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String,Object> result = new HashMap<>();
        result.put(POV,getPov());
        result.put(VIEWPORT, getViewport());
        result.put(FOG_STRENGTH,getFogStrength());
        result.put(FOG_COLOR,getFogColor());
        if (file != null) {
            result.put(PATH,file.getAbsolutePath());
        }
        return result;
    }

    @Override
    public void setPov(double angle) {
        double oldValue = getPov();
        super.setPov(angle);
        if (saveHistory) {
            history.add(new SetPov(null,oldValue,getPov()));
        }
    }


    @Override
    public void setFogColor(Color color) {
        Color old = getFogColor();
        super.setFogColor(color);
        if (saveHistory) {
            history.add(new SetFogColor(null,old,getFogColor()));
        }
    }

    @Override
    public void setFogStrength(double fogStrength) {
        double old = getFogStrength();
        super.setFogStrength(fogStrength);
        if (saveHistory) {
            history.add(new SetFogStrength(null,old,getFogStrength()));
        }
    }

    public void setFile(File file) {
        File oldFile = getFile();
        this.file = file;
        if (file != null && file.exists()) {
            try {
                setBackgroundImage(ImageIO.read(file));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            setBackgroundImage(null);
        }
        sendUpdate(UpdateAction.CHANGE_IMAGE_FILE);
        if (saveHistory) {
            history.add(new SetFile(oldFile != null ? oldFile.getAbsolutePath() : null, file != null ? file.getAbsolutePath() : null));
        }
    }

    public File getFile() {
        return file;
    }

    @Override
    public boolean saveHistory() {
        return saveHistory;
    }

    @Override
    public void setSaveHistory(boolean saveHistory) {
        this.saveHistory = saveHistory;
    }

    private record SetFile(String oldPath, String newPath) implements HistoryStaff {

        @Override
        public String getDescription() {
            return "set background image:" + newPath;
        }

        @Override
        public void undo(Canvas canvas) {
            if (canvas instanceof EditorCanvas editorCanvas) {
                editorCanvas.setSaveHistory(false);
                if (oldPath != null) {
                    editorCanvas.setFile(new File(oldPath));
                } else {
                    editorCanvas.setFile(null);
                }
                editorCanvas.setSaveHistory(true);
            }
        }

        @Override
        public void redo(Canvas canvas) {
            if (canvas instanceof EditorCanvas editorCanvas) {
                editorCanvas.setSaveHistory(false);
                if (newPath != null) {
                    editorCanvas.setFile(new File(newPath));
                } else {
                    editorCanvas.setFile(null);
                }
                editorCanvas.setSaveHistory(true);
            }
        }
    }

    private static class SetPov extends SimpleHistoryStaff<EditorCanvas,Double> {

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
        public String getDescription() {
            return "set pov:"+newValue;
        }
    }

    private static class SetFogColor extends SimpleHistoryStaff<EditorCanvas,Color> {

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
        public String getDescription() {
            return "set fog color:"+newValue;
        }
    }

    private static class SetFogStrength extends SimpleHistoryStaff<EditorCanvas,Double> {

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
        public String getDescription() {
            return "set fog strength:"+newValue;
        }
    }
}
