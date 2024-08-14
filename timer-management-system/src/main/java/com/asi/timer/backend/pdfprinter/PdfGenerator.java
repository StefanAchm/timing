package com.asi.timer.backend.pdfprinter;

import com.asi.timer.backend.pdfprinter.model.Pdf;
import com.asi.timer.backend.pdfprinter.model.Table;
import com.asi.timer.backend.pdfprinter.utils.*;
import com.asi.timer.backend.utils.FileUtils;
import com.asi.timer.enums.EnumPrintType;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class PdfGenerator {


    private Document document;
    private PdfWriter writer;
    private File file;

    private Table table;
    private Pdf pdf;

    public PdfGenerator() {}


    public static File generatePdf(String folderPath, Pdf pdf) {

        PdfGenerator pdfGenerator = new PdfGenerator();

        pdfGenerator.pdf = pdf;
        pdfGenerator.table = getData(pdf);

        pdfGenerator.document = new Document(PageSize.A4.rotate());

        String fileName = FileNameGenerator.getFileName(pdf.getType(), pdf.getGender(), pdf.getRound());

        pdfGenerator.file = FileUtils.createFileOrUnique(folderPath, fileName, "pdf");

        try {

            FileOutputStream fileOutputStream = new FileOutputStream(pdfGenerator.file);

            pdfGenerator.writer = PdfWriter.getInstance(pdfGenerator.document, fileOutputStream);

            pdfGenerator.document.open();

            pdfGenerator.drawContent();

            pdfGenerator.document.close();

            return pdfGenerator.file;

        } catch (FileNotFoundException | DocumentException e) {
            throw new RuntimeException(e);
        }

    }

    private static Table getData(Pdf pdf) {

        return new Table(
                HeaderGenerator.getHeader(pdf.getType()),
                pdf.getType(),
                RowsGenerator.getRows(pdf.getType(), pdf.getCompetitors(), pdf.getCompetitorScores()));

    }


    private void drawContent() throws DocumentException {

        int nrPages = this.table.getNrPages();

        for (int currentPageNr = 0; currentPageNr < nrPages; currentPageNr++) {

            if (currentPageNr == 0) {

                if(this.pdf.getType() == EnumPrintType.RESULT_LIST) {
                    TitleGenerator.addTitleForResultList(this.document, this.pdf);
                } else {
                    TitleGenerator.addTitleForStartList(this.document, this.pdf);
                }

            }

            TableGenerator.addTable(this.document, this.table, currentPageNr);

            FooterGenerator.addTableFooter(this.writer, this.pdf, currentPageNr, nrPages);

            if (currentPageNr < nrPages - 1) {
                this.document.newPage();
            }

        }

    }


}
