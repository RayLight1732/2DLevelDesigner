package com.jp.daichi.designer.simple.editor;

import com.jp.daichi.designer.simple.SimpleMaterial;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class EditorMaterial extends SimpleMaterial {

    private File file;

    public EditorMaterial(String name, UUID uuid) {
        super(name, uuid);
    }


    public void setFile(File file) {
        System.out.println("set image file");
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
    }

    public File getFile() {
        return file;
    }
}
