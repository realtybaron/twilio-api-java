package com.socotech.twilio;

import com.socotech.twilio.api.DefaultTwilioClient;
import com.socotech.twilio.api.TwilioClient;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: marc
 * Date: 7/2/12
 * Time: 6:47 AM
 */
public class TwilioClientTest {
    private TwilioClient twilioClient = new DefaultTwilioClient("AC62bce54e61297a05c41339a4114ae9ed", "f2fdd2ea2927a82ec3bdad91c01816e5");

    @Test
    public void testGetMessageList() {
        Map<String, String> filter = Collections.emptyMap();
        Assert.assertFalse("No SMS messages found", this.twilioClient.getMessages(filter).iterator().hasNext());
    }

    @Test
    public void testIncomingPhoneNumberList() {
        Assert.assertFalse("No phone numbers found", this.twilioClient.getIncomingPhoneNumbers().iterator().hasNext());
    }
}
