package com.jp.daichi.designer.simple.editor.inspector;

import com.jp.daichi.designer.interfaces.*;
import com.jp.daichi.designer.interfaces.Point;
import com.jp.daichi.designer.simple.editor.*;

import javax.swing.*;
import java.text.NumberFormat;

/**
 * エディター上のデザイナーオブジェクトのモック
 */
public class DesignerObjectInspectorView extends ObserverJPanel {
    private final DesignerObject designerObject;
    private JComponent namePanel;
    private JComponent positionPanel;
    private JComponent sizePanel;
    private JComponent priorityPanel;

    public DesignerObjectInspectorView(DesignerObject designerObject) {
        this.designerObject = designerObject;
        namePanel = createNamePanel();
        positionPanel = createPositionPanel();
        sizePanel = createSizePanel();
        priorityPanel = createPriorityPanel();
        init();
    }

    private void init() {
        setBorder(BorderFactory.createEmptyBorder(10, ViewUtils.LEFT_PADDING, 4, 4));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(namePanel);
        add(Box.createVerticalStrut(4));
        add(positionPanel);
        add(Box.createVerticalStrut(4));
        add(sizePanel);
        add(Box.createVerticalStrut(4));
        add(priorityPanel);
    }

    private JComponent createNamePanel() {
        JTextField textField = new JTextField(designerObject.getName());
        ViewUtils.addTextFieldListener(textField, designerObject::setName);
        return textField;
    }
    private JComponent createPositionPanel() {
        JPanel parent = new JPanel();
        parent.setLayout(new BoxLayout(parent, BoxLayout.X_AXIS));
        parent.add(new JLabel("Position"));
        parent.add(Box.createHorizontalStrut(4));
        parent.add(Box.createGlue());

        JPanel panel = new JPanel();
        panel.setLayout(new CustomBoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(new JLabel("X"));
        panel.add(Box.createHorizontalStrut(4));
        panel.add(ViewUtils.createNumberTextField(designerObject.getPosition().x(), value -> designerObject.setPosition(new Point(value.doubleValue(),designerObject.getPosition().y()))));
        panel.add(Box.createHorizontalStrut(4));
        panel.add(new JLabel("Y"));
        panel.add(Box.createHorizontalStrut(4));
        panel.add(ViewUtils.createNumberTextField(designerObject.getPosition().y(), value -> designerObject.setPosition(new Point(designerObject.getPosition().x(), value.doubleValue()))));
        panel.add(Box.createHorizontalStrut(4));
        panel.add(new JLabel("Z"));
        panel.add(Box.createHorizontalStrut(4));
        panel.add(ViewUtils.createNumberTextField(designerObject.getZ(), value ->designerObject.setZ(value.doubleValue())));

        parent.add(panel);
        return parent;
    }

    private JComponent createSizePanel() {
        JPanel parent = new JPanel();
        parent.setLayout(new BoxLayout(parent, BoxLayout.X_AXIS));
        parent.add(new JLabel("Size"));
        parent.add(Box.createHorizontalStrut(4));
        parent.add(Box.createGlue());

        JPanel panel = new JPanel();
        panel.setLayout(new CustomBoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(new JLabel("Width"));
        panel.add(Box.createHorizontalStrut(4));
        panel.add(ViewUtils.createNumberTextField(designerObject.getDimension().width(), value -> designerObject.setDimension(new SignedDimension(value.doubleValue(),designerObject.getDimension().height()))));
        panel.add(Box.createHorizontalStrut(4));
        panel.add(new JLabel("Height"));
        panel.add(Box.createHorizontalStrut(4));
        panel.add(ViewUtils.createNumberTextField(designerObject.getDimension().height(), value -> designerObject.setDimension(new SignedDimension(designerObject.getDimension().width(), value.doubleValue()))));

        parent.add(panel);
        return parent;
    }


    private JComponent createPriorityPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(new JLabel("Priority"));
        panel.add(Box.createGlue());
        NumberFormat format = NumberFormat.getIntegerInstance();
        format.setGroupingUsed(false);
        JTextField textField = ViewUtils.createNumberTextField(designerObject.getPriority(),value -> designerObject.setPriority(value.intValue()));
        textField.setColumns(20);
        panel.add(textField);
        return panel;
    }


    @Override
    public void update(ObservedObject target, UpdateAction action) {
        if (target == designerObject) {
            int z;
            z = getComponentZOrder(namePanel);
            remove(z);
            namePanel = createNamePanel();
            add(namePanel,z);

            z = getComponentZOrder(positionPanel);
            remove(z);
            positionPanel = createPositionPanel();
            add(positionPanel,z);

            z = getComponentZOrder(sizePanel);
            remove(z);
            sizePanel = createSizePanel();
            add(sizePanel,z);

            z = getComponentZOrder(priorityPanel);
            remove(z);
            priorityPanel = createPriorityPanel();
            add(priorityPanel,z);
        }
    }

    public DesignerObject getDesignerObject() {
        return designerObject;
    }
}
