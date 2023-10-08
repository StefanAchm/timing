package com.asi.timer.backend.pdfprinter.model;

import com.asi.timer.backend.pdfprinter.FileUtils;
import com.asi.timer.backend.pdfprinter.PdfContentContainerFactory;
import com.asi.timer.backend.pdfprinter.drawing.FooterGenerator;
import com.asi.timer.backend.pdfprinter.drawing.TableGenerator;
import com.asi.timer.backend.pdfprinter.drawing.TitleGenerator;
import com.asi.timer.enums.EnumPrintType;
import com.asi.timer.model.db.Competitor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.util.List;

public class PdfContainer {

    private final Document document;
    private final PdfWriter writer;
    private final File file;
    private final PdfContentContainer pdfContentContainer;

    public PdfContainer(String folderPath,
                        String eventTitle,
                        EnumPrintType type,
                        String gender,
                        int round,
                        LocalDate date,
                        List<Competitor> competitors) throws DocumentException, FileNotFoundException {

        this.pdfContentContainer = PdfContentContainerFactory.getContainer(folderPath, eventTitle, type, gender, round, date, competitors);

        FileUtils.createFolderIfNotExists(this.pdfContentContainer.getFilePath());

        this.document = generateDocument();

        this.file = FileUtils.createFileOrUnique(
                this.pdfContentContainer.getFilePath(),
                this.pdfContentContainer.getFileName(),
                "pdf"
        );

        FileOutputStream fileOutputStream = new FileOutputStream(file);

        this.writer = generateWriter(this.document, fileOutputStream);

        this.document.open();

    }

    public File generatePdf() throws DocumentException {

        this.drawContent();

        this.document.close();

        return this.file;

    }

    private static Document generateDocument() {

        return new Document(PageSize.A4.rotate());

    }

    private static PdfWriter generateWriter(Document document, FileOutputStream fileOutputStream) throws DocumentException {

        return PdfWriter.getInstance(document, fileOutputStream);

    }

    private void drawContent() throws DocumentException {

        int nrPages = this.pdfContentContainer.getNrPages();

        for (int currentPageNr = 0; currentPageNr < nrPages; currentPageNr++) {

            if (currentPageNr == 0) {

                if(this.pdfContentContainer.getType() == EnumPrintType.RESULT_LIST) {
                    TitleGenerator.addTitleForResultList(this.document, this.pdfContentContainer);
                } else {
                    TitleGenerator.addTitleForStartList(this.document, this.pdfContentContainer);
                }

            }

            TableGenerator.addTable(this.document, this.pdfContentContainer, currentPageNr);

            FooterGenerator.addTableFooter(this.writer, this.pdfContentContainer, currentPageNr, nrPages);

            if (currentPageNr < nrPages - 1) {
                this.document.newPage();
            }

        }

    }

}
