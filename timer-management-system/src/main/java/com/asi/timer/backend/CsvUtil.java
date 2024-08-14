package com.asi.timer.backend;

import com.asi.timer.backend.utils.FileUtils;
import com.asi.timer.enums.EnumGender;
import com.asi.timer.model.view.APICompetitor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CsvUtil {

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

}
