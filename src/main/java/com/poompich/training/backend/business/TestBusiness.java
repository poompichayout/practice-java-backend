package com.poompich.training.backend.business;

import com.poompich.training.backend.exception.BaseException;
import com.poompich.training.backend.exception.UserException;
import com.poompich.training.backend.model.MRegisterRequest;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class TestBusiness {

    public String register(MRegisterRequest request) throws BaseException {
        if (request == null) {
            throw UserException.requestNull();
        }

        if (Objects.isNull(request.getEmail())) {
            throw UserException.emailNull();
        }
        return "registered " + request;
    }

}
