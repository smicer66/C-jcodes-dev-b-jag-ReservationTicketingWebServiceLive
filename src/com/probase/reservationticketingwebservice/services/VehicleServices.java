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
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
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

@Path( "services/VehicleServices" )
@Stateless( name = "VehicleService", mappedName = "services/VehicleServices" )
public class VehicleServices {

	private static Logger log = Logger.getLogger(VehicleServices.class);
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
    @Path( "createNewLine" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response createNewLine(
    		@Context HttpHeaders httpHeaders,
    		@Context HttpServletRequest requestContext,
            @FormParam( "lineName" ) String lineName,
            @FormParam( "lineCode" ) String lineCode,
            @FormParam( "editLineCode" ) String editLineCode,
            @FormParam( "clientCode" ) String clientCode) throws JSONException {

    	String ipAddress = requestContext.getRemoteAddr();
        VehicleFunction vehicleFunction = VehicleFunction.getInstance();
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
        	
            Response authResponse = vehicleFunction.createNewLine( token, lineName,
            		lineCode, requestId, ipAddress, clientCode, editLineCode );

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
    @Path( "listLines" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response listLines(
    		@Context HttpHeaders httpHeaders,
            @FormParam( "startIndex" ) Integer startIndex,
            @FormParam( "limit" ) Integer limit,
            @FormParam( "clientCode" ) String clientCode ) throws JSONException {

        VehicleFunction terminalFunction = VehicleFunction.getInstance();
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
        	
            Response authResponse = terminalFunction.listLines(startIndex, limit, token, requestId, clientCode );

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
    @Path( "createNewVehicle" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response createNewVehicle(
    		@Context HttpHeaders httpHeaders,
    		@Context HttpServletRequest requestContext,
            @FormParam( "vehicleName" ) String vehicleName,
            @FormParam( "manufacturer" ) String manufacturer,
            @FormParam( "speed" ) String speed,
            @FormParam( "editVehicleCode" ) String editVehicleCode,
            @FormParam( "vehicleType" ) String vehicleType,
            @FormParam( "inventoryNumber") String inventoryNumber,
            @FormParam( "trainNumber") String trainNumber,
            @FormParam( "maximumFuelCapacity" ) Double maximumFuelCapacity,
            @FormParam( "clientCode" ) String clientCode) throws JSONException {

    	String ipAddress = requestContext.getRemoteAddr();
        VehicleFunction vehicleFunction = VehicleFunction.getInstance();
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
        	
            Response authResponse = vehicleFunction.createNewVehicle( token, editVehicleCode, vehicleName, inventoryNumber,
            		manufacturer, speed, vehicleType, requestId, ipAddress, clientCode, maximumFuelCapacity, trainNumber );
            
            
            
            
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
    @Path( "upgradePurchasedTicket" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response upgradePurchasedTicket(
    		@Context HttpHeaders httpHeaders,
    		@Context HttpServletRequest requestContext,
            @FormParam( "probaseTransactionRef" ) String probaseTransactionRef,
            @FormParam( "deviceCode" ) String deviceCode,
            @FormParam( "clientCode" ) String clientCode,
            @FormParam( "receiptNo" ) String receiptNo,
            @FormParam( "paymentType" ) String paymentType,
            @FormParam( "purchasePoint" ) String purchasePoint,
            @FormParam( "passengerType" ) String passengerType,
            @FormParam( "newTravelClassCode" ) String newTravelClassCode, 
            @FormParam( "firstName" ) String firstName, 
            @FormParam( "lastName" ) String lastName, 
            @FormParam( "mobileNumber" ) String mobileNumber, 
            @FormParam( "nationalId" ) String nationalId, 
            @FormParam( "email" ) String email) throws JSONException {
		
		
		//log.info "probaseTransactionRef"  +  probaseTransactionRef);
        //log.info "deviceCode"  +  deviceCode);
        //log.info "clientCode"  +  clientCode);
        //log.info "receiptNo"  +  receiptNo);
        //log.info "paymentType"  +  paymentType);
        //log.info "purchasePoint"  +  purchasePoint);
        //log.info "passengerType"  +  passengerType);
        //log.info "newTravelClassCode"  +  newTravelClassCode); 
        //log.info "firstName"  +  firstName); 
        //log.info "lastName"  +  lastName); 
        //log.info "mobileNumber"  +  mobileNumber); 
        //log.info "nationalId"  +  nationalId); 
        //log.info "email"  +  email);

    	String ipAddress = requestContext.getRemoteAddr();
        VehicleFunction vehicleFunction = VehicleFunction.getInstance();
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
        	//log.infodeviceCode + " deviceCode");
        	//log.infoclientCode + " clientCode");
        	//log.inforeceiptNo + " receiptNo");
        	//log.infopassengerType + " passengerType");
        	//log.infonewTravelClassCode + " newTravelClassCode");
        	
            Response authResponse = vehicleFunction.upgradePurchasedTicket(probaseTransactionRef, deviceCode, paymentType, purchasePoint, receiptNo, passengerType, newTravelClassCode, requestId, 
        			ipAddress, clientCode, token, firstName, lastName, mobileNumber, nationalId, email);

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
    @Path( "compareTicketPrices" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response compareTicketPrices(
    		@Context HttpHeaders httpHeaders,
    		@Context HttpServletRequest requestContext,
            @FormParam( "deviceCode" ) String deviceCode,
            @FormParam( "clientCode" ) String clientCode,
            @FormParam( "receiptNo" ) String receiptNo,
            @FormParam( "passengerType" ) String passengerType,
            @FormParam( "newTravelClassCode" ) String newTravelClassCode) throws JSONException {

		
    	String ipAddress = requestContext.getRemoteAddr();
        VehicleFunction vehicleFunction = VehicleFunction.getInstance();
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
        	//log.infoclientCode + " clientCode");
        	
            Response authResponse = vehicleFunction.compareTicketPrices(deviceCode, receiptNo, passengerType, newTravelClassCode, requestId, ipAddress, clientCode, token);

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
    @Path( "createNewVehicleSeatSection" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response createNewVehicleSeatSection(
    		@Context HttpHeaders httpHeaders,
    		@Context HttpServletRequest requestContext,
            @FormParam( "sectionNames" ) String sectionNames,
            @FormParam( "clientCode" ) String clientCode) throws JSONException {

    	String ipAddress = requestContext.getRemoteAddr();
        VehicleFunction vehicleFunction = VehicleFunction.getInstance();
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
        	//log.infosectionNames + " sectionNames");
        	//log.infoclientCode + " clientCode");
        	
            Response authResponse = vehicleFunction.createNewVehicleSeatSection(token, sectionNames, requestId, ipAddress, clientCode);

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
    @Path( "createNewVehicleSeat" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response createNewVehicleSeat(
    		@Context HttpHeaders httpHeaders,
    		@Context HttpServletRequest requestContext,
            @FormParam( "vehicleCode" ) String vehicleCode,
            @FormParam( "vehicleSeats" ) String vehicleSeats,
            @FormParam( "clientCode" ) String clientCode) throws JSONException {

    	String ipAddress = requestContext.getRemoteAddr();
        VehicleFunction vehicleFunction = VehicleFunction.getInstance();
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


            Response authResponse = vehicleFunction.createNewVehicleSeat(token, vehicleSeats, requestId, ipAddress, clientCode);
            
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
    @Path( "getCustomerPurchasedTripsByCustomerMobileNumber" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response getCustomerPurchasedTripsByCustomerMobileNumber(
    		@Context HttpHeaders httpHeaders,
    		@Context HttpServletRequest requestContext,
    		@QueryParam( "customerMobileNumber" ) String customerMobileNumber,
    		@QueryParam( "deviceCode" ) String deviceCode,
    		@QueryParam( "startDate" ) String startDate,
    		@QueryParam( "endDate" ) String endDate,
    		@QueryParam( "clientCode" ) String clientCode) throws JSONException {

    	String ipAddress = requestContext.getRemoteAddr();
        VehicleFunction vehicleFunction = VehicleFunction.getInstance();
        List<String> tokenList = httpHeaders.getRequestHeader(HttpHeaderNames.AUTH_TOKEN);
        String token = tokenList!=null && tokenList.size()>0 ? tokenList.get(0) : null;

        try {
        	
        	String requestId = (token==null ? "" : (token.substring(token.length()-10))) + "-" + (new SimpleDateFormat("YYYYMMddHHmmss").format(new Date())) + ": ";
        	//log.inforequestId + "Proceed 1");
        	//log.info"customerMobileNumber" + customerMobileNumber);
        	//log.info"clientCode" + clientCode);
        	Enumeration<String> pnames = requestContext.getParameterNames();
        	while(pnames.hasMoreElements())
        	{
        		//log.infopnames.nextElement().toString());
        	}
        		
        	//log.info"clientCode" + clientCode);
        	
            Response authResponse = vehicleFunction.getCustomerPurchasedTripsByCustomerMobileNumber(deviceCode, token, customerMobileNumber, requestId, ipAddress, clientCode, startDate, endDate);

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
    @Path( "getVehicleTripDetailsByTransactionRef" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response getVehicleTripDetailsByTransactionRef(
    		@Context HttpHeaders httpHeaders,
    		@Context HttpServletRequest requestContext,
    		@QueryParam( "orderRef" ) String orderRef,
    		@QueryParam( "deviceCode" ) String deviceCode,
    		@QueryParam( "clientCode" ) String clientCode) throws JSONException {

    	String ipAddress = requestContext.getRemoteAddr();
        VehicleFunction vehicleFunction = VehicleFunction.getInstance();
        List<String> tokenList = httpHeaders.getRequestHeader(HttpHeaderNames.AUTH_TOKEN);
        String token = tokenList!=null && tokenList.size()>0 ? tokenList.get(0) : null;

        try {
        	
        	String requestId = (token==null ? "" : (token.substring(token.length()-10))) + "-" + (new SimpleDateFormat("YYYYMMddHHmmss").format(new Date())) + ": ";
        	//log.inforequestId + "Proceed 1");
        	//log.info"orderRef" + orderRef);
        	//log.info"clientCode" + clientCode);
        	Enumeration<String> pnames = requestContext.getParameterNames();
        	while(pnames.hasMoreElements())
        	{
        		//log.infopnames.nextElement().toString());
        	}
        		
        	//log.info"clientCode" + clientCode);
        	
            Response authResponse = vehicleFunction.getVehicleTripDetailsByTransactionRef(deviceCode, token, orderRef, requestId, ipAddress, clientCode);

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
    @Path( "getCustomerPurchasedTripsByCustomerName" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response getCustomerPurchasedTripsByCustomerName(
    		@Context HttpHeaders httpHeaders,
    		@Context HttpServletRequest requestContext,
    		@QueryParam( "firstName" ) String firstName,
    		@QueryParam( "lastName" ) String lastName,
    		@QueryParam( "deviceCode" ) String deviceCode,
    		@QueryParam( "startDate" ) String startDate,
    		@QueryParam( "endDate" ) String endDate,
    		@QueryParam( "clientCode" ) String clientCode) throws JSONException {

    	String ipAddress = requestContext.getRemoteAddr();
        VehicleFunction vehicleFunction = VehicleFunction.getInstance();
        List<String> tokenList = httpHeaders.getRequestHeader(HttpHeaderNames.AUTH_TOKEN);
        String token = tokenList!=null && tokenList.size()>0 ? tokenList.get(0) : null;

        try {
        	
        	String requestId = (token==null ? "" : (token.substring(token.length()-10))) + "-" + (new SimpleDateFormat("YYYYMMddHHmmss").format(new Date())) + ": ";
        	//log.inforequestId + "Proceed 1");
        	//log.info"firstName" + firstName);
        	//log.info"lastName" + lastName);
        	//log.info"clientCode" + clientCode);
        	Enumeration<String> pnames = requestContext.getParameterNames();
        	while(pnames.hasMoreElements())
        	{
        		//log.infopnames.nextElement().toString());
        	}
        		
        	//log.info"clientCode" + clientCode);
        	
            Response authResponse = vehicleFunction.getCustomerPurchasedTripsByCustomerName(deviceCode, token, firstName, lastName, requestId, ipAddress, clientCode, startDate, endDate);

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
    @Path( "listAvailableTrips" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response listAvailableTrips(
    		@Context HttpHeaders httpHeaders,
    		@Context HttpServletRequest requestContext,
            @FormParam( "departureStationCode" ) String departureStationCode,
            @FormParam( "arrivalStationCode" ) String arrivalStationCode,
            @FormParam( "departureTime" ) String departureTime,
            @FormParam( "returnDate" ) String returnDate,
            @FormParam( "hoursAdd" ) Integer hoursAdd,
            @FormParam( "tripClass" ) String tripClass,
            @FormParam( "clientCode" ) String clientCode) throws JSONException {

    	String ipAddress = requestContext.getRemoteAddr();
        VehicleFunction vehicleFunction = VehicleFunction.getInstance();
        List<String> tokenList = httpHeaders.getRequestHeader(HttpHeaderNames.AUTH_TOKEN);
        String token = tokenList!=null && tokenList.size()>0 ? tokenList.get(0) : null;

        try {
        	
        	String requestId = (token==null ? "" : (token.substring(token.length()-10))) + "-" + (new SimpleDateFormat("YYYYMMddHHmmss").format(new Date())) + ": ";
        	//log.inforequestId + "Proceed 1");
        	//log.info"departureStationCode" + departureStationCode);
        	//log.info"arrivalStationCode" + departureTime);
        	//log.info"departureTime" + departureTime);
        	//log.info"clientCode" + clientCode);
        	Enumeration<String> pnames = requestContext.getParameterNames();
        	while(pnames.hasMoreElements())
        	{
        		//log.infopnames.nextElement().toString());
        	}
        		
        	//log.info"clientCode" + clientCode);
        	Boolean returnTrip = Boolean.TRUE;
        	if(returnDate==null)
        		returnTrip = Boolean.FALSE;
        	
            Response authResponse = vehicleFunction.listAvailableTrips(token, departureStationCode, arrivalStationCode, 
        			departureTime, returnDate, hoursAdd, tripClass, returnTrip, requestId, ipAddress, clientCode);

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
    @Path( "searchAvailableTrips" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response searchAvailableTrips(
    		@Context HttpHeaders httpHeaders,
    		@Context HttpServletRequest requestContext,
    		@FormParam( "deviceCode" ) String deviceCode,
            @FormParam( "departureStationCode" ) String departureStationCode,
            @FormParam( "arrivalStationCode" ) String arrivalStationCode,
            @FormParam( "passengerDetails" ) String passengerDetails,
            @FormParam( "departureTime" ) String departureTime,
            @FormParam( "returnDate" ) String returnDate,
            @FormParam( "hoursAdd" ) Integer hoursAdd,
            @FormParam( "tripClass" ) String tripClass,
            @FormParam( "clientCode" ) String clientCode) throws JSONException {

    	String ipAddress = requestContext.getRemoteAddr();
        VehicleFunction vehicleFunction = VehicleFunction.getInstance();
        List<String> tokenList = httpHeaders.getRequestHeader(HttpHeaderNames.AUTH_TOKEN);
        String token = tokenList!=null && tokenList.size()>0 ? tokenList.get(0) : null;

        try {
        	
        	String requestId = (token==null ? "" : (token.substring(token.length()-10))) + "-" + (new SimpleDateFormat("YYYYMMddHHmmss").format(new Date())) + ": ";
        	//log.inforequestId + "Proceed 1");
        	//log.info"departureStationCode" + departureStationCode);
        	//log.info"arrivalStationCode" + departureTime);
        	//log.info"departureTime" + departureTime);
        	//log.info"clientCode" + clientCode);
        	Enumeration<String> pnames = requestContext.getParameterNames();
        	while(pnames.hasMoreElements())
        	{
        		//log.infopnames.nextElement().toString());
        	}
        		
        	//log.info"clientCode" + clientCode);
        	Boolean returnTrip = Boolean.TRUE;
        	if(returnDate==null)
        		returnTrip = Boolean.FALSE;
        	
            Response authResponse = vehicleFunction.searchAvailableTrips(deviceCode, token, departureStationCode, arrivalStationCode, passengerDetails,
        			departureTime, returnDate, hoursAdd, tripClass, returnTrip, requestId, ipAddress, clientCode);

            return authResponse;

        } catch ( final Exception ex ) {
        	log.error(ex);
        	ex.printStackTrace();
        	JsonObjectBuilder jsonObject = Json.createObjectBuilder();
        	jsonObject.add( "message", "Experienced a system server error " );
            JsonObject jsonObj = jsonObject.build();

            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.INTERNAL_SERVER_ERROR ).entity( jsonObj.toString() ).build();
        }
    }
    
    
    
    @POST
    @Path( "searchAvailableTripsAndLockdown" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response searchAvailableTripsAndLockdown(
    		@Context HttpHeaders httpHeaders,
    		@Context HttpServletRequest requestContext,
    		@FormParam( "deviceCode" ) String deviceCode,
            @FormParam( "departureStationCode" ) String departureStationCode,
            @FormParam( "arrivalStationCode" ) String arrivalStationCode,
            @FormParam( "passengerDetails" ) String passengerDetails,
            @FormParam( "departureTime" ) String departureTime,
            @FormParam( "returnDate" ) String returnDate,
            @FormParam( "hoursAdd" ) Integer hoursAdd,
            @FormParam( "tripClass" ) String tripClass,
            @FormParam( "clientCode" ) String clientCode) throws JSONException {

    	String ipAddress = requestContext.getRemoteAddr();
        VehicleFunction vehicleFunction = VehicleFunction.getInstance();
        List<String> tokenList = httpHeaders.getRequestHeader(HttpHeaderNames.AUTH_TOKEN);
        String token = tokenList!=null && tokenList.size()>0 ? tokenList.get(0) : null;

        try {
        	
        	String requestId = (token==null ? "" : (token.substring(token.length()-10))) + "-" + (new SimpleDateFormat("YYYYMMddHHmmss").format(new Date())) + ": ";
        	//log.inforequestId + "Proceed 1");
        	//log.info"departureStationCode" + departureStationCode);
        	//log.info"arrivalStationCode" + departureTime);
        	//log.info"departureTime" + departureTime);
        	//log.info"clientCode" + clientCode);
        	Enumeration<String> pnames = requestContext.getParameterNames();
        	while(pnames.hasMoreElements())
        	{
        		//log.infopnames.nextElement().toString());
        	}
        		
        	//log.info"clientCode" + clientCode);
        	Boolean returnTrip = Boolean.TRUE;
        	if(returnDate==null)
        		returnTrip = Boolean.FALSE;
        	
            Response authResponse = vehicleFunction.searchAvailableTripsAndLockdown(deviceCode, token, departureStationCode, arrivalStationCode, passengerDetails,
        			departureTime, returnDate, hoursAdd, tripClass, returnTrip, requestId, ipAddress, clientCode);

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
    @Path( "getVehicleSeatAndLockDown" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response getVehicleSeatAndLockDown(
    		@Context HttpHeaders httpHeaders,
    		@Context HttpServletRequest requestContext,
            @FormParam( "tripCode" ) String tripCode,
            @FormParam( "departingStationCode" ) String departingStationCode,
            @FormParam( "arrivalStationCode" ) String arrivalStationCode,
            @FormParam( "seatDetails" ) String seatDetails,
            @FormParam( "generalTripClass" ) String generalTripClass,
            @FormParam( "forcePreferences" ) Boolean forcePreferences,
            @FormParam( "lockDownMode" ) Boolean lockDownMode,
            @FormParam( "clientCode" ) String clientCode) throws JSONException {

    	String ipAddress = requestContext.getRemoteAddr();
        VehicleFunction vehicleFunction = VehicleFunction.getInstance();
        List<String> tokenList = httpHeaders.getRequestHeader(HttpHeaderNames.AUTH_TOKEN);
        String token = tokenList!=null && tokenList.size()>0 ? tokenList.get(0) : null;
        List<String> purchaseTokenList = httpHeaders.getRequestHeader(HttpHeaderNames.TICKET_PURCHASE_TOKEN);
        String purchaseToken = purchaseTokenList!=null && purchaseTokenList.size()>0 ? purchaseTokenList.get(0) : null;
        //log.info"tripToken = " + purchaseToken);
        MultivaluedMap<String, String> mvm = httpHeaders.getRequestHeaders();
        Set<String> set = mvm.keySet();
        Iterator<String> setIter = set.iterator();
        while(setIter.hasNext())
        {
        	String setIterString = setIter.next();
        	//log.infosetIterString + " = " + mvm.getFirst(setIterString));
        }
        
        try {
        	String requestId = (token==null ? "" : (token.substring(token.length()-10))) + "-" + (new SimpleDateFormat("YYYYMMddHHmmss").format(new Date())) + ": ";
        	//log.inforequestId + "Proceed 1");
        	//log.info"clientCode" + clientCode);
        	Enumeration<String> pnames = requestContext.getParameterNames();
        	while(pnames.hasMoreElements())
        	{
        		//log.infopnames.nextElement().toString());
        	}
        		
        	//log.info"clientCode" + clientCode);
        	if(forcePreferences==null)
        		forcePreferences = Boolean.FALSE;
        	
            Response authResponse = vehicleFunction.getVehicleSeatAndLockDown(lockDownMode, token, tripCode, departingStationCode, arrivalStationCode, seatDetails, 
            		forcePreferences, generalTripClass, requestId, ipAddress, clientCode, purchaseToken);
            
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
    @Path( "getTripSeatAndLockDown" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response getTripSeatAndLockDown(
    		@Context HttpHeaders httpHeaders,
    		@Context HttpServletRequest requestContext,
    		@FormParam ( "deviceCode" ) String deviceCode,
            @FormParam( "lockdownSeats" ) Boolean lockdownSeats,
            @FormParam( "outwardTrip" ) String outwardTrip,
            @FormParam( "inwardTrip" ) String inwardTrip,
            @FormParam( "clientCode" ) String clientCode) throws JSONException {

    	String ipAddress = requestContext.getRemoteAddr();
        VehicleFunction vehicleFunction = VehicleFunction.getInstance();
        List<String> tokenList = httpHeaders.getRequestHeader(HttpHeaderNames.AUTH_TOKEN);
        String token = tokenList!=null && tokenList.size()>0 ? tokenList.get(0) : null;
        List<String> purchaseTokenList = httpHeaders.getRequestHeader(HttpHeaderNames.TICKET_PURCHASE_TOKEN);
        String purchaseToken = purchaseTokenList!=null && purchaseTokenList.size()>0 ? purchaseTokenList.get(0) : null;
        //log.info"tripToken = " + purchaseToken);
        MultivaluedMap<String, String> mvm = httpHeaders.getRequestHeaders();
        Set<String> set = mvm.keySet();
        Iterator<String> setIter = set.iterator();
        while(setIter.hasNext())
        {
        	String setIterString = setIter.next();
        	//log.infosetIterString + " = " + mvm.getFirst(setIterString));
        }
        
        try {
        	String requestId = (token==null ? "" : (token.substring(token.length()-10))) + "-" + (new SimpleDateFormat("YYYYMMddHHmmss").format(new Date())) + ": ";
        	//log.inforequestId + "Proceed 1");
        	//log.info"clientCode" + clientCode);
        	Enumeration<String> pnames = requestContext.getParameterNames();
        	while(pnames.hasMoreElements())
        	{
        		//log.infopnames.nextElement().toString());
        	}
        		
        	//log.info"clientCode" + clientCode);
        	
            Response authResponse = vehicleFunction.getTripSeatAndLockDown(deviceCode, lockdownSeats, token, outwardTrip, inwardTrip, requestId, ipAddress, clientCode, purchaseToken);
            
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
    @Path( "purchaseVehicleTripGroupTickets" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response purchaseVehicleTripGroupTickets(
    		@Context HttpHeaders httpHeaders,
    		@Context HttpServletRequest requestContext,
            @FormParam( "groupTicketCode" ) String groupTicketCode,
            @FormParam( "paymentType" ) String paymentType,
            @FormParam( "transactionRef" ) String transactionRef,
            @FormParam( "deviceCode" ) String deviceCode, 
            @FormParam( "ticketCollectionCenterCode" ) String ticketCollectionCenterCode, 
            @FormParam( "purchaseDetails" ) String purchaseDetails, 
            @FormParam( "generalTripClass" ) String generalTripClass, 
            @FormParam( "clientCode" ) String clientCode, 
            @FormParam( "couponCode" ) String couponCode, 
            @FormParam( "purchasePoint" ) String purchasePoint,
            @FormParam( "concessionAmount" ) Double concessionAmount,
            @FormParam( "hash" ) String hash) throws JSONException {

    	String ipAddress = requestContext.getRemoteAddr();
        VehicleFunction vehicleFunction = VehicleFunction.getInstance();
        List<String> tokenList = httpHeaders.getRequestHeader(HttpHeaderNames.AUTH_TOKEN);
        String token = tokenList!=null && tokenList.size()>0 ? tokenList.get(0) : null;
        List<String> purchaseTokenList = httpHeaders.getRequestHeader(HttpHeaderNames.TICKET_PURCHASE_TOKEN);
        String purchaseToken = purchaseTokenList!=null && purchaseTokenList.size()>0 ? purchaseTokenList.get(0) : null;

        try {
        	
        	//log.info"tripToken = " + purchaseToken);
            MultivaluedMap<String, String> mvm = httpHeaders.getRequestHeaders();
            Set<String> set = mvm.keySet();
            Iterator<String> setIter = set.iterator();
            while(setIter.hasNext())
            {
            	String setIterString = setIter.next();
            	//log.infosetIterString + " = " + mvm.getFirst(setIterString));
            }
            //log.info"paymentType = " + paymentType);
            //log.info"deviceCode = " + deviceCode);
            //log.info"ticketCollectionCenterCode = " + ticketCollectionCenterCode);
            //log.info"purchaseToken = " + purchaseToken);
            //log.info"purchaseDetails = " + purchaseDetails);
            //log.info"generalTripClass = " + generalTripClass);
            
        	String requestId = (token==null ? "" : (token.substring(token.length()-10))) + "-" + (new SimpleDateFormat("YYYYMMddHHmmss").format(new Date())) + ": ";
        	//log.inforequestId + "Proceed 1");
        	//log.info"clientCode" + clientCode);
        	Enumeration<String> pnames = requestContext.getParameterNames();
        	while(pnames.hasMoreElements())
        	{
        		//log.infopnames.nextElement().toString());
        	}
        		
        	//log.info"clientCode" + clientCode);
        	
        	Response authResponse = vehicleFunction.purchaseVehicleTripGroupTickets(purchasePoint, groupTicketCode, paymentType, transactionRef, deviceCode, 
        			ticketCollectionCenterCode, purchaseToken, purchaseDetails, generalTripClass, requestId, ipAddress, clientCode, token, couponCode, concessionAmount);

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
    @Path( "purchaseVehicleTripTickets" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response purchaseVehicleTripTickets(
    		@Context HttpHeaders httpHeaders,
    		@Context HttpServletRequest requestContext,
            @FormParam( "paymentType" ) String paymentType,
            @FormParam( "transactionRef" ) String transactionRef,
            @FormParam( "deviceCode" ) String deviceCode, 
            @FormParam( "ticketCollectionCenterCode" ) String ticketCollectionCenterCode, 
            @FormParam( "purchaseDetails" ) String purchaseDetails, 
            @FormParam( "generalTripClass" ) String generalTripClass, 
            @FormParam( "clientCode" ) String clientCode, 
            @FormParam( "concessionAmount" ) Double concessionAmount, 
            @FormParam( "couponCode" ) String couponCode, 
            @FormParam( "purchasePoint") String purchasePoint,
            @FormParam( "hash" ) String hash) throws JSONException {

    	String ipAddress = requestContext.getRemoteAddr();
        VehicleFunction vehicleFunction = VehicleFunction.getInstance();
        List<String> tokenList = httpHeaders.getRequestHeader(HttpHeaderNames.AUTH_TOKEN);
        String token = tokenList!=null && tokenList.size()>0 ? tokenList.get(0) : null;
        List<String> purchaseTokenList = httpHeaders.getRequestHeader(HttpHeaderNames.TICKET_PURCHASE_TOKEN);
        String purchaseToken = purchaseTokenList!=null && purchaseTokenList.size()>0 ? purchaseTokenList.get(0) : null;

        try {
        	
        	//log.info"tripToken = " + purchaseToken);
            MultivaluedMap<String, String> mvm = httpHeaders.getRequestHeaders();
            Set<String> set = mvm.keySet();
            Iterator<String> setIter = set.iterator();
            while(setIter.hasNext())
            {
            	String setIterString = setIter.next();
            	//log.infosetIterString + " = " + mvm.getFirst(setIterString));
            }
            //log.info"paymentType = " + paymentType);
            //log.info"deviceCode = " + deviceCode);
            //log.info"ticketCollectionCenterCode = " + ticketCollectionCenterCode);
            //log.info"purchaseToken = " + purchaseToken);
            //log.info"purchaseDetails = " + purchaseDetails);
            //log.info"generalTripClass = " + generalTripClass);
            
        	String requestId = (token==null ? "" : (token.substring(token.length()-10))) + "-" + (new SimpleDateFormat("YYYYMMddHHmmss").format(new Date())) + ": ";
        	//log.inforequestId + "Proceed 1");
        	//log.info"clientCode" + clientCode);
        	Enumeration<String> pnames = requestContext.getParameterNames();
        	while(pnames.hasMoreElements())
        	{
        		//log.infopnames.nextElement().toString());
        	}
        		
        	//log.info"clientCode" + clientCode);
        	
        	Response authResponse = vehicleFunction.purchaseVehicleTripTickets(purchasePoint, concessionAmount, couponCode, paymentType, transactionRef, deviceCode, ticketCollectionCenterCode, 
        			purchaseToken, purchaseDetails, generalTripClass, requestId, ipAddress, clientCode, token);

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
    @Path( "surchargePurchasedTrip" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response surchargePurchasedTrip(
    		@Context HttpHeaders httpHeaders,
    		@Context HttpServletRequest requestContext,
    		@FormParam( "deviceCode" ) String deviceCode, 
    		@FormParam( "surchargeAmount" ) Double surchargeAmount, 
    		@FormParam( "receiptNo" ) String receiptNo,
    		@FormParam( "clientCode" ) String clientCode) throws JSONException {
    	
    	
    	String ipAddress = requestContext.getRemoteAddr();
        VehicleFunction vehicleFunction = VehicleFunction.getInstance();
        List<String> tokenList = httpHeaders.getRequestHeader(HttpHeaderNames.AUTH_TOKEN);
        String token = tokenList!=null && tokenList.size()>0 ? tokenList.get(0) : null;

        try {
        	
            MultivaluedMap<String, String> mvm = httpHeaders.getRequestHeaders();
            Set<String> set = mvm.keySet();
            Iterator<String> setIter = set.iterator();
            while(setIter.hasNext())
            {
            	String setIterString = setIter.next();
            	//log.infosetIterString + " = " + mvm.getFirst(setIterString));
            }

            
        	String requestId = (new SimpleDateFormat("YYYYMMddHHmmss").format(new Date())) + ": ";
        	//log.inforequestId + "Proceed 1");
        	//log.info"clientCode" + clientCode);
        	Enumeration<String> pnames = requestContext.getParameterNames();
        	while(pnames.hasMoreElements())
        	{
        		//log.infopnames.nextElement().toString());
        	}
        		
        	//log.info"clientCode" + clientCode);
        	
        	Response authResponse = vehicleFunction.surchargePurchasedTrip
        			(receiptNo, deviceCode, token, ipAddress, requestId, clientCode);

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
    @Path( "submitGroupTripRequest" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response submitGroupTripRequest(
    		@Context HttpHeaders httpHeaders,
    		@Context HttpServletRequest requestContext,
    		@FormParam( "deviceCode" ) String deviceCode, 
    		@FormParam( "departingStation" ) String departingStation, 
    		@FormParam( "arrivingStation" ) String arrivingStation, 
    		@FormParam( "stationCabinType" ) String stationCabinType, 
    		@FormParam( "departureTime" ) String departureTime, 
    		@FormParam( "returnDate" ) String returnDate, 
    		@FormParam( "tripType" ) String tripType, 
    		@FormParam( "details" ) String details, 
    		@FormParam( "firstName" ) String firstName, 
    		@FormParam( "lastName" ) String lastName, 
    		@FormParam( "emailAddress" ) String emailAddress, 
    		@FormParam( "altmobileNumber" ) String altmobileNumber, 
    		@FormParam( "mobileNumber" ) String mobileNumber, 
    		@FormParam( "nationalId" ) String nationalId, 
    		@FormParam( "adultPassengerCount" ) Integer adultPassengerCount, 
    		@FormParam( "childPassengerCount" ) Integer childPassengerCount, 
    		@FormParam( "seniorPassengerCount" ) Integer seniorPassengerCount, 
    		@FormParam( "disabledPassengerCount" ) Integer disabledPassengerCount, 
    		@FormParam( "clientCode" ) String clientCode) throws JSONException {
		
    	String ipAddress = requestContext.getRemoteAddr();
        VehicleFunction vehicleFunction = VehicleFunction.getInstance();
        List<String> tokenList = httpHeaders.getRequestHeader(HttpHeaderNames.AUTH_TOKEN);
        String token = tokenList!=null && tokenList.size()>0 ? tokenList.get(0) : null;

        try {
        	
            MultivaluedMap<String, String> mvm = httpHeaders.getRequestHeaders();
            Set<String> set = mvm.keySet();
            Iterator<String> setIter = set.iterator();
            while(setIter.hasNext())
            {
            	String setIterString = setIter.next();
            	//log.infosetIterString + " = " + mvm.getFirst(setIterString));
            }

            
        	String requestId = (new SimpleDateFormat("YYYYMMddHHmmss").format(new Date())) + ": ";
        	//log.inforequestId + "Proceed 1");
        	//log.info"clientCode" + clientCode);
        	Enumeration<String> pnames = requestContext.getParameterNames();
        	while(pnames.hasMoreElements())
        	{
        		//log.infopnames.nextElement().toString());
        	}
        		
        	//log.info"clientCode" + clientCode);
        	
        	Response authResponse = vehicleFunction.submitGroupTripRequest(deviceCode, departingStation, 
        			arrivingStation, stationCabinType, departureTime, returnDate, tripType, details, firstName, 
        			lastName, emailAddress, mobileNumber, altmobileNumber, nationalId, adultPassengerCount, 
        			childPassengerCount, seniorPassengerCount, disabledPassengerCount,
        			requestId, ipAddress, clientCode, token);

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
    @Path( "listCheckAvailableGroupTrip" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response listCheckAvailableGroupTrip(
    		@Context HttpHeaders httpHeaders,
    		@Context HttpServletRequest requestContext,
    		@FormParam( "deviceCode" ) String deviceCode, 
    		@FormParam( "requestCode" ) String requestCode,
    		/*@FormParam( "seatClass" ) String seatClass, 
    		@FormParam( "adultPassenger" ) Integer adultPassenger, 
    		@FormParam( "childPassenger" ) Integer childPassenger, 
    		@FormParam( "seniorPassenger" ) Integer seniorPassenger, 
    		@FormParam( "disabledPassenger" ) Integer disabledPassenger,*/
    		@FormParam( "clientCode" ) String clientCode) throws JSONException {
		
    	String ipAddress = requestContext.getRemoteAddr();
        VehicleFunction vehicleFunction = VehicleFunction.getInstance();
        List<String> tokenList = httpHeaders.getRequestHeader(HttpHeaderNames.AUTH_TOKEN);
        String token = tokenList!=null && tokenList.size()>0 ? tokenList.get(0) : null;

        try {
        	
            MultivaluedMap<String, String> mvm = httpHeaders.getRequestHeaders();
            Set<String> set = mvm.keySet();
            Iterator<String> setIter = set.iterator();
            while(setIter.hasNext())
            {
            	String setIterString = setIter.next();
            	//log.infosetIterString + " = " + mvm.getFirst(setIterString));
            }

            
        	String requestId = (new SimpleDateFormat("YYYYMMddHHmmss").format(new Date())) + ": ";
        	//log.inforequestId + "Proceed 1");
        	//log.info"clientCode" + clientCode);
        	Enumeration<String> pnames = requestContext.getParameterNames();
        	while(pnames.hasMoreElements())
        	{
        		//log.infopnames.nextElement().toString());
        	}
        		
        	//log.info"clientCode" + clientCode);
        	/*seatClass, adultPassenger, 
        			childPassenger, seniorPassenger, disabledPassenger, */
        	Response authResponse = vehicleFunction.listCheckAvailableGroupTrip(deviceCode, requestCode,
        			requestId, ipAddress, clientCode, token);
        	

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
    @Path( "refundPurchasedTripTransaction" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response refundPurchasedTripTransaction(
    		@Context HttpHeaders httpHeaders,
    		@Context HttpServletRequest requestContext,
            @FormParam( "deviceCode" ) String deviceCode, 
            @FormParam( "clientCode" ) String clientCode, 
            @FormParam( "orderRef" ) String orderRef) throws JSONException {
		
    	String ipAddress = requestContext.getRemoteAddr();
        VehicleFunction vehicleFunction = VehicleFunction.getInstance();
        List<String> tokenList = httpHeaders.getRequestHeader(HttpHeaderNames.AUTH_TOKEN);
        String token = tokenList!=null && tokenList.size()>0 ? tokenList.get(0) : null;

        try {
        	
            MultivaluedMap<String, String> mvm = httpHeaders.getRequestHeaders();
            Set<String> set = mvm.keySet();
            Iterator<String> setIter = set.iterator();
            while(setIter.hasNext())
            {
            	String setIterString = setIter.next();
            	//log.infosetIterString + " = " + mvm.getFirst(setIterString));
            }

            //log.info"deviceCode = " + deviceCode);
            //log.info"orderRef = " + orderRef);
            
        	String requestId = (new SimpleDateFormat("YYYYMMddHHmmss").format(new Date())) + ": ";
        	//log.inforequestId + "Proceed 1");
        	//log.info"clientCode" + clientCode);
        	Enumeration<String> pnames = requestContext.getParameterNames();
        	while(pnames.hasMoreElements())
        	{
        		//log.infopnames.nextElement().toString());
        	}
        		
        	//log.info"clientCode" + clientCode);
        	
        	Response authResponse = vehicleFunction.refundPurchasedTripTransaction(deviceCode, 
				orderRef, requestId, ipAddress, clientCode, token);

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
    @Path( "cancelVehicleTripSeatsLocked" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response cancelVehicleTripSeatsLocked(
    		@Context HttpHeaders httpHeaders,
    		@Context HttpServletRequest requestContext,
            @FormParam( "deviceCode" ) String deviceCode, 
            @FormParam( "clientCode" ) String clientCode,  
            @FormParam( "generalTripClass" ) String generalTripClass, 
            @FormParam( "purchaseDetails" ) String purchaseDetails) throws JSONException {
		
    	String ipAddress = requestContext.getRemoteAddr();
        VehicleFunction vehicleFunction = VehicleFunction.getInstance();
        List<String> tokenList = httpHeaders.getRequestHeader(HttpHeaderNames.AUTH_TOKEN);
        String token = tokenList!=null && tokenList.size()>0 ? tokenList.get(0) : null;
        List<String> purchaseTokenList = httpHeaders.getRequestHeader(HttpHeaderNames.TICKET_PURCHASE_TOKEN);
        String purchaseToken = purchaseTokenList!=null && purchaseTokenList.size()>0 ? purchaseTokenList.get(0) : null;

        try {
        	
        	//log.info"tripToken = " + purchaseToken);
            MultivaluedMap<String, String> mvm = httpHeaders.getRequestHeaders();
            Set<String> set = mvm.keySet();
            Iterator<String> setIter = set.iterator();
            while(setIter.hasNext())
            {
            	String setIterString = setIter.next();
            	//log.infosetIterString + " = " + mvm.getFirst(setIterString));
            }

            //log.info"deviceCode = " + deviceCode);
            //log.info"purchaseToken = " + purchaseToken);
            //log.info"purchaseDetails = " + purchaseDetails);
            //log.info"generalTripClass = " + generalTripClass);
            
        	String requestId = (token==null ? "" : (token.substring(token.length()-10))) + "-" + (new SimpleDateFormat("YYYYMMddHHmmss").format(new Date())) + ": ";
        	//log.inforequestId + "Proceed 1");
        	//log.info"clientCode" + clientCode);
        	Enumeration<String> pnames = requestContext.getParameterNames();
        	while(pnames.hasMoreElements())
        	{
        		//log.infopnames.nextElement().toString());
        	}
        		
        	//log.info"clientCode" + clientCode);
        	
        	Response authResponse = vehicleFunction.cancelVehicleTripSeatsLocked(deviceCode, purchaseToken, 
				purchaseDetails, generalTripClass, requestId, ipAddress, clientCode, token);

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
    @Path( "listVehicles" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response listVehicles(
    		@Context HttpHeaders httpHeaders,
            @FormParam( "startIndex" ) Integer startIndex,
            @FormParam( "limit" ) Integer limit,
            @FormParam( "clientCode" ) String clientCode ) throws JSONException {

        VehicleFunction terminalFunction = VehicleFunction.getInstance();
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
        	
            Response authResponse = terminalFunction.listVehicles(startIndex, limit, token, requestId, clientCode );

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
    @Path( "approveGroupTripRequest" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response approveGroupTripRequest(
    		@Context HttpHeaders httpHeaders,
    		@Context HttpServletRequest requestContext,
            @FormParam( "requestCode" ) String requestCode,
            @FormParam( "action" ) String action,
            @FormParam( "clientCode" ) String clientCode ) throws JSONException {

    	String ipAddress = requestContext.getRemoteAddr();
        VehicleFunction vehicleFunction = VehicleFunction.getInstance();
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
        	
            Response authResponse = vehicleFunction.approveGroupTripRequest(requestCode, action, token, requestId, clientCode, ipAddress );

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
    @Path( "listGroupTripRequests" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response listGroupTripRequests(
    		@Context HttpHeaders httpHeaders,
            @QueryParam( "clientCode" ) String clientCode ) throws JSONException {

        VehicleFunction vehicleFunction = VehicleFunction.getInstance();
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
        	
            Response authResponse = vehicleFunction.listGroupTripRequests(token, requestId, clientCode );

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
    @Path( "getGroupTripRequest" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response getGroupTripRequest(
    		@Context HttpHeaders httpHeaders,
            @QueryParam( "requestCode" ) String requestCode,
            @QueryParam( "clientCode" ) String clientCode ) throws JSONException {

        VehicleFunction vehicleFunction = VehicleFunction.getInstance();
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
        	
            Response authResponse = vehicleFunction.getGroupTripRequest(token, requestId, clientCode, requestCode );

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
    @Path( "getVehicle" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response getVehicle(
    		@Context HttpHeaders httpHeaders,
            @FormParam( "vehicleCode" ) String vehicleCode,
            @FormParam( "clientCode" ) String clientCode ) throws JSONException {

        VehicleFunction vehicleFunction = VehicleFunction.getInstance();
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
            Response authResponse = vehicleFunction.getVehicle( vehicleCode, token, requestId, clientCode );

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
    @Path( "updateVehicleStatus" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response updateVehicleStatus(
    		@Context HttpHeaders httpHeaders,
            @QueryParam( "vehicleCode" ) String vehicleCode,
            @QueryParam( "status" ) String status,
            @QueryParam( "clientCode" ) String clientCode ) throws JSONException {

        VehicleFunction vehicleFunction = VehicleFunction.getInstance();
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
            Response authResponse = vehicleFunction.updateVehicleStatus( vehicleCode, status, token, requestId, clientCode );

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
    @Path( "getTripLine" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response getTripLine(
    		@Context HttpHeaders httpHeaders,
            @FormParam( "lineCode" ) String lineCode,
            @FormParam( "clientCode" ) String clientCode ) throws JSONException {

        VehicleFunction vehicleFunction = VehicleFunction.getInstance();
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
            Response authResponse = vehicleFunction.getLine( lineCode, token, requestId, clientCode );

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
    @Path( "updateVehicleStatus" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response updateVehicleStatus(
    		@Context HttpHeaders httpHeaders,
    		@Context HttpServletRequest requestContext,
            @FormParam( "vehicleId" ) Long vehicleId,
            @FormParam( "vehicleStatus" ) String vehicleStatus,
            @FormParam( "clientCode" ) String clientCode ) throws JSONException {

    	String ipAddress = requestContext.getRemoteAddr();
        VehicleFunction vehicleFunction = VehicleFunction.getInstance();
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
            Response authResponse = vehicleFunction.updateVehicleStatus( vehicleId, vehicleStatus, token, requestId, clientCode, ipAddress );

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
    @Path( "updateLineStatus" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response updateLineStatus(
    		@Context HttpHeaders httpHeaders,
    		@Context HttpServletRequest requestContext,
            @FormParam( "lineCode" ) String lineCode,
            @FormParam( "lineStatus" ) Integer lineStatus,
            @FormParam( "clientCode" ) String clientCode ) throws JSONException {

    	String ipAddress = requestContext.getRemoteAddr();
        VehicleFunction vehicleFunction = VehicleFunction.getInstance();
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
            Response authResponse = vehicleFunction.updateLineStatus(lineCode, lineStatus, token, requestId, clientCode, ipAddress);
		
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
    @Path( "deleteVehicle" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response deleteVehicle(
    		@Context HttpHeaders httpHeaders,
    		@Context HttpServletRequest requestContext,
            @FormParam( "vehicleId" ) Long vehicleId,
            @FormParam( "clientCode" ) String clientCode ) throws JSONException {

    	String ipAddress = requestContext.getRemoteAddr();
        VehicleFunction vehicleFunction = VehicleFunction.getInstance();
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
            Response authResponse = vehicleFunction.deleteVehicle( vehicleId, token, requestId, clientCode, ipAddress );

            return authResponse;

        } catch ( final Exception ex ) {
        	ex.printStackTrace();
        	JsonObjectBuilder jsonObject = Json.createObjectBuilder();
        	jsonObject.add( "message", "Experienced a system server error " );
            JsonObject jsonObj = jsonObject.build();

            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.INTERNAL_SERVER_ERROR ).entity( jsonObj.toString() ).build();
        }
    }
    
    
    
    
    /*@POST
    @Path( "createNewVehicleTrip" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response createNewVehicleTrip(
    		@Context HttpHeaders httpHeaders,
    		@Context HttpServletRequest requestContext,
    		@FormParam( "vehicleSeatSectionDetails" ) String vehicleSeatSectionDetails,
    		@FormParam( "vehicleTripRouteScheduleStations" ) String vehicleTripRouteScheduleStations,
            @FormParam( "purchaseStartDate" ) String purchaseStartDate,
            @FormParam( "lineCode" ) String lineCode,
            @FormParam( "originVehicleScheduleId" ) Long originVehicleScheduleId,
            @FormParam( "finalVehicleScheduleId" ) Long finalVehicleScheduleId,
            @FormParam( "clientCode" ) String clientCode) throws JSONException {

    	String ipAddress = requestContext.getRemoteAddr();
        VehicleFunction vehicleFunction = VehicleFunction.getInstance();
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
        	
            Response authResponse = vehicleFunction.createNewVehicleTrip( token, originVehicleScheduleId,
            		finalVehicleScheduleId, purchaseStartDate,  vehicleTripRouteScheduleStations, lineCode, requestId, ipAddress, clientCode );

            return authResponse;

        } catch ( final Exception ex ) {
        	ex.printStackTrace();
        	JsonObjectBuilder jsonObject = Json.createObjectBuilder();
        	jsonObject.add( "message", "Experienced a system server error " );
            JsonObject jsonObj = jsonObject.build();

            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.INTERNAL_SERVER_ERROR ).entity( jsonObj.toString() ).build();
        }
    }*/
    
		
    
    
    /*@POST
    @Path( "listVehicleTrips" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response listVehicleTrips(
    		@Context HttpHeaders httpHeaders,
    		@FormParam( "vehicleTripId" ) Long vehicleTripId,
            @FormParam( "vehicleId" ) Long vehicleId,
            @FormParam( "originVehicleScheduleId" ) Long originVehicleScheduleId,
            @FormParam( "finalVehicleScheduleId" ) Long finalVehicleScheduleId,
            @FormParam( "startIndex" ) Integer startIndex,
            @FormParam( "limit" ) Integer limit,
            @FormParam( "clientCode" ) String clientCode ) throws JSONException {

        VehicleFunction vehicleFunction = VehicleFunction.getInstance();
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
        	
            Response authResponse = vehicleFunction.listVehicleTrips(vehicleTripId, vehicleId, originVehicleScheduleId, finalVehicleScheduleId, startIndex, limit, token, requestId, clientCode );

            return authResponse;

        } catch ( final Exception ex ) {
        	ex.printStackTrace();
        	JsonObjectBuilder jsonObject = Json.createObjectBuilder();
        	jsonObject.add( "message", "Experienced a system server error " );
            JsonObject jsonObj = jsonObject.build();

            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.INTERNAL_SERVER_ERROR ).entity( jsonObj.toString() ).build();
        }
    }
    */
    
    @POST
    @Path( "getVehicleTrip" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response getVehicleTrip(
    		@Context HttpHeaders httpHeaders,
            @FormParam( "vehicleTripId" ) Long vehicleTripId,
            @FormParam( "clientCode" ) String clientCode ) throws JSONException {

        VehicleFunction vehicleFunction = VehicleFunction.getInstance();
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
            Response authResponse = vehicleFunction.getVehicleTrip( vehicleTripId, token, requestId, clientCode );

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
    @Path( "deleteVehicleTrip" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response deleteVehicleTrip(
    		@Context HttpHeaders httpHeaders,
    		@Context HttpServletRequest requestContext,
            @FormParam( "vehicleTripId" ) Long vehicleTripId,
            @FormParam( "clientCode" ) String clientCode ) throws JSONException {

    	String ipAddress = requestContext.getRemoteAddr();
        VehicleFunction vehicleFunction = VehicleFunction.getInstance();
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
            Response authResponse = vehicleFunction.deleteVehicleTrip( vehicleTripId, token, requestId, clientCode, ipAddress );

            return authResponse;

        } catch ( final Exception ex ) {
        	ex.printStackTrace();
        	JsonObjectBuilder jsonObject = Json.createObjectBuilder();
        	jsonObject.add( "message", "Experienced a system server error " );
            JsonObject jsonObj = jsonObject.build();

            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.INTERNAL_SERVER_ERROR ).entity( jsonObj.toString() ).build();
        }
    }
    
    
    /*@POST
    @Path( "createNewVehicleTripRouting" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response createNewVehicleTripRouting(
    		@Context HttpHeaders httpHeaders,
    		@Context HttpServletRequest requestContext,
            @FormParam( "vehicleTripId" ) Long vehicleTripId,
            @FormParam( "originVehicleScheduleId" ) Long originVehicleScheduleId,
            @FormParam( "destinationVehicleScheduleId" ) Long destinationVehicleScheduleId,
            @FormParam( "tripRouteOrder" ) Integer tripRouteOrder,
            @FormParam( "clientCode" ) String clientCode) throws JSONException {

    	String ipAddress = requestContext.getRemoteAddr();
        VehicleFunction vehicleFunction = VehicleFunction.getInstance();
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
        	
            Response authResponse = vehicleFunction.createNewVehicleTripRouting( token, vehicleTripId, originVehicleScheduleId,
            		destinationVehicleScheduleId, tripRouteOrder, requestId, ipAddress, clientCode );

            return authResponse;

        } catch ( final Exception ex ) {
        	ex.printStackTrace();
        	JsonObjectBuilder jsonObject = Json.createObjectBuilder();
        	jsonObject.add( "message", "Experienced a system server error " );
            JsonObject jsonObj = jsonObject.build();

            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.INTERNAL_SERVER_ERROR ).entity( jsonObj.toString() ).build();
        }
    }*/
    
		
    
    
    @POST
    @Path( "listVehicleTripRoutings" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response listVehicleTripRoutings(
    		@Context HttpHeaders httpHeaders,
            @FormParam( "vehicleTripId" ) Long vehicleTripId,
            @FormParam( "vehicleTripRoutingOriginScheduleId" ) Long vehicleTripRoutingOriginScheduleId,
            @FormParam( "vehicleTripRoutingDestinationScheduleId" ) Long vehicleTripRoutingDestinationScheduleId,
            @FormParam( "vehicleTripCode" ) Long vehicleTripCode,
            @FormParam( "startIndex" ) Integer startIndex,
            @FormParam( "limit" ) Integer limit,
            @FormParam( "clientCode" ) String clientCode ) throws JSONException {

        VehicleFunction vehicleFunction = VehicleFunction.getInstance();
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
        	
            Response authResponse = vehicleFunction.listVehicleTripRoutings(vehicleTripId, vehicleTripRoutingOriginScheduleId, vehicleTripRoutingDestinationScheduleId, vehicleTripCode, startIndex, limit, token, requestId, clientCode );

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
    @Path( "getVehicleTripRouting" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response getVehicleTripRouting(
    		@Context HttpHeaders httpHeaders,
    		@Context HttpServletRequest requestContext,
    		@FormParam( "vehicleTripRoutingId" ) Long vehicleTripRoutingId,
    		@FormParam( "vehicleTripId" ) Long vehicleTripId,
            @FormParam( "vehicleScheduleId" ) Long vehicleScheduleId,
            @FormParam( "clientCode" ) String clientCode ) throws JSONException {

    	String ipAddress = requestContext.getRemoteAddr();
        VehicleFunction vehicleFunction = VehicleFunction.getInstance();
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
            Response authResponse = vehicleFunction.getVehicleTripRouting( vehicleTripRoutingId, vehicleTripId, vehicleScheduleId, token, requestId, ipAddress, clientCode );

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
    @Path( "deleteVehicleTripRouting" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response deleteVehicleTripRouting(
    		@Context HttpHeaders httpHeaders,
            @FormParam( "vehicleTripRoutingId" ) Long vehicleTripRoutingId,
            @FormParam( "clientCode" ) String clientCode ) throws JSONException {

        VehicleFunction vehicleFunction = VehicleFunction.getInstance();
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
            Response authResponse = vehicleFunction.deleteVehicleTripRouting( vehicleTripRoutingId, token, requestId, clientCode );

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
    @Path( "createNewVehicleScheduleFare" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response createNewVehicleScheduleFare(
    		@Context HttpHeaders httpHeaders,
    		@Context HttpServletRequest requestContext,
            @FormParam( "vehicleTripRoutingId" ) Long vehicleTripRoutingId,
            @FormParam( "trainClassId" ) Long trainClassId,
            @FormParam( "childFare" ) Double childFare,
            @FormParam( "adultFare" ) Double adultFare,
            @FormParam( "clientCode" ) String clientCode) throws JSONException {

    	String ipAddress = requestContext.getRemoteAddr();
        VehicleFunction vehicleFunction = VehicleFunction.getInstance();
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
        	
            Response authResponse = vehicleFunction.createNewVehicleScheduleFares( token, vehicleTripRoutingId, trainClassId,
            		childFare, adultFare, requestId, ipAddress, clientCode );

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
    @Path( "createNewVehicleTripZonePrice" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response createNewVehicleTripZonePrice(
    		@Context HttpHeaders httpHeaders,
    		@Context HttpServletRequest requestContext,
            @FormParam( "priceDetails" ) String priceDetails,
            @FormParam( "clientCode" ) String clientCode) throws JSONException {

    	String ipAddress = requestContext.getRemoteAddr();
        VehicleFunction vehicleFunction = VehicleFunction.getInstance();
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
        	
            Response authResponse = vehicleFunction.createNewVehicleTripZonePrice( token, priceDetails,
            		requestId, ipAddress, clientCode );

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
    @Path( "listTripZonePrices" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response listTripZonePrices(
    		@Context HttpHeaders httpHeaders,
    		@Context HttpServletRequest requestContext,
    	    @FormParam( "clientCode" ) String clientCode ) throws JSONException {

        

        try {
        	String ipAddress = requestContext.getRemoteAddr();
        	VehicleFunction vehicleFunction = VehicleFunction.getInstance();
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
    		Response authResponse = vehicleFunction.getListTripZonePrices(token, clientCode, requestId, ipAddress);
    	            

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
    @Path( "listVehicleScheduleFares" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response listVehicleScheduleFares(
    		@Context HttpHeaders httpHeaders,
            @FormParam( "vehicleTripRoutingId" ) Long vehicleTripRoutingId,
            @FormParam( "vehicleSeatClassId" ) Long vehicleSeatClassId,
            @FormParam( "childFare" ) Double childFare,
            @FormParam( "adultFare" ) Double adultFare,
            @FormParam( "startIndex" ) Integer startIndex,
            @FormParam( "limit" ) Integer limit,
            @FormParam( "clientCode" ) String clientCode ) throws JSONException {

        VehicleFunction vehicleFunction = VehicleFunction.getInstance();
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
        	
            Response authResponse = vehicleFunction.listVehicleScheduleFares(vehicleTripRoutingId, vehicleSeatClassId, childFare, adultFare, startIndex, limit, token, requestId, clientCode );

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
    @Path( "getVehicleScheduleFare" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response getVehicleScheduleFare(
    		@Context HttpHeaders httpHeaders,
            @FormParam( "vehicleScheduleFareId" ) Long vehicleScheduleFareId,
            @FormParam( "clientCode" ) String clientCode ) throws JSONException {

        VehicleFunction vehicleFunction = VehicleFunction.getInstance();
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
            Response authResponse = vehicleFunction.getVehicleScheduleFare( vehicleScheduleFareId, token, requestId, clientCode );

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
    @Path( "deleteVehicleScheduleFare" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response deleteVehicleScheduleFare(
    		@Context HttpHeaders httpHeaders,
    		@Context HttpServletRequest requestContext,
            @FormParam( "vehicleScheduleFareId" ) Long vehicleScheduleFareId,
            @FormParam( "clientCode" ) String clientCode ) throws JSONException {

    	String ipAddress = requestContext.getRemoteAddr();
        VehicleFunction vehicleFunction = VehicleFunction.getInstance();
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
            Response authResponse = vehicleFunction.deleteVehicleScheduleFare( vehicleScheduleFareId, token, requestId, clientCode, ipAddress );

            return authResponse;

        } catch ( final Exception ex ) {
        	ex.printStackTrace();
        	JsonObjectBuilder jsonObject = Json.createObjectBuilder();
        	jsonObject.add( "message", "Experienced a system server error " );
            JsonObject jsonObj = jsonObject.build();

            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.INTERNAL_SERVER_ERROR ).entity( jsonObj.toString() ).build();
        }
    }
    
    
    /*@POST
    @Path( "createNewPurchasedTrip" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response createNewPurchasedTrip(
    		@Context HttpHeaders httpHeaders,
    		@Context HttpServletRequest requestContext,
            @FormParam( "customerId" ) Long customerId,
            @FormParam( "vehicleTripId" ) String vehicleTripCode,
            @FormParam( "amountPayable" ) Double amountPayable,
            @FormParam( "cardPan" ) String cardPan,
            @FormParam( "orderId" ) String orderId, 
            @FormParam( "deviceCode" ) String deviceCode, 
            @FormParam( "channel" ) String channel, 
            @FormParam( "hash" ) String hash, 
            @FormParam( "serviceType" ) String serviceType,
            @FormParam( "vehicleSeatClassId" ) Long vehicleSeatClassId,
            @FormParam( "childSeatCount" ) Integer childSeatCount,
            @FormParam( "adultSeatCount" ) Integer adultSeatCount,
            @FormParam( "ticketCollectionPointId" ) Long ticketCollectionPointId,
            @FormParam( "clientCode" ) String clientCode) throws JSONException {

    	String ipAddress = requestContext.getRemoteAddr();
        VehicleFunction vehicleFunction = VehicleFunction.getInstance();
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
        	
            Response authResponse = vehicleFunction.createNewPurchasedTrip( hash, channel, deviceCode, orderId, token, customerId, vehicleTripCode, 
            		amountPayable, cardPan, ticketCollectionPointId, serviceType, requestId, ipAddress, clientCode, vehicleSeatClassId, childSeatCount, adultSeatCount );

            
            
            return authResponse;

        } catch ( final Exception ex ) {
        	ex.printStackTrace();
        	JsonObjectBuilder jsonObject = Json.createObjectBuilder();
        	jsonObject.add( "message", "Experienced a system server error " );
            JsonObject jsonObj = jsonObject.build();

            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.INTERNAL_SERVER_ERROR ).entity( jsonObj.toString() ).build();
        }
    }*/
    
		
    
    @POST
    @Path( "listTransactions" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response listTransactions(
    		@Context HttpHeaders httpHeaders,
    		@FormParam( "vendorCode" ) String vendorCode,
            @FormParam( "customerId" ) Long customerId,
            @FormParam( "startDate" ) String startDate,
            @FormParam( "endDate" ) String endDate,
            @FormParam( "vehicleScheduleCode" ) String vehicleScheduleCode,
            @FormParam( "clientCode" ) String clientCode ) throws JSONException {

        VehicleFunction vehicleFunction = VehicleFunction.getInstance();
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
        	String requestId = (new SimpleDateFormat("YYYYMMddHHmmss").format(new Date())) + ": ";
        	//log.inforequestId + "Proceed 1");
        	
            Response authResponse = vehicleFunction.listTransactions(vehicleScheduleCode, customerId, vendorCode, token, requestId, clientCode, startDate, endDate);

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
    @Path( "listPurchasedTripsByTransaction" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response listPurchasedTripsByTransaction(
    		@Context HttpHeaders httpHeaders,
            @FormParam( "orderRef" ) String orderRef,
            @FormParam( "startDate" ) String startDate,
            @FormParam( "endDate" ) String endDate,
            @FormParam( "clientCode" ) String clientCode ) throws JSONException {

        VehicleFunction vehicleFunction = VehicleFunction.getInstance();
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
        	
            Response authResponse = vehicleFunction.listPurchasedTripsByTransaction(orderRef, token, requestId, clientCode, startDate, endDate );

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
    @Path( "listPurchasedTrips" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response listPurchasedTrips(
    		@Context HttpHeaders httpHeaders,
            @FormParam( "customerId" ) Long customerId,
            @FormParam( "scheduleStationCode" ) String scheduleStationCode,
            @FormParam( "cardId" ) Long cardId,
            @FormParam( "ticketCollectionPointId" ) Long ticketCollectionPointId,
            @FormParam( "startIndex" ) Integer startIndex,
            @FormParam( "limit" ) Integer limit,
            @FormParam( "clientCode" ) String clientCode ) throws JSONException {

        VehicleFunction vehicleFunction = VehicleFunction.getInstance();
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
        	
            Response authResponse = vehicleFunction.listPurchasedTrips(customerId, scheduleStationCode, cardId, ticketCollectionPointId, startIndex, limit, token, requestId, clientCode );

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
    @Path( "updatePurchasedTripStatus" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response updatePurchasedTripStatus(
    		@Context HttpHeaders httpHeaders,
            @FormParam( "status" ) String status,
            @FormParam( "receiptNo" ) String receiptNo,
            @FormParam( "clientCode" ) String clientCode ) throws JSONException {

        VehicleFunction vehicleFunction = VehicleFunction.getInstance();
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
        	
            Response authResponse = vehicleFunction.updatePurchasedTripStatus(receiptNo, status, token, requestId, clientCode );

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
    @Path( "listPurchasedTripSeats" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response listPurchasedTripSeats(
    		@Context HttpHeaders httpHeaders,
            @FormParam( "purchasedTripId" ) Long purchasedTripId,
            @FormParam( "clientCode" ) String clientCode ) throws JSONException {

        VehicleFunction vehicleFunction = VehicleFunction.getInstance();
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
        	
            Response authResponse = vehicleFunction.listPurchasedTripSeats(purchasedTripId, token, requestId, clientCode );

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
    @Path( "getPurchasedTrip" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response getPurchasedTrip(
    		@Context HttpHeaders httpHeaders,
            @QueryParam( "receiptNo" ) String receiptNo,
            @QueryParam( "clientCode" ) String clientCode ) throws JSONException {

        VehicleFunction vehicleFunction = VehicleFunction.getInstance();
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
            Response authResponse = vehicleFunction.getPurchasedTrip( receiptNo, token, requestId, clientCode );

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
    @Path( "deletePurchasedTrip" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response deletePurchasedTrip(
    		@Context HttpHeaders httpHeaders,
    		@Context HttpServletRequest requestContext,
            @FormParam( "purchasedTripId" ) Long purchasedTripId,
            @FormParam( "clientCode" ) String clientCode ) throws JSONException {

    	String ipAddress = requestContext.getRemoteAddr();
        VehicleFunction vehicleFunction = VehicleFunction.getInstance();
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
            Response authResponse = vehicleFunction.deletePurchasedTrip( purchasedTripId, token, requestId, clientCode, ipAddress );

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
    @Path( "getVehicleSeatClasses" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response getVehicleSeatClasses(
    		@Context HttpHeaders httpHeaders,
    		@Context HttpServletRequest requestContext,
    		@QueryParam( "deviceCode" ) String deviceCode,
    		@QueryParam( "clientCode" ) String clientCode) throws JSONException {

    	String ipAddress = requestContext.getRemoteAddr();
        VehicleFunction vehicleFunction = VehicleFunction.getInstance();
        List<String> tokenList = httpHeaders.getRequestHeader(HttpHeaderNames.AUTH_TOKEN);
        String token = tokenList!=null && tokenList.size()>0 ? tokenList.get(0) : null;

        try {
        	/*if(purchaseToken==null)
        	{
            	JsonObjectBuilder jsonObject = Json.createObjectBuilder();
            	jsonObject.add( "status", ERROR.UNAUTHORIZED_ACTION );
            	jsonObject.add( "message", "Invalid transaction" );
                JsonObject jsonObj = jsonObject.build();
        		return UtilityHelper.getNoCacheResponseBuilder( Response.Status.UNAUTHORIZED ).entity( jsonObj.toString() ).build();
        	}*/
        	String requestId = (token==null ? "" : (token.substring(token.length()-10))) + "-" + (new SimpleDateFormat("YYYYMMddHHmmss").format(new Date())) + ": ";
        	//log.inforequestId + "Proceed 1");
        	//log.info"clientCode" + clientCode);
        	Enumeration<String> pnames = requestContext.getParameterNames();
        	while(pnames.hasMoreElements())
        	{
        		//log.infopnames.nextElement().toString());
        	}
        		
        	//log.info"clientCode" + clientCode);
        	
            Response authResponse = vehicleFunction.getVehicleSeatClasses(deviceCode, clientCode, requestId, ipAddress);

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
    @Path( "getVehicleSeatSections" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response getVehicleSeatSections(
    		@Context HttpHeaders httpHeaders,
    		@Context HttpServletRequest requestContext,
    		@QueryParam( "clientCode" ) String clientCode) throws JSONException {

    	String ipAddress = requestContext.getRemoteAddr();
        VehicleFunction vehicleFunction = VehicleFunction.getInstance();
        List<String> tokenList = httpHeaders.getRequestHeader(HttpHeaderNames.AUTH_TOKEN);
        String token = tokenList!=null && tokenList.size()>0 ? tokenList.get(0) : null;

        try {
        	String requestId = (token==null ? "" : (token.substring(token.length()-10))) + "-" + (new SimpleDateFormat("YYYYMMddHHmmss").format(new Date())) + ": ";
        	//log.inforequestId + "Proceed 1");
        	//log.info"clientCode" + clientCode);
        	Enumeration<String> pnames = requestContext.getParameterNames();
        	while(pnames.hasMoreElements())
        	{
        		//log.infopnames.nextElement().toString());
        	}
        		
        	//log.info"clientCode" + clientCode);
        	
            Response authResponse = vehicleFunction.getVehicleSeatSections(clientCode, requestId, ipAddress);

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
    @Path( "getVehicleSeats" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response getVehicleSeats(
    		@Context HttpHeaders httpHeaders,
    		@Context HttpServletRequest requestContext,
    		@QueryParam( "sectionCode" ) String sectionCode,
    		@QueryParam( "clientCode" ) String clientCode) throws JSONException {

    	String ipAddress = requestContext.getRemoteAddr();
        VehicleFunction vehicleFunction = VehicleFunction.getInstance();
        List<String> tokenList = httpHeaders.getRequestHeader(HttpHeaderNames.AUTH_TOKEN);
        String token = tokenList!=null && tokenList.size()>0 ? tokenList.get(0) : null;

        try {
        	String requestId = (token==null ? "" : (token.substring(token.length()-10))) + "-" + (new SimpleDateFormat("YYYYMMddHHmmss").format(new Date())) + ": ";
        	//log.inforequestId + "Proceed 1");
        	//log.info"clientCode" + clientCode);
        	Enumeration<String> pnames = requestContext.getParameterNames();
        	while(pnames.hasMoreElements())
        	{
        		//log.infopnames.nextElement().toString());
        	}
        		
        	//log.info"clientCode" + clientCode);
        	
            Response authResponse = vehicleFunction.getVehicleSeats(sectionCode, clientCode, requestId, ipAddress);

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
    @Path( "listTripCustomers" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response listTripCustomers(
    		@Context HttpHeaders httpHeaders,
    		@Context HttpServletRequest requestContext,
    		@QueryParam( "vehicleScheduleStationCode" ) String vehicleScheduleStationCode,
    		@QueryParam( "clientCode" ) String clientCode) throws JSONException {

    	String ipAddress = requestContext.getRemoteAddr();
        VehicleFunction vehicleFunction = VehicleFunction.getInstance();
        List<String> tokenList = httpHeaders.getRequestHeader(HttpHeaderNames.AUTH_TOKEN);
        String token = tokenList!=null && tokenList.size()>0 ? tokenList.get(0) : null;

        try {
        	String requestId = (token==null ? "" : (token.substring(token.length()-10))) + "-" + (new SimpleDateFormat("YYYYMMddHHmmss").format(new Date())) + ": ";
        	//log.inforequestId + "Proceed 1");
        	//log.info"clientCode" + clientCode);
        	Enumeration<String> pnames = requestContext.getParameterNames();
        	while(pnames.hasMoreElements())
        	{
        		//log.infopnames.nextElement().toString());
        	}
        		
        	//log.info"clientCode" + clientCode);
        	
            Response authResponse = vehicleFunction.listTripCustomers(token, vehicleScheduleStationCode, requestId, ipAddress, clientCode);

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
    @Path( "listTripSeats" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response listTripSeats(
    		@Context HttpHeaders httpHeaders,
    		@Context HttpServletRequest requestContext,
    		@QueryParam( "vehicleScheduleStationCode" ) String vehicleScheduleStationCode,
    		@QueryParam( "seatStatus" ) String seatStatus,
    		@QueryParam( "pageIndex" ) Integer pageIndex,
    		@QueryParam( "clientCode" ) String clientCode) throws JSONException {

    	String ipAddress = requestContext.getRemoteAddr();
        VehicleFunction vehicleFunction = VehicleFunction.getInstance();
        List<String> tokenList = httpHeaders.getRequestHeader(HttpHeaderNames.AUTH_TOKEN);
        String token = tokenList!=null && tokenList.size()>0 ? tokenList.get(0) : null;
        seatStatus = seatStatus.toUpperCase();

        try {
        	String requestId = (token==null ? "" : (token.substring(token.length()-10))) + "-" + (new SimpleDateFormat("YYYYMMddHHmmss").format(new Date())) + ": ";
        	//log.inforequestId + "Proceed 1");
        	//log.info"clientCode" + clientCode);
        	Enumeration<String> pnames = requestContext.getParameterNames();
        	while(pnames.hasMoreElements())
        	{
        		//log.infopnames.nextElement().toString());
        	}
        		
        	//log.info"clientCode" + clientCode);
        	
            Response authResponse = vehicleFunction.listTripSeats(token, pageIndex, vehicleScheduleStationCode, seatStatus, requestId, ipAddress, clientCode);

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
    @Path( "addPilotToSchedule" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response addPilotToSchedule(
    		@Context HttpHeaders httpHeaders,
    		@Context HttpServletRequest requestContext,
            @FormParam( "assignedPilotUsername" ) String assignedPilotUsername,
            @FormParam( "newTakeOffDate" ) String newTakeOffDate,
            @FormParam( "departureScheduleId" ) Long departureScheduleId,
            @FormParam( "arrivalScheduleId" ) Long arrivalScheduleId,
            @FormParam( "openingFuelBalance" ) Double openingFuelBalance,
            @FormParam( "openingFuelCapacityUnits" ) String openingFuelCapacityUnits,
            @FormParam( "pilotChangeReasons" ) String pilotChangeReasons,
            @FormParam( "clientCode" ) String clientCode ) throws JSONException {

    	String ipAddress = requestContext.getRemoteAddr();
        VehicleFunction vehicleFunction = VehicleFunction.getInstance();
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
        	
            Response authResponse = vehicleFunction.addPilotToSchedule(assignedPilotUsername, newTakeOffDate, departureScheduleId, arrivalScheduleId, 
            		openingFuelBalance, openingFuelCapacityUnits, token, requestId, clientCode, pilotChangeReasons, ipAddress );

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
    @Path( "endPilotSchedule" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response endPilotSchedule(
    		@Context HttpHeaders httpHeaders,
            @FormParam( "departureScheduleId" ) Long departureScheduleId,
            @FormParam( "arrivalScheduleId" ) Long arrivalScheduleId,
            @FormParam( "closingFuelBalance" ) Double closingFuelBalance,
            @FormParam( "closingFuelCapacityUnits" ) String closingFuelCapacityUnits,
            @FormParam( "clientCode" ) String clientCode ) throws JSONException {

        VehicleFunction vehicleFunction = VehicleFunction.getInstance();
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
        	
            Response authResponse = vehicleFunction.endPilotSchedule(departureScheduleId, arrivalScheduleId, 
            		closingFuelBalance, closingFuelCapacityUnits, token, requestId, clientCode );

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
    @Path( "changeVehicle" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response changeVehicle(
    		@Context HttpHeaders httpHeaders,
    		@Context HttpServletRequest requestContext,
    		@FormParam( "newVehicleCode" ) String newVehicleCode,
    		@FormParam( "assignedPilotUsername" ) String assignedPilotUsername,
            @FormParam( "newTakeOffDate" ) String newTakeOffDate,
            @FormParam( "departureScheduleId" ) Long departureScheduleId,
            @FormParam( "arrivalScheduleId" ) Long arrivalScheduleId,
            @FormParam( "openingFuelBalance" ) Double openingFuelBalance,
            @FormParam( "openingFuelCapacityUnits" ) String openingFuelCapacityUnits,
            @FormParam( "changeVehicleReasons" ) String changeVehicleReasons,
            @FormParam( "clientCode" ) String clientCode ) throws JSONException {

    	String ipAddress = requestContext.getRemoteAddr();
        VehicleFunction vehicleFunction = VehicleFunction.getInstance();
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
        	
            Response authResponse = vehicleFunction.changeVehicle(newVehicleCode, assignedPilotUsername, newTakeOffDate, departureScheduleId, arrivalScheduleId, 
            		openingFuelBalance, openingFuelCapacityUnits, token, requestId, clientCode, changeVehicleReasons, ipAddress );

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
