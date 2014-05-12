package fr.bodul.demange;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

import java.text.SimpleDateFormat;
import java.util.*;

@Entity
@Cache
public class Character {

    @Id
    private Long matricule;

    private String name;
    private Integer currentExperience;
    private Date activationDate;

    public Long getMatricule() {
        return matricule;
    }

    public void setMatricule(Long matricule) {
        this.matricule = matricule;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCurrentExperience() {
        return currentExperience;
    }

    public void setCurrentExperience(Integer currentExperience) {
        this.currentExperience = currentExperience;
    }

    public Date getActivationDate() {
        return activationDate;
    }

    public void setActivationDate(Date activationDate) {
        this.activationDate = activationDate;
    }

    public String displayActivationText() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM", Locale.FRANCE);
        dateFormat.setTimeZone(TimeZone.getTimeZone("Europe/Paris"));
        SimpleDateFormat hourFormat = new SimpleDateFormat("H:mm:ss z", Locale.FRANCE);
        hourFormat.setTimeZone(TimeZone.getTimeZone("Europe/Paris"));
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(this.activationDate);
        calendar.add(Calendar.MINUTE, -5);
        return "Le " + dateFormat.format(this.activationDate) + " entre " + hourFormat.format(calendar.getTime()) + " et " + hourFormat.format(this.activationDate);
    }
}
