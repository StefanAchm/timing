package com.asi.timer.backend.pdfprinter.model;

import com.asi.timer.enums.EnumPrintType;

import java.util.List;

public class Table {

    private final int rowsOnFirstPage;
    private final int rowsOnNextPages;

    private final List<Column> columns;

    private final List<Row> rows;

    public Table(List<Column> columns,
                 EnumPrintType type,
                 List<Row> rows) {

        this.columns = columns;
        this.rows = rows;

        switch (type) {
            case START_LIST, RESULT_LIST -> {
                this.rowsOnFirstPage = 11;
                this.rowsOnNextPages = 14;
            }
            default -> throw new IllegalArgumentException("Unknown print type: " + type);

        }

    }



    public List<Column> getColumns() {
        return columns;
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
