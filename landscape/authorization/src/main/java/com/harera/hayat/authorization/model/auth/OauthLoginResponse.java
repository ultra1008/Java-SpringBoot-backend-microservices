package com.harera.hayat.authorization.model.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class OauthLoginResponse {

    @JsonProperty("token")
    private String token;
    @JsonProperty("refresh_token")
    private String refreshToken;
}
