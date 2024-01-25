package com.jp.daichi.designer.interfaces.manager;

import com.jp.daichi.designer.interfaces.Canvas;

/**
 * キャンバスのインスタンスの管理を行う
 */
public interface CanvasManager {
    /**
     * キャンバスのインスタンスを取得する
     * 存在しない場合、新しく作成し、ロードする
     * @return キャンバスのインスタンス
     */
    Canvas getInstance();
}
