package com.jp.daichi.designer.interfaces;

public class UpdateAction {
    public static final UpdateAction REMOVE = new UpdateAction("REMOVE");
    public static final UpdateAction ADD = new UpdateAction("ADD");
    public static final UpdateAction CHANGE_RECTANGLE = new UpdateAction("ChangeRectangle");
    public static final UpdateAction CHANGE_VISIBLY = new UpdateAction("ChangeVisibly");

    public static final UpdateAction CHANGE_Z = new UpdateAction("ChangeZ");
    public static final UpdateAction CHANGE_PRIORITY = new UpdateAction("ChangePriority");
    public static final UpdateAction CHANGE_UV = new UpdateAction("ChangeUV");
    public static final UpdateAction CHANGE_NAME = new UpdateAction("ChangeName");

    public static final UpdateAction CHANGE_IMAGE = new UpdateAction("ChangeImage");
    public static final UpdateAction CHANGE_MATERIAL = new UpdateAction("ChangeMaterial");
    public static final UpdateAction UNDO = new UpdateAction("Undo");
    public static final UpdateAction REDO = new UpdateAction("Redo");
    public static final UpdateAction CHANGE_FOG_COLOR = new UpdateAction("ChangeFogColor");
    public static final UpdateAction CHANGE_FOG_STRENGTH = new UpdateAction("ChangeFogStrength");
    public static final UpdateAction CHANGE_VIEWPORT = new UpdateAction("ChangeViewport");
    public static final UpdateAction CHANGE_POV = new UpdateAction("ChangePov");
    public static final UpdateAction ADD_RENDERER = new UpdateAction("AddRenderer");
    public static final UpdateAction REMOVE_RENDERER = new UpdateAction("RemoveRenderer");

    public static final UpdateAction FIXED_Y = new UpdateAction("FixedY");

    private final String name;

    public UpdateAction(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "UpdateAction:" + name;
    }
}
