package ua.nure.dzhafarov.task1.models;

import org.threeten.bp.LocalDate;

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
    
    private LocalDate birthday;
    
    public User() {
        this(UUID.randomUUID());
    }

    private User(UUID id) {
        this.id = id;
        birthday = LocalDate.now();
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

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }
    
    public String getAgeString() {
        return String.format(Locale.US, "%d y/o", getDiffYears());
    }
    
    public String getFullName() {
        return String.format(Locale.US, "%s %s", name, surname);
    }
    
    private int getDiffYears() {
        LocalDate a = birthday;
        LocalDate b = LocalDate.now();
        
        int diff = b.getYear() - a.getYear();
        if (a.getMonthValue() > b.getMonthValue() ||
                (a.getMonthValue() == b.getMonthValue() && a.getDayOfMonth() > b.getDayOfMonth())) {
            diff--;
        }
        
        return diff;
    }
}
