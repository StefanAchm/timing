package com.asi.timer.backend.pdfprinter.drawing;

import com.asi.timer.backend.pdfprinter.model.PdfContentContainer;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

public class TitleGenerator {

    public static void addTitle(
            Document document,
            String pdfTitle,
            String eventTitle,
            String subTitle,
            String date
    ) throws DocumentException {

        PdfPTable table = new PdfPTable(2);

        table.setWidthPercentage(100);

        PdfPCell cell = new PdfPCell();
        cell.setBorderWidth(0f);
        cell.setPaddingBottom(10f);

        Paragraph phrase;

        Font font1 = new Font(Font.FontFamily.COURIER);
        font1.setSize(25f);

        Font font2 = new Font(Font.FontFamily.COURIER);
        font2.setSize(15f);

        // Links oben (type)
        phrase = new Paragraph(pdfTitle.toUpperCase(), font1);
        cell.setPhrase(phrase);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(cell);

        // Rechts oben
        phrase = new Paragraph(eventTitle, font1);
        cell.setPhrase(phrase);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);

        // Links unten

        phrase = new Paragraph(subTitle, font2);
        cell.setPhrase(phrase);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(cell);

        // Rechts unten
        phrase = new Paragraph("", font2);
        cell.setPhrase(phrase);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);

        document.add(table);

        // Just for a little spacing
        Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
        Paragraph paragraph = new Paragraph();
        Chunk chunk = new Chunk(" ", font);
        paragraph.add(chunk);
        document.add(paragraph);

    }

    public static void addTitleForResultList(Document document, PdfContentContainer pdfContentContainer) throws DocumentException {

        String subTitle = switch (pdfContentContainer.getGender()) {
            case "HERREN" -> "Herren";
            case "DAMEN" -> "Damen";
            default -> throw new IllegalStateException("Unexpected value: " + pdfContentContainer.getGender());
        };

        TitleGenerator.addTitle(
                document,
                "Ergebnisliste",
                pdfContentContainer.getEventTile(),
                subTitle,
                pdfContentContainer.getDateForPdf()
        );

    }

    public static void addTitleForStartList(Document document, PdfContentContainer pdfContentContainer) throws DocumentException {

        String genderName = switch (pdfContentContainer.getGender()) {
            case "HERREN" -> "Herren";
            case "DAMEN" -> "Damen";
            default -> throw new IllegalStateException("Unexpected value: " + pdfContentContainer.getGender());
        };

        String subtitle = genderName + ": Runde " + pdfContentContainer.getRound();

        TitleGenerator.addTitle(
                document,
                "Startliste",
                pdfContentContainer.getEventTile(),
                subtitle,
                pdfContentContainer.getDateForPdf()
        );

    }
}
