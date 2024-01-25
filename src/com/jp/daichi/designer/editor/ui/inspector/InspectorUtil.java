package com.jp.daichi.designer.editor.ui.inspector;

import com.jp.daichi.designer.Util;
import com.jp.daichi.designer.editor.ui.SmoothJLabel;
import com.jp.daichi.designer.editor.ui.ViewUtil;
import com.jp.daichi.designer.interfaces.Material;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.text.NumberFormat;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.jp.daichi.designer.editor.ui.ViewUtil.labelHorizontalStruct;

/**
 * インスペクター用のユーティリティクラス
 */
public class InspectorUtil {

    public static final String materialPanelClientPropertyKey = "MaterialPanel";
    public static final Border labelBorder = BorderFactory.createMatteBorder(1,1,1,1, ViewUtil.HIGHLIGHT_COLOR);

    public static JPanel createMaterialPanel(Consumer<UUID> materialUUIDSetter, Supplier<UUID> materialUUIDGetter, Function<UUID,Material> materialGetter) {
        JPanel panel = new JPanel();
        panel.putClientProperty(materialPanelClientPropertyKey,materialUUIDSetter);
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(new SmoothJLabel("Material"));
        panel.add(Box.createHorizontalStrut(labelHorizontalStruct));
        panel.add(Box.createGlue());
        NumberFormat format = NumberFormat.getIntegerInstance();
        format.setGroupingUsed(false);
        SmoothJLabel label = new SmoothJLabel();
        label.setBorder(labelBorder);
        UUID materialUUID = materialUUIDGetter.get();
        if (materialUUID== null) {
            label.setText("None");
        } else {
            Material material = materialGetter.apply(materialUUID);
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
