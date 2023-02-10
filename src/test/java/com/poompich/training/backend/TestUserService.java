package com.poompich.training.backend;

import com.poompich.training.backend.entity.Address;
import com.poompich.training.backend.entity.Social;
import com.poompich.training.backend.entity.User;
import com.poompich.training.backend.exception.BaseException;
import com.poompich.training.backend.service.AddressService;
import com.poompich.training.backend.service.SocialService;
import com.poompich.training.backend.service.UserService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TestUserService {

    @Autowired
    private UserService userService;

    @Autowired
    private SocialService socialService;

    @Autowired
    private AddressService addressService;

    @Order(1)
    @Test
    void testCreate() throws BaseException {
        User user = userService.create(
                TestCreateData.email,
                TestCreateData.password,
                TestCreateData.name);
        Assertions.assertNotNull(user);
        Assertions.assertNotNull(user.getId());

        Assertions.assertEquals(TestCreateData.email, user.getEmail());
        boolean isMatched = userService.matchPassword(TestCreateData.password, user.getPassword());
        Assertions.assertTrue(isMatched);
        Assertions.assertEquals(TestCreateData.name, user.getName());
    }

    @Order(2)
    @Test
    void testUpdate() throws BaseException {
        Optional<User> opt = userService.findByEmail(TestCreateData.email);
        Assertions.assertTrue(opt.isPresent());

        User user = opt.get();
        User updatedUser = userService.updateName(user.getId(), TestUpdateData.name);

        Assertions.assertNotNull(updatedUser);
        Assertions.assertEquals(TestUpdateData.name, updatedUser.getName());
    }

    @Order(3)
    @Test
    void testCreateSocial() throws BaseException {
        Optional<User> opt = userService.findByEmail(TestCreateData.email);
        Assertions.assertTrue(opt.isPresent());

        User user = opt.get();

        Social social1 = user.getSocial();
        Assertions.assertNull(social1);

        Social social2 = socialService.create(
                user,
                SocialTestCreateData.facebook,
                SocialTestCreateData.line,
                SocialTestCreateData.instagram,
                SocialTestCreateData.tiktok
        );

        Assertions.assertNotNull(social2);
        Assertions.assertEquals(SocialTestCreateData.facebook, social2.getFacebook());
    }

    @Order(4)
    @Test
    void testCreateAddress() throws BaseException {
        Optional<User> opt = userService.findByEmail(TestCreateData.email);
        Assertions.assertTrue(opt.isPresent());

        User user = opt.get();
        List<Address> addresses = user.getAddresses();
        Assertions.assertTrue(addresses.isEmpty());

        createAddress(
                user,
                AddressTestCreateData.line1,
                AddressTestCreateData.line2,
                AddressTestCreateData.zipCode
        );
        createAddress(
                user,
                AddressTestCreateData.line1,
                AddressTestCreateData.line2,
                AddressTestCreateData.zipCode
        );
    }

    private void createAddress(User user, String line1, String line2, String zipCode) {
        Address address = addressService.create(
                user,
                line1,
                line2,
                zipCode
        );

        Assertions.assertNotNull(address);
        Assertions.assertEquals(line1, address.getLine1());
        Assertions.assertEquals(line2, address.getLine2());
        Assertions.assertEquals(zipCode, address.getZipCode());
    }

    @Order(9)
    @Test
    void testDelete() throws BaseException {
        Optional<User> opt = userService.findByEmail(TestCreateData.email);
        Assertions.assertTrue(opt.isPresent());

        User user = opt.get();
        userService.deleteById(user.getId());

        // check social
        Social social = user.getSocial();
        Assertions.assertNotNull(social);
        Assertions.assertEquals(SocialTestCreateData.facebook, social.getFacebook());

        // check address
        List<Address> addresses = user.getAddresses();
        Assertions.assertFalse(addresses.isEmpty());
        Assertions.assertEquals(2., addresses.size());

        Optional<User> optDelete = userService.findByEmail(TestCreateData.email);
        Assertions.assertTrue(optDelete.isEmpty());
    }

    interface TestCreateData {
        String email = "poom@test.com";

        String password = "mE123EeGGEZ";

        String name = "Poompichayout Kongpiam";
    }

    interface SocialTestCreateData {
        String facebook = "poompichayout";

        String line = "";

        String instagram = "";

        String tiktok = "";
    }

    interface AddressTestCreateData {
        String line1 = "123/4";

        String line2 = "Muang";

        String zipCode = "12120";
    }

    interface TestUpdateData {
        String name = "AMPO";
    }

}
