package com.jp.daichi.designer.editor.history;

import com.jp.daichi.designer.interfaces.Canvas;
import com.jp.daichi.designer.interfaces.Point;
import com.jp.daichi.designer.interfaces.SignedDimension;
import com.jp.daichi.designer.interfaces.editor.EditorDesignerObject;
import com.jp.daichi.designer.interfaces.editor.HistoryStaff;

import java.util.UUID;

public abstract class DesignerObjectHistoryStaff<U> extends SimpleHistoryStaff<EditorDesignerObject,U> {

    public static HistoryStaff createPositionInstance(UUID uuid,Point oldValue,Point newValue) {
        return new SetPosition(uuid, oldValue, newValue);
    }

    public static HistoryStaff createDimensionInstance(UUID uuid,SignedDimension oldValue,SignedDimension newValue) {
        return new SetDimension(uuid, oldValue, newValue);
    }

    public static HistoryStaff createPriorityInstance(UUID uuid,int oldValue,int newValue) {
        return new SetPriority(uuid,oldValue,newValue);
    }

    public static HistoryStaff createZInstance(UUID uuid,double oldValue,double newValue) {
        return new SetZ(uuid,oldValue,newValue);
    }

    public static HistoryStaff createNameInstance(UUID uuid,String oldValue,String newValue) {
        return new SetName(uuid, oldValue, newValue);
    }

    private DesignerObjectHistoryStaff(UUID uuid, U oldValue, U newValue) {
        super(uuid, oldValue, newValue);
    }

    @Override
    public EditorDesignerObject getTarget(Canvas canvas) {
        return (EditorDesignerObject) canvas.getDesignerObjectManager().getInstance(uuid);
    }

    private static class SetPosition extends DesignerObjectHistoryStaff<Point> {

        public SetPosition(UUID uuid, Point oldValue, Point newValue) {
            super(uuid, oldValue, newValue);
        }

        @Override
        public String description() {
            return "Set Position:"+"("+oldValue.x()+","+oldValue.y()+")"+"to"+"("+newValue.x()+","+newValue.y()+")";
        }

        @Override
        public void setValue(EditorDesignerObject target, Point value) {
            target.setPosition(value);
        }

    }

    private static class SetDimension extends DesignerObjectHistoryStaff<SignedDimension> {

        public SetDimension(UUID uuid, SignedDimension oldValue, SignedDimension newValue) {
            super(uuid, oldValue, newValue);
        }

        @Override
        public String description() {
            return "Set Dimension:(width="+newValue.width()+",height="+newValue.height()+")";//TODO 名前を表示するのもありかも

        }

        @Override
        public void setValue(EditorDesignerObject target, SignedDimension value) {
            target.setDimension(value);
        }
    }

    private static class SetPriority extends DesignerObjectHistoryStaff<Integer> {

        public SetPriority(UUID uuid, Integer oldValue, Integer newValue) {
            super(uuid, oldValue, newValue);
        }

        @Override
        public String description() {
            return "Set Priority:"+newValue;
        }

        @Override
        public void setValue(EditorDesignerObject target, Integer value) {
            target.setPriority(value);
        }
    }

    private static class SetZ extends DesignerObjectHistoryStaff<Double> {

        private SetZ(UUID uuid, Double oldValue, Double newValue) {
            super(uuid, oldValue, newValue);
        }

        @Override
        public String description() {
            return "Set Z:"+newValue;
        }

        @Override
        public void setValue(EditorDesignerObject target, Double value) {
            target.setZ(value);
        }
    }

    private static class SetName extends DesignerObjectHistoryStaff<String> {

        private SetName(UUID uuid, String oldValue, String newValue) {
            super(uuid, oldValue, newValue);
        }

        @Override
        public String description() {
            return "SetName:"+newValue;
        }

        @Override
        public void setValue(EditorDesignerObject target, String value) {
            target.setName(value);
        }
    }
}
