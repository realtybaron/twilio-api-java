package com.socotech.twilio.web;

import com.google.common.base.Predicate;
import org.apache.commons.lang.StringUtils;

/**
 * Created by IntelliJ IDEA. User: marc Date: Apr 21, 2011 Time: 3:33:49 PM
 */
public abstract class DialStatus implements Predicate<TwilioCallback> {
  private String value;

  protected DialStatus(String value) {
    this.value = value;
  }

  @Override
  public boolean apply(TwilioCallback input) {
    return StringUtils.defaultString(input.DialCallStatus).equalsIgnoreCase(this.value);
  }
}
