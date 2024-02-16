package com.probase.reservationticketingwebservice.services;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.persistence.Column;
import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONException;
import org.json.JSONObject;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.probase.reservationticketingwebservice.authenticator.CardFunction;
import com.probase.reservationticketingwebservice.authenticator.HttpHeaderNames;
import com.probase.reservationticketingwebservice.enumerations.VehicleStatus;
import com.probase.reservationticketingwebservice.enumerations.VehicleType;
import com.probase.reservationticketingwebservice.enumerations.CardStatus;
import com.probase.reservationticketingwebservice.enumerations.CardType;
import com.probase.reservationticketingwebservice.enumerations.Channel;
import com.probase.reservationticketingwebservice.enumerations.CustomerStatus;
import com.probase.reservationticketingwebservice.enumerations.DeviceStatus;
import com.probase.reservationticketingwebservice.enumerations.Gender;
import com.probase.reservationticketingwebservice.enumerations.RoleType;
import com.probase.reservationticketingwebservice.enumerations.ServiceType;
import com.probase.reservationticketingwebservice.enumerations.TransactionStatus;
import com.probase.reservationticketingwebservice.enumerations.UserStatus;
import com.probase.reservationticketingwebservice.models.CardScheme;
import com.probase.reservationticketingwebservice.models.Device;
import com.probase.reservationticketingwebservice.models.District;
import com.probase.reservationticketingwebservice.models.TripCard;
import com.probase.reservationticketingwebservice.models.Transaction;
import com.probase.reservationticketingwebservice.models.User;
import com.probase.reservationticketingwebservice.util.Application;
import com.probase.reservationticketingwebservice.util.ERROR;
import com.probase.reservationticketingwebservice.util.PrbCustomService;
import com.probase.reservationticketingwebservice.util.ServiceLocator;
import com.probase.reservationticketingwebservice.util.SwpService;
import com.probase.reservationticketingwebservice.util.UtilityHelper;

@Path( "services/CardServices" )
@Stateless( name = "CardServices", mappedName = "services/CardServices" )
public class CardServices {

	private static Logger log = Logger.getLogger(CardServices.class);
	private ServiceLocator serviceLocator = ServiceLocator.getInstance();
	public SwpService swpService = null;
	public PrbCustomService swpCustomService = PrbCustomService.getInstance();
	
	
	/*
	 * @params	status	
	 * 	CardStatus.ACTIVE	0
	 *	CardStatus.DELETED	1
	 *	CardStatus.DISABLED	2
	 *	CardStatus.INACTIVE	3
	 */
	
	private static final long serialVersionUID = -6663599014192066936L;

	   
    @POST
    @Path( "addNewCard" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response addNewCard(
    		@Context HttpHeaders httpHeaders,
    		@Context HttpServletRequest requestContext,
            @FormParam( "cardSchemeCode" ) String cardSchemeCode,
            @FormParam( "clientCode" ) String clientCode,
            @FormParam( "quantity" ) Integer quantity) throws JSONException {
        

        try {
        	String ipAddress = requestContext.getRemoteAddr();
        	CardFunction cardFunction = CardFunction.getInstance();
            List<String> tokenList = httpHeaders.getRequestHeader(HttpHeaderNames.AUTH_TOKEN);
            String token = tokenList!=null && tokenList.size()>0 ? tokenList.get(0) : null;
            String requestId = token.substring(token.length()-10) + "-" +  (new SimpleDateFormat("YYYYMMddHHmmss").format(new Date())) + ": ";
            
        	//log.inforequestId + "Proceed 1");
        	if(token==null)
        	{
            	JsonObjectBuilder jsonObject = Json.createObjectBuilder();
            	jsonObject.add( "status", ERROR.UNAUTHORIZED_ACTION );
            	jsonObject.add( "message", "Invalid transaction" );
                JsonObject jsonObj = jsonObject.build();
        		return UtilityHelper.getNoCacheResponseBuilder( Response.Status.UNAUTHORIZED ).entity( jsonObj.toString() ).build();
        	}


            Response authResponse = cardFunction.addNewCard( cardSchemeCode, quantity, clientCode, token, requestId, ipAddress );
            

            return authResponse;

        } catch ( final Exception ex ) {
        	ex.printStackTrace();
        	JsonObjectBuilder jsonObject = Json.createObjectBuilder();
        	jsonObject.add( "message", "Experienced a system server error " );
            JsonObject jsonObj = jsonObject.build();

            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.INTERNAL_SERVER_ERROR ).entity( jsonObj.toString() ).build();
        }
    }
    
		
    
    
    @POST
    @Path( "assignCardToCustomer" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response assignCardToCustomer(
    		@Context HttpHeaders httpHeaders,
    		@Context HttpServletRequest requestContext,
            @FormParam( "customerDetails" ) String customerDetails,
            @FormParam( "amountCollected" ) Double amountCollected,
            @FormParam( "cardDetails" ) String cardDetails,
            @FormParam( "clientCode" ) String clientCode ) throws JSONException {

        

        try {
        	String ipAddress = requestContext.getRemoteAddr();
        	CardFunction cardFunction = CardFunction.getInstance();
            List<String> tokenList = httpHeaders.getRequestHeader(HttpHeaderNames.AUTH_TOKEN);
            String token = tokenList!=null && tokenList.size()>0 ? tokenList.get(0) : null;
        	if(token==null)
        	{
            	JsonObjectBuilder jsonObject = Json.createObjectBuilder();
            	jsonObject.add( "status", ERROR.UNAUTHORIZED_ACTION );
            	jsonObject.add( "message", "Invalid transaction" );
                JsonObject jsonObj = jsonObject.build();
        		return UtilityHelper.getNoCacheResponseBuilder( Response.Status.UNAUTHORIZED ).entity( jsonObj.toString() ).build();
        	}
        	//log.info"Proceed 1");
        	String requestId = (new SimpleDateFormat("YYYYMMddHHmmss").format(new Date())) + ": ";
        	
        	
            Response authResponse = cardFunction.assignCardToCustomer(customerDetails, amountCollected, clientCode, cardDetails, token, requestId, ipAddress);
            

            return authResponse;

        } catch ( final Exception ex ) {
        	ex.printStackTrace();
        	JsonObjectBuilder jsonObject = Json.createObjectBuilder();
        	jsonObject.add( "message", "Experienced a system server error " );
            JsonObject jsonObj = jsonObject.build();

            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.INTERNAL_SERVER_ERROR ).entity( jsonObj.toString() ).build();
        }
    }
    
    
    @GET
    @Path( "getCard" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response getCard(
    		@Context HttpHeaders httpHeaders,
    		@Context HttpServletRequest requestContext,
            @QueryParam( "cardPan" ) String cardPan,
            @QueryParam( "clientCode" ) String clientCode,
            @QueryParam( "cardUniqueId" ) String cardUniqueId ) throws JSONException {

        

        try {
        	String ipAddress = requestContext.getRemoteAddr();
        	CardFunction cardFunction = CardFunction.getInstance();
            List<String> tokenList = httpHeaders.getRequestHeader(HttpHeaderNames.AUTH_TOKEN);
            String token = tokenList!=null && tokenList.size()>0 ? tokenList.get(0) : null;
        	if(token==null)
        	{
            	JsonObjectBuilder jsonObject = Json.createObjectBuilder();
            	jsonObject.add( "status", ERROR.UNAUTHORIZED_ACTION );
            	jsonObject.add( "message", "Invalid transaction" );
                JsonObject jsonObj = jsonObject.build();
        		return UtilityHelper.getNoCacheResponseBuilder( Response.Status.UNAUTHORIZED ).entity( jsonObj.toString() ).build();
        	}
        	String requestId = token.substring(token.length()-10) + "-" +  (new SimpleDateFormat("YYYYMMddHHmmss").format(new Date())) + ": ";
        	//log.inforequestId + "Proceed 1");
        	
            Response authResponse = cardFunction.getCard( cardPan, cardUniqueId, token, requestId, ipAddress, clientCode );
            

            return authResponse;

        } catch ( final Exception ex ) {
        	ex.printStackTrace();
        	JsonObjectBuilder jsonObject = Json.createObjectBuilder();
        	jsonObject.add( "message", "Experienced a system server error " );
            JsonObject jsonObj = jsonObject.build();

            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.INTERNAL_SERVER_ERROR ).entity( jsonObj.toString() ).build();
        }
    }
    
    
    
    
    
    
    
    @GET
    @Path( "listCards" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response listCards(
    		@Context HttpHeaders httpHeaders,
    		@Context HttpServletRequest requestContext,
    		@QueryParam( "cardSchemeCode" ) String cardSchemeCode,
            @QueryParam( "batchId" ) String batchId, 
            @QueryParam( "cardType" ) String cardType, 
            @QueryParam( "assignedToCustomer" ) Boolean assignedToCustomer, 
            @QueryParam( "clientCode" ) String clientCode) throws JSONException {

        

        try {
        	String ipAddress = requestContext.getRemoteAddr();
        	CardFunction cardFunction = CardFunction.getInstance();
            List<String> tokenList = httpHeaders.getRequestHeader(HttpHeaderNames.AUTH_TOKEN);
            String token = tokenList!=null && tokenList.size()>0 ? tokenList.get(0) : null;
        	if(token==null)
        	{
            	JsonObjectBuilder jsonObject = Json.createObjectBuilder();
            	jsonObject.add( "status", ERROR.UNAUTHORIZED_ACTION );
            	jsonObject.add( "message", "Invalid transaction" );
                JsonObject jsonObj = jsonObject.build();
        		return UtilityHelper.getNoCacheResponseBuilder( Response.Status.UNAUTHORIZED ).entity( jsonObj.toString() ).build();
        	}
        	//log.info"Proceed 1");
        	String requestId = (new SimpleDateFormat("YYYYMMddHHmmss").format(new Date())) + ": ";
        	
            Response authResponse = cardFunction.getCardList( cardSchemeCode, batchId, token, assignedToCustomer, clientCode, requestId, ipAddress, cardType );
           
            return authResponse;

        } catch ( final Exception ex ) {
        	ex.printStackTrace();
        	JsonObjectBuilder jsonObject = Json.createObjectBuilder();
        	jsonObject.add( "message", "Experienced a system server error " );
            JsonObject jsonObj = jsonObject.build();

            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.INTERNAL_SERVER_ERROR ).entity( jsonObj.toString() ).build();
        }
    }
    
    
    
    
    @POST
    @Path( "assignCardToVendor" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response assignCardToVendor(
    		@Context HttpHeaders httpHeaders,
    		@Context HttpServletRequest requestContext,
            @FormParam( "vendorCode" ) String vendorCode,
            @FormParam( "cardPanUniqueMapStr" ) String cardPanUniqueMapStr,
            @FormParam( "amountCollected" ) Double amountCollected,
            @FormParam( "clientCode" ) String clientCode) throws JSONException {

        

        try {
        	String ipAddress = requestContext.getRemoteAddr();
        	CardFunction cardFunction = CardFunction.getInstance();
            List<String> tokenList = httpHeaders.getRequestHeader(HttpHeaderNames.AUTH_TOKEN);
            String token = tokenList!=null && tokenList.size()>0 ? tokenList.get(0) : null;
        	if(token==null)
        	{
            	JsonObjectBuilder jsonObject = Json.createObjectBuilder();
            	jsonObject.add( "status", ERROR.UNAUTHORIZED_ACTION );
            	jsonObject.add( "message", "Invalid transaction" );
                JsonObject jsonObj = jsonObject.build();
        		return UtilityHelper.getNoCacheResponseBuilder( Response.Status.UNAUTHORIZED ).entity( jsonObj.toString() ).build();
        	}
        	String requestId = (new SimpleDateFormat("YYYYMMddHHmmss").format(new Date())) + ": ";
        	//log.inforequestId + "Proceed 1");
        	
            Response authResponse = cardFunction.assignCardToVendor(vendorCode, cardPanUniqueMapStr, amountCollected, token, requestId, ipAddress, clientCode);
            return authResponse;

        } catch ( final Exception ex ) {
        	ex.printStackTrace();
        	JsonObjectBuilder jsonObject = Json.createObjectBuilder();
        	jsonObject.add( "message", "Experienced a system server error " );
            JsonObject jsonObj = jsonObject.build();

            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.INTERNAL_SERVER_ERROR ).entity( jsonObj.toString() ).build();
        }
    }
    
    
    @POST
    @Path( "fundCard" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response fundCard(
    		@Context HttpHeaders httpHeaders,
    		@Context HttpServletRequest requestContext,
            @FormParam( "tripCardNumber" ) String tripCardNumber,
            @FormParam( "tripCardUniqueId" ) String tripCardUniqueId,
            @FormParam( "channel" ) String channel,
            @FormParam( "probasePayMerchantCode" ) String probasePayMerchantCode,
            @FormParam( "probasePayDeviceCode" ) String probasePayDeviceCode,
            @FormParam( "paymentMeans" ) String paymentMeans,
            @FormParam( "transactionRef" ) String transactionRef,
            @FormParam( "hash" ) String hash,
            @FormParam( "deviceCode" ) String deviceCode,
            @FormParam( "amountToFund" ) Double amountToFund,
            @FormParam( "clientCode" ) String clientCode) throws JSONException {

    	

        try {
        	String ipAddress = requestContext.getRemoteAddr();
        	CardFunction cardFunction = CardFunction.getInstance();
            List<String> tokenList = httpHeaders.getRequestHeader(HttpHeaderNames.AUTH_TOKEN);
            String token = tokenList!=null && tokenList.size()>0 ? tokenList.get(0) : null;
        	if(token==null)
        	{
            	JsonObjectBuilder jsonObject = Json.createObjectBuilder();
            	jsonObject.add( "status", ERROR.UNAUTHORIZED_ACTION );
            	jsonObject.add( "message", "Invalid transaction" );
                JsonObject jsonObj = jsonObject.build();
        		return UtilityHelper.getNoCacheResponseBuilder( Response.Status.UNAUTHORIZED ).entity( jsonObj.toString() ).build();
        	}
        	String requestId = (new SimpleDateFormat("YYYYMMddHHmmss").format(new Date())) + ": ";
        	//log.inforequestId + "Proceed 1");
        	
            Response authResponse = cardFunction.fundCard(tripCardNumber, tripCardUniqueId, channel, probasePayMerchantCode, probasePayDeviceCode, 
        			paymentMeans, transactionRef, hash, amountToFund, 
        			deviceCode, token, requestId, ipAddress, clientCode);
            return authResponse;

        } catch ( final Exception ex ) {
        	ex.printStackTrace();
        	JsonObjectBuilder jsonObject = Json.createObjectBuilder();
        	jsonObject.add( "message", "Experienced a system server error " );
            JsonObject jsonObj = jsonObject.build();

            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.INTERNAL_SERVER_ERROR ).entity( jsonObj.toString() ).build();
        }
    }
    
    
    
    @POST
    @Path( "getListCardsByBatchIdWithNoUniqueId" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response getListCardsByBatchIdWithNoUniqueId(
    		@Context HttpHeaders httpHeaders,
    		@Context HttpServletRequest requestContext,
            @FormParam( "batchId" ) String batchId,
            @FormParam( "startIndex" ) Integer startIndex,
            @FormParam( "clientCode" ) String clientCode, 
            @FormParam( "limit" ) Integer limit) throws JSONException {

        

        try {
        	String ipAddress = requestContext.getRemoteAddr();
        	CardFunction cardFunction = CardFunction.getInstance();
            List<String> tokenList = httpHeaders.getRequestHeader(HttpHeaderNames.AUTH_TOKEN);
            String token = tokenList!=null && tokenList.size()>0 ? tokenList.get(0) : null;
        	if(token==null)
        	{
            	JsonObjectBuilder jsonObject = Json.createObjectBuilder();
            	jsonObject.add( "status", ERROR.UNAUTHORIZED_ACTION );
            	jsonObject.add( "message", "Invalid transaction" );
                JsonObject jsonObj = jsonObject.build();
        		return UtilityHelper.getNoCacheResponseBuilder( Response.Status.UNAUTHORIZED ).entity( jsonObj.toString() ).build();
        	}
        	String requestId = (new SimpleDateFormat("YYYYMMddHHmmss").format(new Date())) + ": ";
        	//log.inforequestId + "Proceed 1");
        	
            Response authResponse = cardFunction.getListCardsByBatchIdWithNoUniqueId( batchId, startIndex, limit, token, requestId, ipAddress, clientCode );
            

            return authResponse;

        } catch ( final Exception ex ) {
        	ex.printStackTrace();
        	JsonObjectBuilder jsonObject = Json.createObjectBuilder();
        	jsonObject.add( "message", "Experienced a system server error " );
            JsonObject jsonObj = jsonObject.build();

            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.INTERNAL_SERVER_ERROR ).entity( jsonObj.toString() ).build();
        }
    }
    
    
    
    @POST
    @Path( "getCardBatchIds" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response getCardBatchIds(
    		@Context HttpHeaders httpHeaders,
            @FormParam( "clientCode" ) String clientCode,
    		@Context HttpServletRequest requestContext) throws JSONException {

        
        try {
        	String ipAddress = requestContext.getRemoteAddr();
        	CardFunction cardFunction = CardFunction.getInstance();
            List<String> tokenList = httpHeaders.getRequestHeader(HttpHeaderNames.AUTH_TOKEN);
            String token = tokenList!=null && tokenList.size()>0 ? tokenList.get(0) : null;

        	if(token==null)
        	{
            	JsonObjectBuilder jsonObject = Json.createObjectBuilder();
            	jsonObject.add( "status", ERROR.UNAUTHORIZED_ACTION );
            	jsonObject.add( "message", "Invalid transaction" );
                JsonObject jsonObj = jsonObject.build();
        		return UtilityHelper.getNoCacheResponseBuilder( Response.Status.UNAUTHORIZED ).entity( jsonObj.toString() ).build();
        	}
        	String requestId = token.substring(token.length()-10) + "-" +  (new SimpleDateFormat("YYYYMMddHHmmss").format(new Date())) + ": ";
        	//log.inforequestId + "Proceed 121");
        	
            Response authResponse = cardFunction.getCardBatchIds( token, requestId, ipAddress, clientCode );
            

            return authResponse;

        } catch ( final Exception ex ) {
        	//log.info"Proceed 122");
        	ex.printStackTrace();
        	JsonObjectBuilder jsonObject = Json.createObjectBuilder();
        	jsonObject.add( "message", "Experienced a system server error " );
            JsonObject jsonObj = jsonObject.build();

            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.INTERNAL_SERVER_ERROR ).entity( jsonObj.toString() ).build();
        }
    }
    
    
    
    @POST
    @Path( "addNewCardScheme" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response addNewCardScheme(
    		@Context HttpHeaders httpHeaders,
    		@Context HttpServletRequest requestContext, 
            @FormParam( "updateFlag" ) Boolean updateFlag,  
            @FormParam( "schemeCode" ) String schemeCode, 
            @FormParam( "schemeName" ) String schemeName,
            @FormParam( "schemeDetails" ) String schemeDetails,
    		@FormParam( "lowerAgeBoundary" ) Integer lowerAgeBoundary,
    		@FormParam( "upperAgeBoundary" ) Integer upperAgeBoundary,
    		@FormParam( "dayMondayApplicable" ) Boolean dayMondayApplicable,
    		@FormParam( "dayTuesdayApplicable" ) Boolean dayTuesdayApplicable,
    		@FormParam( "dayWednesdayApplicable" ) Boolean dayWednesdayApplicable,
    		@FormParam( "dayThursdayApplicable" ) Boolean dayThursdayApplicable,
    		@FormParam( "dayFridayApplicable" ) Boolean dayFridayApplicable,
    		@FormParam( "daySaturdayApplicable" ) Boolean daySaturdayApplicable,
    		@FormParam( "daySundayApplicable" ) Boolean daySundayApplicable,
    		@FormParam( "expiryDate" ) Date expiryDate,
    		@FormParam( "maxTripCount" ) Integer maxTripCount,
    		@FormParam( "clientCode" ) String clientCode,
    		@FormParam( "cardSchemeStatus" ) Boolean cardSchemeStatus,
    		@FormParam( "fixedCharge") Double fixedCharge, 
    		@FormParam( "transactionCharge") Double transactionCharge,
    		@FormParam( "discountRate") Double discountRate,
    		@FormParam( "vendorCardPrice" ) Double vendorCardPrice,
    		@FormParam( "cardPrice" ) Double cardPrice,
    		@FormParam( "yearlyPrice") Double yearlyPrice, 
    		@FormParam( "validityPeriod") Integer validityPeriod,
    		@FormParam( "openToVendor") Boolean openToVendor,
    		@FormParam( "tripCardType" ) String tripCardType,
    		@FormParam( "tripChargeMode" ) String tripChargeMode) throws JSONException {

    	
        try {
        	String ipAddress = requestContext.getRemoteAddr();
        	//log.info"Proceed 1");
        	CardFunction cardFunction = CardFunction.getInstance();
            List<String> tokenList = httpHeaders.getRequestHeader(HttpHeaderNames.AUTH_TOKEN);
            String token = tokenList!=null && tokenList.size()>0 ? tokenList.get(0) : null;

        	if(token==null)
        	{
            	JsonObjectBuilder jsonObject = Json.createObjectBuilder();
            	jsonObject.add( "status", ERROR.UNAUTHORIZED_ACTION );
            	jsonObject.add( "message", "Invalid transaction" );
                JsonObject jsonObj = jsonObject.build();
        		return UtilityHelper.getNoCacheResponseBuilder( Response.Status.UNAUTHORIZED ).entity( jsonObj.toString() ).build();
        	}
            String requestId = token.substring(token.length()-10) + "-" +  (new SimpleDateFormat("YYYYMMddHHmmss").format(new Date())) + ": ";
        	
            Response authResponse = cardFunction.addNewCardScheme(
	        	schemeName, schemeDetails, lowerAgeBoundary, upperAgeBoundary, dayMondayApplicable, dayTuesdayApplicable, dayWednesdayApplicable, 
	        	dayThursdayApplicable, dayFridayApplicable, daySaturdayApplicable, daySundayApplicable, 
	        	updateFlag, schemeCode, expiryDate, maxTripCount, transactionCharge, fixedCharge, vendorCardPrice,
	        	cardPrice, yearlyPrice, validityPeriod, tripChargeMode, discountRate, openToVendor, tripCardType, clientCode, token, requestId, ipAddress);
            
            //(String, String, Integer, Integer, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, String, Date, Integer, String, Double, Double, Double, Double, Integer, String, String, String, String)
            //(String, String, Integer, Integer, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, String, Date, Integer, String, Double, Double, Double, Double, Integer, String, String, String, String)

            return authResponse;

        } catch ( final Exception ex ) {
        	ex.printStackTrace();
        	JsonObjectBuilder jsonObject = Json.createObjectBuilder();
        	jsonObject.add( "message", "Experienced a system server error " );
            JsonObject jsonObj = jsonObject.build();

            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.INTERNAL_SERVER_ERROR ).entity( jsonObj.toString() ).build();
        }
    }
    
    
    
    
    
    
    
    
    
    @GET
    @Path( "getCardSchemes" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response getCardSchemes(
    		@Context HttpHeaders httpHeaders,
    		@QueryParam( "clientCode" ) String clientCode,
    		@QueryParam( "cardType" ) String cardType
            ) throws JSONException {
    	String requestId = (new SimpleDateFormat("YYYYMMddHHmmss").format(new Date())) + ": ";
    	CardFunction cardFunction = CardFunction.getInstance();
        try {
        	//log.inforequestId + "Proceed 1");
        	
        	
            Response authResponse = cardFunction.getCardSchemes( clientCode, cardType );
            

            return authResponse;

        } catch ( final Exception ex ) {
        	ex.printStackTrace();
        	log.warn(ex);
        	JsonObjectBuilder jsonObject = Json.createObjectBuilder();
        	jsonObject.add( "message", "Experienced a system server error " );
            JsonObject jsonObj = jsonObject.build();

            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.INTERNAL_SERVER_ERROR ).entity( jsonObj.toString() ).build();
        }
    }
    
    

    
    
    
    
    @GET
    @Path( "getCardScheme" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response getCardScheme(
    		@Context HttpHeaders httpHeaders,
    		@QueryParam( "clientCode" ) String clientCode,
    		@QueryParam( "schemeCode" ) String schemeCode
            ) throws JSONException {
    	String requestId = (new SimpleDateFormat("YYYYMMddHHmmss").format(new Date())) + ": ";
    	CardFunction cardFunction = CardFunction.getInstance();
        try {
        	//log.inforequestId + "Proceed 1");
        	
        	
            Response authResponse = cardFunction.getCardScheme( clientCode, schemeCode );
            

            return authResponse;

        } catch ( final Exception ex ) {
        	ex.printStackTrace();
        	log.warn(ex);
        	JsonObjectBuilder jsonObject = Json.createObjectBuilder();
        	jsonObject.add( "message", "Experienced a system server error " );
            JsonObject jsonObj = jsonObject.build();

            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.INTERNAL_SERVER_ERROR ).entity( jsonObj.toString() ).build();
        }
    }
    
    
    
    
    @GET
    @Path( "getCardCountByStatus" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response getCardCountByStatus(
    		@Context HttpHeaders httpHeaders,
    		@Context HttpServletRequest requestContext,
    		@QueryParam( "cardStatus" ) String cardStatus,
    		@QueryParam( "clientCode" ) String clientCode
            ) throws JSONException {
    	String requestId = (new SimpleDateFormat("YYYYMMddHHmmss").format(new Date())) + ": ";
    	CardFunction cardFunction = CardFunction.getInstance();
        try {
        	String ipAddress = requestContext.getRemoteAddr();
        	//log.inforequestId + "Proceed 1");
        	
            Response authResponse = cardFunction.getCardCountByStatus(cardStatus, requestId, ipAddress, clientCode);
            

            return authResponse;

        } catch ( final Exception ex ) {
        	log.warn(ex);
        	ex.printStackTrace();
        	JsonObjectBuilder jsonObject = Json.createObjectBuilder();
        	jsonObject.add( "message", "Experienced a system server error " );
            JsonObject jsonObj = jsonObject.build();

            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.INTERNAL_SERVER_ERROR ).entity( jsonObj.toString() ).build();
        }
    }
    
    
    
}
