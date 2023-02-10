package com.poompich.training.backend.business;

import com.poompich.training.backend.entity.User;
import com.poompich.training.backend.exception.BaseException;
import com.poompich.training.backend.exception.FileException;
import com.poompich.training.backend.exception.UserException;
import com.poompich.training.backend.mapper.UserMapper;
import com.poompich.training.backend.model.MLoginRequest;
import com.poompich.training.backend.model.MRegisterRequest;
import com.poompich.training.backend.model.MRegisterResponse;
import com.poompich.training.backend.service.TokenService;
import com.poompich.training.backend.service.UserService;
import com.poompich.training.backend.util.SecurityUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserBusiness {
    private final UserService userService;

    private final TokenService tokenService;

    public UserBusiness(UserService userService, TokenService tokenService) {
        this.userService = userService;
        this.tokenService = tokenService;
    }

    public String refreshToken() throws BaseException {
        Optional<String> optUserId = SecurityUtil.getCurrentUserId();
        if (optUserId.isEmpty()) {
            throw UserException.unauthorized();
        }

        String userId = optUserId.get();
        Optional<User> opt = userService.findById(userId);
        if (opt.isEmpty()) {
            throw UserException.notFound();
        }

        User user = opt.get();
        return tokenService.tokenize(user);
    }

    public MRegisterResponse register(MRegisterRequest request) throws BaseException {
        User user = userService.create(request.getEmail(), request.getPassword(), request.getName());
        return UserMapper.INSTANCE.toRegisterResponse(user);
    }

    public String login(MLoginRequest request) throws BaseException {
        // validate request
        if (Objects.isNull(request)) {
            throw UserException.requestNull();
        }

        // verify email
        Optional<User> optionalUser = userService.findByEmail(request.getEmail());
        if (optionalUser.isEmpty()) {
            // throw login fail, email not found
            throw UserException.loginFailedEmailNotFound();
        }
        User user = optionalUser.get();
        if (!userService.matchPassword(request.getPassword(), user.getPassword())) {
            // throw login fail, password incorrect
            throw UserException.loginFailedPasswordNotMatched();
        }

        return tokenService.tokenize(user);
    }

    public String uploadProfilePicture(MultipartFile file) throws BaseException {
        if (file == null) {
            throw FileException.fileNull();
        }

        // validate size
        if (file.getSize() > 1048576 * 2) {
            throw FileException.fileMaxSize();
        }

        String contentType = file.getContentType();
        if (contentType == null) {
            throw FileException.unsupportedType();
        }

        // isImage
        List<String> supportedTypes = Arrays.asList("image/jpeg", "image/png");
        if (!supportedTypes.contains(contentType)) {
            throw FileException.unsupportedType();
        }

        try {
            byte[] bytes = file.getBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "Success";

    }

}
