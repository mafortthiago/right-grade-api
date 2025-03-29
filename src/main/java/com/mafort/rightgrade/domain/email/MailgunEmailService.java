package com.mafort.rightgrade.domain.email;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MailgunEmailService {
    @Value("${MAILGUN_API_KEY}")
    private String mailgunApiKey;
    @Value("${MAIL_DOMAIN}")
    private String mailgunDomain;
    @Value("${MAIL_SENDER}")
    private String sender;

    private final WebClient webClient;
    private final TemplateEngine templateEngine;


    @Async
    public void sendEmail(String to, String subject, String templateName, Map<String, String> variables) {
        String emailContent = createEmailContent(templateName, variables);
        String authHeader = createAuthHeader(mailgunApiKey);
        String mailgunUrl = createMailgunUrl(mailgunDomain);

        sendEmailRequest(mailgunUrl, authHeader, sender, to, subject, emailContent);
    }

    private String createEmailContent(String templateName, Map<String, String> variables) {
        Context context = new Context();
        variables.forEach(context::setVariable);
        return templateEngine.process(templateName, context);
    }

    private String createAuthHeader(String apiKey) {
        return "Basic " + Base64.getEncoder().encodeToString(("api:" + apiKey).getBytes(StandardCharsets.UTF_8));
    }

    private String createMailgunUrl(String domain) {
        return "https://api.mailgun.net/v3/" + domain + "/messages";
    }

    private void sendEmailRequest(String url, String authHeader, String from, String to, String subject, String htmlContent) {
        webClient.post()
                .uri(url)
                .header("Authorization", authHeader)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("from", from)
                        .with("to", to)
                        .with("subject", subject)
                        .with("html", htmlContent))
                .retrieve()
                .bodyToMono(String.class)
                .subscribe(
                        response -> System.out.println("Email enviado com sucesso: " + response),
                        error -> System.err.println("Erro ao enviar email: " + error.getMessage())
                );
    }
}
