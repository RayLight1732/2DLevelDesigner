package com.jp.daichi.designer.editor.ui.inspector;

import com.jp.daichi.designer.editor.*;
import com.jp.daichi.designer.editor.ui.*;
import com.jp.daichi.designer.interfaces.Material;
import com.jp.daichi.designer.interfaces.ObservedObject;
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

    public CanvasInspectorView(EditorCanvas canvas, WindowManager windowManager) {
        this.canvas = canvas;
        this.windowManager = windowManager;
        init();
    }

    private void init() {
        materialPanel = createMaterialPanel();
        povPanel = createPovPanel();
        setBorder(BorderFactory.createEmptyBorder(10, ViewUtil.LEFT_PADDING, 4, 4));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(InspectorView.createTitlePanel("Canvas Information"));
        add(Box.createVerticalStrut(4));
        add(povPanel);
        add(Box.createVerticalStrut(4));
        add(materialPanel);
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


    private JPanel createMaterialPanel() {
        return InspectorUtil.createMaterialPanel(canvas::setMaterialUUID, canvas::getMaterialUUID, uuid -> canvas.getMaterialManager().getInstance(uuid));
    }

}
