package com.asi.timer.backend.pdfprinter;

import com.asi.timer.backend.pdfprinter.model.PdfContainer;
import com.asi.timer.enums.EnumPrintType;
import com.asi.timer.model.db.Competitor;
import com.itextpdf.text.DocumentException;

import java.io.FileNotFoundException;
import java.util.List;

public class PdfGenerator {

    public static void generatePdf(String folderPath,
                                   EnumPrintType type,
                                   String gender,
                                   int round,
                                   List<Competitor> competitors) {

        try {

            PdfContainer pdfContainer = new PdfContainer(folderPath, type, gender, round, competitors);

            pdfContainer.generatePdf();

        } catch (DocumentException | FileNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

}
