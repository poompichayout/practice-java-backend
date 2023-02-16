package com.poompich.training.backend;

import com.poompich.training.backend.business.EmailBusiness;
import com.poompich.training.backend.exception.BaseException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TestEmailBusiness {

    @Autowired
    private EmailBusiness emailBusiness;

    @Order(1)
    @Test
    void testSendActivateEmail() throws BaseException {
        emailBusiness.sendActivateUserEmail(
                TestData.email,
                TestData.name,
                TestData.token
        );
    }

    interface TestData {
        String email = "poompichayout@gmail.com";

        String name = "Poompichayout Kongpiam";

        String token = "MOCK_LINK";
    }

}
