package com.socotech.twilio.web;

import com.google.common.base.Predicate;

import java.util.Optional;

/**
 * Created by IntelliJ IDEA. User: marc Date: Apr 21, 2011 Time: 3:33:49 PM
 */
public class HumanAnswered implements Predicate<TwilioCallback> {
    @Override
    public boolean apply(TwilioCallback input) {
        return Optional.ofNullable(input.AnsweredBy).orElse("").equals("human");
    }
}
