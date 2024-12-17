package com.wipro.jcb.livelink.app.machines.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * Entity class representing user feedback data.
 */
@Setter
@Getter
@Entity
@DynamicUpdate
@Table(name = "all_users_feedback_data")
public class UsersFeedbackData implements Serializable {
    @Serial
    private static final long serialVersionUID = -8088220440426528631L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "users_feedback_data_Id", unique = true, nullable = false)
    private Long id;
    private String userName;
    private Date emailSentOn;
    @Column(columnDefinition = "text")
    private String feedback;

    @JsonIgnore
    @Temporal(TemporalType.TIMESTAMP)
    @Column(columnDefinition = "timestamp default current_timestamp", insertable = false)
    private Date createdAt;

    public UsersFeedbackData(String userName, Date emailSentOn, String feedback) {
        super();
        this.userName = userName;
        this.emailSentOn = emailSentOn;
        this.feedback = feedback;
    }

    public UsersFeedbackData() {
        super();
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    @Override
    public String toString() {
        return "UsersFeedbackData [id=" + id + ", userName=" + userName + ", emailSentOn=" + emailSentOn + ", feedback="
                + feedback + "]";
    }
}
