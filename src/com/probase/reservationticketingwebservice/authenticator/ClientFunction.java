package com.probase.reservationticketingwebservice.authenticator;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.security.auth.login.LoginException;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.probase.reservationticketingwebservice.enumerations.CardStatus;
import com.probase.reservationticketingwebservice.enumerations.CardType;
import com.probase.reservationticketingwebservice.enumerations.Channel;
import com.probase.reservationticketingwebservice.enumerations.ClientServiceType;
import com.probase.reservationticketingwebservice.enumerations.CustomerStatus;
import com.probase.reservationticketingwebservice.enumerations.CustomerType;
import com.probase.reservationticketingwebservice.enumerations.DeviceStatus;
import com.probase.reservationticketingwebservice.enumerations.DeviceType;
import com.probase.reservationticketingwebservice.enumerations.PassengerType;
import com.probase.reservationticketingwebservice.enumerations.PaymentMeans;
import com.probase.reservationticketingwebservice.enumerations.PriceType;
import com.probase.reservationticketingwebservice.enumerations.ProductPriceCoefficient;
import com.probase.reservationticketingwebservice.enumerations.PurchasedTripStatus;
import com.probase.reservationticketingwebservice.enumerations.RequestType;
import com.probase.reservationticketingwebservice.enumerations.RoleType;
import com.probase.reservationticketingwebservice.enumerations.SeatAvailabilityStatus;
import com.probase.reservationticketingwebservice.enumerations.ServiceType;
import com.probase.reservationticketingwebservice.enumerations.TransactionCurrency;
import com.probase.reservationticketingwebservice.enumerations.TransactionStatus;
import com.probase.reservationticketingwebservice.enumerations.UserStatus;
import com.probase.reservationticketingwebservice.enumerations.VehicleType;
import com.probase.reservationticketingwebservice.enumerations.VendorStatus;
import com.probase.reservationticketingwebservice.models.AuditTrail;
import com.probase.reservationticketingwebservice.models.CardScheme;
import com.probase.reservationticketingwebservice.models.Client;
import com.probase.reservationticketingwebservice.models.ClientSubscription;
import com.probase.reservationticketingwebservice.models.Country;
import com.probase.reservationticketingwebservice.models.CourierService;
import com.probase.reservationticketingwebservice.models.Customer;
import com.probase.reservationticketingwebservice.models.Device;
import com.probase.reservationticketingwebservice.models.District;
import com.probase.reservationticketingwebservice.models.ProductCategory;
import com.probase.reservationticketingwebservice.models.PurchasedTrip;
import com.probase.reservationticketingwebservice.models.PurchasedTripSeat;
import com.probase.reservationticketingwebservice.models.ScheduleStationSeatAvailability;
import com.probase.reservationticketingwebservice.models.Setting;
import com.probase.reservationticketingwebservice.models.Station;
import com.probase.reservationticketingwebservice.models.TicketCollectionPoint;
import com.probase.reservationticketingwebservice.models.TripCard;
import com.probase.reservationticketingwebservice.models.Transaction;
import com.probase.reservationticketingwebservice.models.TripZone;
import com.probase.reservationticketingwebservice.models.TripZoneStation;
import com.probase.reservationticketingwebservice.models.User;
import com.probase.reservationticketingwebservice.models.Vehicle;
import com.probase.reservationticketingwebservice.models.VehicleSchedule;
import com.probase.reservationticketingwebservice.models.VehicleSeatClass;
import com.probase.reservationticketingwebservice.models.VehicleSeatSection;
import com.probase.reservationticketingwebservice.models.VehicleTripPrice;
import com.probase.reservationticketingwebservice.models.Vendor;
import com.probase.reservationticketingwebservice.models.Wallet;
import com.probase.reservationticketingwebservice.util.Application;
import com.probase.reservationticketingwebservice.util.ERROR;
import com.probase.reservationticketingwebservice.util.PrbCustomService;
import com.probase.reservationticketingwebservice.util.ServiceLocator;
import com.probase.reservationticketingwebservice.util.SmsSender;
import com.probase.reservationticketingwebservice.util.SwpService;
import com.probase.reservationticketingwebservice.util.UtilityHelper;
import com.sun.org.apache.bcel.internal.generic.NEW;

public final class ClientFunction {

    private static ClientFunction authenticator = null;

    // A user storage which stores <username, password>
    private final Map<String, String> usersStorage = new HashMap();

    // A service key storage which stores <service_key, username>
    private final Map<String, String> serviceKeysStorage = new HashMap();

    // An authentication token storage which stores <service_key, auth_token>.
    private final Map<String, String> authorizationTokensStorage = new HashMap();
    
    private static Logger log = Logger.getLogger(ClientFunction.class);
	private ServiceLocator serviceLocator = null;
	public SwpService swpService = null;
	public PrbCustomService swpCustomService = PrbCustomService.getInstance();
	Application application = null;
	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private ClientFunction() {
        // The usersStorage pretty much represents a user table in the database
        //usersStorage.put( "username1", "passwordForUser1" );
        //usersStorage.put( "username2", "passwordForUser2" );
        //usersStorage.put( "username3", "passwordForUser3" );

        /**
         * Service keys are pre-generated by the system and is given to the
         * authorized client who wants to have access to the REST API. Here,
         * only username1 and username2 is given the REST service access with
         * their respective service keys.
         */
        //serviceKeysStorage.put( "f80ebc87-ad5c-4b29-9366-5359768df5a1", "username1" );
        //serviceKeysStorage.put( "3b91cab8-926f-49b6-ba00-920bcf934c2a", "username2" );
    	serviceLocator = ServiceLocator.getInstance();
    }

    public static ClientFunction getInstance() {
        if ( authenticator == null ) {
            authenticator = new ClientFunction();
        }

        return authenticator;
    }
    
    
    
	public Response createNewClient(String contactMobile, String contactEmail, String contactPerson, 
			String clientName, String clientAddressLine1, String clientAddressLine2, Double bookingFee, 
			String token, String requestId, String ipAddress, String clientServiceType) {
		// TODO Auto-generated method stub
		JsonObjectBuilder jsonObject = Json.createObjectBuilder();
    	
    	
		try{
			//log.info(requestId + "Test 1");
			if(clientName==null || token==null || clientServiceType==null)
			{
				//log.info(requestId + "Test 2");
				jsonObject.add("status", ERROR.INCOMPLETE_PARAMETERS);
				jsonObject.add("message", "Incomplete Parameters provided in request.");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			VehicleType cst = null;
			try
			{
				 cst = VehicleType.valueOf(clientServiceType);
			}
			catch(IllegalArgumentException | NullPointerException e)
			{
				//log.info(requestId + "Test 4");
				jsonObject.add("status", ERROR.SERVICE_TYPE_INVALID);
				jsonObject.add("message", "Invalid Client Service Type Specified");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			//log.info(requestId + "Test 3");
			swpService = serviceLocator.getSwpService();
			Application app = Application.getInstance(swpService);
			JSONObject verifyJ = UtilityHelper.verifyToken(token, app);
			if(verifyJ.length()==0 || (verifyJ.length()>0 && verifyJ.has("active") && verifyJ.getInt("active")==0))
			{
				//log.info(requestId + "Test 4");
				jsonObject.add("status", ERROR.FORCE_LOGOUT_USER);
				jsonObject.add("message", "Your session has expired. Please log in again");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			//log.info(requestId + "verifyJ ==" + verifyJ.toString());
			String username = verifyJ.has("username") ? verifyJ.getString("username") : null;
			//log.info(requestId + "username ==" + (username==null ? "" : username));
			String roleCode_ = verifyJ.has("roleCode") ? verifyJ.getString("roleCode") : null;
			//log.info(requestId + "roleCode_ ==" + (roleCode_==null ? "" : roleCode_));
			User user = null;
			RoleType roleCode = null;
			
			if(roleCode_ != null)
			{
				roleCode = RoleType.valueOf(roleCode_);
			}
			
			if(username!=null)
			{
				String hql = "select tp from User tp where tp.username = '" + username + "' AND " +
						"tp.userStatus = " + UserStatus.ACTIVE.ordinal() + " AND tp.deletedAt IS NULL";
				//log.info(requestId + "hql ==" + hql);
				user = (User)this.swpService.getUniqueRecordByHQL(hql);
				
				
				if(user==null)
				{

					//log.info(requestId + "user IS NULL");
					//log.info(requestId + "user firstname = " + user.getFirstName());
					//log.info(requestId + "user lastname = " + user.getLastName());
					jsonObject.add("status", ERROR.INVALID_CLIENT_CREATION_PRIVILEDGES);
					jsonObject.add("message", "Invalid Client Creation Priviledges");
					JsonObject jsonObj = jsonObject.build();
		            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
				}
				//roleCode = user.getRoleCode();
				//log.info("roleCode ==" + roleCode.name());
			}
			
			if(roleCode==null || (roleCode!=null && !roleCode.equals(RoleType.SUPER_ADMIN)))
			{
				jsonObject.add("status", ERROR.INVALID_CLIENT_CREATION_PRIVILEDGES);
				jsonObject.add("message", "Invalid Client Creation Priviledges");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
				
			String clientCode = RandomStringUtils.randomNumeric(10).toUpperCase();
			Client client = new Client();
			client.setClientAddressLine1(clientAddressLine1);
			client.setClientAddressLine2(clientAddressLine2);
			client.setClientCode(clientCode);
			client.setClientName(clientName);
			client.setCreatedAt(new Date());
			client.setUpdatedAt(new Date());
			client.setPrivateKey(RandomStringUtils.randomAlphanumeric(16));
			client.setPublicKey(RandomStringUtils.randomAlphanumeric(16));
			client.setBookingFee(bookingFee);
			client.setClientStatus(Boolean.TRUE);
			client.setVehicleType(cst);
			

			client = (Client)this.swpService.createNewRecord(client);

			JSONObject js = new JSONObject();
			JSONObject txnObjects = new JSONObject();
			app.getInstance(this.swpService, Boolean.TRUE);

			AuditTrail ad = UtilityHelper.createAuditTrailEntry(ipAddress, RequestType.NEW_CLIENT_CREATION, requestId, this.swpService, 
					verifyJ.has("username") ? verifyJ.getString("username") : null, client.getClientId(), Client.class.getName(), 
					"New client created successfully. Client Name: " + clientName, clientCode);
			
			
			Authenticator auth = Authenticator.getInstance();
			String clientUsername = contactEmail;
			String email = contactEmail;
			String mobileNumber = contactMobile;
			String[] names = contactPerson.split(" ");
			String firstName = names.length>0 ? names[0] : "";
			String lastName = names.length> 1 ? names[1] : "";
			String otherName = names.length> 2 ? names[2] : "";
			String clientRoleCode = RoleType.ADMIN_STAFF.name();
			
			Response clientUserResponse = auth.addNewUser(null, token, clientUsername, email, mobileNumber, UserStatus.ACTIVE.name(), null, Boolean.FALSE, null, clientCode, 
					firstName, lastName, otherName, clientRoleCode, null, requestId, ipAddress);
			String clientUserStr = clientUserResponse.getEntity().toString();
			JSONObject clientUserJS = new JSONObject(clientUserStr);
			if(clientUserJS.has("status") && clientUserJS.getInt("status")== (ERROR.GENERAL_SUCCESS))
			{
				String userAccount = clientUserJS.getString("userAccount");
				JSONObject user_ = new JSONObject(userAccount);
				String hql = "Select tp from User tp where tp.userId = " + user_.getLong("userId") + " AND tp.deletedAt is NULL and tp.client.clientCode = '"+clientCode+"'";
				User user__ = (User)swpService.getUniqueRecordByHQL(hql);
				if(user__!=null)
				{
					user__.setClient(client);
					swpService.updateRecord(user__);

					String userDetails = clientUserJS.getString("user");
					jsonObject.add("message", "Client generated successfully");
					jsonObject.add("status", ERROR.GENERAL_SUCCESS);
					jsonObject.add("clientDetails", new Gson().toJson(client));
					jsonObject.add("userDetails", userDetails);
					JsonObject jsonObj = jsonObject.build();
					//log.info(requestId + " -- " + jsonObj.toString());
		            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
				}
				else
				{
					swpService.deleteRecord(client);
				}
				
			}

			
			
			swpService.deleteRecord(client);

			jsonObject.add("message", "Client generation failed");
			jsonObject.add("status", ERROR.GENERAL_FAIL);
			JsonObject jsonObj = jsonObject.build();
			//log.info(requestId + " -- " + jsonObj.toString());
            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			
		}catch(Exception e)
		{
			log.warn(e); log.error("exception...", e);
			e.printStackTrace();
			JsonObject jsonObj = jsonObject.build();
            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
		}
	}
	
	
	public Response createNewDevice(String deviceType, String serialNo, String clientCode, String token, String requestId, String ipAddress) {
		// TODO Auto-generated method stub
		JsonObjectBuilder jsonObject = Json.createObjectBuilder();
    	
    	
		try{
			//log.info(requestId + "Test 1");
			if(deviceType==null || clientCode==null || token==null)
			{
				//log.info(requestId + "Test 2");
				jsonObject.add("status", ERROR.INCOMPLETE_PARAMETERS);
				jsonObject.add("message", "Incomplete Parameters provided in request.");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			//log.info(requestId + "Test 3");
			swpService = serviceLocator.getSwpService();
			Application app = Application.getInstance(swpService);
			JSONObject verifyJ = UtilityHelper.verifyToken(token, app);
			if(verifyJ.length()==0 || (verifyJ.length()>0 && verifyJ.has("active") && verifyJ.getInt("active")==0))
			{
				//log.info(requestId + "Test 4");
				jsonObject.add("status", ERROR.FORCE_LOGOUT_USER);
				jsonObject.add("message", "Your session has expired. Please log in again");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			//log.info(requestId + "verifyJ ==" + verifyJ.toString());
			String username = verifyJ.has("username") ? verifyJ.getString("username") : null;
			//log.info(requestId + "username ==" + (username==null ? "" : username));
			String roleCode_ = verifyJ.has("roleCode") ? verifyJ.getString("roleCode") : null;
			//log.info(requestId + "roleCode_ ==" + (roleCode_==null ? "" : roleCode_));
			User user = null;
			RoleType roleCode = null;
			
			if(roleCode_ != null)
			{
				roleCode = RoleType.valueOf(roleCode_);
			}
			
			if(username!=null)
			{
				String hql = "select tp from User tp where tp.username = '" + username + "' AND " +
						"tp.userStatus = " + UserStatus.ACTIVE.ordinal() + " AND tp.deletedAt IS NULL";
				//log.info(requestId + "hql ==" + hql);
				user = (User)this.swpService.getUniqueRecordByHQL(hql);
				
				
				if(user==null)
				{

					//log.info(requestId + "user IS NULL");
					//log.info(requestId + "user firstname = " + user.getFirstName());
					//log.info(requestId + "user lastname = " + user.getLastName());
					jsonObject.add("status", ERROR.INVALID_DEVICE_CREATION_PRIVILEDGES);
					jsonObject.add("message", "Invalid Device Creation Priviledges");
					JsonObject jsonObj = jsonObject.build();
		            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
				}
				//roleCode = user.getRoleCode();
			}
			
			if(roleCode==null || (roleCode!=null && !roleCode.equals(RoleType.ADMIN_STAFF)))
			{
				jsonObject.add("status", ERROR.INVALID_DEVICE_CREATION_PRIVILEDGES);
				jsonObject.add("message", "Invalid Device Creation Priviledges");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			String hql = "Select tp from Client tp where tp.clientCode = '" + clientCode + "' AND tp.deletedAt IS NULL";
			Client client = (Client)this.swpService.getUniqueRecordByHQL(hql);
			
			DeviceType dt = DeviceType.valueOf(deviceType);
			
				
			String deviceCode = RandomStringUtils.randomNumeric(10).toUpperCase();
			String deviceTerminalApiKey = RandomStringUtils.randomAlphanumeric(32);
			Device device = new Device();
			device.setCreatedAt(new Date());
			device.setUpdatedAt(new Date());
			device.setDeviceCode(deviceCode);
			device.setDeviceSerialNo(serialNo);
			device.setDeviceType(dt);
			device.setSetupByUser(user);
			device.setClient(client);
			device.setTerminalApiKey(deviceTerminalApiKey);
			device.setStatus(DeviceStatus.ACTIVE);
			device = (Device)this.swpService.createNewRecord(device);

			JSONObject js = new JSONObject();
			JSONObject txnObjects = new JSONObject();
			app.getInstance(this.swpService, Boolean.TRUE);

			AuditTrail ad = UtilityHelper.createAuditTrailEntry(ipAddress, RequestType.NEW_DEVICE_CREATION, requestId, this.swpService, 
					verifyJ.has("username") ? verifyJ.getString("username") : null, device.getDeviceId(), Device.class.getName(), 
					"New Device Created. Device Code: " + device.getDeviceCode(), clientCode);

			jsonObject.add("message", "Device generated successfully");
			jsonObject.add("status", ERROR.GENERAL_SUCCESS);
			jsonObject.add("deviceDetails", new Gson().toJson(device));
			JsonObject jsonObj = jsonObject.build();
			//log.info(requestId + " -- " + jsonObj.toString());
            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			
		}catch(Exception e)
		{
			log.warn(e); log.error("exception...", e);
			e.printStackTrace();
			JsonObject jsonObj = jsonObject.build();
            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
		}
	}
	
	
	
	

	
	public Response updateClient(String clientCode, String clientName, String clientTheme, String clientAddressLine1, String clientAddressLine2, Long countryId, Boolean clientStatus, String token, 
			String requestId, String ipAddress) {
		// TODO Auto-generated method stub
		JsonObjectBuilder jsonObject = Json.createObjectBuilder();
    	
    	
		try{
			
			if(clientCode!=null || clientName==null || countryId==null || token==null || clientStatus!=null)
			{
				jsonObject.add("status", ERROR.INCOMPLETE_PARAMETERS);
				jsonObject.add("message", "Incomplete Parameters provided in request.");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			swpService = serviceLocator.getSwpService();
			Application app = Application.getInstance(swpService);
			JSONObject verifyJ = UtilityHelper.verifyToken(token, app);
			if(verifyJ.length()==0 || (verifyJ.length()>0 && verifyJ.has("active") && verifyJ.getInt("active")==0))
			{
				jsonObject.add("status", ERROR.FORCE_LOGOUT_USER);
				jsonObject.add("message", "Your session has expired. Please log in again");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			String username = verifyJ.has("username") ? verifyJ.getString("username") : null;
			//log.info(requestId + "username ==" + (username==null ? "" : username));
			User user = null;
			RoleType roleCode = null;
			
			if(username!=null)
			{
				user = (User)this.swpService.getUniqueRecordByHQL("select tp from User tp where tp.username = '" + username + "' AND " +
						"tp.userStatus = " + UserStatus.ACTIVE.ordinal() + " AND tp.deletedAt IS NULL");
				//roleCode = user.getRoleCode();
				if(user==null)
				{
					jsonObject.add("status", ERROR.INVALID_CLIENT_UPDATE_PRIVILEDGES);
					jsonObject.add("message", "Your session has expired. Please log in again");
					JsonObject jsonObj = jsonObject.build();
		            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
				}
			}
			
			if(roleCode==null || (roleCode!=null && !roleCode.equals(RoleType.ADMIN_STAFF.name())))
			{
				jsonObject.add("status", ERROR.INVALID_CLIENT_UPDATE_PRIVILEDGES);
				jsonObject.add("message", "Invalid Client Creation Priviledges");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}

			String hql = "Select tp from Country tp where id = " + countryId + " AND tp.deletedAt IS NULL";
			Country country = (Country)this.swpService.getUniqueRecordByHQL(hql);
			
			hql = "Select tp from Client tp where tp.clientCode = '" + clientCode + "' AND tp.deletedAt IS NULL";
			Client client = (Client)this.swpService.getUniqueRecordByHQL(hql);
			client.setClientAddressLine1(clientAddressLine1);
			client.setClientAddressLine2(clientAddressLine2);
			client.setClientName(clientName);
			client.setClientStatus(clientStatus);
			client.setUpdatedAt(new Date());
			this.swpService.updateRecord(client);
			

			AuditTrail ad = UtilityHelper.createAuditTrailEntry(ipAddress, RequestType.CLIENT_UPDATE, requestId, this.swpService, 
					verifyJ.has("username") ? verifyJ.getString("username") : null, client.getClientId(), Client.class.getName(),
					"Client Update. Client: " + client.getClientName() + " | " + clientCode+"~"+clientName+"~"+clientTheme+"~"+clientAddressLine1+"~"+clientAddressLine2+"~"+countryId+"~"+Boolean.toString(clientStatus), clientCode);

				
			jsonObject.add("message", "Client updated successfully");
			jsonObject.add("status", ERROR.GENERAL_SUCCESS);
			jsonObject.add("merchantDetails", new Gson().toJson(client));
			JsonObject jsonObj = jsonObject.build();
            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			
		}catch(Exception e)
		{
			log.warn(e); log.error("exception...", e);
			e.printStackTrace();
			JsonObject jsonObj = jsonObject.build();
            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
		}
	}

	
	
	public Response getClientList(Integer startIndex,
			Integer limit, String token, String requestId) {
		// TODO Auto-generated method stub
		JsonObjectBuilder jsonObject = Json.createObjectBuilder();
    	
    	
		try{
			
			if(startIndex==null)
			{
				jsonObject.add("status", ERROR.INCOMPLETE_PARAMETERS);
				jsonObject.add("message", "Incomplete Parameters provided in request.");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			if(limit==null || (limit!=null && limit>50))
				limit = 50;
			
			swpService = serviceLocator.getSwpService();
			Application app = Application.getInstance(swpService);
			JSONObject verifyJ = UtilityHelper.verifyToken(token, app);
			if(verifyJ.length()==0 || (verifyJ.length()>0 && verifyJ.has("active") && verifyJ.getInt("active")==0))
			{
				jsonObject.add("status", ERROR.FORCE_LOGOUT_USER);
				jsonObject.add("message", "Your session has expired. Please log in again");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			String username = verifyJ.has("username") ? verifyJ.getString("username") : null;
			//log.info(requestId + "username ==" + (username==null ? "" : username));
			String roleCode_ = verifyJ.has("roleCode") ? verifyJ.getString("roleCode") : null;
			//log.info(requestId + "roleCode_ ==" + (roleCode_==null ? "" : roleCode_));
			User user = null;
			RoleType roleCode = null;
			
			if(roleCode_ != null)
			{
				roleCode = RoleType.valueOf(roleCode_);
			}
			
			if(username!=null)
			{
				user = (User)this.swpService.getUniqueRecordByHQL("select tp from User tp where tp.username = '" + username + "' AND " +
						"tp.userStatus = " + UserStatus.ACTIVE.ordinal() + " AND tp.deletedAt IS NULL");
				//roleCode = user.getRoleCode();
				if(user==null)
				{
					jsonObject.add("status", ERROR.INVALID_CLIENT_UPDATE_PRIVILEDGES);
					jsonObject.add("message", "Your session has expired. Please log in again");
					JsonObject jsonObj = jsonObject.build();
		            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
				}
			}
			
			if(roleCode!=null && !roleCode.equals(RoleType.ADMIN_STAFF.name()))
			{
				jsonObject.add("status", ERROR.INVALID_CLIENT_LISTING_PRIVILEDGES);
				jsonObject.add("message", "Invalid Client Listing Priviledges");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			String hql = "Select distinct tp from Client tp where tp.deletedAt IS NULL ORDER BY tp.createdAt DESC";
			//DE LIMIT " + startIndex + ", " + limit;
			Collection<Client> clients= (Collection<Client>)this.swpService.getAllRecordsByHQL(hql, startIndex, limit);
			
			String hql1 = "Select count(tp.id) as idCount from Client tp where tp.deletedAt IS NULL ORDER BY tp.createdAt DESC";
			//log.info(requestId + "4.hql ==" + hql1);
			List<Long> totalClientCount = (List<Long>)swpService.getAllRecordsByHQL(hql1);
			Long totalClients = totalClientCount!=null ? totalClientCount.iterator().next() : 0;
			//log.info(requestId + "totalClientCount ==" + totalClients);
			
			JSONArray jsonArray = new JSONArray();
			
			jsonObject.add("message", "Merchant List generated successfully");
			jsonObject.add("status", ERROR.GENERAL_SUCCESS);
			jsonObject.add("merchantList", new Gson().toJson(clients));
			jsonObject.add("totalMerchantCount", totalClients);
			JsonObject jsonObj = jsonObject.build();
			//log.info(requestId + " -- " + jsonObj.toString());
            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			
		}catch(Exception e)
		{
			log.warn(e); log.error("exception...", e);
			e.printStackTrace();
			JsonObject jsonObj = jsonObject.build();
            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
		}
	}
	
	
	
	public Response getDeviceList(String clientCode, String token, String requestId, String ipAddress) {
		// TODO Auto-generated method stub
		JsonObjectBuilder jsonObject = Json.createObjectBuilder();
    	
    	
		try{
			
			if(clientCode==null)
			{
				jsonObject.add("status", ERROR.INCOMPLETE_PARAMETERS);
				jsonObject.add("message", "Incomplete Parameters provided in request.");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			
			swpService = serviceLocator.getSwpService();
			Application app = Application.getInstance(swpService);
			JSONObject verifyJ = UtilityHelper.verifyToken(token, app);
			if(verifyJ.length()==0 || (verifyJ.length()>0 && verifyJ.has("active") && verifyJ.getInt("active")==0))
			{
				jsonObject.add("status", ERROR.FORCE_LOGOUT_USER);
				jsonObject.add("message", "Your session has expired. Please log in again");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			String username = verifyJ.has("username") ? verifyJ.getString("username") : null;
			//log.info(requestId + "username ==" + (username==null ? "" : username));
			String roleCode_ = verifyJ.has("roleCode") ? verifyJ.getString("roleCode") : null;
			//log.info(requestId + "roleCode_ ==" + (roleCode_==null ? "" : roleCode_));
			User user = null;
			RoleType roleCode = null;
			
			if(roleCode_ != null)
			{
				roleCode = RoleType.valueOf(roleCode_);
			}
			
			if(username!=null)
			{
				user = (User)this.swpService.getUniqueRecordByHQL("select tp from User tp where tp.username = '" + username + "' AND " +
						"tp.userStatus = " + UserStatus.ACTIVE.ordinal() + " AND tp.deletedAt IS NULL");
				//roleCode = user.getRoleCode();
				if(user==null)
				{
					jsonObject.add("status", ERROR.INVALID_CLIENT_UPDATE_PRIVILEDGES);
					jsonObject.add("message", "Your session has expired. Please log in again");
					JsonObject jsonObj = jsonObject.build();
		            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
				}
			}
			
			//log.info("roleCode == " + roleCode.name());
			if(roleCode!=null && !roleCode.equals(RoleType.ADMIN_STAFF))
			{
				jsonObject.add("status", ERROR.INVALID_CLIENT_LISTING_PRIVILEDGES);
				jsonObject.add("message", "Invalid Client Listing Priviledges");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			String hql = "Select distinct tp from Client tp where tp.deletedAt IS NULL AND tp.clientCode = '"+clientCode+"'";
			//DE LIMIT " + startIndex + ", " + limit;
			Client client= (Client)this.swpService.getUniqueRecordByHQL(hql);
			
			if(client==null)
			{
				jsonObject.add("status", ERROR.INVALID_CLIENT_CODE_PROVIDED);
				jsonObject.add("message", "Invalid Client Code Provided");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			hql = "Select tp from Device tp where tp.deletedAt IS NULL and tp.client.clientCode = '"+clientCode+"'";
			Collection<Device> deviceList = (Collection<Device>)swpService.getAllRecordsByHQL(hql);
			JSONArray jsonArray = new JSONArray();
			
			jsonObject.add("message", "Device List generated successfully");
			jsonObject.add("status", ERROR.GENERAL_SUCCESS);
			jsonObject.add("deviceList", new Gson().toJson(deviceList));
			JsonObject jsonObj = jsonObject.build();
			//log.info(requestId + " -- " + jsonObj.toString());
            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			
		}catch(Exception e)
		{
			log.warn(e); log.error("exception...", e);
			e.printStackTrace();
			JsonObject jsonObj = jsonObject.build();
            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
		}
	}

	
	public Response getClient(String clientCode, String token, String requestId) {
		// TODO Auto-generated method stub
		JsonObjectBuilder jsonObject = Json.createObjectBuilder();
    	
    	
		try{
			
			if(clientCode==null)
			{
				jsonObject.add("status", ERROR.INCOMPLETE_PARAMETERS);
				jsonObject.add("message", "Incomplete Parameters provided in request.");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			
			swpService = serviceLocator.getSwpService();
			Application app = Application.getInstance(swpService);
			JSONObject verifyJ = UtilityHelper.verifyToken(token, app);
			if(verifyJ.length()==0 || (verifyJ.length()>0 && verifyJ.has("active") && verifyJ.getInt("active")==0))
			{
				jsonObject.add("status", ERROR.FORCE_LOGOUT_USER);
				jsonObject.add("message", "Your session has expired. Please log in again");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			
			String hql = "Select tp from Client tp WHERE tp.clientCode = '" + clientCode + "'";
			Client client= (Client)this.swpService.getUniqueRecordByHQL(hql);
			if(client!=null)
			{
				
				jsonObject.add("message", "Client Detail");
				jsonObject.add("status", ERROR.GENERAL_SUCCESS);
				jsonObject.add("client", new Gson().toJson(client));
			}
			else
			{
				jsonObject.add("message", "Client not found");
				jsonObject.add("status", ERROR.CLIENT_EXIST_FAIL);
			}
			JsonObject jsonObj = jsonObject.build();
			//log.info(requestId + " -- " + jsonObj.toString());
            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			
		}catch(Exception e)
		{
			log.warn(e); log.error("exception...", e);
			e.printStackTrace();
			JsonObject jsonObj = jsonObject.build();
            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
		}
	}
	
	
	
	
	public Response getClientSystemDetails(String clientCode, String requestId) {
		// TODO Auto-generated method stub
		JsonObjectBuilder jsonObject = Json.createObjectBuilder();
    	
    	
		try{
			
			if(clientCode==null)
			{
				jsonObject.add("status", ERROR.INCOMPLETE_PARAMETERS);
				jsonObject.add("message", "Incomplete Parameters provided in request.");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			
			swpService = serviceLocator.getSwpService();
			Application app = Application.getInstance(swpService);
			
			String hql = "Select tp from Client tp WHERE tp.clientCode = '" + clientCode + "'";
			//log.info("client..." + clientCode);
			System.out.println("client..." + clientCode);
			Client client= (Client)this.swpService.getUniqueRecordByHQL(hql);
			Double costPerKg = app.getLuggagePerKg();
			if(client!=null)
			{
				
				hql = "Select tp from Station tp WHERE tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL ORDER by tp.stationName";
				Collection<Station> stations= (Collection<Station>)this.swpService.getAllRecordsByHQL(hql);
				
				hql = "Select tp from VehicleSeatClass tp where tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL";
				Collection<VehicleSeatClass> vehicleSeatClasses= (Collection<VehicleSeatClass>)this.swpService.getAllRecordsByHQL(hql);
				
				hql = "Select tp from VehicleSeatSection tp where tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL";
				Collection<VehicleSeatSection> vehicleSeatSections= (Collection<VehicleSeatSection>)this.swpService.getAllRecordsByHQL(hql);
				
				hql = "Select tp from TripZone tp where tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL";
				Collection<TripZone> tripZones= (Collection<TripZone>)this.swpService.getAllRecordsByHQL(hql);
				
				hql = "Select tp from District tp where tp.deletedAt IS NULL ORDER BY tp.name";
				Collection<District> districtList= (Collection<District>)this.swpService.getAllRecordsByHQL(hql);
				
				hql = "Select tp from ProductCategory tp where tp.deletedAt IS NULL ORDER BY tp.productCategoryName";
				Collection<ProductCategory> productCategoryList = (Collection<ProductCategory>)this.swpService.getAllRecordsByHQL(hql);
				
				hql = "Select tp from CourierService tp where tp.deletedAt IS NULL ORDER BY tp.courierServiceName";
				Collection<CourierService> courierServiceList = (Collection<CourierService>)this.swpService.getAllRecordsByHQL(hql);
				
				hql = "Select tp from Vehicle tp where tp.deletedAt IS NULL ORDER BY tp.vehicleName";
				Collection<Vehicle> vehicleList= (Collection<Vehicle>)this.swpService.getAllRecordsByHQL(hql);
				
				ProductPriceCoefficient[] productPriceCoefficientList = ProductPriceCoefficient.values();
				List<String> list = new ArrayList<String>();
				for(int i=0; i<productPriceCoefficientList.length; i++)
				{
					list.add(productPriceCoefficientList[i].name());
				}
				
				jsonObject.add("message", "Client System Details");
				jsonObject.add("status", ERROR.GENERAL_SUCCESS);
				jsonObject.add("costPerKg", costPerKg);
				jsonObject.add("bookingFee", client.getBookingFee()==null ? 0 : client.getBookingFee());
				jsonObject.add("stations", new Gson().toJson(stations));
				jsonObject.add("vehicleSeatClasses", new Gson().toJson(vehicleSeatClasses));
				jsonObject.add("vehicleSeatSections", new Gson().toJson(vehicleSeatSections));
				jsonObject.add("districtList", new Gson().toJson(districtList));
				jsonObject.add("tripZoneList", new Gson().toJson(tripZones));
				jsonObject.add("priceTypes", new Gson().toJson(Arrays.asList(PriceType.values())));
				jsonObject.add("returnTripAvailable", client.getReturnTripsAvailable());
				jsonObject.add("vehicleList", new Gson().toJson(vehicleList));
				jsonObject.add("productCoefficientList", new Gson().toJson(list));
				jsonObject.add("productCategoryList", new Gson().toJson(productCategoryList));
				jsonObject.add("courierServiceList", new Gson().toJson(courierServiceList));
				jsonObject.add("serviceType", client.getVehicleType().name());
			}
			else
			{
				jsonObject.add("message", "Client not found");
				jsonObject.add("status", ERROR.CLIENT_EXIST_FAIL);
			}
			JsonObject jsonObj = jsonObject.build();
			////log.info(requestId + " -- " + jsonObj.toString());
            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			
		}catch(Exception e)
		{
			log.warn(e); log.error("exception...", e);
			e.printStackTrace();
			JsonObject jsonObj = jsonObject.build();
            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
		}
	}
	
	
	
	public Response getSystemSettings(String clientCode, String requestId) {
		// TODO Auto-generated method stub
		JsonObjectBuilder jsonObject = Json.createObjectBuilder();
    	
    	
		try{
			
			if(clientCode==null)
			{
				jsonObject.add("status", ERROR.INCOMPLETE_PARAMETERS);
				jsonObject.add("message", "Incomplete Parameters provided in request.");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			
			swpService = serviceLocator.getSwpService();
			Application app = Application.getInstance(swpService);
			
			String hql = "Select tp from Client tp WHERE tp.clientCode = '" + clientCode + "'";
			Client client= (Client)this.swpService.getUniqueRecordByHQL(hql);

			if(client!=null)
			{
				
				hql = "Select tp from Setting tp WHERE tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL ORDER by tp.settingName";
				Collection<Setting> settings= (Collection<Setting>)this.swpService.getAllRecordsByHQL(hql);
				
				jsonObject.add("message", "Client System Settings");
				jsonObject.add("status", ERROR.GENERAL_SUCCESS);
				jsonObject.add("settingsList", new Gson().toJson(settings));
			}
			else
			{
				jsonObject.add("message", "Client not found");
				jsonObject.add("status", ERROR.CLIENT_EXIST_FAIL);
			}
			JsonObject jsonObj = jsonObject.build();
			//log.info(requestId + " -- " + jsonObj.toString());
            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			
		}catch(Exception e)
		{
			log.warn(e); log.error("exception...", e);
			e.printStackTrace();
			JsonObject jsonObj = jsonObject.build();
            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
		}
	}
	
	
	
	public Response updateSystemSettings(String token, String systemDetails, String clientCode, String requestId, String ipAddress) {
		// TODO Auto-generated method stub
		JsonObjectBuilder jsonObject = Json.createObjectBuilder();
    	
    	
		try{
			JSONObject jsObject = new JSONObject(systemDetails);
			if(clientCode==null)
			{
				jsonObject.add("status", ERROR.INCOMPLETE_PARAMETERS);
				jsonObject.add("message", "Incomplete Parameters provided in request.");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			
			swpService = serviceLocator.getSwpService();
			Application app = Application.getInstance(swpService);
			JSONObject verifyJ = UtilityHelper.verifyToken(token, app);
			if(verifyJ.length()==0 || (verifyJ.length()>0 && verifyJ.has("active") && verifyJ.getInt("active")==0))
			{
				//log.info(requestId + "Test 4");
				jsonObject.add("status", ERROR.FORCE_LOGOUT_USER);
				jsonObject.add("message", "Your session has expired. Please log in again");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			//log.info(requestId + "verifyJ ==" + verifyJ.toString());
			String username = verifyJ.has("username") ? verifyJ.getString("username") : null;
			//log.info(requestId + "username ==" + (username==null ? "" : username));
			String roleCode_ = verifyJ.has("roleCode") ? verifyJ.getString("roleCode") : null;
			//log.info(requestId + "roleCode_ ==" + (roleCode_==null ? "" : roleCode_));
			User user = null;
			RoleType roleCode = null;
			
			if(roleCode_ != null)
			{
				roleCode = RoleType.valueOf(roleCode_);
			}
			
			String hql = "Select tp from Client tp WHERE tp.clientCode = '" + clientCode + "'";
			Client client= (Client)this.swpService.getUniqueRecordByHQL(hql);

			if(client!=null)
			{
				Iterator<String> keys = jsObject.keys();
				while(keys.hasNext())
				{
					String key = keys.next();
					String settingValue = jsObject.getString(key);
					hql = "Select tp from Setting tp WHERE tp.client.clientCode = '"+clientCode+"' AND tp.settingName = '" + key + "' AND tp.deletedAt IS NULL";
					Setting setting= (Setting)this.swpService.getUniqueRecordByHQL(hql);
					
					if(setting!=null)
					{
						setting.setSettingValue(settingValue);
						setting.setUpdatedAt(new Date());
						swpService.updateRecord(setting);
						AuditTrail ad = UtilityHelper.createAuditTrailEntry(ipAddress, RequestType.UPDATE_SETTING, requestId, this.swpService, 
								verifyJ.has("username") ? verifyJ.getString("username") : null, setting.getSettingId(), Setting.class.getName(), 
								"Update Setting. Client Name: " + client.getClientName() + " | Setting Name: "+ setting.getSettingName() +" | Setting Value: " + setting.getSettingValue(), clientCode);
					}
				}
				
				
				jsonObject.add("message", "Client System Settings Updated Successfully");
				jsonObject.add("status", ERROR.GENERAL_SUCCESS);
			}
			else
			{
				jsonObject.add("message", "Client not found");
				jsonObject.add("status", ERROR.CLIENT_EXIST_FAIL);
			}
			JsonObject jsonObj = jsonObject.build();
			//log.info(requestId + " -- " + jsonObj.toString());
            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			
		}catch(Exception e)
		{
			log.warn(e); log.error("exception...", e);
			e.printStackTrace();
			JsonObject jsonObj = jsonObject.build();
            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
		}
	}
	
	
	
	
	
	public Response getClientCount(Boolean clientStatus, String token, String requestId) {
		// TODO Auto-generated method stub
		JsonObjectBuilder jsonObject = Json.createObjectBuilder();
    	
    	
		try{
			
			swpService = serviceLocator.getSwpService();
			Application app = Application.getInstance(swpService);
			
			if(token==null)
			{
				jsonObject.add("status", ERROR.INCOMPLETE_PARAMETERS);
				jsonObject.add("message", "Incomplete Parameters provided in request.");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			JSONObject verifyJ = UtilityHelper.verifyToken(token, app);
			if(verifyJ.length()==0 || (verifyJ.length()>0 && verifyJ.has("active") && verifyJ.getInt("active")==0))
			{
				jsonObject.add("status", ERROR.FORCE_LOGOUT_USER);
				jsonObject.add("message", "Your session has expired. Please log in again");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			
			
			String hql = "Select count(tp.id) from Client tp";
			String sql = "";
			if(clientStatus!=null)
				sql = sql + (sql!="" ? "AND " :"WHERE ") + "tp.status = " + (clientStatus.equals(Boolean.TRUE) ? 1 : 0) + " ";
			sql = sql + " AND tp.deletedAt is NULL";
			
			hql = hql + sql;
			//log.info(requestId + "4.hql ==" + hql);
			List<Long> totalMerchantCount = (List<Long>)swpService.getAllRecordsByHQL(hql);
			Long totalMerchants = totalMerchantCount!=null ? totalMerchantCount.iterator().next() : 0;
			//log.info(requestId + "totalMerchantCount ==" + totalMerchants);
			
			if(totalMerchantCount==null)
			{
				jsonObject.add("status", ERROR.NO_CLIENTS_EXIST);
				jsonObject.add("message", "No Clients Matching Parameters Provided");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			
			jsonObject.add("message", "Client Count");
			jsonObject.add("merchantCount", totalMerchants);
			jsonObject.add("status", ERROR.GENERAL_SUCCESS);
			JsonObject jsonObj = jsonObject.build();
			//log.info(requestId + " -- " + jsonObj.toString());
            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			
		}catch(Exception e)
		{
			log.warn(e); log.error("exception...", e);
			e.printStackTrace();
			JsonObject jsonObj = jsonObject.build();
            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
		}
	}

	public Response createNewClientSubscription(
			String clientCode,
			String orderRef, 
			String channel, 
			String transactionCurrency,
			String paymentMeans, 
			String token, 
			String requestId,
			String ipAddress) {
		// TODO Auto-generated method stub
		JsonObjectBuilder jsonObject = Json.createObjectBuilder();
    	
    	
		try{
			//log.info(requestId + "Test 1");
			if(clientCode==null || orderRef==null || token==null)
			{
				//log.info(requestId + "Test 2");
				jsonObject.add("status", ERROR.INCOMPLETE_PARAMETERS);
				jsonObject.add("message", "Incomplete Parameters provided in request.");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			//log.info(requestId + "Test 3");
			swpService = serviceLocator.getSwpService();
			Application app = Application.getInstance(swpService);
			JSONObject verifyJ = UtilityHelper.verifyToken(token, app);
			if(verifyJ.length()==0 || (verifyJ.length()>0 && verifyJ.has("active") && verifyJ.getInt("active")==0))
			{
				//log.info(requestId + "Test 4");
				jsonObject.add("status", ERROR.FORCE_LOGOUT_USER);
				jsonObject.add("message", "Your session has expired. Please log in again");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			//log.info(requestId + "verifyJ ==" + verifyJ.toString());
			String username = verifyJ.has("username") ? verifyJ.getString("username") : null;
			//log.info(requestId + "username ==" + (username==null ? "" : username));
			String roleCode_ = verifyJ.has("roleCode") ? verifyJ.getString("roleCode") : null;
			//log.info(requestId + "roleCode_ ==" + (roleCode_==null ? "" : roleCode_));
			User user = null;
			RoleType roleCode = null;
			
			if(roleCode_ != null)
			{
				roleCode = RoleType.valueOf(roleCode_);
			}
			
			if(username!=null)
			{
				String hql = "select tp from User tp where tp.username = '" + username + "' AND " +
						"tp.userStatus = " + UserStatus.ACTIVE.ordinal() + " AND tp.deletedAt IS NULL";
				//log.info(requestId + "hql ==" + hql);
				user = (User)this.swpService.getUniqueRecordByHQL(hql);
				
				
				if(user==null)
				{

					//log.info(requestId + "user IS NULL");
					//log.info(requestId + "user firstname = " + user.getFirstName());
					//log.info(requestId + "user lastname = " + user.getLastName());
					jsonObject.add("status", ERROR.INVALID_CLIENT_CREATION_PRIVILEDGES);
					jsonObject.add("message", "Invalid Client Creation Priviledges");
					JsonObject jsonObj = jsonObject.build();
		            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
				}
				//roleCode = user.getRoleCode();
			}
			
			if(roleCode==null || (roleCode!=null && !roleCode.equals(RoleType.ADMIN_STAFF.name())))
			{
				jsonObject.add("status", ERROR.INVALID_CLIENT_SUBSCRIPTION_CREATION_PRIVILEDGES);
				jsonObject.add("message", "Invalid Client Creation Priviledges");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
				
			String hql = "Select tp from Client tp WHERE tp.clientCode = '" + clientCode + "'";
			Client client= (Client)this.swpService.getUniqueRecordByHQL(hql);
			if(client!=null)
			{
				
				jsonObject.add("message", "Client Detail");
				jsonObject.add("status", ERROR.GENERAL_SUCCESS);
				jsonObject.add("client", new Gson().toJson(client));
			}
			else
			{
				jsonObject.add("message", "Client not found");
				jsonObject.add("status", ERROR.CLIENT_EXIST_FAIL);
			}
			
			Calendar cal = Calendar.getInstance();
			Date startDate = cal.getTime();
			cal.add(Calendar.YEAR, 1);
			Date endDate = cal.getTime();
			
			Transaction subscriptionTransaction = new Transaction();
			subscriptionTransaction.setChannel(Channel.valueOf(channel));
			subscriptionTransaction.setOrderRef(orderRef);
			subscriptionTransaction.setPaymentMeans(PaymentMeans.valueOf(paymentMeans));
			subscriptionTransaction.setResponseCode(0);
			subscriptionTransaction.setServiceType(ServiceType.CASH_PAYMENT);
			subscriptionTransaction.setStatus(TransactionStatus.SUCCESS);
			subscriptionTransaction.setTransactingUser(user);
			subscriptionTransaction.setTransactingUserName(user.getUsername());
			subscriptionTransaction.setTransactionAmount(0.00);
			subscriptionTransaction.setTransactionCurrency("ZMW");
			subscriptionTransaction.setTransactionDate(new Date());
			subscriptionTransaction.setTransactionFee(0.00);
			subscriptionTransaction.setFixedCharge(0.00);
			subscriptionTransaction.setUpdatedAt(new Date());
			subscriptionTransaction.setCreatedAt(new Date());
			subscriptionTransaction.setUserId(user.getUserId());
			subscriptionTransaction = (Transaction)this.swpService.createNewRecord(subscriptionTransaction);
			
			ClientSubscription clientSubscription = new ClientSubscription();
			clientSubscription.setClient(client);
			clientSubscription.setCreatedAt(new Date());
			clientSubscription.setUpdatedAt(new Date());
			clientSubscription.setEndDate(endDate);
			clientSubscription.setStartDate(startDate);
			clientSubscription.setSubscriptionTransaction(subscriptionTransaction);
			this.swpService.createNewRecord(clientSubscription);

			JSONObject js = new JSONObject();
			JSONObject txnObjects = new JSONObject();
			app.getInstance(this.swpService, Boolean.TRUE);

			AuditTrail ad = UtilityHelper.createAuditTrailEntry(ipAddress, RequestType.NEW_CLIENT_SUBSCRIPTION, requestId, this.swpService, 
					verifyJ.has("username") ? verifyJ.getString("username") : null, clientSubscription.getClientSubscriptionId(), ClientSubscription.class.getName(), 
					"New subscription for client. Client name: " + client.getClientName() + " | Client Transaction Order #" + orderRef, clientCode);

			jsonObject.add("message", "Client generated successfully");
			jsonObject.add("status", ERROR.GENERAL_SUCCESS);
			jsonObject.add("clientDetails", new Gson().toJson(client));
			JsonObject jsonObj = jsonObject.build();
			//log.info(requestId + " -- " + jsonObj.toString());
            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			
		}catch(Exception e)
		{
			log.warn(e); log.error("exception...", e);
			e.printStackTrace();
			JsonObject jsonObj = jsonObject.build();
            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
		}
	}

	public Response createNewVendor(String vendorCompanyName,
			String contactPerson, String contactMobile, String contactEmail,
			String clientCode, String token, String requestId, String ipAddress) {
		// TODO Auto-generated method stub
		JsonObjectBuilder jsonObject = Json.createObjectBuilder();
    	
    	
		try{
			//log.info(requestId + "Test 1");
			if(vendorCompanyName==null || contactPerson==null || contactMobile==null || contactEmail==null || clientCode==null || token==null)
			{
				//log.info(requestId + "Test 2");
				jsonObject.add("status", ERROR.INCOMPLETE_PARAMETERS);
				jsonObject.add("message", "Incomplete Parameters provided in request.");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			//log.info(requestId + "Test 3");
			swpService = serviceLocator.getSwpService();
			Application app = Application.getInstance(swpService);
			JSONObject verifyJ = UtilityHelper.verifyToken(token, app);
			if(verifyJ.length()==0 || (verifyJ.length()>0 && verifyJ.has("active") && verifyJ.getInt("active")==0))
			{
				//log.info(requestId + "Test 4");
				jsonObject.add("status", ERROR.FORCE_LOGOUT_USER);
				jsonObject.add("message", "Your session has expired. Please log in again");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			//log.info(requestId + "verifyJ ==" + verifyJ.toString());
			String username = verifyJ.has("username") ? verifyJ.getString("username") : null;
			//log.info(requestId + "username ==" + (username==null ? "" : username));
			String roleCode_ = verifyJ.has("roleCode") ? verifyJ.getString("roleCode") : null;
			//log.info(requestId + "roleCode_ ==" + (roleCode_==null ? "" : roleCode_));
			User user = null;
			RoleType roleCode = null;
			
			if(roleCode_ != null)
			{
				roleCode = RoleType.valueOf(roleCode_);
			}
			
			if(username!=null)
			{
				String hql = "select tp from User tp where tp.username = '" + username + "' AND " +
						"tp.userStatus = " + UserStatus.ACTIVE.ordinal() + " AND tp.deletedAt IS NULL";
				//log.info(requestId + "hql ==" + hql);
				user = (User)this.swpService.getUniqueRecordByHQL(hql);
				
				
				if(user==null)
				{

					//log.info(requestId + "user IS NULL");
					//log.info(requestId + "user firstname = " + user.getFirstName());
					//log.info(requestId + "user lastname = " + user.getLastName());
					jsonObject.add("status", ERROR.INVALID_CLIENT_CREATION_PRIVILEDGES);
					jsonObject.add("message", "Invalid Vendor Creation Priviledges");
					JsonObject jsonObj = jsonObject.build();
		            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
				}
				//roleCode = user.getRoleCode();
			}
			
			if(roleCode==null || (roleCode!=null && !roleCode.equals(RoleType.ADMIN_STAFF)))
			{
				jsonObject.add("status", ERROR.INVALID_CLIENT_CREATION_PRIVILEDGES);
				jsonObject.add("message", "Invalid Vendor Creation Priviledges");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			String hql = "Select tp from Client tp where tp.clientCode = '" + clientCode + "' AND tp.deletedAt IS NULL";
			Client client = (Client)this.swpService.getUniqueRecordByHQL(hql);
				
			String vendorCode = RandomStringUtils.randomNumeric(10).toUpperCase();
			Vendor vendor = new Vendor();
			vendor.setCompanyName(vendorCompanyName);
			vendor.setContactEmail(contactEmail);
			vendor.setContactMobile(contactMobile);
			vendor.setContactPerson(contactPerson);
			vendor.setCreatedAt(new Date());
			vendor.setUpdatedAt(new Date());
			vendor.setClient(client);
			vendor.setVendorCode(vendorCode);
			vendor.setVendorStatus(VendorStatus.ACTIVE);			

			vendor = (Vendor)this.swpService.createNewRecord(vendor);

			JSONObject js = new JSONObject();
			JSONObject txnObjects = new JSONObject();
			app.getInstance(this.swpService, Boolean.TRUE);

			AuditTrail ad = UtilityHelper.createAuditTrailEntry(ipAddress, RequestType.NEW_VENDOR_CREATION, requestId, this.swpService, 
					verifyJ.has("username") ? verifyJ.getString("username") : null, vendor.getVendorId(), Vendor.class.getName(), 
					"New vendor created. Vendor: " + vendor.getCompanyName(), clientCode);
			
			Authenticator auth = Authenticator.getInstance();
			String vendorUsername = contactMobile;
			String email = contactEmail;
			String mobileNumber = contactMobile;
			String[] names = contactPerson.split(" ");
			String firstName = names.length>0 ? names[0] : "";
			String lastName = names.length> 1 ? names[1] : "";
			String otherName = names.length> 2 ? names[2] : "";
			String vendorRoleCode = RoleType.VENDOR.name();
			
			Response vendorUserResponse = auth.addNewUser(null, token, vendorUsername, email, mobileNumber, UserStatus.ACTIVE.name(), null, Boolean.FALSE, null, clientCode, 
					firstName, lastName, otherName, vendorRoleCode, null, requestId, ipAddress);
			String vendorUserStr = vendorUserResponse.getEntity().toString();
			//log.info("vendorUserStr -- " + vendorUserStr);
			JSONObject vendorUserJS = new JSONObject(vendorUserStr);
			if(vendorUserJS.has("status") && vendorUserJS.getInt("status")== (ERROR.GENERAL_SUCCESS))
			{
				
				String userDetails = vendorUserJS.getString("user");
				String userAccount = vendorUserJS.getString("userAccount");
				JSONObject user_ = new JSONObject(userAccount);
				hql = "Select tp from User tp where tp.userId = " + user_.getLong("userId") + " AND tp.deletedAt is NULL and tp.client.clientCode = '"+clientCode+"'";
				User user__ = (User)swpService.getUniqueRecordByHQL(hql);
				if(user__!=null)
				{
				}
				else
				{
					swpService.deleteRecord(vendor);
				}
				
				jsonObject.add("message", "Vendor generated successfully");
				jsonObject.add("status", ERROR.GENERAL_SUCCESS);
				jsonObject.add("vendorDetails", new Gson().toJson(vendor));
				jsonObject.add("userDetails", userDetails);
				JsonObject jsonObj = jsonObject.build();
				//log.info(requestId + " -- " + jsonObj.toString());
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}

			swpService.deleteRecord(vendor);
			jsonObject.add("message", "Vendor generation failed");
			jsonObject.add("status", ERROR.GENERAL_FAIL);
			JsonObject jsonObj = jsonObject.build();
			//log.info(requestId + " -- " + jsonObj.toString());
            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			
		}catch(Exception e)
		{
			log.warn(e); log.error("exception...", e);
			e.printStackTrace();
			JsonObject jsonObj = jsonObject.build();
            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
		}
	}
	
	
	
	
	public Response getVendorList(String clientCode, String token, String requestId, String ipAddress) {
		// TODO Auto-generated method stub
		JsonObjectBuilder jsonObject = Json.createObjectBuilder();
    	
    	
		try{
			
			if(clientCode==null)
			{
				jsonObject.add("status", ERROR.INCOMPLETE_PARAMETERS);
				jsonObject.add("message", "Incomplete Parameters provided in request.");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			
			swpService = serviceLocator.getSwpService();
			Application app = Application.getInstance(swpService);
			JSONObject verifyJ = UtilityHelper.verifyToken(token, app);
			if(verifyJ.length()==0 || (verifyJ.length()>0 && verifyJ.has("active") && verifyJ.getInt("active")==0))
			{
				jsonObject.add("status", ERROR.FORCE_LOGOUT_USER);
				jsonObject.add("message", "Your session has expired. Please log in again");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			String username = verifyJ.has("username") ? verifyJ.getString("username") : null;
			//log.info(requestId + "username ==" + (username==null ? "" : username));
			String roleCode_ = verifyJ.has("roleCode") ? verifyJ.getString("roleCode") : null;
			//log.info(requestId + "roleCode_ ==" + (roleCode_==null ? "" : roleCode_));
			User user = null;
			RoleType roleCode = null;
			
			if(roleCode_ != null)
			{
				roleCode = RoleType.valueOf(roleCode_);
			}
			
			if(username!=null)
			{
				user = (User)this.swpService.getUniqueRecordByHQL("select tp from User tp where tp.username = '" + username + "' AND " +
						"tp.userStatus = " + UserStatus.ACTIVE.ordinal() + " AND tp.deletedAt IS NULL");
				//roleCode = user.getRoleCode();
				if(user==null)
				{
					jsonObject.add("status", ERROR.INVALID_CLIENT_UPDATE_PRIVILEDGES);
					jsonObject.add("message", "Your session has expired. Please log in again");
					JsonObject jsonObj = jsonObject.build();
		            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
				}
			}
			
			//log.info("roleCode == " + roleCode.name());
			if(roleCode!=null && !roleCode.equals(RoleType.ADMIN_STAFF))
			{
				jsonObject.add("status", ERROR.INVALID_CLIENT_LISTING_PRIVILEDGES);
				jsonObject.add("message", "Invalid Client Listing Priviledges");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			String hql = "Select distinct tp from Client tp where tp.deletedAt IS NULL AND tp.clientCode = '"+clientCode+"'";
			//DE LIMIT " + startIndex + ", " + limit;
			Client client= (Client)this.swpService.getUniqueRecordByHQL(hql);
			
			if(client==null)
			{
				jsonObject.add("status", ERROR.INVALID_CLIENT_CODE_PROVIDED);
				jsonObject.add("message", "Invalid Client Code Provided");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			hql = "Select tp from Vendor tp where tp.deletedAt IS NULL and tp.client.clientCode = '"+clientCode+"'";
			Collection<Vendor> vendorList = (Collection<Vendor>)swpService.getAllRecordsByHQL(hql);
			JSONArray jsonArray = new JSONArray();
			
			jsonObject.add("message", "Vendor List generated successfully");
			jsonObject.add("status", ERROR.GENERAL_SUCCESS);
			jsonObject.add("vendorList", new Gson().toJson(vendorList));
			JsonObject jsonObj = jsonObject.build();
			//log.info(requestId + " -- " + jsonObj.toString());
            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			
		}catch(Exception e)
		{
			log.warn(e); log.error("exception...", e);
			e.printStackTrace();
			JsonObject jsonObj = jsonObject.build();
            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
		}
	}
	
	
	
	
	public Response getWalletList(String clientCode, String token, String requestId, String ipAddress) {
		// TODO Auto-generated method stub
		JsonObjectBuilder jsonObject = Json.createObjectBuilder();
    	
    	
		try{
			
			if(clientCode==null)
			{
				jsonObject.add("status", ERROR.INCOMPLETE_PARAMETERS);
				jsonObject.add("message", "Incomplete Parameters provided in request.");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			
			swpService = serviceLocator.getSwpService();
			Application app = Application.getInstance(swpService);
			JSONObject verifyJ = UtilityHelper.verifyToken(token, app);
			if(verifyJ.length()==0 || (verifyJ.length()>0 && verifyJ.has("active") && verifyJ.getInt("active")==0))
			{
				jsonObject.add("status", ERROR.FORCE_LOGOUT_USER);
				jsonObject.add("message", "Your session has expired. Please log in again");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			String username = verifyJ.has("username") ? verifyJ.getString("username") : null;
			//log.info(requestId + "username ==" + (username==null ? "" : username));
			String roleCode_ = verifyJ.has("roleCode") ? verifyJ.getString("roleCode") : null;
			//log.info(requestId + "roleCode_ ==" + (roleCode_==null ? "" : roleCode_));
			User user = null;
			RoleType roleCode = null;
			
			if(roleCode_ != null)
			{
				roleCode = RoleType.valueOf(roleCode_);
			}
			
			if(username!=null)
			{
				user = (User)this.swpService.getUniqueRecordByHQL("select tp from User tp where tp.username = '" + username + "' AND " +
						"tp.userStatus = " + UserStatus.ACTIVE.ordinal() + " AND tp.deletedAt IS NULL");
				//roleCode = user.getRoleCode();
				if(user==null)
				{
					jsonObject.add("status", ERROR.INVALID_CLIENT_UPDATE_PRIVILEDGES);
					jsonObject.add("message", "Your session has expired. Please log in again");
					JsonObject jsonObj = jsonObject.build();
		            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
				}
			}
			
			//log.info("roleCode == " + roleCode.name());
			if(roleCode!=null && !roleCode.equals(RoleType.ADMIN_STAFF))
			{
				jsonObject.add("status", ERROR.INVALID_CLIENT_LISTING_PRIVILEDGES);
				jsonObject.add("message", "Invalid Client Listing Priviledges");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			String hql = "Select distinct tp from Client tp where tp.deletedAt IS NULL AND tp.clientCode = '"+clientCode+"'";
			//DE LIMIT " + startIndex + ", " + limit;
			Client client= (Client)this.swpService.getUniqueRecordByHQL(hql);
			
			if(client==null)
			{
				jsonObject.add("status", ERROR.INVALID_CLIENT_CODE_PROVIDED);
				jsonObject.add("message", "Invalid Client Code Provided");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			hql = "Select tp from Wallet tp where tp.deletedAt IS NULL and tp.vendor.user.client.clientCode = '"+clientCode+"'";
			Collection<Wallet> walletList = (Collection<Wallet>)swpService.getAllRecordsByHQL(hql);
			JSONArray jsonArray = new JSONArray();
			
			jsonObject.add("message", "Wallet List generated successfully");
			jsonObject.add("status", ERROR.GENERAL_SUCCESS);
			jsonObject.add("walletList", new Gson().toJson(walletList));
			JsonObject jsonObj = jsonObject.build();
			//log.info(requestId + " -- " + jsonObj.toString());
            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			
		}catch(Exception e)
		{
			log.warn(e); log.error("exception...", e);
			e.printStackTrace();
			JsonObject jsonObj = jsonObject.build();
            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
		}
	}
	
	
	

	public Response fundVendorWallet(String paymentType, String transactionRef,
			String deviceCode, String paymentDetails, String requestId,
			String ipAddress, String clientCode, String token) {
		// TODO Auto-generated method stub
		JsonObjectBuilder jsonObject = Json.createObjectBuilder();
		try{
			if(deviceCode==null || paymentType==null || paymentDetails==null || transactionRef==null || deviceCode==null)
			{
				jsonObject.add("status", ERROR.INCOMPLETE_PARAMETERS);
				jsonObject.add("message", "Incomplete parameters provided. Provide purchaseToken, transactionRef, deviceCode, purchaseDetails, paymentType, generalTripClass parameters");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			//log.info("paymentType = " + paymentType);
            //log.info("deviceCode = " + deviceCode);
            //log.info("clientCode = " + clientCode);
            //log.info("transactionRef = " + transactionRef);
            //log.info("paymentDetails = " + paymentDetails);
			
			String transactionVerification = null;
			String messageRequest = null;
			String messageResponse = null;
			JSONObject transactionVerificationJson = null;
			//String transactionRef = null;
			if(paymentType.equals(PaymentMeans.PROBASEPAY.name()))
			{
				JSONObject paymentDetailsJS = new JSONObject(paymentDetails);
				String probasePayMerchantCode = paymentDetailsJS.has("probasePayMerchantCode") ? paymentDetailsJS.getString("probasePayMerchantCode") : null; 
				String probasePayDeviceCode = paymentDetailsJS.has("probasePayDeviceCode") ? paymentDetailsJS.getString("probasePayDeviceCode") : null;
				//transactionRef = purchaseDetailsJS.has("transactionRef") ? purchaseDetailsJS.getString("transactionRef") : null;
				String hash = paymentDetailsJS.has("hash") ? paymentDetailsJS.getString("hash") : null;
				if(probasePayMerchantCode==null || probasePayDeviceCode==null || hash==null)
				{
					jsonObject.add("status", ERROR.INCOMPLETE_PARAMETERS);
					jsonObject.add("message", "Incomplete parameters provided. Ensure you provide merchantCode, probasePayDeviceCode, transactionRef, and hash parameters in your paymentDetails parameter");
					JsonObject jsonObj = jsonObject.build();
		            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
				}
				JSONObject jsObj = new JSONObject();
				transactionVerification = UtilityHelper.sendGet(UtilityHelper.PROBASEPAY_BASE_URL+"/transaction/check-status/"+probasePayMerchantCode+"/"+probasePayDeviceCode+"/"+transactionRef+"/"+hash, 
						null, jsObj);
				transactionVerificationJson = new JSONObject(transactionVerification);
				messageRequest = hash+"###"+deviceCode+"###"+transactionRef+"###"+token +"###"+ipAddress+"###"+clientCode;
				//log.info("messageRequest --- " + messageRequest);

				swpService = serviceLocator.getSwpService();
				Application app = Application.getInstance(swpService);
				
				Device device = null;
				String hql = "Select tp from Device tp where tp.deviceCode = '"+deviceCode+"' AND tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL";
				device = (Device)swpService.getUniqueRecordByHQL(hql);
				
				if(device==null)
				{
					jsonObject.add("status", ERROR.DEVICE_NOT_FOUND);
					jsonObject.add("message", "Invalid device used to process transaction");
					JsonObject jsonObj = jsonObject.build();
		            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
				}
				
				User user = null;
				RoleType roleCode = null;
				Boolean skip = Boolean.FALSE;
				if(token!=null)
				{
					JSONObject verifyJ = UtilityHelper.verifyToken(token, app);
					if(verifyJ.length()==0 || (verifyJ.length()>0 && verifyJ.has("active") && verifyJ.getInt("active")==0))
					{
						jsonObject.add("status", ERROR.FORCE_LOGOUT_USER);
						jsonObject.add("message", "Your session has expired. Please log in again");
						JsonObject jsonObj = jsonObject.build();
			            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
					}
					
					String username = verifyJ.has("username") ? verifyJ.getString("username") : null;
					//log.info(requestId + "username ==" + (username==null ? "" : username));
					String roleCode_ = verifyJ.has("roleCode") ? verifyJ.getString("roleCode") : null;
					//log.info(requestId + "roleCode_ ==" + (roleCode_==null ? "" : roleCode_));
					
					
					if(roleCode_ != null)
					{
						roleCode = RoleType.valueOf(roleCode_);
					}
					
					if(username!=null)
					{
						user = (User)this.swpService.getUniqueRecordByHQL("select tp from User tp where tp.username = '" + username + "' AND tp.deletedAt IS NULL AND " +
								"tp.userStatus = " + UserStatus.ACTIVE.ordinal());
					}
				}
				
				Vendor vendor = null;
				if(user!=null && roleCode.equals(RoleType.VENDOR))
				{
					hql = "Select tp from Vendor tp where tp.vendorId = "+user.getVendor().getVendorId()+" AND tp.deletedAt IS NULL " +
							"AND tp.client.clientCode = '"+clientCode+"' AND tp.vendorStatus = " + VendorStatus.ACTIVE.ordinal();
					vendor = (Vendor)swpService.getUniqueRecordByHQL(hql);
				}
				
				Client client=null;
				if(clientCode!=null)
				{
					hql = "Select tp from Client tp where tp.clientCode = '"+ clientCode + "' AND tp.deletedAt IS NULL";
					//log.info(hql);
					client = (Client)this.swpService.getUniqueRecordByHQL(hql);
					if(client==null)
					{
						jsonObject.add("status", ERROR.INCOMPLETE_PARAMETERS);
						jsonObject.add("message", "The clientCode is invalid");
						JsonObject jsonObj = jsonObject.build();
			            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
					}
				}
				
				if(vendor==null)
				{
					jsonObject.add("status", ERROR.VENDOR_NOT_FOUND);
					jsonObject.add("message", "Wallet crediting for the selected vendor was not successful. Vendor not found");
					JsonObject jsonObj = jsonObject.build();
		            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
				}
				else
				{
					hql = "Select tp from Wallet tp where tp.vendor.vendorId = " + vendor.getVendorId() + 
							"AND tp.deletedAt is NULL AND tp.vendor.client.clientCode = '"+clientCode+"'";
					//log.info("....HQL = " + hql);
					Wallet wallet = (Wallet)swpService.getUniqueRecordByHQL(hql);
					if(wallet==null)
					{
						String walletCode = RandomStringUtils.randomNumeric(8);
						wallet = new Wallet();
						wallet.setCreatedAt(new Date());
						wallet.setUpdatedAt(new Date());
						wallet.setCurrentBalance(0.00);
						wallet.setLastFundedByUser(null);
						wallet.setVendor(vendor);
						wallet.setWalletCode(walletCode);
						
						wallet = (Wallet)swpService.createNewRecord(wallet);
					}
					
					
					//log.info("transactionVerification == " + transactionVerification);
					String status = (String)transactionVerificationJson.get("status");
					String serviceType = ServiceType.WALLET_CREDIT.name();
					JSONArray tripSummary = new JSONArray();
					if(status.equals("00"))
					{
						
						String ticketTransactionRef = RandomStringUtils.randomAlphabetic(12);
						
						JSONObject vehicleSeatsLockedDownTrip = new JSONObject();
						Double fixedCharge = transactionVerificationJson.has("fixedCharge") ? transactionVerificationJson.getDouble("fixedCharge") : 0.00;
						Double transactionFee = transactionVerificationJson.has("transactionCharge") ? transactionVerificationJson.getDouble("transactionCharge") : 0.00;
						Double transactionAmount = transactionVerificationJson.has("amount") ? transactionVerificationJson.getDouble("amount") : 0.00;
						
						Transaction transaction = new Transaction();
						transaction.setTransactionRef(ticketTransactionRef);
						transaction.setOrderRef(transactionRef);
						transaction.setChannel(com.probase.reservationticketingwebservice.enumerations.Channel.WEB);
						transaction.setTransactionDate(new Date());
						transaction.setServiceType(ServiceType.valueOf(serviceType));
						transaction.setTransactingUser(user);
						transaction.setTransactingUserName((user!=null && user.getFirstName()!=null ? user.getFirstName() + ((user!=null && user.getLastName()!=null ? (" " + user.getLastName()) : "")) : ""));
						transaction.setUserId(user==null ? null : user.getUserId());
						transaction.setStatus(TransactionStatus.SUCCESS);
						transaction.setCard(null);
						transaction.setDevice(device);
						transaction.setMessageRequest(messageRequest);
						transaction.setMessageResponse(messageResponse);
						transaction.setFixedCharge(fixedCharge);
						transaction.setTransactionFee(transactionFee);
						transaction.setTransactionAmount(transactionAmount);
						transaction.setResponseCode(Response.Status.OK.getStatusCode());
						transaction.setNarration("WalletCredit|#" + transactionRef + "|ZMW" + transaction.getTransactionAmount());
						transaction.setCrVendorId(vendor.getVendorId());
						transaction.setVendor(vendor);
						transaction.setUpdatedAt(new Date());
						transaction.setCreatedAt(new Date());
						transaction.setPaymentMeans(PaymentMeans.CARD);
						transaction.setClient(client);						
						transaction.setTransactionCurrency("ZMW");
						transaction.setWallet(wallet);
						transaction = (Transaction)swpService.createNewRecord(transaction);
						
						wallet.setCurrentBalance(wallet.getCurrentBalance() + transactionAmount);
						wallet.setLastFundedByUser(user);
						swpService.updateRecord(wallet);
						
						AuditTrail ad = UtilityHelper.createAuditTrailEntry(ipAddress, RequestType.VENDOR_WALLET_FUND, requestId, this.swpService, user.getUsername(), wallet.getWalletId(), Wallet.class.getName(), 
								"Fund Wallet |  Wallet Number: " + wallet.getWalletCode() + " | By Customer User: " + user.getFirstName() + " " + user.getLastName() + " | By Vendor: " + vendor.getCompanyName(), clientCode);
						
						jsonObject.add("wallet", new Gson().toJson(wallet));
						jsonObject.add("transactionRef", transaction.getOrderRef());
						//jsonObject.add("tripDetails", tripSummary.toString());
						jsonObject.add("status", ERROR.GENERAL_SUCCESS);
						jsonObject.add("message", "Wallet Funded Successfully");
						JsonObject jsonObj = jsonObject.build();
			            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
					
					}
					else
					{
						//Transaction was not successful
						jsonObject.add("message", "Wallet funding was not successful");
						jsonObject.add("status", ERROR.WALLET_FUNDING_FAILED);
						JsonObject jsonObj = jsonObject.build();
			            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
					}
				}
			}
			else
			{
				jsonObject.add("message", "Invalid Payment Type provided");
				jsonObject.add("status", ERROR.INVALID_PAYMENT_TYPE);
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			
			
		}catch(Exception e)
		{
			log.warn(e); log.error("exception...", e);
			e.printStackTrace();
			JsonObject jsonObj = jsonObject.build();
            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
		}
	}

	
	
	
	public Response listCustomers(String token, String orderRef, String requestId, String ipAddress, String clientCode) {
		// TODO Auto-generated method stub
		JsonObjectBuilder jsonObject = Json.createObjectBuilder();
		try
		{
			if(clientCode==null)
			{
				jsonObject.add("status", ERROR.INCOMPLETE_PARAMETERS);
				jsonObject.add("message", "Incomplete parameters provided. Provide clientCode");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			this.swpService = this.serviceLocator.getSwpService();
			application = Application.getInstance(swpService);
			JSONObject verifyJ = UtilityHelper.verifyToken(token, application);
			Map<String, String> bankKeys = application.getPrivateKeys();
			
			
			Application app = Application.getInstance(swpService);
			if(verifyJ.length()==0 || (verifyJ.length()>0 && verifyJ.has("active") && verifyJ.getInt("active")==0))
			{
				JsonObjectBuilder jsonObjectResponse = Json.createObjectBuilder();
				jsonObjectResponse.add("status", ERROR.FORCE_LOGOUT_USER);
				jsonObjectResponse.add("message", "Your session has expired. Please log in again");
				JsonObject jsonObj = jsonObjectResponse.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			//log.info("3");
			
			String username = verifyJ.has("username") ? verifyJ.getString("username") : null;
			//log.info(requestId + "username ==" + (username==null ? "" : username));
			String roleCode_ = verifyJ.has("roleCode") ? verifyJ.getString("roleCode") : null;
			//log.info(requestId + "roleCode_ ==" + (roleCode_==null ? "" : roleCode_));
			User user = null;
			RoleType roleCode = null;
			
			if(roleCode_ != null)
			{
				roleCode = RoleType.valueOf(roleCode_);
			}
			
			if(username!=null)
			{
				user = (User)this.swpService.getUniqueRecordByHQL("select tp from User tp where tp.username = '" + username + "' AND tp.deletedAt IS NULL AND " +
						"tp.userStatus = " + UserStatus.ACTIVE.ordinal());
				//roleCode = roleCode.name();
			}
			
			
			
			if(!roleCode.equals(RoleType.ADMIN_STAFF.name()) && !roleCode.equals(RoleType.OPERATOR.name()))
			{
				//log.info("5");
				jsonObject.add("status", ERROR.INSUFFICIENT_PRIVILEDGES);
				jsonObject.add("message", "You do not have the priviledges to carry out this action");
				JsonObject jsonObj = jsonObject.build();
				return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			
			String hql = "Select tp from Customer tp WHERE tp.deletedAt IS NULL AND tp.client.clientCode = '"+clientCode+"'";
			if(orderRef!=null)
				hql = hql + " AND tp.purchasedTrip.transaction.orderRef = '"+ orderRef +"'";
			Collection<Customer> customerList = (Collection<Customer>)this.swpService.getAllRecordsByHQL(hql);
			if(customerList!=null && customerList.size()>0)
			{
				jsonObject.add("customerList", new Gson().toJson(customerList));
				jsonObject.add("status", ERROR.GENERAL_SUCCESS);
				jsonObject.add("message", "Customer List");
				
				JsonObject jsonObj = jsonObject.build();
				return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			else
			{
				jsonObject.add("status", ERROR.EMPTY_USER_LIST);
				jsonObject.add("message", "No users currently available");
				JsonObject jsonObj = jsonObject.build();
				return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			log.warn(e); log.error("exception...", e);
            JsonObject jsonObj = jsonObject.build();
			return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
		}
	}

	
}