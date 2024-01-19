package com.jp.daichi.designer.simple.editor.history;

import com.jp.daichi.designer.interfaces.DesignerObject;
import com.jp.daichi.designer.interfaces.Point;
import com.jp.daichi.designer.interfaces.SignedDimension;
import com.jp.daichi.designer.interfaces.editor.HistoryStaff;
import com.jp.daichi.designer.interfaces.manager.DesignerObjectManager;
import com.jp.daichi.designer.interfaces.manager.LayerManager;
import com.jp.daichi.designer.interfaces.manager.MaterialManager;

import java.util.UUID;

public abstract class DesignerObjectHistoryStaff<U> extends SimpleHistoryStaff<DesignerObject,U> {

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
    public DesignerObject getTarget(DesignerObjectManager designerObjectManager, LayerManager layerManager, MaterialManager materialManager) {
        return designerObjectManager.getDesignerObject(uuid);
    }

    private static class SetPosition extends DesignerObjectHistoryStaff<Point> {

        public SetPosition(UUID uuid, Point oldValue, Point newValue) {
            super(uuid, oldValue, newValue);
        }

        @Override
        public String getDescription() {
            return "Set Position:("+newValue.x()+","+newValue.y()+")";
        }

        @Override
        public void setValue(DesignerObject target, Point value) {
            target.setPosition(value);
        }

    }

    private static class SetDimension extends DesignerObjectHistoryStaff<SignedDimension> {

        public SetDimension(UUID uuid, SignedDimension oldValue, SignedDimension newValue) {
            super(uuid, oldValue, newValue);
        }

        @Override
        public String getDescription() {
            return "Set Dimension:(width="+newValue.width()+",height="+newValue.height()+")";//TODO 名前を表示するのもありかも

        }

        @Override
        public void setValue(DesignerObject target, SignedDimension value) {
            target.setDimension(value);
        }
    }

    private static class SetPriority extends DesignerObjectHistoryStaff<Integer> {

        public SetPriority(UUID uuid, Integer oldValue, Integer newValue) {
            super(uuid, oldValue, newValue);
        }

        @Override
        public String getDescription() {
            return "Set Priority:"+newValue;
        }

        @Override
        public void setValue(DesignerObject target, Integer value) {
            target.setPriority(value);
        }
    }

    private static class SetZ extends DesignerObjectHistoryStaff<Double> {

        private SetZ(UUID uuid, Double oldValue, Double newValue) {
            super(uuid, oldValue, newValue);
        }

        @Override
        public String getDescription() {
            return "Set Z:"+newValue;
        }

        @Override
        public void setValue(DesignerObject target, Double value) {
            target.setZ(value);
        }
    }

    private static class SetName extends DesignerObjectHistoryStaff<String> {

        private SetName(UUID uuid, String oldValue, String newValue) {
            super(uuid, oldValue, newValue);
        }

        @Override
        public String getDescription() {
            return "SetName:"+newValue;
        }

        @Override
        public void setValue(DesignerObject target, String value) {
            target.setName(value);
        }
    }
}
