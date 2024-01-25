package com.jp.daichi.designer.editor;

import com.jp.daichi.designer.editor.history.SimpleHistoryStaff;
import com.jp.daichi.designer.interfaces.Canvas;
import com.jp.daichi.designer.interfaces.Point;
import com.jp.daichi.designer.interfaces.SignedDimension;
import com.jp.daichi.designer.interfaces.editor.History;
import com.jp.daichi.designer.interfaces.editor.HistoryStaff;
import com.jp.daichi.designer.interfaces.editor.PermanentObject;
import com.jp.daichi.designer.interfaces.manager.MaterialManager;
import com.jp.daichi.designer.simple.BufferedImageSerializer;
import com.jp.daichi.designer.simple.SimpleMaterial;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * エディタ用のマテリアルの実装
 */
public class EditorMaterial extends SimpleMaterial implements PermanentObject {


    /**
     * デシリアライズを行う
     * @param history 履歴
     * @param materialManager マネージャー
     * @param serialized シリアライズされたデータ
     * @return デシリアライズされた結果
     */
    public static EditorMaterial deserialize(History history,MaterialManager materialManager, Map<String, Object> serialized) {
        try {
            String name = (String) serialized.get("Name");
            UUID uuid = (UUID) serialized.get("UUID");
            Point uv = (Point)serialized.get("UV");
            SignedDimension uvDimension = (SignedDimension) serialized.get("UVDimension");
            Objects.requireNonNull(name);
            Objects.requireNonNull(uuid);
            Objects.requireNonNull(uv);
            Objects.requireNonNull(uvDimension);
            BufferedImageSerializer biSerializer = (BufferedImageSerializer) serialized.get("Image");
            String path = (String) serialized.get("Path");
            File file;
            if (path != null) {
                file = new File(path);
            } else {
                file = null;
            }
            return new EditorMaterial(history, name, uuid,file,biSerializer == null ? null : biSerializer.getImage(),uv,uvDimension,materialManager);
        } catch (NullPointerException | ClassCastException e) {
            return null;
        }
    }

    private boolean saveHistory = true;
    private final History history;
    private File file;

    /**
     * 新しいマテリアルを作成する
     * @param history 履歴
     * @param name 名前
     * @param uuid UUID
     * @param materialManager マネージャー
     */
    public EditorMaterial(History history, String name, UUID uuid,MaterialManager materialManager) {
        super(name, uuid,materialManager);
        this.history = history;
    }

    /**
     * 新しいマテリアルを作成する
     * @param history 履歴
     * @param name 名前
     * @param uuid UUID
     * @param file 画像ファイル
     * @param image 画像
     * @param uv UV座標
     * @param uvDimension UVの描画領域
     * @param materialManager マネージャー
     */
    public EditorMaterial(History history, String name, UUID uuid,File file, BufferedImage image,Point uv,SignedDimension uvDimension, MaterialManager materialManager) {
        super(name, uuid,image,uv,uvDimension,materialManager);
        this.file = file;
        this.history = history;
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

    @Override
    public void setImage(BufferedImage image) {
        //BufferedImage oldImage =getImage();
        super.setImage(image);
        /*if (saveHistory) {
            history.add(new SetImage(getUUID(), oldImage, getImage()));
        }*/
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> result = new HashMap<>();
        result.put("UUID", getUUID());
        result.put("Name", getName());
        result.put("UV",getUV());
        result.put("UVDimension",getUVDimension());
        if (getImage() != null) {
            result.put("Image",new BufferedImageSerializer(getImage()));
        }
        if (getImageFile() != null) {
            result.put("Path",getImageFile().getPath());
        }
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

    public File getImageFile() {
        return file;
    }

    public void setImageFile(File file) throws IOException {
        File oldFile = getImageFile();
        this.file = file;
        if (file != null) {
            setImage(ImageIO.read(file));
        } else {
            setImage(null);
        }
        if (saveHistory) {
            history.add(new SetFile(getUUID(),oldFile == null ? null:oldFile.getPath(),file == null ? null: file.getPath()));
        }
    }

    /*
    private static class SetImage implements HistoryStaff {
        private final UUID uuid;
        private final BufferedImageSerializer oldData;
        private final BufferedImageSerializer newData;
        private SetImage(UUID uuid, BufferedImage oldData,BufferedImage newData) {
            this.uuid = uuid;
            this.oldData = new BufferedImageSerializer(oldData);
            this.newData = new BufferedImageSerializer(newData);
        }

        @Override
        public String description() {
            return "set image";
        }

        @Override
        public void undo(Canvas canvas) {
            MaterialManager materialManager = canvas.getMaterialManager();
            EditorMaterial target = ((EditorMaterial) materialManager.getInstance(uuid));
            if (target != null) {
                target.setSaveHistory(false);
                target.setImage(oldData.getImage());
                target.setSaveHistory(true);
            }
        }

        @Override
        public void redo(Canvas canvas) {
            MaterialManager materialManager = canvas.getMaterialManager();
            EditorMaterial target = ((EditorMaterial) materialManager.getInstance(uuid));
            if (target != null) {
                target.setSaveHistory(false);
                target.setImage(newData.getImage());
                target.setSaveHistory(true);
            }
        }
    }
     */

    private static class SetFile extends SimpleHistoryStaff<EditorMaterial,String> {
        public SetFile(UUID uuid, String oldValue, String newValue) {
            super(uuid, oldValue, newValue);
        }

        @Override
        public void setValue(EditorMaterial target, String value) {
            File file;
            if (value == null) {
                file = null;
            } else {
                file = new File(value);
            }
            try {
                target.setImageFile(file);
            } catch (IOException ignore) {

            }
        }

        @Override
        public EditorMaterial getTarget(Canvas canvas) {
            return (EditorMaterial) canvas.getMaterialManager().getInstance(uuid);
        }

        @Override
        public String description() {
            return "set image file:"+newValue;
        }
    }
    private static class SetUVPoint extends SimpleHistoryStaff<EditorMaterial,Point> {
        public SetUVPoint(UUID uuid, Point oldValue, Point newValue) {
            super(uuid, oldValue, newValue);
        }

        @Override
        public String description() {
            return "Set UV Point:("+newValue.x()+","+newValue.y()+")";
        }
        @Override
        public void setValue(EditorMaterial target, Point value) {
            target.setUV(value);
        }

        @Override
        public EditorMaterial getTarget(Canvas canvas) {
            return (EditorMaterial) canvas.getMaterialManager().getInstance(uuid);
        }
    }

    private static class SetUVDimension extends SimpleHistoryStaff<EditorMaterial, SignedDimension> {

        public SetUVDimension(UUID uuid, SignedDimension oldValue, SignedDimension newValue) {
            super(uuid, oldValue, newValue);
        }

        @Override
        public String description() {
            return "Set UV Dimension:(width="+newValue.width()+",height="+newValue.height()+")";//TODO 名前を表示するのもありかも
        }

        @Override
        public void setValue(EditorMaterial target, SignedDimension value) {
            target.setUVDimension(value);
        }

        @Override
        public EditorMaterial getTarget(Canvas canvas) {
            return (EditorMaterial) canvas.getMaterialManager().getInstance(uuid);
        }
    }

    private static class SetName extends SimpleHistoryStaff<EditorMaterial,String> {

        public SetName(UUID uuid, String oldValue, String newValue) {
            super(uuid, oldValue, newValue);
        }

        @Override
        public String description() {
            return "Set Material Name:"+newValue;
        }

        @Override
        public void setValue(EditorMaterial target, String value) {
            target.setName(value);
        }

        @Override
        public EditorMaterial getTarget(Canvas canvas) {
            return (EditorMaterial) canvas.getMaterialManager().getInstance(uuid);
        }
    }
}
