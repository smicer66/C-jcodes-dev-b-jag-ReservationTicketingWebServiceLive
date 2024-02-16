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
import com.probase.reservationticketingwebservice.authenticator.ClientFunction;
import com.probase.reservationticketingwebservice.authenticator.StationFunction;
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
import com.probase.reservationticketingwebservice.models.Country;
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

@Path( "services/ClientServices" )
@Stateless( name = "ClientServices", mappedName = "services/ClientServices" )
public class ClientServices {

	private static Logger log = Logger.getLogger(ClientServices.class);
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
	
    
    @POST
    @Path( "createNewClient" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response createNewClient(
    		@Context HttpHeaders httpHeaders,
    		@Context HttpServletRequest requestContext,
            @FormParam( "clientName" ) String clientName,
            @FormParam( "contactPerson" ) String contactPerson,
            @FormParam( "contactMobile" ) String contactMobile,
            @FormParam( "contactEmail" ) String contactEmail,
            @FormParam( "clientAddressLine1" ) String clientAddressLine1,
            @FormParam( "clientAddressLine2" ) String clientAddressLine2,
            @FormParam( "clientServiceType" ) String clientServiceType,
            @FormParam( "bookingFee" ) Double bookingFee) throws JSONException {

    	//log.info">>>>>");
    	String ipAddress = requestContext.getRemoteAddr();
        ClientFunction clientFunction = ClientFunction.getInstance();
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
        	
            Response authResponse = clientFunction.createNewClient
            		( contactMobile, contactEmail, contactPerson, clientName, clientAddressLine1, clientAddressLine2, bookingFee, token, requestId, ipAddress, clientServiceType );

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
    @Path( "createNewClientSubscription" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response createNewClientSubscription(
    		@Context HttpHeaders httpHeaders,
    		@Context HttpServletRequest requestContext,
            @FormParam( "clientCode" ) String clientCode,
            @FormParam( "orderRef" ) String orderRef,
            @FormParam( "channel" ) String channel,
            @FormParam( "transactionCurrency" ) String transactionCurrency,
            @FormParam( "paymentMeans" ) String paymentMeans) throws JSONException {

    	String ipAddress = requestContext.getRemoteAddr();
        ClientFunction clientFunction = ClientFunction.getInstance();
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
        	
            Response authResponse = clientFunction.createNewClientSubscription
            		( clientCode, orderRef, channel, transactionCurrency, paymentMeans, token, requestId, ipAddress );

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
    @Path( "updateClient" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response updateClient(
    		@Context HttpHeaders httpHeaders,
    		@Context HttpServletRequest requestContext,
    		@FormParam( "clientCode" ) String clientCode,
    		@FormParam( "clientName" ) String clientName,
            @FormParam( "clientTheme" ) String clientTheme,
            @FormParam( "clientAddressLine1" ) String clientAddressLine1,
            @FormParam( "clientAddressLine2" ) String clientAddressLine2,
            @FormParam( "countryId" ) Long countryId,
            @FormParam( "clientStatus" ) Boolean clientStatus) throws JSONException {

    	String ipAddress = requestContext.getRemoteAddr();
        ClientFunction clientFunction = ClientFunction.getInstance();
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
        	
            Response authResponse = clientFunction.updateClient(clientCode, clientName, clientTheme, clientAddressLine1, clientAddressLine2, countryId, clientStatus, token, 
        			requestId,  ipAddress);

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
    @Path( "getClientList" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response getClientList(
    		@Context HttpHeaders httpHeaders,
            @FormParam( "startIndex" ) Integer startIndex,
            @FormParam( "limit" ) Integer limit ) throws JSONException {

    	ClientFunction clientFunction = ClientFunction.getInstance();
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
        	
            Response authResponse = clientFunction.getClientList(startIndex, limit, token, requestId);

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
    @Path( "getClient" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response getClient(
    		@Context HttpHeaders httpHeaders,
            @FormParam( "clientCode" ) String clientCode ) throws JSONException {

        ClientFunction clientFunction = ClientFunction.getInstance();
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
        	
            Response authResponse = clientFunction.getClient( clientCode, token, requestId );

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
    @Path( "createNewDevice" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response createNewDevice(
    		@Context HttpHeaders httpHeaders,
    		@Context HttpServletRequest requestContext,
            @FormParam( "deviceType" ) String deviceType,
            @FormParam( "serialNo" ) String serialNo,
            @FormParam( "clientCode" ) String clientCode ) throws JSONException {

        ClientFunction clientFunction = ClientFunction.getInstance();
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
        	String ipAddress = requestContext.getRemoteAddr();
            Response authResponse = clientFunction.createNewDevice(deviceType, serialNo, clientCode, token, requestId, ipAddress);

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
    @Path( "getClientCount" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response getClientCount(
    		@Context HttpHeaders httpHeaders,
    		@QueryParam ( "clientStatus" ) Boolean clientStatus
            ) throws JSONException {

    	ClientFunction clientFunction = ClientFunction.getInstance();
    	List<String> tokenList = httpHeaders.getRequestHeader(HttpHeaderNames.AUTH_TOKEN);
        String token = tokenList!=null && tokenList.size()>0 ? tokenList.get(0) : null;
        
        try {
        	String requestId = (new SimpleDateFormat("YYYYMMddHHmmss").format(new Date())) + ": ";
        	//log.inforequestId + "Proceed 1");
        	
        	
            Response authResponse = clientFunction.getClientCount(clientStatus, token, requestId);
            

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
    @Path( "getClientSystemDetails" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response getClientSystemDetails(
    		@Context HttpHeaders httpHeaders,
    		@QueryParam( "clientCode" ) String clientCode
    		)  throws JSONException {

    	
        try {
        	String requestId = (new SimpleDateFormat("YYYYMMddHHmmss").format(new Date())) + ": ";
        	//log.inforequestId + "Proceed 1");
        	
        	ClientFunction clientFunction = ClientFunction.getInstance();
            Response authResponse = clientFunction.getClientSystemDetails(clientCode, requestId);
            

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
    @Path( "getSystemSettings" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response getSystemSettings(
    		@Context HttpHeaders httpHeaders,
    		@QueryParam( "clientCode" ) String clientCode
    		)  throws JSONException {

    	
        try {
        	String requestId = (new SimpleDateFormat("YYYYMMddHHmmss").format(new Date())) + ": ";
        	//log.inforequestId + "Proceed 1");
        	
        	ClientFunction clientFunction = ClientFunction.getInstance();
            Response authResponse = clientFunction.getSystemSettings(clientCode, requestId);
            

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
    @Path( "updateSystemSettings" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response updateSystemSettings(
    		@Context HttpHeaders httpHeaders,
    		@Context HttpServletRequest requestContext,
            @FormParam( "systemDetails" ) String systemDetails,
            @FormParam( "clientCode" ) String clientCode
    ) throws JSONException {

    	//log.info">>>>>");
    	String ipAddress = requestContext.getRemoteAddr();
        ClientFunction clientFunction = ClientFunction.getInstance();
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
        	
            Response authResponse = clientFunction.updateSystemSettings(token, systemDetails, clientCode, requestId, ipAddress);

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
    @Path( "getDeviceList" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response getDeviceList(
    		@Context HttpHeaders httpHeaders,
    		@Context HttpServletRequest requestContext,
            @QueryParam( "clientCode" ) String clientCode ) throws JSONException {

        ClientFunction clientFunction = ClientFunction.getInstance();
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
        	String ipAddress = requestContext.getRemoteAddr();
            Response authResponse = clientFunction.getDeviceList(clientCode, token, requestId, ipAddress);

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
    @Path( "getClientSystemDetail" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response getClientSystemDetail(
    		@Context HttpHeaders httpHeaders,
    		@QueryParam( "deviceCode" ) String deviceCode,
    		@QueryParam( "clientCode" ) String clientCode
    		)  throws JSONException {

    	
        try {
        	String requestId = (new SimpleDateFormat("YYYYMMddHHmmss").format(new Date())) + ": ";
        	//log.inforequestId + "Proceed 1");
        	
        	ClientFunction clientFunction = ClientFunction.getInstance();
            Response authResponse = clientFunction.getClientSystemDetails( clientCode, requestId);
            

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
    @Path( "createNewVendor" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response createNewVendor(
    		@Context HttpHeaders httpHeaders,
    		@Context HttpServletRequest requestContext,
            @FormParam( "vendorCompanyName" ) String vendorCompanyName,
            @FormParam( "contactPerson" ) String contactPerson,
            @FormParam( "contactMobile" ) String contactMobile,
            @FormParam( "contactEmail" ) String contactEmail,
            @FormParam( "clientCode" ) String clientCode) throws JSONException {

    	String ipAddress = requestContext.getRemoteAddr();
        ClientFunction clientFunction = ClientFunction.getInstance();
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
        	
            Response authResponse = clientFunction.createNewVendor
            		( vendorCompanyName, contactPerson, contactMobile, contactEmail, clientCode, token, requestId, ipAddress );

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
    @Path( "getVendorList" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response getVendorList(
    		@Context HttpHeaders httpHeaders,
    		@Context HttpServletRequest requestContext,
            @QueryParam( "clientCode" ) String clientCode ) throws JSONException {

        ClientFunction clientFunction = ClientFunction.getInstance();
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
        	String ipAddress = requestContext.getRemoteAddr();
            Response authResponse = clientFunction.getVendorList(clientCode, token, requestId, ipAddress);

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
    @Path( "getWalletList" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response getWalletList(
    		@Context HttpHeaders httpHeaders,
    		@Context HttpServletRequest requestContext,
            @QueryParam( "clientCode" ) String clientCode ) throws JSONException {

        ClientFunction clientFunction = ClientFunction.getInstance();
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
        	String ipAddress = requestContext.getRemoteAddr();
            Response authResponse = clientFunction.getWalletList(clientCode, token, requestId, ipAddress);

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
    @Path( "fundVendorWallet" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response fundVendorWallet(
    		@Context HttpHeaders httpHeaders,
    		@Context HttpServletRequest requestContext,
            @FormParam( "paymentType" ) String paymentType,
            @FormParam( "clientCode" ) String clientCode, 
            @FormParam( "deviceCode" ) String deviceCode, 
            @FormParam( "transactionRef" ) String transactionRef,
            @FormParam( "paymentDetails" ) String paymentDetails
    ) throws JSONException {

    	String ipAddress = requestContext.getRemoteAddr();
        ClientFunction clientFunction = ClientFunction.getInstance();
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
            //log.info"paymentType = " + paymentType);
            //log.info"deviceCode = " + deviceCode);
            //log.info"clientCode = " + clientCode);
            //log.info"transactionRef = " + transactionRef);
            //log.info"paymentDetails = " + paymentDetails);
            
        	String requestId = token!=null && token.length()>0 ? ((token.substring(token.length()-10))) + "-" + (new SimpleDateFormat("YYYYMMddHHmmss").format(new Date())) : "";
        	//log.inforequestId + "Proceed 1");
        	//log.info"clientCode" + clientCode);
        	Enumeration<String> pnames = requestContext.getParameterNames();
        	while(pnames.hasMoreElements())
        	{
        		//log.infopnames.nextElement().toString());
        	}
        		
        	//log.info"clientCode" + clientCode);
        	
        	Response authResponse = clientFunction.fundVendorWallet(paymentType, transactionRef, deviceCode, 
        			paymentDetails, requestId, ipAddress, clientCode, token);

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
    @Path( "listCustomers" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response listCustomers(
    		@Context HttpHeaders httpHeaders,
    		@Context HttpServletRequest requestContext,
    		@QueryParam ( "orderRef" ) String orderRef,
    		@QueryParam ( "clientCode" ) String clientCode
            ) throws JSONException {

    	ClientFunction clientFunction = ClientFunction.getInstance();
    	List<String> tokenList = httpHeaders.getRequestHeader(HttpHeaderNames.AUTH_TOKEN);
        String token = tokenList!=null && tokenList.size()>0 ? tokenList.get(0) : null;
        String ipAddress = requestContext.getRemoteAddr();
        
        try {
        	String requestId = (new SimpleDateFormat("YYYYMMddHHmmss").format(new Date())) + ": ";
        	//log.inforequestId + "Proceed 1");
        	
        	
            Response authResponse = clientFunction.listCustomers(token, orderRef, requestId, ipAddress, clientCode);
            

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
