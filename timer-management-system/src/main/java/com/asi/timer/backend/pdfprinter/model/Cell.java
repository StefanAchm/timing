package com.asi.timer.backend.pdfprinter.model;

import java.util.List;

public class Cell {

    private List<String> lines;

    public Cell(String line) {
        this.lines = List.of(line);
    }

    public Cell(String... lines) {
        this.lines = List.of(lines);
    }

    public Cell(List<String> lines) {
        this.lines = lines;
    }

    public List<String> getLines() {
        return lines;
    }

}
