package com.asi.timer.backend.pdfprinter;

import com.asi.timer.backend.pdfprinter.model.PdfContainer;
import com.asi.timer.backend.score.model.CompetitorScore;
import com.asi.timer.enums.EnumPrintType;
import com.asi.timer.model.db.Competitor;
import com.itextpdf.text.DocumentException;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.List;

public class PdfGenerator {

    public static File generatePdf(String folderPath,
                                   String eventTitle,
                                   EnumPrintType type,
                                   String gender,
                                   int round,
                                   LocalDate date,
                                   List<Competitor> competitors,
                                   List<CompetitorScore> competitorScores) {

        try {

            PdfContainer pdfContainer = new PdfContainer(folderPath, eventTitle, type, gender, round, date, competitors, competitorScores);

            return pdfContainer.generatePdf();

        } catch (DocumentException | FileNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

}
