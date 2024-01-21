package com.jp.daichi.designer.editor;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.basic.BasicFormattedTextFieldUI;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.text.*;

public class MyFormattedTextField extends JFormattedTextField {

    private static final Color ERROR_BACKGROUND_COLOR = new Color(153,0,51);
    private Color fBackground;

    /**
     * Create a new {@code ImprovedFormattedTextField} instance which will use {@code aFormat} for the
     * validation of the user input.
     *
     * @param aFormat The format. May not be {@code null}
     */
    public MyFormattedTextField(Format aFormat) {
        super(new ParseAllFormat(aFormat));
        setUI(new MyBasicFormattedTextFieldUI());
        setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);
        updateBackgroundOnEachUpdate();
        addFocusListener(new MousePositionCorrectorListener());
    }

    /**
     * Create a new {@code ImprovedFormattedTextField} instance which will use {@code aFormat} for the
     * validation of the user input. The field will be initialized with {@code aValue}.
     *
     * @param aFormat The format. May not be {@code null}
     * @param aValue  The initial value
     */
    public MyFormattedTextField(Format aFormat, Object aValue) {
        this(aFormat);
        setValue(aValue);
    }


    private void updateBackgroundOnEachUpdate() {
        getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateBackground();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateBackground();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateBackground();
            }
        });
    }

    /**
     * Update the background color depending on the valid state of the current input. This provides
     * visual feedback to the user
     */
    private void updateBackground() {
        setBackground(isValidContent() ? fBackground : ERROR_BACKGROUND_COLOR);
    }

    @Override
    public void updateUI() {
        super.updateUI();
        fBackground = getBackground();
    }

    private boolean isValidContent() {
        AbstractFormatter formatter = getFormatter();
        if (formatter != null) {
            try {
                formatter.stringToValue(getText());
                return true;
            } catch (ParseException e) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void setValue(Object value) {
        boolean validValue = true;
        try {
            AbstractFormatter formatter = getFormatter();
            if (formatter != null) {
                formatter.valueToString(value);
            }
        } catch (ParseException e) {
            validValue = false;
            updateBackground();
        }
        if (validValue) {
            int old_caret_position = getCaretPosition();
            super.setValue(value);
            setCaretPosition(Math.min(old_caret_position, getText().length()));
        }
    }

    @Override
    protected boolean processKeyBinding(KeyStroke ks, KeyEvent e, int condition, boolean pressed) {
        //do not let the formatted text field consume the enters. This allows to trigger an OK button by
        //pressing enter from within the formatted text field
        if (isValidContent()) {
            return super.processKeyBinding(ks, e,
                    condition, pressed) && ks != KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
        } else {
            return super.processKeyBinding(ks, e,
                    condition, pressed);
        }
    }

    private static class MousePositionCorrectorListener extends FocusAdapter {
        @Override
        public void focusGained(FocusEvent e) {
            /* After a formatted text field gains focus, it replaces its text with its
             * current value, formatted appropriately of course. It does this after
             * any focus listeners are notified. We want to make sure that the caret
             * is placed in the correct position rather than the dumb default that is
             * before the 1st character ! */
            final JTextField field = (JTextField) e.getSource();
            final int dot = field.getCaret().getDot();
            final int mark = field.getCaret().getMark();
            if (field.isEnabled() && field.isEditable()) {
                SwingUtilities.invokeLater(() -> {
                    // Only set the caret if the textfield hasn't got a selection on it
                    if (dot == mark) {
                        field.getCaret().setDot(dot);
                    }
                });
            }
        }
    }

    public static class ParseAllFormat extends Format {
        private final Format fDelegate;

        /**
         * Decorate <code>aDelegate</code> to make sure if parser everything or nothing
         *
         * @param aDelegate The delegate format
         */
        public ParseAllFormat(Format aDelegate) {
            fDelegate = aDelegate;
        }

        @Override
        public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
            return fDelegate.format(obj, toAppendTo, pos);
        }

        @Override
        public AttributedCharacterIterator formatToCharacterIterator(Object obj) {
            return fDelegate.formatToCharacterIterator(obj);
        }

        @Override
        public Object parseObject(String source, ParsePosition pos) {
            int initialIndex = pos.getIndex();
            Object result = fDelegate.parseObject(source, pos);
            if (result != null && pos.getIndex() < source.length()) {
                int errorIndex = pos.getIndex();
                pos.setIndex(initialIndex);
                pos.setErrorIndex(errorIndex);
                return null;
            }
            return result;
        }

        @Override
        public Object parseObject(String source) throws ParseException {
            //no need to delegate the call, super will call the parseObject( source, pos ) method
            return super.parseObject(source);
        }
    }

    public static class MyBasicFormattedTextFieldUI extends BasicFormattedTextFieldUI {
        @Override
        protected void paintBackground(Graphics g) {
            if (getComponent().getBorder() instanceof CompoundBorder compoundBorder && compoundBorder.getOutsideBorder() instanceof LineBorderEx border && border.isRoundingCorners()) {
                Graphics2D g2d = (Graphics2D) g;
                Color oldColor = g.getColor();
                Stroke oldStroke = g2d.getStroke();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);


                g.setColor(ViewUtils.BACKGROUND_COLOR);//TODO どこかしらが悪さをしていると思われる 要改善
                g2d.fillRect(0,0,getComponent().getWidth(),getComponent().getHeight());
                g.setColor(getComponent().getBackground());
                g2d.setStroke(border.getStroke());
                int p = border.getThickness() >> 1;
                int rw = border.getHorizontalArcRatio();
                int rh = border.getVerticalArcRatio();
                g2d.fillRoundRect(p, p, getComponent().getWidth() - border.getThickness(), getComponent().getHeight()-border.getThickness(), rw, rh);

                g.setColor(oldColor);
                g2d.setStroke(oldStroke);
            } else {
                g.setColor(getComponent().getBackground());
                g.fillRect(0, 0, getComponent().getWidth(), getComponent().getHeight());
            }
        }
    }

}