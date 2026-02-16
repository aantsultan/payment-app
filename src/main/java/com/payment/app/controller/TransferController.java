package com.payment.app.controller;

import com.payment.app.dto.ResponseDto;
import com.payment.app.dto.TransferDto;
import com.payment.app.service.TransferService;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/transfer")
public class TransferController {

    private final TransferService service;

    public TransferController(TransferService service) {
        this.service = service;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response post(TransferDto transferDto) {
        return Response.ok().entity(new ResponseDto<>(service.post(transferDto))).build();
    }

}
