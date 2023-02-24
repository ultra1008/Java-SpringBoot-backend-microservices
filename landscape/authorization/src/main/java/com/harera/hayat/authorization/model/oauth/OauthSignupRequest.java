package com.harera.hayat.authorization.model.oauth;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.harera.hayat.authorization.model.SignupDto;

import lombok.Data;

@Data
public class OauthSignupRequest extends SignupDto {

    @JsonProperty("oauth_token")
    private String oauthToken;
}
