package com.wipro.jcb.livelink.app.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "clients")

public class ClientEntity {

    @Id
    private int client_id;
    private String client_name;
    @OneToOne(targetEntity = IndustryEntity.class)
    @JoinColumn(name = "Industy_ID")
    private IndustryEntity industry_id;

    public ClientEntity() {
    }

    public ClientEntity(int client_id, String client_name, IndustryEntity industry_id) {
        super();
        this.client_id = client_id;
        this.client_name = client_name;
        this.industry_id = industry_id;
    }

}
