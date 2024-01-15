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
        JPanel panel = new MyJPanel() {
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

    private static final int columns = 10;

    private JPanel createPositionPanel() {
        JPanel parent = new JPanel();
        parent.setLayout(new BoxLayout(parent, BoxLayout.X_AXIS));
        parent.add(new JLabel("Position"));
        parent.add(Box.createGlue());

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS) {
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
        });
        panel.add(new JLabel("X"));
        panel.add(Box.createHorizontalStrut(4));
        panel.add(initCoordinateTextField(getPosition().x(), value -> setPosition(new Point(value, getPosition().y()))));
        panel.add(Box.createHorizontalStrut(4));
        panel.add(new JLabel("Y"));
        panel.add(Box.createHorizontalStrut(4));
        panel.add(initCoordinateTextField(getPosition().y(), value -> setPosition(new Point(getPosition().x(), value))));
        panel.add(Box.createHorizontalStrut(4));
        panel.add(new JLabel("Z"));
        panel.add(Box.createHorizontalStrut(4));
        panel.add(initCoordinateTextField(getZ(), this::setZ));

        parent.add(panel);
        return parent;
    }

    private JTextField initCoordinateTextField(double value, ValueSetter<Double> setter) {
        DecimalFormat format = new DecimalFormat();
        format.setGroupingUsed(false);
        JFormattedTextField textField = new MyFormattedTextField(format, value);
        textField.setColumns(columns);
        textField.setMaximumSize(textField.getPreferredSize());
        init(textField,(ValueSetter<Number>)newValue->setter.set(newValue.doubleValue()));
        return textField;
    }

    private JPanel createPriorityPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(new JLabel("Priority"));
        panel.add(Box.createGlue());
        NumberFormat format = NumberFormat.getIntegerInstance();
        format.setGroupingUsed(false);
        JFormattedTextField textField = new MyFormattedTextField(format, getPriority());
        textField.setColumns(20);
        textField.setMaximumSize(textField.getPreferredSize());
        init(textField,(ValueSetter<Number>) value-> setPriority(value.intValue()));
        panel.add(textField);
        return panel;
    }

    private <T> void init(JFormattedTextField textField,ValueSetter<T> setter) {
        textField.addActionListener(e -> setter.set((T)textField.getValue()));
        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                EventQueue.invokeLater(()-> setter.set((T)textField.getValue()));
            }
        });
    }

    private interface ValueSetter<T> {
        void set(T value);
    }

    private static abstract class MyJPanel extends JPanel implements ObserverComponent {

    }



}
