package com.jp.daichi.designer.simple.editor;

public class UpdateAction {
    public static final UpdateAction REMOVE = new UpdateAction("REMOVE");
    public static final UpdateAction ADD = new UpdateAction("ADD");
    public static final UpdateAction CHANGE_RECTANGLE = new UpdateAction("ChangeRectangle");
    public static final UpdateAction REORDER = new UpdateAction("Reorder");
    public static final UpdateAction CHANGE_VISIBLY = new UpdateAction("ChangeVisibly");

    public static final UpdateAction CHANGE_Z = new UpdateAction("ChangeZ");
    public static final UpdateAction CHANGE_PRIORITY = new UpdateAction("ChangePriority");
    public static final UpdateAction CHANGE_UV = new UpdateAction("ChangeUV");
    public static final UpdateAction CHANGE_NAME = new UpdateAction("ChangeName");
    private final String name;
    public UpdateAction(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "UpdateAction:"+name;
    }
}