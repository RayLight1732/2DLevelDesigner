package com.jp.daichi.designer.simple.editor;

import com.jp.daichi.designer.interfaces.*;
import com.jp.daichi.designer.simple.editor.inspector.ImageObjectInspectorView;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.Point;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.stream.Collectors;

public class MaterialViewer extends JScrollPane {

    private final MaterialManager materialManager;
    private final MaterialListViewer materialListViewer;
    private final WindowManager windowManager;
    public MaterialViewer(MaterialManager materialManager,WindowManager windowManager) {
        this.windowManager = windowManager;
        setBorder(null);
        this.materialManager = materialManager;
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                requestFocus();
            }
        });
        materialListViewer = new MaterialListViewer();
        super.setViewportView(materialListViewer);

        super.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        super.getVerticalScrollBar().setUnitIncrement(25);
    }

    private class MaterialListViewer extends ObserverJPanel {
        private final JPopupMenu popup = new JPopupMenu();
        private int lastClickX;
        private int lastClickY;
        private MaterialListViewer() {
            setLayout(new WrapLayout(FlowLayout.LEFT,5,5));
            for (Material material: materialManager.getMaterials()) {
                add(new MaterialPreviewPanel(material));
            }
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    requestFocus();
                    if (SwingUtilities.isRightMouseButton(e)) {
                        lastClickX = e.getX();
                        lastClickY = e.getY();
                        popup.show(e.getComponent(),e.getX(),e.getY());
                    }
                }
            });
            initPopupMenu();
        }

        private void initPopupMenu() {
            JMenuItem item = new JMenuItem("Create");
            item.addActionListener(e -> {
                int index = 0;
                double minDistance = Double.MAX_VALUE;
                for (Component component:getComponents()) {
                    if (component instanceof MaterialPreviewPanel) {
                        double distance = component.getLocation().distance(lastClickX,lastClickY);
                        if (distance < minDistance) {
                            index = getComponentZOrder(component);
                            minDistance = distance;
                        }
                    }
                }
                DummyMaterialPreviewPanel dummyMaterialPreviewPanel = new DummyMaterialPreviewPanel();
                add(dummyMaterialPreviewPanel,index);
                dummyMaterialPreviewPanel.requestFocus();
                revalidate();
            });
            popup.add(item);
        }

        @Override
        public void update(ObservedObject target, UpdateAction action) {
            if (target instanceof MaterialManager) {
                if (action == UpdateAction.ADD_MATERIAL) {
                    List<Material> sorted = materialManager.getMaterials().stream().sorted().toList();
                    for (int i = 0;i < sorted.size();i++) {
                        Material material = sorted.get(i);
                        Component component = getComponent(material);
                        if (component == null) {
                            add(new MaterialPreviewPanel(material), i);
                        } else {
                            setComponentZOrder(component,i);
                        }
                    }
                } else if (action == UpdateAction.REMOVE_MATERIAL) {
                    List<Material> materials = materialManager.getMaterials();
                    for (Component component : getComponents()) {
                        if (component instanceof MaterialPreviewPanel materialPreviewPanel) {
                            if (!materials.contains(materialPreviewPanel.material)) {
                                remove(component);
                                break;
                            }
                        }
                    }
                }
            } else if (target instanceof Material) {
                List<Material> sorted = materialManager.getMaterials().stream().sorted().toList();
                for (int i = 0;i < sorted.size();i++) {
                    Material material = sorted.get(i);
                    Component component = getComponent(material);
                    if (component instanceof MaterialPreviewPanel materialPreviewPanel && materialPreviewPanel.material == target) {
                        remove(materialPreviewPanel);
                        add(new MaterialPreviewPanel(materialPreviewPanel.material),i);
                    } else {
                        setComponentZOrder(component, i);
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

    private static final Border defaultPreviewBorder = BorderFactory.createMatteBorder(1,1,1,1,ViewUtils.HIGHLIGHT_COLOR);

    private static final Dimension previewImageSize = new Dimension(70,50);
    private static final Dimension previewSize = new Dimension(70,70);
    private class MaterialPreviewPanel extends JPanel {
        private final Material material;
        private final JPopupMenu popup = new JPopupMenu();

        private MaterialPreviewPanel(Material material) {
            this.material = material;
            setBorder(defaultPreviewBorder);
            setPreferredSize(previewSize);
            setMinimumSize(previewSize);
            setMaximumSize(previewSize);
            setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
            JLabel imageLabel = new JLabel();
            imageLabel.setPreferredSize(previewImageSize);
            imageLabel.setMinimumSize(previewImageSize);
            imageLabel.setMaximumSize(previewImageSize);
            if (material.getImage() != null) {
                imageLabel.setIcon(new ImageIcon(material.getImage().getScaledInstance(previewImageSize.width,previewImageSize.height,Image.SCALE_SMOOTH)));//TODO 中央に配置する
            } else {
                imageLabel.setBackground(ViewUtils.MATERIAL_ERROR_COLOR);
            }
            JLabel nameLabel = new JLabel(material.getName());
            add(imageLabel);
            add(nameLabel);
            MouseAdapter mouseAdapter = new MouseAdapter() {
                private JPopupMenu draggedMaterial;
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        windowManager.inspectorView().setView(windowManager.inspectorManager().createInspectorView(material));
                    } else if (SwingUtilities.isRightMouseButton(e)) {
                        popup.show(e.getComponent(),e.getX(),e.getY());
                    }
                }

                @Override
                public void mouseDragged(MouseEvent e) {
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        if (draggedMaterial == null) {
                            draggedMaterial = new DragPopup(new MaterialPreviewPanel(material));
                            draggedMaterial.setBorder(BorderFactory.createEmptyBorder());
                        }
                        draggedMaterial.show(e.getComponent(), e.getX() - 1, e.getY() - 1);
                        if (getPropertyAt(e.getComponent(),windowManager.inspectorView(),ImageObjectInspectorView.materialPanelClientPropertyKey,e.getPoint())==null) {
                            windowManager.frame().setCursor(ViewUtils.NO_DRAG);
                        } else {
                            windowManager.frame().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                        }
                    }
                }
                @Override
                public void mouseReleased(MouseEvent e) {
                    if (draggedMaterial != null) {
                        windowManager.frame().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                        draggedMaterial.setVisible(false);
                        draggedMaterial = null;
                        Object property = getPropertyAt(e.getComponent(),windowManager.inspectorView(), ImageObjectInspectorView.materialPanelClientPropertyKey,e.getPoint());
                        if (property instanceof ImageObject imageObject) {
                            imageObject.setMaterialUUID(material.getUUID());
                        }
                    }
                }
            };
            addMouseListener(mouseAdapter);
            addMouseMotionListener(mouseAdapter);

            initPopupMenu();
        }

        private void initPopupMenu() {
            JMenuItem item = new JMenuItem("Delete");
            item.addActionListener(e-> materialManager.removeMaterial(material));
            popup.add(item);
        }

        private Object getPropertyAt(Component source,Container container,Object key,Point point) {
            for (Component component:container.getComponents()) {
                if (component instanceof JComponent jComponent) {
                    Object result = jComponent.getClientProperty(key);
                    if (result != null && !jComponent.contains(SwingUtilities.convertPoint(source,point,jComponent))) {
                        result = null;
                    }
                    if (result == null) {
                        result = getPropertyAt(source,jComponent,key,point);
                    }
                    if (result != null) {
                        return result;
                    }
                }
            }
            return null;
        }
    }

    private class DummyMaterialPreviewPanel extends JPanel {
        private final JTextField textField;
        private DummyMaterialPreviewPanel() {
            setBorder(defaultPreviewBorder);
            setPreferredSize(previewSize);
            setMinimumSize(previewSize);
            setMaximumSize(previewSize);
            setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
            JLabel imageLabel = new JLabel("NoImage");
            imageLabel.setPreferredSize(previewImageSize);
            imageLabel.setMinimumSize(previewImageSize);
            imageLabel.setMaximumSize(previewImageSize);
            textField = new JTextField();
            add(imageLabel);
            add(textField);
            textField.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    materialListViewer.remove(DummyMaterialPreviewPanel.this);
                    if (textField.getText().isEmpty()) {
                        materialManager.addMaterial("NewMaterial");
                    } else {
                        materialManager.addMaterial(textField.getText());
                    }
                }
            });
        }

        @Override
        public void requestFocus() {
            textField.requestFocus();
        }
    }

    private class DragPopup extends JPopupMenu {
        private DragPopup(MaterialPreviewPanel materialPreviewPanel) {
            add(materialPreviewPanel);
        }
    }

    //参考:https://gist.github.com/jirkapenzes/4560255
    //TODO いつか見る:https://ateraimemo.com/Swing/ScrollableWrapLayout.html
    public class WrapLayout extends FlowLayout
    {
        private Dimension preferredLayoutSize;

        public WrapLayout()
        {
            super();
        }

        public WrapLayout(int align)
        {
            super(align);
        }

        public WrapLayout(int align, int hgap, int vgap)
        {
            super(align, hgap, vgap);
        }

        @Override
        public Dimension preferredLayoutSize(Container target)
        {
            return layoutSize(target, true);
        }

        @Override
        public Dimension minimumLayoutSize(Container target)
        {
            Dimension minimum = layoutSize(target, false);
            minimum.width -= (getHgap() + 1);
            return minimum;
        }

        private Dimension layoutSize(Container target, boolean preferred)
        {
            synchronized (target.getTreeLock())
            {
                int targetWidth = target.getSize().width;

                if (targetWidth == 0)
                    targetWidth = Integer.MAX_VALUE;

                int hgap = getHgap();
                int vgap = getVgap();
                Insets insets = target.getInsets();
                int horizontalInsetsAndGap = insets.left + insets.right + (hgap * 2);
                int maxWidth = targetWidth - horizontalInsetsAndGap;

                Dimension dim = new Dimension(0, 0);
                int rowWidth = 0;
                int rowHeight = 0;

                int nmembers = target.getComponentCount();

                for (int i = 0; i < nmembers; i++)
                {
                    Component m = target.getComponent(i);

                    if (m.isVisible())
                    {
                        Dimension d = preferred ? m.getPreferredSize() : m.getMinimumSize();

                        if (rowWidth + d.width > maxWidth)
                        {
                            addRow(dim, rowWidth, rowHeight);
                            rowWidth = 0;
                            rowHeight = 0;
                        }

                        if (rowWidth != 0)
                        {
                            rowWidth += hgap;
                        }

                        rowWidth += d.width;
                        rowHeight = Math.max(rowHeight, d.height);
                    }
                }

                addRow(dim, rowWidth, rowHeight);

                dim.width += horizontalInsetsAndGap;
                dim.height += insets.top + insets.bottom + vgap * 2;

                Container scrollPane = SwingUtilities.getAncestorOfClass(JScrollPane.class, target);
                if (scrollPane != null)
                {
                    dim.width -= (hgap + 1);
                }

                return dim;
            }
        }

        private void addRow(Dimension dim, int rowWidth, int rowHeight)
        {
            dim.width = Math.max(dim.width, rowWidth);

            if (dim.height > 0)
            {
                dim.height += getVgap();
            }

            dim.height += rowHeight;
        }
    }


}
