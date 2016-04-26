package com.socotech.twilio.api;

import com.twilio.sdk.verbs.Gather;

/**
 * Created by IntelliJ IDEA.
 * User: marc
 * Date: 11/17/12
 * Time: 7:10 AM
 */
public class GatherBuilder {
  private int numDigits;

  public GatherBuilder numDigits(int i) {
    this.numDigits = i;
    return this;
  }

  public Gather build() {
    Gather verb = new Gather();
    verb.setNumDigits(this.numDigits);
    return verb;
  }
}
