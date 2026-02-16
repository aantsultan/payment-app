package com.payment.app.controller;

import com.payment.app.dto.AccountDto;
import com.payment.app.dto.ResponseDto;
import com.payment.app.service.AccountService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/account")
public class AccountController {

    @Inject
    AccountService service;

    @GET
    @Path("/{accountNo}")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseDto<AccountDto> get(String accountNo) {
        return new ResponseDto<>(service.get(accountNo));
    }

}
