package com.socotech.twilio.web;

import com.google.common.base.Predicate;
import org.apache.commons.lang.StringUtils;

/**
 * Created by IntelliJ IDEA. User: marc Date: Apr 21, 2011 Time: 3:33:49 PM
 */
public class MachineAnswered implements Predicate<TwilioCallback> {
  @Override
  public boolean apply(TwilioCallback input) {
    return StringUtils.defaultString(input.AnsweredBy).equals("machine");
  }
}
