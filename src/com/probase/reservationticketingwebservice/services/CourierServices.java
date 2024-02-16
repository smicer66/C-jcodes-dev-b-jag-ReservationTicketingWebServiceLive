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
import com.probase.reservationticketingwebservice.authenticator.CourierFunction;
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
import com.probase.reservationticketingwebservice.models.CourierService;
import com.probase.reservationticketingwebservice.models.District;
import com.probase.reservationticketingwebservice.models.ProductCategory;
import com.probase.reservationticketingwebservice.models.TripCard;
import com.probase.reservationticketingwebservice.models.Transaction;
import com.probase.reservationticketingwebservice.models.TripZone;
import com.probase.reservationticketingwebservice.models.User;
import com.probase.reservationticketingwebservice.util.Application;
import com.probase.reservationticketingwebservice.util.ERROR;
import com.probase.reservationticketingwebservice.util.PrbCustomService;
import com.probase.reservationticketingwebservice.util.ServiceLocator;
import com.probase.reservationticketingwebservice.util.SwpService;
import com.probase.reservationticketingwebservice.util.UtilityHelper;

@Path( "services/CourierServices" )
@Stateless( name = "CourierService", mappedName = "services/CourierServices" )
public class CourierServices {

	private static Logger log = Logger.getLogger(CourierServices.class);
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

	
	
	
	@GET
	@Path("getDetailsForCourierPriceUpload")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDetailsForCourierPriceUpload(
			@Context HttpHeaders httpHeaders,
    		@Context HttpServletRequest requestContext,
            @QueryParam( "clientCode" ) String clientCode
			){
		String ipAddress = requestContext.getRemoteAddr();
        CourierFunction courierFunction = CourierFunction.getInstance();
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
        	
            Response authResponse = courierFunction.getDetailsForCourierPriceUpload(token, requestId, clientCode);

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
    @Path( "createNewProductCategory" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response createNewProductCategory(
    		@Context HttpHeaders httpHeaders,
    		@Context HttpServletRequest requestContext,
            @FormParam( "productCategoryName" ) String productCategoryName,
            @FormParam( "productCategoryCode" ) String productCategoryCode,
            @FormParam( "parentProductCategoryCode" ) String parentProductCategoryCode,
            @FormParam( "details" ) String details,
            @FormParam( "isWeightApplicableForPricing" ) Integer isWeightApplicableForPricing,
            @FormParam( "clientCode" ) String clientCode) throws JSONException {

    	String ipAddress = requestContext.getRemoteAddr();
        CourierFunction courierFunction = CourierFunction.getInstance();
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
        	
            Response authResponse = courierFunction.createNewProductCategory(token, productCategoryName, details,
        		    productCategoryCode, requestId, ipAddress, clientCode, parentProductCategoryCode, isWeightApplicableForPricing);

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
    @Path( "listProductCategory" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response listProductCategory(
    		@Context HttpHeaders httpHeaders,
            @QueryParam( "clientCode" ) String clientCode,
            @QueryParam( "parentYes" ) Integer parentYes) throws JSONException {

        CourierFunction courierFunction = CourierFunction.getInstance();
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
        	
            Response authResponse = courierFunction.listProductCategory(token, requestId, clientCode, parentYes);

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
    @Path( "createNewDeliveryDistance" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response createNewDeliveryDistance(
    		@Context HttpHeaders httpHeaders,
    		@Context HttpServletRequest requestContext,
            @FormParam( "stationCode" ) String stationCode, 
            @FormParam( "distance" ) Double distance,
            @FormParam( "distanceId" ) Long distanceId,
            @FormParam( "clientCode" ) String clientCode
	) throws JSONException {

    	String ipAddress = requestContext.getRemoteAddr();
        CourierFunction courierFunction = CourierFunction.getInstance();
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
        	
            Response authResponse = courierFunction.createNewDeliveryDistance(token,
        			stationCode, distance,
        			requestId, ipAddress, clientCode, distanceId);

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
    @Path( "createNewDeliveryPrice" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response createNewDeliveryPrice(
    		@Context HttpHeaders httpHeaders,
    		@Context HttpServletRequest requestContext,
            @FormParam( "productCategoryCode" ) String productCategoryCode,
            @FormParam( "courierServiceCode" ) String courierServiceCode, 
            @FormParam( "minWeight" ) Double minWeight, 
            @FormParam( "maxWeight" ) Double maxWeight,
            @FormParam( "minDistance" ) Double minDistance, 
            @FormParam( "maxDistance" ) Double maxDistance, 
            @FormParam( "deliveryPriceId" ) Long deliveryPriceId, 
            @FormParam( "amount" ) Double amount,
            @FormParam( "clientCode" ) String clientCode
	) throws JSONException {

		//log.info">>>" + productCategoryCode);
		//log.info">>>" + courierServiceCode);
		//log.info">>>" + minWeight);
		//log.info">>>" + maxWeight);
		//log.info">>>" + minDistance);
		//log.info">>>" + maxDistance);
		//log.info">>>" + deliveryPriceId);
		//log.info">>>" + amount);
		//log.info">>>" + clientCode);
    	String ipAddress = requestContext.getRemoteAddr();
        CourierFunction courierFunction = CourierFunction.getInstance();
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


            Response authResponse = courierFunction.createNewDeliveryPrice(token,
        			productCategoryCode, courierServiceCode, minWeight, maxWeight,
        			minDistance, maxDistance, amount,
        			requestId, ipAddress, clientCode, deliveryPriceId);

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
    @Path( "listCourierDeliveryPrices" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response listCourierDeliveryPrices(
    		@Context HttpHeaders httpHeaders,
            @QueryParam( "clientCode" ) String clientCode ) throws JSONException {

        CourierFunction courierFunction = CourierFunction.getInstance();
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
        	
            Response authResponse = courierFunction.listCourierDeliveryPrices(token, requestId, clientCode);

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
    @Path( "getCourierPriceDetailsByCategory" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response getCourierPriceDetailsByCategory(
    		@Context HttpHeaders httpHeaders,
            @QueryParam( "clientCode" ) String clientCode,
            @QueryParam( "categoryCode" ) String categoryCode, 
            @QueryParam( "arrivalStationCode" ) String arrivalStationCode, 
            @QueryParam( "departureStationCode" ) String departureStationCode, 
            @QueryParam( "courierServiceCode" ) String courierServiceCode, 
            @QueryParam( "weight" ) Double weight
            ) throws JSONException {

        CourierFunction courierFunction = CourierFunction.getInstance();
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
        	
            Response authResponse = courierFunction.getCourierPriceDetailsByCategory(token, requestId, clientCode, categoryCode, arrivalStationCode, departureStationCode, courierServiceCode, weight);

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
    @Path( "createNewCourierService" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response createNewLine(
    		@Context HttpHeaders httpHeaders,
    		@Context HttpServletRequest requestContext,
            @FormParam( "courierServiceName" ) String courierServiceName,
            @FormParam( "courierServiceCode" ) String courierServiceCode,
            @FormParam( "details" ) String details,
            @FormParam( "maxDeliveryPeriod" ) Integer maxDeliveryPeriod,
            @FormParam( "clientCode" ) String clientCode) throws JSONException {

    	String ipAddress = requestContext.getRemoteAddr();
        CourierFunction courierFunction = CourierFunction.getInstance();
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
        	
            Response authResponse = courierFunction.createNewCourierService( token, courierServiceName,
            		courierServiceCode, details, maxDeliveryPeriod, requestId, ipAddress, clientCode);

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
    @Path( "listCourierServices" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response listCourierServices(
    		@Context HttpHeaders httpHeaders,
            @QueryParam( "clientCode" ) String clientCode ) throws JSONException {

        CourierFunction courierFunction = CourierFunction.getInstance();
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
        	
            Response authResponse = courierFunction.listCourierServices(token, requestId, clientCode );

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
    @Path( "createNewProductCategoryCoefficientRate" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response createNewProductCategoryCoefficientRate(
    		@Context HttpHeaders httpHeaders,
    		@Context HttpServletRequest requestContext,
            @FormParam( "coefficientList"  ) String coefficientList,
            @FormParam( "clientCode" ) String clientCode) throws JSONException {

    	String ipAddress = requestContext.getRemoteAddr();
        CourierFunction courierFunction = CourierFunction.getInstance();
        List<String> tokenList = httpHeaders.getRequestHeader(HttpHeaderNames.AUTH_TOKEN);
        String token = tokenList!=null && tokenList.size()>0 ? tokenList.get(0) : null;

        try {
        	if(token==null)
        	{
            	JsonObjectBuilder jsonObject = Json.createObjectBuilder();
            	jsonObject.add( "status", ERROR.UNAUTHORIZED_ACTION );
            	jsonObject.add( "message", "Invalid operation" );
                JsonObject jsonObj = jsonObject.build();
        		return UtilityHelper.getNoCacheResponseBuilder( Response.Status.UNAUTHORIZED ).entity( jsonObj.toString() ).build();
        	}
        	String requestId = (new SimpleDateFormat("YYYYMMddHHmmss").format(new Date())) + ": ";
        	//log.inforequestId + "Proceed 1");
            Response authResponse = courierFunction.
            		createNewProductCategoryCoefficientRate(token, coefficientList, requestId, ipAddress, clientCode);
            
            
            
            
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
    @Path( "listProductPriceCoefficientRates" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response listProductPriceCoefficientRates(
    		@Context HttpHeaders httpHeaders,
            @QueryParam( "clientCode" ) String clientCode ) throws JSONException {

        CourierFunction courierFunction = CourierFunction.getInstance();
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
        	
            Response authResponse = courierFunction.listProductPriceCoefficientRates(token, requestId, clientCode);

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
    @Path( "createNewShipment" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response createNewShipment(
    		@Context HttpHeaders httpHeaders,
    		@Context HttpServletRequest requestContext,
    		@FormParam( "shipmentTrackingId" ) String shipmentTrackingId, 
    		@FormParam( "receiverAddress" ) String receiverAddress, 
    		@FormParam( "receiverCity" ) String receiverCity, 
    		@FormParam( "receiverDistrictCode" ) String receiverDistrictCode, 
    		@FormParam( "receiverEmailNumber" ) String receiverEmailNumber, 
    		@FormParam( "receiverMobileNumber" ) String receiverMobileNumber, 
    		@FormParam( "receiverName" ) String receiverName, 
    		@FormParam( "senderAddress" ) String senderAddress, 
    		@FormParam( "senderCity" ) String senderCity, 
    		@FormParam( "senderDistrictCode" ) String senderDistrictCode, 
    		@FormParam( "senderEmailAddress" ) String senderEmailAddress, 
    		@FormParam( "senderName" ) String senderName, 
    		@FormParam( "senderMobileNumber" ) String senderMobileNumber, 
    		@FormParam( "senderSignatureImage" ) String senderSignatureImage, 
    		@FormParam( "clientCode" ) String clientCode, 
    		@FormParam( "deviceCode" ) String deviceCode) throws JSONException {

    	String ipAddress = requestContext.getRemoteAddr();
        CourierFunction vehicleFunction = CourierFunction.getInstance();
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
        	//log.info">>>>" + shipmentTrackingId); 
    		//log.info">>>>" + token); 
    		//log.info">>>>" + requestId); 
    		//log.info">>>>" + clientCode);
        	//log.info">>>>" + receiverAddress); 
        	//log.info">>>>" + receiverCity); 
        	//log.info">>>>" + receiverDistrictCode); 
        	//log.info">>>>" + receiverEmailNumber); 
        	//log.info">>>>" + receiverMobileNumber); 
        	//log.info">>>>" + receiverName); 
        	//log.info">>>>" + senderAddress); 
        	//log.info">>>>" + senderCity); 
        	//log.info">>>>" + senderDistrictCode); 
        	//log.info">>>>" + senderEmailAddress); 
        	//log.info">>>>" + senderName); 
        	//log.info">>>>" + senderMobileNumber); 
        	//log.info">>>>" + senderSignatureImage); 
        	
            Response authResponse = vehicleFunction.createNewShipment(shipmentTrackingId, receiverAddress, receiverCity, receiverDistrictCode, receiverEmailNumber, 
        			receiverMobileNumber, receiverName, senderAddress, senderCity, senderDistrictCode, 
        			senderEmailAddress, senderName, senderMobileNumber, senderSignatureImage, 
        			token, requestId, ipAddress, clientCode, deviceCode);

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
    @Path( "getCourierTripDetailsByTransactionRef" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response getCourierTripDetailsByTransactionRef(
    		@Context HttpHeaders httpHeaders,
    		@Context HttpServletRequest requestContext,
    		@QueryParam( "orderRef" ) String orderRef,
    		@QueryParam( "deviceCode" ) String deviceCode,
    		@QueryParam( "clientCode" ) String clientCode) throws JSONException {

    	String ipAddress = requestContext.getRemoteAddr();
        CourierFunction courierFunction = CourierFunction.getInstance();
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
        	
        	
            Response authResponse = courierFunction.getShipmentDetailsByTransactionRef(deviceCode, token, orderRef, requestId, ipAddress, clientCode);

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
    @Path( "listShipments" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response listShipments(
    		@Context HttpHeaders httpHeaders,
            @QueryParam( "startDate" ) String startDate,
            @QueryParam( "endDate" ) String endDate,
            @QueryParam( "scheduleStationCode" ) String scheduleStationCode,
            @QueryParam( "destinationStationCode" ) String destinationStationCode,
            @QueryParam( "trainCode" ) String trainCode,
            @QueryParam( "locomotiveNumber" ) String locomotiveNumber,
            @QueryParam( "clientCode" ) String clientCode,
            @QueryParam( "scheduleDate" ) String scheduleDate ) throws JSONException {

        CourierFunction courierFunction = CourierFunction.getInstance();
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
        	
            Response authResponse = courierFunction.listShipments(token, requestId, clientCode, startDate, endDate, scheduleStationCode, destinationStationCode, trainCode, locomotiveNumber, scheduleDate);

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
    @Path( "listShipmentItems" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response listShipmentItems(
    		@Context HttpHeaders httpHeaders,
            @QueryParam( "startDate" ) String startDate,
            @QueryParam( "endDate" ) String endDate,
            @QueryParam( "scheduleStationCode" ) String scheduleStationCode,
            @QueryParam( "destinationStationCode" ) String destinationStationCode,
            @QueryParam( "trainCode" ) String trainCode,
            @QueryParam( "locomotiveNumber" ) String locomotiveNumber,
            @QueryParam( "clientCode" ) String clientCode,
            @QueryParam( "trackingId" ) String trackingId,
            @QueryParam( "scheduleDate" ) String scheduleDate ) throws JSONException {

        CourierFunction courierFunction = CourierFunction.getInstance();
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
        	
            Response authResponse = courierFunction.listShipmentItems(token, requestId, clientCode, startDate, endDate, scheduleStationCode, destinationStationCode, 
            		trainCode, locomotiveNumber, scheduleDate, trackingId);

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
    @Path( "getShipment" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response getShipment(
    		@Context HttpHeaders httpHeaders,
            @QueryParam( "trackingId" ) String trackingId,
            @QueryParam( "clientCode" ) String clientCode ) throws JSONException {

        CourierFunction courierFunction = CourierFunction.getInstance();
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
        	
            Response authResponse = courierFunction.getShipment(trackingId, token, requestId, clientCode);

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
    @Path( "searchAvailableCourierTrips" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response searchAvailableCourierTrips(
    		@Context HttpHeaders httpHeaders,
    		@Context HttpServletRequest requestContext,
    		@FormParam( "deviceCode" ) String deviceCode, 
    		@FormParam( "departureStationCode" ) String departureStationCode, 
    		@FormParam( "arrivalStationCode" ) String arrivalStationCode, 
    		@FormParam( "departureTime" ) String departureTime, 
    		@FormParam( "hoursAdd" ) Integer hoursAdd, 
    		//@FormParam( "productCategoryCode" ) String productCategoryCode, 
    		@FormParam( "courierServiceCode" ) String courierServiceCode, 
    		//@FormParam( "description" ) String description, 
    		//@FormParam( "destinationCollectionPointCode" ) String destinationCollectionPointCode, 
    		@FormParam( "items" ) String items, 
    		/*@FormParam( "totalParcelHeight" ) Double totalParcelHeight, 
    		@FormParam( "totalParcelVolume" ) Double totalParcelVolume, 
    		@FormParam( "totalParcelWidth" ) Double totalParcelWidth,  
    		@FormParam( "totalParcelWeight" ) Double totalParcelWeight, */
    		//@FormParam( "parcelName" ) String parcelName, 
    		//@FormParam( "parcelQuantity" ) Integer parcelQuantity, 
    		/*@FormParam( "coefficientList" ) String coefficientList, */
    		@FormParam( "clientCode" ) String clientCode) throws JSONException {

    	String ipAddress = requestContext.getRemoteAddr();
        CourierFunction vehicleFunction = CourierFunction.getInstance();
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
        	//log.info">>>>" + deviceCode); 
        	//log.info">>>>" + token); 
        	//log.info">>>>" + departureStationCode); 
        	//log.info">>>>" + arrivalStationCode); 
        	//log.info">>>>" + departureTime); 
        	//log.info">>>>" + hoursAdd);  
        	////log.info">>>>" + productCategoryCode); 
        	////log.info">>>>" + courierServiceCode); 
        	////log.info">>>>" + description); 
        	////log.info">>>>" + destinationCollectionPointCode); 
        	////log.info">>>>" + totalParcelHeight); 
        	////log.info">>>>" + totalParcelVolume); 
        	////log.info">>>>" + totalParcelWidth);  
        	////log.info">>>>" + totalParcelWeight); 
        	////log.info">>>>" + parcelName); 
        	////log.info">>>>" + parcelQuantity); 
        	////log.info">>>>" + coefficientList); 
        	//log.info">>>>" + requestId); 
        	//log.info">>>>" + ipAddress); 
        	//log.info">>>>" + clientCode);
        	
        	
            Response authResponse = vehicleFunction.searchAvailableCourierTrips(deviceCode, token, departureStationCode, arrivalStationCode, departureTime, 
        			hoursAdd,  courierServiceCode, items, requestId, ipAddress, clientCode);
        			/* destinationCollectionPointCode,  description,totalParcelHeight, totalParcelVolume, totalParcelWidth,  totalParcelWeight, parcelName, */
        			 /*parcelQuantity,coefficientList,  */

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
    @Path( "updateShipmentStatus" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response updateShipmentStatus(
    		@Context HttpHeaders httpHeaders,
    		@Context HttpServletRequest requestContext,
    		@FormParam( "deviceCode" ) String deviceCode, 
    		@FormParam( "trackingDetails" ) String trackingDetails, 
    		@FormParam( "trackingId" ) String trackingId, 
    		@FormParam( "shipmentStatus" ) String shipmentStatus, 
    		@FormParam( "clientCode" ) String clientCode) throws JSONException {

    	String ipAddress = requestContext.getRemoteAddr();
        CourierFunction vehicleFunction = CourierFunction.getInstance();
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
        	
        	
            Response authResponse = vehicleFunction.updateShipmentStatus(shipmentStatus, trackingDetails, deviceCode, token, 
            		trackingId, requestId, ipAddress, clientCode);

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
