package com.socotech.twilio.api;

import com.twilio.sdk.verbs.Dial;

/**
 * Created by IntelliJ IDEA.
 * User: marc
 * Date: 11/17/12
 * Time: 7:24 AM
 */
public class DialBuilder {
    private String number;
    private String caller;
    private Method method;
    private Integer timeout = 30;
    private boolean record;

    public DialBuilder(String number) {
        this.number = number;
    }

    public DialBuilder caller(String caller) {
        this.caller = caller;
        return this;
    }

    public DialBuilder method(Method method) {
        this.method = method;
        return this;
    }

    public DialBuilder timeout(Integer timeout) {
        this.timeout = timeout;
        return this;
    }

    public DialBuilder record(boolean record) {
        this.record = record;
        return this;
    }

    public Dial build() {
        Dial verb = new Dial(number);
        verb.setMethod(method.name());
        verb.setTimeout(timeout);
        verb.set("record", String.valueOf(record));
        if (caller != null && caller.length() != 0) {
            verb.setCallerId(caller);
        }
        return verb;
    }
}
