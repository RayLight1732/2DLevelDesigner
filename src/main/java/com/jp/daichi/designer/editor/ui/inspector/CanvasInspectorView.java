package com.jp.daichi.designer.editor.ui.inspector;

import com.jp.daichi.designer.editor.*;
import com.jp.daichi.designer.editor.ui.*;
import com.jp.daichi.designer.interfaces.Material;
import com.jp.daichi.designer.interfaces.ObservedObject;
import com.jp.daichi.designer.interfaces.SignedDimension;
import com.jp.daichi.designer.interfaces.UpdateAction;

import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;
import java.util.UUID;
import java.util.function.Supplier;

import static com.jp.daichi.designer.editor.ui.ViewUtil.labelHorizontalStruct;

/**
 * キャンバス用のインスペクタービュー
 */
public class CanvasInspectorView extends ObserverJPanel {
    private final EditorCanvas canvas;
    private final WindowManager windowManager;

    private JComponent povPanel;
    private JComponent materialPanel;
    private JComponent viewPortCenterPanel;
    private JComponent viewPortSizePanel;
    private JComponent fixedYPanel;

    public CanvasInspectorView(EditorCanvas canvas, WindowManager windowManager) {
        this.canvas = canvas;
        this.windowManager = windowManager;
        init();
    }

    private void init() {
        materialPanel = createMaterialPanel();
        povPanel = createPovPanel();
        viewPortCenterPanel = createViewPortCenterPanel();
        viewPortSizePanel = createViewPortSizePanel();
        fixedYPanel = createFixedYPanel();
        setBorder(BorderFactory.createEmptyBorder(10, ViewUtil.LEFT_PADDING, 4, 4));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(InspectorView.createTitlePanel("Canvas Information"));
        add(Box.createVerticalStrut(4));
        add(povPanel);
        add(Box.createVerticalStrut(4));
        add(viewPortCenterPanel);
        add(Box.createVerticalStrut(4));
        add(viewPortSizePanel);
        add(Box.createVerticalStrut(4));
        add(materialPanel);
        add(Box.createVerticalStrut(4));
        add(fixedYPanel);
    }


    @Override
    public void update(ObservedObject target, UpdateAction action) {
        if (target == canvas) {
            if (action == UpdateAction.CHANGE_POV) {
                int z = getComponentZOrder(povPanel);
                remove(z);
                povPanel = createPovPanel();
                add(povPanel, z);
            } else if (action == UpdateAction.CHANGE_MATERIAL) {
                int z = getComponentZOrder(materialPanel);
                remove(z);
                materialPanel = createMaterialPanel();
                add(materialPanel, z);
            } else if (action == UpdateAction.CHANGE_VIEWPORT) {
                int z = getComponentZOrder(viewPortCenterPanel);
                remove(z);
                viewPortCenterPanel = createViewPortCenterPanel();
                add(viewPortCenterPanel,z);
                z = getComponentZOrder(viewPortSizePanel);
                remove(z);
                viewPortSizePanel = createViewPortSizePanel();
                add(viewPortSizePanel,z);
            } else if (action == UpdateAction.FIXED_Y) {
                int z = getComponentZOrder(fixedYPanel);
                remove(z);
                fixedYPanel = createFixedYPanel();
                add(fixedYPanel,z);
            }
        } else if (target instanceof Material material && material.getUUID().compareTo(canvas.getMaterialUUID()) == 0) {
            int z = getComponentZOrder(materialPanel);
            remove(z);
            materialPanel = createMaterialPanel();
            add(materialPanel, z);
        }
    }


    private JComponent createPovPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(new SmoothJLabel("Pov(Degrees)"));
        panel.add(Box.createHorizontalStrut(labelHorizontalStruct));
        panel.add(Box.createGlue());
        NumberFormat format = NumberFormat.getIntegerInstance();
        format.setGroupingUsed(false);
        JTextField textField = ViewUtil.createNumberTextField(Math.toDegrees(canvas.getPov()), new ViewUtil.SetterRunnable<>(value -> canvas.setPov(Math.toRadians(value.doubleValue()))));
        textField.setColumns(20);
        panel.add(textField);
        return panel;
    }

    private JComponent createViewPortCenterPanel() {
        JPanel parent = new JPanel();
        parent.setLayout(new BoxLayout(parent, BoxLayout.X_AXIS));
        parent.add(new SmoothJLabel("ViewPortCenter"));
        parent.add(Box.createHorizontalStrut(labelHorizontalStruct));
        parent.add(Box.createGlue());

        JPanel panel = new JPanel();
        panel.setLayout(new CustomBoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(new SmoothJLabel("x"));
        panel.add(Box.createHorizontalStrut(4));
        panel.add(ViewUtil.createNumberTextField(canvas.getViewport().x+canvas.getViewport().width/2, new ViewUtil.SetterRunnable<>(value -> {
            Rectangle rectangle = canvas.getViewport();
            canvas.setViewport(new Rectangle(value.intValue()-canvas.getViewport().width/2, rectangle.y,rectangle.width,rectangle.height));
        })),true);
        panel.add(Box.createHorizontalStrut(4));
        panel.add(new SmoothJLabel("y"));
        panel.add(Box.createHorizontalStrut(4));
        panel.add(ViewUtil.createNumberTextField(canvas.getViewport().y+canvas.getViewport().height/2, new ViewUtil.SetterRunnable<>(value -> {
            Rectangle rectangle = canvas.getViewport();
            canvas.setViewport(new Rectangle(rectangle.x,value.intValue()-canvas.getViewport().height/2,rectangle.width,rectangle.height));
        })),true);parent.add(panel);
        return parent;
    }
    private JComponent createViewPortSizePanel() {
        JPanel parent = new JPanel();
        parent.setLayout(new BoxLayout(parent, BoxLayout.X_AXIS));
        parent.add(new SmoothJLabel("ViewPortSize"));
        parent.add(Box.createHorizontalStrut(labelHorizontalStruct));
        parent.add(Box.createGlue());

        JPanel panel = new JPanel();
        panel.setLayout(new CustomBoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(new SmoothJLabel("Width"));
        panel.add(Box.createHorizontalStrut(4));
        panel.add(ViewUtil.createNumberTextField(canvas.getViewport().width, new ViewUtil.SetterRunnable<>(value -> {
            Rectangle rectangle = canvas.getViewport();
            int centerX = rectangle.x+rectangle.width/2;
            int centerY = rectangle.y+rectangle.height/2;
            canvas.setViewport(new Rectangle(centerX-value.intValue()/2,centerY-rectangle.height/2,value.intValue(),rectangle.height));
        })),true);
        panel.add(Box.createHorizontalStrut(4));
        panel.add(new SmoothJLabel("Height"));
        panel.add(Box.createHorizontalStrut(4));
        panel.add(ViewUtil.createNumberTextField(canvas.getViewport().height, new ViewUtil.SetterRunnable<>(value -> {
            Rectangle rectangle = canvas.getViewport();
            int centerX = rectangle.x+rectangle.width/2;
            int centerY = rectangle.y+rectangle.height/2;
            canvas.setViewport(new Rectangle(centerX-rectangle.width/2,centerY-value.intValue()/2,rectangle.width,value.intValue()));
        })),true);parent.add(panel);
        return parent;
    }

    private JComponent createFixedYPanel() {
        JPanel parent = new JPanel();
        parent.setLayout(new BoxLayout(parent, BoxLayout.X_AXIS));
        parent.add(new SmoothJLabel("FixedY"));
        parent.add(Box.createHorizontalStrut(labelHorizontalStruct));
        parent.add(Box.createGlue());

        JCheckBox checkBox = new JCheckBox();
        checkBox.setSelected(canvas.useFixedY());
        checkBox.addActionListener(e->{
            canvas.setFixedY(checkBox.isSelected());
        });
        checkBox.setOpaque(false);
        parent.add(checkBox);
        return parent;
    }


    private JPanel createMaterialPanel() {
        return InspectorUtil.createMaterialPanel(canvas::setMaterialUUID, canvas::getMaterialUUID, uuid -> canvas.getMaterialManager().getInstance(uuid));
    }

}
