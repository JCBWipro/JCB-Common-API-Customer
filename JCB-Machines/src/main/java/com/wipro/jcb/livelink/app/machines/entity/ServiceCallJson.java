package com.wipro.jcb.livelink.app.machines.entity;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * Author: Jitendra Prasad
 * User: JI20319932
 * Date:10/10/2024
 * project: JCB-Common-API-Customer
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name="service_call_json")
public class ServiceCallJson implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;


    @Column(name = "field")
    private String field;

    @Column(name = "label")
    private String label;


    @Column(name = "value")
    private String value;

    @Column(name = "type")
    private String type;

    @Column(name = "fieldName")
    private String field_name;

    @ApiModelProperty(value = "Is data visible on UI or not", example = "true", allowableValues = "true,false")
    private Boolean required;
}
