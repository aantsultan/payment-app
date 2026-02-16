package com.payment.app.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.payment.app.dto.AccountDto;
import com.payment.app.dto.ResponseDto;
import com.payment.app.dto.TransferDto;
import com.payment.app.model.Account;
import com.payment.app.service.AccountService;
import com.payment.app.service.TransferService;
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
import java.util.UUID;

import static io.restassured.RestAssured.given;

@QuarkusTest
class TransferControllerTest {

    private static final String DB_ACCOUNT = "1001";
    private static final String CR_ACCOUNT = "1002";
    private static final BigDecimal DB_BALANCE = BigDecimal.valueOf(1_000_000);
    private static final BigDecimal CR_BALANCE = BigDecimal.valueOf(100_000);
    private static final BigDecimal AMOUNT = BigDecimal.valueOf(200_000);
    private static final BigDecimal LARGE_AMOUNT = BigDecimal.valueOf(2_000_000);

    @Inject
    AccountService accountService;

    @Inject
    TransferService transferService;

    @Inject
    ObjectMapper objectMapper;

    @BeforeEach
    void createAccount() {
        Account dbAccount = new Account();
        dbAccount.setAccountNo(DB_ACCOUNT);
        dbAccount.setBalance(DB_BALANCE);
        accountService.save(dbAccount);

        Account crAccount = new Account();
        crAccount.setAccountNo(CR_ACCOUNT);
        crAccount.setBalance(CR_BALANCE);
        accountService.save(crAccount);
    }

    /**
     * a. Saldo dapat bertambah dan berkurang.
     * b. Setiap transaksi yang berhasil, harus mengurangi saldo.
     * c. Setiap transaksi top-up atau refund akan menambah saldo
     */
    @Test
    void transfer_OK() throws Exception {
        String code = UUID.randomUUID().toString(); // Assumption, code is UUID + type transfer (top-up / refund)
        TransferDto transferDto = new TransferDto();
        transferDto.setDebitAccount(DB_ACCOUNT);
        transferDto.setCreditAccount(CR_ACCOUNT);
        transferDto.setAmount(AMOUNT);
        transferDto.setTransactionCode(code);
        String request = objectMapper.writeValueAsString(transferDto);
        Response response = given()
                .contentType(ContentType.JSON)
                .body(request)
                .post("/transfer")
                .andReturn();
        int status = response.getStatusCode();
        Assertions.assertEquals(HttpStatus.SC_OK, status);
        ResponseDto<String> responseDto = objectMapper.readValue(response.getBody().prettyPrint(), new TypeReference<>() {
        });
        Assertions.assertNull(responseDto.getError());

        // 1. Make sure Balance is change for DB Account
        response = given()
                .contentType(ContentType.JSON)
                .get("/account/{accountNo}", DB_ACCOUNT)
                .thenReturn();
        status = response.getStatusCode();
        Assertions.assertEquals(HttpStatus.SC_OK, status);
        ResponseDto<AccountDto> responseDtoAcc = objectMapper.readValue(response.getBody().prettyPrint(), new TypeReference<>() {
        });
        AccountDto data = responseDtoAcc.getData();
        Assertions.assertEquals(DB_ACCOUNT, data.getAccountNo());
        Assertions.assertEquals(0, DB_BALANCE.subtract(AMOUNT).compareTo(data.getBalance()));

        // 2. Make sure Balance is change for CR Account
        response = given()
                .contentType(ContentType.JSON)
                .get("/account/{accountNo}", CR_ACCOUNT)
                .thenReturn();
        status = response.getStatusCode();
        Assertions.assertEquals(HttpStatus.SC_OK, status);
        responseDtoAcc = objectMapper.readValue(response.getBody().prettyPrint(), new TypeReference<>() {
        });
        data = responseDtoAcc.getData();
        Assertions.assertEquals(CR_ACCOUNT, data.getAccountNo());
        Assertions.assertEquals(0, CR_BALANCE.add(AMOUNT).compareTo(data.getBalance()));
    }

    @Test
    void transfer_DbAccountNotFound() throws Exception {
        String code = UUID.randomUUID().toString();
        TransferDto transferDto = new TransferDto();
        transferDto.setDebitAccount("Salah");
        transferDto.setCreditAccount(CR_ACCOUNT);
        transferDto.setAmount(AMOUNT);
        transferDto.setTransactionCode(code);
        String request = objectMapper.writeValueAsString(transferDto);
        Response response = given()
                .contentType(ContentType.JSON)
                .body(request)
                .post("/transfer")
                .andReturn();
        int status = response.getStatusCode();
        Assertions.assertEquals(HttpStatus.SC_NOT_FOUND, status);
        ResponseDto<String> responseDto = objectMapper.readValue(response.getBody().prettyPrint(), new TypeReference<>() {
        });
        Assertions.assertNotNull(responseDto.getError());

        // Check if balance DB Account still same
        response = given()
                .contentType(ContentType.JSON)
                .get("/account/{accountNo}", DB_ACCOUNT)
                .thenReturn();
        status = response.getStatusCode();
        Assertions.assertEquals(HttpStatus.SC_OK, status);
        ResponseDto<AccountDto> responseDtoAcc = objectMapper.readValue(response.getBody().prettyPrint(), new TypeReference<>() {
        });
        AccountDto data = responseDtoAcc.getData();
        Assertions.assertEquals(DB_ACCOUNT, data.getAccountNo());
        Assertions.assertEquals(0, DB_BALANCE.compareTo(data.getBalance()));

        // Check if balance CR Account still same
        response = given()
                .contentType(ContentType.JSON)
                .get("/account/{accountNo}", CR_ACCOUNT)
                .thenReturn();
        status = response.getStatusCode();
        Assertions.assertEquals(HttpStatus.SC_OK, status);
        responseDtoAcc = objectMapper.readValue(response.getBody().prettyPrint(), new TypeReference<>() {
        });
        data = responseDtoAcc.getData();
        Assertions.assertEquals(CR_ACCOUNT, data.getAccountNo());
        Assertions.assertEquals(0, CR_BALANCE.compareTo(data.getBalance()));
    }

    @Test
    void transfer_CrAccountNotFound() throws Exception {
        String code = UUID.randomUUID().toString();
        TransferDto transferDto = new TransferDto();
        transferDto.setDebitAccount(DB_ACCOUNT);
        transferDto.setCreditAccount("Salah");
        transferDto.setAmount(AMOUNT);
        transferDto.setTransactionCode(code);
        String request = objectMapper.writeValueAsString(transferDto);
        Response response = given()
                .contentType(ContentType.JSON)
                .body(request)
                .post("/transfer")
                .andReturn();
        int status = response.getStatusCode();
        Assertions.assertEquals(HttpStatus.SC_NOT_FOUND, status);
        ResponseDto<String> responseDto = objectMapper.readValue(response.getBody().prettyPrint(), new TypeReference<>() {
        });
        Assertions.assertNotNull(responseDto.getError());

        // Check if balance DB Account still same
        response = given()
                .contentType(ContentType.JSON)
                .get("/account/{accountNo}", DB_ACCOUNT)
                .thenReturn();
        status = response.getStatusCode();
        Assertions.assertEquals(HttpStatus.SC_OK, status);
        ResponseDto<AccountDto> responseDtoAcc = objectMapper.readValue(response.getBody().prettyPrint(), new TypeReference<>() {
        });
        AccountDto data = responseDtoAcc.getData();
        Assertions.assertEquals(DB_ACCOUNT, data.getAccountNo());
        Assertions.assertEquals(0, DB_BALANCE.compareTo(data.getBalance()));

        // Check if balance CR Account still same
        response = given()
                .contentType(ContentType.JSON)
                .get("/account/{accountNo}", CR_ACCOUNT)
                .thenReturn();
        status = response.getStatusCode();
        Assertions.assertEquals(HttpStatus.SC_OK, status);
        responseDtoAcc = objectMapper.readValue(response.getBody().prettyPrint(), new TypeReference<>() {
        });
        data = responseDtoAcc.getData();
        Assertions.assertEquals(CR_ACCOUNT, data.getAccountNo());
        Assertions.assertEquals(0, CR_BALANCE.compareTo(data.getBalance()));
    }

    @AfterEach
    void deleteAccount() {
        accountService.deleteAll();
        transferService.deleteAll();
    }

}
