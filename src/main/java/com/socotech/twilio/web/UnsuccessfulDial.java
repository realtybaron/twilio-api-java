package com.socotech.twilio.web;

import com.google.common.base.Predicate;

/**
 * Created by IntelliJ IDEA. User: marc Date: Apr 21, 2011 Time: 3:33:49 PM
 */
public class UnsuccessfulDial implements Predicate<TwilioCallback> {
    @Override
    public boolean apply(TwilioCallback input) {
        return new BusyDial().or(new CanceledDial()).or(new NoAnswerDial()).or(new FailedDial()).or(new MachineAnswered()).test(input);
    }
}
