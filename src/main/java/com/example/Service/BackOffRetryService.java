package com.example.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BackOffRetryService {

    private final static Logger logger = LoggerFactory.getLogger(BackOffRetryService.class);

    private static final long MULTIPLIER = 2;


    public static void waitUntilNextRetry(int retryCount, String consumerId){
        long waitTime = getNextTimeOut(retryCount);
        logger.info("Waiting for {} until next retry for consumer - {} with retry count - {}", waitTime, consumerId, retryCount);
        try {
            Thread.sleep(waitTime);
        } catch (Exception ex){
            logger.error(ex.getMessage());
        }
    }

    private static long getNextTimeOut(int retryCount){
        return Math.round(Math.pow(MULTIPLIER, retryCount)) * 1000;
    }

}
