package com.jp.daichi.designer.editor;

import com.jp.daichi.designer.interfaces.ObservedObject;
import com.jp.daichi.designer.interfaces.UpdateAction;

/**
 * デザイナーオブジェクトの状態を監視し、表示するJComponentに付けられる
 */
public interface ObserverComponent {
    void update(ObservedObject target, UpdateAction action);
}
