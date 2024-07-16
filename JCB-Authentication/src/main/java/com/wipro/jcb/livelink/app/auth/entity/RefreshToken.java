package com.wipro.jcb.livelink.app.auth.entity;

import java.time.Instant;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "refresh_token")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String token;
    @Column(name = "expiry_date")
    private Instant expiryDate;

    @ManyToOne(targetEntity = ContactEntity.class)
    @JoinColumn(name = "contact_id")
    private ContactEntity contactEntity;
}
