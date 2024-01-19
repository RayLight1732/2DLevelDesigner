package com.jp.daichi.designer;

import com.jp.daichi.designer.interfaces.*;
import com.jp.daichi.designer.interfaces.Canvas;
import com.jp.daichi.designer.interfaces.Point;
import com.jp.daichi.designer.simple.SelectAndMoveTool;
import com.jp.daichi.designer.simple.SimpleImageObject;
import com.jp.daichi.designer.simple.SimpleLayer;
import com.jp.daichi.designer.simple.editor.*;
import com.jp.daichi.designer.simple.editor.inspector.InspectorView;
import com.jp.daichi.designer.simple.editor.inspector.SimpleInspectorManager;
import com.jp.daichi.designer.simple.editor.manager.EditorMaterialManager;
import com.jp.daichi.designer.test.MockCanvas;
import com.jp.daichi.designer.test.MockDesignerObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;


public class Main {
    //TODO
    //やらなければならないこと
    //オブジェクト新規作成ウィンドウ
    //  名前だけで、あとはインスペクタからやる
    //マテリアルで画像が選択できるようにする△
    //  画像選択ウィンドウ〇
    //  UV選択ウィンドウ
    //履歴を保存できるようにする
    //全体の背景を設定できるようにする
    //  マテリアルの画像選択UIを使いまわせる?
    //新規作成画面を作る
    //  Viewport,Pov,横幅,縦幅,背景画像
    //  ツールバーからもいじれるように
    //横、縦にスクロールできるようにする
    //ゲームに組み込む用の実装
    //  gradle?
    //  出力
    //
    //できるといいこと
    //マテリアルをドロップダウンで選択できるようにする
    //縦横比をUVとそろえる
    //ドラッグアンドドロップでマテリアル適用
    //マテリアル一覧で余白の均等分割
    //マテリアルの検索ウィンドウ
    //インスペクタを開かなくてもリネームできるようにする
    //ヒエラルキーを選択したら画面上でも選択されるようにする
    //満点
    //サーバーで同期する
    //来週以降？

    public static void main(String[] args) throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        UpdateObserver observer = new UpdateObserver();
        MockCanvas mockCanvas = new MockCanvas(new EditorMaterialManager());
        Layer layer = new SimpleLayer("Image");
        mockCanvas.addLayer(layer);
        mockCanvas.getMaterialManager().setUpdateObserver(observer);


        mockCanvas.getMaterialManager().addMaterial("Test0000000000000000000");

        Tool tool = new SelectAndMoveTool(mockCanvas);

        JFrame frame = new JFrame("2D Level Designer");
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        SwingUtilities.updateComponentTreeUI(frame);

        frame.setSize(500,500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel jPanel = new MyJPanel(mockCanvas,tool);
        //frame.add(jPanel);

        mockCanvas.setUpdateObserver(observer);
        tool.setUpdateObserver(observer);
        DesignerObject designerObject =new MockDesignerObject("Object1",mockCanvas,new Point(250,100),new SignedDimension(100,100));
        ImageObject imageObject = new SimpleImageObject("ImageObject",mockCanvas,new Point(0,0),new SignedDimension(100,100));
        layer.add(designerObject);
        layer.add(imageObject);
        designerObject.setZ(1000);
        layer.add(new MockDesignerObject("Object2",mockCanvas,new Point(250,200),new SignedDimension(50,50)));

        InspectorView inspectorView = new InspectorView();
        SimpleInspectorManager simpleInspectorManager = new SimpleInspectorManager();
        WindowManager windowManager = new WindowManager(frame,inspectorView,simpleInspectorManager);
        simpleInspectorManager.setWindowManager(windowManager);
        JSplitPane right = new JSplitPane(JSplitPane.VERTICAL_SPLIT,true,inspectorView,new LayerViewer(mockCanvas,windowManager));
        right.setDividerSize(2);
        right.setResizeWeight(0.5);
        right.setDividerLocation(200);
        right.setBorder(null);
        JSplitPane left = new JSplitPane(JSplitPane.VERTICAL_SPLIT,true,jPanel,new MaterialViewer(mockCanvas.getMaterialManager(),windowManager));
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

        for (int i = 0;i < 100;i++) {
            mockCanvas.getMaterialManager().addMaterial("material"+i);
        }
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
