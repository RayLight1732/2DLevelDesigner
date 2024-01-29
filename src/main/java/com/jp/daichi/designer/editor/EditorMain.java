package com.jp.daichi.designer.editor;

import com.jp.daichi.designer.editor.ui.LineBorderEx;
import com.jp.daichi.designer.editor.ui.WindowManager;
import com.jp.daichi.designer.editor.ui.inspector.InspectorView;
import com.jp.daichi.designer.editor.ui.inspector.SimpleInspectorManager;
import com.jp.daichi.designer.editor.manager.EditorCanvasManager;
import com.jp.daichi.designer.editor.ui.viewer.LayerViewer;
import com.jp.daichi.designer.editor.ui.viewer.MaterialViewer;
import com.jp.daichi.designer.interfaces.Canvas;
import com.jp.daichi.designer.interfaces.*;
import com.jp.daichi.designer.interfaces.editor.History;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

import static com.jp.daichi.designer.ColorProfile.*;


public class EditorMain {
    //TODO
    //やらなければならないこと
    //マテリアルで画像が選択できるようにする△
    //  画像選択ウィンドウ〇
    //  UV選択ウィンドウ
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
    //インスペクタを開かなくてもリネームできるようにする->名前部分ダブルクリック
    //満点
    //サーバーで同期する
    //来週以降？

    public static void main(String[] args) {
        JFrame frame = new JFrame("2D Level Designer");

        //UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        Border defaultBorder = BorderFactory.createCompoundBorder(new LineBorderEx(HIGHLIGHT_COLOR, 1, 10), BorderFactory.createEmptyBorder(0, 5, 0, 0));
        UIManager.put("TextField.border", defaultBorder);
        UIManager.put("FormattedTextField.border", defaultBorder);


        UIManager.put("Label.foreground", FOREGROUND_COLOR);
        UIManager.put("TextField.foreground", FOREGROUND_COLOR);
        UIManager.put("FormattedTextField.foreground", FOREGROUND_COLOR);
        UIManager.put("List.foreground", FOREGROUND_COLOR);
        UIManager.put("Button.foreground", FOREGROUND_COLOR);

        UIManager.put("Label.background", BACKGROUND_COLOR);
        UIManager.put("List.background", BACKGROUND_COLOR);
        UIManager.put("Button.background", BACKGROUND_COLOR);
        UIManager.put("Panel.background", BACKGROUND_COLOR);
        UIManager.put("FormattedTextField.background", HIGHLIGHT_COLOR);
        UIManager.put("TextField.background", HIGHLIGHT_COLOR);
        Font defaultFont = new Font("Dialog", Font.PLAIN, 11);
        UIManager.put("Button.font", defaultFont);
        UIManager.put("Label.font", defaultFont);
        UIManager.put("List.font", defaultFont);
        UIManager.put("ComboBox.font", defaultFont);
        //UIManager.put("FormattedTextField.font",defaultFont2);
        //UIManager.put("TextField.font",defaultFont2);
        SwingUtilities.updateComponentTreeUI(frame);

        EditorCanvasManager canvasManager = new EditorCanvasManager(new File("C:\\Development\\Test"));
        Canvas canvas = canvasManager.getInstance();

        for (DesignerObjectType type : DesignerObjectType.values()) {
            if (canvas.getLayerManager().getLayer(type) == null) {
                Layer layer = canvas.getLayerManager().createInstance(type.getDisplayName() + "Layer", type);
                canvas.addLayer(layer.getUUID());
            }
        }
        //Layer layer = layerManager.createInstance("ImageLayer");
        //canvas.addLayer(layer.getUUID());//TODO canvasにcreateLayerを埋め込んでもいいかも



        frame.setSize(400,300);
        frame.setLocationRelativeTo(null);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        InspectorView inspectorView = new InspectorView();
        SimpleInspectorManager simpleInspectorManager = new SimpleInspectorManager((EditorCanvas) canvas);
        WindowManager windowManager = new WindowManager(frame, inspectorView, simpleInspectorManager);

        Tool tool = new SelectAndMoveTool((EditorCanvas) canvas,windowManager);
        JPanel jPanel = new MyJPanel(canvas, tool);
        setUpKeyBindings(jPanel,(EditorCanvas) canvas);

        jPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    JComponent view = windowManager.inspectorManager().createInspectorView(canvas);
                    if (view != null) {
                        windowManager.inspectorView().setView(view);
                    }
                } else if (SwingUtilities.isLeftMouseButton(e)) {
                    jPanel.requestFocus();
                }
            }
        });


        simpleInspectorManager.setWindowManager(windowManager);
        JSplitPane right = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, inspectorView, new LayerViewer((EditorCanvas) canvas, windowManager));
        right.setUI(new CustomSplitPaneUI());
        right.setDividerSize(3);
        right.setResizeWeight(0.5);
        right.setBorder(null);
        JSplitPane left = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, jPanel, new MaterialViewer(canvas.getMaterialManager(), windowManager));
        left.setUI(new CustomSplitPaneUI());
        left.setDividerSize(3);
        left.setResizeWeight(0.5);
        left.setBorder(null);
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, left, right);
        split.setUI(new CustomSplitPaneUI());
        split.setDividerSize(3);
        split.setResizeWeight(0.5);
        split.setBorder(null);

        frame.add(split);

        canvas.getUpdateObserver().addRunnable((target, action) -> {
            sendUpdateToChild(frame.getContentPane(), target, action);
            frame.getContentPane().revalidate();
            frame.getContentPane().repaint();
        });

        //メニューバー
        JMenuBar menuBar = new JMenuBar();
        JMenu file = new JMenu("File");
        menuBar.add(file);

        JMenu edit = new JMenu("Edit");
        menuBar.add(edit);
        History history = ((EditorCanvas) canvas).getHistory();
        JMenuItem undo = new JMenuItem(getUndoText(history));
        if (!history.canUndo()) {
            undo.setEnabled(false);
        }
        undo.addActionListener(e -> history.undo(canvas));

        JMenuItem redo = new JMenuItem(getRedoText(history));
        if (!history.canRedo()) {
            redo.setEnabled(false);
        }
        redo.addActionListener(e -> history.redo(canvas));
        edit.add(undo);
        edit.add(redo);

        canvas.getUpdateObserver().addRunnable((target, action) -> {
            if (target instanceof History history1) {
                undo.setEnabled(history1.canUndo());
                undo.setText(getUndoText(history1));

                redo.setEnabled(history1.canRedo());
                redo.setText(getRedoText(history1));
            }
        });

        frame.setJMenuBar(menuBar);
        frame.setVisible(true);
        right.setDividerLocation((int) (frame.getSize().height * 0.5));
        left.setDividerLocation((int) (frame.getSize().height * 0.7));
        split.setDividerLocation((int) (frame.getSize().width * 0.7));

        frame.addMouseWheelListener(e -> {
            ((EditorCanvas)canvas).setSaveHistory(false);
            canvas.getViewport().x += e.getPreciseWheelRotation() * 10;
            canvas.setViewport(canvas.getViewport());
            ((EditorCanvas)canvas).setSaveHistory(true);
        });

        /*
        for (int i = 0;i < 100;i++) {
            canvas.getMaterialManager().createInstance("Material"+i);
        }
        */
        canvasManager.getDataSaver().saveAll(canvas);
    }

    private static String getUndoText(History history) {
        if (history.canUndo()) {
            return "Undo " + history.getUndoTarget().description();
        } else {
            return "Undo";
        }
    }

    private static String getRedoText(History history) {
        if (history.canRedo()) {
            return "Redo " + history.getRedoTarget().description();
        } else {
            return "Redo";
        }
    }

    private static void sendUpdateToChild(Container container, ObservedObject target, UpdateAction action) {
        for (Component child : container.getComponents()) {
            if (child instanceof ObserverComponent observerComponent) {
                observerComponent.update(target, action);
            }
            if (child instanceof Container container2) {
                sendUpdateToChild(container2, target, action);
            }
        }
    }

    private static void setUpKeyBindings(JPanel panel,EditorCanvas canvas) {
        panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_SHIFT, InputEvent.SHIFT_DOWN_MASK, false),"ShiftPressed");
        panel.getActionMap().put("ShiftPressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                KeyManager.onPressed(KeyEvent.VK_SHIFT);
            }
        });
        panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_SHIFT, 0, true),"ShiftReleased");
        panel.getActionMap().put("ShiftReleased", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                KeyManager.onReleased(KeyEvent.VK_SHIFT);
            }
        });

        CopyPasteHandler handler = new CopyPasteHandler(canvas);
        panel.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK, false),"Copy");
        panel.getActionMap().put("Copy", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handler.onCopied();
            }
        });
        panel.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK, false),"Paste");
        panel.getActionMap().put("Paste", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handler.onPasted();
            }
        });

    }

    private static class MyJPanel extends JPanel {

        private final Canvas canvas;
        private final Tool tool;

        public MyJPanel(Canvas canvas, Tool tool) {
            this.canvas = canvas;
            this.tool = tool;
            addMouseListener(tool.getMouseAdapter());
            addMouseMotionListener(tool.getMouseAdapter());
            addMouseWheelListener(tool.getMouseAdapter());
            setBackground(Color.GRAY);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (g instanceof Graphics2D g2d) {
                canvas.draw(g2d, getWidth(), getHeight());
                tool.draw(g2d);
            }
        }
    }

    private static class CustomSplitPaneUI extends BasicSplitPaneUI {
        @Override
        public BasicSplitPaneDivider createDefaultDivider() {
            return new BasicSplitPaneDivider(this) {
                public void setBorder(Border b) {
                }

                @Override
                public void paint(Graphics g) {
                    g.setColor(HIGHLIGHT_COLOR);
                    g.fillRect(0, 0, getSize().width, getSize().height);
                    super.paint(g);
                }
            };
        }
    }


}
