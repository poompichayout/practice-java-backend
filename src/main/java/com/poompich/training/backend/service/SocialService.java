package com.poompich.training.backend.service;

import com.poompich.training.backend.entity.Social;
import com.poompich.training.backend.entity.User;
import com.poompich.training.backend.exception.BaseException;
import com.poompich.training.backend.exception.UserException;
import com.poompich.training.backend.repository.SocialRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class SocialService {
    private final SocialRepository repository;

    public SocialService(SocialRepository repository) {
        this.repository = repository;
    }

    public Optional<Social> findByUser(User user) {
        return repository.findByUser(user);
    }

    public Social create(User user, String facebook, String line, String instagram, String tiktok) {
        Social entity = new Social();
        entity.setUser(user);
        entity.setFacebook(facebook);
        entity.setLine(line);
        entity.setInstagram(instagram);
        entity.setTiktok(tiktok);

        return repository.save(entity);
    }
}
