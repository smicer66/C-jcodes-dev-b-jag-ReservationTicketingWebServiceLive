package com.probase.reservationticketingwebservice.services;

import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;

import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
// implements NRFAWebServiceResourceProxy @Override
import com.probase.reservationticketingwebservice.authenticator.Authenticator;
import com.probase.reservationticketingwebservice.authenticator.HttpHeaderNames;
import com.probase.reservationticketingwebservice.enumerations.RoleType;
import com.probase.reservationticketingwebservice.enumerations.UserStatus;
import com.probase.reservationticketingwebservice.models.User;
import com.probase.reservationticketingwebservice.util.Application;
import com.probase.reservationticketingwebservice.util.ERROR;
import com.probase.reservationticketingwebservice.util.UtilityHelper;

@Path( "services/AuthenticationServices" )
@Stateless( name = "AuthenticationService", mappedName = "services/AuthenticationServices" )
public class AuthenticationService {

	private static Logger log = Logger.getLogger(AuthenticationService.class);
	
    private static final long serialVersionUID = -6663599014192066936L;

   
    @POST
    @Path( "authenticateUser" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response authenticateUser(
    		@Context HttpHeaders httpHeaders,
    		@Context HttpServletRequest requestContext,
            @FormParam( "username" ) String username,
            @FormParam( "encPassword" ) String encPassword,
            @FormParam( "clientCode" ) String clientCode,
            @FormParam( "roleCode" ) String roleCode,
            @FormParam( "requireOtp" ) Boolean requireOtp ) throws JSONException {

    	String ipAddress = requestContext.getRemoteAddr();
        Authenticator authenticator = Authenticator.getInstance();
        //List<String> serviceKeyList = httpHeaders.getRequestHeader(HttpHeaderNames.SERVICE_KEY);
        //String serviceKey = serviceKeyList.get(0);
        String requestId = (new SimpleDateFormat("YYYYMMddHHmmss").format(new Date())) + ": ";

        try {
        	//log.inforequestId + "Proceed 1");
        	
            Response authResponse = authenticator.login( username, encPassword, clientCode, requestId, ipAddress, roleCode );
            

            return authResponse;

        } catch ( final LoginException ex ) {
        	log.warn(requestId+ ex);
        	JsonObjectBuilder jsonObject = Json.createObjectBuilder();
        	jsonObject.add( "message", "Problem matching logging in" );
            JsonObject jsonObj = jsonObject.build();

            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.UNAUTHORIZED ).entity( jsonObj.toString() ).build();
        }
        catch ( Exception ex ) {
        	log.warn(requestId+ ex);
        	JsonObjectBuilder jsonObject = Json.createObjectBuilder();
            JsonObject jsonObj = jsonObject.build();

        	return UtilityHelper.getNoCacheResponseBuilder( Response.Status.UNAUTHORIZED ).entity( jsonObj.toString() ).build();
        }
    }
    
    
    
    
    
    @POST
    @Path( "forgotPassword" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response forgotPassword(
    		@Context HttpHeaders httpHeaders,
    		@Context HttpServletRequest requestContext,
            @FormParam( "username" ) String username, 
            @FormParam( "clientCode" ) String clientCode) throws JSONException {

    	String ipAddress = requestContext.getRemoteAddr();
        Authenticator authenticator = Authenticator.getInstance();
        List<String> tokenList = httpHeaders.getRequestHeader(HttpHeaderNames.AUTH_TOKEN);
        String token = tokenList!=null && tokenList.size()>0 ? tokenList.get(0) : null;
        String requestId = (new SimpleDateFormat("YYYYMMddHHmmss").format(new Date())) + ": ";

        try {
        	if(token!=null)
        	{
        		JsonObjectBuilder jsonObject = Json.createObjectBuilder();
                JsonObject jsonObj = jsonObject.build();
                jsonObject.add("status", ERROR.UNAUTHORIZED_ACTION);
				jsonObject.add("message", "You cant carry out this action as a logged in user");

            	return UtilityHelper.getNoCacheResponseBuilder( Response.Status.UNAUTHORIZED ).entity( jsonObj.toString() ).build();
        	}
        	//log.info"Proceed 1");
            Response authResponse = authenticator.forgotPassword( username, requestId, ipAddress, clientCode );
            return authResponse;

        } 
        catch ( Exception ex ) {
        	JsonObjectBuilder jsonObject = Json.createObjectBuilder();
            JsonObject jsonObj = jsonObject.build();

        	return UtilityHelper.getNoCacheResponseBuilder( Response.Status.INTERNAL_SERVER_ERROR ).entity( jsonObj.toString() ).build();
        }
    }
    
    
    @POST
    @Path( "changePassword" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response changePassword(
    		@Context HttpHeaders httpHeaders,
    		@Context HttpServletRequest requestContext,
            @FormParam( "currentPassword" ) String currentPassword,
            @FormParam( "encPassword" ) String encPassword, 
            @FormParam( "clientCode") String clientCode) throws JSONException {

    	//log.info"proceed 0 ");
    	String ipAddress = requestContext.getRemoteAddr();
    	//log.info"proceed 1 ");
        Authenticator authenticator = Authenticator.getInstance();
        //log.info"proceed 2 ");
        List<String> tokenList = httpHeaders.getRequestHeader(HttpHeaderNames.AUTH_TOKEN);
        //log.info"proceed 3 ");
        String token = tokenList!=null && tokenList.size()>0 ? tokenList.get(0) : null;
        //log.info"proceed 4 ");
        String requestId = (new SimpleDateFormat("YYYYMMddHHmmss").format(new Date())) + ": ";
        //log.info"proceed 5 ");

        try {
        	//log.info"proceed 6 ");
        	if(token==null)
        	{
        		//log.info"proceed 7 ");
        		JsonObjectBuilder jsonObject = Json.createObjectBuilder();
                jsonObject.add("status", ERROR.UNAUTHORIZED_ACTION);
				jsonObject.add("message", "You cant carry out this action as a logged in user");
				//log.info"proceed 8 ");
                JsonObject jsonObj = jsonObject.build();
                //log.info"proceed 9 ");
            	return UtilityHelper.getNoCacheResponseBuilder( Response.Status.UNAUTHORIZED ).entity( jsonObj.toString() ).build();
        	}
        	//log.info"Proceed 1");
            Response authResponse = authenticator.changePassword( token, currentPassword, encPassword, requestId, ipAddress, clientCode );
            //log.info"proceed 10 ");
            return authResponse;

        } 
        catch ( Exception ex ) {
        	JsonObjectBuilder jsonObject = Json.createObjectBuilder();
            JsonObject jsonObj = jsonObject.build();

        	return UtilityHelper.getNoCacheResponseBuilder( Response.Status.INTERNAL_SERVER_ERROR ).entity( jsonObj.toString() ).build();
        }
    }
    
    
    @POST
    @Path( "addNewUser" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response addNewUser(
    		@Context HttpHeaders httpHeaders,
    		@Context HttpServletRequest requestContext,
            @FormParam( "username" ) String username,
            @FormParam( "email" ) String email,
            @FormParam( "mobileNumber" ) String mobileNumber,
            @FormParam( "userStatus" ) String userStatus,
            @FormParam( "details" ) String details,
            @FormParam( "updateFlag" ) Boolean updateFlag,
            @FormParam( "uniqueId" ) String uniqueId,
            @FormParam( "clientCode" ) String clientCode,
            @FormParam( "firstName" ) String firstName,
            @FormParam( "lastName" ) String lastName,
            @FormParam( "otherName" ) String otherName,
            @FormParam( "stationCode" ) String stationCode,
            @FormParam( "externalId" ) String externalId,
            @FormParam( "roleCode" ) String roleCode) throws JSONException {
    	
    	
    	//log.info"username = " + username);
        //log.info"email= " +  email);
        //log.info"mobileNumber= " +  mobileNumber);
        //log.info"userStatus= " +  userStatus);
        //log.info"details= " +  details);
        //log.infoupdateFlag);
        //log.info"uniqueId= " +  uniqueId);
        //log.info"firstName= " +  firstName);
        //log.info"lastName= " +  lastName);
        //log.info"otherName= " +  otherName);
        //log.info"roleCode= " +  roleCode);
        //log.info"stationCode= " +  stationCode);

        Authenticator authenticator = Authenticator.getInstance();
        String ipAddress = requestContext.getRemoteAddr();
        List<String> tokenList = httpHeaders.getRequestHeader(HttpHeaderNames.AUTH_TOKEN);
        String token = tokenList!=null && tokenList.size()>0 ? tokenList.get(0) : null;
        //log.info"token ==>" + token);
        String requestId = token.substring(token.length()-10) + "-" + (new SimpleDateFormat("YYYYMMddHHmmss").format(new Date()));

        try {
        	JsonObjectBuilder jsonObject = Json.createObjectBuilder();
        	if(updateFlag!=null && updateFlag.equals(Boolean.FALSE) && (username==null || email==null || mobileNumber==null || 
        			clientCode==null || roleCode==null))
        	{
        		//log.info"Level 1");
        		jsonObject.add("status", ERROR.UNAUTHORIZED_ACTION);
				jsonObject.add("message", "Bad request. Review request parameters");
				JsonObject jsonObj = jsonObject.build();

            	return UtilityHelper.getNoCacheResponseBuilder( Response.Status.BAD_REQUEST ).entity( jsonObj.toString() ).build();
        	}
        	else if(updateFlag!=null && updateFlag.equals(Boolean.TRUE) && (uniqueId==null || email==null || mobileNumber==null || clientCode==null))
        	{
        		jsonObject.add("status", ERROR.UNAUTHORIZED_ACTION);
				jsonObject.add("message", "Bad request. Review request parameters");
				JsonObject jsonObj = jsonObject.build();

            	return UtilityHelper.getNoCacheResponseBuilder( Response.Status.BAD_REQUEST ).entity( jsonObj.toString() ).build();
        	}
        		
        	if(token==null)
        	{
                jsonObject.add("status", ERROR.UNAUTHORIZED_ACTION);
				jsonObject.add("message", "Unauthorized Action");
				JsonObject jsonObj = jsonObject.build();

            	return UtilityHelper.getNoCacheResponseBuilder( Response.Status.UNAUTHORIZED ).entity( jsonObj.toString() ).build();
        	}
        	//log.inforequestId + ": " + "Proceed 1");
        	
            Response authResponse = authenticator.addNewUser( externalId, token, username, email, mobileNumber, userStatus, details, 
        			updateFlag, uniqueId, clientCode, firstName, lastName, otherName, roleCode, stationCode, requestId, ipAddress );
            return authResponse;

        } 
        catch ( Exception ex ) {
        	JsonObjectBuilder jsonObject = Json.createObjectBuilder();
            JsonObject jsonObj = jsonObject.build();
            log.warn(requestId + ": " + ex);

        	return UtilityHelper.getNoCacheResponseBuilder( Response.Status.INTERNAL_SERVER_ERROR ).entity( jsonObj.toString() ).build();
        }
    }
    
    
    @POST
    @Path( "listUsers" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response listUsers(
    		@Context HttpHeaders httpHeaders,
    		@FormParam( "userRoles" ) String userRoles,
    		@FormParam( "clientCode" ) String clientCode,
    		@Context HttpServletRequest requestContext) throws JSONException {

        Authenticator authenticator = Authenticator.getInstance();
        String ipAddress = requestContext.getRemoteAddr();
        List<String> tokenList = httpHeaders.getRequestHeader(HttpHeaderNames.AUTH_TOKEN);
        String token = tokenList!=null && tokenList.size()>0 ? tokenList.get(0) : null;
        //log.info"token ==>" + token);
        String requestId = token.substring(token.length()-10) + "-" + (new SimpleDateFormat("YYYYMMddHHmmss").format(new Date()));

        try {
        	JsonObjectBuilder jsonObject = Json.createObjectBuilder();
        	if(token==null)
        	{
                jsonObject.add("status", ERROR.UNAUTHORIZED_ACTION);
				jsonObject.add("message", "Unauthorized Action");
				JsonObject jsonObj = jsonObject.build();

            	return UtilityHelper.getNoCacheResponseBuilder( Response.Status.UNAUTHORIZED ).entity( jsonObj.toString() ).build();
        	}
        	//log.inforequestId + ": " + "Proceed 1");
            Response authResponse = authenticator.listUsers( userRoles, token, requestId, ipAddress, clientCode );
            return authResponse;

        } 
        catch ( Exception ex ) {
        	JsonObjectBuilder jsonObject = Json.createObjectBuilder();
            JsonObject jsonObj = jsonObject.build();
            log.warn(requestId + ": " + ex);

        	return UtilityHelper.getNoCacheResponseBuilder( Response.Status.INTERNAL_SERVER_ERROR ).entity( jsonObj.toString() ).build();
        }
    }
    
    
    
    @POST
    @Path( "updateUserStatus" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response updateUserStatus(
    		@Context HttpHeaders httpHeaders,
    		@Context HttpServletRequest requestContext,
            @FormParam( "username" ) String username,
    		@FormParam( "clientCode" ) String clientCode,
            @FormParam( "userStatus" ) String userStatus) throws JSONException {

        Authenticator authenticator = Authenticator.getInstance();
        String ipAddress = requestContext.getRemoteAddr();
        List<String> tokenList = httpHeaders.getRequestHeader(HttpHeaderNames.AUTH_TOKEN);
        String token = tokenList!=null && tokenList.size()>0 ? tokenList.get(0) : null;
        //log.info"token ==>" + token);
        String requestId = token.substring(token.length()-10) + "-" + (new SimpleDateFormat("YYYYMMddHHmmss").format(new Date()));

        try {
        	JsonObjectBuilder jsonObject = Json.createObjectBuilder();
        	if(userStatus==null)
        	{
        		jsonObject.add("status", ERROR.INCOMPLETE_PARAMETERS);
				jsonObject.add("message", "Bad request. Review request parameters");
				JsonObject jsonObj = jsonObject.build();

            	return UtilityHelper.getNoCacheResponseBuilder( Response.Status.BAD_REQUEST ).entity( jsonObj.toString() ).build();
        	}
        		
        	if(token==null)
        	{
                jsonObject.add("status", ERROR.UNAUTHORIZED_ACTION);
				jsonObject.add("message", "Unauthorized Action");
				JsonObject jsonObj = jsonObject.build();

            	return UtilityHelper.getNoCacheResponseBuilder( Response.Status.UNAUTHORIZED ).entity( jsonObj.toString() ).build();
        	}
        	//log.inforequestId + ": " + "Proceed 1");
            Response authResponse = authenticator.updateUserStatus( token, username, userStatus, requestId, ipAddress, clientCode );
            return authResponse;

        } 
        catch ( Exception ex ) {
        	JsonObjectBuilder jsonObject = Json.createObjectBuilder();
            JsonObject jsonObj = jsonObject.build();
            log.warn(requestId + ": " + ex);

        	return UtilityHelper.getNoCacheResponseBuilder( Response.Status.INTERNAL_SERVER_ERROR ).entity( jsonObj.toString() ).build();
        }
    }
    
    
    @GET
    @Path( "getUserStatusList" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response getUserStatusList() throws JSONException {
    	JsonObjectBuilder jsonObject = Json.createObjectBuilder();
		UserStatus[] userStatuses = UserStatus.values();
		for(int c=0; c<userStatuses.length; c++)
		{
			jsonObject.add(userStatuses[c].ordinal() + "", userStatuses[c].name());
		}
        JsonObject jsonObj = jsonObject.build();

        return getCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
    }

    @GET
    @Path( "getUserRoleCodes" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response getUserRoleCodes() throws JSONException {
    	JsonObjectBuilder jsonObject = Json.createObjectBuilder();
		RoleType[] userTypes = RoleType.values();
		for(int c=0; c<userTypes.length; c++)
		{
			jsonObject.add(userTypes[c].ordinal() + "", userTypes[c].name());
		}
		JsonObject jsonObj = jsonObject.build();

        return getCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
    }


    @POST
    @Path( "logout" )
    public Response logout(
        @Context HttpHeaders httpHeaders,
		@Context HttpServletRequest requestContext) {
        try {
        	String ipAddress = requestContext.getRemoteAddr();
            Authenticator demoAuthenticator = Authenticator.getInstance();
            List<String> authTokenList = httpHeaders.getRequestHeader(HttpHeaderNames.AUTH_TOKEN);
            String authToken = authTokenList.get(0);
            String requestId = authToken.substring(authToken.length()-10) + "-" + (new SimpleDateFormat("YYYYMMddHHmmss").format(new Date())) + ": ";
            //String serviceKey = httpHeaders.getHeaderString( HttpHeaderNames.SERVICE_KEY );
            //String authToken = httpHeaders.getHeaderString( HttpHeaderNames.AUTH_TOKEN );

            demoAuthenticator.logout( authToken, requestId, ipAddress );

            JsonObjectBuilder jsonObject = Json.createObjectBuilder();
    		RoleType[] userTypes = RoleType.values();
    		jsonObject.add("status", ERROR.GENERAL_SUCCESS);
    		jsonObject.add("message", "Logged Out Successfully");
    		JsonObject jsonObj = jsonObject.build();

            return getCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
        } catch ( final Exception ex ) {
        	ex.printStackTrace();
            return getNoCacheResponseBuilder( Response.Status.INTERNAL_SERVER_ERROR ).build();
        }
    }
    
    

    private Response.ResponseBuilder getNoCacheResponseBuilder( Response.Status status ) {
        CacheControl cc = new CacheControl();
        cc.setNoCache( true );
        cc.setMaxAge( -1 );
        cc.setMustRevalidate( true );

        return Response.status( status ).cacheControl( cc );
    }
    
    
    private Response.ResponseBuilder getCacheResponseBuilder( Response.Status status ) {
        CacheControl cc = new CacheControl();
        cc.setNoCache( false );
        cc.setMaxAge( (60 * 60/2) );
        cc.setMustRevalidate( true );

        return Response.status( status ).cacheControl( cc );
    }
    
    
}