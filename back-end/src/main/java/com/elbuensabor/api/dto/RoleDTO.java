package com.elbuensabor.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RoleDTO extends GenericDTO {
    @JsonProperty("name")
    private String denomination;
    @JsonProperty("id")
    private String auth0RoleId;
}
