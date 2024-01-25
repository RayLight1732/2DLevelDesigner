package com.jp.daichi.designer.editor.ui.inspector;

import com.jp.daichi.designer.editor.*;
import com.jp.daichi.designer.editor.ui.*;
import com.jp.daichi.designer.interfaces.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;

import static com.jp.daichi.designer.editor.ui.ViewUtil.labelHorizontalStruct;

/**
 * マテリアル用のインスペクタービュー
 */
public class MaterialInspectorView extends ObserverJPanel {

    private final EditorMaterial material;
    private JComponent namePanel;
    private JComponent uvPanel;
    private JComponent uvDimensionPanel;
    private JComponent imagePanel;
    private JComponent propertyPanel;
    private final WindowManager windowManager;
    private final EditorCanvas editorCanvas;
    public MaterialInspectorView(EditorMaterial material, WindowManager windowManager,EditorCanvas editorCanvas) {
        this.material = material;
        this.windowManager = windowManager;
        this.editorCanvas = editorCanvas;
        init();
    }

    public Material getMaterial() {
        return material;
    }
    private void init() {
        namePanel = createNamePanel();
        uvPanel = createUVPanel();
        uvDimensionPanel = createUVDimensionPanel();
        imagePanel = createImagePanel();
        setBorder(BorderFactory.createEmptyBorder(10, ViewUtil.LEFT_PADDING, 4, 4));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(InspectorView.createTitlePanel("Material Information"));
        add(Box.createVerticalStrut(4));
        add(namePanel);
        add(Box.createVerticalStrut(4));
        add(uvPanel);
        add(Box.createVerticalStrut(4));
        add(uvDimensionPanel);
        add(Box.createVerticalStrut(4));
        add(imagePanel);
        add(Box.createVerticalStrut(4));
        if (material.getImage() != null) {
            propertyPanel = createPropertyPanel();
            add(propertyPanel);
        }
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
            imagePanel = createImagePanel();
            add(imagePanel,z);

            if (propertyPanel != null) {
                remove(propertyPanel);
            }
            if (material.getImage() != null) {
                propertyPanel = createPropertyPanel();
                add(propertyPanel);
            } else {
                propertyPanel = null;
            }
        }
    }
    private JComponent createNamePanel() {
        SmoothJTextField textField = new SmoothJTextField(material.getName());
        ViewUtil.addTextFieldListener(textField,material::setName);
        return textField;
    }

    private JComponent createUVPanel() {
        JPanel parent = new JPanel();
        parent.setLayout(new BoxLayout(parent, BoxLayout.X_AXIS));
        parent.add(new SmoothJLabel("UV"));
        parent.add(Box.createHorizontalStrut(labelHorizontalStruct));
        parent.add(Box.createGlue());

        JPanel panel = new JPanel();
        panel.setLayout(new CustomBoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(new SmoothJLabel("X"));
        panel.add(Box.createHorizontalStrut(4));
        panel.add(ViewUtil.createNumberTextField(material.getUV().x(),new ViewUtil.SetterRunnable<>(value -> material.setUV(new Point(value.doubleValue(),material.getUV().y())))));
        panel.add(Box.createHorizontalStrut(4));
        panel.add(new SmoothJLabel("Y"));
        panel.add(Box.createHorizontalStrut(4));
        panel.add(ViewUtil.createNumberTextField(material.getUV().y(),new ViewUtil.SetterRunnable<>(value -> material.setUV(new Point(material.getUV().x(),value.doubleValue())))));
        parent.add(panel);
        return parent;
    }

    private JComponent createUVDimensionPanel() {
        JPanel parent = new JPanel();
        parent.setLayout(new BoxLayout(parent, BoxLayout.X_AXIS));
        parent.add(new SmoothJLabel("UV Dimension"));
        parent.add(Box.createHorizontalStrut(labelHorizontalStruct));
        parent.add(Box.createGlue());

        JPanel panel = new JPanel();
        panel.setLayout(new CustomBoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(new SmoothJLabel("Width"));
        panel.add(Box.createHorizontalStrut(4));
        panel.add(ViewUtil.createNumberTextField(material.getUVDimension().width(),new ViewUtil.SetterRunnable<>(value -> material.setUVDimension(new SignedDimension(value.doubleValue(),material.getUVDimension().height())))));
        panel.add(Box.createHorizontalStrut(4));
        panel.add(new SmoothJLabel("Height"));
        panel.add(Box.createHorizontalStrut(4));
        panel.add(ViewUtil.createNumberTextField(material.getUVDimension().height(),new ViewUtil.SetterRunnable<>(value -> material.setUVDimension(new SignedDimension(material.getUVDimension().width(),value.doubleValue())))));
        parent.add(panel);
        return parent;
    }

    private JComponent createImagePanel() {
        JPanel panel = new JPanel();
        SmoothJTextField label = new SmoothJTextField();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(new SmoothJLabel("Image"));
        panel.add(Box.createHorizontalStrut(labelHorizontalStruct));
        panel.add(Box.createGlue());

        File imageFile = material.getImageFile();
        if (imageFile == null) {
            label.setText("None");
        } else {
            label.setText(imageFile.getPath());
        }

        label.setEditable(false);

        panel.add(label);
        label.addMouseListener(imagePanelMouseAdapter);
        return panel;
    }

    private JComponent createPropertyPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel,BoxLayout.X_AXIS));
        panel.add(new SmoothJLabel("Property"));
        panel.add(Box.createHorizontalStrut(labelHorizontalStruct));
        panel.add(Box.createGlue());
        if (material.getImage() != null) {
            panel.add(new SmoothJLabel("Width:"+material.getImage().getWidth()));
            panel.add(Box.createHorizontalStrut(5));
            panel.add(new SmoothJLabel("Height:"+material.getImage().getHeight()));
        }

        return panel;
    }

    private final MouseAdapter imagePanelMouseAdapter = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new ImageFileFilter());
            if (material.getImageFile() != null) {
                fileChooser.setSelectedFile(material.getImageFile());
            }
            int i = fileChooser.showOpenDialog(windowManager.frame());
            if (i == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                editorCanvas.getHistory().startCompress("Set Image:" + file.getName());
                try {
                    material.setImageFile(file);
                    BufferedImage bf = material.getImage();
                    if (bf != null) {
                        material.setUVDimension(new SignedDimension(bf.getWidth(), bf.getHeight()));
                    }
                } catch (IOException exception) {
                    //TODO show dialog
                    exception.printStackTrace();
                }
                editorCanvas.getHistory().finishCompress();
            }
        }
    };

}
