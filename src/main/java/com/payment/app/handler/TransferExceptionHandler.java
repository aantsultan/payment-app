package com.payment.app.handler;

import com.payment.app.dto.ResponseDto;
import com.payment.app.exception.TransferException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.jboss.resteasy.reactive.RestResponse;

@Provider
public class TransferExceptionHandler implements ExceptionMapper<TransferException> {

    @Override
    public Response toResponse(TransferException e) {
        return Response.status(RestResponse.Status.BAD_REQUEST).entity(new ResponseDto<>(null, e.getMessage())).build();
    }
}
