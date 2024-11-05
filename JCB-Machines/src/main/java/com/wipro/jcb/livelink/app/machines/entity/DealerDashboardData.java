package com.wipro.jcb.livelink.app.machines.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:01-11-2024
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@XmlRootElement
@Table(name = "DealerDashboardData", indexes = {
        @Index(name = "DealerDashboardData", columnList = "USER_ID,category,graph_type", unique = true)})
@DynamicUpdate
public class DealerDashboardData implements Serializable {
    @Serial
    private static final long serialVersionUID = 215027707231769726L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DealerDashboardData_Id", unique = true, nullable = false)
    private Long id;
    @ApiModelProperty(value = "unique identifier for user", example = "Lorey_Holland", required = true)
    @Column(name = "USER_ID", columnDefinition = "VARCHAR(64)")
    private String userName;
    @Column(name = "graph_type")
    @ApiModelProperty(value = "unique String for getting graph type", example = "Alert, SerivceStatus,Connectivity", required = true)
    private String graphType;
    @Column(name = "machine_count")
    @ApiModelProperty(value = "category wise machine count ", example = "100", required = true)
    private int machineCount;
    @Column(name = "category")
    @ApiModelProperty(value = "unique String for getting category", example = "Critical,High", required = true)
    private String category;

    @JsonIgnore
    @Temporal(TemporalType.TIMESTAMP)
    @Column(columnDefinition = "timestamp default current_timestamp", insertable = false)
    private Date createdAt;

    public DealerDashboardData(String userName, String graphType, int machineCount, String category) {
        super();
        this.userName = userName;
        this.graphType = graphType;
        this.machineCount = machineCount;
        this.category = category;
    }

}
