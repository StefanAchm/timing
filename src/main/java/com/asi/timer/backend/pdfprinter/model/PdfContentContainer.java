package com.asi.timer.backend.pdfprinter.model;

import com.asi.timer.enums.EnumPrintType;

import java.util.List;

public class PdfContentContainer {

    private static final int rowsOnFirstPage = 10;
    private static final int rowsOnNextPages = 12;

    private String filePath;
    private String fileName;

    private List<Column> columns;

    private EnumPrintType type;

    private String gender;

    private int round;

    private List<Row> rows;

    public PdfContentContainer(String filePath,
                               String fileName,
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
        this.columns = columns;
        this.type = type;
        this.gender = gender;
        this.round = round;
        this.rows = rows;

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

    public List<Row> getRowsForPage(int page) {

        if (page == 0) {

            return this.rows.subList(0, 10);

        } else {

            int start = rowsOnFirstPage + (page - 1) * rowsOnNextPages;
            int end = Math.min(this.rows.size() - 1, start + rowsOnNextPages);

            return this.rows.subList(start, end);

        }

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
