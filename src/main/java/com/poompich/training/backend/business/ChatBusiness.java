package com.poompich.training.backend.business;

import com.poompich.training.backend.exception.BaseException;
import com.poompich.training.backend.exception.ChatException;
import com.poompich.training.backend.model.ChatMessage;
import com.poompich.training.backend.model.ChatMessageRequest;
import com.poompich.training.backend.util.SecurityUtil;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ChatBusiness {

    private final SimpMessagingTemplate template;

    public ChatBusiness(SimpMessagingTemplate template) {
        this.template = template;
    }

    public void post(ChatMessageRequest request) throws BaseException {
        final String destination = "chat";

        Optional<String> opt = SecurityUtil.getCurrentUserId();

        if (opt.isEmpty()) {
            throw ChatException.accessDenied();
        }

        // TODO: validate message

        ChatMessage payload = new ChatMessage();
        payload.setFrom(opt.get());
        payload.setMessage(request.getMessage());

        template.convertAndSend(destination, payload);
    }
}
