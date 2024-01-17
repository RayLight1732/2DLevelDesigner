package com.jp.daichi.designer.simple.editor;

import com.jp.daichi.designer.simple.editor.inspector.InspectorView;

import javax.swing.*;

public class WindowManager {
    private final JFrame frame;
    private final InspectorView inspectorView;

    public WindowManager(JFrame frame,InspectorView inspectorView) {
        this.frame = frame;
        this.inspectorView = inspectorView;
    }

    public JFrame getFrame() {
        return frame;
    }

    public InspectorView getInspectorView() {
        return inspectorView;
    }
}
