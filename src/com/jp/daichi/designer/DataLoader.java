package com.jp.daichi.designer;

import com.jp.daichi.designer.interfaces.Canvas;
import com.jp.daichi.designer.interfaces.Layer;
import com.jp.daichi.designer.interfaces.manager.IManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.stream.Stream;

//TODO テスト用
public class DataLoader {

    private final File file;

    public DataLoader(File file) {
        this.file = file;
    }

    public void loadData(Canvas canvas) {
        File materialParentFile = new File(file.getAbsolutePath(),"materials");
        deserialize(canvas.getMaterialManager(),materialParentFile);
        File designerObjectParentFile = new File(file.getAbsolutePath(),"objects");
        deserialize(canvas.getDesignerObjectManager(),designerObjectParentFile);
        File layerParentFile = new File(file.getAbsolutePath(),"layers");
        deserialize(canvas.getLayerManager(),layerParentFile);
        for (Layer layer:canvas.getLayerManager().getAllInstances()) {
            canvas.addLayer(layer.getUUID());
        }
    }

    private void deserialize(IManager<?> manager,File file) {
        if (file.exists()) {
            try (Stream<Path> stream = Files.list(file.toPath())) {
                stream.forEach(p -> {
                    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(p.toString()))) {
                        Map<String, Object> deserialized = (Map<String, Object>) ois.readObject();
                        manager.deserializeManagedObject(deserialized);
                    } catch (IOException | ClassNotFoundException | ClassCastException e) {
                        e.printStackTrace();
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
