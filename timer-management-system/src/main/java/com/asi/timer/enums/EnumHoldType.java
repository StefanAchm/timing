package com.asi.timer.enums;

public enum EnumHoldType {

    // GERMAN: "Berührt"
    TOUCHED,

    // GERMAN: "Abgerutscht"
    SLIPPED,

    // GERMAN: "Gehalten"
    HELD,

    // GERMAN: "Gefolgt"
    FOLLOWED,

    // GERMAN: "Weitergezogen"
    MOVED_ON;

    @Override
    public String toString() {
        return switch (this) {
            case TOUCHED -> "Berührt";
            case SLIPPED -> "Abgerutscht";
            case HELD -> "Gehalten";
            case FOLLOWED -> "Gefolgt";
            case MOVED_ON -> "Weitergezogen";
        };
    }

}
