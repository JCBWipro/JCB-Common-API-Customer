package com.wipro.jcb.livelink.app.machines.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 *  this class representing a feedback request from a user
 */
@Setter
@Getter
public class FeedbackRequest {
    @ApiModelProperty(value = "users feedback", example = "We have great experience with these application", required = true)
    private String userFeedback;

    public FeedbackRequest(String userFeedback) {
        super();
        this.userFeedback = userFeedback;
    }

    public FeedbackRequest() {
        super();
    }
}
