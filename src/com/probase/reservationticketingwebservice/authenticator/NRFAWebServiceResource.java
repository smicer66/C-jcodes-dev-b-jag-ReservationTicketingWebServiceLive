package com.probase.reservationticketingwebservice.authenticator;

import java.security.GeneralSecurityException;
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
import org.json.JSONException;
import org.json.JSONObject;
// implements NRFAWebServiceResourceProxy @Override
@Path( "reservation-web-service-resource-proxy" )
@Stateless( name = "NRFAWebServiceResource", mappedName = "ejb/NRFAWebServiceResource" )
public class NRFAWebServiceResource {

    private static final long serialVersionUID = -6663599014192066936L;

   
    @POST
    @Path( "login" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response login(
    		@Context HttpHeaders httpHeaders,
            @FormParam( "username" ) String username,
            @FormParam( "password" ) String password ) {

        /*Authenticator demoAuthenticator = Authenticator.getInstance();
        List<String> serviceKeyList = httpHeaders.getRequestHeader(HttpHeaderNames.SERVICE_KEY);
        String serviceKey = serviceKeyList.get(0);

        try {
            String authToken = demoAuthenticator.login( serviceKey, username, password, NULL );

            JSONObject jsonObj = new JSONObject();
            jsonObj.put( "auth_token", authToken );

            return getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();

        } catch ( final LoginException ex ) {
        	JSONObject jsonObj = new JSONObject();
            jsonObj.put( "message", "Problem matching service key, username and password" );

            return getNoCacheResponseBuilder( Response.Status.UNAUTHORIZED ).entity( jsonObj.toString() ).build();
        }*/
    	return getNoCacheResponseBuilder( Response.Status.OK ).entity( new JSONObject() ).build();
    }

    @GET
    @Path( "demo-get-method" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response demoGetMethod() throws JSONException {
    	JSONObject jsonObj = new JSONObject();
        jsonObj.put( "message", "Executed demoGetMethod" );

        return getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
    }

    @POST
    @Path( "demo-post-method" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response demoPostMethod(
    		@Context HttpHeaders httpHeaders) {
    	
    	try {
    		if(httpHeaders!=null && httpHeaders.getRequestHeaders().size()>0)
    		{
	            Authenticator demoAuthenticator = Authenticator.getInstance();
	            List<String> serviceKeyList = httpHeaders.getRequestHeader(HttpHeaderNames.SERVICE_KEY);
	            String serviceKey = serviceKeyList.get(0);
	            List<String> authTokenList = httpHeaders.getRequestHeader(HttpHeaderNames.AUTH_TOKEN);
	            String authToken = authTokenList.get(0);
	            //String serviceKey = httpHeaders.getHeaderString( HttpHeaderNames.SERVICE_KEY );
	            //String authToken = httpHeaders.getHeaderString( HttpHeaderNames.AUTH_TOKEN );
    		}

            JSONObject jsonObj = new JSONObject();
            jsonObj.put( "message", "Executed demoPostMethod" );
            
            return getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
        } catch ( final Exception ex ) {
            return getNoCacheResponseBuilder( Response.Status.INTERNAL_SERVER_ERROR ).build();
        }
    	
    }

    @POST
    @Path( "logout" )
    public Response logout(
        @Context HttpHeaders httpHeaders,
		@Context HttpServletRequest requestContext ) {
        try {
        	String ipAddress = requestContext.getRemoteAddr();
            Authenticator demoAuthenticator = Authenticator.getInstance();
            List<String> serviceKeyList = httpHeaders.getRequestHeader(HttpHeaderNames.SERVICE_KEY);
            String serviceKey = serviceKeyList.get(0);
            List<String> authTokenList = httpHeaders.getRequestHeader(HttpHeaderNames.AUTH_TOKEN);
            String authToken = authTokenList.get(0);
            //String serviceKey = httpHeaders.getHeaderString( HttpHeaderNames.SERVICE_KEY );
            //String authToken = httpHeaders.getHeaderString( HttpHeaderNames.AUTH_TOKEN );
            String requestId = RandomStringUtils.random(10, false, true);

            demoAuthenticator.logout( authToken, requestId, ipAddress );

            return getNoCacheResponseBuilder( Response.Status.NO_CONTENT ).build();
        } catch ( final Exception ex ) {
            return getNoCacheResponseBuilder( Response.Status.INTERNAL_SERVER_ERROR ).build();
        }
    }
    
    /*@OPTIONS
    @Path("{path : .*}")
    public Response options()
    {
    	return Response.ok()
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Headers", "Origin, Content-type, Accept, Authorization")
                .header("Access-Control-Allow-Credentials", "true")
                .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
                .header("Access-Control-Max-Age", "1209600")
                .build();
    }*/

    private Response.ResponseBuilder getNoCacheResponseBuilder( Response.Status status ) {
        CacheControl cc = new CacheControl();
        cc.setNoCache( true );
        cc.setMaxAge( -1 );
        cc.setMustRevalidate( true );

        return Response.status( status ).cacheControl( cc );
    }
}