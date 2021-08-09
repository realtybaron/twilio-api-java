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
 * Date: 11/27/13
 * Time: 1:55 PM
 */
public interface TwilioClient {

    Call call(PhoneNumber ph1, PhoneNumber ph2, Twiml twiml);

    String getOrProvisionNumber(String areaCode);

    String getOrProvisionNumber(String areaCode, BigDecimal lat, BigDecimal lon);

    String getOrProvisionNumber(String areaCode, String postalCode, BigDecimal lat, BigDecimal lon);

    Message message(PhoneNumber to, PhoneNumber from, String body);

    ResourceSet<Local> getAvailablePhoneNumbers(String areaCode, int pageSize);

    ResourceSet<Message> getMessages(int pageSize, ZonedDateTime since);

    IncomingPhoneNumber createPhoneNumber(String number);

    ResourceSet<IncomingPhoneNumber> getIncomingPhoneNumbers(int pageSize);

}
