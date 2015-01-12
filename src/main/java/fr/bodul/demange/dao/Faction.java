package fr.bodul.demange.dao;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Entity
@Cache
public class Faction {

    @Id
    private Long factionId;

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getFactionId() {
        return factionId;
    }

    public void setFactionId(Long factionId) {
        this.factionId = factionId;
    }
}
