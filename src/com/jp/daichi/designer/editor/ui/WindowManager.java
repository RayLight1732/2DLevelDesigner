package com.jp.daichi.designer.editor.ui;

import com.jp.daichi.designer.editor.ui.inspector.InspectorView;
import com.jp.daichi.designer.interfaces.editor.InspectorManager;

import javax.swing.*;

/**
 * ウィンドウを管理するレコード
 * @param frame メインとなるフレーム
 * @param inspectorView インスペクタービュー
 * @param inspectorManager インスペクターマネージャー
 */
public record WindowManager(JFrame frame, InspectorView inspectorView, InspectorManager inspectorManager) {
}
