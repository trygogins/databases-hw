package ru.hsestudy.databases.web.model;

import javax.persistence.Entity;

/**
 * @author georgii
 * @since 6/14/14
 */
@Entity
public class Group {

    Long id;
    String name;
    String screenName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }
}
