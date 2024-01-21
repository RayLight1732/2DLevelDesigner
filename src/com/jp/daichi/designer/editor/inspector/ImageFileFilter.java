package com.jp.daichi.designer.editor.inspector;

import javax.swing.filechooser.FileFilter;
import java.io.File;

public class ImageFileFilter extends FileFilter {

    public boolean accept(File f){
        if (f.isDirectory()){
            return true;
        }

        String ext = getExtension(f);
        if (ext != null){
            return ext.equals("png");
        }

        return false;
    }

    public String getDescription(){
        return "PNGファイル";
    }

    /* 拡張子を取り出す */
    private String getExtension(File f){
        String ext = null;
        String filename = f.getName();
        int dotIndex = filename.lastIndexOf('.');

        if ((dotIndex > 0) && (dotIndex < filename.length() - 1)){
            ext = filename.substring(dotIndex + 1).toLowerCase();
        }

        return ext;
    }
}
