package com.payment.app.handler;

import com.payment.app.dto.ResponseDto;
import com.payment.app.exception.NotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.jboss.resteasy.reactive.RestResponse;

@Provider
public class NotFoundExceptionHandler implements ExceptionMapper<NotFoundException> {

    @Override
    public Response toResponse(NotFoundException e) {
        return Response.status(RestResponse.Status.NOT_FOUND).entity(new ResponseDto<>(null, e.getMessage())).build();
    }
}
