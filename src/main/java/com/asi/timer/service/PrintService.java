package com.asi.timer.service;

import com.asi.timer.backend.pdfprinter.PdfGenerator;
import com.asi.timer.backend.pdfprinter.model.Pdf;
import com.asi.timer.backend.utils.CompetitorScoreUtil;
import com.asi.timer.backend.model.CompetitorScore;
import com.asi.timer.components.FileStorageProperties;
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

    public ByteArrayResource getList(UUID id, EnumPrintType type) {

        File file = getFileForStartList(id, type);

        Path path = Paths.get(file.getAbsolutePath());

        try {
            return new ByteArrayResource(Files.readAllBytes(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    // Return the start list as a string
    public File getFileForStartList(UUID id, EnumPrintType type) {

        DBRound round = this.roundRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Round with id " + id + " not found"));

        List<DBCompetitor> competitors = round.getCompetitorRounds()
                .stream()
                .map(DBCompetitorRound::getCompetitor)
                .sorted(Comparator.comparingInt(DBCompetitor::getStartNumber))
                .toList();

        List<CompetitorScore> competitorScores = type.equals(EnumPrintType.RESULT_LIST)
                ? CompetitorScoreUtil.fromCompetitorRound(round.getCompetitorRounds().stream().map(DBCompetitorRound::toBackendCompetitorRound).toList())
                : null;

        LocalDate date = LocalDate.now();

        String eventTitle = "KIOT Boulder Cup 2024"; // todo

        Pdf pdf = Pdf.newBuilder()
                .eventTitle(eventTitle)
                .type(type)
                .gender(round.getGender().toBackendEnum())
                .round(round.getRoundNumber())
                .date(date)
                .competitors(competitors.stream().map(DBCompetitor::toBackendCompetitor).toList())
                .competitorScores(competitorScores)
                .build();

        return PdfGenerator.generatePdf(fileStorageProperties.getUploadDir(), pdf);

    }

}
