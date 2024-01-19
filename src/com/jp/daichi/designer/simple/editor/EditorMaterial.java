package com.jp.daichi.designer.simple.editor;

import com.jp.daichi.designer.interfaces.Material;
import com.jp.daichi.designer.interfaces.Point;
import com.jp.daichi.designer.interfaces.SignedDimension;
import com.jp.daichi.designer.interfaces.UpdateObserver;
import com.jp.daichi.designer.interfaces.editor.History;
import com.jp.daichi.designer.interfaces.editor.HistoryStaff;
import com.jp.daichi.designer.interfaces.editor.PermanentObject;
import com.jp.daichi.designer.interfaces.manager.DesignerObjectManager;
import com.jp.daichi.designer.interfaces.manager.LayerManager;
import com.jp.daichi.designer.interfaces.manager.MaterialManager;
import com.jp.daichi.designer.simple.SimpleMaterial;
import com.jp.daichi.designer.simple.editor.history.SimpleHistoryStaff;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * エディタ用に拡張されたマテリアル
 */
public class EditorMaterial extends SimpleMaterial implements PermanentObject {

    public static EditorMaterial deserialize(History history, Map<String, Object> serialized) {
        try {
            String name = (String) serialized.get("Name");
            UUID uuid = (UUID) serialized.get("UUID");
            Objects.requireNonNull(name);
            Objects.requireNonNull(uuid);
            String path = (String) serialized.get("Path");
            if (path != null) {
                File file = new File(path);
                return new EditorMaterial(history, name, uuid, file);
            } else {
                return new EditorMaterial(history, name, uuid);
            }
        } catch (NullPointerException | ClassCastException e) {
            return null;
        }
    }

    private File file;
    private boolean saveHistory = true;
    private final History history;

    public EditorMaterial(History history, String name, UUID uuid) {
        super(name, uuid);
        this.history = history;
    }

    public EditorMaterial(History history, String name, UUID uuid, File file) {
        this(history, name, uuid);
        UpdateObserver observer = getUpdateObserver();
        setUpdateObserver(null);//監視をいったんやめる→保存がされなくなる
        setSaveHistory(false);//履歴の保存も行わない
        setFile(file);
        setUpdateObserver(observer);
        setSaveHistory(true);
    }

    @Override
    public void setName(String name) {
        String oldName = getName();
        super.setName(name);
        if (saveHistory) {
            history.add(new SetName(getUUID(), oldName, getName()));
        }
    }

    @Override
    public void setUV(Point point) {
        Point oldPoint = getUV();
        super.setUV(point);
        if (saveHistory) {
            history.add(new SetUVPoint(getUUID(), oldPoint, getUV()));
        }
    }

    @Override
    public void setUVDimension(SignedDimension dimension) {
        SignedDimension oldDimension = getUVDimension();
        super.setUVDimension(dimension);
        if (saveHistory) {
            history.add(new SetUVDimension(getUUID(), oldDimension, getUVDimension()));
        }
    }

    public void setFile(File file) {
        File oldFile = getFile();
        this.file = file;
        if (file != null && file.exists()) {
            try {
                setImage(ImageIO.read(file));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            setImage(null);
        }
        sendUpdate(UpdateAction.CHANGE_IMAGE_FILE);
        if (saveHistory) {
            history.add(new SetFile(getUUID(), oldFile != null ? oldFile.getAbsolutePath() : null, file != null ? file.getAbsolutePath() : null));
        }
    }

    public File getFile() {
        return file;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> result = new HashMap<>();
        result.put("UUID", getUUID());
        if (file != null) {
            result.put("Path", file.getAbsolutePath());
        }
        result.put("Name", getName());
        return result;
    }

    @Override
    public boolean saveHistory() {
        return saveHistory;
    }

    @Override
    public void setSaveHistory(boolean saveHistory) {
        this.saveHistory = saveHistory;
    }

    private record SetFile(UUID uuid, String oldPath, String newPath) implements HistoryStaff {

        @Override
        public String getDescription() {
            return "set image file:" + newPath;
        }

        @Override
        public void undo(DesignerObjectManager designerObjectManager, LayerManager layerManager, MaterialManager materialManager) {
            EditorMaterial target = ((EditorMaterial) materialManager.getInstance(uuid));
            if (target != null) {
                target.setSaveHistory(false);
                if (oldPath != null) {
                    ((EditorMaterial) materialManager.getInstance(uuid)).setFile(new File(oldPath));
                }
                target.setSaveHistory(true);
            }
        }

        @Override
        public void redo(DesignerObjectManager designerObjectManager, LayerManager layerManager, MaterialManager materialManager) {
            EditorMaterial target = ((EditorMaterial) materialManager.getInstance(uuid));
            if (target != null) {
                target.setSaveHistory(false);
                if (newPath != null) {
                    ((EditorMaterial) materialManager.getInstance(uuid)).setFile(new File(newPath));
                }
                target.setSaveHistory(true);
            }
        }
    }

    private static class SetUVPoint extends SimpleHistoryStaff<EditorMaterial,Point> {
        public SetUVPoint(UUID uuid, Point oldValue, Point newValue) {
            super(uuid, oldValue, newValue);
        }

        @Override
        public String getDescription() {
            return "Set UV Point:("+newValue.x()+","+newValue.y()+")";
        }
        @Override
        public void setValue(EditorMaterial target, Point value) {
            target.setUV(value);
        }

        @Override
        public EditorMaterial getTarget(DesignerObjectManager designerObjectManager, LayerManager layerManager, MaterialManager materialManager) {
            return (EditorMaterial) materialManager.getInstance(uuid);
        }
    }

    private static class SetUVDimension extends SimpleHistoryStaff<EditorMaterial, SignedDimension> {

        public SetUVDimension(UUID uuid, SignedDimension oldValue, SignedDimension newValue) {
            super(uuid, oldValue, newValue);
        }

        @Override
        public String getDescription() {
            return "Set UV Dimension:(width="+newValue.width()+",height="+newValue.height()+")";//TODO 名前を表示するのもありかも
        }

        @Override
        public void setValue(EditorMaterial target, SignedDimension value) {
            target.setUVDimension(value);
        }

        @Override
        public EditorMaterial getTarget(DesignerObjectManager designerObjectManager, LayerManager layerManager, MaterialManager materialManager) {
            return (EditorMaterial) materialManager.getInstance(uuid);
        }
    }

    private static class SetName extends SimpleHistoryStaff<EditorMaterial,String> {

        public SetName(UUID uuid, String oldValue, String newValue) {
            super(uuid, oldValue, newValue);
        }

        @Override
        public String getDescription() {
            return "Set Material Name:"+newValue;
        }

        @Override
        public void setValue(EditorMaterial target, String value) {
            target.setName(value);
        }

        @Override
        public EditorMaterial getTarget(DesignerObjectManager designerObjectManager, LayerManager layerManager, MaterialManager materialManager) {
            return (EditorMaterial) materialManager.getInstance(uuid);
        }
    }
}
