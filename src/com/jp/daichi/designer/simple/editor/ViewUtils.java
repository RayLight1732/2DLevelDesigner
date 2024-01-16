package com.jp.daichi.designer.simple.editor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.text.DecimalFormat;

public class ViewUtils {
    public static final Color BACKGROUND_COLOR = Color.GRAY;
    public static final Color HIGHLIGHT_COLOR = new Color(150,150,150);
    public static final Color MATERIAL_ERROR_COLOR = new Color(255,0,255);

    public static final int LEFT_PADDING = 5;

    public static final int TEXTFIELD_COLUMNS = 10;
    public static JTextField createNumberTextField(Number value,ValueSetter<Number> setter) {
        DecimalFormat format = new DecimalFormat();
        format.setGroupingUsed(false);
        JFormattedTextField textField = new MyFormattedTextField(format, value);
        textField.setColumns(TEXTFIELD_COLUMNS);
        textField.setMaximumSize(textField.getPreferredSize());
        addTextFieldListener(textField, setter);
        return textField;
    }
    public static  <T> void addTextFieldListener(JFormattedTextField textField,ValueSetter<T> setter) {
        textField.addActionListener(e -> setter.set((T)textField.getValue()));
        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                EventQueue.invokeLater(()-> setter.set((T)textField.getValue()));
            }
        });
    }

    public interface ValueSetter<T> {
        void set(T value);
    }

}
