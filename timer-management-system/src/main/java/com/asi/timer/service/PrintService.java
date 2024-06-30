package com.asi.timer.service;

import com.asi.timer.backend.model.CompetitorRound;
import com.asi.timer.backend.model.Round;
import com.asi.timer.backend.pdfprinter.PdfGenerator;
import com.asi.timer.backend.pdfprinter.model.Pdf;
import com.asi.timer.backend.model.CompetitorScore;
import com.asi.timer.backend.utils.ScoreUtil;
import com.asi.timer.components.FileStorageProperties;
import com.asi.timer.enums.EnumGender;
import com.asi.timer.enums.EnumPrintType;
import com.asi.timer.model.db.DBCompetitor;
import com.asi.timer.model.db.DBCompetitorRound;
import com.asi.timer.model.db.DBRound;
import com.asi.timer.repositories.CompetitorRoundRepository;
import com.asi.timer.repositories.RoundRepository;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
public class PrintService {

    private final CompetitorRoundRepository competitorRoundRepository;
    private final RoundRepository roundRepository;
    private final FileStorageProperties fileStorageProperties;

    public PrintService(CompetitorRoundRepository competitorRoundRepository,
                        RoundRepository roundRepository,
                        FileStorageProperties fileStorageProperties
    ) {

        this.competitorRoundRepository = competitorRoundRepository;
        this.roundRepository = roundRepository;
        this.fileStorageProperties = fileStorageProperties;

    }

    public ByteArrayResource getResultList(EnumGender gender) {

        File file = createResultPdf(gender);

        Path path = Paths.get(file.getAbsolutePath());

        try {
            return new ByteArrayResource(Files.readAllBytes(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private File createResultPdf(EnumGender gender) {

        List<CompetitorRound> competitorRounds = this.competitorRoundRepository.findByCompetitor_Gender(gender)
                .stream()
                .map(DBCompetitorRound::toBackendCompetitorRound)
                .toList();

        List<Round> rounds = this.roundRepository.findByGender(gender)
                .stream()
                .map(DBRound::toBackendRound)
                .toList();

        List<CompetitorScore> competitorScores = ScoreUtil.getCompetitorScores(competitorRounds, rounds);

        LocalDate date = LocalDate.now();

        String eventTitle = "KIOT Bouldercup 2024"; // todo

        Pdf pdf = Pdf.newBuilder()
                .eventTitle(eventTitle)
                .type(EnumPrintType.RESULT_LIST)
                .gender(gender.toBackendEnum())
                .round(0)
                .date(date)
                .competitors(null)
                .competitorScores(competitorScores)
                .build();

        return PdfGenerator.generatePdf(fileStorageProperties.getUploadDir(), pdf);


    }

    public ByteArrayResource getStartList(UUID roundId) {

        File file = createStartListPdf(roundId);

        Path path = Paths.get(file.getAbsolutePath());

        try {
            return new ByteArrayResource(Files.readAllBytes(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private File createStartListPdf(UUID roundId) {

        DBRound round = this.roundRepository
                .findById(roundId)
                .orElseThrow(() -> new RuntimeException("Round with id " + roundId + " not found"));

        List<DBCompetitor> competitors = round.getCompetitorRounds()
                .stream()
                .map(DBCompetitorRound::getCompetitor)
                .sorted(Comparator.comparingInt(DBCompetitor::getStartNumber))
                .toList();

        LocalDate date = LocalDate.now();

        String eventTitle = "KIOT Bouldercup 2024"; // todo

        Pdf pdf = Pdf.newBuilder()
                .eventTitle(eventTitle)
                .type(EnumPrintType.START_LIST)
                .gender(round.getGender().toBackendEnum())
                .round(round.getRoundNumber())
                .date(date)
                .competitors(competitors.stream().map(DBCompetitor::toBackendCompetitor).toList())
                .competitorScores(null)
                .build();

        return PdfGenerator.generatePdf(fileStorageProperties.getUploadDir(), pdf);

    }

}
