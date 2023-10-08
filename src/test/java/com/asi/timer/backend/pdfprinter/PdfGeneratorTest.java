package com.asi.timer.backend.pdfprinter;

import com.asi.timer.enums.EnumPrintType;
import com.asi.timer.helper.CompetitorScoreDummyData;
import com.asi.timer.helper.CompetitorsDummyData;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.LocalDate;

class PdfGeneratorTest {


    @Test
    void generateStartList() {

        String folderPath = "C:\\Users\\Stefan\\Desktop\\";

        File file = PdfGenerator.generatePdf(
                folderPath,
                "KIOT Bouldercup 2023",
                EnumPrintType.START_LIST,
                "male",
                1,
                LocalDate.now(),
                CompetitorsDummyData.geCompetitorsWithLongStrings(),
                null
        );

        FileUtils.openFile(file);

    }

    @Test
    void generateResultList() {

        String folderPath = "C:\\Users\\Stefan\\Desktop\\";

        File file = PdfGenerator.generatePdf(
                folderPath,
                "KIOT Bouldercup 2023",
                EnumPrintType.RESULT_LIST,
                "male",
                1,
                LocalDate.now(),
                null,
                CompetitorScoreDummyData.getMale1()
        );

        FileUtils.openFile(file);

    }

    @Test
    void generateResultList2() {

        String folderPath = "C:\\Users\\Stefan\\Desktop\\";

        File file = PdfGenerator.generatePdf(
                folderPath,
                "KIOT Bouldercup 2023",
                EnumPrintType.RESULT_LIST,
                "male",
                1,
                LocalDate.now(),
                null,
                CompetitorScoreDummyData.getMale(60)
        );

        FileUtils.openFile(file);

    }

}