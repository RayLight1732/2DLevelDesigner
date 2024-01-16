package com.jp.daichi.designer;

import com.jp.daichi.designer.interfaces.*;
import com.jp.daichi.designer.interfaces.Canvas;
import com.jp.daichi.designer.interfaces.Point;
import com.jp.daichi.designer.simple.SelectAndMoveTool;
import com.jp.daichi.designer.simple.SimpleLayer;
import com.jp.daichi.designer.simple.SimpleMaterial;
import com.jp.daichi.designer.simple.SimpleMaterialManager;
import com.jp.daichi.designer.simple.editor.*;
import com.jp.daichi.designer.test.MockCanvas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.UUID;


public class Main {
    public static void main(String[] args) {
        UpdateObserver observer = new UpdateObserver();
        MockCanvas mockCanvas = new MockCanvas(new SimpleMaterialManager());
        Layer layer = new SimpleLayer("test");
        mockCanvas.addLayer(layer);


        mockCanvas.getMaterialManager().registerMaterial(new SimpleMaterial("Test0000000000000000000", UUID.randomUUID()));

        Tool tool = new SelectAndMoveTool(mockCanvas);

        JFrame frame = new JFrame("2D Level Designer");
        frame.setSize(500,500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel jPanel = new MyJPanel(mockCanvas,tool);
        //frame.add(jPanel);

        mockCanvas.setUpdateObserver(observer);
        tool.setUpdateObserver(observer);
        DesignerObject designerObject = new EditorMockDesignerObject("Object1",mockCanvas,new Point(250,100),new SignedDimension(100,100));
        layer.add(designerObject);
        designerObject.setZ(1000);
        layer.add(new EditorMockDesignerObject("Object2",mockCanvas,new Point(250,200),new SignedDimension(50,50)));

        InspectorView inspectorView = new InspectorView();
        JSplitPane right = new JSplitPane(JSplitPane.VERTICAL_SPLIT,true,inspectorView,new LayerViewer(mockCanvas,inspectorView));
        right.setDividerSize(2);
        right.setResizeWeight(0.5);
        right.setDividerLocation(200);
        right.setBorder(null);
        JSplitPane left = new JSplitPane(JSplitPane.VERTICAL_SPLIT,true,jPanel,new MaterialViewer(mockCanvas.getMaterialManager(),inspectorView));
        left.setDividerSize(2);
        left.setResizeWeight(0.5);
        left.setDividerLocation(200);
        left.setBorder(null);
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,true, left,right);
        split.setDividerSize(2);
        split.setResizeWeight(0.5);
        split.setBorder(null);
        split.setDividerLocation(200);

        frame.add(split);

        observer.addRunnable((target, action) -> {
            sendUpdateToChild(frame.getContentPane(),target,action);
            frame.getContentPane().revalidate();
            frame.getContentPane().repaint();
        });

        frame.setVisible(true);

        mockCanvas.setViewPort(new Rectangle(0,0,300,300));

        frame.addMouseWheelListener(e -> {
            mockCanvas.getViewPort().x += e.getPreciseWheelRotation()*10;
            observer.update(null,null);
        });
    }

    private static void sendUpdateToChild(Container container, ObservedObject target, UpdateAction action) {
        for (Component child: container.getComponents()) {
            if (child instanceof ObserverComponent observerComponent) {
                observerComponent.update(target,action);
            }
            if (child instanceof Container container2) {
                sendUpdateToChild(container2,target,action);
            }
        }
    }
    private static class MyJPanel extends JPanel {

        private final Canvas canvas;
        private final Tool tool;
        public MyJPanel(Canvas canvas,Tool tool) {
            this.canvas = canvas;
            this.tool = tool;
            addMouseListener(tool.getMouseAdapter());
            addMouseMotionListener(tool.getMouseAdapter());
            addMouseWheelListener(tool.getMouseAdapter());
            addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    canvas.updateTransform(getWidth(),getHeight());
                }
            });
            setBackground(Color.GRAY);
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (g instanceof Graphics2D g2d) {
                canvas.draw(g2d,getWidth(),getHeight());
                tool.draw(g2d);
            }
        }
    }

    private static class Glass extends JComponent {

    }

}
