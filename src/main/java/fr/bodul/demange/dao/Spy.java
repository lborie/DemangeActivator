package fr.bodul.demange.dao;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
@Cache
public class Spy {

    @Id
    private Long spyId;
    private String content;

    public Long getSpyId() {
        return spyId;
    }

    public void setSpyId(Long spyId) {
        this.spyId = spyId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
