package com.jp.daichi.designer.simple.editor;

import com.jp.daichi.designer.interfaces.ObservedObject;

/**
 * デザイナーオブジェクトの状態を監視し、表示するJComponentに付けられる
 */
public interface ObserverComponent {
    void update(ObservedObject target, UpdateAction action);
}
