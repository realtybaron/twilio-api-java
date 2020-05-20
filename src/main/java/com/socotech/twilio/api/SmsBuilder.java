package com.socotech.twilio.api;

import com.twilio.sdk.verbs.Sms;

/**
 * Created by IntelliJ IDEA.
 * User: marc
 * Date: 11/17/12
 * Time: 7:51 AM
 */
public class SmsBuilder {

    private String to;
    private String from;
    private String message;

    public SmsBuilder(String message) {
        this.message = message;
    }

    public SmsBuilder to(String to) {
        this.to = to;
        return this;
    }

    public SmsBuilder from(String from) {
        this.from = from;
        return this;
    }

    public Sms build() {
        Sms verb = new Sms(message);
        verb.setTo(to);
        verb.setFrom(from);
        return verb;
    }

}
