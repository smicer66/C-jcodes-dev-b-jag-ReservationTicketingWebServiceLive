package com.probase.reservationticketingwebservice.util;

import java.util.Date;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;

import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.json.JSONArray;

import android.provider.Contacts.Settings;

import com.google.gson.Gson;
import com.probase.reservationticketingwebservice.enumerations.VehicleStatus;
import com.probase.reservationticketingwebservice.enumerations.DeviceType;
import com.probase.reservationticketingwebservice.models.CardScheme;
import com.probase.reservationticketingwebservice.models.Client;
import com.probase.reservationticketingwebservice.models.Province;
import com.probase.reservationticketingwebservice.models.Setting;
import com.probase.reservationticketingwebservice.services.ClientServices;

public class Application {

	
	

	private static Application instance;
	//private static Key key;
	private static Map privateKeys;
	private static Logger log = Logger.getLogger(Application.class);
	private ServiceLocator serviceLocator = ServiceLocator.getInstance();
	public SwpService swpService = null;
	private Map<String, JSONObject> allSettings = null;
	private Double minimumBalance = 10.00;
	private Double minimumTransactionAmountWeb = 1.00;
	private Double maximumTransactionAmountWeb = 100001.00;
	public static final Integer BASE_LIST_COUNT = 20;
	public static final String zrlKey = "Yn7sWDar7yPZZh7xvHFpnRWRNcj1l8Rf";
	public static final String probasePayApiKey = "utKI5vu9edma1Wqtojtl5sA1Z4Knuqar";
	public static final String probasePayMerchantKey = "MWCYY2RBNX";
	private Double luggagePerKg = 50.00;
	private Integer maximumTicketRetrieval = null;
	
	
    private Application(SwpService swpService)
    {
    	Map hmap = new HashMap();
    	try {
    		this.swpService = this.serviceLocator.getSwpService();
			Collection<Client> clientList = (Collection<Client>)swpService.getAllRecords(Client.class);
			Iterator<Client> itr = clientList.iterator();
			while(itr.hasNext())
			{
				Client client = itr.next();
				hmap.put(client.getClientCode(), client.getPrivateKey());
			}
			hmap.put("ZRL", this.zrlKey);
			this.setPrivateKeys(hmap);
			
			
			String hql = "Select tp from Setting tp where tp.deletedAt IS NULL";
			Collection<Setting> allSystemSettings = (Collection<Setting>)swpService.getAllRecordsByHQL(hql);
			Iterator<Setting> it = allSystemSettings.iterator();
			this.allSettings = new HashMap<String, JSONObject>();
			while(it.hasNext())
			{
				Setting setting = it.next();
				if(this.allSettings.containsKey(setting.getSettingId()))
				{
					JSONObject settingsForClient = (JSONObject)this.allSettings.get(setting.getSettingId());
					settingsForClient.put(setting.getSettingName(), setting.getSettingValue());
					this.allSettings.put(setting.getClient().getClientCode(), settingsForClient);
				}
				else
				{
					JSONObject settingsForClient = new JSONObject();
					settingsForClient.put(setting.getSettingName(), setting.getSettingValue());
					this.allSettings.put(setting.getClient().getClientCode(), settingsForClient);
				}
			}
			
			
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.getLocalizedMessage());
			System.out.println(e.getMessage());
		}
    }

    /**
     * Gets the shared instance of this Class
     *
     * @return the shared service locator instance.
     */
    public static final Application getInstance(SwpService swpService)
    {
    	System.out.println("Get Instance 1");
        if (instance == null)
        {
            instance = new Application(swpService);
        }
        return instance;
    }
    
    public static final Application getInstance(SwpService swpService, Boolean force)
    {
    	System.out.println("Get Instance 2");
        if (instance == null || force.equals(Boolean.TRUE))
        {
        	System.out.println("Force Reload of Application");
            instance = new Application(swpService);
        }
        return instance;
    }

	public void setPrivateKeys(Map privateKeys) {
		Application.privateKeys = privateKeys;
	}
	
	public static String getTokenKey()
	{
		return Application.zrlKey;
	}
	
	public String createJWT(String id, String issuer, String subject, long ttlMillis)
	{
		SignatureAlgorithm signAlg = SignatureAlgorithm.HS256;
		
		long nowMillis = System.currentTimeMillis();
		Date date = new Date(nowMillis);
		byte[] keyBytes = DatatypeConverter.parseBase64Binary(getTokenKey());
		Key signingKey = new SecretKeySpec(keyBytes, signAlg.getJcaName());
		
		JwtBuilder jwtBuilder = Jwts.builder().setId(id)
				.setIssuedAt(date)
				.setSubject(subject)
				.setIssuer(issuer)
				.signWith(signAlg, signingKey);
		
		
		
		if (ttlMillis >= 0) {
		    long expMillis = nowMillis + ttlMillis;
		    Date exp = new Date(expMillis);
		    jwtBuilder.setExpiration(exp);
		}
		
		return jwtBuilder.compact();
		
	}
	
	
	public Claims validate(String token) {
		SignatureAlgorithm signAlg = SignatureAlgorithm.HS256;
		byte[] keyBytes = DatatypeConverter.parseBase64Binary(getTokenKey());
		final Key signingKey = new SecretKeySpec(keyBytes, signAlg.getJcaName());

        return Jwts.parser()
            .setSigningKey(signingKey)
            .parseClaimsJws(token)
            .getBody();
    }
	
	
	public String destroyJWT(String token)
	{
		SignatureAlgorithm signAlg = SignatureAlgorithm.HS256;
		
		long nowMillis = System.currentTimeMillis();
		Date date = new Date(nowMillis);
		byte[] keyBytes = DatatypeConverter.parseBase64Binary(getTokenKey());
		Key signingKey = new SecretKeySpec(keyBytes, signAlg.getJcaName());
		
		JwtBuilder jwtBuilder = Jwts.builder().setId(null)
				.setIssuedAt(date)
				.setSubject(null)
				.setIssuer(null)
				.signWith(signAlg, signingKey)
				.setExpiration(null);
		
		return jwtBuilder.compact();
		
	}

	public Double getMinimumBalance() {
		return minimumBalance;
	}

	public Double getMinimumTransactionAmountWeb() {
		return minimumTransactionAmountWeb;
	}

	public Double getMaximumTransactionAmountWeb() {
		return maximumTransactionAmountWeb;
	}

	public Map getAllSettings() {
		return this.allSettings;
	}

	public void setAllSettings(Map allSettings) {
		this.allSettings = allSettings;
	}

	public void setMinimumBalance(Double minimumBalance) {
		this.minimumBalance = minimumBalance;
	}

	public void setMinimumTransactionAmountWeb(Double minimumTransactionAmountWeb) {
		this.minimumTransactionAmountWeb = minimumTransactionAmountWeb;
	}

	public void setMaximumTransactionAmountWeb(Double maximumTransactionAmountWeb) {
		this.maximumTransactionAmountWeb = maximumTransactionAmountWeb;
	}

	public static Map getPrivateKeys() {
		return privateKeys;
	}

	public String getPublicKey(String clientCode) {
		// TODO Auto-generated method stub
		String obj = (String)Application.privateKeys.get(clientCode);
		return obj;
		
	}

	public Double getLuggagePerKg() {
		return luggagePerKg;
	}

	public void setLuggagePerKg(Double luggagePerKg) {
		this.luggagePerKg = luggagePerKg;
	}

	public Integer getMaximumTicketRetrieval() {
		return maximumTicketRetrieval;
	}

	public void setMaximumTicketRetrieval(Integer maximumTicketRetrieval) {
		this.maximumTicketRetrieval = maximumTicketRetrieval;
	}
}
