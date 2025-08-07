package com.asi.timer.model.db;

import com.asi.timer.backend.model.Competitor;
import com.asi.timer.enums.EnumGender;
import com.asi.timer.enums.EnumPaymentStatus;
import com.asi.timer.model.view.APICompetitor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "competitors",
        uniqueConstraints = {
                @UniqueConstraint(name = "uc_competitor_first_last_name", columnNames = {"firstName", "lastName"}),
                @UniqueConstraint(name = "uc_competitor_start_number", columnNames = {"startNumber"}),
        })
public class DBCompetitor {

    @Id
    @GeneratedValue
    private UUID id;

    private int startNumber;

    private String firstName;

    private String lastName;

    private String city;

    private String club;

    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    private EnumGender gender;

    @Enumerated(EnumType.STRING)
    private EnumPaymentStatus paymentStatus;

    @OneToMany(mappedBy = "competitor", cascade = CascadeType.ALL)
    private Set<DBCompetitorRound> competitorRounds;

    public DBCompetitor() {
        this.paymentStatus = EnumPaymentStatus.NOT_PAID;
    }

    public static DBCompetitor fromAPICompetitor(APICompetitor competitor) {

        DBCompetitor dbCompetitor = new DBCompetitor();
        dbCompetitor.setStartNumber(competitor.getStartNumber());
        dbCompetitor.setFirstName(competitor.getFirstName());
        dbCompetitor.setLastName(competitor.getLastName());
        dbCompetitor.setClub(competitor.getClub());
        dbCompetitor.setCity(competitor.getCity());
        dbCompetitor.setDateOfBirth(competitor.getDateOfBirth());
        dbCompetitor.setGender(competitor.getGender());

        if (competitor.getPaymentStatus() != null) {
            dbCompetitor.setPaymentStatus(competitor.getPaymentStatus());
        }

        return dbCompetitor;

    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public int getStartNumber() {
        return startNumber;
    }

    public void setStartNumber(int startNumber) {
        this.startNumber = startNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getClub() {
        return club;
    }

    public void setClub(String club) {
        this.club = club;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public EnumGender getGender() {
        return gender;
    }

    public void setGender(EnumGender gender) {
        this.gender = gender;
    }

    public Set<DBCompetitorRound> getCompetitorRounds() {
        return competitorRounds;
    }

    public void setCompetitorRounds(Set<DBCompetitorRound> competitorRounds) {
        this.competitorRounds = competitorRounds;
    }

    public EnumPaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(EnumPaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Competitor toBackendCompetitor() {

        Competitor competitor = new Competitor();
        competitor.setId(this.id);
        competitor.setStartNumber(this.startNumber);
        competitor.setFirstName(this.firstName);
        competitor.setLastName(this.lastName);
        competitor.setCity(this.city);
        competitor.setClub(this.club);
        competitor.setDateOfBirth(this.dateOfBirth);
        competitor.setGender(this.gender);

        return competitor;

    }
}
