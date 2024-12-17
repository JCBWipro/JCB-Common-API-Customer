package com.wipro.jcb.livelink.app.machines.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:04-11-2024
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement
@Table(name = "DealerDashboardCustomer", indexes = {@Index(name = "DealerDashboardCustomer", columnList = "USER_ID")})
@DynamicUpdate
public class DealerDashboardCustomer implements Serializable {
    @Serial
    private static final long serialVersionUID = 215027707231769726L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DealerDashboardCustomer_Id", unique = true, nullable = false)
    private Long id;
    @ApiModelProperty(value = "unique identifier for user", example = "Lorey_Holland", required = true)
    @Column(name = "USER_ID", columnDefinition = "VARCHAR(64)")
    private String userName;
    @Column(name = "customer_name")
    @ApiModelProperty(value = "unique String for getting graph type", example = "Alert, SerivceStatus,Connectivity", required = true)
    private String customerName;
    @Column(name = "machine_count")
    @ApiModelProperty(value = "category wise machine count ", example = "100", required = true)
    private int machineCount;
    @Column(name = "customer_id")
    @ApiModelProperty(value = "unique String for getting category", example = "Critical,High", required = true)
    private String customerId;

    @JsonIgnore
    @Temporal(TemporalType.TIMESTAMP)
    @Column(columnDefinition = "timestamp default current_timestamp", insertable = false)
    private Date createdAt;
}
