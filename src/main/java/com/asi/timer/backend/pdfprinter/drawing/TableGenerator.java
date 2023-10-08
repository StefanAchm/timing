package com.asi.timer.backend.pdfprinter.drawing;

import com.asi.timer.backend.pdfprinter.model.Column;
import com.asi.timer.backend.pdfprinter.model.PdfContentContainer;
import com.asi.timer.backend.pdfprinter.model.Row;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

import java.util.List;

public class TableGenerator {

    public static void addTable(Document document, PdfContentContainer pdfContentContainer, int currentPageNr) throws DocumentException {

        PdfPTable table = new PdfPTable(pdfContentContainer.getColumns().size());
        table.setWidthPercentage(100);

        setWidths(table, pdfContentContainer.getColumns());

        addTableHeader(table, pdfContentContainer.getColumns());

        addRows(table, pdfContentContainer.getRowsForPage(currentPageNr), pdfContentContainer.getColumns());

        document.add(table);

    }

    private static void setWidths(PdfPTable table, List<Column> columns) throws DocumentException {

        float[] widths = new float[columns.size()];

        for (int i = 0; i < columns.size(); i++) {
            widths[i] = (float) columns.get(i).getColumnWidth();
        }

        table.setWidths(widths);

    }

    private static void addTableHeader(PdfPTable table,
                                       List<Column> columns) {

        for (Column column : columns) {

            PdfPCell header = new PdfPCell();

            Font font = new Font(Font.FontFamily.UNDEFINED, 12f, Font.BOLD);

            header.setPhrase(new Paragraph(column.getName(), font));
            header.setVerticalAlignment(Element.ALIGN_MIDDLE);
            header.setFixedHeight(25f);
            header.setBorderWidth(0f);
            header.setPaddingBottom(10f);

            table.addCell(header);

        }

    }

    private static void addRows(PdfPTable table, List<Row> rows, List<Column> columns) {

        for (int currentRowNr = 0; currentRowNr < rows.size(); currentRowNr++) {

            Row row = rows.get(currentRowNr);

            List<String> cells = row.getCells();
            for (int currentColumnNr = 0; currentColumnNr < cells.size(); currentColumnNr++) {
                String cell = cells.get(currentColumnNr);

                PdfPCell content = new PdfPCell();

                Font font = new Font(Font.FontFamily.UNDEFINED, 11f);

                // If cell content exceeds the width of the cell, wrapp it!

                String cellContent = getCorrectCellContent(cell, columns, currentColumnNr, font);

                content.setPhrase(new Paragraph(cellContent, font));
                content.setVerticalAlignment(Element.ALIGN_MIDDLE);
                content.setFixedHeight(25f);
                content.setBorderWidth(0f);

                if (currentRowNr % 2 == 0) {
                    content.setBackgroundColor(new BaseColor(230, 230, 230));
                }

                table.addCell(content);

            }
        }

    }

    protected static String getCorrectCellContent(String cell, List<Column> columns, int currentColumnNr, Font font) {

        double allCellWidths = columns.stream()
                .map(Column::getColumnWidth)
                .reduce(0.0, Double::sum);

        double relativeCellWidth = columns.get(currentColumnNr).getColumnWidth() / allCellWidths;

        double cellWidth = relativeCellWidth * 270; // A4 width landscape

        double cellWidthPoint = Utilities.millimetersToPoints((float) cellWidth);

        double cellContentWidthPoint ;

        String finalContent = cell;
        String finalContentWidthDots = cell;

        int i = 0;
        do {

            if(i > 0) {
                finalContent = finalContent.substring(0, finalContent.length() - 1);
                finalContentWidthDots = finalContent + "...";
            }

            cellContentWidthPoint = font
                    .getCalculatedBaseFont(true)
                    .getWidthPoint(finalContentWidthDots, font.getSize());

            cellContentWidthPoint += 5; //

            i++;

        } while (cellContentWidthPoint > cellWidthPoint);

        return finalContentWidthDots;

    }

}
