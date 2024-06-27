package com.asi.timer.backend.pdfprinter.model;

public class Column {

    private String name;
    private double columnWidth;

    public Column(String name, double columnWidth) {
        this.name = name;
        this.columnWidth = columnWidth;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getColumnWidth() {
        return columnWidth;
    }

    public void setColumnWidth(double columnWidth) {
        this.columnWidth = columnWidth;
    }
}
