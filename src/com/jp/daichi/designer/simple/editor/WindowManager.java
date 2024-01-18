package com.jp.daichi.designer.simple.editor;

import com.jp.daichi.designer.interfaces.editor.InspectorManager;
import com.jp.daichi.designer.simple.editor.inspector.InspectorView;

import javax.swing.*;

public record WindowManager(JFrame frame, InspectorView inspectorView, InspectorManager inspectorManager) {
}
