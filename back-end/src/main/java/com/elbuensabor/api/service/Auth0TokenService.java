package com.elbuensabor.api.service;

public interface Auth0TokenService {
    String getAuth0Token() throws Exception;
    String getDomain();
}
