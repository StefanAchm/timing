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

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

public class PdfContainer {

    private final Document document;
    private final PdfWriter writer;
    private final PdfContentContainer pdfContentContainer;

    public PdfContainer(String folderPath,
                        EnumPrintType type,
                        String gender,
                        int round,
                        List<Competitor> competitors) throws DocumentException, FileNotFoundException {

        this.pdfContentContainer = PdfContentContainerFactory.getContainer(folderPath, type, gender, round, competitors);

        FileUtils.createFolderIfNotExists(this.pdfContentContainer.getFilePath());

        this.document = generateDocument();

        this.writer = generateWriter(this.document, this.pdfContentContainer.getFilePath(), this.pdfContentContainer.getFileName());

        this.document.open();

    }

    public void generatePdf() throws DocumentException {

        this.drawContent();

        this.document.close();

    }

    private static Document generateDocument() {

        return new Document(PageSize.A4.rotate());

    }

    private static PdfWriter generateWriter(Document document, String pdfFilePath, String pdfFileName) throws FileNotFoundException, DocumentException {


        return PdfWriter.getInstance(document, new FileOutputStream(pdfFilePath + "/" + pdfFileName));

    }

    private void drawContent() throws DocumentException {

        int nrPages = this.pdfContentContainer.getNrPages();

        for (int currentPageNr = 0; currentPageNr < nrPages; currentPageNr++) {

            if (currentPageNr == 0) {

                TitleGenerator.addTitle(
                        this.document,
                        "Ergebnisliste", // todo
                        "KIOT Boulderup 2023", // todo
                        "Herren", // todo (can also be Damen, or 'Herren Runde 1')
                        "20. 08. 2022"
                );

            }

            TableGenerator.addTable(this.document, this.pdfContentContainer, currentPageNr);

            FooterGenerator.addTableFooter(this.document, this.writer, currentPageNr, nrPages); // TODO: more dynamic content

            if (currentPageNr < nrPages - 1) {
                this.document.newPage();
            }

        }

    }

}
