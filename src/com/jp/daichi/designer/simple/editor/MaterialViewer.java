package com.jp.daichi.designer.simple.editor;

import com.jp.daichi.designer.interfaces.*;
import com.jp.daichi.designer.interfaces.Canvas;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class MaterialViewer extends JScrollPane {

    private final MaterialManager materialManager;
    private final InspectorView inspectorView;
    public MaterialViewer(MaterialManager materialManager,InspectorView inspectorView) {
        this.inspectorView = inspectorView;
        setBorder(null);
        this.materialManager = materialManager;
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                requestFocus();
            }
        });
        JPanel panel = new MaterialListViewer();
        super.setViewportView(panel);

        super.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        super.getVerticalScrollBar().setUnitIncrement(25);
    }

    private class MaterialListViewer extends ObserverJPanel {

        private MaterialListViewer() {
            setLayout(new FlowLayout());
            for (Material material: materialManager.getMaterials()) {
                add(new MaterialPreviewPanel(material));
            }
        }

        @Override
        public void update(ObservedObject target, UpdateAction action) {
            if (action == UpdateAction.ADD) {
                int zOrder = 0;
                for (Material material:materialManager.getMaterials()) {
                    Component component = getComponent(material);
                    if (component == null) {
                        add(new MaterialPreviewPanel(material),zOrder);
                    } else {
                        zOrder = getComponentZOrder(component);
                    }
                }
            } else if (action == UpdateAction.REMOVE) {
                List<Material> materials = materialManager.getMaterials();
                for (Component component:getComponents()) {
                    if (component instanceof MaterialPreviewPanel materialPreviewPanel) {
                        if (!materials.contains(materialPreviewPanel.material)) {
                            remove(component);
                            break;
                        }
                    }
                }
            } else if (target instanceof Material targetMaterial) {
                for (Component component:getComponents()) {
                    if (component instanceof MaterialPreviewPanel materialPreviewPanel) {
                        if (targetMaterial == materialPreviewPanel.material) {
                            int zOrder = getComponentZOrder(component);
                            remove(component);
                            add(new MaterialPreviewPanel(targetMaterial),zOrder);
                            break;
                        }
                    }
                }
            }
        }

        private Component getComponent(Material material) {
            for (Component component:getComponents()) {
                if (component instanceof MaterialPreviewPanel materialPreviewPanel && materialPreviewPanel.material == material) {
                    return component;
                }
            }
            return null;
        }
    }

    private class MaterialPreviewPanel extends JPanel {
        private static final Border defaultBorder = BorderFactory.createMatteBorder(1,1,1,1,ViewUtils.HIGHLIGHT_COLOR);

        private final Material material;
        private static final Dimension imageSize = new Dimension(50,50);
        private static final Dimension previewSize = new Dimension(50,70);
        private MaterialPreviewPanel(Material material) {
            setBorder(defaultBorder);
            setPreferredSize(previewSize);
            setMinimumSize(previewSize);
            setMaximumSize(previewSize);
            this.material = material;
            setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
            JLabel imageLabel = new JLabel();
            imageLabel.setPreferredSize(imageSize);
            imageLabel.setMinimumSize(imageSize);
            imageLabel.setMaximumSize(imageSize);
            if (material.getImage() != null) {
                imageLabel.setIcon(new ImageIcon(material.getImage()));
            } else {
                imageLabel.setBackground(ViewUtils.MATERIAL_ERROR_COLOR);
            }
            JLabel nameLabel = new JLabel(material.getName());
            add(imageLabel);
            add(nameLabel);
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    inspectorView.setView(new MaterialInspectorView(material));
                }
            });
        }
    }

}
