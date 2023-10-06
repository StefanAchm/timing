package com.asi.timer.backend.pdfprinter.drawing;

import com.asi.timer.backend.pdfprinter.model.Column;
import com.itextpdf.text.pdf.PdfPTable;

import java.util.List;

public class TableHeaderGenerator {


    public static void addTableHeader(List<Column> columns) {

        PdfPTable table = new PdfPTable(columns.size());

        table.setWidthPercentage(100);


    }
}
