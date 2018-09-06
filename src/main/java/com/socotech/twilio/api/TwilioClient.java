package com.socotech.twilio.api;

import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.instance.Call;
import com.twilio.sdk.resource.instance.IncomingPhoneNumber;
import com.twilio.sdk.resource.instance.Message;
import com.twilio.sdk.resource.list.AvailablePhoneNumberList;
import com.twilio.sdk.resource.list.IncomingPhoneNumberList;
import com.twilio.sdk.resource.list.MessageList;
import org.apache.http.NameValuePair;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: marc
 * Date: 11/27/13
 * Time: 1:55 PM
 */
public interface TwilioClient {

    Call call(Map<String, String> params) throws TwilioRestException;

    String getOrProvisionNumber(String areaCode);

    String getOrProvisionNumber(String areaCode, BigDecimal lat, BigDecimal lon);

    String getOrProvisionNumber(String areaCode, String postalCode, BigDecimal lat, BigDecimal lon);

    Message message(List<NameValuePair> params) throws TwilioRestException;

    MessageList getMessages(Map<String, String> filters);

    IncomingPhoneNumber createPhoneNumber(Map<String, String> params) throws TwilioRestException;

    IncomingPhoneNumberList getIncomingPhoneNumbers();

    AvailablePhoneNumberList getAvailablePhoneNumbers(Map<String, String> vars);
}
