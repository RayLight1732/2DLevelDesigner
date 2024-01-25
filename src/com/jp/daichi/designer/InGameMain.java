package com.jp.daichi.designer;

import com.jp.daichi.designer.ingame.manager.InGameCanvasManager;
import com.jp.daichi.designer.interfaces.Canvas;
import com.jp.daichi.designer.interfaces.manager.CanvasManager;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class InGameMain {
    public static void main(String[] args) {
        CanvasManager canvasManager = new InGameCanvasManager(new File("C:\\Development\\Test"));
        Canvas canvas = canvasManager.getInstance();
        JFrame frame = new JFrame("TestGameWindow");
        frame.setSize(300,300);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        MainPanel mainPanel = new MainPanel(canvas);
        frame.add(mainPanel);
        frame.setVisible(true);
        System.out.println(canvas.getDesignerObjectManager().getAllInstances().size());
    }

    private static class MainPanel extends JPanel {
        private final Canvas canvas;
        private MainPanel(Canvas canvas) {
            this.canvas = canvas;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            canvas.draw((Graphics2D) g,getWidth(),getHeight());
        }
    }
}
