package com.socotech.twilio.api;

import com.twilio.sdk.verbs.Redirect;

/**
 * Created by IntelliJ IDEA.
 * User: marc
 * Date: 11/17/12
 * Time: 8:04 AM
 */
public class RedirectBuilder {

    private final String url;
    private Method method = Method.POST;

    public RedirectBuilder(String url) {
        this.url = url;
    }

    public RedirectBuilder method(Method method) {
        this.method = method;
        return this;
    }

    public Redirect build() {
        Redirect verb = new Redirect(url);
        verb.setMethod(method.name());
        return verb;
    }
}
