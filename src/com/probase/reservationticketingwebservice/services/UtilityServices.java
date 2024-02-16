package com.probase.reservationticketingwebservice.services;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
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
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.json.JSONException;
import org.json.JSONObject;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.probase.reservationticketingwebservice.authenticator.CardFunction;
import com.probase.reservationticketingwebservice.authenticator.HttpHeaderNames;
import com.probase.reservationticketingwebservice.authenticator.UtilityFunction;
import com.probase.reservationticketingwebservice.authenticator.VehicleFunction;
import com.probase.reservationticketingwebservice.enumerations.CouponStatus;
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
import com.probase.reservationticketingwebservice.models.Client;
import com.probase.reservationticketingwebservice.models.Customer;
import com.probase.reservationticketingwebservice.models.Device;
import com.probase.reservationticketingwebservice.models.District;
import com.probase.reservationticketingwebservice.models.TripCard;
import com.probase.reservationticketingwebservice.models.Transaction;
import com.probase.reservationticketingwebservice.models.User;
import com.probase.reservationticketingwebservice.models.Vendor;
import com.probase.reservationticketingwebservice.util.Application;
import com.probase.reservationticketingwebservice.util.ERROR;
import com.probase.reservationticketingwebservice.util.PrbCustomService;
import com.probase.reservationticketingwebservice.util.ServiceLocator;
import com.probase.reservationticketingwebservice.util.SwpService;
import com.probase.reservationticketingwebservice.util.UtilityHelper;

@Path( "services/UtilityServices" )
@Stateless( name = "UtilityServices", mappedName = "services/UtilityServices" )
public class UtilityServices {

	private static Logger log = Logger.getLogger(UtilityServices.class);
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
    @Path( "createNewPublicHoliday" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response createNewPublicHoliday(
    		@Context HttpHeaders httpHeaders,
    		@Context HttpServletRequest requestContext,
            @FormParam( "holidayName" ) String holidayName,
            @FormParam( "holidayDate" ) String holidayDate,
            @FormParam( "clientCode" ) String clientCode ) throws JSONException {

        

        try {
        	String ipAddress = requestContext.getRemoteAddr();
        	UtilityFunction utilityFunction = UtilityFunction.getInstance();
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
        	String requestId = token.substring(token.length()-10) + "-" + (new SimpleDateFormat("YYYYMMddHHmmss").format(new Date())) + ": ";
        	//log.inforequestId + "Proceed 1");
            Response authResponse = utilityFunction.createNewPublicHoliday( holidayName, holidayDate, clientCode, token, requestId, ipAddress );
            

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
    @Path( "createNewCoupons" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response createNewCoupons(
    		@Context HttpHeaders httpHeaders,
    		@Context HttpServletRequest requestContext,
    		@FormParam( "expiryDate" ) String expiryDate,
    		@FormParam( "clientCode" ) String clientCode,
    		@FormParam( "quantity" ) Integer quantity,
    		@FormParam( "discountRate" ) Double discountRate ) throws JSONException {

        

        try {
        	String ipAddress = requestContext.getRemoteAddr();
        	UtilityFunction utilityFunction = UtilityFunction.getInstance();
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
        	String requestId = token.substring(token.length()-10) + "-" + (new SimpleDateFormat("YYYYMMddHHmmss").format(new Date())) + ": ";
        	//log.inforequestId + "Proceed 1");
            Response authResponse = utilityFunction.createNewCoupons( quantity, expiryDate, discountRate, clientCode, token, requestId, ipAddress );
            

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
    @Path( "createNewTicketCollectionPoint" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response createNewTicketCollectionPoint(
    		@Context HttpHeaders httpHeaders,
    		@Context HttpServletRequest requestContext,
            @FormParam( "collectionPointName" ) String collectionPointName,
            @FormParam( "districtId" ) Long districtId,
            @FormParam( "clientCode" ) String clientCode ) throws JSONException {

        

        try {
        	String ipAddress = requestContext.getRemoteAddr();
        	UtilityFunction utilityFunction = UtilityFunction.getInstance();
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
        	String requestId = token.substring(token.length()-10) + "-" + (new SimpleDateFormat("YYYYMMddHHmmss").format(new Date())) + ": ";
        	//log.inforequestId + "Proceed 1");
            Response authResponse = utilityFunction.createNewTicketCollectionPoint( collectionPointName, districtId, clientCode, token, requestId, ipAddress );
            

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
    @Path( "getListTicketCollectionPoints" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response getListTicketCollectionPoints(
    		@Context HttpHeaders httpHeaders,
    		@Context HttpServletRequest requestContext,
            @FormParam( "clientCode" ) String clientCode ) throws JSONException {

        

        try {
        	String ipAddress = requestContext.getRemoteAddr();
        	UtilityFunction utilityFunction = UtilityFunction.getInstance();
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
        	String requestId = token.substring(token.length()-10) + "-" + (new SimpleDateFormat("YYYYMMddHHmmss").format(new Date())) + ": ";
        	//log.inforequestId + "Proceed 1");
            Response authResponse = utilityFunction.getListTicketCollectionPoints( token, clientCode,  requestId, ipAddress );
            

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
    @Path( "createNewScheduleDay" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response createNewScheduleDay(
    		@Context HttpHeaders httpHeaders,
    		@Context HttpServletRequest requestContext,
    	    @FormParam( "scheduleDates" ) String scheduleDates,
    	    @FormParam( "clientCode" ) String clientCode ) throws JSONException {

        

        try {
        	String ipAddress = requestContext.getRemoteAddr();
        	UtilityFunction utilityFunction = UtilityFunction.getInstance();
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
        	String requestId = token.substring(token.length()-10) + "-" + (new SimpleDateFormat("YYYYMMddHHmmss").format(new Date())) + ": ";
        	//log.inforequestId + "Proceed 1");
    		Response authResponse = utilityFunction.createNewScheduleDay(scheduleDates, clientCode, token, requestId, ipAddress);


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
    @Path( "getScheduleDays" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response getScheduleDays(
    		@Context HttpHeaders httpHeaders,
    		@Context HttpServletRequest requestContext,
    	    @FormParam( "startDate" ) String startDate ,
    	    @FormParam( "endDate" ) String endDate ,
    	    @FormParam( "clientCode" ) String clientCode ) throws JSONException {

        

        try {
        	String ipAddress = requestContext.getRemoteAddr();
        	UtilityFunction utilityFunction = UtilityFunction.getInstance();
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
        	String requestId = token.substring(token.length()-10) + "-" + (new SimpleDateFormat("YYYYMMddHHmmss").format(new Date())) + ": ";
        	//log.inforequestId + "Proceed 1");
    		Response authResponse = utilityFunction.getScheduleDays(startDate, endDate, clientCode, requestId, ipAddress);
    	            

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
    @Path( "createNewScheduleStation" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response createNewScheduleStation(
    		@Context HttpHeaders httpHeaders,
    		@Context HttpServletRequest requestContext,
    	    @FormParam( "scheduleDay" ) String scheduleDay,
    		@FormParam( "vehicleCode" ) String vehicleCode,
    		@FormParam( "tripLineCode" ) String tripLineCode,
    		@FormParam( "purchaseStartDate" ) String purchaseStartDate,
    	    @FormParam( "stationCode" ) String stationCode,
    	    @FormParam( "clientCode" ) String clientCode,
    		@FormParam( "seatSections" ) String seatSections,
    		@FormParam( "courierSections") String courierSections,
    		@FormParam( "scheduleStationCode") String scheduleStationCode,
    	    @FormParam( "departureTime" ) String departureTime,
    	    @FormParam( "refundPercentage" ) Double refundPercentage,
    		@FormParam( "departureStationCode") String departureStationCode,
    		@FormParam( "destinationStationCode") String destinationStationCode,
    	    @FormParam( "arrivalTime" ) String arrivalTime ) throws JSONException {

        

        try {
        	String ipAddress = requestContext.getRemoteAddr();
        	UtilityFunction utilityFunction = UtilityFunction.getInstance();
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
        	String requestId = token.substring(token.length()-10) + "-" + (new SimpleDateFormat("YYYYMMddHHmmss").format(new Date())) + ": ";
        	//log.inforequestId + "Proceed 1");
    		Response authResponse = utilityFunction.createNewScheduleStation(refundPercentage, tripLineCode, purchaseStartDate, scheduleStationCode, 
    				vehicleCode, seatSections, scheduleDay, departureTime, arrivalTime, stationCode, courierSections, clientCode, 
    				token, requestId, ipAddress, departureStationCode, destinationStationCode);
    	            

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
    @Path( "getScheduleStations" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response getScheduleStations(
    		@Context HttpHeaders httpHeaders,
    		@Context HttpServletRequest requestContext,
    	    @FormParam( "scheduleDayName" ) String scheduleDayName,
    	    @FormParam( "departureTime" ) String departureTime,
    	    @FormParam( "arrivalTime" ) String arrivalTime,
    		@FormParam( "departureStationId" ) Long departureStationId, 
    		@FormParam( "arrivalStationId" ) Long arrivalStationId,
    	    @FormParam( "clientCode" ) String clientCode ) throws JSONException {

        

        try {
        	String ipAddress = requestContext.getRemoteAddr();
        	UtilityFunction utilityFunction = UtilityFunction.getInstance();
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
        	String requestId = token.substring(token.length()-10) + "-" + (new SimpleDateFormat("YYYYMMddHHmmss").format(new Date())) + ": ";
        	//log.inforequestId + "Proceed 1");
    		Response authResponse = utilityFunction.getScheduleStations(scheduleDayName, departureTime, arrivalTime, departureStationId, arrivalStationId, token, clientCode, requestId, ipAddress);
    	            

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
    @Path( "getScheduleStationsBetweenScheduleDates" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response getScheduleStationsBetweenScheduleDates(
    		@Context HttpHeaders httpHeaders,
    		@Context HttpServletRequest requestContext,
    	    @FormParam( "startScheduleDate" ) String startScheduleDate,
    	    @FormParam( "endScheduleDate" ) String endScheduleDate,
    	    @FormParam( "stationIds" ) String stationIds,
    	    @FormParam( "clientCode" ) String clientCode ) throws JSONException {

        

        try {
        	String ipAddress = requestContext.getRemoteAddr();
        	UtilityFunction utilityFunction = UtilityFunction.getInstance();
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
        	String requestId = token.substring(token.length()-10) + "-" + (new SimpleDateFormat("YYYYMMddHHmmss").format(new Date())) + ": ";
        	//log.inforequestId + "Proceed 1");
    		Response authResponse = utilityFunction.getScheduleStationsBetweenScheduleDates(startScheduleDate, endScheduleDate, stationIds, token, clientCode, requestId, ipAddress);
    	            

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
    @Path( "listScheduleStations" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response listScheduleStations(
    		@Context HttpHeaders httpHeaders,
    		@Context HttpServletRequest requestContext,
    		@FormParam( "scheduleStationCode" ) String scheduleStationCode,
    	    @FormParam( "clientCode" ) String clientCode ) throws JSONException {

        

        try {
        	String ipAddress = requestContext.getRemoteAddr();
        	UtilityFunction utilityFunction = UtilityFunction.getInstance();
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
        	String requestId = token.substring(token.length()-10) + "-" + (new SimpleDateFormat("YYYYMMddHHmmss").format(new Date())) + ": ";
        	//log.inforequestId + "Proceed 1");
    		Response authResponse = utilityFunction.getListScheduleStations(scheduleStationCode, token, clientCode, requestId, ipAddress);
    	            

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
    @Path( "createNewTripZone" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response createNewTripZone(
    		@Context HttpHeaders httpHeaders,
    		@Context HttpServletRequest requestContext,
    	    @FormParam( "zoneName" ) String zoneName,
    	    @FormParam( "routeOrder" ) Integer routeOrder,
    	    @FormParam( "stations" ) String stations,
    	    @FormParam( "editTripZoneCode" ) String editTripZoneCode,
    	    @FormParam( "clientCode" ) String clientCode ) throws JSONException {

        

        try {
        	String ipAddress = requestContext.getRemoteAddr();
        	UtilityFunction utilityFunction = UtilityFunction.getInstance();
            List<String> tokenList = httpHeaders.getRequestHeader(HttpHeaderNames.AUTH_TOKEN);
            String token = tokenList!=null && tokenList.size()>0 ? tokenList.get(0) : null;
            
        	if(token==null)
        	{
            	JsonObjectBuilder jsonObject = Json.createObjectBuilder();
            	jsonObject.add( "status", ERROR.UNAUTHORIZED_ACTION );
            	jsonObject.add( "message", "Unauthorized action" );
                JsonObject jsonObj = jsonObject.build();
        		return UtilityHelper.getNoCacheResponseBuilder( Response.Status.UNAUTHORIZED ).entity( jsonObj.toString() ).build();
        	}
        	String requestId = token.substring(token.length()-10) + "-" + (new SimpleDateFormat("YYYYMMddHHmmss").format(new Date())) + ": ";
        	//log.inforequestId + "Proceed 1");
    		Response authResponse = utilityFunction.createNewTripZone(zoneName, routeOrder, stations, editTripZoneCode, clientCode, token, requestId, ipAddress);
    	            

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
    @Path( "listTripZones" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response listTripZones(
    		@Context HttpHeaders httpHeaders,
    		@Context HttpServletRequest requestContext,
    	    @FormParam( "clientCode" ) String clientCode,
    	    @FormParam( "zoneCode" ) String zoneCode ) throws JSONException {

        

        try {
        	String ipAddress = requestContext.getRemoteAddr();
        	UtilityFunction utilityFunction = UtilityFunction.getInstance();
            List<String> tokenList = httpHeaders.getRequestHeader(HttpHeaderNames.AUTH_TOKEN);
            String token = tokenList!=null && tokenList.size()>0 ? tokenList.get(0) : null;
            
        	/*if(token==null)
        	{
            	JsonObjectBuilder jsonObject = Json.createObjectBuilder();
            	jsonObject.add( "status", ERROR.UNAUTHORIZED_ACTION );
            	jsonObject.add( "message", "Invalid transaction" );
                JsonObject jsonObj = jsonObject.build();
        		return UtilityHelper.getNoCacheResponseBuilder( Response.Status.UNAUTHORIZED ).entity( jsonObj.toString() ).build();
        	}*/
        	String requestId = (new SimpleDateFormat("YYYYMMddHHmmss").format(new Date())) + ": ";
        	//log.inforequestId + "Proceed 1");
    		Response authResponse = utilityFunction.getListTripZones(token, clientCode, requestId, ipAddress, zoneCode);
    	            

            return authResponse;

        } catch ( final Exception ex ) {
        	log.error("Error", ex);
        	ex.printStackTrace();
        	JsonObjectBuilder jsonObject = Json.createObjectBuilder();
        	jsonObject.add( "message", "Experienced a system server error " );
            JsonObject jsonObj = jsonObject.build();

            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.INTERNAL_SERVER_ERROR ).entity( jsonObj.toString() ).build();
        }
    }
    
    
    
    
    
    
    
    @POST
    @Path( "createNewVehicleSchedule" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response createNewVehicleSchedule(
    		@Context HttpHeaders httpHeaders,
    		@Context HttpServletRequest requestContext,
    	    @FormParam( "scheduleStationCode" ) String scheduleStationCode,
    	    @FormParam( "vehicleCode" ) String vehicleCode,
    	    @FormParam( "clientCode" ) String clientCode ) throws JSONException {

        

        try {
        	String ipAddress = requestContext.getRemoteAddr();
        	UtilityFunction utilityFunction = UtilityFunction.getInstance();
            List<String> tokenList = httpHeaders.getRequestHeader(HttpHeaderNames.AUTH_TOKEN);
            String token = tokenList!=null && tokenList.size()>0 ? tokenList.get(0) : null;
            
        	if(token==null)
        	{
            	JsonObjectBuilder jsonObject = Json.createObjectBuilder();
            	jsonObject.add( "status", ERROR.UNAUTHORIZED_ACTION );
            	jsonObject.add( "message", "Unauthorized action" );
                JsonObject jsonObj = jsonObject.build();
        		return UtilityHelper.getNoCacheResponseBuilder( Response.Status.UNAUTHORIZED ).entity( jsonObj.toString() ).build();
        	}
        	String requestId = token.substring(token.length()-10) + "-" + (new SimpleDateFormat("YYYYMMddHHmmss").format(new Date())) + ": ";
        	//log.inforequestId + "Proceed 1");
    		Response authResponse = utilityFunction.createNewVehicleSchedule(scheduleStationCode, vehicleCode, clientCode, token, requestId, ipAddress);
    	            

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
    @Path( "listVehicleSchedules" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response listVehicleSchedules(
    		@Context HttpHeaders httpHeaders,
    		@Context HttpServletRequest requestContext,
    	    @FormParam( "scheduleStationId" ) Long scheduleStationId,
    	    @FormParam( "vehicleId" ) Long vehicleId,
    	    @FormParam( "startingDate" ) String startingDate,
    	    @FormParam( "vehicleCode" ) String vehicleCode,
    	    @FormParam( "clientCode" ) String clientCode ) throws JSONException {

        

        try {
        	String ipAddress = requestContext.getRemoteAddr();
        	UtilityFunction utilityFunction = UtilityFunction.getInstance();
            List<String> tokenList = httpHeaders.getRequestHeader(HttpHeaderNames.AUTH_TOKEN);
            String token = tokenList!=null && tokenList.size()>0 ? tokenList.get(0) : null;
            
        	if(token==null)
        	{
            	JsonObjectBuilder jsonObject = Json.createObjectBuilder();
            	jsonObject.add( "status", ERROR.UNAUTHORIZED_ACTION );
            	jsonObject.add( "message", "Unauthorized action" );
                JsonObject jsonObj = jsonObject.build();
        		return UtilityHelper.getNoCacheResponseBuilder( Response.Status.UNAUTHORIZED ).entity( jsonObj.toString() ).build();
        	}
        	String requestId = token.substring(token.length()-10) + "-" + (new SimpleDateFormat("YYYYMMddHHmmss").format(new Date())) + ": ";
        	//log.inforequestId + "Proceed 1");
    		Response authResponse = utilityFunction.listVehicleSchedules(vehicleCode, startingDate, scheduleStationId, vehicleId, token, clientCode, requestId, ipAddress);
    	            

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
    @Path( "getVehicleSchedule" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response getVehicleSchedule(
    		@Context HttpHeaders httpHeaders,
    		@Context HttpServletRequest requestContext,
    	    @FormParam( "scheduleStationId" ) Long scheduleStationId,
    		@FormParam( "vehicleScheduleId" ) Long vehicleScheduleId,
    	    @FormParam( "vehicleId" ) Long vehicleId,
    	    @FormParam( "clientCode" ) String clientCode ) throws JSONException {

        

        try {
        	String ipAddress = requestContext.getRemoteAddr();
        	UtilityFunction utilityFunction = UtilityFunction.getInstance();
            List<String> tokenList = httpHeaders.getRequestHeader(HttpHeaderNames.AUTH_TOKEN);
            String token = tokenList!=null && tokenList.size()>0 ? tokenList.get(0) : null;
            
        	if(token==null)
        	{
            	JsonObjectBuilder jsonObject = Json.createObjectBuilder();
            	jsonObject.add( "status", ERROR.UNAUTHORIZED_ACTION );
            	jsonObject.add( "message", "Unauthorized action" );
                JsonObject jsonObj = jsonObject.build();
        		return UtilityHelper.getNoCacheResponseBuilder( Response.Status.UNAUTHORIZED ).entity( jsonObj.toString() ).build();
        	}
        	String requestId = token.substring(token.length()-10) + "-" + (new SimpleDateFormat("YYYYMMddHHmmss").format(new Date())) + ": ";
        	//log.inforequestId + "Proceed 1");
    		Response authResponse = utilityFunction.getVehicleSchedule(vehicleScheduleId, scheduleStationId, vehicleId, token, clientCode, requestId, ipAddress);
    	            

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
    @Path( "createNewVehicleClass" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response createNewVehicleClass(
    		@Context HttpHeaders httpHeaders,
    		@Context HttpServletRequest requestContext,
    	    @FormParam( "vehicleSeatClassName" ) String vehicleSeatClassName,
    	    @FormParam( "description" ) String description,
    	    @FormParam( "clientCode" ) String clientCode ) throws JSONException {

        

        try {
        	String ipAddress = requestContext.getRemoteAddr();
        	UtilityFunction utilityFunction = UtilityFunction.getInstance();
            List<String> tokenList = httpHeaders.getRequestHeader(HttpHeaderNames.AUTH_TOKEN);
            String token = tokenList!=null && tokenList.size()>0 ? tokenList.get(0) : null;
            
        	if(token==null)
        	{
            	JsonObjectBuilder jsonObject = Json.createObjectBuilder();
            	jsonObject.add( "status", ERROR.UNAUTHORIZED_ACTION );
            	jsonObject.add( "message", "Unauthorized action" );
                JsonObject jsonObj = jsonObject.build();
        		return UtilityHelper.getNoCacheResponseBuilder( Response.Status.UNAUTHORIZED ).entity( jsonObj.toString() ).build();
        	}
        	String requestId = token.substring(token.length()-10) + "-" + (new SimpleDateFormat("YYYYMMddHHmmss").format(new Date())) + ": ";
        	//log.inforequestId + "Proceed 1");
        	Response authResponse = utilityFunction.createNewVehicleClass(vehicleSeatClassName, description, clientCode, token, requestId, ipAddress);
    	            

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
    @Path( "listVehicleSeatClasses" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response listVehicleSeatClasses(
    		@Context HttpHeaders httpHeaders,
    		@Context HttpServletRequest requestContext,
    	    @FormParam( "clientCode" ) String clientCode ) throws JSONException {

        

        try {
        	String ipAddress = requestContext.getRemoteAddr();
        	UtilityFunction utilityFunction = UtilityFunction.getInstance();
            List<String> tokenList = httpHeaders.getRequestHeader(HttpHeaderNames.AUTH_TOKEN);
            String token = tokenList!=null && tokenList.size()>0 ? tokenList.get(0) : null;
            
        	if(token==null)
        	{
            	JsonObjectBuilder jsonObject = Json.createObjectBuilder();
            	jsonObject.add( "status", ERROR.UNAUTHORIZED_ACTION );
            	jsonObject.add( "message", "Unauthorized action" );
                JsonObject jsonObj = jsonObject.build();
        		return UtilityHelper.getNoCacheResponseBuilder( Response.Status.UNAUTHORIZED ).entity( jsonObj.toString() ).build();
        	}
        	String requestId = token.substring(token.length()-10) + "-" + (new SimpleDateFormat("YYYYMMddHHmmss").format(new Date())) + ": ";
        	//log.inforequestId + "Proceed 1");
    		Response authResponse = utilityFunction.listVehicleSeatClasses(token, clientCode, requestId, ipAddress);
    	            

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
    @Path( "getVehicleSeatClass" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response getVehicleSeatClass(
    		@Context HttpHeaders httpHeaders,
    		@Context HttpServletRequest requestContext,
    	    @FormParam( "clientCode" ) String clientCode, 
    	    @FormParam( "vehicleSeatClassId" ) Long vehicleSeatClassId, 
    	    @FormParam( "vehicleSeatClassName" ) String vehicleSeatClassName) throws JSONException {

        

        try {
        	String ipAddress = requestContext.getRemoteAddr();
        	UtilityFunction utilityFunction = UtilityFunction.getInstance();
            List<String> tokenList = httpHeaders.getRequestHeader(HttpHeaderNames.AUTH_TOKEN);
            String token = tokenList!=null && tokenList.size()>0 ? tokenList.get(0) : null;
            
        	if(token==null)
        	{
            	JsonObjectBuilder jsonObject = Json.createObjectBuilder();
            	jsonObject.add( "status", ERROR.UNAUTHORIZED_ACTION );
            	jsonObject.add( "message", "Unauthorized action" );
                JsonObject jsonObj = jsonObject.build();
        		return UtilityHelper.getNoCacheResponseBuilder( Response.Status.UNAUTHORIZED ).entity( jsonObj.toString() ).build();
        	}
        	String requestId = token.substring(token.length()-10) + "-" + (new SimpleDateFormat("YYYYMMddHHmmss").format(new Date())) + ": ";
        	//log.inforequestId + "Proceed 1");
    		Response authResponse = utilityFunction.getVehicleSeatClass(vehicleSeatClassId, vehicleSeatClassName, token, clientCode, requestId, ipAddress);
    	            

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
    @Path( "getPublicHolidays" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response getPublicHolidays(
    		@Context HttpHeaders httpHeaders,
            @FormParam( "clientCode" ) String clientCode) throws JSONException {

    	UtilityFunction utilityFunction = UtilityFunction.getInstance();

        try {
        	//log.info"Proceed 1");
        	
            Response authResponse = utilityFunction.getPublicHolidays(clientCode);
            

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
    @Path( "getAuditTrail" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response getAuditTrail(
    		@Context HttpHeaders httpHeaders,
    		@Context HttpServletRequest requestContext,
    		@QueryParam( "startDate" ) String startDate,
    		@QueryParam( "endDate" ) String endDate,
    		@QueryParam( "clientCode" ) String clientCode) throws JSONException {

    	String ipAddress = requestContext.getRemoteAddr();
        UtilityFunction utilityFunction = UtilityFunction.getInstance();
        List<String> tokenList = httpHeaders.getRequestHeader(HttpHeaderNames.AUTH_TOKEN);
        String token = tokenList!=null && tokenList.size()>0 ? tokenList.get(0) : null;

        try {
        	
        	String requestId = (token==null ? "" : (token.substring(token.length()-10))) + "-" + (new SimpleDateFormat("YYYYMMddHHmmss").format(new Date())) + ": ";
        	//log.inforequestId + "Proceed 1");
        	//log.info"startDate" + startDate);
        	//log.info"endDate" + endDate);
        	//log.info"clientCode" + clientCode);
        	Enumeration<String> pnames = requestContext.getParameterNames();
        	while(pnames.hasMoreElements())
        	{
        		//log.infopnames.nextElement().toString());
        	}
        		
        	//log.info"clientCode" + clientCode);
        	
            Response authResponse = utilityFunction.getAuditTrail(clientCode, startDate, endDate);

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
    @Path( "getCouponDetails" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response getCouponDetails(
    		@Context HttpHeaders httpHeaders,
    		@Context HttpServletRequest requestContext,
    		@QueryParam( "couponCode" ) String couponCode,
    		@QueryParam( "clientCode" ) String clientCode) throws JSONException {

    	String ipAddress = requestContext.getRemoteAddr();
        UtilityFunction utilityFunction = UtilityFunction.getInstance();
        List<String> tokenList = httpHeaders.getRequestHeader(HttpHeaderNames.AUTH_TOKEN);
        String token = tokenList!=null && tokenList.size()>0 ? tokenList.get(0) : null;

        try {
        	
        	String requestId = (token==null ? "" : (token.substring(token.length()-10))) + "-" + (new SimpleDateFormat("YYYYMMddHHmmss").format(new Date())) + ": ";
        	//log.inforequestId + "Proceed 1");
        	//log.info"couponCode" + couponCode);
        	//log.info"clientCode" + clientCode);
        	Enumeration<String> pnames = requestContext.getParameterNames();
        	while(pnames.hasMoreElements())
        	{
        		//log.infopnames.nextElement().toString());
        	}
        		
        	//log.info"clientCode" + clientCode);
        	
            Response authResponse = utilityFunction.getCouponDetails(couponCode, token, requestId, ipAddress, clientCode);

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
    @Path( "getViewForReportingData" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response getViewForReportingData(
    		@Context HttpHeaders httpHeaders,
    		@Context HttpServletRequest requestContext,
    		@QueryParam( "clientCode" ) String clientCode) throws JSONException {

    	String ipAddress = requestContext.getRemoteAddr();
        UtilityFunction utilityFunction = UtilityFunction.getInstance();
        List<String> tokenList = httpHeaders.getRequestHeader(HttpHeaderNames.AUTH_TOKEN);
        String token = tokenList!=null && tokenList.size()>0 ? tokenList.get(0) : null;

        try {
        	
        	String requestId = (token==null ? "" : (token.substring(token.length()-10))) + "-" + (new SimpleDateFormat("YYYYMMddHHmmss").format(new Date())) + ": ";
        	//log.inforequestId + "Proceed 1");
        	//log.info"couponCode" + clientCode);
        	//log.info"clientCode" + clientCode);
        	Enumeration<String> pnames = requestContext.getParameterNames();
        	while(pnames.hasMoreElements())
        	{
        		//log.infopnames.nextElement().toString());
        	}
        		
        	//log.info"clientCode" + clientCode);
        	
            Response authResponse = utilityFunction.getViewForReportingData(clientCode);

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
    @Path( "getCouponList" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response getCouponList(
    		@Context HttpHeaders httpHeaders,
            @QueryParam( "clientCode" ) String clientCode,
            @QueryParam( "batchNumber" ) String batchNumber) throws JSONException {

    	UtilityFunction utilityFunction = UtilityFunction.getInstance();

        try {
        	//log.info"Proceed 1");
        	
            Response authResponse = utilityFunction.getCoupons(clientCode, batchNumber);
            

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
    @Path( "updateCustomerDetails" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response updateCustomerDetails(@Context HttpHeaders httpHeaders,
    		@Context HttpServletRequest requestContext,
    		@FormParam( "verificationNumber" ) String verificationNumber, 
    		@FormParam( "mobileNumber" ) String mobileNumber,  
    		@FormParam( "firstName" ) String firstName, @FormParam( "lastName" ) String lastName, 
    		@FormParam( "otherName" ) String otherName, @FormParam( "emailAddress" ) String emailAddress, 
    		@FormParam("clientCode") String clientCode
    		)
	{
    	String ipAddress = requestContext.getRemoteAddr();
    	UtilityFunction utilityFunction = UtilityFunction.getInstance();
        List<String> tokenList = httpHeaders.getRequestHeader(HttpHeaderNames.AUTH_TOKEN);
        String token = tokenList!=null && tokenList.size()>0 ? tokenList.get(0) : null;

        try {
        	if(token==null)
        	{
            	JsonObjectBuilder jsonObject = Json.createObjectBuilder();
            	jsonObject.add( "status", ERROR.UNAUTHORIZED_ACTION );
            	jsonObject.add( "message", "Invalid transaction" );
                JsonObject jsonObj = jsonObject.build();
        		return UtilityHelper.getNoCacheResponseBuilder( Response.Status.UNAUTHORIZED ).entity( jsonObj.toString() ).build();
        	}
        	String requestId = token.substring(token.length()-10) + "-" + (new SimpleDateFormat("YYYYMMddHHmmss").format(new Date())) + ": ";
        	//log.inforequestId + "Proceed 1");
        	
            Response authResponse = utilityFunction.updateCustomerDetails(verificationNumber, mobileNumber, firstName, lastName, otherName, 
        			emailAddress, token, requestId, ipAddress, clientCode);

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
    @Path( "createNewTarget" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response createNewTarget(
    		@Context HttpHeaders httpHeaders,
    		@Context HttpServletRequest requestContext,
    	    @FormParam( "targetMonth" ) String targetMonth,
    	    @FormParam( "targetYear" ) Integer targetYear,
    	    @FormParam( "lineCode" ) String lineCode,
    	    @FormParam( "ticketRevenue" ) Double ticketRevenue, 
    	    @FormParam( "courierRevenue" ) Double courierRevenue,
    	    @FormParam( "clientCode" ) String clientCode ) throws JSONException {

        

        try {
        	String ipAddress = requestContext.getRemoteAddr();
        	UtilityFunction utilityFunction = UtilityFunction.getInstance();
            List<String> tokenList = httpHeaders.getRequestHeader(HttpHeaderNames.AUTH_TOKEN);
            String token = tokenList!=null && tokenList.size()>0 ? tokenList.get(0) : null;
            
        	if(token==null)
        	{
            	JsonObjectBuilder jsonObject = Json.createObjectBuilder();
            	jsonObject.add( "status", ERROR.UNAUTHORIZED_ACTION );
            	jsonObject.add( "message", "Unauthorized action" );
                JsonObject jsonObj = jsonObject.build();
        		return UtilityHelper.getNoCacheResponseBuilder( Response.Status.UNAUTHORIZED ).entity( jsonObj.toString() ).build();
        	}
        	String requestId = token.substring(token.length()-10) + "-" + (new SimpleDateFormat("YYYYMMddHHmmss").format(new Date())) + ": ";
        	//log.inforequestId + "Proceed 1");
    		Response authResponse = utilityFunction.createNewTarget(lineCode, targetMonth, targetYear, ticketRevenue, courierRevenue, clientCode, token, requestId, ipAddress);
    	            

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
    @Path( "listTargets" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response listTargets(
    		@Context HttpHeaders httpHeaders,
    		@Context HttpServletRequest requestContext,
    	    @FormParam( "clientCode" ) String clientCode ) throws JSONException {

        

        try {
        	String ipAddress = requestContext.getRemoteAddr();
        	UtilityFunction utilityFunction = UtilityFunction.getInstance();
            List<String> tokenList = httpHeaders.getRequestHeader(HttpHeaderNames.AUTH_TOKEN);
            String token = tokenList!=null && tokenList.size()>0 ? tokenList.get(0) : null;
            
        	if(token==null)
        	{
            	JsonObjectBuilder jsonObject = Json.createObjectBuilder();
            	jsonObject.add( "status", ERROR.UNAUTHORIZED_ACTION );
            	jsonObject.add( "message", "Unauthorized action" );
                JsonObject jsonObj = jsonObject.build();
        		return UtilityHelper.getNoCacheResponseBuilder( Response.Status.UNAUTHORIZED ).entity( jsonObj.toString() ).build();
        	}
        	String requestId = token.substring(token.length()-10) + "-" + (new SimpleDateFormat("YYYYMMddHHmmss").format(new Date())) + ": ";
        	//log.inforequestId + "Proceed 1");
    		Response authResponse = utilityFunction.listTargets(token, clientCode, requestId, ipAddress);
    	            

            return authResponse;

        } catch ( final Exception ex ) {
        	ex.printStackTrace();
        	log.error(ex);
        	JsonObjectBuilder jsonObject = Json.createObjectBuilder();
        	jsonObject.add( "message", "Experienced a system server error " );
            JsonObject jsonObj = jsonObject.build();

            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.INTERNAL_SERVER_ERROR ).entity( jsonObj.toString() ).build();
        }
    }
    
    
    @POST
    @Path( "getTargetRevenueByFilter" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response getTargetRevenueByFilter(
    		@Context HttpHeaders httpHeaders,
    		@Context HttpServletRequest requestContext,
    	    @FormParam( "targetMonth" ) String targetMonth,
    		@FormParam( "targetId" ) Long targetId,
    	    @FormParam( "targetYear" ) Integer targetYear,
    	    @FormParam( "clientCode" ) String clientCode ) throws JSONException {

        

        try {
        	String ipAddress = requestContext.getRemoteAddr();
        	UtilityFunction utilityFunction = UtilityFunction.getInstance();
            List<String> tokenList = httpHeaders.getRequestHeader(HttpHeaderNames.AUTH_TOKEN);
            String token = tokenList!=null && tokenList.size()>0 ? tokenList.get(0) : null;
            
        	if(token==null)
        	{
            	JsonObjectBuilder jsonObject = Json.createObjectBuilder();
            	jsonObject.add( "status", ERROR.UNAUTHORIZED_ACTION );
            	jsonObject.add( "message", "Unauthorized action" );
                JsonObject jsonObj = jsonObject.build();
        		return UtilityHelper.getNoCacheResponseBuilder( Response.Status.UNAUTHORIZED ).entity( jsonObj.toString() ).build();
        	}
        	String requestId = token.substring(token.length()-10) + "-" + (new SimpleDateFormat("YYYYMMddHHmmss").format(new Date())) + ": ";
        	//log.inforequestId + "Proceed 1");
    		Response authResponse = utilityFunction.getTargetRevenueByFilter(targetMonth, targetYear, targetId, token, clientCode, requestId, ipAddress);
    	            

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
    @Path("summarizeAndCleanUp")
    @Produces(MediaType.APPLICATION_JSON)
    public Response summarizeAndCleanUp(
    		@Context HttpHeaders httpHeaders,
    		@Context HttpServletRequest requestContext,
    	    @QueryParam( "clientCode" ) String clientCode ) throws JSONException {

        

        try {
        	String ipAddress = requestContext.getRemoteAddr();
        	UtilityFunction utilityFunction = UtilityFunction.getInstance();
        	String requestId = (new SimpleDateFormat("YYYYMMddHHmmss").format(new Date())) + ": ";
        	//log.info"Proceed 2");
    		Response authResponse = utilityFunction.summarizeAndCleanUp(clientCode, requestId,
        			ipAddress);
    	            

            return authResponse;

        } catch ( final Exception ex ) {
        	ex.printStackTrace();
        	JsonObjectBuilder jsonObject = Json.createObjectBuilder();
        	jsonObject.add( "message", "Experienced a system server error " );
            JsonObject jsonObj = jsonObject.build();

            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.INTERNAL_SERVER_ERROR ).entity( jsonObj.toString() ).build();
        }
    }
}
