package com.socotech.twilio.api;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Maps;
import com.twilio.Twilio;
import com.twilio.base.ResourceSet;
import com.twilio.rest.api.v2010.account.Call;
import com.twilio.rest.api.v2010.account.IncomingPhoneNumber;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.api.v2010.account.availablephonenumbercountry.Local;
import com.twilio.type.PhoneNumber;
import com.twilio.type.Twiml;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: marc
 * Date: 11/27/13
 * Time: 1:56 PM
 */
public class DefaultTwilioClient implements TwilioClient {

    /**
     * Cache provisioned phone numbers
     */
    private LoadingCache<String, String> areaCodeCache;

    /**
     * Alternative constructor
     *
     * @param accountSid account SID
     * @param authToken  auth token
     */
    public DefaultTwilioClient(String accountSid, String authToken) {
        Twilio.init(accountSid, authToken);
    }

    /**
     * Initialize this object
     */
    public DefaultTwilioClient init() {
        // build area code cache
        CacheLoader<String, String> loader = new CacheLoader<String, String>() {
            @Override
            public String load(String areaCode) throws Exception {
                // check for existing incoming with exact area code
                ResourceSet<IncomingPhoneNumber> incomings = IncomingPhoneNumber.reader().setPhoneNumber(areaCode).limit(100).read();
                for (IncomingPhoneNumber incoming : incomings) {
                    if (incoming.getPhoneNumber().getEndpoint().startsWith("+1" + areaCode)) {
                        return incoming.getPhoneNumber().getEndpoint();
                    } else if (incoming.getFriendlyName().equals(areaCode)) {
                        return incoming.getPhoneNumber().getEndpoint();
                    }
                }
                // provision a new incoming number
                String number = null;
                Iterator<Local> availables = findAvailablesUsingAreaCode(Integer.parseInt(areaCode)).iterator();
                while (number == null && availables.hasNext()) {
                    number = provisionIncomingNumber(Integer.parseInt(areaCode), availables.next().getPhoneNumber().getEndpoint());
                }
                // validate number
                if (number == null) {
                    throw new Exception("Unable to load value for key: " + areaCode);
                }
                // return number
                return number;
            }
        };
        areaCodeCache = CacheBuilder.newBuilder().maximumSize(100).expireAfterAccess(1, TimeUnit.MINUTES).build(loader);
        return this;
    }

    @Override
    public Call call(PhoneNumber to, PhoneNumber from, Twiml twiml) {
        return Call.creator(to, from, twiml).create();
    }

    @Override
    public Message message(PhoneNumber to, PhoneNumber from, String body) {
        return Message.creator(to, from, body).create();
    }

    @Override
    public ResourceSet<Local> getAvailablePhoneNumbers(String areaCode, int pageSize) {
        ResourceSet<Local> reader = Local.reader("US").setAreaCode(Integer.parseInt(areaCode)).pageSize(pageSize).read();
        reader.setAutoPaging(true);
        return reader;
    }

    @Override
    public ResourceSet<Message> getMessages(int pageSize, ZonedDateTime since) {
        ResourceSet<Message> reader = Message.reader("US").setDateSentAfter(since).pageSize(pageSize).read();
        reader.setAutoPaging(true);
        return reader;
    }

    @Override
    public IncomingPhoneNumber createPhoneNumber(String number) {
        return IncomingPhoneNumber.creator(new PhoneNumber(number)).create();
    }

    @Override
    public ResourceSet<IncomingPhoneNumber> getIncomingPhoneNumbers(int pageSize) {
        ResourceSet<IncomingPhoneNumber> read = IncomingPhoneNumber.reader().pageSize(pageSize).read();
        read.setAutoPaging(true);
        return read;
    }

    @Override
    public String getOrProvisionNumber(String areaCode) {
        return this.getIncomingByAreaCode(areaCode);
    }

    @Override
    public String getOrProvisionNumber(String areaCode, BigDecimal lat, BigDecimal lon) {
        String number = this.getIncomingByAreaCode(areaCode);
        // got an area code number?
        if (number == null) {
            // provision a number using lat/lon
            Iterator<Local> list = this.findAvailablesUsingGeoLocation(lat, lon).iterator();
            if (list.hasNext()) {
                return this.provisionIncomingNumber(Integer.parseInt(areaCode), list.next().getPhoneNumber().getEndpoint());
            }
        }
        return number;
    }

    @Override
    public String getOrProvisionNumber(String areaCode, String postalCode, BigDecimal lat, BigDecimal lon) {
        String number = this.getIncomingByAreaCode(areaCode);
        // got an area code number?
        if (number == null) {
            // provision a number using postal code
            Iterator<Local> list = this.findAvailablesUsingPostalCode(postalCode).iterator();
            if (list.hasNext()) {
                return this.provisionIncomingNumber(Integer.parseInt(areaCode), list.next().getPhoneNumber().getEndpoint());
            } else {
                // provision a number using lat/lon
                list = this.findAvailablesUsingGeoLocation(lat, lon).iterator();
                if (list.hasNext()) {
                    return this.provisionIncomingNumber(Integer.parseInt(areaCode), list.next().getPhoneNumber().getEndpoint());
                } else {
                    // provision a number using 817 area code
                    number = this.getIncomingByAreaCode("817");
                }
            }
        }
        return number;
    }

    private String getIncomingByAreaCode(String areaCode) {
        String number = null;
        try {
            number = areaCodeCache.get(areaCode);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return number;
    }

    private String provisionIncomingNumber(Integer areaCode, String number) {
        return IncomingPhoneNumber.creator(new PhoneNumber(number)).setFriendlyName(Integer.toString(areaCode)).create().getPhoneNumber().getEndpoint();
    }

    private Iterable<Local> findAvailablesUsingAreaCode(int areaCode) {
        return Local.reader("US").setAreaCode(areaCode).limit(20).read();
    }

    private Iterable<Local> findAvailablesUsingPostalCode(String postalCode) {
        return Local.reader("US").setInPostalCode(postalCode).limit(20).read();
    }

    private Iterable<Local> findAvailablesUsingGeoLocation(BigDecimal lat, BigDecimal lon) {
        return Local.reader("US").setNearLatLong(String.join(",", lat.toPlainString(), lon.toPlainString())).limit(20).read();
    }

    private Map<String, String> newProvisioningParams() {
        Map<String, String> vars = Maps.newHashMap();
        vars.put("VoiceUrl", "http://twimlbin.com/external/25c03e2c50357eab");
        return vars;
    }

    /**
     * log4j
     */
    private static final Logger log = LoggerFactory.getLogger(DefaultTwilioClient.class);
}
