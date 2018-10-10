package com.example.jonnd.fuelfinder.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

@Entity(indices = @Index(value = "userName", unique = true))
public class User {
    @PrimaryKey(autoGenerate = true)
    private long Id;
    private String firstName;
    private String lastName;
    private String userName;
    private String password;
    private Date dateCreated;

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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }
}
