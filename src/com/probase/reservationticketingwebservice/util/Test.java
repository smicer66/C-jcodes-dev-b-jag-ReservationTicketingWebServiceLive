package com.probase.reservationticketingwebservice.util;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.Key;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.ws.rs.FormParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.xml.namespace.QName;


import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.NodeList;
/*import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;*/
/*import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;*/

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.probase.reservationticketingwebservice.authenticator.Authenticator;
import com.probase.reservationticketingwebservice.authenticator.CardFunction;
import com.probase.reservationticketingwebservice.authenticator.ClientFunction;
import com.probase.reservationticketingwebservice.authenticator.CourierFunction;
import com.probase.reservationticketingwebservice.authenticator.PaymentFunction;
import com.probase.reservationticketingwebservice.authenticator.StationFunction;
import com.probase.reservationticketingwebservice.authenticator.UtilityFunction;
import com.probase.reservationticketingwebservice.authenticator.VehicleFunction;
import com.probase.reservationticketingwebservice.enumerations.CardStatus;
import com.probase.reservationticketingwebservice.enumerations.CardType;
import com.probase.reservationticketingwebservice.enumerations.Channel;
import com.probase.reservationticketingwebservice.enumerations.CustomerType;
import com.probase.reservationticketingwebservice.enumerations.DeviceType;
import com.probase.reservationticketingwebservice.enumerations.PassengerType;
import com.probase.reservationticketingwebservice.enumerations.PaymentMeans;
import com.probase.reservationticketingwebservice.enumerations.PurchasePoint;
import com.probase.reservationticketingwebservice.enumerations.SeatAvailabilityStatus;
import com.probase.reservationticketingwebservice.enumerations.TransactionCurrency;
import com.probase.reservationticketingwebservice.enumerations.RoleType;
import com.probase.reservationticketingwebservice.enumerations.ServiceType;
import com.probase.reservationticketingwebservice.enumerations.UserStatus;
import com.probase.reservationticketingwebservice.models.CardScheme;
import com.probase.reservationticketingwebservice.models.District;
import com.probase.reservationticketingwebservice.models.ScheduleStation;
import com.probase.reservationticketingwebservice.models.TripCard;
import com.probase.reservationticketingwebservice.models.User;
import com.probase.reservationticketingwebservice.models.VehicleSeat;
import com.probase.reservationticketingwebservice.models.VehicleSeatAvailability;
import com.probase.reservationticketingwebservice.models.VehicleTripRouting;
import com.probase.reservationticketingwebservice.services.CardServices;
import com.probase.reservationticketingwebservice.services.UtilityServices;
import com.probase.reservationticketingwebservice.util.Application;
import com.probase.reservationticketingwebservice.util.PrbCustomService;
import com.probase.reservationticketingwebservice.util.ServiceLocator;
import com.probase.reservationticketingwebservice.util.SwpService;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;
import javax.xml.ws.handler.MessageContext;


public class Test {
	private static final String WS_URL = "http://localhost:9999/ws/hello?wsdl";

	/**
	 * @param args
	 */
	private static Logger log = Logger.getLogger(com.probase.reservationticketingwebservice.util.Test.class);
	private static ServiceLocator serviceLocator = ServiceLocator.getInstance();
	public static SwpService swpService = null;
	//public static PrbCustomService swpCustomService = PrbCustomService.getInstance();
	/**/
	private final static String USER_AGENT = "Mozilla/5.0";
	
	public static void main4(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String password = "password";
		String clientCode = "5539619948";
		swpService = serviceLocator.getSwpService();
		Application application = Application.getInstance(swpService);
		Authenticator at = Authenticator.getInstance();
		String adminPassword = "AL54KWXZ";
		
		String adminPasswordEnc = UtilityHelper.encryptData(adminPassword, application.getPublicKey(clientCode));
		//String jsonObjectStr1 = (String)(at.loginOld("zrlsuperadmin@zrl.com", "AL54KWXZ", "5539619948", "127.0.0.1", RandomStringUtils.randomAlphanumeric(8)).getEntity());
		String jsonObjectStr1 = (String)(at.login("zrlsuperadmin@zrl.com", adminPasswordEnc, "5539619948", "127.0.0.1", RandomStringUtils.randomAlphanumeric(8), RoleType.SUPER_ADMIN.name()).getEntity());
		System.out.println(jsonObjectStr1);
		JSONObject userData1 = new JSONObject(jsonObjectStr1);
		String token = userData1.getString("token");
		System.out.println("token..." + token);/**/
		
		
		/*Response userResponse = at.addNewUser(null, token, "test@zrl.com", "test@zrl.com", "+260967307789", UserStatus.ACTIVE.name(), 
				null, Boolean.FALSE, null,"5539619948", "John", "Doe", "Samson", RoleType.OPERATOR.name(), "83107", RandomStringUtils.randomAlphanumeric(8), "127.0.0.1");
		System.out.println("rs..." + userResponse.getEntity().toString());*/
		//Password - mAGpYqEx
		
		//String pwd = "eyJpdiI6IlwvSktaOWY2ZHNBd0RTVzIwbHYyQnR3PT0iLCJ2YWx1ZSI6InUyK09TNXFEc2xGQ3hpVXA1Q0x3N1E9PSIsIm1hYyI6IjE4YmRhMzg5NjY2YTQ3MTVhN2E5NjQ5OWY1MGUwZTUxNGI4NjI0MGY0NDY3NmU3ZWQxOGJiYmE1NTAxNTA0YjAifQ==";
		/*String pwd = "eyJpdiI6IlR6Vm1aSFJrUkdseGNtMTZhbTB6Ymc9PSIsInZhbHVlIjoiZXRyaHFwbEd5KzN3VnpJeW1NblRGQT09IiwibWFjIjoi77+977+9XHUwMDFkXHUwMDE3W++/ve+/vSfvv73vv73vv70gXHUwMDBlTDJ1LeiTu8+R77+9L++/ve+/vVxuXHUwMDFl77+9LmdcXCJ9";
		String jsonObjectStr1 = (String)(at.login("zrladmin@zrl.com", pwd, "5539619948", "127.0.0.1", RandomStringUtils.randomAlphanumeric(8), RoleType.ADMIN_STAFF.name()).getEntity());
		//String jsonObjectStr1 = (String)(at.login("test@zrl.com", pwd, "5539619948", "127.0.0.1", RandomStringUtils.randomAlphanumeric(8), RoleType.VENDOR.name()).getEntity());
		System.out.println(jsonObjectStr1);
		JSONObject userData1 = new JSONObject(jsonObjectStr1);
		String token = userData1.getString("token");
		System.out.println("token..." + token);*/
	}
	
	public static void main(String[] args) throws Exception {
		swpService = serviceLocator.getSwpService();
		Authenticator at = Authenticator.getInstance();
		Application application = Application.getInstance(swpService);
		String password = "mAGpYqEx";
		//password = "pass123";
		String clientCode = "96";
		String key = application.getPublicKey(clientCode);
		System.out.println(">>>" + key);
		String epassword = UtilityHelper.encryptData(password, key);
		System.out.println(">>>" + epassword);
		String bpassword = UtilityHelper.bcryptData(epassword);
		System.out.println(">>>" + bpassword);
		//epassword = "eyJpdiI6Ik5HSk9iR3h6U2paeVEyUjJWRk5PV0E9PSIsInZhbHVlIjoidEJrSFh4SjRDRzhqeHdmaEc2a3RoQT09IiwibWFjIjoiPz9WP3M/Pz8pPz8ofz8/P1x0Pyx3P1RcIls/Sj+oXCJBPyJ9";
		//boolean chk = UtilityHelper.bcryptCheckData(bpassword, epassword);
		//String jsonObjectStr1 = (String)(at.login("zrladmin@zrl.com", epassword, "5539619948", "127.0.0.1", RandomStringUtils.randomAlphanumeric(8), RoleType.ADMIN_STAFF.name()).getEntity());
		//String jsonObjectStr1 = (String)(at.login("test@zrl.com", pwd, "5539619948", "127.0.0.1", RandomStringUtils.randomAlphanumeric(8), RoleType.VENDOR.name()).getEntity());
		String jsonObjectStr1 = (String)(at.login("zrladmin@zrl.com", epassword, clientCode, "127.0.0.1", RandomStringUtils.randomAlphanumeric(8), RoleType.ADMIN_STAFF.name()).getEntity());
		System.out.println(jsonObjectStr1);
		JSONObject userData1 = new JSONObject(jsonObjectStr1);
		String token = userData1.getString("token");
		System.out.println(">>>token == " + token);
		VehicleFunction vf = VehicleFunction.getInstance();
		CourierFunction cf = CourierFunction.getInstance();
		//System.out.println(vf.getCustomerPurchasedTripsByCustomerName("4059730729", token, "Kachi", "Akujua", 434344+"", "", clientCode, "2019-01-01", "2020-02-20").getEntity().toString());
		//System.out.println(vf.getCustomerPurchasedTripsByCustomerMobileNumber("4059730729", token, "260967307151", 434344+"", "", clientCode, "2019-01-01", "2020-02-20").getEntity().toString());
		//Response res = vf.upgradePurchasedTicket(null, "004", PaymentMeans.CASH.name(), PurchasePoint.WEB.name(), "000000128", PassengerType.ADULT_PASSENGER.name(), 
		//		"86864506", RandomStringUtils.randomAlphanumeric(8), "127.0.0.1", clientCode, token, "John", "Doe", "260967307151", "21111", "smicer99@gmail.com"); 
		//System.out.println(res.getEntity().toString());
		UtilityFunction uf = UtilityFunction.getInstance();
		//Response res = uf.summarizeAndCleanUp(clientCode, RandomStringUtils.randomAlphanumeric(8), "127.0.0.1");
		//Response res = uf.listVehicleSchedules("601", "6", null, null, token, clientCode, RandomStringUtils.randomAlphanumeric(8), "127.0.0.1");
		Response res = cf.listShipments(token, "343452352323", clientCode, null, null, null, "62478", "601", null, "2020-02-28");
		System.out.println(res.getEntity().toString());
		
		
		/*Response userResponse = at.addNewUser(null, token, "123456", "123456@zrl.com", "+260976642379", UserStatus.ACTIVE.name(), 
				null, Boolean.FALSE, null,"5539619948", "Chembe", "Chembe", "Chembe", RoleType.OPERATOR.name(), "83107", RandomStringUtils.randomAlphanumeric(8), "127.0.0.1");
		System.out.println("rs..." + userResponse.getEntity().toString());*/
		
		//System.out.println(chk);
		/*Select tp from Shipment tp where tp.deletedAt IS NULL AND tp.vehicleSchedule.departureScheduleStation.scheduleStationCode.departureDate >= '2020-02-14 00:00:00' 
				AND tp.vehicleSchedule.departureScheduleStation.scheduleStationCode.departureDate <= '2020-02-14 23:59:59'  AND 
				tp.vehicleSchedule.scheduleStationCode.scheduleStationCode = '27219' AND tp.vehicleSchedule.vehicle.vehicleCode = '601' AND 
				tp.arrivalScheduleStation.station.stationCode = '62478' ORDER BY tp.id DESC*/
	}
	
	
	public static void main1(String[] args) throws Exception {
		swpService = serviceLocator.getSwpService();
		Application application  = Application.getInstance(swpService);
		/*
		 * >>>TMcMqZa2a8qfrLzB
>>>eyJpdiI6IlpVdzNhbXhwY2tvd05GRjRSWEUzUXc9PSIsInZhbHVlIjoiamxpWWlLUUlldWcvRVdmMDNyclNJUT09IiwibWFjIjoiPz8kPzxcdGhdPz8/PXdRP1x1MDAxZT8/KD9oP3JiP3gmKnU0yCJ9
>>>$2a$10$zfibpCkeuQpGrB0x3ah.2.XC7IKz8.UWJnC1jgIC803uXl4HWUjZC
true
		 * */
		/*String encryptedStr = "eyJpdiI6IjZYakNKT2RsRUtodTJyU3IwRldkaFE9PSI" + 
				"sInZhbHVlIjoiXC9cL3dacXl3TmV1M3hoNUhcL1FYSU9WUT09IiwibWFjIj" +
				"oiMjljODRlYmFjY2E3MGM1Yzc4MGI4MmM2MWZjNDhiOGRkYTZhODVmN2Y5M" + 
				"DliNDliZDhkODhkNmFiMDk1MTBlOCJ9";
		encryptedStr = "eyJpdiI6Im4rREhONzhFUTcycXV6cFwvYU5cL0pyZz09IiwidmFsd" +
				"WUiOiJuMHFCU1pGQUdpNlZwMkNDVGJweVJnPT0iLCJtYWMiOiJmNzJlYjAzM" +
				"DcxYzdlNDU1NTJmYjc3MDM2OWJhNWI2NGZiYTczMjkwMzEyYTQ1ZWExY2QzN" +
				"TE5YmViZmRhOTIzIn0=";*/
		/*encryptedStr = "eyJpdiI6InZoVjF1RUJsUE9wVWxCSGdpV1wvdzdRPT0iLCJ2YWx1Z" +
				"SI6InZRcWIyNm4wQVpVUE5VT3JIbHJ1NEE9PSIsIm1hYyI6IjhjOTU2YWVmO" +
				"DYzMTM1NjMwNDE4ZTExZTRhMTAwYWEyMzNhMzM2YTczODQ4Mzc0MzE5ODViZ" +
				"DZiNzZlNjE0YzYifQ==";*/
		/*encryptedStr = "eyJpdiI6IlNzXC8rdDBibG9vTDVFVUhMTCtud0pnPT0iLCJ2YWx1ZSI6InN2ZmJ5VjdnaHZFYTdFdDhtK25LSDRWWHZLUzl2Rlh4eWM3eVVJRVlkakt0Slh2MTNtMG85bnRFZ0dXSXRQRXAiLCJtYWMiOiI1N2VjMGViMGIyMDFjZGZkOWEyZGYxMDYxZTFhYjUyNjQ1MTZlZTcxNWNlZmJmMTE0YTU0MmYwODhlZWY2YzcxIn0=";//strval
		String bankKey = "WMXGGHowzFdq0fpTg93pYmA5Wjuiq97l";
		Object pws = UtilityHelper.decryptData(encryptedStr, bankKey);
		System.out.println(">>>" + (((Long)pws)*2));*/
		/*System.out.println(CardStatus.ACTIVATED_LEVEL_3.ordinal());
		Double amount = 1700.00;
		String terminalId = "81420";
		String serviceType ="CUSTOMER_LOAD_CARD_VIA_DISTRIBUTOR_ONUS";
		String orderId = "nQ3ekLzT";
		String api_key = "iZfv7kd3vdtThuc9LwbPXTOpzNsXIAXK";
		String hash = "d725800ac8130d4250acf04e0f107fa00ac55bee460b8dc66a1af3e0117018a3caf6a86dd09522e6393673d1ee62cdc5b80e3fa375bfd1156e62cd7ed906b044";
		
		*/
		try{
			
			/*"{\"ADULT\":[{\"seatClass\":\"58120015\",\"seatLocation\":\"WINDOW\"}],\"CHILD\":[],\"SENIOR\":[],\"DISABLED\":[]}";
			Response userResponse = vf.searchAvailableTrips("4059730729", null, "32287", "11684", pass, "2019-09-08 01:00", "2019-09-12", 72, "58120015", 
					true, requestId, ipAddress, "5539619948");*/
			


			JSONObject authHeader = new JSONObject();
			String qry = "";
			qry = qry + "departureStationCode=" + 88046;
			qry = qry + "&arrivalStationCode=" + 83107;
			qry = qry + "&departureTime=" + "2020-03-07 01:00";
			qry = qry + "&hoursAdd=" + 72;
			qry = qry + "&tripClass=" + 48019972;
			qry = qry + "&clientCode=5539619948";
			qry = qry + "&deviceCode=4059730729";
			qry = qry + "&passengerDetails=" + "{\"ADULT\":[{\"seatClass\":\"58120015\",\"seatLocation\":\"WINDOW\"}],\"CHILD\":[],\"SENIOR\":[],\"DISABLED\":[]}";
			System.out.println("qry --- " + qry);
			qry = "";
			String jsonObjectStr = sendPost("http://108.61.189.36:8080/ReservationTicketingWebService/NCE/services/VehicleServices/searchAvailableTrips", qry, authHeader);
			System.out.println("1.1 jsonObjectStr = " + jsonObjectStr);
			//String jsonObjectStr = "{\"500065856135\":{\"73602\":[{\"sectionName\":\"Premium Economy\",\"originStation\":\"CHIKONKOMENE\",\"seatClassName\":\"Premium Economy\",\"passengerType\":\"ADULT_PASSENGER\",\"originStationCode\":\"73602\",\"sectionCode\":\"JQVXLURB\",\"seatLocation\":\"MIDDLE\",\"seatNumber\":\"2C\",\"seatAvailabilityId\":80460}],\"87073\":[{\"sectionName\":\"Premium Economy\",\"originStation\":\"FILATO\",\"seatClassName\":\"Premium Economy\",\"passengerType\":\"ADULT_PASSENGER\",\"originStationCode\":\"87073\",\"sectionCode\":\"JQVXLURB\",\"seatLocation\":\"MIDDLE\",\"seatNumber\":\"2C\",\"seatAvailabilityId\":80540}],\"21244\":[{\"sectionName\":\"Premium Economy\",\"originStation\":\"CHIBWE\",\"seatClassName\":\"Premium Economy\",\"passengerType\":\"ADULT_PASSENGER\",\"originStationCode\":\"21244\",\"sectionCode\":\"JQVXLURB\",\"seatLocation\":\"MIDDLE\",\"seatNumber\":\"2C\",\"seatAvailabilityId\":80420}],\"11480\":[{\"sectionName\":\"Premium Economy\",\"originStation\":\"BOWWOOD\",\"seatClassName\":\"Premium Economy\",\"passengerType\":\"ADULT_PASSENGER\",\"originStationCode\":\"11480\",\"sectionCode\":\"JQVXLURB\",\"seatLocation\":\"MIDDLE\",\"seatNumber\":\"2C\",\"seatAvailabilityId\":80300}],\"25752\":[{\"sectionName\":\"Premium Economy\",\"originStation\":\"KABUYU\",\"seatClassName\":\"Premium Economy\",\"passengerType\":\"ADULT_PASSENGER\",\"originStationCode\":\"25752\",\"sectionCode\":\"JQVXLURB\",\"seatLocation\":\"MIDDLE\",\"seatNumber\":\"2C\",\"seatAvailabilityId\":80620}],\"19690\":[{\"sectionName\":\"Premium Economy\",\"originStation\":\"KALEYA\",\"seatClassName\":\"Premium Economy\",\"passengerType\":\"ADULT_PASSENGER\",\"originStationCode\":\"19690\",\"sectionCode\":\"JQVXLURB\",\"seatLocation\":\"MIDDLE\",\"seatNumber\":\"2C\",\"seatAvailabilityId\":80780}],\"05626\":[{\"sectionName\":\"Premium Economy\",\"originStation\":\"MAZABUKA\",\"seatClassName\":\"Premium Economy\",\"passengerType\":\"ADULT_PASSENGER\",\"originStationCode\":\"05626\",\"sectionCode\":\"JQVXLURB\",\"seatLocation\":\"MIDDLE\",\"seatNumber\":\"2C\",\"seatAvailabilityId\":80820}],\"72673\":[{\"sectionName\":\"Premium Economy\",\"originStation\":\"BWANA M'KUBWA\",\"seatClassName\":\"Premium Economy\",\"passengerType\":\"ADULT_PASSENGER\",\"originStationCode\":\"72673\",\"sectionCode\":\"JQVXLURB\",\"seatLocation\":\"MIDDLE\",\"seatNumber\":\"2C\",\"seatAvailabilityId\":80340}],\"01773\":[{\"sectionName\":\"Premium Economy\",\"originStation\":\"KAFUE\",\"seatClassName\":\"Premium Economy\",\"passengerType\":\"ADULT_PASSENGER\",\"originStationCode\":\"01773\",\"sectionCode\":\"JQVXLURB\",\"seatLocation\":\"MIDDLE\",\"seatNumber\":\"2C\",\"seatAvailabilityId\":80700}],\"27219\":[{\"sectionName\":\"Premium Economy\",\"originStation\":\"CHERA\",\"seatClassName\":\"Premium Economy\",\"passengerType\":\"ADULT_PASSENGER\",\"originStationCode\":\"27219\",\"sectionCode\":\"JQVXLURB\",\"seatLocation\":\"MIDDLE\",\"seatNumber\":\"2C\",\"seatAvailabilityId\":80380}],\"29584\":[{\"sectionName\":\"Premium Economy\",\"originStation\":\"CHOMA\",\"seatClassName\":\"Premium Economy\",\"passengerType\":\"ADULT_PASSENGER\",\"originStationCode\":\"29584\",\"sectionCode\":\"JQVXLURB\",\"seatLocation\":\"MIDDLE\",\"seatNumber\":\"2C\",\"seatAvailabilityId\":80500}],\"55001\":[{\"sectionName\":\"Premium Economy\",\"originStation\":\"MIENGWE\",\"seatClassName\":\"Premium Economy\",\"passengerType\":\"ADULT_PASSENGER\",\"originStationCode\":\"55001\",\"sectionCode\":\"JQVXLURB\",\"seatLocation\":\"MIDDLE\",\"seatNumber\":\"2C\",\"seatAvailabilityId\":80860}],\"01273\":[{\"sectionName\":\"Premium Economy\",\"originStation\":\"FUBERA\",\"seatClassName\":\"Premium Economy\",\"passengerType\":\"ADULT_PASSENGER\",\"originStationCode\":\"01273\",\"sectionCode\":\"JQVXLURB\",\"seatLocation\":\"MIDDLE\",\"seatNumber\":\"2C\",\"seatAvailabilityId\":80580}],\"11684\":[{\"sectionName\":\"Premium Economy\",\"originStation\":\"KAFULAFUTA\",\"seatClassName\":\"Premium Economy\",\"passengerType\":\"ADULT_PASSENGER\",\"originStationCode\":\"11684\",\"sectionCode\":\"JQVXLURB\",\"seatLocation\":\"MIDDLE\",\"seatNumber\":\"2C\",\"seatAvailabilityId\":80740}],\"21181\":[{\"sectionName\":\"Premium Economy\",\"originStation\":\"KABWE\",\"seatClassName\":\"Premium Economy\",\"passengerType\":\"ADULT_PASSENGER\",\"originStationCode\":\"21181\",\"sectionCode\":\"JQVXLURB\",\"seatLocation\":\"MIDDLE\",\"seatNumber\":\"2C\",\"seatAvailabilityId\":80660}],\"88046\":[{\"sectionName\":\"Premium Economy\",\"originStation\":\"BATOKA\",\"seatClassName\":\"Premium Economy\",\"passengerType\":\"ADULT_PASSENGER\",\"originStationCode\":\"88046\",\"sectionCode\":\"JQVXLURB\",\"seatLocation\":\"MIDDLE\",\"seatNumber\":\"2C\",\"seatAvailabilityId\":80260}],\"37505\":[{\"sectionName\":\"Premium Economy\",\"originStation\":\"MISWA\",\"seatClassName\":\"Premium Economy\",\"passengerType\":\"ADULT_PASSENGER\",\"originStationCode\":\"37505\",\"sectionCode\":\"JQVXLURB\",\"seatLocation\":\"MIDDLE\",\"seatNumber\":\"2C\",\"seatAvailabilityId\":80900}]}}";
			JSONObject userData1 = new JSONObject(jsonObjectStr);
			Iterator<String> keys = userData1.keys();
			while(keys.hasNext())
			{
				System.out.println("key == " + keys.next());
			}
			
			//String resp = "<?xml version='1.0' encoding='UTF-8' ?><responses><response status-code='0'><messageid>197121913464094042</messageid><messagestatus>SUCCESS</messagestatus><mobile>260976360360</mobile></response></responses>";
			/*Document doc = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder()
                    .parse(new InputSource(new StringReader(resp)));
			NodeList errNodes = doc.getElementsByTagName("response");
			if (errNodes.getLength() > 0) {
			    Element err = (Element)errNodes.item(0);
			    System.out.println(err.getElementsByTagName("messagestatus")
			                          .item(0)
			                          .getTextContent());
			} else { 
			        // success
			}*/
			
			
			try
			{
				/*boolean proceed = true;
				String status = null;
				SAXReader reader = new SAXReader();
				
				Document doc = reader.read(new StringReader(resp));
				NodeList nodes =
				Iterator nodeIterator = doc.nodeIterator();
				while(nodeIterator.hasNext() && proceed==true)
				{
					Node node = (Node)nodeIterator.next();
					System.out.println("node.getName()" + node.getName());
					if(node.getName()=="responses")
					{
						node.get
					}
					if(node.getName()=="messageStatus")
					{
						status = node.getText();
						proceed = false;
					}
				}
				System.out.println(status);*/
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
			//SmsSender ss = new SmsSender(swpService, "Test ABC", "260976360360");
			//ss.run();
			
			//String BASE_ENDPOINT = "159.8.22.212:8080";
			//BASE_ENDPOINT = "";
			/*//BASE_ENDPOINT = "localhost:8080";
			 * 
			
			DecimalFormat df = new DecimalFormat("0.00");
			String amt = df.format(amount);
			amount = Double.valueOf(amt);
			//
			//
			String toHash = terminalId+serviceType+orderId+amt+api_key;
			System.out.println("ToHAsh = " + toHash);
			System.out.println("To HAsh = " + terminalId+"-"+serviceType+"-"+orderId+"-"+amt+"-"+api_key);
			try {
				//toHash = "81420CUSTOMER_DEPOSIT_CASH_VIA_MERCHANT_OTC_ONUSfBkPZ2cEiZfv7kd3vdtThuc9LwbPXTOpzNsXIAXK";
				String hashed = UtilityHelper.get_SHA_512_SecurePassword(toHash);
				System.out.println("1.hash = " + hash);
				System.out.println("2.hash = " + hashed);
				if(hashed.equals(hash))
				{
					System.out.println("valid hash match");
				}
				else
				{
					System.out.println("invalid hash match");
				}
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				System.out.println(e.getMessage());
				System.out.println("valid hash match");
			}
			/*
			//com.probase.nrfa.services.AuthenticationService authService = new com.probase.nrfa.services.AuthenticationService();
			
			//1. Create New User//
			createNewVehicle(String token, String vehicleName,
				    String manufacturer, Integer seatingCapacity, String speed, String vehicleType, String requestId, String ipAddress, String clientCode)
			 createNewClient(String clientName, String clientTheme, String clientAddressLine1, String clientAddressLine2, Long countryId, String token, 
			String requestId, String ipAddress)
			addNewUser(String token, String username, String email, String mobileNumber, String userStatus, String details, 
					Boolean updateFlag, String uniqueId, String clientCode, String firstName, String lastName, String otherName, 
					String roleCode, String requestId, String ipAddress)
			login(String username, String encPassword, String clientCode, String ipAddress, String requestId)*/
			String requestId = RandomStringUtils.randomAlphanumeric(8);
			String ipAddress = "127.0.0.1";	    
			Authenticator at = Authenticator.getInstance();
			UtilityFunction uf = UtilityFunction.getInstance();
			VehicleFunction vf = VehicleFunction.getInstance();
			ClientFunction cf = ClientFunction.getInstance();
			CourierFunction cf2 = CourierFunction.getInstance();
			
			String pass = "{\"ADULT\":[{\"seatClass\":\"58120015\",\"seatLocation\":\"WINDOW\"}],\"CHILD\":[],\"SENIOR\":[],\"DISABLED\":[]}";
			Response userResponse = vf.searchAvailableTrips("4059730729", null, "32287", "11684", pass, "2019-09-08 01:00", "2019-09-12", 72, "58120015", 
					true, requestId, ipAddress, "5539619948");
			System.out.println("rs..." + userResponse.getEntity().toString());
			/*StationFunction sf = StationFunction.getInstance();
			Application app = Application.getInstance(swpService);
			
			
			
			
			Long districtId1 = 17L;
			Long districtId2 = 29L;
			Long districtId3 = 5L;
			Long districtId4 = 82L;*/
			
			//cf.createNewClient("Zambia Railway Limited", "Default", "Plot 100 Cadastrain Lane", "Northmead, Lusaka", 1, token, requestId, ipAddress);
			
			/*at.addNewUser2("distributor1@gmail.com", "distributor1@gmail.com", "+260972019919", UserStatus.ACTIVE.name(), 
					null, Boolean.FALSE, null, "001", "031", "Jude", "Paul", "Ben", RoleType.DISTRIBUTOR_STAFF.name());
			at.addNewUser2("tolluser2@nrfa.co.zm", "tolluser2@nrfa.co.zm", "+260972019919", UserStatus.ACTIVE.name(), 
							null, Boolean.FALSE, null, "001", "NRFA", "Chola", "Chinedu", "Mwenya", RoleType.NFRA_TOLL_STAFF.name());
			VehicleFunction vf = VehicleFunction.getInstance();
			Response userResponse = at.addNewUser(null, "zrladmin@zrl.com", "zrladmin@zrl.com", "+260967307151", UserStatus.ACTIVE.name(), 
					null, Boolean.FALSE, null,"ZRL", "John", "Doe", "Samson", RoleType.SUPER_ADMIN.name(), RandomStringUtils.randomAlphanumeric(8), "127.0.0.1");
			System.out.println("rs..." + userResponse.getEntity().toString());*/
			/*at.addNewUser2(null, "zrladmin@zrl.com", "zrladmin@zrl.com", "+260967307151", UserStatus.ACTIVE.name(), 
					null, Boolean.FALSE, null, "001", "NRFA", "Lucky1", "Somili1", "Muleyu1", RoleType.ADMIN_STAFF.name());*/
			/*at.addNewUser2("test2@zanazo.co.zm", "test2@zanazo.co.zm", "+260972812129", UserStatus.ACTIVE.name(), 
					null, Boolean.FALSE, null, "001", "031", "Charles", "Mare", "Madini", RoleType.BANK_STAFF.name());*/
					
			//2. Authenticate User
			//Admin Password: iaVzphRo
			/*String jsonObjectStr1 = (String)(at.login("superadmin@reservations.com", "CmJvpIAG", "ZRL", "127.0.0.1", requestId).getEntity());
			System.out.println(jsonObjectStr1);
			JSONObject userData1 = new JSONObject(jsonObjectStr1);
			String token = userData1.getString("token");
			System.out.println("token..." + token);*/
			//jsonObjectStr1 = (String)cf.createNewClient("Zambia Railway Limited", "Default", "Plot 100 Cadastrain Lane", "Northmead, Lusaka", 2L, token, requestId, ipAddress).getEntity();
			//System.out.println(jsonObjectStr1);
			/*Response userResponse = at.addNewUser(token, "zrlsuperadmin@zrl.com", "zrlsuperadmin@zrl.com", "+260967307151", UserStatus.ACTIVE.name(), 
					null, Boolean.FALSE, null,"5539619948", "Joseph", "Malama", "Odita", RoleType.ADMIN_STAFF.name(), RandomStringUtils.randomAlphanumeric(8), "127.0.0.1");*/
			
			//System.out.println("rs..." + userResponse.getEntity().toString());
			
			//String jsonObjectStr1 = (String)(at.login("zrlsuperadmin@zrl.com", "AL54KWXZ", "5539619948", "127.0.0.1", requestId).getEntity());
			/*String jsonObjectStr1 = (String)(at.login("pius.samuel@gmail.com", "ZSY7MekR", "5539619948", "127.0.0.1", requestId).getEntity());
			System.out.println(jsonObjectStr1);
			userData1 = new JSONObject(jsonObjectStr1);
			String token = userData1.getString("token");
			String clientCode = "5539619948";*/
			
			/*jsonObjectStr1 = (String)(cf2.updateShipmentStatus("INFO_RECEIVED", "33333", "4059730729", token, "4588584731663874", requestId, ipAddress, clientCode).getEntity());
			System.out.println(jsonObjectStr1);
			JSONObject jsOb = new JSONObject(jsonObjectStr1);
			Iterator<String> it = jsOb.keys();
			while(it.hasNext())
			{
				String s = it.next();
				System.out.println("s ==> " + s);
				
			}*/
			//jsonObjectStr1 = (String)(vf.createNewVehicle(token, "Orange Express", "Alstom Ferroviaria", 100, "603km/hr", "TRAIN", requestId, ipAddress, "5539619948").getEntity());
			//System.out.println(jsonObjectStr1);
			//jsonObjectStr1 = (String)(sf.createNewStation(token, "Lusaka Railway Station", districtId1, "Lusaka", "S0001", Boolean.FALSE, requestId, ipAddress, "5539619948").getEntity());
			//System.out.println(jsonObjectStr1);
			//jsonObjectStr1 = (String)(sf.createNewStation(token, "Ndola Railway Station", districtId2, "Ndola", "S0002", Boolean.FALSE, requestId, ipAddress, "5539619948").getEntity());
			//System.out.println(jsonObjectStr1);
			//jsonObjectStr1 = (String)(sf.createNewStation(token, "Kabwe Railway Station", districtId3, "Kabwe", "S0002", Boolean.FALSE, requestId, ipAddress, "5539619948").getEntity());
			//System.out.println(jsonObjectStr1);
			//jsonObjectStr1 = (String)(sf.createNewStation(token, "Livigstone Railway Station", districtId4, "Livingstone", "S0002", Boolean.FALSE, requestId, ipAddress, "5539619948").getEntity());
			//System.out.println(jsonObjectStr1);
			

			/*jsonObjectStr1 = (String)(uf.createNewScheduleDay("2019-06-27", "5539619948", token, requestId, ipAddress).getEntity());
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(uf.createNewScheduleDay("2019-06-28", "5539619948", token, requestId, ipAddress).getEntity());
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(uf.createNewScheduleDay("2019-06-29", "5539619948", token, requestId, ipAddress).getEntity());
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(uf.createNewScheduleDay("2019-06-30", "5539619948", token, requestId, ipAddress).getEntity());
			System.out.println(jsonObjectStr1);*/
			
			//(String scheduleDayName, String departureTime, String arrivalTime, String stationCode, String clientCode, String token, String requestId, String ipAddress)
			/*jsonObjectStr1 = (String)(uf.createNewScheduleStation("2019-06-27", "10:00", null, "53480", "5539619948", token, requestId, ipAddress).getEntity());
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(uf.createNewScheduleStation("2019-06-27", null, "13:00", "07796", "5539619948", token, requestId, ipAddress).getEntity());
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(uf.createNewScheduleStation("2019-06-27", "13:30", null, "07796", "5539619948", token, requestId, ipAddress).getEntity());
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(uf.createNewScheduleStation("2019-06-27", null, "16:00", "95527", "5539619948", token, requestId, ipAddress).getEntity());
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(uf.createNewScheduleStation("2019-06-27", "16:30", null, "95527", "5539619948", token, requestId, ipAddress).getEntity());
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(uf.createNewScheduleStation("2019-06-27", null, "18:00", "36135", "5539619948", token, requestId, ipAddress).getEntity());
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(uf.createNewScheduleStation("2019-06-27", "19:00", null, "36135", "5539619948", token, requestId, ipAddress).getEntity());
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(uf.createNewScheduleStation("2019-06-27", null, "20:30", "95527", "5539619948", token, requestId, ipAddress).getEntity());
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(uf.createNewScheduleStation("2019-06-27", "21:00", null, "95527", "5539619948", token, requestId, ipAddress).getEntity());
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(uf.createNewScheduleStation("2019-06-27", null, "23:30", "07796", "5539619948", token, requestId, ipAddress).getEntity());
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(uf.createNewScheduleStation("2019-06-28", "00:00", null, "07796", "5539619948", token, requestId, ipAddress).getEntity());
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(uf.createNewScheduleStation("2019-06-28", null, "03:00", "53480", "5539619948", token, requestId, ipAddress).getEntity());
			System.out.println(jsonObjectStr1);
			
			jsonObjectStr1 = (String)(uf.createNewScheduleStation("2019-06-28", "10:00", null, "53480", "5539619948", token, requestId, ipAddress).getEntity());
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(uf.createNewScheduleStation("2019-06-28", null, "13:00", "07796", "5539619948", token, requestId, ipAddress).getEntity());
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(uf.createNewScheduleStation("2019-06-28", "13:30", null, "07796", "5539619948", token, requestId, ipAddress).getEntity());
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(uf.createNewScheduleStation("2019-06-28", null, "16:00", "95527", "5539619948", token, requestId, ipAddress).getEntity());
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(uf.createNewScheduleStation("2019-06-28", "16:30", null, "95527", "5539619948", token, requestId, ipAddress).getEntity());
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(uf.createNewScheduleStation("2019-06-28", null, "18:00", "36135", "5539619948", token, requestId, ipAddress).getEntity());
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(uf.createNewScheduleStation("2019-06-28", "19:00", null, "36135", "5539619948", token, requestId, ipAddress).getEntity());
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(uf.createNewScheduleStation("2019-06-28", null, "20:30", "95527", "5539619948", token, requestId, ipAddress).getEntity());
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(uf.createNewScheduleStation("2019-06-28", "21:00", null, "95527", "5539619948", token, requestId, ipAddress).getEntity());
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(uf.createNewScheduleStation("2019-06-28", null, "23:30", "07796", "5539619948", token, requestId, ipAddress).getEntity());
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(uf.createNewScheduleStation("2019-06-29", "00:00", null, "07796", "5539619948", token, requestId, ipAddress).getEntity());
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(uf.createNewScheduleStation("2019-06-29", null, "03:00", "53480", "5539619948", token, requestId, ipAddress).getEntity());
			System.out.println(jsonObjectStr1);
			
			jsonObjectStr1 = (String)(uf.createNewScheduleStation("2019-06-29", "10:00", null, "53480", "5539619948", token, requestId, ipAddress).getEntity());
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(uf.createNewScheduleStation("2019-06-29", null, "13:00", "07796", "5539619948", token, requestId, ipAddress).getEntity());
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(uf.createNewScheduleStation("2019-06-29", "13:30", null, "07796", "5539619948", token, requestId, ipAddress).getEntity());
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(uf.createNewScheduleStation("2019-06-29", null, "16:00", "95527", "5539619948", token, requestId, ipAddress).getEntity());
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(uf.createNewScheduleStation("2019-06-29", "16:30", null, "95527", "5539619948", token, requestId, ipAddress).getEntity());
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(uf.createNewScheduleStation("2019-06-29", null, "18:00", "36135", "5539619948", token, requestId, ipAddress).getEntity());
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(uf.createNewScheduleStation("2019-06-29", "19:00", null, "36135", "5539619948", token, requestId, ipAddress).getEntity());
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(uf.createNewScheduleStation("2019-06-29", null, "20:30", "95527", "5539619948", token, requestId, ipAddress).getEntity());
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(uf.createNewScheduleStation("2019-06-29", "21:00", null, "95527", "5539619948", token, requestId, ipAddress).getEntity());
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(uf.createNewScheduleStation("2019-06-29", null, "23:30", "07796", "5539619948", token, requestId, ipAddress).getEntity());
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(uf.createNewScheduleStation("2019-06-30", "00:00", null, "07796", "5539619948", token, requestId, ipAddress).getEntity());
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(uf.createNewScheduleStation("2019-06-30", null, "03:00", "53480", "5539619948", token, requestId, ipAddress).getEntity());
			System.out.println(jsonObjectStr1);*/
			

//			//createNewVehicleSchedule(Long scheduleStationId, Long vehicleId, String clientCode, String token, String requestId, String ipAddress)
			/*jsonObjectStr1 = (String)(uf.createNewVehicleSchedule(110L, 3L, "5539619948", token, requestId, ipAddress).getEntity());	//Lusaka		Depart
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(uf.createNewVehicleSchedule(111L, 3L, "5539619948", token, requestId, ipAddress).getEntity());	//Ndola			Arrive
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(uf.createNewVehicleSchedule(112L, 3L, "5539619948", token, requestId, ipAddress).getEntity());	//Ndola			Depart
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(uf.createNewVehicleSchedule(113L, 3L, "5539619948", token, requestId, ipAddress).getEntity());	//Kabwe			Arrive
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(uf.createNewVehicleSchedule(114L, 3L, "5539619948", token, requestId, ipAddress).getEntity());	//Kabwe			Depart
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(uf.createNewVehicleSchedule(115L, 3L, "5539619948", token, requestId, ipAddress).getEntity());	//Livingstone	Arrive
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(uf.createNewVehicleSchedule(116L, 3L, "5539619948", token, requestId, ipAddress).getEntity());	//Livingstone	Depart
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(uf.createNewVehicleSchedule(117L, 3L, "5539619948", token, requestId, ipAddress).getEntity());	//Kabwe	Arrive
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(uf.createNewVehicleSchedule(118L, 3L, "5539619948", token, requestId, ipAddress).getEntity());	//Kabwe	Depart
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(uf.createNewVehicleSchedule(119L, 3L, "5539619948", token, requestId, ipAddress).getEntity());	//Ndola			Arrive
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(uf.createNewVehicleSchedule(120L, 3L, "5539619948", token, requestId, ipAddress).getEntity());	//Ndola			Depart
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(uf.createNewVehicleSchedule(121L, 3L, "5539619948", token, requestId, ipAddress).getEntity());	//Lusaka		Arrive
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(uf.createNewVehicleSchedule(122L, 3L, "5539619948", token, requestId, ipAddress).getEntity());	//Lusaka			Depart
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(uf.createNewVehicleSchedule(123L, 3L, "5539619948", token, requestId, ipAddress).getEntity());	//Ndola		Arrive
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(uf.createNewVehicleSchedule(124L, 3L, "5539619948", token, requestId, ipAddress).getEntity());	//Ndola			Depart
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(uf.createNewVehicleSchedule(125L, 3L, "5539619948", token, requestId, ipAddress).getEntity());	//Kabwe			Arrive
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(uf.createNewVehicleSchedule(126L, 3L, "5539619948", token, requestId, ipAddress).getEntity());	//Kabwe			Depart
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(uf.createNewVehicleSchedule(127L, 3L, "5539619948", token, requestId, ipAddress).getEntity());	//Livingstone		Arrive
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(uf.createNewVehicleSchedule(128L, 3L, "5539619948", token, requestId, ipAddress).getEntity());	//Livingstone			Depart
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(uf.createNewVehicleSchedule(129L, 3L, "5539619948", token, requestId, ipAddress).getEntity());	//Kabwe		Arrive
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(uf.createNewVehicleSchedule(130L, 3L, "5539619948", token, requestId, ipAddress).getEntity());	//Kabwe			Depart
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(uf.createNewVehicleSchedule(131L, 3L, "5539619948", token, requestId, ipAddress).getEntity());	//Ndola			Arrive
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(uf.createNewVehicleSchedule(132L, 3L, "5539619948", token, requestId, ipAddress).getEntity());	//Ndola			Depart
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(uf.createNewVehicleSchedule(133L, 3L, "5539619948", token, requestId, ipAddress).getEntity());	//Lusaka		Arrive
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(uf.createNewVehicleSchedule(134L, 3L, "5539619948", token, requestId, ipAddress).getEntity());	//Lusaka		Depart
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(uf.createNewVehicleSchedule(135L, 3L, "5539619948", token, requestId, ipAddress).getEntity());	//Ndola			Arrive
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(uf.createNewVehicleSchedule(136L, 3L, "5539619948", token, requestId, ipAddress).getEntity());	//Ndola			Depart
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(uf.createNewVehicleSchedule(137L, 3L, "5539619948", token, requestId, ipAddress).getEntity());	//Kabwe			Arrive
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(uf.createNewVehicleSchedule(138L, 3L, "5539619948", token, requestId, ipAddress).getEntity());	//Kabwe			Depart
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(uf.createNewVehicleSchedule(139L, 3L, "5539619948", token, requestId, ipAddress).getEntity());	//Livingstone		Arrive
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(uf.createNewVehicleSchedule(140L, 3L, "5539619948", token, requestId, ipAddress).getEntity());	//Livingstone			Depart
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(uf.createNewVehicleSchedule(143L, 3L, "5539619948", token, requestId, ipAddress).getEntity());	//Ndola		Arrive
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(uf.createNewVehicleSchedule(144L, 3L, "5539619948", token, requestId, ipAddress).getEntity());	//Ndola			Depart
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(uf.createNewVehicleSchedule(145L, 3L, "5539619948", token, requestId, ipAddress).getEntity());	//Lusaka		Arrive
			System.out.println(jsonObjectStr1);
			
			
			
			jsonObjectStr1 = (String)(uf.createNewVehicleSchedule(110L, 4L, "5539619948", token, requestId, ipAddress).getEntity());	//Lusaka		Depart
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(uf.createNewVehicleSchedule(113L, 4L, "5539619948", token, requestId, ipAddress).getEntity());	//Kabwe			Arrive
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(uf.createNewVehicleSchedule(114L, 4L, "5539619948", token, requestId, ipAddress).getEntity());	//Kabwe			Depart
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(uf.createNewVehicleSchedule(115L, 4L, "5539619948", token, requestId, ipAddress).getEntity());	//Livingstone	Arrive
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(uf.createNewVehicleSchedule(116L, 4L, "5539619948", token, requestId, ipAddress).getEntity());	//Livingstone	Depart
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(uf.createNewVehicleSchedule(119L, 4L, "5539619948", token, requestId, ipAddress).getEntity());	//Ndola			Arrive
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(uf.createNewVehicleSchedule(120L, 4L, "5539619948", token, requestId, ipAddress).getEntity());	//Ndola			Depart
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(uf.createNewVehicleSchedule(121L, 4L, "5539619948", token, requestId, ipAddress).getEntity());	//Lusaka		Arrive
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(uf.createNewVehicleSchedule(122L, 4L, "5539619948", token, requestId, ipAddress).getEntity());	//Lusaka			Depart
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(uf.createNewVehicleSchedule(123L, 4L, "5539619948", token, requestId, ipAddress).getEntity());	//Ndola		Arrive
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(uf.createNewVehicleSchedule(124L, 4L, "5539619948", token, requestId, ipAddress).getEntity());	//Ndola			Depart
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(uf.createNewVehicleSchedule(127L, 4L, "5539619948", token, requestId, ipAddress).getEntity());	//Livingstone		Arrive
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(uf.createNewVehicleSchedule(128L, 4L, "5539619948", token, requestId, ipAddress).getEntity());	//Livingstone			Depart
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(uf.createNewVehicleSchedule(129L, 4L, "5539619948", token, requestId, ipAddress).getEntity());	//Kabwe		Arrive
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(uf.createNewVehicleSchedule(130L, 4L, "5539619948", token, requestId, ipAddress).getEntity());	//Kabwe			Depart
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(uf.createNewVehicleSchedule(133L, 4L, "5539619948", token, requestId, ipAddress).getEntity());	//Lusaka		Arrive
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(uf.createNewVehicleSchedule(134L, 4L, "5539619948", token, requestId, ipAddress).getEntity());	//Lusaka			Depart
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(uf.createNewVehicleSchedule(135L, 4L, "5539619948", token, requestId, ipAddress).getEntity());	//Ndola		Arrive
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(uf.createNewVehicleSchedule(136L, 4L, "5539619948", token, requestId, ipAddress).getEntity());	//Ndola			Depart
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(uf.createNewVehicleSchedule(139L, 4L, "5539619948", token, requestId, ipAddress).getEntity());	//Livingstone		Arrive
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(uf.createNewVehicleSchedule(140L, 4L, "5539619948", token, requestId, ipAddress).getEntity());	//Livingstone			Depart
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(uf.createNewVehicleSchedule(143L, 4L, "5539619948", token, requestId, ipAddress).getEntity());	//Ndola		Arrive
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(uf.createNewVehicleSchedule(144L, 4L, "5539619948", token, requestId, ipAddress).getEntity());	//Ndola			Depart
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(uf.createNewVehicleSchedule(145L, 4L, "5539619948", token, requestId, ipAddress).getEntity());	//Lusaka		Arrive
			System.out.println(jsonObjectStr1);*/
			
			/*//createNewVehicleClass(String vehicleSeatClassName, String description, String clientCode, String token, String requestId, String ipAddress)
			jsonObjectStr1 = (String)(uf.createNewVehicleClass("First Class", "First Class Tickets", "5539619948", token, requestId, ipAddress).getEntity());
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(uf.createNewVehicleClass("Regular Class", "Regular Class Tickets", "5539619948", token, requestId, ipAddress).getEntity());
			System.out.println(jsonObjectStr1);*/
			

			/*JSONArray sectionNames = new JSONArray();
			JSONObject js1 = new JSONObject();
			js1.put("sectionName", "Cabin A");
			js1.put("sectionOrder", "1");
			js1.put("maxSeatingCapacity", 12);
			js1.put("maxStandingCapacity", 20);
			js1.put("sectionSeatClass", "Economy Class");
			sectionNames.put(js1);
			JSONObject js2 = new JSONObject();
			js2.put("sectionName", "Cabin B");
			js2.put("sectionOrder", "2");
			js2.put("maxSeatingCapacity", 6);
			js2.put("maxStandingCapacity", 10);
			js2.put("sectionSeatClass", "Standard Class");
			sectionNames.put(js2);
			JSONObject js3 = new JSONObject();
			js3.put("sectionName", "Cabin C");
			js3.put("sectionOrder", "3");
			js3.put("maxSeatingCapacity", 3);
			js3.put("sectionSeatClass", "Business Class");
			sectionNames.put(js3);
			//jsonObjectStr1 = (String)(vf.createNewVehicleSeatSection(token, sectionNames.toString(), requestId, ipAddress, clientCode).getEntity());
			*/
			
		/*	
		//VEHICLE 3
			int k1 = 62;
			while(k1<96)
			{
				int k = 1;		
				JSONObject allSchedulestations = new JSONObject();
				JSONObject schedulestations = new JSONObject();
				schedulestations.put("tripRouteOriginVehicleScheduleId", k1++);//Lusaka to Ndola
				schedulestations.put("tripRouteDestinationVehicleScheduleId", k1++);
				schedulestations.put("tripRouteOrder", 1);
				JSONObject jsFares = new JSONObject();
					JSONObject jsFare_ = new JSONObject();
					jsFare_.put("adultFare", 200.00);
					jsFare_.put("childFare", 100.00);
					jsFare_.put("maximumNumberOfCabins", 1);
				jsFares.put("UUVRCTTU", jsFare_);
					jsFare_ = new JSONObject();
					jsFare_.put("adultFare", 300.00);
					jsFare_.put("childFare", 150.00);
					jsFare_.put("maximumNumberOfCabins", 1);
				jsFares.put("NBADEMTO", jsFare_);
					jsFare_ = new JSONObject();
					jsFare_.put("adultFare", 400.00);
					jsFare_.put("childFare", 200.00);
					jsFare_.put("maximumNumberOfCabins", 1);
				jsFares.put("AYULMJZT", jsFare_);
				schedulestations.put("vehicleTripRouteSeatSection", jsFares);
				//schedulestations.put("adultFare", 200.00);
				//schedulestations.put("childFare", 100.00);
				allSchedulestations.put("" + k++, schedulestations);
				
				schedulestations = new JSONObject();
				schedulestations.put("tripRouteOriginVehicleScheduleId", k1++);	//Ndola to Kabwe
				schedulestations.put("tripRouteDestinationVehicleScheduleId", k1++);
				schedulestations.put("tripRouteOrder", 2);	
				jsFares = new JSONObject();
					jsFare_ = new JSONObject();
					jsFare_.put("adultFare", 300.00);
					jsFare_.put("childFare", 150.00);
					jsFare_.put("maximumNumberOfCabins", 1);
				jsFares.put("UUVRCTTU", jsFare_);
					jsFare_ = new JSONObject();
					jsFare_.put("adultFare", 400.00);
					jsFare_.put("childFare", 200.00);
					jsFare_.put("maximumNumberOfCabins", 1);
				jsFares.put("NBADEMTO", jsFare_);
					jsFare_ = new JSONObject();
					jsFare_.put("adultFare", 500.00);
					jsFare_.put("childFare", 250.00);
					jsFare_.put("maximumNumberOfCabins", 1);
				jsFares.put("AYULMJZT", jsFare_);
				schedulestations.put("vehicleTripRouteSeatSection", jsFares);
				//schedulestations.put("vehicleTripRouteSeatSection", "UUVRCTTU");
				//schedulestations.put("adultFare", 300.00);
				//schedulestations.put("childFare", 150.00);
				allSchedulestations.put("" + k++, schedulestations);
				
				schedulestations = new JSONObject();
				schedulestations.put("tripRouteOriginVehicleScheduleId", k1++);		//kabwe to livingstone
				schedulestations.put("tripRouteDestinationVehicleScheduleId", k1);
				schedulestations.put("tripRouteOrder", 3);
				jsFares = new JSONObject();
					jsFare_ = new JSONObject();
					jsFare_.put("adultFare", 250.00);
					jsFare_.put("childFare", 120.00);
					jsFare_.put("maximumNumberOfCabins", 1);
				jsFares.put("UUVRCTTU", jsFare_);
					jsFare_ = new JSONObject();
					jsFare_.put("adultFare", 350.00);
					jsFare_.put("childFare", 170.00);
					jsFare_.put("maximumNumberOfCabins", 1);
				jsFares.put("NBADEMTO", jsFare_);
					jsFare_ = new JSONObject();
					jsFare_.put("adultFare", 450.00);
					jsFare_.put("childFare", 220.00);
					jsFare_.put("maximumNumberOfCabins", 1);
				jsFares.put("AYULMJZT", jsFare_);
				schedulestations.put("vehicleTripRouteSeatSection", jsFares);
				allSchedulestations.put("" + k++, schedulestations);
				
				
				System.out.println("#####################");
				jsonObjectStr1 = (String)(vf.createNewVehicleTrip(token, Long.valueOf((k1-5)+""), Long.valueOf((k1)+""), null, allSchedulestations.toString(), requestId, ipAddress, "5539619948").getEntity());
				System.out.println(jsonObjectStr1);
				
				
				k1++;
				k = 1;
				allSchedulestations = new JSONObject();
				schedulestations = new JSONObject();
				schedulestations.put("tripRouteOriginVehicleScheduleId", k1++);//Lusaka to Ndola
				schedulestations.put("tripRouteDestinationVehicleScheduleId", k1++);
				schedulestations.put("tripRouteOrder", 4);
				jsFares = new JSONObject();
					jsFare_ = new JSONObject();
					jsFare_.put("adultFare", 250.00);
					jsFare_.put("childFare", 120.00);
					jsFare_.put("maximumNumberOfCabins", 1);
				jsFares.put("UUVRCTTU", jsFare_);
					jsFare_ = new JSONObject();
					jsFare_.put("adultFare", 350.00);
					jsFare_.put("childFare", 170.00);
					jsFare_.put("maximumNumberOfCabins", 1);
				jsFares.put("NBADEMTO", jsFare_);
					jsFare_ = new JSONObject();
					jsFare_.put("adultFare", 450.00);
					jsFare_.put("childFare", 220.00);
					jsFare_.put("maximumNumberOfCabins", 1);
				schedulestations.put("vehicleTripRouteSeatSection", jsFares);
				//schedulestations.put("adultFare", 200.00);
				//schedulestations.put("childFare", 100.00);
				allSchedulestations.put("" + k++, schedulestations);
				
				schedulestations = new JSONObject();
				schedulestations.put("tripRouteOriginVehicleScheduleId", k1++);	//Ndola to Kabwe
				schedulestations.put("tripRouteDestinationVehicleScheduleId", k1++);	
				schedulestations.put("tripRouteOrder", 5);
				jsFares = new JSONObject();
					jsFare_ = new JSONObject();
					jsFare_.put("adultFare", 300.00);
					jsFare_.put("childFare", 150.00);
					jsFare_.put("maximumNumberOfCabins", 1);
				jsFares.put("UUVRCTTU", jsFare_);
					jsFare_ = new JSONObject();
					jsFare_.put("adultFare", 400.00);
					jsFare_.put("childFare", 200.00);
					jsFare_.put("maximumNumberOfCabins", 1);
				jsFares.put("NBADEMTO", jsFare_);
					jsFare_ = new JSONObject();
					jsFare_.put("adultFare", 500.00);
					jsFare_.put("childFare", 250.00);
					jsFare_.put("maximumNumberOfCabins", 1);
				jsFares.put("AYULMJZT", jsFare_);
				schedulestations.put("vehicleTripRouteSeatSection", jsFares);
				//schedulestations.put("vehicleTripRouteSeatSection", "UUVRCTTU");
				//schedulestations.put("adultFare", 300.00);
				//schedulestations.put("childFare", 150.00);
				allSchedulestations.put("" + k++, schedulestations);
				
				schedulestations = new JSONObject();
				schedulestations.put("tripRouteOriginVehicleScheduleId", k1++);		//kabwe to livingstone
				schedulestations.put("tripRouteDestinationVehicleScheduleId", k1);
				schedulestations.put("tripRouteOrder", 6);
				jsFares = new JSONObject();
					jsFare_ = new JSONObject();
					jsFare_.put("adultFare", 200.00);
					jsFare_.put("childFare", 100.00);
					jsFare_.put("maximumNumberOfCabins", 1);
				jsFares.put("UUVRCTTU", jsFare_);
					jsFare_ = new JSONObject();
					jsFare_.put("adultFare", 300.00);
					jsFare_.put("childFare", 150.00);
					jsFare_.put("maximumNumberOfCabins", 1);
				jsFares.put("NBADEMTO", jsFare_);
					jsFare_ = new JSONObject();
					jsFare_.put("adultFare", 400.00);
					jsFare_.put("childFare", 200.00);
					jsFare_.put("maximumNumberOfCabins", 1);
				jsFares.put("AYULMJZT", jsFare_);
				jsFares.put("AYULMJZT", jsFare_);
				schedulestations.put("vehicleTripRouteSeatSection", jsFares);
				allSchedulestations.put("" + k++, schedulestations);
				
				
				System.out.println("#####################");
				jsonObjectStr1 = (String)(vf.createNewVehicleTrip(token, Long.valueOf((k1-5)+""), Long.valueOf((k1)+""), null, allSchedulestations.toString(), requestId, ipAddress, "5539619948").getEntity());
				System.out.println(jsonObjectStr1);
				

				k1++;
			}
			
			
			
			
			k1 = 96;
			//while(k1<120)
			//{
				int k = 1;		
				JSONObject allSchedulestations = new JSONObject();
				JSONObject schedulestations = new JSONObject();
				schedulestations.put("tripRouteOriginVehicleScheduleId", k1++);//Lusaka to Kabwe
				schedulestations.put("tripRouteDestinationVehicleScheduleId", k1++);
				schedulestations.put("tripRouteOrder", 1);
				JSONObject jsFares = new JSONObject();
					JSONObject jsFare_ = new JSONObject();
					jsFare_.put("adultFare", 200.00);
					jsFare_.put("childFare", 100.00);
					jsFare_.put("maximumNumberOfCabins", 1);
				jsFares.put("UUVRCTTU", jsFare_);
					jsFare_ = new JSONObject();
					jsFare_.put("adultFare", 300.00);
					jsFare_.put("childFare", 150.00);
					jsFare_.put("maximumNumberOfCabins", 1);
				jsFares.put("NBADEMTO", jsFare_);
					jsFare_ = new JSONObject();
					jsFare_.put("adultFare", 400.00);
					jsFare_.put("childFare", 200.00);
					jsFare_.put("maximumNumberOfCabins", 1);
				jsFares.put("AYULMJZT", jsFare_);
				schedulestations.put("vehicleTripRouteSeatSection", jsFares);
				//schedulestations.put("adultFare", 200.00);
				//schedulestations.put("childFare", 100.00);
				allSchedulestations.put("" + k++, schedulestations);
				
				schedulestations = new JSONObject();
				schedulestations.put("tripRouteOriginVehicleScheduleId", k1++);	//Kabwe to Livingstone
				schedulestations.put("tripRouteDestinationVehicleScheduleId", k1);
				schedulestations.put("tripRouteOrder", 2);	
				jsFares = new JSONObject();
					jsFare_ = new JSONObject();
					jsFare_.put("adultFare", 300.00);
					jsFare_.put("childFare", 150.00);
					jsFare_.put("maximumNumberOfCabins", 1);
				jsFares.put("UUVRCTTU", jsFare_);
					jsFare_ = new JSONObject();
					jsFare_.put("adultFare", 400.00);
					jsFare_.put("childFare", 200.00);
					jsFare_.put("maximumNumberOfCabins", 1);
				jsFares.put("NBADEMTO", jsFare_);
					jsFare_ = new JSONObject();
					jsFare_.put("adultFare", 500.00);
					jsFare_.put("childFare", 250.00);
					jsFare_.put("maximumNumberOfCabins", 1);
				jsFares.put("AYULMJZT", jsFare_);
				schedulestations.put("vehicleTripRouteSeatSection", jsFares);
				//schedulestations.put("vehicleTripRouteSeatSection", "UUVRCTTU");
				//schedulestations.put("adultFare", 300.00);
				//schedulestations.put("childFare", 150.00);
				allSchedulestations.put("" + k++, schedulestations);
				
				System.out.println("#####################");
				jsonObjectStr1 = (String)(vf.createNewVehicleTrip(token, Long.valueOf((k1-3)+""), Long.valueOf((k1)+""), null, allSchedulestations.toString(), requestId, ipAddress, "5539619948").getEntity());
				System.out.println(jsonObjectStr1);
				
				k1++;
				k=1;
				schedulestations = new JSONObject();
				schedulestations.put("tripRouteOriginVehicleScheduleId", k1++);		//Livingstone to Ndola
				schedulestations.put("tripRouteDestinationVehicleScheduleId", k1++);
				schedulestations.put("tripRouteOrder", 1);
				jsFares = new JSONObject();
					jsFare_ = new JSONObject();
					jsFare_.put("adultFare", 250.00);
					jsFare_.put("childFare", 120.00);
					jsFare_.put("maximumNumberOfCabins", 1);
				jsFares.put("UUVRCTTU", jsFare_);
					jsFare_ = new JSONObject();
					jsFare_.put("adultFare", 350.00);
					jsFare_.put("childFare", 170.00);
					jsFare_.put("maximumNumberOfCabins", 1);
				jsFares.put("NBADEMTO", jsFare_);
					jsFare_ = new JSONObject();
					jsFare_.put("adultFare", 450.00);
					jsFare_.put("childFare", 220.00);
					jsFare_.put("maximumNumberOfCabins", 1);
				jsFares.put("AYULMJZT", jsFare_);
				schedulestations.put("vehicleTripRouteSeatSection", jsFares);
				allSchedulestations.put("" + k++, schedulestations);
				
				schedulestations = new JSONObject();
				schedulestations.put("tripRouteOriginVehicleScheduleId", k1++);		//Ndola to Lusaka
				schedulestations.put("tripRouteDestinationVehicleScheduleId", k1);
				schedulestations.put("tripRouteOrder", 2);
				jsFares = new JSONObject();
					jsFare_ = new JSONObject();
					jsFare_.put("adultFare", 250.00);
					jsFare_.put("childFare", 120.00);
					jsFare_.put("maximumNumberOfCabins", 1);
				jsFares.put("UUVRCTTU", jsFare_);
					jsFare_ = new JSONObject();
					jsFare_.put("adultFare", 350.00);
					jsFare_.put("childFare", 170.00);
					jsFare_.put("maximumNumberOfCabins", 1);
				jsFares.put("NBADEMTO", jsFare_);
					jsFare_ = new JSONObject();
					jsFare_.put("adultFare", 450.00);
					jsFare_.put("childFare", 220.00);
					jsFare_.put("maximumNumberOfCabins", 1);
				jsFares.put("AYULMJZT", jsFare_);
				schedulestations.put("vehicleTripRouteSeatSection", jsFares);
				allSchedulestations.put("" + k++, schedulestations);
				
				
				System.out.println("#####################");
				jsonObjectStr1 = (String)(vf.createNewVehicleTrip(token, Long.valueOf((k1-3)+""), Long.valueOf((k1)+""), null, allSchedulestations.toString(), requestId, ipAddress, "5539619948").getEntity());
				System.out.println(jsonObjectStr1);
				
				
				k1++;
				k = 1;
				allSchedulestations = new JSONObject();
				schedulestations = new JSONObject();
				schedulestations.put("tripRouteOriginVehicleScheduleId", k1++);//Lusaka to Ndola
				schedulestations.put("tripRouteDestinationVehicleScheduleId", k1++);
				schedulestations.put("tripRouteOrder", 1);
				jsFares = new JSONObject();
					jsFare_ = new JSONObject();
					jsFare_.put("adultFare", 250.00);
					jsFare_.put("childFare", 120.00);
					jsFare_.put("maximumNumberOfCabins", 1);
				jsFares.put("UUVRCTTU", jsFare_);
					jsFare_ = new JSONObject();
					jsFare_.put("adultFare", 350.00);
					jsFare_.put("childFare", 170.00);
					jsFare_.put("maximumNumberOfCabins", 1);
				jsFares.put("NBADEMTO", jsFare_);
					jsFare_ = new JSONObject();
					jsFare_.put("adultFare", 450.00);
					jsFare_.put("childFare", 220.00);
					jsFare_.put("maximumNumberOfCabins", 1);
				schedulestations.put("vehicleTripRouteSeatSection", jsFares);
				//schedulestations.put("adultFare", 200.00);
				//schedulestations.put("childFare", 100.00);
				allSchedulestations.put("" + k++, schedulestations);
				
				schedulestations = new JSONObject();
				schedulestations.put("tripRouteOriginVehicleScheduleId", k1++);	//Ndola to Livingstone
				schedulestations.put("tripRouteDestinationVehicleScheduleId", k1);	
				schedulestations.put("tripRouteOrder", 2);
				jsFares = new JSONObject();
					jsFare_ = new JSONObject();
					jsFare_.put("adultFare", 300.00);
					jsFare_.put("childFare", 150.00);
					jsFare_.put("maximumNumberOfCabins", 1);
				jsFares.put("UUVRCTTU", jsFare_);
					jsFare_ = new JSONObject();
					jsFare_.put("adultFare", 400.00);
					jsFare_.put("childFare", 200.00);
					jsFare_.put("maximumNumberOfCabins", 1);
				jsFares.put("NBADEMTO", jsFare_);
					jsFare_ = new JSONObject();
					jsFare_.put("adultFare", 500.00);
					jsFare_.put("childFare", 250.00);
					jsFare_.put("maximumNumberOfCabins", 1);
				jsFares.put("AYULMJZT", jsFare_);
				schedulestations.put("vehicleTripRouteSeatSection", jsFares);
				//schedulestations.put("vehicleTripRouteSeatSection", "UUVRCTTU");
				//schedulestations.put("adultFare", 300.00);
				//schedulestations.put("childFare", 150.00);
				allSchedulestations.put("" + k++, schedulestations);
				System.out.println("#####################");
				jsonObjectStr1 = (String)(vf.createNewVehicleTrip(token, Long.valueOf((k1-3)+""), Long.valueOf((k1)+""), null, allSchedulestations.toString(), requestId, ipAddress, "5539619948").getEntity());
				System.out.println(jsonObjectStr1);
				
				
				k1++;
				k = 1;

				
				schedulestations = new JSONObject();
				schedulestations.put("tripRouteOriginVehicleScheduleId", k1++);		//Livingstone to kabwe
				schedulestations.put("tripRouteDestinationVehicleScheduleId", k1++);
				schedulestations.put("tripRouteOrder", 1);
				jsFares = new JSONObject();
					jsFare_ = new JSONObject();
					jsFare_.put("adultFare", 200.00);
					jsFare_.put("childFare", 100.00);
					jsFare_.put("maximumNumberOfCabins", 1);
				jsFares.put("UUVRCTTU", jsFare_);
					jsFare_ = new JSONObject();
					jsFare_.put("adultFare", 300.00);
					jsFare_.put("childFare", 150.00);
					jsFare_.put("maximumNumberOfCabins", 1);
				jsFares.put("NBADEMTO", jsFare_);
					jsFare_ = new JSONObject();
					jsFare_.put("adultFare", 400.00);
					jsFare_.put("childFare", 200.00);
					jsFare_.put("maximumNumberOfCabins", 1);
				jsFares.put("AYULMJZT", jsFare_);
				jsFares.put("AYULMJZT", jsFare_);
				schedulestations.put("vehicleTripRouteSeatSection", jsFares);
				allSchedulestations.put("" + k++, schedulestations);
				
								
				schedulestations = new JSONObject();
				schedulestations.put("tripRouteOriginVehicleScheduleId", k1++);		//Kabwe to Lusaka
				schedulestations.put("tripRouteDestinationVehicleScheduleId", k1);
				schedulestations.put("tripRouteOrder", 2);
				jsFares = new JSONObject();
					jsFare_ = new JSONObject();
					jsFare_.put("adultFare", 200.00);
					jsFare_.put("childFare", 100.00);
					jsFare_.put("maximumNumberOfCabins", 1);
				jsFares.put("UUVRCTTU", jsFare_);
					jsFare_ = new JSONObject();
					jsFare_.put("adultFare", 300.00);
					jsFare_.put("childFare", 150.00);
					jsFare_.put("maximumNumberOfCabins", 1);
				jsFares.put("NBADEMTO", jsFare_);
					jsFare_ = new JSONObject();
					jsFare_.put("adultFare", 400.00);
					jsFare_.put("childFare", 200.00);
					jsFare_.put("maximumNumberOfCabins", 1);
				jsFares.put("AYULMJZT", jsFare_);
				jsFares.put("AYULMJZT", jsFare_);
				schedulestations.put("vehicleTripRouteSeatSection", jsFares);
				allSchedulestations.put("" + k++, schedulestations);
				
				
				System.out.println("#####################");
				jsonObjectStr1 = (String)(vf.createNewVehicleTrip(token, Long.valueOf((k1-3)+""), Long.valueOf((k1)+""), null, allSchedulestations.toString(), requestId, ipAddress, "5539619948").getEntity());
				System.out.println(jsonObjectStr1);
				
				
				k1++;
				k = 1;

								
				schedulestations = new JSONObject();
				schedulestations.put("tripRouteOriginVehicleScheduleId", k1++);		//Lusaka to Ndola
				schedulestations.put("tripRouteDestinationVehicleScheduleId", k1++);
				schedulestations.put("tripRouteOrder", 1);
				jsFares = new JSONObject();
					jsFare_ = new JSONObject();
					jsFare_.put("adultFare", 200.00);
					jsFare_.put("childFare", 100.00);
					jsFare_.put("maximumNumberOfCabins", 1);
				jsFares.put("UUVRCTTU", jsFare_);
					jsFare_ = new JSONObject();
					jsFare_.put("adultFare", 300.00);
					jsFare_.put("childFare", 150.00);
					jsFare_.put("maximumNumberOfCabins", 1);
				jsFares.put("NBADEMTO", jsFare_);
					jsFare_ = new JSONObject();
					jsFare_.put("adultFare", 400.00);
					jsFare_.put("childFare", 200.00);
					jsFare_.put("maximumNumberOfCabins", 1);
				jsFares.put("AYULMJZT", jsFare_);
				jsFares.put("AYULMJZT", jsFare_);
				schedulestations.put("vehicleTripRouteSeatSection", jsFares);
				allSchedulestations.put("" + k++, schedulestations);
				
								
				schedulestations = new JSONObject();
				schedulestations.put("tripRouteOriginVehicleScheduleId", k1++);		//Ndola to Livingstone
				schedulestations.put("tripRouteDestinationVehicleScheduleId", k1);
				schedulestations.put("tripRouteOrder", 2);
				jsFares = new JSONObject();
					jsFare_ = new JSONObject();
					jsFare_.put("adultFare", 200.00);
					jsFare_.put("childFare", 100.00);
					jsFare_.put("maximumNumberOfCabins", 1);
				jsFares.put("UUVRCTTU", jsFare_);
					jsFare_ = new JSONObject();
					jsFare_.put("adultFare", 300.00);
					jsFare_.put("childFare", 150.00);
					jsFare_.put("maximumNumberOfCabins", 1);
				jsFares.put("NBADEMTO", jsFare_);
					jsFare_ = new JSONObject();
					jsFare_.put("adultFare", 400.00);
					jsFare_.put("childFare", 200.00);
					jsFare_.put("maximumNumberOfCabins", 1);
				jsFares.put("AYULMJZT", jsFare_);
				jsFares.put("AYULMJZT", jsFare_);
				schedulestations.put("vehicleTripRouteSeatSection", jsFares);
				allSchedulestations.put("" + k++, schedulestations);
				
				System.out.println("#####################");
				jsonObjectStr1 = (String)(vf.createNewVehicleTrip(token, Long.valueOf((k1-3)+""), Long.valueOf((k1)+""), null, allSchedulestations.toString(), requestId, ipAddress, "5539619948").getEntity());
				System.out.println(jsonObjectStr1);
				
				
				k1++;
				k = 1;

				
				
				schedulestations = new JSONObject();
				schedulestations.put("tripRouteOriginVehicleScheduleId", k1++);		//Livingstone to Ndola
				schedulestations.put("tripRouteDestinationVehicleScheduleId", k1++);
				schedulestations.put("tripRouteOrder", 1);
				jsFares = new JSONObject();
					jsFare_ = new JSONObject();
					jsFare_.put("adultFare", 200.00);
					jsFare_.put("childFare", 100.00);
					jsFare_.put("maximumNumberOfCabins", 1);
				jsFares.put("UUVRCTTU", jsFare_);
					jsFare_ = new JSONObject();
					jsFare_.put("adultFare", 300.00);
					jsFare_.put("childFare", 150.00);
					jsFare_.put("maximumNumberOfCabins", 1);
				jsFares.put("NBADEMTO", jsFare_);
					jsFare_ = new JSONObject();
					jsFare_.put("adultFare", 400.00);
					jsFare_.put("childFare", 200.00);
					jsFare_.put("maximumNumberOfCabins", 1);
				jsFares.put("AYULMJZT", jsFare_);
				jsFares.put("AYULMJZT", jsFare_);
				schedulestations.put("vehicleTripRouteSeatSection", jsFares);
				allSchedulestations.put("" + k++, schedulestations);
				

				
				
				schedulestations = new JSONObject();
				schedulestations.put("tripRouteOriginVehicleScheduleId", k1++);		//Ndola to Lusaka
				schedulestations.put("tripRouteDestinationVehicleScheduleId", k1);
				schedulestations.put("tripRouteOrder", 2);
				jsFares = new JSONObject();
					jsFare_ = new JSONObject();
					jsFare_.put("adultFare", 200.00);
					jsFare_.put("childFare", 100.00);
					jsFare_.put("maximumNumberOfCabins", 1);
				jsFares.put("UUVRCTTU", jsFare_);
					jsFare_ = new JSONObject();
					jsFare_.put("adultFare", 300.00);
					jsFare_.put("childFare", 150.00);
					jsFare_.put("maximumNumberOfCabins", 1);
				jsFares.put("NBADEMTO", jsFare_);
					jsFare_ = new JSONObject();
					jsFare_.put("adultFare", 400.00);
					jsFare_.put("childFare", 200.00);
					jsFare_.put("maximumNumberOfCabins", 1);
				jsFares.put("AYULMJZT", jsFare_);
				jsFares.put("AYULMJZT", jsFare_);
				schedulestations.put("vehicleTripRouteSeatSection", jsFares);
				allSchedulestations.put("" + k++, schedulestations);
				
				System.out.println("#####################");
				jsonObjectStr1 = (String)(vf.createNewVehicleTrip(token, Long.valueOf((k1-3)+""), Long.valueOf((k1)+""), null, allSchedulestations.toString(), requestId, ipAddress, "5539619948").getEntity());
				System.out.println(k1);
				

				k1++;
			//}
			 
			 
			
			
			
			
			
			
			
			
			
			
			
			
			
			
		//VEHICLE 4
			
			
			
			
			
			/*jsonObjectStr1 = (String)(vf.createNewVehicleTrip(token, 68L, 73L, requestId, ipAddress, "5539619948").getEntity());
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(vf.createNewVehicleTrip(token, 74L, 79L, requestId, ipAddress, "5539619948").getEntity());
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(vf.createNewVehicleTrip(token, 80L, 85L, requestId, ipAddress, "5539619948").getEntity());
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(vf.createNewVehicleTrip(token, 86L, 91L, requestId, ipAddress, "5539619948").getEntity());
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(vf.createNewVehicleTrip(token, 92L, 95L, requestId, ipAddress, "5539619948").getEntity());
			System.out.println(jsonObjectStr1);
			
			jsonObjectStr1 = (String)(vf.createNewVehicleTrip(token, 62L, 67L, requestId, ipAddress, "5539619948").getEntity());
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(vf.createNewVehicleTrip(token, 68L, 73L, requestId, ipAddress, "5539619948").getEntity());
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(vf.createNewVehicleTrip(token, 74L, 79L, requestId, ipAddress, "5539619948").getEntity());
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(vf.createNewVehicleTrip(token, 80L, 85L, requestId, ipAddress, "5539619948").getEntity());
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(vf.createNewVehicleTrip(token, 86L, 91L, requestId, ipAddress, "5539619948").getEntity());
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(vf.createNewVehicleTrip(token, 92L, 95L, requestId, ipAddress, "5539619948").getEntity());
			System.out.println(jsonObjectStr1);
			
			//Vehicle 4
			jsonObjectStr1 = (String)(vf.createNewVehicleTrip(token, 96L, 99L, requestId, ipAddress, "5539619948").getEntity());
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(vf.createNewVehicleTrip(token, 100L, 103L, requestId, ipAddress, "5539619948").getEntity());
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(vf.createNewVehicleTrip(token, 104L, 107L, requestId, ipAddress, "5539619948").getEntity());
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(vf.createNewVehicleTrip(token, 108L, 111L, requestId, ipAddress, "5539619948").getEntity());
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(vf.createNewVehicleTrip(token, 112L, 115L, requestId, ipAddress, "5539619948").getEntity());
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(vf.createNewVehicleTrip(token, 116L, 119L, requestId, ipAddress, "5539619948").getEntity());
			System.out.println(jsonObjectStr1);
			
			jsonObjectStr1 = (String)(vf.createNewVehicleTrip(token, 62L, 67L, requestId, ipAddress, "5539619948").getEntity());
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(vf.createNewVehicleTrip(token, 68L, 73L, requestId, ipAddress, "5539619948").getEntity());
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(vf.createNewVehicleTrip(token, 74L, 79L, requestId, ipAddress, "5539619948").getEntity());
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(vf.createNewVehicleTrip(token, 80L, 85L, requestId, ipAddress, "5539619948").getEntity());
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(vf.createNewVehicleTrip(token, 86L, 91L, requestId, ipAddress, "5539619948").getEntity());
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(vf.createNewVehicleTrip(token, 92L, 95L, requestId, ipAddress, "5539619948").getEntity());
			System.out.println(jsonObjectStr1);*/
			/**/
			
			
			
			/*jsonObjectStr1 = (String)(vf.createNewVehicleTripRouting(token, 7L, 38L, 39L, requestId, ipAddress, "5539619948").getEntity());
			System.out.println(jsonObjectStr1);		//2019-06-27 Train 3; Trip FSSQCWFZF; From Lusaka to Kabwe 
			jsonObjectStr1 = (String)(vf.createNewVehicleTripRouting(token, 7L, 40L, 41L, requestId, ipAddress, "5539619948").getEntity());
			System.out.println(jsonObjectStr1);		//2019-06-27 Train 3; Trip FSSQCWFZF; From Kabwe to Livingstone 
			jsonObjectStr1 = (String)(vf.createNewVehicleTripRouting(token, 8L, 42L, 43L, requestId, ipAddress, "5539619948").getEntity());
			System.out.println(jsonObjectStr1);		//2019-06-27 Train 3; Trip FSSQCWFZF; From Livingstone to Ndola 
			jsonObjectStr1 = (String)(vf.createNewVehicleTripRouting(token, 8L, 44L, 45L, requestId, ipAddress, "5539619948").getEntity());
			System.out.println(jsonObjectStr1);		//2019-06-27 Train 3; Trip FSSQCWFZF; From Ndola to Lusaka 
			jsonObjectStr1 = (String)(vf.createNewVehicleTripRouting(token, 9L, 46L, 47L, requestId, ipAddress, "5539619948").getEntity());
			System.out.println(jsonObjectStr1);		//2019-06-27 Train 3; Trip FSSQCWFZF; From Lusaka to Ndola 
			jsonObjectStr1 = (String)(vf.createNewVehicleTripRouting(token, 9L, 48L, 49L, requestId, ipAddress, "5539619948").getEntity());
			System.out.println(jsonObjectStr1);		//2019-06-27 Train 3; Trip FSSQCWFZF; From Ndola to Livingstone 
			jsonObjectStr1 = (String)(vf.createNewVehicleTripRouting(token, 10L, 50L, 51L, requestId, ipAddress, "5539619948").getEntity());
			System.out.println(jsonObjectStr1);		//2019-06-28 Train 3; Trip FSSQCWFZF; From Livingstone to Kabwe 
			jsonObjectStr1 = (String)(vf.createNewVehicleTripRouting(token, 10L, 52L, 53L, requestId, ipAddress, "5539619948").getEntity());
			System.out.println(jsonObjectStr1);		//2019-06-28 Train 3; Trip FSSQCWFZF; From Kabwe to Lusaka 
			jsonObjectStr1 = (String)(vf.createNewVehicleTripRouting(token, 11L, 54L, 55L, requestId, ipAddress, "5539619948").getEntity());
			System.out.println(jsonObjectStr1);		//2019-06-28 Train 3; Trip FSSQCWFZF; From Lusaka to Ndola 
			
			jsonObjectStr1 = (String)(vf.createNewVehicleTripRouting(token, 11L, 56L, 57L, requestId, ipAddress, "5539619948").getEntity());
			System.out.println(jsonObjectStr1);		//2019-06-28 Train 3; Trip FSSQCWFZF; From Ndola to Livingstone 
			jsonObjectStr1 = (String)(vf.createNewVehicleTripRouting(token, 12L, 58L, 59L, requestId, ipAddress, "5539619948").getEntity());
			System.out.println(jsonObjectStr1);		//2019-06-28 Train 3; Trip FSSQCWFZF; From Livingstone to Ndola 
			jsonObjectStr1 = (String)(vf.createNewVehicleTripRouting(token, 12L, 60L, 61L, requestId, ipAddress, "5539619948").getEntity());
			System.out.println(jsonObjectStr1);		//2019-06-28 Train 3; Trip FSSQCWFZF; From Ndola to Lusaka 
			jsonObjectStr1 = (String)(vf.createNewVehicleTripRouting(token, 5L, 62L, 63L, requestId, ipAddress, "5539619948").getEntity());
			System.out.println(jsonObjectStr1);		//2019-06-29 Train 3; Trip FSSQCWFZF; From Lusaka to Ndola 
			jsonObjectStr1 = (String)(vf.createNewVehicleTripRouting(token, 5L, 64L, 65L, requestId, ipAddress, "5539619948").getEntity());
			System.out.println(jsonObjectStr1);		//2019-06-29 Train 3; Trip FSSQCWFZF; From Lusaka to Ndola 
			jsonObjectStr1 = (String)(vf.createNewVehicleTripRouting(token, 5L, 66L, 67L, requestId, ipAddress, "5539619948").getEntity());
			System.out.println(jsonObjectStr1);		//2019-06-29 Train 3; Trip FSSQCWFZF; From Lusaka to Ndola 
			jsonObjectStr1 = (String)(vf.createNewVehicleTripRouting(token, 6L, 68L, 69L, requestId, ipAddress, "5539619948").getEntity());
			System.out.println(jsonObjectStr1);		//2019-06-29 Train 3; Trip FSSQCWFZF; From Lusaka to Ndola 
			jsonObjectStr1 = (String)(vf.createNewVehicleTripRouting(token, 6L, 70L, 71L, requestId, ipAddress, "5539619948").getEntity());
			System.out.println(jsonObjectStr1);		//2019-06-29 Train 3; Trip FSSQCWFZF; From Lusaka to Ndola 
			jsonObjectStr1 = (String)(vf.createNewVehicleTripRouting(token, 6L, 72L, 73L, requestId, ipAddress, "5539619948").getEntity());
			System.out.println(jsonObjectStr1);		//2019-06-29 Train 3; Trip FSSQCWFZF; From Lusaka to Ndola */
			
			/*jsonObjectStr1 = (String)(vf.createNewVehicleScheduleFares(token, 51L, 3L, 100.00, 180.00, requestId, ipAddress, "5539619948").getEntity());
			System.out.println(jsonObjectStr1);	//Lusaka to Kabwe 
			jsonObjectStr1 = (String)(vf.createNewVehicleScheduleFares(token, 52L, 3L, 60.00, 100.00, requestId, ipAddress, "5539619948").getEntity());
			System.out.println(jsonObjectStr1);	//Kabwe to Livingstone
			jsonObjectStr1 = (String)(vf.createNewVehicleScheduleFares(token, 53L, 3L, 120.00, 180.00, requestId, ipAddress, "5539619948").getEntity());
			System.out.println(jsonObjectStr1);	//Livingstone to Ndola
			jsonObjectStr1 = (String)(vf.createNewVehicleScheduleFares(token, 54L, 3L, 60.00, 100.00, requestId, ipAddress, "5539619948").getEntity());
			System.out.println(jsonObjectStr1);	//Ndola to Lusaka
			jsonObjectStr1 = (String)(vf.createNewVehicleScheduleFares(token, 55L, 3L, 60.00, 100.00, requestId, ipAddress, "5539619948").getEntity());
			System.out.println(jsonObjectStr1);	//Lusaka to Ndola
			jsonObjectStr1 = (String)(vf.createNewVehicleScheduleFares(token, 56L, 3L, 120.00, 180.00, requestId, ipAddress, "5539619948").getEntity());
			System.out.println(jsonObjectStr1);	//Ndola to Livingstone
			jsonObjectStr1 = (String)(vf.createNewVehicleScheduleFares(token, 57L, 3L, 60.00, 100.00, requestId, ipAddress, "5539619948").getEntity());
			System.out.println(jsonObjectStr1);	//Livingstone to Kabwe
			jsonObjectStr1 = (String)(vf.createNewVehicleScheduleFares(token, 58L, 3L, 120.00, 180.00, requestId, ipAddress, "5539619948").getEntity());
			System.out.println(jsonObjectStr1);	//Kabwe to Lusaka
			jsonObjectStr1 = (String)(vf.createNewVehicleScheduleFares(token, 59L, 3L, 60.00, 100.00, requestId, ipAddress, "5539619948").getEntity());
			System.out.println(jsonObjectStr1);	//Lusaka to Ndola
			jsonObjectStr1 = (String)(vf.createNewVehicleScheduleFares(token, 60L, 3L, 120.00, 180.00, requestId, ipAddress, "5539619948").getEntity());
			System.out.println(jsonObjectStr1);	//Ndola to Livingstone
			jsonObjectStr1 = (String)(vf.createNewVehicleScheduleFares(token, 61L, 3L, 120.00, 180.00, requestId, ipAddress, "5539619948").getEntity());
			System.out.println(jsonObjectStr1);	//Livingstone to Ndola
			jsonObjectStr1 = (String)(vf.createNewVehicleScheduleFares(token, 62L, 3L, 60.00, 100.00, requestId, ipAddress, "5539619948").getEntity());
			System.out.println(jsonObjectStr1);	//Ndola to Lusaka
			
			jsonObjectStr1 = (String)(vf.createNewVehicleScheduleFares(token, 51L, 4L, 60.00, 140.00, requestId, ipAddress, "5539619948").getEntity());
			System.out.println(jsonObjectStr1);	//Lusaka to Kabwe 
			jsonObjectStr1 = (String)(vf.createNewVehicleScheduleFares(token, 52L, 4L, 40.00, 80.00, requestId, ipAddress, "5539619948").getEntity());
			System.out.println(jsonObjectStr1);	//Kabwe to Livingstone
			jsonObjectStr1 = (String)(vf.createNewVehicleScheduleFares(token, 53L, 4L, 60.00, 140.00, requestId, ipAddress, "5539619948").getEntity());
			System.out.println(jsonObjectStr1);	//Livingstone to Ndola
			jsonObjectStr1 = (String)(vf.createNewVehicleScheduleFares(token, 54L, 4L, 40.00, 80.00, requestId, ipAddress, "5539619948").getEntity());
			System.out.println(jsonObjectStr1);	//Ndola to Lusaka
			jsonObjectStr1 = (String)(vf.createNewVehicleScheduleFares(token, 55L, 4L, 40.00, 80.00, requestId, ipAddress, "5539619948").getEntity());
			System.out.println(jsonObjectStr1);	//Lusaka to Ndola
			jsonObjectStr1 = (String)(vf.createNewVehicleScheduleFares(token, 56L, 4L, 60.00, 140.00, requestId, ipAddress, "5539619948").getEntity());
			System.out.println(jsonObjectStr1);	//Ndola to Livingstone
			jsonObjectStr1 = (String)(vf.createNewVehicleScheduleFares(token, 57L, 4L, 40.00, 80.00, requestId, ipAddress, "5539619948").getEntity());
			System.out.println(jsonObjectStr1);	//Livingstone to Kabwe
			jsonObjectStr1 = (String)(vf.createNewVehicleScheduleFares(token, 58L, 4L, 60.00, 140.00, requestId, ipAddress, "5539619948").getEntity());
			System.out.println(jsonObjectStr1);	//Kabwe to Lusaka
			jsonObjectStr1 = (String)(vf.createNewVehicleScheduleFares(token, 59L, 4L, 40.00, 80.00, requestId, ipAddress, "5539619948").getEntity());
			System.out.println(jsonObjectStr1);	//Lusaka to Ndola
			jsonObjectStr1 = (String)(vf.createNewVehicleScheduleFares(token, 60L, 4L, 60.00, 140.00, requestId, ipAddress, "5539619948").getEntity());
			System.out.println(jsonObjectStr1);	//Ndola to Livingstone
			jsonObjectStr1 = (String)(vf.createNewVehicleScheduleFares(token, 61L, 4L, 60.00, 140.00, requestId, ipAddress, "5539619948").getEntity());
			System.out.println(jsonObjectStr1);	//Livingstone to Ndola
			jsonObjectStr1 = (String)(vf.createNewVehicleScheduleFares(token, 62L, 4L, 40.00, 80.00, requestId, ipAddress, "5539619948").getEntity());
			System.out.println(jsonObjectStr1);	//Ndola to Lusaka
			jsonObjectStr1 = (String)(vf.createNewVehicleScheduleFares(token, 14L, 3L, 60.00, 100.00, requestId, ipAddress, "5539619948").getEntity());
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(vf.createNewVehicleScheduleFares(token, 15L, 3L, 60.00, 100.00, requestId, ipAddress, "5539619948").getEntity());
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(vf.createNewVehicleScheduleFares(token, 16L, 3L, 60.00, 100.00, requestId, ipAddress, "5539619948").getEntity());
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(vf.createNewVehicleScheduleFares(token, 17L, 3L, 60.00, 100.00, requestId, ipAddress, "5539619948").getEntity());
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(vf.createNewVehicleScheduleFares(token, 18L, 3L, 60.00, 100.00, requestId, ipAddress, "5539619948").getEntity());
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(vf.createNewVehicleScheduleFares(token, 19L, 3L, 60.00, 100.00, requestId, ipAddress, "5539619948").getEntity());
			System.out.println(jsonObjectStr1);
			

			jsonObjectStr1 = (String)(vf.createNewVehicleScheduleFares(token, 2L, 4L, 40.00, 80.00, requestId, ipAddress, "5539619948").getEntity());
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(vf.createNewVehicleScheduleFares(token, 3L, 4L, 40.00, 80.00, requestId, ipAddress, "5539619948").getEntity());
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(vf.createNewVehicleScheduleFares(token, 4L, 4L, 40.00, 80.00, requestId, ipAddress, "5539619948").getEntity());
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(vf.createNewVehicleScheduleFares(token, 5L, 4L, 40.00, 80.00, requestId, ipAddress, "5539619948").getEntity());
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(vf.createNewVehicleScheduleFares(token, 6L, 4L, 40.00, 80.00, requestId, ipAddress, "5539619948").getEntity());
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(vf.createNewVehicleScheduleFares(token, 7L, 4L, 40.00, 80.00, requestId, ipAddress, "5539619948").getEntity());
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(vf.createNewVehicleScheduleFares(token, 8L, 4L, 40.00, 80.00, requestId, ipAddress, "5539619948").getEntity());
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(vf.createNewVehicleScheduleFares(token, 9L, 4L, 40.00, 80.00, requestId, ipAddress, "5539619948").getEntity());
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(vf.createNewVehicleScheduleFares(token, 10L, 4L, 40.00, 80.00, requestId, ipAddress, "5539619948").getEntity());
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(vf.createNewVehicleScheduleFares(token, 11L, 4L, 40.00, 80.00, requestId, ipAddress, "5539619948").getEntity());
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(vf.createNewVehicleScheduleFares(token, 12L, 4L, 40.00, 80.00, requestId, ipAddress, "5539619948").getEntity());
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(vf.createNewVehicleScheduleFares(token, 13L, 4L, 40.00, 80.00, requestId, ipAddress, "5539619948").getEntity());
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(vf.createNewVehicleScheduleFares(token, 14L, 4L, 40.00, 80.00, requestId, ipAddress, "5539619948").getEntity());
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(vf.createNewVehicleScheduleFares(token, 15L, 4L, 40.00, 80.00, requestId, ipAddress, "5539619948").getEntity());
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(vf.createNewVehicleScheduleFares(token, 16L, 4L, 40.00, 80.00, requestId, ipAddress, "5539619948").getEntity());
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(vf.createNewVehicleScheduleFares(token, 17L, 4L, 40.00, 80.00, requestId, ipAddress, "5539619948").getEntity());
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(vf.createNewVehicleScheduleFares(token, 18L, 4L, 40.00, 80.00, requestId, ipAddress, "5539619948").getEntity());
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(vf.createNewVehicleScheduleFares(token, 19L, 4L, 40.00, 80.00, requestId, ipAddress, "5539619948").getEntity());
			System.out.println(jsonObjectStr1);*/
			
			
			//Departure Date: 28-06-2019	Departure Station: Lusaka		Arrival: Livingstone	Return = true	Class=Regular	Adult=2	Child=0
			String departureDate = "2019-06-28";
			String departureTime = "2019-06-28 10:00";
			//String departureStation = "53480";	//"Lusaka Railway Station";
			String departureStation = "07796";	//"Ndola Railway Station";
			String arrivalStation = "95527";	//"Kabwe Railway Station";
			//String arrivalStation = "36135";	//"Livigstone Railway Station";
			Boolean returnTrip = true;
			//String tripClass = "NBADEMTO";
			Long tripClass = 5L;
			int adultCount = 2;
			int childCount = 0;
			Long vehicleTripRoutingId;
			Long vehicleSeatClassId = null;
			int limit = -1;
			//String clientCode = "5539619948";
			//String requestId = RandomStringUtils.randomAlphanumeric(8);
			//String ipAddress = "127.0.0.1";
			int startIndex=0;
			Double adultFare = null;
			Double childFare = null;
			
			
			
			//jsonObjectStr1 = (String)(vf.listAvailableTrips(token, departureStation, arrivalStation, departureTime, 24, tripClass, false, requestId, ipAddress, clientCode).getEntity());
			//System.out.println(jsonObjectStr1);
			
			/*(String token, String tripCode, String departingStationCode, String arrivalStationCode, 
			String seatDetails, Boolean forcePreferences, Long generalTripClass, String requestId, String ipAddress, String clientCode, String tripToken)
			*/
			System.out.println("=========================>");
			String seatDetails = "{\"ADULT\":[{\"seatClass\":\"5\",\"seatLocation\":\"WINDOW\"},{\"seatClass\":\"5\",\"seatLocation\":\"WINDOW\"}],\"CHILD\":[{\"seatClass\":\"5\",\"seatLocation\":\"WINDOW\"},{\"seatClass\":\"5\",\"seatLocation\":\"WINDOW\"}]}";
			//jsonObjectStr1 = (String)(uf.createNewTicketCollectionPoint("Ndola Station", 29L, clientCode, token, requestId, ipAddress).getEntity());
			//(token, "Main Line 1", "ML1", requestId, ipAddress, clientCode).getEntity());
			//System.out.println(jsonObjectStr1);
			//jsonObjectStr1 = (String)(uf.createNewTicketCollectionPoint("Lusaka Station", 17L, clientCode, token, requestId, ipAddress).getEntity());
			//System.out.println(jsonObjectStr1);
			//jsonObjectStr1 = (String)(vf.getVehicleSeatAndLockDown(null, "ntyw7j244", "07796", "95527", seatDetails, false, 5L, requestId, ipAddress, clientCode, null).getEntity());
			//jsonObjectStr1 = (String)(vf.purchaseVehicleTripTickets("PROBASEPAY", deviceCode, ticketCollectionCenterCode, purchaseToken, purchaseDetails, generalTripClass, requestId, ipAddress, clientCode, token))
			//jsonObjectStr1 = (String)(cf.createNewDevice("POS", "18910110", clientCode, token, requestId, ipAddress).getEntity());
			String purchaseDetails= "{\"tripTicketData\":[{\"tripCode\":\"NTYW7J244\",\"departingStationCode\":\"07796\",\"arrivalStationCode\":\"95527\",\"adultPassenger\":2,\"childPassenger\":2,\"tripBookingToken\":\"ey" +
					"JhbGciOiJIUzI1NiJ9.eyJqdGkiOiI3a0FqRnZERFNQIiwiaWF0IjoxNTYzNzU2ODIyLCJzdWIiOiJ7XCJtYXBcIjp7XCJyb2xlQ29kZVwiOlwiR1VFU1RcIixcInVzZXJuYW1lXCI6XCJHVUVTVC1QU1RNQkJaV0dGXCJ9fSIsImlzcyI6IjU1Mzk2MTk5NDgiLCJleHAiOjE1NjM3NTcxMjJ9.le1I3iu8-EA" +
					"9XoXLHCySUSUQO-cgoGu1eggAqw3tSaE\",\"clientCode\":\"5539619948\",\"seatsAlloted\":[{\"seatClass\":\"Cabin A\",\"seatName\":\"1E\",\"seatFacingOtherSeats\":false,\"expirationPeriodMinutes\":5,\"seat" +
					"Location\":\"MIDDLE\",\"seatAvailabilityId\":668,\"seatNumber\":\"1E\",\"expirationDate\":\"2019-07-22 01:58\"},{\"seatClass\":\"Cabin A\",\"seatName\":\"1F\",\"seatFacingOtherSeats\":false,\"expirationPeri" +
					"odMinutes\":5,\"seatLocation\":\"AISLE\",\"seatAvailabilityId\":669,\"seatNumber\":\"1F\",\"expirationDate\":\"2019-07-22 01:58\"},{\"seatClass\":\"Cabin A\",\"seatName\":\"2A\",\"seatFacingOtherSeats\":false,\"ex" +
					"pirationPeriodMinutes\":5,\"seatLocation\":\"WINDOW\",\"seatAvailabilityId\":670,\"seatNumber\":\"2A\",\"expirationDate\":\"2019-07-22 01:58\"},{\"seatClass\":\"Cabin A\",\"seatName\":\"2B\",\"seatFacingOtherSeats\":fal" +
					"se,\"expirationPeriodMinutes\":5,\"seatLocation\":\"MIDDLE\",\"seatAvailabilityId\":671,\"seatNumber\":\"2B\",\"expirationDate\":\"2019-07-22 01:58\"}],\"passengerdetails\":{\"leadPassenger\":{\"firstname\":\"Kachi\",\"last" +
					"name\":\"Akujua\",\"emailaddress\":\"smicer66@yahoo.com\",\"mobilenumber\":\"967307151\",\"nationalid\":\"34343//344//590\"},\"otherPassengers\":[{\"firstname\":\"\",\"lastname\":\"\",\"emailaddress\":\"\",\"mobilenumber\":\"\",\"nationa" +
					"lid\":\"\"},{\"firstname\":\"\",\"lastname\":\"\",\"emailaddress\":\"\",\"mobilenumber\":\"\",\"nationalid\":\"\"},{\"firstname\":\"\",\"lastname\":\"\",\"emailaddress\":\"\",\"mobilenumber\":\"\",\"nationalid\":\"\"}]}}],\"probasePayMercha" +
					"ntCode\":\"MWCYY2RBNX\",\"probasePayDeviceCode\":\"GV4L8WPU\",\"transactionRef\":\"8387962456\",\"hash\":\"418976259152c1dedf69321d0e33bafb3af246bced47f1a7e9d8b9ccfc2844e6bbce351ed2b24edb88c7940a9fe6aec29b88e2d158ba8fc535e6a8dc09c3e4f3\"}";
			String purchaseToken = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI3a0FqRnZERFNQIiwiaWF0IjoxNTYzNzU2ODIyLCJzdWIiOiJ7XCJtYXBcIjp7XCJyb2xlQ29kZVwiOlwiR1VFU1RcIixcInVzZXJuYW1lXCI6XCJHVUVTVC1QU1RNQkJaV0dGXCJ9fSIsImlzcyI6IjU1Mzk2MTk5NDgiLCJleHAiOjE1NjM3NTcxMjJ9.le1I3iu8-EA9XoXLHCySUSUQO-cgoGu1eggAqw3tSaE";
			//jsonObjectStr1 = (String)(vf.purchaseVehicleTripTickets("PROBASEPAY", "5303152187", "13950509", purchaseToken, purchaseDetails, 5L, 
			//		requestId, ipAddress, clientCode, null).getEntity());
			//jsonObjectStr1 = (String)(vf.getVehicleTripDetailsByTransactionRef(token, "6701469607", requestId, ipAddress, clientCode).getEntity());
			//System.out.println(jsonObjectStr1);
			
			String passengerDetails = "{\"ADULT\":[{\"seatClass\":\"58120015\",\"seatLocation\":\"WINDOW\"}],\"CHILD\":[],\"SENIOR\":[],\"DISABLED\":[]}";
			/*(String token, String departureStationCode, String arrivalStationCode, String passengerDetails, 
					String departureTime, String returnTime, Integer hoursAdd,  String tripClass, Boolean returnTrip, String requestId, String ipAddress, String clientCode) */
			//jsonObjectStr1 = (String)(vf.
			//		searchAvailableTrips(token, "29584", "79333", passengerDetails, "2019-08-04 01:00", "2019-08-05 01:00", 24, "58120015",  false, requestId, ipAddress, clientCode).
			//		getEntity());
			String outwardTrip = "{\"tripCode\":\"410212515507\",\"departingStationCode\":\"29584\",\"arrivalStationCode\":\"15789\",\"seatDetails\":{\"ADULT\":[{\"seatClass\":\"58120015\",\"seatLocation\":\"WINDOW\"}],\"CHILD\":[],\"SENIOR\":[],\"DISABLED\":[]},\"clientCode\":\"5539619948\",\"forcePreferences\":false,\"generalTripClass\":\"58120015\"}";
			String inwardTrip = "{\"tripCode\":\"970226966112\",\"departingStationCode\":\"15789\",\"arrivalStationCode\":\"29584\",\"seatDetails\":{\"ADULT\":[{\"seatClass\":\"58120015\",\"seatLocation\":\"WINDOW\"}],\"CHILD\":[],\"SENIOR\":[],\"DISABLED\":[]},\"clientCode\":\"5539619948\",\"forcePreferences\":false,\"generalTripClass\":\"58120015\"}";
			inwardTrip = null;
			//jsonObjectStr1 = (String)(vf.getTripSeatAndLockDown(true, token, outwardTrip, inwardTrip, requestId, ipAddress, clientCode, null).
			//		getEntity());
			/*jsonObjectStr1 = (String)(uf.createNewScheduleStation(null, "42022629", "[\"UUVRCTTU\",\"AYULMJZT\"]", "2019-08-03", "16:00", 
					null, "08968",  clientCode, token, requestId, ipAddress).getEntity());*/
			//jsonObjectStr1 = (String)(uf.createNewScheduleStation(null, "42022629", "[\"UUVRCTTU\",\"AYULMJZT\"]", "2019-08-03", "16:00", 
			//		null, "08968",  clientCode, token, requestId, ipAddress).getEntity());
			/*jsonObjectStr1 = (String)(vf.getVehicleSeatAndLockDown(token, "970226966112", "79333", "21181", 
					seatDetails, false, "58120015", requestId, ipAddress, clientCode, null).getEntity());*/
			purchaseToken = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJtampSWlVFOHpwIiwiaWF0IjoxNTY2MDk3ODg2LCJzdWIiOiJ7XCJtYXBcIjp7XCJyb2xlQ29kZVwiOlwiR1VFU1RcIixcInVzZXJuYW1lXCI6XCJHVUVTVC1aQ1RESFNSUE1UXCJ9fSIsImlzcyI6IjU1Mzk2MTk5NDgiLCJleHAiOjE1NjYwOTg3ODZ9.xPeIPM_Nurds3DfgRrzaH12rFyhxr9uXQ7asOSrlsQk";
			purchaseDetails = "{\"tripTicketData\":{\"outwardTrip\":{\"410212515507\":{\"passengerdetails\":{\"leadPassenger\":{\"firstname\":\"Kachi\",\"lastname\":\"Akujua\",\"emailaddress\":\"smicer66@yahoo.com\",\"mobilenumber\":\"967307151\",\"nationalid\":\"11111\"},\"otherPassengers\":[{\"firstname\":\"a\",\"lastname\":\"a\",\"emailaddress\":\"sd@ggg.com\",\"mobilenumber\":\"967307151\",\"nationalid\":\"33333\"},{\"firstname\":\"f\",\"lastname\":\"g\",\"emailaddress\":\"fdf@ssdsd.com\",\"mobilenumber\":\"967307151\",\"nationalid\":\"55555\"}]},\"adultPassenger\":1,\"childPassenger\":0,\"seniorPassenger\":1,\"disabledPassenger\":1,\"seatsAlloted\":{\"29584\":[{\"sectionName\":\"Cabin A\",\"originStation\":\"CHOMA\",\"seatClassName\":\"Economy Class\",\"passengerType\":\"ADULT_PASSENGER\",\"originStationCode\":\"29584\",\"sectionCode\":\"UUVRCTTU\",\"seatLocation\":\"WINDOW\",\"seatNumber\":\"1A\",\"seatAvailabilityId\":4384},{\"sectionName\":\"Cabin A\",\"originStation\":\"CHOMA\",\"seatClassName\":\"Economy Class\",\"passengerType\":\"SENIOR_PASSENGER\",\"originStationCode\":\"29584\",\"sectionCode\":\"UUVRCTTU\",\"seatLocation\":\"MIDDLE\",\"seatNumber\":\"1B\",\"seatAvailabilityId\":4385},{\"sectionName\":\"Cabin A\",\"originStation\":\"CHOMA\",\"seatClassName\":\"Economy Class\",\"passengerType\":\"SPECIAL_NEEDS_PASSENGER\",\"originStationCode\":\"29584\",\"sectionCode\":\"UUVRCTTU\",\"seatLocation\":\"AISLE\",\"seatNumber\":\"1C\",\"seatAvailabilityId\":4386}],\"50279\":[{\"sectionName\":\"Cabin A\",\"originStation\":\"TARA\",\"seatClassName\":\"Economy Class\",\"passengerType\":\"ADULT_PASSENGER\",\"originStationCode\":\"50279\",\"sectionCode\":\"UUVRCTTU\",\"seatLocation\":\"WINDOW\",\"seatNumber\":\"1A\",\"seatAvailabilityId\":4408},{\"sectionName\":\"Cabin A\",\"originStation\":\"TARA\",\"seatClassName\":\"Economy Class\",\"passengerType\":\"SENIOR_PASSENGER\",\"originStationCode\":\"50279\",\"sectionCode\":\"UUVRCTTU\",\"seatLocation\":\"MIDDLE\",\"seatNumber\":\"1B\",\"seatAvailabilityId\":4409},{\"sectionName\":\"Cabin A\",\"originStation\":\"TARA\",\"seatClassName\":\"Economy Class\",\"passengerType\":\"SPECIAL_NEEDS_PASSENGER\",\"originStationCode\":\"50279\",\"sectionCode\":\"UUVRCTTU\",\"seatLocation\":\"AISLE\",\"seatNumber\":\"1C\",\"seatAvailabilityId\":4410}],\"21636\":[{\"sectionName\":\"Cabin A\",\"originStation\":\"SIBANYATI\",\"seatClassName\":\"Economy Class\",\"passengerType\":\"ADULT_PASSENGER\",\"originStationCode\":\"21636\",\"sectionCode\":\"UUVRCTTU\",\"seatLocation\":\"WINDOW\",\"seatNumber\":\"1A\",\"seatAvailabilityId\":4396},{\"sectionName\":\"Cabin A\",\"originStation\":\"SIBANYATI\",\"seatClassName\":\"Economy Class\",\"passengerType\":\"SENIOR_PASSENGER\",\"originStationCode\":\"21636\",\"sectionCode\":\"UUVRCTTU\",\"seatLocation\":\"MIDDLE\",\"seatNumber\":\"1B\",\"seatAvailabilityId\":4397},{\"sectionName\":\"Cabin A\",\"originStation\":\"SIBANYATI\",\"seatClassName\":\"Economy Class\",\"passengerType\":\"SPECIAL_NEEDS_PASSENGER\",\"originStationCode\":\"21636\",\"sectionCode\":\"UUVRCTTU\",\"seatLocation\":\"AISLE\",\"seatNumber\":\"1C\",\"seatAvailabilityId\":4398}],\"16725\":[{\"sectionName\":\"Cabin A\",\"originStation\":\"MUKWELA\",\"seatClassName\":\"Economy Class\",\"passengerType\":\"ADULT_PASSENGER\",\"originStationCode\":\"16725\",\"sectionCode\":\"UUVRCTTU\",\"seatLocation\":\"WINDOW\",\"seatNumber\":\"1A\",\"seatAvailabilityId\":4420},{\"sectionName\":\"Cabin A\",\"originStation\":\"MUKWELA\",\"seatClassName\":\"Economy Class\",\"passengerType\":\"SENIOR_PASSENGER\",\"originStationCode\":\"16725\",\"sectionCode\":\"UUVRCTTU\",\"seatLocation\":\"MIDDLE\",\"seatNumber\":\"1B\",\"seatAvailabilityId\":4421},{\"sectionName\":\"Cabin A\",\"originStation\":\"MUKWELA\",\"seatClassName\":\"Economy Class\",\"passengerType\":\"SPECIAL_NEEDS_PASSENGER\",\"originStationCode\":\"16725\",\"sectionCode\":\"UUVRCTTU\",\"seatLocation\":\"AISLE\",\"seatNumber\":\"1C\",\"seatAvailabilityId\":4422}]},\"departingFrom\":\"CHOMA\",\"arrivingAt\":\"KALOMO\",\"departingStation\":\"29584\",\"arrivingStation\":\"15789\",\"departureTime\":\"2019-08-04 18:34:00.0\",\"arrivalTime\":\"2019-08-04 20:53:00.0\",\"tripCode\":\"410212515507\"}},\"inwardTrip\":{\"970226966112\":{\"passengerdetails\":{\"leadPassenger\":{\"firstname\":\"Kachi\",\"lastname\":\"Akujua\",\"emailaddress\":\"smicer66@yahoo.com\",\"mobilenumber\":\"967307151\",\"nationalid\":\"11111\"},\"otherPassengers\":[{\"firstname\":\"a\",\"lastname\":\"a\",\"emailaddress\":\"sd@ggg.com\",\"mobilenumber\":\"967307151\",\"nationalid\":\"33333\"},{\"firstname\":\"f\",\"lastname\":\"g\",\"emailaddress\":\"fdf@ssdsd.com\",\"mobilenumber\":\"967307151\",\"nationalid\":\"55555\"}]},\"adultPassenger\":1,\"childPassenger\":0,\"seniorPassenger\":1,\"disabledPassenger\":1,\"seatsAlloted\":{\"16725\":[{\"sectionName\":\"Cabin A\",\"originStation\":\"MUKWELA\",\"seatClassName\":\"Economy Class\",\"passengerType\":\"ADULT_PASSENGER\",\"originStationCode\":\"16725\",\"sectionCode\":\"UUVRCTTU\",\"seatLocation\":\"WINDOW\",\"seatNumber\":\"1A\",\"seatAvailabilityId\":4684},{\"sectionName\":\"Cabin A\",\"originStation\":\"MUKWELA\",\"seatClassName\":\"Economy Class\",\"passengerType\":\"SENIOR_PASSENGER\",\"originStationCode\":\"16725\",\"sectionCode\":\"UUVRCTTU\",\"seatLocation\":\"MIDDLE\",\"seatNumber\":\"1B\",\"seatAvailabilityId\":4685},{\"sectionName\":\"Cabin A\",\"originStation\":\"MUKWELA\",\"seatClassName\":\"Economy Class\",\"passengerType\":\"SPECIAL_NEEDS_PASSENGER\",\"originStationCode\":\"16725\",\"sectionCode\":\"UUVRCTTU\",\"seatLocation\":\"AISLE\",\"seatNumber\":\"1C\",\"seatAvailabilityId\":4686}],\"15789\":[{\"sectionName\":\"Cabin A\",\"originStation\":\"KALOMO\",\"seatClassName\":\"Economy Class\",\"passengerType\":\"ADULT_PASSENGER\",\"originStationCode\":\"15789\",\"sectionCode\":\"UUVRCTTU\",\"seatLocation\":\"WINDOW\",\"seatNumber\":\"1A\",\"seatAvailabilityId\":4672},{\"sectionName\":\"Cabin A\",\"originStation\":\"KALOMO\",\"seatClassName\":\"Economy Class\",\"passengerType\":\"SENIOR_PASSENGER\",\"originStationCode\":\"15789\",\"sectionCode\":\"UUVRCTTU\",\"seatLocation\":\"MIDDLE\",\"seatNumber\":\"1B\",\"seatAvailabilityId\":4673},{\"sectionName\":\"Cabin A\",\"originStation\":\"KALOMO\",\"seatClassName\":\"Economy Class\",\"passengerType\":\"SPECIAL_NEEDS_PASSENGER\",\"originStationCode\":\"15789\",\"sectionCode\":\"UUVRCTTU\",\"seatLocation\":\"AISLE\",\"seatNumber\":\"1C\",\"seatAvailabilityId\":4674}]},\"departingFrom\":\"KALOMO\",\"arrivingAt\":\"CHOMA\",\"departingStation\":\"15789\",\"arrivingStation\":\"29584\",\"departureTime\":\"2019-08-06 01:00:00.0\",\"arrivalTime\":\"2019-08-06 03:15:00.0\",\"tripCode\":\"970226966112\"}}},\"probasePayMerchantCode\":\"MWCYY2RBNX\",\"probasePayDeviceCode\":\"GV4L8WPU\",\"hash\":\"80289e662fcbd541dddeaa3b93d45058cf74e197d3793eebdf4997f2b6a872ffc9aaa4fdee340c4fb003ff7b8f7f198a74bddb1450841e07197a1ca0e82f9343\"}";
			//jsonObjectStr1 = (String)(vf.purchaseVehicleTripTickets("PROBASEPAY", "4065648401", "5303152187", "13950509", purchaseToken, 
			//		purchaseDetails, "58120015", requestId, ipAddress, clientCode, null).getEntity());
			//jsonObjectStr1 = (String)(vf.getVehicleTripDetailsByTransactionRef(null, "4112057411", requestId, ipAddress, clientCode).getEntity());
			//System.out.println("jsonObjectStr1 --- " + jsonObjectStr1);
			
			Collection<CardScheme> districts = (Collection<CardScheme>)swpService.getAllRecords(District.class);
			Iterator<CardScheme> it = districts.iterator();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd HH:MM");
			while(it.hasNext())
			{
				CardScheme cs = it.next();
				/*String sql = "INSERT INTO `card_schemes` (`cardSchemeId`,`adultDiscountRate`,`cardSchemeStatus`,`childDiscountRate`,`createdAt`,`dayFridayApplicable`,`dayMondayApplicable`,`daySaturdayApplicable`,`daySundayApplicable`" +
						",`dayThursdayApplicable`,`dayTuesdayApplicable`,`dayWednesdayApplicable`,`deletedAt`,`expiryDate`,`fixedCharge`,`lowerAgeBoundary`,`maxAdultCount`,`maxChildCount`" +
						",`maxTripCount`,`peakPeriodApplicable`,`publicHolidayApplicable`,`schemeCode`,`schemeDetail`,`schemeName`,`transactionCharge`,`updatedAt`,`upperAgeBoundary`" +
						",`client_clientId`,`tripCardChargeMode`,`cardPrice`,`validityPeriod`,`yearlyPrice`,`discountRate`,`vendorCardPrice`,`openToVendor`) " +
					"VALUES ("+cs.getCardSchemeId()+", '"+ cs.getDiscountRate()==null ? "NULL" : cs.getDiscountRate() +"', '"+district.getCountryName()+"', '"+sdf.format(district.getCreatedAt())+"', '"+district.getDistrictCode()
					+"', '"+district.getName()+"', '"+district.getProvinceId()+"', '"+district.getProvinceName()+"', '"+sdf.format(district.getUpdatedAt())+"');";
				System.out.println(sql);*/
			}
			/*seatDetails = "{\"ADULT\":[{\"seatClass\":\"58120015\",\"seatLocation\":\"WINDOW\"}],\"CHILD\":[{\"seatClass\":\"58120015\",\"seatLocation\":\"WINDOW\"}],\"SENIOR\":[],\"DISABLED\":[]}";
			jsonObjectStr1 = (String)(vf.getVehicleSeatAndLockDown(token, "970226966112", "79333", "21181", 
					seatDetails, false, "58120015", requestId, ipAddress, clientCode, null).getEntity());
			jsonObjectStr1 = (String)(vf.
			JSONObject header = new JSONObject();
			System.out.println(jsonObjectStr1);
			header.put("", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIyMDE4MTIwMzAyNDgyNTk2NTI2MSIsImlzcyI6Imh0dHBzOi8vc2hpa29sYS5jb20vYXBpL2FwaS1hdXRoZW50aWNhdGUvc2hpa29sYUBpbnZlc3RydXN0YmFuay5jb20vYXpmeVhoZGwiLCJpYXQiOjE1NjQwNjgxMzIsImV4cCI6MTU2NDA3MTczMiwibmJmIjoxNTY0MDY4MTMyLCJqdGkiOiJVcVJ3VUpXT0lSOHg2SGN6In0.uURNUHElyGVDhoce2CQcUdhCQamHke8kYzYbr5U-XOk");
			//sendPost("https://shikola.com/api/", String parameters, JSONObject jsObj)
			/*jsonObjectStr1 = (String)(vf.createNewLine(token, "Main Line 1", "ML1", requestId, ipAddress, clientCode).getEntity());
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(vf.createNewLine(token, "Main Line 2", "ML2", requestId, ipAddress, clientCode).getEntity());
			System.out.println(jsonObjectStr1);
			jsonObjectStr1 = (String)(vf.createNewLine(token, "Main Line 3", "ML3", requestId, ipAddress, clientCode).getEntity());
			System.out.println(jsonObjectStr1);
			/*getVehicleSeatAndLockDown*/
			/*jsonObjectStr1 = (String)(vf.listVehicles(0, 100, token, requestId, clientCode).getEntity());
			JSONObject js = new JSONObject(jsonObjectStr1);
			String vehicleList = js.getString("vehicleList");
			System.out.println(vehicleList);
			JSONArray jsa = new JSONArray(vehicleList);
			for(int j=0; j<jsa.length(); j++)
			{
				String vehicleCode = ((JSONObject)jsa.get(j)).getString("vehicleCode");
				JSONArray sectionNames = new JSONArray();
				JSONObject section1 = new JSONObject();
				section1.put("sectionName", "Cabin 1");
				section1.put("sectionOrder", 1);
				JSONObject section2 = new JSONObject();
				section2.put("sectionName", "Cabin 2");
				section2.put("sectionOrder", 2);
				sectionNames.put(section1);
				sectionNames.put(section2);
				jsonObjectStr1 = (String)(vf.createNewVehicleSeatSection(token, sectionNames.toString(), vehicleCode, requestId, ipAddress, clientCode).getEntity());
				System.out.println(jsonObjectStr1);
			}*/
			
			/*JSONObject vehicleSeats = new JSONObject();
			vehicleSeats.put("sectionCode", "UUVRCTTU");
			JSONArray jsArray = new JSONArray();
			String[] seatColumns = {"A", "B", "C", "D", "E", "F"};
			int seatOrder = 1;
			for(int seatRow=1; seatRow<3; seatRow++)
			{
				for(int seatColumn=0; seatColumn<seatColumns.length; seatColumn++)
				{
					System.out.println("seatOrder%3 == " + (seatOrder%3));
					JSONObject seat1 = new JSONObject();
					seat1.put("seatNumber", seatRow + "" + seatColumns[seatColumn]);
					seat1.put("tripSeatFacing", false);
					seat1.put("vehicleSeatLocation", (seatOrder%3)==1 ? "WINDOW" : ((seatOrder%3==2) ? "NO_AISLE_NO_WINDOW" : "AISLE"));
					//AISLE, WINDOW
					seat1.put("seatOrder", seatOrder++);
					jsArray.put(seat1);
				}
			}
			System.out.println("seatOrder%3 == " + (jsArray.toString()));
			jsonObjectStr1 = jsArray.toString();
			vehicleSeats.put("sectionSeats", jsArray);
			jsonObjectStr1 = (String)(vf.createNewVehicleSeat(token, vehicleSeats.toString(), requestId, ipAddress, clientCode).getEntity());
			System.out.println(jsonObjectStr1);
			
			vehicleSeats.put("sectionCode", "NBADEMTO");
			seatColumns = new String[]{"A", "B", "C", "D", "E", "F"};
			seatOrder = 1;
			for(int seatRow=1; seatRow<2; seatRow++)
			{
				for(int seatColumn=0; seatColumn<seatColumns.length; seatColumn++)
				{
					System.out.println("seatOrder%3 == " + (seatOrder%3));
					JSONObject seat1 = new JSONObject();
					seat1.put("seatNumber", seatRow + "" + seatColumns[seatColumn]);
					seat1.put("tripSeatFacing", false);
					seat1.put("vehicleSeatLocation", (seatOrder%3)==1 ? "WINDOW" : ((seatOrder%3==2) ? "NO_AISLE_NO_WINDOW" : "AISLE"));
					//AISLE, WINDOW
					seat1.put("seatOrder", seatOrder++);
					jsArray.put(seat1);
				}
			}
			System.out.println("seatOrder%3 == " + (jsArray.toString()));
			jsonObjectStr1 = jsArray.toString();
			vehicleSeats.put("sectionSeats", jsArray);
			jsonObjectStr1 = (String)(vf.createNewVehicleSeat(token, vehicleSeats.toString(), requestId, ipAddress, clientCode).getEntity());
			System.out.println(jsonObjectStr1);
			
			

			vehicleSeats.put("sectionCode", "NBADEMTO");
			seatColumns = new String[]{"A", "B", "C"};
			seatOrder = 1;
			for(int seatRow=1; seatRow<2; seatRow++)
			{
				for(int seatColumn=0; seatColumn<seatColumns.length; seatColumn++)
				{
					System.out.println("seatOrder%3 == " + (seatOrder%3));
					JSONObject seat1 = new JSONObject();
					seat1.put("seatNumber", seatRow + "" + seatColumns[seatColumn]);
					seat1.put("tripSeatFacing", false);
					seat1.put("vehicleSeatLocation", (seatOrder%3)==1 ? "WINDOW" : ((seatOrder%3==2) ? "NO_AISLE_NO_WINDOW" : "AISLE"));
					//AISLE, WINDOW
					seat1.put("seatOrder", seatOrder++);
					jsArray.put(seat1);
				}
			}
			System.out.println("seatOrder%3 == " + (jsArray.toString()));
			jsonObjectStr1 = jsArray.toString();
			vehicleSeats.put("sectionSeats", jsArray);
			jsonObjectStr1 = (String)(vf.createNewVehicleSeat(token, vehicleSeats.toString(), requestId, ipAddress, clientCode).getEntity());
			System.out.println(jsonObjectStr1);*/
			
			
			
			/*Collection<VehicleTripRouting> vehicleTripRoutings = (Collection<VehicleTripRouting>)swpService.getAllRecords(VehicleTripRouting.class);
			Iterator<VehicleTripRouting> iter1 = vehicleTripRoutings.iterator();
			while(iter1.hasNext())
			{
				VehicleTripRouting vtr = iter1.next();
				String hql = "Select tp from VehicleSeat tp WHERE tp.client.clientCode = '" + vtr.getClient().getClientCode() + "' AND tp.deletedAt IS NULL";
				Collection<VehicleSeat> vehicleSeats= (Collection<VehicleSeat>)swpService.getAllRecordsByHQL(hql);
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
					vsa.setVehicleTripRouting(vtr);
					vsa = (VehicleSeatAvailability)(swpService.createNewRecord(vsa));
				}
			}*/
			
			
			/*jsonObjectStr1 = (String)(uf.getVehicleSeatClass(null, tripClass, token, clientCode, requestId, ipAddress).getEntity());
			System.out.println(jsonObjectStr1);
			JSONObject jsObj = null;
			if(jsonObjectStr1!=null)
			{
				jsObj = new JSONObject(jsonObjectStr1);
				jsObj = new JSONObject(jsObj.getString("vehicleSeatClassList"));
				vehicleSeatClassId = jsObj.has("vehicleSeatClassId") ? jsObj.getLong("vehicleSeatClassId") : null;
				System.out.println("vehicleSeatClassId == " + vehicleSeatClassId);
				jsonObjectStr1 = (String)(sf.getStation(null, departureStation, token, requestId, clientCode).getEntity());
				System.out.println(jsonObjectStr1);
				Long departureStationId = null;
				Long arrivalStationId = null;
				if(jsonObjectStr1!=null)
				{
					jsObj = new JSONObject(jsonObjectStr1);
					departureStationId = jsObj.has("station") ? (new JSONObject(jsObj.getString("station"))).getLong("stationId") : null;
					System.out.println("departureStationId == " + departureStationId);
				}
				
				jsonObjectStr1 = (String)(sf.getStation(null, arrivalStation, token, requestId, clientCode).getEntity());
				System.out.println(jsonObjectStr1);
				if(jsonObjectStr1!=null)
				{
					jsObj = new JSONObject(jsonObjectStr1);
					arrivalStationId = jsObj.has("station") ? (new JSONObject(jsObj.getString("station"))).getLong("stationId") : null;
					System.out.println("arrivalStationId == " + arrivalStationId);
				}
				
				jsonObjectStr1 = (String)(uf.getScheduleStations(departureDate, departureTime, null, departureStationId, arrivalStationId, token, clientCode, requestId, ipAddress).getEntity());
				System.out.println(jsonObjectStr1);
				List<Long> allTripIds = new ArrayList<Long>();
				if(jsonObjectStr1!=null)
				{
					jsObj = new JSONObject(jsonObjectStr1);
					JSONArray scheduleStations = jsObj.has("scheduleStationList") ? (new JSONArray(jsObj.getString("scheduleStationList"))) : null;
					System.out.println("scheduleStations == " + scheduleStations.toString());
					System.out.println("scheduleStations size == " + scheduleStations.length());
					for(int x=0; x < scheduleStations.length(); x++)
					{
						JSONObject scheduleStation = (JSONObject)(scheduleStations.get(x));
						System.out.println("scheduleStation.getLong('scheduleStationId') --- " + scheduleStation.toString());
						//.getLong("scheduleStationId")
						jsonObjectStr1 = (String)(uf.getVehicleSchedule(null, scheduleStation.getLong("scheduleStationId"), null, token, clientCode, requestId, ipAddress).getEntity());
						System.out.println(jsonObjectStr1);
						jsObj = new JSONObject(jsonObjectStr1);
						JSONArray vehicleSchedules = jsObj.has("vehicleSchedules") ? (new JSONArray(jsObj.getString("vehicleSchedules"))) : null;
						for(int y=0; y < vehicleSchedules.length(); y++)
						{
							JSONObject vehicleSchedule = (JSONObject)(vehicleSchedules.get(y));
							System.out.println("vehicleSchedule --- " + vehicleSchedule.toString());
							System.out.println("vehicleSchedule.getLong('vehicleScheduleId') == " + vehicleSchedule.getLong("vehicleScheduleId"));
							jsonObjectStr1 = (String)(vf.getVehicleTripRouting(null,  null, vehicleSchedule.getLong("vehicleScheduleId"), token, requestId, ipAddress, clientCode).getEntity());
							System.out.println(jsonObjectStr1);
							jsObj = new JSONObject(jsonObjectStr1);
							JSONArray vehicleTripRoutings = jsObj.has("vehicleTripRoutings") ? (new JSONArray(jsObj.getString("vehicleTripRoutings"))) : null;
							System.out.println("--------------------------");
							System.out.println(vehicleTripRoutings.length());
							System.out.println("--------------------------");
							for(int z=0; z < vehicleTripRoutings.length(); z++)
							{
								JSONObject vehicleTripRouting = (JSONObject)(vehicleTripRoutings.get(z));
								System.out.println("vehicleTripRouting --- " + vehicleTripRouting.toString());
								System.out.println("vehicleTripRouting.getLong('vehicleTripRoutingId') == " + vehicleTripRouting.getLong("vehicleTripRoutingId"));
								allTripIds.add(vehicleTripRouting.getLong("vehicleTripRoutingId"));
								
							}
						}
						
					}
				}
				
				
				System.out.println("##########################");
				Set<Long> allTripSet = new LinkedHashSet<Long>();
				allTripSet.addAll(allTripIds);
				allTripIds.clear();
				allTripIds.addAll(allTripSet);
				for(int i=0; i<allTripIds.size(); i++)
				{
					Long vehicleTripId = allTripIds.get(i);
					//jsonObjectStr1 = (String)(vf.listVehicleTrips(vehicleTripId, null, null, null, startIndex, limit, token, requestId, clientCode).getEntity());
					jsonObjectStr1 = (String)(vf.getVehicleTripRouting(null,  vehicleTripId, null, token, requestId, ipAddress, clientCode).getEntity());
					//System.out.println("jsonObjectStr1 --- " + jsonObjectStr1.toString());
					jsObj = new JSONObject(jsonObjectStr1);
					JSONArray vehicleTripRoutings = jsObj.has("vehicleTripRoutings") ? (new JSONArray(jsObj.getString("vehicleTripRoutings"))) : null;
					System.out.println("--------------------------");
					System.out.println(vehicleTripRoutings.length());
				}
			}*/
			
			
			//jsonObjectStr1 = (String)(vf.listVehicleScheduleFares(vehicleTripRoutingId, vehicleSeatClassId, childFare, adultFare, startIndex, limit, token, requestId, clientCode).getEntity());

			
			/*JSONObject userData1 = new JSONObject(jsonObjectStr1);
			String token1 = userData1.getString("token");
			System.out.println(token1);
			
			PaymentFunction pf = PaymentFunction.getInstance();
			Response rs = pf.listTransactions("031", null, null, null, null, null, null, null, null, null, "2017-09-21", "2017-09-23", null, 0, 50, token1, "123443");
			
			System.out.println((rs.getEntity().toString()));
			
			
			ReportFunction rpFn = ReportFunction.getInstance();
			Response rs = rpFn.getCardList(null, null, token1, null, null, null, null, null, "Test 123", "127.0.0.1");
			
			
			rs = rpFn.listTransactions( null, null, null, null, null, null,
					null, null, null, null, null, null, null, null, null, null, null, null, token1, "127.0.0.1");
			
			rs = rpFn.getTollPlazaByTransactionCountAndValue("1001",
					null, null, token1, "121212");
			System.out.println("rs..." + rs.getEntity().toString());
			
			rs = rpFn.getMerchantsByTransactionCountAndValue(null,
					null, null, token1, "121212");
			System.out.println("rs..." + rs.getEntity().toString());*/
			String cardPanUniqueMapStr = "[{\"cardSerialNo\":\"100001\",\"cardPan\":\"05539619948980965318000\"},{\"cardSerialNo\":\"100002\",\"cardPan\":\"05539619948980977706889\"}]";
			CardFunction cf1 = CardFunction.getInstance();
			//jsonObjectStr1 = (String)(cf1.getCardList( "98091", null, token, false, clientCode, requestId, ipAddress ).getEntity());
			//jsonObjectStr1 = (String)(cf1.assignCardToVendor("0422433092", cardPanUniqueMapStr, 80.00, token, requestId, ipAddress, clientCode).getEntity());
			//System.out.println(jsonObjectStr1);
			/*JSONObject json = new JSONObject();
			json.put("0", "89AJ29A0");
			json.put("1", "89AJ281K");
			json.put("2", "8KD82K03");
			json.put("3", "LOPA3923");
			json.put("4", "923SKD23");
			json.put("5", "202LD23");
			json.put("6", "910293E");
			cf.assignUniqueIdToCard("TNXMJCKSUN", json.toString(), token1);
			//at.logout(token1);
			/*JSONObject userData1 = new JSONObject(jsonObjectStr1);
			String token1 = userData1.getString("token");
			//String token1 = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI5RUdIbHE2OVU0IiwiaWF0IjoxNTA1ODc2MDc3LCJzdWIiOiJ7XCJtYXBcIjp7XCJ1c2VybmFtZVwiOlwibnJmYWRldkBwcm9iYXNlZ3JvdXAuY29tXCIsXCJyb2xlXCI6XCJORlJBX0FETUlOX1NUQUZGXCIsXCJicmFuY2hDb2RlXCI6XCIwMDFcIixcImJhbmtDb2RlXCI6XCJQUk9CQVNFXCJ9fSIsImlzcyI6IlBST0JBU0UiLCJhdWQiOiIwMDEiLCJleHAiOjE1MDU4Nzk2Nzd9.ZEE4Bp0Nfa70dKOCxOUrE8222P5L2HAfp_iW2zzSGqk";
			
			UtilityServices cardServices = new UtilityServices();
			UtilityFunction uFunction = UtilityFunction.getInstance();
			uFunction.createNewBank("Stanbic", "http://stanbic.co.za", "031", token1);
			System.out.println("End");
			
			
			//sendGet("http://localhost:8080/TestWebService/NCE/services/AuthenticationServices/demo-get-method", null);
			JSONObject jsObj = new JSONObject();
			String jsonObjectStr = sendPost("http://"+BASE_ENDPOINT+"/NFRAWebService/NCE/services/AuthenticationServices/authenticateUser", 
					"username=nrfaadmin@probasegroup.com&encPassword=password&bankCode=PROBASE",
					null);
			System.out.println(jsonObjectStr);
			JSONObject userData = new JSONObject(jsonObjectStr);
			String token = userData.getString("token");*/
			
			/*jsObj = new JSONObject();
			jsObj.put("auth_token", token);
			jsonObjectStr = sendPost("http://"+BASE_ENDPOINT+"/TestWebService/NCE/services/PaymentServices/getTrafficTollRouteIdentifers", 
					null,
					jsObj);
			System.out.println(jsonObjectStr);
			//Create New bank
			jsObj = new JSONObject();
			jsObj.put("auth_token", token);
			jsonObjectStr = sendPost("http://localhost:8080/TestWebService/NCE/services/UtilityServices/createNewBank", 
					"bankName=Stanbic&fqdn=http://stanbic.co.za&bankCode=031",
					jsObj);
			System.out.println(jsonObjectStr);
*/

			//List banks
			/*jsObj = new JSONObject();
			jsObj.put("auth_token", token);
			jsonObjectStr = sendPost("http://localhost:8080/TestWebService/NCE/services/UtilityServices/getBanks", 
					null,
					jsObj);*/
			


			/*jsObj = new JSONObject();
			jsObj.put("auth_token", token);
			jsonObjectStr = sendPost("http://159.8.22.212:8080/TestWebService/NCE/services/UtilityServices/getBanks", 
					null,
					jsObj);*/
			
			/*jsObj = new JSONObject();
			jsObj.put("auth_token", token);
			jsonObjectStr = sendPost("http://"+BASE_ENDPOINT+"/TestWebService/NCE/services/MerchantServices/createNewMerchantScheme", 
					"schemename=Scheme002&schemecode=002&transactionPercentage=0.0&fixedCharge=0.0",
					jsObj);
			System.out.println("jsonObjectStr == " + jsonObjectStr);
			
			jsObj = new JSONObject();
			jsObj.put("auth_token", token);
			jsonObjectStr = sendPost("http://"+BASE_ENDPOINT+"/TestWebService/NCE/services/MerchantServices/getMerchantSchemeList", 
					null,
					jsObj);
			
			jsObj = new JSONObject();
			jsObj.put("auth_token", token);
			jsonObjectStr = sendPost("http://"+BASE_ENDPOINT+"/TestWebService/NCE/services/MerchantServices/createNewMerchant", 
					"merchantName=Stanbic&merchantBankCode=031&merchantSchemeCode=001",
					jsObj);
			System.out.println("jsonObjectStr == " + jsonObjectStr);
			
			jsObj = new JSONObject();
			jsObj.put("auth_token", token);
			jsonObjectStr = sendPost("http://"+BASE_ENDPOINT+"/TestWebService/NCE/services/CardServices/addNewCardScheme", 
					"schemeName=CardScheme01&schemeDetails=test&extraCharges=0.00&transactionFee=0.00",
					jsObj);
			System.out.println("jsonObjectStr == " + jsonObjectStr);*/
			
			/*jsObj = new JSONObject();
			jsObj.put("auth_token", token);
			jsonObjectStr = sendPost("http://"+BASE_ENDPOINT+"/TestWebService/NCE/services/CardServices/addNewCard", 
					"cardType=CardScheme01&cardScheme=test&extraCharges=0.00&transactionFee=0.00",
					jsObj);

			//bankuser@gmail.com	XOhTF0Do
			//testtollsupervisor@gmail.com	yLCo8eMT
			System.out.println("111");
			String str;
			//String str = sendPost("http://localhost:8080/NFRAWebService/NCE/services/AuthenticationServices/authenticateUser/", "username=superadmin@probasegroup.com&encPassword=dCFF0RHE&bankCode=PROBASE", new JSONObject());
			String qry;
			qry = "username=nrfaadmin@probasegroup.com&encPassword=password&bankCode=NRFA";
			//qry = "username=testtollsupervisor@gmail.com&encPassword=yLCo8eMT&bankCode=NRFA";
			//qry = "username=bankuser@gmail.com&encPassword=XOhTF0Do&bankCode=001";
			//qry = "username=distributor1@gmail.com&encPassword=XOhTF0Do&bankCode=001";
			//str = sendPost("http://localhost:8080/NFRAWebService/NCE/services/AuthenticationServices/authenticateUser/", qry, new JSONObject());
			str = "";
			/*JSONObject js = new JSONObject(str);
			String token = js.getString("token");
			JSONObject authHeader = new JSONObject();
			authHeader.put("auth_token", token);
			
			qry = "username=testtollsupervisor@gmail.com&email=testtollsupervisor@gmail.com&mobileNumber=260964926646&userStatus=" + UserStatus.ACTIVE + "&branchCode=001&bankCode=NRFA&firstName=Bhon&lastName=Lars&roleCode=" + RoleType.NFRA_TOLL_STAFF_SUPERVISOR.name();
			//str = sendPost("http://localhost:8080/NFRAWebService/NCE/services/AuthenticationServices/addNewUser/", qry, authHeader);
			
			
			qry = "schemeName=Test Scheme&schemeDetails=Testing schemes&extraCharges=0.10&schemeCode=SCH10912&transactionFee=1.00&updateFlag=false";
			//str = sendPost("http://localhost:8080/NFRAWebService/NCE/services/CardServices/addNewCardScheme/", qry, authHeader);
			
			qry = "";
			//str = sendPost("http://localhost:8080/NFRAWebService/NCE/services/CardServices/getCardBatchIds/", qry, authHeader);
			
			qry = "batchId=DBLIPUVEYK&startIndex=0&limit=50";
			//str = sendPost("http://localhost:8080/NFRAWebService/NCE/services/CardServices/getListCardsByBatchIdWithNoUniqueId/", qry, authHeader);
			//CardFunction cf = new CardFunction();
			//cf.getListCardsByBatchIdWithNoUniqueId("DBLIPUVEYK", 0, 50, token, "34", "127.0.0.1");
			//cf.getCardList("001", 0, 50, "DBLIPUVEYK", token, Boolean.FALSE, null, null, "23", "127.0.0.1");
			//cf.getLastFiveTransactions("000100195401403", "1954", token, "23");
			//cf.getCard("000100195401403", "1954", token, "23", "127.0.0.1");
			
			//MerchantFunction mf = new MerchantFunction();
			//mf.createNewMerchant("Teddy Sales", "001", "M1023", Boolean.FALSE, token, "23", "127.0.0.1");
			//mf.getMerchantList(0, 50, token, "34");
			//mf.getMerchant("M00120", token, "23");
			//mf.createNewMerchantScheme("New Scheme Name", "M00120", 1.5, 0.00, token, "23", "127.0.0.1");
			//mf.getMerchantScheme("M00120", token, "43");
			//mf.getMerchantSchemeList(token, "54");
			//mf.getMerchantCount(MerchantStatus.ACTIVE.name(), "001", "56");
			
			//PaymentFunction pf = new PaymentFunction();
			String startDate = null, endDate = null;
			//pf.listTransactions(null, null, null, null, null, null, null, null, null, null, startDate, endDate, null, 0, 50, token, "76");
			//pf.generateEODTransactionList("001", token, "2017-12-02", "89", "127.0.0.1");
			
			qry = "username=testtolluser@gmail.com&tollRouteCode=M1023&tollRouteLane=1";
			//str = sendPost("http://localhost:8080/NFRAWebService/NCE/services/AuthenticationServices/assignTollStaffToTollRoute/", qry, authHeader);
			
			qry = "";
			//str = sendPost("http://localhost:8080/NFRAWebService/NCE/services/AuthenticationServices/listUsers/", qry, authHeader);
			
			qry = "username=testtolluser@gmail.com&userStatus=" + UserStatus.ACTIVE.name();
			//str = sendPost("http://localhost:8080/NFRAWebService/NCE/services/AuthenticationServices/updateUserStatus/", qry, authHeader);
			
			qry = "terminalCount=1&merchantCode=001&deviceType=" + DeviceType.POS.name();
			//str = sendPost("http://localhost:8080/NFRAWebService/NCE/services/TerminalServices/createNewTerminal/", qry, authHeader);
			
			qry = "startIndex=0&merchantCode=001";
			//str = sendPost("http://localhost:8080/NFRAWebService/NCE/services/TerminalServices/listTerminals/", qry, authHeader);
			
			qry = "terminalSerialId=190192&deviceType=" + DeviceType.POS.name();
			//str = sendPost("http://localhost:8080/NFRAWebService/NCE/services/TerminalServices/syncSetUpData/", qry, authHeader);
			
			qry = "contactEmail=distributor1@gmail.com&contactMobile=260964926646&address=1 Tern Road&city=Lusaka&district=Lusaka&province=Lusaka&companyName=Terb" +
					"&companyRegNo=RC18291&distributorStatus="+DistributorStatus.ACTIVE.name()+"&fundingAccountType="+FundingAccountType.DISTRIBUTOR_BANK_ACCOUNT.name()+
					"&distributorType="+DistributorType.INDIVIDUAL+"&contactFullName=Paul Reamet&merchantCode=001";
			//str = sendPost("http://localhost:8080/NFRAWebService/NCE/services/DistributorServices/createDistributor/", qry, authHeader);
			
			String orderId = RandomStringUtils.randomNumeric(8);
			String hash="12";
			qry = "distributorCode=16957959&amount="+2000.00+"&hash="+hash+"&orderId="+orderId+"&serviceType="+ServiceType.DISTRIBUTOR_DEPOSIT_CASH_INTO_DISTRIBUTOR_ACCOUNT+"&terminalId=81354";
			//str = sendPost("http://localhost:8080/NFRAWebService/NCE/services/DistributorServices/creditDistributorAccount/", qry, authHeader);
			
			qry = "";
			//str = sendGet("http://localhost:8080/NFRAWebService/NCE/services/CardServices/getCardSchemes/", qry, authHeader);
			
			String cardScheme = "8531";
			qry = "cardType=" + CardType.NRFA_TOLL_CARD_DEBIT_CARD.name() + "&cardScheme=" + cardScheme + "&quantity=5&merchantCode=001&branchCode=001";
			//str = sendPost("http://localhost:8080/NFRAWebService/NCE/services/CardServices/addNewCard/", qry, authHeader);
			
			JSONObject cardList = new JSONObject();
			cardList.put("0", "1954");
			cardList.put("1", "1893");
			qry = "batchId=DBLIPUVEYK&cardList="+cardList;
			//str = sendPost("http://localhost:8080/NFRAWebService/NCE/services/CardServices/assignUniqueIdToCard/", qry, authHeader);
					
			
			JSONObject jsCard = new JSONObject();
			JSONArray jsArr = new JSONArray();
			jsCard.put("cardPan", "000100195401403");
			jsCard.put("cardUniqueId", "1954");
			jsArr.put(jsCard);
			jsCard = new JSONObject();
			jsCard.put("cardPan", "000100189352638");
			jsCard.put("cardUniqueId", "1893");
			jsArr.put(jsCard);
			qry = "distributorCode=16957959&cardList=" + jsArr.toString();
			//str = sendPost("http://localhost:8080/NFRAWebService/NCE/services/DistributorServices/assignCardsToDistributor/", qry, authHeader);
			
			
			qry = "merchantCode=001&startIndex=0&limit=50";
			//str = sendPost("http://localhost:8080/NFRAWebService/NCE/services/DistributorServices/listDistributors/", qry, authHeader);
			
			
			qry = "";
			//str = sendGet("http://localhost:8080/NFRAWebService/NCE/services/DistributorServices/getFundingAccountTypes/", qry, authHeader);
			
			qry = "cardPan=000100195401403&cardUniqueId=1954&customerMobile=260964926646&distributorCode=16957959&firstName=Bopa&lastName" +
					"=Lasini&otherName=Brood&contactEmail=bopa.lasini@gmail.com&customerType="+CustomerType.INDIVIDUAL.name()+
					"&distributorTxnHash="+hash+"&orderId="+orderId+"&distributorTerminalId="+81354+"&amount="+500+
					"&narration=Assign Customer Card - 000100195401403&distributorChannel" +
					"="+Channel.POS+"&vehicleRegNumber=YPA91KS&nrfaCurrency="+TransactionCurrency.ZAMBIAN_KWACHA.name();
			//str = sendPost("http://localhost:8080/NFRAWebService/NCE/services/DistributorServices/assignCardsToCustomer/", qry, authHeader);
			
			qry = "distributorStatus="+DistributorStatus.ACTIVE.name()+"&merchantCode=001";
			qry = "cardStatus=" + CardStatus.ACTIVATED_LEVEL_1.name();
			//str = sendGet("http://localhost:8080/NFRAWebService/NCE/services/CardServices/getCardTypes", qry, authHeader);
			
			System.out.println("str == " + str);
			
			//terminalId=81354		terminalApiKey=sMwrnf9GDoPnpq83I8JMyHayluD720wK		serialNo=190192
			
			JSONObject qry1 = new JSONObject();
			JSONObject header = new JSONObject();
			header.put("serviceType", "cardDepositsEodNotification");
			qry1.put("header", header);
			JSONArray payload = new JSONArray();
			JSONObject data1 = new JSONObject();
			data1.put("distributorId", "10574589");
			data1.put("merchantCode", "1057458");
			data1.put("bankCode", "033");
			data1.put("bankName", "Stanic bank");
			data1.put("cardPanNumber", "74546557525565");
			data1.put("transactionDate", "2017-09-26");
			data1.put("amount", 3);
			data1.put("transactionReference", "TEST00301");
			payload.put(data1);

			JSONObject data2 = new JSONObject();
			data2.put("distributorId", "10574589");
			data2.put("merchantCode", "1057458");
			data2.put("bankCode", "033");
			data2.put("bankName", "Stanic bank");
			data2.put("cardPanNumber", "4545457878785");
			data2.put("transactionDate", "2017-10-26");
			data2.put("amount", 3);
			data2.put("transactionReference", "TEST00302");
			payload.put(data2);
			qry1.put("payload", payload);
			str = sendPost("http://probase.a2hosted.com/SmartPay/services/nrfa/eod", qry1.toString(), null);
			
			
			/*String token = "eyJpdiI6ImMzbDRXRkZXTkVKNFFrcHFUMHByYlE9PSIsInZhbHVlIjoiaHBmcmhYVVJIc2tsVzMwNlMwTUh5QT09IiwibWFjIjoiMj9kP11LSjd5Py4/P29YPz94JyE3XHUwMDE0P35AVz8oaT9cdTAwMDQ/In0=";
			
			MerchantFunction mf = MerchantFunction.getInstance();
			mf.createNewMerchant("Stanbic", "031", "001", token);*/
			/*JSONObject jsObj = new JSONObject();
			String merchantCode = "7OESIFCUXQ";
			String deviceCode = "5095943808";
			String transactionRef = "7452289038";
			String hash = "hash";
			//hash = transactionRef+apiKey+merchant.getMerchantCode()
			
			
			
			String transactionVerification = UtilityHelper.sendGet("http://probasepay.com/transaction/check-status/" +merchantCode+ "/" + deviceCode + "/" + transactionRef + "/" + hash, 
					null,
					jsObj);
			System.out.println("transactionVerification == " + transactionVerification);*/
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}

	private static String sendGet(String baseUrl, String parameters, JSONObject jsObj) throws Exception {

		String url = baseUrl + (parameters!=null ? ("?" + parameters) : "");
		System.out.println("url ==" + url);
		url="";

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		//add request header
		con.setRequestProperty("User-Agent", USER_AGENT);
		if(jsObj!=null && jsObj.length()>0)
		{
			Iterator<String> iter = jsObj.keys();
			while(iter.hasNext())
			{
				String key = iter.next();
				con.setRequestProperty(key, jsObj.getString(key));
			}
		}

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		//print result
		System.out.println(response.toString());
		return response.toString();

	}

	// HTTP POST request
	private static String sendPost(String baseUrl, String parameters, JSONObject jsObj) throws Exception {

		String url = "https://selfsolve.apple.com/wcResults.do";
		url = baseUrl;
		System.out.println("url == " + url);
		url = "";
		java.net.URL obj = new java.net.URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		System.out.println("113");
		//add reuqest header
		con.setRequestMethod("POST");
		System.out.println("114");
		con.setRequestProperty("User-Agent", USER_AGENT);
		System.out.println("115");
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		System.out.println("116");
		if(jsObj!=null && jsObj.length()>0)
		{
			Iterator<String> iter = jsObj.keys();
			while(iter.hasNext())
			{
				String key = iter.next();
				con.setRequestProperty(key, jsObj.getString(key));
			}
		}
		System.out.println("117");

		//String urlParameters = "sn=C02G8416DRJM&cn=&locale=&caller=&num=12345";
		String urlParameters = java.net.URLEncoder.encode(parameters, "UTF-8");;
		System.out.println("118");

		// Send post request
		con.setDoOutput(true);
		System.out.println("119");
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		System.out.println("121");
		if(urlParameters!=null)
			wr.writeBytes(urlParameters);
		
		System.out.println("112");
		
		wr.flush();
		wr.close();
		System.out.println("113");

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'POST' request to URL : " + url);
		System.out.println("Post parameters : " + urlParameters);
		System.out.println("Response Code : " + responseCode);
		
		if(responseCode==200)
		{
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
	
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
	
			//print result
			System.out.println(response.toString());
			return response.toString();
		}
		else
		{
			InputStream is1 = con.getErrorStream();
			BufferedReader in = new BufferedReader(new InputStreamReader(is1));
			String inputLine;
			StringBuffer response = new StringBuffer();
	
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
	
			//print result
			System.out.println(response.toString());
			return response.toString();
		}

	}

}
