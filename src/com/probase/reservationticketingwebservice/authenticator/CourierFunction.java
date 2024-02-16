package com.probase.reservationticketingwebservice.authenticator;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.security.auth.login.LoginException;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;

import com.google.gson.Gson;
import com.probase.reservationticketingwebservice.enumerations.CardStatus;
import com.probase.reservationticketingwebservice.enumerations.CardType;
import com.probase.reservationticketingwebservice.enumerations.CourierAvailabilityStatus;
import com.probase.reservationticketingwebservice.enumerations.CustomerStatus;
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
import com.probase.reservationticketingwebservice.enumerations.ShipmentStatus;
import com.probase.reservationticketingwebservice.enumerations.TransactionCurrency;
import com.probase.reservationticketingwebservice.enumerations.TransactionStatus;
import com.probase.reservationticketingwebservice.enumerations.TripCardChargeMode;
import com.probase.reservationticketingwebservice.enumerations.UserStatus;
import com.probase.reservationticketingwebservice.enumerations.VehicleSeatLocation;
import com.probase.reservationticketingwebservice.enumerations.VehicleStatus;
import com.probase.reservationticketingwebservice.enumerations.VehicleTripStatus;
import com.probase.reservationticketingwebservice.enumerations.VehicleType;
import com.probase.reservationticketingwebservice.models.AuditTrail;
import com.probase.reservationticketingwebservice.models.CardScheme;
import com.probase.reservationticketingwebservice.models.Client;
import com.probase.reservationticketingwebservice.models.Country;
import com.probase.reservationticketingwebservice.models.CourierCabinShipment;
import com.probase.reservationticketingwebservice.models.CourierDeliveryPrice;
import com.probase.reservationticketingwebservice.models.CourierService;
import com.probase.reservationticketingwebservice.models.CourierTrackHistory;
import com.probase.reservationticketingwebservice.models.Customer;
import com.probase.reservationticketingwebservice.models.DeliveryDistance;
import com.probase.reservationticketingwebservice.models.Device;
import com.probase.reservationticketingwebservice.models.District;
import com.probase.reservationticketingwebservice.models.Line;
import com.probase.reservationticketingwebservice.models.ProductCategory;
import com.probase.reservationticketingwebservice.models.ProductPriceCoefficientRate;
import com.probase.reservationticketingwebservice.models.PurchasedTrip;
import com.probase.reservationticketingwebservice.models.PurchasedTripSeat;
import com.probase.reservationticketingwebservice.models.ScheduleStation;
import com.probase.reservationticketingwebservice.models.ScheduleStationCode;
import com.probase.reservationticketingwebservice.models.ScheduleStationCourierSection;
import com.probase.reservationticketingwebservice.models.ScheduleStationSeatAvailability;
import com.probase.reservationticketingwebservice.models.Shipment;
import com.probase.reservationticketingwebservice.models.ShipmentItem;
import com.probase.reservationticketingwebservice.models.Station;
import com.probase.reservationticketingwebservice.models.TicketCollectionPoint;
import com.probase.reservationticketingwebservice.models.TripCard;
import com.probase.reservationticketingwebservice.models.Transaction;
import com.probase.reservationticketingwebservice.models.TripZone;
import com.probase.reservationticketingwebservice.models.TripZoneStation;
import com.probase.reservationticketingwebservice.models.User;
import com.probase.reservationticketingwebservice.models.Vehicle;
import com.probase.reservationticketingwebservice.models.VehicleSchedule;
import com.probase.reservationticketingwebservice.models.VehicleScheduleFare;
import com.probase.reservationticketingwebservice.models.VehicleSeat;
import com.probase.reservationticketingwebservice.models.VehicleSeatAvailability;
import com.probase.reservationticketingwebservice.models.VehicleSeatClass;
import com.probase.reservationticketingwebservice.models.VehicleSeatSection;
import com.probase.reservationticketingwebservice.models.VehicleTrip;
import com.probase.reservationticketingwebservice.models.VehicleTripPrice;
import com.probase.reservationticketingwebservice.models.VehicleTripRouting;
import com.probase.reservationticketingwebservice.models.VehicleTripRouteSeatSection;
import com.probase.reservationticketingwebservice.models.Vendor;
import com.probase.reservationticketingwebservice.models.Wallet;
import com.probase.reservationticketingwebservice.util.Application;
import com.probase.reservationticketingwebservice.util.ERROR;
import com.probase.reservationticketingwebservice.util.PrbCustomService;
import com.probase.reservationticketingwebservice.util.ServiceLocator;
import com.probase.reservationticketingwebservice.util.SwpService;
import com.probase.reservationticketingwebservice.util.UtilityHelper;
import com.sun.org.apache.bcel.internal.generic.NEW;

public final class CourierFunction {

    private static CourierFunction authenticator = null;

    // A user storage which stores <username, password>
    private final Map<String, String> usersStorage = new HashMap();

    // A service key storage which stores <service_key, username>
    private final Map<String, String> serviceKeysStorage = new HashMap();

    // An authentication token storage which stores <service_key, auth_token>.
    private final Map<String, String> authorizationTokensStorage = new HashMap();
    
    private static Logger log = Logger.getLogger(CourierFunction.class);
	private ServiceLocator serviceLocator = null;
	public SwpService swpService = null;
	public PrbCustomService swpCustomService = PrbCustomService.getInstance();
	Application application = null;
	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private CourierFunction() {
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

    public static CourierFunction getInstance() {
        if ( authenticator == null ) {
            authenticator = new CourierFunction();
        }

        return authenticator;
    }
    


    public Response createNewCourierService(String token,
			String courierServiceName, String courierServiceCode,
			String details, Integer maxDeliveryPeriod, String requestId,
			String ipAddress, String clientCode) {
		// TODO Auto-generated method stub
		JsonObjectBuilder jsonObject = Json.createObjectBuilder();
    	
		try{
			
			if(courierServiceName==null || maxDeliveryPeriod==null || clientCode==null)
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
			//log.inforequestId + "username ==" + (username==null ? "" : username));
			String roleCode_ = verifyJ.has("roleCode") ? verifyJ.getString("roleCode") : null;
			//log.inforequestId + "roleCode_ ==" + (roleCode_==null ? "" : roleCode_));
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
			}
			
			Client client=null;
			if(clientCode!=null)
			{
				String hql = "Select tp from Client tp where tp.clientCode = '"+ clientCode + "' AND tp.deletedAt IS NULL";
				//log.infohql);
				client = (Client)this.swpService.getUniqueRecordByHQL(hql);
				if(client==null)
				{
					jsonObject.add("status", ERROR.INCOMPLETE_PARAMETERS);
					jsonObject.add("message", "The clientCode is invalid");
					JsonObject jsonObj = jsonObject.build();
		            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
				}
			}
			
			
			
			if(user==null || (roleCode!=null && !roleCode.equals(RoleType.ADMIN_STAFF)))
			{
				jsonObject.add("status", ERROR.INVALID_STATION_CREATION_PRIVILEDGES);
				jsonObject.add("message", "Invalid Creation Priviledges");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
				
			
			JSONArray jsonArray = new JSONArray();
			JSONObject txnObjects = new JSONObject();
			int a = 0;
			String serialNo = "";
			
			CourierService courierService = new CourierService();
			RequestType rt = null;
			String msg = "";
			String msg_1 = "";
			
			
			if(courierServiceCode!=null)
			{
				String hql = "Select tp from CourierService tp where tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL AND lower(tp.courierServiceCode) = '"+courierServiceCode.toLowerCase()+"'";
				//log.info"hql...." + hql);
				courierService = (CourierService)swpService.getUniqueRecordByHQL(hql);
				if(courierService==null)
				{
					jsonObject.add("status", ERROR.TRIP_LINE_DOES_NOT_EXIST);
					jsonObject.add("message", "No courier service exists matching the courier service code provided");
					JsonObject jsonObj = jsonObject.build();
		            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
				}
				
				
				rt = RequestType.UPDATE_COURIER_SERVICE;
				msg = "Courier Service updated successfully";
				courierService.setUpdatedAt(new Date());
				courierService.setCourierServiceName(courierServiceName);
				courierService.setDetails(details);
				courierService.setMaxDeliveryPeriod(maxDeliveryPeriod);
				this.swpService.updateRecord(courierService);
				
				msg_1 = "Update Courier Service. Service Name: " + courierService.getCourierServiceName() + " | By " + user.getFirstName() + " " + user.getLastName();
			}
			else
			{
				String hql = "Select tp from CourierService tp where tp.client.clientCode = '"+clientCode+"' AND " +
						"tp.deletedAt IS NULL AND lower(tp.courierServiceName) = '"+courierServiceName.trim().toLowerCase()+"'";
				courierService = (CourierService)swpService.getUniqueRecordByHQL(hql);
				if(courierService!=null)
				{
					jsonObject.add("status", ERROR.COURIER_SERVICE_NAME_EXISTS);
					jsonObject.add("message", "Courier service name provided already belongs to another courier service");
					JsonObject jsonObj = jsonObject.build();
		            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
				}
				courierService = new CourierService();
				courierService.setCreatedAt(new Date());
				courierService.setCourierServiceCode(RandomStringUtils.randomNumeric(4));
				courierService.setDetails(details);
				courierService.setCourierServiceName(courierServiceName);
				courierService.setMaxDeliveryPeriod(maxDeliveryPeriod);
				courierService.setClient(client);
				courierService.setUpdatedAt(new Date());
				courierService = (CourierService)this.swpService.createNewRecord(courierService);
				rt = RequestType.NEW_COURIER_SERVICE_CREATION;
				msg = "Courier Service created successfully";
				msg_1 = "New Courier Service. Service Name: " + courierService.getCourierServiceName() + " | By " + user.getFirstName() + " " + user.getLastName();
			}
			
			
			
			AuditTrail ad = UtilityHelper.createAuditTrailEntry(ipAddress, rt, requestId, this.swpService, 
					verifyJ.has("username") ? verifyJ.getString("username") : null, courierService.getCourierServiceId(), CourierService.class.getName(), 
					msg_1, clientCode);
			
			
			jsonObject.add("message", msg);
			jsonObject.add("status", ERROR.GENERAL_SUCCESS);
			jsonObject.add("courierService", new Gson().toJson(courierService));
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
	
	public Response listCourierServices(String token, String requestId, String clientCode) {
		// TODO Auto-generated method stub
		JsonObjectBuilder jsonObject = Json.createObjectBuilder();
    	
    	
		try{
			
			
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
			
			
			
			
			String hql = "Select tp from CourierService tp where tp.deletedAt IS NULL AND tp.client.clientCode = '"+clientCode+"'";
			String sql = "";
			hql = hql + sql;
			Collection<CourierService> courierServices= (Collection<CourierService>)this.swpService.getAllRecordsByHQL(hql);
			
			jsonObject.add("message", "Courier services listed successfully");
			jsonObject.add("status", ERROR.GENERAL_SUCCESS);
			jsonObject.add("courierServiceList", new Gson().toJson(courierServices));
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
	


	public Response createNewDeliveryPrice(String token,
			String productCategoryCode, 
			String courierServiceCode, Double minWeight, Double maxWeight,
			Double minDistance, Double maxDistance, /*Double minWidth,
			Double maxWidth, Double minVolume, Double maxVolume, */Double amount,
			String requestId, String ipAddress, String clientCode, Long priceId) {
		// TODO Auto-generated method stub
		JsonObjectBuilder jsonObject = Json.createObjectBuilder();
    	
		try{
			
			if(productCategoryCode==null || courierServiceCode==null || amount==null || clientCode==null)
			{
				jsonObject.add("status", ERROR.INCOMPLETE_PARAMETERS);
				jsonObject.add("message", "Incomplete Parameters provided in request.");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			if((minDistance==null || maxDistance==null))
			{
				jsonObject.add("status", ERROR.INCOMPLETE_PARAMETERS);
				jsonObject.add("message", "To specify height dimensions, you must specify both the minimum and maximum weight and distances");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			/*if((minWidth!=null || maxWidth!=null) && (minWidth==null || maxWidth==null))
			{
				jsonObject.add("status", ERROR.INCOMPLETE_PARAMETERS);
				jsonObject.add("message", "To specify width dimensions, you must specify both the minimum and maximum widths");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			if((minWeight!=null || maxWeight!=null) && (minWeight==null || maxWeight==null))
			{
				jsonObject.add("status", ERROR.INCOMPLETE_PARAMETERS);
				jsonObject.add("message", "To specify weight dimensions, you must specify both the minimum and maximum weights");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			if((minVolume!=null || maxVolume!=null) && (minVolume==null || maxVolume==null))
			{
				jsonObject.add("status", ERROR.INCOMPLETE_PARAMETERS);
				jsonObject.add("message", "To specify volume dimensions, you must specify both the minimum and maximum volumes");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}*/
			
			
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
			//log.inforequestId + "username ==" + (username==null ? "" : username));
			String roleCode_ = verifyJ.has("roleCode") ? verifyJ.getString("roleCode") : null;
			//log.inforequestId + "roleCode_ ==" + (roleCode_==null ? "" : roleCode_));
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
			}
			
			Client client=null;
			if(clientCode!=null)
			{
				String hql = "Select tp from Client tp where tp.clientCode = '"+ clientCode + "' AND tp.deletedAt IS NULL";
				//log.infohql);
				client = (Client)this.swpService.getUniqueRecordByHQL(hql);
				if(client==null)
				{
					jsonObject.add("status", ERROR.INCOMPLETE_PARAMETERS);
					jsonObject.add("message", "The clientCode is invalid");
					JsonObject jsonObj = jsonObject.build();
		            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
				}
			}
			
			
			
			if(user==null || (roleCode!=null && !roleCode.equals(RoleType.ADMIN_STAFF)))
			{
				jsonObject.add("status", ERROR.INVALID_STATION_CREATION_PRIVILEDGES);
				jsonObject.add("message", "Invalid Creation Priviledges");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
				
			
			JSONArray jsonArray = new JSONArray();
			JSONObject txnObjects = new JSONObject();
			int a = 0;
			String serialNo = "";
			
			CourierDeliveryPrice cdp = new CourierDeliveryPrice();
			RequestType rt = null;
			String msg = "";
			
			String hql = "Select tp from ProductCategory tp where tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL AND tp.productCategoryCode = '"+productCategoryCode+"'";
			ProductCategory productCategory = (ProductCategory)swpService.getUniqueRecordByHQL(hql);
			
			
			hql = "Select tp from CourierService tp where tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL AND lower(tp.courierServiceCode) = '"+courierServiceCode.trim().toLowerCase()+"'";
			CourierService courierService = (CourierService)swpService.getUniqueRecordByHQL(hql);
			
			hql = "Select tp from CourierDeliveryPrice tp where tp.client.clientCode = '"+clientCode+"' AND " +
					"tp.deletedAt IS NULL AND lower(tp.courierService.courierServiceCode) = '"+courierServiceCode.trim().toLowerCase()+"' " +
					"AND tp.productCategory.productCategoryCode = '"+productCategoryCode+"'";
			if(minDistance!=null)
				hql = hql + " AND ("+minDistance+" BETWEEN tp.minDistance AND tp.maxDistance)";
			if(maxDistance!=null)
				hql = hql + " AND ("+maxDistance+" BETWEEN tp.minDistance AND tp.maxDistance)";
			if(productCategory!=null && productCategory.getIsWeightApplicable()!=null && productCategory.getIsWeightApplicable().equals(Boolean.TRUE))
			{
				if(minWeight!=null)
					hql = hql + " AND ("+minWeight+" BETWEEN tp.minWeight AND tp.maxWeight)";
				if(maxWeight!=null)
					hql = hql + " AND ("+maxWeight+" BETWEEN tp.minWeight AND tp.maxWeight)";/**/
			}
			else
			{
				if(minWeight!=null)
					hql = hql + " AND ("+minWeight+" BETWEEN tp.minWeight AND tp.maxWeight) AND tp.minWeight IS NULL AND tp.maxWeight IS NULL";
				if(maxWeight!=null)
					hql = hql + " AND ("+maxWeight+" BETWEEN tp.minWeight AND tp.maxWeight) AND tp.minWeight IS NULL AND tp.maxWeight IS NULL";
			}
			if(priceId!=null)
				hql = hql + " AND tp.courierDeliveryPriceId NOT IN ("+priceId+")";
			CourierDeliveryPrice cdpCheck = (CourierDeliveryPrice)swpService.getUniqueRecordByHQL(hql);
			if(cdpCheck!=null)
			{
				jsonObject.add("status", ERROR.COURIER_DELIVERY_PRICE_EXISTS);
				jsonObject.add("message", "A Courier delivery price matching the distance and weight provided already exists. Please provide a different distance span and weight");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			else
			{
				cdp.setCreatedAt(new Date());
				cdp.setUpdatedAt(new Date());
			}
			
			
			
			
			cdp.setAmount(amount);
			cdp.setClient(client);
			cdp.setCourierService(courierService);
			cdp.setProductCategory(productCategory);
			/*cdp.setMaxHeight(maxHeight);
			cdp.setMinHeight(minHeight);
			cdp.setMaxVolume(maxVolume);
			cdp.setMinVolume(minVolume);*/
			cdp.setMaxDistance(maxDistance);
			cdp.setMinDistance(minDistance);
			cdp.setMaxWeight(maxWeight);
			cdp.setMinWeight(minWeight);
			if(priceId==null)
			{
				cdp = (CourierDeliveryPrice)this.swpService.createNewRecord(cdp);
				rt = RequestType.NEW_COURIER_DELIVERY_PRICE_CREATION;
				msg = "Courier Delivery Price created successfully";
				AuditTrail ad = UtilityHelper.createAuditTrailEntry(ipAddress, rt, requestId, this.swpService, 
						verifyJ.has("username") ? verifyJ.getString("username") : null, cdp.getCourierDeliveryPriceId(), CourierDeliveryPrice.class.getName(), 
						"New Courier Delivery Price. Courier Service: " + courierService.getCourierServiceName() + " | Amount: " + amount, clientCode);
			}
			else
			{
				this.swpService.updateRecord(cdp);
				rt = RequestType.UPDATE_COURIER_DELIVERY_PRICE;
				msg = "Courier Delivery Price created successfully";
				AuditTrail ad = UtilityHelper.createAuditTrailEntry(ipAddress, rt, requestId, this.swpService, 
						verifyJ.has("username") ? verifyJ.getString("username") : null, cdp.getCourierDeliveryPriceId(), CourierDeliveryPrice.class.getName(), 
						"Courier Delivery Price Updated. Courier Service: " + courierService.getCourierServiceName() + " | Amount: " + amount, clientCode);
			}
				
			
			
			
			
			
			jsonObject.add("message", msg);
			jsonObject.add("status", ERROR.GENERAL_SUCCESS);
			jsonObject.add("courierDeliveryPrice", new Gson().toJson(cdp));
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
	
	
	
	public Response createNewDeliveryDistance(String token,
			String stationCode, Double distance,
			String requestId, String ipAddress, String clientCode, Long distanceId) {
		// TODO Auto-generated method stub
		JsonObjectBuilder jsonObject = Json.createObjectBuilder();
    	
		try{
			
			if(stationCode==null || distance==null || clientCode==null)
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
			//log.inforequestId + "username ==" + (username==null ? "" : username));
			String roleCode_ = verifyJ.has("roleCode") ? verifyJ.getString("roleCode") : null;
			//log.inforequestId + "roleCode_ ==" + (roleCode_==null ? "" : roleCode_));
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
			}
			
			Client client=null;
			if(clientCode!=null)
			{
				String hql = "Select tp from Client tp where tp.clientCode = '"+ clientCode + "' AND tp.deletedAt IS NULL";
				//log.infohql);
				client = (Client)this.swpService.getUniqueRecordByHQL(hql);
				if(client==null)
				{
					jsonObject.add("status", ERROR.INCOMPLETE_PARAMETERS);
					jsonObject.add("message", "The clientCode is invalid");
					JsonObject jsonObj = jsonObject.build();
		            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
				}
			}
			
			
			
			if(user==null || (roleCode!=null && !roleCode.equals(RoleType.ADMIN_STAFF)))
			{
				jsonObject.add("status", ERROR.INVALID_STATION_CREATION_PRIVILEDGES);
				jsonObject.add("message", "Invalid Creation Priviledges");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
				
			
			JSONArray jsonArray = new JSONArray();
			JSONObject txnObjects = new JSONObject();
			int a = 0;
			String serialNo = "";
			
			DeliveryDistance deliveryDistance = new DeliveryDistance();
			deliveryDistance.setCreatedAt(new Date());
			RequestType rt = null;
			String msg = "";
			
			
			String hql = "";
			if(distanceId!=null)
			{
				hql = "Select tp from DeliveryDistance tp where tp.client.clientCode = '"+clientCode+"' AND " +
					"tp.deletedAt IS NULL AND tp.deliveryDistanceId = "+distanceId;
				deliveryDistance = (DeliveryDistance)swpService.getUniqueRecordByHQL(hql);
				if(deliveryDistance==null)
				{
					jsonObject.add("status", ERROR.GENERAL_FAIL);
					jsonObject.add("message", "Delivery distance not found");
					JsonObject jsonObj = jsonObject.build();
		            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
				}
			}
			
			hql = "Select tp from Station tp where tp.client.clientCode = '"+ clientCode+ "' AND tp.deletedAt IS NULL " +
					"AND tp.stationCode = '"+stationCode+"'";
			//log.infohql);
			Station station = (Station)swpService.getUniqueRecordByHQL(hql);
			if(station==null)
			{
				jsonObject.add("status", ERROR.STATION_NOT_FOUND);
				jsonObject.add("message", "Station not found");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			deliveryDistance.setClient(client);
			deliveryDistance.setUpdatedAt(new Date());
			deliveryDistance.setDistance(distance);
			deliveryDistance.setStation(station);
			if(distanceId==null)
			{
				deliveryDistance = (DeliveryDistance)this.swpService.createNewRecord(deliveryDistance);
				rt = RequestType.NEW_DELIVERY_DISTANCE_CREATION;
				msg = "Courier Delivery Distance created successfully";
				AuditTrail ad = UtilityHelper.createAuditTrailEntry(ipAddress, rt, requestId, this.swpService, 
						verifyJ.has("username") ? verifyJ.getString("username") : null, deliveryDistance.getDeliveryDistanceId(), DeliveryDistance.class.getName(), 
						"New Delivery Distance. Station: " + deliveryDistance.getStation().getStationName() + " | Distance: " + distance, clientCode);
			}
			else
			{
				this.swpService.updateRecord(deliveryDistance);
				rt = RequestType.UPDATE_DELIVERY_DISTANCE;
				msg = "Courier Delivery distance updated successfully";
				AuditTrail ad = UtilityHelper.createAuditTrailEntry(ipAddress, rt, requestId, this.swpService, 
						verifyJ.has("username") ? verifyJ.getString("username") : null, deliveryDistance.getDeliveryDistanceId(), CourierDeliveryPrice.class.getName(), 
						"Updated Delivery Distance. Station: " + deliveryDistance.getStation().getStationName() + " | Distance: " + distance, clientCode);
			}
				
			
			
			
			
			
			jsonObject.add("message", msg);
			jsonObject.add("status", ERROR.GENERAL_SUCCESS);
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
	
	
	
	
	public Response getDetailsForCourierPriceUpload(String token, String requestId, String clientCode) {
		// TODO Auto-generated method stub
		JsonObjectBuilder jsonObject = Json.createObjectBuilder();
    	
    	
		try{
			
			
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
			
			String hql = "Select tp from DeliveryDistance tp where tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL";
			//log.infohql);
			Collection<DeliveryDistance> deliveryDistances = (Collection<DeliveryDistance>)swpService.getAllRecordsByHQL(hql);
			
			hql = "Select tp from ProductCategory tp where tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL";
			//log.infohql);
			Collection<ProductCategory> productCategories = (Collection<ProductCategory>)swpService.getAllRecordsByHQL(hql);
			
			jsonObject.add("message", "Delivery distance and product categories provided");
			jsonObject.add("status", ERROR.GENERAL_SUCCESS);
			jsonObject.add("deliveryDistanceList", new Gson().toJson(deliveryDistances));
			jsonObject.add("productCategoryList", new Gson().toJson(productCategories));
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
	
	public Response getCourierPriceDetailsByCategory(String token, String requestId, String clientCode, String categoryCode, 
			String arrivalStationCode, String departureStationCode, String courierServiceCode, 
			Double weight) {
		// TODO Auto-generated method stub
		JsonObjectBuilder jsonObject = Json.createObjectBuilder();
    	
    	
		try{
			
			
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
			
			String hql = "Select tp from DeliveryDistance tp where tp.station.stationCode = '"+departureStationCode+"' AND tp.client.clientCode = '"+clientCode+"'";
			//log.infohql);
			DeliveryDistance departureDeliverDistance = (DeliveryDistance)swpService.getUniqueRecordByHQL(hql);
			
			hql = "Select tp from DeliveryDistance tp where tp.station.stationCode = '"+arrivalStationCode+"' AND tp.client.clientCode = '"+clientCode+"'";
			//log.infohql);
			DeliveryDistance arrivalDeliverDistance = (DeliveryDistance)swpService.getUniqueRecordByHQL(hql);
			
			if(departureDeliverDistance==null || arrivalDeliverDistance==null)
			{
				//log.info"dep & arr are null");
				jsonObject.add("status", ERROR.TRIP_ZONE_NOT_FOUND);
				jsonObject.add("message", "We could not find a delivery price for this trip");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			Double deliveryDistance = Math.abs(departureDeliverDistance.getDistance() - arrivalDeliverDistance.getDistance());
			//log.info"deliveryDistance ..." + deliveryDistance);
			hql = "Select tp from ProductCategory tp where tp.productCategoryCode = " +
					"'"+categoryCode+"' AND tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt is NULL";
			//log.info"hql ..." + hql);
			ProductCategory productCategory = (ProductCategory)swpService.getUniqueRecordByHQL(hql);
			if(productCategory==null)
			{
				//log.info"prodCate is null ..." );
				jsonObject.add("status", ERROR.PRODUCT_CATEGORY_CODE);
				jsonObject.add("message", "Parent product category not found matching the product category code provided.");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			hql = "Select tp from CourierDeliveryPrice tp where tp.deletedAt IS NULL AND tp.client.clientCode = '"+clientCode+"' AND " +
					"tp.productCategory.productCategoryCode = '"+categoryCode+"'";
			hql = hql + " AND ("+deliveryDistance+" BETWEEN tp.minDistance AND tp.maxDistance)";
			if(productCategory.getIsWeightApplicable()!=null && productCategory.getIsWeightApplicable().equals(Boolean.TRUE))
			{
				hql = hql + " AND ("+weight+" BETWEEN tp.minWeight AND tp.maxWeight)";
			}
			hql = hql + " AND tp.courierService.courierServiceCode = '"+courierServiceCode+"'";
			String sql = "";
			hql = hql + sql;
			//log.infohql);
			CourierDeliveryPrice courierDeliveryPrice= (CourierDeliveryPrice)this.swpService.getUniqueRecordByHQL(hql);
			if(courierDeliveryPrice!=null)
			{
				jsonObject.add("message", "Courier delivery price found");
				jsonObject.add("status", ERROR.GENERAL_SUCCESS);
				jsonObject.add("courierDeliveryPrice", new Gson().toJson(courierDeliveryPrice));
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			else {
				jsonObject.add("status", ERROR.TRIP_ZONE_NOT_FOUND);
				jsonObject.add("message", "We could not find a delivery price for this trip");
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
	
	
	public Response getCourierPriceDetailsByCategoryOld(String token, String requestId, String clientCode, String categoryCode, String arrivalStationCode, String courierServiceCode) {
		// TODO Auto-generated method stub
		JsonObjectBuilder jsonObject = Json.createObjectBuilder();
    	
    	
		try{
			
			
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
			
			String hql = "Select tp from TripZoneStation tp where tp.station.stationCode = '"+arrivalStationCode+"' AND tp.client.clientCode = '"+clientCode+"'";
			//log.infohql);
			TripZoneStation tripZoneStation = (TripZoneStation)swpService.getUniqueRecordByHQL(hql);
			
			if(tripZoneStation==null)
			{
				jsonObject.add("status", ERROR.TRIP_ZONE_NOT_FOUND);
				jsonObject.add("message", "Trip Zone not found for the selected destination station");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			hql = "Select tp from CourierDeliveryPrice tp where tp.deletedAt IS NULL AND tp.client.clientCode = '"+clientCode+"' AND " +
					"tp.productCategory.productCategoryCode = '"+categoryCode+"' AND tp.tripZone.tripZoneId = " + tripZoneStation.getTripZone().getTripZoneId() + 
					" AND tp.courierService.courierServiceCode = '"+courierServiceCode+"'";
			String sql = "";
			hql = hql + sql;
			//log.infohql);
			CourierDeliveryPrice courierDeliveryPrice= (CourierDeliveryPrice)this.swpService.getUniqueRecordByHQL(hql);
			
			jsonObject.add("message", "Courier delivery price found");
			jsonObject.add("status", ERROR.GENERAL_SUCCESS);
			jsonObject.add("courierDeliveryPrice", new Gson().toJson(courierDeliveryPrice));
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
	
	
	
	public Response listCourierDeliveryPrices(String token, String requestId, String clientCode) {
		// TODO Auto-generated method stub
		JsonObjectBuilder jsonObject = Json.createObjectBuilder();
    	
    	
		try{
			
			
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
			
			
			
			
			String hql = "Select tp from CourierDeliveryPrice tp where tp.deletedAt IS NULL AND tp.client.clientCode = '"+clientCode+"'";
			String sql = "";
			hql = hql + sql;
			Collection<CourierDeliveryPrice> courierDeliveryPrices= (Collection<CourierDeliveryPrice>)this.swpService.getAllRecordsByHQL(hql);
			
			jsonObject.add("message", "Courier delivery prices listed successfully");
			jsonObject.add("status", ERROR.GENERAL_SUCCESS);
			jsonObject.add("courierDeliveryPriceList", new Gson().toJson(courierDeliveryPrices));
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

	
	
	public Response createNewProductCategory(String token, String productCategoryName, String details,
		    String productCategoryCode, String requestId, String ipAddress, String clientCode, String parentProductCategoryCode, Integer isWeightApplicableForPricing) {
		// TODO Auto-generated method stub
		JsonObjectBuilder jsonObject = Json.createObjectBuilder();
    	
		try{
			
			if(productCategoryName==null || details==null || clientCode==null)
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
			//log.inforequestId + "username ==" + (username==null ? "" : username));
			String roleCode_ = verifyJ.has("roleCode") ? verifyJ.getString("roleCode") : null;
			//log.inforequestId + "roleCode_ ==" + (roleCode_==null ? "" : roleCode_));
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
			}
			
			Client client=null;
			if(clientCode!=null)
			{
				String hql = "Select tp from Client tp where tp.clientCode = '"+ clientCode + "' AND tp.deletedAt IS NULL";
				//log.infohql);
				client = (Client)this.swpService.getUniqueRecordByHQL(hql);
				if(client==null)
				{
					jsonObject.add("status", ERROR.INCOMPLETE_PARAMETERS);
					jsonObject.add("message", "The clientCode is invalid");
					JsonObject jsonObj = jsonObject.build();
		            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
				}
			}
			
			
			
			if(user==null || (roleCode!=null && !roleCode.equals(RoleType.ADMIN_STAFF)))
			{
				jsonObject.add("status", ERROR.INVALID_STATION_CREATION_PRIVILEDGES);
				jsonObject.add("message", "Invalid Creation Priviledges");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			ProductCategory parentProductCategory = null;
			if(parentProductCategoryCode!=null)
			{
				String hql = "Select tp from ProductCategory tp where tp.productCategoryCode = " +
						"'"+parentProductCategoryCode+"' AND tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt is NULL";
				parentProductCategory = (ProductCategory)swpService.getUniqueRecordByHQL(hql);
				if(parentProductCategory==null)
				{
					jsonObject.add("status", ERROR.PRODUCT_CATEGORY_CODE);
					jsonObject.add("message", "Parent product category not found matching the parent product category code provided.");
					JsonObject jsonObj = jsonObject.build();
		            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
				}
			}
			
				
			
			JSONArray jsonArray = new JSONArray();
			JSONObject txnObjects = new JSONObject();
			int a = 0;
			String serialNo = "";
			
			ProductCategory productCategory = new ProductCategory();
			String msg = "";
			String msg_1 = "";
			RequestType rt = RequestType.NEW_PRODUCT_CATEGORY;
			if(productCategoryCode!=null)
			{
				String hql = "Select tp from ProductCategory tp where tp.productCategoryCode = " +
						"'"+productCategoryCode+"' AND tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt is NULL";
				productCategory = (ProductCategory)swpService.getUniqueRecordByHQL(hql);
				if(productCategory==null)
				{
					jsonObject.add("status", ERROR.PRODUCT_CATEGORY_CODE);
					jsonObject.add("message", "No product category found matching the product category code provided.");
					JsonObject jsonObj = jsonObject.build();
		            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
				}
				msg = "Product Category updated successfully";
				rt  = RequestType.PRODUCT_CATEGORY_UPDATE;
				msg_1 = "Product Category Updated. Category name: " + productCategoryName;
			}
			else
			{
				msg = "Product Category generated successfully";
				productCategory.setCreatedAt(new Date());
				productCategory.setProductCategoryCode(RandomStringUtils.randomNumeric(8));
				productCategory.setClient(client);
				msg_1 = "New Product Category. Category name: " + productCategoryName;
			}
			productCategory.setProductCategoryName(productCategoryName);
			productCategory.setDetails(details);
			productCategory.setUpdatedAt(new Date());
			if(parentProductCategory!=null)
				productCategory.setParentProductCategory(parentProductCategory);
			if(isWeightApplicableForPricing!=null && isWeightApplicableForPricing==1)
				productCategory.setIsWeightApplicable(Boolean.TRUE);
			else
				productCategory.setIsWeightApplicable(Boolean.FALSE);
			
			
			if(productCategoryCode!=null)
				this.swpService.updateRecord(productCategory);
			else
				productCategory = (ProductCategory)this.swpService.createNewRecord(productCategory);
			
			
			AuditTrail ad = UtilityHelper.createAuditTrailEntry(ipAddress, rt, requestId, this.swpService, 
					verifyJ.has("username") ? verifyJ.getString("username") : null, productCategory.getProductCategoryId(), ProductCategory.class.getName(), 
					msg_1, clientCode);
			
			
			jsonObject.add("message", msg);
			jsonObject.add("status", ERROR.GENERAL_SUCCESS);
			jsonObject.add("productCategory", new Gson().toJson(productCategory));
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
	
	
	
	public Response listProductCategory(String token, String requestId, String clientCode, Integer parentYes) {
		// TODO Auto-generated method stub
		JsonObjectBuilder jsonObject = Json.createObjectBuilder();
    	
    	
		try{
			
			
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
			
			
			
			
			String hql = "Select tp from ProductCategory tp where tp.deletedAt IS NULL AND tp.client.clientCode = '"+clientCode+"'";
			if(parentYes!=null && parentYes.equals(1))
			{
				hql = hql + " AND tp.parentProductCategory IS NULL";
			}
			hql = hql + " ORDER BY tp.productCategoryName DESC";
			String sql = "";
			hql = hql + sql;
			Collection<ProductCategory> productCategories= (Collection<ProductCategory>)this.swpService.getAllRecordsByHQL(hql);
			
			jsonObject.add("message", "Product categories listed successfully");
			jsonObject.add("status", ERROR.GENERAL_SUCCESS);
			jsonObject.add("productCategoryList", new Gson().toJson(productCategories));
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


	
	public Response createNewProductCategoryCoefficientRate(String token, String coefficientList, String requestId, String ipAddress, String clientCode) {
		// TODO Auto-generated method stub
		JsonObjectBuilder jsonObject = Json.createObjectBuilder();
    	
		try{
			
			if(coefficientList==null || clientCode==null)
			{
				jsonObject.add("status", ERROR.INCOMPLETE_PARAMETERS);
				jsonObject.add("message", "Incomplete Parameters provided in request.");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			//log.info"coefficientList = " + coefficientList);
			JSONObject coefficients = new JSONObject(coefficientList);
			
			
			
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
			//log.inforequestId + "username ==" + (username==null ? "" : username));
			String roleCode_ = verifyJ.has("roleCode") ? verifyJ.getString("roleCode") : null;
			//log.inforequestId + "roleCode_ ==" + (roleCode_==null ? "" : roleCode_));
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
			}
			
			Client client=null;
			if(clientCode!=null)
			{
				String hql = "Select tp from Client tp where tp.clientCode = '"+ clientCode + "' AND tp.deletedAt IS NULL";
				//log.infohql);
				client = (Client)this.swpService.getUniqueRecordByHQL(hql);
				if(client==null)
				{
					jsonObject.add("status", ERROR.INCOMPLETE_PARAMETERS);
					jsonObject.add("message", "The clientCode is invalid");
					JsonObject jsonObj = jsonObject.build();
		            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
				}
			}
			
			
			
			
			if(user==null || (roleCode!=null && !roleCode.equals(RoleType.ADMIN_STAFF)))
			{
				jsonObject.add("status", ERROR.INVALID_STATION_CREATION_PRIVILEDGES);
				jsonObject.add("message", "Invalid Creation Priviledges");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
				
			
			JSONArray jsonArray = new JSONArray();
			JSONObject txnObjects = new JSONObject();
			int a = 0;
			String serialNo = "";
			Iterator<String> coefficientNames = coefficients.keys();
			RequestType rt = null;
			String msg_1 = "";
			while(coefficientNames.hasNext())
			{
				String coefficientName = coefficientNames.next();
				try
				{
					ProductPriceCoefficient ppc = ProductPriceCoefficient.valueOf(coefficientName);
					ProductPriceCoefficientRate ppcr = null;
					String hql = "Select tp from ProductPriceCoefficientRate tp where tp.productPriceCoefficient = " + ppc.ordinal() + " AND " +
							"tp.deletedAt IS NULL AND tp.client.clientCode = '"+clientCode+"'";
					ppcr = (ProductPriceCoefficientRate)swpService.getUniqueRecordByHQL(hql);
					if(ppcr==null)
					{
						ppcr = new ProductPriceCoefficientRate();
						ppcr.setClient(client);
						ppcr.setCreatedAt(new Date());
						ppcr.setUpdatedAt(new Date());
						ppcr.setProductPriceCoefficient(ppc);
						ppcr.setProductPriceCoefficientValue(coefficients.getDouble(coefficientName));
						swpService.createNewRecord(ppcr);
						rt = RequestType.NEW_PRODUCT_PRICE_COEFFICIENT_RATE;
						msg_1 = "New Product Coefficient Rate. Coefficient Name: " + ppc.name() + " | Coefficient value: " + coefficients.getDouble(coefficientName) + " | By " + user.getFirstName() + " " + user.getLastName();
					}
					else
					{
						ppcr.setUpdatedAt(new Date());
						ppcr.setProductPriceCoefficient(ppc);
						ppcr.setProductPriceCoefficientValue(coefficients.getDouble(coefficientName));
						swpService.updateRecord(ppcr);
						rt = RequestType.UPDATE_PRODUCT_PRICE_COEFFICIENT_RATE;
						msg_1 = "Update Product Coefficient Rate. Coefficient Name: " + ppc.name() + " | Coefficient value: " + coefficients.getDouble(coefficientName) + " | By " + user.getFirstName() + " " + user.getLastName();
					}
					AuditTrail ad = UtilityHelper.createAuditTrailEntry(ipAddress, rt, requestId, this.swpService, 
						verifyJ.has("username") ? verifyJ.getString("username") : null, ppcr.getProductPriceCoefficientRateId(), ProductPriceCoefficientRate.class.getName(), 
						msg_1, clientCode);
				}
				catch(Exception e)
				{
					
				}
			}
			
			
			
			
			
			jsonObject.add("message", "Product Price Coefficient Ratings Generated Successfully");
			jsonObject.add("status", ERROR.GENERAL_SUCCESS);
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
	
	
	public Response listProductPriceCoefficientRates(String token, String requestId, String clientCode) {
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
			
			
			
			
			String hql = "Select tp from ProductPriceCoefficientRate tp where tp.deletedAt IS NULL";
			String sql = "";
			hql = hql + sql;
			Collection<ProductPriceCoefficientRate> productPriceCoefficientRates = (Collection<ProductPriceCoefficientRate>)this.swpService.getAllRecordsByHQL(hql);
			ProductPriceCoefficient[] ppc = ProductPriceCoefficient.values();
			List<String> ppcs = new ArrayList<String>();
			for(int i=0; i<ppc.length; i++)
			{
				ppcs.add(ppc[i].name());
			}
			
			jsonObject.add("message", "Coefficient Rates listed successfully");
			jsonObject.add("status", ERROR.GENERAL_SUCCESS);
			jsonObject.add("productPriceCoefficientRateList", new Gson().toJson(productPriceCoefficientRates));
			jsonObject.add("coefficientRates", new Gson().toJson(ppcs));
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
	
	
	
	
	
	
	public Response createNewShipment(String shipmentTrackingId, String receiverAddress, String receiverCity, String receiverDistrictCode, String receiverEmailNumber, 
			String receiverMobileNumber, String receiverName, String senderAddress, String senderCity, String senderDistrictCode, 
			String senderEmailAddress, String senderName, String senderMobileNumber, String senderSignatureImage, 
			String token, String requestId, String ipAddress, String clientCode, String deviceCode) {
		// TODO Auto-generated method stub
		JsonObjectBuilder jsonObject = Json.createObjectBuilder();
    	
		try{
			
			if(token==null || shipmentTrackingId==null || clientCode==null || deviceCode==null)
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
			//log.inforequestId + "username ==" + (username==null ? "" : username));
			String roleCode_ = verifyJ.has("roleCode") ? verifyJ.getString("roleCode") : null;
			//log.inforequestId + "roleCode_ ==" + (roleCode_==null ? "" : roleCode_));
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
			}
			
			Client client=null;
			if(clientCode!=null)
			{
				String hql = "Select tp from Client tp where tp.clientCode = '"+ clientCode + "' AND tp.deletedAt IS NULL";
				//log.infohql);
				client = (Client)this.swpService.getUniqueRecordByHQL(hql);
				if(client==null)
				{
					jsonObject.add("status", ERROR.INCOMPLETE_PARAMETERS);
					jsonObject.add("message", "The clientCode is invalid");
					JsonObject jsonObj = jsonObject.build();
		            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
				}
			}
			
			if(user==null || (roleCode!=null && !roleCode.equals(RoleType.OPERATOR)))
			{
				jsonObject.add("status", ERROR.INVALID_STATION_CREATION_PRIVILEDGES);
				jsonObject.add("message", "Invalid Creation Priviledges");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			Shipment shipment=null;
			String hql = "Select tp from Shipment tp where tp.trackingId = '" + shipmentTrackingId + "' AND tp.client.clientCode = '"+ clientCode + "' AND tp.deletedAt IS NULL";
			//log.infohql);
			shipment = (Shipment)this.swpService.getUniqueRecordByHQL(hql);
			if(shipment==null)
			{
				jsonObject.add("status", ERROR.INVALID_COURIER_SERVICE);
				jsonObject.add("message", "The tracking code is invalid");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			
			hql = "Select tp from CourierCabinShipment tp where tp.shipment.shipmentId = " + shipment.getShipmentId() + " AND tp.deletedAt IS NULL";
			Collection<CourierCabinShipment> csList = (Collection<CourierCabinShipment>) swpService.getAllRecordsByHQL(hql);
			Iterator<CourierCabinShipment> csIt = csList.iterator();
			Date nowDate = Calendar.getInstance().getTime();
			int dontProceedCount = 0;
			while(csIt.hasNext())
			{
				CourierCabinShipment ccs = csIt.next();
				if(ccs.getLockedDownExpiryDate().before(nowDate))
				{
					dontProceedCount++;
				}
			}
			
			if((csList.size()>0 && dontProceedCount>0))
			{
				jsonObject.add("status", ERROR.INVALID_COURIER_SERVICE);
				jsonObject.add("message", "Time alloted to book for shipping this parcel has elasped. Please try again");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			if(csList.size()==0)
			{
				jsonObject.add("status", ERROR.INVALID_COURIER_SERVICE);
				jsonObject.add("message", "No space available for shipping this parcel on the specified date. You can search for other dates");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			String daSql = "Select tp from District tp where tp.districtCode = '"+senderDistrictCode+"' AND " +
					"tp.deletedAt IS NULL";
			District senderDistrict = (District)swpService.getUniqueRecordByHQL(daSql);
			
			daSql = "Select tp from District tp where tp.districtCode = '"+receiverDistrictCode+"' AND " +
					"tp.deletedAt IS NULL";
			District receiverDistrict = (District)swpService.getUniqueRecordByHQL(daSql);

			daSql = "Select tp from Device tp where tp.deviceCode = '"+deviceCode+"' AND " +
					"tp.deletedAt IS NULL";
			Device device = (Device)swpService.getUniqueRecordByHQL(daSql);
			
			String ticketTransactionRef = null;
			boolean continueLoop = true;
			while(continueLoop==true)
			{
				ticketTransactionRef = RandomStringUtils.randomNumeric(8);
				hql = "Select tp from Transaction tp where tp.transactionRef = '"+ticketTransactionRef+"' AND tp.client.clientCode = '"+clientCode+"' AND " +
					"tp.deletedAt IS NULL";
				Transaction transaction = (Transaction)swpService.getUniqueRecordByHQL(hql);
				if(transaction==null)
					continueLoop = false;
			}
			
			//log.info"---Create Transaction---");
			String messageRequest = deviceCode+"###"+ticketTransactionRef+"###"+token+"###"+ipAddress+"###"+clientCode;
			Transaction transaction = new Transaction();
			transaction.setTransactionRef(ticketTransactionRef);
			transaction.setOrderRef(device.getDeviceCode() + "-" + RandomStringUtils.randomNumeric(8));
			transaction.setChannel(com.probase.reservationticketingwebservice.enumerations.Channel.WEB);
			transaction.setTransactionDate(new Date());
			transaction.setServiceType(ServiceType.CASH_COLLECT_FOR_SHIPMENT);
			transaction.setTransactingUser(user);
			transaction.setTransactingUserName((user!=null && user.getFirstName()!=null ? user.getFirstName() + ((user!=null && user.getLastName()!=null ? (" " + user.getLastName()) : "")) : ""));
			transaction.setUserId(user==null ? null : user.getUserId());
			transaction.setStatus(TransactionStatus.SUCCESS);
			transaction.setCard(null);
			transaction.setDevice(device);
			transaction.setMessageRequest(messageRequest);
			transaction.setMessageResponse(null);
			transaction.setFixedCharge(0.00);
			transaction.setTransactionFee(0.00);
			transaction.setTransactionAmount(shipment.getDeliveryCharge());
			transaction.setResponseCode(Response.Status.OK.getStatusCode());
			transaction.setNarration("CASH|#"+ticketTransactionRef + "|ZMW" + transaction.getTransactionAmount());
			transaction.setDrVendorId(null);
			transaction.setVendor(null);
			transaction.setUpdatedAt(new Date());
			transaction.setCreatedAt(new Date());
			transaction.setPaymentMeans(PaymentMeans.CASH);
			transaction.setClient(client);						
			transaction.setTransactionCurrency("ZMW");
			transaction = (Transaction)swpService.createNewRecord(transaction);
			
			//log.info"---Upate shipment---");
			hql = "Select tp.shipmentId from Shipment tp where " +
					//"tp.vehicleSchedule.line.lineId = '" + shipment.getVehicleSchedule().getLine().getLineId() + "' AND " +
					"tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL";
			//log.info"Shipment... = " + hql);
			List<Long> transactionsByLine = (List<Long>)swpService.getAllRecordsByHQL(hql);
			
			shipment.setTransaction(transaction);
			shipment.setReceiverAddress(receiverAddress);
			shipment.setReceiverCity(receiverCity);
			shipment.setReceiverDistrict(receiverDistrict);
			shipment.setReceiverEmailNumber(receiverEmailNumber);
			shipment.setReceiverMobileNumber(receiverMobileNumber);
			shipment.setReceiverName(receiverName);
			shipment.setFinalReceiverName(null);
			shipment.setRegisteredByUser(user);
			shipment.setReceiverSignatureImage(null);
			shipment.setSenderAddress(senderAddress);
			shipment.setSenderCity(senderCity);
			shipment.setSenderDistrict(senderDistrict);
			shipment.setSenderEmailAddress(senderEmailAddress);
			shipment.setSenderMobileNumber(senderMobileNumber);
			shipment.setSenderName(senderName);
			shipment.setSenderSignatureImage(senderSignatureImage);
			shipment.setShipmentStatus(ShipmentStatus.DELIVERY_PENDING);
			shipment.setBookingFee(client.getBookingFee());
			shipment.setReceiptNo(UtilityHelper.padNumbers(9, (transactionsByLine.size()+1)+""));
			swpService.updateRecord(shipment);
			
			//log.info"---Create Audit trail---");
			AuditTrail ad = UtilityHelper.createAuditTrailEntry(ipAddress, RequestType.SHIPMENT_REGISTERED_FOR_DELIVERY, requestId, this.swpService, 
				verifyJ.has("username") ? verifyJ.getString("username") : null, shipment.getShipmentId(), Shipment.class.getName(), 
				"New Shipment Registered. Shipment Receipt: " + shipment.getReceiptNo() + " | Registered By " + user.getFirstName() + " " + user.getLastName(), clientCode);
			
			hql = "Select tp from CourierCabinShipment tp where tp.shipment.shipmentId = " + shipment.getShipmentId() + " AND tp.deletedAt IS NULL " +
					"AND tp.courierAvailabilityStatus = " + CourierAvailabilityStatus.OPEN.ordinal();
			Collection<CourierCabinShipment> courierCabinShipments = (Collection<CourierCabinShipment>)swpService.getAllRecordsByHQL(hql);
			Iterator<CourierCabinShipment> its = courierCabinShipments.iterator();
			while(its.hasNext())
			{
				//log.info"---Update Courier Cabin Shipment---");
				CourierCabinShipment ccs = its.next();
				ccs.setCourierAvailabilityStatus(CourierAvailabilityStatus.PURCHASED);
				ccs.setLockedDownBy(user.getFirstName() + " " + user.getLastName());
				ccs.setUpdatedAt(new Date());
				swpService.updateRecord(ccs);
			}
			
			
			String trackingDetails = "Parcel booked for delivery";
			CourierTrackHistory cth = new CourierTrackHistory();
			cth.setClient(client);
			cth.setCreatedAt(new Date());
			cth.setUpdatedAt(new Date());
			cth.setShipment(shipment);
			cth.setUpdateByUser(user);
			cth.setShipmentStatus(ShipmentStatus.DELIVERY_PENDING);
			cth.setTrackingDetails(trackingDetails);
			swpService.createNewRecord(cth);
			
			

			jsonObject.add("transaction", new Gson().toJson(transaction));
			jsonObject.add("shipmentDetails", new Gson().toJson(shipment));
			jsonObject.add("shipmentBreakdown", new Gson().toJson(courierCabinShipments));
			jsonObject.add("message", "Shipping order created successfully");
			jsonObject.add("status", ERROR.GENERAL_SUCCESS);
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
	
	
	
	public Response getShipmentDetailsByTransactionRef(String deviceCode, String token, String orderRef, String requestId, String ipAddress, String clientCode) {
		// TODO Auto-generated method stub
		JsonObjectBuilder jsonObject = Json.createObjectBuilder();
    	
		try{
			
			if(deviceCode==null || orderRef==null || clientCode==null)
			{
				jsonObject.add("status", ERROR.INCOMPLETE_PARAMETERS);
				jsonObject.add("message", "Incomplete Parameters provided in request.");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			//log.info"transactionRefs = " + orderRef);
			
			
			
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
			
			JSONObject verifyJ = null;
			User user = null;
			if(token!=null)
			{
				verifyJ = UtilityHelper.verifyToken(token, app);
				if(verifyJ.length()==0 || (verifyJ.length()>0 && verifyJ.has("active") && verifyJ.getInt("active")==0))
				{
					jsonObject.add("status", ERROR.FORCE_LOGOUT_USER);
					jsonObject.add("message", "Your session has expired. Please log in again");
					JsonObject jsonObj = jsonObject.build();
		            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
				}
				
				String username = verifyJ.has("username") ? verifyJ.getString("username") : null;
				//log.inforequestId + "username ==" + (username==null ? "" : username));
				String roleCode_ = verifyJ.has("roleCode") ? verifyJ.getString("roleCode") : null;
				//log.inforequestId + "roleCode_ ==" + (roleCode_==null ? "" : roleCode_));
				//User user = null;
				RoleType roleCode = null;
				
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
			
			Client client=null;
			if(clientCode!=null)
			{
				hql = "Select tp from Client tp where tp.clientCode = '"+ clientCode + "' AND tp.deletedAt IS NULL";
				//log.infohql);
				client = (Client)this.swpService.getUniqueRecordByHQL(hql);
				if(client==null)
				{
					jsonObject.add("status", ERROR.INCOMPLETE_PARAMETERS);
					jsonObject.add("message", "The clientCode is invalid");
					JsonObject jsonObj = jsonObject.build();
		            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
				}
			}
			
			
			hql = "Select tp from Transaction tp where tp.orderRef = '"+orderRef+"' AND tp.deletedAt is NULL AND tp.client.clientCode = '"+clientCode+"'";
			Transaction transaction = (Transaction)swpService.getUniqueRecordByHQL(hql);
			if(transaction==null)
			{
				jsonObject.add("status", ERROR.TRANSACTION_NOT_FOUND);
				jsonObject.add("message", "The transaction could not be found");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			//log.infonew Gson().toJson(transaction));
			List<Long> ptIds = new ArrayList<Long>();
			
			hql = "Select tp from Shipment tp where tp.transaction.transactionId = " + transaction.getTransactionId() + " AND tp.deletedAt IS NULL " +
					"AND tp.client.clientCode = '"+clientCode+"'";
			Shipment shipment = (Shipment)swpService.getUniqueRecordByHQL(hql);
			
			

			hql = "Select tp from ShipmentItem tp where tp.shipment.shipmentId = " + shipment.getShipmentId();
			Collection<ShipmentItem> shipmentItems = (Collection<ShipmentItem>) swpService.getAllRecordsByHQL(hql);
			
			
			JSONArray allShipmentCabins = new JSONArray();
			
			hql = "Select tp from CourierCabinShipment tp where tp.shipment.shipmentId = "+shipment.getShipmentId()+" AND " +
					"tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL";
			//log.infohql);
			CourierCabinShipment courierCabinShipments= (CourierCabinShipment)swpService.getUniqueRecordByHQL(hql);
			//Iterator<CourierCabinShipment> ccsIter = courierCabinShipments.iterator();
			Set<String> allOriginStationCodes = new HashSet<String>();
			CourierCabinShipment departureCourierCabinShipment = null;
			//CourierCabinShipment arrivalCourierCabinShipment = null;
			int x= 0;
			//while(ccsIter.hasNext())
			//{
				//CourierCabinShipment ccs = ccsIter.next();
			CourierCabinShipment ccs = courierCabinShipments;
			JSONObject tripRoutes = new JSONObject();
			tripRoutes.put("departureStationCode", ccs.getScheduleStationCourierSection().getOriginScheduleStation().getStation().getStationCode());
			tripRoutes.put("departureStationName", ccs.getScheduleStationCourierSection().getOriginScheduleStation().getStation().getStationName());
			tripRoutes.put("departureTime", ccs.getScheduleStationCourierSection().getOriginScheduleStation().getDepartureTime());
			tripRoutes.put("sectionName", ccs.getScheduleStationCourierSection().getVehicleSeatSection().getSectionName());
			//if(x == 0)
				departureCourierCabinShipment = ccs;
			//if(x == (courierCabinShipments.size()-1))
			//	arrivalCourierCabinShipment = ccs;
			x++;
			//}
			VehicleSchedule vs = shipment.getVehicleSchedule();
			JSONObject entry_ = new JSONObject();
			entry_.put("vehicleName", vs.getVehicle().getVehicleName());
			entry_.put("tripLineName", vs.getLine().getLineName());
			entry_.put("tripLineCode", vs.getLine().getLineCode());
			entry_.put("transactionRef", transaction.getTransactionRef());
			entry_.put("receiptNo", shipment.getReceiptNo());
			entry_.put("departureStationName", departureCourierCabinShipment.getScheduleStationCourierSection().getOriginScheduleStation().getStation().getStationName());
			entry_.put("departureStationCode", departureCourierCabinShipment.getScheduleStationCourierSection().getOriginScheduleStation().getStation().getStationCode());
			entry_.put("departureTime", departureCourierCabinShipment.getScheduleStationCourierSection().getOriginScheduleStation().getDepartureTime());
			entry_.put("totalTripCost", transaction.getTransactionAmount());
			entry_.put("bookingFee", client.getBookingFee());
			
			//allPurchasedTripSeat.put(entry_);
			
			JSONObject js = new JSONObject();
			Date datePurchased = transaction.getCreatedAt();
			Double totalAmountPaid = transaction.getTransactionAmount();
			String currency = transaction.getTransactionCurrency();
			
			
			
			
			
			/*//log.info"allPurchasedTripSeat size = " + allPurchasedTripSeat.length());
			//log.info"allPurchasedTripSeat = " + allPurchasedTripSeat.toString());
			
			if(allPurchasedTripSeat.length()==0)
			{
				jsonObject.add("status", ERROR.AVAILABLE_SEATS_NOT_FOUND);
				jsonObject.add("message", "There are no Available seats found purchased for this trip");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			else
			{*/
				js.put("datePurchased", new SimpleDateFormat("E, dd MMM yyyy HH:mm a").format(datePurchased));
				js.put("datePurchasedUnformatted", datePurchased);
				js.put("ticketDetails", entry_);
				js.put("shipmentItems", new Gson().toJson(shipmentItems));
				js.put("totalAmountPaid", totalAmountPaid);
				
				jsonObject.add("status", ERROR.GENERAL_SUCCESS);
				jsonObject.add("message", "Available seats found purchased for this trip");
				jsonObject.add("orderRef", orderRef);
				jsonObject.add("shipment", new Gson().toJson(shipment));
				jsonObject.add("transaction", new Gson().toJson(transaction));
				//jsonObject.add("vehicleSchedule", new Gson().toJson(shipment.getVehicleSchedule()));
				jsonObject.add("transactionRef", transaction.getTransactionRef());
				jsonObject.add("totalTripCost", transaction.getTransactionAmount());
				jsonObject.add("purchasedTripDetails", js.toString());
				jsonObject.add("dateCreated", new SimpleDateFormat("yyyy-MM-dd HH:mm").format(transaction.getCreatedAt()));
				if(transaction.getTransactingUserName()!=null)
					jsonObject.add("bookedBy", transaction.getTransactingUserName());
				if(transaction.getTransactingUser()!=null && transaction.getTransactingUser().getExternalId()!=null)
					jsonObject.add("bookedById", transaction.getTransactingUser().getExternalId());
				jsonObject.add("vehicleNumber", vs.getVehicle().getInventoryNumber());
				jsonObject.add("scheduleStationCode", vs.getScheduleStationCode().getScheduleStationCode());
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			//}
			
		}catch(Exception e)
		{
			log.warn(e); log.error("exception...", e);
			e.printStackTrace();
			JsonObject jsonObj = jsonObject.build();
            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
		}
	}
	


	
	public Response listShipments(String token, String requestId, String clientCode, String startDate, String endDate, String scheduleStationCode, 
			String destinationStationCode, String trainCode, String locomotiveNumber, String scheduleDate) {
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
			
			
			
			String sql = "";
			String hql = "Select tp from Shipment tp where tp.deletedAt IS NULL";

			if(startDate!=null && endDate!=null)
			{
				hql = hql + " AND tp.createdAt >= '" + startDate + "' AND tp.createdAt <= '" + endDate +  "' ";
			}
			
			if(scheduleDate!=null)
			{
				String scheduleDateEnd = scheduleDate + " 23:59:59";
				scheduleDate = scheduleDate + " 00:00:00";
				hql = hql + " AND tp.vehicleSchedule.departureScheduleStation.scheduleStationCode.departureDate >= '" + scheduleDate + "' " +
						"AND tp.vehicleSchedule.departureScheduleStation.scheduleStationCode.departureDate <= '" + scheduleDateEnd +  "' ";
			}
			
			if(scheduleStationCode!=null)
			{
				hql = hql + " AND tp.vehicleSchedule.scheduleStationCode.scheduleStationCode = '"+scheduleStationCode+"'";
			}
			
			if(trainCode!=null)
			{
				hql = hql + " AND tp.vehicleSchedule.vehicle.vehicleCode = '"+ trainCode +"'";
			}
			
			if(locomotiveNumber!=null)
			{
				hql = hql + " AND tp.vehicleSchedule.vehicle.inventoryNumber = '"+ locomotiveNumber +"'";
			}
			
			if(destinationStationCode!=null)
			{
				hql = hql + " AND tp.arrivalScheduleStation.station.stationCode = '"+ destinationStationCode +"'";
			}
			
			hql = hql + sql;
			hql = hql + " ORDER BY tp.id DESC";
			System.out.println(hql);
			//log.infohql);
			
			Collection<Shipment> shipments = (Collection<Shipment>)this.swpService.getAllRecordsByHQL(hql);
			
			
			jsonObject.add("message", "Shipments listed successfully");
			jsonObject.add("status", ERROR.GENERAL_SUCCESS);
			jsonObject.add("shipmentList", new Gson().toJson(shipments));
			
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
	
	
	
	public Response listShipmentItems(String token, String requestId, String clientCode, String startDate, String endDate, String scheduleStationCode, 
			String destinationStationCode, String trainCode, String locomotiveNumber, String scheduleDate, String trackingId) {
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
			
			
			
			String sql = "";
			String hql = "Select tp from ShipmentItem tp where tp.deletedAt IS NULL";

			if(startDate!=null && endDate!=null)
			{
				hql = hql + " AND tp.createdAt >= '" + startDate + "' AND tp.createdAt <= '" + endDate +  "' ";
			}
			
			if(scheduleDate!=null)
			{
				String scheduleDateEnd = scheduleDate + " 23:59:59";
				scheduleDate = scheduleDate + " 00:00:00";
				hql = hql + " AND tp.shipment.vehicleSchedule.departureScheduleStation.scheduleStationCode.departureDate >= '" + scheduleDate + "' " +
						"AND tp.shipment.vehicleSchedule.departureScheduleStation.scheduleStationCode.departureDate <= '" + scheduleDateEnd +  "' ";
			}
			
			if(scheduleStationCode!=null)
			{
				hql = hql + " AND tp.shipment.vehicleSchedule.scheduleStationCode.scheduleStationCode = '"+scheduleStationCode+"'";
			}
			
			if(trainCode!=null)
			{
				hql = hql + " AND tp.shipment.vehicleSchedule.vehicle.vehicleCode = '"+ trainCode +"'";
			}
			
			if(locomotiveNumber!=null)
			{
				hql = hql + " AND tp.shipment.vehicleSchedule.vehicle.inventoryNumber = '"+ locomotiveNumber +"'";
			}
			
			if(destinationStationCode!=null)
			{
				hql = hql + " AND tp.shipment.arrivalScheduleStation.station.stationCode = '"+ destinationStationCode +"'";
			}
			
			hql = hql + sql;
			hql = hql + " ORDER BY tp.id DESC";
			System.out.println(hql);
			//log.infohql);
			
			Collection<ShipmentItem> shipments = (Collection<ShipmentItem>)this.swpService.getAllRecordsByHQL(hql);
			
			
			jsonObject.add("message", "Shipment Items listed successfully");
			jsonObject.add("status", ERROR.GENERAL_SUCCESS);
			jsonObject.add("shipmentItemList", new Gson().toJson(shipments));
			
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
	
	
	
	public Response getShipment(String trackingId, String token, String requestId, String clientCode) {
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
			
			
			
			String sql = "";
			String hql = "Select tp from Shipment tp where tp.deletedAt IS NULL";
			if(trackingId!=null)
			{
				sql = " AND tp.trackingId = '"+trackingId+"'";
			}
			
			hql = hql + sql;
			hql = hql + " ORDER BY tp.id DESC";
			Shipment shipment = (Shipment)this.swpService.getUniqueRecordByHQL(hql);
			
			
			hql = "Select tp from ShipmentItem tp where tp.shipment.shipmentId = " + shipment.getShipmentId();
			Collection<ShipmentItem> shipmentItems = (Collection<ShipmentItem>) swpService.getAllRecordsByHQL(hql);
			
			hql = "Select tp from CourierTrackHistory tp where tp.shipment.shipmentId = " + shipment.getShipmentId();
			Collection<CourierTrackHistory> courierTrackHistory = (Collection<CourierTrackHistory>) swpService.getAllRecordsByHQL(hql);
			
			jsonObject.add("message", "Shipment found");
			jsonObject.add("status", ERROR.GENERAL_SUCCESS);
			jsonObject.add("shipment", new Gson().toJson(shipment));
			jsonObject.add("courierTrackHistory", new Gson().toJson(courierTrackHistory));
			jsonObject.add("shipmentItemList", new Gson().toJson(shipmentItems));
			
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

	
	


	
	public Response searchAvailableCourierTrips(String deviceCode, String token, String departureStationCode, String arrivalStationCode, String departureTime, 
			Integer hoursAdd,  String courierServiceCode, String items, String requestId, String ipAddress, String clientCode) {
		// TODO Auto-generated method stub
		JsonObjectBuilder jsonObject = Json.createObjectBuilder();
    	
    	
		try{
			Integer outwardStatus = null;
			String outwardMessage = null;
			Boolean outwardSeatsAvailabilityStatus = null;
			String outwardVehicleTripList = null;
			String outwardTicketPrices = null;
			
			if(deviceCode==null || departureStationCode==null || arrivalStationCode==null)
			{
				jsonObject.add("status", ERROR.INCOMPLETE_PARAMETERS);
				jsonObject.add("message", "Incomplete Parameters provided in request.");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			//log.infodeviceCode);
			//log.infotoken);
			//log.infodepartureStationCode);
			//log.infoarrivalStationCode);
			//log.infodepartureTime);
			//log.infohoursAdd);
			//log.infocourierServiceCode);
			//log.infoitems);
			//log.inforequestId);
			//log.infoipAddress);
			//log.infoclientCode);
			
			swpService = serviceLocator.getSwpService();
			Application app = Application.getInstance(swpService);
			
			Device device = null;
			String hql = "Select tp from Device tp where tp.deviceCode = '"+deviceCode+"' AND tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL";
			//log.info"hql ==" + hql);
			device = (Device)swpService.getUniqueRecordByHQL(hql);
			
			if(device==null)
			{
				jsonObject.add("status", ERROR.DEVICE_NOT_FOUND);
				jsonObject.add("message", "Invalid device used to process transaction");
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
			
			String username = verifyJ.has("username") ? verifyJ.getString("username") : null;
			//log.inforequestId + "username ==" + (username==null ? "" : username));
			String roleCode_ = verifyJ.has("roleCode") ? verifyJ.getString("roleCode") : null;
			//log.inforequestId + "roleCode_ ==" + (roleCode_==null ? "" : roleCode_));
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
			}
			
			JsonObject outwardTrip = findAvailableTrips(token, departureStationCode, arrivalStationCode,  
					departureTime, hoursAdd, courierServiceCode, items, requestId,  ipAddress,  clientCode, user);
			
			
			outwardStatus = outwardTrip!=null && outwardTrip.containsKey("status") ? outwardTrip.getInt("status") : ERROR.GENERAL_SYSTEM_ERROR;
			outwardMessage = outwardTrip!=null && outwardTrip.containsKey("message") ? outwardTrip.getString("message") : "No Outward Trips Available"; 
			if(outwardTrip!=null && outwardTrip.containsKey("status") && outwardTrip.getInt("status")==0)
			{
				outwardSeatsAvailabilityStatus = outwardTrip.getBoolean("seatsAvailabilityStatus");
				outwardVehicleTripList = outwardTrip.getString("vehicleTripList");
				outwardTicketPrices = outwardTrip.getString("ticketPrices");
				String shippingTrackingId = outwardTrip.getString("shippingTrackingId");
				//String tripCode = outwardTrip.getString("tripCode");

				jsonObject.add("status", ERROR.GENERAL_SUCCESS);
				jsonObject.add("outwardTripMessage", "Outward Trips listed successfully");
				jsonObject.add("shippingTrackingId", shippingTrackingId);
				jsonObject.add("outwardTripStatus", ERROR.GENERAL_SUCCESS);
				jsonObject.add("outwardTripSeatsAvailabilityStatus", outwardSeatsAvailabilityStatus);
				jsonObject.add("outwardVehicleTripList", outwardVehicleTripList);
				jsonObject.add("outwardTicketPrices", outwardTicketPrices);
				//jsonObject.add("tripCode", tripCode);
			}
			else
			{
				jsonObject.add("status", ERROR.TRIP_NOT_FOUND);
				jsonObject.add("outwardTripMessage", outwardMessage);
				jsonObject.add("outwardTripStatus", outwardStatus);
				if(outwardSeatsAvailabilityStatus!=null)
					jsonObject.add("outwardTripSeatsAvailabilityStatus", outwardSeatsAvailabilityStatus);
				if(outwardVehicleTripList!=null)
					jsonObject.add("outwardVehicleTripList", outwardVehicleTripList);
				if(outwardTicketPrices!=null)
					jsonObject.add("outwardTicketPrices", outwardTicketPrices);
			}
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
	
	
	private JsonObject findAvailableTrips(String token, String mainDepartureStationCode, String mainArrivalStationCode, 
			String departureTime, Integer hoursAdd,  String courierServiceCode, String items, String requestId, String ipAddress, String clientCode, User user) {
		// TODO Auto-generated method stub
		
		//(token, departureStationCode, arrivalStationCode,  
		//departureTime, hoursAdd, courierServiceCode, items, requestId,  ipAddress,  clientCode)productCategoryCode, description, parcelName, parcelQuantity;
		JsonObjectBuilder jsonObject = Json.createObjectBuilder();
    	String tripBookingToken = null;
    	//String appliedPricingIndexDescription = null;
		try{
			
			if(mainDepartureStationCode==null || mainArrivalStationCode==null || hoursAdd==null || courierServiceCode==null || departureTime==null || items==null)
			{
				jsonObject.add("status", ERROR.INCOMPLETE_PARAMETERS);
				jsonObject.add("message", "Incomplete Parameters provided in request.");
				JsonObject jsonObj = jsonObject.build();
	            return jsonObj;
			}
			
			
			swpService = serviceLocator.getSwpService();
			Application app = Application.getInstance(swpService);
			JSONObject verifyJ = null;
			if(token!=null)
			{
				verifyJ = UtilityHelper.verifyToken(token, app);
				if(verifyJ.length()==0 || (verifyJ.length()>0 && verifyJ.has("active") && verifyJ.getInt("active")==0))
				{
					jsonObject.add("user_session_status", ERROR.FORCE_LOGOUT_USER);
					jsonObject.add("user_session_message", "Your session has expired. Please log in again");
				}
			}

			Client client=null;
			if(clientCode!=null)
			{
				String hql = "Select tp from Client tp where tp.clientCode = '"+ clientCode + "' AND tp.deletedAt IS NULL";
				//log.infohql);
				client = (Client)this.swpService.getUniqueRecordByHQL(hql);
				if(client==null)
				{
					jsonObject.add("status", ERROR.INCOMPLETE_PARAMETERS);
					jsonObject.add("message", "The clientCode is invalid");
					JsonObject jsonObj = jsonObject.build();
		            return jsonObj;
				}
			}
			
			String daSql = "Select ta.scheduleStationId from ScheduleStation ta";
			if(mainDepartureStationCode!=null)
			{
				//Date departureTimeFormatted = (new SimpleDateFormat("yyyy-MM-dd HH:mm")).parse(departureTime);
				daSql = daSql + " WHERE (lower(ta.station.stationCode) = '"+mainDepartureStationCode.toLowerCase()+"' OR lower(ta.station.stationCode) = '"+mainArrivalStationCode.toLowerCase()+"')" +
						" AND ta.client.clientCode = '"+clientCode+"') ";
			}
			
			//log.info"daSQL --- " + daSql);
			List<Long> scheduleStationIds= (List<Long>)this.swpService.getAllRecordsByHQL(daSql);
			//log.info"scheduleStationIds size --- " + scheduleStationIds.size());
			//log.info"departureTime --- " + departureTime);
			//log.info"departureTime.split()[0] == " + (departureTime.split(" ")[0]));
			
			String departureDay = departureTime.split(" ")[0];
			departureDay = departureDay + " 00:00";
			Date departureDate = new SimpleDateFormat("yyyy-MM-dd").parse(departureDay);
			Calendar cal = Calendar.getInstance();
			cal.setTime(departureDate);
			if(hoursAdd!=null)
				cal.add(Calendar.HOUR, hoursAdd);
			else
				cal.add(Calendar.HOUR, 24);
			
			Date bufferDepartureDate = cal.getTime();
			String bufferDepartureDateStr = new SimpleDateFormat("yyyy-MM-dd").format(bufferDepartureDate);
			String bufferDepartureTime = bufferDepartureDateStr + " 23:59";
			daSql = "Select tp.scheduleStationCode.scheduleStationCode" +
					" from ScheduleStation tp where (tp.departureTime >= '"+departureDay+"' AND " +
					"tp.departureTime <= '"+bufferDepartureDateStr+" 23:59') AND tp.scheduleStationId IN " +
					"("+StringUtils.join(scheduleStationIds, ", ")+") AND tp.scheduleStationCode.courierEnabled = 1 " +
					" GROUP BY tp.scheduleStationCode.scheduleStationCode";
			//AND tp.vehicleTripRoutingOriginSchedule.scheduleStation.departureTime <= '"+bufferDepartureTime+"'
			// AND tp.vehicleTripRoutingId IN ("+StringUtils.join(vehicleTripIds, ',')+")
			//log.info"---> " + daSql);
			List<String> scheduleStationCodes = (List<String>)this.swpService.getAllRecordsByHQL(daSql);
			//log.info"---> scheduleStationCodes size = " + scheduleStationCodes.size());
			//log.info"---> scheduleStationCodes = " + new Gson().toJson(scheduleStationCodes));
			
			if(scheduleStationCodes.size()>0)
			{

				daSql = "Select tp from CourierService tp where tp.courierServiceCode = '"+courierServiceCode+"' AND " +
						"tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL";
				CourierService courierService = (CourierService)swpService.getUniqueRecordByHQL(daSql);
				
				daSql = "Select tp from Station tp where tp.stationCode = '"+mainDepartureStationCode+"' AND " +
							"tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL";
				Station departureStation = (Station)swpService.getUniqueRecordByHQL(daSql);

				daSql = "Select tp from Station tp where tp.stationCode = '"+mainArrivalStationCode+"' AND " +
							"tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL";
				Station arrivalStation = (Station)swpService.getUniqueRecordByHQL(daSql);
				
				daSql = "Select tp from TripZoneStation tp where tp.station.stationCode = '"+mainArrivalStationCode+"' AND " +
						"tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL";
				TripZoneStation arrivalTripZoneStation = (TripZoneStation)swpService.getUniqueRecordByHQL(daSql);

				
				daSql = "Select tp from TripZoneStation tp where tp.station.stationCode = '"+mainDepartureStationCode+"' AND " +
						"tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL";
				TripZoneStation depatureTripZoneStation = (TripZoneStation)swpService.getUniqueRecordByHQL(daSql);
				
				
				Integer tripRouteOrderdifference =  depatureTripZoneStation.getTripZone().getRouteOrder() - arrivalTripZoneStation.getTripZone().getRouteOrder();
				tripRouteOrderdifference = Math.abs(tripRouteOrderdifference);
				
				
				JSONArray jsArr = new JSONArray(items);
				JSONArray jsonTicketPrices = new JSONArray();
				
				for(int i=0; i<jsArr.length(); i++)
				{
					JSONObject jsItem = jsArr.getJSONObject(i);
					String productCategoryCode = jsItem.getString("productCategoryCode");
					String description = jsItem.getString("description");
					String parcelName = jsItem.getString("parcelName");
					Integer parcelQuantity = jsItem.getInt("parcelQuantity");
					Double weight = jsItem.has("parcelWeight") ? jsItem.getDouble("parcelWeight") : null;
					
					
					
	
					daSql = "Select tp from ProductCategory tp where tp.productCategoryCode = '"+productCategoryCode+"' AND " +
							"tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL";
					ProductCategory productCategory = (ProductCategory)swpService.getUniqueRecordByHQL(daSql);
					
					if(productCategory!=null)
					{
						/*daSql = "Select tp from CourierDeliveryPrice tp where tp.tripZone.routeOrder = " + tripRouteOrderdifference + 
									" AND tp.deletedAt is NULL and tp.client.clientCode = '"+clientCode+ 
									"' AND tp.productCategory.id = " + productCategory.getProductCategoryId() + 
									//" AND tp.tripZone.tripZoneId = " + arrivalTripZoneStation.getTripZone().getTripZoneId() + 
									" AND tp.courierService.courierServiceId = "+courierService.getCourierServiceId()+"";*/
						String hql = "Select tp from DeliveryDistance tp where tp.station.stationCode = '"+mainDepartureStationCode+"' AND tp.client.clientCode = '"+clientCode+"'";
						//log.infohql);
						DeliveryDistance departureDeliverDistance = (DeliveryDistance)swpService.getUniqueRecordByHQL(hql);
						
						hql = "Select tp from DeliveryDistance tp where tp.station.stationCode = '"+mainArrivalStationCode+"' AND tp.client.clientCode = '"+clientCode+"'";
						//log.infohql);
						DeliveryDistance arrivalDeliverDistance = (DeliveryDistance)swpService.getUniqueRecordByHQL(hql);
						
						if(departureDeliverDistance==null || arrivalDeliverDistance==null || weight==null)
						{
							jsonObject.add("status", ERROR.TRIP_ZONE_NOT_FOUND);
							jsonObject.add("message", "We could not find a delivery price for this trip");
							JsonObject jsonObj = jsonObject.build();
				            return jsonObj;
						}
						
						Double deliveryDistance = Math.abs(departureDeliverDistance.getDistance() - arrivalDeliverDistance.getDistance());
						
						
						daSql = "Select tp from CourierDeliveryPrice tp where tp.deletedAt IS NULL AND tp.client.clientCode = '"+clientCode+"' AND " +
								"tp.productCategory.productCategoryId = '"+productCategory.getProductCategoryId()+"'";
						daSql = daSql + " AND ("+deliveryDistance+" BETWEEN tp.minDistance AND tp.maxDistance)";
						if(productCategory.getIsWeightApplicable()!=null && productCategory.getIsWeightApplicable().equals(Boolean.TRUE))
						{
							daSql = daSql + " AND ("+weight+" BETWEEN tp.minWeight AND tp.maxWeight)";
						}
						daSql = daSql + " AND tp.courierService.courierServiceCode = '"+courierServiceCode+"'";
						
						
						//log.info"daSQL ===> " + daSql);
						CourierDeliveryPrice courierDeliveryPrice = (CourierDeliveryPrice)swpService.getUniqueRecordByHQL(daSql);
						
						if(courierDeliveryPrice==null)
						{
							jsonObject.add("status", ERROR.NO_TRIPS_FOUND_FOR_SPECIFIED_DATES);
							jsonObject.add("message", "No pricing Found For the Product specified");
							JsonObject jsonObj = jsonObject.build();
				            return jsonObj;
						}
						else
						{
							//log.info"v size ---> " + courierDeliveryPrice.getAmount() + " && " + courierDeliveryPrice.getCourierDeliveryPriceId());
							
							Double tempAmount = courierDeliveryPrice.getAmount();
							JSONObject vtpJS = new JSONObject();
							vtpJS.put("productCategoryName", courierDeliveryPrice.getProductCategory().getProductCategoryName());
							vtpJS.put("productCategoryCode", courierDeliveryPrice.getProductCategory().getProductCategoryCode());
							vtpJS.put("courierServiceName", courierDeliveryPrice.getCourierService().getCourierServiceName());
							vtpJS.put("courierServiceCode", courierDeliveryPrice.getCourierService().getCourierServiceCode());
							vtpJS.put("amount", courierDeliveryPrice.getAmount());
							vtpJS.put("deliveryPriceId", courierDeliveryPrice.getCourierDeliveryPriceId());
							vtpJS.put("deliveryPrice", courierDeliveryPrice);
							vtpJS.put("deliveryPriceAmount", courierDeliveryPrice.getAmount());
							vtpJS.put("description", description);
							vtpJS.put("parcelName", parcelName);
							vtpJS.put("parcelQuantity", parcelQuantity);
							if(weight!=null)
							{
								vtpJS.put("parcelWeight", weight);
							}
							vtpJS.put("productCategory", courierDeliveryPrice.getProductCategory());
							jsonTicketPrices.put(vtpJS);
							
							Double deliveryCharge = 0.00;
							if(jsonTicketPrices!=null && jsonTicketPrices.length()>0)
							{
								deliveryCharge = jsonTicketPrices.getJSONObject(0).getDouble("amount");						
							}
						}
					}
				}
				
				
				
				String hql = "Select tp from VehicleSchedule tp where " +
						"tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL AND tp.courierEnabled = 1";
				Collection<VehicleSchedule> vehicleSchedules = (Collection<VehicleSchedule>)swpService.getAllRecordsByHQL(hql);
				Iterator<VehicleSchedule> it1 = vehicleSchedules.iterator();
				Map<String, Vehicle> vs = new HashMap<String, Vehicle>();
				while(it1.hasNext())
				{
					VehicleSchedule vs1 = it1.next();
					vs.put(vs1.getScheduleStationCode().getScheduleStationCode(), vs1.getVehicle());
				}
				hql = "Select tp from ScheduleStation tp where tp.scheduleStationCode.scheduleStationCode " +
						"IN ('" + StringUtils.join(scheduleStationCodes, "', '") + "') ORDER BY tp.departureTime";
				//log.info"hql --- " + hql);
				Collection<ScheduleStation> scheduleStations = (Collection<ScheduleStation>)this.swpService.getAllRecordsByHQL(hql);
				Iterator<ScheduleStation> iter = scheduleStations.iterator();
				
				

				JSONObject jsonTrips = new JSONObject();
				JSONObject jsonObjectEntry = null;
				String shippingTrackingId = null;
				ScheduleStation departureScheduleStation = null;
				ScheduleStation arrivalScheduleStation = null;
				while(iter.hasNext())
				{
					ScheduleStation scheduleStation = iter.next();
					String scheduleStationCode = scheduleStation.getScheduleStationCode().getScheduleStationCode();
					//log.info"scheduleStationCode ----//// " + scheduleStationCode);
					//log.info"jsonTrips size ----//// " + jsonTrips.length());
					if(jsonTrips.has(scheduleStationCode))
					{
						jsonObjectEntry = jsonTrips.getJSONObject(scheduleStationCode);
						if(departureStation.getStationId().equals(scheduleStation.getStation().getStationId()))
						{
							//log.info"---> departureStation scheduleStation = " + new Gson().toJson(scheduleStation));
							jsonObjectEntry.put("originStationCity", scheduleStation.getStation().getDistrict().getName());
							jsonObjectEntry.put("originStation", scheduleStation.getStation().getStationName());
							jsonObjectEntry.put("originStationCode", scheduleStation.getStation().getStationCode());
							if(scheduleStation.getDepartureTime()!=null)
							{
								jsonObjectEntry.put("departureTime", scheduleStation.getDepartureTime());
							}
							departureScheduleStation = scheduleStation;
							
						}
						if(arrivalStation.getStationId().equals(scheduleStation.getStation().getStationId()))
						{
							//log.info"---> arrivalStation scheduleStation = " + new Gson().toJson(scheduleStation));
							jsonObjectEntry.put("arrivalStationCity", scheduleStation.getStation().getDistrict().getName());
							jsonObjectEntry.put("arrivalStation", scheduleStation.getStation().getStationName());
							jsonObjectEntry.put("arrivalStationCode", scheduleStation.getStation().getStationCode());
							if(scheduleStation.getArrivalTime()!=null)
							{
								jsonObjectEntry.put("arrivalTime", scheduleStation.getArrivalTime());
							}
							arrivalScheduleStation = scheduleStation;
						}
						jsonObjectEntry.put("vehicleName", vs.get(scheduleStation.getScheduleStationCode().getScheduleStationCode()).getVehicleName());
						jsonTrips.put(scheduleStationCode, jsonObjectEntry);
							
					}
					else
					{
						jsonObjectEntry = new JSONObject();
						if(departureStation.getStationId().equals(scheduleStation.getStation().getStationId()))
						{
							jsonObjectEntry.put("originStationCity", scheduleStation.getStation().getDistrict().getName());
							jsonObjectEntry.put("originStation", scheduleStation.getStation().getStationName());
							jsonObjectEntry.put("originStationCode", scheduleStation.getStation().getStationCode());
							if(scheduleStation.getDepartureTime()!=null)
							{
								jsonObjectEntry.put("departureTime", scheduleStation.getDepartureTime());
							}
							departureScheduleStation = scheduleStation;
						}
						if(arrivalStation.getStationId().equals(scheduleStation.getStation().getStationId()))
						{
							jsonObjectEntry.put("arrivalStationCity", scheduleStation.getStation().getDistrict().getName());
							jsonObjectEntry.put("arrivalStation", scheduleStation.getStation().getStationName());
							jsonObjectEntry.put("arrivalStationCode", scheduleStation.getStation().getStationCode());
							if(scheduleStation.getArrivalTime()!=null)
							{
								jsonObjectEntry.put("arrivalTime", scheduleStation.getArrivalTime());
							}
							arrivalScheduleStation = scheduleStation;
							
						}
						jsonObjectEntry.put("vehicleName", vs.get(scheduleStation.getScheduleStationCode().getScheduleStationCode()).getVehicleName());
						jsonTrips.put(scheduleStationCode, jsonObjectEntry);
					}
					
				}
				//log.info"##------------------------");
				//log.infojsonTrips.toString());
				//log.info"##------------------------");
				
				JSONObject jsonTrips_ = new JSONObject();
				Iterator<String> iteratorString = jsonTrips.keys();
				Boolean seatsAvailableStatus = Boolean.FALSE;
				Boolean returnSeatsAvailableStatus = Boolean.FALSE;
				while(iteratorString.hasNext())
				{
					String key = iteratorString.next();
					jsonObjectEntry = jsonTrips.getJSONObject(key);
					boolean proceed = false;
					Integer departRouteOrder = null;
					Integer arriveRouteOrder = null;
					String departStationCode = null;
					String arriveStationCode = null;
					Boolean departureStationChecker = false;
					Boolean arriveStationCodeChecker = false;
					Integer departureTripOrder = null;
					Integer arriveTripOrder = null;
					Date departTime = null;
					Date arrivalTime = null;
					
					
					departRouteOrder = null;
					arriveRouteOrder = null;
					//log.info"---===>trip = " + jsonObjectEntry.toString());
					departStationCode = jsonObjectEntry.has("originStationCode") ? jsonObjectEntry.getString("originStationCode") : null;
					arriveStationCode = jsonObjectEntry.has("arrivalStationCode") ? jsonObjectEntry.getString("arrivalStationCode") : null;
					departTime = jsonObjectEntry.has("departureTime") ? (Date)jsonObjectEntry.get("departureTime") : null;
					arrivalTime = jsonObjectEntry.has("arrivalTime") ? (Date)jsonObjectEntry.get("arrivalTime") : null;
					
					//log.info"-------------");
					//log.info"departureStationCode = " + departStationCode);
					//log.info"arrivalStationCode = " + arriveStationCode);
					//log.info"departTime = " + departTime);
					//log.info"arrivalTime = " + arrivalTime);
					//log.info"-------------");
					
					//log.info"departureScheduleStation dep ==" + departureScheduleStation.getDepartureTime());
					//log.info"departureScheduleStation arr ==" + departureScheduleStation.getArrivalTime());
					//log.info"departureScheduleStation dep ==" + arrivalScheduleStation.getDepartureTime());
					//log.info"departureScheduleStation arr ==" + arrivalScheduleStation.getArrivalTime());
					
					
					
					String getAvailableSeatStr = (String)(getCourierCabinAllocationAndLockDown(Boolean.FALSE, token, key, mainDepartureStationCode, mainArrivalStationCode, 
							courierService, jsonTicketPrices, requestId, ipAddress, client, tripBookingToken, user).getEntity());
					//log.info"getAvailableSeatStr ==== " + getAvailableSeatStr);
					JSONObject availableSeats = new JSONObject(getAvailableSeatStr);
					if(availableSeats.has("status") && availableSeats.getInt("status")==0)
					{
						//log.info"availableSeats ---- " + availableSeats.toString());
						String detailsStr = availableSeats.getString("cabinSpacesLockedDown");
						//log.info"detailsStr ---- " + detailsStr);
						JSONObject details = new JSONObject(detailsStr);
						//log.info"details === " + details.toString());
						String tripDetailsStr = availableSeats.getString("tripDetails");
						shippingTrackingId = availableSeats.getString("shippingTrackingId");
						//log.info"tripDetailsStr ---- " + tripDetailsStr);
						//tripBookingToken = details.getString("tripBookingToken");
						JSONArray tripDetails = new JSONArray(tripDetailsStr);
						//log.info"tripDetails length === " + tripDetails.length());
						seatsAvailableStatus = Boolean.TRUE;
						JSONObject js_ = new JSONObject();
						js_.put("cabinSpacesLockedDown", detailsStr);
						js_.put("tripDetails", tripDetailsStr);
						js_.put("tripBreakdown", jsonTrips.get(key));
						js_.put("tripCode", key);
						if(departTime!=null && arrivalTime!=null && departTime.before(arrivalTime))
							jsonTrips_.put(key, js_);
					}			
					
					
					
					
				}
				
				
				//log.info"##------------------------");
				//log.infojsonTrips_.toString());
				//log.info"##------------------------");
				
				
				
				
				String vehicleTripListReturnStr = null;
				String ticketPricesStr = null;
				
				
				
				
				if(jsonTrips_.length()>0)
				{
					jsonObject.add("message", "Vehicle Trips listed successfully");
					jsonObject.add("status", ERROR.GENERAL_SUCCESS);
					jsonObject.add("seatsAvailabilityStatus", seatsAvailableStatus);
					jsonObject.add("shippingTrackingId", shippingTrackingId);
					//jsonObject.add("token", tripToken);
					jsonObject.add("vehicleTripList", (jsonTrips_.toString()));
					jsonObject.add("ticketPrices", jsonTicketPrices.toString());
				}
				else
				{
					jsonObject.add("message", "Vehicle Trips searched for not available");
					jsonObject.add("status", ERROR.TRIP_NON_AVAILABLE);
					//jsonObject.add("token", tripToken);
					jsonObject.add("vehicleTripList", (jsonTrips_.toString()));
				}
				JsonObject jsonObj = jsonObject.build();
	            return jsonObj;
				
			}
			else
			{
				jsonObject.add("message", "Vehicle Trips not found matching the details provided");
				jsonObject.add("status", ERROR.NO_TRIPS_FOUND_FOR_SPECIFIED_DATES);
				jsonObject.add("token", token);
				JsonObject jsonObj = jsonObject.build();
	            return jsonObj;
			}
			
		}catch(Exception e)
		{
			log.warn(e); log.error("exception...", e);
			e.printStackTrace();
			JsonObject jsonObj = jsonObject.build();
            return jsonObj;
		}
	}
	
	
	
	public Response getCourierCabinAllocationAndLockDownOld(Boolean lockdownSpace, String token, String tripCode, String departingStationCode, String arrivalStationCode, 
			Double appliedPricingIndex, String appliedPricingIndexDescription, ProductCategory productCategory, CourierService courierService, Double deliveryCharge, Double advicedDeliveryCharge,
			String description, TicketCollectionPoint destinationCollectionPoint, Double totalParcelHeight, 
			Double totalParcelVolume, Double totalParcelWidth,  Double totalParcelWeight, String parcelName, Integer parcelQuantity,
			String requestId, String ipAddress, String clientCode, String tripToken) {
		// TODO Auto-generated method stub
		JsonObjectBuilder jsonObject = Json.createObjectBuilder();
    	String lockedDownBy = null;
		try{
			
			if(departingStationCode==null || tripCode==null || arrivalStationCode==null)
			{
				jsonObject.add("status", ERROR.INCOMPLETE_PARAMETERS);
				jsonObject.add("message", "Incomplete Parameters provided in request.");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			if(lockdownSpace==null)
			{
				lockdownSpace= Boolean.FALSE;
			}

			
			Client client=null;
			if(clientCode!=null)
			{
				String hql = "Select tp from Client tp where tp.clientCode = '"+ clientCode + "' AND tp.deletedAt IS NULL";
				//log.infohql);
				client = (Client)this.swpService.getUniqueRecordByHQL(hql);
				if(client==null)
				{
					jsonObject.add("status", ERROR.INCOMPLETE_PARAMETERS);
					jsonObject.add("message", "The clientCode is invalid");
					JsonObject jsonObj = jsonObject.build();
		            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
				}
			}
			
			
			
			swpService = serviceLocator.getSwpService();
			Application app = Application.getInstance(swpService);
			JSONObject verifyJ = null;
			User user = null;
			if(token!=null)
			{
				verifyJ = UtilityHelper.verifyToken(token, app);
				if(verifyJ.length()==0 || (verifyJ.length()>0 && verifyJ.has("active") && verifyJ.getInt("active")==0))
				{
					jsonObject.add("status", ERROR.FORCE_LOGOUT_USER);
					jsonObject.add("message", "Your session has expired. Please log in again");
					JsonObject jsonObj = jsonObject.build();
		            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
				}
				
				String username = verifyJ.has("username") ? verifyJ.getString("username") : null;
				//log.inforequestId + "username ==" + (username==null ? "" : username));
				String roleCode_ = verifyJ.has("roleCode") ? verifyJ.getString("roleCode") : null;
				//log.inforequestId + "roleCode_ ==" + (roleCode_==null ? "" : roleCode_));
				//User user = null;
				RoleType roleCode = null;
				
				if(roleCode_ != null)
				{
					roleCode = RoleType.valueOf(roleCode_);
				}
				
				if(username!=null)
				{
					user = (User)this.swpService.getUniqueRecordByHQL("select tp from User tp where tp.username = '" + username + "' AND tp.deletedAt IS NULL AND " +
							"tp.userStatus = " + UserStatus.ACTIVE.ordinal());
					
					if(user!=null)
					{

						if(tripToken==null)
						{
							JSONObject userDetails = new JSONObject();
							userDetails.put("username", user.getUsername() + "-" + RandomStringUtils.randomAlphabetic(10).toUpperCase());
							userDetails.put("roleCode", roleCode.name());
							//userDetails.put("priviledges", {});
							Gson gson = new Gson();
							String obj = gson.toJson(userDetails);
							String tkId = RandomStringUtils.randomAlphanumeric(10);
							tripToken = app.createJWT(tkId, clientCode, obj, (client.getLockDownInterval() *60*1000));
						}
					}
				}
			}
			
			lockedDownBy = tripToken;
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.MINUTE, client.getLockDownInterval());
			Date lockedDownExpiryDate = cal.getTime();
			JSONObject vehicleSeatsLockedDown = new JSONObject();
			
			
			
			String hql = "Select tp from ScheduleStation tp where " + 
					"(tp.station.stationCode = '"+ departingStationCode +"') AND " + 
					"tp.scheduleStationCode.scheduleStationCode = '" + tripCode+"' " + 
					"AND tp.scheduleStationCode.courierEnabled = 1 " + 
					"AND tp.deletedAt is NULL AND " +
					"tp.arrivalTime IS NULL AND " + 
					"tp.client.clientCode = '"+clientCode+"'";
			ScheduleStation departureScheduleStation = (ScheduleStation)swpService.getUniqueRecordByHQL(hql);
			
			hql = "Select tp from ScheduleStation tp where " + 
					"(tp.station.stationCode = '"+arrivalStationCode+"') AND " + 
					"tp.departureTime IS NULL AND " + 
					"tp.scheduleStationCode.scheduleStationCode = '" + tripCode+"' " + 
					"AND tp.scheduleStationCode.courierEnabled = 1 " + 
					"AND tp.deletedAt is NULL AND tp.client.clientCode = '"+clientCode+"'";
			ScheduleStation arrivalScheduleStation = (ScheduleStation)swpService.getUniqueRecordByHQL(hql);
			if(departureScheduleStation==null || (arrivalScheduleStation==null))
			{
				jsonObject.add("status", ERROR.TRIP_NOT_FOUND);
				jsonObject.add("message", "The vehicle trip could not be found");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			
			
			
			hql = "Select tp.scheduleStationId from ScheduleStation tp where tp.client.clientCode = '"+ clientCode + "' AND tp.deletedAt IS NULL " +
				"AND tp.scheduleStationCode.scheduleStationCode = '"+tripCode+"' AND " +
				"COALESCE(tp.arrivalTime, tp.departureTime) >= '" + departureScheduleStation.getDepartureTime() + "' AND " + 
				"COALESCE(tp.arrivalTime, tp.departureTime) <= '" + arrivalScheduleStation.getArrivalTime() + "' " +
				"AND tp.scheduleStationCode.scheduleStationCode = '"+ tripCode +"' " +
				"AND tp.scheduleStationCode.courierEnabled = 1 " + 
				"ORDER BY COALESCE(tp.arrivalTime, tp.departureTime)";
			//log.info"coalesce = " + hql);
			List<Long> scheduleStationDepartures = (List<Long>)this.swpService.getAllRecordsByHQL(hql);
			//log.info"departure size = " + scheduleStationDepartures.size());
			
			
			if(scheduleStationDepartures==null || (scheduleStationDepartures!=null && scheduleStationDepartures.size()==0))
			{
				jsonObject.add("message", "Vehicle cabin space could not be locked. No available spaces");
				jsonObject.add("status", ERROR.AVAILABLE_CABINS_COULD_NOT_BE_LOCKED_DOWN);
				jsonObject.add("lifeSpan", client.getLockDownInterval());
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			hql = "Select tp FROM CourierCabinShipment tp WHERE tp.deletedAt IS NULL AND " +
					"tp.client.clientCode = '"+clientCode+"' AND " + 
					"tp.courierAvailabilityStatus = " + CourierAvailabilityStatus.OPEN.ordinal() + " AND " + 
					"tp.scheduleStationCourierSection.originScheduleStation.scheduleStationCode.scheduleStationCode = '"+ tripCode +"' AND " +
					"tp.scheduleStationCourierSection.originScheduleStation.scheduleStationCode.courierEnabled = 1 AND " +
					"(tp.lockedDownExpiryDate IS NULL OR (ADDTIME(tp.lockedDownExpiryDate, "+(client.getLockDownInterval()*60)+") > CURRENT_TIMESTAMP)) ";
			//log.info"<<<<hql ---+++ = " + hql);
			Collection<CourierCabinShipment> vsaids = (Collection<CourierCabinShipment>)swpService.getAllRecordsByHQL(hql);
			Iterator<CourierCabinShipment> it2_ = vsaids.iterator();
			//log.info"<<<<hql size ---+++ = " + vsaids.size());
			while(it2_.hasNext())
			{
				//log.info"<<<<checker");
				CourierCabinShipment courierCabinShipment = it2_.next();
				ScheduleStationCourierSection sscs = courierCabinShipment.getScheduleStationCourierSection();
				//log.info"<<<<checker 1 = " + sscs.getSchedStatCourSectId());
				//sscs.setCurrentAvailableTonnage(sscs.getCurrentAvailableTonnage() + courierCabinShipment.getShipment().getParcelWeight());
				//sscs.setCurrentAvailableVolume(sscs.getCurrentAvailableVolume() + courierCabinShipment.getShipment().getParcelVolume());
				swpService.updateRecord(sscs);
				courierCabinShipment.setDeletedAt(new Date());
				swpService.updateRecord(courierCabinShipment);
			}
			
			hql = "Select tp FROM ScheduleStationCourierSection tp where tp.deletedAt IS NULL AND " + 
					"((tp.originScheduleStation.scheduleStationCode.scheduleStationCode = '"+ tripCode +"' AND " +
					"tp.originScheduleStation.scheduleStationId IN ("+ StringUtils.join(scheduleStationDepartures, ',')+")) OR " +
					"(tp.arrivalScheduleStation.scheduleStationCode.scheduleStationCode = '"+ tripCode +"' AND " +
					"tp.arrivalScheduleStation.scheduleStationId IN ("+ StringUtils.join(scheduleStationDepartures, ',')+"))) AND " + 
					"tp.client.clientCode = '"+clientCode+"'";
			//log.info"hql___ = " + hql);
			Collection<ScheduleStationCourierSection> scheduleStationCourierSections = (Collection<ScheduleStationCourierSection>)swpService.getAllRecordsByHQL(hql);
			//log.info"vsaids.size()....." + vsaids.size());
			//log.info"scheduleStationCourierSections.size()....." + (scheduleStationCourierSections==null?0:scheduleStationCourierSections.size()));
			if(scheduleStationCourierSections!=null && scheduleStationCourierSections.size()>0)
			{
				hql = "Select tp from ScheduleStation tp where tp.client.clientCode = '"+ clientCode + "' AND tp.deletedAt IS NULL " +
					"AND tp.scheduleStationCode.scheduleStationCode = '"+tripCode+"' AND " +
					"COALESCE(tp.arrivalTime, tp.departureTime) >= '" + departureScheduleStation.getDepartureTime() + "' AND " + 
					"COALESCE(tp.arrivalTime, tp.departureTime) <= '" + arrivalScheduleStation.getArrivalTime() + "' " +
					"AND tp.scheduleStationCode.scheduleStationCode = '"+ tripCode +"' " +
					"AND tp.scheduleStationCode.courierEnabled = 1 " + 
					"ORDER BY COALESCE(tp.arrivalTime, tp.departureTime)";
				//log.info"coalesce = " + hql);
				List<ScheduleStation> allScheduleStations = (List<ScheduleStation>)this.swpService.getAllRecordsByHQL(hql);
				//log.info"departure size = " + scheduleStationDepartures.size());
				
				int xy =0;
				JSONArray allTrips = new JSONArray();
				JSONObject jsonObjectEntry = new JSONObject();
				JSONObject js = null;
				Iterator<ScheduleStation> allScheduleStationIter = allScheduleStations.iterator();
				while(allScheduleStationIter.hasNext())
				{
					ScheduleStation scheduleStation = allScheduleStationIter.next();
					if(xy%2==0)
						js = new JSONObject();
					
					
					if(scheduleStation.getDepartureTime()!=null)
					{
						js.put("originStationCity", scheduleStation.getStation().getDistrict().getName());
						js.put("originStation", scheduleStation.getStation().getStationName());
						js.put("departureTime", scheduleStation.getDepartureTime());
						js.put("originStationCode", scheduleStation.getStation().getStationCode());
					}
					if(scheduleStation.getArrivalTime()!=null)
					{
						js.put("arrivalStationCity", scheduleStation.getStation().getDistrict().getName());
						js.put("arrivalStation", scheduleStation.getStation().getStationName());
						js.put("arrivalTime", scheduleStation.getArrivalTime());
						js.put("arrivalStationCode", scheduleStation.getStation().getStationCode());
					}
					
					if(xy%2==1)
					{
						allTrips.put(js);
						//log.info"##------------------------");
						//log.infojs.toString());
						//log.info"##------------------------");
					}
					xy++;
					
				}
				
				/**NOW CHECK FOR SPACE IN COMPARTMENTS*/
				hql = "Select tp FROM ScheduleStationCourierSection tp where tp.deletedAt IS NULL AND " + 
						"((tp.originScheduleStation.scheduleStationCode.scheduleStationCode = '"+ tripCode +"' AND " +
						"tp.originScheduleStation.scheduleStationId IN ("+ StringUtils.join(scheduleStationDepartures, ',')+")) OR " +
						"(tp.arrivalScheduleStation.scheduleStationCode.scheduleStationCode = '"+ tripCode +"' AND " +
						"tp.arrivalScheduleStation.scheduleStationId IN ("+ StringUtils.join(scheduleStationDepartures, ',')+"))) AND " + 
						"tp.client.clientCode = '"+clientCode+"'";
						//AND " + 
						//"tp.currentAvailableTonnage >= " + totalParcelWeight + " AND " + 
						//"tp.currentAvailableVolume >= " + totalParcelVolume;
				//log.info">>>>.hql ..." + hql);
				
				Collection<ScheduleStationCourierSection> scheduleStationCourierSectionsChecker = (Collection<ScheduleStationCourierSection>)swpService.getAllRecordsByHQL(hql);

				//log.info"scheduleStationCourierSectionsChecker.size() -- " + scheduleStationCourierSectionsChecker.size());
				//log.info"scheduleStationCourierSections.size() -- " + scheduleStationCourierSections.size());
				
				if(scheduleStationCourierSectionsChecker.size() < scheduleStationCourierSections.size())
				{
					jsonObject.add("status", ERROR.AVAILABLE_SEATS_NOT_FOUND);
					jsonObject.add("message", "Space for parcel not available from origin station to destination station");
					jsonObject.add("stationsWithAvailableSpaces", new Gson().toJson(scheduleStationCourierSectionsChecker));
					JsonObject jsonObj = jsonObject.build();
		            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
				}
				
				hql = "Select tp from ScheduleStationCode tp where tp.scheduleStationCode = '"+ tripCode +"' AND tp.deletedAt IS NULL";
				ScheduleStationCode scheduleStationCode = (ScheduleStationCode)swpService.getUniqueRecordByHQL(hql);
				if(scheduleStationCode==null)
				{
					jsonObject.add("status", ERROR.INVALID_TRIP_CODE_PROVIDED);
					jsonObject.add("message", "Space for parcel not available from origin station to destination station");
					jsonObject.add("stationsWithAvailableSpaces", new Gson().toJson(scheduleStationCourierSectionsChecker));
					JsonObject jsonObj = jsonObject.build();
		            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
				}
				
				hql = "Select tp from VehicleSchedule tp where tp.scheduleStationCode.scheduleStationCode = '"+tripCode+"'AND tp.deletedAt IS NULL " +
						"AND tp.client.clientCode = '"+clientCode+"'";
				VehicleSchedule vs = (VehicleSchedule)swpService.getUniqueRecordByHQL(hql);
				
				
				
				Shipment shipment = new Shipment();
				//shipment.setAppliedPricingIndex(appliedPricingIndex);
				//shipment.setAppliedPricingIndexDescription(appliedPricingIndexDescription);
				shipment.setClient(client);
				shipment.setCourierService(courierService);
				shipment.setProductCategory(productCategory);
				shipment.setCreatedAt(new Date());
				shipment.setDeliveredByUser(null);
				shipment.setDeliveryCharge(deliveryCharge);
				//shipment.setAdvicedDeliveryCharge(advicedDeliveryCharge);
				shipment.setDescription(description);
				//shipment.setDestinationCollectionPoint(destinationCollectionPoint);
				//shipment.setParcelHeight(totalParcelHeight);
				//shipment.setParcelName(parcelName);
				//shipment.setParcelQuantity(parcelQuantity);
				//shipment.setParcelVolume(totalParcelVolume);
				//shipment.setParcelWeight(totalParcelWeight);
				//shipment.setParcelWidth(totalParcelWidth);
				shipment.setVehicleSchedule(vs);
				shipment.setTrackingId(RandomStringUtils.randomNumeric(8));
				shipment.setUpdatedAt(new Date());
				shipment.setShipmentStatus(ShipmentStatus.PENDING);
				shipment.setDepartureScheduleStation(departureScheduleStation);
				shipment.setArrivalScheduleStation(arrivalScheduleStation);
				shipment = (Shipment)swpService.createNewRecord(shipment);
				
				
				Iterator<ScheduleStationCourierSection> sscscIt = scheduleStationCourierSectionsChecker.iterator();
				while(sscscIt.hasNext())
				{
					ScheduleStationCourierSection sscsc = sscscIt.next();
					CourierCabinShipment courierCabinShipment = new CourierCabinShipment();
					courierCabinShipment.setCourierAvailabilityStatus(CourierAvailabilityStatus.OPEN);
					courierCabinShipment.setCreatedAt(new Date());
					courierCabinShipment.setUpdatedAt(new Date());
					courierCabinShipment.setLockedDown(Boolean.TRUE);
					courierCabinShipment.setLockedDownBy(lockedDownBy);
					courierCabinShipment.setLockedDownExpiryDate(lockedDownExpiryDate);
					courierCabinShipment.setShipment(shipment);
					courierCabinShipment.setScheduleStationCourierSection(sscsc);
					courierCabinShipment.setClient(client);
					courierCabinShipment = (CourierCabinShipment)swpService.createNewRecord(courierCabinShipment);
					
					//sscsc.setCurrentAvailableTonnage(sscsc.getCurrentAvailableTonnage() - shipment.getParcelWeight());
					//sscsc.setCurrentAvailableVolume(sscsc.getCurrentAvailableVolume() - shipment.getParcelVolume());
					//swpService.updateRecord(sscsc);
					
					js = new JSONObject();
					js.put("originStation", courierCabinShipment.getScheduleStationCourierSection().getOriginScheduleStation().getStation().getStationName());
					js.put("originStationCode", courierCabinShipment.getScheduleStationCourierSection().getOriginScheduleStation().getStation().getStationCode());
					js.put("sectionName", courierCabinShipment.getScheduleStationCourierSection().getVehicleSeatSection().getSectionName());
					js.put("sectionCode", courierCabinShipment.getScheduleStationCourierSection().getVehicleSeatSection().getSectionCode());
					js.put("courierCabinShipmentId", courierCabinShipment.getCourierCabinShipmentId());
					js.put("shippingTrackingId", courierCabinShipment.getShipment().getTrackingId());
					
					JSONArray jsArray = null;
					String key = courierCabinShipment.getScheduleStationCourierSection().getOriginScheduleStation().getStation().getStationCode();
					if(!vehicleSeatsLockedDown.has(key))
					{
						jsArray = new JSONArray();
					}
					else
					{
						jsArray = vehicleSeatsLockedDown.getJSONArray(key);
						
					}
					jsArray.put(js);
					
					vehicleSeatsLockedDown.put(key, jsArray);
					
					
				}
				jsonObject.add("shippingTrackingId", shipment.getTrackingId());
				jsonObject.add("tripDetails", allTrips.toString());
				jsonObject.add("cabinSpacesLockedDown", vehicleSeatsLockedDown.toString());
				jsonObject.add("status", ERROR.GENERAL_SUCCESS);
				jsonObject.add("message", "Vehicle cabin space locked for payment");
				jsonObject.add("lifeSpan", client.getLockDownInterval());
			}
			else
			{
				jsonObject.add("message", "Vehicle cabin space could not be locked. No available spaces");
				jsonObject.add("status", ERROR.AVAILABLE_CABINS_COULD_NOT_BE_LOCKED_DOWN);
				jsonObject.add("lifeSpan", client.getLockDownInterval());
			}
			
			
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
	

	
	private Response getCourierCabinAllocationAndLockDown(Boolean lockdownSpace, String token, String tripCode, String departingStationCode, String arrivalStationCode, 
			CourierService courierService, JSONArray jsonTicketPrices, String requestId, String ipAddress, Client client, String tripToken, User user) {
		// TODO Auto-generated method stub
		JsonObjectBuilder jsonObject = Json.createObjectBuilder();
    	String lockedDownBy = null;
		try{
			
			if(departingStationCode==null || tripCode==null || arrivalStationCode==null)
			{
				jsonObject.add("status", ERROR.INCOMPLETE_PARAMETERS);
				jsonObject.add("message", "Incomplete Parameters provided in request.");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			if(lockdownSpace==null)
			{
				lockdownSpace= Boolean.FALSE;
			}

			
			swpService = serviceLocator.getSwpService();
			Application app = Application.getInstance(swpService);
			//JSONObject verifyJ = null;
			JSONObject verifyJ = UtilityHelper.verifyToken(token, app);
			String username = user.getUsername();
			//log.inforequestId + "username ==" + (username==null ? "" : username));
			String roleCode_ = verifyJ.has("roleCode") ? verifyJ.getString("roleCode") : null;
			//log.inforequestId + "roleCode_ ==" + (roleCode_==null ? "" : roleCode_));
			RoleType roleCode = null;
			
			if(roleCode_ != null)
			{
				roleCode = RoleType.valueOf(roleCode_);
			}
			
			if(username!=null)
			{
				user = (User)this.swpService.getUniqueRecordByHQL("select tp from User tp where tp.username = '" + username + "' AND tp.deletedAt IS NULL AND " +
						"tp.userStatus = " + UserStatus.ACTIVE.ordinal());
				
				if(user!=null)
				{

					if(tripToken==null)
					{
						JSONObject userDetails = new JSONObject();
						userDetails.put("username", user.getUsername() + "-" + RandomStringUtils.randomAlphabetic(10).toUpperCase());
						userDetails.put("roleCode", roleCode.name());
						//userDetails.put("priviledges", {});
						Gson gson = new Gson();
						String obj = gson.toJson(userDetails);
						String tkId = RandomStringUtils.randomAlphanumeric(10);
						tripToken = app.createJWT(tkId, client.getClientCode(), obj, (client.getLockDownInterval() *60*1000));
					}
				}
			}
			
			lockedDownBy = tripToken;
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.MINUTE, client.getLockDownInterval());
			Date lockedDownExpiryDate = cal.getTime();
			JSONObject vehicleSeatsLockedDown = new JSONObject();
			
			
			
			String hql = "Select tp from ScheduleStation tp where " + 
					"(tp.station.stationCode = '"+ departingStationCode +"') AND " + 
					"tp.scheduleStationCode.scheduleStationCode = '" + tripCode+"' " + 
					"AND tp.scheduleStationCode.courierEnabled = 1 " + 
					"AND tp.deletedAt is NULL AND " +
					"tp.arrivalTime IS NULL AND " + 
					"tp.client.clientCode = '"+client.getClientCode()+"'";
			ScheduleStation departureScheduleStation = (ScheduleStation)swpService.getUniqueRecordByHQL(hql);
			
			hql = "Select tp from ScheduleStation tp where " + 
					"(tp.station.stationCode = '"+arrivalStationCode+"') AND " + 
					"tp.departureTime IS NULL AND " + 
					"tp.scheduleStationCode.scheduleStationCode = '" + tripCode+"' " + 
					"AND tp.scheduleStationCode.courierEnabled = 1 " + 
					"AND tp.deletedAt is NULL AND tp.client.clientCode = '"+client.getClientCode()+"'";
			ScheduleStation arrivalScheduleStation = (ScheduleStation)swpService.getUniqueRecordByHQL(hql);
			if(departureScheduleStation==null || (arrivalScheduleStation==null))
			{
				jsonObject.add("status", ERROR.TRIP_NOT_FOUND);
				jsonObject.add("message", "The vehicle trip could not be found");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			
			
			
			hql = "Select tp.scheduleStationId from ScheduleStation tp where tp.client.clientCode = '"+ client.getClientCode() + "' AND tp.deletedAt IS NULL " +
				"AND tp.scheduleStationCode.scheduleStationCode = '"+tripCode+"' AND " +
				"COALESCE(tp.arrivalTime, tp.departureTime) >= '" + departureScheduleStation.getDepartureTime() + "' AND " + 
				"COALESCE(tp.arrivalTime, tp.departureTime) <= '" + arrivalScheduleStation.getArrivalTime() + "' " +
				"AND tp.scheduleStationCode.scheduleStationCode = '"+ tripCode +"' " +
				"AND tp.scheduleStationCode.courierEnabled = 1 " + 
				"ORDER BY COALESCE(tp.arrivalTime, tp.departureTime)";
			//log.info"coalesce = " + hql);
			List<Long> scheduleStationDepartures = (List<Long>)this.swpService.getAllRecordsByHQL(hql);
			//log.info"departure size = " + scheduleStationDepartures.size());
			
			
			if(scheduleStationDepartures==null || (scheduleStationDepartures!=null && scheduleStationDepartures.size()==0))
			{
				jsonObject.add("message", "Vehicle cabin space could not be locked. No available spaces");
				jsonObject.add("status", ERROR.AVAILABLE_CABINS_COULD_NOT_BE_LOCKED_DOWN);
				jsonObject.add("lifeSpan", client.getLockDownInterval());
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			hql = "Select tp FROM CourierCabinShipment tp WHERE tp.deletedAt IS NULL AND " +
					"tp.client.clientCode = '"+client.getClientCode()+"' AND " + 
					"tp.courierAvailabilityStatus = " + CourierAvailabilityStatus.OPEN.ordinal() + " AND " + 
					"tp.scheduleStationCourierSection.originScheduleStation.scheduleStationCode.scheduleStationCode = '"+ tripCode +"' AND " +
					"tp.scheduleStationCourierSection.originScheduleStation.scheduleStationCode.courierEnabled = 1 AND " +
					"(tp.lockedDownExpiryDate IS NULL OR (ADDTIME(tp.lockedDownExpiryDate, "+(client.getLockDownInterval()*60)+") > CURRENT_TIMESTAMP)) ";
			//log.info"<<<<hql ---+++ = " + hql);
			Collection<CourierCabinShipment> vsaids = (Collection<CourierCabinShipment>)swpService.getAllRecordsByHQL(hql);
			Iterator<CourierCabinShipment> it2_ = vsaids.iterator();
			//log.info"<<<<hql size ---+++ = " + vsaids.size());
			while(it2_.hasNext())
			{
				//log.info"<<<<checker");
				CourierCabinShipment courierCabinShipment = it2_.next();
				//ScheduleStationCourierSection sscs = courierCabinShipment.getScheduleStationCourierSection();
				////log.info"<<<<checker 1 = " + sscs.getSchedStatCourSectId());
				//sscs.setCurrentAvailableTonnage(sscs.getCurrentAvailableTonnage() + courierCabinShipment.getShipment().getParcelWeight());
				//sscs.setCurrentAvailableVolume(sscs.getCurrentAvailableVolume() + courierCabinShipment.getShipment().getParcelVolume());
				//swpService.updateRecord(sscs);
				courierCabinShipment.setDeletedAt(new Date());
				swpService.updateRecord(courierCabinShipment);
			}
			
			hql = "Select tp FROM ScheduleStationCourierSection tp where tp.deletedAt IS NULL AND " + 
					"((tp.originScheduleStation.scheduleStationCode.scheduleStationCode = '"+ tripCode +"' AND " +
					"tp.originScheduleStation.scheduleStationId IN ("+ StringUtils.join(scheduleStationDepartures, ',')+")) OR " +
					"(tp.arrivalScheduleStation.scheduleStationCode.scheduleStationCode = '"+ tripCode +"' AND " +
					"tp.arrivalScheduleStation.scheduleStationId IN ("+ StringUtils.join(scheduleStationDepartures, ',')+"))) AND " + 
					"tp.client.clientId = '"+client.getClientId()+"'";
			//log.info"hql___ = " + hql);
			Collection<ScheduleStationCourierSection> scheduleStationCourierSections = (Collection<ScheduleStationCourierSection>)swpService.getAllRecordsByHQL(hql);
			//log.info"vsaids.size()....." + vsaids.size());
			//log.info"scheduleStationCourierSections.size()....." + (scheduleStationCourierSections==null?0:scheduleStationCourierSections.size()));
			if(scheduleStationCourierSections!=null && scheduleStationCourierSections.size()>0)
			{
				hql = "Select tp from ScheduleStation tp where tp.client.clientId = '"+ client.getClientId() + "' AND tp.deletedAt IS NULL " +
					"AND tp.scheduleStationCode.scheduleStationCode = '"+tripCode+"' AND " +
					"COALESCE(tp.arrivalTime, tp.departureTime) >= '" + departureScheduleStation.getDepartureTime() + "' AND " + 
					"COALESCE(tp.arrivalTime, tp.departureTime) <= '" + arrivalScheduleStation.getArrivalTime() + "' " +
					"AND tp.scheduleStationCode.scheduleStationCode = '"+ tripCode +"' " +
					"AND tp.scheduleStationCode.courierEnabled = 1 " + 
					"ORDER BY COALESCE(tp.arrivalTime, tp.departureTime)";
				//log.info"coalesce = " + hql);
				List<ScheduleStation> allScheduleStations = (List<ScheduleStation>)this.swpService.getAllRecordsByHQL(hql);
				//log.info"departure size = " + scheduleStationDepartures.size());
				
				int xy =0;
				JSONArray allTrips = new JSONArray();
				JSONObject jsonObjectEntry = new JSONObject();
				JSONObject js = null;
				Iterator<ScheduleStation> allScheduleStationIter = allScheduleStations.iterator();
				while(allScheduleStationIter.hasNext())
				{
					ScheduleStation scheduleStation = allScheduleStationIter.next();
					if(xy%2==0)
						js = new JSONObject();
					
					
					if(scheduleStation.getDepartureTime()!=null)
					{
						js.put("originStationCity", scheduleStation.getStation().getDistrict().getName());
						js.put("originStation", scheduleStation.getStation().getStationName());
						js.put("departureTime", scheduleStation.getDepartureTime());
						js.put("originStationCode", scheduleStation.getStation().getStationCode());
					}
					if(scheduleStation.getArrivalTime()!=null)
					{
						js.put("arrivalStationCity", scheduleStation.getStation().getDistrict().getName());
						js.put("arrivalStation", scheduleStation.getStation().getStationName());
						js.put("arrivalTime", scheduleStation.getArrivalTime());
						js.put("arrivalStationCode", scheduleStation.getStation().getStationCode());
					}
					
					if(xy%2==1)
					{
						allTrips.put(js);
						//log.info"##------------------------");
						//log.infojs.toString());
						//log.info"##------------------------");
					}
					xy++;
					
				}
				
				/**NOW CHECK FOR SPACE IN COMPARTMENTS*/
				hql = "Select tp FROM ScheduleStationCourierSection tp where tp.deletedAt IS NULL AND " + 
						"((tp.originScheduleStation.scheduleStationCode.scheduleStationCode = '"+ tripCode +"' AND " +
						"tp.originScheduleStation.scheduleStationId IN ("+ StringUtils.join(scheduleStationDepartures, ',')+")) OR " +
						"(tp.arrivalScheduleStation.scheduleStationCode.scheduleStationCode = '"+ tripCode +"' AND " +
						"tp.arrivalScheduleStation.scheduleStationId IN ("+ StringUtils.join(scheduleStationDepartures, ',')+"))) AND " + 
						"tp.client.clientId = "+client.getClientId();
				//log.info">>>>.hql ..." + hql);
				Collection<ScheduleStationCourierSection> scheduleStationCourierSectionsChecker = (Collection<ScheduleStationCourierSection>)swpService.getAllRecordsByHQL(hql, 0, 1);
				
				//log.info"scheduleStationCourierSectionsChecker.size() --- " + scheduleStationCourierSectionsChecker.size());
				//log.info"scheduleStationCourierSections.size() --- " + scheduleStationCourierSections.size());
				//if(scheduleStationCourierSectionsChecker.size() < scheduleStationCourierSections.size())
				if(scheduleStationCourierSectionsChecker.size() ==0)
				{
					jsonObject.add("status", ERROR.AVAILABLE_SEATS_NOT_FOUND);
					jsonObject.add("message", "Space for parcel not available from origin station to destination station");
					jsonObject.add("stationsWithAvailableSpaces", new Gson().toJson(scheduleStationCourierSectionsChecker));
					JsonObject jsonObj = jsonObject.build();
		            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
				}
				
				hql = "Select tp from ScheduleStationCode tp where tp.scheduleStationCode = '"+ tripCode +"' AND tp.deletedAt IS NULL";
				ScheduleStationCode scheduleStationCode = (ScheduleStationCode)swpService.getUniqueRecordByHQL(hql);
				if(scheduleStationCode==null)
				{
					jsonObject.add("status", ERROR.INVALID_TRIP_CODE_PROVIDED);
					jsonObject.add("message", "Space for parcel not available from origin station to destination station");
					jsonObject.add("stationsWithAvailableSpaces", new Gson().toJson(scheduleStationCourierSectionsChecker));
					JsonObject jsonObj = jsonObject.build();
		            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
				}
				
				hql = "Select tp from VehicleSchedule tp where tp.scheduleStationCode.scheduleStationCode = '"+tripCode+"'AND tp.deletedAt IS NULL " +
						"AND tp.client.clientId = '"+client.getClientId()+"'";
				VehicleSchedule vs = (VehicleSchedule)swpService.getUniqueRecordByHQL(hql);
				
				Double totalAmount = 0.00;
				for(int i1=0; i1<jsonTicketPrices.length(); i1++)
				{
					
					JSONObject jsonTicket = jsonTicketPrices.getJSONObject(i1);
					ProductCategory productCategory = (ProductCategory)jsonTicket.get("productCategory");
					String description = jsonTicket.getString("description");
					Double amount = jsonTicket.getDouble("amount");
					String parcelName = jsonTicket.getString("parcelName");
					Integer parcelQuantity = jsonTicket.getInt("parcelQuantity");
					Double parcelWeight = jsonTicket.has("parcelWeight") ? jsonTicket.getDouble("parcelWeight") : 0.00;
					totalAmount = totalAmount + amount;
				}
				totalAmount = totalAmount + client.getBookingFee();
				
				//shipment.setAppliedPricingIndex(appliedPricingIndex);
				//shipment.setAppliedPricingIndexDescription(appliedPricingIndexDescription);
				//shipment.setAdvicedDeliveryCharge(advicedDeliveryCharge);
				//shipment.setDestinationCollectionPoint(destinationCollectionPoint);
				//shipment.setParcelHeight(totalParcelHeight);
				//shipment.setParcelVolume(totalParcelVolume);
				//shipment.setParcelWeight(totalParcelWeight);
				//shipment.setParcelWidth(totalParcelWidth);
				Shipment shipment = new Shipment();
				shipment.setClient(client);
				shipment.setCourierService(courierService);
				shipment.setCreatedAt(new Date());
				shipment.setUpdatedAt(new Date());
				shipment.setDeliveredByUser(null);
				shipment.setVehicleSchedule(vs);
				shipment.setTrackingId(RandomStringUtils.randomNumeric(8));
				shipment.setShipmentStatus(ShipmentStatus.PENDING);
				shipment.setDepartureScheduleStation(departureScheduleStation);
				shipment.setArrivalScheduleStation(arrivalScheduleStation);
				shipment.setDeliveryCharge(totalAmount);
				shipment = (Shipment)swpService.createNewRecord(shipment);
				
				for(int i1=0; i1<jsonTicketPrices.length(); i1++)
				{
					
					JSONObject jsonTicket = jsonTicketPrices.getJSONObject(i1);
					ProductCategory productCategory = (ProductCategory)jsonTicket.get("productCategory");
					String description = jsonTicket.getString("description");
					Double amount = jsonTicket.getDouble("amount");
					String parcelName = jsonTicket.getString("parcelName");
					Integer parcelQuantity = jsonTicket.getInt("parcelQuantity");
					Double parcelWeight = jsonTicket.has("parcelWeight") ? jsonTicket.getDouble("parcelWeight") : null;
					
					
					ShipmentItem shipmentItem = new ShipmentItem();
					shipmentItem.setClient(client);
					shipmentItem.setProductCategory(productCategory);
					shipmentItem.setDescription(description);
					shipmentItem.setDeliveryCharge(amount);
					shipmentItem.setParcelName(parcelName);
					shipmentItem.setParcelQuantity(parcelQuantity);
					shipmentItem.setParcelWeight(parcelWeight);
					shipmentItem.setShipment(shipment);
					shipmentItem.setCreatedAt(new Date());
					shipmentItem.setUpdatedAt(new Date());
					shipmentItem.setParcelSerialNo((i1 + 1));
					swpService.createNewRecord(shipmentItem);
				}
				
				
				Iterator<ScheduleStationCourierSection> sscscIt = scheduleStationCourierSectionsChecker.iterator();
				while(sscscIt.hasNext())
				{
					ScheduleStationCourierSection sscsc = sscscIt.next();
					CourierCabinShipment courierCabinShipment = new CourierCabinShipment();
					courierCabinShipment.setCourierAvailabilityStatus(CourierAvailabilityStatus.OPEN);
					courierCabinShipment.setCreatedAt(new Date());
					courierCabinShipment.setUpdatedAt(new Date());
					courierCabinShipment.setLockedDown(Boolean.TRUE);
					courierCabinShipment.setLockedDownBy(lockedDownBy);
					courierCabinShipment.setLockedDownExpiryDate(lockedDownExpiryDate);
					courierCabinShipment.setShipment(shipment);
					courierCabinShipment.setScheduleStationCourierSection(sscsc);
					courierCabinShipment.setClient(client);
					courierCabinShipment = (CourierCabinShipment)swpService.createNewRecord(courierCabinShipment);
					
					//sscsc.setCurrentAvailableTonnage(sscsc.getCurrentAvailableTonnage() - shipment.getParcelWeight());
					//sscsc.setCurrentAvailableVolume(sscsc.getCurrentAvailableVolume() - shipment.getParcelVolume());
					swpService.updateRecord(sscsc);
					
					js = new JSONObject();
					js.put("originStation", courierCabinShipment.getScheduleStationCourierSection().getOriginScheduleStation().getStation().getStationName());
					js.put("originStationCode", courierCabinShipment.getScheduleStationCourierSection().getOriginScheduleStation().getStation().getStationCode());
					js.put("sectionName", courierCabinShipment.getScheduleStationCourierSection().getVehicleSeatSection().getSectionName());
					js.put("sectionCode", courierCabinShipment.getScheduleStationCourierSection().getVehicleSeatSection().getSectionCode());
					js.put("courierCabinShipmentId", courierCabinShipment.getCourierCabinShipmentId());
					js.put("shippingTrackingId", courierCabinShipment.getShipment().getTrackingId());
					
					JSONArray jsArray = null;
					String key = courierCabinShipment.getScheduleStationCourierSection().getOriginScheduleStation().getStation().getStationCode();
					if(!vehicleSeatsLockedDown.has(key))
					{
						jsArray = new JSONArray();
					}
					else
					{
						jsArray = vehicleSeatsLockedDown.getJSONArray(key);
						
					}
					jsArray.put(js);
					
					vehicleSeatsLockedDown.put(key, jsArray);
					
					
				}
				jsonObject.add("shippingTrackingId", shipment.getTrackingId());
				jsonObject.add("tripDetails", allTrips.toString());
				jsonObject.add("cabinSpacesLockedDown", vehicleSeatsLockedDown.toString());
				jsonObject.add("status", ERROR.GENERAL_SUCCESS);
				jsonObject.add("message", "Vehicle cabin space locked for payment");
				jsonObject.add("lifeSpan", client.getLockDownInterval());
			}
			else
			{
				jsonObject.add("message", "Vehicle cabin space could not be locked. No available spaces");
				jsonObject.add("status", ERROR.AVAILABLE_CABINS_COULD_NOT_BE_LOCKED_DOWN);
				jsonObject.add("lifeSpan", client.getLockDownInterval());
			}
			
			
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
	

	
	public Response getCourierTripDetailsByTransactionRef(String deviceCode, String token, String orderRef, String requestId, String ipAddress, String clientCode) {
		// TODO Auto-generated method stub
		JsonObjectBuilder jsonObject = Json.createObjectBuilder();
    	
		try{
			
			if(deviceCode==null || orderRef==null || clientCode==null)
			{
				jsonObject.add("status", ERROR.INCOMPLETE_PARAMETERS);
				jsonObject.add("message", "Incomplete Parameters provided in request.");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			//log.info"transactionRefs = " + orderRef);
			
			
			
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
			
			JSONObject verifyJ = null;
			User user = null;
			if(token!=null)
			{
				verifyJ = UtilityHelper.verifyToken(token, app);
				if(verifyJ.length()==0 || (verifyJ.length()>0 && verifyJ.has("active") && verifyJ.getInt("active")==0))
				{
					jsonObject.add("status", ERROR.FORCE_LOGOUT_USER);
					jsonObject.add("message", "Your session has expired. Please log in again");
					JsonObject jsonObj = jsonObject.build();
		            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
				}
				
				String username = verifyJ.has("username") ? verifyJ.getString("username") : null;
				//log.inforequestId + "username ==" + (username==null ? "" : username));
				String roleCode_ = verifyJ.has("roleCode") ? verifyJ.getString("roleCode") : null;
				//log.inforequestId + "roleCode_ ==" + (roleCode_==null ? "" : roleCode_));
				//User user = null;
				RoleType roleCode = null;
				
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
			
			Client client=null;
			if(clientCode!=null)
			{
				hql = "Select tp from Client tp where tp.clientCode = '"+ clientCode + "' AND tp.deletedAt IS NULL";
				//log.infohql);
				client = (Client)this.swpService.getUniqueRecordByHQL(hql);
				if(client==null)
				{
					jsonObject.add("status", ERROR.INCOMPLETE_PARAMETERS);
					jsonObject.add("message", "The clientCode is invalid");
					JsonObject jsonObj = jsonObject.build();
		            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
				}
			}
			
			
			hql = "Select tp from Transaction tp where tp.orderRef = '"+orderRef+"' AND tp.deletedAt is NULL AND tp.client.clientCode = '"+clientCode+"'";
			Transaction transaction = (Transaction)swpService.getUniqueRecordByHQL(hql);
			if(transaction==null)
			{
				jsonObject.add("status", ERROR.TRANSACTION_NOT_FOUND);
				jsonObject.add("message", "The transaction could not be found");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			//log.infonew Gson().toJson(transaction));
			List<Long> ptIds = new ArrayList<Long>();
			
			hql = "Select tp from Shipment tp where tp.transaction.transactionId = " + transaction.getTransactionId() + " AND tp.deletedAt IS NULL " +
					"AND tp.client.clientCode = '"+clientCode+"'";
			Shipment shipment = (Shipment)swpService.getUniqueRecordByHQL(hql);
				
			
			
			if(shipment!=null)
			{
				hql = "Select tp from VehicleSchedule tp where tp.vehicleScheduleId = "+shipment.getVehicleSchedule().getVehicleScheduleId()+" AND " +
						"tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL";
				//log.infohql);
				VehicleSchedule vs = (VehicleSchedule)swpService.getUniqueRecordByHQL(hql);
				
				
				hql = "Select tp from CourierCabinShipment tp where tp.client.clientCode = '"+ clientCode + "' AND tp.deletedAt IS NULL " +
					"AND tp.shipment.shipmentId = "+ shipment.getShipmentId();
				//log.infohql);
				Collection<CourierCabinShipment> courierCabinShipments = (Collection<CourierCabinShipment>)this.swpService.getAllRecordsByHQL(hql);
				
				jsonObject.add("status", ERROR.GENERAL_SUCCESS);
				jsonObject.add("message", "Shipment order found for this trip");
				jsonObject.add("shipment", new Gson().toJson(shipment));
				jsonObject.add("transaction", new Gson().toJson(transaction));
				jsonObject.add("vehicleSchedule", new Gson().toJson(vs));
				jsonObject.add("courierCabinShipments", new Gson().toJson(courierCabinShipments));
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			else
			{
				jsonObject.add("status", ERROR.AVAILABLE_SEATS_NOT_FOUND);
				jsonObject.add("message", "Shipment order not found for this trip");
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
	

	
	
	public Response updateShipmentStatus(String shipmentStatus, String trackingDetails, String deviceCode, 
			String token, String trackingId, String requestId, String ipAddress, String clientCode) {
		// TODO Auto-generated method stub
		JsonObjectBuilder jsonObject = Json.createObjectBuilder();
    	
		try{
			
			if(deviceCode==null || trackingId==null || clientCode==null)
			{
				jsonObject.add("status", ERROR.INCOMPLETE_PARAMETERS);
				jsonObject.add("message", "Incomplete Parameters provided in request.");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			//log.info"trackingId = " + trackingId);
			
			
			
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
			
			ShipmentStatus ss = null;
			//log.infoshipmentStatus);
			try{
				ss = ShipmentStatus.valueOf(shipmentStatus);
				if(ss==null)
					//log.info"ss is null");
			}catch(IllegalArgumentException e)
			{
				jsonObject.add("status", ERROR.INVALID_SHIPMENT_STATUS_SELECTED);
				jsonObject.add("message", "Select a valid shipment status to update the shipments' status");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
				
			}
			
			JSONObject verifyJ = null;
			User user = null;
			RoleType roleCode = null;
			
			if(token!=null)
			{
				verifyJ = UtilityHelper.verifyToken(token, app);
				if(verifyJ.length()==0 || (verifyJ.length()>0 && verifyJ.has("active") && verifyJ.getInt("active")==0))
				{
					jsonObject.add("status", ERROR.FORCE_LOGOUT_USER);
					jsonObject.add("message", "Your session has expired. Please log in again");
					JsonObject jsonObj = jsonObject.build();
		            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
				}
				
				String username = verifyJ.has("username") ? verifyJ.getString("username") : null;
				//log.inforequestId + "username ==" + (username==null ? "" : username));
				String roleCode_ = verifyJ.has("roleCode") ? verifyJ.getString("roleCode") : null;
				//log.inforequestId + "roleCode_ ==" + (roleCode_==null ? "" : roleCode_));
				
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
			
			if(user==null || (roleCode!=null && !roleCode.equals(RoleType.OPERATOR)))
			{
				jsonObject.add("status", ERROR.INVALID_SHIPPING_STATUS_UPDATE_PRIVILEDGES);
				jsonObject.add("message", "Invalid Priviledges");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			Client client=null;
			if(clientCode!=null)
			{
				hql = "Select tp from Client tp where tp.clientCode = '"+ clientCode + "' AND tp.deletedAt IS NULL";
				//log.infohql);
				client = (Client)this.swpService.getUniqueRecordByHQL(hql);
				if(client==null)
				{
					jsonObject.add("status", ERROR.INCOMPLETE_PARAMETERS);
					jsonObject.add("message", "The clientCode is invalid");
					JsonObject jsonObj = jsonObject.build();
		            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
				}
			}
			
			
			
			hql = "Select tp from Shipment tp where tp.trackingId = '" + trackingId + "' AND tp.deletedAt IS NULL " +
					"AND tp.client.clientCode = '"+clientCode+"'";
			Shipment shipment = (Shipment)swpService.getUniqueRecordByHQL(hql);
			if(shipment==null)
			{
				jsonObject.add("status", ERROR.INVALID_TRACKING_ID);
				jsonObject.add("message", "Invalid tracking Id Provided");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			
			shipment.setShipmentStatus(ss);
			swpService.updateRecord(shipment);
				
			
			
			if(shipment!=null)
			{
				CourierTrackHistory cth = new CourierTrackHistory();
				cth.setClient(client);
				cth.setCreatedAt(new Date());
				cth.setUpdatedAt(new Date());
				cth.setShipment(shipment);
				cth.setShipmentStatus(ss);
				cth.setTrackingDetails(trackingDetails);
				cth.setUpdateByUser(user);
				cth = (CourierTrackHistory)swpService.createNewRecord(cth);
				
				AuditTrail ad = UtilityHelper.createAuditTrailEntry(ipAddress, RequestType.SHIPMENT_STATUS_UPDATE, requestId, this.swpService, 
						verifyJ.has("username") ? verifyJ.getString("username") : null, shipment.getShipmentId(), Shipment.class.getName(), 
						"Shipment status updated. Shipment Receipt: " + shipment.getReceiptNo() + " | Updated By " + user.getFirstName() + " " + user.getLastName(), clientCode);
				
				jsonObject.add("status", ERROR.GENERAL_SUCCESS);
				jsonObject.add("message", "Shipment updated successfully");
				jsonObject.add("shipment", new Gson().toJson(shipment));
				jsonObject.add("courierTrackingHistory", new Gson().toJson(cth));
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			else
			{
				jsonObject.add("status", ERROR.SHIPMENT_UPDATE_NOT_SUCCESSFUL);
				jsonObject.add("message", "Shipment update was not successful");
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
}