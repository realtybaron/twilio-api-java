package com.socotech.twilio.api;

import com.twilio.base.ResourceSet;
import com.twilio.rest.api.v2010.account.Call;
import com.twilio.rest.api.v2010.account.IncomingPhoneNumber;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.api.v2010.account.availablephonenumbercountry.Local;
import com.twilio.type.PhoneNumber;
import com.twilio.type.Twiml;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

/**
 * Created with IntelliJ IDEA.
 * User: marc
 * Date: 2020-05-19
 * Time: 13:36
 */
public class NoopTwilioClient implements TwilioClient {
    @Override
    public Call call(PhoneNumber ph1, PhoneNumber ph2, Twiml twiml) {
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
    public Message message(PhoneNumber to, PhoneNumber from, String body) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ResourceSet<Local> getAvailablePhoneNumbers(String areaCode, int pageSize) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ResourceSet<Message> getMessages(int pageSize, ZonedDateTime since) {
        throw new UnsupportedOperationException();
    }

    @Override
    public IncomingPhoneNumber createPhoneNumber(String number) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ResourceSet<IncomingPhoneNumber> getIncomingPhoneNumbers(int pageSize) {
        throw new UnsupportedOperationException();
    }
}
