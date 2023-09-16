package com.example.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import java.util.HashMap;
import java.util.Map;

import static com.example.model.Constants.*;

public class EventDeliveryExceptionMapper implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception ex) {
        if (ex instanceof EventDeliveryException){
            EventDeliveryException ede = (EventDeliveryException) ex;
            Map<String, Object> exceptionMap = new HashMap<>();
            exceptionMap.put(ERROR_MESSAGE, ede.getErrorMessage());
            exceptionMap.put(CODE, ede.getCode());
            return Response.status(ede.getCode()).entity(exceptionMap).build();
        }
        else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
}
