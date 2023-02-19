package com.poompich.training.backend.business;

import com.poompich.training.backend.entity.User;
import com.poompich.training.backend.exception.BaseException;
import com.poompich.training.backend.exception.FileException;
import com.poompich.training.backend.exception.UserException;
import com.poompich.training.backend.mapper.UserMapper;
import com.poompich.training.backend.model.*;
import com.poompich.training.backend.service.TokenService;
import com.poompich.training.backend.service.UserService;
import com.poompich.training.backend.util.SecurityUtil;
import io.netty.util.internal.StringUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class UserBusiness {
    private final UserService userService;

    private final TokenService tokenService;

    private final EmailBusiness emailBusiness;

    public UserBusiness(UserService userService, TokenService tokenService, EmailBusiness emailBusiness) {
        this.userService = userService;
        this.tokenService = tokenService;
        this.emailBusiness = emailBusiness;
    }

    public String refreshToken() throws BaseException {
        User user = getUserFromToken();
        return tokenService.tokenize(user);
    }

    public MRegisterResponse register(MRegisterRequest request) throws BaseException {
        User user = userService.create(
                request.getEmail(),
                request.getPassword(),
                request.getName(),
                SecurityUtil.generateToken(),
                nextXMinute(30)
        );

        sendEmail(user);

        return UserMapper.INSTANCE.toRegisterResponse(user);
    }

    private void sendEmail(User user) {
        try {
            emailBusiness.sendActivateUserEmail(user.getEmail(), user.getName(), user.getToken());
        } catch (BaseException e) {
            throw new RuntimeException(e);
        }
    }

    public void resendActivationEmail(MResendActivationEmailRequest request) throws BaseException {
        String email = request.getEmail();
        if (StringUtil.isNullOrEmpty(email)) {
            throw UserException.resendActivationEmailNoEmail();
        }

        Optional<User> opt = userService.findByEmail(email);
        if (opt.isEmpty()) {
            throw UserException.resendActivationEmailUserNotFound();
        }

        User user = opt.get();
        if (user.isActivated()) {
            throw UserException.activateFailAlreadyActivated();
        }

        String token = SecurityUtil.generateToken();
        Date tokenExpiredDate = nextXMinute(30);

        user.setToken(token);
        user.setTokenExpired(tokenExpiredDate);

        user = userService.update(user);

        sendEmail(user);
    }

    public MLoginResponse login(MLoginRequest request) throws BaseException {
        // validate request
        if (Objects.isNull(request)) {
            throw UserException.requestNull();
        }

        // verify email
        Optional<User> optionalUser = userService.findByEmail(request.getEmail());
        if (optionalUser.isEmpty()) {
            throw UserException.loginFailedEmailNotFound();
        }
        User user = optionalUser.get();
        if (!userService.matchPassword(request.getPassword(), user.getPassword())) {
            throw UserException.loginFailedPasswordNotMatched();
        }

        // verify activate status
        if (!user.isActivated()) {
            throw UserException.loginFailedUserUnactivated();
        }

        MLoginResponse response = new MLoginResponse();
        response.setToken(tokenService.tokenize(user));
        return response;
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

    public MActivateResponse activate(MActivateRequest request) throws BaseException {
        String token = request.getToken();
        if (StringUtil.isNullOrEmpty(token)) {
            throw UserException.activateNoToken();
        }

        Optional<User> opt = userService.findByToken(token);
        if (opt.isEmpty()) {
            throw UserException.activateFail();
        }

        User user = opt.get();
        if (user.isActivated()) {
            throw UserException.activateFail();
        }

        Date now = new Date();
        Date expiredDate = user.getTokenExpired();
        if (now.after(expiredDate)) {
            // TODO: re-email

            // TODO: remove user

            throw UserException.activateTokenExpired();
        }

        user.setActivated(true);
        userService.update(user);

        MActivateResponse response = new MActivateResponse();
        response.setSuccess(true);
        return response;
    }

    private Date nextXMinute(int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, minute);
        return calendar.getTime();
    }

    public MUserProfileResponse getUserProfile() throws BaseException {
        User user = getUserFromToken();
        return UserMapper.INSTANCE.toUserProfileResponse(user);
    }

    private User getUserFromToken() throws UserException {
        Optional<String> optUserId = SecurityUtil.getCurrentUserId();
        if (optUserId.isEmpty()) {
            throw UserException.unauthorized();
        }

        String userId = optUserId.get();
        Optional<User> opt = userService.findById(userId);
        if (opt.isEmpty()) {
            throw UserException.notFound();
        }

        return opt.get();
    }

    public MUserProfileResponse updateUserProfile(MUpdateUserProfileRequest request) throws BaseException {
        if (Objects.isNull(request)) {
            throw UserException.requestNull();
        }

        if (StringUtil.isNullOrEmpty(request.getName())) {
            throw UserException.updateNameNull();
        }

        User user = getUserFromToken();
        user = userService.updateName(user.getId(), request.getName());
        return UserMapper.INSTANCE.toUserProfileResponse(user);
    }

}
