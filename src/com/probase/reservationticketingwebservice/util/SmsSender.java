package com.probase.reservationticketingwebservice.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.probase.reservationticketingwebservice.models.SMSMesage;

public class SmsSender implements Runnable {
	private static Logger log = Logger.getLogger(SmsSender.class);
	SwpService swpService;
	String message;
	String receipientMobileNumber;
	//final static String baseUrl = "https://www.probasesms.com/text/multi/res/trns/sms";
	final static String baseUrl = "http://smsapi.probasesms.com/apis/text/index.php";
	///https://www.probasesms.com/text/multi/res/trns/sms?username=rtsa&password=password@1&mobiles=260967307151&message=Logging+Into+ProbasePay.com%3F+%0AYour+One+Time+Password+is+6958%0A%0AThank+You.&sender=RTSA&type=TEXT
	
	public SmsSender(SwpService swpService, String message, String receipientMobileNumber)
	{
		this.swpService = swpService;
		this.message = message;
		this.receipientMobileNumber = receipientMobileNumber;
	}
	
	public void run() {
		try
		{
			receipientMobileNumber = receipientMobileNumber.startsWith("260") ? receipientMobileNumber : (receipientMobileNumber.startsWith("0") ? ("260"+receipientMobileNumber.substring(1)) : null);
			if(receipientMobileNumber!=null)
			{
				String url = baseUrl + "?username=demo&password=password&mobiles="+receipientMobileNumber+"&message="+URLEncoder.encode(message,"UTF-8")+"&sender=NRFA&type=TEXT";
				
		
				URL obj = new URL(url);
				HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		
				// optional default is GET
				con.setRequestMethod("GET");
		
				//add request header
				con.setRequestProperty("User-Agent", "Mozilla/5.0");
		
				int responseCode = con.getResponseCode();
				//log.info"\nSending 'GET' request to URL : " + url);
				//log.info"Response Code : " + responseCode);
		
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
					con.disconnect();
			
					//print result
					//log.inforesponse.toString());
					String status = getSmsResponseStatus(response.toString());
					SMSMesage sms = new SMSMesage();
					sms.setReceipentMobileNumber(receipientMobileNumber);
					sms.setResponseCode(responseCode);
					sms.setMessage(message);
					sms.setCreatedAt(new Date());
					sms.setUpdatedAt(new Date());
					sms.setDataResponse(response.toString());
					sms.setStatus(status==null ? "FAILED" : "SUCCESS");
					swpService.createNewRecord(sms);
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			log.error(">>", e);
		}
	}
	
	
	public static String getSmsResponseStatus(String resp)
	{
		try
		{
			Document doc = DocumentBuilderFactory.newInstance()
	                .newDocumentBuilder()
	                .parse(new InputSource(new StringReader(resp)));
			NodeList errNodes = doc.getElementsByTagName("response");
			if (errNodes.getLength() > 0) {
			    Element err = (Element)errNodes.item(0);
			    return (err.getElementsByTagName("messagestatus")
			                          .item(0)
			                          .getTextContent());
			}
			return null;
		}
		catch(Exception e)
		{
			return null;
		}
	}
	
	private static HostnameVerifier getHostnameVerifier() {
        HostnameVerifier hostnameVerifier = new HostnameVerifier() {

			@Override
			public boolean verify(String hosdtname, SSLSession session) {
				// TODO Auto-generated method stub
				HostnameVerifier hv =
                        HttpsURLConnection.getDefaultHostnameVerifier();
                //return true;
				return hv.verify("www.probasesms.com", session);
			}
        };
        return hostnameVerifier;
    }
}
