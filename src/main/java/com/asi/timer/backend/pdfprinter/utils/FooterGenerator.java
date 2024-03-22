package com.asi.timer.backend.pdfprinter.utils;

import com.asi.timer.backend.pdfprinter.model.Pdf;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class FooterGenerator {

    public static void addTableFooter(PdfWriter writer,
                                      Pdf pdf,
                                      int pageNumber,
                                      int pageNumberTotal) {

        PdfPTable table = new PdfPTable(3);

        //table.setWidthPercentage(100);
        table.setTotalWidth(writer.getPageSize().getWidth() - (2 * 34));

        PdfPCell cell = new PdfPCell();
        cell.setBorderWidth(0f);

        Paragraph phrase;
        Font font = new Font(Font.FontFamily.UNDEFINED, 10f);
        font.setColor(BaseColor.LIGHT_GRAY);

        // Links
        phrase = new Paragraph(pdf.getDateForPdf(), font);
        cell.setPhrase(phrase);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(cell);

        // Mitte
        phrase = new Paragraph("© KIOT - Klettern in Osttirol | www.kiot.at \n kiot.klettern@hotmail.com", font);
        cell.setPhrase(phrase);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        // Rechts
        int actualPageNumber = pageNumber + 1;
        phrase = new Paragraph("Seite " + actualPageNumber + " von " + pageNumberTotal, font);
        cell.setPhrase(phrase);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);


        table.writeSelectedRows(0, -1, 34, 40, writer.getDirectContent());

    }

}
