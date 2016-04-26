package com.socotech.twilio.web;

import com.google.common.collect.Maps;
import com.realtybaron.twilio.TwilioClient;
import com.realtybaron.user.TelephoneImpl;
import com.realtybaron.user.UserImpl;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.verbs.TwiMLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Map;

/**
 * Created by IntelliJ IDEA. User: marc Date: Apr 18, 2011 Time: 2:40:20 PM
 */
@Component("auctionDialer")
public class TwilioAuctionDialer implements AuctionDialer {
  @Autowired
  public AuctionDAO auctionDAO;
  @Autowired
  public TwilioClient twilioClient;
  @Autowired
  public MessageSource messageSource;
  @Autowired
  public AuctionService auctionService;

  @Override
  public void call(AuctionImpl auction) throws TwilioRestException, TwiMLException {
    BidImpl bid = auction.getBestBid();
    if (bid != null) {
      UserImpl user = bid.getUser();
      TelephoneImpl phone = user.findTelephone(auction.getTimeZone());
      if (phone == null) {
        log.debug("No phone found for " + user.getCanonicalName() + ". Deleting bid.");
        // delete bid...
        if (auction.removeBid(bid)) {
          this.auctionDAO.deleteBid(bid);
        }
        // ...and redirect again
        this.call(auction);
      } else {
        log.debug("Phone found for " + user.getCanonicalName() + ". Calling now...");
        Map<String, String> vars = Maps.newHashMap();
        vars.put("To", "+1" + phone.getAreaCode() + phone.getPrefix() + phone.getNumber());
        vars.put("Url", messageSource.getMessage(APIMessageKey.twilio_lead_offer_url.name(), new String[]{bid.getId().toString()}, Locale.getDefault()));
        vars.put("From", twilioClient.getOrProvisionNumber(phone.getAreaCode(), auction.getPostals().iterator().next().getCode(), auction.getLatitude(), auction.getLongitude()));
        vars.put("Method", "GET");
        vars.put("Timeout", AuctionDialer.CALL_TIMEOUT.toString());
        // vars.put("IfMachine", "Hangup");
        vars.put("StatusCallback", this.messageSource.getMessage(APIMessageKey.twilio_callstatus_recipient_url.name(), new String[]{bid.getId().toString()}, Locale.getDefault()));
        vars.put("StatusCallbackMethod", "GET");
        this.twilioClient.call(vars);
      }
    } else if (auction.getAuctionStatus() == AuctionStatus.opening) {
      log.debug("No best bid found. Opening auction...");
      // ...open auction for bidding...
      this.auctionService.doOpenAuction(auction);
      // ...and trigger default inquiry
      this.auctionService.doDefaultInquiry(auction);
    }
  }

  @Override
  public void recall(BidImpl bid) throws TwilioRestException, TwiMLException {
    AuctionImpl auction = bid.getAuction();
    TelephoneImpl phone = bid.getUser().findTelephone(auction.getTimeZone());
    Map<String, String> vars = Maps.newHashMap();
    vars.put("To", "+1" + phone.getAreaCode() + phone.getPrefix() + phone.getNumber());
    vars.put("Url", messageSource.getMessage(APIMessageKey.twilio_lead_purchase_url.name(), new String[]{bid.getId().toString()}, Locale.getDefault()));
    vars.put("From", twilioClient.getOrProvisionNumber(phone.getAreaCode(), auction.getPostals().iterator().next().getCode(), auction.getLatitude(), auction.getLongitude()));
    vars.put("Method", "GET");
    vars.put("Timeout", AuctionDialer.CALL_TIMEOUT.toString());
    // vars.put("IfMachine", "Hangup");
    vars.put("StatusCallback", this.messageSource.getMessage(APIMessageKey.twilio_callstatus_recipient_url.name(), new String[]{bid.getId().toString()}, Locale.getDefault()));
    vars.put("StatusCallbackMethod", "GET");
    this.twilioClient.call(vars);
  }

  /**
   * log4j
   */
  private static final Logger log = LoggerFactory.getLogger(TwilioAuctionDialer.class);
}
