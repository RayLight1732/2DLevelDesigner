package com.jp.daichi.designer.simple.editor;

import com.jp.daichi.designer.interfaces.DesignerObject;
import com.jp.daichi.designer.interfaces.Point;
import com.jp.daichi.designer.interfaces.SignedDimension;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class DesignerObjectSerializer {
    public static Map<String,Object> serialize(DesignerObject designerObject) {
        return serialize(designerObject,new HashMap<>());
    }
    private static final String NAME = "Name";
    private static final String UUID = "UUID";
    private static final String POSITION = "Position";
    private static final String Z = "Z";
    private static final String DIMENSION = "Dimension";
    private static final String PRIORITY = "Priority";
    public static Map<String,Object> serialize(DesignerObject designerObject,Map<String,Object> map) {
        map.put(NAME, designerObject.getName());
        map.put(UUID, designerObject.getUUID());
        map.put(POSITION,designerObject.getPosition());
        map.put(Z,designerObject.getZ());
        map.put(DIMENSION,designerObject.getDimension());
        map.put(PRIORITY,designerObject.getPriority());
        return map;
    }

    public static DeserializedData deserialize(Map<String,Object> map) {
        try {
            String name = (String) map.get(NAME);
            Objects.requireNonNull(name);
            UUID uuid = (UUID) map.get(UUID);
            Objects.requireNonNull(uuid);
            Point position = (Point) map.get(POSITION);
            Objects.requireNonNull(position);
            double z = (double) map.get(Z);
            SignedDimension dimension = (SignedDimension) map.get(DIMENSION);
            Objects.requireNonNull(dimension);
            int priority = (int)map.get(PRIORITY);
            return new DeserializedData(name,uuid,position,z,dimension,priority);
        } catch (NullPointerException | ClassCastException e) {
            return null;
        }
    }

    public record DeserializedData(String name, UUID uuid, Point position, double z, SignedDimension dimension,int priority) {

    }
}
