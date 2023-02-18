package com.poompich.training.backend.api;

import com.poompich.training.backend.business.UserBusiness;
import com.poompich.training.backend.exception.BaseException;
import com.poompich.training.backend.model.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/user")
public class UserApi {
    private final UserBusiness businessService;

    public UserApi(UserBusiness businessService) {
        this.businessService = businessService;
    }

    @PostMapping("/login")
    public ResponseEntity<MLoginResponse> login(@RequestBody MLoginRequest request) throws BaseException {
        return ResponseEntity.ok(businessService.login(request));
    }

    @PostMapping
    @RequestMapping("/register")
    public ResponseEntity<MRegisterResponse> register(@RequestBody MRegisterRequest request) throws BaseException {
        MRegisterResponse response = businessService.register(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/activate")
    public ResponseEntity<MActivateResponse> activate(@RequestBody MActivateRequest request) throws BaseException {
        return ResponseEntity.ok(businessService.activate(request));
    }

    @PostMapping("/resend-activation-email")
    public ResponseEntity<Void> resendActionEmail(@RequestBody MResendActivationEmailRequest request) throws BaseException {
        businessService.resendActivationEmail(request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/refresh-token")
    public ResponseEntity<String> refreshToken() throws BaseException {
        String response = businessService.refreshToken();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<String> uploadProfilePicture(@RequestPart MultipartFile file) throws BaseException {
        return ResponseEntity.ok(businessService.uploadProfilePicture(file));
    }
}
