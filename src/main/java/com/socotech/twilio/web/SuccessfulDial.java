package com.socotech.twilio.web;

import com.google.common.base.Predicate;

/**
 * Created by IntelliJ IDEA. User: marc Date: Apr 21, 2011 Time: 3:33:49 PM
 */
public class SuccessfulDial implements Predicate<TwilioCallback> {
  @Override
  public boolean apply(TwilioCallback input) {
    return new CompletedDial().apply(input);
    // return Predicates.and(new CompletedDial(), new HumanAnswered()).apply(input);
  }
}
