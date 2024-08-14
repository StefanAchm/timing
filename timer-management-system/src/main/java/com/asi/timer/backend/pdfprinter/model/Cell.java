package com.asi.timer.backend.pdfprinter.model;

import java.util.List;
import java.util.Objects;

public class Cell {

    private final List<String> lines;

    public Cell(String line) {
        this.lines = List.of(Objects.requireNonNullElse(line, ""));
    }

    public Cell(String... lines) {
        this.lines = List.of(Objects.requireNonNullElse(lines, new String[]{""}));
    }

    public List<String> getLines() {
        return lines;
    }

}
