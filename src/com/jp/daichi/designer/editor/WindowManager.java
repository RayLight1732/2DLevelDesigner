package com.jp.daichi.designer.editor;

import com.jp.daichi.designer.interfaces.editor.InspectorManager;
import com.jp.daichi.designer.editor.inspector.InspectorView;

import javax.swing.*;

public record WindowManager(JFrame frame, InspectorView inspectorView, InspectorManager inspectorManager) {
}
