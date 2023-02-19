package com.poompich.training.backend.service;

import com.poompich.training.backend.entity.User;
import com.poompich.training.backend.exception.BaseException;
import com.poompich.training.backend.exception.UserException;
import com.poompich.training.backend.repository.UserRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<User> findByEmail(String email) throws BaseException {
        if (Objects.isNull(email)) {
            throw UserException.createEmailNull();
        }
        return repository.findByEmail(email);
    }

    @Cacheable(value = "user", key = "#userId", unless = "#result == null")
    public Optional<User> findById(String userId) {
        return repository.findById(userId);
    }

    public boolean matchPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    public User create(String email, String password, String name, String token, Date tokenExpiredDate) throws BaseException {
        // validate
        if (Objects.isNull(email)) {
            throw UserException.createEmailNull();
        }

        if (Objects.isNull(password)) {
            throw UserException.createPasswordNull();
        }

        if (Objects.isNull(name)) {
            throw UserException.createNameNull();
        }

        // verify
        if (repository.existsByEmail(email)) {
            throw UserException.createEmailDuplicated();
        }

        // save
        User entity = new User();
        entity.setEmail(email);
        entity.setPassword(passwordEncoder.encode(password));
        entity.setName(name);
        entity.setToken(token);
        entity.setTokenExpired(tokenExpiredDate);

        return repository.save(entity);
    }

    public Optional<User> findByToken(String token) {
        return repository.findByToken(token);
    }

    public User update(User user) {
        return repository.save(user);
    }

    @CachePut(value = "user", key = "#id")
    public User updateName(String id, String name) throws BaseException {
        Optional<User> opt = repository.findById(id);
        if (opt.isEmpty()) {
            throw UserException.notFound();
        }

        User user = opt.get();
        user.setName(name);

        return repository.save(user);
    }

    @CacheEvict(value = "user", key = "#id")
    public void deleteById(String id) {
        repository.deleteById(id);
    }

    @CacheEvict(value = "user", allEntries = true)
    public void deleteAll() {
        repository.deleteAll();
    }
}
