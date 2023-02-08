package com.poompich.training.backend.api;

import com.poompich.training.backend.business.UserBusiness;
import com.poompich.training.backend.exception.BaseException;
import com.poompich.training.backend.model.MLoginRequest;
import com.poompich.training.backend.model.MRegisterRequest;
import com.poompich.training.backend.model.MRegisterResponse;
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
    public ResponseEntity<String> login(@RequestBody MLoginRequest request) throws BaseException {
        return ResponseEntity.ok(businessService.login(request));
    }

    @PostMapping
    @RequestMapping("/register")
    public ResponseEntity<MRegisterResponse> register(@RequestBody MRegisterRequest request) throws BaseException {
        MRegisterResponse response = businessService.register(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<String> uploadProfilePicture(@RequestPart MultipartFile file) throws BaseException {
        return ResponseEntity.ok(businessService.uploadProfilePicture(file));
    }
}
