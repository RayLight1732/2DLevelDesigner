package com.jp.daichi.designer.simple.editor.history;

import com.jp.daichi.designer.interfaces.editor.History;
import com.jp.daichi.designer.interfaces.editor.HistoryStaff;

import java.util.ArrayList;
import java.util.List;

/**
 * 基本的な履歴の実装
 */
public class SimpleHistory implements History {
    private int head = -1;
    private final List<HistoryStaff> historyStaffs = new ArrayList<>();

    @Override
    public int getHead() {
        return head;
    }

    @Override
    public void add(HistoryStaff historyStaff) {
        historyStaffs.add(historyStaff);
        head++;
    }

    @Override
    public List<HistoryStaff> getHistories() {
        return new ArrayList<>(historyStaffs);
    }
}
