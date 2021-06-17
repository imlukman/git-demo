package com.boot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
 
@Service
public class SampleRetryService {
 
    private static final Logger LOGGER = LoggerFactory.getLogger(SampleRetryService.class);
 
    int counter = 1;
 
    // To indicate any method to be a candidate of retry.
    // 'maxAttempts' attribute tells that how many times we would need to retry the 3rd party service to fetch the details.
    // 'value' attribute tells the type of exceptions (e.g. TimeoutException, IOException, etc.) that we can happen when retry takes place.
    // 'backoff' attribute tells to create a gap between the retries.
    @Retryable(maxAttempts = 3, value = { RuntimeException.class }, backoff = @Backoff(delay = 2000, multiplier = 2))
    public String invoke(final String ssn) {
        // For simplicity we are returning the user-input. In ideal scenario it will call the 
        // social-security service to fetch the details.
        // return ssn;      // As we are showing the retry mechanism in this tutorial, we will comment this to assume that the 3rd-party service is down.
 
 
        // So to perform this retry mechanism we'll throw some dummy exception
        // (considering the 3rd-party service is down and throwing an exception).
        LOGGER.info("Executed counter= {}.", counter);
        // This counter will help us to understand that after 3 retry attempts the fallback method would be called.
        counter++;
        throw new RuntimeException("Some random Exception");
    }
 
    // To specify the fallback method.
    // The exception in this method should match the exception defined in the @Retryable annotation.
    @Recover
    public String recover(final RuntimeException e, final String ssn) {
        LOGGER.info("Sending the fall-back method response to the user as the number of max-attempts for the "
                + "3rd-party service has been reached.");
        return "Not able to connect to the social security details portal at this time.";
    }
    
    @Retryable(maxAttempts = 3, value = { Exception.class }, backoff = @Backoff(delay = 2000, multiplier = 2))
     public void retryMethod() throws Exception{
    	LOGGER.info("retryMehtod");
    	throw new Exception();
    }
    @Recover
    public void recoverMethod(Exception e) {
    	LOGGER.info("recovering {}",e.getMessage());
    }
}
