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

        addRows(table, pdfContentContainer.getRowsForPage(currentPageNr));

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
            header.setVerticalAlignment(Element.ALIGN_MIDDLE); // TODO
            header.setFixedHeight(25f);
            header.setBorderWidth(0f);
            header.setPaddingBottom(10f);

            table.addCell(header);

        }

    }

    private static void addRows(PdfPTable table, List<Row> rows) {

        for (int currentRowNr = 0; currentRowNr < rows.size(); currentRowNr++) {

            Row row = rows.get(currentRowNr);

            for (String cell : row.getCells()) {

                PdfPCell content = new PdfPCell();

                Font font = new Font(Font.FontFamily.UNDEFINED, 11f);

                content.setPhrase(new Paragraph(cell, font));
                content.setVerticalAlignment(Element.ALIGN_MIDDLE); // TODO
                content.setFixedHeight(25f);
                content.setBorderWidth(0f);

                if (currentRowNr % 2 == 0) {
                    content.setBackgroundColor(new BaseColor(230, 230, 230));
                }

                table.addCell(content);

            }
        }

    }

}
