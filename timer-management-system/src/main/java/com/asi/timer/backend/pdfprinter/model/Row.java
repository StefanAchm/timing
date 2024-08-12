package com.asi.timer.backend.pdfprinter.model;

import java.util.ArrayList;
import java.util.List;

public class Row {

    private List<Cell> cells;

    public Row() {
        this.cells = new ArrayList<>();
    }

    public List<Cell> getCells() {
        return cells;
    }

    public void setCells(List<Cell> cells) {
        this.cells = cells;
    }
}
