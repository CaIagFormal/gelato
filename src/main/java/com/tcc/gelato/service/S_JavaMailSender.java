package com.tcc.gelato.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Date;

/**
 * Serviço para enviar e-mails
 */
@Service
public class S_JavaMailSender {
    private final JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String email;

    private final TemplateEngine templateEngine;

    public S_JavaMailSender(JavaMailSender javaMailSender, TemplateEngine templateEngine) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
    }

    /**
     * Envia uma mensagem HTML para um e-mail a partir de texto
     * @param destinatario Destinatário do e-mail
     * @param assunto Assunto do texto
     * @param corpo Texto do e-mail
     */
    public void enviarEmailSimples(String destinatario, String assunto, String corpo) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(destinatario);
        message.setSubject(assunto);
        message.setSentDate(new Date());
        message.setFrom(this.email);
        message.setText(corpo);

        this.javaMailSender.send(message);
    }

    /**
     * Envia uma mensagem HTML para um e-mail a partir de um template e context
     * @param destinatario Destinatário do e-mail
     * @param assunto Assunto do texto
     * @param template Template da página
     * @param context Contexto do template
     * @throws MessagingException Caso o e-mail falhe de enviar
     */
    public void enviarHTML(String destinatario, String assunto, String template, Context context) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,true,"UTF-8");

        helper.setTo(destinatario);
        helper.setSubject("GELATO - "+assunto);
        helper.setSentDate(new Date());
        helper.setFrom(this.email);
        helper.setText(this.templateEngine.process(template,context),true);

        this.javaMailSender.send(message);
    }
}
