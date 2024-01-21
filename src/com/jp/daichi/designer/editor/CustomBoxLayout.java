package com.jp.daichi.designer.editor;

import javax.swing.*;
import java.awt.*;

/**
 * 常に右側に張り付くように調整されたBoxLayoutする
 */
public class CustomBoxLayout extends BoxLayout {
    /**
     * Creates a layout manager that will lay out components along the
     * given axis.
     *
     * @param target the container that needs to be laid out
     * @param axis   the axis to lay out components along. Can be one of:
     *               {@code BoxLayout.X_AXIS, BoxLayout.Y_AXIS,
     *               BoxLayout.LINE_AXIS} or {@code BoxLayout.PAGE_AXIS}
     * @throws AWTError if the value of {@code axis} is invalid
     */
    public CustomBoxLayout(Container target, int axis) {
        super(target, axis);
    }

    @Override
    public void layoutContainer(Container target) {
        super.layoutContainer(target);
        if (target.getComponents().length >= 1) {
            Component component = target.getComponents()[target.getComponents().length-1];
            Insets insets = target.getInsets();
            if (getAxis() == BoxLayout.X_AXIS) {
                if (component.getLocationOnScreen().x+component.getSize().width != target.getLocationOnScreen().x+target.getSize().width-insets.right) {
                    component.setSize(new Dimension(target.getLocationOnScreen().x+target.getSize().width-insets.right-component.getLocationOnScreen().x,component.getSize().height));
                }
            } else if (getAxis() == BoxLayout.Y_AXIS) {
                if (component.getLocationOnScreen().y+component.getSize().height != target.getLocationOnScreen().y+target.getSize().height-insets.bottom) {
                    component.setSize(new Dimension(component.getSize().width,target.getLocationOnScreen().y+target.getSize().height-insets.bottom-component.getLocationOnScreen().y));
                }
            }
        }
    }
}
