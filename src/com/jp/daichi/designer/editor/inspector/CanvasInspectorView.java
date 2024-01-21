package com.jp.daichi.designer.editor.inspector;

import com.jp.daichi.designer.editor.*;
import com.jp.daichi.designer.interfaces.Canvas;
import com.jp.daichi.designer.interfaces.ObservedObject;
import com.jp.daichi.designer.interfaces.SignedDimension;
import com.jp.daichi.designer.interfaces.UpdateAction;

import javax.swing.*;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.NumberFormat;

import static com.jp.daichi.designer.editor.ViewUtils.HIGHLIGHT_COLOR;
import static com.jp.daichi.designer.editor.ViewUtils.labelHorizontalStruct;

public class CanvasInspectorView extends ObserverJPanel {
    private final EditorCanvas canvas;
    private final WindowManager windowManager;

    private JComponent povPanel;
    private JComponent imagePathPanel;

    public CanvasInspectorView(EditorCanvas canvas, WindowManager windowManager) {
        this.canvas = canvas;
        this.windowManager = windowManager;
        init();
    }

    private void init() {
        imagePathPanel = createImagePathPanel();
        povPanel = createPovPanel();
        setBorder(BorderFactory.createEmptyBorder(10, ViewUtils.LEFT_PADDING, 4, 4));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(InspectorView.createTitlePanel("Canvas Information"));
        add(Box.createVerticalStrut(4));
        add(povPanel);
        add(Box.createVerticalStrut(4));
        add(imagePathPanel);
    }


    @Override
    public void update(ObservedObject target, UpdateAction action) {
        if (target == canvas) {
            if (action == UpdateAction.CHANGE_IMAGE) {
                int z = getComponentZOrder(imagePathPanel);
                remove(z);
                imagePathPanel = createImagePathPanel();
                add(imagePathPanel,z);
            } else if (action == UpdateAction.CHANGE_POV) {
                int z = getComponentZOrder(povPanel);
                remove(z);
                povPanel = createPovPanel();
                add(povPanel,z);
            }
        }
    }


    private JComponent createPovPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(new SmoothJLabel("Pov(Degrees)"));
        panel.add(Box.createHorizontalStrut(labelHorizontalStruct));
        panel.add(Box.createGlue());
        NumberFormat format = NumberFormat.getIntegerInstance();
        format.setGroupingUsed(false);
        JTextField textField = ViewUtils.createNumberTextField(Math.toDegrees(canvas.getPov()),new ViewUtils.SetterRunnable<>(value -> canvas.setPov(Math.toRadians(value.doubleValue()))));
        textField.setColumns(20);
        panel.add(textField);
        return panel;
    }

    private JComponent createImagePathPanel() {
        JPanel panel = new JPanel();
        SmoothJTextField label = new SmoothJTextField();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(new SmoothJLabel("Image"));
        panel.add(Box.createHorizontalStrut(labelHorizontalStruct));
        panel.add(Box.createGlue());
        if (canvas.getFile() == null) {
            label.setText("None");
        } else if (canvas.getFile().exists()) {
            label.setText(canvas.getFile().getAbsolutePath());
        } else {
            label.setText("Missing");
        }

        label.setEditable(false);

        panel.add(label);
        label.addMouseListener(imagePanelMouseAdapter);
        return panel;
    }

    private final MouseAdapter imagePanelMouseAdapter = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new ImageFileFilter());
            if (canvas.getFile() != null) {
                fileChooser.setSelectedFile(canvas.getFile());
            }
            int i = fileChooser.showOpenDialog(windowManager.frame());
            if (i == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                canvas.setFile(file);
            }
        }
    };

}
