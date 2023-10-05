package com.asi.timer.backend.pdfprinter;

import com.asi.timer.enums.EnumPrintType;
import com.asi.timer.model.db.CompetitorRound;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PdfPrinter {

    public static String print(EnumPrintType printType, CompetitorRound competitorRound) {

        throw new RuntimeException("Not implemented yet");

    }


    public static boolean saveTable(String type,
                                    String pdfPath,
                                    String pdfName,
                                    String gender,
                                    String round,
                                    List<String> header,
                                    List<List<String>> content) {

        if (header.size() != content.get(0).size()) {
            System.out.println("Could not save Table! header length != array length");
            return false;
        }

        Document document = new Document(PageSize.A4.rotate());

        try {

            if(!pdfPath.equals("")) {
                new File(pdfPath).mkdir();
            }

            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pdfPath + "/" + pdfName));

            document.open();

            addTitle(document, gender, type, round);

            addTable(writer, document, type, header, content);

        } catch (Exception e) {
            e.printStackTrace();
            document.close();
            return false;
        }

        document.close();

        return true;
    }

    private static void addTable(PdfWriter writer,
                                 Document document,
                                 String type,
                                 List<String> header,
                                 List<List<String>> content) throws DocumentException {

        int start = 0, end;
        int pageNumber = 1;
        int numberOfRowsFirstPage = 15;
        int numberOfRowsOtherPages = 19; // 4 in between
        int pageNumberTotal = 0;

        if(content.size() <= numberOfRowsFirstPage)
            pageNumberTotal = 1;
        else
            pageNumberTotal = 2 + (content.size() - numberOfRowsFirstPage) / numberOfRowsOtherPages;

        do {

            if (pageNumber == 1)
                end = start + numberOfRowsFirstPage;
            else
                end = start + numberOfRowsOtherPages;

            if (end >= content.size()) {
                end = content.size();
            }

            PdfPTable table = new PdfPTable(header.size());

            table.setWidthPercentage(100);

            // set relative columns width
            // Set for 8 columns
            float faktor = 1;

//            if (type.equals(PrintController.scorelist)) {
//                table.setWidths(new float[]{
//                        1.5f * faktor,
//                        5f * faktor,
//                        3f * faktor,
//                        3f * faktor,
//                        4f * faktor,
//                        2.5f * faktor,
//                        2f * faktor,
//                        3f * faktor,
//                        2f * faktor,
//                        2.5f * faktor});
//            } else if (type.equals(PrintController.startlist)) {
//                table.setWidths(new float[]{
//                        2f * faktor,
//                        6f * faktor,
//                        3f * faktor,
//                        3f * faktor,
//                        3f * faktor});
//            }


            addTableHeader(table, header);
            addRows(table, content, start, end);
            document.add(table);

            newPage(writer, document, pageNumber, pageNumberTotal);

            pageNumber++;
            start = end;

        } while (end < content.size());

    }

    private static void addTitle(Document document,
                                 String gender,
                                 String type,
                                 String round) throws DocumentException {

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

        // Links oben
        phrase = new Paragraph(type.toUpperCase(), font1);
        cell.setPhrase(phrase);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(cell);

        // Rechts oben
        phrase = new Paragraph("KIOT Bouldercup " + Calendar.getInstance().get(Calendar.YEAR), font1);
        cell.setPhrase(phrase);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);

        // Links unten

        if(round != null)
            phrase = new Paragraph(round + " " + gender, font2);
        else
            phrase = new Paragraph(gender, font2);
        cell.setPhrase(phrase);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(cell);

        // Rechts unten
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        Date date = new Date();
        phrase = new Paragraph(dateFormat.format(date), font2);
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

    private static void addTableHeader(PdfPTable table,
                                       List<String> headerStr) {

        for (String s : headerStr) {
            PdfPCell header = new PdfPCell();

            Font font = new Font(Font.FontFamily.UNDEFINED, 12f, Font.BOLD);

            header.setPhrase(new Paragraph(s, font));
            header.setVerticalAlignment(Element.ALIGN_MIDDLE); // TODO
            header.setFixedHeight(25f);
            header.setBorderWidth(0f);
            header.setPaddingBottom(10f);
            table.addCell(header);
        }

    }

    private static void addRows(PdfPTable table, List<List<String>> contentStr, int start, int end) {
        int i = 0;
        for (int j = start; j < end; j++) {
            for (String s : contentStr.get(j)) {
                PdfPCell content = new PdfPCell();

                Font font = new Font(Font.FontFamily.UNDEFINED, 11f);

                content.setPhrase(new Paragraph(s, font));
                content.setVerticalAlignment(Element.ALIGN_MIDDLE); // TODO
                content.setFixedHeight(25f);
                content.setBorderWidth(0f);

                if (i % 2 == 0)
                    content.setBackgroundColor(new BaseColor(230, 230, 230));

                table.addCell(content);
            }
            i++;
        }
    }

    private static void newPage(PdfWriter writer, Document document, int pagenumber, int pagenumberTotal) throws DocumentException {
        addFooter(writer, document, pagenumber, pagenumberTotal);
        document.newPage();
    }

    private static void addFooter(PdfWriter writer, Document document, int pagenumber, int pagenumberTotal) throws DocumentException{

        PdfPTable table = new PdfPTable(3);

        //table.setWidthPercentage(100);
        table.setTotalWidth(writer.getPageSize().getWidth() - (2 * 34));

        PdfPCell cell = new PdfPCell();
        cell.setBorderWidth(0f);

        Paragraph phrase;
        Font font = new Font(Font.FontFamily.UNDEFINED, 10f);
        font.setColor(BaseColor.LIGHT_GRAY);

        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        Date date = new Date();

        // Links
        phrase = new Paragraph(dateFormat.format(date), font);
        cell.setPhrase(phrase);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(cell);

        // Mitte
        phrase = new Paragraph("© KIOT - Klettern in Osttirol | www.kiot.at \n kiot.klettern@hotmail.com", font);
        cell.setPhrase(phrase);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        // Rechts
        phrase = new Paragraph("Seite " + pagenumber + " von " + pagenumberTotal, font);
        cell.setPhrase(phrase);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);

        //        document.add(table);
        table.writeSelectedRows(0, -1, 34, 40, writer.getDirectContent());
    }


}
