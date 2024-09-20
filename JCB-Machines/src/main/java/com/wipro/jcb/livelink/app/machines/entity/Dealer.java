package com.wipro.jcb.livelink.app.machines.entity;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.*;
import org.hibernate.annotations.DynamicUpdate;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:14-09-2024
 * project: JCB-Common-API-Customer
 */

@Entity
@DynamicUpdate
@Table(name = "dealer", indexes = {
        @Index(name = "dealerIndexes", columnList = "id", unique = false) })
public class Dealer implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @Id
    private String id;
    private String name;
    private String phonenumber;
    private String country = "";
    private String address;
    private String firstName;
    private String lastName;

    @JsonIgnore
    @Temporal(TemporalType.TIMESTAMP)
    @Column(columnDefinition = "timestamp default current_timestamp", insertable = false)
    private Date createdAt;

    public Dealer() {
    }

    public Dealer(String id, String name, String phonenumber, String country, String address, String firstName,
                  String lastName) {
        super();
        this.id = id;
        this.name = name;
        this.phonenumber = phonenumber;
        this.country = country;
        this.address = address;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
