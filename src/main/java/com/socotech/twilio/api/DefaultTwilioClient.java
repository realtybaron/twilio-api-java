package com.socotech.twilio.api;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Maps;
import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.instance.AvailablePhoneNumber;
import com.twilio.sdk.resource.instance.Call;
import com.twilio.sdk.resource.instance.IncomingPhoneNumber;
import com.twilio.sdk.resource.instance.Message;
import com.twilio.sdk.resource.list.AvailablePhoneNumberList;
import com.twilio.sdk.resource.list.IncomingPhoneNumberList;
import com.twilio.sdk.resource.list.MessageList;
import org.apache.http.NameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: marc
 * Date: 11/27/13
 * Time: 1:56 PM
 */
public class DefaultTwilioClient implements TwilioClient {
  private TwilioRestClient restClient;
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
    restClient = new TwilioRestClient(accountSid, authToken);
  }

  /**
   * Initialize this object
   */
  public void init() {
    // build area code cache
    CacheLoader<String, String> loader = new CacheLoader<String, String>() {
      @Override
      public String load(String areaCode) throws Exception {
        // check for existing incoming with exact area code
        Map<String, String> params = Collections.singletonMap("PhoneNumber", "+1" + areaCode);
        Iterator<IncomingPhoneNumber> incomings = restClient.getAccount().getIncomingPhoneNumbers(params).iterator();
        if (incomings.hasNext()) {
          return incomings.next().getPhoneNumber();
        }
        // check for existing incoming with "friendly" name as area code
        Map<String, String> params2 = Collections.singletonMap("FriendlyName", areaCode);
        Iterator<IncomingPhoneNumber> incomings2 = restClient.getAccount().getIncomingPhoneNumbers(params2).iterator();
        if (incomings2.hasNext()) {
          return incomings2.next().getPhoneNumber();
        }
        // provision a new incoming number
        String number = null;
        Iterator<AvailablePhoneNumber> availables = findAvailablesUsingAreaCode(areaCode).iterator();
        while (number == null && availables.hasNext()) {
          number = provisionIncomingNumber(areaCode, availables.next().getPhoneNumber());
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
  }

  @Override
  public Call call(Map<String, String> params) throws TwilioRestException {
    return restClient.getAccount().getCallFactory().create(params);
  }

  @Override
  public Message message(List<NameValuePair> params) throws TwilioRestException {
    return restClient.getAccount().getMessageFactory().create(params);
  }

  @Override
  public MessageList getMessages(Map<String, String> filters) {
    return restClient.getAccount().getMessages(filters);
  }

  @Override
  public IncomingPhoneNumber createPhoneNumber(Map<String, String> params) throws TwilioRestException {
    return this.restClient.getAccount().getIncomingPhoneNumberFactory().create(params);
  }

  @Override
  public IncomingPhoneNumberList getIncomingPhoneNumbers() {
    return restClient.getAccount().getIncomingPhoneNumbers();
  }

  @Override
  public AvailablePhoneNumberList getAvailablePhoneNumbers(Map<String, String> vars) {
    return this.restClient.getAccount().getAvailablePhoneNumbers(vars);
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
      Iterator<AvailablePhoneNumber> list = this.findAvailablesUsingGeoLocation(lat, lon).iterator();
      if (list.hasNext()) {
        return this.provisionIncomingNumber(areaCode, list.next().getPhoneNumber());
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
      Iterator<AvailablePhoneNumber> list = this.findAvailablesUsingPostalCode(postalCode).iterator();
      if (list.hasNext()) {
        return this.provisionIncomingNumber(areaCode, list.next().getPhoneNumber());
      } else {
        // provision a number using lat/lon
        list = this.findAvailablesUsingGeoLocation(lat, lon).iterator();
        if (list.hasNext()) {
          return this.provisionIncomingNumber(areaCode, list.next().getPhoneNumber());
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

  private String provisionIncomingNumber(String areaCode, String number) {
    try {
      Map<String, String> vars = newProvisioningParams();
      vars.put("PhoneNumber", number);
      vars.put("FriendlyName", areaCode);
      return restClient.getAccount().getIncomingPhoneNumberFactory().create(vars).getPhoneNumber();
    } catch (TwilioRestException e) {
      log.error(e.getMessage(), e);
    }
    return null; // fail :(
  }

  private Iterable<AvailablePhoneNumber> findAvailablesUsingAreaCode(String areaCode) {
    Map<String, String> vars = Collections.singletonMap("AreaCode", areaCode);
    return restClient.getAccount().getAvailablePhoneNumbers(vars);
  }

  private Iterable<AvailablePhoneNumber> findAvailablesUsingPostalCode(String postalCode) {
    Map<String, String> vars = Collections.singletonMap("InPostalCode", postalCode);
    return restClient.getAccount().getAvailablePhoneNumbers(vars);
  }

  private Iterable<AvailablePhoneNumber> findAvailablesUsingGeoLocation(BigDecimal lat, BigDecimal lon) {
    Map<String, String> vars = Collections.singletonMap("NearLatLong", lat.toString() + "," + lon.toString());
    return restClient.getAccount().getAvailablePhoneNumbers(vars);
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
