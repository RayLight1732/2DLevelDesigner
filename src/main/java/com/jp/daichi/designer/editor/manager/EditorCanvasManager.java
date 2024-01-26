package com.jp.daichi.designer.editor.manager;

import com.jp.daichi.designer.DataLoader;
import com.jp.daichi.designer.editor.DataSaver;
import com.jp.daichi.designer.editor.EditorCanvas;
import com.jp.daichi.designer.editor.history.SimpleHistory;
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
 * エディタ用のキャンバスマネージャーの実装
 */
public class EditorCanvasManager implements CanvasManager {

    private final File parentFolder;
    private Canvas canvas;
    private DataSaver saver;

    /**
     * 新しいキャンバスマネージャーのインスタンスを作成する
     *
     * @param parentFolder データを保存している親フォルダー このフォルダーの下に様々なデータが保存される
     */
    public EditorCanvasManager(File parentFolder) {
        this.parentFolder = parentFolder;
    }

    @Override
    public Canvas getInstance() {
        if (canvas == null) {
            UpdateObserver observer = new UpdateObserver();
            History history = new SimpleHistory();
            history.setUpdateObserver(observer);
            EditorDesignerObjectManager designerObjectManager = new EditorDesignerObjectManager(history);
            MaterialManager materialManager = new EditorMaterialManager(history);
            LayerManager layerManager = new EditorLayerManager(history);

            Canvas canvas = null;
            boolean tried = false;
            if (parentFolder.exists()) {
                File[] files = parentFolder.listFiles();
                if (files != null) {
                    for (File file : files) {
                        if (file.getName().equals("canvas.bin")) {
                            tried = true;
                            canvas = deserializeCanvas(Path.of(parentFolder.getAbsolutePath(), "canvas.bin"), history, materialManager, layerManager, designerObjectManager);
                            break;
                        }
                    }
                }
            }
            if (!tried) {
                canvas = new EditorCanvas(history, materialManager, layerManager, designerObjectManager);
            }
            if (canvas != null) {
                designerObjectManager.setCanvas(canvas);
                canvas.setUpdateObserver(observer);
                DataLoader loader = new DataLoader(parentFolder);
                loader.loadData(canvas);
                this.saver = new DataSaver(parentFolder);
                observer.addRunnable((target, action) -> {
                    if (target instanceof PermanentObject permanentObject) {
                        saver.save(permanentObject);
                    } else if (target instanceof DesignerObjectManager designerObjectManager1) {
                        saver.update(designerObjectManager1);
                    } else if (target instanceof MaterialManager materialManager1) {
                        saver.update(materialManager1);
                    }
                });

                this.canvas = canvas;
            }
        }
        return canvas;
    }

    /**
     * データを保存するためのデータセーバーを取得する
     *
     * @return データセーバー
     */
    public DataSaver getDataSaver() {
        return saver;
    }


    private Canvas deserializeCanvas(Path path, History history, MaterialManager materialManager, LayerManager layerManager, DesignerObjectManager designerObjectManager) {
        if (Files.exists(path)) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path.toString()))) {
                Map<String, Object> deserialized = (Map<String, Object>) ois.readObject();
                return EditorCanvas.deserialize(history, materialManager, layerManager, designerObjectManager, deserialized);
            } catch (IOException | ClassNotFoundException | ClassCastException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }
}
