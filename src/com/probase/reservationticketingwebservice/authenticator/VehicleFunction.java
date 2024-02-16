package com.probase.reservationticketingwebservice.authenticator;


import java.text.DecimalFormat;
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
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.security.auth.login.LoginException;
import javax.ws.rs.FormParam;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.exception.SQLGrammarException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mindrot.jbcrypt.BCrypt;

import android.R.integer;

import com.google.gson.Gson;
import com.probase.reservationticketingwebservice.enumerations.CabinType;
import com.probase.reservationticketingwebservice.enumerations.CardStatus;
import com.probase.reservationticketingwebservice.enumerations.CardType;
import com.probase.reservationticketingwebservice.enumerations.CouponStatus;
import com.probase.reservationticketingwebservice.enumerations.CustomerStatus;
import com.probase.reservationticketingwebservice.enumerations.DeviceStatus;
import com.probase.reservationticketingwebservice.enumerations.DeviceType;
import com.probase.reservationticketingwebservice.enumerations.GroupTripRequestStatus;
import com.probase.reservationticketingwebservice.enumerations.PassengerType;
import com.probase.reservationticketingwebservice.enumerations.PaymentMeans;
import com.probase.reservationticketingwebservice.enumerations.PriceType;
import com.probase.reservationticketingwebservice.enumerations.PurchasePoint;
import com.probase.reservationticketingwebservice.enumerations.PurchasedTripStatus;
import com.probase.reservationticketingwebservice.enumerations.RequestType;
import com.probase.reservationticketingwebservice.enumerations.RoleType;
import com.probase.reservationticketingwebservice.enumerations.SeatAvailabilityStatus;
import com.probase.reservationticketingwebservice.enumerations.ServiceType;
import com.probase.reservationticketingwebservice.enumerations.Settings;
import com.probase.reservationticketingwebservice.enumerations.TransactionCurrency;
import com.probase.reservationticketingwebservice.enumerations.TransactionStatus;
import com.probase.reservationticketingwebservice.enumerations.TripCardChargeMode;
import com.probase.reservationticketingwebservice.enumerations.TripType;
import com.probase.reservationticketingwebservice.enumerations.UserStatus;
import com.probase.reservationticketingwebservice.enumerations.VehicleSeatLocation;
import com.probase.reservationticketingwebservice.enumerations.VehicleStatus;
import com.probase.reservationticketingwebservice.enumerations.VehicleTripStatus;
import com.probase.reservationticketingwebservice.enumerations.VehicleType;
import com.probase.reservationticketingwebservice.models.AuditTrail;
import com.probase.reservationticketingwebservice.models.Bank;
import com.probase.reservationticketingwebservice.models.CardScheme;
import com.probase.reservationticketingwebservice.models.Client;
import com.probase.reservationticketingwebservice.models.Country;
import com.probase.reservationticketingwebservice.models.Coupon;
import com.probase.reservationticketingwebservice.models.Customer;
import com.probase.reservationticketingwebservice.models.Device;
import com.probase.reservationticketingwebservice.models.District;
import com.probase.reservationticketingwebservice.models.GroupTripRequest;
import com.probase.reservationticketingwebservice.models.Line;
import com.probase.reservationticketingwebservice.models.PurchasedTrip;
import com.probase.reservationticketingwebservice.models.PurchasedTripSeat;
import com.probase.reservationticketingwebservice.models.ScheduleStation;
import com.probase.reservationticketingwebservice.models.ScheduleStationSeatAvailability;
import com.probase.reservationticketingwebservice.models.Shipment;
import com.probase.reservationticketingwebservice.models.Station;
import com.probase.reservationticketingwebservice.models.TicketCollectionPoint;
import com.probase.reservationticketingwebservice.models.TripCard;
import com.probase.reservationticketingwebservice.models.Transaction;
import com.probase.reservationticketingwebservice.models.TripZone;
import com.probase.reservationticketingwebservice.models.TripZoneStation;
import com.probase.reservationticketingwebservice.models.UpgradedPurchasedTrip;
import com.probase.reservationticketingwebservice.models.User;
import com.probase.reservationticketingwebservice.models.UserRole;
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

public final class VehicleFunction {

    private static VehicleFunction authenticator = null;

    // A user storage which stores <username, password>
    private final Map<String, String> usersStorage = new HashMap();

    // A service key storage which stores <service_key, username>
    private final Map<String, String> serviceKeysStorage = new HashMap();

    // An authentication token storage which stores <service_key, auth_token>.
    private final Map<String, String> authorizationTokensStorage = new HashMap();
    
    private static Logger log = Logger.getLogger(VehicleFunction.class);
	private ServiceLocator serviceLocator = null;
	public SwpService swpService = null;
	public PrbCustomService swpCustomService = PrbCustomService.getInstance();
	Application application = null;
	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private VehicleFunction() {
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

    public static VehicleFunction getInstance() {
        if ( authenticator == null ) {
            authenticator = new VehicleFunction();
        }

        return authenticator;
    }
    


	public Response createNewLine(String token, String lineName,
			String lineCode, String requestId, String ipAddress,
			String clientCode, String editLineCode) {
		// TODO Auto-generated method stub
		JsonObjectBuilder jsonObject = Json.createObjectBuilder();
    	
		try{
			
			if(lineName==null || lineCode==null || clientCode==null)
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
			
			Line line = new Line();
			RequestType rt = null;
			String msg = "";
			String msg_1 = "";
			
			if(editLineCode!=null)
			{
				String hql = "Select tp from Line tp where tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL AND lower(tp.lineCode) = '"+editLineCode.toLowerCase()+"'";
				//log.info"hql...." + hql);
				line = (Line)swpService.getUniqueRecordByHQL(hql);
				if(line==null)
				{
					jsonObject.add("status", ERROR.TRIP_LINE_DOES_NOT_EXIST);
					jsonObject.add("message", "No trip line exists matching the line code provided");
					JsonObject jsonObj = jsonObject.build();
		            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
				}
				
				hql = "Select tp from Line tp where tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL AND lower(tp.lineCode) = '"+lineCode.toLowerCase()+"'";
				//log.info"hql...." + hql);
				Line line1 = (Line)swpService.getUniqueRecordByHQL(hql);
				if(line1!=null && !line1.getLineId().equals(line.getLineId()))
				{
					jsonObject.add("status", ERROR.TRIP_LINE_CODE_EXISTS);
					jsonObject.add("message", "Trip line code provided already belongs to another trip line");
					JsonObject jsonObj = jsonObject.build();
		            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
				}
				rt = RequestType.UPDATE_LINE;
				msg = "Trip Line updated successfully";
				msg_1 = "Trip Line Updated. Trip Line: " + line1.getLineName() + " | Updated By: " + user.getFirstName() + " " + user.getLastName();
				line.setUpdatedAt(new Date());
				line.setLineName(lineName);
				this.swpService.updateRecord(line);
			}
			else
			{
				String hql = "Select tp from Line tp where tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL AND lower(tp.lineCode) = '"+lineCode.toLowerCase()+"'";
				line = (Line)swpService.getUniqueRecordByHQL(hql);
				if(line!=null)
				{
					jsonObject.add("status", ERROR.TRIP_LINE_CODE_EXISTS);
					jsonObject.add("message", "Trip line code provided already belongs to another trip line");
					JsonObject jsonObj = jsonObject.build();
		            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
				}
				line = new Line();
				line.setCreatedAt(new Date());
				line.setLineCode(lineCode);
				line.setLineStatus(Boolean.TRUE);
				line.setClient(client);
				line.setUpdatedAt(new Date());
				line.setLineName(lineName);
				line = (Line)this.swpService.createNewRecord(line);
				rt = RequestType.NEW_LINE_CREATION;
				msg = "Trip Line created successfully";
				msg_1 = "New Trip Line. Trip Line: " + line.getLineName() + " | Created By: " + user.getFirstName() + " " + user.getLastName();
			}
			
			
			
			AuditTrail ad = UtilityHelper.createAuditTrailEntry(ipAddress, rt, requestId, this.swpService, 
					verifyJ.has("username") ? verifyJ.getString("username") : null, line.getLineId(), Line.class.getName(), msg_1, clientCode);
			
			
			jsonObject.add("message", msg);
			jsonObject.add("status", ERROR.GENERAL_SUCCESS);
			jsonObject.add("line", new Gson().toJson(line));
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
	
	public Response listLines(Integer startIndex, Integer limit, String token, String requestId, String clientCode) {
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
			
			
			
			
			String hql = "Select tp from Line tp where tp.deletedAt IS NULL";
			String sql = "";
			hql = hql + sql;
			Collection<Line> lines= (Collection<Line>)this.swpService.getAllRecordsByHQL(hql, startIndex, limit);
			
			String hql1 = "Select count(tp.id) as idCount from Line tp WHERE tp.deletedAt IS NULL";
			//log.info"4.hql ==" + hql1);
			List<Long> totalLineCount = (List<Long>)swpService.getAllRecordsByHQL(hql1);
			Long totalLines = totalLineCount!=null ? totalLineCount.iterator().next() : 0;
			//log.info"totalLineCount ==" + totalLines);
			
			
			jsonObject.add("message", "Trip lines listed successfully");
			jsonObject.add("status", ERROR.GENERAL_SUCCESS);
			jsonObject.add("tripLineList", new Gson().toJson(lines));
			jsonObject.add("totalLineCount", totalLines);
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

	
	
	public Response createNewVehicle(String token, String editVehicleCode, String vehicleName, String inventoryNumber,
		    String manufacturer, String speed, String vehicleType, String requestId, String ipAddress, String clientCode, Double maximumFuelCapacity, String trainNumber) {
		// TODO Auto-generated method stub
		JsonObjectBuilder jsonObject = Json.createObjectBuilder();
    	
		try{
			
			if(vehicleName==null || inventoryNumber==null || vehicleType==null || clientCode==null || trainNumber==null)
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
			
			VehicleType vehicleType_ = VehicleType.valueOf(vehicleType);
			Vehicle vehicle  = new Vehicle();
			String msg = "";
			String msg_1 = "";
			RequestType rt = RequestType.NEW_VEHICLE_CREATION;
			if(editVehicleCode!=null)
			{
				String hql = "Select tp from Vehicle tp where tp.vehicleCode = '"+editVehicleCode+"' AND tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt is NULL";
				vehicle = (Vehicle)swpService.getUniqueRecordByHQL(hql);
				if(vehicle==null)
				{
					jsonObject.add("status", ERROR.VEHICLE_NOT_FOUND);
					jsonObject.add("message", "No vehicle with the vehicle code provided.");
					JsonObject jsonObj = jsonObject.build();
		            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
				}
				msg = "Vehicle(s) updated successfully";
				msg_1 = "New Vehicle Created. Vehicle Name: " + vehicle.getVehicleName() + " | Created By: " + user.getFirstName() + " " + user.getLastName();
				rt  = RequestType.NEW_VEHICLE_UPDATE;
			}
			else
			{
				String hql = "Select tp from Vehicle tp where tp.vehicleCode = '"+trainNumber.trim() +"' AND tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt is NULL";
				vehicle = (Vehicle)swpService.getUniqueRecordByHQL(hql);
				if(vehicle!=null)
				{
					jsonObject.add("status", ERROR.VEHICLE_NOT_FOUND);
					jsonObject.add("message", "Train number provided already belongs to another train");
					JsonObject jsonObj = jsonObject.build();
		            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
				}
				vehicle  = new Vehicle();
				msg = "Vehicle(s) generated successfully";
				vehicle.setCreatedAt(new Date());
				vehicle.setClient(client);
				vehicle.setVehicleStatus(VehicleStatus.ACTIVE);
			}
			vehicle.setVehicleCode(trainNumber.toUpperCase());
			vehicle.setUpdatedAt(new Date());
			vehicle.setManufacturer(manufacturer);
			vehicle.setSpeed(speed);
			vehicle.setVehicleName(vehicleName);
			vehicle.setVehicleType(vehicleType_);
			vehicle.setInventoryNumber(inventoryNumber);
			vehicle.setMaximumFuelCapacity(maximumFuelCapacity);
			if(editVehicleCode!=null)
				this.swpService.updateRecord(vehicle);
			else
				vehicle = (Vehicle)this.swpService.createNewRecord(vehicle);
			
			
			AuditTrail ad = UtilityHelper.createAuditTrailEntry(ipAddress, rt, requestId, this.swpService, 
					verifyJ.has("username") ? verifyJ.getString("username") : null, vehicle.getVehicleId(), Vehicle.class.getName(), msg_1, clientCode);
			
			
			jsonObject.add("message", msg);
			jsonObject.add("status", ERROR.GENERAL_SUCCESS);
			jsonObject.add("vehicle", new Gson().toJson(vehicle));
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


	
	public Response createNewVehicleSeatSection(String token, String sectionNames, String requestId, String ipAddress, String clientCode) {
		// TODO Auto-generated method stub
		JsonObjectBuilder jsonObject = Json.createObjectBuilder();
    	
		try{
			
			if(sectionNames==null || clientCode==null)
			{
				jsonObject.add("status", ERROR.INCOMPLETE_PARAMETERS);
				jsonObject.add("message", "Incomplete Parameters provided in request.");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			//log.info"sectionNames = " + sectionNames);
			JSONArray seatSections = new JSONArray(sectionNames);
			
			
			
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
			
			for(int i=0; i< seatSections.length(); i++)
			{
				////log.info(JSONObject)seatSections.get(i)).toString());
				String cabinType = ((JSONObject)seatSections.get(i)).getString("cabinType");
				//log.info"cabinType == " + cabinType);
				if(cabinType.equals(CabinType.COURIER.name()))
				{
					String sectionName = ((JSONObject)seatSections.get(i)).getString("sectionName");
					//log.info"sectionName == " + sectionName);
					Double maxTonnage = ((JSONObject)seatSections.get(i)).getDouble("maxTonnage");
					//log.info"maxTonnage == " + maxTonnage);
					String hql = "Select tp from VehicleSeatSection tp where tp.client.clientCode = '"+ clientCode + "' AND tp.deletedAt IS NULL AND (tp.sectionName = '"+sectionName+"')";
					//log.infohql);
					VehicleSeatSection vehicleSeatSection = (VehicleSeatSection)this.swpService.getUniqueRecordByHQL(hql);
					Station currentStation = null;
					Station destinationStation = null;
					
					if(vehicleSeatSection==null)
					{
						vehicleSeatSection = new VehicleSeatSection();
						vehicleSeatSection.setCreatedAt(new Date());
						vehicleSeatSection.setUpdatedAt(new Date());
						vehicleSeatSection.setClient(client);
						vehicleSeatSection.setCabinType(CabinType.COURIER);
						vehicleSeatSection.setSectionName(sectionName);
						vehicleSeatSection.setSectionCode(RandomStringUtils.randomAlphabetic(8).toUpperCase());
						vehicleSeatSection.setCurrentStation(currentStation);
						vehicleSeatSection.setDestinationStation(destinationStation);
						vehicleSeatSection.setMaxTonnage(maxTonnage);
						vehicleSeatSection = (VehicleSeatSection)this.swpService.createNewRecord(vehicleSeatSection);
						AuditTrail ad = UtilityHelper.createAuditTrailEntry(ipAddress, RequestType.NEW_SECTION_CREATION, requestId, this.swpService, 
								verifyJ.has("username") ? verifyJ.getString("username") : null, vehicleSeatSection.getVehicleSeatSectionId(), VehicleSeatSection.class.getName(), 
								"New Vehicle Seat Section. Section Name: " + sectionName + " | Created By " + user.getFirstName() + " " + user.getLastName(), clientCode);
						jsonArray.put(vehicleSeatSection);
					}
				}
				else if(cabinType.equals(CabinType.PASSENGER.name()))
				{
					String sectionName = ((JSONObject)seatSections.get(i)).getString("sectionName");
					//log.info"sectionName == " + sectionName);
					int maxSeatingCapacity = ((JSONObject)seatSections.get(i)).getInt("maxSeatingCapacity");
					//log.info"maxSeatingCapacity == " + maxSeatingCapacity);
					Integer maxStandingCapacity = ((JSONObject)seatSections.get(i)).has("maxStandingCapacity") ? ((JSONObject)seatSections.get(i)).getInt("maxStandingCapacity") : 0;
					//log.info"maxStandingCapacity == " + maxStandingCapacity);
					String sectionSeatClass = ((JSONObject)seatSections.get(i)).getString("sectionSeatClass");
					//log.info"sectionSeatClass == " + sectionSeatClass);
					JSONArray vehicleSeats = ((JSONObject)seatSections.get(i)).getJSONArray("vehicleSeats");
					//log.info"vehicleSeats == " + vehicleSeats);
					String hql = "Select tp from VehicleSeatSection tp where tp.client.clientCode = '"+ clientCode + "' AND tp.deletedAt IS NULL AND (tp.sectionName = '"+sectionName+"')";
					//log.infohql);
					VehicleSeatSection vehicleSeatSection = (VehicleSeatSection)this.swpService.getUniqueRecordByHQL(hql);
					hql = "Select tp from VehicleSeatClass tp where tp.client.clientCode = '"+ clientCode + "' AND tp.deletedAt IS NULL AND (tp.vehicleSeatClassCode = '"+sectionSeatClass+"')";
					//log.infohql);
					VehicleSeatClass vehicleSeatClass = (VehicleSeatClass)this.swpService.getUniqueRecordByHQL(hql);
					Station currentStation = null;
					Station destinationStation = null;
					
					if(vehicleSeatSection==null)
					{
						vehicleSeatSection = new VehicleSeatSection();
						vehicleSeatSection.setCreatedAt(new Date());
						vehicleSeatSection.setUpdatedAt(new Date());
						vehicleSeatSection.setClient(client);
						vehicleSeatSection.setCabinType(CabinType.PASSENGER);
						vehicleSeatSection.setSectionName(sectionName);
						vehicleSeatSection.setSectionCode(RandomStringUtils.randomAlphabetic(8).toUpperCase());
						vehicleSeatSection.setVehicleSeatClass(vehicleSeatClass);
						vehicleSeatSection.setCurrentStation(currentStation);
						vehicleSeatSection.setDestinationStation(destinationStation);
						vehicleSeatSection.setMaxSeatingCapacity(maxSeatingCapacity);
						vehicleSeatSection.setMaxStandingCapacity(maxStandingCapacity);
						vehicleSeatSection.setStandingAllowed(maxStandingCapacity==0 ? Boolean.FALSE : Boolean.TRUE);
						vehicleSeatSection = (VehicleSeatSection)this.swpService.createNewRecord(vehicleSeatSection);
						AuditTrail ad = UtilityHelper.createAuditTrailEntry(ipAddress, RequestType.UPDATE_VEHICLE_SECTION, requestId, this.swpService, 
								verifyJ.has("username") ? verifyJ.getString("username") : null, vehicleSeatSection.getVehicleSeatSectionId(), VehicleSeatSection.class.getName(), 
								"Update Vehicle Seat Section. Section Name: " + sectionName + " | Updated By: " + user.getFirstName() + " " + user.getLastName(), clientCode);
						jsonArray.put(vehicleSeatSection);
						
						//JSONArray vehicleSeatArray = new JSONArray(vehicleSeats);
						JSONObject jsSeats = new JSONObject();
						jsSeats.put("sectionCode", vehicleSeatSection.getSectionCode());
						jsSeats.put("sectionSeats", vehicleSeats);
						createNewVehicleSeat(token, jsSeats.toString(), requestId, ipAddress, clientCode);
					}
				}
			}
			
			
			
			
			
			jsonObject.add("message", "Vehicle seat section generated successfully");
			jsonObject.add("status", ERROR.GENERAL_SUCCESS);
			jsonObject.add("vehicleSeatSections", new Gson().toJson(jsonArray));
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
	
	
	
	public Response createNewVehicleSeat(String token, String vehicleSeats, String requestId, String ipAddress, String clientCode) {
		// TODO Auto-generated method stub
		JsonObjectBuilder jsonObject = Json.createObjectBuilder();
    	
		try{
			
			if(vehicleSeats==null || clientCode==null)
			{
				jsonObject.add("status", ERROR.INCOMPLETE_PARAMETERS);
				jsonObject.add("message", "Incomplete Parameters provided in request.");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			JSONObject allVehicleSeats = new JSONObject(vehicleSeats);
			
			
			
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
			
			
			String sectionCode = allVehicleSeats.getString("sectionCode");
			JSONArray sectionSeats = allVehicleSeats.getJSONArray("sectionSeats");
			String hql = "Select tp from VehicleSeatSection tp where tp.client.clientCode = '"+ clientCode + "' AND tp.deletedAt IS NULL AND tp.sectionCode = '"+sectionCode+"'";
			//log.infohql);
			VehicleSeatSection vehicleSeatSection = (VehicleSeatSection)this.swpService.getUniqueRecordByHQL(hql);
			if(vehicleSeatSection==null)
			{
				jsonObject.add("status", ERROR.INCOMPLETE_PARAMETERS);
				jsonObject.add("message", "The vehicle seat section code is invalid");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			for(int i=0; i<sectionSeats.length(); i++)
			{
				String seatNumber = ((JSONObject)sectionSeats.get(i)).getString("seatNumber");
				Boolean tripSeatFacing = ((JSONObject)sectionSeats.get(i)).getBoolean("tripSeatFacing");
				String vehicleSeatLocation = ((JSONObject)sectionSeats.get(i)).getString("vehicleSeatLocation");
				Integer seatOrder = ((JSONObject)sectionSeats.get(i)).getInt("seatOrder");
				
				VehicleSeatLocation vehicleSeatLocation_ = VehicleSeatLocation.valueOf(vehicleSeatLocation);
				VehicleSeat vehicleSeat = new VehicleSeat();
				vehicleSeat.setCabinName(vehicleSeatSection.getSectionName());
				vehicleSeat.setSeatNumber(seatNumber);
				vehicleSeat.setTripSeatFacing(tripSeatFacing);
				vehicleSeat.setVehicleSeatLocation(vehicleSeatLocation_);
				vehicleSeat.setCreatedAt(new Date());
				vehicleSeat.setUpdatedAt(new Date());
				vehicleSeat.setClient(client);
				vehicleSeat.setVehicleSeatSection(vehicleSeatSection);
				vehicleSeat.setSeatOrder(seatOrder);
				vehicleSeat = (VehicleSeat)this.swpService.createNewRecord(vehicleSeat);
				AuditTrail ad = UtilityHelper.createAuditTrailEntry(ipAddress, RequestType.NEW_SECTION_SEAT_CREATION, requestId, this.swpService, 
						verifyJ.has("username") ? verifyJ.getString("username") : null, vehicleSeat.getVehicleSeatId(), VehicleSeat.class.getName(), 
						"New Vehicle Seat. Seat Number: " + seatNumber + " | Cabin Class: " + vehicleSeatSection.getSectionName(), clientCode);
				
				
			}
			
			
			
			
			jsonObject.add("message", "Vehicle seat section generated successfully");
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


	
	
	

	
	
	public Response listVehicles(Integer startIndex, Integer limit, String token, String requestId, String clientCode) {
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
			
			
			
			
			String hql = "Select tp from Vehicle tp where tp.deletedAt IS NULL";
			String sql = "";
			hql = hql + sql;
			Collection<Vehicle> vehicles= (Collection<Vehicle>)this.swpService.getAllRecordsByHQL(hql, startIndex, limit);
			Iterator<Vehicle> vehicleIterator = vehicles.iterator();
			
			String hql1 = "Select count(tp.id) as idCount from Vehicle tp WHERE tp.deletedAt IS NULL";
			//log.info"4.hql ==" + hql1);
			List<Long> totalVehicleCount = (List<Long>)swpService.getAllRecordsByHQL(hql1);
			Long totalVehicles = totalVehicleCount!=null ? totalVehicleCount.iterator().next() : 0;
			//log.info"totalVehicleCount ==" + totalVehicles);
			
			
			jsonObject.add("message", "Vehicles listed successfully");
			jsonObject.add("status", ERROR.GENERAL_SUCCESS);
			jsonObject.add("vehicleList", new Gson().toJson(vehicles));
			jsonObject.add("totalVehicleCount", totalVehicles);
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
	
	
	
	public Response listGroupTripRequests(String token, String requestId, String clientCode) {
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
			
			
			
			
			String hql = "Select tp from GroupTripRequest tp where tp.deletedAt IS NULL";
			String sql = "";
			hql = hql + sql;
			Collection<GroupTripRequest> groupTripRequests= (Collection<GroupTripRequest>)this.swpService.getAllRecordsByHQL(hql);
			Iterator<GroupTripRequest> groupTripRequestIterator = groupTripRequests.iterator();
			
			
			
			jsonObject.add("message", "Group trip requests listed successfully");
			jsonObject.add("status", ERROR.GENERAL_SUCCESS);
			jsonObject.add("groupTripRequestList", new Gson().toJson(groupTripRequests));
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
	
	
	
	public Response getGroupTripRequest(String token, String requestId, String clientCode, String requestCode) {
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
			
			
			
			
			String hql = "Select tp from GroupTripRequest tp where tp.requestCode = '"+ requestCode +"' AND tp.deletedAt IS NULL";
			String sql = "";
			hql = hql + sql;
			GroupTripRequest groupTripRequest= (GroupTripRequest)this.swpService.getUniqueRecordByHQL(hql);
			
			if(groupTripRequest==null)
			{
				jsonObject.add("message", "Group trip request not found");
				jsonObject.add("status", ERROR.GROUP_TRIP_REQUEST_NOT_FOUND);
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			jsonObject.add("message", "Group trip request found");
			jsonObject.add("status", ERROR.GENERAL_SUCCESS);
			jsonObject.add("groupTripRequest", new Gson().toJson(groupTripRequest));
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

	
	
	public Response getVehicle(String vehicleCode, String token, String requestId, String clientCode) {
		// TODO Auto-generated method stub
		JsonObjectBuilder jsonObject = Json.createObjectBuilder();
    	
    	
		try{
			
			if(vehicleCode==null || clientCode==null)
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
			
			
			String hql = "Select tp from Vehicle tp WHERE tp.deletedAt IS NULL AND tp.vehicleCode = '" + vehicleCode + "' AND tp.client.clientCode = '" + clientCode + "'";
			Vehicle vehicle= (Vehicle)this.swpService.getUniqueRecordByHQL(hql);
			jsonObject.add("message", "Vehicle found");
			jsonObject.add("status", ERROR.GENERAL_SUCCESS);
			jsonObject.add("vehicle", new Gson().toJson(vehicle));
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
	
	
	public Response updateVehicleStatus(String vehicleCode, String status, String token, String requestId, String clientCode) {
		// TODO Auto-generated method stub
		JsonObjectBuilder jsonObject = Json.createObjectBuilder();
    	
    	
		try{
			
			if(vehicleCode==null || clientCode==null)
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
			
			
			String hql = "Select tp from Vehicle tp WHERE tp.deletedAt IS NULL AND tp.vehicleCode = '" + vehicleCode + "' AND tp.client.clientCode = '" + clientCode + "'";
			Vehicle vehicle= (Vehicle)this.swpService.getUniqueRecordByHQL(hql);
			if(vehicle!=null)
			{
				vehicle.setVehicleStatus(VehicleStatus.valueOf(status));
			}
			vehicle.setUpdatedAt(new Date());
			this.swpService.updateRecord(vehicle);
			jsonObject.add("message", "Vehicle status updated");
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
	
	
	
	
	

	public Response getLine(String lineCode, String token, String requestId, String clientCode) {
		// TODO Auto-generated method stub
		JsonObjectBuilder jsonObject = Json.createObjectBuilder();
    	
    	
		try{
			
			if(lineCode==null || clientCode==null)
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
			
			
			String hql = "Select tp from Line tp WHERE tp.deletedAt IS NULL AND tp.lineCode = '" + lineCode + "' AND tp.client.clientCode = '" + clientCode + "'";
			Line line= (Line)this.swpService.getUniqueRecordByHQL(hql);
			jsonObject.add("message", "Line found");
			jsonObject.add("status", ERROR.GENERAL_SUCCESS);
			jsonObject.add("line", new Gson().toJson(line));
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
	
	
	
	public Response updateVehicleStatus(Long vehicleId, String vehicleStatus, String token, String requestId, String clientCode, String ipAddress) {
		// TODO Auto-generated method stub
		JsonObjectBuilder jsonObject = Json.createObjectBuilder();
    	
    	
		try{
			
			if(vehicleId==null || clientCode==null)
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
			//log.info"------>>>>" + RoleType.ADMIN_STAFF.name());
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
				String hql = "select tp from User tp where tp.username = '" + username + "' AND " +
						"tp.userStatus = " + UserStatus.ACTIVE.ordinal() + " AND tp.deletedAt IS NULL";
				//log.inforequestId + "hql ==" + hql);
				user = (User)this.swpService.getUniqueRecordByHQL(hql);
				
				
				if(user==null)
				{

					//log.inforequestId + "user IS NULL");
					//log.inforequestId + "user firstname = " + user.getFirstName());
					//log.inforequestId + "user lastname = " + user.getLastName());
					jsonObject.add("status", ERROR.INVALID_ACTION_PROVIDED);
					jsonObject.add("message", "Invalid Vehicle deletion Priviledges");
					JsonObject jsonObj = jsonObject.build();
		            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
				}
				//roleCode = user.getRoleCode();
			}
			
			VehicleStatus vs = null;
			try
			{
				
				vs = VehicleStatus.valueOf(vehicleStatus);
			}
			catch(IllegalArgumentException | NullPointerException e)
			{
				jsonObject.add("status", ERROR.INCOMPLETE_PARAMETERS);
				jsonObject.add("message", "Invalid status provided");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			String hql = "Select tp from Vehicle tp WHERE tp.vehicleId = '" + vehicleId + "' AND tp.client.clientCode = '" + clientCode + "' AND tp.deletedAt IS NULL";
			Vehicle vehicle = (Vehicle)this.swpService.getUniqueRecordByHQL(hql);
			
			if(vehicle!=null)
			{
				vehicle.setDeletedAt(new Date());
				swpService.updateRecord(vehicle);
				
				AuditTrail ad = UtilityHelper.createAuditTrailEntry(ipAddress, RequestType.UPDATE_VEHICLE_STATUS, requestId, this.swpService, 
						verifyJ.has("username") ? verifyJ.getString("username") : null, vehicle.getVehicleId(), Vehicle.class.getName(), 
						"Updated Vehicle Status. Vehicle Name: " + vehicle.getVehicleName() + " | Deleted By: " + user.getFirstName() + " " + user.getLastName() + 
						" | New Status : " + vs.name(), clientCode);
			}
			jsonObject.add("message", "Vehicle status updated successfully");
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
	
	
	public Response updateLineStatus(String lineCode, Integer lineStatus, String token, String requestId, String clientCode, String ipAddress) {
		// TODO Auto-generated method stub
		JsonObjectBuilder jsonObject = Json.createObjectBuilder();
    	
    	
		try{
			
			if(lineCode==null || clientCode==null || lineStatus==null)
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
			//log.info"------>>>>" + RoleType.ADMIN_STAFF.name());
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
				String hql = "select tp from User tp where tp.username = '" + username + "' AND " +
						"tp.userStatus = " + UserStatus.ACTIVE.ordinal() + " AND tp.deletedAt IS NULL";
				//log.inforequestId + "hql ==" + hql);
				user = (User)this.swpService.getUniqueRecordByHQL(hql);
				
				
				if(user==null)
				{

					//log.inforequestId + "user IS NULL");
					//log.inforequestId + "user firstname = " + user.getFirstName());
					//log.inforequestId + "user lastname = " + user.getLastName());
					jsonObject.add("status", ERROR.INVALID_ACTION_PROVIDED);
					jsonObject.add("message", "Invalid Vehicle deletion Priviledges");
					JsonObject jsonObj = jsonObject.build();
		            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
				}
				//roleCode = user.getRoleCode();
			}
			
			String hql = "Select tp from Line tp WHERE tp.lineCode = '" + lineCode + "' AND tp.client.clientCode = '" + clientCode + "' AND tp.deletedAt IS NULL";
			Line line = (Line)this.swpService.getUniqueRecordByHQL(hql);
			
			if(lineStatus!=1)
			{
				line.setLineStatus(Boolean.TRUE);
				swpService.updateRecord(line);
			}
			else
			{
				line.setLineStatus(Boolean.FALSE);
				swpService.updateRecord(line);
			}
			AuditTrail ad = UtilityHelper.createAuditTrailEntry(ipAddress, RequestType.UPDATE_LINE, requestId, this.swpService, 
					verifyJ.has("username") ? verifyJ.getString("username") : null, line.getLineId(), Line.class.getName(), 
					"Updated Line Status. Line Name: " + line.getLineName() + " | Updated By: " + user.getFirstName() + " " + user.getLastName() + 
					" | New Status : " + (lineStatus.equals(1) ? "Active" : "Inactive"), clientCode);
			
			jsonObject.add("message", "Vehicle status updated successfully");
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
	
	public Response deleteVehicle(Long vehicleId, String token, String requestId, String clientCode, String ipAddress) {
		// TODO Auto-generated method stub
		JsonObjectBuilder jsonObject = Json.createObjectBuilder();
    	
    	
		try{
			
			if(vehicleId==null || clientCode==null)
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
			//log.info"------>>>>" + RoleType.ADMIN_STAFF.name());
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
				String hql = "select tp from User tp where tp.username = '" + username + "' AND " +
						"tp.userStatus = " + UserStatus.ACTIVE.ordinal() + " AND tp.deletedAt IS NULL";
				//log.inforequestId + "hql ==" + hql);
				user = (User)this.swpService.getUniqueRecordByHQL(hql);
				
				
				if(user==null)
				{

					//log.inforequestId + "user IS NULL");
					//log.inforequestId + "user firstname = " + user.getFirstName());
					//log.inforequestId + "user lastname = " + user.getLastName());
					jsonObject.add("status", ERROR.INVALID_ACTION_PROVIDED);
					jsonObject.add("message", "Invalid Vehicle deletion Priviledges");
					JsonObject jsonObj = jsonObject.build();
		            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
				}
				//roleCode = user.getRoleCode();
			}
			
			String hql = "Select tp from Vehicle tp WHERE tp.vehicleId = '" + vehicleId + "' AND tp.client.clientCode = '" + clientCode + "' AND tp.deletedAt IS NULL";
			Vehicle vehicle = (Vehicle)this.swpService.getUniqueRecordByHQL(hql);
			
			if(vehicle!=null)
			{
				vehicle.setDeletedAt(new Date());
				swpService.updateRecord(vehicle);
				
				AuditTrail ad = UtilityHelper.createAuditTrailEntry(ipAddress, RequestType.DELETE_VEHICLE, requestId, this.swpService, 
						verifyJ.has("username") ? verifyJ.getString("username") : null, vehicle.getVehicleId(), Vehicle.class.getName(), 
						"Deleted Vehicle. Vehicle Name: " + vehicle.getVehicleName() + " | Deleted By: " + user.getFirstName() + " " + user.getLastName(), clientCode);
			}
			jsonObject.add("message", "Vehicle deleted successfully");
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
	/*
	public Response createNewVehicleTrip(String token,
			Long originScheduleStationId, Long finalScheduleStationId, String purchaseStartDate, 
			String vehicleTripRouteScheduleStations, String lineCode, 
			String requestId, String ipAddress,
			String clientCode) {
		// TODO Auto-generated method stub || vehicleSeatSectionDetails==null
		JsonObjectBuilder jsonObject = Json.createObjectBuilder();
		try{
			//log.info"3#####################");
			if(lineCode==null || originScheduleStationId==null || finalScheduleStationId==null || (originScheduleStationId==finalScheduleStationId))
			{
				jsonObject.add("status", ERROR.INCOMPLETE_PARAMETERS);
				jsonObject.add("message", "Incomplete Parameters provided in request.");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			//log.info"1 #####################");
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
			//log.info"2#####################");
			
			
			
			
			//log.info"------------");
			String hql = "Select tp from ScheduleStation tp WHERE tp.scheduleStationId = " + originScheduleStationId + " AND tp.client.clientCode = '" + clientCode + "' AND tp.deletedAt IS NULL";
			ScheduleStation originVehicleSchedule= (ScheduleStation)this.swpService.getUniqueRecordByHQL(hql);
			
			hql = "Select tp from ScheduleStation tp WHERE tp.scheduleStationId = " + finalScheduleStationId + " AND tp.client.clientCode = '" + clientCode + "' AND tp.deletedAt IS NULL";
			ScheduleStation finalVehicleSchedule= (ScheduleStation)this.swpService.getUniqueRecordByHQL(hql);
			
			Date originDate = originVehicleSchedule.getDepartureTime();
			Date destDate = finalVehicleSchedule.getArrivalTime();
			if(originDate.before(destDate)==false)
			{
				jsonObject.add("status", ERROR.DEPARTURE_DATE_AHEAD_OF_ARRIVAL_DATE);
				jsonObject.add("message", "Departure date must be before final arrival date of the trip");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			hql = "Select tp from Client tp where tp.clientCode = '" +clientCode+ "' AND tp.deletedAt IS NULL";
			Client client = (Client)this.swpService.getUniqueRecordByHQL(hql);

			hql = "Select tp from Line tp where tp.lineCode = '"+lineCode+"' AND tp.clientCode = '" +clientCode+ "' AND tp.deletedAt IS NULL";
			Line line = (Line)this.swpService.getUniqueRecordByHQL(hql);
			
			Date psd = null;
			try{
				psd = purchaseStartDate==null ? new Date() : new SimpleDateFormat("yyyy-MM-dd").parse(purchaseStartDate);
			}catch(ParseException e)
			{
				psd = new Date();
			}
			
			hql = "Select tp from VehicleTrip tp where tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL " +
					" AND tp.finalDestinationScheduleStation.scheduleStationId = " + finalVehicleSchedule.getScheduleStationId() + 
					" AND tp.originScheduleStation.scheduleStationId = " + originVehicleSchedule.getScheduleStationId();
			VehicleTrip vehicleTrip = (VehicleTrip)swpService.getUniqueRecordByHQL(hql);
			if(vehicleTrip!=null)
			{
				jsonObject.add("status", ERROR.VEHICLE_TRIP_EXISTS);
				jsonObject.add("message", "Vehicle Trip a been created previously");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.CONFLICT ).entity( jsonObj.toString() ).build();
			}
			vehicleTrip = new VehicleTrip();
			
			vehicleTrip.setCreatedAt(new Date());
			vehicleTrip.setUpdatedAt(new Date());
			vehicleTrip.setPurchaseStartDate(psd);
			vehicleTrip.setClient(client);
			vehicleTrip.setLine(line);
			vehicleTrip.setFinalDestinationScheduleStation(finalVehicleSchedule);
			vehicleTrip.setOriginScheduleStation(originVehicleSchedule);
			vehicleTrip.setVehicleTripStatus(VehicleTripStatus.ACTIVE);
			vehicleTrip = (VehicleTrip)(this.swpService.createNewRecord(vehicleTrip));
			
			JSONObject jsRouteScheduleStations = new JSONObject(vehicleTripRouteScheduleStations);
			for(int k=1; k<(jsRouteScheduleStations.length()+1); k++)
			{
				String key = k + "";
				JSONObject schedulestations = jsRouteScheduleStations.getJSONObject(key);
				Long tripRouteOriginVehicleScheduleId = schedulestations.getLong("tripRouteOriginVehicleScheduleId");
				Long tripRouteDestinationVehicleScheduleId = schedulestations.getLong("tripRouteDestinationVehicleScheduleId");
				Integer tripRouteOrder = schedulestations.getInt("tripRouteOrder");
				JSONObject vehicleTripRouteSeatSectionJS = schedulestations.getJSONObject("vehicleTripRouteSeatSection");
				String vehicleTripRouting = (String)(createNewVehicleTripRouting(token, vehicleTrip.getVehicleTripId(),
						tripRouteOriginVehicleScheduleId, tripRouteDestinationVehicleScheduleId, tripRouteOrder,
						requestId, ipAddress, clientCode).getEntity());
				//log.info"vehicleTripRouting === " + vehicleTripRouting);
				if(vehicleTripRouting!=null)
				{
					JSONObject vehicleTripRoutingJS = new JSONObject(vehicleTripRouting);
					if(vehicleTripRoutingJS.getInt("status")==ERROR.GENERAL_SUCCESS)
					{
						vehicleTripRouting = vehicleTripRoutingJS.getString("vehicleTripRouting");
						vehicleTripRoutingJS = new JSONObject(vehicleTripRouting);
						
						Iterator<String> vehicleTripRouteSeatSectionJSKeys = vehicleTripRouteSeatSectionJS.keys();	//Codes
						while(vehicleTripRouteSeatSectionJSKeys.hasNext())
						{
							String seatSectionCode = vehicleTripRouteSeatSectionJSKeys.next();
							//log.info"seatSectionCode === " + seatSectionCode);
							JSONObject sectionAndfareDetails = vehicleTripRouteSeatSectionJS.getJSONObject(seatSectionCode);
							Integer maximumNumberOfCabins = sectionAndfareDetails.getInt("maximumNumberOfCabins");
							//log.info"maximumNumberOfCabins === " + maximumNumberOfCabins);
							Double childFare = sectionAndfareDetails.getDouble("childFare");
							//log.info"childFare === " + childFare);
							Double adultFare = sectionAndfareDetails.getDouble("adultFare");
							//log.info"adultFare === " + adultFare);
							hql = "Select tp from VehicleSeatSection tp WHERE tp.sectionCode = '" + seatSectionCode + "' AND tp.client.clientCode = '" + clientCode + "' AND tp.deletedAt IS NULL";
							VehicleSeatSection vss = (VehicleSeatSection)this.swpService.getUniqueRecordByHQL(hql);
							Long vehicleTripRoutingId = vehicleTripRoutingJS.getLong("vehicleTripRoutingId");
							Long vehicleSeatClassId = vss.getVehicleSeatClass().getVehicleSeatClassId();
							if(vss!=null)
							{
								hql = "Select tp from VehicleTripRouting tp WHERE tp.vehicleTripRoutingId = " + vehicleTripRoutingId + " AND tp.client.clientCode = '" + clientCode + "' AND tp.deletedAt IS NULL";
								VehicleTripRouting vtr = (VehicleTripRouting)this.swpService.getUniqueRecordByHQL(hql);
								
								VehicleTripRouteSeatSection vtss = new VehicleTripRouteSeatSection();
								vtss.setClient(client);
								vtss.setCreatedAt(new Date());
								vtss.setUpdatedAt(new Date());
								vtss.setMaximumNumberOfCabins(maximumNumberOfCabins);
								vtss.setVehicleSeatSection(vss);
								vtss.setVehicleTripRouting(vtr);
								vtss = (VehicleTripRouteSeatSection)(this.swpService.createNewRecord(vtss));
								
								if(maximumNumberOfCabins!=null)
								{
									hql = "Select tp from VehicleSeat tp where tp.vehicleSeatSection.vehicleSeatSectionId = " + vss.getVehicleSeatSectionId() + " AND tp.deletedAt IS NULL AND tp.client.clientCode = '"+clientCode+"'";
									Collection<VehicleSeat> vehicleSeats = swpService.getAllRecordsByHQL(hql);
									for(int i1=0; i1<vtss.getMaximumNumberOfCabins(); i1++)
									{
										Iterator<VehicleSeat> vehicleSeatIt = vehicleSeats.iterator();
										while(vehicleSeatIt.hasNext())
										{
											VehicleSeat vs = vehicleSeatIt.next();
											VehicleSeatAvailability vsa = new VehicleSeatAvailability();
											vsa.setBoughtByCustomer(null);
											vsa.setCreatedAt(new Date());
											vsa.setUpdatedAt(new Date());
											vsa.setLockedDown(Boolean.FALSE);
											vsa.setLockedDownExpiryDate(null);
											vsa.setSeatAvailabilityStatus(SeatAvailabilityStatus.OPEN);
											vsa.setVehicleSeat(vs);
											vsa.setVehicleTripRouteSeatSection(vtss);
											vsa = (VehicleSeatAvailability)(this.swpService.createNewRecord(vsa));
										}
									}
								}
							}
							//log.info"==================================");
							hql = "Select tp from VehicleTripRouteSeatSection tp where tp.vehicleTripRouting.vehicleTripRoutingId = " + vehicleTripRoutingId + 
									" AND tp.vehicleSeatSection.vehicleSeatSectionId = " + vss.getVehicleSeatSectionId() + " AND tp.deletedAt is NULL" +
									" AND tp.client.clientCode = '"+clientCode+"'";
							VehicleTripRouteSeatSection vehicleTripRouteSeatSection = (VehicleTripRouteSeatSection)swpService.getUniqueRecordByHQL(hql);
							String vehicleScheduleFares = (String)(createNewVehicleScheduleFares(token,
									vehicleTripRoutingId, vehicleTripRouteSeatSection.getVehicleTripRouteSeatSectionId(), childFare,
									adultFare, requestId, ipAddress, clientCode).getEntity());
							//log.info"4555>>>>>>>>>>>>>===" + vehicleScheduleFares);
						}
						
					}
				}
			}
			
			
			
			jsonObject.add("message", "Vehicle Trip created successfully");
			jsonObject.add("status", ERROR.GENERAL_SUCCESS);
			jsonObject.add("terminal", new Gson().toJson(vehicleTrip));
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
	
	public Response listVehicleTrips(Long vehicleTripId, Long vehicleId,
			Long originVehicleScheduleId, Long finalVehicleScheduleId,
			Integer startIndex, Integer limit, String token, String requestId,
			String clientCode) {
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
			
			
			
			
			String hql = "Select tp from VehicleTrip tp where tp.deletedAt IS NULL AND tp.client.clientCode = '"+clientCode+"'";
			if(vehicleTripId!=null)
				hql = hql + " AND tp.vehicleTripId = " + vehicleTripId;
			if(originVehicleScheduleId!=null)
				hql = hql + " AND tp.originVehicleSchedule.vehicleScheduleId = " + originVehicleScheduleId;
			if(finalVehicleScheduleId!=null)
				hql = hql + " AND tp.finalVehicleSchedule.vehicleScheduleId = " + finalVehicleScheduleId;
			if(vehicleId!=null)
				hql = hql + " AND tp.finalVehicleSchedule.vehicle.vehicleId = " + vehicleId;
			String sql = "";
			hql = hql + sql;
			Collection<VehicleTrip> vehicleTrips= (Collection<VehicleTrip>)this.swpService.getAllRecordsByHQL(hql, startIndex, limit);
			
			
			List<Long> totalVehicleTripCount = (List<Long>)swpService.getAllRecordsByHQL(hql);
			Long totalVehicleTrips = totalVehicleTripCount!=null ? totalVehicleTripCount.iterator().next() : 0;
			//log.info"totalVehicleTripCount ==" + totalVehicleTrips);
			
			
			jsonObject.add("message", "Vehicle Trips listed successfully");
			jsonObject.add("status", ERROR.GENERAL_SUCCESS);
			jsonObject.add("vehicleTripList", new Gson().toJson(vehicleTrips));
			jsonObject.add("totalVehicleTripsCount", totalVehicleTrips);
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
	*/
	
	
	public Response getVehicleTrip(Long vehicleTripId, String token,
			String requestId, String clientCode) {
		// TODO Auto-generated method stub
		JsonObjectBuilder jsonObject = Json.createObjectBuilder();
    	
    	
		try{
			
			if(vehicleTripId==null || clientCode==null)
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
			
			
			String hql = "Select tp from VehicleTrip tp WHERE tp.deletedAt IS NULL AND tp.vehicleTripId = '" + vehicleTripId + "' AND tp.client.clientCode = '" + clientCode + "'";
			VehicleTrip vehicleTrip= (VehicleTrip)this.swpService.getUniqueRecordByHQL(hql);
			jsonObject.add("message", "Vehicle Trip found");
			jsonObject.add("status", ERROR.GENERAL_SUCCESS);
			jsonObject.add("vehicleTrip", new Gson().toJson(vehicleTrip));
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

	public Response deleteVehicleTrip(Long vehicleTripId, String token,
			String requestId, String clientCode, String ipAddress) {
		// TODO Auto-generated method stub
		JsonObjectBuilder jsonObject = Json.createObjectBuilder();
    	
    	
		try{
			
			if(vehicleTripId==null || clientCode==null)
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
			//log.info"------>>>>" + RoleType.ADMIN_STAFF.name());
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
				String hql = "select tp from User tp where tp.username = '" + username + "' AND " +
						"tp.userStatus = " + UserStatus.ACTIVE.ordinal() + " AND tp.deletedAt IS NULL";
				//log.inforequestId + "hql ==" + hql);
				user = (User)this.swpService.getUniqueRecordByHQL(hql);
				
				
				if(user==null)
				{

					//log.inforequestId + "user IS NULL");
					//log.inforequestId + "user firstname = " + user.getFirstName());
					//log.inforequestId + "user lastname = " + user.getLastName());
					jsonObject.add("status", ERROR.INVALID_ACTION_PROVIDED);
					jsonObject.add("message", "Invalid vehicle trip deletion Priviledges");
					JsonObject jsonObj = jsonObject.build();
		            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
				}
				//roleCode = user.getRoleCode();
			}
			
			
			String hql = "Select tp from VehicleTrip tp WHERE tp.vehicleTripId = '" + vehicleTripId + "' AND tp.client.clientCode = '" + clientCode + "' AND tp.deletedAt IS NULL";
			VehicleTrip vehicleTrip= (VehicleTrip)this.swpService.getUniqueRecordByHQL(hql);
			
			if(vehicleTrip!=null)
			{
				vehicleTrip.setDeletedAt(new Date());
				swpService.updateRecord(vehicleTrip);
				
				AuditTrail ad = UtilityHelper.createAuditTrailEntry(ipAddress, RequestType.VEHICLE_SCHEDULE_FARE_DELETE, requestId, this.swpService, 
						verifyJ.has("username") ? verifyJ.getString("username") : null, vehicleTrip.getVehicleTripId(), VehicleTrip.class.getName(), 
						"Delete Vehicle Trip. Departure Station: " + vehicleTrip.getOriginScheduleStation().getStation().getStationName() +
						"| Arrival Station: " + vehicleTrip.getFinalDestinationScheduleStation().getStation().getStationName() + 
						"| Departure Time: " + vehicleTrip.getOriginScheduleStation().getDepartureTime() + 
						"| Arrival Time: " + vehicleTrip.getFinalDestinationScheduleStation().getArrivalTime() + 
						"| Deleted By " + user.getFirstName() + " " + user.getLastName(), clientCode);
			}
			jsonObject.add("message", "Vehicle trip deleted successfully");
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

	/*public Response createNewVehicleTripRouting(String token, Long vehicleTripId,
			Long originVehicleScheduleId, Long destinationVehicleScheduleId, Integer tripRouteOrder,
			String requestId, String ipAddress, String clientCode) {
		// TODO Auto-generated method stub
		JsonObjectBuilder jsonObject = Json.createObjectBuilder();
		try{
			
			if(originVehicleScheduleId==null || destinationVehicleScheduleId==null || (originVehicleScheduleId==destinationVehicleScheduleId) || (tripRouteOrder==null || tripRouteOrder==0))
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
			
			String hql = "Select tp from VehicleSchedule tp WHERE tp.vehicleScheduleId = " + originVehicleScheduleId + " AND tp.client.clientCode = '" + clientCode + "' AND tp.deletedAt IS NULL";
			VehicleSchedule originVehicleSchedule= (VehicleSchedule)this.swpService.getUniqueRecordByHQL(hql);
			
			hql = "Select tp from VehicleSchedule tp WHERE tp.vehicleScheduleId = " + destinationVehicleScheduleId + " AND tp.client.clientCode = '" + clientCode + "' AND tp.deletedAt IS NULL";
			VehicleSchedule destinationVehicleSchedule= (VehicleSchedule)this.swpService.getUniqueRecordByHQL(hql);
			
			

			if(originVehicleSchedule==null || destinationVehicleSchedule==null)
			{
				jsonObject.add("status", ERROR.VEHICLE_ORIGIN_DESTINATION_VEHICLE_SCHEDULE);
				jsonObject.add("message", "Invalid origin and or destination vehicle schedule provided");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			
			hql = "Select tp from VehicleTripRouting tp where tp.vehicleTripRoutingOriginSchedule.vehicleScheduleId = " + originVehicleScheduleId + " AND " +
					"tp.vehicleTripRoutingDestinationSchedule.vehicleScheduleId = " + destinationVehicleScheduleId + " AND tp.vehicleTrip.vehicleTripId = " + vehicleTripId
					+ " AND tp.deletedAt IS NULL AND tp.client.clientCode = '"+clientCode+"'";
			VehicleTripRouting vehicleTripRouting = (VehicleTripRouting)this.swpService.getUniqueRecordByHQL(hql);
			
			if(vehicleTripRouting!=null)
			{
				jsonObject.add("status", ERROR.VEHICLE_TRIP_ROUTING_EXISTS);
				jsonObject.add("message", "A Vehicle trip routing matching the details provided already exists");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			

			hql = "Select tp from VehicleTrip tp WHERE tp.vehicleTripId = " + vehicleTripId + " AND tp.client.clientCode = '" + clientCode + "' AND tp.deletedAt IS NULL";
			VehicleTrip vehicleTrip= (VehicleTrip)this.swpService.getUniqueRecordByHQL(hql);
			
			hql = "Select tp from Client tp where tp.clientCode = '" +clientCode+ "' AND tp.deletedAt IS NULL";
			Client client = (Client)this.swpService.getUniqueRecordByHQL(hql);
			
			vehicleTripRouting = new VehicleTripRouting();
			vehicleTripRouting.setCreatedAt(new Date());
			vehicleTripRouting.setUpdatedAt(new Date());
			vehicleTripRouting.setStatus(Boolean.TRUE);
			vehicleTripRouting.setVehicleTrip(vehicleTrip);
			vehicleTripRouting.setClient(client);
			vehicleTripRouting.setRouteOrder(tripRouteOrder);
			vehicleTripRouting.setVehicleTripRoutingDestinationSchedule(destinationVehicleSchedule);
			vehicleTripRouting.setVehicleTripRoutingOriginSchedule(originVehicleSchedule);
			vehicleTripRouting.setRouteCode(RandomStringUtils.randomAlphanumeric(8).toUpperCase());
			vehicleTripRouting = (VehicleTripRouting)(this.swpService.createNewRecord(vehicleTripRouting));
			
			hql = "Select tp from VehicleSeat tp WHERE tp.client.clientCode = '" + clientCode + "' AND tp.deletedAt IS NULL";
			Collection<VehicleSeat> vehicleSeats= (Collection<VehicleSeat>)this.swpService.getAllRecordsByHQL(hql);
			Iterator<VehicleSeat> iter = vehicleSeats.iterator();
			while(iter.hasNext())
			{
				VehicleSeat vehicleSeat = iter.next();
				VehicleSeatAvailability vsa = new VehicleSeatAvailability();
				vsa.setBoughtByCustomer(null);
				vsa.setCreatedAt(new Date());
				vsa.setUpdatedAt(new Date());
				vsa.setLockedDown(Boolean.FALSE);
				vsa.setLockedDownExpiryDate(null);
				vsa.setSeatAvailabilityStatus(SeatAvailabilityStatus.OPEN);
				vsa.setVehicleSeat(vehicleSeat);
				vsa.setVehicleTripRouting(vehicleTripRouting);
				vsa = (VehicleSeatAvailability)(this.swpService.createNewRecord(vsa));
			}
			
			jsonObject.add("message", "Vehicle Trip Routing created successfully");
			jsonObject.add("status", ERROR.GENERAL_SUCCESS);
			jsonObject.add("vehicleTripRouting", new Gson().toJson(vehicleTripRouting));
			JsonObject jsonObj = jsonObject.build();
            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			
		}catch(Exception e)
		{
			log.warn(e); log.error("exception...", e);
			e.printStackTrace();
			JsonObject jsonObj = jsonObject.build();
            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
		}
	}*/

	public Response listVehicleTripRoutings(Long vehicleTripId,
			Long vehicleTripRoutingOriginScheduleId,
			Long vehicleTripRoutingDestinationScheduleId, 
			Long vehicleTripCode,
			Integer startIndex, Integer limit, String token, String requestId,
			String clientCode) {
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
			
			
			
			
			String hql = "Select tp from VehicleTripRouting tp where tp.deletedAt IS NULL AND tp.client.clientCode = '"+clientCode+"'";
			if(vehicleTripRoutingOriginScheduleId!=null)
				hql = hql + " AND tp.vehicleTripRoutingOriginSchedule.id = " + vehicleTripRoutingOriginScheduleId;
			if(vehicleTripRoutingDestinationScheduleId!=null)
				hql = hql + " AND tp.vehicleTripRoutingDestinationSchedule.id = " + vehicleTripRoutingDestinationScheduleId;
			if(vehicleTripCode!=null)
				hql = hql + " AND tp.vehicleTripCode = '" + vehicleTripRoutingDestinationScheduleId + "'";
			if(vehicleTripId!=null)
				hql = hql + " AND tp.vehicleTrip.id = " + vehicleTripId;
			String sql = "";
			hql = hql + sql;
			Collection<VehicleTripRouting> vehicleTripRoutings= (Collection<VehicleTripRouting>)this.swpService.getAllRecordsByHQL(hql, startIndex, limit);
			
			
			List<Long> totalVehicleTripRoutingCount = (List<Long>)swpService.getAllRecordsByHQL(hql);
			Long totalVehicleTripRoutings = totalVehicleTripRoutingCount!=null ? totalVehicleTripRoutingCount.iterator().next() : 0;
			//log.info"totalVehicleTripRoutingCount ==" + totalVehicleTripRoutings);
			
			
			jsonObject.add("message", "Vehicle Trip Routings listed successfully");
			jsonObject.add("status", ERROR.GENERAL_SUCCESS);
			jsonObject.add("vehicleTripRoutingList", new Gson().toJson(vehicleTripRoutings));
			jsonObject.add("totalVehicleTripsRoutingCount", totalVehicleTripRoutings);
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

	public Response getVehicleTripRouting(Long vehicleTripRoutingId, Long vehicleTripId, Long vehicleScheduleId,
			String token, String requestId, String ipAddress, String clientCode) {
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
			
			
			String hql = "Select tp from VehicleTripRouting tp WHERE tp.deletedAt IS NULL";
			if(vehicleTripRoutingId!=null)
				hql = hql + " AND tp.vehicleTripRoutingId = " + vehicleTripRoutingId;
			if(vehicleTripId!=null)
				hql = hql + " AND tp.vehicleTrip.vehicleTripId = " + vehicleTripId;
			if(vehicleScheduleId!=null)
				hql = hql + " AND tp.vehicleTripRoutingOriginSchedule.vehicleScheduleId = " + vehicleScheduleId;
			hql = hql + " AND tp.client.clientCode = '" + clientCode + "'";
			Collection<VehicleTripRouting> vehicleTripRouting= (Collection<VehicleTripRouting>)this.swpService.getAllRecordsByHQL(hql);
			jsonObject.add("message", "Vehicle Trip Routing found");
			jsonObject.add("status", ERROR.GENERAL_SUCCESS);
			jsonObject.add("vehicleTripRoutings", new Gson().toJson(vehicleTripRouting));
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

	public Response deleteVehicleTripRouting(Long vehicleTripRoutingId,
			String token, String requestId, String clientCode) {
		// TODO Auto-generated method stub
		JsonObjectBuilder jsonObject = Json.createObjectBuilder();
    	
    	
		try{
			
			if(vehicleTripRoutingId==null || clientCode==null)
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
			
			
			String hql = "Select tp from VehicleTripRouting tp WHERE tp.vehicleTripRoutingId = '" + vehicleTripRoutingId + "' AND tp.client.clientCode = '" + clientCode + "' AND tp.deletedAt IS NULL";
			VehicleTripRouting vehicleTripRouting= (VehicleTripRouting)this.swpService.getUniqueRecordByHQL(hql);
			
			if(vehicleTripRouting!=null)
			{
				vehicleTripRouting.setDeletedAt(new Date());
				swpService.updateRecord(vehicleTripRouting);
			}
			jsonObject.add("message", "Vehicle trip routing deleted successfully");
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

	public Response createNewVehicleScheduleFares(String token,
			Long vehicleTripRoutingId, Long vehicleTripRouteSeatSectionId, Double childFare,
			Double adultFare, String requestId, String ipAddress,
			String clientCode) {
		// TODO Auto-generated method stub
		JsonObjectBuilder jsonObject = Json.createObjectBuilder();
		try{
			
			if(vehicleTripRoutingId==null || vehicleTripRouteSeatSectionId==null || (childFare==null) || adultFare==null || clientCode==null)
			{
				jsonObject.add("status", ERROR.INCOMPLETE_PARAMETERS);
				jsonObject.add("message", "Incomplete Parameters provided in request.");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			//log.info">>>>>>>>>>>>>===" + (vehicleTripRoutingId + " - " + vehicleTripRouteSeatSectionId + " - " + childFare + " - " + adultFare + " - " + clientCode));
			
			
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
			
			String hql = "Select tp from VehicleTripRouting tp WHERE tp.vehicleTripRoutingId = " + vehicleTripRoutingId + " AND tp.client.clientCode = '" + clientCode + "' AND tp.deletedAt IS NULL";
			VehicleTripRouting vehicleTripRouting= (VehicleTripRouting)this.swpService.getUniqueRecordByHQL(hql);
			
			hql = "Select tp from VehicleTripRouteSeatSection tp WHERE tp.vehicleTripRouteSeatSectionId = " + vehicleTripRouteSeatSectionId + " AND tp.client.clientCode = '" + clientCode + "' AND tp.deletedAt IS NULL";
			VehicleTripRouteSeatSection vehicleTripRouteSeatSection= (VehicleTripRouteSeatSection)this.swpService.getUniqueRecordByHQL(hql);
			
			hql = "Select tp from VehicleScheduleFare tp where tp.vehicleTripRouteSeatSection.vehicleTripRouteSeatSectionId = " + vehicleTripRouteSeatSectionId + " AND tp.deletedAt IS NULL AND tp.client.clientCode = '"+clientCode+"'";
			VehicleScheduleFare vehicleScheduleFare = (VehicleScheduleFare)this.swpService.getUniqueRecordByHQL(hql);
			
			if(vehicleScheduleFare!=null)
			{
				jsonObject.add("status", ERROR.VEHICLE_SCHEDULE_FARE_EXISTS);
				jsonObject.add("message", "A Vehicle schedule fare already exists");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			hql = "Select tp from Client tp where tp.clientCode = '" +clientCode+ "' AND tp.deletedAt IS NULL";
			Client client = (Client)this.swpService.getUniqueRecordByHQL(hql);
			
			//log.info"233...>>>>>>>>>>>>>===");
			vehicleScheduleFare = new VehicleScheduleFare();
			vehicleScheduleFare.setCreatedAt(new Date());
			vehicleScheduleFare.setUpdatedAt(new Date());
			vehicleScheduleFare.setBaseAdultFare(adultFare);
			vehicleScheduleFare.setBaseChildFare(childFare);
			vehicleScheduleFare.setClient(client);
			vehicleScheduleFare.setVehicleTripRouteSeatSection(vehicleTripRouteSeatSection);
			vehicleScheduleFare = (VehicleScheduleFare)(this.swpService.createNewRecord(vehicleScheduleFare));
			
			AuditTrail ad = UtilityHelper.createAuditTrailEntry(ipAddress, RequestType.NEW_VEHICLE_SCHEDULE_FARE, requestId, this.swpService, 
					verifyJ.has("username") ? verifyJ.getString("username") : null, vehicleScheduleFare.getVehicleScheduleFareId(), VehicleScheduleFare.class.getName(), 
					"New Schedule Fare. Trip Class: " + vehicleTripRouteSeatSection.getVehicleSeatSection().getSectionName() + " | Adult Fare: " + adultFare + 
					" | Child Fare: " + childFare, clientCode);
			
			jsonObject.add("message", "Vehicle Schedule Fare created successfully");
			jsonObject.add("status", ERROR.GENERAL_SUCCESS);
			jsonObject.add("vehicleScheduleFare", new Gson().toJson(vehicleScheduleFare));
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

	public Response listVehicleScheduleFares(Long vehicleTripRoutingId,
			Long vehicleSeatClassId, Double childFare, Double adultFare,
			Integer startIndex, Integer limit, String token, String requestId,
			String clientCode) {
		// TODO Auto-generated method stub
		JsonObjectBuilder jsonObject = Json.createObjectBuilder();
    	
    	
		try{
			
			if(vehicleTripRoutingId==null || vehicleSeatClassId==null || childFare==null || adultFare==null || clientCode==null)
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
			
			
			
			
			String hql = "Select tp from VehicleScheduleFare tp where tp.deletedAt IS NULL AND tp.client.clientCode = '"+clientCode+"'";
			if(vehicleTripRoutingId!=null)
				hql = hql + " AND tp.vehicleTripRouting.id = " + vehicleTripRoutingId;
			if(vehicleSeatClassId!=null)
				hql = hql + " AND tp.vehicleSeatClass.id = " + vehicleSeatClassId;
			if(childFare!=null)
				hql = hql + " AND tp.childFare = " + childFare;
			if(adultFare!=null)
				hql = hql + " AND tp.adultFare = " + adultFare;
			String sql = "";
			hql = hql + sql;
			Collection<VehicleScheduleFare> vehicleScheduleFares= (Collection<VehicleScheduleFare>)this.swpService.getAllRecordsByHQL(hql, startIndex, limit);
			
			
			List<Long> totalVehicleScheduleFareCount = (List<Long>)swpService.getAllRecordsByHQL(hql);
			Long totalVehicleScheduleFares = totalVehicleScheduleFareCount!=null ? totalVehicleScheduleFareCount.iterator().next() : 0;
			//log.info"totalVehicleScheduleFareCount ==" + totalVehicleScheduleFares);
			
			
			jsonObject.add("message", "Vehicle Schedule Fares listed successfully");
			jsonObject.add("status", ERROR.GENERAL_SUCCESS);
			jsonObject.add("vehicleScheduleFareList", new Gson().toJson(vehicleScheduleFares));
			jsonObject.add("totalVehicleScheduleFareCount", totalVehicleScheduleFares);
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

	

	public Response getVehicleScheduleFare(Long vehicleScheduleFareId,
			String token, String requestId, String clientCode) {
		// TODO Auto-generated method stub
		JsonObjectBuilder jsonObject = Json.createObjectBuilder();
    	
    	
		try{
			
			if(vehicleScheduleFareId==null || clientCode==null)
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
			
			
			String hql = "Select tp from VehicleScheduleFare tp WHERE tp.deletedAt IS NULL AND tp.vehicleTripRouting.id = '" + vehicleScheduleFareId + "' AND tp.client.clientCode = '" + clientCode + "'";
			VehicleScheduleFare vehicleScheduleFare= (VehicleScheduleFare)this.swpService.getUniqueRecordByHQL(hql);
			jsonObject.add("message", "Vehicle Schedule Fare found");
			jsonObject.add("status", ERROR.GENERAL_SUCCESS);
			jsonObject.add("vehicleTrip", new Gson().toJson(vehicleScheduleFare));
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
	
	
	
	
	public Response updatePurchasedTripStatus(String receiptNo, String status,
			String token, String requestId, String clientCode) {
		// TODO Auto-generated method stub
		JsonObjectBuilder jsonObject = Json.createObjectBuilder();
    	
    	
		try{
			
			if(receiptNo==null || clientCode==null || status==null)
			{
				jsonObject.add("status", ERROR.INCOMPLETE_PARAMETERS);
				jsonObject.add("message", "Incomplete Parameters provided in request.");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			
			swpService = serviceLocator.getSwpService();
			Application app = Application.getInstance(swpService);
			
			String hql = "Select tp from PurchasedTrip tp WHERE tp.deletedAt IS NULL AND " +
					"tp.receiptNo = '" + receiptNo + "' AND tp.client.clientCode = '" + clientCode + "'";
			PurchasedTrip purchasedTrip= (PurchasedTrip)this.swpService.getUniqueRecordByHQL(hql);
			
			if(purchasedTrip==null)
			{
				jsonObject.add("message", "Purchased trip not found");
				jsonObject.add("status", ERROR.PURCHASED_TRIP_NOT_FOUND);
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			try{
				PurchasedTripStatus pts = PurchasedTripStatus.valueOf(status);
				if(pts.equals(PurchasedTripStatus.PENDING))
				{
					jsonObject.add("message", "Invalid status");
					jsonObject.add("status", ERROR.INVALID_STATUS_PROVIDED);
					JsonObject jsonObj = jsonObject.build();
		            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
				}
			}catch(IllegalArgumentException e)
			{
				jsonObject.add("message", "Status provided is invalid");
				jsonObject.add("status", ERROR.INVALID_STATUS_PROVIDED);
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			purchasedTrip.setPurchasedTripStatus(PurchasedTripStatus.valueOf(status));
			swpService.updateRecord(purchasedTrip);
			
			jsonObject.add("message", "Purchased trip updated");
			jsonObject.add("status", ERROR.GENERAL_SUCCESS);
			jsonObject.add("purchasedTrip", new Gson().toJson(purchasedTrip));
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
	
	
	
	public Response getPurchasedTrip(String receiptNo,
			String token, String requestId, String clientCode) {
		// TODO Auto-generated method stub
		JsonObjectBuilder jsonObject = Json.createObjectBuilder();
    	
    	
		try{
			
			if(receiptNo==null || clientCode==null)
			{
				jsonObject.add("status", ERROR.INCOMPLETE_PARAMETERS);
				jsonObject.add("message", "Incomplete Parameters provided in request.");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			
			swpService = serviceLocator.getSwpService();
			Application app = Application.getInstance(swpService);
			
			String hql = "Select tp from PurchasedTrip tp WHERE tp.deletedAt IS NULL AND " +
					"tp.receiptNo = '" + receiptNo + "' AND tp.client.clientCode = '" + clientCode + "'";
			PurchasedTrip purchasedTrip= (PurchasedTrip)this.swpService.getUniqueRecordByHQL(hql);

			if(purchasedTrip==null)
			{
				jsonObject.add("message", "Purchased trip not found");
				jsonObject.add("status", ERROR.PURCHASED_TRIP_NOT_FOUND);
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			jsonObject.add("message", "Purchased trip found");
			jsonObject.add("status", ERROR.GENERAL_SUCCESS);
			jsonObject.add("purchasedTrip", new Gson().toJson(purchasedTrip));
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
	
	

	public Response deleteVehicleScheduleFare(Long vehicleScheduleFareId,
			String token, String requestId, String clientCode, String ipAddress) {
		// TODO Auto-generated method stub
		JsonObjectBuilder jsonObject = Json.createObjectBuilder();
    	
    	
		try{
			
			if(vehicleScheduleFareId==null || clientCode==null)
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
			//User user = null;
			
			if(username!=null)
			{
				user = (User)this.swpService.getUniqueRecordByHQL("select tp from User tp where tp.username = '" + username + "' AND tp.deletedAt IS NULL AND " +
						"tp.userStatus = " + UserStatus.ACTIVE.ordinal());
			}
			
			String hql = "Select tp from VehicleScheduleFare tp WHERE tp.vehicleScheduleFareId = " + vehicleScheduleFareId + " AND tp.client.clientCode = '" + clientCode + "' AND tp.deletedAt IS NULL";
			VehicleScheduleFare vehicleScheduleFare= (VehicleScheduleFare)this.swpService.getUniqueRecordByHQL(hql);
			
			if(vehicleScheduleFare!=null)
			{
				vehicleScheduleFare.setDeletedAt(new Date());
				swpService.updateRecord(vehicleScheduleFare);
				AuditTrail ad = UtilityHelper.createAuditTrailEntry(ipAddress, RequestType.VEHICLE_SCHEDULE_FARE_DELETE, requestId, this.swpService, 
						verifyJ.has("username") ? verifyJ.getString("username") : null, vehicleScheduleFare.getVehicleScheduleFareId(), VehicleScheduleFare.class.getName(), 
						"Delete Vehicle Schedule Fare. Base Adult Fare: " + (vehicleScheduleFare.getBaseAdultFare()==null ? "N/A" : vehicleScheduleFare.getBaseAdultFare()) + " | Base Child Fare: " + 
						(vehicleScheduleFare.getBaseChildFare()!=null ? vehicleScheduleFare.getBaseChildFare() : "N/A") + 
						" | Section name: " + vehicleScheduleFare.getVehicleTripRouteSeatSection().getVehicleSeatSection().getSectionName() + 
						" | Deleted By " + user.getFirstName() + " " + user.getLastName(), clientCode);
				
			}
			jsonObject.add("message", "Vehicle schedule fare deleted successfully");
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

	/*public Response createNewPurchasedTrip(String hash, String channel, String deviceCode, String orderId, String token, Long customerId,
			String vehicleTripCode, Double adultAmountPayable, Double childAmountPayable, String cardPan, Long ticketCollectionPointId, String serviceType,
			String requestId, String ipAddress, String clientCode, Long vehicleSeatClassId, Boolean childSeatCount, Boolean adultSeatCount ) {
		// TODO Auto-generated method stub
		JsonObjectBuilder jsonObject = Json.createObjectBuilder();
		try{
			
			if(customerId==null || vehicleTripCode==null
					 || ticketCollectionPointId==null || token==null || clientCode==null 
					 || vehicleSeatClassId==null || childSeatCount==null || adultSeatCount==null
					 || hash==null || channel==null || deviceCode ==null || orderId==null || (adultAmountPayable==null && childAmountPayable==null) || 
					 (childSeatCount.equals(adultSeatCount)))
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
			
			String hql = "Select tp from Device tp where tp.deviceCode = '" + deviceCode + "'";
			Device device = (Device)this.swpService.getUniqueRecordByHQL(hql);
			
			if(device==null || (device!=null && !device.getStatus().equals(DeviceStatus.ACTIVE)))
			{
				jsonObject.add("status", ERROR.DEVICE_NOT_ACTIVE);
				jsonObject.add("message", "Device Not Active");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}


			String api_key = device.getTerminalApiKey();
			Double amtPayable = null; 
			if(childSeatCount.equals(Boolean.TRUE))
			{
				amtPayable = childAmountPayable;
			}
			else if(adultSeatCount.equals(Boolean.TRUE))
			{
				amtPayable = adultAmountPayable;
			}
			
			if(UtilityHelper.validateTransactionHash(
					hash, 
					deviceCode,
					vehicleTripCode,
					orderId,
					amtPayable,
					api_key)==false)
			{
				jsonObject.add("status", ERROR.HASH_FAILED);
				jsonObject.add("message", "Your hash failed");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			else
			{
			
				hql = "Select tp from Customer tp WHERE tp.customerId = " + customerId + "' AND tp.client.clientCode = '" + clientCode + "' AND tp.deletedAt IS NULL";
				Customer customer= (Customer)this.swpService.getUniqueRecordByHQL(hql);
	
				hql = "Select tp from TripCard tp WHERE tp.tripCard.pan = " + cardPan + "' AND tp.client.clientCode = '" + clientCode + "' AND tp.deletedAt IS NULL";
				TripCard tripCard= (TripCard)this.swpService.getUniqueRecordByHQL(hql);
				
				hql = "Select tp from VehicleTrip tp WHERE tp.vehicleTripCode = " + vehicleTripCode + "' AND tp.client.clientCode = '" + clientCode + "' AND tp.deletedAt IS NULL";
				VehicleTrip vehicleTrip= (VehicleTrip)this.swpService.getUniqueRecordByHQL(hql);
				
				hql = "Select tp from TicketCollectionPoint tp WHERE tp.ticketCollectionPointId = " + ticketCollectionPointId + "' AND tp.client.clientCode = '" + clientCode + "' AND tp.deletedAt IS NULL";
				TicketCollectionPoint ticketCollectionPoint= (TicketCollectionPoint)this.swpService.getUniqueRecordByHQL(hql);
				
				hql = "Select tp from VehicleSeatClass tp WHERE tp.vehicleSeatClassId = " + vehicleSeatClassId + "' AND tp.client.clientCode = '" + clientCode + "' AND tp.deletedAt IS NULL";
				VehicleSeatClass vehicleSeatClass= (VehicleSeatClass)this.swpService.getUniqueRecordByHQL(hql);
				
				hql = "Select tp from VehicleSeatAvailability tp WHERE tp.vehicleSeatClassId = " + vehicleSeatClassId + "' AND tp.client.clientCode = '" + clientCode + "' AND tp.deletedAt IS NULL";
				VehicleSeatClass vehicleSeatClass= (VehicleSeatClass)this.swpService.getUniqueRecordByHQL(hql);
				
				Client client = (Client)this.swpService.getUniqueRecordByHQL("Select tp from Client tp where tp.clientCode = '"+ clientCode + "' AND tp.deletedAt IS NULL");
				
				
				hql = "Select tp.purchasedTripSeatId from PurchasedTripSeat tp WHERE tp.purchasedTrip.vehicleTrip.id = " + vehicleTrip.getVehicleTripId() + "' AND tp.client.clientCode = '" + clientCode + "' AND tp.deletedAt IS NULL";
				Collection<Long> purchasedTripSeats= (Collection<Long>)this.swpService.getAllRecordsByHQL(hql);
				Long[] pt = (Long[])purchasedTripSeats.toArray();
				
				ServiceType sType = ServiceType.valueOf(serviceType); 
				
				hql = "Select tp from VehicleSeat tp WHERE tp.vehicleSeatId NOT IN (" + StringUtils.join(pt) + ") AND tp.vehicleSeatClass.id = " + vehicleSeatClassId + " AND tp.client.clientCode = '" + clientCode + "' AND tp.deletedAt IS NULL";
				Collection<VehicleSeat> availableSeats= (Collection<VehicleSeat>)this.swpService.getAllRecordsByHQL(hql);
				
				hql = "Select tp from Transaction tp where tp.orderRef = '" + orderId + "'";
				Collection<Transaction> oldTrxn = (Collection<Transaction>)this.swpService.getAllRecordsByHQL(hql);
				if(oldTrxn.size()>0)
				{
					jsonObject.add("status", ERROR.ORDER_REF_ALREADY_PROCESSED_PREVIOUSLY);
					jsonObject.add("message", "This order has already been processed previously");
					JsonObject jsonObj = jsonObject.build();
		            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
				}
				
				String transactionRef = null;
				while(transactionRef==null)
				{
					String txnRef = RandomStringUtils.random(12, true, true).toUpperCase();
					hql = "Select tp from Transaction tp where tp.transactionRef = '" + txnRef + "'";
					Transaction txn = (Transaction)this.swpService.getUniqueRecordByHQL(hql);
					if(txn==null)
						transactionRef = txnRef;
				}
				
				String username = verifyJ.has("username") ? verifyJ.getString("username") : null;
				//log.inforequestId + "username ==" + (username==null ? "" : username));
				User user = null;
				
				if(username!=null)
				{
					user = (User)this.swpService.getUniqueRecordByHQL("select tp from User tp where tp.username = '" + username + "' AND tp.deletedAt IS NULL AND " +
							"tp.userStatus = " + UserStatus.ACTIVE.ordinal());
				}
				
				
				String messageRequest = hash+"###"+channel+"###"+deviceCode+"###"+orderId+"###"+token+"###"+customerId+"###"+vehicleTripCode+"###"+amountPayable+"###"+cardPan+
						"###"+ticketCollectionPointId+"###"+ipAddress+"###"+clientCode+"###"+vehicleSeatClassId+"###"+childSeatCount+"###"+adultSeatCount+"###"+
						(user==null ? "" : user.getUserId())+"###"+(customer==null ? "" : customer.getCustomerId());
				String messageResponse = transactionRef+"###"+orderId+"###"+deviceCode+"###"+vehicleTrip.getVehicleTripCode()+"###"+vehicleTrip.getClient().getClientCode()
						+"###"+vehicleTrip.getFinalDestinationVehicleSchedule().getScheduleStation().getScheduleDay().getScheduleDayName()+"###"+
						vehicleTrip.getOriginVehicleSchedule().getScheduleStation().getStation().getStationName()+"###"+
						vehicleTrip.getFinalDestinationVehicleSchedule().getScheduleStation().getDepartureTime()+"###"+
						vehicleTrip.getOriginVehicleSchedule().getScheduleStation().getDepartureTime();
				
				PurchasedTrip purchasedTrip = new PurchasedTrip();
				purchasedTrip.setCreatedAt(new Date());
				purchasedTrip.setUpdatedAt(new Date());
				purchasedTrip.setAmountPayable(amtPayable);
				purchasedTrip.setPurchasedTripStatus(PurchasedTripStatus.PENDING);
				purchasedTrip.setTicketCollectionPoint(ticketCollectionPoint);
				purchasedTrip.setVehicleTrip(vehicleTrip);
				purchasedTrip = (PurchasedTrip)(this.swpService.createNewRecord(purchasedTrip));
				
				
				
				Transaction transaction = new Transaction();
				transaction.setTransactionRef(transactionRef);
				transaction.setOrderRef(orderId);
				transaction.setChannel(com.probase.reservationticketingwebservice.enumerations.Channel.POS);
				transaction.setTransactionDate(new Date());
				transaction.setServiceType(ServiceType.valueOf(serviceType));
				transaction.setTransactingUser(user);
				transaction.setTransactingUserName((user.getFirstName()==null ? "" : user.getFirstName()) + (user.getLastName()==null ? "" : ("" + user.getLastName())));
				transaction.setUserId(user.getUserId());
				transaction.setStatus(TransactionStatus.SUCCESS);
				transaction.setCard(tripCard);
				transaction.setDevice(device);
				transaction.setMessageRequest(messageRequest);
				transaction.setMessageResponse(messageResponse);
				transaction.setFixedCharge(tripCard.getCardScheme().getFixedCharge());
				transaction.setTransactionFee(tripCard.getCardScheme().getTransactionCharge());
				transaction.setTransactionAmount(amountPayable);
				transaction.setResponseCode(Response.Status.OK.getStatusCode());
				transaction.setNarration("Debit|TripCard|#" + tripCard.getTripCardNumber() + "|ZMW" + transaction.getTransactionAmount());
				transaction.setDrCardPan(tripCard.getTripCardNumber());
				transaction.setDrCardId(tripCard.getTripCardId());
				transaction.setUpdatedAt(new Date());
				transaction.setCreatedAt(new Date());
				transaction.setPaymentMeans(PaymentMeans.CARD);
				transaction.setTransactionCurrency((client.getCountry().getCurrency()));
				transaction.setPurchasedTrip(purchasedTrip);
				transaction.setCustomer(customer);
				purchasedTrip.setVehiclePaymentCard(tripCard);
				transaction = (Transaction)swpService.createNewRecord(transaction);
				
				transaction.setReceiptNo(purchasedTrip.getVehicleTrip().getVehicleTripCode() + "/" + zeroPadNumber(transaction.getTransactionId()));
				swpService.updateRecord(transaction);
				
				
				
				
				
				jsonObject.add("message", "Trip Purchased successfully");
				jsonObject.add("status", ERROR.GENERAL_SUCCESS);
				jsonObject.add("purchasedTrip", new Gson().toJson(purchasedTrip));
				jsonObject.add("purchasedTripSeats", new Gson().toJson(ptsList));
				jsonObject.add("transaction", new Gson().toJson(transaction));
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
	*/
	
	private String zeroPadNumber(Long id) {
		// TODO Auto-generated method stub
    	String idS = Long.toString(id);
    	String padded = "";
    	for(int i=0; i<(8-(idS.length())); i++)
		{
    		padded = padded + "0";
		}
    	padded = padded + "" + Long.toString(id);
		return padded;
	}

	public Response listPurchasedTrips(Long customerId, String scheduleStationCode,
			Long cardId, Long ticketCollectionPointId,
			Integer startIndex, Integer limit, String token, String requestId,
			String clientCode) {
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
			
			
			
			
			String hql = "Select tp from PurchasedTrip tp where tp.deletedAt IS NULL AND tp.client.clientCode = '"+clientCode+"'";
			if(customerId!=null)
				hql = hql + " AND tp.customer.customerId = " + customerId;
			if(scheduleStationCode!=null)
				hql = hql + " AND tp.vehicleSchedule.scheduleStationCode.scheduleStationCode = '" + scheduleStationCode + "'";
			if(cardId!=null)
				hql = hql + " AND tp.card.id = " + cardId;
			if(ticketCollectionPointId!=null)
				hql = hql + " AND tp.ticketCollectionPoint.id = " + ticketCollectionPointId;
			String sql = "";
			hql = hql + sql;
			Collection<PurchasedTrip> purchasedTrips= (Collection<PurchasedTrip>)this.swpService.getAllRecordsByHQL(hql);
			
			
			
			
			jsonObject.add("message", "Purchased trips listed successfully");
			jsonObject.add("status", ERROR.GENERAL_SUCCESS);
			if(scheduleStationCode!=null)
				jsonObject.add("scheduleStationCode", scheduleStationCode);
			jsonObject.add("purchasedTripList", new Gson().toJson(purchasedTrips));
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
	
	
	
	public Response listTransactions(String vehicleScheduleCode, Long customerId, String vendorCode, String token, String requestId, String clientCode, String startDate, String endDate) {
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
			//log.inforequestId + "username ==" + (username==null ? "" : username));
			String roleCode_ = verifyJ.has("roleCode") ? verifyJ.getString("roleCode") : null;
			//log.inforequestId + "roleCode_ ==" + (roleCode_==null ? "" : roleCode_));
			User user = null;
			RoleType roleCode = null;
			
			if(roleCode_ != null)
			{
				roleCode = RoleType.valueOf(roleCode_);
			}
			

			String hql = "Select tp from Transaction tp where tp.deletedAt IS NULL AND tp.client.clientCode = '"+clientCode+"'";
			
			if(startDate!=null && endDate!=null)
			{
				hql = hql + " AND tp.createdAt >= '" + startDate + "' AND tp.createdAt <= '" + endDate +  "' ";
			}
			
			Vendor vendor = null;
			Wallet wallet = null;
			if(username!=null)
			{
				user = (User)this.swpService.getUniqueRecordByHQL("select tp from User tp where tp.username = '" + username + "' AND tp.deletedAt IS NULL AND " +
						"tp.userStatus = " + UserStatus.ACTIVE.ordinal());
				
				if(user!=null && roleCode.equals(RoleType.VENDOR))
				{
					String hql_ = "Select tp from Vendor tp where tp.vendorId = " + user.getVendor().getVendorId() + " AND tp.deletedAt IS NULL AND " +
							"tp.client.clientId = "+user.getClient().getClientId();
					vendor = (Vendor)swpService.getUniqueRecordByHQL(hql_);
					
					if(vendor!=null)
					{
						hql = hql + " AND (tp.drVendorId = " + vendor.getVendorId() + " OR tp.crVendorId = "+ vendor.getVendorId() +")";
					}
				}
				else if(user!=null && (roleCode.equals(RoleType.OPERATOR) || roleCode.equals(RoleType.ADMIN_STAFF)))
				{
					String hql_ = "Select tp from Vendor tp where tp.vendorCode = '" + vendorCode + "' AND tp.deletedAt IS NULL AND " +
							"tp.client.clientId = "+user.getClient().getClientId();
					vendor = (Vendor)swpService.getUniqueRecordByHQL(hql_);
					
					if(vendor!=null)
					{
						hql = hql + " AND (tp.drVendorId = " + vendor.getVendorId() + " OR tp.crVendorId = "+ vendor.getVendorId() +")";
					}
				}
			}
			//log.info"transaction sql == " + hql);
			
			Customer customer  = null;
			if(customerId!=null)
			{
				String hqlC = "Select tp from Customer tp where tp.deletedAt IS NULL and tp.client.clientCode = '"+clientCode+"' AND " +
						"tp.customerId = " + customerId;
				customer = (Customer)swpService.getUniqueRecordByHQL(hqlC);

				if(customer!=null)
					hql = hql + " AND tp.customer.customerId = " + customerId;
			}
			
			if(vehicleScheduleCode!=null)
			{
				String hql1 = "Select tp.transaction.transactionId from PurchasedTrip tp where tp.vehicleSchedule.scheduleStationCode.scheduleStationCode = '"+ vehicleScheduleCode +"' AND tp.deletedAt IS NULL" +
						" AND tp.client.clientCode = '"+clientCode+"'";
				List<Long> txnIds = (List<Long>)swpService.getAllRecordsByHQL(hql1);
				
				hql1 = "Select tp from VehicleSchedule tp where tp.scheduleStationCode.scheduleStationCode = '"+ vehicleScheduleCode +"' AND tp.deletedAt IS NULL" +
						" AND tp.client.clientCode = '"+clientCode+"'";
				VehicleSchedule vehicleSchedule = (VehicleSchedule)swpService.getUniqueRecordByHQL(hql1);
				if(vehicleSchedule!=null)
					jsonObject.add("vehicleSchedule", new Gson().toJson(vehicleSchedule));
				
				if(txnIds!=null && txnIds.size()>0)
					hql = hql + " AND tp.transactionId IN ("+StringUtils.join(txnIds, ',')+")";
				else
					hql = hql + " AND tp.transactionId IN ('')";
			}
			
			String sql = "";
			hql = hql + sql;
			hql = hql + " ORDER by tp.createdAt DESC";
			
			Collection<Transaction> transactions= (Collection<Transaction>)this.swpService.getAllRecordsByHQL(hql);
			
			
			
			
			jsonObject.add("message", "Transactions listed successfully");
			jsonObject.add("status", ERROR.GENERAL_SUCCESS);
			if(customer!=null)
				jsonObject.add("customer", new Gson().toJson(customer));
			if(vendor!=null)
				jsonObject.add("vendor", new Gson().toJson(vendor));
			jsonObject.add("transactionList", new Gson().toJson(transactions));
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
	
	
	
	
	
	public Response surchargePurchasedTrip(String receiptNo, String deviceCode, String token, String ipAddress, String requestId, String clientCode)
	{
		JsonObjectBuilder jsonObject = Json.createObjectBuilder();
    	
		try{
			if(receiptNo==null ||  token==null || deviceCode==null || clientCode==null)
			{
				jsonObject.add("status", ERROR.INCOMPLETE_PARAMETERS);
				jsonObject.add("message", "Incomplete parameters provided. Provide receiptNo, deviceCode, clientCode, surchargeAmount");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			//log.info">>>..." + deviceCode);
			//log.info">>>..." + receiptNo); 
			//log.info">>>..." + requestId);
			//log.info">>>..." + clientCode); 
			//log.info">>>..." + token);
			
			JSONObject tripTicketDataJSArray = null;
			//String transactionRef = null;
			
			if(token==null)
			{
				jsonObject.add("status", ERROR.INSUFFICIENT_PRIVILEDGES);
				jsonObject.add("message", "You do not have the priviledges to carry out this action");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			
			JSONObject jsObj = new JSONObject();
			

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
			Boolean skip = Boolean.FALSE;
			RoleType roleCode = null;
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
					
					//roleCode = user.getRoleCode();
				}
			}
			
			if(!roleCode.equals(RoleType.ADMIN_STAFF) && !roleCode.equals(RoleType.OPERATOR))
			{
				//log.info"5");
				jsonObject.add("status", ERROR.INSUFFICIENT_PRIVILEDGES);
				jsonObject.add("message", "You do not have the priviledges to carry out this action");
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



			hql = "Select tp from PurchasedTrip tp where tp.receiptNo = '"+receiptNo+"' AND tp.purchasedTripStatus = " + PurchasedTripStatus.PENDING.ordinal() + " AND " +
					"tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL";
			PurchasedTrip purchasedTrip = (PurchasedTrip)swpService.getUniqueRecordByHQL(hql);
			if(purchasedTrip==null)
			{
				jsonObject.add("status", ERROR.PURCHASED_TRIP_NOT_FOUND);
				jsonObject.add("message", "Purchased trip could not be found");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
				
			}
			
			String transactionRef = RandomStringUtils.randomNumeric(8);
			String newOrderRef = RandomStringUtils.randomNumeric(8);
			String messageRequest = deviceCode+"###"+transactionRef+"###"+token
					+"###"+ipAddress+"###"+clientCode;
			String messageResponse = "";
			Double fixedCharge = 0.00;
			Double transactionFee = 0.00;
			
			
			Double surchargeAmount = purchasedTrip.getAmountPayable()/2;
			
			Transaction transaction = new Transaction();
			transaction.setTransactionRef(transactionRef);
			transaction.setOrderRef(device.getDeviceCode() + "-" + newOrderRef);
			transaction.setChannel(com.probase.reservationticketingwebservice.enumerations.Channel.CASH);
			transaction.setTransactionDate(new Date());
			transaction.setServiceType(ServiceType.CASH_SURCHARGE);
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
			transaction.setTransactionAmount(surchargeAmount);
			transaction.setResponseCode(Response.Status.OK.getStatusCode());
			transaction.setNarration("Surcharge|#" + transactionRef + "|ZMW" + transaction.getTransactionAmount());
			transaction.setDrCardPan(null);
			transaction.setDrCardId(null);
			transaction.setUpdatedAt(new Date());
			transaction.setCreatedAt(new Date());
			transaction.setPaymentMeans(PaymentMeans.CASH);
			transaction.setClient(client);						
			transaction.setTransactionCurrency("ZMW");
			transaction = (Transaction)swpService.createNewRecord(transaction);
			
			purchasedTrip.setSurchargedAmount((purchasedTrip.getSurchargedAmount()==null ? surchargeAmount : purchasedTrip.getSurchargedAmount()) + surchargeAmount);
			purchasedTrip.setSurchargeTransaction(transaction);
			swpService.updateRecord(purchasedTrip);
			
			jsonObject.add("status", ERROR.GENERAL_SUCCESS);
			jsonObject.add("message", "Surcharged Amount registered for the purchased trip");
			jsonObject.add("transaction", new Gson().toJson(transaction));
			jsonObject.add("purchasedTrip", new Gson().toJson(purchasedTrip));
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
	
	
	
	public Response listPurchasedTripsByTransaction(String orderRef, String token, String requestId, String clientCode, String startDate, String endDate) {
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
			//log.inforequestId + "username ==" + (username==null ? "" : username));
			String roleCode_ = verifyJ.has("roleCode") ? verifyJ.getString("roleCode") : null;
			//log.inforequestId + "roleCode_ ==" + (roleCode_==null ? "" : roleCode_));
			User user = null;
			RoleType roleCode = null;
			
			if(roleCode_ != null)
			{
				roleCode = RoleType.valueOf(roleCode_);
			}
			

			String hql = "Select tp from PurchasedTrip tp where tp.deletedAt IS NULL AND tp.client.clientCode = '"+clientCode+"'";
			hql = hql + " AND lower(tp.transaction.orderRef) = '" + orderRef.toLowerCase() + "'";
			if(startDate!=null && endDate!=null)
			{
				hql = hql + " AND tp.createdAt >= '" + startDate + "' AND tp.createdAt <= '" + endDate +  "' ";
			}
			
			Vendor vendor = null;
			Wallet wallet = null;
			if(username!=null)
			{
				user = (User)this.swpService.getUniqueRecordByHQL("select tp from User tp where tp.username = '" + username + "' AND tp.deletedAt IS NULL AND " +
						"tp.userStatus = " + UserStatus.ACTIVE.ordinal());
				
				if(user!=null && roleCode.equals(RoleType.VENDOR))
				{
					String hql_ = "Select tp from Vendor tp where tp.vendorId = " + user.getVendor().getVendorId() + " AND tp.deletedAt IS NULL AND " +
							"tp.client.clientId = '"+user.getClient().getClientId()+"'";
					vendor = (Vendor)swpService.getUniqueRecordByHQL(hql_);
					
					if(vendor!=null)
					{
						hql = hql + " AND tp.transaction.drVendorId = " + vendor.getVendorId();
					}
				}
			}
			//log.info"transaction sql == " + hql);
			
			
			
			
			String sql = "";
			hql = hql + sql;
			Collection<PurchasedTrip> purchasedTrips= (Collection<PurchasedTrip>)this.swpService.getAllRecordsByHQL(hql);
			
			
			
			
			jsonObject.add("message", "Purchased Trips listed successfully");
			jsonObject.add("status", ERROR.GENERAL_SUCCESS);
			jsonObject.add("transactionOrderRef", orderRef);
			jsonObject.add("purchasedTripsList", new Gson().toJson(purchasedTrips));
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
	
	
	
	
	public Response listPurchasedTripSeats(Long purchasedTripId, String token, String requestId,
			String clientCode) {
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
			
			PurchasedTrip purchasedTrip = null;
			if(purchasedTripId!=null)
			{
				String hql = "Select tp from PurchasedTrip tp where tp.deletedAt IS NULL AND tp.client.clientCode = '"+clientCode+"' AND " +
						"tp.purchasedTripId = " + purchasedTripId;
				//log.info"hql --" + hql);
				purchasedTrip = (PurchasedTrip)this.swpService.getUniqueRecordByHQL(hql);
			}
			
			String hql = "Select tp from PurchasedTripSeat tp where tp.deletedAt IS NULL AND tp.client.clientCode = '"+clientCode+"'";
			if(purchasedTripId!=null)
				hql = hql + " AND tp.purchasedTrip.purchasedTripId = " + purchasedTripId;
			//log.info"1. hql --" + hql);
			String sql = "";
			hql = hql + sql;
			Collection<PurchasedTripSeat> purchasedTripSeats= (Collection<PurchasedTripSeat>)this.swpService.getAllRecordsByHQL(hql);
			
			
			
			
			jsonObject.add("message", "Purchased trip seats listed successfully");
			jsonObject.add("status", ERROR.GENERAL_SUCCESS);
			if(purchasedTrip!=null)
				jsonObject.add("purchasedTrip", new Gson().toJson(purchasedTrip));
			jsonObject.add("purchasedTripSeatList", new Gson().toJson(purchasedTripSeats));
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

	public Response deletePurchasedTrip(Long purchasedTripId, String token,
			String requestId, String clientCode, String ipAddress) {
		// TODO Auto-generated method stub
		JsonObjectBuilder jsonObject = Json.createObjectBuilder();
    	
    	
		try{
			
			if(purchasedTripId==null || clientCode==null)
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
			//User user = null;
			RoleType roleCode = null;
			
			if(roleCode_ != null)
			{
				roleCode = RoleType.valueOf(roleCode_);
			}
			
			User user = null; 
			if(username!=null)
			{
				user = (User)this.swpService.getUniqueRecordByHQL("select tp from User tp where tp.username = '" + username + "' AND tp.deletedAt IS NULL AND " +
					"tp.userStatus = " + UserStatus.ACTIVE.ordinal());
			}
			
			
			String hql = "Select tp from PurchasedTrip tp WHERE tp.purchasedTripId = " + purchasedTripId + " AND tp.client.clientCode = '" + clientCode + "' AND tp.deletedAt IS NULL";
			PurchasedTrip purchasedTrip= (PurchasedTrip)this.swpService.getUniqueRecordByHQL(hql);
			
			if(purchasedTrip!=null)
			{
				purchasedTrip.setDeletedAt(new Date());
				swpService.updateRecord(purchasedTrip);
				

				AuditTrail ad = UtilityHelper.createAuditTrailEntry(ipAddress, RequestType.DELETE_PURCHASED_TRIP, requestId, this.swpService, 
						verifyJ.has("username") ? verifyJ.getString("username") : null, purchasedTrip.getPurchasedTripId(), PurchasedTrip.class.getName(), 
						"Delete Purchased Trip. Purchased Trip Receipt No: " + purchasedTrip.getReceiptNo() + " | Deleted By: " + user.getFirstName() + " " + user.getLastName(), clientCode);
			}
			jsonObject.add("message", "Purchased trip deleted successfully");
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
	
	
	
	
	public Response listAvailableTrips(String token, String departureStationCode, String arrivalStationCode, 
			String departureTime, String returnTime, Integer hoursAdd,  String tripClass, Boolean returnTrip, String requestId, String ipAddress, String clientCode) {
		// TODO Auto-generated method stub
		JsonObjectBuilder jsonObject = Json.createObjectBuilder();
    	
    	
		try{
			
			if(departureStationCode==null || arrivalStationCode==null || tripClass==null || (returnTrip.equals(Boolean.TRUE) && returnTime==null))
			{
				jsonObject.add("status", ERROR.INCOMPLETE_PARAMETERS);
				jsonObject.add("message", "Incomplete Parameters provided in request.");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
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
		            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
				}
			}
			
			String daSql = "Select ta.scheduleStationId from ScheduleStation ta";
			if(departureStationCode!=null)
			{
				//Date departureTimeFormatted = (new SimpleDateFormat("yyyy-MM-dd HH:mm")).parse(departureTime);
				daSql = daSql + " WHERE (lower(ta.station.stationCode) = '"+departureStationCode.toLowerCase()+"' OR lower(ta.station.stationCode) = '"+arrivalStationCode.toLowerCase()+"')" +
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
					"("+StringUtils.join(scheduleStationIds, ", ")+")" +
					" GROUP BY tp.scheduleStationCode.scheduleStationCode";
			//AND tp.vehicleTripRoutingOriginSchedule.scheduleStation.departureTime <= '"+bufferDepartureTime+"'
			// AND tp.vehicleTripRoutingId IN ("+StringUtils.join(vehicleTripIds, ',')+")
			//log.info"---> " + daSql);
			List<String> scheduleStationCodes = (List<String>)this.swpService.getAllRecordsByHQL(daSql);
			//log.info"---> scheduleStationCodes size = " + scheduleStationCodes.size());
			////log.info"---> scheduleStationCodes = " + new Gson().toJson(scheduleStationCodes));
			
			if(scheduleStationCodes.size()>0)
			{
				daSql = "Select tp from Station tp where tp.stationCode = '"+departureStationCode+"' AND " +
							"tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL";
				Station departureStation = (Station)swpService.getUniqueRecordByHQL(daSql);

				daSql = "Select tp from Station tp where tp.stationCode = '"+arrivalStationCode+"' AND " +
							"tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL";
				Station arrivalStation = (Station)swpService.getUniqueRecordByHQL(daSql);
				
				daSql = "Select tp from TripZoneStation tp where tp.station.stationCode = '"+arrivalStationCode+"' AND " +
						"tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL";
				TripZoneStation arrivalTripZoneStation = (TripZoneStation)swpService.getUniqueRecordByHQL(daSql);
				
				daSql = "Select tp from TripZoneStation tp where tp.station.stationCode = '"+departureStationCode+"' AND " +
						"tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL";
				TripZoneStation depatureTripZoneStation = (TripZoneStation)swpService.getUniqueRecordByHQL(daSql);
				

				//log.info"2 .... >>>>>" + arrivalTripZoneStation.getTripZone().getRouteOrder());
				//log.info"2 .... >>>>>" + depatureTripZoneStation.getTripZone().getRouteOrder());
				
				Integer tripRouteOrderdifference =  depatureTripZoneStation.getTripZone().getRouteOrder() - arrivalTripZoneStation.getTripZone().getRouteOrder();
				tripRouteOrderdifference = Math.abs(tripRouteOrderdifference);
				tripRouteOrderdifference = tripRouteOrderdifference + 1;
				
				
				daSql = "Select tp from VehicleTripPrice tp where tp.finalDestinationTripZone.routeOrder = " + tripRouteOrderdifference +  
							" AND tp.deletedAt is NULL and tp.client.clientCode = '"+clientCode+"' " +
							" AND lower(tp.vehicleSeatClass.vehicleSeatClassCode) = '"+tripClass.toLowerCase() +"'";
				//log.info"daSQL ===> " + daSql);
				Collection<VehicleTripPrice> vehicleTripPrices = (Collection<VehicleTripPrice>)swpService.getAllRecordsByHQL(daSql);
				//log.info"v size ---> " + vehicleTripPrices.size());
				
				if(vehicleTripPrices.size()==0)
				{
					jsonObject.add("status", ERROR.NO_TRIPS_FOUND_FOR_SPECIFIED_DATES);
					jsonObject.add("message", "No pricing Found For the Specified trip");
					JsonObject jsonObj = jsonObject.build();
		            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
				}
				if(vehicleTripPrices.size()>0)
				{
					String hql = "Select tp from VehicleSchedule tp where " +
							"tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL";
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
					JSONArray jsonTicketPrices = new JSONArray();
					JSONObject jsonObjectEntry = null;
					
					
					Iterator<VehicleTripPrice> itVTP = vehicleTripPrices.iterator();
					while(itVTP.hasNext())
					{
						VehicleTripPrice vtp = itVTP.next();
						JSONObject vtpJS = new JSONObject();
						vtpJS.put("priceType", vtp.getPriceType().name());
						vtpJS.put("amount", vtp.getAmount());
						vtpJS.put("tripClass", vtp.getVehicleSeatClass().getVehicleSeatClassName());
						vtpJS.put("tripPriceId", vtp.getVehicleTripPriceId());
						jsonTicketPrices.put(vtpJS);
					}
					
					while(iter.hasNext())
					{
						ScheduleStation scheduleStation = iter.next();
						String scheduleStationCode = scheduleStation.getScheduleStationCode().getScheduleStationCode();
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
						//log.info"departureStationCode = " + departureStationCode);
						//log.info"arrivalStationCode = " + arrivalStationCode);
						//log.info"departTime = " + departTime);
						//log.info"arrivalTime = " + arrivalTime);
						//log.info"-------------");
						
						if(departTime!=null && arrivalTime!=null && departTime.before(arrivalTime))
							jsonTrips_.put(key, jsonTrips.get(key));
						
						
					}
					
					
					//log.info"##------------------------");
					//log.infojsonTrips_.toString());
					//log.info"##------------------------");
					
					
					
					
					String vehicleTripListReturnStr = null;
					String ticketPricesStr = null;
					int returnStatus = ERROR.GENERAL_SYSTEM_ERROR;
					String returnMessage = "Vehicle return trips not found";
					
					if(returnTrip!=null && returnTrip.equals(Boolean.TRUE))
					{
						String returnTripString = (String)(listAvailableTrips(token, arrivalStationCode, departureStationCode, 
								returnTime, null, hoursAdd,  tripClass, Boolean.FALSE, requestId, ipAddress, clientCode).getEntity());
						JSONObject returnTripJSONObject = new JSONObject(returnTripString);
						if(returnTripJSONObject.has("status") && returnTripJSONObject.getInt("status")==0)
						{
							vehicleTripListReturnStr = returnTripJSONObject.getString("vehicleTripList");
							ticketPricesStr = returnTripJSONObject.getString("ticketPrices");
							returnStatus = returnTripJSONObject.getInt("status");
							returnMessage = returnTripJSONObject.getString("message");
						}
						else
						{
							returnStatus = returnTripJSONObject.getInt("status");
							returnMessage = returnTripJSONObject.getString("message");
						}
						
					}
					
					
					if(jsonTrips_.length()>0)
					{
						jsonObject.add("message", "Vehicle Trips listed successfully");
						jsonObject.add("status", ERROR.GENERAL_SUCCESS);
						jsonObject.add("returnMessage", returnMessage);
						jsonObject.add("returnStatus", returnStatus);
						//jsonObject.add("token", tripToken);
						jsonObject.add("vehicleTripList", (jsonTrips_.toString()));
						jsonObject.add("ticketPrices", jsonTicketPrices.toString());
						if(vehicleTripListReturnStr!=null)
							jsonObject.add("vehicleTripReturnList", vehicleTripListReturnStr);
						if(vehicleTripListReturnStr!=null)
							jsonObject.add("ticketReturnPrices", ticketPricesStr);
					}
					else
					{
						jsonObject.add("message", "Vehicle Trips searched for not available");
						jsonObject.add("status", ERROR.TRIP_NON_AVAILABLE);
						//jsonObject.add("token", tripToken);
						jsonObject.add("vehicleTripList", (jsonTrips_.toString()));
					}
					JsonObject jsonObj = jsonObject.build();
		            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
					
				}
				else
				{
					jsonObject.add("message", "Vehicle Trips not found matching the details provided");
					jsonObject.add("status", ERROR.NO_TRIPS_FOUND_FOR_SPECIFIED_DATES);
					jsonObject.add("token", token);
					JsonObject jsonObj = jsonObject.build();
		            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
				}
				
			}
			else
			{
				jsonObject.add("message", "Vehicle Trips not found matching the details provided");
				jsonObject.add("status", ERROR.NO_TRIPS_FOUND_FOR_SPECIFIED_DATES);
				jsonObject.add("token", token);
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
	
	
	
	
	public Response searchAvailableTrips(String deviceCode, String token, String departureStationCode, String arrivalStationCode, String passengerDetails, 
			String departureTime, String returnTime, Integer hoursAdd,  String tripClass, Boolean returnTrip, String requestId, String ipAddress, String clientCode) {
		// TODO Auto-generated method stub
		JsonObjectBuilder jsonObject = Json.createObjectBuilder();
    	
    	
		try{
			Integer inwardStatus = null;
			String inwardMessage = null;
			Boolean inwardSeatsAvailabilityStatus = null;
			String inwardVehicleTripList = null;
			String inwardTicketPrices = null;
			Integer outwardStatus = null;
			String outwardMessage = null;
			Boolean outwardSeatsAvailabilityStatus = null;
			String outwardVehicleTripList = null;
			String outwardTicketPrices = null;
			String seatsAlloted = null;
			
			if(deviceCode==null || departureStationCode==null || arrivalStationCode==null || tripClass==null || passengerDetails==null)
			{
				jsonObject.add("status", ERROR.INCOMPLETE_PARAMETERS);
				jsonObject.add("message", "Incomplete Parameters provided in request.");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
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
			
			JsonObject outwardTrip = findAvailableTrips(token, departureStationCode, arrivalStationCode, passengerDetails, null, 
					departureTime, hoursAdd, tripClass, requestId, ipAddress, clientCode);
			outwardStatus = outwardTrip!=null && outwardTrip.containsKey("status") ? outwardTrip.getInt("status") : ERROR.GENERAL_SYSTEM_ERROR;
			outwardMessage = outwardTrip!=null && outwardTrip.containsKey("message") ? outwardTrip.getString("message") : "No Outward Trips Available"; 
			Date earliestArrivalTime = null;
			if(outwardTrip!=null && outwardTrip.containsKey("status") && outwardTrip.getInt("status")==0)
			{
				outwardSeatsAvailabilityStatus = outwardTrip.getBoolean("seatsAvailabilityStatus");
				outwardVehicleTripList = outwardTrip.getString("vehicleTripList");
				outwardTicketPrices = outwardTrip.getString("ticketPrices");
				seatsAlloted = outwardTrip.getString("seatsAlloted");
				
				
				JSONObject vtl = new JSONObject(outwardVehicleTripList);
				Iterator<String> vtlIt = vtl.keys();
				while(vtlIt.hasNext())
				{
					String key = vtlIt.next();
					JSONObject entry = vtl.getJSONObject(key);
					if(entry.has("arrivalTime"))
					{
						String arrivalTimeStr = entry.getString("arrivalTime");
						//log.info"arrivalTime -- " + arrivalTimeStr);
						Date arrivalTime = (new SimpleDateFormat("yyyy-MM-dd HH:mm")).parse(arrivalTimeStr);
						//log.info"arrivalTime -- " + arrivalTime);
						if(earliestArrivalTime==null)
						{
							earliestArrivalTime = arrivalTime;
						}
						else
						{
							if(earliestArrivalTime.after(arrivalTime))
							{
								earliestArrivalTime = arrivalTime;
							}
						}
						
					}
				}

				//log.info"earliestArrivalTime --- " + earliestArrivalTime);

				jsonObject.add("status", ERROR.GENERAL_SUCCESS);
				jsonObject.add("outwardTripMessage", "Outward Trips listed successfully");
				jsonObject.add("outwardTripStatus", ERROR.GENERAL_SUCCESS);
				jsonObject.add("outwardTripSeatsAvailabilityStatus", outwardSeatsAvailabilityStatus);
				jsonObject.add("outwardVehicleTripList", outwardVehicleTripList);
				jsonObject.add("outwardSeatsAlloted", seatsAlloted);
				jsonObject.add("outwardTicketPrices", outwardTicketPrices);
				
				if(returnTrip!=null && returnTrip.equals(Boolean.TRUE))
				{
					JsonObject inwardTrip = findAvailableTrips(token, arrivalStationCode, departureStationCode, passengerDetails, earliestArrivalTime, 
							returnTime, hoursAdd, tripClass, requestId, ipAddress, clientCode);
					inwardStatus = inwardTrip!=null && inwardTrip.containsKey("status") ? inwardTrip.getInt("status") : null;
					inwardMessage = inwardTrip!=null && inwardTrip.containsKey("message") ? inwardTrip.getString("message") : null; 
					if(inwardTrip!=null && inwardTrip.containsKey("status") && inwardTrip.getInt("status")==0)
					{
						inwardSeatsAvailabilityStatus = inwardTrip.getBoolean("seatsAvailabilityStatus");
						inwardVehicleTripList = inwardTrip.getString("vehicleTripList");
						inwardTicketPrices = inwardTrip.getString("ticketPrices");
						seatsAlloted = inwardTrip.getString("seatsAlloted");
						jsonObject.add("inwardTripMessage", "Inward Trips listed successfully");
						jsonObject.add("inwardTripStatus", ERROR.GENERAL_SUCCESS);
						jsonObject.add("inwardTripSeatsAvailabilityStatus", inwardSeatsAvailabilityStatus);
						jsonObject.add("inwardVehicleTripList", inwardVehicleTripList);
						jsonObject.add("inwardTicketPrices", inwardTicketPrices);
						jsonObject.add("inwardSeatsAlloted", seatsAlloted);
					}
					else
					{
						jsonObject.add("inwardTripMessage", inwardMessage==null ? "Inward trip not found" : inwardMessage);
						jsonObject.add("inwardTripStatus", inwardStatus==null ? ERROR.TRIP_NOT_FOUND : inwardStatus);
						if(inwardSeatsAvailabilityStatus!=null)
							jsonObject.add("inwardTripSeatsAvailabilityStatus", inwardSeatsAvailabilityStatus);
						if(inwardVehicleTripList!=null)
							jsonObject.add("inwardVehicleTripList", inwardVehicleTripList);
						if(inwardTicketPrices!=null)
							jsonObject.add("inwardTicketPrices", inwardTicketPrices);
					}
				}
			}
			else
			{
				jsonObject.add("status", ERROR.TRIP_NOT_FOUND);
				jsonObject.add("outwardTripMessage", outwardMessage==null ? "Outward trip not found" : outwardMessage);
				jsonObject.add("outwardTripStatus", outwardStatus==null ? ERROR.TRIP_NOT_FOUND : outwardStatus);
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
	
	
	public Response searchAvailableTripsAndLockdown(String deviceCode, String token, String departureStationCode, String arrivalStationCode, String passengerDetails, 
			String departureTime, String returnTime, Integer hoursAdd,  String tripClass, Boolean returnTrip, String requestId, String ipAddress, String clientCode) {
		// TODO Auto-generated method stub
		JsonObjectBuilder jsonObject = Json.createObjectBuilder();
    	
    	
		try{
			Integer inwardStatus = null;
			String inwardMessage = null;
			Boolean inwardSeatsAvailabilityStatus = null;
			String inwardVehicleTripList = null;
			String inwardTicketPrices = null;
			Integer outwardStatus = null;
			String outwardMessage = null;
			Boolean outwardSeatsAvailabilityStatus = null;
			String outwardVehicleTripList = null;
			String outwardTicketPrices = null;
			String seatsAlloted = null;
			
			if(deviceCode==null || departureStationCode==null || arrivalStationCode==null || tripClass==null || passengerDetails==null)
			{
				jsonObject.add("status", ERROR.INCOMPLETE_PARAMETERS);
				jsonObject.add("message", "Incomplete Parameters provided in request.");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
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
			
			JsonObject outwardTrip = findAvailableTripsAndLockdown(token, departureStationCode, arrivalStationCode, passengerDetails, null, 
					departureTime, hoursAdd, tripClass, requestId, ipAddress, clientCode);
			outwardStatus = outwardTrip!=null && outwardTrip.containsKey("status") ? outwardTrip.getInt("status") : ERROR.GENERAL_SYSTEM_ERROR;
			outwardMessage = outwardTrip!=null && outwardTrip.containsKey("message") ? outwardTrip.getString("message") : "No Outward Trips Available"; 
			Date earliestArrivalTime = null;
			if(outwardTrip!=null && outwardTrip.containsKey("status") && outwardTrip.getInt("status")==0)
			{
				outwardSeatsAvailabilityStatus = outwardTrip.getBoolean("seatsAvailabilityStatus");
				outwardVehicleTripList = outwardTrip.getString("vehicleTripList");
				outwardTicketPrices = outwardTrip.getString("ticketPrices");
				seatsAlloted = outwardTrip.getString("seatsAlloted");
				
				
				JSONObject vtl = new JSONObject(outwardVehicleTripList);
				Iterator<String> vtlIt = vtl.keys();
				while(vtlIt.hasNext())
				{
					String key = vtlIt.next();
					JSONObject entry = vtl.getJSONObject(key);
					if(entry.has("arrivalTime"))
					{
						String arrivalTimeStr = entry.getString("arrivalTime");
						//log.info"arrivalTime -- " + arrivalTimeStr);
						Date arrivalTime = (new SimpleDateFormat("yyyy-MM-dd HH:mm")).parse(arrivalTimeStr);
						//log.info"arrivalTime -- " + arrivalTime);
						if(earliestArrivalTime==null)
						{
							earliestArrivalTime = arrivalTime;
						}
						else
						{
							if(earliestArrivalTime.after(arrivalTime))
							{
								earliestArrivalTime = arrivalTime;
							}
						}
						
					}
				}

				//log.info"earliestArrivalTime --- " + earliestArrivalTime);

				jsonObject.add("status", ERROR.GENERAL_SUCCESS);
				jsonObject.add("outwardTripMessage", "Outward Trips listed successfully");
				jsonObject.add("outwardTripStatus", ERROR.GENERAL_SUCCESS);
				jsonObject.add("outwardTripSeatsAvailabilityStatus", outwardSeatsAvailabilityStatus);
				jsonObject.add("outwardVehicleTripList", outwardVehicleTripList);
				jsonObject.add("outwardSeatsAlloted", seatsAlloted);
				jsonObject.add("outwardTicketPrices", outwardTicketPrices);
				
				if(returnTrip!=null && returnTrip.equals(Boolean.TRUE))
				{
					JsonObject inwardTrip = findAvailableTripsAndLockdown(token, arrivalStationCode, departureStationCode, passengerDetails, earliestArrivalTime, 
							returnTime, hoursAdd, tripClass, requestId, ipAddress, clientCode);
					inwardStatus = inwardTrip!=null && inwardTrip.containsKey("status") ? inwardTrip.getInt("status") : null;
					inwardMessage = inwardTrip!=null && inwardTrip.containsKey("message") ? inwardTrip.getString("message") : null; 
					if(inwardTrip!=null && inwardTrip.containsKey("status") && inwardTrip.getInt("status")==0)
					{
						inwardSeatsAvailabilityStatus = inwardTrip.getBoolean("seatsAvailabilityStatus");
						inwardVehicleTripList = inwardTrip.getString("vehicleTripList");
						inwardTicketPrices = inwardTrip.getString("ticketPrices");
						seatsAlloted = inwardTrip.getString("seatsAlloted");
						jsonObject.add("inwardTripMessage", "Inward Trips listed successfully");
						jsonObject.add("inwardTripStatus", ERROR.GENERAL_SUCCESS);
						jsonObject.add("inwardTripSeatsAvailabilityStatus", inwardSeatsAvailabilityStatus);
						jsonObject.add("inwardVehicleTripList", inwardVehicleTripList);
						jsonObject.add("inwardTicketPrices", inwardTicketPrices);
						jsonObject.add("inwardSeatsAlloted", seatsAlloted);
					}
					else
					{
						jsonObject.add("inwardTripMessage", inwardMessage);
						jsonObject.add("inwardTripStatus", inwardStatus);
						if(inwardSeatsAvailabilityStatus!=null)
							jsonObject.add("inwardTripSeatsAvailabilityStatus", inwardSeatsAvailabilityStatus);
						if(inwardVehicleTripList!=null)
							jsonObject.add("inwardVehicleTripList", inwardVehicleTripList);
						if(inwardTicketPrices!=null)
							jsonObject.add("inwardTicketPrices", inwardTicketPrices);
					}
				}
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
	
	
	
	
	private JsonObject findAvailableTrips(String token, String departureStationCode, String arrivalStationCode, String passengerDetails, Date minimumReturnDate,
			String departureTime, Integer hoursAdd,  String tripClass, String requestId, String ipAddress, String clientCode) {
		// TODO Auto-generated method stub
		JsonObjectBuilder jsonObject = Json.createObjectBuilder();
    	String tripBookingToken = null;
    	
		try{
			
			if(departureStationCode==null || arrivalStationCode==null || tripClass==null || passengerDetails==null)
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
			if(departureStationCode!=null)
			{
				//Date departureTimeFormatted = (new SimpleDateFormat("yyyy-MM-dd HH:mm")).parse(departureTime);
				daSql = daSql + " WHERE (lower(ta.station.stationCode) = '"+departureStationCode.toLowerCase()+"' OR lower(ta.station.stationCode) = '"+arrivalStationCode.toLowerCase()+"')" +
						" AND ta.client.clientCode = '"+clientCode+"') ";
			}
			
			//log.info"depart --- " + daSql);
			List<Long> scheduleStationIds= (List<Long>)this.swpService.getAllRecordsByHQL(daSql);
			//log.info"scheduleStationIds size --- " + scheduleStationIds.size());
			//log.info"departureTime --- " + departureTime);
			//log.info"departureTime.split()[0] == " + (departureTime.split(" ")[0]));
			
			
			String departureDay = departureTime.split(" ")[0];										//min - 2019-01-31
			departureDay = departureDay + " 00:00";													//2019-01-30 00:00
			Date departureDate = new SimpleDateFormat("yyyy-MM-dd").parse(departureDay);			//2019-01-30
			String bufferDepartureDateStr = null;
			
			if(minimumReturnDate!=null && departureDate.before(minimumReturnDate))
			{
				Date bufferDepartureDate = null;
				departureDate = minimumReturnDate;
				departureDay = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(departureDate);
				Calendar cal = Calendar.getInstance();
				cal.setTime(departureDate);
				if(hoursAdd!=null)
					cal.add(Calendar.HOUR, hoursAdd);
				else
					cal.add(Calendar.HOUR, 24);
				
				bufferDepartureDate = cal.getTime();
				bufferDepartureDateStr = new SimpleDateFormat("yyyy-MM-dd").format(bufferDepartureDate);
				String bufferDepartureTime = bufferDepartureDateStr + " 23:59";
			}
			else
			{
				
				Date bufferDepartureDate = null;
				Calendar cal = Calendar.getInstance();
				cal.setTime(departureDate);
				if(hoursAdd!=null)
					cal.add(Calendar.HOUR, hoursAdd);
				else
					cal.add(Calendar.HOUR, 24);
				
				bufferDepartureDate = cal.getTime();
				bufferDepartureDateStr = new SimpleDateFormat("yyyy-MM-dd").format(bufferDepartureDate);
				String bufferDepartureTime = bufferDepartureDateStr + " 23:59";
			}
			daSql = "Select tp.scheduleStationCode.scheduleStationCode" +
					" from ScheduleStation tp where (tp.departureTime >= '"+departureDay+"' AND " +
					"tp.departureTime <= '"+bufferDepartureDateStr+" 23:59') AND tp.scheduleStationId IN " +
					"("+StringUtils.join(scheduleStationIds, ", ")+") AND tp.scheduleStationCode.passengerEnabled = 1 " +
					" GROUP BY tp.scheduleStationCode.scheduleStationCode";
			//AND tp.vehicleTripRoutingOriginSchedule.scheduleStation.departureTime <= '"+bufferDepartureTime+"'
			// AND tp.vehicleTripRoutingId IN ("+StringUtils.join(vehicleTripIds, ',')+")
			//log.info"---> " + daSql);
			List<String> scheduleStationCodes = (List<String>)this.swpService.getAllRecordsByHQL(daSql);
			//log.info"---> scheduleStationCodes size = " + scheduleStationCodes.size());
			////log.info"---> scheduleStationCodes = " + new Gson().toJson(scheduleStationCodes));
			
			if(scheduleStationCodes.size()>0)
			{
				daSql = "Select tp from Station tp where tp.stationCode = '"+departureStationCode+"' AND " +
							"tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL";
				Station departureStation = (Station)swpService.getUniqueRecordByHQL(daSql);

				daSql = "Select tp from Station tp where tp.stationCode = '"+arrivalStationCode+"' AND " +
							"tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL";
				//log.info"daSql == " + daSql);
				Station arrivalStation = (Station)swpService.getUniqueRecordByHQL(daSql);
				
				daSql = "Select tp from TripZoneStation tp where tp.station.stationCode = '"+arrivalStationCode+"' AND " +
						"tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL";
				//log.info"daSql == " + daSql);
				TripZoneStation arrivalTripZoneStation = (TripZoneStation)swpService.getUniqueRecordByHQL(daSql);

				
				daSql = "Select tp from TripZoneStation tp where tp.station.stationCode = '"+departureStationCode+"' AND " +
						"tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL";
				//log.info"daSql == " + daSql);
				TripZoneStation depatureTripZoneStation = (TripZoneStation)swpService.getUniqueRecordByHQL(daSql);
				
				
				if(arrivalTripZoneStation==null || depatureTripZoneStation==null)
				{
					jsonObject.add("status", ERROR.NO_TRIPS_FOUND_FOR_SPECIFIED_DATES);
					jsonObject.add("message", "No pricing Found For the Specified trip");
					JsonObject jsonObj = jsonObject.build();
		            return jsonObj;
				}
				

				//log.info"3 .... >>>>>" + arrivalTripZoneStation.getTripZone().getRouteOrder());
				//log.info"3 .... >>>>>" + depatureTripZoneStation.getTripZone().getRouteOrder());
				
				Integer tripRouteOrderdifference =  depatureTripZoneStation.getTripZone().getRouteOrder() - arrivalTripZoneStation.getTripZone().getRouteOrder();
				tripRouteOrderdifference = Math.abs(tripRouteOrderdifference);
				tripRouteOrderdifference = tripRouteOrderdifference + 1;
				
				daSql = "Select tp from VehicleTripPrice tp where tp.finalDestinationTripZone.routeOrder = " + tripRouteOrderdifference + 
							" AND tp.deletedAt is NULL and tp.client.clientCode = '"+clientCode+"' " +
							" AND lower(tp.vehicleSeatClass.vehicleSeatClassCode) = '"+tripClass.toLowerCase() +"'";
				//log.info"daSQL ===> " + daSql);
				Collection<VehicleTripPrice> vehicleTripPrices = (Collection<VehicleTripPrice>)swpService.getAllRecordsByHQL(daSql);
				//log.info"v size ---> " + vehicleTripPrices.size());
				
				if(vehicleTripPrices.size()==0)
				{
					jsonObject.add("status", ERROR.NO_TRIPS_FOUND_FOR_SPECIFIED_DATES);
					jsonObject.add("message", "No pricing Found For the Specified trip");
					JsonObject jsonObj = jsonObject.build();
		            return jsonObj;
				}
				if(vehicleTripPrices.size()>0)
				{
					String hql = "Select tp from VehicleSchedule tp where " +
							"tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL AND tp.passengerEnabled = 1";
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
					JSONArray jsonTicketPrices = new JSONArray();
					JSONObject jsonObjectEntry = null;
					
					
					Iterator<VehicleTripPrice> itVTP = vehicleTripPrices.iterator();
					while(itVTP.hasNext())
					{
						VehicleTripPrice vtp = itVTP.next();
						JSONObject vtpJS = new JSONObject();
						vtpJS.put("priceType", vtp.getPriceType().name());
						vtpJS.put("amount", vtp.getAmount());
						vtpJS.put("tripClass", vtp.getVehicleSeatClass().getVehicleSeatClassName());
						vtpJS.put("tripPriceId", vtp.getVehicleTripPriceId());
						jsonTicketPrices.put(vtpJS);
					}
					
					ScheduleStation departureScheduleStation = null;
					ScheduleStation arrivalScheduleStation = null;
					while(iter.hasNext())
					{
						ScheduleStation scheduleStation = iter.next();
						String scheduleStationCode = scheduleStation.getScheduleStationCode().getScheduleStationCode();
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
					JSONObject jsonTripSeats_ = new JSONObject();
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
						//log.info"departureStationCode = " + departureStationCode);
						//log.info"arrivalStationCode = " + arrivalStationCode);
						//log.info"departTime = " + departTime);
						//log.info"arrivalTime = " + arrivalTime);
						//log.info"-------------");
						
						//log.info"departureScheduleStation dep ==" + departureScheduleStation.getDepartureTime());
						//log.info"departureScheduleStation arr ==" + departureScheduleStation.getArrivalTime());
						//log.info"departureScheduleStation dep ==" + arrivalScheduleStation.getDepartureTime());
						//log.info"departureScheduleStation arr ==" + arrivalScheduleStation.getArrivalTime());
						
						
						
						String getAvailableSeatStr = (String)(getVehicleSeatAndLockDown(Boolean.FALSE, token, key, departStationCode, arriveStationCode, 
								passengerDetails, Boolean.FALSE, tripClass, requestId, ipAddress, clientCode, tripBookingToken).getEntity());
						//log.info"getAvailableSeatStr ==== " + getAvailableSeatStr);
						JSONObject availableSeats = new JSONObject(getAvailableSeatStr);
						if(availableSeats.has("status") && availableSeats.getInt("status")==0)
						{
							//log.info"availableSeats ---- " + availableSeats.toString());
							String detailsStr = availableSeats.getString("details");
							JSONObject details = new JSONObject(detailsStr);
							//log.info"details === " + details.toString());
							String tripDetailsStr = details.getString("tripDetails");
							JSONObject seatsAlloted = details.getJSONObject("seatsAlloted");
							//tripBookingToken = details.getString("tripBookingToken");
							JSONArray tripDetails = new JSONArray(tripDetailsStr);
							//log.info"tripDetails length === " + tripDetails.length());
							seatsAvailableStatus = Boolean.TRUE;
							if(departTime!=null && arrivalTime!=null && departTime.before(arrivalTime))
							{
								jsonTrips_.put(key, jsonTrips.get(key));
								jsonTripSeats_.put(key, seatsAlloted);
							}
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
						//jsonObject.add("token", tripToken);
						jsonObject.add("vehicleTripList", (jsonTrips_.toString()));
						jsonObject.add("seatsAlloted", jsonTripSeats_.toString());
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
	
	private JsonObject findAvailableTripsAndLockdown(String token, String departureStationCode, String arrivalStationCode, String passengerDetails, Date minimumReturnDate,
			String departureTime, Integer hoursAdd,  String tripClass, String requestId, String ipAddress, String clientCode) {
		// TODO Auto-generated method stub
		JsonObjectBuilder jsonObject = Json.createObjectBuilder();
    	String tripBookingToken = null;
    	
		try{
			
			if(departureStationCode==null || arrivalStationCode==null || tripClass==null || passengerDetails==null)
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
			if(departureStationCode!=null)
			{
				//Date departureTimeFormatted = (new SimpleDateFormat("yyyy-MM-dd HH:mm")).parse(departureTime);
				daSql = daSql + " WHERE (lower(ta.station.stationCode) = '"+departureStationCode.toLowerCase()+"' OR lower(ta.station.stationCode) = '"+arrivalStationCode.toLowerCase()+"')" +
						" AND ta.client.clientCode = '"+clientCode+"') ";
			}
			
			//log.info"depart --- " + daSql);
			List<Long> scheduleStationIds= (List<Long>)this.swpService.getAllRecordsByHQL(daSql);
			//log.info"scheduleStationIds size --- " + scheduleStationIds.size());
			//log.info"departureTime --- " + departureTime);
			//log.info"departureTime.split()[0] == " + (departureTime.split(" ")[0]));
			
			
			String departureDay = departureTime.split(" ")[0];										//min - 2019-01-31
			departureDay = departureDay + " 00:00";													//2019-01-30 00:00
			Date departureDate = new SimpleDateFormat("yyyy-MM-dd").parse(departureDay);			//2019-01-30
			String bufferDepartureDateStr = null;
			
			if(minimumReturnDate!=null && departureDate.before(minimumReturnDate))
			{
				Date bufferDepartureDate = null;
				departureDate = minimumReturnDate;
				departureDay = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(departureDate);
				Calendar cal = Calendar.getInstance();
				cal.setTime(departureDate);
				if(hoursAdd!=null)
					cal.add(Calendar.HOUR, hoursAdd);
				else
					cal.add(Calendar.HOUR, 24);
				
				bufferDepartureDate = cal.getTime();
				bufferDepartureDateStr = new SimpleDateFormat("yyyy-MM-dd").format(bufferDepartureDate);
				String bufferDepartureTime = bufferDepartureDateStr + " 23:59";
			}
			else
			{
				
				Date bufferDepartureDate = null;
				Calendar cal = Calendar.getInstance();
				cal.setTime(departureDate);
				if(hoursAdd!=null)
					cal.add(Calendar.HOUR, hoursAdd);
				else
					cal.add(Calendar.HOUR, 24);
				
				bufferDepartureDate = cal.getTime();
				bufferDepartureDateStr = new SimpleDateFormat("yyyy-MM-dd").format(bufferDepartureDate);
				String bufferDepartureTime = bufferDepartureDateStr + " 23:59";
			}
			daSql = "Select tp.scheduleStationCode.scheduleStationCode" +
					" from ScheduleStation tp where (tp.departureTime >= '"+departureDay+"' AND " +
					"tp.departureTime <= '"+bufferDepartureDateStr+" 23:59') AND tp.scheduleStationId IN " +
					"("+StringUtils.join(scheduleStationIds, ", ")+") AND tp.scheduleStationCode.passengerEnabled = 1 " +
					" GROUP BY tp.scheduleStationCode.scheduleStationCode";
			//AND tp.vehicleTripRoutingOriginSchedule.scheduleStation.departureTime <= '"+bufferDepartureTime+"'
			// AND tp.vehicleTripRoutingId IN ("+StringUtils.join(vehicleTripIds, ',')+")
			//log.info"---> " + daSql);
			List<String> scheduleStationCodes = (List<String>)this.swpService.getAllRecordsByHQL(daSql);
			//log.info"---> scheduleStationCodes size = " + scheduleStationCodes.size());
			////log.info"---> scheduleStationCodes = " + new Gson().toJson(scheduleStationCodes));
			
			if(scheduleStationCodes.size()>0)
			{
				daSql = "Select tp from Station tp where tp.stationCode = '"+departureStationCode+"' AND " +
							"tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL";
				Station departureStation = (Station)swpService.getUniqueRecordByHQL(daSql);

				daSql = "Select tp from Station tp where tp.stationCode = '"+arrivalStationCode+"' AND " +
							"tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL";
				//log.info"daSql == " + daSql);
				Station arrivalStation = (Station)swpService.getUniqueRecordByHQL(daSql);
				
				daSql = "Select tp from TripZoneStation tp where tp.station.stationCode = '"+arrivalStationCode+"' AND " +
						"tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL";
				//log.info"daSql == " + daSql);
				TripZoneStation arrivalTripZoneStation = (TripZoneStation)swpService.getUniqueRecordByHQL(daSql);

				
				daSql = "Select tp from TripZoneStation tp where tp.station.stationCode = '"+departureStationCode+"' AND " +
						"tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL";
				//log.info"daSql == " + daSql);
				TripZoneStation depatureTripZoneStation = (TripZoneStation)swpService.getUniqueRecordByHQL(daSql);
				
				
				if(arrivalTripZoneStation==null || depatureTripZoneStation==null)
				{
					jsonObject.add("status", ERROR.NO_TRIPS_FOUND_FOR_SPECIFIED_DATES);
					jsonObject.add("message", "No pricing Found For the Specified trip");
					JsonObject jsonObj = jsonObject.build();
		            return jsonObj;
				}
				
				

				//log.info"4 .... >>>>>" + arrivalTripZoneStation.getTripZone().getRouteOrder());
				//log.info"4 .... >>>>>" + depatureTripZoneStation.getTripZone().getRouteOrder());
				
				
				Integer tripRouteOrderdifference =  depatureTripZoneStation.getTripZone().getRouteOrder() - arrivalTripZoneStation.getTripZone().getRouteOrder();
				tripRouteOrderdifference = Math.abs(tripRouteOrderdifference);
				tripRouteOrderdifference = tripRouteOrderdifference + 1;
				
				
				daSql = "Select tp from VehicleTripPrice tp where tp.finalDestinationTripZone.routeOrder = " + tripRouteOrderdifference + 
							" AND tp.deletedAt is NULL and tp.client.clientCode = '"+clientCode+"' " +
							" AND lower(tp.vehicleSeatClass.vehicleSeatClassCode) = '"+tripClass.toLowerCase() +"'";
				//log.info"daSQL ===> " + daSql);
				Collection<VehicleTripPrice> vehicleTripPrices = (Collection<VehicleTripPrice>)swpService.getAllRecordsByHQL(daSql);
				//log.info"v size ---> " + vehicleTripPrices.size());
				
				if(vehicleTripPrices.size()==0)
				{
					jsonObject.add("status", ERROR.NO_TRIPS_FOUND_FOR_SPECIFIED_DATES);
					jsonObject.add("message", "No pricing Found For the Specified trip");
					JsonObject jsonObj = jsonObject.build();
		            return jsonObj;
				}
				if(vehicleTripPrices.size()>0)
				{
					String hql = "Select tp from VehicleSchedule tp where " +
							"tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL AND tp.passengerEnabled = 1";
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
					JSONArray jsonTicketPrices = new JSONArray();
					JSONObject jsonObjectEntry = null;
					
					
					Iterator<VehicleTripPrice> itVTP = vehicleTripPrices.iterator();
					while(itVTP.hasNext())
					{
						VehicleTripPrice vtp = itVTP.next();
						JSONObject vtpJS = new JSONObject();
						vtpJS.put("priceType", vtp.getPriceType().name());
						vtpJS.put("amount", vtp.getAmount());
						vtpJS.put("tripClass", vtp.getVehicleSeatClass().getVehicleSeatClassName());
						vtpJS.put("tripPriceId", vtp.getVehicleTripPriceId());
						jsonTicketPrices.put(vtpJS);
					}
					
					ScheduleStation departureScheduleStation = null;
					ScheduleStation arrivalScheduleStation = null;
					while(iter.hasNext())
					{
						ScheduleStation scheduleStation = iter.next();
						String scheduleStationCode = scheduleStation.getScheduleStationCode().getScheduleStationCode();
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
					JSONObject jsonTripSeats_ = new JSONObject();
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
						//log.info"departureStationCode = " + departureStationCode);
						//log.info"arrivalStationCode = " + arrivalStationCode);
						//log.info"departTime = " + departTime);
						//log.info"arrivalTime = " + arrivalTime);
						//log.info"-------------");
						
						//log.info"departureScheduleStation dep ==" + departureScheduleStation.getDepartureTime());
						//log.info"departureScheduleStation arr ==" + departureScheduleStation.getArrivalTime());
						//log.info"departureScheduleStation dep ==" + arrivalScheduleStation.getDepartureTime());
						//log.info"departureScheduleStation arr ==" + arrivalScheduleStation.getArrivalTime());
						
						
						
						String getAvailableSeatStr = (String)(getVehicleSeatAndLockDown(Boolean.TRUE, token, key, departStationCode, arriveStationCode, 
								passengerDetails, Boolean.FALSE, tripClass, requestId, ipAddress, clientCode, tripBookingToken).getEntity());
						//log.info"getAvailableSeatStr ==== " + getAvailableSeatStr);
						JSONObject availableSeats = new JSONObject(getAvailableSeatStr);
						if(availableSeats.has("status") && availableSeats.getInt("status")==0)
						{
							//log.info"availableSeats ---- " + availableSeats.toString());
							String detailsStr = availableSeats.getString("details");
							JSONObject details = new JSONObject(detailsStr);
							//log.info"details === " + details.toString());
							String tripDetailsStr = details.getString("tripDetails");
							JSONObject seatsAlloted = details.getJSONObject("seatsAlloted");
							//tripBookingToken = details.getString("tripBookingToken");
							JSONArray tripDetails = new JSONArray(tripDetailsStr);
							//log.info"tripDetails length === " + tripDetails.length());
							seatsAvailableStatus = Boolean.TRUE;
							if(departTime!=null && arrivalTime!=null && departTime.before(arrivalTime))
							{
								jsonTrips_.put(key, jsonTrips.get(key));
								jsonTripSeats_.put(key, seatsAlloted);
							}
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
						//jsonObject.add("token", tripToken);
						jsonObject.add("vehicleTripList", (jsonTrips_.toString()));
						jsonObject.add("seatsAlloted", jsonTripSeats_.toString());
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
					if(token!=null)
						jsonObject.add("token", token);
					JsonObject jsonObj = jsonObject.build();
		            return jsonObj;
				}
				
			}
			else
			{
				jsonObject.add("message", "Vehicle Trips not found matching the details provided");
				jsonObject.add("status", ERROR.NO_TRIPS_FOUND_FOR_SPECIFIED_DATES);
				if(token!=null)
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
	
	
	
	
	/*
	public Response oldListAvailableTrips(String token, String departureStationCode, String arrivalStationCode, 
			String departureTime, Integer hoursAdd,  Long tripClass, Boolean returnTrip, String requestId, String ipAddress, String clientCode) {
		// TODO Auto-generated method stub
		JsonObjectBuilder jsonObject = Json.createObjectBuilder();
    	
    	
		try{
			
			if(departureStationCode==null || arrivalStationCode==null || tripClass==null)
			{
				jsonObject.add("status", ERROR.INCOMPLETE_PARAMETERS);
				jsonObject.add("message", "Incomplete Parameters provided in request.");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
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
			
			String daSql = "Select ta.vehicleTrip.vehicleTripId from VehicleTripRouting ta";
			if(departureStationCode!=null)
			{
				//Date departureTimeFormatted = (new SimpleDateFormat("yyyy-MM-dd HH:mm")).parse(departureTime);
				daSql = daSql + " WHERE (ta.vehicleTripRoutingOriginSchedule.scheduleStation.station.stationCode = '"+departureStationCode+"' AND ta.client.clientCode = '"+clientCode+"') ";
			}
			
			//log.info"daSQL --- " + daSql);
			List<Long> vehicleTripIds= (List<Long>)this.swpService.getAllRecordsByHQL(daSql);
			//log.info"vehicleTripIds size --- " + vehicleTripIds.size());
			
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
			
			
			Date departureDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(departureTime);
			Calendar cal = Calendar.getInstance();
			cal.setTime(departureDate);
			if(hoursAdd!=null)
				cal.add(Calendar.HOUR, hoursAdd);
			else
				cal.add(Calendar.HOUR, 24);
			
			Date bufferDepartureDate = cal.getTime();
			String bufferDepartureTime = new SimpleDateFormat("yyyy-MM-dd").format(bufferDepartureDate) + " 23:59";
			daSql = "Select tp.vehicleTrip.vehicleTripId from VehicleTripRouting tp where (tp.vehicleTripRoutingOriginSchedule.scheduleStation.departureTime >= '"+departureTime+"' " +
					")" +
					" GROUP BY tp.vehicleTrip.vehicleTripId";
			//AND tp.vehicleTripRoutingOriginSchedule.scheduleStation.departureTime <= '"+bufferDepartureTime+"'
			// AND tp.vehicleTripRoutingId IN ("+StringUtils.join(vehicleTripIds, ',')+")
			//log.info"---> " + daSql);
			vehicleTripIds= (List<Long>)this.swpService.getAllRecordsByHQL(daSql);
			//log.info"---> vehicleTripIds size = " + vehicleTripIds.size());
			
			if(vehicleTripIds.size()>0)
			{
				daSql = "Select tp.vehicleTrip.vehicleTripId from VehicleTripRouting tp where tp.vehicleTripRoutingDestinationSchedule.scheduleStation.station.stationCode = '"+arrivalStationCode+"'" +
						" AND tp.vehicleTrip.vehicleTripId IN ("+StringUtils.join(vehicleTripIds, ',')+") " +
						" GROUP BY tp.vehicleTrip.vehicleTripId";
				//log.info"1---> " + daSql);
						
				vehicleTripIds= (List<Long>)this.swpService.getAllRecordsByHQL(daSql);
				//log.info"v size ---> " + vehicleTripIds.size());
				
				if(vehicleTripIds.size()>0)
				{
					String hql = "Select tp from VehicleScheduleFare tp where tp.deletedAt IS NULL AND tp.client.clientCode = '"+clientCode+"' " +
							"AND tp.vehicleTripRouteSeatSection.vehicleTripRouting.vehicleTrip.vehicleTripId IN ("+StringUtils.join(vehicleTripIds, ',')+") " +
							"AND tp.vehicleTripRouteSeatSection.vehicleSeatSection.vehicleSeatClass.vehicleSeatClassId = "+tripClass+"";
					//log.info"hql ---> " + hql);
					
					Collection<VehicleScheduleFare> vehicleScheduleFares= (Collection<VehicleScheduleFare>)this.swpService.getAllRecordsByHQL(hql);
					Iterator<VehicleScheduleFare> it = vehicleScheduleFares.iterator();
					JSONObject schedulePrices = new JSONObject();
					//log.info"vehicleScheduleFares === " + vehicleScheduleFares.size());
					while(it.hasNext())
					{
						VehicleScheduleFare vehicleScheduleFare = it.next();
						String key = vehicleScheduleFare.getVehicleTripRouteSeatSection().getVehicleTripRouting().getVehicleTripRoutingId() + "";
						//log.info"key === " + key);
						if(schedulePrices.has(key) && schedulePrices.getJSONObject(key).getDouble("baseAdultFare")>vehicleScheduleFare.getBaseAdultFare())
						{
							JSONObject jsonPrices = schedulePrices.getJSONObject(key);
							jsonPrices.put("baseAdultFare", vehicleScheduleFare.getBaseAdultFare());
							jsonPrices.put("baseChildFare", vehicleScheduleFare.getBaseChildFare());
							jsonPrices.put("seatClassName", vehicleScheduleFare.getVehicleTripRouteSeatSection().getVehicleSeatSection().getVehicleSeatClass().getVehicleSeatClassName());
							jsonPrices.put("seatClassId", vehicleScheduleFare.getVehicleTripRouteSeatSection().getVehicleSeatSection().getVehicleSeatClass().getVehicleSeatClassId());
							jsonPrices.put("vehicleScheduleFareId", vehicleScheduleFare.getVehicleScheduleFareId());
							schedulePrices.put(key, jsonPrices);
						}
						else if(schedulePrices.has(key) && schedulePrices.getJSONObject(key).getDouble("baseAdultFare")<vehicleScheduleFare.getBaseAdultFare())
						{
							
						}
						else
						{
							JSONObject jsonPrices = new JSONObject();
							jsonPrices.put("baseAdultFare", vehicleScheduleFare.getBaseAdultFare());
							jsonPrices.put("baseChildFare", vehicleScheduleFare.getBaseChildFare());
							jsonPrices.put("seatClassName", vehicleScheduleFare.getVehicleTripRouteSeatSection().getVehicleSeatSection().getVehicleSeatClass().getVehicleSeatClassName());
							jsonPrices.put("seatClassId", vehicleScheduleFare.getVehicleTripRouteSeatSection().getVehicleSeatSection().getVehicleSeatClass().getVehicleSeatClassId());
							jsonPrices.put("vehicleScheduleFareId", vehicleScheduleFare.getVehicleScheduleFareId());
							schedulePrices.put(key, jsonPrices);
						}
					}
					
					if(vehicleTripIds.size()==0)
					{
						jsonObject.add("status", ERROR.NO_TRIPS_FOUND_FOR_SPECIFIED_DATES);
						jsonObject.add("message", "No Trips Found For the Specified Departure Date");
						JsonObject jsonObj = jsonObject.build();
			            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
					}
					
					hql = "Select tp from VehicleTripRouting tp where tp.deletedAt IS NULL AND tp.vehicleTrip.vehicleTripId IN ("+StringUtils.join(vehicleTripIds, ',')+") " +
							"ORDER BY tp.routeOrder";
					//log.info"hql --- " + hql);
					//log.info"jsonPrices --- " + schedulePrices.toString());
					Collection<VehicleTripRouting> vehicleTripRoutings= (Collection<VehicleTripRouting>)this.swpService.getAllRecordsByHQL(hql);
					Iterator<VehicleTripRouting> iter = vehicleTripRoutings.iterator();
					
					
					JSONObject jsonTrips = new JSONObject();
					JSONObject jsonObjectEntry = null;
					while(iter.hasNext())
					{
						VehicleTripRouting vehicleTripRoute = iter.next();
						String tripCode = vehicleTripRoute.getVehicleTrip().getVehicleTripCode();
						if(jsonTrips.has(tripCode))
						{
							jsonObjectEntry = jsonTrips.getJSONObject(tripCode);
							if(jsonObjectEntry.has("trips"))
							{
								if(schedulePrices.has(vehicleTripRoute.getVehicleTripRoutingId()+""))
								{
									VehicleSchedule originSchedule = vehicleTripRoute.getVehicleTripRoutingOriginSchedule();
									VehicleSchedule destinationSchedule = vehicleTripRoute.getVehicleTripRoutingDestinationSchedule();
									String vehicleCode = originSchedule!=null ? originSchedule.getVehicle().getVehicleCode() : destinationSchedule.getVehicle().getVehicleCode();
									JSONArray trips = (JSONArray)jsonObjectEntry.get("trips");
									JSONObject trip = new JSONObject();
									trip.put("originStationCity", vehicleTripRoute.getVehicleTripRoutingOriginSchedule().getScheduleStation().getStation().getDistrict().getName());
									trip.put("originStation", vehicleTripRoute.getVehicleTripRoutingOriginSchedule().getScheduleStation().getStation().getStationName());
									trip.put("departureTime", vehicleTripRoute.getVehicleTripRoutingOriginSchedule().getScheduleStation().getDepartureTime());
									trip.put("arrivalStationCity", vehicleTripRoute.getVehicleTripRoutingDestinationSchedule().getScheduleStation().getStation().getDistrict().getName());
									trip.put("arrivalStation", vehicleTripRoute.getVehicleTripRoutingDestinationSchedule().getScheduleStation().getStation().getStationName());
									trip.put("arrivalStationCode", vehicleTripRoute.getVehicleTripRoutingDestinationSchedule().getScheduleStation().getStation().getStationCode());
									trip.put("originStationCode", vehicleTripRoute.getVehicleTripRoutingOriginSchedule().getScheduleStation().getStation().getStationCode());
									trip.put("arrivalTime", vehicleTripRoute.getVehicleTripRoutingDestinationSchedule().getScheduleStation().getArrivalTime());
									trip.put("vehicleName", vehicleTripRoute.getVehicleTripRoutingDestinationSchedule().getVehicle().getVehicleName());
									trip.put("routeOrder", vehicleTripRoute.getRouteOrder());
									JSONObject jsonPrice = schedulePrices.getJSONObject(vehicleTripRoute.getVehicleTripRoutingId()+"");
									if(jsonPrice!=null)
									{
										trip.put("baseAdultFare", jsonPrice.getDouble("baseAdultFare"));
										trip.put("baseChildFare", jsonPrice.getDouble("baseChildFare"));
										trip.put("seatClassName", jsonPrice.getString("seatClassName"));
										//trip.put("seatClassId", jsonPrice.getLong("seatClassId"));
										//trip.put("vehicleScheduleFareId", jsonPrice.getLong("vehicleScheduleFareId"));
									}
									trips.put(trip);
									jsonObjectEntry.put("trips", trips);
									jsonTrips.put(tripCode, jsonObjectEntry);
								}
							}
						}
						else
						{
							if(schedulePrices.has(vehicleTripRoute.getVehicleTripRoutingId()+""))
							{
								jsonObjectEntry = new JSONObject();
								VehicleSchedule originSchedule = vehicleTripRoute.getVehicleTripRoutingOriginSchedule();
								VehicleSchedule destinationSchedule = vehicleTripRoute.getVehicleTripRoutingDestinationSchedule(); 
								jsonObjectEntry.put("tripCode", vehicleTripRoute.getVehicleTrip().getVehicleTripCode());
								jsonObjectEntry.put("vehicleName", originSchedule==null ? destinationSchedule.getVehicle().getVehicleName() : originSchedule.getVehicle().getVehicleName());
								JSONArray trips = new JSONArray();
								JSONObject trip = new JSONObject();
								trip.put("originStationCity", vehicleTripRoute.getVehicleTripRoutingOriginSchedule().getScheduleStation().getStation().getDistrict().getName());
								trip.put("originStation", vehicleTripRoute.getVehicleTripRoutingOriginSchedule().getScheduleStation().getStation().getStationName());
								trip.put("departureTime", vehicleTripRoute.getVehicleTripRoutingOriginSchedule().getScheduleStation().getDepartureTime());
								trip.put("arrivalStation", vehicleTripRoute.getVehicleTripRoutingDestinationSchedule().getScheduleStation().getStation().getStationName());
								trip.put("arrivalStationCity", vehicleTripRoute.getVehicleTripRoutingDestinationSchedule().getScheduleStation().getStation().getDistrict().getName());
								trip.put("arrivalStationCode", vehicleTripRoute.getVehicleTripRoutingDestinationSchedule().getScheduleStation().getStation().getStationCode());
								trip.put("originStationCode", vehicleTripRoute.getVehicleTripRoutingOriginSchedule().getScheduleStation().getStation().getStationCode());
								trip.put("arrivalTime", vehicleTripRoute.getVehicleTripRoutingDestinationSchedule().getScheduleStation().getArrivalTime());
								trip.put("vehicleName", vehicleTripRoute.getVehicleTripRoutingDestinationSchedule().getVehicle().getVehicleName());
								trip.put("routeOrder", vehicleTripRoute.getRouteOrder());
								JSONObject jsonPrice = schedulePrices.getJSONObject(vehicleTripRoute.getVehicleTripRoutingId()+"");
								if(jsonPrice!=null)
								{
									trip.put("baseAdultFare", jsonPrice.getDouble("baseAdultFare"));
									trip.put("baseChildFare", jsonPrice.getDouble("baseChildFare"));
									trip.put("seatClassName", jsonPrice.getString("seatClassName"));
									//trip.put("seatClassId", jsonPrice.getLong("seatClassId"));
									//trip.put("vehicleScheduleFareId", jsonPrice.getLong("vehicleScheduleFareId"));
								}
								trips.put(trip);
								jsonObjectEntry.put("trips", trips);
								jsonTrips.put(tripCode, jsonObjectEntry);
							}
						}
						
					}
					//log.info"##------------------------");
					//log.infojsonTrips.toString());
					//log.info"##------------------------");
					
					JSONObject jsonTrips_ = new JSONObject();
					Iterator<String> iteratorString = jsonTrips.keys();
					while(iteratorString.hasNext())
					{
						String key = iteratorString.next();
						jsonObjectEntry = jsonTrips.getJSONObject(key);
						JSONArray trips = jsonObjectEntry.getJSONArray("trips");
						boolean proceed = false;
						Integer departRouteOrder = null;
						Integer arriveRouteOrder = null;
						String departStationCode = null;
						String arriveStationCode = null;
						Boolean departureStationChecker = false;
						Boolean arriveStationCodeChecker = false;
						Integer departureTripOrder = null;
						Integer arriveTripOrder = null;
						for(int i=0; i<trips.length(); i++)
						{
							JSONObject trip = trips.getJSONObject(i);
							departRouteOrder = null;
							arriveRouteOrder = null;
							//log.info"---===>trip = " + trip.toString());
							departStationCode = trip.getString("originStationCode");
							arriveStationCode = trip.getString("arrivalStationCode");
							Integer routeOrder = trip.getInt("routeOrder");
							
							//log.info"-------------");
							//log.info"departureStationCode = " + departureStationCode);
							//log.info"arrivalStationCode = " + arrivalStationCode);
							//log.info"departStationCode = " + departStationCode);
							//log.info"arriveStationCode = " + arriveStationCode);
							//log.info"routeOrder = " + routeOrder);
							//log.info"-------------");
							
							if(departureStationCode.equals(departStationCode))
							{
								departureStationChecker = true;
								departureTripOrder = trip.getInt("routeOrder");
							}
							
							if(arrivalStationCode.equals(arriveStationCode))
							{
								arriveStationCodeChecker = true;
								arriveTripOrder = trip.getInt("routeOrder");
							}
						}
						
						if(departureStationChecker.equals(Boolean.TRUE) && arriveStationCodeChecker.equals(Boolean.TRUE) && departureTripOrder!=null && arriveTripOrder!=null && (departureTripOrder==arriveTripOrder || departureTripOrder<arriveTripOrder))
							jsonTrips_.put(key, jsonTrips.get(key));
						
						
					}
					
					
					//log.info"##------------------------");
					//log.infojsonTrips_.toString());
					//log.info"##------------------------");
					
					
					
					
					
					ClientFunction cf = ClientFunction.getInstance();
					String jsonObjectStr1 = (String)(cf.getClientSystemDetails(clientCode, requestId).getEntity());
					JSONObject jsonObjectStr2 = new JSONObject(jsonObjectStr1);
					
					if(jsonTrips_.length()>0)
					{
						jsonObject.add("message", "Vehicle Trips listed successfully");
						jsonObject.add("status", ERROR.GENERAL_SUCCESS);
						//jsonObject.add("token", tripToken);
						jsonObject.add("vehicleTripList", (jsonTrips_.toString()));
					}
					else
					{
						jsonObject.add("message", "Vehicle Trips searched for not available");
						jsonObject.add("status", ERROR.TRIP_NON_AVAILABLE);
						//jsonObject.add("token", tripToken);
						jsonObject.add("vehicleTripList", (jsonTrips_.toString()));
					}
					JsonObject jsonObj = jsonObject.build();
		            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
					
				}
				else
				{
					jsonObject.add("message", "Vehicle Trips not found matching the details provided");
					jsonObject.add("status", ERROR.NO_TRIPS_FOUND_FOR_SPECIFIED_DATES);
					jsonObject.add("token", token);
					JsonObject jsonObj = jsonObject.build();
		            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
				}
				
			}
			else
			{
				jsonObject.add("message", "Vehicle Trips not found matching the details provided");
				jsonObject.add("status", ERROR.NO_TRIPS_FOUND_FOR_SPECIFIED_DATES);
				jsonObject.add("token", token);
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

	*/
	
	
	
	
	public Response purchaseVehicleTripTickets(String purchasePoint, Double concessionAmount, String couponCode, String paymentType, String transactionRef, String deviceCode, 
			String ticketCollectionCenterCode, String purchaseToken, String purchaseDetails, 
			String generalTripClass, String requestId, String ipAddress, String clientCode, String token) {
		// TODO Auto-generated method stub
		JsonObjectBuilder jsonObject = Json.createObjectBuilder();
		//RoleType roleCode = null;
    	
		try{
			if(purchasePoint==null || deviceCode==null || purchaseToken==null || purchaseDetails==null || paymentType==null || generalTripClass==null || deviceCode==null)
			{
				jsonObject.add("status", ERROR.INCOMPLETE_PARAMETERS);
				jsonObject.add("message", "Incomplete parameters provided. Provide purchasePoint, purchaseToken, transactionRef, deviceCode, purchaseDetails, paymentType, generalTripClass parameters");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}

			//log.info">>>..." + purchasePoint);
			//log.info">>>..." + paymentType);
			//log.info">>>..." + transactionRef);
			//log.info">>>..." + deviceCode);
			//log.info">>>..." + ticketCollectionCenterCode); 
			//log.info">>>..." + purchaseToken);
			//log.info">>>..." + purchaseDetails); 
			//log.info">>>..." + generalTripClass); 
			//log.info">>>..." + requestId);
			//log.info">>>..." + ipAddress);
			//log.info">>>..." + clientCode); 
			//log.info">>>..." + token);
			//log.info">>>..." + couponCode);
			//log.info">>>..." + concessionAmount);
			
			JSONObject tripTicketDataJSArray = null;
			String messageRequest = null;
			String messageResponse = null;
			//String transactionRef = null;
			JSONArray receiptNos = new JSONArray();
			PurchasePoint pp = null;
			
			try
			{
				pp = PurchasePoint.valueOf(purchasePoint);
			}
			catch(IllegalArgumentException | NullPointerException e)
			{
				jsonObject.add("status", ERROR.INCOMPLETE_PARAMETERS);
				jsonObject.add("message", "Invalid purchase point provided");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			if(paymentType.equals(PaymentMeans.VENDOR_WALLET.name()))
			{
				RoleType roleCode = null;
				if(token==null)
				{
					jsonObject.add("status", ERROR.INCOMPLETE_PARAMETERS);
					jsonObject.add("message", "As a vendor, a payee must be logged in to purchase tickets");
					JsonObject jsonObj = jsonObject.build();
		            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
				}
				
				JSONObject purchaseDetailsJS = new JSONObject(purchaseDetails);
				tripTicketDataJSArray = purchaseDetailsJS.getJSONObject("tripTicketData");
				//String probasePayMerchantCode = purchaseDetailsJS.has("probasePayMerchantCode") ? purchaseDetailsJS.getString("probasePayMerchantCode") : null; 
				//String probasePayDeviceCode = purchaseDetailsJS.has("probasePayDeviceCode") ? purchaseDetailsJS.getString("probasePayDeviceCode") : null;
				String orderId = purchaseDetailsJS.has("orderId") ? purchaseDetailsJS.getString("orderId") : null;
				//transactionRef = purchaseDetailsJS.has("transactionRef") ? purchaseDetailsJS.getString("transactionRef") : null;
				String hash = purchaseDetailsJS.has("hash") ? purchaseDetailsJS.getString("hash") : null;
				// || probasePayMerchantCode==null || probasePayDeviceCode==null


				if(tripTicketDataJSArray==null || hash==null)
				{
					jsonObject.add("status", ERROR.INCOMPLETE_PARAMETERS);
					jsonObject.add("message", "Incomplete parameters provided. Ensure you provide merchantCode, probasePayDeviceCode, transactionRef, and hash parameters in your tripTicketData parameter");
					JsonObject jsonObj = jsonObject.build();
		            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
				}
				JSONObject jsObj = new JSONObject();
				
				messageRequest = hash+"###"+deviceCode+"###"+transactionRef+"###"+token+"###"+"###"+ticketCollectionCenterCode
						+"###"+ipAddress+"###"+clientCode;

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
				Boolean skip = Boolean.FALSE;
				Vendor vendor = null;
				Wallet wallet = null;
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
						
						if(user!=null)
						{
							Vendor userVendor = user.getVendor();
							hql = "Select tp from Wallet tp where tp.vendor.id = " + userVendor.getVendorId() + " AND tp.deletedAt IS NULL AND " +
									"tp.vendor.client.clientId = '"+user.getClient().getClientId()+"'";
							wallet = (Wallet)swpService.getUniqueRecordByHQL(hql);
						
							hql = "Select tp from Vendor tp where tp.vendorId = " + user.getVendor().getVendorId() + " AND tp.deletedAt IS NULL AND " +
									"tp.client.clientId = '"+user.getClient().getClientId()+"'";
							vendor = (Vendor)swpService.getUniqueRecordByHQL(hql);
						}
					}
				}
				
				if(wallet==null || (wallet!=null && wallet.getCurrentBalance()==0))
				{
					jsonObject.add("status", ERROR.FORCE_LOGOUT_USER);
					jsonObject.add("message", "Your session has expired. Please log in again");
					JsonObject jsonObj = jsonObject.build();
		            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
				}
				
				
				TicketCollectionPoint tcp = null;
				if(ticketCollectionCenterCode!=null)
				{
					hql = "Select tp from TicketCollectionPoint tp where tp.collectionPointCode = '"+ticketCollectionCenterCode+"' AND tp.deletedAt IS NULL " +
							"AND tp.client.clientCode = '"+clientCode+"'";
					tcp = (TicketCollectionPoint)swpService.getUniqueRecordByHQL(hql);
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



				JSONObject allTripSeatDetails = new JSONObject();
				List<Long> seatIds = new ArrayList<Long>();
				Iterator<String> outwardInwardIter = tripTicketDataJSArray.keys();
				while(outwardInwardIter.hasNext())
				{
					String outwardInwardStr = outwardInwardIter.next();
					JSONObject outwardTrips = tripTicketDataJSArray.getJSONObject(outwardInwardStr);
					JSONArray tripCodes = outwardTrips.names();
					for(int i=0; i<tripCodes.length(); i++)
					{
						String tripCode = tripCodes.getString(i);
						JSONObject tripEntry = outwardTrips.getJSONObject(tripCode);
						Integer adultPassenger = 0;
						Integer childPassenger = 0;
						Integer seniorPassenger = 0;
						Integer disabledPassenger = 0;
						JSONObject seatsAlloted = tripEntry.getJSONObject("seatsAlloted");
						if(seatsAlloted==null)
						{
							skip = Boolean.TRUE;
						}
						else
						{
							Iterator<String> seatsAllotedIter = seatsAlloted.keys();
							while(seatsAllotedIter.hasNext())
							{
								String seatsAllotedIterStr = seatsAllotedIter.next();
								JSONArray seatsAllotedArray = seatsAlloted.getJSONArray(seatsAllotedIterStr);
								//log.info"seatsAllotedArray Str = " + seatsAllotedArray.toString());
								for(int j=0; j<seatsAllotedArray.length(); j++)
								{
									JSONObject jsSeatAlloted = (seatsAllotedArray.getJSONObject(j));
									//log.info"jsSeatAlloted = " + jsSeatAlloted.toString());
									seatIds.add(jsSeatAlloted.getLong("seatAvailabilityId"));
								}
							}
							
						}
						adultPassenger = (tripEntry.has("adultPassenger") ? tripEntry.getInt("adultPassenger") : 0);
						childPassenger = (tripEntry.has("childPassenger") ? tripEntry.getInt("childPassenger") : 0);
						seniorPassenger = (tripEntry.has("seniorPassenger") ? tripEntry.getInt("seniorPassenger") : 0);
						disabledPassenger = (tripEntry.has("disabledPassenger") ? tripEntry.getInt("disabledPassenger") : 0);
						JSONObject passengerdetails = tripEntry.getJSONObject("passengerdetails");
						JSONObject leadPassenger = passengerdetails.getJSONObject("leadPassenger");
						JSONArray otherPassengers = passengerdetails.has("otherPassengers") ? passengerdetails.getJSONArray("otherPassengers") : null;
					}
				}
				
				//log.info"...join == " + StringUtils.join(seatIds, ", "));
				
				if(skip.equals(Boolean.TRUE))
				{
					jsonObject.add("status", ERROR.INCOMPLETE_PARAMETERS_SEATS_NOT_ALLOTTED_COMPLETELY);
					jsonObject.add("message", "Seats purchased not specified completely");
					JsonObject jsonObj = jsonObject.build();
		            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
				}
				else
				{
					hql = "Select tp from ScheduleStationSeatAvailability tp where tp.schedSeatAvailId IN ("+StringUtils.join(seatIds, ", ")+") " +
							"AND tp.deletedAt is NULL AND tp.vehicleSeat.client.clientCode = '"+clientCode+"' AND tp.lockedDownBy = '"+purchaseToken+"' AND " +
							"(tp.lockedDownExpiryDate > CURRENT_TIMESTAMP) AND tp.seatAvailabilityStatus = "+ SeatAvailabilityStatus.OPEN.ordinal() +"";
					//log.info"....HQL = " + hql);
					Collection<ScheduleStationSeatAvailability> vsaList = (Collection<ScheduleStationSeatAvailability>)swpService.getAllRecordsByHQL(hql);
					//log.info"vsaList.size()<seatIds.size() ==" + vsaList.size() + " -- " + seatIds.size());
					if(vsaList!=null && vsaList.size()<seatIds.size())
					{
						jsonObject.add("status", ERROR.INCOMPLETE_SEATS_AVAILABLE_PURCHASE_TIME_EXPIRED);
						jsonObject.add("message", "Time expired for purchasing the seats alloted to you");
						JsonObject jsonObj = jsonObject.build();
			            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
					}

					
					Double adultTicketPrice = 0.00;
					Double childTicketPrice = 0.00;
					Double seniorTicketPrice = 0.00;
					Double disabledTicketPrice = 0.00;
					Double totalPrice = 0.00;
					
					
					outwardInwardIter = tripTicketDataJSArray.keys();
					while(outwardInwardIter.hasNext())
					{
						String outwardInwardStr = outwardInwardIter.next();
						JSONObject outwardTrips = tripTicketDataJSArray.getJSONObject(outwardInwardStr);
						JSONArray tripCodes = outwardTrips.names();
						for(int i=0; i<tripCodes.length(); i++)
						{
							String tripCode = tripCodes.getString(i);
							JSONObject tripEntry = outwardTrips.getJSONObject(tripCode);
							Integer adultPassenger = 0;
							Integer childPassenger = 0;
							Integer seniorPassenger = 0;
							Integer disabledPassenger = 0;
							
							hql = "Select tp from VehicleSchedule tp where tp.scheduleStationCode.scheduleStationCode = '"+tripCode+"' " +
									"AND tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL";
							VehicleSchedule vs = (VehicleSchedule)swpService.getUniqueRecordByHQL(hql);
							
							Double baseAdultFare = 0.00;
							Double baseChildFare = 0.00;
							Double baseSeniorFare = 0.00;
							Double baseDisabledFare = 0.00;
							
							hql = "Select tp.purchasedTripId from PurchasedTrip tp where " +
									//"tp.vehicleSchedule.line.lineId = '" + vs.getLine().getLineId() + "' AND " +
									"tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL";
							//log.info"PurchasedTrip... = " + hql);
							List<Long> transactionsByLine = (List<Long>)swpService.getAllRecordsByHQL(hql);
							
							String departingStationCode = tripEntry.getString("departingStation");
							String arrivalStationCode = tripEntry.getString("arrivingStation");
							
							String daSql = "Select tp from Station tp where tp.stationCode = '"+departingStationCode+"' AND " +
									"tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL";
							Station departureStation = (Station)swpService.getUniqueRecordByHQL(daSql);

							daSql = "Select tp from Station tp where tp.stationCode = '"+arrivalStationCode+"' AND " +
										"tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL";
							Station arrivalStation = (Station)swpService.getUniqueRecordByHQL(daSql);
							
							daSql = "Select tp from TripZoneStation tp where tp.station.stationCode = '"+arrivalStationCode+"' AND " +
									"tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL";
							TripZoneStation arrivalTripZoneStation = (TripZoneStation)swpService.getUniqueRecordByHQL(daSql);
							//log.info">>>>>>>>>" + arrivalTripZoneStation.getTripZone().getRouteOrder());
															
							daSql = "Select tp from TripZoneStation tp where tp.station.stationCode = '"+departingStationCode+"' AND " +
									"tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL";
							TripZoneStation depatureTripZoneStation = (TripZoneStation)swpService.getUniqueRecordByHQL(daSql);
							//log.info">>>>>>>>>" + depatureTripZoneStation.getTripZone().getRouteOrder());
							
							int tripRouteOrderdifference = depatureTripZoneStation.getTripZone().getRouteOrder() - arrivalTripZoneStation.getTripZone().getRouteOrder();
							tripRouteOrderdifference = Math.abs(tripRouteOrderdifference);
							tripRouteOrderdifference = tripRouteOrderdifference + 1;
							//log.info">>>>>>>>>" + tripRouteOrderdifference);
							
							daSql = "Select tp from VehicleTripPrice tp where tp.finalDestinationTripZone.routeOrder = " + tripRouteOrderdifference +  
									" AND tp.deletedAt is NULL and tp.client.clientCode = '"+clientCode+"' " +
									" AND lower(tp.vehicleSeatClass.vehicleSeatClassCode) = '"+generalTripClass.toLowerCase() +"'";
							//log.info"daSQL ===> " + daSql);
							Collection<VehicleTripPrice> vehicleTripPrices = (Collection<VehicleTripPrice>)swpService.getAllRecordsByHQL(daSql);
							//log.info"v size ---> " + vehicleTripPrices.size());


							Iterator<VehicleTripPrice> itVTP = vehicleTripPrices.iterator();
							while(itVTP.hasNext())
							{
								VehicleTripPrice vtp = itVTP.next();
								if(vtp.getPriceType().equals(PriceType.FOR_ADULT_PRICE))
								{
									baseAdultFare = vtp.getAmount();
								}
								else if(vtp.getPriceType().equals(PriceType.FOR_CHILD_PRICE))
								{
									baseChildFare = vtp.getAmount();
								}
								else if(vtp.getPriceType().equals(PriceType.FOR_SENIOR_CITIZENS))
								{
									baseSeniorFare = vtp.getAmount();
								}
								else if(vtp.getPriceType().equals(PriceType.FOR_SPECIALLY_ABLED))
								{
									baseDisabledFare = vtp.getAmount();
								}
							}
							
							adultPassenger = (tripEntry.has("adultPassenger") ? tripEntry.getInt("adultPassenger") : 0);
							childPassenger = (tripEntry.has("childPassenger") ? tripEntry.getInt("childPassenger") : 0);
							seniorPassenger = (tripEntry.has("seniorPassenger") ? tripEntry.getInt("seniorPassenger") : 0);
							disabledPassenger = (tripEntry.has("disabledPassenger") ? tripEntry.getInt("disabledPassenger") : 0);
							
							adultTicketPrice = (adultPassenger*baseAdultFare);
							childTicketPrice = (childPassenger*baseChildFare);
							seniorTicketPrice = (seniorPassenger*baseSeniorFare);
							disabledTicketPrice = (disabledPassenger*baseDisabledFare);
							//log.info"adultPrice === " + adultTicketPrice);
							//log.info"childPrice === " + childTicketPrice);
							//log.info"seniorPrice === " + seniorTicketPrice);
							//log.info"disabledPrice === " + disabledTicketPrice);
						}
						totalPrice = adultTicketPrice + childTicketPrice + seniorTicketPrice + disabledTicketPrice;
						//log.info"*******total price = " + totalPrice);
					}
					
					Coupon coupon = null;
					Double couponValuation = 0.00;
					if(couponCode!=null)
					{
						hql = "Select tp from Coupon tp where lower(tp.couponCode) = '"+couponCode.toLowerCase()+"' AND tp.deletedAt IS NULL AND tp.client.clientId = " + client.getClientId();
						coupon = (Coupon)swpService.getUniqueRecordByHQL(hql);
						if(coupon!=null)
						{
							couponValuation = totalPrice*coupon.getDiscountRate()/100;
							totalPrice = totalPrice - couponValuation; 
						}
					}
					
					if(user!=null && (roleCode.equals(RoleType.OPERATOR) || roleCode.equals(RoleType.ADMIN_STAFF)))
					{
						if(concessionAmount!=null)
						{
							totalPrice = totalPrice - concessionAmount;
						}
					}
					else
					{
						concessionAmount = null;
					}
					
					totalPrice = totalPrice + (client.getBookingFee()==null ? 0 : client.getBookingFee());
					
					String status = null;
					if(wallet.getCurrentBalance() > totalPrice)
					{
						String toHash = client.getClientCode() + "" + deviceCode + "" + orderId + "" + totalPrice + user.getWebActivationCode();
						//log.info"toHash == " + toHash);
						status = "00";
					}
					
					
					
					String serviceType = ServiceType.EPAYMENT_FOR_TRIP.name();
					JSONArray tripSummary = new JSONArray();
					if(status!=null && status.equals("00"))
					{
						
						String ticketTransactionRef = RandomStringUtils.randomNumeric(8);
						hql = "Select tp from Transaction tp where tp.orderRef = '"+(device.getDeviceCode() + "-" + transactionRef)+"' AND tp.client.clientCode = '"+clientCode+"' AND " +
								"tp.deletedAt IS NULL";
						Transaction transaction = (Transaction)swpService.getUniqueRecordByHQL(hql);
						
						if(transaction!=null)
						{
							jsonObject.add("status", ERROR.TRANSACTION_REF_ALREADY_PAID_PREVIOUSLY);
							jsonObject.add("message", "Transaction Reference you provided belongs to another transaction that has already been paid");
							JsonObject jsonObj = jsonObject.build();
				            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
						}
						
						JSONObject vehicleSeatsLockedDownTrip = new JSONObject();
						
						Double fixedCharge = 0.00;
						Double transactionFee = 0.00;
						Double transactionAmount = totalPrice;
						
						
						if(user!=null && (roleCode.equals(RoleType.OPERATOR) || roleCode.equals(RoleType.ADMIN_STAFF)))
						{
						}
						else
						{
							concessionAmount = null;
						}
							
						transaction = new Transaction();
						transaction.setTransactionRef(ticketTransactionRef);
						transaction.setOrderRef(device.getDeviceCode() + "-" + transactionRef);
						transaction.setChannel(com.probase.reservationticketingwebservice.enumerations.Channel.POS);
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
						transaction.setNarration("Debit|"+wallet.getWalletCode()+"|#" + device.getDeviceCode()+"-"+transactionRef + "|ZMW" + transaction.getTransactionAmount());
						transaction.setDrVendorId(vendor.getVendorId());
						transaction.setVendor(vendor);
						transaction.setUpdatedAt(new Date());
						transaction.setCreatedAt(new Date());
						transaction.setPaymentMeans(PaymentMeans.VENDOR_WALLET);
						transaction.setClient(client);						
						transaction.setTransactionCurrency("ZMW");
						transaction.setWallet(wallet);
						//transaction.setConcessionApplied(concessionAmount);
						if(coupon!=null)
						{
							transaction.setAppliedCoupon(coupon);
						}
						transaction = (Transaction)swpService.createNewRecord(transaction);
						
						
						wallet.setCurrentBalance(wallet.getCurrentBalance() - transactionAmount);
						swpService.updateRecord(wallet);
						
						JSONObject jsonTrips = new JSONObject();
						JSONObject leadPassenger = null;
						JSONArray otherPassengers = null;
						
						
						outwardInwardIter = tripTicketDataJSArray.keys();
						
						while(outwardInwardIter.hasNext())
						{
							String outwardInwardStr = outwardInwardIter.next();
							JSONObject outwardTrips = tripTicketDataJSArray.getJSONObject(outwardInwardStr);
							JSONArray tripCodes = outwardTrips.names();
							for(int i=0; i<tripCodes.length(); i++)
							{
								String tripCode = tripCodes.getString(i);
								JSONObject tripEntry = outwardTrips.getJSONObject(tripCode);
								Integer adultPassenger = 0;
								Integer childPassenger = 0;
								Integer seniorPassenger = 0;
								Integer disabledPassenger = 0;
								JSONObject seatsAlloted = tripEntry.getJSONObject("seatsAlloted");
								
								hql = "Select tp from VehicleSchedule tp where tp.scheduleStationCode.scheduleStationCode = '"+tripCode+"' " +
										"AND tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL";
								VehicleSchedule vs = (VehicleSchedule)swpService.getUniqueRecordByHQL(hql);
								
								adultTicketPrice = 0.00;
								childTicketPrice = 0.00;
								seniorTicketPrice = 0.00;
								disabledTicketPrice = 0.00;
								Double baseAdultFare = 0.00;
								Double baseChildFare = 0.00;
								Double baseSeniorFare = 0.00;
								Double baseDisabledFare = 0.00;
								
								hql = "Select tp.purchasedTripId from PurchasedTrip tp where " +
										//"tp.vehicleSchedule.line.lineId = '" + vs.getLine().getLineId() + "' AND " +
										"tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL";
								//log.info"PurchasedTrip... = " + hql);
								List<Long> transactionsByLine = (List<Long>)swpService.getAllRecordsByHQL(hql);
								
								String departingStationCode = tripEntry.getString("departingStation");
								String arrivalStationCode = tripEntry.getString("arrivingStation");
								String departureTime = tripEntry.getString("departureTime");
								String arrivalTime = tripEntry.getString("arrivalTime");
								Date departureTimeDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(departureTime);
								Date arrivalTimeDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(arrivalTime);
								
								String daSql = "Select tp from Station tp where tp.stationCode = '"+departingStationCode+"' AND " +
										"tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL";
								Station departureStation = (Station)swpService.getUniqueRecordByHQL(daSql);
	
								daSql = "Select tp from Station tp where tp.stationCode = '"+arrivalStationCode+"' AND " +
											"tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL";
								Station arrivalStation = (Station)swpService.getUniqueRecordByHQL(daSql);
								
								daSql = "Select tp from TripZoneStation tp where tp.station.stationCode = '"+arrivalStationCode+"' AND " +
										"tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL";
								TripZoneStation arrivalTripZoneStation = (TripZoneStation)swpService.getUniqueRecordByHQL(daSql);
																
								daSql = "Select tp from TripZoneStation tp where tp.station.stationCode = '"+departingStationCode+"' AND " +
										"tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL";
								TripZoneStation depatureTripZoneStation = (TripZoneStation)swpService.getUniqueRecordByHQL(daSql);
								

								//log.info"5 .... >>>>>" + arrivalTripZoneStation.getTripZone().getRouteOrder());
								//log.info"5 .... >>>>>" + depatureTripZoneStation.getTripZone().getRouteOrder());
								
								Integer tripRouteOrderdifference =  depatureTripZoneStation.getTripZone().getRouteOrder() - arrivalTripZoneStation.getTripZone().getRouteOrder();
								tripRouteOrderdifference = Math.abs(tripRouteOrderdifference);
								tripRouteOrderdifference = tripRouteOrderdifference + 1;
								
								daSql = "Select tp from VehicleTripPrice tp where tp.finalDestinationTripZone.routeOrder = " + tripRouteOrderdifference +  
										" AND tp.deletedAt is NULL and tp.client.clientCode = '"+clientCode+"' " +
										" AND lower(tp.vehicleSeatClass.vehicleSeatClassCode) = '"+generalTripClass.toLowerCase() +"'";
								//log.info"daSQL ===> " + daSql);
								Collection<VehicleTripPrice> vehicleTripPrices = (Collection<VehicleTripPrice>)swpService.getAllRecordsByHQL(daSql);
								//log.info"v size ---> " + vehicleTripPrices.size());
								
								messageResponse = ticketTransactionRef +"###"+transactionRef+"###"+deviceCode+"###"+tripCode +"###"+client.getClientCode()+
										"###" + departingStationCode + "###" + arrivalStationCode + "###" + departureStation.getStationName() + 
										"###" + arrivalStation.getStationName() + "###" + departureTime + "###" + arrivalTime;
								
								
								if(vehicleTripPrices.size()==0)
								{
									jsonObject.add("status", ERROR.NO_TRIPS_FOUND_FOR_SPECIFIED_DATES);
									jsonObject.add("message", "No pricing Found For the Specified trip");
									JsonObject jsonObj = jsonObject.build();
						            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
								}
								
								Iterator<VehicleTripPrice> itVTP = vehicleTripPrices.iterator();
								while(itVTP.hasNext())
								{
									VehicleTripPrice vtp = itVTP.next();
									if(vtp.getPriceType().equals(PriceType.FOR_ADULT_PRICE))
									{
										baseAdultFare = vtp.getAmount();
									}
									else if(vtp.getPriceType().equals(PriceType.FOR_CHILD_PRICE))
									{
										baseChildFare = vtp.getAmount();
									}
									else if(vtp.getPriceType().equals(PriceType.FOR_SENIOR_CITIZENS))
									{
										baseSeniorFare = vtp.getAmount();
									}
									else if(vtp.getPriceType().equals(PriceType.FOR_SPECIALLY_ABLED))
									{
										baseDisabledFare = vtp.getAmount();
									}
								}
								
								adultPassenger = (tripEntry.has("adultPassenger") ? tripEntry.getInt("adultPassenger") : 0);
								childPassenger = (tripEntry.has("childPassenger") ? tripEntry.getInt("childPassenger") : 0);
								seniorPassenger = (tripEntry.has("seniorPassenger") ? tripEntry.getInt("seniorPassenger") : 0);
								disabledPassenger = (tripEntry.has("disabledPassenger") ? tripEntry.getInt("disabledPassenger") : 0);
								JSONObject passengerdetails = tripEntry.getJSONObject("passengerdetails");
								leadPassenger = passengerdetails.getJSONObject("leadPassenger");
								otherPassengers = passengerdetails.has("otherPassengers") ? passengerdetails.getJSONArray("otherPassengers") : null;
								
								
								adultTicketPrice = (adultPassenger*baseAdultFare);
								childTicketPrice = (childPassenger*baseChildFare);
								seniorTicketPrice = (seniorPassenger*baseSeniorFare);
								disabledTicketPrice = (disabledPassenger*baseDisabledFare);
								//log.info"adultPrice === " + adultTicketPrice);
								//log.info"childPrice === " + childTicketPrice);
								
								String receiptNo = UtilityHelper.padNumbers(9, (transactionsByLine.size()+1)+"");
								receiptNos.put(receiptNo);
								PurchasedTrip purchasedTrip = new PurchasedTrip();
								purchasedTrip.setVehicleSchedule(vs);
								purchasedTrip.setReceiptNo(receiptNo);
								purchasedTrip.setCreatedAt(new Date());
								purchasedTrip.setUpdatedAt(new Date());
								purchasedTrip.setAmountPayable(adultTicketPrice + childTicketPrice + seniorTicketPrice + disabledTicketPrice/* + client.getBookingFee()*/);
								purchasedTrip.setBookingFee(client.getBookingFee());
								purchasedTrip.setPurchasedTripStatus(PurchasedTripStatus.PENDING);
								purchasedTrip.setTicketCollectionPoint(tcp);
								purchasedTrip.setTransaction(transaction);
								purchasedTrip.setClient(client);
								purchasedTrip.setPassengerCount(adultPassenger + childPassenger + seniorPassenger + disabledPassenger);
								purchasedTrip.setMessageResponse(messageResponse);
								purchasedTrip.setDepartureStation(departureStation);
								purchasedTrip.setArrivalStation(arrivalStation);
								purchasedTrip.setDepartureTime(departureTimeDate);
								purchasedTrip.setArrivalTime(arrivalTimeDate);
								purchasedTrip.setAdultPassengers(adultPassenger);
								purchasedTrip.setChildPassengers(childPassenger);
								purchasedTrip.setSeniorPassengers(seniorPassenger);
								purchasedTrip.setDisabledPassengers(disabledPassenger);
								purchasedTrip.setPurchasePoint(pp);
								purchasedTrip.setTotalUpgradedAdultPassengers(0);
								purchasedTrip.setTotalUpgradedChildPassengers(0);
								purchasedTrip.setTotalUpgradedDisabledPassengers(0);
								purchasedTrip.setTotalUpgradedSeniorPassengers(0);
								purchasedTrip.setTotalUpgradedAmount(0.00);
								purchasedTrip = (PurchasedTrip)(this.swpService.createNewRecord(purchasedTrip));
								
								Customer leadPassengerCustomer = null;
								JSONArray customerListCreated = new JSONArray();
								if(leadPassenger!=null)
								{
									String firstname = leadPassenger.getString("firstname").trim();
									String lastname = leadPassenger.getString("lastname").trim();
									String emailaddress = leadPassenger.getString("emailaddress").trim();
									String mobilenumber = leadPassenger.getString("mobilenumber").trim();
									String nationalid = leadPassenger.getString("nationalid").trim();
									
									if(firstname.length()>0 && lastname.length()>0 && emailaddress.length()>0 && mobilenumber.length()>0)
									{
										leadPassengerCustomer = new Customer();
										leadPassengerCustomer.setClient(client);
										leadPassengerCustomer.setPurchasedTrip(purchasedTrip);
										leadPassengerCustomer.setCreatedAt(new Date());
										leadPassengerCustomer.setUpdatedAt(new Date());
										leadPassengerCustomer.setCustomerStatus(CustomerStatus.ACTIVE);
										leadPassengerCustomer.setEmailAddress(emailaddress);
										leadPassengerCustomer.setFirstName(firstname);
										leadPassengerCustomer.setLastName(lastname);
										leadPassengerCustomer.setMeansOfId(nationalid);
										leadPassengerCustomer.setMobileNumber(mobilenumber);
										leadPassengerCustomer.setIsLeadPassenger(Boolean.TRUE);
										leadPassengerCustomer.setVerificationNumber(RandomStringUtils.randomNumeric(12));
										leadPassengerCustomer = (Customer)swpService.createNewRecord(leadPassengerCustomer);
										customerListCreated.put(leadPassengerCustomer);
									}
									
									if(otherPassengers!=null)
									{
										for(int j=0; j<otherPassengers.length(); j++)
										{
											JSONObject otherPassenger = otherPassengers.getJSONObject(j);
											firstname = otherPassenger.getString("firstname").trim();
											lastname = otherPassenger.getString("lastname").trim();
											emailaddress = otherPassenger.getString("emailaddress").trim();
											mobilenumber = otherPassenger.getString("mobilenumber").trim();
											nationalid = otherPassenger.getString("nationalid").trim();
											
											if(firstname.length()>0 && lastname.length()>0 && emailaddress.length()>0 && mobilenumber.length()>0)
											{
												Customer otherPassengerCustomer = new Customer();
												otherPassengerCustomer.setClient(client);
												otherPassengerCustomer.setPurchasedTrip(purchasedTrip);
												otherPassengerCustomer.setCreatedAt(new Date());
												otherPassengerCustomer.setUpdatedAt(new Date());
												otherPassengerCustomer.setCustomerStatus(CustomerStatus.ACTIVE);
												otherPassengerCustomer.setEmailAddress(emailaddress);
												otherPassengerCustomer.setFirstName(firstname);
												otherPassengerCustomer.setLastName(lastname);
												otherPassengerCustomer.setMeansOfId(nationalid);
												otherPassengerCustomer.setMobileNumber(mobilenumber);
												otherPassengerCustomer.setIsLeadPassenger(Boolean.FALSE);
												otherPassengerCustomer.setVerificationNumber(RandomStringUtils.randomNumeric(12));
												otherPassengerCustomer = (Customer)swpService.createNewRecord(otherPassengerCustomer);
												customerListCreated.put(otherPassengerCustomer);
											}
										}
										
									}
									
									seatIds = new ArrayList<Long>();
									seatsAlloted = tripEntry.getJSONObject("seatsAlloted");
									if(seatsAlloted==null)
									{
										skip = Boolean.TRUE;
									}
									else
									{
										Iterator<String> seatsAllotedIter = seatsAlloted.keys();
										//log.info"numberOfSeats = " + seatsAlloted.toString());
										while(seatsAllotedIter.hasNext())
										{
											String seatsAllotedIterStr = seatsAllotedIter.next();
											JSONArray seatsAllotedArray = seatsAlloted.getJSONArray(seatsAllotedIterStr);
											//log.info"seatsAllotedArray Str = " + seatsAllotedArray.toString());
											for(int j=0; j<seatsAllotedArray.length(); j++)
											{
												JSONObject jsSeatAlloted = (seatsAllotedArray.getJSONObject(j));
												//log.info"jsSeatAlloted = " + jsSeatAlloted.toString());
												seatIds.add(jsSeatAlloted.getLong("seatAvailabilityId"));
											}
										}
									}
									
									hql = "Select tp from ScheduleStationSeatAvailability tp where tp.schedSeatAvailId IN ("+StringUtils.join(seatIds, ", ")+") " +
											"AND tp.deletedAt is NULL AND tp.vehicleSeat.client.clientCode = '"+clientCode+"' AND tp.lockedDownBy = '"+purchaseToken+"' AND " +
											"(tp.lockedDownExpiryDate > CURRENT_TIMESTAMP) AND tp.boughtByCustomer IS NULL AND tp.seatAvailabilityStatus = " + SeatAvailabilityStatus.OPEN.ordinal();
											//+ " " +
											//"AND tp.scheduleStationSeatSection.vehicleTripRouting.vehicleTrip.vehicleTripCode = '"+tripCode+"' " +
											//"ORDER BY tp.vehicleTripRouteSeatSection.vehicleTripRouting.routeOrder";
									vsaList = (Collection<ScheduleStationSeatAvailability>)swpService.getAllRecordsByHQL(hql);
									Iterator<ScheduleStationSeatAvailability> vsaListIter = vsaList.iterator();
									int passengerCounter = 0;
									//log.info"hql --- " + hql);
									//log.info"vsalistier size -- " + vsaList.size());
									//log.info"seatIds size -- " + seatIds.size());
									JSONArray vehicleSeatsLockedDown = new JSONArray();
									while(vsaListIter.hasNext())
									{
										ScheduleStationSeatAvailability vsa = vsaListIter.next();
										vsa.setBoughtByCustomer(leadPassengerCustomer);
										vsa.setSeatAvailabilityStatus(SeatAvailabilityStatus.PURCHASED);
										vsa.setUpdatedAt(new Date());
										swpService.updateRecord(vsa);
										
										String channel = "";
										String cardPan = "";
										Long ticketCollectionPointId = null;
										Integer childSeatCount = null;
										Integer adultSeatCount = null;
										
										
										
										
										if(vsa.getPassengerType().equals(PassengerType.ADULT_PASSENGER))
										{
											PurchasedTripSeat pts = new PurchasedTripSeat();
											pts.setCreatedAt(new Date());
											pts.setPurchasedTrip(purchasedTrip);
											pts.setUpdatedAt(new Date());
											pts.setVehicleSeat(vsa.getVehicleSeat());
											pts.setScheduleStationSeatAvailability(vsa);
											pts.setClient(client);
											pts.setPassengerType(PassengerType.ADULT_PASSENGER);
											pts = (PurchasedTripSeat)(this.swpService.createNewRecord(pts));
										}
										else if(vsa.getPassengerType().equals(PassengerType.CHILD_PASSENGER))
										{
											PurchasedTripSeat pts = new PurchasedTripSeat();
											pts.setCreatedAt(new Date());
											pts.setPurchasedTrip(purchasedTrip);
											pts.setUpdatedAt(new Date());
											pts.setVehicleSeat(vsa.getVehicleSeat());
											pts.setScheduleStationSeatAvailability(vsa);
											pts.setClient(client);
											pts.setPassengerType(PassengerType.CHILD_PASSENGER);
											pts = (PurchasedTripSeat)(this.swpService.createNewRecord(pts));
										}
										else if(vsa.getPassengerType().equals(PassengerType.SENIOR_PASSENGER))
										{
											PurchasedTripSeat pts = new PurchasedTripSeat();
											pts.setCreatedAt(new Date());
											pts.setPurchasedTrip(purchasedTrip);
											pts.setUpdatedAt(new Date());
											pts.setVehicleSeat(vsa.getVehicleSeat());
											pts.setScheduleStationSeatAvailability(vsa);
											pts.setClient(client);
											pts.setPassengerType(PassengerType.SENIOR_PASSENGER);
											pts = (PurchasedTripSeat)(this.swpService.createNewRecord(pts));
										}
										else if(vsa.getPassengerType().equals(PassengerType.SPECIAL_NEEDS_PASSENGER))
										{
											PurchasedTripSeat pts = new PurchasedTripSeat();
											pts.setCreatedAt(new Date());
											pts.setPurchasedTrip(purchasedTrip);
											pts.setUpdatedAt(new Date());
											pts.setVehicleSeat(vsa.getVehicleSeat());
											pts.setScheduleStationSeatAvailability(vsa);
											pts.setClient(client);
											pts.setPassengerType(PassengerType.SPECIAL_NEEDS_PASSENGER);
											pts = (PurchasedTripSeat)(this.swpService.createNewRecord(pts));
										}
										
										passengerCounter++;
												
										JSONObject js = new JSONObject();
										js.put("seatAvailabilityId", vsa.getSchedSeatAvailId());
										js.put("seatNumber", vsa.getVehicleSeat().getSeatNumber());
										js.put("seatName", vsa.getVehicleSeat().getSeatNumber());
										js.put("expirationPeriodMinutes", client.getLockDownInterval());
										js.put("seatClass", vsa.getVehicleSeat().getVehicleSeatSection().getSectionName());
										js.put("seatLocation", vsa.getVehicleSeat().getVehicleSeatLocation().name());
										js.put("seatFacingOtherSeats", vsa.getVehicleSeat().getTripSeatFacing());
										js.put("expirationDate", new SimpleDateFormat("yyyy-MM-dd HH:mm").format(vsa.getLockedDownExpiryDate()));
										vehicleSeatsLockedDown.put(js);
									}
									JSONObject tempHolder = new JSONObject();
									tempHolder.put("allSeatsAlloted", vehicleSeatsLockedDown);
									allTripSeatDetails.put(tripCode, tempHolder);
								}
							}
						}
						
						if(coupon!=null)
						{
							coupon.setCouponStatus(CouponStatus.USED);
							swpService.updateRecord(coupon);
							
							jsonObject.add("couponValuation", couponValuation);
							jsonObject.add("concessionAmount", concessionAmount);
							jsonObject.add("coupon", new Gson().toJson(coupon));
						}
						
						jsonObject.add("seatsAlloted", allTripSeatDetails.toString());
						jsonObject.add("transactionRef", transaction.getOrderRef());
						jsonObject.add("wallet", new Gson().toJson(wallet));
						jsonObject.add("receiptNos", receiptNos.toString());
						//jsonObject.add("tripDetails", tripSummary.toString());
						jsonObject.add("status", ERROR.GENERAL_SUCCESS);
						jsonObject.add("message", "Trip purchase was successful");
						JsonObject jsonObj = jsonObject.build();
			            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
					
					}
					else
					{
						//Transaction was not successful
						jsonObject.add("message", "Transaction Failed. Transaction was not successful");
						jsonObject.add("status", ERROR.TICKET_PURCHASE_FAILED);
						JsonObject jsonObj = jsonObject.build();
			            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
					}
				}
			}
			
			
			
			else if(paymentType.equals(PaymentMeans.CASH.name()))
			{
				RoleType roleCode = null;
				if(token==null)
				{
					jsonObject.add("status", ERROR.INSUFFICIENT_PRIVILEDGES);
					jsonObject.add("message", "Insufficient priviledges. You can not carry out this transaction");
					JsonObject jsonObj = jsonObject.build();
		            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
				}
				
				JSONObject purchaseDetailsJS = new JSONObject(purchaseDetails);
				tripTicketDataJSArray = purchaseDetailsJS.getJSONObject("tripTicketData");
				String orderId = purchaseDetailsJS.has("orderId") ? purchaseDetailsJS.getString("orderId") : null;
				String hash = purchaseDetailsJS.has("hash") ? purchaseDetailsJS.getString("hash") : null;
				// || probasePayMerchantCode==null || probasePayDeviceCode==null


				if(tripTicketDataJSArray==null || hash==null)
				{
					jsonObject.add("status", ERROR.INCOMPLETE_PARAMETERS);
					jsonObject.add("message", "Incomplete parameters provided. Ensure you provide appropriate parameters");
					JsonObject jsonObj = jsonObject.build();
		            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
				}
				JSONObject jsObj = new JSONObject();
				
				messageRequest = hash+"###"+deviceCode+"###"+transactionRef+"###"+token+"###"+"###"+ticketCollectionCenterCode
						+"###"+ipAddress+"###"+clientCode;

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
				
				if(user==null || (roleCode!=null && (!roleCode.equals(RoleType.OPERATOR) && !roleCode.equals(RoleType.ADMIN_STAFF))))
				{
					jsonObject.add("status", ERROR.INVALID_STATION_CREATION_PRIVILEDGES);
					jsonObject.add("message", "Invalid Creation Priviledges");
					JsonObject jsonObj = jsonObject.build();
		            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
				}
				
				
				TicketCollectionPoint tcp = null;
				if(ticketCollectionCenterCode!=null)
				{
					hql = "Select tp from TicketCollectionPoint tp where tp.collectionPointCode = '"+ticketCollectionCenterCode+"' AND tp.deletedAt IS NULL " +
							"AND tp.client.clientCode = '"+clientCode+"'";
					tcp = (TicketCollectionPoint)swpService.getUniqueRecordByHQL(hql);
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



				JSONObject allTripSeatDetails = new JSONObject();
				List<Long> seatIds = new ArrayList<Long>();
				Iterator<String> outwardInwardIter = tripTicketDataJSArray.keys();
				while(outwardInwardIter.hasNext())
				{
					String outwardInwardStr = outwardInwardIter.next();
					//log.info"outwardInwardStr -- " + outwardInwardStr);
					JSONObject outwardTrips = tripTicketDataJSArray.getJSONObject(outwardInwardStr);
					//log.info"outwardTrips -- " + outwardTrips.toString());
					JSONArray tripCodes = outwardTrips.names();
					for(int i=0; i<tripCodes.length(); i++)
					{
						String tripCode = tripCodes.getString(i);
						//log.info"tripCode -- " + tripCode);
						JSONObject tripEntry = outwardTrips.getJSONObject(tripCode);
						//log.info"tripEntry --- " + tripEntry.toString());
						Integer adultPassenger = 0;
						Integer childPassenger = 0;
						Integer seniorPassenger = 0;
						Integer disabledPassenger = 0;
						JSONObject seatsAlloted = tripEntry.getJSONObject("seatsAlloted");
						//log.info"seatsAlloted --- " + seatsAlloted.toString());
						if(seatsAlloted==null)
						{
							skip = Boolean.TRUE;
						}
						else
						{
							Iterator<String> seatsAllotedIter = seatsAlloted.keys();
							while(seatsAllotedIter.hasNext())
							{
								String seatsAllotedIterStr = seatsAllotedIter.next();
								JSONArray seatsAllotedArray = seatsAlloted.getJSONArray(seatsAllotedIterStr);
								//log.info"seatsAllotedArray Str = " + seatsAllotedArray.toString());
								for(int j=0; j<seatsAllotedArray.length(); j++)
								{
									JSONObject jsSeatAlloted = (seatsAllotedArray.getJSONObject(j));
									//log.info"jsSeatAlloted = " + jsSeatAlloted.toString());
									seatIds.add(jsSeatAlloted.getLong("seatAvailabilityId"));
								}
							}
							
						}
						adultPassenger = (tripEntry.has("adultPassenger") ? tripEntry.getInt("adultPassenger") : 0);
						childPassenger = (tripEntry.has("childPassenger") ? tripEntry.getInt("childPassenger") : 0);
						seniorPassenger = (tripEntry.has("seniorPassenger") ? tripEntry.getInt("seniorPassenger") : 0);
						disabledPassenger = (tripEntry.has("disabledPassenger") ? tripEntry.getInt("disabledPassenger") : 0);
						JSONObject passengerdetails = tripEntry.getJSONObject("passengerdetails");
						System.out.println(passengerdetails.toString());
						JSONObject leadPassenger = passengerdetails.getJSONObject("leadPassenger");
						JSONArray otherPassengers = passengerdetails.has("otherPassengers") ? passengerdetails.getJSONArray("otherPassengers") : null;
					}
				}
				
				//log.info"...join == " + StringUtils.join(seatIds, ", "));
				
				if(skip.equals(Boolean.TRUE))
				{
					jsonObject.add("status", ERROR.INCOMPLETE_PARAMETERS_SEATS_NOT_ALLOTTED_COMPLETELY);
					jsonObject.add("message", "Seats purchased not specified completely");
					JsonObject jsonObj = jsonObject.build();
		            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
				}
				else
				{
					hql = "Select tp from ScheduleStationSeatAvailability tp where tp.schedSeatAvailId IN ("+StringUtils.join(seatIds, ", ")+") " +
							"AND tp.deletedAt is NULL AND tp.vehicleSeat.client.clientCode = '"+clientCode+"' AND tp.lockedDownBy = '"+purchaseToken+"' AND " +
							"(tp.lockedDownExpiryDate > CURRENT_TIMESTAMP) AND tp.seatAvailabilityStatus = "+ SeatAvailabilityStatus.OPEN.ordinal() +"";
					//log.info"....HQL = " + hql);
					Collection<ScheduleStationSeatAvailability> vsaList = (Collection<ScheduleStationSeatAvailability>)swpService.getAllRecordsByHQL(hql);
					//log.info"vsaList.size()<seatIds.size() ==" + vsaList.size() + " -- " + seatIds.size());
					if(vsaList!=null && vsaList.size()<seatIds.size())
					{
						jsonObject.add("status", ERROR.INCOMPLETE_SEATS_AVAILABLE_PURCHASE_TIME_EXPIRED);
						jsonObject.add("message", "Time expired for purchasing the seats alloted to you");
						JsonObject jsonObj = jsonObject.build();
			            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
					}

					
					Double adultTicketPrice = 0.00;
					Double childTicketPrice = 0.00;
					Double seniorTicketPrice = 0.00;
					Double disabledTicketPrice = 0.00;
					Double totalPrice = 0.00;
					
					
					outwardInwardIter = tripTicketDataJSArray.keys();
					while(outwardInwardIter.hasNext())
					{
						String outwardInwardStr = outwardInwardIter.next();
						JSONObject outwardTrips = tripTicketDataJSArray.getJSONObject(outwardInwardStr);
						JSONArray tripCodes = outwardTrips.names();
						for(int i=0; i<tripCodes.length(); i++)
						{
							String tripCode = tripCodes.getString(i);
							JSONObject tripEntry = outwardTrips.getJSONObject(tripCode);
							Integer adultPassenger = 0;
							Integer childPassenger = 0;
							Integer seniorPassenger = 0;
							Integer disabledPassenger = 0;
							
							hql = "Select tp from VehicleSchedule tp where tp.scheduleStationCode.scheduleStationCode = '"+tripCode+"' " +
									"AND tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL";
							VehicleSchedule vs = (VehicleSchedule)swpService.getUniqueRecordByHQL(hql);
							
							Double baseAdultFare = 0.00;
							Double baseChildFare = 0.00;
							Double baseSeniorFare = 0.00;
							Double baseDisabledFare = 0.00;
							
							hql = "Select tp.purchasedTripId from PurchasedTrip tp where " +
									//"tp.vehicleSchedule.line.lineId = '" + vs.getLine().getLineId() + "' AND " +
									"tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL";
							//log.info"PurchasedTrip... = " + hql);
							List<Long> transactionsByLine = (List<Long>)swpService.getAllRecordsByHQL(hql);
							
							String departingStationCode = tripEntry.getString("departingStation");
							String arrivalStationCode = tripEntry.getString("arrivingStation");
							
							String daSql = "Select tp from Station tp where tp.stationCode = '"+departingStationCode+"' AND " +
									"tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL";
							Station departureStation = (Station)swpService.getUniqueRecordByHQL(daSql);

							daSql = "Select tp from Station tp where tp.stationCode = '"+arrivalStationCode+"' AND " +
										"tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL";
							Station arrivalStation = (Station)swpService.getUniqueRecordByHQL(daSql);
							
							daSql = "Select tp from TripZoneStation tp where tp.station.stationCode = '"+arrivalStationCode+"' AND " +
									"tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL";
							TripZoneStation arrivalTripZoneStation = (TripZoneStation)swpService.getUniqueRecordByHQL(daSql);
															
							daSql = "Select tp from TripZoneStation tp where tp.station.stationCode = '"+departingStationCode+"' AND " +
									"tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL";
							TripZoneStation depatureTripZoneStation = (TripZoneStation)swpService.getUniqueRecordByHQL(daSql);
							

							//log.info"6 .... >>>>>" + arrivalTripZoneStation.getTripZone().getRouteOrder());
							//log.info"6 .... >>>>>" + depatureTripZoneStation.getTripZone().getRouteOrder());
							
							Integer tripRouteOrderdifference =  depatureTripZoneStation.getTripZone().getRouteOrder() - arrivalTripZoneStation.getTripZone().getRouteOrder();
							tripRouteOrderdifference = Math.abs(tripRouteOrderdifference);
							tripRouteOrderdifference = tripRouteOrderdifference + 1;
							
							daSql = "Select tp from VehicleTripPrice tp where tp.finalDestinationTripZone.routeOrder = " + tripRouteOrderdifference +  
									" AND tp.deletedAt is NULL and tp.client.clientCode = '"+clientCode+"' " +
									" AND lower(tp.vehicleSeatClass.vehicleSeatClassCode) = '"+generalTripClass.toLowerCase() +"'";
							//log.info"daSQL ===> " + daSql);
							Collection<VehicleTripPrice> vehicleTripPrices = (Collection<VehicleTripPrice>)swpService.getAllRecordsByHQL(daSql);
							//log.info"v size ---> " + vehicleTripPrices.size());


							Iterator<VehicleTripPrice> itVTP = vehicleTripPrices.iterator();
							while(itVTP.hasNext())
							{
								VehicleTripPrice vtp = itVTP.next();
								if(vtp.getPriceType().equals(PriceType.FOR_ADULT_PRICE))
								{
									baseAdultFare = vtp.getAmount();
								}
								else if(vtp.getPriceType().equals(PriceType.FOR_CHILD_PRICE))
								{
									baseChildFare = vtp.getAmount();
								}
								else if(vtp.getPriceType().equals(PriceType.FOR_SENIOR_CITIZENS))
								{
									baseSeniorFare = vtp.getAmount();
								}
								else if(vtp.getPriceType().equals(PriceType.FOR_SPECIALLY_ABLED))
								{
									baseDisabledFare = vtp.getAmount();
								}
							}
							
							adultPassenger = (tripEntry.has("adultPassenger") ? tripEntry.getInt("adultPassenger") : 0);
							childPassenger = (tripEntry.has("childPassenger") ? tripEntry.getInt("childPassenger") : 0);
							seniorPassenger = (tripEntry.has("seniorPassenger") ? tripEntry.getInt("seniorPassenger") : 0);
							disabledPassenger = (tripEntry.has("disabledPassenger") ? tripEntry.getInt("disabledPassenger") : 0);
							
							adultTicketPrice = (adultPassenger*baseAdultFare);
							childTicketPrice = (childPassenger*baseChildFare);
							seniorTicketPrice = (seniorPassenger*baseSeniorFare);
							disabledTicketPrice = (disabledPassenger*baseDisabledFare);
							//log.info"adultPrice === " + adultTicketPrice);
							//log.info"childPrice === " + childTicketPrice);
							//log.info"seniorPrice === " + seniorTicketPrice);
							//log.info"disabledPrice === " + disabledTicketPrice);
						}
						totalPrice = adultTicketPrice + childTicketPrice + seniorTicketPrice + disabledTicketPrice;
						//log.info"*******total price = " + totalPrice);
					}
					
					Coupon coupon = null;
					if(couponCode!=null)
					{
						hql = "Select tp from Coupon tp where lower(tp.couponCode) = '"+couponCode.toLowerCase()+"' AND tp.deletedAt IS NULL AND tp.client.clientId = " + client.getClientId();
						coupon = (Coupon)swpService.getUniqueRecordByHQL(hql);
						if(coupon!=null)
						{
							totalPrice = totalPrice - (totalPrice*coupon.getDiscountRate()/100); 
						}
					}
					
					if(user!=null && (roleCode.equals(RoleType.OPERATOR) || roleCode.equals(RoleType.ADMIN_STAFF)))
					{
						if(concessionAmount!=null)
						{
							totalPrice = totalPrice - concessionAmount;
						}
					}
					else
					{
						concessionAmount = null;
					}
					
					totalPrice = totalPrice + (client.getBookingFee()==null ? 0 : client.getBookingFee());
					
					String status = null;
					status = "00";
					
					
					
					String serviceType = ServiceType.CASH_COLLECT_FOR_TRIP.name();
					JSONArray tripSummary = new JSONArray();
					if(status!=null && status.equals("00"))
					{
						
						String ticketTransactionRef = RandomStringUtils.randomNumeric(8);
						hql = "Select tp from Transaction tp where tp.orderRef = '"+(device.getDeviceCode()+"-"+transactionRef)+"' AND tp.client.clientCode = '"+clientCode+"' AND " +
								"tp.deletedAt IS NULL";
						Transaction transaction = (Transaction)swpService.getUniqueRecordByHQL(hql);
						
						if(transaction!=null)
						{
							jsonObject.add("status", ERROR.TRANSACTION_REF_ALREADY_PAID_PREVIOUSLY);
							jsonObject.add("message", "Transaction Reference you provided belongs to another transaction that has already been paid");
							JsonObject jsonObj = jsonObject.build();
				            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
						}
						
						JSONObject vehicleSeatsLockedDownTrip = new JSONObject();
						
						Double fixedCharge = 0.00;
						Double transactionFee = 0.00;
						Double transactionAmount = totalPrice;
						
						
						if(user!=null && (roleCode.equals(RoleType.OPERATOR) || roleCode.equals(RoleType.ADMIN_STAFF)))
						{
						}
						else
						{
							concessionAmount = null;
						}
							
						transaction = new Transaction();
						transaction.setTransactionRef(ticketTransactionRef);
						transaction.setOrderRef(device.getDeviceCode()+ "-" + transactionRef);
						transaction.setChannel(com.probase.reservationticketingwebservice.enumerations.Channel.CASH);
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
						transaction.setNarration("Credit|"+"#" + device.getDeviceCode()+"-"+transactionRef + "|ZMW" + transaction.getTransactionAmount());
						transaction.setUpdatedAt(new Date());
						transaction.setCreatedAt(new Date());
						transaction.setPaymentMeans(PaymentMeans.VENDOR_WALLET);
						transaction.setClient(client);						
						transaction.setTransactionCurrency("ZMW");
						transaction.setConcessionApplied(concessionAmount);
						if(coupon!=null)
						{
							transaction.setAppliedCoupon(coupon);
						}
						transaction = (Transaction)swpService.createNewRecord(transaction);
						
						JSONObject jsonTrips = new JSONObject();
						JSONObject leadPassenger = null;
						JSONArray otherPassengers = null;
						
						
						outwardInwardIter = tripTicketDataJSArray.keys();
						while(outwardInwardIter.hasNext())
						{
							String outwardInwardStr = outwardInwardIter.next();
							JSONObject outwardTrips = tripTicketDataJSArray.getJSONObject(outwardInwardStr);
							JSONArray tripCodes = outwardTrips.names();
							for(int i=0; i<tripCodes.length(); i++)
							{
								String tripCode = tripCodes.getString(i);
								JSONObject tripEntry = outwardTrips.getJSONObject(tripCode);
								Integer adultPassenger = 0;
								Integer childPassenger = 0;
								Integer seniorPassenger = 0;
								Integer disabledPassenger = 0;
								JSONObject seatsAlloted = tripEntry.getJSONObject("seatsAlloted");
								
								hql = "Select tp from VehicleSchedule tp where tp.scheduleStationCode.scheduleStationCode = '"+tripCode+"' " +
										"AND tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL";
								VehicleSchedule vs = (VehicleSchedule)swpService.getUniqueRecordByHQL(hql);
								
								adultTicketPrice = 0.00;
								childTicketPrice = 0.00;
								seniorTicketPrice = 0.00;
								disabledTicketPrice = 0.00;
								Double baseAdultFare = 0.00;
								Double baseChildFare = 0.00;
								Double baseSeniorFare = 0.00;
								Double baseDisabledFare = 0.00;
								
								hql = "Select tp.purchasedTripId from PurchasedTrip tp where " +
										//"tp.vehicleSchedule.line.lineId = '" + vs.getLine().getLineId() + "' AND " +
										"tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL";
								//log.info"PurchasedTrip... = " + hql);
								List<Long> transactionsByLine = (List<Long>)swpService.getAllRecordsByHQL(hql);
								
								String departingStationCode = tripEntry.getString("departingStation");
								String arrivalStationCode = tripEntry.getString("arrivingStation");
								String departureTime = tripEntry.getString("departureTime");
								String arrivalTime = tripEntry.getString("arrivalTime");
								Date departureTimeDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(departureTime);
								Date arrivalTimeDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(arrivalTime);
								
								String daSql = "Select tp from Station tp where tp.stationCode = '"+departingStationCode+"' AND " +
										"tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL";
								Station departureStation = (Station)swpService.getUniqueRecordByHQL(daSql);
	
								daSql = "Select tp from Station tp where tp.stationCode = '"+arrivalStationCode+"' AND " +
											"tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL";
								Station arrivalStation = (Station)swpService.getUniqueRecordByHQL(daSql);
								
								daSql = "Select tp from TripZoneStation tp where tp.station.stationCode = '"+arrivalStationCode+"' AND " +
										"tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL";
								TripZoneStation arrivalTripZoneStation = (TripZoneStation)swpService.getUniqueRecordByHQL(daSql);
																
								daSql = "Select tp from TripZoneStation tp where tp.station.stationCode = '"+departingStationCode+"' AND " +
										"tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL";
								TripZoneStation depatureTripZoneStation = (TripZoneStation)swpService.getUniqueRecordByHQL(daSql);
								

								//log.info"7 .... >>>>>" + arrivalTripZoneStation.getTripZone().getRouteOrder());
								//log.info"7 .... >>>>>" + depatureTripZoneStation.getTripZone().getRouteOrder());
								
								Integer tripRouteOrderdifference =  depatureTripZoneStation.getTripZone().getRouteOrder() - arrivalTripZoneStation.getTripZone().getRouteOrder();
								tripRouteOrderdifference = Math.abs(tripRouteOrderdifference);
								tripRouteOrderdifference = tripRouteOrderdifference + 1;
								
								daSql = "Select tp from VehicleTripPrice tp where tp.finalDestinationTripZone.routeOrder = " + tripRouteOrderdifference +  
										" AND tp.deletedAt is NULL and tp.client.clientCode = '"+clientCode+"' " +
										" AND lower(tp.vehicleSeatClass.vehicleSeatClassCode) = '"+generalTripClass.toLowerCase() +"'";
								//log.info"daSQL ===> " + daSql);
								Collection<VehicleTripPrice> vehicleTripPrices = (Collection<VehicleTripPrice>)swpService.getAllRecordsByHQL(daSql);
								//log.info"v size ---> " + vehicleTripPrices.size());
								
								messageResponse = ticketTransactionRef +"###"+transactionRef+"###"+deviceCode+"###"+tripCode +"###"+client.getClientCode()+
										"###" + departingStationCode + "###" + arrivalStationCode + "###" + departureStation.getStationName() + 
										"###" + arrivalStation.getStationName() + "###" + departureTime + "###" + arrivalTime;
								
								
								if(vehicleTripPrices.size()==0)
								{
									jsonObject.add("status", ERROR.NO_TRIPS_FOUND_FOR_SPECIFIED_DATES);
									jsonObject.add("message", "No pricing Found For the Specified trip");
									JsonObject jsonObj = jsonObject.build();
						            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
								}
								
								Iterator<VehicleTripPrice> itVTP = vehicleTripPrices.iterator();
								while(itVTP.hasNext())
								{
									VehicleTripPrice vtp = itVTP.next();
									if(vtp.getPriceType().equals(PriceType.FOR_ADULT_PRICE))
									{
										baseAdultFare = vtp.getAmount();
									}
									else if(vtp.getPriceType().equals(PriceType.FOR_CHILD_PRICE))
									{
										baseChildFare = vtp.getAmount();
									}
									else if(vtp.getPriceType().equals(PriceType.FOR_SENIOR_CITIZENS))
									{
										baseSeniorFare = vtp.getAmount();
									}
									else if(vtp.getPriceType().equals(PriceType.FOR_SPECIALLY_ABLED))
									{
										baseDisabledFare = vtp.getAmount();
									}
								}
								
								adultPassenger = (tripEntry.has("adultPassenger") ? tripEntry.getInt("adultPassenger") : 0);
								childPassenger = (tripEntry.has("childPassenger") ? tripEntry.getInt("childPassenger") : 0);
								seniorPassenger = (tripEntry.has("seniorPassenger") ? tripEntry.getInt("seniorPassenger") : 0);
								disabledPassenger = (tripEntry.has("disabledPassenger") ? tripEntry.getInt("disabledPassenger") : 0);
								JSONObject passengerdetails = tripEntry.getJSONObject("passengerdetails");
								leadPassenger = passengerdetails.getJSONObject("leadPassenger");
								otherPassengers = passengerdetails.has("otherPassengers") ? passengerdetails.getJSONArray("otherPassengers") : null;
								
								
								adultTicketPrice = (adultPassenger*baseAdultFare);
								childTicketPrice = (childPassenger*baseChildFare);
								seniorTicketPrice = (seniorPassenger*baseSeniorFare);
								disabledTicketPrice = (disabledPassenger*baseDisabledFare);
								//log.info"adultPrice === " + adultTicketPrice);
								//log.info"childPrice === " + childTicketPrice);
								
								
								PurchasedTrip purchasedTrip = new PurchasedTrip();
								purchasedTrip.setVehicleSchedule(vs);
								String receiptNo = UtilityHelper.padNumbers(9, (transactionsByLine.size()+1)+"");
								receiptNos.put(receiptNo);
								purchasedTrip.setReceiptNo(receiptNo);
								purchasedTrip.setCreatedAt(new Date());
								purchasedTrip.setUpdatedAt(new Date());
								purchasedTrip.setAmountPayable(adultTicketPrice + childTicketPrice + seniorTicketPrice + disabledTicketPrice/* + client.getBookingFee()*/);
								purchasedTrip.setBookingFee(client.getBookingFee());
								purchasedTrip.setPurchasedTripStatus(PurchasedTripStatus.PENDING);
								purchasedTrip.setTicketCollectionPoint(tcp);
								purchasedTrip.setTransaction(transaction);
								purchasedTrip.setClient(client);
								purchasedTrip.setPassengerCount(adultPassenger + childPassenger + seniorPassenger + disabledPassenger);
								purchasedTrip.setMessageResponse(messageResponse);
								purchasedTrip.setDepartureStation(departureStation);
								purchasedTrip.setArrivalStation(arrivalStation);
								purchasedTrip.setDepartureTime(departureTimeDate);
								purchasedTrip.setArrivalTime(arrivalTimeDate);
								purchasedTrip.setAdultPassengers(adultPassenger);
								purchasedTrip.setChildPassengers(childPassenger);
								purchasedTrip.setSeniorPassengers(seniorPassenger);
								purchasedTrip.setDisabledPassengers(disabledPassenger);
								purchasedTrip.setPurchasePoint(pp);
								purchasedTrip.setTotalUpgradedAdultPassengers(0);
								purchasedTrip.setTotalUpgradedChildPassengers(0);
								purchasedTrip.setTotalUpgradedDisabledPassengers(0);
								purchasedTrip.setTotalUpgradedSeniorPassengers(0);
								purchasedTrip.setTotalUpgradedAmount(0.00);
								purchasedTrip = (PurchasedTrip)(this.swpService.createNewRecord(purchasedTrip));
								
								Customer leadPassengerCustomer = null;
								JSONArray customerListCreated = new JSONArray();
								if(leadPassenger!=null)
								{
									String firstname = leadPassenger.getString("firstname").trim();
									String lastname = leadPassenger.getString("lastname").trim();
									String emailaddress = leadPassenger.getString("emailaddress").trim();
									String mobilenumber = leadPassenger.getString("mobilenumber").trim();
									String nationalid = leadPassenger.getString("nationalid").trim();
									
									if(firstname.length()>0 && lastname.length()>0 && emailaddress.length()>0 && mobilenumber.length()>0)
									{
										leadPassengerCustomer = new Customer();
										leadPassengerCustomer.setClient(client);
										leadPassengerCustomer.setPurchasedTrip(purchasedTrip);
										leadPassengerCustomer.setCreatedAt(new Date());
										leadPassengerCustomer.setUpdatedAt(new Date());
										leadPassengerCustomer.setCustomerStatus(CustomerStatus.ACTIVE);
										leadPassengerCustomer.setEmailAddress(emailaddress);
										leadPassengerCustomer.setFirstName(firstname);
										leadPassengerCustomer.setLastName(lastname);
										leadPassengerCustomer.setMeansOfId(nationalid);
										leadPassengerCustomer.setMobileNumber(mobilenumber);
										leadPassengerCustomer.setIsLeadPassenger(Boolean.TRUE);
										leadPassengerCustomer.setVerificationNumber(RandomStringUtils.randomNumeric(12));
										leadPassengerCustomer = (Customer)swpService.createNewRecord(leadPassengerCustomer);
										customerListCreated.put(leadPassengerCustomer);
									}
									
									if(otherPassengers!=null)
									{
										for(int j=0; j<otherPassengers.length(); j++)
										{
											JSONObject otherPassenger = otherPassengers.getJSONObject(j);
											firstname = otherPassenger.getString("firstname").trim();
											lastname = otherPassenger.getString("lastname").trim();
											emailaddress = otherPassenger.getString("emailaddress").trim();
											mobilenumber = otherPassenger.getString("mobilenumber").trim();
											nationalid = otherPassenger.getString("nationalid").trim();
											
											if(firstname.length()>0 && lastname.length()>0 && emailaddress.length()>0 && mobilenumber.length()>0)
											{
												Customer otherPassengerCustomer = new Customer();
												otherPassengerCustomer.setClient(client);
												otherPassengerCustomer.setPurchasedTrip(purchasedTrip);
												otherPassengerCustomer.setCreatedAt(new Date());
												otherPassengerCustomer.setUpdatedAt(new Date());
												otherPassengerCustomer.setCustomerStatus(CustomerStatus.ACTIVE);
												otherPassengerCustomer.setEmailAddress(emailaddress);
												otherPassengerCustomer.setFirstName(firstname);
												otherPassengerCustomer.setLastName(lastname);
												otherPassengerCustomer.setMeansOfId(nationalid);
												otherPassengerCustomer.setMobileNumber(mobilenumber);
												otherPassengerCustomer.setIsLeadPassenger(Boolean.FALSE);
												otherPassengerCustomer.setVerificationNumber(RandomStringUtils.randomNumeric(12));
												otherPassengerCustomer = (Customer)swpService.createNewRecord(otherPassengerCustomer);
												customerListCreated.put(otherPassengerCustomer);
											}
										}
										
									}
									
									seatIds = new ArrayList<Long>();
									seatsAlloted = tripEntry.getJSONObject("seatsAlloted");
									if(seatsAlloted==null)
									{
										skip = Boolean.TRUE;
									}
									else
									{
										Iterator<String> seatsAllotedIter = seatsAlloted.keys();
										//log.info"numberOfSeats = " + seatsAlloted.toString());
										while(seatsAllotedIter.hasNext())
										{
											String seatsAllotedIterStr = seatsAllotedIter.next();
											JSONArray seatsAllotedArray = seatsAlloted.getJSONArray(seatsAllotedIterStr);
											//log.info"seatsAllotedArray Str = " + seatsAllotedArray.toString());
											for(int j=0; j<seatsAllotedArray.length(); j++)
											{
												JSONObject jsSeatAlloted = (seatsAllotedArray.getJSONObject(j));
												//log.info"jsSeatAlloted = " + jsSeatAlloted.toString());
												seatIds.add(jsSeatAlloted.getLong("seatAvailabilityId"));
											}
										}
									}
									
									hql = "Select tp from ScheduleStationSeatAvailability tp where tp.schedSeatAvailId IN ("+StringUtils.join(seatIds, ", ")+") " +
											"AND tp.deletedAt is NULL AND tp.vehicleSeat.client.clientCode = '"+clientCode+"' AND tp.lockedDownBy = '"+purchaseToken+"' AND " +
											"(tp.lockedDownExpiryDate > CURRENT_TIMESTAMP) AND tp.boughtByCustomer IS NULL AND tp.seatAvailabilityStatus = " + SeatAvailabilityStatus.OPEN.ordinal();
											//+ " " +
											//"AND tp.scheduleStationSeatSection.vehicleTripRouting.vehicleTrip.vehicleTripCode = '"+tripCode+"' " +
											//"ORDER BY tp.vehicleTripRouteSeatSection.vehicleTripRouting.routeOrder";
									vsaList = (Collection<ScheduleStationSeatAvailability>)swpService.getAllRecordsByHQL(hql);
									Iterator<ScheduleStationSeatAvailability> vsaListIter = vsaList.iterator();
									int passengerCounter = 0;
									//log.info"hql --- " + hql);
									//log.info"vsalistier size -- " + vsaList.size());
									//log.info"seatIds size -- " + seatIds.size());
									JSONArray vehicleSeatsLockedDown = new JSONArray();
									while(vsaListIter.hasNext())
									{
										ScheduleStationSeatAvailability vsa = vsaListIter.next();
										vsa.setBoughtByCustomer(leadPassengerCustomer);
										vsa.setSeatAvailabilityStatus(SeatAvailabilityStatus.PURCHASED);
										vsa.setUpdatedAt(new Date());
										swpService.updateRecord(vsa);
										
										String channel = "";
										String cardPan = "";
										Long ticketCollectionPointId = null;
										Integer childSeatCount = null;
										Integer adultSeatCount = null;
										
										
										
										
										if(vsa.getPassengerType().equals(PassengerType.ADULT_PASSENGER))
										{
											PurchasedTripSeat pts = new PurchasedTripSeat();
											pts.setCreatedAt(new Date());
											pts.setPurchasedTrip(purchasedTrip);
											pts.setUpdatedAt(new Date());
											pts.setVehicleSeat(vsa.getVehicleSeat());
											pts.setScheduleStationSeatAvailability(vsa);
											pts.setClient(client);
											pts.setPassengerType(PassengerType.ADULT_PASSENGER);
											pts = (PurchasedTripSeat)(this.swpService.createNewRecord(pts));
										}
										else if(vsa.getPassengerType().equals(PassengerType.CHILD_PASSENGER))
										{
											PurchasedTripSeat pts = new PurchasedTripSeat();
											pts.setCreatedAt(new Date());
											pts.setPurchasedTrip(purchasedTrip);
											pts.setUpdatedAt(new Date());
											pts.setVehicleSeat(vsa.getVehicleSeat());
											pts.setScheduleStationSeatAvailability(vsa);
											pts.setClient(client);
											pts.setPassengerType(PassengerType.CHILD_PASSENGER);
											pts = (PurchasedTripSeat)(this.swpService.createNewRecord(pts));
										}
										else if(vsa.getPassengerType().equals(PassengerType.SENIOR_PASSENGER))
										{
											PurchasedTripSeat pts = new PurchasedTripSeat();
											pts.setCreatedAt(new Date());
											pts.setPurchasedTrip(purchasedTrip);
											pts.setUpdatedAt(new Date());
											pts.setVehicleSeat(vsa.getVehicleSeat());
											pts.setScheduleStationSeatAvailability(vsa);
											pts.setClient(client);
											pts.setPassengerType(PassengerType.SENIOR_PASSENGER);
											pts = (PurchasedTripSeat)(this.swpService.createNewRecord(pts));
										}
										else if(vsa.getPassengerType().equals(PassengerType.SPECIAL_NEEDS_PASSENGER))
										{
											PurchasedTripSeat pts = new PurchasedTripSeat();
											pts.setCreatedAt(new Date());
											pts.setPurchasedTrip(purchasedTrip);
											pts.setUpdatedAt(new Date());
											pts.setVehicleSeat(vsa.getVehicleSeat());
											pts.setScheduleStationSeatAvailability(vsa);
											pts.setClient(client);
											pts.setPassengerType(PassengerType.SPECIAL_NEEDS_PASSENGER);
											pts = (PurchasedTripSeat)(this.swpService.createNewRecord(pts));
										}
										
										passengerCounter++;
												
										JSONObject js = new JSONObject();
										js.put("seatAvailabilityId", vsa.getSchedSeatAvailId());
										js.put("seatNumber", vsa.getVehicleSeat().getSeatNumber());
										js.put("seatName", vsa.getVehicleSeat().getSeatNumber());
										js.put("expirationPeriodMinutes", client.getLockDownInterval());
										js.put("seatClass", vsa.getVehicleSeat().getVehicleSeatSection().getSectionName());
										js.put("seatLocation", vsa.getVehicleSeat().getVehicleSeatLocation().name());
										js.put("seatFacingOtherSeats", vsa.getVehicleSeat().getTripSeatFacing());
										js.put("expirationDate", new SimpleDateFormat("yyyy-MM-dd HH:mm").format(vsa.getLockedDownExpiryDate()));
										vehicleSeatsLockedDown.put(js);
									}
									JSONObject tempHolder = new JSONObject();
									tempHolder.put("allSeatsAlloted", vehicleSeatsLockedDown);
									allTripSeatDetails.put(tripCode, tempHolder);
								}
							}
						}
						
						if(coupon!=null)
						{
							coupon.setCouponStatus(CouponStatus.USED);
							swpService.updateRecord(coupon);
						}
						
						jsonObject.add("seatsAlloted", allTripSeatDetails.toString());
						jsonObject.add("transactionRef", transaction.getOrderRef());
						jsonObject.add("receiptNos", receiptNos.toString());
						//jsonObject.add("tripDetails", tripSummary.toString());
						jsonObject.add("status", ERROR.GENERAL_SUCCESS);
						jsonObject.add("message", "Trip purchase was successful");
						JsonObject jsonObj = jsonObject.build();
			            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
					
					}
					else
					{
						//Transaction was not successful
						jsonObject.add("message", "Transaction Failed. Transaction was not successful");
						jsonObject.add("status", ERROR.TICKET_PURCHASE_FAILED);
						JsonObject jsonObj = jsonObject.build();
			            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
					}
				}
			}
			
			
			
			else if(paymentType.equals(PaymentMeans.PROBASEPAY.name()))
			{
				RoleType roleCode = null;
				String transactionVerification = null;
				JSONObject transactionVerificationJson = null;
				JSONObject purchaseDetailsJS = new JSONObject(purchaseDetails);
				tripTicketDataJSArray = purchaseDetailsJS.getJSONObject("tripTicketData");
				String probasePayMerchantCode = purchaseDetailsJS.has("probasePayMerchantCode") ? purchaseDetailsJS.getString("probasePayMerchantCode") : null; 
				String probasePayDeviceCode = purchaseDetailsJS.has("probasePayDeviceCode") ? purchaseDetailsJS.getString("probasePayDeviceCode") : null;
				//transactionRef = purchaseDetailsJS.has("transactionRef") ? purchaseDetailsJS.getString("transactionRef") : null;
				String hash = purchaseDetailsJS.has("hash") ? purchaseDetailsJS.getString("hash") : null;
				if(tripTicketDataJSArray==null || probasePayMerchantCode==null || probasePayDeviceCode==null || hash==null)
				{
					jsonObject.add("status", ERROR.INCOMPLETE_PARAMETERS);
					jsonObject.add("message", "Incomplete parameters provided. Ensure you provide merchantCode, probasePayDeviceCode, transactionRef, and hash parameters in your tripTicketData parameter");
					JsonObject jsonObj = jsonObject.build();
		            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
				}
				JSONObject jsObj = new JSONObject();
				transactionVerification = UtilityHelper.sendGet(UtilityHelper.PROBASEPAY_BASE_URL+"/transaction/check-status/"+probasePayMerchantCode+"/"+probasePayDeviceCode+"/"+transactionRef+"/"+hash, 
						null, jsObj);
				transactionVerificationJson = new JSONObject(transactionVerification);
				messageRequest = hash+"###"+deviceCode+"###"+transactionRef+"###"+token+"###"+"###"+ticketCollectionCenterCode
						+"###"+ipAddress+"###"+clientCode;

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
				
				TicketCollectionPoint tcp = null;
				if(ticketCollectionCenterCode!=null)
				{
					hql = "Select tp from TicketCollectionPoint tp where tp.collectionPointCode = '"+ticketCollectionCenterCode+"' AND tp.deletedAt IS NULL " +
							"AND tp.client.clientCode = '"+clientCode+"'";
					tcp = (TicketCollectionPoint)swpService.getUniqueRecordByHQL(hql);
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



				JSONObject allTripSeatDetails = new JSONObject();
				List<Long> seatIds = new ArrayList<Long>();
				Iterator<String> outwardInwardIter = tripTicketDataJSArray.keys();
				while(outwardInwardIter.hasNext())
				{
					String outwardInwardStr = outwardInwardIter.next();
					JSONObject outwardTrips = tripTicketDataJSArray.getJSONObject(outwardInwardStr);
					JSONArray tripCodes = outwardTrips.names();
					for(int i=0; i<tripCodes.length(); i++)
					{
						String tripCode = tripCodes.getString(i);
						JSONObject tripEntry = outwardTrips.getJSONObject(tripCode);
						Integer adultPassenger = 0;
						Integer childPassenger = 0;
						Integer seniorPassenger = 0;
						Integer disabledPassenger = 0;
						JSONObject seatsAlloted = tripEntry.getJSONObject("seatsAlloted");
						if(seatsAlloted==null)
						{
							skip = Boolean.TRUE;
						}
						else
						{
							Iterator<String> seatsAllotedIter = seatsAlloted.keys();
							while(seatsAllotedIter.hasNext())
							{
								String seatsAllotedIterStr = seatsAllotedIter.next();
								JSONArray seatsAllotedArray = seatsAlloted.getJSONArray(seatsAllotedIterStr);
								//log.info"seatsAllotedArray Str = " + seatsAllotedArray.toString());
								for(int j=0; j<seatsAllotedArray.length(); j++)
								{
									JSONObject jsSeatAlloted = (seatsAllotedArray.getJSONObject(j));
									//log.info"jsSeatAlloted = " + jsSeatAlloted.toString());
									seatIds.add(jsSeatAlloted.getLong("seatAvailabilityId"));
								}
							}
							
						}
						adultPassenger = (tripEntry.has("adultPassenger") ? tripEntry.getInt("adultPassenger") : 0);
						childPassenger = (tripEntry.has("childPassenger") ? tripEntry.getInt("childPassenger") : 0);
						seniorPassenger = (tripEntry.has("seniorPassenger") ? tripEntry.getInt("seniorPassenger") : 0);
						disabledPassenger = (tripEntry.has("disabledPassenger") ? tripEntry.getInt("disabledPassenger") : 0);
						JSONObject passengerdetails = tripEntry.getJSONObject("passengerdetails");
						JSONObject leadPassenger = passengerdetails.getJSONObject("leadPassenger");
						JSONArray otherPassengers = passengerdetails.has("otherPassengers") ? passengerdetails.getJSONArray("otherPassengers") : null;
					}
				}
				
				//log.info"...join == " + StringUtils.join(seatIds, ", "));
				
				if(skip.equals(Boolean.TRUE))
				{
					jsonObject.add("status", ERROR.INCOMPLETE_PARAMETERS_SEATS_NOT_ALLOTTED_COMPLETELY);
					jsonObject.add("message", "Seats purchased not specified completely");
					JsonObject jsonObj = jsonObject.build();
		            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
				}
				else
				{
					hql = "Select tp from ScheduleStationSeatAvailability tp where tp.schedSeatAvailId IN ("+StringUtils.join(seatIds, ", ")+") " +
							"AND tp.deletedAt is NULL AND tp.vehicleSeat.client.clientCode = '"+clientCode+"' AND tp.lockedDownBy = '"+purchaseToken+"' AND " +
							"(tp.lockedDownExpiryDate > CURRENT_TIMESTAMP) AND tp.seatAvailabilityStatus = "+ SeatAvailabilityStatus.OPEN.ordinal() +"";
					//log.info"....HQL = " + hql);
					Collection<ScheduleStationSeatAvailability> vsaList = (Collection<ScheduleStationSeatAvailability>)swpService.getAllRecordsByHQL(hql);
					//log.info"vsaList.size()<seatIds.size() ==" + vsaList.size() + " -- " + seatIds.size());
					if(vsaList!=null && vsaList.size()<seatIds.size())
					{
						jsonObject.add("status", ERROR.INCOMPLETE_SEATS_AVAILABLE_PURCHASE_TIME_EXPIRED);
						jsonObject.add("message", "Time expired for purchasing the seats alloted to you");
						JsonObject jsonObj = jsonObject.build();
			            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
					}
					
					
					//log.info"transactionVerification == " + transactionVerification);
					String status = (String)transactionVerificationJson.get("status");
					String serviceType = ServiceType.EPAYMENT_FOR_TRIP.name();
					JSONArray tripSummary = new JSONArray();
					if(status.equals("00"))
					{
						
						String ticketTransactionRef = RandomStringUtils.randomNumeric(8);
						hql = "Select tp from Transaction tp where tp.orderRef = '"+(device.getDeviceCode()+"-"+transactionRef)+"' AND tp.client.clientCode = '"+clientCode+"' AND " +
								"tp.deletedAt IS NULL";
						Transaction transaction = (Transaction)swpService.getUniqueRecordByHQL(hql);
						
						if(transaction!=null)
						{
							jsonObject.add("status", ERROR.TRANSACTION_REF_ALREADY_PAID_PREVIOUSLY);
							jsonObject.add("message", "Transaction Reference you provided belongs to another transaction that has already been paid");
							JsonObject jsonObj = jsonObject.build();
				            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
						}
						
						JSONObject vehicleSeatsLockedDownTrip = new JSONObject();
						
						Double fixedCharge = transactionVerificationJson.has("fixedCharge") ? transactionVerificationJson.getDouble("fixedCharge") : 0.00;
						Double transactionFee = transactionVerificationJson.has("transactionCharge") ? transactionVerificationJson.getDouble("transactionCharge") : 0.00;
						Double transactionAmount = transactionVerificationJson.has("amount") ? transactionVerificationJson.getDouble("amount") : 0.00;
						
						Coupon coupon = null;
						if(couponCode!=null)
						{
							hql = "Select tp from Coupon tp where lower(tp.couponCode) = '"+couponCode.toLowerCase()+"' AND tp.deletedAt IS NULL AND tp.client.clientId = " + client.getClientId();
							coupon = (Coupon)swpService.getUniqueRecordByHQL(hql);
						}
						
						if(user!=null && (roleCode.equals(RoleType.OPERATOR) || roleCode.equals(RoleType.ADMIN_STAFF)))
						{
						}
						else
						{
							concessionAmount = null;
						}
						
						String probasePayPaymentChannel = transactionVerificationJson.getString("channel");
						
						transaction = new Transaction();
						transaction.setTransactionRef(ticketTransactionRef);
						transaction.setOrderRef(device.getDeviceCode()+"-"+transactionRef);
						
						if(probasePayPaymentChannel.equals(com.probase.reservationticketingwebservice.enumerations.Channel.CASH.name()))
						{
							transaction.setChannel(com.probase.reservationticketingwebservice.enumerations.Channel.CASH);
						}
						else if(probasePayPaymentChannel.equals(com.probase.reservationticketingwebservice.enumerations.Channel.ONLINE_BANKING.name()))
						{
							transaction.setChannel(com.probase.reservationticketingwebservice.enumerations.Channel.ONLINE_BANKING);
							String bankCode = transactionVerificationJson.has("bankCode") ? transactionVerificationJson.getString("bankCode") : null;
							String bankName = transactionVerificationJson.has("bankName") ? transactionVerificationJson.getString("bankName") : null;
							hql = "Select tp from Bank tp where tp.bankCode = '"+ bankCode +"'";
							Bank bank = (Bank)swpService.getUniqueRecordByHQL(hql);
							if(bank!=null)
							{
								transaction.setCrBankId(bank.getId());
							}
							transaction.setCrBankName(bankName);
						}
						else if(probasePayPaymentChannel.equals(com.probase.reservationticketingwebservice.enumerations.Channel.OTC.name()))
						{
							transaction.setChannel(com.probase.reservationticketingwebservice.enumerations.Channel.OTC);
							String bankCode = transactionVerificationJson.has("bankCode") ? transactionVerificationJson.getString("bankCode") : null;
							String bankName = transactionVerificationJson.has("bankName") ? transactionVerificationJson.getString("bankName") : null;
							hql = "Select tp from Bank tp where tp.bankCode = '"+ bankCode +"'";
							Bank bank = (Bank)swpService.getUniqueRecordByHQL(hql);
							if(bank!=null)
							{
								transaction.setCrBankId(bank.getId());
							}
							transaction.setCrBankName(bankName);
						}
						else if(probasePayPaymentChannel.equals(com.probase.reservationticketingwebservice.enumerations.Channel.POS.name()))
						{
							transaction.setChannel(com.probase.reservationticketingwebservice.enumerations.Channel.POS);
						}
						else if(probasePayPaymentChannel.equals(com.probase.reservationticketingwebservice.enumerations.Channel.WEB.name()))
						{
							transaction.setChannel(com.probase.reservationticketingwebservice.enumerations.Channel.WEB);
						}
						else if(probasePayPaymentChannel.equals(com.probase.reservationticketingwebservice.enumerations.Channel.WALLET.name()))
						{
							transaction.setChannel(com.probase.reservationticketingwebservice.enumerations.Channel.WALLET);
						}
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
						transaction.setNarration("Debit|#" + device.getDeviceCode()+"-"+transactionRef + "|ZMW" + transaction.getTransactionAmount());
						transaction.setDrCardPan(null);
						transaction.setDrCardId(null);
						transaction.setUpdatedAt(new Date());
						transaction.setCreatedAt(new Date());
						transaction.setPaymentMeans(PaymentMeans.PROBASEPAY);
						transaction.setClient(client);						
						transaction.setTransactionCurrency("ZMW");
						transaction.setConcessionApplied(concessionAmount);
						if(coupon!=null)
							transaction.setAppliedCoupon(coupon);
						transaction = (Transaction)swpService.createNewRecord(transaction);
						
						
						JSONObject jsonTrips = new JSONObject();
						JSONObject leadPassenger = null;
						JSONArray otherPassengers = null;
						
						
						outwardInwardIter = tripTicketDataJSArray.keys();
						while(outwardInwardIter.hasNext())
						{
							String outwardInwardStr = outwardInwardIter.next();
							JSONObject outwardTrips = tripTicketDataJSArray.getJSONObject(outwardInwardStr);
							JSONArray tripCodes = outwardTrips.names();
							for(int i=0; i<tripCodes.length(); i++)
							{
								String tripCode = tripCodes.getString(i);
								JSONObject tripEntry = outwardTrips.getJSONObject(tripCode);
								Integer adultPassenger = 0;
								Integer childPassenger = 0;
								Integer seniorPassenger = 0;
								Integer disabledPassenger = 0;
								JSONObject seatsAlloted = tripEntry.getJSONObject("seatsAlloted");
								
								hql = "Select tp from VehicleSchedule tp where tp.scheduleStationCode.scheduleStationCode = '"+tripCode+"' " +
										"AND tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL";
								VehicleSchedule vs = (VehicleSchedule)swpService.getUniqueRecordByHQL(hql);
								
								Double adultTicketPrice = 0.00;
								Double childTicketPrice = 0.00;
								Double seniorTicketPrice = 0.00;
								Double disabledTicketPrice = 0.00;
								Double baseAdultFare = 0.00;
								Double baseChildFare = 0.00;
								Double baseSeniorFare = 0.00;
								Double baseDisabledFare = 0.00;
								
								hql = "Select tp.purchasedTripId from PurchasedTrip tp where " +
										//"tp.vehicleSchedule.line.lineId = '" + vs.getLine().getLineId() + "' AND " +
										"tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL";
								//log.info"PurchasedTrip... = " + hql);
								List<Long> transactionsByLine = (List<Long>)swpService.getAllRecordsByHQL(hql);
								
								String departingStationCode = tripEntry.getString("departingStation");
								String arrivalStationCode = tripEntry.getString("arrivingStation");
								String departureTime = tripEntry.getString("departureTime");
								String arrivalTime = tripEntry.getString("arrivalTime");
								Date departureTimeDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(departureTime);
								Date arrivalTimeDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(arrivalTime);
								
								String daSql = "Select tp from Station tp where tp.stationCode = '"+departingStationCode+"' AND " +
										"tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL";
								Station departureStation = (Station)swpService.getUniqueRecordByHQL(daSql);
	
								daSql = "Select tp from Station tp where tp.stationCode = '"+arrivalStationCode+"' AND " +
											"tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL";
								Station arrivalStation = (Station)swpService.getUniqueRecordByHQL(daSql);
								
								daSql = "Select tp from TripZoneStation tp where tp.station.stationCode = '"+arrivalStationCode+"' AND " +
										"tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL";
								TripZoneStation arrivalTripZoneStation = (TripZoneStation)swpService.getUniqueRecordByHQL(daSql);
																
								daSql = "Select tp from TripZoneStation tp where tp.station.stationCode = '"+departingStationCode+"' AND " +
										"tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL";
								TripZoneStation depatureTripZoneStation = (TripZoneStation)swpService.getUniqueRecordByHQL(daSql);
								

								//log.info"8 .... >>>>>" + arrivalTripZoneStation.getTripZone().getRouteOrder());
								//log.info"8 .... >>>>>" + depatureTripZoneStation.getTripZone().getRouteOrder());
								
								Integer tripRouteOrderdifference =  depatureTripZoneStation.getTripZone().getRouteOrder() - arrivalTripZoneStation.getTripZone().getRouteOrder();
								tripRouteOrderdifference = Math.abs(tripRouteOrderdifference);
								tripRouteOrderdifference = tripRouteOrderdifference + 1;
								
								daSql = "Select tp from VehicleTripPrice tp where tp.finalDestinationTripZone.routeOrder = " + tripRouteOrderdifference +  
										" AND tp.deletedAt is NULL and tp.client.clientCode = '"+clientCode+"' " +
										" AND lower(tp.vehicleSeatClass.vehicleSeatClassCode) = '"+generalTripClass.toLowerCase() +"'";
								//log.info"daSQL ===> " + daSql);
								Collection<VehicleTripPrice> vehicleTripPrices = (Collection<VehicleTripPrice>)swpService.getAllRecordsByHQL(daSql);
								//log.info"v size ---> " + vehicleTripPrices.size());
								
								messageResponse = ticketTransactionRef +"###"+transactionRef+"###"+deviceCode+"###"+tripCode +"###"+client.getClientCode()+
										"###" + departingStationCode + "###" + arrivalStationCode + "###" + departureStation.getStationName() + 
										"###" + arrivalStation.getStationName() + "###" + departureTime + "###" + arrivalTime;
								
								
								if(vehicleTripPrices.size()==0)
								{
									jsonObject.add("status", ERROR.NO_TRIPS_FOUND_FOR_SPECIFIED_DATES);
									jsonObject.add("message", "No pricing Found For the Specified trip");
									JsonObject jsonObj = jsonObject.build();
						            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
								}
								
								Iterator<VehicleTripPrice> itVTP = vehicleTripPrices.iterator();
								while(itVTP.hasNext())
								{
									VehicleTripPrice vtp = itVTP.next();
									if(vtp.getPriceType().equals(PriceType.FOR_ADULT_PRICE))
									{
										baseAdultFare = vtp.getAmount();
									}
									else if(vtp.getPriceType().equals(PriceType.FOR_CHILD_PRICE))
									{
										baseChildFare = vtp.getAmount();
									}
									else if(vtp.getPriceType().equals(PriceType.FOR_SENIOR_CITIZENS))
									{
										baseSeniorFare = vtp.getAmount();
									}
									else if(vtp.getPriceType().equals(PriceType.FOR_SPECIALLY_ABLED))
									{
										baseDisabledFare = vtp.getAmount();
									}
								}
								
								adultPassenger = (tripEntry.has("adultPassenger") ? tripEntry.getInt("adultPassenger") : 0);
								childPassenger = (tripEntry.has("childPassenger") ? tripEntry.getInt("childPassenger") : 0);
								seniorPassenger = (tripEntry.has("seniorPassenger") ? tripEntry.getInt("seniorPassenger") : 0);
								disabledPassenger = (tripEntry.has("disabledPassenger") ? tripEntry.getInt("disabledPassenger") : 0);
								JSONObject passengerdetails = tripEntry.getJSONObject("passengerdetails");
								leadPassenger = passengerdetails.getJSONObject("leadPassenger");
								otherPassengers = passengerdetails.has("otherPassengers") ? passengerdetails.getJSONArray("otherPassengers") : null;
								
								
								adultTicketPrice = (adultPassenger*baseAdultFare);
								childTicketPrice = (childPassenger*baseChildFare);
								seniorTicketPrice = (seniorPassenger*baseSeniorFare);
								disabledTicketPrice = (disabledPassenger*baseDisabledFare);
								//log.info"adultPrice === " + adultTicketPrice);
								//log.info"childPrice === " + childTicketPrice);
								
								
								PurchasedTrip purchasedTrip = new PurchasedTrip();
								purchasedTrip.setVehicleSchedule(vs);
								String receiptNo = UtilityHelper.padNumbers(9, (transactionsByLine.size()+1)+"");
								receiptNos.put(receiptNo);
								purchasedTrip.setReceiptNo(receiptNo);
								purchasedTrip.setCreatedAt(new Date());
								purchasedTrip.setUpdatedAt(new Date());
								purchasedTrip.setAmountPayable(adultTicketPrice + childTicketPrice + seniorTicketPrice + disabledTicketPrice/* + client.getBookingFee()*/);
								purchasedTrip.setBookingFee(client.getBookingFee());
								purchasedTrip.setPurchasedTripStatus(PurchasedTripStatus.PENDING);
								purchasedTrip.setTicketCollectionPoint(tcp);
								purchasedTrip.setTransaction(transaction);
								purchasedTrip.setClient(client);
								purchasedTrip.setPassengerCount(adultPassenger + childPassenger + seniorPassenger + disabledPassenger);
								purchasedTrip.setMessageResponse(messageResponse);
								purchasedTrip.setDepartureStation(departureStation);
								purchasedTrip.setArrivalStation(arrivalStation);
								purchasedTrip.setDepartureTime(departureTimeDate);
								purchasedTrip.setArrivalTime(arrivalTimeDate);
								purchasedTrip.setAdultPassengers(adultPassenger);
								purchasedTrip.setChildPassengers(childPassenger);
								purchasedTrip.setSeniorPassengers(seniorPassenger);
								purchasedTrip.setDisabledPassengers(disabledPassenger);
								purchasedTrip.setPurchasePoint(pp);
								purchasedTrip.setTotalUpgradedAdultPassengers(0);
								purchasedTrip.setTotalUpgradedChildPassengers(0);
								purchasedTrip.setTotalUpgradedDisabledPassengers(0);
								purchasedTrip.setTotalUpgradedSeniorPassengers(0);
								purchasedTrip.setTotalUpgradedAmount(0.00);
								purchasedTrip = (PurchasedTrip)(this.swpService.createNewRecord(purchasedTrip));
								
								Customer leadPassengerCustomer = null;
								JSONArray customerListCreated = new JSONArray();
								if(leadPassenger!=null)
								{
									String firstname = leadPassenger.getString("firstname").trim();
									String lastname = leadPassenger.getString("lastname").trim();
									String emailaddress = leadPassenger.getString("emailaddress").trim();
									String mobilenumber = leadPassenger.getString("mobilenumber").trim();
									String nationalid = leadPassenger.getString("nationalid").trim();
									
									if(firstname.length()>0 && lastname.length()>0 && emailaddress.length()>0 && mobilenumber.length()>0)
									{
										leadPassengerCustomer = new Customer();
										leadPassengerCustomer.setClient(client);
										leadPassengerCustomer.setPurchasedTrip(purchasedTrip);
										leadPassengerCustomer.setCreatedAt(new Date());
										leadPassengerCustomer.setUpdatedAt(new Date());
										leadPassengerCustomer.setCustomerStatus(CustomerStatus.ACTIVE);
										leadPassengerCustomer.setEmailAddress(emailaddress);
										leadPassengerCustomer.setFirstName(firstname);
										leadPassengerCustomer.setLastName(lastname);
										leadPassengerCustomer.setMeansOfId(nationalid);
										leadPassengerCustomer.setIsLeadPassenger(Boolean.TRUE);
										leadPassengerCustomer.setMobileNumber(mobilenumber);
										leadPassengerCustomer.setVerificationNumber(RandomStringUtils.randomNumeric(12));
										leadPassengerCustomer = (Customer)swpService.createNewRecord(leadPassengerCustomer);
										customerListCreated.put(leadPassengerCustomer);
									}
									
									if(otherPassengers!=null)
									{
										for(int j=0; j<otherPassengers.length(); j++)
										{
											JSONObject otherPassenger = otherPassengers.getJSONObject(j);
											firstname = otherPassenger.getString("firstname").trim();
											lastname = otherPassenger.getString("lastname").trim();
											emailaddress = otherPassenger.getString("emailaddress").trim();
											mobilenumber = otherPassenger.getString("mobilenumber").trim();
											nationalid = otherPassenger.getString("nationalid").trim();
											
											if(firstname.length()>0 && lastname.length()>0 && emailaddress.length()>0 && mobilenumber.length()>0)
											{
												Customer otherPassengerCustomer = new Customer();
												otherPassengerCustomer.setClient(client);
												otherPassengerCustomer.setPurchasedTrip(purchasedTrip);
												otherPassengerCustomer.setCreatedAt(new Date());
												otherPassengerCustomer.setUpdatedAt(new Date());
												otherPassengerCustomer.setCustomerStatus(CustomerStatus.ACTIVE);
												otherPassengerCustomer.setEmailAddress(emailaddress);
												otherPassengerCustomer.setFirstName(firstname);
												otherPassengerCustomer.setLastName(lastname);
												otherPassengerCustomer.setMeansOfId(nationalid);
												otherPassengerCustomer.setMobileNumber(mobilenumber);
												otherPassengerCustomer.setIsLeadPassenger(Boolean.FALSE);
												otherPassengerCustomer.setVerificationNumber(RandomStringUtils.randomNumeric(12));
												otherPassengerCustomer = (Customer)swpService.createNewRecord(otherPassengerCustomer);
												customerListCreated.put(otherPassengerCustomer);
											}
										}
										
									}
									
									seatIds = new ArrayList<Long>();
									seatsAlloted = tripEntry.getJSONObject("seatsAlloted");
									if(seatsAlloted==null)
									{
										skip = Boolean.TRUE;
									}
									else
									{
										Iterator<String> seatsAllotedIter = seatsAlloted.keys();
										//log.info"numberOfSeats = " + seatsAlloted.toString());
										while(seatsAllotedIter.hasNext())
										{
											String seatsAllotedIterStr = seatsAllotedIter.next();
											JSONArray seatsAllotedArray = seatsAlloted.getJSONArray(seatsAllotedIterStr);
											//log.info"seatsAllotedArray Str = " + seatsAllotedArray.toString());
											for(int j=0; j<seatsAllotedArray.length(); j++)
											{
												JSONObject jsSeatAlloted = (seatsAllotedArray.getJSONObject(j));
												//log.info"jsSeatAlloted = " + jsSeatAlloted.toString());
												seatIds.add(jsSeatAlloted.getLong("seatAvailabilityId"));
											}
										}
									}
									
									hql = "Select tp from ScheduleStationSeatAvailability tp where tp.schedSeatAvailId IN ("+StringUtils.join(seatIds, ", ")+") " +
											"AND tp.deletedAt is NULL AND tp.vehicleSeat.client.clientCode = '"+clientCode+"' AND tp.lockedDownBy = '"+purchaseToken+"' AND " +
											"(tp.lockedDownExpiryDate > CURRENT_TIMESTAMP) AND tp.boughtByCustomer IS NULL AND tp.seatAvailabilityStatus = " + SeatAvailabilityStatus.OPEN.ordinal();
											//+ " " +
											//"AND tp.scheduleStationSeatSection.vehicleTripRouting.vehicleTrip.vehicleTripCode = '"+tripCode+"' " +
											//"ORDER BY tp.vehicleTripRouteSeatSection.vehicleTripRouting.routeOrder";
									vsaList = (Collection<ScheduleStationSeatAvailability>)swpService.getAllRecordsByHQL(hql);
									Iterator<ScheduleStationSeatAvailability> vsaListIter = vsaList.iterator();
									int passengerCounter = 0;
									//log.info"hql --- " + hql);
									//log.info"vsalistier size -- " + vsaList.size());
									//log.info"seatIds size -- " + seatIds.size());
									JSONArray vehicleSeatsLockedDown = new JSONArray();
									while(vsaListIter.hasNext())
									{
										ScheduleStationSeatAvailability vsa = vsaListIter.next();
										vsa.setBoughtByCustomer(leadPassengerCustomer);
										vsa.setSeatAvailabilityStatus(SeatAvailabilityStatus.PURCHASED);
										vsa.setUpdatedAt(new Date());
										swpService.updateRecord(vsa);
										
										String channel = "";
										String cardPan = "";
										Long ticketCollectionPointId = null;
										Integer childSeatCount = null;
										Integer adultSeatCount = null;
										
										
										
										
										if(vsa.getPassengerType().equals(PassengerType.ADULT_PASSENGER))
										{
											PurchasedTripSeat pts = new PurchasedTripSeat();
											pts.setCreatedAt(new Date());
											pts.setPurchasedTrip(purchasedTrip);
											pts.setUpdatedAt(new Date());
											pts.setVehicleSeat(vsa.getVehicleSeat());
											pts.setScheduleStationSeatAvailability(vsa);
											pts.setClient(client);
											pts.setPassengerType(PassengerType.ADULT_PASSENGER);
											pts = (PurchasedTripSeat)(this.swpService.createNewRecord(pts));
										}
										else if(vsa.getPassengerType().equals(PassengerType.CHILD_PASSENGER))
										{
											PurchasedTripSeat pts = new PurchasedTripSeat();
											pts.setCreatedAt(new Date());
											pts.setPurchasedTrip(purchasedTrip);
											pts.setUpdatedAt(new Date());
											pts.setVehicleSeat(vsa.getVehicleSeat());
											pts.setScheduleStationSeatAvailability(vsa);
											pts.setClient(client);
											pts.setPassengerType(PassengerType.CHILD_PASSENGER);
											pts = (PurchasedTripSeat)(this.swpService.createNewRecord(pts));
										}
										else if(vsa.getPassengerType().equals(PassengerType.SENIOR_PASSENGER))
										{
											PurchasedTripSeat pts = new PurchasedTripSeat();
											pts.setCreatedAt(new Date());
											pts.setPurchasedTrip(purchasedTrip);
											pts.setUpdatedAt(new Date());
											pts.setVehicleSeat(vsa.getVehicleSeat());
											pts.setScheduleStationSeatAvailability(vsa);
											pts.setClient(client);
											pts.setPassengerType(PassengerType.SENIOR_PASSENGER);
											pts = (PurchasedTripSeat)(this.swpService.createNewRecord(pts));
										}
										else if(vsa.getPassengerType().equals(PassengerType.SPECIAL_NEEDS_PASSENGER))
										{
											PurchasedTripSeat pts = new PurchasedTripSeat();
											pts.setCreatedAt(new Date());
											pts.setPurchasedTrip(purchasedTrip);
											pts.setUpdatedAt(new Date());
											pts.setVehicleSeat(vsa.getVehicleSeat());
											pts.setScheduleStationSeatAvailability(vsa);
											pts.setClient(client);
											pts.setPassengerType(PassengerType.SPECIAL_NEEDS_PASSENGER);
											pts = (PurchasedTripSeat)(this.swpService.createNewRecord(pts));
										}
										
										passengerCounter++;
												
										JSONObject js = new JSONObject();
										js.put("seatAvailabilityId", vsa.getSchedSeatAvailId());
										js.put("seatNumber", vsa.getVehicleSeat().getSeatNumber());
										js.put("seatName", vsa.getVehicleSeat().getSeatNumber());
										js.put("expirationPeriodMinutes", client.getLockDownInterval());
										js.put("seatClass", vsa.getVehicleSeat().getVehicleSeatSection().getSectionName());
										js.put("seatLocation", vsa.getVehicleSeat().getVehicleSeatLocation().name());
										js.put("seatFacingOtherSeats", vsa.getVehicleSeat().getTripSeatFacing());
										js.put("expirationDate", new SimpleDateFormat("yyyy-MM-dd HH:mm").format(vsa.getLockedDownExpiryDate()));
										vehicleSeatsLockedDown.put(js);
									}
									JSONObject tempHolder = new JSONObject();
									tempHolder.put("allSeatsAlloted", vehicleSeatsLockedDown);
									allTripSeatDetails.put(tripCode, tempHolder);
								}
							}
						}
						

						if(coupon!=null)
						{
							coupon.setCouponStatus(CouponStatus.USED);
							swpService.updateRecord(coupon);
						}
						
						jsonObject.add("seatsAlloted", allTripSeatDetails.toString());
						jsonObject.add("transactionRef", transaction.getOrderRef());
						jsonObject.add("receiptNos", receiptNos.toString());
						//jsonObject.add("tripDetails", tripSummary.toString());
						jsonObject.add("status", ERROR.GENERAL_SUCCESS);
						jsonObject.add("message", "Trip purchase was successful");
						JsonObject jsonObj = jsonObject.build();
			            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
					
					}
					else
					{
						//Transaction was not successful
						jsonObject.add("message", "Transaction Failed. Transaction was not successful");
						jsonObject.add("status", ERROR.TICKET_PURCHASE_FAILED);
						JsonObject jsonObj = jsonObject.build();
			            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
					}
				}
			}
			else if(paymentType.equals(PaymentMeans.CARD.name()))
			{
				RoleType roleCode = null;
				Boolean transactionVerification = false;
				JSONObject purchaseDetailsJS = new JSONObject(purchaseDetails);
				tripTicketDataJSArray = purchaseDetailsJS.getJSONObject("tripTicketData");
				String hash = purchaseDetailsJS.has("hash") ? purchaseDetailsJS.getString("hash") : null;
				String tripCardNumber = purchaseDetailsJS.has("tripCardNumber") ? purchaseDetailsJS.getString("tripCardNumber") : null; 
				
				
				if(tripTicketDataJSArray==null || deviceCode==null || hash==null)
				{
					jsonObject.add("status", ERROR.INCOMPLETE_PARAMETERS);
					jsonObject.add("message", "Incomplete parameters provided. Ensure you provide terminalApiKey, deviceCode, transactionRef, and hash parameters in your tripTicketData parameter");
					JsonObject jsonObj = jsonObject.build();
		            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
				}
				JSONObject jsObj = new JSONObject();
				

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
				
				
				
				messageRequest = hash+"###"+deviceCode+"###"+transactionRef+"###"+token+"###"+"###"+ticketCollectionCenterCode
						+"###"+ipAddress+"###"+clientCode;
				
				User user = null;
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
					//log.inforequestId + "username ==" + (username==null ? "" : username));
					String roleCode_ = verifyJ.has("roleCode") ? verifyJ.getString("roleCode") : null;
					//log.inforequestId + "roleCode_ ==" + (roleCode_==null ? "" : roleCode_));
					//User user = null;
					//RoleType roleCode = null;
					
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
				
				
				TripCard tripCard = null;
				hql = "Select tp from TripCard tp where tp.tripCardNumber = '"+tripCardNumber+"' " +
						"AND tp.subsciptionExpiryDate > CURRENT_TIMESTAMP AND tp.cardStatus = " + CardStatus.ACTIVE.ordinal() + 
						" AND tp.customer.user.id = " + user.getUserId();
				tripCard = (TripCard)swpService.getUniqueRecordByHQL(hql);
				if(tripCard==null)
				{
					jsonObject.add("status", ERROR.INVALID_CARD);
					jsonObject.add("message", "Ensure the card is still valid and belongs to you");
					JsonObject jsonObj = jsonObject.build();
		            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
				}
				
				if(device.getDeviceType().equals(DeviceType.WEB) || device.getDeviceType().equals(DeviceType.MOBILE))
				{
					if(user==null)
					{
						jsonObject.add("status", ERROR.NO_USER_SESSION_FOUND);
						jsonObject.add("message", "Can not process this type of payment. No User session to process this transaction");
						JsonObject jsonObj = jsonObject.build();
			            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
					}
				}
				
				TicketCollectionPoint tcp = null;
				if(ticketCollectionCenterCode!=null)
				{
					hql = "Select tp from TicketCollectionPoint tp where tp.collectionPointCode = '"+ticketCollectionCenterCode+"' AND tp.deletedAt IS NULL " +
							"AND tp.client.clientCode = '"+clientCode+"'";
					tcp = (TicketCollectionPoint)swpService.getUniqueRecordByHQL(hql);
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



				
				/*CHECK PRICE OF TRIP*/
				JSONObject allTripSeatDetails = new JSONObject();
				List<Long> seatIds = new ArrayList<Long>();
				Iterator<String> outwardInwardIter = tripTicketDataJSArray.keys();
				while(outwardInwardIter.hasNext())
				{
					String outwardInwardStr = outwardInwardIter.next();
					JSONObject outwardTrips = tripTicketDataJSArray.getJSONObject(outwardInwardStr);
					JSONArray tripCodes = outwardTrips.names();
					for(int i=0; i<tripCodes.length(); i++)
					{
						String tripCode = tripCodes.getString(i);
						JSONObject tripEntry = outwardTrips.getJSONObject(tripCode);
						Integer adultPassenger = 0;
						Integer childPassenger = 0;
						Integer seniorPassenger = 0;
						Integer disabledPassenger = 0;
						JSONObject seatsAlloted = tripEntry.getJSONObject("seatsAlloted");
						if(seatsAlloted==null)
						{
							skip = Boolean.TRUE;
						}
						else
						{
							Iterator<String> seatsAllotedIter = seatsAlloted.keys();
							while(seatsAllotedIter.hasNext())
							{
								String seatsAllotedIterStr = seatsAllotedIter.next();
								JSONArray seatsAllotedArray = seatsAlloted.getJSONArray(seatsAllotedIterStr);
								//log.info"seatsAllotedArray Str = " + seatsAllotedArray.toString());
								for(int j=0; j<seatsAllotedArray.length(); j++)
								{
									JSONObject jsSeatAlloted = (seatsAllotedArray.getJSONObject(j));
									//log.info"jsSeatAlloted = " + jsSeatAlloted.toString());
									seatIds.add(jsSeatAlloted.getLong("seatAvailabilityId"));
								}
							}
							
						}
					}
				}
				
				//log.info"...join == " + StringUtils.join(seatIds, ", "));
				
				Double adultTicketPrice = 0.00;
				Double childTicketPrice = 0.00;
				Double seniorTicketPrice = 0.00;
				Double disabledTicketPrice = 0.00;
				Double totalPrice = 0.00;
				Integer totalPassengers = 0;
				if(skip.equals(Boolean.TRUE))
				{
					jsonObject.add("status", ERROR.INCOMPLETE_PARAMETERS_SEATS_NOT_ALLOTTED_COMPLETELY);
					jsonObject.add("message", "Seats purchased not specified completely");
					JsonObject jsonObj = jsonObject.build();
		            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
				}
				else
				{
					hql = "Select tp from ScheduleStationSeatAvailability tp where tp.schedSeatAvailId IN ("+StringUtils.join(seatIds, ", ")+") " +
							"AND tp.deletedAt is NULL AND tp.vehicleSeat.client.clientCode = '"+clientCode+"' AND tp.lockedDownBy = '"+purchaseToken+"' AND " +
							"(tp.lockedDownExpiryDate > CURRENT_TIMESTAMP) AND tp.seatAvailabilityStatus = "+ SeatAvailabilityStatus.OPEN.ordinal() +"";
					//log.info"....HQL = " + hql);
					Collection<ScheduleStationSeatAvailability> vsaList = (Collection<ScheduleStationSeatAvailability>)swpService.getAllRecordsByHQL(hql);
					//log.info"vsaList.size()<seatIds.size() ==" + vsaList.size() + " -- " + seatIds.size());
					if(vsaList!=null && vsaList.size()<seatIds.size())
					{
						jsonObject.add("status", ERROR.INCOMPLETE_SEATS_AVAILABLE_PURCHASE_TIME_EXPIRED);
						jsonObject.add("message", "Time expired for purchasing the seats alloted to you");
						JsonObject jsonObj = jsonObject.build();
			            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
					}
					
					
					String serviceType = ServiceType.EPAYMENT_FOR_TRIP.name();
					JSONArray tripSummary = new JSONArray();
					
					JSONObject jsonTrips = new JSONObject();
					JSONObject leadPassenger = null;
					JSONArray otherPassengers = null;
					
					
					outwardInwardIter = tripTicketDataJSArray.keys();
					while(outwardInwardIter.hasNext())
					{
						String outwardInwardStr = outwardInwardIter.next();
						JSONObject outwardTrips = tripTicketDataJSArray.getJSONObject(outwardInwardStr);
						JSONArray tripCodes = outwardTrips.names();
						for(int i=0; i<tripCodes.length(); i++)
						{
							String tripCode = tripCodes.getString(i);
							JSONObject tripEntry = outwardTrips.getJSONObject(tripCode);
							Integer adultPassenger = 0;
							Integer childPassenger = 0;
							Integer seniorPassenger = 0;
							Integer disabledPassenger = 0;
							JSONObject seatsAlloted = tripEntry.getJSONObject("seatsAlloted");
							
							hql = "Select tp from VehicleSchedule tp where tp.scheduleStationCode.scheduleStationCode = '"+tripCode+"' " +
									"AND tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL";
							VehicleSchedule vs = (VehicleSchedule)swpService.getUniqueRecordByHQL(hql);
							
							adultTicketPrice = 0.00;
							childTicketPrice = 0.00;
							seniorTicketPrice = 0.00;
							disabledTicketPrice = 0.00;
							Double baseAdultFare = 0.00;
							Double baseChildFare = 0.00;
							Double baseSeniorFare = 0.00;
							Double baseDisabledFare = 0.00;
							
							hql = "Select tp.purchasedTripId from PurchasedTrip tp where " +
									//"tp.vehicleSchedule.line.lineId = '" + vs.getLine().getLineId() + "' AND " +
									"tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL";
							//log.info"PurchasedTrip... = " + hql);
							List<Long> transactionsByLine = (List<Long>)swpService.getAllRecordsByHQL(hql);
							
							String departingStationCode = tripEntry.getString("departingStation");
							String arrivalStationCode = tripEntry.getString("arrivingStation");
							String departureTime = tripEntry.getString("departureTime");
							String arrivalTime = tripEntry.getString("arrivalTime");
							Date departureTimeDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(departureTime);
							Date arrivalTimeDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(arrivalTime);
							
							String daSql = "Select tp from Station tp where tp.stationCode = '"+departingStationCode+"' AND " +
									"tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL";
							Station departureStation = (Station)swpService.getUniqueRecordByHQL(daSql);

							daSql = "Select tp from Station tp where tp.stationCode = '"+arrivalStationCode+"' AND " +
										"tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL";
							Station arrivalStation = (Station)swpService.getUniqueRecordByHQL(daSql);
							
							daSql = "Select tp from TripZoneStation tp where tp.station.stationCode = '"+arrivalStationCode+"' AND " +
									"tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL";
							TripZoneStation arrivalTripZoneStation = (TripZoneStation)swpService.getUniqueRecordByHQL(daSql);
															
							daSql = "Select tp from TripZoneStation tp where tp.station.stationCode = '"+departingStationCode+"' AND " +
									"tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL";
							TripZoneStation depatureTripZoneStation = (TripZoneStation)swpService.getUniqueRecordByHQL(daSql);
							

							//log.info"9 .... >>>>>" + arrivalTripZoneStation.getTripZone().getRouteOrder());
							//log.info"9 .... >>>>>" + depatureTripZoneStation.getTripZone().getRouteOrder());
							
							Integer tripRouteOrderdifference =  depatureTripZoneStation.getTripZone().getRouteOrder() - arrivalTripZoneStation.getTripZone().getRouteOrder();
							tripRouteOrderdifference = Math.abs(tripRouteOrderdifference);
							tripRouteOrderdifference = tripRouteOrderdifference + 1;
							
							daSql = "Select tp from VehicleTripPrice tp where tp.finalDestinationTripZone.routeOrder = " + tripRouteOrderdifference +  
									" AND tp.deletedAt is NULL and tp.client.clientCode = '"+clientCode+"' " +
									" AND lower(tp.vehicleSeatClass.vehicleSeatClassCode) = '"+generalTripClass.toLowerCase() +"'";
							//log.info"daSQL ===> " + daSql);
							Collection<VehicleTripPrice> vehicleTripPrices = (Collection<VehicleTripPrice>)swpService.getAllRecordsByHQL(daSql);
							//log.info"v size ---> " + vehicleTripPrices.size());
							
							if(vehicleTripPrices.size()==0)
							{
								jsonObject.add("status", ERROR.NO_TRIPS_FOUND_FOR_SPECIFIED_DATES);
								jsonObject.add("message", "No pricing Found For the Specified trip");
								JsonObject jsonObj = jsonObject.build();
								return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
							}
							
							Iterator<VehicleTripPrice> itVTP = vehicleTripPrices.iterator();
							while(itVTP.hasNext())
							{
								VehicleTripPrice vtp = itVTP.next();
								if(vtp.getPriceType().equals(PriceType.FOR_ADULT_PRICE))
								{
									baseAdultFare = vtp.getAmount();
								}
								else if(vtp.getPriceType().equals(PriceType.FOR_CHILD_PRICE))
								{
									baseChildFare = vtp.getAmount();
								}
								else if(vtp.getPriceType().equals(PriceType.FOR_SENIOR_CITIZENS))
								{
									baseSeniorFare = vtp.getAmount();
								}
								else if(vtp.getPriceType().equals(PriceType.FOR_SPECIALLY_ABLED))
								{
									baseDisabledFare = vtp.getAmount();
								}
							}
							
							adultPassenger = (tripEntry.has("adultPassenger") ? tripEntry.getInt("adultPassenger") : 0);
							childPassenger = (tripEntry.has("childPassenger") ? tripEntry.getInt("childPassenger") : 0);
							seniorPassenger = (tripEntry.has("seniorPassenger") ? tripEntry.getInt("seniorPassenger") : 0);
							disabledPassenger = (tripEntry.has("disabledPassenger") ? tripEntry.getInt("disabledPassenger") : 0);
							JSONObject passengerdetails = tripEntry.getJSONObject("passengerdetails");
							leadPassenger = passengerdetails.getJSONObject("leadPassenger");
							otherPassengers = passengerdetails.has("otherPassengers") ? passengerdetails.getJSONArray("otherPassengers") : null;
							
							totalPassengers = totalPassengers + adultPassenger + childPassenger + seniorPassenger + disabledPassenger;
							adultTicketPrice = (adultPassenger*baseAdultFare);
							childTicketPrice = (childPassenger*baseChildFare);
							seniorTicketPrice = (seniorPassenger*baseSeniorFare);
							disabledTicketPrice = (disabledPassenger*baseDisabledFare);
							//log.info"adultPrice === " + adultTicketPrice);
							//log.info"childPrice === " + childTicketPrice);
							totalPrice = totalPrice + adultTicketPrice + childTicketPrice + seniorTicketPrice + seniorTicketPrice + client.getBookingFee();
						}
					}
					
				}
				
				
				if(tripCard.getCardScheme().getTripCardChargeMode().equals(TripCardChargeMode.PER_AMOUNT))
				{
					totalPrice = totalPrice - (totalPrice*tripCard.getCardScheme().getDiscountRate()/100);
				}
				
				Coupon coupon = null;
				if(couponCode!=null)
				{
					hql = "Select tp from Coupon tp where lower(tp.couponCode) = '"+couponCode.toLowerCase()+"' AND tp.deletedAt IS NULL AND tp.client.clientId = " + client.getClientId();
					coupon = (Coupon)swpService.getUniqueRecordByHQL(hql);
					if(coupon!=null)
					{
						totalPrice = totalPrice - (totalPrice*coupon.getDiscountRate()/100); 
					}
				}
				if(user!=null && (roleCode.equals(RoleType.OPERATOR) || roleCode.equals(RoleType.ADMIN_STAFF)))
				{
					if(concessionAmount!=null)
					{
						totalPrice = totalPrice - concessionAmount;
					}
				}
				
				if(tripCard.getCardScheme().getTripCardChargeMode().equals(TripCardChargeMode.PER_TRIP))
				{
					totalPrice = 0.00;
				}
				
				
				//totalPrice = totalPrice;
				transactionVerification = UtilityHelper.validateTransactionHash(hash, deviceCode, clientCode, transactionRef, totalPrice, device.getTerminalApiKey());
				//log.info"transactionVerification == " + transactionVerification);
				
				if(tripCard.getCardScheme().getTripCardChargeMode().equals(TripCardChargeMode.PER_TRIP))
				{
					if(tripCard.getSubsciptionExpiryDate().before(new Date()))
					{
						jsonObject.add("status", ERROR.TRIP_CARD_EXPIRED);
						jsonObject.add("message", "Trip card has expired. You can not use this card");
						JsonObject jsonObj = jsonObject.build();
			            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
					}
					if(tripCard.getCurrentAvailableTrips()<totalPassengers)
					{
						jsonObject.add("status", ERROR.INSUFFICIENT_FUNDS);
						jsonObject.add("message", "Insufficient available in your trip card/pass. Fund your trip card/pass to use");
						JsonObject jsonObj = jsonObject.build();
			            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
					}
				}
				if(tripCard.getCardScheme().getTripCardChargeMode().equals(TripCardChargeMode.PER_AMOUNT))
				{
					if(tripCard.getSubsciptionExpiryDate().before(new Date()))
					{
						jsonObject.add("status", ERROR.TRIP_CARD_EXPIRED);
						jsonObject.add("message", "Trip card has expired. You can not use this card");
						JsonObject jsonObj = jsonObject.build();
			            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
					}
					if(tripCard.getCurrentBalance()<totalPrice)
					{
						jsonObject.add("status", ERROR.INSUFFICIENT_FUNDS);
						jsonObject.add("message", "Insufficient funds in your trip card. Fund your trip card to use");
						JsonObject jsonObj = jsonObject.build();
			            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
					}
				}
				
					
				
				
				
				
				/*DEBIT CARD*/
				allTripSeatDetails = new JSONObject();
				seatIds = new ArrayList<Long>();
				outwardInwardIter = tripTicketDataJSArray.keys();
				while(outwardInwardIter.hasNext())
				{
					String outwardInwardStr = outwardInwardIter.next();
					JSONObject outwardTrips = tripTicketDataJSArray.getJSONObject(outwardInwardStr);
					JSONArray tripCodes = outwardTrips.names();
					for(int i=0; i<tripCodes.length(); i++)
					{
						String tripCode = tripCodes.getString(i);
						JSONObject tripEntry = outwardTrips.getJSONObject(tripCode);
						Integer adultPassenger = 0;
						Integer childPassenger = 0;
						Integer seniorPassenger = 0;
						Integer disabledPassenger = 0;
						JSONObject seatsAlloted = tripEntry.getJSONObject("seatsAlloted");
						if(seatsAlloted==null)
						{
							skip = Boolean.TRUE;
						}
						else
						{
							Iterator<String> seatsAllotedIter = seatsAlloted.keys();
							while(seatsAllotedIter.hasNext())
							{
								String seatsAllotedIterStr = seatsAllotedIter.next();
								JSONArray seatsAllotedArray = seatsAlloted.getJSONArray(seatsAllotedIterStr);
								//log.info"seatsAllotedArray Str = " + seatsAllotedArray.toString());
								for(int j=0; j<seatsAllotedArray.length(); j++)
								{
									JSONObject jsSeatAlloted = (seatsAllotedArray.getJSONObject(j));
									//log.info"jsSeatAlloted = " + jsSeatAlloted.toString());
									seatIds.add(jsSeatAlloted.getLong("seatAvailabilityId"));
								}
							}
							
						}
						adultPassenger = (tripEntry.has("adultPassenger") ? tripEntry.getInt("adultPassenger") : 0);
						childPassenger = (tripEntry.has("childPassenger") ? tripEntry.getInt("childPassenger") : 0);
						seniorPassenger = (tripEntry.has("seniorPassenger") ? tripEntry.getInt("seniorPassenger") : 0);
						disabledPassenger = (tripEntry.has("disabledPassenger") ? tripEntry.getInt("disabledPassenger") : 0);
						JSONObject passengerdetails = tripEntry.getJSONObject("passengerdetails");
						JSONObject leadPassenger = passengerdetails.getJSONObject("leadPassenger");
						JSONArray otherPassengers = passengerdetails.has("otherPassengers") ? passengerdetails.getJSONArray("otherPassengers") : null;
					}
				}
				
				//log.info"...join == " + StringUtils.join(seatIds, ", "));
				
				if(skip.equals(Boolean.TRUE))
				{
					jsonObject.add("status", ERROR.INCOMPLETE_PARAMETERS_SEATS_NOT_ALLOTTED_COMPLETELY);
					jsonObject.add("message", "Seats purchased not specified completely");
					JsonObject jsonObj = jsonObject.build();
		            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
				}
				else
				{
					hql = "Select tp from ScheduleStationSeatAvailability tp where tp.schedSeatAvailId IN ("+StringUtils.join(seatIds, ", ")+") " +
							"AND tp.deletedAt is NULL AND tp.vehicleSeat.client.clientCode = '"+clientCode+"' AND tp.lockedDownBy = '"+purchaseToken+"' AND " +
							"(tp.lockedDownExpiryDate > CURRENT_TIMESTAMP) AND tp.seatAvailabilityStatus = "+ SeatAvailabilityStatus.OPEN.ordinal() +"";
					//log.info"....HQL = " + hql);
					Collection<ScheduleStationSeatAvailability> vsaList = (Collection<ScheduleStationSeatAvailability>)swpService.getAllRecordsByHQL(hql);
					//log.info"vsaList.size()<seatIds.size() ==" + vsaList.size() + " -- " + seatIds.size());
					if(vsaList!=null && vsaList.size()<seatIds.size())
					{
						jsonObject.add("status", ERROR.INCOMPLETE_SEATS_AVAILABLE_PURCHASE_TIME_EXPIRED);
						jsonObject.add("message", "Time expired for purchasing the seats alloted to you");
						JsonObject jsonObj = jsonObject.build();
			            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
					}
					
					
					//log.info"transactionVerification == " + transactionVerification);
					String serviceType = ServiceType.EPAYMENT_FOR_TRIP.name();
					JSONArray tripSummary = new JSONArray();
					if(transactionVerification.equals(Boolean.TRUE))
					{
						
						String ticketTransactionRef = RandomStringUtils.randomNumeric(8);
						hql = "Select tp from Transaction tp where tp.orderRef = '"+(device.getDeviceCode()+"-"+transactionRef)+"' AND tp.client.clientCode = '"+clientCode+"' AND " +
								"tp.deletedAt IS NULL";
						Transaction transaction = (Transaction)swpService.getUniqueRecordByHQL(hql);
						
						if(transaction!=null)
						{
							jsonObject.add("status", ERROR.TRANSACTION_REF_ALREADY_PAID_PREVIOUSLY);
							jsonObject.add("message", "Transaction Reference you provided belongs to another transaction that has already been paid");
							JsonObject jsonObj = jsonObject.build();
				            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
						}
						
						JSONObject vehicleSeatsLockedDownTrip = new JSONObject();
						
						Double fixedCharge = tripCard.getCardScheme().getFixedCharge();
						Double transactionFee = tripCard.getCardScheme().getTransactionCharge();
						Double transactionAmount = totalPrice;
						
						
						
						
						
						
						
						
							
						transaction = new Transaction();
						transaction.setTransactionRef(ticketTransactionRef);
						transaction.setOrderRef(device.getDeviceCode()+"-"+transactionRef);
						transaction.setChannel(com.probase.reservationticketingwebservice.enumerations.Channel.POS);
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
						transaction.setNarration("Debit|#" + device.getDeviceCode()+"-"+transactionRef + "|ZMW" + transaction.getTransactionAmount());
						transaction.setDrCardPan(null);
						transaction.setDrCardId(null);
						transaction.setUpdatedAt(new Date());
						transaction.setCreatedAt(new Date());
						transaction.setPaymentMeans(PaymentMeans.CARD);
						transaction.setClient(client);						
						transaction.setTransactionCurrency("ZMW");
						transaction.setConcessionApplied(concessionAmount);
						if(coupon!=null)
							transaction.setAppliedCoupon(coupon);
						transaction = (Transaction)swpService.createNewRecord(transaction);
						
						
						JSONObject jsonTrips = new JSONObject();
						JSONObject leadPassenger = null;
						JSONArray otherPassengers = null;
						
						
						outwardInwardIter = tripTicketDataJSArray.keys();
						while(outwardInwardIter.hasNext())
						{
							String outwardInwardStr = outwardInwardIter.next();
							JSONObject outwardTrips = tripTicketDataJSArray.getJSONObject(outwardInwardStr);
							JSONArray tripCodes = outwardTrips.names();
							for(int i=0; i<tripCodes.length(); i++)
							{
								String tripCode = tripCodes.getString(i);
								JSONObject tripEntry = outwardTrips.getJSONObject(tripCode);
								Integer adultPassenger = 0;
								Integer childPassenger = 0;
								Integer seniorPassenger = 0;
								Integer disabledPassenger = 0;
								JSONObject seatsAlloted = tripEntry.getJSONObject("seatsAlloted");
								
								hql = "Select tp from VehicleSchedule tp where tp.scheduleStationCode.scheduleStationCode = '"+tripCode+"' " +
										"AND tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL";
								VehicleSchedule vs = (VehicleSchedule)swpService.getUniqueRecordByHQL(hql);
								
								adultTicketPrice = 0.00;
								childTicketPrice = 0.00;
								seniorTicketPrice = 0.00;
								disabledTicketPrice = 0.00;
								Double baseAdultFare = 0.00;
								Double baseChildFare = 0.00;
								Double baseSeniorFare = 0.00;
								Double baseDisabledFare = 0.00;
								
								hql = "Select tp.purchasedTripId from PurchasedTrip tp where " +
										//"tp.vehicleSchedule.line.lineId = '" + vs.getLine().getLineId() + "' AND " +
										"tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL";
								//log.info"PurchasedTrip... = " + hql);
								List<Long> transactionsByLine = (List<Long>)swpService.getAllRecordsByHQL(hql);
								
								String departingStationCode = tripEntry.getString("departingStation");
								String arrivalStationCode = tripEntry.getString("arrivingStation");
								String departureTime = tripEntry.getString("departureTime");
								String arrivalTime = tripEntry.getString("arrivalTime");
								Date departureTimeDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(departureTime);
								Date arrivalTimeDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(arrivalTime);
								
								String daSql = "Select tp from Station tp where tp.stationCode = '"+departingStationCode+"' AND " +
										"tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL";
								Station departureStation = (Station)swpService.getUniqueRecordByHQL(daSql);
	
								daSql = "Select tp from Station tp where tp.stationCode = '"+arrivalStationCode+"' AND " +
											"tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL";
								Station arrivalStation = (Station)swpService.getUniqueRecordByHQL(daSql);
								
								daSql = "Select tp from TripZoneStation tp where tp.station.stationCode = '"+arrivalStationCode+"' AND " +
										"tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL";
								TripZoneStation arrivalTripZoneStation = (TripZoneStation)swpService.getUniqueRecordByHQL(daSql);
																
								daSql = "Select tp from TripZoneStation tp where tp.station.stationCode = '"+departingStationCode+"' AND " +
										"tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL";
								TripZoneStation depatureTripZoneStation = (TripZoneStation)swpService.getUniqueRecordByHQL(daSql);
								

								//log.info"10 .... >>>>>" + arrivalTripZoneStation.getTripZone().getRouteOrder());
								//log.info"10 .... >>>>>" + depatureTripZoneStation.getTripZone().getRouteOrder());
								
								Integer tripRouteOrderdifference =  depatureTripZoneStation.getTripZone().getRouteOrder() - arrivalTripZoneStation.getTripZone().getRouteOrder();
								tripRouteOrderdifference = Math.abs(tripRouteOrderdifference);
								tripRouteOrderdifference = tripRouteOrderdifference + 1;
								
								daSql = "Select tp from VehicleTripPrice tp where tp.finalDestinationTripZone.routeOrder = " + tripRouteOrderdifference +  
										" AND tp.deletedAt is NULL and tp.client.clientCode = '"+clientCode+"' " +
										" AND lower(tp.vehicleSeatClass.vehicleSeatClassCode) = '"+generalTripClass.toLowerCase() +"'";
								//log.info"daSQL ===> " + daSql);
								Collection<VehicleTripPrice> vehicleTripPrices = (Collection<VehicleTripPrice>)swpService.getAllRecordsByHQL(daSql);
								//log.info"v size ---> " + vehicleTripPrices.size());
								
								messageResponse = ticketTransactionRef +"###"+transactionRef+"###"+deviceCode+"###"+tripCode +"###"+client.getClientCode()+
										"###" + departingStationCode + "###" + arrivalStationCode + "###" + departureStation.getStationName() + 
										"###" + arrivalStation.getStationName() + "###" + departureTime + "###" + arrivalTime;
								
								
								if(vehicleTripPrices.size()==0)
								{
									jsonObject.add("status", ERROR.NO_TRIPS_FOUND_FOR_SPECIFIED_DATES);
									jsonObject.add("message", "No pricing Found For the Specified trip");
									JsonObject jsonObj = jsonObject.build();
						            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
								}
								
								Iterator<VehicleTripPrice> itVTP = vehicleTripPrices.iterator();
								while(itVTP.hasNext())
								{
									VehicleTripPrice vtp = itVTP.next();
									if(vtp.getPriceType().equals(PriceType.FOR_ADULT_PRICE))
									{
										baseAdultFare = vtp.getAmount();
									}
									else if(vtp.getPriceType().equals(PriceType.FOR_CHILD_PRICE))
									{
										baseChildFare = vtp.getAmount();
									}
									else if(vtp.getPriceType().equals(PriceType.FOR_SENIOR_CITIZENS))
									{
										baseSeniorFare = vtp.getAmount();
									}
									else if(vtp.getPriceType().equals(PriceType.FOR_SPECIALLY_ABLED))
									{
										baseDisabledFare = vtp.getAmount();
									}
								}
								
								adultPassenger = (tripEntry.has("adultPassenger") ? tripEntry.getInt("adultPassenger") : 0);
								childPassenger = (tripEntry.has("childPassenger") ? tripEntry.getInt("childPassenger") : 0);
								seniorPassenger = (tripEntry.has("seniorPassenger") ? tripEntry.getInt("seniorPassenger") : 0);
								disabledPassenger = (tripEntry.has("disabledPassenger") ? tripEntry.getInt("disabledPassenger") : 0);
								JSONObject passengerdetails = tripEntry.getJSONObject("passengerdetails");
								leadPassenger = passengerdetails.getJSONObject("leadPassenger");
								otherPassengers = passengerdetails.has("otherPassengers") ? passengerdetails.getJSONArray("otherPassengers") : null;
								
								
								adultTicketPrice = (adultPassenger*baseAdultFare);
								childTicketPrice = (childPassenger*baseChildFare);
								seniorTicketPrice = (seniorPassenger*baseSeniorFare);
								disabledTicketPrice = (disabledPassenger*baseDisabledFare);
								//log.info"adultPrice === " + adultTicketPrice);
								//log.info"childPrice === " + childTicketPrice);
								
								
								PurchasedTrip purchasedTrip = new PurchasedTrip();
								purchasedTrip.setVehicleSchedule(vs);
								String receiptNo = UtilityHelper.padNumbers(9, (transactionsByLine.size()+1)+"");
								receiptNos.put(receiptNo);
								purchasedTrip.setReceiptNo(receiptNo);
								purchasedTrip.setCreatedAt(new Date());
								purchasedTrip.setUpdatedAt(new Date());
								purchasedTrip.setAmountPayable(adultTicketPrice + childTicketPrice + seniorTicketPrice + disabledTicketPrice/* + client.getBookingFee()*/);
								purchasedTrip.setBookingFee(client.getBookingFee());
								purchasedTrip.setPurchasedTripStatus(PurchasedTripStatus.PENDING);
								purchasedTrip.setTicketCollectionPoint(tcp);
								purchasedTrip.setTransaction(transaction);
								purchasedTrip.setClient(client);
								purchasedTrip.setPassengerCount(adultPassenger + childPassenger + seniorPassenger + disabledPassenger);
								purchasedTrip.setMessageResponse(messageResponse);
								purchasedTrip.setDepartureStation(departureStation);
								purchasedTrip.setArrivalStation(arrivalStation);
								purchasedTrip.setDepartureTime(departureTimeDate);
								purchasedTrip.setArrivalTime(arrivalTimeDate);
								purchasedTrip.setAdultPassengers(adultPassenger);
								purchasedTrip.setChildPassengers(childPassenger);
								purchasedTrip.setSeniorPassengers(seniorPassenger);
								purchasedTrip.setDisabledPassengers(disabledPassenger);
								purchasedTrip.setPurchasePoint(pp);
								purchasedTrip.setTotalUpgradedAdultPassengers(0);
								purchasedTrip.setTotalUpgradedChildPassengers(0);
								purchasedTrip.setTotalUpgradedDisabledPassengers(0);
								purchasedTrip.setTotalUpgradedSeniorPassengers(0);
								purchasedTrip.setTotalUpgradedAmount(0.00);
								purchasedTrip = (PurchasedTrip)(this.swpService.createNewRecord(purchasedTrip));
								
								Customer leadPassengerCustomer = null;
								JSONArray customerListCreated = new JSONArray();
								if(leadPassenger!=null)
								{
									String firstname = leadPassenger.getString("firstname").trim();
									String lastname = leadPassenger.getString("lastname").trim();
									String emailaddress = leadPassenger.getString("emailaddress").trim();
									String mobilenumber = leadPassenger.getString("mobilenumber").trim();
									String nationalid = leadPassenger.getString("nationalid").trim();
									
									if(firstname.length()>0 && lastname.length()>0 && emailaddress.length()>0 && mobilenumber.length()>0)
									{
										leadPassengerCustomer = new Customer();
										leadPassengerCustomer.setClient(client);
										leadPassengerCustomer.setPurchasedTrip(purchasedTrip);
										leadPassengerCustomer.setCreatedAt(new Date());
										leadPassengerCustomer.setUpdatedAt(new Date());
										leadPassengerCustomer.setCustomerStatus(CustomerStatus.ACTIVE);
										leadPassengerCustomer.setEmailAddress(emailaddress);
										leadPassengerCustomer.setFirstName(firstname);
										leadPassengerCustomer.setLastName(lastname);
										leadPassengerCustomer.setMeansOfId(nationalid);
										leadPassengerCustomer.setMobileNumber(mobilenumber);
										leadPassengerCustomer.setIsLeadPassenger(Boolean.TRUE);
										leadPassengerCustomer.setVerificationNumber(RandomStringUtils.randomNumeric(12));
										leadPassengerCustomer = (Customer)swpService.createNewRecord(leadPassengerCustomer);
										customerListCreated.put(leadPassengerCustomer);
									}
									
									if(otherPassengers!=null)
									{
										for(int j=0; j<otherPassengers.length(); j++)
										{
											JSONObject otherPassenger = otherPassengers.getJSONObject(j);
											firstname = otherPassenger.getString("firstname").trim();
											lastname = otherPassenger.getString("lastname").trim();
											emailaddress = otherPassenger.getString("emailaddress").trim();
											mobilenumber = otherPassenger.getString("mobilenumber").trim();
											nationalid = otherPassenger.getString("nationalid").trim();
											
											if(firstname.length()>0 && lastname.length()>0 && emailaddress.length()>0 && mobilenumber.length()>0)
											{
												Customer otherPassengerCustomer = new Customer();
												otherPassengerCustomer.setClient(client);
												otherPassengerCustomer.setPurchasedTrip(purchasedTrip);
												otherPassengerCustomer.setCreatedAt(new Date());
												otherPassengerCustomer.setUpdatedAt(new Date());
												otherPassengerCustomer.setCustomerStatus(CustomerStatus.ACTIVE);
												otherPassengerCustomer.setEmailAddress(emailaddress);
												otherPassengerCustomer.setFirstName(firstname);
												otherPassengerCustomer.setLastName(lastname);
												otherPassengerCustomer.setMeansOfId(nationalid);
												otherPassengerCustomer.setMobileNumber(mobilenumber);
												otherPassengerCustomer.setIsLeadPassenger(Boolean.FALSE);
												otherPassengerCustomer.setVerificationNumber(RandomStringUtils.randomNumeric(12));
												otherPassengerCustomer = (Customer)swpService.createNewRecord(otherPassengerCustomer);
												customerListCreated.put(otherPassengerCustomer);
											}
										}
										
									}
									
									seatIds = new ArrayList<Long>();
									seatsAlloted = tripEntry.getJSONObject("seatsAlloted");
									if(seatsAlloted==null)
									{
										skip = Boolean.TRUE;
									}
									else
									{
										Iterator<String> seatsAllotedIter = seatsAlloted.keys();
										//log.info"numberOfSeats = " + seatsAlloted.toString());
										while(seatsAllotedIter.hasNext())
										{
											String seatsAllotedIterStr = seatsAllotedIter.next();
											JSONArray seatsAllotedArray = seatsAlloted.getJSONArray(seatsAllotedIterStr);
											//log.info"seatsAllotedArray Str = " + seatsAllotedArray.toString());
											for(int j=0; j<seatsAllotedArray.length(); j++)
											{
												JSONObject jsSeatAlloted = (seatsAllotedArray.getJSONObject(j));
												//log.info"jsSeatAlloted = " + jsSeatAlloted.toString());
												seatIds.add(jsSeatAlloted.getLong("seatAvailabilityId"));
											}
										}
									}
									
									hql = "Select tp from ScheduleStationSeatAvailability tp where tp.schedSeatAvailId IN ("+StringUtils.join(seatIds, ", ")+") " +
											"AND tp.deletedAt is NULL AND tp.vehicleSeat.client.clientCode = '"+clientCode+"' AND tp.lockedDownBy = '"+purchaseToken+"' AND " +
											"(tp.lockedDownExpiryDate > CURRENT_TIMESTAMP) AND tp.boughtByCustomer IS NULL AND tp.seatAvailabilityStatus = " + SeatAvailabilityStatus.OPEN.ordinal();
											//+ " " +
											//"AND tp.scheduleStationSeatSection.vehicleTripRouting.vehicleTrip.vehicleTripCode = '"+tripCode+"' " +
											//"ORDER BY tp.vehicleTripRouteSeatSection.vehicleTripRouting.routeOrder";
									vsaList = (Collection<ScheduleStationSeatAvailability>)swpService.getAllRecordsByHQL(hql);
									Iterator<ScheduleStationSeatAvailability> vsaListIter = vsaList.iterator();
									int passengerCounter = 0;
									//log.info"hql --- " + hql);
									//log.info"vsalistier size -- " + vsaList.size());
									//log.info"seatIds size -- " + seatIds.size());
									JSONArray vehicleSeatsLockedDown = new JSONArray();
									while(vsaListIter.hasNext())
									{
										ScheduleStationSeatAvailability vsa = vsaListIter.next();
										vsa.setBoughtByCustomer(leadPassengerCustomer);
										vsa.setSeatAvailabilityStatus(SeatAvailabilityStatus.PURCHASED);
										vsa.setUpdatedAt(new Date());
										swpService.updateRecord(vsa);
										
										String channel = "";
										String cardPan = "";
										Long ticketCollectionPointId = null;
										Integer childSeatCount = null;
										Integer adultSeatCount = null;
										
										
										
										
										if(vsa.getPassengerType().equals(PassengerType.ADULT_PASSENGER))
										{
											PurchasedTripSeat pts = new PurchasedTripSeat();
											pts.setCreatedAt(new Date());
											pts.setPurchasedTrip(purchasedTrip);
											pts.setUpdatedAt(new Date());
											pts.setVehicleSeat(vsa.getVehicleSeat());
											pts.setScheduleStationSeatAvailability(vsa);
											pts.setClient(client);
											pts.setPassengerType(PassengerType.ADULT_PASSENGER);
											pts = (PurchasedTripSeat)(this.swpService.createNewRecord(pts));
										}
										else if(vsa.getPassengerType().equals(PassengerType.CHILD_PASSENGER))
										{
											PurchasedTripSeat pts = new PurchasedTripSeat();
											pts.setCreatedAt(new Date());
											pts.setPurchasedTrip(purchasedTrip);
											pts.setUpdatedAt(new Date());
											pts.setVehicleSeat(vsa.getVehicleSeat());
											pts.setScheduleStationSeatAvailability(vsa);
											pts.setClient(client);
											pts.setPassengerType(PassengerType.CHILD_PASSENGER);
											pts = (PurchasedTripSeat)(this.swpService.createNewRecord(pts));
										}
										else if(vsa.getPassengerType().equals(PassengerType.SENIOR_PASSENGER))
										{
											PurchasedTripSeat pts = new PurchasedTripSeat();
											pts.setCreatedAt(new Date());
											pts.setPurchasedTrip(purchasedTrip);
											pts.setUpdatedAt(new Date());
											pts.setVehicleSeat(vsa.getVehicleSeat());
											pts.setScheduleStationSeatAvailability(vsa);
											pts.setClient(client);
											pts.setPassengerType(PassengerType.SENIOR_PASSENGER);
											pts = (PurchasedTripSeat)(this.swpService.createNewRecord(pts));
										}
										else if(vsa.getPassengerType().equals(PassengerType.SPECIAL_NEEDS_PASSENGER))
										{
											PurchasedTripSeat pts = new PurchasedTripSeat();
											pts.setCreatedAt(new Date());
											pts.setPurchasedTrip(purchasedTrip);
											pts.setUpdatedAt(new Date());
											pts.setVehicleSeat(vsa.getVehicleSeat());
											pts.setScheduleStationSeatAvailability(vsa);
											pts.setClient(client);
											pts.setPassengerType(PassengerType.SPECIAL_NEEDS_PASSENGER);
											pts = (PurchasedTripSeat)(this.swpService.createNewRecord(pts));
										}
										
										passengerCounter++;
												
										JSONObject js = new JSONObject();
										js.put("seatAvailabilityId", vsa.getSchedSeatAvailId());
										js.put("seatNumber", vsa.getVehicleSeat().getSeatNumber());
										js.put("seatName", vsa.getVehicleSeat().getSeatNumber());
										js.put("expirationPeriodMinutes", client.getLockDownInterval());
										js.put("seatClass", vsa.getVehicleSeat().getVehicleSeatSection().getSectionName());
										js.put("seatLocation", vsa.getVehicleSeat().getVehicleSeatLocation().name());
										js.put("seatFacingOtherSeats", vsa.getVehicleSeat().getTripSeatFacing());
										js.put("expirationDate", new SimpleDateFormat("yyyy-MM-dd HH:mm").format(vsa.getLockedDownExpiryDate()));
										vehicleSeatsLockedDown.put(js);
									}
									JSONObject tempHolder = new JSONObject();
									tempHolder.put("allSeatsAlloted", vehicleSeatsLockedDown);
									allTripSeatDetails.put(tripCode, tempHolder);
								}
							}
						}
						

						if(coupon!=null)
						{
							coupon.setCouponStatus(CouponStatus.USED);
							swpService.updateRecord(coupon);
						}
						
						jsonObject.add("seatsAlloted", allTripSeatDetails.toString());
						jsonObject.add("transactionRef", transaction.getOrderRef());
						jsonObject.add("receiptNos", receiptNos.toString());
						//jsonObject.add("tripDetails", tripSummary.toString());
						jsonObject.add("status", ERROR.GENERAL_SUCCESS);
						jsonObject.add("message", "Trip purchase was successful");
						JsonObject jsonObj = jsonObject.build();
			            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
					
					}
					else
					{
						//Transaction was not successful
						jsonObject.add("message", "Transaction Failed. Transaction was not successful");
						jsonObject.add("status", ERROR.TICKET_PURCHASE_FAILED);
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
			log.error("exception...", e);
			e.printStackTrace();
			JsonObject jsonObj = jsonObject.build();
            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
		}
	}
	
	
	
	
	public Response purchaseVehicleTripGroupTickets(String purchasePoint, String groupTicketCode, String paymentType, String transactionRef, String deviceCode, 
			String ticketCollectionCenterCode, String purchaseToken, String purchaseDetails, 
			String generalTripClass, String requestId, String ipAddress, String clientCode, String token, String couponCode, Double concessionAmount) {
		// TODO Auto-generated method stub
		JsonObjectBuilder jsonObject = Json.createObjectBuilder();
    	JSONArray receiptNos = new JSONArray();
		try{
			//log.info">>>..." + purchasePoint);
			//log.info">>>..." + paymentType);
			//log.info">>>..." + transactionRef);
			//log.info">>>..." + deviceCode);
			//log.info">>>..." + ticketCollectionCenterCode); 
			//log.info">>>..." + purchaseToken);
			//log.info">>>..." + purchaseDetails); 
			//log.info">>>..." + generalTripClass); 
			//log.info">>>..." + requestId);
			//log.info">>>..." + ipAddress);
			//log.info">>>..." + clientCode); 
			//log.info">>>..." + token);
			//log.info">>>..." + groupTicketCode);
			if(purchasePoint==null || deviceCode==null || purchaseToken==null || groupTicketCode==null || purchaseDetails==null || paymentType==null || generalTripClass==null || deviceCode==null)
			{
				jsonObject.add("status", ERROR.INCOMPLETE_PARAMETERS);
				jsonObject.add("message", "Incomplete parameters provided. Provide purchaseToken, transactionRef, deviceCode, purchaseDetails, paymentType, generalTripClass parameters");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			PurchasePoint pp = null;
			
			try
			{
				pp = PurchasePoint.valueOf(purchasePoint);
			}
			catch(IllegalArgumentException | NullPointerException e)
			{
				jsonObject.add("status", ERROR.INCOMPLETE_PARAMETERS);
				jsonObject.add("message", "Invalid purchase point provided");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			Double totalPrice = 0.00;
			
			JSONObject tripTicketDataJSArray = null;
			String messageRequest = null;
			String messageResponse = null;
			//String transactionRef = null;
			
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
			
			
			Coupon coupon = null;
			Double couponValuation = 0.00;
			if(couponCode!=null)
			{
				String hql1 = "Select tp from Coupon tp where lower(tp.couponCode) = '"+couponCode.toLowerCase()+"' AND tp.deletedAt IS NULL AND tp.client.clientId = " + client.getClientId();
				coupon = (Coupon)swpService.getUniqueRecordByHQL(hql1);
				
			}
			
			if(paymentType.equals(PaymentMeans.CASH.name()))
			{
				if(token==null)
				{
					jsonObject.add("status", ERROR.INSUFFICIENT_PRIVILEDGES);
					jsonObject.add("message", "Insufficient priviledges. You can not carry out this transaction");
					JsonObject jsonObj = jsonObject.build();
		            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
				}
				
				JSONObject purchaseDetailsJS = new JSONObject(purchaseDetails);
				tripTicketDataJSArray = purchaseDetailsJS.getJSONObject("tripTicketData");
				//String probasePayMerchantCode = purchaseDetailsJS.has("probasePayMerchantCode") ? purchaseDetailsJS.getString("probasePayMerchantCode") : null; 
				//String probasePayDeviceCode = purchaseDetailsJS.has("probasePayDeviceCode") ? purchaseDetailsJS.getString("probasePayDeviceCode") : null;
				String orderId = purchaseDetailsJS.has("orderId") ? purchaseDetailsJS.getString("orderId") : null;
				//transactionRef = purchaseDetailsJS.has("transactionRef") ? purchaseDetailsJS.getString("transactionRef") : null;
				String hash = purchaseDetailsJS.has("hash") ? purchaseDetailsJS.getString("hash") : null;
				// || probasePayMerchantCode==null || probasePayDeviceCode==null


				if(tripTicketDataJSArray==null || hash==null)
				{
					jsonObject.add("status", ERROR.INCOMPLETE_PARAMETERS);
					jsonObject.add("message", "Incomplete parameters provided. Ensure you provide merchantCode, probasePayDeviceCode, transactionRef, and hash parameters in your tripTicketData parameter");
					JsonObject jsonObj = jsonObject.build();
		            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
				}
				JSONObject jsObj = new JSONObject();
				
				messageRequest = hash+"###"+deviceCode+"###"+transactionRef+"###"+token+"###"+"###"+ticketCollectionCenterCode
						+"###"+ipAddress+"###"+clientCode;

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
				
				GroupTripRequest gtr = null;
				gtr = (GroupTripRequest)swpService.getUniqueRecordByHQL("Select tp from GroupTripRequest tp where " +
						"tp.requestCode = '"+groupTicketCode+"' AND tp.client.clientCode = '"+clientCode+"' AND " +
						"tp.deletedAt IS NULL AND tp.groupTripRequestStatus = " + GroupTripRequestStatus.PENDING.ordinal());
				if(gtr==null)
				{
					jsonObject.add("status", ERROR.GROUP_TRIP_REQUEST_NOT_FOUND);
					jsonObject.add("message", "Invalid group ticket request code provided. Group Trip ticket may have already been booked");
					JsonObject jsonObj = jsonObject.build();
		            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
				}
				
				User user = null;
				Boolean skip = Boolean.FALSE;
				RoleType roleCode = null;
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
						//if(user!=null)
						//	roleCode = user.getRoleCode();
					}
				}
				
				
				if(roleCode==null || (roleCode!=null && !roleCode.equals(RoleType.ADMIN_STAFF)))
				{
					//log.info"5");
					jsonObject.add("status", ERROR.INSUFFICIENT_PRIVILEDGES);
					jsonObject.add("message", "You do not have the priviledges to carry out this action");
					JsonObject jsonObj = jsonObject.build();
					return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
				}
				
				TicketCollectionPoint tcp = null;
				if(ticketCollectionCenterCode!=null)
				{
					hql = "Select tp from TicketCollectionPoint tp where tp.collectionPointCode = '"+ticketCollectionCenterCode+"' AND tp.deletedAt IS NULL " +
							"AND tp.client.clientCode = '"+clientCode+"'";
					tcp = (TicketCollectionPoint)swpService.getUniqueRecordByHQL(hql);
				}
				



				JSONObject allTripSeatDetails = new JSONObject();
				List<Long> seatIds = new ArrayList<Long>();
				Iterator<String> outwardInwardIter = tripTicketDataJSArray.keys();
				
				while(outwardInwardIter.hasNext())
				{
					String outwardInwardStr = outwardInwardIter.next();
					JSONObject outwardTrips = tripTicketDataJSArray.getJSONObject(outwardInwardStr);
					JSONArray tripCodes = outwardTrips.names();
					for(int i=0; i<tripCodes.length(); i++)
					{
						String tripCode = tripCodes.getString(i);
						JSONObject tripEntry = outwardTrips.getJSONObject(tripCode);
						Integer adultPassenger = 0;
						Integer childPassenger = 0;
						Integer seniorPassenger = 0;
						Integer disabledPassenger = 0;
						JSONObject seatsAlloted = tripEntry.getJSONObject("seatsAlloted");
						Double amount = tripEntry.getDouble("amount");
						totalPrice = totalPrice + amount + client.getBookingFee();
						if(seatsAlloted==null)
						{
							skip = Boolean.TRUE;
						}
						else
						{
							Iterator<String> seatsAllotedIter = seatsAlloted.keys();
							while(seatsAllotedIter.hasNext())
							{
								String seatsAllotedIterStr = seatsAllotedIter.next();
								JSONArray seatsAllotedArray = seatsAlloted.getJSONArray(seatsAllotedIterStr);
								//log.info"seatsAllotedArray Str = " + seatsAllotedArray.toString());
								for(int j=0; j<seatsAllotedArray.length(); j++)
								{
									JSONObject jsSeatAlloted = (seatsAllotedArray.getJSONObject(j));
									//log.info"jsSeatAlloted = " + jsSeatAlloted.toString());
									seatIds.add(jsSeatAlloted.getLong("seatAvailabilityId"));
								}
							}
							
						}
						adultPassenger = (tripEntry.has("adultPassenger") ? tripEntry.getInt("adultPassenger") : 0);
						childPassenger = (tripEntry.has("childPassenger") ? tripEntry.getInt("childPassenger") : 0);
						seniorPassenger = (tripEntry.has("seniorPassenger") ? tripEntry.getInt("seniorPassenger") : 0);
						disabledPassenger = (tripEntry.has("disabledPassenger") ? tripEntry.getInt("disabledPassenger") : 0);
						JSONObject passengerdetails = tripEntry.getJSONObject("passengerdetails");
						JSONObject leadPassenger = passengerdetails.getJSONObject("leadPassenger");
						JSONArray otherPassengers = passengerdetails.has("otherPassengers") ? passengerdetails.getJSONArray("otherPassengers") : null;
					}
				}
				
				//log.info"...join == " + StringUtils.join(seatIds, ", "));
				
				if(skip.equals(Boolean.TRUE))
				{
					jsonObject.add("status", ERROR.INCOMPLETE_PARAMETERS_SEATS_NOT_ALLOTTED_COMPLETELY);
					jsonObject.add("message", "Seats purchased not specified completely");
					JsonObject jsonObj = jsonObject.build();
		            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
				}
				else
				{
					hql = "Select tp from ScheduleStationSeatAvailability tp where tp.schedSeatAvailId IN ("+StringUtils.join(seatIds, ", ")+") " +
							"AND tp.deletedAt is NULL AND tp.vehicleSeat.client.clientCode = '"+clientCode+"' AND tp.lockedDownBy = '"+purchaseToken+"' AND " +
							"(tp.lockedDownExpiryDate > CURRENT_TIMESTAMP) AND tp.seatAvailabilityStatus = "+ SeatAvailabilityStatus.OPEN.ordinal() +"";
					//log.info"....HQL = " + hql);
					Collection<ScheduleStationSeatAvailability> vsaList = (Collection<ScheduleStationSeatAvailability>)swpService.getAllRecordsByHQL(hql);
					//log.info"vsaList.size()<seatIds.size() ==" + vsaList.size() + " -- " + seatIds.size());
					if(vsaList!=null && vsaList.size()<seatIds.size())
					{
						jsonObject.add("status", ERROR.INCOMPLETE_SEATS_AVAILABLE_PURCHASE_TIME_EXPIRED);
						jsonObject.add("message", "Time expired for purchasing the seats alloted to you");
						JsonObject jsonObj = jsonObject.build();
			            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
					}

					String status = null;
					String toHash = client.getClientCode() + "" + deviceCode + "" + orderId + "" + totalPrice + user.getWebActivationCode();
					//log.info"toHash == " + toHash);
					
					
					
					String serviceType = ServiceType.EPAYMENT_FOR_TRIP.name();
					JSONArray tripSummary = new JSONArray();
					String ticketTransactionRef = RandomStringUtils.randomNumeric(8);
					hql = "Select tp from Transaction tp where tp.orderRef = '"+transactionRef+"' AND tp.client.clientCode = '"+clientCode+"' AND " +
							"tp.deletedAt IS NULL";
					Transaction transaction = (Transaction)swpService.getUniqueRecordByHQL(hql);
					
					if(transaction!=null)
					{
						jsonObject.add("status", ERROR.TRANSACTION_REF_ALREADY_PAID_PREVIOUSLY);
						jsonObject.add("message", "Transaction Reference you provided belongs to another transaction that has already been paid");
						JsonObject jsonObj = jsonObject.build();
			            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
					}
					
						
					transaction = new Transaction();
					if(coupon!=null)
					{
						couponValuation = (totalPrice*coupon.getDiscountRate()/100);
						totalPrice = totalPrice - couponValuation; 
						transaction.setAppliedCoupon(coupon);
					}
					
					JSONObject vehicleSeatsLockedDownTrip = new JSONObject();
					
					Double fixedCharge = 0.00;
					Double transactionFee = 0.00;
					Double transactionAmount = totalPrice;
					
					
					
					transaction.setTransactionRef(ticketTransactionRef);
					transaction.setOrderRef(device.getDeviceCode() + "-" + transactionRef);
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
					transaction.setNarration("Debit|"+gtr.getRequestCode()+"|#" + transactionRef + "|ZMW" + transaction.getTransactionAmount());
					transaction.setUpdatedAt(new Date());
					transaction.setCreatedAt(new Date());
					transaction.setPaymentMeans(PaymentMeans.CASH);
					transaction.setClient(client);						
					transaction.setTransactionCurrency("ZMW");
					transaction = (Transaction)swpService.createNewRecord(transaction);
					
					
					
					JSONObject jsonTrips = new JSONObject();
					JSONObject leadPassenger = null;
					JSONArray otherPassengers = null;
					
					
					outwardInwardIter = tripTicketDataJSArray.keys();
					while(outwardInwardIter.hasNext())
					{
						String outwardInwardStr = outwardInwardIter.next();
						JSONObject outwardTrips = tripTicketDataJSArray.getJSONObject(outwardInwardStr);
						JSONArray tripCodes = outwardTrips.names();
						for(int i=0; i<tripCodes.length(); i++)
						{
							String tripCode = tripCodes.getString(i);
							JSONObject tripEntry = outwardTrips.getJSONObject(tripCode);
							Integer adultPassenger = 0;
							Integer childPassenger = 0;
							Integer seniorPassenger = 0;
							Integer disabledPassenger = 0;
							JSONObject seatsAlloted = tripEntry.getJSONObject("seatsAlloted");
							Double tripPrice = tripEntry.getDouble("amount");
							
							hql = "Select tp from VehicleSchedule tp where tp.scheduleStationCode.scheduleStationCode = '"+tripCode+"' " +
									"AND tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL";
							VehicleSchedule vs = (VehicleSchedule)swpService.getUniqueRecordByHQL(hql);
							
							hql = "Select tp.purchasedTripId from PurchasedTrip tp where " +
									//"tp.vehicleSchedule.line.lineId = '" + vs.getLine().getLineId() + "' AND " +
									"tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL";
							//log.info"PurchasedTrip... = " + hql);
							List<Long> transactionsByLine = (List<Long>)swpService.getAllRecordsByHQL(hql);
							
							String departingStationCode = tripEntry.getString("departingStation");
							String arrivalStationCode = tripEntry.getString("arrivingStation");
							String departureTime = tripEntry.getString("departureTime");
							String arrivalTime = tripEntry.getString("arrivalTime");
							Date departureTimeDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(departureTime);
							Date arrivalTimeDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(arrivalTime);
							
							String daSql = "Select tp from Station tp where tp.stationCode = '"+departingStationCode+"' AND " +
									"tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL";
							Station departureStation = (Station)swpService.getUniqueRecordByHQL(daSql);

							daSql = "Select tp from Station tp where tp.stationCode = '"+arrivalStationCode+"' AND " +
										"tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL";
							Station arrivalStation = (Station)swpService.getUniqueRecordByHQL(daSql);
							
							
							messageResponse = ticketTransactionRef +"###"+transactionRef+"###"+deviceCode+"###"+tripCode +"###"+client.getClientCode()+
									"###" + departingStationCode + "###" + arrivalStationCode + "###" + departureStation.getStationName() + 
									"###" + arrivalStation.getStationName() + "###" + departureTime + "###" + arrivalTime;
							
							
							
							adultPassenger = (tripEntry.has("adultPassenger") ? tripEntry.getInt("adultPassenger") : 0);
							childPassenger = (tripEntry.has("childPassenger") ? tripEntry.getInt("childPassenger") : 0);
							seniorPassenger = (tripEntry.has("seniorPassenger") ? tripEntry.getInt("seniorPassenger") : 0);
							disabledPassenger = (tripEntry.has("disabledPassenger") ? tripEntry.getInt("disabledPassenger") : 0);
							JSONObject passengerdetails = tripEntry.getJSONObject("passengerdetails");
							leadPassenger = passengerdetails.getJSONObject("leadPassenger");
							otherPassengers = passengerdetails.has("otherPassengers") ? passengerdetails.getJSONArray("otherPassengers") : null;
							
							
							
							
							PurchasedTrip purchasedTrip = new PurchasedTrip();
							purchasedTrip.setVehicleSchedule(vs);
							String receiptNo = UtilityHelper.padNumbers(9, (transactionsByLine.size()+1)+"");
							receiptNos.put(receiptNo);
							purchasedTrip.setReceiptNo(receiptNo);
							purchasedTrip.setCreatedAt(new Date());
							purchasedTrip.setUpdatedAt(new Date());
							purchasedTrip.setAmountPayable(tripPrice);
							purchasedTrip.setBookingFee(client.getBookingFee());
							purchasedTrip.setPurchasedTripStatus(PurchasedTripStatus.PENDING);
							purchasedTrip.setTicketCollectionPoint(tcp);
							purchasedTrip.setTransaction(transaction);
							purchasedTrip.setClient(client);
							purchasedTrip.setPassengerCount(adultPassenger + childPassenger + seniorPassenger + disabledPassenger);
							purchasedTrip.setMessageResponse(messageResponse);
							purchasedTrip.setDepartureStation(departureStation);
							purchasedTrip.setArrivalStation(arrivalStation);
							purchasedTrip.setDepartureTime(departureTimeDate);
							purchasedTrip.setArrivalTime(arrivalTimeDate);
							purchasedTrip.setGroupTripRequest(gtr);
							purchasedTrip.setAdultPassengers(adultPassenger);
							purchasedTrip.setChildPassengers(childPassenger);
							purchasedTrip.setSeniorPassengers(seniorPassenger);
							purchasedTrip.setDisabledPassengers(disabledPassenger);
							purchasedTrip.setPurchasePoint(pp);
							purchasedTrip.setTotalUpgradedAdultPassengers(0);
							purchasedTrip.setTotalUpgradedChildPassengers(0);
							purchasedTrip.setTotalUpgradedDisabledPassengers(0);
							purchasedTrip.setTotalUpgradedSeniorPassengers(0);
							purchasedTrip.setTotalUpgradedAmount(0.00);
							purchasedTrip = (PurchasedTrip)(this.swpService.createNewRecord(purchasedTrip));
							
							Customer leadPassengerCustomer = null;
							JSONArray customerListCreated = new JSONArray();
							if(leadPassenger!=null)
							{
								String firstname = leadPassenger.getString("firstname").trim();
								String lastname = leadPassenger.getString("lastname").trim();
								String emailaddress = leadPassenger.getString("emailaddress").trim();
								String mobilenumber = leadPassenger.getString("mobilenumber").trim();
								String nationalid = leadPassenger.getString("nationalid").trim();
								
								if(firstname.length()>0 && lastname.length()>0 && emailaddress.length()>0 && mobilenumber.length()>0)
								{
									leadPassengerCustomer = new Customer();
									leadPassengerCustomer.setClient(client);
									leadPassengerCustomer.setPurchasedTrip(purchasedTrip);
									leadPassengerCustomer.setCreatedAt(new Date());
									leadPassengerCustomer.setUpdatedAt(new Date());
									leadPassengerCustomer.setCustomerStatus(CustomerStatus.ACTIVE);
									leadPassengerCustomer.setEmailAddress(emailaddress);
									leadPassengerCustomer.setFirstName(firstname);
									leadPassengerCustomer.setLastName(lastname);
									leadPassengerCustomer.setMeansOfId(nationalid);
									leadPassengerCustomer.setMobileNumber(mobilenumber);
									leadPassengerCustomer.setIsLeadPassenger(Boolean.TRUE);
									leadPassengerCustomer.setVerificationNumber(RandomStringUtils.randomNumeric(12));
									leadPassengerCustomer = (Customer)swpService.createNewRecord(leadPassengerCustomer);
									customerListCreated.put(leadPassengerCustomer);
								}
								
								if(otherPassengers!=null)
								{
									for(int j=0; j<otherPassengers.length(); j++)
									{
										JSONObject otherPassenger = otherPassengers.getJSONObject(j);
										firstname = otherPassenger.getString("firstname").trim();
										lastname = otherPassenger.getString("lastname").trim();
										emailaddress = otherPassenger.getString("emailaddress").trim();
										mobilenumber = otherPassenger.getString("mobilenumber").trim();
										nationalid = otherPassenger.getString("nationalid").trim();
										
										if(firstname.length()>0 && lastname.length()>0 && emailaddress.length()>0 && mobilenumber.length()>0)
										{
											Customer otherPassengerCustomer = new Customer();
											otherPassengerCustomer.setClient(client);
											otherPassengerCustomer.setPurchasedTrip(purchasedTrip);
											otherPassengerCustomer.setCreatedAt(new Date());
											otherPassengerCustomer.setUpdatedAt(new Date());
											otherPassengerCustomer.setCustomerStatus(CustomerStatus.ACTIVE);
											otherPassengerCustomer.setEmailAddress(emailaddress);
											otherPassengerCustomer.setFirstName(firstname);
											otherPassengerCustomer.setLastName(lastname);
											otherPassengerCustomer.setMeansOfId(nationalid);
											otherPassengerCustomer.setMobileNumber(mobilenumber);
											otherPassengerCustomer.setIsLeadPassenger(Boolean.FALSE);
											otherPassengerCustomer.setVerificationNumber(RandomStringUtils.randomNumeric(12));
											otherPassengerCustomer = (Customer)swpService.createNewRecord(otherPassengerCustomer);
											customerListCreated.put(otherPassengerCustomer);
										}
									}
									
								}
								
								seatIds = new ArrayList<Long>();
								seatsAlloted = tripEntry.getJSONObject("seatsAlloted");
								if(seatsAlloted==null)
								{
									skip = Boolean.TRUE;
								}
								else
								{
									Iterator<String> seatsAllotedIter = seatsAlloted.keys();
									//log.info"numberOfSeats = " + seatsAlloted.toString());
									while(seatsAllotedIter.hasNext())
									{
										String seatsAllotedIterStr = seatsAllotedIter.next();
										JSONArray seatsAllotedArray = seatsAlloted.getJSONArray(seatsAllotedIterStr);
										//log.info"seatsAllotedArray Str = " + seatsAllotedArray.toString());
										for(int j=0; j<seatsAllotedArray.length(); j++)
										{
											JSONObject jsSeatAlloted = (seatsAllotedArray.getJSONObject(j));
											//log.info"jsSeatAlloted = " + jsSeatAlloted.toString());
											seatIds.add(jsSeatAlloted.getLong("seatAvailabilityId"));
										}
									}
								}
								
								hql = "Select tp from ScheduleStationSeatAvailability tp where tp.schedSeatAvailId IN ("+StringUtils.join(seatIds, ", ")+") " +
										"AND tp.deletedAt is NULL AND tp.vehicleSeat.client.clientCode = '"+clientCode+"' AND tp.lockedDownBy = '"+purchaseToken+"' AND " +
										"(tp.lockedDownExpiryDate > CURRENT_TIMESTAMP) AND tp.boughtByCustomer IS NULL AND tp.seatAvailabilityStatus = " + SeatAvailabilityStatus.OPEN.ordinal();
										//+ " " +
										//"AND tp.scheduleStationSeatSection.vehicleTripRouting.vehicleTrip.vehicleTripCode = '"+tripCode+"' " +
										//"ORDER BY tp.vehicleTripRouteSeatSection.vehicleTripRouting.routeOrder";
								vsaList = (Collection<ScheduleStationSeatAvailability>)swpService.getAllRecordsByHQL(hql);
								Iterator<ScheduleStationSeatAvailability> vsaListIter = vsaList.iterator();
								int passengerCounter = 0;
								//log.info"hql --- " + hql);
								//log.info"vsalistier size -- " + vsaList.size());
								//log.info"seatIds size -- " + seatIds.size());
								JSONArray vehicleSeatsLockedDown = new JSONArray();
								while(vsaListIter.hasNext())
								{
									ScheduleStationSeatAvailability vsa = vsaListIter.next();
									vsa.setBoughtByCustomer(leadPassengerCustomer);
									vsa.setSeatAvailabilityStatus(SeatAvailabilityStatus.PURCHASED);
									vsa.setUpdatedAt(new Date());
									swpService.updateRecord(vsa);
									
									String channel = "";
									String cardPan = "";
									Long ticketCollectionPointId = null;
									Integer childSeatCount = null;
									Integer adultSeatCount = null;
									
									
									
									
									if(vsa.getPassengerType().equals(PassengerType.ADULT_PASSENGER))
									{
										PurchasedTripSeat pts = new PurchasedTripSeat();
										pts.setCreatedAt(new Date());
										pts.setPurchasedTrip(purchasedTrip);
										pts.setUpdatedAt(new Date());
										pts.setVehicleSeat(vsa.getVehicleSeat());
										pts.setScheduleStationSeatAvailability(vsa);
										pts.setClient(client);
										pts.setPassengerType(PassengerType.ADULT_PASSENGER);
										pts = (PurchasedTripSeat)(this.swpService.createNewRecord(pts));
									}
									else if(vsa.getPassengerType().equals(PassengerType.CHILD_PASSENGER))
									{
										PurchasedTripSeat pts = new PurchasedTripSeat();
										pts.setCreatedAt(new Date());
										pts.setPurchasedTrip(purchasedTrip);
										pts.setUpdatedAt(new Date());
										pts.setVehicleSeat(vsa.getVehicleSeat());
										pts.setScheduleStationSeatAvailability(vsa);
										pts.setClient(client);
										pts.setPassengerType(PassengerType.CHILD_PASSENGER);
										pts = (PurchasedTripSeat)(this.swpService.createNewRecord(pts));
									}
									else if(vsa.getPassengerType().equals(PassengerType.SENIOR_PASSENGER))
									{
										PurchasedTripSeat pts = new PurchasedTripSeat();
										pts.setCreatedAt(new Date());
										pts.setPurchasedTrip(purchasedTrip);
										pts.setUpdatedAt(new Date());
										pts.setVehicleSeat(vsa.getVehicleSeat());
										pts.setScheduleStationSeatAvailability(vsa);
										pts.setClient(client);
										pts.setPassengerType(PassengerType.SENIOR_PASSENGER);
										pts = (PurchasedTripSeat)(this.swpService.createNewRecord(pts));
									}
									else if(vsa.getPassengerType().equals(PassengerType.SPECIAL_NEEDS_PASSENGER))
									{
										PurchasedTripSeat pts = new PurchasedTripSeat();
										pts.setCreatedAt(new Date());
										pts.setPurchasedTrip(purchasedTrip);
										pts.setUpdatedAt(new Date());
										pts.setVehicleSeat(vsa.getVehicleSeat());
										pts.setScheduleStationSeatAvailability(vsa);
										pts.setClient(client);
										pts.setPassengerType(PassengerType.SPECIAL_NEEDS_PASSENGER);
										pts = (PurchasedTripSeat)(this.swpService.createNewRecord(pts));
									}
									
									passengerCounter++;
											
									JSONObject js = new JSONObject();
									js.put("seatAvailabilityId", vsa.getSchedSeatAvailId());
									js.put("seatNumber", vsa.getVehicleSeat().getSeatNumber());
									js.put("seatName", vsa.getVehicleSeat().getSeatNumber());
									js.put("expirationPeriodMinutes", client.getLockDownInterval());
									js.put("seatClass", vsa.getVehicleSeat().getVehicleSeatSection().getSectionName());
									js.put("seatLocation", vsa.getVehicleSeat().getVehicleSeatLocation().name());
									js.put("seatFacingOtherSeats", vsa.getVehicleSeat().getTripSeatFacing());
									js.put("expirationDate", new SimpleDateFormat("yyyy-MM-dd HH:mm").format(vsa.getLockedDownExpiryDate()));
									vehicleSeatsLockedDown.put(js);
								}
								JSONObject tempHolder = new JSONObject();
								tempHolder.put("allSeatsAlloted", vehicleSeatsLockedDown);
								allTripSeatDetails.put(tripCode, tempHolder);
							}
						}
					}
					
					gtr.setGroupTripRequestStatus(GroupTripRequestStatus.PAID);
					gtr.setUpdatedAt(new Date());
					gtr.setApprovedByUser(user);
					swpService.updateRecord(gtr);
					
					if(coupon!=null)
					{
						jsonObject.add("couponValuation", couponValuation);
						jsonObject.add("coupon", new Gson().toJson(coupon));
					}
					
					if(concessionAmount!=null)
					{
						jsonObject.add("concessionAmount", concessionAmount);	
					}
					
					jsonObject.add("seatsAlloted", allTripSeatDetails.toString());
					jsonObject.add("transactionRef", transaction.getOrderRef());
					//jsonObject.add("tripDetails", tripSummary.toString());
					jsonObject.add("status", ERROR.GENERAL_SUCCESS);
					jsonObject.add("message", "Trip purchase was successful");
					JsonObject jsonObj = jsonObject.build();
		            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
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
	
	
	public Response refundPurchasedTripTransaction(String deviceCode, String receiptNo, String requestId, String ipAddress, String clientCode, String token) {
		// TODO Auto-generated method stub
		JsonObjectBuilder jsonObject = Json.createObjectBuilder();
    	
		try{
			if(deviceCode==null ||  receiptNo==null || deviceCode==null)
			{
				jsonObject.add("status", ERROR.INCOMPLETE_PARAMETERS);
				jsonObject.add("message", "Incomplete parameters provided. Provide purchaseToken, orderRef, deviceCode parameters");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			//log.info">>>..." + deviceCode);
			//log.info">>>..." + receiptNo); 
			//log.info">>>..." + requestId);
			//log.info">>>..." + ipAddress);
			//log.info">>>..." + clientCode); 
			//log.info">>>..." + token);
			
			JSONObject tripTicketDataJSArray = null;
			//String transactionRef = null;
			
			if(token==null)
			{
				jsonObject.add("status", ERROR.INSUFFICIENT_PRIVILEDGES);
				jsonObject.add("message", "You do not have the priviledges to carry out this action");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			
			JSONObject jsObj = new JSONObject();
			

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
			Boolean skip = Boolean.FALSE;
			RoleType roleCode = null;
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
					
					//roleCode = user.getRoleCode();
				}
			}
			
			if(!roleCode.equals(RoleType.ADMIN_STAFF) && !roleCode.equals(RoleType.OPERATOR))
			{
				//log.info"5");
				jsonObject.add("status", ERROR.INSUFFICIENT_PRIVILEDGES);
				jsonObject.add("message", "You do not have the priviledges to carry out this action");
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


			hql = "Select tp from PurchasedTrip tp where tp.receiptNo = '"+receiptNo+"' AND tp.purchasedTripStatus = " + PurchasedTripStatus.PENDING.ordinal() + " AND " +
					"tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL";
			Collection<PurchasedTrip> purchasedTrips = (Collection<PurchasedTrip>)swpService.getAllRecordsByHQL(hql);
			if(purchasedTrips!=null  && purchasedTrips.size()>0)
			{
			}
			else
			{
				jsonObject.add("status", ERROR.PURCHASED_TRIP_NOT_FOUND);
				jsonObject.add("message", "Purchased trip could not be found");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
				
			}

			/*hql = "Select tp from Transaction tp where tp.orderRef = '"+orderRef+"' AND tp.status = " + TransactionStatus.SUCCESS.ordinal() + " AND " +
					"tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL";
			Transaction oldTransaction= (Transaction)swpService.getUniqueRecordByHQL(hql);
			if(oldTransaction==null)
			{
				jsonObject.add("status", ERROR.TRANSACTION_NOT_FOUND);
				jsonObject.add("message", "Transaction could not be found");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
				
			}
			
			
			hql = "Select tp from PurchasedTrip tp where tp.transaction.transactionId = '"+oldTransaction.getTransactionId()+"' AND tp.deletedAt IS NULL";
			Collection<PurchasedTrip> purchasedTrips = (Collection<PurchasedTrip>)swpService.getAllRecordsByHQL(hql);*/
			//log.info"purchasedTrips size = " + purchasedTrips.size());
			if(purchasedTrips==null || (purchasedTrips!=null && purchasedTrips.size()==0))
			{
				jsonObject.add("status", ERROR.PURCHASED_TRIP_NOT_FOUND);
				jsonObject.add("message", "Purchased trips could not be found");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
				
			}
				
			Double transactionAmount = 0.00;
			Double refundIndex = 0.00;
			Iterator<PurchasedTrip> ptIt = purchasedTrips.iterator();
			int counter = 0;
			Transaction oldTransaction = null;
			while(ptIt.hasNext())
			{
				PurchasedTrip purchasedTrip = ptIt.next();
				
				hql = "Select tp from PurchasedTripSeat tp where tp.purchasedTrip.purchasedTripId = '"+purchasedTrip.getPurchasedTripId()+"' AND tp.deletedAt IS NULL";
				Collection<PurchasedTripSeat> purchasedTripSeatList = (Collection<PurchasedTripSeat>)swpService.getAllRecordsByHQL(hql);
				Iterator<PurchasedTripSeat> itpts = purchasedTripSeatList.iterator();
				//log.info"purchasedTripSeatList size = " + purchasedTripSeatList.size());
				while(itpts.hasNext())
				{
					PurchasedTripSeat pts = itpts.next();
					ScheduleStationSeatAvailability sssa = pts.getScheduleStationSeatAvailability();
					sssa.setSeatAvailabilityStatus(SeatAvailabilityStatus.CANCELED);
					sssa.setDeletedAt(new Date());
					swpService.updateRecord(sssa);
					
					pts.setDeletedAt(new Date());
					swpService.updateRecord(pts);
				}
				purchasedTrip.setPurchasedTripStatus(PurchasedTripStatus.ADMIN_CANCELED);
				purchasedTrip.setDeletedAt(new Date());
				swpService.updateRecord(purchasedTrip);
				counter++;
				refundIndex = purchasedTrip.getVehicleSchedule().getRefundIndex();

				oldTransaction = purchasedTrip.getTransaction();
				transactionAmount = transactionAmount + oldTransaction.getTransactionAmount() * ((refundIndex)/100);
			}
			
			if(counter==0)
			{
				jsonObject.add("status", ERROR.PURCHASED_TRIP_NOT_FOUND);
				jsonObject.add("message", "Purchased trips could not be found");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			
			hql = "Select tp from Transaction tp where tp.client.clientId = '"+client.getClientId()+"' AND tp.deletedAt IS NULL AND tp.refundedTransactionId = " + oldTransaction.getTransactionId();
			Transaction txn = (Transaction)swpService.getUniqueRecordByHQL(hql);
			if(txn==null)
			{
				String transactionRef = RandomStringUtils.randomNumeric(8);
				String newOrderRef = RandomStringUtils.randomNumeric(8);
				String messageRequest = deviceCode+"###"+transactionRef+"###"+token
						+"###"+ipAddress+"###"+clientCode;
				String messageResponse = "";
				Double fixedCharge = 0.00;
				Double transactionFee = 0.00;
				
				Transaction transaction = new Transaction();
				transaction.setTransactionRef(transactionRef);
				transaction.setOrderRef(device.getDeviceCode() + "-" + newOrderRef);
				transaction.setChannel(com.probase.reservationticketingwebservice.enumerations.Channel.WEB);
				transaction.setTransactionDate(new Date());
				transaction.setServiceType(ServiceType.CASH_REFUND_FOR_TRIP);
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
				transaction.setNarration("Refund|#" + transactionRef + "|ZMW" + transaction.getTransactionAmount());
				transaction.setDrCardPan(null);
				transaction.setDrCardId(null);
				transaction.setUpdatedAt(new Date());
				transaction.setCreatedAt(new Date());
				transaction.setPaymentMeans(PaymentMeans.CASH);
				transaction.setClient(client);						
				transaction.setTransactionCurrency("ZMW");
				transaction.setRefundedTransactionId(oldTransaction.getTransactionId());
				transaction = (Transaction)swpService.createNewRecord(transaction);
				
				
				jsonObject.add("status", ERROR.GENERAL_SUCCESS);
				jsonObject.add("message", "Purchased Trip(s) canceled");
				jsonObject.add("refundedPurchasedTrip", new Gson().toJson(purchasedTrips));
				jsonObject.add("refundedPurchasedTripTransaction", new Gson().toJson(transaction));
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			else
			{
				jsonObject.add("status", ERROR.PURCHASED_TRIP_ALREADY_REFUNDED);
				jsonObject.add("message", "Purchased Trip(s) already refunded");
				jsonObject.add("refundedPurchasedTrip", new Gson().toJson(purchasedTrips));
				jsonObject.add("refundedPurchasedTripTransaction", new Gson().toJson(txn));
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
	
	
	
	
	public Response compareTicketPrices(String deviceCode, String receiptNo, String passengerType, String newTravelClassCode, String requestId, String ipAddress, String clientCode, String token) 
	{
		JsonObjectBuilder jsonObject = Json.createObjectBuilder();
    	
		try{
			if(deviceCode==null ||  receiptNo==null || deviceCode==null || passengerType==null || newTravelClassCode==null)
			{
				jsonObject.add("status", ERROR.INCOMPLETE_PARAMETERS);
				jsonObject.add("message", "Incomplete parameters provided. Provide deviceCode, receiptNo, newTravelClassCode, passengerType parameters");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			//log.info">>>..." + deviceCode);
			//log.info">>>..." + receiptNo); 
			//log.info">>>..." + requestId);
			//log.info">>>..." + ipAddress);
			//log.info">>>..." + clientCode); 
			//log.info">>>..." + token);
			
			JSONObject tripTicketDataJSArray = null;
			//String transactionRef = null;
			
			if(token==null)
			{
				jsonObject.add("status", ERROR.INSUFFICIENT_PRIVILEDGES);
				jsonObject.add("message", "You do not have the priviledges to carry out this action");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			
			JSONObject jsObj = new JSONObject();
			

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
			Boolean skip = Boolean.FALSE;
			RoleType roleCode = null;
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
					
					//roleCode = user.getRoleCode();
				}
			}
			
			if(!roleCode.equals(RoleType.ADMIN_STAFF) && !roleCode.equals(RoleType.OPERATOR))
			{
				//log.info"5");
				jsonObject.add("status", ERROR.INSUFFICIENT_PRIVILEDGES);
				jsonObject.add("message", "You do not have the priviledges to carry out this action");
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


			hql = "Select tp from PurchasedTrip tp where tp.receiptNo = '"+receiptNo+"' AND tp.purchasedTripStatus = " + PurchasedTripStatus.PENDING.ordinal() + " AND " +
					"tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL";
			//log.infohql);
			PurchasedTrip purchasedTrip = (PurchasedTrip)swpService.getUniqueRecordByHQL(hql);
			if(purchasedTrip!=null)
			{
			}
			else
			{
				jsonObject.add("status", ERROR.PURCHASED_TRIP_NOT_FOUND);
				jsonObject.add("message", "Purchased trip could not be found");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
				
			}
			
			VehicleSeatClass oldTripClass = null;
			hql = "Select tp from PurchasedTripSeat tp where tp.purchasedTrip.purchasedTripId = "+ purchasedTrip.getPurchasedTripId() +" AND tp.deletedAt IS NULL";
			//log.infohql);
			Collection<PurchasedTripSeat> purchasedTripSeats = (Collection<PurchasedTripSeat>)swpService.getAllRecordsByHQL(hql);
			if(purchasedTripSeats!=null && purchasedTripSeats.size()>0)
			{
				Iterator ptsIter = purchasedTripSeats.iterator();
				PurchasedTripSeat purchasedTripSeat = (PurchasedTripSeat)ptsIter.next();
				oldTripClass = purchasedTripSeat.getScheduleStationSeatAvailability().getScheduleStationSeatSection().getVehicleSeatSection().getVehicleSeatClass();
			}
			else
			{
				jsonObject.add("status", ERROR.PURCHASED_TRIP_NOT_FOUND);
				jsonObject.add("message", "Purchased trip seats could not be found");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
				
			}							
			
			String departureStationCode = purchasedTrip.getDepartureStation().getStationCode();
			String arrivalStationCode = purchasedTrip.getArrivalStation().getStationCode();
			PassengerType ptype = null;
			try
			{
				ptype = PassengerType.valueOf(passengerType);
			}
			catch(IllegalArgumentException | NullPointerException e)
			{
				jsonObject.add("status", ERROR.INCOMPLETE_PARAMETERS);
				jsonObject.add("message", "Invalid passenger type provided");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			boolean proceed = false;
			PriceType priceType = null;
			JSONObject seatDetails = new JSONObject();
			if(ptype.equals(PassengerType.ADULT_PASSENGER))
			{
				if(purchasedTrip.getAdultPassengers()>=1)
				{
					priceType = PriceType.FOR_ADULT_PRICE;
					proceed = true;
				}
			}
			else if(ptype.equals(PassengerType.CHILD_PASSENGER))
			{
				if(purchasedTrip.getChildPassengers()>=1)
				{
					priceType = PriceType.FOR_CHILD_PRICE;
					proceed = true;
				}
			}
			else if(ptype.equals(PassengerType.SENIOR_PASSENGER))
			{
				if(purchasedTrip.getSeniorPassengers()>=1)
				{
					priceType = PriceType.FOR_SENIOR_CITIZENS;
					proceed = true;
				}
			}
			else if(ptype.equals(PassengerType.SPECIAL_NEEDS_PASSENGER))
			{
				if(purchasedTrip.getDisabledPassengers()>=1)
				{
					priceType = PriceType.FOR_SPECIALLY_ABLED;
					proceed = true;
				}
			}
			
			if(proceed==false)
			{
				jsonObject.add("status", ERROR.INCOMPLETE_PARAMETERS);
				jsonObject.add("message", "Number of passenger(s) to be upgraded exceeds the passenger on the ticket");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			String daSql = "Select tp from TripZoneStation tp where tp.station.stationCode = '"+arrivalStationCode+"' AND " +
					"tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL";
			//log.infodaSql);
			TripZoneStation arrivalTripZoneStation = (TripZoneStation)swpService.getUniqueRecordByHQL(daSql);
			
			daSql = "Select tp from TripZoneStation tp where tp.station.stationCode = '"+departureStationCode+"' AND " +
					"tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL";
			TripZoneStation depatureTripZoneStation = (TripZoneStation)swpService.getUniqueRecordByHQL(daSql);
			//log.info"1 .... >>>>>" + arrivalTripZoneStation.getTripZone().getRouteOrder());
			//log.info"1 .... >>>>>" + depatureTripZoneStation.getTripZone().getRouteOrder());
			
			Integer tripRouteOrderdifference =  depatureTripZoneStation.getTripZone().getRouteOrder() - arrivalTripZoneStation.getTripZone().getRouteOrder();
			tripRouteOrderdifference = Math.abs(tripRouteOrderdifference);
			tripRouteOrderdifference = tripRouteOrderdifference + 1;
			
			
			daSql = "Select tp from VehicleTripPrice tp where tp.finalDestinationTripZone.routeOrder = " + tripRouteOrderdifference + 
					" AND tp.deletedAt is NULL and tp.client.clientCode = '"+clientCode+"' " + 
					" AND tp.priceType = " + priceType.ordinal() + 
					" AND tp.vehicleSeatClass.id = " + oldTripClass.getVehicleSeatClassId();
			//log.info"daSQL ===> " + daSql);
			VehicleTripPrice oldVehicleTripPrice = (VehicleTripPrice)swpService.getUniqueRecordByHQL(daSql);
			
			hql = "Select tp from VehicleSeatClass tp where tp.vehicleSeatClassCode = '"+newTravelClassCode+"' AND tp.deletedAt IS NULL";
			VehicleSeatClass newVehicleSeatClass = (VehicleSeatClass)swpService.getUniqueRecordByHQL(hql);
			daSql = "Select tp from VehicleTripPrice tp where tp.finalDestinationTripZone.routeOrder = " + tripRouteOrderdifference + 
					" AND tp.deletedAt is NULL and tp.client.clientCode = '"+clientCode+"' " + 
					" AND tp.priceType = " + priceType.ordinal() + 
					" AND tp.vehicleSeatClass.id = " + newVehicleSeatClass.getVehicleSeatClassId();
			//log.info"daSQL ===> " + daSql);
			VehicleTripPrice newVehicleTripPrice = (VehicleTripPrice)swpService.getUniqueRecordByHQL(daSql);
			
			
			jsonObject.add("status", ERROR.GENERAL_SUCCESS);
			jsonObject.add("message", "You do not have the priviledges to carry out this action");
			jsonObject.add("oldPrice", oldVehicleTripPrice.getAmount());
			jsonObject.add("newPrice", newVehicleTripPrice.getAmount());
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
	
	
	
	
	public Response upgradePurchasedTicket(String transactionRef, String deviceCode, String paymentType, String purchasePoint, String receiptNo, String passengerType, String newTravelClassCode, String requestId, String ipAddress, String clientCode, String token, 
			String firstName, String lastName, String mobileNumber, String nationalId, String email) {
		// TODO Auto-generated method stub
		JsonObjectBuilder jsonObject = Json.createObjectBuilder();
    	
		try{
			if(deviceCode==null ||  receiptNo==null || deviceCode==null || passengerType==null || newTravelClassCode==null)
			{
				jsonObject.add("status", ERROR.INCOMPLETE_PARAMETERS);
				jsonObject.add("message", "Incomplete parameters provided. Provide deviceCode, receiptNo, newTravelClassCode, passengerType parameters");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			//log.info">>>..." + deviceCode);
			//log.info">>>..." + receiptNo); 
			//log.info">>>..." + requestId);
			//log.info">>>..." + ipAddress);
			//log.info">>>..." + clientCode); 
			//log.info">>>..." + token);
			
			JSONObject tripTicketDataJSArray = null;
			//String transactionRef = null;
			
			if(token==null)
			{
				jsonObject.add("status", ERROR.INSUFFICIENT_PRIVILEDGES);
				jsonObject.add("message", "You do not have the priviledges to carry out this action");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			
			JSONObject jsObj = new JSONObject();
			

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
			Boolean skip = Boolean.FALSE;
			RoleType roleCode = null;
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
					
					//roleCode = user.getRoleCode();
				}
			}
			
			if(!roleCode.equals(RoleType.ADMIN_STAFF) && !roleCode.equals(RoleType.OPERATOR))
			{
				//log.info"5");
				jsonObject.add("status", ERROR.INSUFFICIENT_PRIVILEDGES);
				jsonObject.add("message", "You do not have the priviledges to carry out this action");
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


			hql = "Select tp from PurchasedTrip tp where tp.receiptNo = '"+receiptNo+"' AND tp.purchasedTripStatus = " + PurchasedTripStatus.PENDING.ordinal() + " AND " +
					"tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL";
			System.out.println("hql --" + hql);
			PurchasedTrip purchasedTrip = (PurchasedTrip)swpService.getUniqueRecordByHQL(hql);
			if(purchasedTrip!=null)
			{
			}
			else
			{
				jsonObject.add("status", ERROR.PURCHASED_TRIP_NOT_FOUND);
				jsonObject.add("message", "Purchased trip could not be found");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
				
			}
			
			VehicleSeatClass oldTripClass = null;
			hql = "Select tp from PurchasedTripSeat tp where tp.purchasedTrip.purchasedTripId = "+ purchasedTrip.getPurchasedTripId() +" AND tp.deletedAt IS NULL";
			Collection<PurchasedTripSeat> purchasedTripSeats = (Collection<PurchasedTripSeat>)swpService.getAllRecordsByHQL(hql);
			if(purchasedTripSeats!=null && purchasedTripSeats.size()>0)
			{
				Iterator ptsIter = purchasedTripSeats.iterator();
				PurchasedTripSeat purchasedTripSeat = (PurchasedTripSeat)ptsIter.next();
				oldTripClass = purchasedTripSeat.getScheduleStationSeatAvailability().getScheduleStationSeatSection().getVehicleSeatSection().getVehicleSeatClass();
			}
			else
			{
				jsonObject.add("status", ERROR.PURCHASED_TRIP_NOT_FOUND);
				jsonObject.add("message", "Purchased trip seats could not be found");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
				
			}

			
			PassengerType ptype = null;
			try
			{
				ptype = PassengerType.valueOf(passengerType);
			}
			catch(IllegalArgumentException | NullPointerException e)
			{
				jsonObject.add("status", ERROR.INCOMPLETE_PARAMETERS);
				jsonObject.add("message", "Invalid passenger type provided");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			boolean proceed = false;
			PriceType priceType = null;
			JSONObject seatDetails = new JSONObject();
			
			if(ptype.equals(PassengerType.ADULT_PASSENGER))
			{
				if(purchasedTrip.getAdultPassengers()>=1)
				{
					priceType = PriceType.FOR_ADULT_PRICE;
					proceed = true;
					JSONObject seatDetail = new JSONObject();
					seatDetail.put("seatClass", newTravelClassCode);
					seatDetail.put("seatLocation", VehicleSeatLocation.WINDOW.name());
					JSONArray js_ar = new JSONArray();
					js_ar.put(seatDetail);
					seatDetails.put("ADULT", js_ar);
				}
			}
			else if(ptype.equals(PassengerType.CHILD_PASSENGER))
			{
				if(purchasedTrip.getChildPassengers()>=1)
				{
					priceType = PriceType.FOR_CHILD_PRICE;
					proceed = true;
					JSONObject seatDetail = new JSONObject();
					seatDetail.put("seatClass", newTravelClassCode);
					seatDetail.put("seatLocation", VehicleSeatLocation.WINDOW.name());
					JSONArray js_ar = new JSONArray();
					js_ar.put(seatDetail);
					seatDetails.put("CHILD", js_ar);
				}
			}
			else if(ptype.equals(PassengerType.SENIOR_PASSENGER))
			{
				if(purchasedTrip.getSeniorPassengers()>=1)
				{
					priceType = PriceType.FOR_SENIOR_CITIZENS;
					proceed = true;
					JSONObject seatDetail = new JSONObject();
					seatDetail.put("seatClass", newTravelClassCode);
					seatDetail.put("seatLocation", VehicleSeatLocation.WINDOW.name());
					JSONArray js_ar = new JSONArray();
					js_ar.put(seatDetail);
					seatDetails.put("SENIOR", js_ar);
				}
			}
			else if(ptype.equals(PassengerType.SPECIAL_NEEDS_PASSENGER))
			{
				if(purchasedTrip.getDisabledPassengers()>=1)
				{
					priceType = PriceType.FOR_SPECIALLY_ABLED;
					proceed = true;
					JSONObject seatDetail = new JSONObject();
					seatDetail.put("seatClass", newTravelClassCode);
					seatDetail.put("seatLocation", VehicleSeatLocation.WINDOW.name());
					JSONArray js_ar = new JSONArray();
					js_ar.put(seatDetail);
					seatDetails.put("DISABLED", js_ar);
				}
			}
			
			if(proceed==false)
			{
				jsonObject.add("status", ERROR.INCOMPLETE_PARAMETERS);
				jsonObject.add("message", "Number of passenger(s) to be upgraded exceeds the passenger on the ticket");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
				
			Double transactionAmount = 0.00;
			Double refundIndex = 0.00;
			
			int counter = 0;
			Transaction oldTransaction = null;
				
			
			String departureStationCode = purchasedTrip.getDepartureStation().getStationCode();
			String arrivalStationCode = purchasedTrip.getArrivalStation().getStationCode();
			Integer newPassengerCount = 1;
			String departureDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(purchasedTrip.getDepartureTime());
			String tripClassCode = newTravelClassCode;
			String passengerDetails = seatDetails.toString();
			Response searchTripResponse = this.searchAvailableTrips(deviceCode, token, departureStationCode, arrivalStationCode, passengerDetails, departureDate, null, 7*24, tripClassCode, 
					Boolean.FALSE, requestId, ipAddress, clientCode);
			String searchTripResponseStr = (String)searchTripResponse.getEntity();
			System.out.println("search trip response = " + searchTripResponseStr);
			if(searchTripResponseStr!=null)
			{
				JSONObject searchTripResponseJSObj = new JSONObject(searchTripResponseStr);
				if(searchTripResponseJSObj.getInt("status")==0)
				{
					if(searchTripResponseJSObj.has("outwardTripStatus") && searchTripResponseJSObj.getInt("outwardTripStatus")==0)
					{
						JSONObject outwardVehicleTripList = new JSONObject(searchTripResponseJSObj.getString("outwardVehicleTripList"));
						Boolean outwardTripSeatsAvailabilityStatus = searchTripResponseJSObj.getBoolean("outwardTripSeatsAvailabilityStatus");
						JSONArray outwardTicketPrices = new JSONArray(searchTripResponseJSObj.getString("outwardTicketPrices"));
						//String ticketPurchaseExpiryDate = searchTripResponseJSObj.getString("ticketPurchaseExpiryDate");
						
						
						
						if(outwardVehicleTripList!=null && outwardTripSeatsAvailabilityStatus!=null && outwardTripSeatsAvailabilityStatus.equals(Boolean.TRUE))
						{
							
							JSONObject outwardFormParam = new JSONObject();
							outwardFormParam.put("tripCode", purchasedTrip.getVehicleSchedule().getScheduleStationCode().getScheduleStationCode());
							outwardFormParam.put("departingStationCode", purchasedTrip.getDepartureStation().getStationCode());
							outwardFormParam.put("arrivalStationCode", purchasedTrip.getArrivalStation().getStationCode());
							outwardFormParam.put("seatDetails", seatDetails);
							outwardFormParam.put("clientCode", clientCode);
							outwardFormParam.put("forcePreferences", false);
							outwardFormParam.put("generalTripClass", newTravelClassCode);
							JSONObject formParam = new JSONObject();
							formParam.put("outwardTrip", outwardFormParam.toString());	
							formParam.put("lockdownSeats", true);
							formParam.put("clientCode", clientCode);
							formParam.put("deviceCode", deviceCode);

							Response lockedDownResp = this.getTripSeatAndLockDown(deviceCode, Boolean.TRUE, null, outwardFormParam.toString(), null, requestId, ipAddress, clientCode, null);
							String lockedDownRespStr = null;
							JSONObject lockedDownRespJS_ = null;
							lockedDownRespStr = (String)lockedDownResp.getEntity();
							System.out.println("lockedDownRespStr..." + lockedDownRespStr);
							lockedDownRespJS_ = new JSONObject(lockedDownRespStr);
							String outwardTripDetails = lockedDownRespJS_.getString("outwardTripDetails");
							JSONObject lockedDownRespJS = new JSONObject(outwardTripDetails);
							
							Double baseAdultFare = 0.00;
							Double baseChildFare = 0.00;
							Double baseSeniorFare = 0.00;
							Double baseDisabledFare = 0.00;
							Double totalFare = 0.00;
							
							if(lockedDownRespJS!=null && lockedDownRespJS.has("status") && lockedDownRespJS.getInt("status")==0)
							{
								baseAdultFare = lockedDownRespJS.has("baseAdultFare") ?  lockedDownRespJS.getDouble("baseAdultFare") : 0.00;
								baseChildFare = lockedDownRespJS.has("baseChildFare") ? lockedDownRespJS.getDouble("baseChildFare") : 0.00;
								baseSeniorFare = lockedDownRespJS.has("baseSeniorFare") ? lockedDownRespJS.getDouble("baseSeniorFare") : 0.00;
								baseDisabledFare = lockedDownRespJS.has("baseDisabledFare") ? lockedDownRespJS.getDouble("baseDisabledFare") : 0.00;
								String details = lockedDownRespJS.getString("details");
								String tripBookingToken = lockedDownRespJS.getString("tripBookingToken");
								JSONObject detailsJS = new JSONObject(details);
								String tripDetails = detailsJS.getString("tripDetails");
								JSONObject seatsAlloted = detailsJS.getJSONObject("seatsAlloted");
								totalFare = totalFare + baseAdultFare + baseChildFare + baseSeniorFare + baseDisabledFare;
								
								
								hql = "Select tp from Customer tp where tp.isLeadPassenger = 1 " + " AND tp.purchasedTrip.id = " + purchasedTrip.getPurchasedTripId() + " AND tp.deletedAt IS NULL"; 
								Customer customer = (Customer)swpService.getUniqueRecordByHQL(hql);
								JSONObject passenger_details = new JSONObject();
								if(customer!=null)
								{
									JSONObject passenger_detail = new JSONObject();
									passenger_detail.put("firstname", customer.getFirstName());
									passenger_detail.put("lastname", customer.getLastName());
									passenger_detail.put("emailaddress", customer.getEmailAddress()==null ? "" : customer.getEmailAddress());
									passenger_detail.put("mobilenumber", customer.getMobileNumber()==null ? "" : customer.getMobileNumber());
									passenger_detail.put("nationalid", customer.getVerificationNumber()==null ? "" : customer.getVerificationNumber());
									passenger_details.put("leadPassenger", passenger_detail);
								}
								else
								{
									JSONObject passenger_detail = new JSONObject();
									passenger_detail.put("firstname", firstName);
									passenger_detail.put("lastname", lastName);
									passenger_detail.put("emailaddress", email);
									passenger_detail.put("mobilenumber", mobileNumber);
									passenger_detail.put("nationalid", nationalId);
									passenger_details.put("leadPassenger", passenger_detail);
								}
								JSONObject entry = new JSONObject();
								entry.put("tripCode", purchasedTrip.getVehicleSchedule().getScheduleStationCode().getScheduleStationCode());
								entry.put("passengerdetails", passenger_details);
								entry.put("adultPassenger", ptype.equals(PassengerType.ADULT_PASSENGER) ? 1 : 0);
								entry.put("childPassenger", ptype.equals(PassengerType.CHILD_PASSENGER) ? 1 : 0);
								entry.put("seniorPassenger", ptype.equals(PassengerType.SENIOR_PASSENGER) ? 1 : 0);
								entry.put("disabledPassenger", ptype.equals(PassengerType.SPECIAL_NEEDS_PASSENGER) ? 1 : 0);
								entry.put("departingFrom", purchasedTrip.getDepartureStation().getStationName());
								entry.put("arrivingAt", purchasedTrip.getArrivalStation().getStationName());
								entry.put("departureTime", new SimpleDateFormat("yyyy-MM-dd HH:mm").format(purchasedTrip.getDepartureTime()));
								entry.put("arrivalTime", new SimpleDateFormat("yyyy-MM-dd HH:mm").format(purchasedTrip.getArrivalTime()));
								entry.put("departingStation", purchasedTrip.getDepartureStation().getStationCode());
								entry.put("arrivingStation", purchasedTrip.getArrivalStation().getStationCode());
								entry.put("adultTicketPrice", baseAdultFare);
								entry.put("childTicketPrice", baseChildFare);
								entry.put("seniorTicketPrice", baseSeniorFare);
								entry.put("disabledTicketPrice", baseDisabledFare);
								entry.put("tripDetails", tripDetails);
								entry.put("seatsAlloted", seatsAlloted);
								JSONObject entry_ = new JSONObject();
								entry_.put(purchasedTrip.getVehicleSchedule().getScheduleStationCode().getScheduleStationCode(), entry);
								JSONObject outwardTripEntry = new JSONObject();
								outwardTripEntry.put("outwardTrip", entry_);
								
								String toHash = "";
								String orderId = RandomStringUtils.randomNumeric(8) + "0";
								if(paymentType.equals(PaymentMeans.CARD.name()))
								{
									transactionRef = RandomStringUtils.randomNumeric(10);
									toHash = deviceCode + "" + clientCode + "" + orderId + (new DecimalFormat().format(totalFare)) + "" + device.getTerminalApiKey();
								}
								else if(paymentType.equals(PaymentMeans.CASH.name()))
								{
									transactionRef = RandomStringUtils.randomNumeric(10);
									toHash = clientCode + "" + deviceCode + "" + orderId + (new DecimalFormat().format(totalFare)) + "" + user.getUsername();
								}
								else if(paymentType.equals(PaymentMeans.PROBASEPAY.name()))
								{
									toHash = transactionRef + "" + Application.probasePayApiKey + "" + Application.probasePayMerchantKey;
								}
								else if(paymentType.equals(PaymentMeans.VENDOR_WALLET.name()))
								{
									transactionRef = RandomStringUtils.randomNumeric(10);
									toHash = clientCode + "" + deviceCode + "" + orderId + (new DecimalFormat().format(totalFare)) + "" + user.getWebActivationCode();
								}
								
								
								//String hash = UtilityHelper.hash_hmac(toHash, client.getPrivateKey());
								String hash = BCrypt.hashpw(toHash, BCrypt.gensalt());
								System.out.println("hash....." + hash);
								
								JSONObject purchaseDetailsJS = new JSONObject();
								purchaseDetailsJS.put("tripTicketData", outwardTripEntry);
								purchaseDetailsJS.put("hash", hash);
								purchaseDetailsJS.put("orderId", orderId);
								
								String purchaseDetails = purchaseDetailsJS.toString();
								Response purchaseResp = purchaseVehicleTripTickets(purchasePoint, null, null, paymentType, orderId, deviceCode, 
										"50932023", tripBookingToken, purchaseDetails, newTravelClassCode, requestId, ipAddress, clientCode, token);
								String purchaseRespStr = purchaseResp.getEntity().toString();
								System.out.println("purchaseRespStr..." + purchaseRespStr);
								
								JSONObject purchaseRespJS = new JSONObject(purchaseRespStr);
								if(purchaseRespJS!=null && purchaseRespJS.has("status") && purchaseRespJS.getInt("status")==ERROR.GENERAL_SUCCESS)
								{
									String receiptNos = purchaseRespJS.getString("receiptNos");
									JSONArray receiptNos_ = new JSONArray(receiptNos);
									String newReceiptNo = receiptNos_.getString(0);
									Response compareResp = this.compareTicketPrices(deviceCode, receiptNo, ptype.name(), newTravelClassCode, requestId, ipAddress, clientCode, token);
									String compareRespStr = compareResp.getEntity().toString();
									//log.info"compareRespStr..." + compareRespStr);
									JSONObject compareRespJS = new JSONObject(compareRespStr);
									Double oldAmount = compareRespJS.getDouble("oldPrice");
									
									
									
									if(ptype.equals(PassengerType.ADULT_PASSENGER))
										purchasedTrip.setTotalUpgradedAdultPassengers((purchasedTrip.getTotalUpgradedAdultPassengers()==null ? 0 : purchasedTrip.getTotalUpgradedAdultPassengers()) + 1);
									else if(ptype.equals(PassengerType.CHILD_PASSENGER))
										purchasedTrip.setTotalUpgradedChildPassengers((purchasedTrip.getTotalUpgradedChildPassengers()==null ? 0 : purchasedTrip.getTotalUpgradedChildPassengers()) + 1);
									else if(ptype.equals(PassengerType.SENIOR_PASSENGER))
										purchasedTrip.setTotalUpgradedSeniorPassengers((purchasedTrip.getTotalUpgradedSeniorPassengers()==null ? 0 : purchasedTrip.getTotalUpgradedSeniorPassengers()) + 1);
									else if(ptype.equals(PassengerType.SPECIAL_NEEDS_PASSENGER))
										purchasedTrip.setTotalUpgradedDisabledPassengers((purchasedTrip.getTotalUpgradedDisabledPassengers()==null ? 0 : purchasedTrip.getTotalUpgradedDisabledPassengers()) + 1);
									purchasedTrip.setPassengerCount(purchasedTrip.getPassengerCount() - 1);
									if(ptype.equals(PassengerType.ADULT_PASSENGER))
									{
										purchasedTrip.setAdultPassengers(purchasedTrip.getAdultPassengers() - 1);
									}
									else if(ptype.equals(PassengerType.CHILD_PASSENGER))
									{
										purchasedTrip.setChildPassengers(purchasedTrip.getChildPassengers() - 1);
									}
									else if(ptype.equals(PassengerType.SENIOR_PASSENGER))
									{
										purchasedTrip.setSeniorPassengers(purchasedTrip.getSeniorPassengers() - 1);
									}
									else if(ptype.equals(PassengerType.SPECIAL_NEEDS_PASSENGER))
									{
										purchasedTrip.setDisabledPassengers(purchasedTrip.getDisabledPassengers() - 1);
									}
									
									purchasedTrip.setAmountPayable(purchasedTrip.getAmountPayable() - oldAmount);
									purchasedTrip.setTotalUpgradedAmount((purchasedTrip.getTotalUpgradedAmount()==null ? 0 : purchasedTrip.getTotalUpgradedAmount()) + (totalFare - oldAmount));
									swpService.updateRecord(purchasedTrip);
									
									
									
									hql = "Select tp from PurchasedTrip tp where tp.receiptNo = '"+newReceiptNo+"' AND tp.purchasedTripStatus = " + PurchasedTripStatus.PENDING.ordinal() + " AND " +
											"tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL";
									System.out.println("hql --" + hql);
									PurchasedTrip newPurchasedTrip = (PurchasedTrip)swpService.getUniqueRecordByHQL(hql);
									if(totalFare>oldAmount)
										newPurchasedTrip.setIsUpgradeAmountRefund(Boolean.FALSE);
									else if(totalFare<oldAmount)
										newPurchasedTrip.setIsUpgradeAmountRefund(Boolean.TRUE);
									

									Transaction newTransaction = newPurchasedTrip.getTransaction();
									newTransaction = purchasedTrip.getTransaction();
									if(totalFare>oldAmount)
										newTransaction.setIsUpgradeAmountRefund(Boolean.FALSE);
									else if(totalFare<oldAmount)
										newTransaction.setIsUpgradeAmountRefund(Boolean.TRUE);
									swpService.updateRecord(newTransaction);
									
									UpgradedPurchasedTrip upgradedPurchasedTrip = new UpgradedPurchasedTrip();
									upgradedPurchasedTrip.setClient(client);
									upgradedPurchasedTrip.setCreatedAt(new Date());
									upgradedPurchasedTrip.setUpdatedAt(new Date());
									upgradedPurchasedTrip.setNewPurchasedTrip(newPurchasedTrip);
									upgradedPurchasedTrip.setOldPurchasedTrip(purchasedTrip);
									upgradedPurchasedTrip.setOldAmount(totalFare);
									upgradedPurchasedTrip.setNewAmount(newPurchasedTrip.getAmountPayable());
									upgradedPurchasedTrip = (UpgradedPurchasedTrip)swpService.createNewRecord(upgradedPurchasedTrip);
									
									purchaseRespJS.put("oldReceiptNo", purchasedTrip.getReceiptNo());
									purchaseRespJS.put("newReceiptNo", newPurchasedTrip.getReceiptNo());
									purchaseRespJS.put("amountToPay", Math.abs(newPurchasedTrip.getAmountPayable() - totalFare));
									if((newPurchasedTrip.getAmountPayable() - totalFare)>0)
										purchaseRespJS.put("refundAmount", false);
									else
										purchaseRespJS.put("refundAmount", true);

									//JsonObject jsonObj = jsonObject.build();
									return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( purchaseRespJS.toString() ).build();
								}
								else
								{
									return purchaseResp;
								}
								//entry.put("seatsAlloted", seatsAlloted);
										
								//outwardTripEntry[outwardTripCode] = entry);
								//tripTransaction.put("tripdetails'].put("outwardTrip", outwardTripEntry);
								//tripTransaction.put("stationCabinType", forwardedInput.put("stationCabinType']);
								
										
										
								//JSONObject purchaseDetails = new JSONObject();
								//purchaseDetails.put("tripTicketData", formData);
								//purchaseDetails.put("hash", hash);
								//purchaseDetails.put("orderId, params.put("orderId']);
												
								/*JSONObject allData = new JSONObject();
								allData.put("paymentType", paymentType);
								allData.put("deviceCode", deviceCode);
								allData.put("ticketCollectionCenterCode", "13950509");
								allData.put("purchaseDetails", purchaseDetails);
								allData.put("generalTripClass", newTravelClassCode);
								allData.put("clientCode", clientCode);
								allData.put("transactionRef", transactionRef);
								allData.put("purchasePoint", purchasePoint);*/
								
							}
							else
							{
								if(lockedDownRespJS==null)
								{
									jsonObject.add("status", ERROR.NO_TRIPS_FOUND_FOR_SPECIFIED_DATES);
									jsonObject.add("message", "No Available trips");
									JsonObject jsonObj = jsonObject.build();
									return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
								}
								else
								{
									return lockedDownResp;
								}
							}
							
							
						}					
						else
						{
							jsonObject.add("status", ERROR.NO_TRIPS_FOUND_FOR_SPECIFIED_DATES);
							jsonObject.add("message", "No Available trips");
							
							JsonObject jsonObj = jsonObject.build();
							return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
						}
					}
					else
					{
						jsonObject.add("status", ERROR.NO_TRIPS_FOUND_FOR_SPECIFIED_DATES);
						jsonObject.add("message", "No Available trips");
						
						JsonObject jsonObj = jsonObject.build();
						return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
					}
				}
				else
				{
					jsonObject.add("status", ERROR.NO_TRIPS_FOUND_FOR_SPECIFIED_DATES);
					jsonObject.add("message", "No Available trips");
					
					JsonObject jsonObj = jsonObject.build();
					return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
				}
			}
			else
			{
				jsonObject.add("status", ERROR.NO_TRIPS_FOUND_FOR_SPECIFIED_DATES);
				jsonObject.add("message", "No Available trips");
				
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
	
	
	
	
	
	public Response submitGroupTripRequest(String deviceCode, String departingStation, 
			String arrivingStation, String stationCabinType, String departureTime, String returnDate, String tripType, String details, String firstName, 
			String lastName, String emailAddress, String mobileNumber, String altmobileNumber, String nationalId, Integer adultPassengerCount, 
			Integer childPassengerCount, Integer seniorPassengerCount, Integer disabledPassengerCount,
			String requestId, String ipAddress, String clientCode, String token)
	{
		JsonObjectBuilder jsonObject = Json.createObjectBuilder();
    	
		try{
			
			//log.info">>>>>" + deviceCode); 
			//log.info">>>>>" + departingStation); 
			//log.info">>>>>" + arrivingStation); 
			//log.info">>>>>" + stationCabinType); 
			//log.info">>>>>" + departureTime); 
			//log.info">>>>>" + returnDate); 
			//log.info">>>>>" + tripType); 
			//log.info">>>>>" + details); 
			//log.info">>>>>" + firstName); 
			//log.info">>>>>" + lastName); 
			//log.info">>>>>" + emailAddress); 
			//log.info">>>>>" + mobileNumber);
			//log.info">>>>>" + altmobileNumber); 
			//log.info">>>>>" + nationalId); 
			//log.info">>>>>" + adultPassengerCount); 
			//log.info">>>>>" + seniorPassengerCount); 
			//log.info">>>>>" + childPassengerCount); 
			//log.info">>>>>" + disabledPassengerCount); 
			//log.info">>>>>" + requestId); 
			//log.info">>>>>" + ipAddress); 
			//log.info">>>>>" + clientCode); 
			//log.info">>>>>" + token);
			
			if(deviceCode==null ||  departingStation==null || arrivingStation==null ||  stationCabinType==null || departureTime==null || 
					tripType==null ||  details==null || firstName==null ||  lastName==null || emailAddress==null || mobileNumber==null ||  
					nationalId==null ||  adultPassengerCount==null || seniorPassengerCount==null || childPassengerCount==null || disabledPassengerCount==null || 
					clientCode==null)
			{
				jsonObject.add("status", ERROR.INCOMPLETE_PARAMETERS);
				jsonObject.add("message", "Incomplete parameters provided. Provide purchaseToken, orderRef, deviceCode parameters");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			JSONObject tripTicketDataJSArray = null;
			//String transactionRef = null;
		
			
			
			JSONObject jsObj = new JSONObject();
			

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
			Boolean skip = Boolean.FALSE;
			RoleType roleCode = null;
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
					
					//roleCode = user.getRoleCode();
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



			hql = "Select tp from Station tp where tp.stationCode = '"+departingStation+"' AND tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL";
			Station depStation = (Station)swpService.getUniqueRecordByHQL(hql);
			if(depStation==null)
			{
				jsonObject.add("status", ERROR.STATION_NOT_FOUND);
				jsonObject.add("message", "Departure station not found");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
				
			}
			
			hql = "Select tp from Station tp where tp.stationCode = '"+arrivingStation +"' AND tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL";
			Station arrStation = (Station)swpService.getUniqueRecordByHQL(hql);
			if(depStation==null)
			{
				jsonObject.add("status", ERROR.STATION_NOT_FOUND);
				jsonObject.add("message", "Destination station not found");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}

			hql = "Select tp from VehicleSeatClass tp where tp.vehicleSeatClassCode = '"+stationCabinType +"' AND tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL";
			VehicleSeatClass vehicleSeatClass = (VehicleSeatClass)swpService.getUniqueRecordByHQL(hql);
			if(depStation==null)
			{
				jsonObject.add("status", ERROR.STATION_NOT_FOUND);
				jsonObject.add("message", "Destination station not found");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			Date retDate = null;
			Date depDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(departureTime);
			if(tripType.equals(TripType.RETURN.name()))
			{
				retDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(returnDate);
			}
			
			TripType tt = null;
			try
			{
				tt = TripType.valueOf(tripType);
			}
			catch(IllegalArgumentException e)
			{
				jsonObject.add("status", ERROR.INVALID_TRIP_TYPE);
				jsonObject.add("message", "Provide a valid trip type");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			GroupTripRequest gtr = new GroupTripRequest();
			gtr.setAltMobileNumber(altmobileNumber);
			gtr.setMobileNumber(mobileNumber);
			gtr.setArrivingStation(arrStation);
			gtr.setClient(client);
			gtr.setCreatedAt(new Date());
			gtr.setDepartingStation(depStation);
			gtr.setDepartureDate(depDate);
			if(returnDate!=null && tt.equals(TripType.RETURN))
				gtr.setReturnDate(retDate);
			gtr.setDetails(details);
			gtr.setEmailAddress(emailAddress);
			gtr.setFirstName(firstName);
			gtr.setGroupTripRequestStatus(GroupTripRequestStatus.PENDING);
			gtr.setLastName(lastName);
			gtr.setNationalId(nationalId);
			gtr.setAdultPassengerCount(adultPassengerCount);
			gtr.setChildPassengerCount(childPassengerCount);
			gtr.setSeniorPassengerCount(seniorPassengerCount);
			gtr.setDisabledPassengerCount(disabledPassengerCount);
			gtr.setRequestByUser(user);
			gtr.setStationCabinType(vehicleSeatClass);
			gtr.setTripType(tt);
			gtr.setUpdatedAt(new Date());
			gtr.setRequestCode(RandomStringUtils.randomNumeric(12));
			gtr = (GroupTripRequest)swpService.createNewRecord(gtr);
				
			jsonObject.add("status", ERROR.GENERAL_SUCCESS);
			jsonObject.add("message", "Your new group trip request has been created");
			jsonObject.add("groupTripRequest", new Gson().toJson(gtr));
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
	
	
	
	public Response cancelVehicleTripSeatsLocked(String deviceCode, String purchaseToken, 
			String purchaseDetails, String generalTripClass, String requestId, String ipAddress, String clientCode, String token) {
		// TODO Auto-generated method stub
		JsonObjectBuilder jsonObject = Json.createObjectBuilder();
    	
		try{
			if(deviceCode==null || purchaseToken==null || purchaseDetails==null || generalTripClass==null || deviceCode==null)
			{
				jsonObject.add("status", ERROR.INCOMPLETE_PARAMETERS);
				jsonObject.add("message", "Incomplete parameters provided. Provide purchaseToken, transactionRef, deviceCode, purchaseDetails, paymentType, generalTripClass parameters");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			//log.info">>>..." + deviceCode);
			//log.info">>>..." + purchaseToken);
			//log.info">>>..." + purchaseDetails); 
			//log.info">>>..." + generalTripClass); 
			//log.info">>>..." + requestId);
			//log.info">>>..." + ipAddress);
			//log.info">>>..." + clientCode); 
			//log.info">>>..." + token);
			
			JSONObject tripTicketDataJSArray = null;
			//String transactionRef = null;
			
			if(token==null)
			{
				jsonObject.add("status", ERROR.INCOMPLETE_PARAMETERS);
				jsonObject.add("message", "As a vendor, a payee must be logged in to purchase tickets");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			
			JSONObject purchaseDetailsJS = new JSONObject(purchaseDetails);
			tripTicketDataJSArray = purchaseDetailsJS.getJSONObject("tripTicketData");
			String orderId = purchaseDetailsJS.has("orderId") ? purchaseDetailsJS.getString("orderId") : null;


			if(tripTicketDataJSArray==null )
			{
				jsonObject.add("status", ERROR.INCOMPLETE_PARAMETERS);
				jsonObject.add("message", "Incomplete parameters provided. Ensure you provide merchantCode, probasePayDeviceCode, transactionRef, and hash parameters in your tripTicketData parameter");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			JSONObject jsObj = new JSONObject();
			

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
			Boolean skip = Boolean.FALSE;
			Vendor vendor = null;
			String username = null;
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
				
				username = verifyJ.has("username") ? verifyJ.getString("username") : null;
				//log.inforequestId + "username ==" + (username==null ? "" : username));
				
				if(username!=null)
				{
					user = (User)this.swpService.getUniqueRecordByHQL("select tp from User tp where tp.username = '" + username + "' AND tp.deletedAt IS NULL AND " +
							"tp.userStatus = " + UserStatus.ACTIVE.ordinal());
					
					if(user!=null)
					{
					
						hql = "Select tp from Vendor tp where tp.vendorId = " + user.getVendor().getVendorId() + " AND tp.deletedAt IS NULL AND " +
								"tp.client.clientId = '"+user.getClient().getClientId()+"'";
						vendor = (Vendor)swpService.getUniqueRecordByHQL(hql);
					}
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



			JSONObject allTripSeatDetails = new JSONObject();
			List<Long> seatIds = new ArrayList<Long>();
			Iterator<String> outwardInwardIter = tripTicketDataJSArray.keys();
			while(outwardInwardIter.hasNext())
			{
				String outwardInwardStr = outwardInwardIter.next();
				JSONObject outwardTrips = tripTicketDataJSArray.getJSONObject(outwardInwardStr);
				JSONArray tripCodes = outwardTrips.names();
				for(int i=0; i<tripCodes.length(); i++)
				{
					String tripCode = tripCodes.getString(i);
					JSONObject tripEntry = outwardTrips.getJSONObject(tripCode);
					JSONObject seatsAlloted = tripEntry.getJSONObject("seatsAlloted");
					if(seatsAlloted==null)
					{
						skip = Boolean.TRUE;
					}
					else
					{
						Iterator<String> seatsAllotedIter = seatsAlloted.keys();
						while(seatsAllotedIter.hasNext())
						{
							String seatsAllotedIterStr = seatsAllotedIter.next();
							JSONArray seatsAllotedArray = seatsAlloted.getJSONArray(seatsAllotedIterStr);
							//log.info"seatsAllotedArray Str = " + seatsAllotedArray.toString());
							for(int j=0; j<seatsAllotedArray.length(); j++)
							{
								JSONObject jsSeatAlloted = (seatsAllotedArray.getJSONObject(j));
								//log.info"jsSeatAlloted = " + jsSeatAlloted.toString());
								seatIds.add(jsSeatAlloted.getLong("seatAvailabilityId"));
							}
						}
						
					}
				}
			}
			
			//log.info"...join == " + StringUtils.join(seatIds, ", "));
			
			if(skip.equals(Boolean.TRUE))
			{
				jsonObject.add("status", ERROR.INCOMPLETE_PARAMETERS_SEATS_NOT_ALLOTTED_COMPLETELY);
				jsonObject.add("message", "Seats purchased not specified completely");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			else
			{
				hql = "Select tp from ScheduleStationSeatAvailability tp where tp.schedSeatAvailId IN ("+StringUtils.join(seatIds, ", ")+") " +
						"AND tp.deletedAt is NULL AND tp.vehicleSeat.client.clientCode = '"+clientCode+"' AND tp.lockedDownBy = '"+purchaseToken+"' AND " +
						"tp.boughtByCustomer IS NULL AND tp.seatAvailabilityStatus = " + SeatAvailabilityStatus.OPEN.ordinal();
						
				Collection<ScheduleStationSeatAvailability> vsaList = (Collection<ScheduleStationSeatAvailability>)swpService.getAllRecordsByHQL(hql);
				Iterator<ScheduleStationSeatAvailability> vsaListIter = vsaList.iterator();
				int passengerCounter = 0;
				//log.info"hql --- " + hql);
				//log.info"vsalistier size -- " + vsaList.size());
				//log.info"seatIds size -- " + seatIds.size());
				JSONArray vehicleSeatsLockedDown = new JSONArray();
				while(vsaListIter.hasNext())
				{
					ScheduleStationSeatAvailability vsa = vsaListIter.next();
					vsa.setBoughtByCustomer(null);
					vsa.setSeatAvailabilityStatus(SeatAvailabilityStatus.OPEN);
					vsa.setUpdatedAt(new Date());
					vsa.setLockedDownBy(null);
					vsa.setLockedDownExpiryDate(null);
					swpService.updateRecord(vsa);
					
					AuditTrail ad = UtilityHelper.createAuditTrailEntry(ipAddress, RequestType.CANCEL_TRIP_SEATS_LOCKED, requestId, this.swpService, 
						username, vsa.getSchedSeatAvailId(), ScheduleStationSeatAvailability.class.getName(), 
						"Cancel Locked Seats. Seat Number: " + vsa.getVehicleSeat().getSeatNumber() + " | Canceled By " + user.getFirstName() + " " + user.getLastName(), clientCode);
				}

				jsonObject.add("status", ERROR.GENERAL_SUCCESS);
				jsonObject.add("message", "Trips canceled");
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
	
	
	
	
	public Response getVehicleSeatAndLockDown(Boolean lockdownSeats, String token, String tripCode, String departingStationCode, String arrivalStationCode, 
			String seatDetails, Boolean forcePreferences, String generalTripClass, String requestId, String ipAddress, String clientCode, String tripToken) {
		// TODO Auto-generated method stub
		JsonObjectBuilder jsonObject = Json.createObjectBuilder();
    	
		try{
			
			if(seatDetails==null || departingStationCode==null || tripCode==null || arrivalStationCode==null)
			{
				jsonObject.add("status", ERROR.INCOMPLETE_PARAMETERS);
				jsonObject.add("message", "Incomplete Parameters provided in request.");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			if(lockdownSeats==null)
			{
				lockdownSeats= Boolean.FALSE;
			}
			
			//log.info"numberOfSeats = " + seatDetails);
			
			
			
			swpService = serviceLocator.getSwpService();
			Application app = Application.getInstance(swpService);
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
			
			
			String hql = "Select tp from ScheduleStation tp where " + 
					"(tp.station.stationCode = '"+ departingStationCode +"') AND " + 
					"tp.scheduleStationCode.scheduleStationCode = '" + tripCode+"' AND tp.deletedAt is NULL AND " +
					"tp.arrivalTime IS NULL AND " + 
					"tp.client.clientCode = '"+clientCode+"'";
			ScheduleStation departureScheduleStation = (ScheduleStation)swpService.getUniqueRecordByHQL(hql);
			
			hql = "Select tp from ScheduleStation tp where " + 
					"(tp.station.stationCode = '"+arrivalStationCode+"') AND " + 
					"tp.departureTime IS NULL AND " + 
					"tp.scheduleStationCode.scheduleStationCode = '" + tripCode+"' AND tp.deletedAt is NULL AND tp.client.clientCode = '"+clientCode+"'";
			ScheduleStation arrivalScheduleStation = (ScheduleStation)swpService.getUniqueRecordByHQL(hql);
			if(departureScheduleStation==null || (arrivalScheduleStation==null))
			{
				jsonObject.add("status", ERROR.TRIP_NOT_FOUND);
				jsonObject.add("message", "The vehicle trip could not be found");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			
			JSONObject seatDetailsJsonObject = new JSONObject(seatDetails);
			JSONObject seatClassLocation = new JSONObject();
			JSONObject uniqueClasses = new JSONObject();
			//String generalTripClass = "";
			Integer adultPassenger = 0;
			Integer childPassenger = 0;
			Integer seniorPassenger = 0;
			Integer disabledPassenger = 0;
			JSONObject jsPassengerTypes = new JSONObject();
			jsPassengerTypes.put(PassengerType.ADULT_PASSENGER.name(), 0);
			jsPassengerTypes.put(PassengerType.CHILD_PASSENGER.name(), 0);
			jsPassengerTypes.put(PassengerType.SENIOR_PASSENGER.name(), 0);
			jsPassengerTypes.put(PassengerType.SPECIAL_NEEDS_PASSENGER.name(), 0);
			if(seatDetailsJsonObject.has("ADULT"))
			{
				JSONArray seatDetailsJson = seatDetailsJsonObject.getJSONArray("ADULT");
				adultPassenger = seatDetailsJson.length();
				//log.info"seatDetailsJSON----adult = " + seatDetailsJson.length());
				for(int k =0; k<seatDetailsJson.length(); k++)
				{
					JSONObject js = seatDetailsJson.getJSONObject(k);
					String seatClass = generalTripClass+"";//js.getString("seatClass");
					String seatLocation = js.getString("seatLocation");
					String key = seatClass + "~" + seatLocation + "~ADULT"; 
					//generalTripClass = seatClass;
					if(seatClassLocation.has(key))
					{
						int seatsCount = seatClassLocation.getInt(key);
						seatsCount = seatsCount+1;
						seatClassLocation.put(key, seatsCount);
					}
					else
					{
						seatClassLocation.put(key, 1);
					}
					
					if(uniqueClasses.has(seatClass))
					{
						uniqueClasses.put(seatClass, (uniqueClasses.getInt(seatClass))+1);
					}
					else
					{
						uniqueClasses.put(seatClass, 1);
					}
					jsPassengerTypes.put(PassengerType.ADULT_PASSENGER.name(), jsPassengerTypes.getInt(PassengerType.ADULT_PASSENGER.name()) + 1);
				}
			}
			
			
			
			if(seatDetailsJsonObject.has("CHILD"))
			{
				JSONArray seatDetailsJson = seatDetailsJsonObject.getJSONArray("CHILD");
				//log.info"seatDetailsJSON----child = " + seatDetailsJson.length());
				childPassenger = seatDetailsJson.length();
				for(int k =0; k<seatDetailsJson.length(); k++)
				{
					JSONObject js = seatDetailsJson.getJSONObject(k);
					String seatClass = generalTripClass+"";//js.getString("seatClass");
					String seatLocation = js.getString("seatLocation");
					String key = seatClass + "~" + seatLocation + "~CHILD"; 
					//generalTripClass = seatClass;
					
					if(seatClassLocation.has(key))
					{
						int seatsCount = seatClassLocation.getInt(key);
						seatsCount = seatsCount+1;
						seatClassLocation.put(key, seatsCount);
					}
					else
					{
						seatClassLocation.put(key, 1);
					}
					
					if(uniqueClasses.has(seatClass))
					{
						uniqueClasses.put(seatClass, (uniqueClasses.getInt(seatClass))+1);
					}
					else
					{
						uniqueClasses.put(seatClass, 1);
					}
					jsPassengerTypes.put(PassengerType.CHILD_PASSENGER.name(), jsPassengerTypes.getInt(PassengerType.CHILD_PASSENGER.name()) + 1);
				}
				
			}
			
			
			if(seatDetailsJsonObject.has("SENIOR"))
			{
				JSONArray seatDetailsJson = seatDetailsJsonObject.getJSONArray("SENIOR");
				//log.info"seatDetailsJSON----seniors = " + seatDetailsJson.length());
				seniorPassenger = seatDetailsJson.length();
				for(int k =0; k<seatDetailsJson.length(); k++)
				{
					JSONObject js = seatDetailsJson.getJSONObject(k);
					String seatClass = generalTripClass+"";//js.getString("seatClass");
					String seatLocation = js.getString("seatLocation");
					String key = seatClass + "~" + seatLocation + "~SENIOR"; 
					//generalTripClass = seatClass;
					
					if(seatClassLocation.has(key))
					{
						int seatsCount = seatClassLocation.getInt(key);
						seatsCount = seatsCount+1;
						seatClassLocation.put(key, seatsCount);
					}
					else
					{
						seatClassLocation.put(key, 1);
					}
					
					if(uniqueClasses.has(seatClass))
					{
						uniqueClasses.put(seatClass, (uniqueClasses.getInt(seatClass))+1);
					}
					else
					{
						uniqueClasses.put(seatClass, 1);
					}
					jsPassengerTypes.put(PassengerType.SENIOR_PASSENGER.name(), jsPassengerTypes.getInt(PassengerType.SENIOR_PASSENGER.name()) + 1);
				}
			}
			
			if(seatDetailsJsonObject.has("DISABLED"))
			{
				JSONArray seatDetailsJson = seatDetailsJsonObject.getJSONArray("DISABLED");
				//log.info"seatDetailsJSON----disabled = " + seatDetailsJson.length());
				disabledPassenger = seatDetailsJson.length();
				for(int k =0; k<seatDetailsJson.length(); k++)
				{
					JSONObject js = seatDetailsJson.getJSONObject(k);
					String seatClass = generalTripClass+"";//js.getString("seatClass");
					String seatLocation = js.getString("seatLocation");
					String key = seatClass + "~" + seatLocation + "~DISABLED"; 
					//generalTripClass = seatClass;
					
					if(seatClassLocation.has(key))
					{
						int seatsCount = seatClassLocation.getInt(key);
						seatsCount = seatsCount+1;
						seatClassLocation.put(key, seatsCount);
					}
					else
					{
						seatClassLocation.put(key, 1);
					}
					
					if(uniqueClasses.has(seatClass))
					{
						uniqueClasses.put(seatClass, (uniqueClasses.getInt(seatClass))+1);
					}
					else
					{
						uniqueClasses.put(seatClass, 1);
					}
					jsPassengerTypes.put(PassengerType.SPECIAL_NEEDS_PASSENGER.name(), jsPassengerTypes.getInt(PassengerType.SPECIAL_NEEDS_PASSENGER.name()) + 1);
				}
			}
			
			hql = "Select tp from ScheduleStation tp where tp.client.clientCode = '"+ clientCode + "' AND tp.deletedAt IS NULL " +
				"AND tp.scheduleStationCode.scheduleStationCode = '"+tripCode+"' AND " +
				"COALESCE(tp.arrivalTime, tp.departureTime) >= '" + departureScheduleStation.getDepartureTime() + "' AND " + 
				"COALESCE(tp.arrivalTime, tp.departureTime) <= '" + arrivalScheduleStation.getArrivalTime() + "' " +
				"ORDER BY COALESCE(tp.arrivalTime, tp.departureTime)";
			//log.info"coalesce = " + hql);
			List<ScheduleStation> scheduleStationDepartures = (List<ScheduleStation>)this.swpService.getAllRecordsByHQL(hql);
			//log.info"departure size = " + scheduleStationDepartures.size());
			Iterator<ScheduleStation> it = scheduleStationDepartures.iterator();
			List<Long> scheduledStationsList = new ArrayList<Long>();
			Boolean startAdding = false;
			JSONArray jsPassengers = new JSONArray();
			
			//log.info"adultPassenger --" + adultPassenger);
			//log.info"childPassenger --" + childPassenger);
			//log.info"seniorPassenger --" + seniorPassenger);
			//log.info"disabledPassenger --" + disabledPassenger);
			
			while(it.hasNext())
			{
				ScheduleStation ssIt = it.next();
				/*if(ssIt.getStation().getStationCode().equals(departingStationCode))
				{
					startAdding = true;
				}
				if(startAdding.equals(Boolean.TRUE)){*/
					scheduledStationsList.add(ssIt.getScheduleStationId());
					for(int m=0; m<adultPassenger; m++)
						jsPassengers.put((PassengerType.ADULT_PASSENGER));
					for(int m=0; m<childPassenger; m++)
						jsPassengers.put((PassengerType.CHILD_PASSENGER));
					for(int m=0; m<seniorPassenger; m++)
						jsPassengers.put((PassengerType.SENIOR_PASSENGER));
					for(int m=0; m<disabledPassenger; m++)
						jsPassengers.put((PassengerType.SPECIAL_NEEDS_PASSENGER));
					
				/*}
				
				if(ssIt.getStation().getStationCode().equals(arrivalStationCode))
				{
					startAdding = false;
				}*/
			}
			
			//log.info"scheduledStationsList size = " + scheduledStationsList.size());
			//log.info"jsTripRoutePassenger = " + jsPassengers.toString());
			////log.info"tripRoutes size = " + tripRoutes.size());
			//log.info"seatDetails = " + seatDetails);
			
			
			if(uniqueClasses.length()==0)
			{
				jsonObject.add("status", ERROR.AVAILABLE_SEATS_NOT_FOUND);
				jsonObject.add("message", "Provide valid information on number of passengers for this trip");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			/*JSONArray uniqueClassesKeys = uniqueClasses.names();
			for(int i=0; i<uniqueClassesKeys.length(); i++)
			{
				String key = uniqueClassesKeys.getString(i);
				String[] keys_ = key.split("~");
				Iterator<Long> scheduledStationsListIter = scheduledStationsList.iterator();
				while(scheduledStationsListIter.hasNext())
				{
					Long scheduledStationsId = scheduledStationsListIter.next(); 
					hql = "Select tp.schedSeatAvailId from ScheduleStationSeatAvailability tp where tp.vehicleSeat.client.clientCode = '"+ clientCode + "' " +
						"AND tp.deletedAt IS NULL " +
						//(tripToken==null ? "" : "AND tp.lockedDownBy = '"+tripToken+"'") +
						"AND tp.boughtByCustomer IS NULL " + 
						"AND tp.vehicleSeat.vehicleSeatSection.vehicleSeatClass.vehicleSeatClassCode = '"+generalTripClass+"' " +
						"AND tp.scheduleStationSeatSection.originScheduleStation.scheduleStationId = " + scheduledStationsId;
					//log.info"@@@@hql====" + hql);
					List<Long> vsaIds = (List<Long>)this.swpService.getAllRecordsByHQL(hql, 0, uniqueClasses.getInt(key));
				}
				
			}*/
			
			
			
			/*hql = "Select tp from ScheduleStation tp where tp.client.clientCode = '"+ clientCode + "' AND tp.deletedAt IS NULL " +
					"AND tp.scheduleStationId IN ("+StringUtils.join(scheduledStationsList, ",")+")";
			//log.info"@@@@hql3====" + hql);
			Collection<ScheduleStation> allScheduleStations = (List<ScheduleStation>)this.swpService.getAllRecordsByHQL(hql);*/
			Collection<ScheduleStation> allScheduleStations = scheduleStationDepartures;
			
			JSONArray keys = seatClassLocation.names();
			//log.info"keys =" + keys.toString());
			List<Long> inPipeline = new ArrayList<Long>();
			List<Long> inPipeline1 = new ArrayList<Long>();
			hql = "Select tp.schedSeatAvailId FROM ScheduleStationSeatAvailability tp WHERE tp.deletedAt IS NULL AND " +
					"tp.vehicleSeat.client.clientCode = '"+clientCode+"' AND " + 
					"tp.boughtByCustomer IS NULL AND " + 
					"(tp.lockedDownExpiryDate IS NULL OR (ADDTIME(tp.lockedDownExpiryDate, "+client.getLockDownInterval()*60+") < CURRENT_TIMESTAMP)) ";
			//log.info"hql = " + hql);
			List<Long> vsaids = (List<Long>)swpService.getAllRecordsByHQL(hql);
			
			Iterator<ScheduleStation> it2_ = allScheduleStations.iterator();
			int totalSeatsNeeded = 0;
			while(it2_.hasNext())
			{
				ScheduleStation scheduleStation = it2_.next();
				inPipeline1 = new ArrayList<Long>();
				if(scheduleStation.getArrivalTime()==null)
				{
					for(int k=0; k<keys.length(); k++)
					{
						int seatCount = seatClassLocation.getInt(keys.getString(k));
						String[] key_ = keys.getString(k).split("~");
						//log.info"keys =" + keys.getString(k).toString());
						if(key_.length==3)
						{
							String seatClass = key_[0];
							String seatLocation = key_[1];
							String seatPassengerType = key_[2];
							//log.info"seatClass =" + seatClass);
							//log.info"seatLocation =" + seatLocation);
							//log.info"seatPassengerType =" + seatPassengerType);
							if(seatClass!=null)
							{
								String preferences = " AND tp.scheduleStationSeatSection.vehicleSeatSection.vehicleSeatClass.vehicleSeatClassCode = '"+generalTripClass+"' ";
								String preferencesLocation = "";
								if(seatLocation!=null && (seatLocation.toUpperCase()==VehicleSeatLocation.AISLE.name() || seatLocation.toUpperCase()==VehicleSeatLocation.WINDOW.name()))
								{
									preferencesLocation = " AND tp.vehicleSeat.vehicleSeatLocation = " + VehicleSeatLocation.valueOf(seatLocation).ordinal();
								}
								
								hql = "Select tp.schedSeatAvailId FROM ScheduleStationSeatAvailability tp where tp.vehicleSeat.client.clientCode = '"+ clientCode + "' AND tp.deletedAt IS NULL ";									
										//(tripToken==null ? "" : "AND tp.lockedDownBy = '"+tripToken+"'") + " " + 
								if(vsaids.size()>0)
								{
									//hql = hql + "AND tp.schedSeatAvailId IN ("+StringUtils.join(vsaids, ", ")+") ";
									hql = hql + "AND tp.boughtByCustomer IS NULL AND (tp.lockedDownExpiryDate IS NULL OR (ADDTIME(tp.lockedDownExpiryDate, "+(client.getLockDownInterval()*60)+") < CURRENT_TIMESTAMP)) ";
								}
								hql = hql + " AND tp.boughtByCustomer IS NULL ";
								hql = hql + "AND tp.scheduleStationSeatSection.originScheduleStation.scheduleStationId = " + scheduleStation.getScheduleStationId() + 
										(inPipeline1.size() == 0 ? "" : (" AND tp.schedSeatAvailId NOT IN (" +StringUtils.join(inPipeline1, ", ") + ")")) + 
										preferences +
										(forcePreferences!=null && forcePreferences.equals(Boolean.TRUE) ? (preferencesLocation) : (""));
								hql = hql + " ORDER BY tp.vehicleSeat.seatOrder";
								//log.info"seatCount == " + seatCount + " >>>>" + hql);
								////log.info"inPipeline==" + StringUtils.join(inPipeline, ","));
								List<Long> vsa = (List<Long>)this.swpService.getAllRecordsByHQL(hql, 0, seatCount);
								//log.info"vsa==" + StringUtils.join(vsa, ","));
								inPipeline.addAll(vsa);
								inPipeline1.addAll(vsa);
								totalSeatsNeeded = totalSeatsNeeded + seatCount;
	
							}
						}
	
						//log.info"************************************************");
					}
					
				}
			}
			
			
			
			////log.info"vsa==" + StringUtils.join(vsa, ","));
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.MINUTE, client.getLockDownInterval());
			Date lockedDownExpiryDate = cal.getTime();
			
			
			if(lockdownSeats.equals(Boolean.TRUE))
			{
				if(tripToken!=null && tripToken.length()>0)
				{
					hql = "Select tp from ScheduleStationSeatAvailability tp where tp.vehicleSeat.client.clientCode = '"+clientCode+"' AND tp.deletedAt is NULL " +
						"AND tp.lockedDownBy = '"+tripToken+"'";
					//log.info">>>>> " + hql);
					Collection<ScheduleStationSeatAvailability> sssalist = (Collection<ScheduleStationSeatAvailability>)swpService.getAllRecordsByHQL(hql, 0, (childPassenger+adultPassenger));
					Iterator<ScheduleStationSeatAvailability> it2 = sssalist.iterator();
					int xy =0;
					while(it2.hasNext())
					{
						ScheduleStationSeatAvailability sssalistEntry = it2.next();
						sssalistEntry.setLockedDownExpiryDate(lockedDownExpiryDate);
						swpService.updateRecord(sssalistEntry);
						xy++;
					}
				}
			}
			
			if(inPipeline.size()==0)
			{
				jsonObject.add("status", ERROR.AVAILABLE_SEATS_NOT_FOUND);
				jsonObject.add("message", "There are no Available seats");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			else
			{
				if(inPipeline.size()<totalSeatsNeeded)
				{
					jsonObject.add("status", ERROR.NOT_ENOUGH_AVAILABLE_SEATS_NOT_FOUND);
					jsonObject.add("message", "There are no enough seats available");
					JsonObject jsonObj = jsonObject.build();
		            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
				}
				//log.info"...inPipeline = " + StringUtils.join(inPipeline, ", "));
				hql = "Select tp from ScheduleStationSeatAvailability tp where tp.vehicleSeat.client.clientCode = '"+ clientCode + "' AND tp.deletedAt IS NULL " +
						"AND tp.schedSeatAvailId IN ("+StringUtils.join(inPipeline, ", ")+") " +
						"AND tp.boughtByCustomer IS NULL " + 
						"ORDER BY tp.scheduleStationSeatSection.originScheduleStation.departureTime";
				//log.infohql);
				Collection<ScheduleStationSeatAvailability> vsa = (Collection<ScheduleStationSeatAvailability>)this.swpService.getAllRecordsByHQL(hql);
				Iterator<ScheduleStationSeatAvailability> it1 = vsa.iterator();
				JSONObject vehicleSeatsLockedDownTrip = new JSONObject();
				JSONObject vehicleSeatsLockedDown = new JSONObject();
				
				
				if(tripToken==null)
				{
					if(token==null)
					{
						JSONObject userDetails = new JSONObject();
						userDetails.put("username", "GUEST-" + RandomStringUtils.randomAlphabetic(10).toUpperCase());
						userDetails.put("roleCode", "GUEST");
						//userDetails.put("priviledges", {});
						Gson gson = new Gson();
						String obj = gson.toJson(userDetails);
						//Key jwtKey = Application.getKey();
						//String token = Jwts.builder().setSubject(obj).signWith(SignatureAlgorithm.HS512, jwtKey).compact();
						String tkId = RandomStringUtils.randomAlphanumeric(10);
						//log.info"tkId ==" + tkId);
						//log.info"clientCode ==" + clientCode);
						//log.info"obj ==" + obj);
						//log.info"client.getLockDownInterval() ==" + client.getLockDownInterval());
						tripToken = app.createJWT(tkId, clientCode, obj, (client.getLockDownInterval() *60*1000));
					}
					else
					{
						JSONObject userDetails = new JSONObject();
						userDetails.put("username", user.getUsername() + "-" + RandomStringUtils.randomAlphabetic(10).toUpperCase());
						userDetails.put("roleCode", roleCode.name());
						//userDetails.put("priviledges", {});
						Gson gson = new Gson();
						String obj = gson.toJson(userDetails);
						//Key jwtKey = Application.getKey();
						//String token = Jwts.builder().setSubject(obj).signWith(SignatureAlgorithm.HS512, jwtKey).compact();
						String tkId = RandomStringUtils.randomAlphanumeric(10);
						//log.info"tkId1 ==" + tkId);
						//log.info"clientCode1 ==" + clientCode);
						//log.info"obj1 ==" + obj);
						//log.info"client.getLockDownInterval()1 ==" + client.getLockDownInterval());
						tripToken = app.createJWT(tkId, clientCode, obj, (client.getLockDownInterval() *60*1000));
					}
				}
				
				
				/*if((tripToken==null || (tripToken!=null && tripToken.length()==0)) && token==null)
				{
					JSONObject userDetails = new JSONObject();
					userDetails.put("username", "GUEST-" + RandomStringUtils.randomAlphabetic(10).toUpperCase());
					userDetails.put("roleCode", "GUEST");
					//userDetails.put("priviledges", {});
					Gson gson = new Gson();
					String obj = gson.toJson(userDetails);
					//Key jwtKey = Application.getKey();
					//String token = Jwts.builder().setSubject(obj).signWith(SignatureAlgorithm.HS512, jwtKey).compact();
					String tkId = RandomStringUtils.randomAlphanumeric(10);
					//log.info"tkId ==" + tkId);
					//log.info"clientCode ==" + clientCode);
					//log.info"obj ==" + obj);
					//log.info"client.getLockDownInterval() ==" + client.getLockDownInterval());
					tripToken = app.createJWT(tkId, clientCode, obj, (client.getLockDownInterval() *60*1000));
				}
				else if((tripToken==null || (tripToken!=null && tripToken.length()==0)) && token!=null)
				{
					
					JSONObject userDetails = new JSONObject();
					userDetails.put("username", user.getUsername() + "-" + RandomStringUtils.randomAlphabetic(10).toUpperCase());
					userDetails.put("roleCode", user.getRoleCode().name());
					//userDetails.put("priviledges", {});
					Gson gson = new Gson();
					String obj = gson.toJson(userDetails);
					//Key jwtKey = Application.getKey();
					//String token = Jwts.builder().setSubject(obj).signWith(SignatureAlgorithm.HS512, jwtKey).compact();
					String tkId = RandomStringUtils.randomAlphanumeric(10);
					//log.info"tkId ==" + tkId);
					//log.info"clientCode ==" + clientCode);
					//log.info"obj ==" + obj);
					//log.info"client.getLockDownInterval() ==" + client.getLockDownInterval());
					tripToken = app.createJWT(tkId, clientCode, obj, (client.getLockDownInterval() *60*1000));
				}*/
				
				//log.info".....tripToken = " + tripToken);
				//log.info".....vsa size = " + vsa.size());
				//log.info".....vsa = " + vsa.toString());
				String daSql = "Select tp from Station tp where tp.stationCode = '"+departingStationCode+"' AND " +
						"tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL";
				Station departureStation = (Station)swpService.getUniqueRecordByHQL(daSql);

				daSql = "Select tp from Station tp where tp.stationCode = '"+arrivalStationCode+"' AND " +
							"tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL";
				Station arrivalStation = (Station)swpService.getUniqueRecordByHQL(daSql);
				
				daSql = "Select tp from TripZoneStation tp where tp.station.stationCode = '"+arrivalStationCode+"' AND " +
						"tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL";
				TripZoneStation arrivalTripZoneStation = (TripZoneStation)swpService.getUniqueRecordByHQL(daSql);
				
				daSql = "Select tp from TripZoneStation tp where tp.station.stationCode = '"+departingStationCode+"' AND " +
						"tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL";
				TripZoneStation depatureTripZoneStation = (TripZoneStation)swpService.getUniqueRecordByHQL(daSql);
				//log.info"1 .... >>>>>" + arrivalTripZoneStation.getTripZone().getRouteOrder());
				//log.info"1 .... >>>>>" + depatureTripZoneStation.getTripZone().getRouteOrder());
				
				Integer tripRouteOrderdifference =  depatureTripZoneStation.getTripZone().getRouteOrder() - arrivalTripZoneStation.getTripZone().getRouteOrder();
				tripRouteOrderdifference = Math.abs(tripRouteOrderdifference);
				tripRouteOrderdifference = tripRouteOrderdifference + 1;
				
				
				daSql = "Select tp from VehicleTripPrice tp where tp.finalDestinationTripZone.routeOrder = " + tripRouteOrderdifference + 
							" AND tp.deletedAt is NULL and tp.client.clientCode = '"+clientCode+"' " +
							" AND lower(tp.vehicleSeatClass.vehicleSeatClassCode) = '"+generalTripClass.toLowerCase() +"'";
				//log.info"daSQL ===> " + daSql);
				Collection<VehicleTripPrice> vehicleTripPrices = (Collection<VehicleTripPrice>)swpService.getAllRecordsByHQL(daSql);
				//log.info"v size ---> " + vehicleTripPrices.size());
				Double baseAdultFare = 0.00;
				Double baseChildFare = 0.00;
				Double baseSeniorFare = 0.00;
				Double baseDisabledFare = 0.00;
				
				if(vehicleTripPrices.size()==0)
				{
					jsonObject.add("status", ERROR.NO_TRIPS_FOUND_FOR_SPECIFIED_DATES);
					jsonObject.add("message", "No pricing Found For the Specified trip");
					JsonObject jsonObj = jsonObject.build();
		            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
				}
				
				Iterator<VehicleTripPrice> itVTP = vehicleTripPrices.iterator();
				while(itVTP.hasNext())
				{
					VehicleTripPrice vtp = itVTP.next();
					if(vtp.getPriceType().equals(PriceType.FOR_ADULT_PRICE))
					{
						baseAdultFare = vtp.getAmount();
					}
					else if(vtp.getPriceType().equals(PriceType.FOR_CHILD_PRICE))
					{
						baseChildFare = vtp.getAmount();
					}
					else if(vtp.getPriceType().equals(PriceType.FOR_SENIOR_CITIZENS))
					{
						baseSeniorFare = vtp.getAmount();
					}
					else if(vtp.getPriceType().equals(PriceType.FOR_SPECIALLY_ABLED))
					{
						baseDisabledFare = vtp.getAmount();
					}
				}
				
				
				Map<Long, ScheduleStationSeatAvailability> sssaHolder = new HashMap<Long, ScheduleStationSeatAvailability>();
				int xy = 0;
				while(it1.hasNext())
				{
					ScheduleStationSeatAvailability ssssa_ = (ScheduleStationSeatAvailability)it1.next();
					sssaHolder.put(ssssa_.getScheduleStationSeatSection().getOriginScheduleStation().getScheduleStationId(), ssssa_);
					ssssa_.setLockedDown(Boolean.TRUE);
					ssssa_.setLockedDownBy(tripToken);
					ssssa_.setLockedDownExpiryDate(lockedDownExpiryDate);
					ssssa_.setSeatAvailabilityStatus(SeatAvailabilityStatus.OPEN);
					ssssa_.setPassengerType((PassengerType)jsPassengers.get(xy++));
					if(lockdownSeats.equals(Boolean.TRUE))
					{
						swpService.updateRecord(ssssa_);
					}
					JSONObject js = new JSONObject();
					js.put("seatNumber", ssssa_.getVehicleSeat().getSeatNumber());
					js.put("originStation", ssssa_.getScheduleStationSeatSection().getOriginScheduleStation().getStation().getStationName());
					js.put("originStationCode", ssssa_.getScheduleStationSeatSection().getOriginScheduleStation().getStation().getStationCode());
					js.put("passengerType", ssssa_.getPassengerType().name());
					js.put("sectionName", ssssa_.getScheduleStationSeatSection().getVehicleSeatSection().getSectionName());
					js.put("sectionCode", ssssa_.getScheduleStationSeatSection().getVehicleSeatSection().getSectionCode());
					js.put("seatClassName", ssssa_.getScheduleStationSeatSection().getVehicleSeatSection().getVehicleSeatClass().getVehicleSeatClassName());
					js.put("seatLocation", ssssa_.getVehicleSeat().getVehicleSeatLocation().name());
					js.put("seatAvailabilityId", ssssa_.getSchedSeatAvailId());
					
					JSONArray jsArray = null;
					String key = ssssa_.getScheduleStationSeatSection().getOriginScheduleStation().getStation().getStationCode();
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
				
				xy =0;
				JSONArray allTrips = new JSONArray();
				JSONObject jsonObjectEntry = new JSONObject();
				JSONObject js = null;
				Iterator<ScheduleStation> allScheduleStationIter = allScheduleStations.iterator();
				while(allScheduleStationIter.hasNext())
				{
					ScheduleStation scheduleStation = allScheduleStationIter.next();
					if(xy%2==0)
						js = new JSONObject();
					
					ScheduleStationSeatAvailability ssssa_ = sssaHolder.get(scheduleStation.getScheduleStationId());
					
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
					
					if(ssssa_!=null)
					{
						js.put("seatAvailabilityId", ssssa_.getSchedSeatAvailId());
						js.put("seatNumber", ssssa_.getVehicleSeat().getSeatNumber());
						js.put("seatName", ssssa_.getVehicleSeat().getSeatNumber());
						js.put("expirationPeriodMinutes", client.getLockDownInterval());
						js.put("seatClass", ssssa_.getVehicleSeat().getVehicleSeatSection().getSectionName());
						js.put("seatLocation", ssssa_.getVehicleSeat().getVehicleSeatLocation().name());
						js.put("seatFacingOtherSeats", ssssa_.getVehicleSeat().getTripSeatFacing());
						js.put("expirationDate", new SimpleDateFormat("yyyy-MM-dd HH:mm").format(ssssa_.getLockedDownExpiryDate()));
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
				jsonObjectEntry.put("trips", allTrips);
				jsonObjectEntry.put("tripCode", tripCode);
				//jsonObjectEntry.put("vehicleName", originSchedule==null ? destinationSchedule.getVehicle().getVehicleName() : originSchedule.getVehicle().getVehicleName());
				
				
				if(vehicleSeatsLockedDown.length()>0)
				{
					
					vehicleSeatsLockedDownTrip.put("seatsAlloted", vehicleSeatsLockedDown);
					vehicleSeatsLockedDownTrip.put("tripDetails", allTrips.toString());
					jsonObject.add("message", "Vehicle seats found");
					jsonObject.add("status", ERROR.GENERAL_SUCCESS);
					jsonObject.add("tripBookingToken", tripToken);
					jsonObject.add("details", vehicleSeatsLockedDownTrip.toString());
					jsonObject.add("vehicleTripPrices", new Gson().toJson(vehicleTripPrices));
					jsonObject.add("baseAdultFare", baseAdultFare);
					jsonObject.add("baseChildFare", baseChildFare);
					jsonObject.add("baseSeniorFare", baseSeniorFare);
					jsonObject.add("baseDisabledFare", baseDisabledFare);
					if(lockedDownExpiryDate!=null)
						jsonObject.add("ticketPurchaseExpiryDate", new SimpleDateFormat("yyyy-MM-dd HH:mm").format(lockedDownExpiryDate));
				}
				else
				{
					jsonObject.add("message", "Vehicle seats could not be assigned to customer");
					jsonObject.add("status", ERROR.AVAILABLE_SEATS_COULD_NOT_BE_LOCKED_DOWN);
					if(tripToken!=null)
						jsonObject.add("tripBookingToken", tripToken);
				}
				//jsonObject.add("vehicleSeats", new Gson().toJson(vehicleSeatsLockedDown));
				jsonObject.add("tripCode", tripCode);
				jsonObject.add("adultPassenger", adultPassenger);
				jsonObject.add("childPassenger", childPassenger);
				jsonObject.add("disabledPassenger", disabledPassenger);
				jsonObject.add("seniorPassenger", seniorPassenger);
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
	

	
	
	
	public Response getTripSeatAndLockDown(String deviceCode, Boolean lockdownSeats, String token, String outwardTrip, String inwardTrip, String requestId, String ipAddress, String clientCode, String tripToken) {
		// TODO Auto-generated method stub
		JsonObjectBuilder jsonObject = Json.createObjectBuilder();
    	
		try{
			if(outwardTrip==null)
			{
				jsonObject.add("status", ERROR.INCOMPLETE_PARAMETERS);
				jsonObject.add("message", "Incomplete Parameters provided in request.");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			swpService = serviceLocator.getSwpService();
			Application app = Application.getInstance(swpService);
			
			
			Client client=null;
			if(clientCode!=null)
			{
				String hql = "Select tp from Client tp where tp.clientCode = '"+ clientCode + "' AND tp.deletedAt IS NULL";
				//log.infohql);
				client = (Client)swpService.getUniqueRecordByHQL(hql);
			}
			
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
			
			if(lockdownSeats==null)
				lockdownSeats = Boolean.FALSE;
			
			//Return Trip
			if(outwardTrip!=null && inwardTrip!=null)
			{
				//Outward Trip
				//log.info"outwardTrip -- > " + outwardTrip);
				//log.info">>>>tripToken == " + tripToken);
				JSONObject outwardTripJS = new JSONObject(outwardTrip);
				String tripCode = outwardTripJS.getString("tripCode");
				String departingStationCode = outwardTripJS.getString("departingStationCode");
				String arrivalStationCode =  outwardTripJS.getString("arrivalStationCode");
				JSONObject seatDetails = outwardTripJS.getJSONObject("seatDetails");
				String seatDetailStr = seatDetails.toString();
				Boolean forcePreferences = outwardTripJS.getBoolean("forcePreferences");
				String generalTripClass = outwardTripJS.getString("generalTripClass");
				
				Response res = getVehicleSeatAndLockDown(Boolean.TRUE, token, tripCode, departingStationCode, arrivalStationCode, 
						seatDetailStr, forcePreferences, generalTripClass, requestId, ipAddress, clientCode, tripToken);
				String responseStr = (String)(res.getEntity());
				JSONObject availableSeats = new JSONObject(responseStr);
				//log.info"outwardtrip availableSeats ---- " + availableSeats.toString());
				Boolean outwardSuccess = Boolean.FALSE;
				Boolean inwardSuccess = Boolean.FALSE;
				String ticketPurchaseExpiryDate = "";
				if(availableSeats.has("status") && availableSeats.getInt("status")==0)
				{
					String detailsStr = availableSeats.getString("details");
					JSONObject details = new JSONObject(detailsStr);
					//log.info"details === " + details.toString());
					String tripDetailsStr = details.getString("tripDetails");
					tripToken = availableSeats.getString("tripBookingToken");
					JSONArray tripDetails = new JSONArray(tripDetailsStr);
					//log.info"tripDetails length === " + tripDetails.length());
					jsonObject.add("outwardTripDetails", availableSeats.toString());
					outwardSuccess = Boolean.TRUE;
					ticketPurchaseExpiryDate = availableSeats.getString("ticketPurchaseExpiryDate");
					//log.info"ticketPurchaseExpiryDate === " + ticketPurchaseExpiryDate);
				}
				
				//Inward Trip
				JSONObject inwardTripJS = new JSONObject(inwardTrip);
				tripCode = inwardTripJS.getString("tripCode");
				departingStationCode = inwardTripJS.getString("departingStationCode");
				arrivalStationCode =  inwardTripJS.getString("arrivalStationCode");
				seatDetails = inwardTripJS.getJSONObject("seatDetails");
				seatDetailStr = seatDetails.toString();
				forcePreferences = inwardTripJS.getBoolean("forcePreferences");
				generalTripClass = inwardTripJS.getString("generalTripClass");
				
				res = getVehicleSeatAndLockDown(Boolean.TRUE, token, tripCode, departingStationCode, arrivalStationCode, 
						seatDetailStr, forcePreferences, generalTripClass, requestId, ipAddress, clientCode, tripToken);
				responseStr = (String)(res.getEntity());
				availableSeats = new JSONObject(responseStr);
				//log.info"inwardtrip availableSeats ---- " + availableSeats.toString());
				if(availableSeats.has("status") && availableSeats.getInt("status")==ERROR.GENERAL_SUCCESS)
				{
					String detailsStr = availableSeats.getString("details");
					JSONObject details = new JSONObject(detailsStr);
					//log.info"details === " + details.toString());
					String tripDetailsStr = details.getString("tripDetails");
					JSONArray tripDetails = new JSONArray(tripDetailsStr);
					//log.info"tripDetails length === " + tripDetails.length());
					jsonObject.add("inwardTripDetails", availableSeats.toString());
					inwardSuccess = Boolean.TRUE;
					ticketPurchaseExpiryDate = availableSeats.getString("ticketPurchaseExpiryDate");
					//log.info"ticketPurchaseExpiryDate === " + ticketPurchaseExpiryDate);
				}
				
				if(inwardSuccess.equals(Boolean.TRUE) && outwardSuccess.equals(Boolean.TRUE))
				{
					jsonObject.add("status", ERROR.GENERAL_SUCCESS);
					jsonObject.add("message", "Vehicle seats locked for payment");
					jsonObject.add("lifeSpan", client.getLockDownInterval());
					if(ticketPurchaseExpiryDate!=null)
						jsonObject.add("ticketPurchaseExpiryDate", ticketPurchaseExpiryDate);
				}
				else
				{
					if(outwardSuccess.equals(Boolean.TRUE))
					{
						jsonObject.add("status", ERROR.RETURNTRIP_OUTWARD_TRIP_ONLY_SUCCESS);
						jsonObject.add("message", "Only Outward trip seats locked for payment");
						jsonObject.add("lifeSpan", client.getLockDownInterval());
						if(ticketPurchaseExpiryDate!=null)
							jsonObject.add("ticketPurchaseExpiryDate", ticketPurchaseExpiryDate);
					}
					else if(outwardSuccess.equals(Boolean.TRUE))
					{
						jsonObject.add("status", ERROR.RETURNTRIP_INWARD_TRIP_ONLY_SUCCESS);
						jsonObject.add("message", "Only Inward trip seats locked for payment");
						jsonObject.add("lifeSpan", client.getLockDownInterval());
						if(ticketPurchaseExpiryDate!=null)
							jsonObject.add("ticketPurchaseExpiryDate", ticketPurchaseExpiryDate);
					}
					else
					{
						jsonObject.add("message", "Vehicle seats could not be assigned to customer");
						jsonObject.add("status", ERROR.AVAILABLE_SEATS_COULD_NOT_BE_LOCKED_DOWN);
						jsonObject.add("lifeSpan", client.getLockDownInterval());
					}
				}
			}
			else
			{
				//Single One-Way Trip
				//Outward Trip
				System.out.println("outwardTrip ... " + outwardTrip);
				JSONObject outwardTripJS = new JSONObject(outwardTrip);
				String tripCode = outwardTripJS.getString("tripCode");
				String departingStationCode = outwardTripJS.getString("departingStationCode");
				String arrivalStationCode =  outwardTripJS.getString("arrivalStationCode");
				JSONObject seatDetails = outwardTripJS.getJSONObject("seatDetails");
				String seatDetailStr = seatDetails.toString();
				Boolean forcePreferences = outwardTripJS.getBoolean("forcePreferences");
				String generalTripClass = outwardTripJS.getString("generalTripClass");
				
				Response res = getVehicleSeatAndLockDown(Boolean.TRUE, token, tripCode, departingStationCode, arrivalStationCode, 
						seatDetailStr, forcePreferences, generalTripClass, requestId, ipAddress, clientCode, tripToken);
				String responseStr = (String)(res.getEntity());
				JSONObject availableSeats = new JSONObject(responseStr);
				//log.info"outwardtrip availableSeats ---- " + availableSeats.toString());
				Boolean outwardSuccess = Boolean.FALSE;
				String ticketPurchaseExpiryDate = null;
				if(availableSeats.has("status") && availableSeats.getInt("status")==0)
				{
					String detailsStr = availableSeats.getString("details");
					JSONObject details = new JSONObject(detailsStr);
					//log.info"details === " + details.toString());
					String tripDetailsStr = details.getString("tripDetails");
					JSONArray tripDetails = new JSONArray(tripDetailsStr);
					//log.info"tripDetails length === " + tripDetails.length());
					jsonObject.add("outwardTripDetails", availableSeats.toString());
					outwardSuccess = Boolean.TRUE;
					ticketPurchaseExpiryDate = availableSeats.getString("ticketPurchaseExpiryDate");
					//log.info"ticketPurchaseExpiryDate === " + ticketPurchaseExpiryDate);
				}
				
				if(outwardSuccess.equals(Boolean.TRUE))
				{
					jsonObject.add("status", ERROR.GENERAL_SUCCESS);
					jsonObject.add("message", "Vehicle seats locked for payment");
					jsonObject.add("lifeSpan", client.getLockDownInterval());
					if(ticketPurchaseExpiryDate!=null)
						jsonObject.add("ticketPurchaseExpiryDate", ticketPurchaseExpiryDate);
				}
				else
				{
					
					jsonObject.add("message", "Vehicle seats could not be assigned to customer");
					jsonObject.add("status", ERROR.AVAILABLE_SEATS_COULD_NOT_BE_LOCKED_DOWN);
					jsonObject.add("lifeSpan", client.getLockDownInterval());
				}
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
	
	
	
	
	public Response getVehicleTripDetailsByTransactionRef(String deviceCode, String token, String orderRef, String requestId, String ipAddress, String clientCode) {
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
			
			JSONObject clientSettings = (JSONObject)app.getAllSettings().get(clientCode);
			Integer retrievalCount = clientSettings!=null && clientSettings.length()>0 && clientSettings.has(Settings.MAXIMUM_TICKET_RETRIEVAL.name()) ? clientSettings.getInt(Settings.MAXIMUM_TICKET_RETRIEVAL.name()) : null;
			
			if(retrievalCount!=null && transaction.getRetrievalCount()!=null && transaction.getRetrievalCount()==retrievalCount)
			{
				jsonObject.add("status", ERROR.GENERAL_SYSTEM_ERROR);
				jsonObject.add("message", "Exceeded maximum ticket retrieval count");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			//log.infonew Gson().toJson(transaction));
			List<Long> ptIds = new ArrayList<Long>();
			
			hql = "Select tp from PurchasedTrip tp where tp.transaction.transactionId = " + transaction.getTransactionId() + " AND tp.deletedAt IS NULL " +
					"AND tp.client.clientCode = '"+clientCode+"'";
			Collection<PurchasedTrip> purchasedTrips = (Collection<PurchasedTrip>)swpService.getAllRecordsByHQL(hql);
			Iterator<PurchasedTrip> ptIt = purchasedTrips.iterator();
			JSONArray allPurchasedTripSeat = new JSONArray();
			Vehicle vehicle = null;
			while(ptIt.hasNext())
			{
				PurchasedTrip pt = ptIt.next();
				vehicle = pt.getVehicleSchedule().getVehicle();
				
				hql = "Select tp from Customer tp where tp.purchasedTrip.purchasedTripId = " + pt.getPurchasedTripId() + " AND tp.deletedAt IS NULL";
				Collection<Customer> customers = swpService.getAllRecordsByHQL(hql);
				JSONArray customerList = new JSONArray();
				Iterator<Customer> cs = customers.iterator();
				while(cs.hasNext())
				{
					Customer cs1 = cs.next();
					JSONObject js_ = new JSONObject();
					js_.put("firstName", cs1.getFirstName());
					js_.put("lastName", cs1.getLastName());
					if(cs1.getOtherName()!=null)
						js_.put("otherName", cs1.getOtherName());
					
					js_.put("emailAddress", cs1.getEmailAddress());
					js_.put("mobileNumber", cs1.getMobileNumber());
					js_.put("isLeadPassenger", cs1.getIsLeadPassenger()!=null && cs1.getIsLeadPassenger().equals(Boolean.TRUE) ? "1" : "0");
					customerList.put(js_);
				}
				
				
				hql = "Select tp from VehicleSchedule tp where tp.vehicleScheduleId = "+pt.getVehicleSchedule().getVehicleScheduleId()+" AND " +
						"tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL";
				//log.infohql);
				VehicleSchedule vs = (VehicleSchedule)swpService.getUniqueRecordByHQL(hql);
				
				
				hql = "Select tp from PurchasedTripSeat tp where tp.purchasedTrip.client.clientCode = '"+ clientCode + "' AND tp.deletedAt IS NULL " +
					"AND tp.purchasedTrip.purchasedTripId = "+ pt.getPurchasedTripId();
				//log.infohql);
				Collection<PurchasedTripSeat> purchasedTripSeats = (Collection<PurchasedTripSeat>)this.swpService.getAllRecordsByHQL(hql);
				Iterator<PurchasedTripSeat> it = purchasedTripSeats.iterator();
				JSONArray entry = new JSONArray();
				JSONObject entry_ = new JSONObject();
				int adultPassenger = 0;
				int childPassenger = 0;
				int seniorPassenger = 0;
				int disabledPassenger = 0;
				Set<String> allOriginStationCodes = new HashSet<String>();
				while(it.hasNext())
				{
					PurchasedTripSeat pts = it.next();
					JSONObject tripRoutes = new JSONObject();
					tripRoutes.put("departureStationCode", pts.getScheduleStationSeatAvailability().getScheduleStationSeatSection().getOriginScheduleStation().getStation().getStationCode());
					tripRoutes.put("departureStationName", pts.getScheduleStationSeatAvailability().getScheduleStationSeatSection().getOriginScheduleStation().getStation().getStationName());
					tripRoutes.put("departureTime", pts.getScheduleStationSeatAvailability().getScheduleStationSeatSection().getOriginScheduleStation().getDepartureTime());
					//tripRoutes.put("destinationStationCode", pts.getPurchasedTrip().getArrivalStation().getStationCode());
					//tripRoutes.put("destinationStationName", pts.getPurchasedTrip().getArrivalStation().getStationName());
					//tripRoutes.put("destinationArrivalTime", pts.getPurchasedTrip().getArrivalTime());
					//tripRoutes.put("routeOrder", pts.getVehicleSeatAvailability().getVehicleTripRouteSeatSection().getVehicleTripRouting().getRouteOrder());
					//tripRoutes.put("routeCode", pts.getVehicleSeatAvailability().getVehicleTripRouteSeatSection().getVehicleTripRouting().getRouteCode());
					tripRoutes.put("sectionName", pts.getScheduleStationSeatAvailability().getScheduleStationSeatSection().getVehicleSeatSection().getSectionName());
					tripRoutes.put("sectionClass", pts.getScheduleStationSeatAvailability().getScheduleStationSeatSection().getVehicleSeatSection().getVehicleSeatClass().getVehicleSeatClassName());
					tripRoutes.put("passengerType", pts.getPassengerType().name());
					entry.put(tripRoutes);
					
					if(pts.getPassengerType().equals(PassengerType.ADULT_PASSENGER))
						adultPassenger = adultPassenger + 1;
					if(pts.getPassengerType().equals(PassengerType.CHILD_PASSENGER))
						childPassenger = childPassenger + 1;
					if(pts.getPassengerType().equals(PassengerType.SENIOR_PASSENGER))
						seniorPassenger = seniorPassenger + 1;
					if(pts.getPassengerType().equals(PassengerType.SPECIAL_NEEDS_PASSENGER))
						disabledPassenger = disabledPassenger + 1;
					
					allOriginStationCodes.add(pts.getScheduleStationSeatAvailability().getScheduleStationSeatSection().getOriginScheduleStation().getStation().getStationCode());
				}
				entry_.put("vehicleName", vs.getVehicle().getVehicleName());
				entry_.put("tripLineName", vs.getLine().getLineName());
				entry_.put("tripLineCode", vs.getLine().getLineCode());
				entry_.put("transactionRef", pt.getTransaction().getTransactionRef());
				entry_.put("receiptNo", pt.getReceiptNo());
				entry_.put("departureStationName", pt.getDepartureStation().getStationName());
				entry_.put("departureStationCode", pt.getDepartureStation().getStationCode());
				entry_.put("departureTime", pt.getDepartureTime());
				entry_.put("arrivalStationName", pt.getArrivalStation().getStationName());
				entry_.put("arrivalStationCode", pt.getArrivalStation().getStationCode());
				entry_.put("arrivalTime", pt.getArrivalTime());
				entry_.put("tripRoutes", entry);
				entry_.put("adultPassenger", adultPassenger/allOriginStationCodes.size());
				entry_.put("childPassenger", childPassenger/allOriginStationCodes.size());
				entry_.put("seniorPassenger", seniorPassenger/allOriginStationCodes.size());
				entry_.put("disabledPassenger", disabledPassenger/allOriginStationCodes.size());
				entry_.put("totalPassengers", ((adultPassenger/allOriginStationCodes.size()) + (childPassenger/allOriginStationCodes.size()) + (seniorPassenger/allOriginStationCodes.size()) + (disabledPassenger/allOriginStationCodes.size())));
				entry_.put("totalTripCost", pt.getAmountPayable());
				entry_.put("bookingFee", client.getBookingFee());
				entry_.put("customers", customerList);
				allPurchasedTripSeat.put(entry_);
			}
			
			JSONObject js = new JSONObject();
			Date datePurchased = transaction.getCreatedAt();
			Double totalAmountPaid = transaction.getTransactionAmount();
			String currency = transaction.getTransactionCurrency();
			
			
			
			
			
			//log.info"allPurchasedTripSeat size = " + allPurchasedTripSeat.length());
			//log.info"allPurchasedTripSeat = " + allPurchasedTripSeat.toString());
			
			if(allPurchasedTripSeat.length()==0)
			{
				jsonObject.add("status", ERROR.AVAILABLE_SEATS_NOT_FOUND);
				jsonObject.add("message", "There are no Available seats found purchased for this trip");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			else
			{
				js.put("datePurchased", new SimpleDateFormat("E, dd MMM yyyy HH:mm a").format(datePurchased));
				js.put("datePurchasedUnformatted", datePurchased);
				js.put("ticketDetails", allPurchasedTripSeat);
				js.put("totalAmountPaid", totalAmountPaid);
			
				
				transaction.setRetrievalCount(transaction.getRetrievalCount()==null ? 1 : (transaction.getRetrievalCount() + 1));
				transaction.setUpdatedAt(new Date());
				swpService.updateRecord(transaction);
				
				//log.info"transaction.getTransactingUserName() - " + transaction.getTransactingUserName());
				//log.info"vehicle!=null ? vehicle.getInventoryNumber() : " + (vehicle!=null && vehicle.getInventoryNumber()!=null ? vehicle.getInventoryNumber() : ""));
				jsonObject.add("status", ERROR.GENERAL_SUCCESS);
				if(transaction.getTransactingUserName()!=null)
					jsonObject.add("bookedBy", transaction.getTransactingUserName());
				if(transaction.getTransactingUser()!=null && transaction.getTransactingUser().getExternalId()!=null)
					jsonObject.add("bookedById", transaction.getTransactingUser().getExternalId());
				jsonObject.add("vehicleNumber", vehicle!=null ? vehicle.getInventoryNumber() : "");
				jsonObject.add("message", "Available seats found purchased for this trip");
				jsonObject.add("orderRef", orderRef);
				jsonObject.add("purchasedTripDetails", js.toString());
				jsonObject.add("purchasedTrips", new Gson().toJson(purchasedTrips));
				jsonObject.add("transaction", new Gson().toJson(transaction));
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
	
	
	

	public Response getCustomerPurchasedTripsByCustomerMobileNumber(String deviceCode, String token, String customerMobileNumber, String requestId, String ipAddress, String clientCode, String startDate, String endDate) {
		// TODO Auto-generated method stub
		JsonObjectBuilder jsonObject = Json.createObjectBuilder();
    	
		try{
			
			if(deviceCode==null || customerMobileNumber==null || clientCode==null || startDate==null || endDate==null)
			{
				jsonObject.add("status", ERROR.INCOMPLETE_PARAMETERS);
				jsonObject.add("message", "Incomplete Parameters provided in request.");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			//log.info"customerMobileNumber = " + customerMobileNumber);
			
			
			
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
			
			
			hql = "Select tp from Customer tp where " +
					"tp.createdAt >= '" + startDate + "' AND tp.createdAt <= '" + endDate +  "' AND " +
					"lower(tp.mobileNumber) = '"+customerMobileNumber.trim() +"' AND tp.deletedAt is NULL AND tp.client.clientCode = '"+clientCode+"'";
			//AND tp.isLeadPassenger = 1";
			//log.infohql);
			Collection<Customer> customerList = (Collection<Customer>)swpService.getAllRecordsByHQL(hql);
			if(customerList!=null && customerList.size()>0)
			{
				
			}
			else
			{
				jsonObject.add("status", ERROR.TRANSACTION_NOT_FOUND);
				jsonObject.add("message", "No lead passenger found matching the mobile number provided. You may want to use the international code to search again");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			//log.info"&&&&&");
			//log.infonew Gson().toJson(customerList.size()));
			List<Long> ptIds = new ArrayList<Long>();
			Iterator<Customer> custIter = customerList.iterator();
			JSONArray jsonArray = new JSONArray();
			
			while(custIter.hasNext())
			{
				Customer cust = custIter.next();
				JSONObject jsonObject1 = new JSONObject();
				if(cust.getPurchasedTrip()!=null && cust.getPurchasedTrip().getTransaction()!=null)
				{
					jsonObject1.put("customerName", cust.getFirstName() + (cust.getOtherName()==null ? "" : (" " + cust.getOtherName())) + " " + cust.getLastName());
					jsonObject1.put("customerMobileNumber", cust.getMobileNumber());
					jsonObject1.put("orderRef", cust.getPurchasedTrip().getTransaction().getOrderRef());
					jsonObject1.put("departureStationName", cust.getPurchasedTrip().getDepartureStation().getStationName());
					jsonObject1.put("departureTime", cust.getPurchasedTrip().getDepartureTime());
					jsonObject1.put("arrivalStationName", cust.getPurchasedTrip().getArrivalStation().getStationName());
					jsonObject1.put("arrivalTime", cust.getPurchasedTrip().getArrivalTime());
					jsonArray.put(jsonObject1);
				}
			}
			
			if(jsonArray.length()==0)
			{
				jsonObject.add("status", ERROR.PURCHASED_TRIP_NOT_FOUND);
				jsonObject.add("message", "No lead passenger found matching the mobile number provided. You may want to use the international code to search again");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			else
			{
				jsonObject.add("status", ERROR.GENERAL_SUCCESS);
				jsonObject.add("purchasedTrips", jsonArray.toString());
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
	
	
	
	
	public Response getCustomerPurchasedTripsByCustomerName(String deviceCode, String token, String firstName, String lastName, String requestId, String ipAddress, String clientCode, String startDate, String endDate) {
		// TODO Auto-generated method stub
		JsonObjectBuilder jsonObject = Json.createObjectBuilder();
    	
		try{
			
			if(deviceCode==null || firstName==null || lastName==null || clientCode==null || startDate==null || endDate==null)
			{
				jsonObject.add("status", ERROR.INCOMPLETE_PARAMETERS);
				jsonObject.add("message", "Incomplete Parameters provided in request.");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			//log.info"customerFullName = " + firstName + " " + lastName);
			
			
			
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
			
			
			hql = "Select tp from Customer tp where " +
					"tp.createdAt >= '" + startDate + "' AND tp.createdAt <= '" + endDate +  "' AND " +
					"lower(tp.firstName) = '"+firstName.trim().toLowerCase() +"' AND " +
					"lower(tp.lastName) = '"+lastName.trim().toLowerCase() +"' AND tp.deletedAt is NULL AND " +
					"tp.client.clientCode = '"+clientCode+"'";
					//AND tp.isLeadPassenger = 1";
			//log.infohql);
			Collection<Customer> customerList = (Collection<Customer>)swpService.getAllRecordsByHQL(hql);
			if(customerList!=null && customerList.size()>0)
			{
				
			}
			else
			{
				jsonObject.add("status", ERROR.TRANSACTION_NOT_FOUND);
				jsonObject.add("message", "No lead passenger found matching the names provided.");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			//log.info"&&&&&");
			//log.infonew Gson().toJson(customerList.size()));
			List<Long> ptIds = new ArrayList<Long>();
			Iterator<Customer> custIter = customerList.iterator();
			JSONArray jsonArray = new JSONArray();
			
			while(custIter.hasNext())
			{
				Customer cust = custIter.next();
				JSONObject jsonObject1 = new JSONObject();
				if(cust.getPurchasedTrip()!=null && cust.getPurchasedTrip().getTransaction()!=null)
				{
					jsonObject1.put("customerName", cust.getFirstName() + (cust.getOtherName()==null ? "" : (" " + cust.getOtherName())) + " " + cust.getLastName());
					jsonObject1.put("customerMobileNumber", cust.getMobileNumber());
					jsonObject1.put("orderRef", cust.getPurchasedTrip().getTransaction().getOrderRef());
					jsonObject1.put("departureStationName", cust.getPurchasedTrip().getDepartureStation().getStationName());
					jsonObject1.put("departureTime", cust.getPurchasedTrip().getDepartureTime());
					jsonObject1.put("arrivalStationName", cust.getPurchasedTrip().getArrivalStation().getStationName());
					jsonObject1.put("arrivalTime", cust.getPurchasedTrip().getArrivalTime());
					jsonArray.put(jsonObject1);
				}
			}
			
			if(jsonArray.length()==0)
			{
				jsonObject.add("status", ERROR.PURCHASED_TRIP_NOT_FOUND);
				jsonObject.add("message", "No lead passenger found matching the names provided.");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			else
			{
				jsonObject.add("status", ERROR.GENERAL_SUCCESS);
				jsonObject.add("purchasedTrips", jsonArray.toString());
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
	
	
	
	
	
	public Response getVehicleSeatClasses(String deviceCode, String clientCode, String requestId, String ipAddress) throws JSONException
	{
		JsonObjectBuilder jsonObject = Json.createObjectBuilder();
		
    	
    	
		try{
			//log.inforequestId + "Test 1");
			if(clientCode==null)
			{
				//log.inforequestId + "Test 2");
				jsonObject.add("status", ERROR.INCOMPLETE_PARAMETERS);
				jsonObject.add("message", "Incomplete Parameters provided in request.");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			//log.inforequestId + "Test 3");
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
			
			hql = "Select tp from VehicleSeatClass tp where tp.client.clientCode = '" + clientCode 
					+ "' AND tp.deletedAt IS NULL";
			//log.infohql);
			Collection<VehicleSeatClass> vehicleSeatClasses = (Collection<VehicleSeatClass>)this.swpService.getAllRecordsByHQL(hql);
				
			if(vehicleSeatClasses!=null)
			{
				jsonObject.add("message", "Vehicle Seat Classes Found");
				jsonObject.add("status", ERROR.GENERAL_SUCCESS);
				jsonObject.add("vehicleSeatClassList", new Gson().toJson(vehicleSeatClasses));
				JsonObject jsonObj = jsonObject.build();
				//log.inforequestId + " -- " + jsonObj.toString());
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			jsonObject.add("message", "Vehicle Seat Classes Not Found");
			jsonObject.add("status", ERROR.VEHICLE_SEAT_CLASS_NOT_FOUND);
			JsonObject jsonObj = jsonObject.build();
			//log.inforequestId + " -- " + jsonObj.toString());
            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			
			
		}catch(Exception e)
		{
			log.warn(e); log.error("exception...", e);
			e.printStackTrace();
			JsonObject jsonObj = jsonObject.build();
            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
		}
	}
	
	
	
	public Response getVehicleSeatSections(String clientCode, String requestId, String ipAddress) throws JSONException
	{
		JsonObjectBuilder jsonObject = Json.createObjectBuilder();
		
    	
    	
		try{
			//log.inforequestId + "Test 1");
			if(clientCode==null)
			{
				//log.inforequestId + "Test 2");
				jsonObject.add("status", ERROR.INCOMPLETE_PARAMETERS);
				jsonObject.add("message", "Incomplete Parameters provided in request.");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			//log.inforequestId + "Test 3");
			swpService = serviceLocator.getSwpService();
			Application app = Application.getInstance(swpService);
			
			
			String hql = "Select tp from VehicleSeatSection tp where tp.client.clientCode = '" + clientCode 
					+ "' AND tp.deletedAt IS NULL";
			//log.infohql);
			Collection<VehicleSeatSection> vehicleSeatSections = (Collection<VehicleSeatSection>)this.swpService.getAllRecordsByHQL(hql);
				
			if(vehicleSeatSections!=null)
			{
				jsonObject.add("message", "Vehicle Seat Sections Found");
				jsonObject.add("status", ERROR.GENERAL_SUCCESS);
				jsonObject.add("vehicleSeatSectionList", new Gson().toJson(vehicleSeatSections));
				JsonObject jsonObj = jsonObject.build();
				//log.inforequestId + " -- " + jsonObj.toString());
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			jsonObject.add("message", "Vehicle Seat Sections Not Found");
			jsonObject.add("status", ERROR.VEHICLE_SEAT_SECTIONS_NOT_FOUND);
			JsonObject jsonObj = jsonObject.build();
			//log.inforequestId + " -- " + jsonObj.toString());
            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			
			
		}catch(Exception e)
		{
			log.warn(e); log.error("exception...", e);
			e.printStackTrace();
			JsonObject jsonObj = jsonObject.build();
            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
		}
	}
	
	
	
	public Response getVehicleSeats(String sectionCode, String clientCode, String requestId, String ipAddress) throws JSONException
	{
		JsonObjectBuilder jsonObject = Json.createObjectBuilder();
		
    	
    	
		try{
			//log.inforequestId + "Test 1");
			if(clientCode==null)
			{
				//log.inforequestId + "Test 2");
				jsonObject.add("status", ERROR.INCOMPLETE_PARAMETERS);
				jsonObject.add("message", "Incomplete Parameters provided in request.");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			//log.inforequestId + "Test 3");
			swpService = serviceLocator.getSwpService();
			Application app = Application.getInstance(swpService);
			
			
			
			if(sectionCode!=null)
			{
				String hql = "Select tp from VehicleSeatSection tp where tp.client.clientCode = '" + clientCode + "'";
				hql = hql + " AND tp.sectionCode = '"+sectionCode+"' ";
				hql = hql + " AND tp.deletedAt IS NULL";
				//log.infohql);
				VehicleSeatSection vehicleSeatSection = (VehicleSeatSection)this.swpService.getUniqueRecordByHQL(hql);
			
			
				if(vehicleSeatSection!=null)
				{
					hql = "Select tp from VehicleSeat tp where tp.client.clientCode = '" + clientCode +"'";
					hql = hql + " AND tp.vehicleSeatSection.vehicleSeatSectionId = "+ vehicleSeatSection.getVehicleSeatSectionId() +" ";
					hql = hql + " AND tp.deletedAt IS NULL";
					//log.infohql);
					Collection<VehicleSeat> vehicleSeats = (Collection<VehicleSeat>)this.swpService.getAllRecordsByHQL(hql);
						
					if(vehicleSeats!=null)
					{
						jsonObject.add("message", "Vehicle Seats Found");
						jsonObject.add("status", ERROR.GENERAL_SUCCESS);
						jsonObject.add("vehicleSeatSection", new Gson().toJson(vehicleSeatSection));
						jsonObject.add("vehicleSeatList", new Gson().toJson(vehicleSeats));
						JsonObject jsonObj = jsonObject.build();
						//log.inforequestId + " -- " + jsonObj.toString());
			            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
					}
				}
				jsonObject.add("message", "Vehicle Seats Not Found");
				jsonObject.add("status", ERROR.VEHICLE_SEAT_SECTIONS_NOT_FOUND);
				JsonObject jsonObj = jsonObject.build();
				//log.inforequestId + " -- " + jsonObj.toString());
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			else
			{
				String hql = "Select tp from VehicleSeat tp where tp.client.clientCode = '" + clientCode;
				hql = hql + "' AND tp.deletedAt IS NULL";
				//log.infohql);
				Collection<VehicleSeat> vehicleSeats = (Collection<VehicleSeat>)this.swpService.getAllRecordsByHQL(hql);
					
				if(vehicleSeats!=null)
				{
					jsonObject.add("message", "Vehicle Seats Found");
					jsonObject.add("status", ERROR.GENERAL_SUCCESS);
					jsonObject.add("vehicleSeatList", new Gson().toJson(vehicleSeats));
					JsonObject jsonObj = jsonObject.build();
					//log.inforequestId + " -- " + jsonObj.toString());
		            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
				}
			
				jsonObject.add("message", "Vehicle Seats Not Found");
				jsonObject.add("status", ERROR.VEHICLE_SEAT_SECTIONS_NOT_FOUND);
				JsonObject jsonObj = jsonObject.build();
				//log.inforequestId + " -- " + jsonObj.toString());
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

	public Response createNewVehicleTripZonePrice(String token,
			String priceDetails, String requestId, String ipAddress,
			String clientCode) {
		// TODO Auto-generated method stub
		JsonObjectBuilder jsonObject = Json.createObjectBuilder();
		try{
			if(clientCode==null)
			{
				//log.inforequestId + "Test 2");
				jsonObject.add("status", ERROR.INCOMPLETE_PARAMETERS);
				jsonObject.add("message", "Incomplete Parameters provided in request.");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			//log.inforequestId + "Test 3");
			if(priceDetails==null || clientCode==null)
			{
				jsonObject.add("status", ERROR.INCOMPLETE_PARAMETERS);
				jsonObject.add("message", "Incomplete Parameters provided in request.");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			//log.info">>>>>>>>>>>>>===" + (priceDetails + " - " + clientCode));
			
			
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
			//log.info"------>>>>" + RoleType.ADMIN_STAFF.name());
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
				String hql = "select tp from User tp where tp.username = '" + username + "' AND " +
						"tp.userStatus = " + UserStatus.ACTIVE.ordinal() + " AND tp.deletedAt IS NULL";
				//log.inforequestId + "hql ==" + hql);
				user = (User)this.swpService.getUniqueRecordByHQL(hql);
				
				
				if(user==null)
				{

					//log.inforequestId + "user IS NULL");
					//log.inforequestId + "user firstname = " + user.getFirstName());
					//log.inforequestId + "user lastname = " + user.getLastName());
					jsonObject.add("status", ERROR.INVALID_PUBLIC_HOLIDAY_CREATION_PRIVILEDGES);
					jsonObject.add("message", "Invalid Client Creation Priviledges");
					JsonObject jsonObj = jsonObject.build();
		            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
				}
				//roleCode = user.getRoleCode();
			}
			
			String hql = "Select tp from Client tp where tp.clientCode = '" +clientCode+ "' AND tp.deletedAt IS NULL";
			Client client = (Client)this.swpService.getUniqueRecordByHQL(hql);
			
			
			JSONObject priceDetailsJS = new JSONObject(priceDetails);
			JSONArray destinationTripZone = priceDetailsJS.getJSONArray("destinationTripZone");
			JSONArray seatClass = priceDetailsJS.getJSONArray("seatClass");
			JSONArray priceTypes = priceDetailsJS.getJSONArray("priceType");
			JSONArray ticket_prices = priceDetailsJS.getJSONArray("ticket_price");
			
			
			
			for(int i=0; i<destinationTripZone.length(); i++)
			{
				String zoneCode = destinationTripZone.getString(i);
				hql = "Select tp from TripZone tp where tp.zoneCode = '"+zoneCode+"' AND tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL";
				TripZone dtz = (TripZone)swpService.getUniqueRecordByHQL(hql);
				
				Long seatClass_ = seatClass.getLong(i);
				hql = "Select tp from VehicleSeatClass tp where tp.vehicleSeatClassId = "+seatClass_+" AND tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL";
				VehicleSeatClass seatClassEntry = (VehicleSeatClass)swpService.getUniqueRecordByHQL(hql);
				
				PriceType priceType = PriceType.valueOf(priceTypes.getString(i));
				Double ticket_price = ticket_prices.getDouble(i);
				
				if(dtz!=null && seatClassEntry!=null && priceType!=null)
				{
					VehicleTripPrice vtp  = new VehicleTripPrice();
					vtp.setClient(client);
					vtp.setCreatedAt(new Date());
					vtp.setUpdatedAt(new Date());
					vtp.setFinalDestinationTripZone(dtz);
					vtp.setPriceType(priceType);
					vtp.setRouteOrder(i+1);
					vtp.setAmount(ticket_price);
					vtp.setVehicleSeatClass(seatClassEntry);
					vtp = (VehicleTripPrice)swpService.createNewRecord(vtp);
					

					AuditTrail ad = UtilityHelper.createAuditTrailEntry(ipAddress, RequestType.NEW_VEHICLE_TRIP_PRICE, requestId, this.swpService, 
							verifyJ.has("username") ? verifyJ.getString("username") : null, vtp.getVehicleTripPriceId(), VehicleTripPrice.class.getName(), 
							"New Vehicle Trip Price. Trip Zone: " + dtz.getZoneName() + " | Created By: " + user.getFirstName() + " " + user.getLastName(), clientCode);
				}
			}
			//log.info"233...>>>>>>>>>>>>>===");
			
			jsonObject.add("message", "Vehicle Trip Price(s) created successfully");
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
	
	
	
	public Response getListTripZonePrices(String token, String clientCode,
			String requestId, String ipAddress)
	{
		// TODO Auto-generated method stub
    	JsonObjectBuilder jsonObject = Json.createObjectBuilder();
    	
		try{
			if(token==null || clientCode==null)
			{
				jsonObject.add("status", ERROR.INCOMPLETE_PARAMETERS);
				jsonObject.add("message", "Incomplete Parameters provided in request.");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			swpService = serviceLocator.getSwpService();
			Application app = Application.getInstance(swpService);
			
			String hql = "Select tp from VehicleTripPrice tp where tp.client.clientCode = '" + clientCode + "' AND tp.deletedAt IS NULL";
			Collection<VehicleTripPrice> tripZonePrices = (Collection<VehicleTripPrice>)swpService.getAllRecordsByHQL(hql);
			
			
			jsonObject.add("message", "Trip Zone Price Listing");
			jsonObject.add("tripZonePriceList", new Gson().toJson(tripZonePrices));
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
	
	
	
	public Response listTripCustomers(String token, String vehicleScheduleCode, String requestId, String ipAddress, String clientCode) {
		// TODO Auto-generated method stub
		JsonObjectBuilder jsonObject = Json.createObjectBuilder();
		try
		{
			if(clientCode==null || vehicleScheduleCode==null)
			{
				jsonObject.add("status", ERROR.INCOMPLETE_PARAMETERS);
				jsonObject.add("message", "Incomplete parameters provided. Provide clientCode, vehicleScheduleCode");
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
			//log.info"3");
			
			String username = verifyJ.has("username") ? verifyJ.getString("username") : null;
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
				User user = (User)this.swpService.getUniqueRecordByHQL("select tp from User tp where tp.username = '" + username + "' AND tp.deletedAt IS NULL AND " +
						"tp.userStatus = " + UserStatus.ACTIVE.ordinal());
				//roleCode = user.getRoleCode().name();
			}
			
			
			
			if(!roleCode.equals(RoleType.ADMIN_STAFF) && !roleCode.equals(RoleType.OPERATOR))
			{
				//log.info"5");
				jsonObject.add("status", ERROR.INSUFFICIENT_PRIVILEDGES);
				jsonObject.add("message", "You do not have the priviledges to carry out this action");
				JsonObject jsonObj = jsonObject.build();
				return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			
			String hql = "Select tp from Customer tp WHERE tp.deletedAt IS NULL AND tp.client.clientCode = '"+clientCode+"'";
			hql = hql + " AND tp.purchasedTrip.vehicleSchedule.scheduleStationCode.scheduleStationCode = '"+ vehicleScheduleCode +"'";
			Collection<Customer> customerList = (Collection<Customer>)this.swpService.getAllRecordsByHQL(hql);
			if(customerList!=null && customerList.size()>0)
			{
				jsonObject.add("customerList", new Gson().toJson(customerList));
				jsonObject.add("status", ERROR.GENERAL_SUCCESS);
				jsonObject.add("message", "Customer List Pulled Sucessfully");
				
				JsonObject jsonObj = jsonObject.build();
				return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			else
			{
				jsonObject.add("status", ERROR.EMPTY_USER_LIST);
				jsonObject.add("message", "No customers registered for this trip currently");
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
	
	
	
	public Response listTripSeats(String token, Integer pageIndex, String vehicleScheduleCode, String seatStatus, String requestId, String ipAddress, String clientCode) {
		// TODO Auto-generated method stub
		JsonObjectBuilder jsonObject = Json.createObjectBuilder();
		try
		{
			if(pageIndex==null)
			{
				pageIndex = 0;
			}
			if(clientCode==null || vehicleScheduleCode==null || seatStatus==null)
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
			//log.info"3");
			
			String username = verifyJ.has("username") ? verifyJ.getString("username") : null;
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
				User user = (User)this.swpService.getUniqueRecordByHQL("select tp from User tp where tp.username = '" + username + "' AND tp.deletedAt IS NULL AND " +
						"tp.userStatus = " + UserStatus.ACTIVE.ordinal());
				//roleCode = user.getRoleCode().name();
			}
			
			
			
			if(!roleCode.equals(RoleType.ADMIN_STAFF) && !roleCode.equals(RoleType.OPERATOR))
			{
				//log.info"5");
				jsonObject.add("status", ERROR.INSUFFICIENT_PRIVILEDGES);
				jsonObject.add("message", "You do not have the priviledges to carry out this action");
				JsonObject jsonObj = jsonObject.build();
				return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			
			
			SeatAvailabilityStatus seatAvailabilityStatus = SeatAvailabilityStatus.valueOf(seatStatus);
			
			String hql = "Select tp from ScheduleStationSeatAvailability tp WHERE tp.deletedAt IS NULL AND tp.scheduleStationSeatSection.client.clientCode = '"+clientCode+"'";
			hql = hql + " AND tp.scheduleStationSeatSection.originScheduleStation.scheduleStationCode.scheduleStationCode = '"+ vehicleScheduleCode + "'";
			hql = hql + " AND tp.seatAvailabilityStatus = " + seatAvailabilityStatus.ordinal();
			Collection<ScheduleStationSeatAvailability> scheduleStationSeatAvailabilityList = (Collection<ScheduleStationSeatAvailability>)this.swpService.getAllRecordsByHQL(hql, pageIndex, 50);
			if(scheduleStationSeatAvailabilityList!=null && scheduleStationSeatAvailabilityList.size()>0)
			{
				hql = "Select tp from VehicleSchedule tp where tp.scheduleStationCode.scheduleStationCode = '"+ vehicleScheduleCode + "' AND tp.deletedAt IS NULL";
				VehicleSchedule vs = (VehicleSchedule)swpService.getUniqueRecordByHQL(hql);
				
				jsonObject.add("tripSeatList", new Gson().toJson(scheduleStationSeatAvailabilityList));
				jsonObject.add("vehicleSchedule", new Gson().toJson(vs));
				jsonObject.add("status", ERROR.GENERAL_SUCCESS);
				jsonObject.add("message", "Purchased Trip Seats List Pulled Sucessfully");
				
				JsonObject jsonObj = jsonObject.build();
				return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			else
			{
				jsonObject.add("status", ERROR.PURCHASED_TRIP_SEATS_EMPTY);
				jsonObject.add("message", "No Purchased Trip seats for this trip currently");
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

	
	
	/*String seatClass, Integer adultPassenger, 
			Integer childPassenger, Integer seniorPassenger, Integer disabledPassenger,*/ 
			
	public Response listCheckAvailableGroupTrip(String deviceCode, String requestCode, String requestId, String ipAddress,
			String clientCode, String token) {
		// TODO Auto-generated method stub
		JsonObjectBuilder jsonObject = Json.createObjectBuilder();
		try
		{
			if(clientCode==null || deviceCode==null || requestCode==null)
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
			//log.info"3");
			
			String username = verifyJ.has("username") ? verifyJ.getString("username") : null;
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
				User user = (User)this.swpService.getUniqueRecordByHQL("select tp from User tp where tp.username = '" + username + "' AND tp.deletedAt IS NULL AND " +
						"tp.userStatus = " + UserStatus.ACTIVE.ordinal());
				//roleCode = user.getRoleCode().name();
			}
			
			
			
			if(!roleCode.equals(RoleType.ADMIN_STAFF) && !roleCode.equals(RoleType.MANAGER) && !roleCode.equals(RoleType.OPERATOR))
			{
				//log.info"5");
				jsonObject.add("status", ERROR.INSUFFICIENT_PRIVILEDGES);
				jsonObject.add("message", "You do not have the priviledges to carry out this action");
				JsonObject jsonObj = jsonObject.build();
				return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			
			
			
			String hql = "Select tp from GroupTripRequest tp where tp.client.clientCode = '"+ clientCode +"' AND tp.requestCode = '"+requestCode+"' AND tp.deletedAt IS NULL";
			GroupTripRequest groupTripRequest= (GroupTripRequest)this.swpService.getUniqueRecordByHQL(hql);
			if(groupTripRequest!=null)
			{
				if(groupTripRequest.getGroupTripRequestStatus().equals(GroupTripRequestStatus.PENDING))
				{
					String departDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(groupTripRequest.getDepartureDate());
					//log.info"departDate...." + departDate);
					String returnTime = null;
					boolean returnTrip = false;
					if(groupTripRequest.getReturnDate()!=null && groupTripRequest.getTripType().equals(TripType.RETURN))
					{
						returnTime = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(groupTripRequest.getReturnDate());
						returnTrip = true;
					}
					
					String passengerDetails = "";
					JSONObject jsPassenger = new JSONObject();
					jsPassenger.put("DISABLED", new JSONArray());
					jsPassenger.put("ADULT", new JSONArray());
					jsPassenger.put("CHILD", new JSONArray());
					jsPassenger.put("SENIOR", new JSONArray());
					
					Integer adultPassenger = groupTripRequest.getAdultPassengerCount();
					Integer childPassenger = groupTripRequest.getChildPassengerCount();
					Integer seniorPassenger = groupTripRequest.getSeniorPassengerCount();
					Integer disabledPassenger = groupTripRequest.getDisabledPassengerCount();
					String seatClass = groupTripRequest.getStationCabinType().getVehicleSeatClassCode();
					
					if(adultPassenger!=null && adultPassenger>0)
					{
						JSONArray holder = jsPassenger.getJSONArray("ADULT");
						for(int i=0; i<adultPassenger; i++)
						{
							JSONObject seatDetail = new JSONObject();
							seatDetail.put("seatClass", seatClass);
							seatDetail.put("seatLocation", "WINDOW");
							holder.put(seatDetail);
						}
						jsPassenger.put("ADULT", holder);
					}
					
					if(childPassenger!=null && childPassenger>0)
					{
						JSONArray holder = jsPassenger.getJSONArray("CHILD");
						for(int i=0; i<childPassenger; i++)
						{
							JSONObject seatDetail = new JSONObject();
							seatDetail.put("seatClass", seatClass);
							seatDetail.put("seatLocation", "WINDOW");
							holder.put(seatDetail);
						}
						jsPassenger.put("CHILD", holder);
					}
	
					if(seniorPassenger!=null && seniorPassenger>0)
					{
						JSONArray holder = jsPassenger.getJSONArray("SENIOR");
						for(int i=0; i<seniorPassenger; i++)
						{
							JSONObject seatDetail = new JSONObject();
							seatDetail.put("seatClass", seatClass);
							seatDetail.put("seatLocation", "WINDOW");
							holder.put(seatDetail);
						}
						jsPassenger.put("SENIOR", holder);
					}
					
	
					if(disabledPassenger!=null && disabledPassenger>0)
					{
						JSONArray holder = jsPassenger.getJSONArray("DISABLED");
						for(int i=0; i<disabledPassenger; i++)
						{
							JSONObject seatDetail = new JSONObject();
							seatDetail.put("seatClass", seatClass);
							seatDetail.put("seatLocation", "WINDOW");
							holder.put(seatDetail);
						}
						jsPassenger.put("DISABLED", holder);
					}
					
	
					passengerDetails = jsPassenger.toString();
					String searchStr = (String)(searchAvailableTrips(deviceCode, token, groupTripRequest.getDepartingStation().getStationCode(), 
							groupTripRequest.getArrivingStation().getStationCode(), passengerDetails, 
							departDate, returnTime, 72,  groupTripRequest.getStationCabinType().getVehicleSeatClassCode(), 
							returnTrip, requestId, ipAddress, clientCode).getEntity());
					
					jsonObject.add("groupTripRequest", new Gson().toJson(groupTripRequest));
					jsonObject.add("status", ERROR.GENERAL_SUCCESS);
					jsonObject.add("availableTripsResponse", searchStr);
					jsonObject.add("message", "Available trips");
					
					JsonObject jsonObj = jsonObject.build();
					return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
				}
				else if(groupTripRequest.getGroupTripRequestStatus().equals(GroupTripRequestStatus.APPROVED))
				{
					Collection<PurchasedTrip> pts = null;
					pts = (Collection<PurchasedTrip>)swpService.getAllRecordsByHQL("Select tp from PurchasedTrip tp where " +
							"tp.groupTripRequest.groupTripRequestId = " + groupTripRequest.getGroupTripRequestId() + " AND tp.deletedAt IS NULL");
					Iterator<PurchasedTrip> itPt = pts.iterator();
					List<Long> ptId = new ArrayList<Long>();
					while(itPt.hasNext())
					{
						ptId.add(itPt.next().getPurchasedTripId());
					}
					Collection<PurchasedTripSeat> ptseats = (Collection<PurchasedTripSeat>)swpService.getAllRecordsByHQL("Select tp from PurchasedTripSeat tp where " +
							"tp.purchasedTrip.purchasedTripId IN (" + (StringUtils.join(ptId, ',')) + ") AND tp.deletedAt IS NULL");
					jsonObject.add("groupTripRequest", new Gson().toJson(groupTripRequest));
					jsonObject.add("status", ERROR.GENERAL_SUCCESS);
					jsonObject.add("message", "Purchased Group Trip Details");
					jsonObject.add("purchasedTrips", new Gson().toJson(pts));
					jsonObject.add("purchasedTripSeats", new Gson().toJson(ptseats));
					
					JsonObject jsonObj = jsonObject.build();
					return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
				}
				else
				{
					jsonObject.add("groupTripRequest", new Gson().toJson(groupTripRequest));
					jsonObject.add("status", ERROR.GENERAL_SUCCESS);
					jsonObject.add("message", "Canceled Group Trip Details");
					
					JsonObject jsonObj = jsonObject.build();
					return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
				}
			}
			else
			{
				jsonObject.add("status", ERROR.GROUP_TRIP_REQUEST_NOT_FOUND);
				jsonObject.add("message", "Group trip request not found");
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

	public Response getTransactionList(String token, String transactionStatus, String groupBy, String reportType,  
			String startDate, String endDate, String userCode, String zoneCode,
			String departureStationCode, String vehicleCode, String cabinCode,
			String paymentMeans, String vendorCode, String serviceType,
			String requestId, String ipAddress, String clientCode) {
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
			//log.inforequestId + "username ==" + (username==null ? "" : username));
			String roleCode_ = verifyJ.has("roleCode") ? verifyJ.getString("roleCode") : null;
			//log.inforequestId + "roleCode_ ==" + (roleCode_==null ? "" : roleCode_));
			User user = null;
			RoleType roleCode = null;
			
			if(roleCode_ != null)
			{
				roleCode = RoleType.valueOf(roleCode_);
			}

			String hql = "";
			
			if(groupBy!=null && groupBy.equals("DAILY"))
			{
				hql = "Select SUM(tp.transactionAmount) as totalSum, DATE(tp.createdAt) as day_ from Transaction tp where tp.deletedAt IS NULL AND tp.client.clientCode = '"+clientCode+"'";
			}
			else if(groupBy!=null && groupBy.equals("WEEK"))
			{
				hql = "Select SUM(tp.transactionAmount) as totalSum, WEEK(tp.createdAt) as week_ from Transaction tp where tp.deletedAt IS NULL AND tp.client.clientCode = '"+clientCode+"'";
			}
			else if(groupBy!=null && groupBy.equals("MONTH"))
			{
				hql = "Select SUM(tp.transactionAmount) as totalSum, MONTH(tp.createdAt) as month_ from Transaction tp where tp.deletedAt IS NULL AND tp.client.clientCode = '"+clientCode+"'";
			}
			else if(groupBy!=null && groupBy.equals("ANNUAL"))
			{
				hql = "Select SUM(tp.transactionAmount) as totalSum, YEAR(tp.createdAt) as year_ from Transaction tp where tp.deletedAt IS NULL AND tp.client.clientCode = '"+clientCode+"'";
			}
			else 
			{
				hql = "Select tp from Transaction tp where tp.deletedAt IS NULL AND tp.client.clientCode = '"+clientCode+"'";
			}
			
			Vendor vendor = null;
			Wallet wallet = null;
			if(username!=null)
			{
				user = (User)this.swpService.getUniqueRecordByHQL("select tp from User tp where tp.username = '" + username + "' AND tp.deletedAt IS NULL AND " +
						"tp.userStatus = " + UserStatus.ACTIVE.ordinal());
				
				if(user!=null && roleCode.equals(RoleType.VENDOR))
				{
					String hql_ = "Select tp from Vendor tp where tp.vendorId = " + user.getVendor().getVendorId() + " AND tp.deletedAt IS NULL AND " +
							"tp.client.clientId = "+user.getClient().getClientId();
					vendor = (Vendor)swpService.getUniqueRecordByHQL(hql_);
					
					if(vendor!=null)
					{
						hql = hql + " AND (tp.drVendorId = " + vendor.getVendorId() + " OR tp.crVendorId = "+ vendor.getVendorId() +")";
					}
					if(userCode!=null)
					{
						hql_ = "Select tp from UserRole tp where tp.user.uniqueId = '"+userCode+"' AND tp.roleCode = " + RoleType.VENDOR.ordinal();
						UserRole userRole = (UserRole)swpService.getUniqueRecordByHQL(hql_);
						if(userRole!=null)
							hql = hql + " AND tp.userId = " + userRole.getUser().getUserId() + "'";
					}
				}
				else if(user!=null && (roleCode.equals(RoleType.OPERATOR) || roleCode.equals(RoleType.ADMIN_STAFF)))
				{
					if(vendorCode!=null)
					{
						String hql_ = "Select tp from Vendor tp where tp.vendorCode = '" + vendorCode + "' AND tp.deletedAt IS NULL AND " +
								"tp.client.clientId = "+user.getClient().getClientId();
						vendor = (Vendor)swpService.getUniqueRecordByHQL(hql_);
						
						if(vendor!=null)
						{
							hql = hql + " AND (tp.drVendorId = " + vendor.getVendorId() + " OR tp.crVendorId = "+ vendor.getVendorId() +")";
						}
					}

					if(userCode!=null)
					{
						String hql_ = "Select tp from User tp where tp.uniqueId = '"+userCode+"'";
						User user_ = (User)swpService.getUniqueRecordByHQL(hql_);
						if(user_!=null)
							hql = hql + " AND tp.userId = " + user_.getUserId();
					}
					
				}
			}
			//log.info"transaction sql == " + hql);

            
            boolean reportTypeCheck = false;
			TripZone tripZone  = null;
            if(zoneCode!=null)
            {
            	System.out.print("zoneCode Check");
            	tripZone = (TripZone)swpService.getUniqueRecordByHQL("Select tp from TripZone tp where tp.zoneCode = '"+zoneCode+"' AND tp.deletedAt is NULL AND tp.client.clientCode = '"+clientCode+"'");
            	
            	if(reportType!=null && reportType.equals("COURIER"))
            	{
            		reportTypeCheck = true;
            		String hql_ = "Select tp.station.stationId from TripZoneStation tp where tp.tripZone.tripZoneId = " + tripZone.getTripZoneId() + " AND tp.deletedAt IS NULL";
                	List<Long> tripZoneStations = (List<Long>)swpService.getAllRecordsByHQL(hql_);
                	hql_ = "Select tp.transaction.transactionId from Shipment tp where " +
                			"tp.departureScheduleStation.station.stationId IN ("+ StringUtils.join(tripZoneStations, ',') +") AND tp.deletedAt IS NULL " +
                					"AND tp.transaction IS NOT NULL GROUP BY tp.transaction.transactionId";
                	List<Long> shipmentTransactionIds = (List<Long>)swpService.getAllRecordsByHQL(hql_);
                	//log.info"tripTransactionIds size == " + shipmentTransactionIds.size());
                	
                	hql = hql + " AND tp.transactionId IN ("+StringUtils.join(shipmentTransactionIds, ',')+")";
            		
            	}
            	else if(reportType!=null && reportType.equals("PASSENGER"))
            	{
            		reportTypeCheck = true;
            		String hql_ = "Select tp.station.stationId from TripZoneStation tp where tp.tripZone.tripZoneId = " + tripZone.getTripZoneId() + " AND tp.deletedAt IS NULL";
                	List<Long> tripZoneStations = (List<Long>)swpService.getAllRecordsByHQL(hql_);
                	hql_ = "Select tp.transaction.transactionId from PurchasedTrip tp where tp.departureStation.stationId IN ("+ StringUtils.join(tripZoneStations, ',') +") " +
                			"AND tp.deletedAt IS NULL AND tp.transaction IS NOT NULL GROUP BY tp.transaction.transactionId";
                	List<Long> tripTransactionIds = (List<Long>)swpService.getAllRecordsByHQL(hql_);
                	//log.info"tripTransactionIds size == " + tripTransactionIds.size());
                	hql = hql + " AND tp.transactionId IN ("+StringUtils.join(tripTransactionIds, ',')+")";
            	}
            	else
            	{
            		reportTypeCheck = true;
            		String hql_ = "Select tp.station.stationId from TripZoneStation tp where tp.tripZone.tripZoneId = " + tripZone.getTripZoneId() + " AND tp.deletedAt IS NULL";
                	List<Long> tripZoneStations = (List<Long>)swpService.getAllRecordsByHQL(hql_);
                	hql_ = "Select tp.transaction.transactionId from PurchasedTrip tp where tp.departureStation.stationId IN ("+ StringUtils.join(tripZoneStations, ',') +") " +
                			"AND tp.deletedAt IS NULL AND tp.transaction IS NOT NULL GROUP BY tp.transaction.transactionId";
                	List<Long> tripTransactionIds = (List<Long>)swpService.getAllRecordsByHQL(hql_);
                	//log.info"tripTransactionIds size == " + tripTransactionIds.size());
                	hql_ = "Select tp.transaction.transactionId from Shipment tp where tp.departureScheduleStation.station.stationId IN ("+ StringUtils.join(tripZoneStations, ',') +") " +
                			"AND tp.deletedAt IS NULL AND tp.transaction IS NOT NULL GROUP BY tp.transaction.transactionId";
                	List<Long> shipmentTransactionIds = (List<Long>)swpService.getAllRecordsByHQL(hql_);
                	//log.info"tripTransactionIds size == " + shipmentTransactionIds.size());
                	
                	tripTransactionIds.addAll(shipmentTransactionIds);
                	//log.info"new tripTransactionIds size == " + tripTransactionIds.size());
                	
                	hql = hql + " AND tp.transactionId IN ("+StringUtils.join(tripTransactionIds, ',')+")";
            	}
            }
            
            if(departureStationCode!=null)
            {
            	System.out.print("departureStationCode Check");
            	if(reportType!=null && reportType.equals("COURIER"))
            	{
            		reportTypeCheck = true;
            		String hql_ = "Select tp.transaction.transactionId from Shipment tp where " +
            				"tp.departureScheduleStation.station.stationCode = '"+ departureStationCode +"' AND tp.deletedAt IS NULL AND tp.transaction IS NOT NULL " +
            						"GROUP BY tp.transaction.transactionId";
                	List<Long> shipmentTransactionIds = (List<Long>)swpService.getAllRecordsByHQL(hql_);
                	//log.info"tripTransactionIds size == " + shipmentTransactionIds.size());
                	//log.info"new tripTransactionIds size == " + shipmentTransactionIds.size());
                	
                	hql = hql + " AND tp.transactionId IN ("+StringUtils.join(shipmentTransactionIds, ',')+")";
            		
            	}
            	else if(reportType!=null && reportType.equals("PASSENGER"))
            	{
            		reportTypeCheck = true;
            		String hql_ = "Select tp.transaction.transactionId from PurchasedTrip tp where tp.departureStation.stationCode = '"+ departureStationCode +"' " +
            				"AND tp.deletedAt IS NULL AND tp.transaction IS NOT NULL GROUP BY tp.transaction.transactionId";
                	List<Long> tripTransactionIds = (List<Long>)swpService.getAllRecordsByHQL(hql_);
                	//log.info"tripTransactionIds size == " + tripTransactionIds.size());
                	hql = hql + " AND tp.transactionId IN ("+StringUtils.join(tripTransactionIds, ',')+")";
            	}
            	else
            	{
            		reportTypeCheck = true;
                	String hql_ = "Select tp.transaction.transactionId from PurchasedTrip tp where " +
                			"tp.departureStation.stationCode = '"+ departureStationCode +"' AND tp.deletedAt IS NULL AND tp.transaction IS NOT NULL " +
                					"GROUP BY tp.transaction.transactionId";
                	List<Long> tripTransactionIds = (List<Long>)swpService.getAllRecordsByHQL(hql_);
                	//log.info"tripTransactionIds size == " + tripTransactionIds.size());
                	hql_ = "Select tp.transaction.transactionId from Shipment tp where " +
                			"tp.departureScheduleStation.station.stationCode = '"+ departureStationCode +"' AND tp.deletedAt IS NULL AND tp.transaction IS NOT NULL GROUP BY tp.transaction.transactionId";
                	List<Long> shipmentTransactionIds = (List<Long>)swpService.getAllRecordsByHQL(hql_);
                	//log.info"tripTransactionIds size == " + shipmentTransactionIds.size());
                	
                	tripTransactionIds.addAll(shipmentTransactionIds);
                	//log.info"new tripTransactionIds size == " + tripTransactionIds.size());
                	
                	hql = hql + " AND tp.transactionId IN ("+StringUtils.join(tripTransactionIds, ',')+")";
            	}
            }
            
            if(vehicleCode!=null)
            {
            	System.out.print("vehicleCode check");
            	if(reportType!=null && reportType.equals("COURIER"))
            	{
            		reportTypeCheck =true;
            		String hql_ = "Select tp.transaction.transactionId from Shipment tp where " +
            				"tp.vehicleSchedule.vehicle.vehicleCode = '"+ departureStationCode +"' AND tp.deletedAt IS NULL AND tp.transaction IS NOT NULL " +
            						"GROUP BY tp.transaction.transactionId";
                	List<Long> shipmentTransactionIds = (List<Long>)swpService.getAllRecordsByHQL(hql_);
                	//log.info"tripTransactionIds size == " + shipmentTransactionIds.size());
                	
                	hql = hql + " AND tp.transactionId IN ("+StringUtils.join(shipmentTransactionIds, ',')+")";
            	}
            	else if(reportType!=null && reportType.equals("PASSENGER"))
            	{
            		reportTypeCheck = true;
            		String hql_ = "Select tp.transaction.transactionId from PurchasedTrip tp where " +
            				"tp.vehicleSchedule.vehicle.vehicleCode = '"+ vehicleCode +"' AND tp.deletedAt IS NULL AND tp.transaction IS NOT NULL " +
            						"GROUP BY tp.transaction.transactionId";
                	List<Long> tripTransactionIds = (List<Long>)swpService.getAllRecordsByHQL(hql_);
                	//log.info"tripTransactionIds size == " + tripTransactionIds.size());
                	
                	hql = hql + " AND tp.transactionId IN ("+StringUtils.join(tripTransactionIds, ',')+")";
            	}
            	else
            	{
            		reportTypeCheck = true;
            		String hql_ = "Select tp.transaction.transactionId from PurchasedTrip tp where " +
            				"tp.vehicleSchedule.vehicle.vehicleCode = '"+ vehicleCode +"' AND tp.deletedAt IS NULL AND tp.transaction IS NOT NULL " +
            						"GROUP BY tp.transaction.transactionId";
                	List<Long> tripTransactionIds = (List<Long>)swpService.getAllRecordsByHQL(hql_);
                	//log.info"tripTransactionIds size == " + tripTransactionIds.size());
                	hql_ = "Select tp.transaction.transactionId from Shipment tp where tp.vehicleSchedule.vehicle.vehicleCode = '"+ departureStationCode +"' " +
                			"AND tp.transaction IS NOT NULL AND tp.deletedAt IS NULL GROUP BY tp.transaction.transactionId";
                	List<Long> shipmentTransactionIds = (List<Long>)swpService.getAllRecordsByHQL(hql_);
                	//log.info"tripTransactionIds size == " + shipmentTransactionIds.size());
                	
                	tripTransactionIds.addAll(shipmentTransactionIds);
                	//log.info"new tripTransactionIds size == " + tripTransactionIds.size());
                	
                	hql = hql + " AND tp.transactionId IN ("+StringUtils.join(tripTransactionIds, ',')+")";
            	}
            }
            
            if(cabinCode!=null)
            {
            	System.out.print("cabinCode check");
            	
            	if(reportType!=null && reportType.equals("COURIER"))
            	{
            		reportTypeCheck = true;
            		String hql_ = "Select tp.shipment.transaction.transactionId from CourierCabinShipment tp where " +
            				"tp.scheduleStationCourierSection.vehicleSeatSection.sectionCode = '"+ cabinCode +"' AND tp.shipment.transaction IS NOT NULL AND " +
            						"tp.deletedAt IS NULL GROUP BY tp.shipment.transaction.transactionId";
                	List<Long> shipmentTransactionIds = (List<Long>)swpService.getAllRecordsByHQL(hql_);
                	//log.info"tripTransactionIds size == " + shipmentTransactionIds.size());
                	hql = hql + " AND tp.transactionId IN ("+StringUtils.join(shipmentTransactionIds, ',')+")";
            	}
            	else if(reportType!=null && reportType.equals("PASSENGER"))
            	{
            		reportTypeCheck = true;
            		String hql_ = "Select tp.purchasedTrip.transaction.transactionId from PurchasedTripSeat tp where " +
            				"tp.vehicleSeat.vehicleSeatSection.sectionCode = '"+ cabinCode +"' AND tp.deletedAt IS NULL AND tp.purchasedTrip.transaction IS NOT NULL " +
            						"GROUP BY tp.purchasedTrip.transaction.transactionId";
                	List<Long> tripTransactionIds = (List<Long>)swpService.getAllRecordsByHQL(hql_);
                	//log.info"tripTransactionIds size == " + tripTransactionIds.size());
                	hql = hql + " AND tp.transactionId IN ("+StringUtils.join(tripTransactionIds, ',')+")";
            	}
            	else
            	{
            		reportTypeCheck = true;
            		String hql_ = "Select tp.purchasedTrip.transaction.transactionId from PurchasedTripSeat tp where " +
            				"tp.vehicleSeat.vehicleSeatSection.sectionCode = '"+ cabinCode +"' AND tp.deletedAt IS NULL AND tp.purchasedTrip.transaction IS NOT NULL GROUP BY tp.purchasedTrip.transaction.transactionId";
                	List<Long> tripTransactionIds = (List<Long>)swpService.getAllRecordsByHQL(hql_);
                	//log.info"tripTransactionIds size == " + tripTransactionIds.size());
                	hql_ = "Select tp.shipment.transaction.transactionId from CourierCabinShipment tp where " +
                			"tp.scheduleStationCourierSection.vehicleSeatSection.sectionCode = '"+ cabinCode +"' AND tp.deletedAt IS NULL AND tp.shipment.transaction IS NOT NULL" +
        					" GROUP BY tp.shipment.transaction.transactionId";
                	List<Long> shipmentTransactionIds = (List<Long>)swpService.getAllRecordsByHQL(hql_);
                	//log.info"tripTransactionIds size == " + shipmentTransactionIds.size());
                	
                	tripTransactionIds.addAll(shipmentTransactionIds);
                	//log.info"new tripTransactionIds size == " + tripTransactionIds.size());
                	
                	hql = hql + " AND tp.transactionId IN ("+StringUtils.join(tripTransactionIds, ',')+")";
            	}
            	
            	
            }
            
            
            if(reportTypeCheck==false)
            {
            	if(reportType!=null && reportType.equals("COURIER"))
            	{
            		String hql_ = "Select tp.transaction.transactionId from Shipment tp where tp.deletedAt IS NULL AND tp.transaction IS NOT NULL";
                	List<Long> shipmentTransactionIds = (List<Long>)swpService.getAllRecordsByHQL(hql_);
                	//log.info"tripTransactionIds size == " + shipmentTransactionIds.size());
                	
                	hql = hql + " AND tp.transactionId IN ("+StringUtils.join(shipmentTransactionIds, ',')+")";
            		
            	}
            	else if(reportType!=null && reportType.equals("PASSENGER"))
            	{
            		String hql_ = "Select tp.transaction.transactionId from PurchasedTrip tp where tp.deletedAt IS NULL AND tp.transaction IS NOT NULL";
                	List<Long> tripTransactionIds = (List<Long>)swpService.getAllRecordsByHQL(hql_);
                	//log.info"tripTransactionIds size == " + tripTransactionIds.size());
                	hql = hql + " AND tp.transactionId IN ("+StringUtils.join(tripTransactionIds, ',')+")";
            	}
            	else
            	{
            		String hql_ = "Select tp.transaction.transactionId from PurchasedTrip tp where tp.deletedAt IS NULL AND tp.transaction IS NOT NULL";
                	List<Long> tripTransactionIds = (List<Long>)swpService.getAllRecordsByHQL(hql_);
                	//log.info"tripTransactionIds size == " + tripTransactionIds.size());
                	hql_ = "Select tp.transaction.transactionId from Shipment tp where tp.deletedAt IS NULL AND tp.transaction IS NOT NULL";
                	List<Long> shipmentTransactionIds = (List<Long>)swpService.getAllRecordsByHQL(hql_);
                	//log.info"tripTransactionIds size == " + shipmentTransactionIds.size());
                	
                	tripTransactionIds.addAll(shipmentTransactionIds);
                	//log.info"new tripTransactionIds size == " + tripTransactionIds.size());
                	
                	hql = hql + " AND tp.transactionId IN ("+StringUtils.join(tripTransactionIds, ',')+")";
            	}
            }
            
            if(paymentMeans!=null)
            {
            	System.out.print("paymentMeans check");
            	hql = hql + " AND tp.paymentMeans = " + PaymentMeans.valueOf(paymentMeans).ordinal();
            }
            
            if(paymentMeans!=null)
            {
            	System.out.print("paymentMeans check");
            	hql = hql + " AND tp.paymentMeans = " + PaymentMeans.valueOf(paymentMeans).ordinal();
            }
            
            if(serviceType!=null)
            {
            	System.out.print("serviceType check");
            	hql = hql + " AND tp.serviceType = " + ServiceType.valueOf(serviceType).ordinal();
            }
            
            
			if(startDate!=null)
			{
				String[] startDate_ = startDate.split(" ");
				if(startDate_.length==1)
					startDate = startDate + " 00:00";
				
				hql = hql + " AND tp.createdAt >= '" + startDate + "'";
			}
			if(endDate!=null)
			{
				String[] endDate_ = endDate.split(" ");
				if(endDate_.length==1)
					endDate = endDate + " 23:59";
				
				hql = hql + " AND tp.createdAt <= '" + endDate + "'";
			}
			if(transactionStatus!=null)
			{				
				hql = hql + " AND tp.status = " + TransactionStatus.valueOf(transactionStatus).ordinal();
			}
			
			
			String sql = "";
			hql = hql + sql;
			
			if(groupBy!=null && groupBy.equals("DAILY"))
			{
				//hql = hql + " GROUP BY DAY(tp.createdAt)";
				hql = hql + " GROUP BY day_";
			}
			else if(groupBy!=null && groupBy.equals("WEEK"))
			{
				//hql = hql + " GROUP BY WEEK(tp.createdAt)";
				hql = hql + " GROUP BY week_";
			}
			else if(groupBy!=null && groupBy.equals("MONTH"))
			{
				//hql = hql + " GROUP BY MONTH(tp.createdAt)";
				hql = hql + " GROUP BY month_";
			}
			else if(groupBy!=null && groupBy.equals("ANNUAL"))
			{
				//hql = hql + " GROUP BY YEAR(tp.createdAt)";
				hql = hql + " GROUP BY year_";
			}
			else 
			{
				hql = hql + " ORDER by tp.createdAt DESC";
			}
			//log.info"HQL ---> " + hql);
			
			Collection<Transaction> transactions = new ArrayList<Transaction>();
			try
			{
				transactions = (Collection<Transaction>)this.swpService.getAllRecordsByHQL(hql);
			}
			catch(Exception e)
			{
				transactions = new ArrayList<Transaction>();
			}
			
			
			
			
			jsonObject.add("message", "Transactions listed successfully");
			jsonObject.add("status", ERROR.GENERAL_SUCCESS);
			jsonObject.add("transactionList", new Gson().toJson(transactions));
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

	public Response getTransactionsByGroup(String token, String groupType, String groupTypeValue, String requestId, String clientCode, String startDate, String endDate) {
		// TODO Auto-generated method stub
		JsonObjectBuilder jsonObject = Json.createObjectBuilder();
    	
		//log.info"0startDate -- " + startDate);
    	//log.info"0endDate -- " + endDate);
		try{
			
			if(clientCode==null || groupType==null)
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

			String hql = "";
			List<Transaction> allTransactions = new ArrayList<Transaction>();
			
			if(groupType!=null && groupType.equals("ROLETYPE") && groupTypeValue!=null)
			{
				try
				{
					RoleType.valueOf(groupTypeValue);
				}
				catch(IllegalArgumentException | NullPointerException e)
				{
					jsonObject.add("status", ERROR.INCOMPLETE_PARAMETERS);
					jsonObject.add("message", "Invalid Role Type provided");
					JsonObject jsonObj = jsonObject.build();
		            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
				}
				
				String hql_ = "Select tp.user.userId from UserRole tp where tp.roleCode = " + RoleType.valueOf(groupTypeValue).ordinal() + " AND tp.deletedAt IS NULL";
				List<Long> userIds = (List<Long>)this.swpService.getAllRecordsByHQL(hql_);
				//List<Long> l1 = new ArrayList(userIds);
				
				
				hql_ = "Select SUM(tp.transactionAmount) as totalSum, tp.transactingUser.firstName, tp.transactingUser.lastName FROM Transaction tp " +
						"WHERE tp.deletedAt IS NULL AND tp.client.clientCode = '"+clientCode+"' AND tp.transactingUser.userId IN (" + StringUtils.join(userIds, ',') + ")";
				if(startDate!=null && endDate!=null)
				{
					hql_ = hql_ + " AND tp.createdAt >= '" + startDate + "' AND tp.createdAt <= '" + endDate +  "' ";
				}
				hql_ = hql_ + " GROUP BY tp.transactingUser.id";
				//log.info"hql_ == " + hql_);
				Collection<Transaction> shipmentTransactionIds = (Collection<Transaction>)swpService.getAllRecordsByHQL(hql_);
            	allTransactions.addAll(shipmentTransactionIds);
            	

            	jsonObject.add("message", "Transactions listed successfully");
    			jsonObject.add("status", ERROR.GENERAL_SUCCESS);
    			jsonObject.add("transactionList", new Gson().toJson(allTransactions));
    			JsonObject jsonObj = jsonObject.build();
                return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			else if(groupType!=null && groupType.equals("ZONE"))
			{
				String hql_ = "Select tp from TripZone tp where tp.deletedAt IS NULL";
            	Collection<TripZone> tripZones = (Collection<TripZone>)swpService.getAllRecordsByHQL(hql_);
            	Iterator<TripZone> tzIt = tripZones.iterator();
            	JSONObject js_ = new JSONObject();
            	while(tzIt.hasNext())
            	{
            		TripZone tripZone = tzIt.next();
            		hql_ = "Select tp.station.stationId from TripZoneStation tp where tp.deletedAt IS NULL AND tp.tripZone.tripZoneId = " + tripZone.getTripZoneId();
                	List<Long> tripZoneStations = (List<Long>)swpService.getAllRecordsByHQL(hql_);
                	hql_ = "Select SUM(tp.transaction.transactionAmount) from PurchasedTrip tp where tp.departureStation.stationId IN ("+ StringUtils.join(tripZoneStations, ',') +") " +
                			"AND tp.deletedAt IS NULL AND tp.transaction IS NOT NULL";
                	
                	//log.info"1startDate -- " + startDate);
                	//log.info"2endDate -- " + endDate);
                	if(startDate!=null && endDate!=null)
    				{
    					hql_ = hql_ + " AND tp.createdAt >= '" + startDate + "' AND tp.createdAt <= '" + endDate +  "' ";
    				}
                	//log.info"1. hql_ == " + hql_);
                	Double transactionAmount = (Double)swpService.getUniqueRecordByHQL(hql_);
                	//log.info"transactionAmount == " + transactionAmount);
                	
                	hql_ = "Select SUM(tp.transaction.transactionAmount) from Shipment tp where tp.departureScheduleStation.station.stationId IN ("+ StringUtils.join(tripZoneStations, ',') +") " +
                			"AND tp.deletedAt IS NULL AND tp.transaction IS NOT NULL";
                	if(startDate!=null && endDate!=null)
    				{
    					hql_ = hql_ + " AND tp.createdAt >= '" + startDate + "' AND tp.createdAt <= '" + endDate +  "' ";
    				}
                	//log.info"2. hql_ == " + hql_);
                	Double shipTransactionAmount = (Double)swpService.getUniqueRecordByHQL(hql_);
                	js_.put(tripZone.getZoneName(), (transactionAmount==null ? 0 : transactionAmount) + (shipTransactionAmount==null ? 0 : shipTransactionAmount));
            	}
            	//log.info"tripTransactionIds size == " + js_.length());

            	jsonObject.add("message", "Transactions listed successfully");
    			jsonObject.add("status", ERROR.GENERAL_SUCCESS);
    			jsonObject.add("transactionList", js_.toString());
    			JsonObject jsonObj = jsonObject.build();
                return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			else if(groupType!=null && groupType.equals("STATION"))
			{
				String hql_ = "Select SUM(tp.transaction.transactionAmount), tp.departureStation.stationName from PurchasedTrip tp where tp.deletedAt IS NULL AND tp.transaction IS NOT NULL";
				if(startDate!=null && endDate!=null)
				{
					hql_ = hql_ + " AND tp.createdAt >= '" + startDate + "' AND tp.createdAt <= '" + endDate +  "' ";
				}
				hql_ = hql_ + " GROUP BY tp.departureStation.stationName";
            	//log.info"hql_ == " + hql_);
            	Collection<Transaction> tripTransactionIds = (Collection<Transaction>)swpService.getAllRecordsByHQL(hql_);
            	allTransactions.addAll(tripTransactionIds);
            	//log.info"tripTransactionIds size == " + tripTransactionIds.size());
            	hql_ = "Select SUM(tp.transaction.transactionAmount), tp.departureScheduleStation.station.stationName from Shipment tp where tp.deletedAt IS NULL AND tp.transaction IS NOT NULL";
            	if(startDate!=null && endDate!=null)
				{
					hql_ = hql_ + " AND tp.createdAt >= '" + startDate + "' AND tp.createdAt <= '" + endDate +  "' ";
				}
            	hql_ = hql_ + " GROUP BY tp.departureScheduleStation.station.stationName";
            	//log.info"hql_ == " + hql_);
            	Collection<Transaction> shipmentTransactionIds = (Collection<Transaction>)swpService.getAllRecordsByHQL(hql_);
            	//log.info"tripTransactionIds size == " + shipmentTransactionIds.size());
            	allTransactions.addAll(shipmentTransactionIds);
            	

            	jsonObject.add("message", "Transactions listed successfully");
    			jsonObject.add("status", ERROR.GENERAL_SUCCESS);
    			jsonObject.add("transactionList", new Gson().toJson(allTransactions));
    			JsonObject jsonObj = jsonObject.build();
                return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			else if(groupType!=null && groupType.equals("MAINSTATION"))
			{
				String hql_ = "Select SUM(tp.transaction.transactionAmount), tp.departureStation.stationName, tp.departureStation.targetPercentage from PurchasedTrip tp where tp.deletedAt IS NULL AND " +
						"tp.transaction IS NOT NULL AND tp.departureStation.mainStation = 1";
				if(startDate!=null && endDate!=null)
				{
					hql_ = hql_ + " AND tp.createdAt >= '" + startDate + "' AND tp.createdAt <= '" + endDate +  "' ";
				}
				hql_ = hql_ + " GROUP BY tp.departureStation.stationName";
            	//log.info"hql_ == " + hql_);
            	Collection<Transaction> tripTransactionIds = (Collection<Transaction>)swpService.getAllRecordsByHQL(hql_);
            	allTransactions.addAll(tripTransactionIds);
            	//log.info"tripTransactionIds size == " + tripTransactionIds.size());
            	hql_ = "Select SUM(tp.transaction.transactionAmount), tp.departureScheduleStation.station.stationName, tp.departureScheduleStation.station.targetPercentage from Shipment tp where tp.deletedAt IS NULL AND tp.transaction IS NOT NULL";
            	if(startDate!=null && endDate!=null)
				{
					hql_ = hql_ + " AND tp.createdAt >= '" + startDate + "' AND tp.createdAt <= '" + endDate +  "' ";
				}
            	hql_ = hql_ + " GROUP BY tp.departureScheduleStation.station.stationName";
            	//log.info"hql_ == " + hql_);
            	Collection<Transaction> shipmentTransactionIds = (Collection<Transaction>)swpService.getAllRecordsByHQL(hql_);
            	//log.info"tripTransactionIds size == " + shipmentTransactionIds.size());
            	//allTransactions.addAll(shipmentTransactionIds);
            	List<Transaction> shipmentTransactions = new ArrayList<Transaction>();
            	

            	jsonObject.add("message", "Transactions listed successfully");
    			jsonObject.add("status", ERROR.GENERAL_SUCCESS);
    			jsonObject.add("passengerTransactionList", new Gson().toJson(allTransactions));
    			jsonObject.add("shipmentTransactionList", new Gson().toJson(shipmentTransactions));
    			JsonObject jsonObj = jsonObject.build();
                return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			else if(groupType!=null && groupType.equals("VEHICLE"))
			{
				String hql_ = "Select SUM(tp.transaction.transactionAmount), tp.vehicleSchedule.vehicle.vehicleName from PurchasedTrip tp where " +
						"tp.transaction IS NOT NULL AND tp.deletedAt IS NULL";
				if(startDate!=null && endDate!=null)
				{
					hql_ = hql_ + " AND tp.createdAt >= '" + startDate + "' AND tp.createdAt <= '" + endDate +  "' ";
				}
				hql_ = hql_ + " GROUP BY tp.vehicleSchedule.vehicle.vehicleName";
            	List<Long> tripTransactionIds = (List<Long>)swpService.getAllRecordsByHQL(hql_);
            	//log.info"tripTransactionIds size == " + tripTransactionIds.size());
            	hql_ = "Select SUM(tp.transaction.transactionAmount), tp.vehicleSchedule.vehicle.vehicleName from Shipment tp where " +
            			"tp.transaction IS NOT NULL AND tp.deletedAt IS NULL";
            	if(startDate!=null && endDate!=null)
				{
					hql_ = hql_ + " AND tp.createdAt >= '" + startDate + "' AND tp.createdAt <= '" + endDate +  "' ";
				}
            	hql_ = hql_ + " GROUP BY tp.vehicleSchedule.vehicle.vehicleName";
            	List<Long> shipmentTransactionIds = (List<Long>)swpService.getAllRecordsByHQL(hql_);
            	//log.info"tripTransactionIds size == " + shipmentTransactionIds.size());
            	
            	tripTransactionIds.addAll(shipmentTransactionIds);
            	//log.info"new tripTransactionIds size == " + tripTransactionIds.size());
            	jsonObject.add("message", "Transactions listed successfully");
    			jsonObject.add("status", ERROR.GENERAL_SUCCESS);
    			jsonObject.add("transactionList", new Gson().toJson(tripTransactionIds));
    			JsonObject jsonObj = jsonObject.build();
                return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			else if(groupType!=null && groupType.equals("COACH"))
			{
				String hql_ = "Select SUM(tp.purchasedTrip.transaction.transactionAmount), tp.vehicleSeat.vehicleSeatSection.sectionName from PurchasedTripSeat tp where " +
        				"tp.deletedAt IS NULL AND tp.purchasedTrip.transaction IS NOT NULL";
				if(startDate!=null && endDate!=null)
				{
					hql_ = hql_ + " AND tp.createdAt >= '" + startDate + "' AND tp.createdAt <= '" + endDate +  "' ";
				}
				hql_ = hql_ + " GROUP BY tp.vehicleSeat.vehicleSeatSection.sectionName";
            	List<Long> tripTransactionIds = (List<Long>)swpService.getAllRecordsByHQL(hql_);
            	//log.info"tripTransactionIds size == " + tripTransactionIds.size());
            	hql_ = "Select SUM(tp.shipment.transaction.transactionAmount), tp.scheduleStationCourierSection.vehicleSeatSection.sectionName from CourierCabinShipment tp where " +
            			"tp.deletedAt IS NULL AND tp.shipment.transaction IS NOT NULL";
            	if(startDate!=null && endDate!=null)
				{
					hql_ = hql_ + " AND tp.createdAt >= '" + startDate + "' AND tp.createdAt <= '" + endDate +  "' ";
				}
				hql_ = hql_ + " GROUP BY tp.scheduleStationCourierSection.vehicleSeatSection.sectionName";
            	List<Long> shipmentTransactionIds = (List<Long>)swpService.getAllRecordsByHQL(hql_);
            	//log.info"tripTransactionIds size == " + shipmentTransactionIds.size());
            	
            	tripTransactionIds.addAll(shipmentTransactionIds);
            	//log.info"new tripTransactionIds size == " + tripTransactionIds.size());
            	jsonObject.add("message", "Transactions listed successfully");
    			jsonObject.add("status", ERROR.GENERAL_SUCCESS);
    			jsonObject.add("transactionList", new Gson().toJson(tripTransactionIds));
    			JsonObject jsonObj = jsonObject.build();
                return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			jsonObject.add("status", ERROR.INVALID_GROUP_TYPE_PROVIDED);
			jsonObject.add("message", "Invalid group type provided");
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
	
	
	public Response addPilotToSchedule(String assignedPilotUsername, String newTakeOffDate, Long departureScheduleId, 
			Long arrivalScheduleId, Double openingFuelBalance, String openingFuelCapacityUnits, String token, String requestId, String clientCode, String pilotChangeReasons, String ipAddress )
	{
		JsonObjectBuilder jsonObject = Json.createObjectBuilder();
    	
    	
		try{
			
			if(assignedPilotUsername==null || newTakeOffDate==null || (departureScheduleId==null && arrivalScheduleId==null))
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
				user = (User)this.swpService.getUniqueRecordByHQL("select tp from User tp where tp.username = '" + username + "' AND " +
						"tp.userStatus = " + UserStatus.ACTIVE.ordinal() + " AND tp.deletedAt IS NULL AND tp.client.clientCode = '"+clientCode+"'");
				//roleCode = user.getRoleCode();
			}
			String hql = "";
			
			User pilotAssigned = null;
			hql = "Select tp from UserRole tp where tp.user.username = '"+ assignedPilotUsername.trim() +"' AND tp.user.userStatus = " + UserStatus.ACTIVE.ordinal() + " AND " + 
					"tp.deletedAt IS NULL AND tp.roleCode = " + RoleType.PILOT.ordinal() + " AND tp.user.client.clientCode = '"+ clientCode +"'";
			//log.info"hql --" + hql);
			UserRole uRole = (UserRole)swpService.getUniqueRecordByHQL(hql);
			if(uRole==null)
			{
				jsonObject.add("status", ERROR.USER_NOT_FOUND);
				jsonObject.add("message", "Pilot provided could not be found. Ensure you provide a valid pilot");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			pilotAssigned = uRole.getUser();

			ScheduleStation departureScheduleStation = null;
			ScheduleStation arrivalScheduleStation = null;
			
			if(departureScheduleId!=null)
			{
				hql = "Select tp from ScheduleStation tp where tp.scheduleStationId = " + departureScheduleId + " AND tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL";			
				//log.info"hql_ == " + hql);
				departureScheduleStation = (ScheduleStation)swpService.getUniqueRecordByHQL(hql);
			}
			
			if(arrivalScheduleId!=null)
			{
				hql = "Select tp from ScheduleStation tp where tp.scheduleStationId = " + arrivalScheduleId + " AND tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL";			
				//log.info"hql_ == " + hql);
				arrivalScheduleStation = (ScheduleStation)swpService.getUniqueRecordByHQL(hql);
			}
			
			
			boolean updated = false;
			if(departureScheduleStation!=null)
			{
				Date departureTime = departureScheduleStation.getDepartureTime();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				try
				{
					Date newTakeOfDate_ = sdf.parse(newTakeOffDate);
					long secondsDiff = (newTakeOfDate_.getTime() - departureTime.getTime())/1000;
					//log.info"secondsDiff===" + secondsDiff);
					
					if(secondsDiff<0)
					{
						jsonObject.add("status", ERROR.INVALID_TAKE_OFF_DATE);
						jsonObject.add("message", "Invalid take off date provided. New Take off date must be in the future after the current take-off date");
						JsonObject jsonObj = jsonObject.build();
			            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
					}
					
					Long scheduleStationCodeId = departureScheduleStation.getScheduleStationCode().getScheduleStationId();
					hql = "Select tp from ScheduleStation tp where tp.scheduleStationCode.scheduleStationId = " + scheduleStationCodeId + " AND tp.client.clientCode = '"+clientCode+"' " +
							"AND tp.deletedAt IS NULL AND COALESCE(tp.arrivalTime, tp.departureTime) >= '"+ sdf.format(departureScheduleStation.getDepartureTime()) +"'";
					Collection<ScheduleStation> scheduleStationsAfter = (Collection<ScheduleStation>)swpService.getAllRecordsByHQL(hql);
					//log.info"scheduleStationsAfter size = " + scheduleStationsAfter.size());
					Iterator<ScheduleStation> ssIt = scheduleStationsAfter.iterator();
					while(ssIt.hasNext())
					{
						ScheduleStation ss = ssIt.next();
						//log.info"original dep -- " + ss.getDepartureTime() + " && " + ss.getStation().getStationName());
						//log.info"original arr -- " + ss.getArrivalTime() + " && " + ss.getStation().getStationName());

						Calendar calendar = Calendar.getInstance();
						if(ss.getDepartureTime()!=null)
						{
							calendar.setTime(ss.getDepartureTime());
							//log.info"calendar set time  - " + sdf.format(calendar.getTime()));
							if(secondsDiff>60)
							{
								double minDiff = secondsDiff/60;
								int minDiff_ = (int)Math.round(minDiff);
								long secsRemaining = secondsDiff - (minDiff_*60);
								calendar.add(Calendar.MINUTE, minDiff_);
								//log.info"1. MINUTES  - " + minDiff_);
								//log.info"1. SECS REM  - " + secsRemaining);
								calendar.add(Calendar.SECOND, (int)Math.round(secsRemaining));
							}
							else
							{
								calendar.add(Calendar.SECOND, (int)Math.round(secondsDiff));
								//log.info"2. SECS REM  - " + secondsDiff);
							}
							ss.setDepartureTime(calendar.getTime());
							ss.setPilotAssigned(pilotAssigned);
							
							if(ss.getScheduleStationId().equals(departureScheduleStation.getScheduleStationId()))
							{
								ss.setDatePilotSignedIn(new Date());
								ss.setOpeningFuelCapacity(openingFuelBalance);
								ss.setOpeningFuelCapacityUnits(openingFuelCapacityUnits);
							}
							ss.setIncidentReasons(pilotChangeReasons);
							swpService.updateRecord(ss);
							
							AuditTrail ad = UtilityHelper.createAuditTrailEntry(ipAddress, RequestType.ADD_PILOT, requestId, this.swpService, 
									verifyJ.has("username") ? verifyJ.getString("username") : null, ss.getScheduleStationId(), ScheduleStation.class.getName(), 
									("Add Pilot to a train schedule. Schedule: " + 
									(ss.getDepartureTime()!=null ? ("Departure Schedule: " + ss.getDepartureTime()) : ("Arrival Schedule" + ss.getArrivalTime())) +
									" | Pilot Assigned: " + pilotAssigned.getFirstName() + " " + pilotAssigned.getLastName() +
									" | Updated By " + user.getFirstName() + " " + user.getLastName()), clientCode);
							
							updated = true;
							//log.info"new dep -- " + sdf.format(calendar.getTime()) + " && " + ss.getStation().getStationName());
						}
						
						
						if(ss.getArrivalTime()!=null)
						{
							calendar.setTime(ss.getArrivalTime());
							if(secondsDiff>60)
							{
								double minDiff = secondsDiff/60;
								int minDiff_ = (int)Math.round(minDiff);
								long secsRemaining = secondsDiff - (minDiff_*60);
								calendar.add(Calendar.MINUTE, minDiff_);
								calendar.add(Calendar.SECOND, (int)Math.round(secsRemaining));
							}
							else
							{
								calendar.add(Calendar.SECOND, (int)Math.round(secondsDiff));
							}
							ss.setArrivalTime(calendar.getTime());
							ss.setPilotAssigned(pilotAssigned);
							ss.setIncidentReasons(pilotChangeReasons);
							
							swpService.updateRecord(ss);
							updated = true;
							//log.info"new arr -- " + sdf.format(calendar.getTime()) + " && " + ss.getStation().getStationName());
						}
						
					}
					
				}
				catch(NullPointerException | IllegalArgumentException e)
				{
					log.error(e);
					e.printStackTrace();
				}
			}
			
			
			if(updated==true)
			{
				jsonObject.add("message", "Pilot assigned successfully");
				jsonObject.add("status", ERROR.GENERAL_SUCCESS);
			}
			else
			{

				jsonObject.add("message", "Pilot could not be assigned successfully");
				jsonObject.add("status", ERROR.GENERAL_FAIL);
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

	

	public Response endPilotSchedule(Long departureScheduleId, 
			Long arrivalScheduleId, Double closingFuelBalance, String closingFuelCapacityUnits, String token, String requestId, String clientCode )
	{
		JsonObjectBuilder jsonObject = Json.createObjectBuilder();
    	
    	
		try{
			
			if((departureScheduleId==null && arrivalScheduleId==null))
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
			String hql = "";
			
			

			ScheduleStation departureScheduleStation = null;
			ScheduleStation arrivalScheduleStation = null;
			
			if(departureScheduleId!=null)
			{
				hql = "Select tp from ScheduleStation tp where tp.scheduleStationId = " + departureScheduleId + " AND tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL";			
				//log.info"hql_ == " + hql);
				departureScheduleStation = (ScheduleStation)swpService.getUniqueRecordByHQL(hql);
			}
			
			if(arrivalScheduleId!=null)
			{
				hql = "Select tp from ScheduleStation tp where tp.scheduleStationId = " + arrivalScheduleId + " AND tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL";			
				//log.info"hql_ == " + hql);
				arrivalScheduleStation = (ScheduleStation)swpService.getUniqueRecordByHQL(hql);
			}
			
			
			boolean updated = false;
			if(arrivalScheduleStation!=null)
			{
				Date arrivalTime = arrivalScheduleStation.getArrivalTime();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				try
				{
					Long scheduleStationCodeId = arrivalScheduleStation.getScheduleStationCode().getScheduleStationId();
					hql = "Select tp from ScheduleStation tp where tp.scheduleStationCode.scheduleStationId = " + scheduleStationCodeId + " AND tp.client.clientCode = '"+clientCode+"' " +
							"AND tp.deletedAt IS NULL AND COALESCE(tp.arrivalTime, tp.departureTime) >= '"+ sdf.format(arrivalScheduleStation.getArrivalTime()) +"'";
					Collection<ScheduleStation> scheduleStationsAfter = (Collection<ScheduleStation>)swpService.getAllRecordsByHQL(hql);
					//log.info"scheduleStationsAfter size = " + scheduleStationsAfter.size());
					Iterator<ScheduleStation> ssIt = scheduleStationsAfter.iterator();
					while(ssIt.hasNext())
					{
						ScheduleStation ss = ssIt.next();
						//log.info"original dep -- " + ss.getDepartureTime() + " && " + ss.getStation().getStationName());
						//log.info"original arr -- " + ss.getArrivalTime() + " && " + ss.getStation().getStationName());
						
						Calendar calendar = Calendar.getInstance();
						if(ss.getDepartureTime()!=null)
						{
							if(!ss.getScheduleStationId().equals(arrivalScheduleStation.getScheduleStationId()))
							{
								ss.setPilotAssigned(null);
								swpService.updateRecord(ss);
								updated = true;
							}
						}
						
						
						if(ss.getArrivalTime()!=null)
						{
							if(ss.getScheduleStationId().equals(arrivalScheduleStation.getScheduleStationId()))
							{
								ss.setClosingFuelCapacity(closingFuelBalance);
								ss.setClosingFuelCapacityUnits(closingFuelCapacityUnits);
								ss.setDatePilotSignedOut(new Date());
							}
							else
							{
								ss.setPilotAssigned(null);
							}
							
							swpService.updateRecord(ss);
							updated = true;
						}
						
					}
					
				}
				catch(NullPointerException | IllegalArgumentException e)
				{
					log.error(e);
					e.printStackTrace();
				}
			}
			
			
			if(updated==true)
			{
				jsonObject.add("message", "Pilot signed out successfully");
				jsonObject.add("status", ERROR.GENERAL_SUCCESS);
			}
			else
			{

				jsonObject.add("message", "Pilot could not be signed out successfully");
				jsonObject.add("status", ERROR.GENERAL_FAIL);
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


	
	public Response changeVehicle(String newVehicleCode, String assignedPilotUsername, String newTakeOffDate, Long departureScheduleId, 
			Long arrivalScheduleId, Double openingFuelBalance, String openingFuelCapacityUnits, 
			String token, String requestId, String clientCode, String changeVehicleReasons, String ipAddress )
	{
		JsonObjectBuilder jsonObject = Json.createObjectBuilder();
    	
    	
		try{
			
			if(newVehicleCode==null || newTakeOffDate==null || (departureScheduleId==null && arrivalScheduleId==null))
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
				user = (User)this.swpService.getUniqueRecordByHQL("select tp from User tp where tp.username = '" + username + "' AND " +
						"tp.userStatus = " + UserStatus.ACTIVE.ordinal() + " AND tp.deletedAt IS NULL AND tp.client.clientCode = '"+clientCode+"'");
				//roleCode = user.getRoleCode();
			}
			
			String hql = "";
			
			User pilotAssigned = null;
			hql = "Select tp from UserRole tp where tp.user.username = '"+ assignedPilotUsername.trim() +"' AND tp.user.userStatus = " + UserStatus.ACTIVE.ordinal() + " AND " + 
					"tp.deletedAt IS NULL AND tp.roleCode = " + RoleType.PILOT.ordinal() + " AND tp.user.client.clientCode = '"+ clientCode +"'";
			//log.info"hql --" + hql);
			UserRole uRole = (UserRole)swpService.getUniqueRecordByHQL(hql);
			if(uRole==null)
			{
				jsonObject.add("status", ERROR.USER_NOT_FOUND);
				jsonObject.add("message", "Pilot provided could not be found. Ensure you provide a valid pilot");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			pilotAssigned = uRole.getUser();
			
			hql = "Select tp from Vehicle tp where tp.vehicleCode = '" + newVehicleCode + "' AND tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL";			
			//log.info"hql_ == " + hql);
			Vehicle vehicle = (Vehicle)swpService.getUniqueRecordByHQL(hql);
			if(vehicle==null)
			{
				jsonObject.add("status", ERROR.VEHICLE_NOT_FOUND);
				jsonObject.add("message", "Train not found. Please provide a valid one");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}

			ScheduleStation departureScheduleStation = null;
			ScheduleStation arrivalScheduleStation = null;
			
			if(departureScheduleId!=null)
			{
				hql = "Select tp from ScheduleStation tp where tp.scheduleStationId = " + departureScheduleId + " AND tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL";			
				//log.info"hql_ == " + hql);
				departureScheduleStation = (ScheduleStation)swpService.getUniqueRecordByHQL(hql);
			}
			
			if(arrivalScheduleId!=null)
			{
				hql = "Select tp from ScheduleStation tp where tp.scheduleStationId = " + arrivalScheduleId + " AND tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL";			
				//log.info"hql_ == " + hql);
				arrivalScheduleStation = (ScheduleStation)swpService.getUniqueRecordByHQL(hql);
			}
			
			
			boolean updated = false;
			if(departureScheduleStation!=null)
			{
				Date departureTime = departureScheduleStation.getDepartureTime();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				try
				{
					Date newTakeOfDate_ = sdf.parse(newTakeOffDate);
					long secondsDiff = (newTakeOfDate_.getTime() - departureTime.getTime())/1000;
					//log.info"secondsDiff===" + secondsDiff);
					
					if(secondsDiff<0)
					{
						jsonObject.add("status", ERROR.INVALID_TAKE_OFF_DATE);
						jsonObject.add("message", "Invalid take off date provided. New Take off date must be in the future after the current take-off date");
						JsonObject jsonObj = jsonObject.build();
			            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
					}
					
					Long scheduleStationCodeId = departureScheduleStation.getScheduleStationCode().getScheduleStationId();
					hql = "Select tp from ScheduleStation tp where tp.scheduleStationCode.scheduleStationId = " + scheduleStationCodeId + " AND tp.client.clientCode = '"+clientCode+"' " +
							"AND tp.deletedAt IS NULL AND COALESCE(tp.arrivalTime, tp.departureTime) >= '"+ sdf.format(departureScheduleStation.getDepartureTime()) +"'";
					Collection<ScheduleStation> scheduleStationsAfter = (Collection<ScheduleStation>)swpService.getAllRecordsByHQL(hql);
					//log.info"scheduleStationsAfter size = " + scheduleStationsAfter.size());
					Iterator<ScheduleStation> ssIt = null;
					/*Assign new pilot*/
					ssIt = scheduleStationsAfter.iterator();
					while(ssIt.hasNext())
					{
						ScheduleStation ss = ssIt.next();
						//log.info"original dep -- " + ss.getDepartureTime() + " && " + ss.getStation().getStationName());
						//log.info"original arr -- " + ss.getArrivalTime() + " && " + ss.getStation().getStationName());

						Calendar calendar = Calendar.getInstance();
						
						if(ss.getDepartureTime()!=null)
						{
							calendar.setTime(ss.getDepartureTime());
							//log.info"calendar set time  - " + sdf.format(calendar.getTime()));
							if(secondsDiff>60)
							{
								double minDiff = secondsDiff/60;
								int minDiff_ = (int)Math.round(minDiff);
								long secsRemaining = secondsDiff - (minDiff_*60);
								calendar.add(Calendar.MINUTE, minDiff_);
								//log.info"1. MINUTES  - " + minDiff_);
								//log.info"1. SECS REM  - " + secsRemaining);
								calendar.add(Calendar.SECOND, (int)Math.round(secsRemaining));
							}
							else
							{
								calendar.add(Calendar.SECOND, (int)Math.round(secondsDiff));
								//log.info"2. SECS REM  - " + secondsDiff);
							}
							ss.setDepartureTime(calendar.getTime());
							ss.setPilotAssigned(pilotAssigned);
							
							if(ss.getScheduleStationId().equals(departureScheduleStation.getScheduleStationId()))
							{
								ss.setDatePilotSignedIn(new Date());
								ss.setOpeningFuelCapacity(openingFuelBalance);
								ss.setOpeningFuelCapacityUnits(openingFuelCapacityUnits);
							}
							updated = true;
							//log.info"new dep -- " + sdf.format(calendar.getTime()) + " && " + ss.getStation().getStationName());
						}
						
						
						if(ss.getArrivalTime()!=null)
						{
							calendar.setTime(ss.getArrivalTime());
							if(secondsDiff>60)
							{
								double minDiff = secondsDiff/60;
								int minDiff_ = (int)Math.round(minDiff);
								long secsRemaining = secondsDiff - (minDiff_*60);
								calendar.add(Calendar.MINUTE, minDiff_);
								calendar.add(Calendar.SECOND, (int)Math.round(secsRemaining));
							}
							else
							{
								calendar.add(Calendar.SECOND, (int)Math.round(secondsDiff));
							}
							ss.setArrivalTime(calendar.getTime());
							ss.setPilotAssigned(pilotAssigned);
							
							updated = true;
							//log.info"new arr -- " + sdf.format(calendar.getTime()) + " && " + ss.getStation().getStationName());
						}
						ss.setVehicle(vehicle);
						ss.setIncidentReasons(changeVehicleReasons);
						swpService.updateRecord(ss);
						
						AuditTrail ad = UtilityHelper.createAuditTrailEntry(ipAddress, RequestType.CHANGE_VEHICLE, requestId, this.swpService, 
								username, ss.getScheduleStationId(), ScheduleStation.class.getName(), 
								"Change Vehicle. Schedule Station Code: " + ss.getScheduleStationCode().getScheduleStationCode() + " | New Vehicle: " + vehicle.getVehicleName() + 
								" | Change By " + user.getFirstName() + " " + user.getLastName(), clientCode);
					}
					
				}
				catch(NullPointerException | IllegalArgumentException e)
				{
					log.error(e);
					e.printStackTrace();
				}
			}
			
			
			if(updated==true)
			{
				jsonObject.add("message", "Pilot assigned successfully");
				jsonObject.add("status", ERROR.GENERAL_SUCCESS);
			}
			else
			{

				jsonObject.add("message", "Pilot could not be assigned successfully");
				jsonObject.add("status", ERROR.GENERAL_FAIL);
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
	
	

	public Response approveGroupTripRequest(String requestCode, String action,
			String token, String requestId, String clientCode, String ipAddress) {
		// TODO Auto-generated method stub
		JsonObjectBuilder jsonObject = Json.createObjectBuilder();
    	
    	
		try{
			
			if(requestCode==null || action==null)
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
				user = (User)this.swpService.getUniqueRecordByHQL("select tp from User tp where tp.username = '" + username + "' AND " +
						"tp.userStatus = " + UserStatus.ACTIVE.ordinal() + " AND tp.deletedAt IS NULL AND tp.client.clientCode = '"+clientCode+"'");
				//roleCode = user.getRoleCode();
			}
			
			String hql = "Select tp from GroupTripRequest tp where tp.requestCode = '"+ requestCode +"' AND tp.deletedAt IS NULL";
			String sql = "";
			hql = hql + sql;
			GroupTripRequest gtr = (GroupTripRequest)this.swpService.getUniqueRecordByHQL(hql);
			if(gtr==null)
			{
				jsonObject.add("status", ERROR.GROUP_TRIP_REQUEST_NOT_FOUND);
				jsonObject.add("message", "Your group trip request could not be found");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			
			if(action.equals("APPROVE"))
			{
				gtr.setGroupTripRequestStatus(GroupTripRequestStatus.APPROVED);
				swpService.updateRecord(gtr);
				jsonObject.add("message", "Approved successfully");
				
				AuditTrail ad = UtilityHelper.createAuditTrailEntry(ipAddress, RequestType.APPROVE_GROUP_TRIP_REQUEST, requestId, this.swpService, 
					verifyJ.has("username") ? verifyJ.getString("username") : null, gtr.getGroupTripRequestId(), GroupTripRequest.class.getName(), 
					"Approve Group Trip Request. Request Code: " + gtr.getRequestCode() + " | Approved By " + user.getFirstName() + " " + user.getLastName(), clientCode);
			}
			else if(action.equals("DISAPPROVE"))
			{
				gtr.setGroupTripRequestStatus(GroupTripRequestStatus.CANCELED_BY_STAFF);
				swpService.updateRecord(gtr);
				jsonObject.add("message", "Disapproved successfully");
				
				AuditTrail ad = UtilityHelper.createAuditTrailEntry(ipAddress, RequestType.DISAPPROVE_GROUP_TRIP_REQUEST, requestId, this.swpService, 
					verifyJ.has("username") ? verifyJ.getString("username") : null, gtr.getGroupTripRequestId(), GroupTripRequest.class.getName(), 
					"Disapprove Group Trip Request. Request Code: " + gtr.getRequestCode() + " | Disapproved By " + user.getFirstName() + " " + user.getLastName(), clientCode);
			}
			else
			{
				jsonObject.add("status", ERROR.INVALID_ACTION_PROVIDED);
				jsonObject.add("message", "Invalid action provided. Specify if you are approving or disapproving this group trip request");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}

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

	
}