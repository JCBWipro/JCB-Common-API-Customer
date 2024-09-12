package com.wipro.jcb.livelink.app.user.web.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "address")
public class AddressEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="Address_ID")
    private int id;
    private String region;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String zipCode;
    private String state;
    private String zone;
    private String country;
}
