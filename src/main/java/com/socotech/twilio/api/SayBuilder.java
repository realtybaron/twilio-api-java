package com.socotech.twilio.api;

import com.twilio.sdk.verbs.Say;

/**
 * Created by IntelliJ IDEA.
 * User: marc
 * Date: 11/17/12
 * Time: 7:20 AM
 */
public class SayBuilder {

    private Voice voice;
    private String body;

    public SayBuilder(String body) {
        this.body = body;
    }

    public SayBuilder voice(Voice voice) {
        this.voice = voice;
        return this;
    }

    public Say build() {
        Say verb = new Say(body);
        verb.setVoice(voice.name());
        return verb;
    }

}
