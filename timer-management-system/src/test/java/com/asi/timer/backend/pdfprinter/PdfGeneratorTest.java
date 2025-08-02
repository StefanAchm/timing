package com.asi.timer.backend.pdfprinter;

import com.asi.timer.backend.model.Competitor;
import com.asi.timer.backend.model.CompetitorRound;
import com.asi.timer.backend.model.CompetitorScore;
import com.asi.timer.backend.model.Round;
import com.asi.timer.backend.pdfprinter.enums.EnumGender;
import com.asi.timer.backend.pdfprinter.model.Pdf;
import com.asi.timer.backend.utils.FileUtils;
import com.asi.timer.backend.utils.ScoreUtil;
import com.asi.timer.enums.EnumPrintType;
import com.asi.timer.helper.CompetitorDummyData;
import com.asi.timer.helper.CompetitorScoreDummyData;
import com.asi.timer.helper.FileHelper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.io.File;
import java.time.LocalDate;
import java.util.List;

@Disabled // Disabled because it requires a GUI to open the generated PDF files, there are no assertions to check the output.
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

    @ParameterizedTest
    @EnumSource(com.asi.timer.enums.EnumGender.class)
    void generateResultList2024(com.asi.timer.enums.EnumGender gender) {

        List<Competitor> competitors = FileHelper.getCompetitors("2024", gender);
        List<Round> rounds = FileHelper.getRounds("2024", gender);
        List<CompetitorRound> competitorRounds = FileHelper.getCompetitorRounds("2024", competitors, rounds, gender);

        List<CompetitorScore> competitorScores = ScoreUtil.getCompetitorScores(competitorRounds, rounds);

        Pdf build = Pdf.newBuilder()
                .eventTitle("KIOT Bouldercup 2024")
                .type(EnumPrintType.RESULT_LIST)
                .gender(gender.toBackendEnum())
                .round(1)
                .date(LocalDate.now())
                .competitors(competitors)
                .competitorScores(competitorScores)
                .build();

        File file = PdfGenerator.generatePdf(folderPath, build);

        FileUtils.openFile(file);

    }

}