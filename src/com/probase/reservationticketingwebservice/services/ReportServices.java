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

@Path( "services/ReportServices" )
@Stateless( name = "ReportService", mappedName = "services/ReportServices" )
public class ReportServices {

	private static Logger log = Logger.getLogger(ReportServices.class);
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
    @Path( "getTransactionList" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response getTransactionList(
    		@Context HttpHeaders httpHeaders,
    		@Context HttpServletRequest requestContext,
            @FormParam( "startDate" ) String startDate,
            @FormParam( "endDate" ) String endDate,
            @FormParam( "userCode" ) String userCode,
            @FormParam( "zoneCode" ) String zoneCode,
            @FormParam( "departureStationCode" ) String departureStationCode,
            @FormParam( "vehicleCode" ) String vehicleCode,
            @FormParam( "cabinCode" ) String cabinCode,
            @FormParam( "paymentMeans" ) String paymentMeans,
            @FormParam( "vendorCode" ) String vendorCode,
            @FormParam( "clientCode" ) String clientCode,
            @FormParam( "transactionStatus" ) String transactionStatus,
            @FormParam( "groupBy" ) String groupBy,
            @FormParam( "reportType" ) String reportType,
            @FormParam( "serviceType" ) String serviceType) throws JSONException {

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
        	
            Response authResponse = vehicleFunction.getTransactionList( token, transactionStatus, groupBy, reportType,  startDate, endDate, userCode, zoneCode, 
            		departureStationCode, vehicleCode, cabinCode, paymentMeans, vendorCode, serviceType, 
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
    @Path( "getTransactionsByGroup" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response getTransactionsByGroup(
    		@Context HttpHeaders httpHeaders,
    		@Context HttpServletRequest requestContext,
            @FormParam( "groupType" ) String groupType,
            @FormParam( "groupTypeValue" ) String groupTypeValue,
            @FormParam( "startDate" ) String startDate,
            @FormParam( "endDate" ) String endDate,
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
        	
        	//log.info"*startDate -- " + startDate);
        	//log.info"*endDate -- " + endDate);
            Response authResponse = vehicleFunction.getTransactionsByGroup( token, groupType, groupTypeValue, requestId, clientCode, startDate, endDate );

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
