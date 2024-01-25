package com.jp.daichi.designer.editor.history;

import com.jp.daichi.designer.interfaces.Canvas;
import com.jp.daichi.designer.interfaces.UpdateAction;
import com.jp.daichi.designer.interfaces.editor.History;
import com.jp.daichi.designer.interfaces.editor.HistoryStaff;
import com.jp.daichi.designer.simple.SimpleObservedObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * 基本的な履歴の実装
 */
public class SimpleHistory extends SimpleObservedObject implements History {
    private int head = -1;
    private List<HistoryStaff> historyStaffs = new ArrayList<>();
    private List<HistoryStaff> staffsToCompress = new ArrayList<>();

    private Supplier<String> compressDescriptionSupplier = null;
    private boolean compress = false;

    @Override
    public int getHead() {
        return head;
    }

    @Override
    public void add(HistoryStaff historyStaff) {
        if (!compress) {
            head++;
            historyStaffs = historyStaffs.subList(0, head);
            historyStaffs.add(historyStaff);
            sendUpdate(UpdateAction.ADD);
        } else {
            staffsToCompress.add(historyStaff);
        }
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

    @Override
    public void startCompress(String description) {
        startCompress(()->description);
    }

    @Override
    public void startCompress(Supplier<String> descriptionSupplier) {
        if (!compress) {
            compress = true;
            staffsToCompress = new ArrayList<>();
            compressDescriptionSupplier = descriptionSupplier;
        }
    }

    @Override
    public void finishCompress() {
        if (compress) {
            compress = false;
            if (staffsToCompress.size() > 0) {
                add(new CompressStaff(compressDescriptionSupplier.get(), staffsToCompress));
            }
            staffsToCompress = null;
            compressDescriptionSupplier = null;
        }
    }


    private record CompressStaff(String description, List<HistoryStaff> staffs) implements HistoryStaff {

        @Override
            public void undo(Canvas canvas) {
                for (HistoryStaff staff : staffs) {
                    staff.undo(canvas);
                }
            }

            @Override
            public void redo(Canvas canvas) {
                for (HistoryStaff staff : staffs) {
                    staff.redo(canvas);
                }
            }
        }
}
