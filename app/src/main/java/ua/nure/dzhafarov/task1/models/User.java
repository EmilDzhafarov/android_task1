package ua.nure.dzhafarov.task1.models;

import java.util.Date;
import java.util.UUID;

public class User {
    
    private UUID id;
    
    private String name;
    
    private String surname;
    
    private Date birthday;
    
    public User() {
        this(UUID.randomUUID());
    }

    public User(UUID id) {
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

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", birthday=" + birthday +
                '}';
    }
}