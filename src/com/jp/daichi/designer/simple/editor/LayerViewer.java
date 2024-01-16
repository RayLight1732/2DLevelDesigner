package com.jp.daichi.designer.simple.editor;

import com.jp.daichi.designer.interfaces.Canvas;
import com.jp.daichi.designer.interfaces.DesignerObject;
import com.jp.daichi.designer.interfaces.Layer;
import com.jp.daichi.designer.interfaces.ObservedObject;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LayerViewer extends JScrollPane {

    public LayerViewer(Canvas canvas, InspectorView inspectorView) {
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

        for (Layer layer:canvas.getLayers()) {
            JPanel p = new LayerPanel(layer,inspectorView);
            accordion.add(p);
            accordion.add(Box.createVerticalStrut(5));
        }
        accordion.add(Box.createVerticalGlue());

        super.setViewportView(accordion);

        super.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        super.getVerticalScrollBar().setUnitIncrement(25);
    }


    private static abstract class AAccordionPanel extends JPanel implements ObserverComponent {
        private static final String closed = "▶";
        private static final String opened = "▼";

        private final JLabel label;
        private JPanel contentPanel;
        private AAccordionPanel() {
            super(new BorderLayout());
            setOpaque(false);
            label = new JLabel();
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
                    revalidateContentPanel();
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

        private void revalidateContentPanel() {
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
    }

    private static class LayerPanel extends AAccordionPanel {
        private static final Border border = BorderFactory.createEmptyBorder(0, 10, 0, 0);
        private final Layer layer;
        private final InspectorView inspectorView;
        public LayerPanel(Layer layer,InspectorView inspectorView) {
            this.layer = layer;
            this.inspectorView = inspectorView;
            initContentPanel();
        }

        private final JPanel contentPanel = new JPanel(new GridLayout(0,1));
        @Override
        public JPanel getContentPanel() {
            contentPanel.setOpaque(false);
            for (DesignerObject designerObject:layer.getObjects()) {
                contentPanel.add(createDesignerObjectComponent(designerObject));
            }
            return contentPanel;
        }

        @Override
        public String getTitle() {
            return layer.getName();
        }

        private JComponent createDesignerObjectComponent(DesignerObject designerObject) {
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel,BoxLayout.X_AXIS));
            JLabel label = new JLabel(designerObject.getName());
            panel.setBorder(border);
            panel.setOpaque(true);
            panel.setBackground(ViewUtils.BACKGROUND_COLOR);
            panel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    panel.setBackground(ViewUtils.HIGHLIGHT_COLOR);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    panel.setBackground(ViewUtils.BACKGROUND_COLOR);
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    if (designerObject instanceof Viewable viewable) {
                        inspectorView.setView(viewable);
                    }
                }
            });
            panel.add(label);
            panel.add(Box.createHorizontalStrut(5));
            panel.add(Box.createGlue());
            JLabel label2 = new JLabel("Z:"+designerObject.getZ()+" Priority:"+designerObject.getPriority());
            panel.add(label2);

            return panel;
        }

        @Override
        public void update(ObservedObject target, UpdateAction action) {
            if (target == layer && action == UpdateAction.CHANGE_NAME) {
                updateLabelText();
            }
            if (target instanceof DesignerObject && layer.getObjects().contains(target)) {
                contentPanel.removeAll();
                for (DesignerObject designerObject : layer.getObjects()) {
                    contentPanel.add(createDesignerObjectComponent(designerObject));
                }
            }
        }
    }

}
