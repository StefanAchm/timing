package com.asi.timer.backend;

import com.asi.timer.backend.model.Competitor;
import com.asi.timer.backend.model.CompetitorRound;
import com.asi.timer.backend.model.Round;
import com.asi.timer.backend.utils.FileUtils;
import com.asi.timer.enums.EnumCompetitorRoundStatus;
import com.asi.timer.enums.EnumGender;
import com.asi.timer.enums.EnumHoldType;
import com.asi.timer.model.view.APICompetitor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CsvUtil {

    private CsvUtil() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static List<APICompetitor> parseFromFile(String fileName) {

        String csv = FileUtils.readFileFromResources(fileName);
        return parseCsv(csv);

    }

    private static List<APICompetitor> parseCsv(String csv) {
        String[] lines = csv.split("\n");
        List<APICompetitor> competitors = new ArrayList<>();
        for (String line : lines) {
            competitors.add(parseCsvLine(line));
        }
        return competitors;
    }

    private static APICompetitor parseCsvLine(String line) {

        String[] parts = line.split(";");

        if (parts.length != 6) {
            throw new IllegalArgumentException("Invalid CSV line: " + line);
        }

        // remove \n \t \r from the strings
        for (int i = 0; i < parts.length; i++) {
            parts[i] = parts[i].replaceAll("[\n\t\r]", "");
        }

        try {

            APICompetitor apiCompetitor = new APICompetitor();
            apiCompetitor.setFirstName(parts[0]);
            apiCompetitor.setLastName(parts[1]);
            apiCompetitor.setCity(parts[2]);
            apiCompetitor.setClub(parts[3]);

            // Dates are stored as '09-09-1996'
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            apiCompetitor.setDateOfBirth(LocalDate.parse(parts[4], dateTimeFormatter));
            apiCompetitor.setGender(EnumGender.valueOf(parts[5]));

            return apiCompetitor;

        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid CSV line: " + line, e);
        }

    }

    public static List<Competitor> readCompetitors(String filePath) {

        // Read the csv: id,city,club,date_of_birth,first_name,gender,last_name,payment_status,start_number

        String csv = FileUtils.readFileFromResources(filePath);
        String[] lines = csv.split("\n");
        List<Competitor> competitors = new ArrayList<>();
        for (int i = 1; i < lines.length; i++) {
            try {
                String line = lines[i];
                line = line.trim().replace("\r", "");
                // Skip the header line if firstLineIsHeader is true
                String[] parts = line.split(",");
                if (parts.length != 9) {
                    throw new IllegalArgumentException("Invalid CSV line: " + line);
                }
                Competitor competitor = new Competitor();
                competitor.setId(UUID.fromString(parts[0]));
                competitor.setCity(parts[1]);
                competitor.setClub(parts[2]);
                competitor.setDateOfBirth(LocalDate.parse(parts[3]));
                competitor.setFirstName(parts[4]);
                competitor.setGender(EnumGender.valueOf(parts[5]));
                competitor.setLastName(parts[6]);
                competitor.setStartNumber(Integer.parseInt(parts[8]));
                competitors.add(competitor);
            } catch (Exception e) {
                throw new IllegalArgumentException("Invalid CSV line: " + lines[i], e);
            }
        }
        return competitors;


    }

    public static List<CompetitorRound> readCompetitorRounds(String filePath, List<Competitor> competitors, List<Round> rounds) {
        // Read the csv: id,competitor_round_status,hold_number,hold_type,try_number,competitor_id,round_id

        String csv = FileUtils.readFileFromResources(filePath);
        String[] lines = csv.split("\n");
        List<CompetitorRound> competitorRounds = new ArrayList<>();
        for (int i = 1; i < lines.length; i++) {
            String line = lines[i];
            line = line.trim().replace("\r", "");
            String[] parts = line.split(",");
            if (parts.length != 7) {
                throw new IllegalArgumentException("Invalid CSV line: " + line);
            }

            UUID competitorId = UUID.fromString(parts[5]);
            Competitor competitor = competitors.stream()
                    .filter(c -> c.getId().equals(competitorId))
                    .findFirst()
                    .orElse(null);

            if(competitor == null) {
                continue; // Skip this line if the competitor is not found
            }

            UUID roundId = UUID.fromString(parts[6]);
            Round round = rounds.stream()
                    .filter(r -> r.getId().equals(roundId))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Round not found: " + roundId));


            CompetitorRound competitorRound = new CompetitorRound();
            competitorRound.setId(UUID.fromString(parts[0]));
            competitorRound.setCompetitorRoundStatus(EnumCompetitorRoundStatus.valueOf(parts[1]));
            competitorRound.setHoldNumber(Integer.parseInt(parts[2]));
            competitorRound.setHoldType(EnumHoldType.valueOf(parts[3]));
            competitorRound.setTryNumber(Integer.parseInt(parts[4]));
            competitorRound.setCompetitor(competitor);
            competitorRound.setRoundNumber(round.getRoundNumber());
            competitorRounds.add(competitorRound);
        }
        return competitorRounds;
    }

    public static List<Round> readRounds(String filePath) {

        // Read the csv: id,gender,max_holds,round_number

        String csv = FileUtils.readFileFromResources(filePath);
        String[] lines = csv.split("\n");
        List<Round> rounds = new ArrayList<>();
        for (int i = 1; i < lines.length; i++) {
            String line = lines[i];
            line = line.trim().replace("\r", "");
            String[] parts = line.split(",");
            if (parts.length != 4) {
                throw new IllegalArgumentException("Invalid CSV line: " + line);
            }
            Round round = new Round(
                    UUID.fromString(parts[0]),
                    Integer.parseInt(parts[3]),
                    Integer.parseInt(parts[2]),
                    EnumGender.valueOf(parts[1])
            );
            rounds.add(round);
        }

        return rounds;
    }
}
