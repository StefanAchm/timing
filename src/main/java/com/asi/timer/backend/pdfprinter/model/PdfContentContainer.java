package com.asi.timer.backend.pdfprinter.model;

import com.asi.timer.enums.EnumPrintType;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PdfContentContainer {

    private final int rowsOnFirstPage;
    private final int rowsOnNextPages;

    private String filePath;
    private String fileName;

    private List<Column> columns;

    private String eventTile;

    private EnumPrintType type;

    private LocalDate date;

    private String gender;

    private int round;

    private List<Row> rows;

    public PdfContentContainer(String filePath,
                               String fileName,
                               String eventTile,
                               LocalDate date,
                               List<Column> columns,
                               EnumPrintType type,
                               String gender,
                               int round,
                               List<Row> rows) {

        if (filePath == null || filePath.isBlank()) {
            throw new IllegalArgumentException("filePath is null or blank");
        }

        if (fileName == null || fileName.isBlank()) {
            throw new IllegalArgumentException("fileName is null or blank");
        }

        if (columns == null || columns.isEmpty()) {
            throw new IllegalArgumentException("columns is null or empty");
        }

        if (rows == null || rows.isEmpty()) {
            throw new IllegalArgumentException("rows is null or empty");
        }

        this.filePath = filePath;
        this.fileName = fileName;
        this.eventTile = eventTile;
        this.columns = columns;
        this.type = type;
        this.gender = gender;
        this.round = round;
        this.rows = rows;
        this.date = date;

        switch (type) {
            case START_LIST -> {
                this.rowsOnFirstPage = 15;
                this.rowsOnNextPages = 20;
            }
            case RESULT_LIST -> {
                this.rowsOnFirstPage = 10;
                this.rowsOnNextPages = 12;
            }
            default -> throw new IllegalArgumentException("type is not supported");
        }

    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getEventTile() {
        return eventTile;
    }

    public void setEventTile(String eventTile) {
        this.eventTile = eventTile;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

    public EnumPrintType getType() {
        return type;
    }

    public void setType(EnumPrintType type) {
        this.type = type;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public List<Row> getRows() {
        return rows;
    }

    public void setRows(List<Row> rows) {
        this.rows = rows;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDateForPdf() {

        return this.date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

    }

    public List<Row> getRowsForPage(int page) {

        int start;
        int end;

        if (page == 0) {

            start = 0;
            end = Math.min(this.rows.size(), start + rowsOnFirstPage);

        } else {

            start = rowsOnFirstPage + (page - 1) * rowsOnNextPages;
            end = Math.min(this.rows.size(), start + rowsOnNextPages);

        }

        return this.rows.subList(start, end);

    }

    public int getNrPages() {

        int nrPages = 1;

        if (this.rows.size() > rowsOnFirstPage) {

            int rowsLeft = this.rows.size() - rowsOnFirstPage;

            nrPages += rowsLeft / rowsOnNextPages;

            if (rowsLeft % rowsOnNextPages != 0) {
                nrPages++;
            }

        }

        return nrPages;

    }

}
