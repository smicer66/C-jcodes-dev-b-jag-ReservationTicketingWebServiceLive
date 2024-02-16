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
import java.util.Set;
import java.util.UUID;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.security.auth.login.LoginException;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.probase.reservationticketingwebservice.enumerations.CabinType;
import com.probase.reservationticketingwebservice.enumerations.CouponStatus;
import com.probase.reservationticketingwebservice.enumerations.RegionType;
import com.probase.reservationticketingwebservice.enumerations.SeatAvailabilityStatus;
import com.probase.reservationticketingwebservice.enumerations.TargetMonth;
import com.probase.reservationticketingwebservice.enumerations.VehicleStatus;
import com.probase.reservationticketingwebservice.enumerations.VehicleType;
import com.probase.reservationticketingwebservice.enumerations.CardStatus;
import com.probase.reservationticketingwebservice.enumerations.CardType;
import com.probase.reservationticketingwebservice.enumerations.Channel;
import com.probase.reservationticketingwebservice.enumerations.CustomerStatus;
import com.probase.reservationticketingwebservice.enumerations.CustomerType;
import com.probase.reservationticketingwebservice.enumerations.DeviceStatus;
import com.probase.reservationticketingwebservice.enumerations.DeviceType;
import com.probase.reservationticketingwebservice.enumerations.TransactionCurrency;
import com.probase.reservationticketingwebservice.enumerations.PaymentMeans;
import com.probase.reservationticketingwebservice.enumerations.RequestType;
import com.probase.reservationticketingwebservice.enumerations.RoleType;
import com.probase.reservationticketingwebservice.enumerations.ServiceType;
import com.probase.reservationticketingwebservice.enumerations.TransactionStatus;
import com.probase.reservationticketingwebservice.enumerations.UserStatus;
import com.probase.reservationticketingwebservice.models.AuditTrail;
import com.probase.reservationticketingwebservice.models.CardScheme;
import com.probase.reservationticketingwebservice.models.Client;
import com.probase.reservationticketingwebservice.models.Coupon;
import com.probase.reservationticketingwebservice.models.Customer;
import com.probase.reservationticketingwebservice.models.District;
import com.probase.reservationticketingwebservice.models.Line;
import com.probase.reservationticketingwebservice.models.PublicHoliday;
import com.probase.reservationticketingwebservice.models.ScheduleDay;
import com.probase.reservationticketingwebservice.models.ScheduleStation;
import com.probase.reservationticketingwebservice.models.ScheduleStationCode;
import com.probase.reservationticketingwebservice.models.ScheduleStationCourierSection;
import com.probase.reservationticketingwebservice.models.ScheduleStationSeatAvailability;
import com.probase.reservationticketingwebservice.models.ScheduleStationSeatSection;
import com.probase.reservationticketingwebservice.models.Station;
import com.probase.reservationticketingwebservice.models.Target;
import com.probase.reservationticketingwebservice.models.TicketCollectionPoint;
import com.probase.reservationticketingwebservice.models.TripCard;
import com.probase.reservationticketingwebservice.models.Transaction;
import com.probase.reservationticketingwebservice.models.TripZone;
import com.probase.reservationticketingwebservice.models.TripZoneStation;
import com.probase.reservationticketingwebservice.models.UnpurchasedSeatSummary;
import com.probase.reservationticketingwebservice.models.User;
import com.probase.reservationticketingwebservice.models.Vehicle;
import com.probase.reservationticketingwebservice.models.VehicleSchedule;
import com.probase.reservationticketingwebservice.models.VehicleSeat;
import com.probase.reservationticketingwebservice.models.VehicleSeatClass;
import com.probase.reservationticketingwebservice.models.VehicleSeatSection;
import com.probase.reservationticketingwebservice.models.Vendor;
import com.probase.reservationticketingwebservice.util.Application;
import com.probase.reservationticketingwebservice.util.ERROR;
import com.probase.reservationticketingwebservice.util.PrbCustomService;
import com.probase.reservationticketingwebservice.util.ServiceLocator;
import com.probase.reservationticketingwebservice.util.SmsSender;
import com.probase.reservationticketingwebservice.util.SwpService;
import com.probase.reservationticketingwebservice.util.UtilityHelper;
import com.sun.org.apache.bcel.internal.generic.NEW;

public final class UtilityFunction {

    private static UtilityFunction authenticator = null;

    // A user storage which stores <username, password>
    private final Map<String, String> usersStorage = new HashMap();

    // A service key storage which stores <service_key, username>
    private final Map<String, String> serviceKeysStorage = new HashMap();

    // An authentication token storage which stores <service_key, auth_token>.
    private final Map<String, String> authorizationTokensStorage = new HashMap();
    
    private static Logger log = Logger.getLogger(UtilityFunction.class);
	private ServiceLocator serviceLocator = null;
	public SwpService swpService = null;
	public PrbCustomService swpCustomService = PrbCustomService.getInstance();
	Application application = null;
	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private UtilityFunction() {
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

    public static UtilityFunction getInstance() {
        if ( authenticator == null ) {
            authenticator = new UtilityFunction();
        }

        return authenticator;
    }

    
    
    public Response getAuditTrail(String clientCode, String startDate, String endDate) {
		// TODO Auto-generated method stub
    	JsonObjectBuilder jsonObject = Json.createObjectBuilder();
    	
		try{
			if(clientCode==null || startDate==null || endDate==null)
			{
				jsonObject.add("status", ERROR.INCOMPLETE_PARAMETERS);
				jsonObject.add("message", "Incomplete Parameters provided in request.");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			
			swpService = serviceLocator.getSwpService();
			Application app = Application.getInstance(swpService);
			
			String hql = "Select tp from AuditTrail tp where tp.client.clientCode = '" + clientCode + "' AND tp.deletedAt IS NULL AND tp.createdAt >= '" + startDate + "' " +
					" AND tp.createdAt <= '" + endDate + "'" ;
			Collection<AuditTrail> auditTrails = (Collection<AuditTrail>)swpService.getAllRecordsByHQL(hql);
			
			
			jsonObject.add("message", "Audit Listing");
			jsonObject.add("auditList", new Gson().toJson(auditTrails));
			jsonObject.add("startDate", startDate);
			jsonObject.add("endDate", endDate);
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
    
    public Response createNewPublicHoliday(String holidayName, String holidayDate, String clientCode, String token, String requestId, String ipAddress)
	{
    	JsonObjectBuilder jsonObject = Json.createObjectBuilder();
    	
    	
		try{
			if(holidayName==null || holidayDate==null || token==null)
			{
				//log.inforequestId + "Test 2");
				jsonObject.add("status", ERROR.INCOMPLETE_PARAMETERS);
				jsonObject.add("message", "Incomplete Parameters provided in request.");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			swpService = serviceLocator.getSwpService();
			Application app = Application.getInstance(swpService);
			JSONObject verifyJ = UtilityHelper.verifyToken(token, app);
			//log.info"1---verifyJ ==" + verifyJ.length() + " && v= " + verifyJ.toString());
			if(verifyJ.length()==0 || (verifyJ.length()>0 && verifyJ.has("active") && verifyJ.getInt("active")==0))
			{
				//log.info"2---verifyJ ==" + verifyJ.toString());
				jsonObject.add("status", ERROR.FORCE_LOGOUT_USER);
				jsonObject.add("message", "Your session has expired. Please log in again");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			//log.inforequestId + "verifyJ ==" + verifyJ.toString());
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
			
			if(roleCode==null || (roleCode!=null && !roleCode.equals(RoleType.ADMIN_STAFF)))
			{
				jsonObject.add("status", ERROR.INVALID_PUBLIC_HOLIDAY_CREATION_PRIVILEDGES);
				jsonObject.add("message", "Invalid Public Holiday Creation Priviledges");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			String hql = "Select tp from PublicHoliday tp where tp.client.clientCode = '" + clientCode + "' AND lower(tp.holidayName) = '" + holidayName + "' AND tp.holidayDate = '"+holidayDate+"' AND tp.deletedAt IS NULL";
			//log.info"hql ==" + hql);
			PublicHoliday publicHoliday = (PublicHoliday)swpService.getUniqueRecordByHQL(hql);
			
			if(publicHoliday!=null)
			{
				//bank already exists
				jsonObject.add("message", "Public Holiday Creation Failed. A Holiday matching the name and date Already Exists");
				jsonObject.add("status", ERROR.PUBLIC_HOLIDAY_ALREADY_EXISTS);
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}

			hql = "Select tp from Client tp where tp.clientCode = '" +clientCode+ "' AND tp.deletedAt IS NULL";
			Client client = (Client)this.swpService.getUniqueRecordByHQL(hql);
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date holidayDateFormatted = sdf.parse(holidayDate);
			PublicHoliday holiday = new PublicHoliday();
			holiday.setCreatedAt(new Date());
			holiday.setUpdatedAt(new Date());
			holiday.setHolidayDate(holidayDateFormatted);
			holiday.setHolidayName(holidayName);
			holiday.setUpdatedAt(new Date());
			holiday.setClient(client);
			holiday = (PublicHoliday)this.swpService.createNewRecord(holiday);
			
			AuditTrail ad = UtilityHelper.createAuditTrailEntry(ipAddress, RequestType.NEW_PUBLIC_HOLIDAY_CREATION, requestId, this.swpService, 
					verifyJ.has("username") ? verifyJ.getString("username") : null, holiday.getPublicHolidayId(), PublicHoliday.class.getName(),
					"New Public Holiday. Holiday Date - " + holidayDateFormatted + " | Holiday Name - " + holidayName + " | Created By: " + 
					user.getFirstName() + " " + user.getLastName(), clientCode);
			
			if(holiday!=null)
			{
				
				jsonObject.add("message", "New Public Holiday Created successfully");
				jsonObject.add("publicHoliday", new Gson().toJson(holiday));
				jsonObject.add("status", ERROR.GENERAL_SUCCESS);
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			else
			{
				jsonObject.add("message", "New Public Holiday Creation Failed");
				jsonObject.add("status", ERROR.PUBLIC_HOLIDAY_CREATE_FAIL);
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			
			
		}catch(Exception e)
		{
			//log.info"Error = " + e.getMessage());
			log.warn(e); log.error("exception...", e);
			e.printStackTrace();
			JsonObject jsonObj = jsonObject.build();
            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.INTERNAL_SERVER_ERROR ).entity( jsonObj.toString() ).build();
		}
	}

    
    
    public Response createNewTicketCollectionPoint(String collectionPointName, Long districtId, String clientCode, String token, String requestId, String ipAddress)
	{
    	JsonObjectBuilder jsonObject = Json.createObjectBuilder();
    	
    	
		try{
			if(collectionPointName==null || districtId==null || token==null)
			{
				//log.inforequestId + "Test 2");
				jsonObject.add("status", ERROR.INCOMPLETE_PARAMETERS);
				jsonObject.add("message", "Incomplete Parameters provided in request.");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			swpService = serviceLocator.getSwpService();
			Application app = Application.getInstance(swpService);
			JSONObject verifyJ = UtilityHelper.verifyToken(token, app);
			//log.info"1---verifyJ ==" + verifyJ.length() + " && v= " + verifyJ.toString());
			if(verifyJ.length()==0 || (verifyJ.length()>0 && verifyJ.has("active") && verifyJ.getInt("active")==0))
			{
				//log.info"2---verifyJ ==" + verifyJ.toString());
				jsonObject.add("status", ERROR.FORCE_LOGOUT_USER);
				jsonObject.add("message", "Your session has expired. Please log in again");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			//log.inforequestId + "verifyJ ==" + verifyJ.toString());
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
					jsonObject.add("status", ERROR.INVALID_TICKET_COLLECTION_CENTER_CREATION_PRIVILEDGES);
					jsonObject.add("message", "Invalid Ticket Collection Center Creation Priviledges");
					JsonObject jsonObj = jsonObject.build();
		            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
				}
				//roleCode = user.getRoleCode();
			}
			
			if(roleCode==null || (roleCode!=null && !roleCode.equals(RoleType.ADMIN_STAFF)))
			{
				jsonObject.add("status", ERROR.INVALID_TICKET_COLLECTION_CENTER_CREATION_PRIVILEDGES);
				jsonObject.add("message", "Invalid Ticket Collection Center Creation Priviledges");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			String hql = "Select tp from TicketCollectionPoint tp where lower(tp.client.clientCode) = '" + clientCode.toLowerCase() + "' AND lower(tp.collectionPointName) = '" + collectionPointName + "' AND tp.deletedAt IS NULL";
			//log.info"hql ==" + hql);
			TicketCollectionPoint ticketCollectionPoint = (TicketCollectionPoint)swpService.getUniqueRecordByHQL(hql);
			
			if(ticketCollectionPoint!=null)
			{
				//bank already exists
				jsonObject.add("message", "New Ticket Collection Center/Point could not be created successfully. A collection center with a similar name already exists");
				jsonObject.add("status", ERROR.TICKET_COLLECTION_CENTER_EXISTS);
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}

			hql = "Select tp from Client tp where tp.clientCode = '" +clientCode+ "' AND tp.deletedAt IS NULL";
			Client client = (Client)this.swpService.getUniqueRecordByHQL(hql);
			
			hql = "Select tp from District tp where tp.districtId= '" +districtId+ "' AND tp.deletedAt IS NULL";
			District district = (District)this.swpService.getUniqueRecordByHQL(hql);
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			ticketCollectionPoint = new TicketCollectionPoint();
			ticketCollectionPoint.setCreatedAt(new Date());
			ticketCollectionPoint.setUpdatedAt(new Date());
			ticketCollectionPoint.setCollectionPointName(collectionPointName);
			ticketCollectionPoint.setClient(client);
			ticketCollectionPoint.setCollectionPointCode(RandomStringUtils.randomNumeric(8));
			ticketCollectionPoint.setDistrict(district);
			ticketCollectionPoint = (TicketCollectionPoint)this.swpService.createNewRecord(ticketCollectionPoint);
			
			AuditTrail ad = UtilityHelper.createAuditTrailEntry(ipAddress, RequestType.NEW_TICKET_COLLECTION_POINT, requestId, this.swpService, 
					verifyJ.has("username") ? verifyJ.getString("username") : null, ticketCollectionPoint.getTicketCollectionPointId(), TicketCollectionPoint.class.getName(), 
					"New Ticket Collection Point. Collection Point: " + collectionPointName + 
					" | Created By " + user.getFirstName() + " " + user.getLastName(), clientCode);
			
			if(ticketCollectionPoint!=null)
			{
				
				jsonObject.add("message", "New Ticket Collection Center Created successfully");
				jsonObject.add("ticketCollectionPoint", new Gson().toJson(ticketCollectionPoint));
				jsonObject.add("status", ERROR.GENERAL_SUCCESS);
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			else
			{
				jsonObject.add("message", "New Ticket Collection Center Creation Failed");
				jsonObject.add("status", ERROR.TICKET_COLLECTION_CREATION_FAIL);
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			
			
		}catch(Exception e)
		{
			//log.info"Error = " + e.getMessage());
			log.warn(e); log.error("exception...", e);
			e.printStackTrace();
			JsonObject jsonObj = jsonObject.build();
            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.INTERNAL_SERVER_ERROR ).entity( jsonObj.toString() ).build();
		}
	}
    
    
    
    
    public Response getListTicketCollectionPoints(String token, String clientCode,
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
			
			String hql = "Select tp from TicketCollectionPoint tp where tp.client.clientCode = '" + clientCode + "' AND tp.deletedAt IS NULL";
			Collection<TicketCollectionPoint> ticketCollectionPoints = (Collection<TicketCollectionPoint>)swpService.getAllRecordsByHQL(hql);
			
			
			
			jsonObject.add("message", "Ticket Collection Point Listing");
			jsonObject.add("ticketCollectionPoints", new Gson().toJson(ticketCollectionPoints));
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

    
    
    
    public Response getPublicHolidays(String clientCode) {
		// TODO Auto-generated method stub
    	JsonObjectBuilder jsonObject = Json.createObjectBuilder();
    	
		try{
			
			swpService = serviceLocator.getSwpService();
			Application app = Application.getInstance(swpService);
			
			String hql = "Select tp from PublicHoliday tp where tp.client.clientCode = '" + clientCode + "' AND tp.deletedAt IS NULL";
			//log.info"3.hql ==" + hql);
			Collection<PublicHoliday> publicHolidays = (Collection<PublicHoliday>)swpService.getAllRecordsByHQL(hql);
			
			
			jsonObject.add("message", "Public Holiday Listing");
			jsonObject.add("publicHolidayList", new Gson().toJson(publicHolidays));
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
    
    
    
    public Response getViewForReportingData(String clientCode) {
		// TODO Auto-generated method stub
    	JsonObjectBuilder jsonObject = Json.createObjectBuilder();
    	
		try{
			
			swpService = serviceLocator.getSwpService();
			Application app = Application.getInstance(swpService);
			
			
			List allServiceTypes = Arrays.asList(ServiceType.values());
			List allPaymentMeans = Arrays.asList(PaymentMeans.values());
			
			String hql = "Select tp from VehicleSeatSection tp where tp.client.clientCode = '" + clientCode + "' AND tp.deletedAt IS NULL";
			//log.info"3.hql ==" + hql);
			Collection<VehicleSeatSection> cabinList = (Collection<VehicleSeatSection>)swpService.getAllRecordsByHQL(hql);

			hql = "Select tp from Vehicle tp where tp.client.clientCode = '" + clientCode + "' AND tp.deletedAt IS NULL";
			//log.info"3.hql ==" + hql);
			Collection<Vehicle> vehicleList = (Collection<Vehicle>)swpService.getAllRecordsByHQL(hql);

			hql = "Select tp from Station tp where tp.client.clientCode = '" + clientCode + "' AND tp.deletedAt IS NULL";
			//log.info"3.hql ==" + hql);
			Collection<Station> stationList = (Collection<Station>)swpService.getAllRecordsByHQL(hql);

			hql = "Select tp from TripZone tp where tp.client.clientCode = '" + clientCode + "' AND tp.deletedAt IS NULL";
			//log.info"3.hql ==" + hql);
			Collection<TripZone> tripZoneList = (Collection<TripZone>)swpService.getAllRecordsByHQL(hql);

			hql = "Select tp from Vendor tp where tp.client.clientCode = '" + clientCode + "' AND tp.deletedAt IS NULL";
			//log.info"3.hql ==" + hql);
			Collection<Vendor> vendorList = (Collection<Vendor>)swpService.getAllRecordsByHQL(hql);

			String hql2 = "Select tp.user.userId from UserRole tp where tp.roleCode IN (" + RoleType.ADMIN_STAFF.ordinal() + ", "+ RoleType.OPERATOR.ordinal() +") AND tp.deletedAt IS NULL and tp.user.client.clientCode = '"+clientCode+"'";
			List<Long> userIds = (List<Long>)swpService.getAllRecordsByHQL(hql2);
			hql = "Select tp from User tp where tp.client.clientCode = '" + clientCode + "' AND tp.deletedAt IS NULL AND tp.userId IN  " +
					"(" + StringUtils.join(userIds, ", ") +")";
			//log.info"3.hql ==" + hql);
			Collection<User> userList = (Collection<User>)swpService.getAllRecordsByHQL(hql);
			Iterator<User> it = userList.iterator();
			List allUsers = new ArrayList();
			while(it.hasNext())
			{
				User us = it.next();
				JSONObject js = new JSONObject();
				js.put("firstName", us.getFirstName());
				js.put("lastName", us.getLastName());
				js.put("userCode", us.getUniqueId());
				//js.put("roleCode", us.getRoleCode().name());
				if(us.getCurrentStation()!=null)
					js.put("firstName", us.getCurrentStation().getStationName());
				allUsers.add(js);
			}

			jsonObject.add("message", "Public Holiday Listing");

			jsonObject.add("allServiceTypes", new Gson().toJson(allServiceTypes));
			jsonObject.add("allPaymentMeans", new Gson().toJson(allPaymentMeans));
			jsonObject.add("cabinList", new Gson().toJson(cabinList));
			jsonObject.add("vehicleList", new Gson().toJson(vehicleList));
			jsonObject.add("stationList", new Gson().toJson(stationList));
			jsonObject.add("vendorList", new Gson().toJson(vendorList));
			jsonObject.add("userList", new Gson().toJson(allUsers));
			jsonObject.add("tripZoneList", new Gson().toJson(tripZoneList));
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
    
    
    public Response getCoupons(String clientCode, String batchNumber) {
		// TODO Auto-generated method stub
    	JsonObjectBuilder jsonObject = Json.createObjectBuilder();
    	
		try{
			
			swpService = serviceLocator.getSwpService();
			Application app = Application.getInstance(swpService);
			
			String hql = "Select tp from Coupon tp where tp.client.clientCode = '" + clientCode + "' AND tp.deletedAt IS NULL";
			if(batchNumber!=null)
				hql = hql + " AND tp.batchNumber = '"+batchNumber+"'";
			//log.info"3.hql ==" + hql);
			Collection<Coupon> coupons = (Collection<Coupon>)swpService.getAllRecordsByHQL(hql);
			
			
			jsonObject.add("message", "Coupon Listing");
			jsonObject.add("couponList", new Gson().toJson(coupons));
			if(batchNumber!=null)
				jsonObject.add("batchNumber", batchNumber);
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

    

    
    public Response getScheduleDays(String startDate, String endDate, String clientCode, String requestId, String ipAddress) {
		// TODO Auto-generated method stub
    	JsonObjectBuilder jsonObject = Json.createObjectBuilder();
    	
    	
    	
		try{
			String hql = "Select tp from ScheduleDay tp where tp.client.clientCode = '" + clientCode + "' AND tp.deletedAt IS NULL";
			if(startDate!=null && endDate!=null)
	    	{
	    		Date startDate_ = new SimpleDateFormat("yyyy-mm-dd").parse(startDate);
	    		Date endDate_ = new SimpleDateFormat("yyyy-mm-dd").parse(endDate);
	    		if(startDate_.after(endDate_))
				{
					jsonObject.add("status", ERROR.INCOMPLETE_PARAMETERS);
					jsonObject.add("message", "Start date must be before or on the end date");
					JsonObject jsonObj = jsonObject.build();
		            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
				}
	    		startDate = startDate + " 00:00";
	    		endDate = endDate + " 23:59";
	    		hql = hql + " AND (tp.scheduleDayName >= '"+startDate+"' AND tp.scheduleDayName <= '"+endDate+"')";
	    	}
			
			swpService = serviceLocator.getSwpService();
			Application app = Application.getInstance(swpService);
			//log.info"3.hql ==" + hql);
			Collection<PublicHoliday> publicHolidays = (Collection<PublicHoliday>)swpService.getAllRecordsByHQL(hql);
			
			
			jsonObject.add("message", "Schedule Day Listing");
			jsonObject.add("scheduleDayList", new Gson().toJson(publicHolidays));
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

    
    
    public Response createNewScheduleStation(Double refundPercentage, String lineCode, String purchaseStartDate, String scheduleStationCode, String vehicleCode, String seatSections, 
    		String scheduleDayName, String departureTime, String arrivalTime, String stationCode, String courierSections, String clientCode, String token, 
    		String requestId, String ipAddress, String departureStationCode, String destinationStationCode)
	{
    	JsonObjectBuilder jsonObject = Json.createObjectBuilder();
    	
    	
		try{
			if((seatSections==null && courierSections==null) || refundPercentage==null || scheduleDayName==null || lineCode==null || (departureTime==null && arrivalTime==null) || stationCode==null || token==null || clientCode==null)
			{
				//log.inforequestId + "Test 2");
				jsonObject.add("status", ERROR.INCOMPLETE_PARAMETERS);
				jsonObject.add("message", "Incomplete Parameters provided in request.");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			swpService = serviceLocator.getSwpService();
			Application app = Application.getInstance(swpService);
			JSONObject verifyJ = UtilityHelper.verifyToken(token, app);
			//log.info"1---verifyJ ==" + verifyJ.length() + " && v= " + verifyJ.toString());
			if(verifyJ.length()==0 || (verifyJ.length()>0 && verifyJ.has("active") && verifyJ.getInt("active")==0))
			{
				//log.info"2---verifyJ ==" + verifyJ.toString());
				jsonObject.add("status", ERROR.FORCE_LOGOUT_USER);
				jsonObject.add("message", "Your session has expired. Please log in again");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			//log.inforequestId + "verifyJ ==" + verifyJ.toString());
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
						"tp.userStatus = " + UserStatus.ACTIVE.ordinal() + " AND tp.deletedAt IS NULL AND tp.client.clientCode = '"+clientCode+"'";
				//log.inforequestId + "hql ==" + hql);
				user = (User)this.swpService.getUniqueRecordByHQL(hql);
				
				
				if(user==null)
				{

					//log.inforequestId + "user IS NULL");
					//log.inforequestId + "user firstname = " + user.getFirstName());
					//log.inforequestId + "user lastname = " + user.getLastName());
					jsonObject.add("status", ERROR.INVALID_SCHEDULE_DAY_CREATION_PRIVILEDGES);
					jsonObject.add("message", "Invalid Client Creation Priviledges");
					JsonObject jsonObj = jsonObject.build();
		            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
				}
				//roleCode = user.getRoleCode();
			}
			
			if(roleCode==null || (roleCode!=null && !roleCode.equals(RoleType.ADMIN_STAFF)))
			{
				jsonObject.add("status", ERROR.INVALID_SCHEDULE_DAY_CREATION_PRIVILEDGES);
				jsonObject.add("message", "Invalid Schedule Date Creation Priviledges");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			
			
			String hql = "Select tp from ScheduleDay tp where tp.scheduleDayName = '" + scheduleDayName + "' AND tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL";
			ScheduleDay scheduleDay = (ScheduleDay)this.swpService.getUniqueRecordByHQL(hql);
			if(scheduleDay==null)
			{
				jsonObject.add("message", "Schedule day could not be found");
				jsonObject.add("status", ERROR.PUBLIC_SCHEDULE_DAY_ALREADY_EXISTS);
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			
			hql = "Select tp from Line tp where tp.lineCode = '" + lineCode + "' AND tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL";
			Line line = (Line)this.swpService.getUniqueRecordByHQL(hql);
			if(scheduleDay==null)
			{
				jsonObject.add("message", "Trip Line could not be found");
				jsonObject.add("status", ERROR.TRIP_LINE_DOES_NOT_EXIST);
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			
			hql = "Select tp from Station tp where tp.stationCode = '" + stationCode + "' AND tp.client.clientCode = '"+clientCode+"'";
			Station station = (Station)this.swpService.getUniqueRecordByHQL(hql);
			if(station==null)
			{
				jsonObject.add("message", "Station could not be found");
				jsonObject.add("status", ERROR.STATION_NOT_FOUND);
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			hql = "Select tp from Client tp where tp.clientCode = '" + clientCode + "'";
			Client client = (Client)this.swpService.getUniqueRecordByHQL(hql);
			
			hql = "Select tp from Vehicle tp where tp.vehicleCode = '" + vehicleCode + "'";
			Vehicle vehicle = (Vehicle)this.swpService.getUniqueRecordByHQL(hql);
			
			Collection<VehicleSeatSection> vehicleSeatSections = null;
			Collection<VehicleSeatSection> vehicleCabinSections = null;
			if(seatSections!=null)
			{
				JSONArray seatSectionArray = new JSONArray(seatSections);
				List<String> l = new ArrayList();
				for(int k=0; k<seatSectionArray.length(); k++)
				{
					l.add(seatSectionArray.getString(k));
				}
				hql = "Select tp from VehicleSeatSection tp where tp.sectionCode IN ('" + StringUtils.join(l, "','") + "') AND "
						+ " tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL AND tp.cabinType = " + CabinType.PASSENGER.ordinal();
				//log.info"hql === " + hql);
				vehicleSeatSections = (Collection<VehicleSeatSection>)swpService.getAllRecordsByHQL(hql);
			}
			if(courierSections!=null)
			{
				JSONArray seatSectionArray = new JSONArray(courierSections);
				List<String> l = new ArrayList();
				for(int k=0; k<seatSectionArray.length(); k++)
				{
					l.add(seatSectionArray.getString(k));
				}
				hql = "Select tp from VehicleSeatSection tp where tp.sectionCode IN ('" + StringUtils.join(l, "','") + "') AND "
						+ " tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL AND tp.cabinType = " + CabinType.COURIER.ordinal();
				//log.info"hql === " + hql);
				vehicleCabinSections = (Collection<VehicleSeatSection>)swpService.getAllRecordsByHQL(hql);
			}
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Date departureTimeFormatted = departureTime==null ? null : sdf.parse(scheduleDayName + " " + departureTime);
			Date arrivalTimeFormatted = arrivalTime==null ? null : sdf.parse(scheduleDayName + " " + arrivalTime);
			List<ScheduleStation> scheduleStationList = new ArrayList<ScheduleStation>();
			
			ScheduleStationCode ssc = null;
			VehicleSchedule vs = null;
			if(scheduleStationCode!=null)
			{
				hql = "Select tp from ScheduleStationCode tp where lower(tp.scheduleStationCode) = '" + scheduleStationCode.toLowerCase() + "' AND tp.deletedAt IS NULL " +
						"AND tp.client.clientCode = '"+clientCode+"'";
				ssc = (ScheduleStationCode)swpService.getUniqueRecordByHQL(hql);
				
				
			}
			if(ssc==null)
			{
				if(departureTimeFormatted!=null)
				{
					if(vehicleCode==null)
					{
						jsonObject.add("message", "No vehicle has been mapped to this schedule");
						jsonObject.add("status", ERROR.NO_VEHICLE_MAPPED_TO_SCHEDULE);
						JsonObject jsonObj = jsonObject.build();
			            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
					}
					
					Station departureStation = null;
					Station destinationStation = null;
					if(departureStationCode!=null && destinationStationCode!=null)
					{
						hql = "Select tp from Station tp where tp.stationCode = '"+departureStationCode+"' AND tp.deletedAt is NULL AND tp.client.clientCode = '"+clientCode+"'";
						departureStation = (Station)swpService.getUniqueRecordByHQL(hql);
						
						hql = "Select tp from Station tp where tp.stationCode = '"+destinationStationCode+"' AND tp.deletedAt is NULL AND tp.client.clientCode = '"+clientCode+"'";
						destinationStation = (Station)swpService.getUniqueRecordByHQL(hql);
					}
					
					if(departureStation==null || destinationStation==null)
					{
						jsonObject.add("message", "Departure and final destination station codes need to be provided");
						jsonObject.add("status", ERROR.INCOMPLETE_PARAMETERS);
						JsonObject jsonObj = jsonObject.build();
			            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
					}
					
					
					String directionCode = null;
					Calendar c = Calendar.getInstance();
					c.setTime(departureTimeFormatted);
					Integer departureTimeFormattedDay = c.get(Calendar.DAY_OF_WEEK);
					switch(destinationStation.getRegionType().name())
					{
						case "NORTH":
							directionCode = "02";
							break;
						case "SOUTH":
							directionCode = "01";
							break;
						case "WEST":
							directionCode = "58";
							break;
						case "EAST":
							directionCode = "57";
							break;
						default:
							directionCode = null;
					}
					
					
					if(directionCode==null)
					{
						jsonObject.add("message", "We could not generate a train number for this trip. Please ensure your station locations have " +
								"been specified as NORTH, SOUTH, EAST and WEST to enable us calculate the direction of the trip");
						jsonObject.add("status", ERROR.INCOMPLETE_PARAMETERS);
						JsonObject jsonObj = jsonObject.build();
			            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
					}
					
					String trainNumber = departureTimeFormattedDay + "" + directionCode;
					
					scheduleStationCode = RandomStringUtils.randomNumeric(12);
					ssc = new ScheduleStationCode();
					ssc.setClient(client);
					ssc.setCreatedAt(new Date());
					ssc.setUpdatedAt(new Date());
					ssc.setScheduleStationCode(scheduleStationCode);
					ssc.setDepartureDate(departureTimeFormatted);
					ssc.setTrainNumber(trainNumber);
					if(vehicleCabinSections!=null && vehicleCabinSections.size()>0)
						ssc.setCourierEnabled(Boolean.TRUE);
					else
						ssc.setCourierEnabled(Boolean.FALSE);
					if(vehicleSeatSections!=null && vehicleSeatSections.size()>0)
						ssc.setPassengerEnabled(Boolean.TRUE);
					else
						ssc.setPassengerEnabled(Boolean.FALSE);
					ssc = (ScheduleStationCode)swpService.createNewRecord(ssc);
					
					vs = new VehicleSchedule();
					vs.setClient(client);
					vs.setCreatedAt(new Date());
					vs.setScheduleStationCode(ssc);
					vs.setUpdatedAt(new Date());
					vs.setVehicle(vehicle);
					vs.setLine(line);
					vs.setRefundIndex(refundPercentage);
					if(vehicleCabinSections!=null && vehicleCabinSections.size()>0)
						vs.setCourierEnabled(Boolean.TRUE);
					else
						vs.setCourierEnabled(Boolean.FALSE);
					if(vehicleSeatSections!=null && vehicleSeatSections.size()>0)
						vs.setPassengerEnabled(Boolean.TRUE);
					else
						vs.setPassengerEnabled(Boolean.FALSE);
					if(purchaseStartDate!=null)
					{
						Date purchaseStartDate_ = new SimpleDateFormat("yyyy-mm-dd").parse(purchaseStartDate);
						vs.setElectronicPurchaseStartDate(purchaseStartDate_);
					}
					vs = (VehicleSchedule)swpService.createNewRecord(vs);
					AuditTrail ad = UtilityHelper.createAuditTrailEntry(ipAddress, RequestType.NEW_SCHEDULE_STATION_CREATION, requestId, this.swpService, 
							verifyJ.has("username") ? verifyJ.getString("username") : null, vs.getVehicleScheduleId(), VehicleSchedule.class.getName(), 
							"New Vehicle Schedule. Vehicle: " + vehicle.getVehicleName() + " | Line: " + line.getLineName() + 
							" | Schedule Code: " + ssc.getScheduleStationCode(), clientCode);
				}
			}
			else
			{
				hql = "Select tp from VehicleSchedule tp where tp.deletedAt IS NULL AND tp.client.clientCode = '"+ clientCode +
						"' AND tp.scheduleStationCode.scheduleStationId = " + ssc.getScheduleStationId();
				//log.info"hql ------> " + hql);
				vs = (VehicleSchedule)swpService.getUniqueRecordByHQL(hql);
			}
			ScheduleStation scheduleStationArriv = null;
			ScheduleStation scheduleStationDep = null;
			if(vehicleSeatSections!=null && vehicleSeatSections.size()>0)
			{
				if(arrivalTimeFormatted!=null && arrivalTimeFormatted!=null)
				{
					hql = "Select tp from ScheduleStation tp where (tp.scheduleDay.scheduleDayName) = '" + scheduleDayName + "' AND tp.departureTime = '" +(scheduleDayName + " " + arrivalTime)+ "' " +
							"AND tp.station.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL " + 
							"AND tp.scheduleStationCode.scheduleStationCode = '"+ ssc.getScheduleStationCode()+"'";
					//log.info"hql ==" + hql);
					ScheduleStation scheduleStationCheck = (ScheduleStation)swpService.getUniqueRecordByHQL(hql);
					scheduleStationArriv = scheduleStationCheck;
					if(scheduleStationCheck==null)
					{
						scheduleStationArriv = new ScheduleStation();
						scheduleStationArriv.setCreatedAt(new Date());
						scheduleStationArriv.setUpdatedAt(new Date());
						scheduleStationArriv.setArrivalTime(arrivalTimeFormatted);
						scheduleStationArriv.setScheduleDay(scheduleDay);
						scheduleStationArriv.setStation(station);
						scheduleStationArriv.setClient(client);
						scheduleStationArriv.setScheduleStationCode(ssc);
						scheduleStationArriv.setVehicle(vehicle);
						scheduleStationArriv = (ScheduleStation)this.swpService.createNewRecord(scheduleStationArriv);
						
						if(scheduleStationArriv!=null)
						{
							scheduleStationList.add(scheduleStationArriv);
							AuditTrail ad = UtilityHelper.createAuditTrailEntry(ipAddress, RequestType.NEW_SCHEDULE_STATION_CREATION, requestId, this.swpService, 
									verifyJ.has("username") ? verifyJ.getString("username") : null, scheduleStationArriv.getScheduleStationId(), ScheduleStation.class.getName(), 
									"New Passenger Schedule Station. Arrival Time: " + arrivalTimeFormatted + " | Station: " + 
									station.getStationName() + " | Schedule Code: " + ssc.getScheduleStationCode(), clientCode);
						}
					}
				}
				
				if(departureTimeFormatted!=null && departureTimeFormatted!=null)
				{
					hql = "Select tp from ScheduleStation tp where (tp.scheduleDay.scheduleDayName) = '" + scheduleDayName + "' AND tp.departureTime = '" +(scheduleDayName + " " + departureTime)+ "' " +
							"AND tp.station.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL " + 
							"AND tp.scheduleStationCode.scheduleStationCode = '"+ ssc.getScheduleStationCode()+"'";
					//log.info"hql ==" + hql);
					ScheduleStation scheduleStationCheck = (ScheduleStation)swpService.getUniqueRecordByHQL(hql);
					scheduleStationDep = scheduleStationCheck;
					if(scheduleStationCheck==null)
					{
						scheduleStationDep = new ScheduleStation();
						scheduleStationDep.setCreatedAt(new Date());
						scheduleStationDep.setUpdatedAt(new Date());
						scheduleStationDep.setDepartureTime(departureTimeFormatted);
						scheduleStationDep.setScheduleDay(scheduleDay);
						scheduleStationDep.setStation(station);
						scheduleStationDep.setClient(client);
						scheduleStationDep.setScheduleStationCode(ssc);
						scheduleStationDep.setVehicle(vehicle);
						scheduleStationDep = (ScheduleStation)this.swpService.createNewRecord(scheduleStationDep);
						
						if(scheduleStationDep!=null)
						{
							AuditTrail ad = UtilityHelper.createAuditTrailEntry(ipAddress, RequestType.NEW_SCHEDULE_STATION_CREATION, requestId, this.swpService, 
									verifyJ.has("username") ? verifyJ.getString("username") : null, scheduleStationDep.getScheduleStationId(), ScheduleStation.class.getName(), 
									"New Passenger Schedule Station. Departure Time: " + departureTimeFormatted + " | Station: " + 
									station.getStationName() + " | Schedule Code: " + ssc.getScheduleStationCode(), clientCode);
						}
					}
					if(scheduleStationDep!=null)
					{
						scheduleStationList.add(scheduleStationDep);
						Iterator<VehicleSeatSection> vssIt = vehicleSeatSections.iterator();
						while(vssIt.hasNext())
						{
							VehicleSeatSection vss = vssIt.next();
							ScheduleStationSeatSection ssss = new ScheduleStationSeatSection();
							ssss.setClient(client);
							ssss.setCreatedAt(new Date());
							ssss.setUpdatedAt(new Date());
							ssss.setOriginScheduleStation(scheduleStationDep);
							ssss.setVehicleSeatSection(vss);
							ssss = (ScheduleStationSeatSection)swpService.createNewRecord(ssss);
							AuditTrail ad = UtilityHelper.createAuditTrailEntry(ipAddress, RequestType.NEW_SCHEDULE_STATION_SEAT_SECTION_CREATION, requestId, this.swpService, 
									verifyJ.has("username") ? verifyJ.getString("username") : null, vss.getVehicleSeatSectionId(), VehicleSeatSection.class.getName(), 
									"New Schedule Station Cabin. Arrival Time: " + scheduleStationDep.getDepartureTime()==null ? "N/A" : scheduleStationDep.getDepartureTime() + 
									" | Station: " + station.getStationName() + " | Schedule Code: " + ssc.getScheduleStationCode(), clientCode);
							
							hql = "Select tp from VehicleSeat tp where tp.vehicleSeatSection.vehicleSeatSectionId = "+vss.getVehicleSeatSectionId()+" AND "
									+ " tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL";
							//log.info"hql === " + hql);
							Collection<VehicleSeat> vehicleSeats = (Collection<VehicleSeat>)swpService.getAllRecordsByHQL(hql);
							Iterator<VehicleSeat> vsIter = vehicleSeats.iterator();
							while(vsIter.hasNext())
							{
								VehicleSeat vseat = vsIter.next();
								ScheduleStationSeatAvailability sssa = new ScheduleStationSeatAvailability();
								sssa.setBoughtByCustomer(null);
								sssa.setCreatedAt(new Date());
								sssa.setDeletedAt(null);
								sssa.setLockedDown(Boolean.FALSE);
								sssa.setLockedDownBy(null);
								sssa.setLockedDownExpiryDate(null);
								sssa.setPassengerType(null);
								sssa.setSeatAvailabilityStatus(SeatAvailabilityStatus.OPEN);
								sssa.setUpdatedAt(new Date());
								sssa.setVehicleSeat(vseat);
								sssa.setScheduleStationSeatSection(ssss);
								sssa = (ScheduleStationSeatAvailability)swpService.createNewRecord(sssa);
								ad = UtilityHelper.createAuditTrailEntry(ipAddress, RequestType.SCHEDULE_STATION_SEAT_AVAILABILITY_CREATION, requestId, this.swpService, 
										verifyJ.has("username") ? verifyJ.getString("username") : null, sssa.getSchedSeatAvailId(), ScheduleStationSeatAvailability.class.getName(), 
										"New Passenger Seat Availability. Seat: " + vseat.getSeatNumber() + " | Departure Time: " + scheduleStationDep.getDepartureTime() + " | Station: " + scheduleStationDep.getStation().getStationName() + 
										" | Schedule Code: " + scheduleStationDep.getScheduleStationCode().getScheduleStationCode(), clientCode);
							}
						}
					}
				}
			}
			
			
			if(vehicleCabinSections!=null && vehicleCabinSections.size()>0)
			{
				//log.info"vehicleCabinSections>>> " + vehicleCabinSections.size());
				if(arrivalTimeFormatted!=null && scheduleStationArriv==null)
				{
					hql = "Select tp from ScheduleStation tp where (tp.scheduleDay.scheduleDayName) = '" + scheduleDayName + "' AND tp.departureTime = '" +(scheduleDayName + " " + arrivalTime)+ "' " +
							"AND tp.station.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL " + 
							"AND tp.scheduleStationCode.scheduleStationCode = '"+ ssc.getScheduleStationCode()+"'";
					//log.info"hql ==" + hql);
					ScheduleStation scheduleStationCheck = (ScheduleStation)swpService.getUniqueRecordByHQL(hql);
					scheduleStationArriv = scheduleStationCheck;
					if(scheduleStationCheck==null)
					{
						scheduleStationArriv = new ScheduleStation();
						scheduleStationArriv.setCreatedAt(new Date());
						scheduleStationArriv.setUpdatedAt(new Date());
						scheduleStationArriv.setArrivalTime(arrivalTimeFormatted);
						scheduleStationArriv.setScheduleDay(scheduleDay);
						scheduleStationArriv.setStation(station);
						scheduleStationArriv.setClient(client);
						scheduleStationArriv.setScheduleStationCode(ssc);
						scheduleStationArriv.setVehicle(vehicle);
						scheduleStationArriv = (ScheduleStation)this.swpService.createNewRecord(scheduleStationArriv);
						
						if(scheduleStationArriv!=null)
						{
							scheduleStationList.add(scheduleStationArriv);
							AuditTrail ad = UtilityHelper.createAuditTrailEntry(ipAddress, RequestType.NEW_SCHEDULE_STATION_CREATION, requestId, this.swpService, 
									verifyJ.has("username") ? verifyJ.getString("username") : null, scheduleStationArriv.getScheduleStationId(), ScheduleStation.class.getName(), 
									"New Courier Schedule Station. Arrival Time: " + arrivalTimeFormatted + 
									" | Station: " + station.getStationName() + " | Schedule Code: " + ssc.getScheduleStationCode(), clientCode);
						}
					}
				}
				
				if(departureTimeFormatted!=null && scheduleStationDep==null)
				{
					hql = "Select tp from ScheduleStation tp where (tp.scheduleDay.scheduleDayName) = '" + scheduleDayName + "' AND tp.departureTime = '" +(scheduleDayName + " " + departureTime)+ "' " +
							"AND tp.station.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL " + 
							"AND tp.scheduleStationCode.scheduleStationCode = '"+ ssc.getScheduleStationCode()+"'";
					//log.info"hql ==" + hql);
					ScheduleStation scheduleStationCheck = (ScheduleStation)swpService.getUniqueRecordByHQL(hql);
					scheduleStationDep = scheduleStationCheck;
					if(scheduleStationCheck==null)
					{
						scheduleStationDep = new ScheduleStation();
						scheduleStationDep.setCreatedAt(new Date());
						scheduleStationDep.setUpdatedAt(new Date());
						scheduleStationDep.setDepartureTime(departureTimeFormatted);
						scheduleStationDep.setScheduleDay(scheduleDay);
						scheduleStationDep.setStation(station);
						scheduleStationDep.setClient(client);
						scheduleStationDep.setScheduleStationCode(ssc);
						scheduleStationDep.setVehicle(vehicle);
						scheduleStationDep = (ScheduleStation)this.swpService.createNewRecord(scheduleStationDep);
						
						if(scheduleStationDep!=null)
						{
							AuditTrail ad = UtilityHelper.createAuditTrailEntry(ipAddress, RequestType.NEW_SCHEDULE_STATION_CREATION, requestId, this.swpService, 
									verifyJ.has("username") ? verifyJ.getString("username") : null, scheduleStationDep.getScheduleStationId(), ScheduleStation.class.getName(), 
									"New Courier Schedule Station. Departure Time: " + departureTimeFormatted + " | Station: " + 
									station.getStationName() + " | Schedule Code: " + ssc.getScheduleStationCode(), clientCode);
						}
						scheduleStationList.add(scheduleStationDep);
					}
				}
				
				if(scheduleStationDep!=null || scheduleStationArriv!=null)
				{
					Iterator<VehicleSeatSection> vssIt = vehicleCabinSections.iterator();
					while(vssIt.hasNext())
					{
						VehicleSeatSection vss = vssIt.next();
						ScheduleStationCourierSection ssss = new ScheduleStationCourierSection();
						ssss.setClient(client);
						ssss.setCreatedAt(new Date());
						ssss.setUpdatedAt(new Date());
						if(scheduleStationDep!=null)
							ssss.setOriginScheduleStation(scheduleStationDep);
						if(scheduleStationArriv!=null)
							ssss.setArrivalScheduleStation(scheduleStationArriv);
						ssss.setVehicleSeatSection(vss);
						//ssss.setCurrentAvailableTonnage(vss.getMaxTonnage());
						//ssss.setCurrentAvailableVolume(vss.getMaxVolume());
						ssss = (ScheduleStationCourierSection)swpService.createNewRecord(ssss);
						/*AuditTrail ad = UtilityHelper.createAuditTrailEntry(ipAddress, RequestType.NEW_SCHEDULE_STATION_COURIER_SECTION_CREATION, requestId, this.swpService, 
								verifyJ.has("username") ? verifyJ.getString("username") : null, ssss.getSchedStatCourSectId(), ScheduleStationCourierSection.class.getName(), 
								"New Courier Cabin Availability. Cabin: " + vss.getSectionName() +
								scheduleStationDep!=null ? 
								(" | Departure Time: " + scheduleStationDep.getDepartureTime() + 
									" | Station: " + scheduleStationDep.getStation().getStationName() + 
									" | Schedule Code: " + scheduleStationDep.getScheduleStationCode().getScheduleStationCode()) : 
								(" | Arrival Time: " + scheduleStationArriv.getDepartureTime() + 
									" | Station: " + scheduleStationArriv.getStation().getStationName() + 
									" | Schedule Code: " + scheduleStationArriv.getScheduleStationCode().getScheduleStationCode()), 
								clientCode);*/
						
						
					}
				}
			}
			
			
			hql = "Select tp from ScheduleStation tp where tp.client.clientCode = '"+ clientCode + "' AND tp.deletedAt IS NULL " +
				"AND tp.scheduleStationCode.scheduleStationId = '"+vs.getScheduleStationCode().getScheduleStationId()+"' " +
				"ORDER BY COALESCE(tp.arrivalTime, tp.departureTime)";
			Collection<ScheduleStation> allScheduleStations = (Collection<ScheduleStation>)swpService.getAllRecordsByHQL(hql);
			Iterator<ScheduleStation> itSch = allScheduleStations.iterator();
			int itCounter = 0;
			ScheduleStation depScheduleStation = null;
			ScheduleStation arrScheduleStation = null;
			while(itSch.hasNext())
			{
				ScheduleStation schSt = itSch.next();
				if(itCounter==0)
				{
					depScheduleStation = schSt;
				}
				if(itCounter==(allScheduleStations.size()-1))
				{
					arrScheduleStation = schSt;
				}
				itCounter++;
			}
			
			if(depScheduleStation!=null)
				vs.setDepartureScheduleStation(depScheduleStation);
			if(arrScheduleStation!=null)
				vs.setArrivalScheduleStation(arrScheduleStation);
			swpService.updateRecord(vs);
			
			
			
			if(scheduleStationList!=null && scheduleStationList.size()>0)
			{
				
				jsonObject.add("message", "New Train Trip Created successfully");
				jsonObject.add("scheduleStation", new Gson().toJson(scheduleStationList));
				jsonObject.add("scheduleStationCode", scheduleStationCode);
				jsonObject.add("status", ERROR.GENERAL_SUCCESS);
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			else
			{
				jsonObject.add("message", "New Train Trip Creation Failed");
				jsonObject.add("status", ERROR.PUBLIC_SCHEDULE_STATION_CREATE_FAIL);
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			
			
		}catch(Exception e)
		{
			//log.info"Error = " + e.getMessage());
			log.warn(e); log.error("exception...", e);
			e.printStackTrace();
			JsonObject jsonObj = jsonObject.build();
            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.INTERNAL_SERVER_ERROR ).entity( jsonObj.toString() ).build();
		}
	}

    
    
    
    public Response createNewScheduleDay(String scheduleDates, String clientCode, String token, String requestId, String ipAddress)
	{
    	JsonObjectBuilder jsonObject = Json.createObjectBuilder();
    	
    	
		try{
			if(scheduleDates==null || token==null || clientCode==null)
			{
				//log.inforequestId + "Test 2");
				jsonObject.add("status", ERROR.INCOMPLETE_PARAMETERS);
				jsonObject.add("message", "Incomplete Parameters provided in request.");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			swpService = serviceLocator.getSwpService();
			Application app = Application.getInstance(swpService);
			JSONObject verifyJ = UtilityHelper.verifyToken(token, app);
			//log.info"1---verifyJ ==" + verifyJ.length() + " && v= " + verifyJ.toString());
			if(verifyJ.length()==0 || (verifyJ.length()>0 && verifyJ.has("active") && verifyJ.getInt("active")==0))
			{
				//log.info"2---verifyJ ==" + verifyJ.toString());
				jsonObject.add("status", ERROR.FORCE_LOGOUT_USER);
				jsonObject.add("message", "Your session has expired. Please log in again");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			//log.inforequestId + "verifyJ ==" + verifyJ.toString());
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
						"tp.userStatus = " + UserStatus.ACTIVE.ordinal() + " AND tp.deletedAt IS NULL AND tp.client.clientCode = '"+clientCode+"'";
				//log.inforequestId + "hql ==" + hql);
				user = (User)this.swpService.getUniqueRecordByHQL(hql);
				
				
				if(user==null)
				{

					//log.inforequestId + "user IS NULL");
					//log.inforequestId + "user firstname = " + user.getFirstName());
					//log.inforequestId + "user lastname = " + user.getLastName());
					jsonObject.add("status", ERROR.INVALID_SCHEDULE_DAY_CREATION_PRIVILEDGES);
					jsonObject.add("message", "Invalid Client Creation Priviledges");
					JsonObject jsonObj = jsonObject.build();
		            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
				}
				//roleCode = user.getRoleCode();
			}
			
			if(roleCode==null || (roleCode!=null && !roleCode.equals(RoleType.ADMIN_STAFF)))
			{
				jsonObject.add("status", ERROR.INVALID_SCHEDULE_DAY_CREATION_PRIVILEDGES);
				jsonObject.add("message", "Invalid Schedule Date Creation Priviledges");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			JSONArray scheduleDays = new JSONArray(scheduleDates);
			List<ScheduleDay> scheduleDayList = new ArrayList<ScheduleDay>();
			
			for(int i=0; i<scheduleDays.length(); i++)
			{
				String scheduleDayName = scheduleDays.getString(i);
				//log.info"scheduleDayName --- " + scheduleDayName);
				String hql = "Select tp from ScheduleDay tp where tp.scheduleDayName = '" + scheduleDayName + "' AND tp.client.clientCode = '"+clientCode+"'";
				//log.info"hql --- " + hql);
				ScheduleDay scheduleDay = (ScheduleDay)this.swpService.getUniqueRecordByHQL(hql);
				if(scheduleDay!=null)
				{
					
				}
				else
				{
					hql = "Select tp from Client tp where tp.clientCode = '" + clientCode + "'";
					Client client = (Client)this.swpService.getUniqueRecordByHQL(hql);
					
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					Date scheduleDayFormatted = scheduleDayName==null ? null : sdf.parse(scheduleDayName);
					//log.info"scheduleDayFormatted --- " + scheduleDayFormatted.toString());
					
					ScheduleDay scheduleDay_ = new ScheduleDay();
					scheduleDay_.setClient(client);
					scheduleDay_.setCreatedAt(new Date());
					scheduleDay_.setUpdatedAt(new Date());
					scheduleDay_.setScheduleDayName(scheduleDayFormatted);
					scheduleDay_ = (ScheduleDay)this.swpService.createNewRecord(scheduleDay_);
					
					if(scheduleDay_!=null)
					{
						scheduleDayList.add(scheduleDay_);
						AuditTrail ad = UtilityHelper.createAuditTrailEntry(ipAddress, RequestType.NEW_SCHEDULE_DAY_CREATION, requestId, this.swpService, 
								verifyJ.has("username") ? verifyJ.getString("username") : null, scheduleDay_.getScheduleDayId(), ScheduleDay.class.getName(), 
								"New Schedule Day. Day: " + scheduleDayFormatted + " | Created By: " + user.getFirstName() + " " + user.getLastName(), clientCode);
					}
				}
			}
			if(scheduleDayList!=null && scheduleDayList.size()>0)
			{
				
				jsonObject.add("message", "New Schedule Days Created successfully");
				jsonObject.add("scheduleStation", new Gson().toJson(scheduleDayList));
				jsonObject.add("status", ERROR.GENERAL_SUCCESS);
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			else
			{
				jsonObject.add("message", "New Schedule Days Creation Failed");
				jsonObject.add("status", ERROR.PUBLIC_SCHEDULE_STATION_CREATE_FAIL);
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			
			
		}catch(Exception e)
		{
			//log.info"Error = " + e.getMessage());
			log.warn(e); log.error("exception...", e);
			e.printStackTrace();
			JsonObject jsonObj = jsonObject.build();
            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.INTERNAL_SERVER_ERROR ).entity( jsonObj.toString() ).build();
		}
	}

    
    
    
    
    
    
    
    public Response getScheduleStations(String scheduleDayName, String departureTime, String arrivalTime, Long departureStationId, Long arrivalStationId, String token, String clientCode, String requestId, String ipAddress) {
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
			
			String daSql_ = "Select ta.scheduleStationId from ScheduleStation ta";
			String daSql = "";
			if(departureStationId!=null)
			{
				//Date departureTimeFormatted = (new SimpleDateFormat("yyyy-MM-dd HH:mm")).parse(departureTime);
				if(departureTime!=null)
					daSql = daSql + " (ta.departureTime = '" + departureTime + "' AND ta.station.stationId = "+departureStationId+" AND ta.client.clientCode = '"+clientCode+"') ";
				else
					daSql = daSql + " (ta.station.stationId = "+departureStationId+" AND ta.client.clientCode = '"+clientCode+"') ";
			}
			else
			{
				if(departureTime!=null)
					daSql = daSql + " (ta.departureTime = '" + departureTime + "' AND ta.client.clientCode = '"+clientCode+"') ";
			}
			
			
			if(arrivalStationId!=null)
			{
				//Date arrivalTimeFormatted = (new SimpleDateFormat("yyyy-MM-dd HH:mm")).parse(arrivalTime);
				if(daSql.length()>0)
				{
					daSql = daSql + " OR ";
				}
				
				if(arrivalTime!=null)
					daSql = daSql + "(ta.arrivalTime = '" + arrivalTime + "' AND ta.station.stationId = "+arrivalStationId+" AND ta.client.clientCode = '"+clientCode+"')";
				else
					daSql = daSql + "(ta.station.stationId = "+arrivalStationId+" AND ta.client.clientCode = '"+clientCode+"') ";
			}
			else
			{
				if(arrivalTime!=null)
					daSql = daSql + "(ta.arrivalTime = '" + arrivalTime + "' AND ta.client.clientCode = '"+clientCode+"') ";
			}
			daSql = daSql_ + (daSql.length()==0 ? "" : (" where " + daSql));
			//log.info"3.daSql ==" + daSql);
			List<Long> scheduleStationIds = (List<Long>)swpService.getAllRecordsByHQL(daSql);
			
			//log.info"3.scheduleStationIds ==" + StringUtils.join(scheduleStationIds, ','));
			
			String hql = "Select tp from ScheduleStation tp where tp.client.clientCode = '" + clientCode + "' AND tp.deletedAt IS NULL";
			if(scheduleDayName!=null)
			{
				hql = hql + " AND tp.scheduleDay.scheduleDayName = '" + scheduleDayName + "' ";
			}
			if(scheduleStationIds.size()>0)
			{
				hql = hql + " AND tp.scheduleStationId IN (" + StringUtils.join(scheduleStationIds, ',') + ") ";
			}
			Collection<ScheduleStation> scheduleStations = (Collection<ScheduleStation>)swpService.getAllRecordsByHQL(hql);
			
			
			jsonObject.add("message", "Schedule Station Listing");
			jsonObject.add("scheduleStationList", new Gson().toJson(scheduleStations));
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
    
    
    
    
    public Response getListScheduleStations(String scheduleStationCode, String token, String clientCode, String requestId, String ipAddress) {
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
			ScheduleStationCode scheduleStationCode_ = null;
			Collection<ScheduleStation> scheduleStations = null;
			if(scheduleStationCode!=null)
			{
				String hql = "Select tp from ScheduleStationCode tp where tp.scheduleStationCode = '"+scheduleStationCode+"' AND tp.deletedAt IS NULL AND client.clientCode = '"+clientCode+"'";
				scheduleStationCode_ = (ScheduleStationCode)swpService.getUniqueRecordByHQL(hql);
				if(scheduleStationCode_!=null)
				{
					hql = "Select tp from ScheduleStation tp where tp.scheduleStationCode.scheduleStationId = " + scheduleStationCode_.getScheduleStationId() + " AND tp.client.clientCode = '" + clientCode + "' AND tp.deletedAt IS NULL";
					scheduleStations = (Collection<ScheduleStation>)swpService.getAllRecordsByHQL(hql);
					if(scheduleStations!=null && scheduleStations.size()>0)
					{
						jsonObject.add("message", "Schedule Station Listing");
						jsonObject.add("scheduleStationList", new Gson().toJson(scheduleStations));
						jsonObject.add("scheduleStationCode", new Gson().toJson(scheduleStationCode));
						jsonObject.add("status", ERROR.GENERAL_SUCCESS);
						JsonObject jsonObj = jsonObject.build();
			            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
					}
				}
				jsonObject.add("message", "No Schedule Station Listing");
				jsonObject.add("scheduleStationList", new Gson().toJson(new JSONObject()));
				jsonObject.add("status", ERROR.GENERAL_SUCCESS);
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			else
			{
				String hql = "Select tp from ScheduleStation tp where tp.client.clientCode = '" + clientCode + "' AND tp.deletedAt IS NULL";
				scheduleStations = (Collection<ScheduleStation>)swpService.getAllRecordsByHQL(hql);
				if(scheduleStationCode_!=null)
				{
					hql = "Select tp from ScheduleStation tp where tp.scheduleStationCode.scheduleStationCodeId = " + scheduleStationCode_.getScheduleStationId() + " AND tp.client.clientCode = '" + clientCode + "' AND tp.deletedAt IS NULL";
					scheduleStations = (Collection<ScheduleStation>)swpService.getAllRecordsByHQL(hql);
					if(scheduleStations!=null && scheduleStations.size()>0)
					{
						jsonObject.add("message", "Schedule Station Listing");
						jsonObject.add("scheduleStationList", new Gson().toJson(scheduleStations));
						jsonObject.add("scheduleStationCode", new Gson().toJson(scheduleStationCode));
						jsonObject.add("status", ERROR.GENERAL_SUCCESS);
						JsonObject jsonObj = jsonObject.build();
						return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
						
					}
				}
				jsonObject.add("message", "No Schedule Station Listing");
				jsonObject.add("scheduleStationCode", new Gson().toJson(scheduleStationCode));
				jsonObject.add("scheduleStationList", new Gson().toJson(new JSONObject()));
				jsonObject.add("status", ERROR.GENERAL_SUCCESS);
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
    
    
    
    
    public Response createNewVehicleSchedule(String scheduleStationCode, String vehicleCode, String clientCode, String token, String requestId, String ipAddress)
	{
    	JsonObjectBuilder jsonObject = Json.createObjectBuilder();
    	
    	
		try{
			if(vehicleCode==null || token==null || clientCode==null || scheduleStationCode==null)
			{
				//log.inforequestId + "Test 2");
				jsonObject.add("status", ERROR.INCOMPLETE_PARAMETERS);
				jsonObject.add("message", "Incomplete Parameters provided in request.");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			swpService = serviceLocator.getSwpService();
			Application app = Application.getInstance(swpService);
			JSONObject verifyJ = UtilityHelper.verifyToken(token, app);
			//log.info"1---verifyJ ==" + verifyJ.length() + " && v= " + verifyJ.toString());
			if(verifyJ.length()==0 || (verifyJ.length()>0 && verifyJ.has("active") && verifyJ.getInt("active")==0))
			{
				//log.info"2---verifyJ ==" + verifyJ.toString());
				jsonObject.add("status", ERROR.FORCE_LOGOUT_USER);
				jsonObject.add("message", "Your session has expired. Please log in again");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			//log.inforequestId + "verifyJ ==" + verifyJ.toString());
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
				String hql = "select tp from User tp where tp.client.clientCode = '" + clientCode + "' AND tp.username = '" + username + "' AND " +
						"tp.userStatus = " + UserStatus.ACTIVE.ordinal() + " AND tp.deletedAt IS NULL";
				//log.inforequestId + "hql ==" + hql);
				user = (User)this.swpService.getUniqueRecordByHQL(hql);
				
				
				if(user==null)
				{

					//log.inforequestId + "user IS NULL");
					//log.inforequestId + "user firstname = " + user.getFirstName());
					//log.inforequestId + "user lastname = " + user.getLastName());
					jsonObject.add("status", ERROR.INVALID_VEHICLE_SCHEDULE_CREATION_PRIVILEDGES);
					jsonObject.add("message", "Invalid Vehicle Schedule Creation Priviledges");
					JsonObject jsonObj = jsonObject.build();
		            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
				}
				//roleCode = user.getRoleCode();
			}
			
			if(roleCode==null || (roleCode!=null && !roleCode.equals(RoleType.ADMIN_STAFF)))
			{
				jsonObject.add("status", ERROR.INVALID_VEHICLE_SCHEDULE_CREATION_PRIVILEDGES);
				jsonObject.add("message", "Invalid Vehicle Schedule Creation Priviledges");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			String hql = "Select tp from VehicleSchedule tp where lower(tp.vehicle.vehicleCode) = '" + vehicleCode.toLowerCase() + "' AND tp.scheduleStationCode.scheduleStationCode = '" +scheduleStationCode.toLowerCase()+ "' " +
					"AND tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL";
			//log.info"hql ==" + hql);
			VehicleSchedule vehicleSchedule = (VehicleSchedule)swpService.getUniqueRecordByHQL(hql);
			
			if(vehicleSchedule!=null)
			{
				//bank already exists
				jsonObject.add("message", "Vehicle Schedule Creation Failed. A vehicle schedule matching the provided parameters already exists");
				jsonObject.add("status", ERROR.VEHICLE_SCHEDULE_ALREADY_EXISTS);
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			hql = "Select tp from ScheduleStation tp where tp.client.clientCode = '" + clientCode + "' AND  lower(tp.scheduleStationCode) = '" + scheduleStationCode.toLowerCase() + "' AND tp.deletedAt IS NULL";
			ScheduleStation scheduleStation = (ScheduleStation)this.swpService.getUniqueRecordByHQL(hql);
			
			hql = "Select tp from Vehicle tp where tp.client.clientCode = '" + clientCode + "' AND  lower(tp.vehicleCode) = '" + vehicleCode.toLowerCase() + "' AND tp.deletedAt IS NULL";
			Vehicle vehicle = (Vehicle)this.swpService.getUniqueRecordByHQL(hql);
			
			hql = "Select tp from Client tp where tp.clientCode = '" + clientCode + "' AND tp.deletedAt IS NULL";
			Client client = (Client)this.swpService.getUniqueRecordByHQL(hql);
			
			hql = "Select tp from ScheduleStationCode tp where tp.scheduleStationCode = '"+scheduleStationCode+"' AND tp.client.clientCode = '" + clientCode + "' AND tp.deletedAt IS NULL";
			ScheduleStationCode scheduleStationCode_ = (ScheduleStationCode)this.swpService.getUniqueRecordByHQL(hql);
			
			vehicleSchedule = new VehicleSchedule();
			vehicleSchedule.setCreatedAt(new Date());
			vehicleSchedule.setUpdatedAt(new Date());
			vehicleSchedule.setScheduleStationCode(scheduleStationCode_);
			vehicleSchedule.setVehicle(vehicle);
			vehicleSchedule.setClient(client);
			vehicleSchedule = (VehicleSchedule)this.swpService.createNewRecord(vehicleSchedule);
			
			AuditTrail ad = UtilityHelper.createAuditTrailEntry(ipAddress, RequestType.NEW_VEHICLE_SCHEDULE_CREATION, requestId, this.swpService, 
					verifyJ.has("username") ? verifyJ.getString("username") : null, vehicleSchedule.getVehicleScheduleId(), VehicleSchedule.class.getName(), 
					"New Vehicle Schedule. Vehicle: " + vehicle.getVehicleName() + " | Station Code: " + scheduleStationCode_.getScheduleStationCode(), clientCode);
			
			if(vehicleSchedule!=null)
			{
				
				jsonObject.add("message", "New Vehicle Schedule Created successfully");
				jsonObject.add("vehicleSchedule", new Gson().toJson(vehicleSchedule));
				jsonObject.add("status", ERROR.GENERAL_SUCCESS);
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			else
			{
				jsonObject.add("message", "New Vehicle Schedule Creation Failed");
				jsonObject.add("status", ERROR.VEHICLE_SCHEDULE_CREATE_FAIL);
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			
			
		}catch(Exception e)
		{
			//log.info"Error = " + e.getMessage());
			log.warn(e); log.error("exception...", e);
			e.printStackTrace();
			JsonObject jsonObj = jsonObject.build();
            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.INTERNAL_SERVER_ERROR ).entity( jsonObj.toString() ).build();
		}
	}

    
    public Response listVehicleSchedules(String vehicleCode, String startingDate, Long scheduleStationId, Long vehicleId, String token, String clientCode, String requestId, String ipAddress) {
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
			
			String hql = "Select tp from VehicleSchedule tp where tp.deletedAt IS NULL";
			if(vehicleId!=null)
			{
				hql = hql + " AND tp.vehicle.vehicleId = "+vehicleId;
			}
			if(vehicleCode!=null)
			{
				hql = hql + " AND tp.vehicle.vehicleCode = '"+vehicleCode + "'";
			}
			if(scheduleStationId!=null)
			{
				hql = hql + " AND tp.scheduleStationCode.scheduleStationId = " + scheduleStationId;
			}
			if(startingDate!=null)
			{
				startingDate = startingDate  + " 00:00:00";
				hql = hql + " AND tp.scheduleStationCode.departureDate >= '" + startingDate + "' AND tp.scheduleStationCode.departureDate <= CURRENT_TIMESTAMP";
			}
			//log.info"3.hql ==" + hql);
			Collection<VehicleSchedule> vehicleSchedules = (Collection<VehicleSchedule>)swpService.getAllRecordsByHQL(hql);
			
			jsonObject.add("message", "Vehicle Schedule Listing");
			jsonObject.add("vehicleScheduleList", new Gson().toJson(vehicleSchedules));
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
    
    
    public Response getVehicleSchedule(Long vehicleScheduleId, Long scheduleStationId, Long vehicleId, String token, String clientCode, String requestId, String ipAddress) {
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
			
			String hql = "Select tp from VehicleSchedule tp where tp.deletedAt IS NULL";
			if(vehicleScheduleId!=null)
			{
				hql = hql + " AND tp.vehicleScheduleId = "+vehicleScheduleId;
			}
			if(vehicleId!=null)
			{
				hql = hql + " AND tp.vehicle.vehicleId = "+vehicleId;
			}
			if(scheduleStationId!=null)
			{
				hql = hql + " AND tp.scheduleStationCode.scheduleStationId = " + scheduleStationId;
			}
			//log.info"3.hql ==" + hql);
			Collection<VehicleSchedule> vehicleSchedules = (Collection<VehicleSchedule>)swpService.getAllRecordsByHQL(hql);
			
			
			
			if(vehicleSchedules!=null)
			{
				jsonObject.add("message", "Vehicle Schedules Found");
				jsonObject.add("vehicleSchedules", new Gson().toJson(vehicleSchedules));
				jsonObject.add("status", ERROR.GENERAL_SUCCESS);
			}
			else
			{
				jsonObject.add("message", "Vehicle Schedules Not Found");
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
    
    
    public Response createNewVehicleClass(String vehicleSeatClassName, String description, String clientCode, String token, String requestId, String ipAddress)
	{
    	JsonObjectBuilder jsonObject = Json.createObjectBuilder();
    	
    	
		try{
			if(vehicleSeatClassName==null || token==null)
			{
				//log.inforequestId + "Test 2");
				jsonObject.add("status", ERROR.INCOMPLETE_PARAMETERS);
				jsonObject.add("message", "Incomplete Parameters provided in request.");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			swpService = serviceLocator.getSwpService();
			Application app = Application.getInstance(swpService);
			JSONObject verifyJ = UtilityHelper.verifyToken(token, app);
			//log.info"1---verifyJ ==" + verifyJ.length() + " && v= " + verifyJ.toString());
			if(verifyJ.length()==0 || (verifyJ.length()>0 && verifyJ.has("active") && verifyJ.getInt("active")==0))
			{
				//log.info"2---verifyJ ==" + verifyJ.toString());
				jsonObject.add("status", ERROR.FORCE_LOGOUT_USER);
				jsonObject.add("message", "Your session has expired. Please log in again");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			//log.inforequestId + "verifyJ ==" + verifyJ.toString());
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
					jsonObject.add("status", ERROR.INVALID_VEHICLE_CLASS_CREATION_PRIVILEDGES);
					jsonObject.add("message", "Invalid Vehicle Class Creation Priviledges");
					JsonObject jsonObj = jsonObject.build();
		            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
				}
				//roleCode = user.getRoleCode();
			}
			
			if(roleCode==null || (roleCode!=null && !roleCode.equals(RoleType.ADMIN_STAFF)))
			{
				jsonObject.add("status", ERROR.INVALID_VEHICLE_CLASS_CREATION_PRIVILEDGES);
				jsonObject.add("message", "Invalid Vehicle Class Creation Priviledges");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			String hql = "Select tp from VehicleSeatClass tp where lower(tp.vehicleSeatClassName) = '" + vehicleSeatClassName + "' AND tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL";
			//log.info"hql ==" + hql);
			VehicleSeatClass vehicleSeatClass = (VehicleSeatClass)swpService.getUniqueRecordByHQL(hql);
			
			if(vehicleSeatClass!=null)
			{
				//bank already exists
				jsonObject.add("message", "Vehicle Seat Class Creation Failed. A seat class name matching the name provided Already Exists");
				jsonObject.add("status", ERROR.VEHICLE_SEAT_CLASS_ALREADY_EXISTS);
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}

			hql = "Select tp from Client tp where tp.clientCode = '" +clientCode+ "' AND tp.deletedAt IS NULL";
			Client client = (Client)this.swpService.getUniqueRecordByHQL(hql);


			vehicleSeatClass = new VehicleSeatClass();
			vehicleSeatClass.setCreatedAt(new Date());
			vehicleSeatClass.setUpdatedAt(new Date());
			vehicleSeatClass.setClient(client);
			vehicleSeatClass.setDescription(description);
			vehicleSeatClass.setVehicleSeatClassCode(RandomStringUtils.randomNumeric(8));
			vehicleSeatClass.setVehicleSeatClassName(vehicleSeatClassName);

			vehicleSeatClass = (VehicleSeatClass)this.swpService.createNewRecord(vehicleSeatClass);
			
			AuditTrail ad = UtilityHelper.createAuditTrailEntry(ipAddress, RequestType.NEW_VEHICLE_SEAT_CLASS_CREATION, requestId, this.swpService, 
					verifyJ.has("username") ? verifyJ.getString("username") : null, vehicleSeatClass.getVehicleSeatClassId(), VehicleSeatClass.class.getName(), 
					"New Vehicle Seat Class. Class Name: " + vehicleSeatClassName + " | Created By: " + user.getFirstName() + " " + user.getLastName(), clientCode);
			
			if(vehicleSeatClass!=null)
			{
				
				jsonObject.add("message", "New Vehicle Seat Class Created successfully");
				jsonObject.add("vehicleSeatClass", new Gson().toJson(vehicleSeatClass));
				jsonObject.add("status", ERROR.GENERAL_SUCCESS);
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			else
			{
				jsonObject.add("message", "New Vehicle Seat Class Creation Failed");
				jsonObject.add("status", ERROR.VEHICLE_SEAT_CLASS_CREATION_FAIL);
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
		}catch(Exception e)
		{
			//log.info"Error = " + e.getMessage());
			log.warn(e); log.error("exception...", e);
			e.printStackTrace();
			JsonObject jsonObj = jsonObject.build();
            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.INTERNAL_SERVER_ERROR ).entity( jsonObj.toString() ).build();
		}
	}

    
    public Response listVehicleSeatClasses(String token, String clientCode, String requestId, String ipAddress) {
		// TODO Auto-generated method stub
    	JsonObjectBuilder jsonObject = Json.createObjectBuilder();
    	
		try{
			
			swpService = serviceLocator.getSwpService();
			Application app = Application.getInstance(swpService);
			
			String hql = "Select tp from VehicleSeatClass tp where tp.client.clientCode = '" + clientCode + "' AND tp.deletedAt IS NULL";
			//log.info"3.hql ==" + hql);
			Collection<VehicleSeatClass> vehicleSeatClasses = (Collection<VehicleSeatClass>)swpService.getAllRecordsByHQL(hql);
			
			
			jsonObject.add("message", "Vehicle Seat Class Listing");
			jsonObject.add("vehicleClassList", new Gson().toJson(vehicleSeatClasses));
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
    
    
    public Response getVehicleSeatClass(Long vehicleSeatClassId, String vehicleSeatClassName, String token, String clientCode, String requestId, String ipAddress) {
		// TODO Auto-generated method stub
    	JsonObjectBuilder jsonObject = Json.createObjectBuilder();
    	
		try{
			
			swpService = serviceLocator.getSwpService();
			Application app = Application.getInstance(swpService);
			
			String hql = "Select tp from VehicleSeatClass tp where tp.client.clientCode = '" + clientCode + "' AND tp.deletedAt IS NULL";
			//log.info"3.hql ==" + hql);
			if(vehicleSeatClassId!=null)
				hql = hql + " AND tp.vehicleSeatClassId = " + vehicleSeatClassId;
			if(vehicleSeatClassName!=null)
				hql = hql + " AND tp.vehicleSeatClassName = '" + vehicleSeatClassName + "'";
			VehicleSeatClass vehicleSeatClass = (VehicleSeatClass)swpService.getUniqueRecordByHQL(hql);
			
			
			jsonObject.add("message", "Vehicle Seat Class Found");
			jsonObject.add("vehicleSeatClassList", new Gson().toJson(vehicleSeatClass));
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
    
    
    public Response updateCustomerDetails(String verificationNumber,
			String mobileNumber, String firstName, String lastName,
			String otherName, String emailAddress, String token,
			String requestId, String ipAddress, String clientCode) {
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
						"tp.userStatus = " + UserStatus.ACTIVE.ordinal() + " AND tp.deletedAt is NULL AND tp.client.clientCode = '"+clientCode+"'");
				//roleCode = user.getRoleCode();
			}
			
			
			if(mobileNumber==null || verificationNumber==null)
			{
				jsonObject.add("status", ERROR.INCOMPLETE_PARAMETERS);
				jsonObject.add("message", "Incomplete Parameters provided in request.");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			
			if(user==null || roleCode==null || (roleCode!=null && !roleCode.equals(RoleType.CUSTOMER.name())))
			{
				jsonObject.add("status", ERROR.INSUFFICIENT_PRIVILEDGES_UPDATE_CUSTOMER);
				jsonObject.add("message", "Insufficient Priviledges To Update Customer Details");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			Customer customer = (Customer)this.swpService.getUniqueRecordByHQL("select tp from Customer tp where tp.verificationNumber = '" + verificationNumber + "' " +
					"AND tp. AND " +
					"tp.status = " + CustomerStatus.ACTIVE.ordinal() + " AND tp.deletedAt is NULL AND tp.client.clientCode = '"+clientCode+"'");
			if(customer==null)
			{
				jsonObject.add("status", ERROR.CUSTOMER_FETCH_FAILED);
				jsonObject.add("message", "Customer match failed. No Customer matching details provided");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			if(firstName!=null)
				customer.setFirstName(firstName);
			if(lastName!=null)
				customer.setLastName(lastName);
			if(emailAddress!=null)
				customer.setEmailAddress(emailAddress);
			if(mobileNumber!=null)
				customer.setMobileNumber(mobileNumber);
			if(otherName!=null)
				customer.setOtherName(otherName);
			customer.setUpdatedAt(new Date());
			
			this.swpService.updateRecord(customer);
			
			AuditTrail ad = UtilityHelper.createAuditTrailEntry(ipAddress, RequestType.UPDATE_CUSTOMER_DETAILS, requestId, this.swpService, 
					verifyJ.has("username") ? verifyJ.getString("username") : null, customer.getCustomerId(), Customer.class.getName(), 
					"Update Customer Details. Customer Name: " + firstName + " " + lastName + " | Customer Code: " + customer.getVerificationNumber(), clientCode);
			
			JSONObject js = new JSONObject();
			JSONObject txnObjects = new JSONObject();
			int a = 0;
			js.put("id", customer.getCustomerId());
			js.put("firstName", customer.getFirstName());
			js.put("lastName", customer.getLastName());
			js.put("contactEmail", customer.getEmailAddress());
			js.put("customerMobile", customer.getMobileNumber());
			js.put("createdAt", sdf1.format(customer.getCreatedAt()));
			js.put("otherName", customer.getOtherName());
			js.put("status", customer.getCustomerStatus()==null ? customer.getCustomerStatus() : customer.getCustomerStatus().ordinal());
			js.put("updatedAt", sdf1.format(customer.getUpdatedAt()));
			js.put("user", customer.getUser()==null ? customer.getUser() : customer.getUser().getUserId());
			txnObjects.put("" + a++, js);
			
			
			
			
			
			
			
			String message = "Hello,\nYour NRFA Customer account details have been upated successfully. If you did not request this account updated, kindly contact our administrators immediately";
            //UtilityHelper.sendSMS(this.swpService, message, customerMobile);
			try
			{
				new Thread(new SmsSender(this.swpService, message, mobileNumber)).start();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
				
			jsonObject.add("message", "Customer account details updated successfully");
			jsonObject.add("status", ERROR.GENERAL_SUCCESS);
			JsonObject jsonObj = jsonObject.build();
			return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			
		}catch(Exception e)
		{
			log.warn(e); log.error("exception...", e);
			e.printStackTrace();
			JsonObject jsonObj = jsonObject.build();
            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.INTERNAL_SERVER_ERROR ).entity( jsonObj.toString() ).build();
		}
	}

	public Response getScheduleStationsBetweenScheduleDates(
			String startScheduleDate, String endScheduleDate, String stationIds, String token,
			String clientCode, String requestId, String ipAddress) {
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
			
			String daSql_ = "Select ta from ScheduleStation ta";
			String daSql = "";
			
			if(stationIds!=null)
			{
				JSONArray js = new JSONArray(stationIds);
				String[] str_ = new String[js.length()];
				for(int i=0; i<js.length(); i++)
				{
					str_[i] = js.getString(i);
				}
				if(daSql.length()>0)
					daSql = daSql + " AND ";
				daSql = daSql + " (ta.station.stationId IN ("+StringUtils.join(str_, ",")+") AND ta.client.clientCode = '"+clientCode+"') ";
			}
			
			if(startScheduleDate!=null && endScheduleDate!=null)
			{
				startScheduleDate = startScheduleDate + " 00:00:00";
				endScheduleDate = endScheduleDate + " 23:59:59";
				if(daSql.length()>0)
					daSql = daSql + " AND ";
				daSql = daSql + " (ta.scheduleDay.scheduleDayName >= '" + startScheduleDate + "' AND ta.scheduleDay.scheduleDayName <= '" + endScheduleDate + "') ";
			}
			else
			{
				if(startScheduleDate!=null)
				{
					startScheduleDate = startScheduleDate + " 00:00:00";
					if(daSql.length()>0)
						daSql = daSql + " AND ";
					daSql = daSql + " (ta.scheduleDay.scheduleDayName >= '" + startScheduleDate + "') ";
				}
				if(endScheduleDate!=null)
				{
					endScheduleDate = endScheduleDate + " 23:59:59";
					if(daSql.length()>0)
						daSql = daSql + " AND ";
					daSql = daSql + " (ta.scheduleDay.scheduleDayName <= '" + endScheduleDate + "') ";
				}
			}
			
			
			daSql = daSql_ + (daSql.length()==0 ? "" : (" WHERE " + daSql));
			//log.info"3.daSql ==" + daSql);
			Collection<ScheduleStation> scheduleStations = (Collection<ScheduleStation>)swpService.getAllRecordsByHQL(daSql);
			
			
			jsonObject.add("message", "Schedule Station Listing");
			jsonObject.add("scheduleStationList", new Gson().toJson(scheduleStations));
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

	public Response createNewTripZone(String zoneName, Integer routeOrder, String stations,
			String editTripZoneCode, String clientCode, String token,
			String requestId, String ipAddress) {
		// TODO Auto-generated method stub
		JsonObjectBuilder jsonObject = Json.createObjectBuilder();
    	
		try{
			if(token==null || clientCode==null || zoneName==null || stations==null || routeOrder==null || (routeOrder!=null && routeOrder<1))
			{
				jsonObject.add("status", ERROR.INCOMPLETE_PARAMETERS);
				jsonObject.add("message", "Incomplete Parameters provided in request.");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			swpService = serviceLocator.getSwpService();
			Application app = Application.getInstance(swpService);
			
			String hql = "Select tp from Client tp where tp.clientCode = '" +clientCode+ "' AND tp.deletedAt IS NULL";
			Client client = (Client)this.swpService.getUniqueRecordByHQL(hql);
			
			if(editTripZoneCode!=null)
			{
				String hSql = "Select tp from TripZone tp where tp.zoneCode = '"+editTripZoneCode+"' AND ta.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL";
				TripZone editTripZone = (TripZone)swpService.getUniqueRecordByHQL(hSql);
				if(editTripZone==null)
				{
					jsonObject.add("status", ERROR.TRIP_ZONE_NOT_FOUND);
					jsonObject.add("message", "Trip zone matching the zone code provided does not exist");
					JsonObject jsonObj = jsonObject.build();
		            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
				}
				
				hSql = "Select tp from TripZoneStation tp where " +
						"tp.tripZone.tripZoneId = "+editTripZone.getTripZoneId()+" AND tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL";
				Collection<TripZoneStation> allTripZoneStations = (Collection<TripZoneStation>)swpService.getAllRecordsByHQL(hSql);
				editTripZone.setZoneName(zoneName);
				swpService.updateRecord(editTripZone);
				
				if(stations!=null)
				{
					JSONArray js = new JSONArray(stations);
					String[] str_ = new String[js.length()];
					for(int i=0; i<js.length(); i++)
					{
						str_[i] = "'" + js.getString(i) + "'";
					}
					
					hSql = "Select tp from TripZoneStation tp where " +
							"tp.tripZone.tripZoneId = "+editTripZone.getTripZoneId()+" AND tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL";
					List<TripZoneStation> allTripZoneStationCodes= (List<TripZoneStation>)swpService.getAllRecordsByHQL(hSql);
					Iterator<TripZoneStation> it = allTripZoneStationCodes.iterator();
					List<String> str_1 = new ArrayList<String>();
					while(it.hasNext())
					{ 
						TripZoneStation tzs = it.next();
						if(!Arrays.asList(str_).contains(tzs.getStation().getStationCode()))
						{
							tzs.setDeletedAt(new Date());
							swpService.updateRecord(tzs);
						}
						else
						{
							str_1.add(tzs.getStation().getStationCode());
						}
					}
					
					
					for(int i=0; i < str_.length; i++)
					{
						if(!str_1.contains(str_[i]))
						{
							hql = "Select tp from Station where tp.stationCode = '"+str_[i]+"' AND tp.deletedAt IS NULL AND tp.clientCode = '"+clientCode+"'";
							Station station = (Station)swpService.getUniqueRecordByHQL(hql);
							TripZoneStation tzs = new TripZoneStation();
							tzs.setClient(client);
							tzs.setCreatedAt(new Date());
							tzs.setUpdatedAt(new Date());
							tzs.setTripZone(editTripZone);
							tzs.setStation(station);
							tzs = (TripZoneStation)swpService.createNewRecord(tzs);
						}
					}
				}
				jsonObject.add("message", "New Trip Zone updated successfully");
				jsonObject.add("scheduleStationList", new Gson().toJson(editTripZone));
				jsonObject.add("status", ERROR.GENERAL_SUCCESS);
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			else
			{
				JSONArray js = new JSONArray(stations);
				String[] str_ = new String[js.length()];
				for(int i=0; i<js.length(); i++)
				{
					str_[i] = "'" + js.getString(i) + "'";
				}
				String hSql = "Select tp from Station tp where tp.stationCode IN ("+StringUtils.join(str_, ",")+") " +
						"AND tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL";
				Collection<Station> allStations = (Collection<Station>)swpService.getAllRecordsByHQL(hSql);
				
				if(allStations.size()==0)
				{
					jsonObject.add("message", "Invalid stations selected");
					jsonObject.add("status", ERROR.INVALID_STATIONS_SELECTED);
					JsonObject jsonObj = jsonObject.build();
		            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
				}
				
				TripZone tz = new TripZone();
				tz.setClient(client);
				tz.setCreatedAt(new Date());
				tz.setUpdatedAt(new Date());
				tz.setZoneCode(RandomStringUtils.randomNumeric(8));
				tz.setZoneName(zoneName);
				tz.setRouteOrder(routeOrder);
				tz = (TripZone)swpService.createNewRecord(tz);
				
				Iterator<Station> it = allStations.iterator();
				while(it.hasNext())
				{
					Station station = it.next();
					TripZoneStation tzs = new TripZoneStation();
					tzs.setClient(client);
					tzs.setCreatedAt(new Date());
					tzs.setUpdatedAt(new Date());
					tzs.setStation(station);
					tzs.setTripZone(tz);
					tzs = (TripZoneStation)swpService.createNewRecord(tzs);
				}
				jsonObject.add("message", "New Trip Zone created successfully");
				jsonObject.add("scheduleStationList", new Gson().toJson(tz));
				jsonObject.add("status", ERROR.GENERAL_SUCCESS);
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

	public Response getListTripZones(String token, String clientCode,
			String requestId, String ipAddress, String zoneCode)
	{
		// TODO Auto-generated method stub
    	JsonObjectBuilder jsonObject = Json.createObjectBuilder();
    	
		try{
			if(/*token==null || */clientCode==null)
			{
				jsonObject.add("status", ERROR.INCOMPLETE_PARAMETERS);
				jsonObject.add("message", "Incomplete Parameters provided in request.");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			swpService = serviceLocator.getSwpService();
			Application app = Application.getInstance(swpService);
			
			String hql = "Select tp from TripZone tp where tp.client.clientCode = '" + clientCode + "' AND tp.deletedAt IS NULL";
			if(zoneCode!=null)
			{
				hql = hql + " AND tp.zoneCode = '"+zoneCode+"'";
			}
			Collection<TripZone> tripZones = (Collection<TripZone>)swpService.getAllRecordsByHQL(hql);
			
			hql = "Select tp from TripZoneStation tp where tp.client.clientCode = '" + clientCode + "' AND tp.deletedAt IS NULL";
			if(zoneCode!=null)
			{
				hql = hql + " AND tp.tripZone.zoneCode = '"+zoneCode+"'";
			}
			Collection<TripZoneStation> tripZoneStations = (Collection<TripZoneStation>)swpService.getAllRecordsByHQL(hql);
			
			
			
			jsonObject.add("message", "Trip Zone Listing");
			jsonObject.add("tripZoneList", new Gson().toJson(tripZones));
			jsonObject.add("tripZoneStationsList", new Gson().toJson(tripZoneStations));
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

	public Response createNewCoupons(Integer quantity, String expiryDate, Double discountRate, 
			String clientCode, String token, String requestId, String ipAddress) {
		// TODO Auto-generated method stub
		JsonObjectBuilder jsonObject = Json.createObjectBuilder();
    	
    	
		try{
			if(discountRate==null || expiryDate==null || clientCode==null || token==null)
			{
				//log.inforequestId + "Test 2");
				jsonObject.add("status", ERROR.INCOMPLETE_PARAMETERS);
				jsonObject.add("message", "Incomplete Parameters provided in request.");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			swpService = serviceLocator.getSwpService();
			Application app = Application.getInstance(swpService);
			JSONObject verifyJ = UtilityHelper.verifyToken(token, app);
			//log.info"1---verifyJ ==" + verifyJ.length() + " && v= " + verifyJ.toString());
			if(verifyJ.length()==0 || (verifyJ.length()>0 && verifyJ.has("active") && verifyJ.getInt("active")==0))
			{
				//log.info"2---verifyJ ==" + verifyJ.toString());
				jsonObject.add("status", ERROR.FORCE_LOGOUT_USER);
				jsonObject.add("message", "Your session has expired. Please log in again");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			//log.inforequestId + "verifyJ ==" + verifyJ.toString());
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
			
			if(roleCode==null || (roleCode!=null && !roleCode.equals(RoleType.ADMIN_STAFF)))
			{
				jsonObject.add("status", ERROR.INSUFFICIENT_PRIVILEDGES);
				jsonObject.add("message", "Invalid Action. Insufficient Priviledges For This Action");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			String hql = "Select tp from Client tp where tp.clientCode = '" +clientCode+ "' AND tp.deletedAt IS NULL";
			Client client = (Client)this.swpService.getUniqueRecordByHQL(hql);
			
			expiryDate = expiryDate + " " + "23:59";
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			Date expiryDateFormatted = sdf.parse(expiryDate);

			String batchNumber = RandomStringUtils.randomNumeric(8);
			List couponList = new ArrayList();
			for(int i=0; i<quantity; i++)
			{
				String couponCode = RandomStringUtils.randomAlphanumeric(12).toUpperCase();
				Coupon coupon = new Coupon();
				coupon.setCreatedAt(new Date());
				coupon.setUpdatedAt(new Date());
				coupon.setExpiryDate(expiryDateFormatted);
				coupon.setCouponCode(couponCode);
				coupon.setBatchNumber(batchNumber);
				coupon.setCouponStatus(CouponStatus.AVAILABLE);
				coupon.setDiscountRate(discountRate);
				coupon.setClient(client);
				coupon = (Coupon)this.swpService.createNewRecord(coupon);
				couponList.add(coupon);
				AuditTrail ad = UtilityHelper.createAuditTrailEntry(ipAddress, RequestType.NEW_COUPON_CREATION, requestId, this.swpService, 
						verifyJ.has("username") ? verifyJ.getString("username") : null, coupon.getCouponId(), Coupon.class.getName(),
						"New Coupon. Coupon Batch Number: " + batchNumber + " | Coupon: " + couponCode.substring(0, 2) + 
						"***" + couponCode.substring(couponCode.length()-2), clientCode);
			}
			
			
			
			if(couponList!=null && couponList.size()>0)
			{
				
				jsonObject.add("message", "New coupons created successfully");
				jsonObject.add("couponList", new Gson().toJson(couponList));
				jsonObject.add("couponBatchId", batchNumber);
				jsonObject.add("status", ERROR.GENERAL_SUCCESS);
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			else
			{
				jsonObject.add("message", "New coupon creation failed");
				jsonObject.add("status", ERROR.PUBLIC_COUPON_CREATE_FAIL);
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			
			
		}catch(Exception e)
		{
			//log.info"Error = " + e.getMessage());
			log.warn(e); log.error("exception...", e);
			e.printStackTrace();
			JsonObject jsonObj = jsonObject.build();
            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.INTERNAL_SERVER_ERROR ).entity( jsonObj.toString() ).build();
		}
	}
	
	
	
	public Response getCouponDetails(String couponCode, String token, String requestId, String ipAddress, String clientCode) {
		// TODO Auto-generated method stub
		JsonObjectBuilder jsonObject = Json.createObjectBuilder();
    	
		try{
			
			if(couponCode==null || clientCode==null)
			{
				jsonObject.add("status", ERROR.INCOMPLETE_PARAMETERS);
				jsonObject.add("message", "Incomplete Parameters provided in request.");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			//log.info"couponCode = " + couponCode);
			
			
			
			swpService = serviceLocator.getSwpService();
			Application app = Application.getInstance(swpService);
			
			Coupon coupon = null;
			String hql = "Select tp from Coupon tp where tp.couponCode = '"+couponCode+"' AND tp.client.clientCode = '"+clientCode+"' " +
					"AND tp.couponStatus = " + CouponStatus.AVAILABLE.ordinal() + 
					"AND tp.deletedAt IS NULL";
			coupon = (Coupon)swpService.getUniqueRecordByHQL(hql);
			
			if(coupon==null)
			{
				jsonObject.add("status", ERROR.GENERAL_FAIL);
				jsonObject.add("message", "Coupon not found");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
				
			jsonObject.add("status", ERROR.GENERAL_SUCCESS);
			jsonObject.add("message", "Coupon found");
			jsonObject.add("discountPercentRate", coupon.getDiscountRate());
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

	public Response createNewTarget(String lineCode, String targetMonth, Integer targetYear,
			Double ticketRevenue, Double courierRevenue, String clientCode,
			String token, String requestId, String ipAddress) {
		// TODO Auto-generated method stub
		JsonObjectBuilder jsonObject = Json.createObjectBuilder();
    	
    	
		try{
			if(lineCode==null || targetMonth==null || token==null || clientCode==null || targetYear==null || ticketRevenue==null || courierRevenue==null)
			{
				//log.inforequestId + "Test 2");
				jsonObject.add("status", ERROR.INCOMPLETE_PARAMETERS);
				jsonObject.add("message", "Incomplete Parameters provided in request.");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			TargetMonth tm = null;
			try
			{
				tm = TargetMonth.valueOf(targetMonth);
			}
			catch(IllegalArgumentException | NullPointerException e)
			{
				//log.inforequestId + "Test 2");
				jsonObject.add("status", ERROR.INCOMPLETE_PARAMETERS);
				jsonObject.add("message", "Month provided is invalid. Valid months are JANUARY, FEBRUARY, MARCH...etc");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
						
			
			swpService = serviceLocator.getSwpService();
			Application app = Application.getInstance(swpService);
			JSONObject verifyJ = UtilityHelper.verifyToken(token, app);
			//log.info"1---verifyJ ==" + verifyJ.length() + " && v= " + verifyJ.toString());
			if(verifyJ.length()==0 || (verifyJ.length()>0 && verifyJ.has("active") && verifyJ.getInt("active")==0))
			{
				//log.info"2---verifyJ ==" + verifyJ.toString());
				jsonObject.add("status", ERROR.FORCE_LOGOUT_USER);
				jsonObject.add("message", "Your session has expired. Please log in again");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			//log.inforequestId + "verifyJ ==" + verifyJ.toString());
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
				String hql = "select tp from User tp where tp.client.clientCode = '" + clientCode + "' AND tp.username = '" + username + "' AND " +
						"tp.userStatus = " + UserStatus.ACTIVE.ordinal() + " AND tp.deletedAt IS NULL";
				//log.inforequestId + "hql ==" + hql);
				user = (User)this.swpService.getUniqueRecordByHQL(hql);
				
				
				if(user==null)
				{

					//log.inforequestId + "user IS NULL");
					//log.inforequestId + "user firstname = " + user.getFirstName());
					//log.inforequestId + "user lastname = " + user.getLastName());
					jsonObject.add("status", ERROR.INVALID_VEHICLE_SCHEDULE_CREATION_PRIVILEDGES);
					jsonObject.add("message", "Invalid Vehicle Schedule Creation Priviledges");
					JsonObject jsonObj = jsonObject.build();
		            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
				}
				//roleCode = user.getRoleCode();
			}
			
			if(roleCode==null || (roleCode!=null && !roleCode.equals(RoleType.ADMIN_STAFF)))
			{
				jsonObject.add("status", ERROR.INVALID_VEHICLE_SCHEDULE_CREATION_PRIVILEDGES);
				jsonObject.add("message", "Invalid Vehicle Schedule Creation Priviledges");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			
			String hql = "Select tp from Client tp where tp.clientCode = '" +clientCode+ "' AND tp.deletedAt IS NULL";
			Client client = (Client)this.swpService.getUniqueRecordByHQL(hql);
			
			hql = "Select tp from Line tp where tp.lineCode = '" +lineCode+ "' AND tp.deletedAt IS NULL";
			Line line = (Line)this.swpService.getUniqueRecordByHQL(hql);
			if(line==null)
			{
				//bank already exists
				jsonObject.add("message", "Trip line does not exist");
				jsonObject.add("status", ERROR.TRIP_LINE_DOES_NOT_EXIST);
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			
			hql = "Select tp from Target tp where tp.targetMonth = " + TargetMonth.valueOf(targetMonth).ordinal() + 
					" AND tp.targetYear = " + targetYear + 
					"AND tp.client.clientCode = '"+clientCode+"' AND tp.deletedAt IS NULL";
			//log.info"hql ==" + hql);
			Target target = (Target)swpService.getUniqueRecordByHQL(hql);
			
			if(target!=null)
			{
				//bank already exists
				jsonObject.add("message", "Target Creation Failed. A target matching the provided month and year already exists");
				jsonObject.add("status", ERROR.TARGET_ALREADY_EXISTS);
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			
			
			target = new Target();
			target.setCreatedAt(new Date());
			target.setUpdatedAt(new Date());
			target.setCourierRevenue(courierRevenue);
			target.setTargetMonth(tm);
			target.setTargetYear(targetYear);
			target.setTicketRevenue(ticketRevenue);
			target.setClient(client);
			target.setLine(line);
			target = (Target)this.swpService.createNewRecord(target);
			
			AuditTrail ad = UtilityHelper.createAuditTrailEntry(ipAddress, RequestType.NEW_TARGET, requestId, this.swpService, 
					verifyJ.has("username") ? verifyJ.getString("username") : null, target.getTargetId(), Target.class.getName(), 
					"New Target For Month: " + targetMonth + " | Target Year: " + targetYear + " | Passenger Revenue: " + 
					ticketRevenue + " | Courier Revenue: " + courierRevenue, clientCode);
			
			if(target!=null)
			{
				
				jsonObject.add("message", "New Target Created successfully");
				jsonObject.add("target", new Gson().toJson(target));
				jsonObject.add("status", ERROR.GENERAL_SUCCESS);
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			else
			{
				jsonObject.add("message", "New Target Creation Failed");
				jsonObject.add("status", ERROR.TARGET_CREATE_FAIL);
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			
			
		}catch(Exception e)
		{
			//log.info"Error = " + e.getMessage());
			log.warn(e); log.error("exception...", e);
			e.printStackTrace();
			JsonObject jsonObj = jsonObject.build();
            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.INTERNAL_SERVER_ERROR ).entity( jsonObj.toString() ).build();
		}
	}

	public Response listTargets(String token, String clientCode,
			String requestId, String ipAddress) {
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
			
			String hql = "Select tp from Target tp where tp.deletedAt IS NULL AND tp.client.clientCode = '"+clientCode+"'";
			//log.info"3.hql ==" + hql);
			Collection<VehicleSchedule> vehicleSchedules = (Collection<VehicleSchedule>)swpService.getAllRecordsByHQL(hql);
			
			jsonObject.add("message", "Target Listing");
			jsonObject.add("targetList", new Gson().toJson(vehicleSchedules));
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

	public Response getTargetRevenueByFilter(String targetMonth, Integer targetYear,
			Long targetId, String token, String clientCode, String requestId,
			String ipAddress) {
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
			
			TargetMonth tm = null;
			try
			{
				tm = TargetMonth.valueOf(targetMonth);
				
			}
			catch(IllegalArgumentException | NullPointerException e)
			{
				
			}
			
			String hql = "Select tp from Target tp where tp.deletedAt IS NULL";
			if(targetId!=null)
			{
				hql = hql + " AND tp.targetId = "+targetId;
			}
			if(targetMonth!=null)
			{
				hql = hql + " AND tp.targetMonth = "+tm.ordinal();
			}
			if(targetYear!=null)
			{
				hql = hql + " AND tp.targetYear = " + targetYear;
			}
			//log.info"3.hql ==" + hql);
			Collection<Target> targets = (Collection<Target>)swpService.getAllRecordsByHQL(hql);
			
			
			
			if(targets!=null)
			{
				jsonObject.add("message", "Targets Found");
				jsonObject.add("targets", new Gson().toJson(targets));
				jsonObject.add("status", ERROR.GENERAL_SUCCESS);
			}
			else
			{
				jsonObject.add("message", "Targets Not Found");
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
	
	
	


	public Response summarizeAndCleanUp(String clientCode, String requestId,
			String ipAddress) {
		// TODO Auto-generated method stub
		JsonObjectBuilder jsonObject = Json.createObjectBuilder();
    	//log.info"clientCode..." + clientCode);
		try{
			if(clientCode==null)
			{
				jsonObject.add("status", ERROR.INCOMPLETE_PARAMETERS);
				jsonObject.add("message", "Incomplete Parameters provided in request.");
				JsonObject jsonObj = jsonObject.build();
	            return UtilityHelper.getNoCacheResponseBuilder( Response.Status.OK ).entity( jsonObj.toString() ).build();
			}
			
			swpService = serviceLocator.getSwpService();
			Session session = swpService.getSession();
			Application app = Application.getInstance(swpService);
			
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
			
			
			String hql = "Select ta from VehicleSchedule ta WHERE " +
					"ADDTIME(ta.scheduleStationCode.departureDate, " + client.getSummarizeAfterDays()*24*60*60 + ") < CURRENT_TIMESTAMP " +
					"AND ta.deletedAt IS NULL";
			Collection<VehicleSchedule> vsList = (Collection<VehicleSchedule>)swpService.getAllRecordsByHQL(hql);
			//log.info"vsList size == " + vsList.size());
			Iterator<VehicleSchedule> itVS = vsList.iterator();
			while(itVS.hasNext())
			{
				VehicleSchedule vs = itVS.next();
				hql = "Select tp from UnpurchasedSeatSummary tp where tp.vehicleSchedule.vehicleScheduleId = " + vs.getVehicleScheduleId() + 
						" AND tp.client.clientId = " + client.getClientId() + " AND tp.deletedAt IS NULL";
				//log.info"hql>>>" + hql);
				UnpurchasedSeatSummary ussCheck = (UnpurchasedSeatSummary)swpService.getUniqueRecordByHQL(hql);
				if(ussCheck==null)
				{
					hql = "Select ta.schedSeatAvailId from ScheduleStationSeatAvailability ta WHERE " +
							" ta.scheduleStationSeatSection.originScheduleStation.scheduleStationCode.scheduleStationId = " + vs.getScheduleStationCode().getScheduleStationId() +
							" AND ta.deletedAt IS NULL AND ta.boughtByCustomer IS NULL";
					//log.info"hql...." + hql);
					List<Long> sssaList = (List<Long>)swpService.getAllRecordsByHQL(hql);
					if(sssaList!=null)
					{
						int unBoughtSeats = sssaList.size();
						UnpurchasedSeatSummary uss = new UnpurchasedSeatSummary();
						uss.setClient(client);
						uss.setCreatedAt(new Date());
						uss.setUpdatedAt(new Date());
						uss.setSeatCount(unBoughtSeats);
						uss.setVehicleSchedule(vs);
						swpService.createNewRecord(uss);
						Query q = session.createQuery("delete ScheduleStationSeatAvailability where id IN ("+StringUtils.join(sssaList, ", ")+")");
						q.executeUpdate();
						//log.info"vehicleSchedule...." + vs.getVehicleScheduleId());
						//log.info"seats not purchased...." + unBoughtSeats);
					}
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
	    
	    


    
	
}