package com.socotech.twilio.web;

import com.google.common.base.MoreObjects;
import com.google.common.base.Predicate;

/**
 * Created by IntelliJ IDEA. User: marc Date: Apr 21, 2011 Time: 3:33:49 PM
 */
public abstract class CallStatus implements Predicate<TwilioCallback> {
  private String value;

  protected CallStatus(String value) {
    this.value = value;
  }

  @Override
  public boolean apply(TwilioCallback input) {
    return MoreObjects.firstNonNull(input.CallStatus, "").equals(this.value);
  }
}
