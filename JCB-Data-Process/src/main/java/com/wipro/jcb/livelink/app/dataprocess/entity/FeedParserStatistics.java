package com.wipro.jcb.livelink.app.dataprocess.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * Author: Rituraj Azad
 * User: RI20474447
 * Date:12-12-2024
 */
@Table
@Entity(name="feed_parser_statistics")
public class FeedParserStatistics implements Serializable {

    @Serial
    private static final long serialVersionUID = -1391423561597534702L;

    @Setter
    @Getter
    @Id
    @JsonIgnore
    @Temporal(TemporalType.TIMESTAMP)
    @Column(columnDefinition = "timestamp default current_timestamp", insertable = false)
    private Date createdAt;

    @Column(name = "data")
    private long feedCount= 0;


    public long getData() {
        return feedCount;
    }

    public void setData(long data) {
        this.feedCount = data;
    }
}
