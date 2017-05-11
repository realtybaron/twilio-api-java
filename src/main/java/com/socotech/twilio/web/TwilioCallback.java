package com.socotech.twilio.web;

import com.google.common.base.MoreObjects;

/**
 * Created by IntelliJ IDEA. User: marc Date: Apr 21, 2011 Time: 12:34:44 PM
 */
public class TwilioCallback {
    public String To;
    public String From;
    public String CallSid;
    public String CallStatus;
    public String AccountSid;
    public String AnsweredBy;
    public String RecordingUrl;
    public String DialCallStatus;
    public Integer Digits;
    public Integer CallDuration;
    public Integer DialCallDuration;

    public static final Integer TIMEOUT = -1;

    @Override
    public String toString() {
        MoreObjects.ToStringHelper helper = MoreObjects.toStringHelper(this);
        helper.add("To", this.To).add("From", this.From).add("CallSid", this.CallSid).add("CallStatus", this.CallStatus).add("DialCallStatus", this.DialCallStatus);
        return helper.toString();
    }
}
