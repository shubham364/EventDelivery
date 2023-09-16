package com.example.exception;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;

public class EventDeliveryException extends Exception{

    private final String errorMessage;

    private final int code;

    public EventDeliveryException(String errorMessage, int code){
        super(errorMessage);
        this.errorMessage = errorMessage;
        this.code = code;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public int getCode() {
        return code;
    }

    public static String getStackTrace(Exception ex){
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ex.printStackTrace(new PrintStream(os));
        String stackTrace = os.toString();
        return Arrays.toString(stackTrace.split("[\\n]"));
    }
}
