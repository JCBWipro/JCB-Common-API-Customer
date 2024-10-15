package com.wipro.jcb.livelink.app.machines.service.response;

import com.wipro.jcb.livelink.app.machines.entity.MachineDownQuestion;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * Author: Jitendra Prasad
 * User: JI20319932
 * Date:10/2/2024
 * project: JCB-Common-API-Customer
 */
@AllArgsConstructor
@ToString
@Setter
@Getter
public class MachineDownQuestionResponse {
    @ApiModelProperty(value = "List of Machine down Question", required = true)
    List<MachineDownQuestion> questions;

}
