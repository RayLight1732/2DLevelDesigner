package com.jp.daichi.designer.simple.editor.inspector;

import com.jp.daichi.designer.interfaces.Material;
import com.jp.daichi.designer.interfaces.ObservedObject;
import com.jp.daichi.designer.interfaces.Point;
import com.jp.daichi.designer.interfaces.SignedDimension;
import com.jp.daichi.designer.simple.editor.*;

import javax.swing.*;

public class MaterialInspectorView implements Viewable {

    private final Material material;

    public MaterialInspectorView(Material material) {
        this.material = material;
    }
    @Override
    public JComponent getView() {
        JComponent uvPanel = createUVPanel();
        JComponent uvDimensionPanel = createUVDimensionPanel();
        JPanel panel = new ObserverJPanel() {
            private JComponent uvPanel_ = uvPanel;
            private JComponent uvDimensionPanel_ = uvDimensionPanel;
            @Override
            public void update(ObservedObject target, UpdateAction action) {
                if (target == material) {
                    int index1 = getComponentZOrder(uvPanel_);
                    int index2 = getComponentZOrder(uvDimensionPanel_);
                    remove(uvPanel_);
                    remove(uvDimensionPanel_);
                    uvPanel_ = createUVPanel();
                    uvDimensionPanel_ = createUVDimensionPanel();
                    add(uvPanel_, index1);
                    add(uvDimensionPanel_, index2);
                }
            }
        };
        panel.setBorder(BorderFactory.createEmptyBorder(10, ViewUtils.LEFT_PADDING, 4, 4));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JTextField textField = new JTextField(material.getName());
        panel.add(textField);
        panel.add(Box.createVerticalStrut(4));
        panel.add(uvPanel);
        panel.add(Box.createVerticalStrut(4));
        panel.add(uvDimensionPanel);
        return panel;
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
        panel.add(ViewUtils.createNumberTextField(material.getUVDimension().height(),value -> material.setUV(new Point(material.getUVDimension().width(),value.doubleValue()))));
        parent.add(panel);
        return parent;
    }
}
