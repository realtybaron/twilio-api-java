package com.socotech.twilio;

import com.socotech.twilio.api.DefaultTwilioClient;
import com.socotech.twilio.api.TwilioClient;
import org.junit.Assert;
import org.junit.Test;

import java.time.ZonedDateTime;

/**
 * Created by IntelliJ IDEA.
 * User: marc
 * Date: 7/2/12
 * Time: 6:47 AM
 */
public class TwilioClientTest {

    private final TwilioClient twilioClient = new DefaultTwilioClient("AC62bce54e61297a05c41339a4114ae9ed", "f2fdd2ea2927a82ec3bdad91c01816e5");

    @Test
    public void testGetMessageList() {
        Assert.assertFalse("No SMS messages found", this.twilioClient.getMessages(100, ZonedDateTime.now().minusDays(1)).iterator().hasNext());
    }

    @Test
    public void testIncomingPhoneNumberList() {
        Assert.assertFalse("No phone numbers found", this.twilioClient.getIncomingPhoneNumbers(100).iterator().hasNext());
    }

}
