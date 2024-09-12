package com.wipro.jcb.livelink.app.user.web.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "industry")
public class IndustryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int industry_id;
    private String industry_name;

}
