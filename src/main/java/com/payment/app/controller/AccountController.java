package com.payment.app.controller;

import com.payment.app.dto.ResponseDto;
import com.payment.app.service.AccountService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/account")
public class AccountController {

    @Inject
    AccountService service;

    @GET
    @Path("/{accountNo}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(String accountNo) {
        return Response.ok().entity(new ResponseDto<>(service.get(accountNo))).build();
    }

}
