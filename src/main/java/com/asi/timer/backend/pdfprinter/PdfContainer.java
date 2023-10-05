package com.asi.timer.backend.pdfprinter;

import com.asi.timer.enums.EnumPrintType;

import java.util.List;

public class PdfContainer {

    private String filePath;
    private String fileName;

    private List<Column> columns;

    private EnumPrintType type;

    private String gender;

    private int round;

    private List<Row> rows;

    public PdfContainer(String filePath,
                        String fileName,
                        List<Column> columns,
                        EnumPrintType type,
                        String gender,
                        int round,
                        List<Row> rows) {

        if(filePath == null || filePath.isBlank()) {
            throw new IllegalArgumentException("filePath is null or blank");
        }

        if(fileName == null || fileName.isBlank()) {
            throw new IllegalArgumentException("fileName is null or blank");
        }

        if(columns == null || columns.isEmpty()) {
            throw new IllegalArgumentException("columns is null or empty");
        }

        if(rows == null || rows.isEmpty()) {
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
}
