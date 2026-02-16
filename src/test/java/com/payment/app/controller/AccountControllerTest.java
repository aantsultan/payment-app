package com.payment.app.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.payment.app.dto.AccountDto;
import com.payment.app.dto.ResponseDto;
import com.payment.app.model.Account;
import com.payment.app.service.AccountService;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import jakarta.inject.Inject;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class AccountControllerTest {

    private static final String DB_ACCOUNT = "1001";
    private static final BigDecimal DB_BALANCE = new BigDecimal("1000000.00");

    @Inject
    AccountService accountService;

    @Inject
    ObjectMapper objectMapper;

    @BeforeEach
    void before() {
        Account dbAccount = new Account();
        dbAccount.setAccountNo(DB_ACCOUNT);
        dbAccount.setBalance(DB_BALANCE);
        accountService.save(dbAccount);
    }

    @AfterEach
    void after() {
        accountService.deleteAll();
    }

    @Test
    void get_OK() throws JsonProcessingException {
        Response response = given()
                .contentType(ContentType.JSON)
                .get("/account/{accountNo}", DB_ACCOUNT)
                .thenReturn();
        Assertions.assertEquals(HttpStatus.SC_OK, response.getStatusCode());
        String contentAsString = response.prettyPrint();
        ResponseDto<AccountDto> responseDto = objectMapper.readValue(contentAsString, new TypeReference<>() {
        });
        Assertions.assertEquals(DB_ACCOUNT, responseDto.getData().getAccountNo());
        Assertions.assertEquals(0, responseDto.getData().getBalance().compareTo(DB_BALANCE));
    }
}
