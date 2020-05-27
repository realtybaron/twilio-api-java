package com.socotech.twilio.web;

import com.google.common.base.Predicate;

/**
 * Created by IntelliJ IDEA. User: marc Date: Apr 21, 2011 Time: 3:33:49 PM
 */
public class UnsuccessfulCall implements Predicate<TwilioCallback> {
    @Override
    public boolean apply(TwilioCallback input) {
        return new BusyCall().or(new CanceledCall()).or(new NoAnswerCall()).or(new FailedCall()).or(new MachineAnswered()).test(input);
    }
}
