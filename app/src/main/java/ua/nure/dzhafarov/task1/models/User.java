package ua.nure.dzhafarov.task1.models;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import static java.util.Calendar.DATE;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;
import static java.util.Calendar.getInstance;

public class User implements Serializable {
    
    private UUID id;
    
    private String name;
    
    private String surname;
    
    private Date birthday;
    
    public User() {
        this(UUID.randomUUID());
    }

    private User(UUID id) {
        this.id = id;
        birthday = new Date();
    }
    
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }
    
    public String getAgeString() {
        return String.format(Locale.US, "%d y/o", getDiffYears());
    }
    
    public String getFullName() {
        return String.format(Locale.US, "%s %s", name, surname);
    }
    
    private int getDiffYears() {
        Calendar a = Calendar.getInstance();
        a.setTime(birthday);
        Calendar b = getInstance();

        int diff = b.get(YEAR) - a.get(YEAR);
        if (a.get(MONTH) > b.get(MONTH) ||
                (a.get(MONTH) == b.get(MONTH) && a.get(DATE) > b.get(DATE))) {
            diff--;
        }
        
        return diff;
    }
}
