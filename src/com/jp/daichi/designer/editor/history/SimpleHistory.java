package com.jp.daichi.designer.editor.history;

import com.jp.daichi.designer.interfaces.Canvas;
import com.jp.daichi.designer.interfaces.editor.History;
import com.jp.daichi.designer.interfaces.editor.HistoryStaff;
import com.jp.daichi.designer.interfaces.manager.DesignerObjectManager;
import com.jp.daichi.designer.interfaces.manager.LayerManager;
import com.jp.daichi.designer.interfaces.manager.MaterialManager;
import com.jp.daichi.designer.simple.SimpleObservedObject;
import com.jp.daichi.designer.interfaces.UpdateAction;

import java.util.ArrayList;
import java.util.List;

/**
 * 基本的な履歴の実装
 */
public class SimpleHistory extends SimpleObservedObject implements History {
    private int head = -1;
    private List<HistoryStaff> historyStaffs = new ArrayList<>();

    @Override
    public int getHead() {
        return head;
    }

    @Override
    public void add(HistoryStaff historyStaff) {
        head++;
        historyStaffs = historyStaffs.subList(0, head);
        historyStaffs.add(historyStaff);
        sendUpdate(UpdateAction.ADD);
    }

    @Override
    public List<HistoryStaff> getHistories() {
        return new ArrayList<>(historyStaffs);
    }

    @Override
    public HistoryStaff getUndoTarget() {
        if (getHead() != -1) {
            return historyStaffs.get(getHead());
        } else {
            return null;
        }
    }

    @Override
    public HistoryStaff getRedoTarget() {
        if (getHead()+1 < historyStaffs.size()) {
            return historyStaffs.get(getHead()+1);
        } else {
            return null;
        }
    }

    @Override
    public void undo(Canvas canvas) {
        if (canUndo()) {
            getUndoTarget().undo(canvas);
            head--;
            sendUpdate(UpdateAction.UNDO);
        }
    }

    @Override
    public boolean canUndo() {
        return head >= 0;
    }

    @Override
    public void redo(Canvas canvas) {
        if (canRedo()) {
            getRedoTarget().redo(canvas);
            head++;
            sendUpdate(UpdateAction.REDO);
        }
    }

    @Override
    public boolean canRedo() {
        return head+1 < historyStaffs.size();
    }
}
