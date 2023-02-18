package com.poompich.training.backend.business;

import com.poompich.training.backend.exception.BaseException;
import com.poompich.training.backend.exception.EmailException;
import com.poompich.training.common.EmailRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@Service
@Log4j2
public class EmailBusiness {

    private final KafkaTemplate<String, EmailRequest> kafkaTemplate;

    public EmailBusiness(KafkaTemplate<String, EmailRequest> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendActivateUserEmail(String email, String name, String token) throws BaseException {
        String html;
        try {
            html = readEmailTemplate("email-activate-user.html");
        } catch (IOException e) {
            throw EmailException.templateNotFound();
        }

        log.info(token);

        String finalLink = "http://localhost:3000/activate/" + token;
        html = html.replace("${P_NAME}", name);
        html = html.replace("${LINK}", finalLink);

        EmailRequest request = new EmailRequest();
        request.setTo(email);
        request.setSubject("Please activate your account");
        request.setContent(html);

        CompletableFuture<SendResult<String, EmailRequest>> future = kafkaTemplate.send("activation-email", request);
        future.whenComplete((result, ex) -> {
            if (ex != null) {
                log.error("Kafka send fail");
                log.error(ex);
            } else {
                log.info("Kafka send success");
                log.info(result);
            }
        });
    }

    private String readEmailTemplate(String filename) throws IOException {
        File file = ResourceUtils.getFile("classpath:email/" + filename);
        return FileCopyUtils.copyToString(new FileReader(file));
    }

}
