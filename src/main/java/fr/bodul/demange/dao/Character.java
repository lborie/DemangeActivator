package fr.bodul.demange.dao;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

import java.text.DateFormat;
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
    private List<Long> factionsId;
    private Boolean active;

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
        return "Le " + getDateFormat().format(this.activationDate) + " à " + getHourFormat().format(this.activationDate) + "H. " +
                "(Rejoue le "+ getDateFormat().format(activationDatePlus45Hours()) + " entre " + getHourFormat().format(activationDatePlus45Hours()) + "H. et " + getHourFormat().format(activationDatePlus47Hours()) + "H.)";
    }

    public boolean isPlayingInLessThan2Hours() {
        boolean isPlaying = false;
        if (new Date().after(activationDatePlus45Hours())) {
            isPlaying = true;
        }
        return isPlaying;
    }

    private DateFormat getDateFormat() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM", Locale.FRANCE);
        dateFormat.setTimeZone(TimeZone.getTimeZone("Europe/Paris"));
        return dateFormat;
    }

    private DateFormat getHourFormat() {
        SimpleDateFormat hourFormat = new SimpleDateFormat("H", Locale.FRANCE);
        hourFormat.setTimeZone(TimeZone.getTimeZone("Europe/Paris"));
        return hourFormat;
    }

    private Date activationDatePlus45Hours() {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(this.activationDate);
        calendar.add(Calendar.HOUR, 45);
        return calendar.getTime();
    }

    private Date activationDatePlus47Hours() {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(this.activationDate);
        calendar.add(Calendar.HOUR, 47);
        return calendar.getTime();
    }

    public List<Long> getFactionsId() {
        return factionsId;
    }

    public void setFactionsId(List<Long> factionsId) {
        this.factionsId = factionsId;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean isActive) {
        this.active = isActive;
    }
}
