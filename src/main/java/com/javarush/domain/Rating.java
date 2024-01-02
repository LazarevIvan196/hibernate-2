package com.javarush.domain;

public enum Rating {
    G("G"),
    PG("PG"),
    PG13("PG-13"),
    R("R"),
    NC17("NC-17");
    private final String val;

    Rating(String val) {
        this.val = val;
    }

    public String getVal() {
        return val;
    }
}
