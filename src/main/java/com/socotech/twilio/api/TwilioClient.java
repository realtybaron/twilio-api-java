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

  public Call call(Map<String, String> params) throws TwilioRestException;

  public String getOrProvisionNumber(String areaCode);

  public String getOrProvisionNumber(String areaCode, BigDecimal lat, BigDecimal lon);

  public String getOrProvisionNumber(String areaCode, String postalCode, BigDecimal lat, BigDecimal lon);

  public Message message(List<NameValuePair> params) throws TwilioRestException;

  public MessageList getMessages(Map<String, String> filters);

  public IncomingPhoneNumber createPhoneNumber(Map<String, String> params) throws TwilioRestException;

  public IncomingPhoneNumberList getIncomingPhoneNumbers();

  public AvailablePhoneNumberList getAvailablePhoneNumbers(Map<String, String> vars);
}
