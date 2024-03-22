package com.asi.timer.enums;

public enum EnumGender {

    HERREN,

    DAMEN;

    public com.asi.timer.backend.pdfprinter.enums.EnumGender toBackendEnum() {
        return switch (this) {
            case HERREN -> com.asi.timer.backend.pdfprinter.enums.EnumGender.HERREN;
            case DAMEN -> com.asi.timer.backend.pdfprinter.enums.EnumGender.DAMEN;
        };
    }

}
