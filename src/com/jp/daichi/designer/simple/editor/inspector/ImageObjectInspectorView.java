package com.jp.daichi.designer.simple.editor.inspector;

import com.jp.daichi.designer.Utils;
import com.jp.daichi.designer.interfaces.*;
import com.jp.daichi.designer.simple.editor.UpdateAction;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.NumberFormat;

public class ImageObjectInspectorView extends DesignerObjectInspectorView {
    public static final String materialPanelClientPropertyKey = "MaterialPanel";
    private static final Border labelBorder = BorderFactory.createMatteBorder(1,1,1,1,Color.BLACK);//TODO

    private JPanel materialPanel;

    public ImageObjectInspectorView(ImageObject designerObject) {
        super(designerObject);
        add(Box.createVerticalStrut(4));
        materialPanel = createMaterialPanel();
        add(materialPanel);
    }


    @Override
    public void update(ObservedObject target, UpdateAction action) {
        super.update(target, action);
        if (target == getDesignerObject() || target == Utils.getMaterial(getDesignerObject()) || action == UpdateAction.REMOVE_MATERIAL) {
            int z;
            z = getComponentZOrder(materialPanel);
            remove(z);
            materialPanel = createMaterialPanel();
            add(materialPanel,z);
        }
    }

    @Override
    public ImageObject getDesignerObject() {
        return (ImageObject) super.getDesignerObject();
    }

    private JPanel createMaterialPanel() {
        JPanel panel = new JPanel();
        panel.putClientProperty(materialPanelClientPropertyKey,getDesignerObject());
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(new JLabel("Material"));
        panel.add(Box.createGlue());
        NumberFormat format = NumberFormat.getIntegerInstance();
        format.setGroupingUsed(false);
        JLabel label = new JLabel();
        label.setBorder(labelBorder);
        if (getDesignerObject().getMaterialUUID() == null) {
            label.setText("None");
        } else {
            Material material = Utils.getMaterial(getDesignerObject());
            if (material == null) {
                label.setText("Missing");
            } else {
                label.setText(material.getName());
            }
        }
        label.validate();
        Dimension dimension = new Dimension(200,label.getPreferredSize().height);
        label.setPreferredSize(dimension);
        label.setMaximumSize(dimension);
        panel.add(label);
        return panel;
    }
}
