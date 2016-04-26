package com.socotech.twilio.web;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

/**
 * Created by IntelliJ IDEA. User: marc Date: Apr 21, 2011 Time: 3:33:49 PM
 */
public class UnsuccessfulDial implements Predicate<TwilioCallback> {
  @Override
  public boolean apply(TwilioCallback input) {
    return Predicates.or(new BusyDial(), new CanceledDial(), new NoAnswerDial(), new FailedDial(), new MachineAnswered()).apply(input);
  }
}
