package com.lauryn.monthe.NextCarBackend.domain;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Status {

    ACTIVE("ACTIVE"),
    BUSY("BUSY");

    private String value;

    private Status(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public String toString() {
        return String.valueOf(this.value);
    }

    @JsonCreator
    public static Status fromValue(String value) {
        Status[] var1 = values();
        int var2 = var1.length;
        for(int var3 = 0; var3 < var2; ++var3) {
            Status b = var1[var3];
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }

}
