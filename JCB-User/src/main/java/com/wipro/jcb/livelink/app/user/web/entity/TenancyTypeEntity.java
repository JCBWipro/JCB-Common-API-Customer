package com.wipro.jcb.livelink.app.user.web.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tenancy_type")
public class TenancyTypeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int tenancy_type_id;
    private String tenancy_type_name;

}
