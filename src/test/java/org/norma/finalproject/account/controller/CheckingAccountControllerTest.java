package org.norma.finalproject.account.controller;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.norma.finalproject.account.service.FacadeCheckinAccountService;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(CheckingAccountController.class)
class CheckingAccountControllerTest {

    @Mock
    private FacadeCheckinAccountService underTest;


    @InjectMocks
    private CheckingAccountController checkingAccountController;

    @Test
    public void given_when_then(){

    }


}