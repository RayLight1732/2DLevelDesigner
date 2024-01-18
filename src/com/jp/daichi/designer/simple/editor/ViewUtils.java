package com.jp.daichi.designer.simple.editor;

import com.jp.daichi.designer.Main;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;

public class ViewUtils {
    public static final Color BACKGROUND_COLOR = Color.GRAY;
    public static final Color HIGHLIGHT_COLOR = new Color(150,150,150);
    public static final Color MATERIAL_ERROR_COLOR = new Color(255,0,255);

    public static final Color BORDER_COLOR = Color.BLACK;

    public static final Cursor NO_DRAG = createNoDragCursor();

    public static final int LEFT_PADDING = 5;

    public static final int TEXTFIELD_COLUMNS = 10;
    public static JTextField createNumberTextField(Number value,ValueSetter<Number> setter) {
        DecimalFormat format = new DecimalFormat();
        format.setGroupingUsed(false);
        JFormattedTextField textField = new MyFormattedTextField(format, value);
        textField.setColumns(TEXTFIELD_COLUMNS);
        textField.setMaximumSize(textField.getPreferredSize());
        addFormattedTextFieldListener(textField, setter);
        return textField;
    }
    public static  <T> void addFormattedTextFieldListener(JFormattedTextField textField, ValueSetter<T> setter) {
        textField.addActionListener(e -> setter.set((T)textField.getValue()));
        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                EventQueue.invokeLater(()-> setter.set((T)textField.getValue()));
            }
        });
    }
    public static void addTextFieldListener(JTextField textField,ValueSetter<String> setter) {
        textField.addActionListener(e->setter.set(textField.getText()));
        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                EventQueue.invokeLater(()->setter.set(textField.getText()));
            }
        });
    }

    private static Cursor createNoDragCursor() {
        try (InputStream is = Main.class.getResourceAsStream("\\NoDrag.png")){
             //TODO resourcesを使用するようにする
            if (is != null) {
                BufferedImage bf = ImageIO.read(is);
                return Toolkit.getDefaultToolkit().createCustomCursor(bf,new Point(0,0),"NoDrag");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public interface ValueSetter<T> {
        void set(T value);
    }

}
