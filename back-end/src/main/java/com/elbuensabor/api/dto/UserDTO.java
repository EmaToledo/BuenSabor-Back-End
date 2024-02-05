package com.elbuensabor.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDTO extends GenericDTO {
    @JsonProperty("user_id")
    private String auth0UserId;
    private String email;
    @JsonProperty("password")
    private String password;
    private boolean blocked;
    private boolean logged;
    private String roleId;
}
