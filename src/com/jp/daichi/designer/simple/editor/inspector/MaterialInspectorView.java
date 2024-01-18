package com.jp.daichi.designer.simple.editor.inspector;

import com.jp.daichi.designer.interfaces.ObservedObject;
import com.jp.daichi.designer.interfaces.Point;
import com.jp.daichi.designer.interfaces.SignedDimension;
import com.jp.daichi.designer.simple.editor.*;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;

public class MaterialInspectorView extends ObserverJPanel {

    private final EditorMaterial material;
    private JComponent namePanel;
    private JComponent uvPanel;
    private JComponent uvDimensionPanel;
    private JComponent imagePanel;
    private final WindowManager windowManager;
    public MaterialInspectorView(EditorMaterial material,WindowManager windowManager) {
        this.material = material;
        this.windowManager = windowManager;
        init();
    }

    private void init() {
        namePanel = createNamePanel();
        uvPanel = createUVPanel();
        uvDimensionPanel = createUVDimensionPanel();
        imagePanel = createImagePathPanel();
        setBorder(BorderFactory.createEmptyBorder(10, ViewUtils.LEFT_PADDING, 4, 4));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(namePanel);
        add(Box.createVerticalStrut(4));
        add(uvPanel);
        add(Box.createVerticalStrut(4));
        add(uvDimensionPanel);
        add(Box.createVerticalStrut(4));

        add(imagePanel);
    }


    @Override
    public void update(ObservedObject target, UpdateAction action) {
        if (target == material) {
            int z;
            z = getComponentZOrder(namePanel);
            remove(z);
            namePanel = createNamePanel();
            add(namePanel,z);

            z = getComponentZOrder(uvPanel);
            remove(z);
            uvPanel = createUVPanel();
            add(uvPanel,z);

            z = getComponentZOrder(uvDimensionPanel);
            remove(z);
            uvDimensionPanel = createUVDimensionPanel();
            add(uvDimensionPanel,z);

            z = getComponentZOrder(imagePanel);
            remove(z);
            imagePanel = createImagePathPanel();
            add(imagePanel,z);
        }
    }
    private JComponent createNamePanel() {
        JTextField textField = new JTextField(material.getName());
        ViewUtils.addTextFieldListener(textField, material::setName);
        return textField;
    }

    private JComponent createUVPanel() {
        JPanel parent = new JPanel();
        parent.setLayout(new BoxLayout(parent, BoxLayout.X_AXIS));
        parent.add(new JLabel("UV"));
        parent.add(Box.createHorizontalStrut(4));
        parent.add(Box.createGlue());

        JPanel panel = new JPanel();
        panel.setLayout(new CustomBoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(new JLabel("X"));
        panel.add(Box.createHorizontalStrut(4));
        panel.add(ViewUtils.createNumberTextField(material.getUV().x(),value -> material.setUV(new Point(value.doubleValue(),material.getUV().y()))));
        panel.add(Box.createHorizontalStrut(4));
        panel.add(new JLabel("Y"));
        panel.add(Box.createHorizontalStrut(4));
        panel.add(ViewUtils.createNumberTextField(material.getUV().y(),value -> material.setUV(new Point(material.getUV().x(),value.doubleValue()))));
        parent.add(panel);
        return parent;
    }

    private JComponent createUVDimensionPanel() {
        JPanel parent = new JPanel();
        parent.setLayout(new BoxLayout(parent, BoxLayout.X_AXIS));
        parent.add(new JLabel("UV Dimension"));
        parent.add(Box.createHorizontalStrut(4));
        parent.add(Box.createGlue());

        JPanel panel = new JPanel();
        panel.setLayout(new CustomBoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(new JLabel("Width"));
        panel.add(Box.createHorizontalStrut(4));
        panel.add(ViewUtils.createNumberTextField(material.getUVDimension().width(),value -> material.setUVDimension(new SignedDimension(value.doubleValue(),material.getUVDimension().height()))));
        panel.add(Box.createHorizontalStrut(4));
        panel.add(new JLabel("Height"));
        panel.add(Box.createHorizontalStrut(4));
        panel.add(ViewUtils.createNumberTextField(material.getUVDimension().height(),value -> material.setUVDimension(new SignedDimension(material.getUVDimension().width(),value.doubleValue()))));
        parent.add(panel);
        return parent;
    }

    private JComponent createImagePathPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(new JLabel("Image"));
        panel.add(Box.createHorizontalStrut(4));
        panel.add(Box.createGlue());
        JLabel label = new JLabel();
        if (material.getFile() == null) {
            label.setText("None");
        } else if (material.getFile().exists()) {
            label.setText(material.getFile().getAbsolutePath());
        } else {
            label.setText("Missing");
        }
        label.validate();
        label.setMinimumSize(new Dimension(0,0));
        int prefWidth = label.getPreferredSize().width+20;
        int prefHeight = label.getPreferredSize().height;
        label.setPreferredSize(new Dimension(Math.max(prefWidth, 200),prefHeight));//TODO 想定以上に小さくなてしまう問題
        label.setBorder(BorderFactory.createMatteBorder(1,1,1,1,ViewUtils.BORDER_COLOR));

        panel.add(label);
        label.addMouseListener(imagePanelMouseAdapter);
        //TODO ダブルクリックしたらMaterialView上のマテリアルに飛ぶ
        return panel;
    }

    private final MouseAdapter imagePanelMouseAdapter = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {

            JFileChooser filechooser = new JFileChooser();
            filechooser.setFileFilter(new PngFilter());
            if (material.getFile() != null) {
                filechooser.setSelectedFile(material.getFile());
            }
            int i = filechooser.showOpenDialog(windowManager.frame());
            if (i == JFileChooser.APPROVE_OPTION) {
                File file = filechooser.getSelectedFile();
                material.setFile(file);
                BufferedImage bf = material.getImage();
                if (bf != null) {
                    material.setUVDimension(new SignedDimension(bf.getWidth(),bf.getHeight()));
                }
            }
            /*
            FileDialog dialog = new FileDialog(windowManager.frame(),"Image",FileDialog.LOAD);
            dialog.setVisible(true);
            String fileName =dialog.getFile();
            if (fileName != null) {
                File file = new File(fileName);
                material.setFile(file);
            }*/
        }
    };

    private static class PngFilter extends FileFilter {

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
}
