package fr.bodul.demange;

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
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(this.activationDate);
        calendar.add(Calendar.MINUTE, -3);
        return "Le " + getDateFormat().format(this.activationDate) + " entre " + getHourFormat().format(calendar.getTime()) + " et " + getHourFormat().format(this.activationDate);
    }

    public boolean isPlayingInLessThan3Hours() {
        boolean isPlaying = false;
        if (new Date().after(activationDatePlus44Hours())) {
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
        SimpleDateFormat hourFormat = new SimpleDateFormat("H:mm:ss", Locale.FRANCE);
        hourFormat.setTimeZone(TimeZone.getTimeZone("Europe/Paris"));
        return hourFormat;
    }

    private Date activationDatePlus44Hours() {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(this.activationDate);
        calendar.add(Calendar.HOUR, 44);
        return calendar.getTime();
    }
}
