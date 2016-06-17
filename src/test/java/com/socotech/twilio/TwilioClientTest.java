package com.socotech.twilio;

import com.socotech.twilio.api.DefaultTwilioClient;
import com.socotech.twilio.api.TwilioClient;
import junit.framework.Assert;
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
  protected TwilioClient twilioClient = new DefaultTwilioClient();

  @Test
  public void testGetMessageList() throws Exception {
    Map<String, String> filter = Collections.emptyMap();
    Assert.assertTrue("No SMS messages found", this.twilioClient.getMessages(filter).iterator().hasNext());
  }

  @Test
  public void testIncomingPhoneNumberList() throws Exception {
    Assert.assertTrue("No phone numbers found", this.twilioClient.getIncomingPhoneNumbers().iterator().hasNext());
  }
}
