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
 * Date: 2020-05-19
 * Time: 13:36
 */
public class NoopTwilioClient implements TwilioClient {
    @Override
    public Call call(Map<String, String> params) throws TwilioRestException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getOrProvisionNumber(String areaCode) {
        return "18175555555";
    }

    @Override
    public String getOrProvisionNumber(String areaCode, BigDecimal lat, BigDecimal lon) {
        return "18175555555";
    }

    @Override
    public String getOrProvisionNumber(String areaCode, String postalCode, BigDecimal lat, BigDecimal lon) {
        return "18175555555";
    }

    @Override
    public Message message(List<NameValuePair> params) throws TwilioRestException {
        throw new UnsupportedOperationException();
    }

    @Override
    public MessageList getMessages(Map<String, String> filters) {
        throw new UnsupportedOperationException();
    }

    @Override
    public IncomingPhoneNumber createPhoneNumber(Map<String, String> params) throws TwilioRestException {
        throw new UnsupportedOperationException();
    }

    @Override
    public IncomingPhoneNumberList getIncomingPhoneNumbers() {
        throw new UnsupportedOperationException();
    }

    @Override
    public AvailablePhoneNumberList getAvailablePhoneNumbers(Map<String, String> vars) {
        throw new UnsupportedOperationException();
    }
}
