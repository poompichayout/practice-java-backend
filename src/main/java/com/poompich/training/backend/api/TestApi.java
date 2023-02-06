package com.poompich.training.backend.api;

import com.poompich.training.backend.business.TestBusiness;
import com.poompich.training.backend.exception.BaseException;
import com.poompich.training.backend.model.MRegisterRequest;
import com.poompich.training.backend.model.TestResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
public class TestApi {
    private final TestBusiness businessService;

    public TestApi(TestBusiness businessService) {
        this.businessService = businessService;
    }

    @GetMapping
    public TestResponse test() {
        TestResponse testResponse = new TestResponse();
        testResponse.setName("Poom");
        testResponse.setFood("KFC");
        return testResponse;
    }

    @PostMapping
    @RequestMapping("/register")
    public ResponseEntity<String> register(@RequestBody MRegisterRequest request) throws BaseException {
        String response = businessService.register(request);
        return ResponseEntity.ok(response);
    }
}
