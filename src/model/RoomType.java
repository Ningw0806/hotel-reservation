package model;

import java.util.HashMap;
import java.util.Map;

public enum RoomType {
    SINGLE("1"),
    DOUBLE("2");

    private static final Map<String, RoomType> LABEL_TO_TYPE_MAP = new HashMap<>();

    static {
        for (RoomType roomType : values()) {
            LABEL_TO_TYPE_MAP.put(roomType.getLabel(), roomType);
        }
    }

    private final String label;

    RoomType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static RoomType valueOfLabel(String label) {
        if (!LABEL_TO_TYPE_MAP.containsKey(label)) {
            throw new IllegalArgumentException("Unknown label: " + label);
        }
        return LABEL_TO_TYPE_MAP.get(label);
    }
}
