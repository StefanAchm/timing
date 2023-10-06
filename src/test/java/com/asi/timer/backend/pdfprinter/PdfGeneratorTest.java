package com.asi.timer.backend.pdfprinter;

import com.asi.timer.enums.EnumPrintType;
import com.asi.timer.helper.CompetitorsDummyData;
import org.junit.jupiter.api.Test;

class PdfGeneratorTest {


    @Test
    void generateStartList() {

        String folderPath = "C:\\Users\\Stefan\\Desktop\\";

        PdfGenerator.generatePdf(
                folderPath,
                EnumPrintType.START_LIST,
                "male",
                1,
                CompetitorsDummyData.getCompetitors()
        );

    }

}