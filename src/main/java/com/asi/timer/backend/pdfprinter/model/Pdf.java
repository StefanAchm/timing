package com.asi.timer.backend.pdfprinter.model;

import com.asi.timer.backend.model.Competitor;
import com.asi.timer.backend.pdfprinter.enums.EnumGender;
import com.asi.timer.backend.model.CompetitorScore;
import com.asi.timer.enums.EnumPrintType;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;


public class Pdf {

    private String eventTitle;
    private EnumPrintType type;
    private EnumGender gender;
    private int round;

    private LocalDate date;

    private List<Competitor> competitors;
    private List<CompetitorScore> competitorScores;

    public Pdf(String eventTitle,
               EnumPrintType type,
               EnumGender gender,
               int round,
               LocalDate date,
               List<Competitor> competitors,
               List<CompetitorScore> competitorScores) {

        this.eventTitle = eventTitle;
        this.type = type;
        this.gender = gender;
        this.round = round;
        this.date = date;
        this.competitors = competitors;
        this.competitorScores = competitorScores;

    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public EnumPrintType getType() {
        return type;
    }

    public void setType(EnumPrintType type) {
        this.type = type;
    }

    public EnumGender getGender() {
        return gender;
    }

    public void setGender(EnumGender gender) {
        this.gender = gender;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getDateForPdf() {

        return this.date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<Competitor> getCompetitors() {
        return competitors;
    }

    public void setCompetitors(List<Competitor> competitors) {
        this.competitors = competitors;
    }

    public List<CompetitorScore> getCompetitorScores() {
        return competitorScores;
    }

    public void setCompetitorScores(List<CompetitorScore> competitorScores) {
        this.competitorScores = competitorScores;
    }

    public static EventTitleStep newBuilder() {
        return new Builder();
    }

    private static class Builder implements EventTitleStep, TypeStep, GenderStep, RoundStep, DateStep, CompetitorsStep, CompetitorScoresStep, BuildStep {

        private String eventTitle;
        private EnumPrintType type;
        private EnumGender gender;
        private int round;
        private LocalDate date;
        private List<Competitor> competitors;
        private List<CompetitorScore> competitorScores;

        @Override
        public TypeStep eventTitle(String eventTitle) {
            this.eventTitle = eventTitle;
            return this;
        }

        @Override
        public GenderStep type(EnumPrintType type) {
            this.type = type;
            return this;
        }

        @Override
        public RoundStep gender(EnumGender gender) {
            this.gender = gender;
            return this;
        }

        @Override
        public DateStep round(int round) {
            this.round = round;
            return this;
        }

        @Override
        public CompetitorsStep date(LocalDate date) {
            this.date = date;
            return this;
        }

        @Override
        public CompetitorScoresStep competitors(List<Competitor> competitors) {
            this.competitors = competitors;
            return this;
        }

        @Override
        public BuildStep competitorScores(List<CompetitorScore> competitorScores) {
            this.competitorScores = competitorScores;
            return this;
        }

        @Override
        public Pdf build() {
            return new Pdf(eventTitle, type, gender, round, date, competitors, competitorScores);
        }

    }


    public interface EventTitleStep {
        TypeStep eventTitle(String eventTitle);
    }

    public interface TypeStep {
        GenderStep type(EnumPrintType type);
    }

    public interface GenderStep {
        RoundStep gender(EnumGender gender);
    }

    public interface RoundStep {
        DateStep round(int round);
    }

    public interface DateStep {
        CompetitorsStep date(LocalDate date);
    }

    public interface CompetitorsStep {
        CompetitorScoresStep competitors(List<Competitor> competitors);
    }

    public interface CompetitorScoresStep {
        BuildStep competitorScores(List<CompetitorScore> competitorScores);
    }

    public interface BuildStep {
        Pdf build();
    }

}
