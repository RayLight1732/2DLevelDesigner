package com.jp.daichi.designer.editor;

import javax.swing.*;
import java.awt.*;

public class SmoothJFormattedTextField extends JFormattedTextField {
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        super.paintComponent(g2d);
    }
}
