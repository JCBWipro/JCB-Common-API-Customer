package com.wipro.jcb.livelink.app.alerts.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:14-09-2024
 *
 */
@Getter
@Setter
@RequiredArgsConstructor
@ToString
@Entity
@DynamicUpdate
@Table(name = "customer", indexes = {
        @Index(name = "customerIndexes", columnList = "id")})
public class Customer implements Serializable {
    @Serial
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

    public Customer(String id, String name, String phonenumber, String country, String address, String firstName,
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

}

