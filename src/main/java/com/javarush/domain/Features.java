package com.javarush.domain;

import static java.util.Objects.isNull;

public enum Features {
    TRAILERS("Trailers"),
    COMMENTARIES("Commentaries"),
    DELETED_SCENES("Deleted Scenes"),
    BEHIND_THE_SCENES("Behind the Scenes");
    private final String val;

    Features(String val) {
        this.val = val;
    }

    public String getVal() {
        return val;
    }

    public static Features getValueByFeatures(String val) {
        if (isNull(val) || val.isEmpty() || val.isBlank()) {
            return null;
        }
        Features[] features = Features.values();
        for (Features feature : features) {
            if (feature.val.equals(val)) {
                return feature;
            }
        }
        return null;
    }
}
