package com.asi.timer.backend.pdfprinter;

import com.asi.timer.backend.pdfprinter.enums.EnumGender;
import com.asi.timer.backend.pdfprinter.model.Pdf;
import com.asi.timer.backend.utils.FileUtils;
import com.asi.timer.enums.EnumPrintType;
import com.asi.timer.helper.CompetitorDummyData;
import com.asi.timer.helper.CompetitorScoreDummyData;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.LocalDate;

class PdfGeneratorTest {

    private static final String folderPath = "C:\\Users\\user\\Desktop\\";

    @Test
    void generateStartList() {

        Pdf build = Pdf.newBuilder()
                .eventTitle("KIOT Bouldercup 2023")
                .type(EnumPrintType.START_LIST)
                .gender(EnumGender.HERREN)
                .round(1)
                .date(LocalDate.now())
                .competitors(CompetitorDummyData.geCompetitorsWithLongStrings())
                .competitorScores(null)
                .build();

        File file = PdfGenerator.generatePdf(folderPath, build);

        FileUtils.openFile(file);

    }

    @Test
    void generateResultList() {

        Pdf build = Pdf.newBuilder()
                .eventTitle("KIOT Bouldercup 2023")
                .type(EnumPrintType.RESULT_LIST)
                .gender(EnumGender.HERREN)
                .round(1)
                .date(LocalDate.now())
                .competitors(null)
                .competitorScores(CompetitorScoreDummyData.getMale1())
                .build();

        File file = PdfGenerator.generatePdf(folderPath, build);

        FileUtils.openFile(file);

    }

    @Test
    void generateResultList2() {

        Pdf build = Pdf.newBuilder()
                .eventTitle("KIOT Bouldercup 2023")
                .type(EnumPrintType.RESULT_LIST)
                .gender(EnumGender.HERREN)
                .round(1)
                .date(LocalDate.now())
                .competitors(null)
                .competitorScores(CompetitorScoreDummyData.getMale(60))
                .build();

        File file = PdfGenerator.generatePdf(folderPath, build);

        FileUtils.openFile(file);

    }

}