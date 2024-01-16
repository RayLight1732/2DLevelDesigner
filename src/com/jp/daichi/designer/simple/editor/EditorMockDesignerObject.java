package com.jp.daichi.designer.simple.editor;

import com.jp.daichi.designer.interfaces.Canvas;
import com.jp.daichi.designer.interfaces.ObservedObject;
import com.jp.daichi.designer.interfaces.Point;
import com.jp.daichi.designer.interfaces.SignedDimension;
import com.jp.daichi.designer.test.MockDesignerObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;

/**
 * エディター上のデザイナーオブジェクトのモック
 */
public class EditorMockDesignerObject extends MockDesignerObject implements Viewable {
    /**
     * デザイナーオブジェクトのインスタンスを作成する
     *
     * @param name      このオブジェクトの名前
     * @param canvas    キャンバス
     * @param center    中心座標
     * @param dimension 表示領域
     */
    public EditorMockDesignerObject(String name, Canvas canvas, Point center, SignedDimension dimension) {
        super(name, canvas, center, dimension);
    }


    @Override
    public JComponent getView() {
        JPanel positionPanel = createPositionPanel();
        JPanel priorityPanel = createPriorityPanel();
        JPanel panel = new ObserverJPanel() {
            private JPanel positionPanel_ = positionPanel;
            private JPanel priorityPanel_ = priorityPanel;
            @Override
            public void update(ObservedObject target, UpdateAction action) {
                if (target == EditorMockDesignerObject.this) {
                    int index1 = getComponentZOrder(positionPanel_);
                    int index2 = getComponentZOrder(priorityPanel_);
                    remove(positionPanel_);
                    remove(priorityPanel_);
                    positionPanel_ = createPositionPanel();
                    priorityPanel_ = createPriorityPanel();
                    add(positionPanel_, index1);
                    add(priorityPanel_, index2);
                }
            }
        };
        panel.setBorder(BorderFactory.createEmptyBorder(10, ViewUtils.LEFT_PADDING, 4, 4));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JTextField textField = new JTextField(getName());
        panel.add(textField);
        panel.add(Box.createVerticalStrut(4));
        panel.add(positionPanel);
        panel.add(Box.createVerticalStrut(4));
        panel.add(priorityPanel);
        return panel;
    }


    private JPanel createPositionPanel() {
        JPanel parent = new JPanel();
        parent.setLayout(new BoxLayout(parent, BoxLayout.X_AXIS));
        parent.add(new JLabel("Position"));
        parent.add(Box.createHorizontalStrut(4));
        parent.add(Box.createGlue());

        JPanel panel = new JPanel();
        panel.setLayout(new CustomBoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(new JLabel("X"));
        panel.add(Box.createHorizontalStrut(4));
        panel.add(ViewUtils.createNumberTextField(getPosition().x(), value -> setPosition(new Point(value.doubleValue(), getPosition().y()))));
        panel.add(Box.createHorizontalStrut(4));
        panel.add(new JLabel("Y"));
        panel.add(Box.createHorizontalStrut(4));
        panel.add(ViewUtils.createNumberTextField(getPosition().y(), value -> setPosition(new Point(getPosition().x(), value.doubleValue()))));
        panel.add(Box.createHorizontalStrut(4));
        panel.add(new JLabel("Z"));
        panel.add(Box.createHorizontalStrut(4));
        panel.add(ViewUtils.createNumberTextField(getZ(), value -> setZ(value.doubleValue())));

        parent.add(panel);
        return parent;
    }


    private JPanel createPriorityPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(new JLabel("Priority"));
        panel.add(Box.createGlue());
        NumberFormat format = NumberFormat.getIntegerInstance();
        format.setGroupingUsed(false);
        JTextField textField = ViewUtils.createNumberTextField(getPriority(),value -> setPriority(value.intValue()));
        textField.setColumns(20);
        panel.add(textField);
        return panel;
    }





}
