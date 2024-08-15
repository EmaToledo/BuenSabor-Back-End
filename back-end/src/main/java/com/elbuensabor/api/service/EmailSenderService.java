package com.elbuensabor.api.service;


public interface EmailSenderService {
    void sendEmail(String mail, String base64PDF,Long number) throws Exception;
}
