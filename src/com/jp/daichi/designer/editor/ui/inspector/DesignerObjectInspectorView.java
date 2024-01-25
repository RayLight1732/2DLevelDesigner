package com.jp.daichi.designer.editor.ui.inspector;

import com.jp.daichi.designer.editor.ui.CustomBoxLayout;
import com.jp.daichi.designer.editor.ui.ObserverJPanel;
import com.jp.daichi.designer.editor.ui.SmoothJLabel;
import com.jp.daichi.designer.editor.ui.ViewUtil;
import com.jp.daichi.designer.interfaces.*;

import javax.swing.*;
import java.text.NumberFormat;

import static com.jp.daichi.designer.editor.ui.ViewUtil.labelHorizontalStruct;

/**
 * デザイナーオブジェクト用のインスペクタービュー
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
        setBorder(BorderFactory.createEmptyBorder(10, ViewUtil.LEFT_PADDING, 4, 4));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(InspectorView.createTitlePanel(designerObject.getType().getDisplayName()+" Object Information"));
        add(Box.createVerticalStrut(4));
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
        ViewUtil.addTextFieldListener(textField,designerObject::setName);
        return textField;
    }
    private JComponent createPositionPanel() {
        JPanel parent = new JPanel();
        parent.setLayout(new BoxLayout(parent, BoxLayout.X_AXIS));
        parent.add(new SmoothJLabel("Position"));
        parent.add(Box.createHorizontalStrut(labelHorizontalStruct));
        parent.add(Box.createGlue());

        JPanel panel = new JPanel();
        panel.setLayout(new CustomBoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(new SmoothJLabel("X"));
        panel.add(Box.createHorizontalStrut(4));
        panel.add(ViewUtil.createNumberTextField(designerObject.getPosition().x(), new ViewUtil.SetterRunnable<>(value -> designerObject.setPosition(new Point(value.doubleValue(),designerObject.getPosition().y())))));
        panel.add(Box.createHorizontalStrut(4));
        panel.add(new SmoothJLabel("Y"));
        panel.add(Box.createHorizontalStrut(4));
        panel.add(ViewUtil.createNumberTextField(designerObject.getPosition().y(),new ViewUtil.SetterRunnable<>(value -> designerObject.setPosition(new Point(designerObject.getPosition().x(), value.doubleValue())))));
        panel.add(Box.createHorizontalStrut(4));
        panel.add(new SmoothJLabel("Z"));
        panel.add(Box.createHorizontalStrut(4));
        panel.add(ViewUtil.createNumberTextField(designerObject.getZ(),new ViewUtil.SetterRunnable<>(value ->designerObject.setZ(value.doubleValue()))));

        parent.add(panel);
        return parent;
    }

    private JComponent createSizePanel() {
        JPanel parent = new JPanel();
        parent.setLayout(new BoxLayout(parent, BoxLayout.X_AXIS));
        parent.add(new SmoothJLabel("Size"));
        parent.add(Box.createHorizontalStrut(labelHorizontalStruct));
        parent.add(Box.createGlue());

        JPanel panel = new JPanel();
        panel.setLayout(new CustomBoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(new SmoothJLabel("Width"));
        panel.add(Box.createHorizontalStrut(4));
        panel.add(ViewUtil.createNumberTextField(designerObject.getDimension().width(),new ViewUtil.SetterRunnable<>(value -> designerObject.setDimension(new SignedDimension(value.doubleValue(),designerObject.getDimension().height())))));
        panel.add(Box.createHorizontalStrut(4));
        panel.add(new SmoothJLabel("Height"));
        panel.add(Box.createHorizontalStrut(4));
        panel.add(ViewUtil.createNumberTextField(designerObject.getDimension().height(),new ViewUtil.SetterRunnable<>(value -> designerObject.setDimension(new SignedDimension(designerObject.getDimension().width(), value.doubleValue())))));

        parent.add(panel);
        return parent;
    }


    private JComponent createPriorityPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(new SmoothJLabel("Priority"));
        panel.add(Box.createHorizontalStrut(labelHorizontalStruct));
        panel.add(Box.createGlue());
        NumberFormat format = NumberFormat.getIntegerInstance();
        format.setGroupingUsed(false);
        JTextField textField = ViewUtil.createNumberTextField(designerObject.getPriority(),new ViewUtil.SetterRunnable<>(value -> designerObject.setPriority(value.intValue())),true);
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
