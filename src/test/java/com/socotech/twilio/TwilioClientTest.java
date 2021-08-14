package com.socotech.twilio;

import com.socotech.twilio.api.TwilioClient;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.ZonedDateTime;

/**
 * Created by IntelliJ IDEA.
 * User: marc
 * Date: 7/2/12
 * Time: 6:47 AM
 */
public class TwilioClientTest {

    @Mock
    TwilioClient twilioClient;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetMessageList() {
        Assert.assertFalse("No SMS messages found", this.twilioClient.getMessages(100, ZonedDateTime.now().minusDays(1)).iterator().hasNext());
    }

    @Test
    public void testIncomingPhoneNumberList() {
        Assert.assertFalse("No phone numbers found", this.twilioClient.getIncomingPhoneNumbers(100).iterator().hasNext());
    }

}
