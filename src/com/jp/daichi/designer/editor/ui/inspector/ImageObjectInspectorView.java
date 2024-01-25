package com.jp.daichi.designer.editor.ui.inspector;

import com.jp.daichi.designer.Util;
import com.jp.daichi.designer.editor.ui.SmoothJLabel;
import com.jp.daichi.designer.interfaces.ImageObject;
import com.jp.daichi.designer.interfaces.Material;
import com.jp.daichi.designer.interfaces.ObservedObject;
import com.jp.daichi.designer.interfaces.UpdateAction;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.text.NumberFormat;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.jp.daichi.designer.editor.ui.ViewUtil.labelHorizontalStruct;

/**
 * イメージオブジェクト用のインスペクタービュー
 */
public class ImageObjectInspectorView extends DesignerObjectInspectorView {


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
        if (target == getDesignerObject() || target == Util.getMaterial(getDesignerObject()) || action == UpdateAction.REMOVE) {
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


    private final Supplier<UUID> materialUUIDGetter = ()->getDesignerObject().getMaterialUUID();
    private final Function<UUID,Material> materialGetter = uuid -> Util.getMaterial(getDesignerObject());
    private JPanel createMaterialPanel() {
        return InspectorUtil.createMaterialPanel(uuid ->  getDesignerObject().setMaterialUUID(uuid),materialUUIDGetter,materialGetter);
    }

}
