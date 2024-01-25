package com.jp.daichi.designer.ingame.manager;

import com.jp.daichi.designer.DataLoader;
import com.jp.daichi.designer.editor.DataSaver;
import com.jp.daichi.designer.editor.EditorCanvas;
import com.jp.daichi.designer.editor.history.SimpleHistory;
import com.jp.daichi.designer.editor.manager.EditorDesignerObjectManager;
import com.jp.daichi.designer.editor.manager.EditorLayerManager;
import com.jp.daichi.designer.editor.manager.EditorMaterialManager;
import com.jp.daichi.designer.ingame.InGameCanvas;
import com.jp.daichi.designer.ingame.InGameMaterial;
import com.jp.daichi.designer.interfaces.Canvas;
import com.jp.daichi.designer.interfaces.UpdateObserver;
import com.jp.daichi.designer.interfaces.editor.History;
import com.jp.daichi.designer.interfaces.editor.PermanentObject;
import com.jp.daichi.designer.interfaces.manager.CanvasManager;
import com.jp.daichi.designer.interfaces.manager.DesignerObjectManager;
import com.jp.daichi.designer.interfaces.manager.LayerManager;
import com.jp.daichi.designer.interfaces.manager.MaterialManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

/**
 * ゲーム用のキャンバスマネージャーの実装
 */
public class InGameCanvasManager implements CanvasManager {
    private final File parentFolder;
    private Canvas canvas;

    /**
     * 新しいキャンバスマネージャーのインスタンスを作成する
     * @param parentFolder データを保存している親フォルダー このフォルダーの下に様々なデータが保存される
     */
    public InGameCanvasManager(File parentFolder) {
        this.parentFolder = parentFolder;
    }

    @Override
    public Canvas getInstance() {
        if (canvas == null) {
            UpdateObserver observer = new UpdateObserver();
            InGameDesignerObjectManager designerObjectManager = new InGameDesignerObjectManager();
            InGameMaterialManager materialManager = new InGameMaterialManager();
            LayerManager layerManager = new InGameLayerManager();

            if (parentFolder.exists()) {
                File[] files = parentFolder.listFiles();
                if (files != null) {
                    for (File file:files) {
                        if (file.getName().equals("canvas.bin")) {
                            this.canvas = deserializeCanvas(Path.of(parentFolder.getAbsolutePath(),"canvas.bin"),materialManager,layerManager,designerObjectManager);
                            if (canvas != null) {
                                canvas.setUpdateObserver(observer);
                                designerObjectManager.setCanvas(canvas);
                                DataLoader loader = new DataLoader(parentFolder);
                                loader.loadData(canvas);
                            }
                            return canvas;
                        }
                    }
                }
            }
        }
        return canvas;
    }

    private Canvas deserializeCanvas(Path path,MaterialManager materialManager,LayerManager layerManager,DesignerObjectManager designerObjectManager) {
        if (Files.exists(path)) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path.toString()))) {
                Map<String, Object> deserialized = (Map<String, Object>) ois.readObject();
                return InGameCanvas.deserialize(materialManager,layerManager,designerObjectManager,deserialized);
            } catch (IOException | ClassNotFoundException | ClassCastException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

}
