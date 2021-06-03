package com.socotech.twilio.web;

import java.util.Optional;
import java.util.function.Predicate;

/**
 * Created by IntelliJ IDEA. User: marc Date: Apr 21, 2011 Time: 3:33:49 PM
 */
public abstract class CallStatus implements Predicate<TwilioCallback> {

    private final String value;

    protected CallStatus(String value) {
        this.value = value;
    }

    @Override
    public boolean test(TwilioCallback input) {
        return Optional.ofNullable(input.CallStatus).orElse("").equals(this.value);
    }
}
