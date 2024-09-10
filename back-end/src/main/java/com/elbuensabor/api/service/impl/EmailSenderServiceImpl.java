package com.elbuensabor.api.service.impl;


import com.elbuensabor.api.service.EmailSenderService;
import jakarta.mail.internet.MimeMessage;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.Base64;

@Service
@Getter
public class EmailSenderServiceImpl implements EmailSenderService {
    @Value("${EMAIL_USER_NAME}")
    private String email;

@Autowired
private JavaMailSender mailSender;
    @Override
    public void sendEmail(String toEmail, String base64Pdf,Long number) throws Exception {
        try {
            // Convertir el string base64 a bytes
            byte[] pdfBytes = Base64.getDecoder().decode(base64Pdf);

            // Crear un InputStreamSource a partir del InputStream
            InputStreamSource pdfResource = new ByteArrayResource(pdfBytes);

            // Crear un objeto MimeMessage
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);

            // Configurar el mensaje
            messageHelper.setTo(toEmail);
            messageHelper.setFrom(email);
            messageHelper.setSubject("Factura N°" +String.format("%04d", number));
            messageHelper.setText("Adjunto encontrarás tu factura en formato PDF.");

            // Agregar el archivo PDF adjunto
            messageHelper.addAttachment("factura.pdf", pdfResource, "application/pdf");

            // Enviar el correo
            mailSender.send(mimeMessage);
        } catch (Exception e) {
            throw new Exception("Error send email : " + e.getMessage(), e);
        }
    }
}
