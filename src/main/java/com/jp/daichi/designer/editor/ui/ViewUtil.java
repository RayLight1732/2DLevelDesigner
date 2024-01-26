package com.jp.daichi.designer.editor.ui;

import com.jp.daichi.designer.editor.EditorMain;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class ViewUtil {

    public static final int labelHorizontalStruct = 6;

    public static final int LEFT_PADDING = 5;

    public static final int TEXT_FIELD_COLUMNS = 10;

    public static final Font HIGHLIGHT_FONT = new Font("Dialog", Font.PLAIN, 13);

    public static JTextField createNumberTextField(Number value, SetterRunnable<Number> setter) {
        return createNumberTextField(value, setter, false);
    }

    public static JTextField createNumberTextField(Number value, SetterRunnable<Number> setter, boolean isInteger) {
        NumberFormat format = isInteger ? NumberFormat.getIntegerInstance() : new DecimalFormat();
        format.setGroupingUsed(false);
        JFormattedTextField textField = new MyFormattedTextField(format, value);
        textField.setColumns(TEXT_FIELD_COLUMNS);
        textField.setMaximumSize(textField.getPreferredSize());
        addFormattedTextFieldListener(textField, setter);
        return textField;
    }

    public static <T> void addFormattedTextFieldListener(JFormattedTextField textField, SetterRunnable<T> setter) {
        textField.addActionListener(e -> {
            if (!setter.set) {
                setter.set = true;
                setter.set((T) textField.getValue());
            }
        });
        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if (!setter.set) {
                    setter.set = true;
                    EventQueue.invokeLater(() -> setter.set((T) textField.getValue()));
                }
            }
        });
    }

    public static SetterRunnable<String> addTextFieldListener(JTextField textField, ValueSetter<String> setter) {
        SetterRunnable<String> setterRunnable = new SetterRunnable<>(setter);
        ;
        textField.addActionListener(e -> {
            if (!setterRunnable.set) {
                setterRunnable.set = true;
                setterRunnable.set(textField.getText());
            }
        });
        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if (!setterRunnable.set) {
                    setterRunnable.set = true;
                    EventQueue.invokeLater(() -> setterRunnable.set(textField.getText()));
                }
            }
        });
        return setterRunnable;
    }

    public static Cursor NO_DRAG = createNoDragCursor();

    private static Cursor createNoDragCursor() {
        try (InputStream is = ViewUtil.class.getResourceAsStream("/NoDrag.png")) {
            if (is != null) {
                BufferedImage bf = ImageIO.read(is);
                return Toolkit.getDefaultToolkit().createCustomCursor(bf, new Point(0, 0), "NoDrag");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public interface ValueSetter<T> {
        void set(T value);

    }

    /**
     * 値を設定するために用いられるクラス
     * 一度値が設定されると、Resetが呼ばれるまで設定されない
     *
     * @param <T> 与えられる型
     */
    public static class SetterRunnable<T> {
        private final ValueSetter<T> setter;
        private boolean set = false;

        /**
         * 新しいインスタンスを作成する
         *
         * @param setter セッター
         */
        public SetterRunnable(ValueSetter<T> setter) {
            this.setter = setter;
        }

        /**
         * 値を設定する
         *
         * @param value 値
         */
        public void set(T value) {
            setter.set(value);
        }

        /**
         * 設定状態をリセットする
         */
        public void reset() {
            set = false;
        }
    }


}
