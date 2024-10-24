package com.wipro.jcb.livelink.app.machines.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

/*
 * Author: Jitendra Prasad
 * User: JI20319932
 * Date:10/10/2024
 */

/**
 * The ServiceCallJsonData class represents the structure of a JSON object used in service calls.
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ServiceCallJsonData {
    @JsonProperty("label")
    private String label;

    @JsonProperty("field")
    private String field;

    @JsonProperty("value")
    private List<String> value;

    @JsonProperty("type")
    private String type;

    @JsonProperty("required")
    private Boolean required;

    @JsonProperty("field_name")
    private String fieldName;
    public ServiceCallJsonData(String label, String field, String type, Boolean required, String fieldName) {
        super();
        this.label = label;
        this.field = field;
        this.type = type;
        this.required = required;
        this.fieldName = fieldName;
    }

}
