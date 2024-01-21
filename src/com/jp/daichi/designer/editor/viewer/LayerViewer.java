package com.jp.daichi.designer.editor.viewer;

import com.jp.daichi.designer.interfaces.*;
import com.jp.daichi.designer.interfaces.Canvas;
import com.jp.daichi.designer.interfaces.Point;
import com.jp.daichi.designer.editor.*;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;

/**
 * レイヤーの閲覧を行うためのパネル
 */
public class LayerViewer extends JScrollPane {

    private final WindowManager windowManager;
    private final Canvas canvas;

    /**
     * レイヤーの閲覧を行うためのパネルを作成する
     * @param canvas 対象のキャンバス
     * @param windowManager ウィンドウマネージャー
     */
    public LayerViewer(Canvas canvas, WindowManager windowManager) {
        this.windowManager = windowManager;
        this.canvas = canvas;
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                requestFocus();
            }
        });
        setBorder(null);
        Box accordion = Box.createVerticalBox();
        accordion.setOpaque(true);
        accordion.setBackground(ViewUtils.BACKGROUND_COLOR);
        accordion.setBorder(BorderFactory.createEmptyBorder(10, ViewUtils.LEFT_PADDING, 5, 5));

        canvas.getLayers().stream().map(it ->canvas.getLayerManager().getInstance(it)).filter(Objects::nonNull).forEach(layer-> {
            JPanel p = new LayerPanel(canvas,layer);
            accordion.add(p);
            accordion.add(Box.createVerticalStrut(5));
        });
        accordion.add(Box.createVerticalGlue());

        super.setViewportView(accordion);

        super.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        super.getVerticalScrollBar().setUnitIncrement(25);
    }


    private static abstract class AAccordionPanel extends JPanel implements ObserverComponent {
        private static final String closed = "▶";
        private static final String opened = "▼";

        private final SmoothJLabel label;
        private JPanel contentPanel;
        private AAccordionPanel() {
            super(new BorderLayout());
            setOpaque(false);
            label = new SmoothJLabel();
            label.setOpaque(true);
            label.setBackground(ViewUtils.BACKGROUND_COLOR);
            label.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    label.setBackground(ViewUtils.HIGHLIGHT_COLOR);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    label.setBackground(ViewUtils.BACKGROUND_COLOR);
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        revalidateContentPanel();
                    }
                }

                @Override
                public void mouseClicked(MouseEvent e) {
                    requestFocus();
                }
            });
            add(label, BorderLayout.NORTH);
        }

        protected void initContentPanel() {
            label.setText(closed +" " + getTitle());

            contentPanel = getContentPanel();
            //Border outBorder = BorderFactory.createMatteBorder(0, 2, 2, 2, Color.WHITE);
            //Border inBorder = BorderFactory.createEmptyBorder(10, 10, 10, 10);
            //Border border = BorderFactory.createCompoundBorder(outBorder, inBorder);
            //contentPanel.setBorder(border);
            contentPanel.setVisible(false);
            add(contentPanel);
        }


        protected void revalidateContentPanel() {
            contentPanel.setVisible(!contentPanel.isVisible());
            updateLabelText();
            revalidate();
        }

        protected void updateLabelText() {
            label.setText(String.format("%s %s", contentPanel.isVisible() ? opened : closed, getTitle()));
        }

        @Override
        public Dimension getPreferredSize() {
            Dimension d = label.getPreferredSize();
            if (contentPanel.isVisible()) {
                d.height += contentPanel.getPreferredSize().height;
            }
            return d;
        }

        @Override
        public Dimension getMaximumSize() {
            Dimension d = getPreferredSize();
            return new Dimension(Integer.MAX_VALUE,d.height);
        }

        public abstract JPanel getContentPanel();

        public abstract String getTitle();

        protected SmoothJLabel getAccordionLabel() {
            return label;
        }
    }

    private class LayerPanel extends AAccordionPanel {
        private final Canvas canvas;
        private final Layer layer;
        private final JPopupMenu  popup;

        private final JPanel contentPanel = new JPanel(new GridLayout(0,1));
        public LayerPanel(Canvas canvas,Layer layer) {
            this.canvas = canvas;
            this.layer = layer;
            this.popup = new JPopupMenu();
            initPopupMenu();
            initContentPanel();
            getAccordionLabel().addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (SwingUtilities.isRightMouseButton(e)) {
                        popup.show(e.getComponent(),e.getX(),e.getY());
                    }
                }
            });
        }

        private void initPopupMenu() {
            JMenuItem item = new JMenuItem("Create");
            item.addActionListener(e -> {
                JPanel panel = new NewDesignerObjectPanel(layer,contentPanel);
                contentPanel.add(panel);
                if (!contentPanel.isVisible()) {
                    revalidateContentPanel();
                }
                contentPanel.revalidate();
                panel.requestFocus();
            });
            popup.add(item);
        }

        @Override
        public JPanel getContentPanel() {
            contentPanel.setOpaque(false);
            addContentsToContentPanel();
            return contentPanel;
        }

        private void addContentsToContentPanel() {
            layer.getObjects().stream().map(it->canvas.getDesignerObjectManager().getInstance(it)).filter(Objects::nonNull)
                    .sorted()
                    .forEach(designerObject -> contentPanel.add(new DesignerObjectPanel(designerObject,layer)));
        }

        @Override
        public String getTitle() {
            return layer.getName();
        }

        @Override
        public void update(ObservedObject target, UpdateAction action) {
            if (target == layer && action == UpdateAction.CHANGE_NAME) {
                updateLabelText();
            } else if (target instanceof DesignerObject designerObject && layer.getObjects().contains(designerObject.getUUID())) {
                if (action==UpdateAction.CHANGE_NAME || action == UpdateAction.CHANGE_PRIORITY || action == UpdateAction.CHANGE_Z) {
                    int index = layer.getObjects().stream().map(designerObjectUUID -> canvas.getDesignerObjectManager().getInstance(designerObjectUUID)).filter(Objects::nonNull).sorted().toList().indexOf(designerObject);
                    for (Component component : contentPanel.getComponents()) {
                        if (component instanceof DesignerObjectPanel designerObjectPanel && designerObjectPanel.designerObject == designerObject) {
                            int z = contentPanel.getComponentZOrder(component);
                            contentPanel.remove(z);
                            contentPanel.add(new DesignerObjectPanel(designerObject,layer), index);
                            break;
                        }
                    }
                }
            } else if (target == layer && (action == UpdateAction.ADD||action == UpdateAction.REMOVE)) {
                contentPanel.removeAll();
                addContentsToContentPanel();
            }
        }

        private Component getComponent(DesignerObject designerObject) {
            for (Component component:contentPanel.getComponents()) {
                if (component instanceof DesignerObjectPanel designerObjectPanel && designerObjectPanel.designerObject == designerObject) {
                    return component;
                }
            }
            return null;
        }
    }

    private class DesignerObjectPanel extends JPanel {
        private static final Border border = BorderFactory.createEmptyBorder(0, 10, 0, 0);


        private final DesignerObject designerObject;
        private final JPopupMenu  popup;
        private final Layer layer;

        private DesignerObjectPanel(DesignerObject designerObject,Layer layer) {
            this.designerObject = designerObject;
            this.popup = new JPopupMenu();
            this.layer = layer;
            initPopupMenu();
            setLayout(new BoxLayout(this,BoxLayout.X_AXIS));
            SmoothJLabel label = new SmoothJLabel(designerObject.getName());
            setBorder(border);
            setOpaque(true);
            setBackground(ViewUtils.BACKGROUND_COLOR);
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    setBackground(ViewUtils.HIGHLIGHT_COLOR);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    setBackground(ViewUtils.BACKGROUND_COLOR);
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        requestFocus();
                        JComponent inspectorView = windowManager.inspectorManager().createInspectorView(designerObject);
                        if (inspectorView != null) {
                            windowManager.inspectorView().setView(inspectorView);
                        }
                        canvas.getFrame().clearSelectedObject();
                        canvas.getFrame().addSelectedObject(designerObject);
                    } else if (SwingUtilities.isRightMouseButton(e)) {
                        popup.show(e.getComponent(),e.getX(),e.getY());
                    }
                }
            });
            add(label);
            add(Box.createHorizontalStrut(5));
            add(Box.createGlue());
            SmoothJLabel label2 = new SmoothJLabel("Z:"+designerObject.getZ()+" Priority:"+designerObject.getPriority());
            add(label2);
        }


        private void initPopupMenu() {
            JMenuItem item = new JMenuItem("Delete");
            item.addActionListener(e -> {
                if (windowManager.inspectorManager().isShowed(designerObject)) {
                    windowManager.inspectorView().setView(null);
                }
                canvas.getDesignerObjectManager().removeInstance(designerObject);
                layer.remove(designerObject.getUUID());
            });
            popup.add(item);
        }

    }

    /**
     * 新しいデザイナーオブジェクトを作成する用のパネル
     */
    private class NewDesignerObjectPanel extends JPanel {
        private final Layer layer;
        private final JPanel contentPanel;
        private final JTextField textField;
        private boolean added = false;
        private NewDesignerObjectPanel(Layer layer,JPanel contentPanel) {
            this.layer = layer;
            this.contentPanel = contentPanel;
            this.textField = new JTextField(20);
            init();
        }

        private void init() {
            setLayout(new BoxLayout(this,BoxLayout.X_AXIS));
            setOpaque(true);
            setBackground(ViewUtils.BACKGROUND_COLOR);
            add(textField);
            textField.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    onEndEnteringName();
                }
            });
            textField.addActionListener(e->{
                onEndEnteringName();
            });
        }

        private void onEndEnteringName() {
            if (!added) {
                added = true;
                contentPanel.remove(this);
                DesignerObject designerObject = canvas.getDesignerObjectManager().createInstance(textField.getText(), layer.getObjectType());
                UpdateObserver updateObserver = designerObject.getUpdateObserver();
                int size = 100;
                double centerX = canvas.getViewport().getCenterX();
                double centerY = canvas.getViewport().getCenterY();
                designerObject.setPosition(canvas.convertFromScreenPosition(new Point(centerX-size/2.0,centerY-size/2.0),designerObject.getZ(),true));
                designerObject.setDimension(new SignedDimension(size,size));
                designerObject.setUpdateObserver(updateObserver);
                layer.add(designerObject.getUUID());
            }
        }

        @Override
        public void requestFocus() {
            textField.requestFocus();
        }
    }
}
