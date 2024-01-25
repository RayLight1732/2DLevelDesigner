package com.jp.daichi.designer.editor;

import com.jp.daichi.designer.interfaces.Canvas;
import com.jp.daichi.designer.interfaces.DesignerObject;
import com.jp.daichi.designer.interfaces.Layer;
import com.jp.daichi.designer.interfaces.Material;
import com.jp.daichi.designer.interfaces.editor.EditorDesignerObject;
import com.jp.daichi.designer.interfaces.editor.PermanentObject;
import com.jp.daichi.designer.interfaces.manager.DesignerObjectManager;
import com.jp.daichi.designer.interfaces.manager.MaterialManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class DataSaver {

    private final File parentFolder;

    public DataSaver(File parentFolder) {
        this.parentFolder = parentFolder;
    }

    public void saveAll(Canvas canvas) {
        canvas.getLayers().stream()
                .map(uuid->canvas.getLayerManager().getInstance(uuid))
                .filter(Objects::nonNull)
                .forEach(layer->{
                    save((EditorLayer)layer);
                    layer.getObjects().stream()
                            .map(uuid->canvas.getDesignerObjectManager().getInstance(uuid))
                            .forEach(designerObject -> save((EditorDesignerObject)designerObject));
                });
        canvas.getMaterialManager().getAllInstances().forEach(material -> save((EditorMaterial)material));
    }

    public void save(PermanentObject target) {
        if (target instanceof Layer) {
            save(target,Path.of(parentFolder.getAbsolutePath(),"layers",((Layer) target).getUUID().toString()+".bin"));
        } else if (target instanceof DesignerObject) {
            save(target,Path.of(parentFolder.getAbsolutePath(),"objects",((DesignerObject) target).getUUID().toString()+".bin"));
        } else if (target instanceof Material) {
            save(target,Path.of(parentFolder.getAbsolutePath(),"materials",((Material) target).getUUID().toString()+".bin"));
        } else if (target instanceof EditorCanvas editorCanvas) {
            System.out.println("save");
            save(target,Path.of(parentFolder.getAbsolutePath(),"canvas.bin"));
        }
    }

    /**
     * 新たに登録されたものや、削除されたもののデータの更新を行う
     * @param manager デザイナーオブジェクトマネージャー
     */
    public void update(DesignerObjectManager manager) {
        update(Path.of(parentFolder.getAbsolutePath(),"objects"),manager.getAllInstances(), DesignerObject::getUUID);
    }

    /**
     * 新たに登録されたものや、削除されたもののデータの更新を行う
     * @param manager デザイナーオブジェクトマネージャー
     */
    public void update(MaterialManager manager) {
        update(Path.of(parentFolder.getAbsolutePath(),"materials"),manager.getAllInstances(), Material::getUUID);
    }

    private <T> void update(Path targetParentPath,List<T> allInstances,UUIDGetter<T> getter) {
        try {
            if (Files.notExists(targetParentPath)) {
                Files.createDirectories(targetParentPath);
            }
            File[] files = targetParentPath.toFile().listFiles();
            if (files != null) {
                List<String> uuids = allInstances.stream().map(getter::get).map(UUID::toString).toList();
                for (File file:files) {
                    if (!uuids.contains(file.getName().replaceAll("\\.bin$",""))){
                        file.delete();
                    }
                }
            }
            files = targetParentPath.toFile().listFiles();
            files = files == null ? new File[]{} : files;
            List<String> uuids = Arrays.stream(files).map(file -> file.getName().replaceAll("\\.bin$","")).toList();
            for (T designerObject:allInstances) {
                String uuid = getter.get(designerObject).toString();
                if (!uuids.contains(uuid)) {
                    save((PermanentObject) designerObject);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void save(PermanentObject target, Path targetPath) {
        try {
            createDirectoriesAndFile(targetPath);
            serializeObject(target, targetPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createDirectoriesAndFile(Path filePath) throws IOException {
        if (Files.notExists(filePath)) {
            Files.createDirectories(filePath.getParent());
            Files.createFile(filePath);
        }
    }

    private void serializeObject(PermanentObject target, Path filePath) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath.toString()))) {
            oos.writeObject(target.serialize());
        }
    }

    private interface UUIDGetter<T> {
        UUID get(T target);
    }
}
