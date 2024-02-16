package com.probase.reservationticketingwebservice.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.security.auth.login.LoginException;
import javax.ws.rs.core.Response;

import org.json.JSONException;
import org.json.JSONObject;

import com.probase.reservationticketingwebservice.authenticator.Authenticator;
import com.probase.reservationticketingwebservice.authenticator.PaymentFunction;

public class Test2 {

	/**
	 * @param args
	 * @throws ParseException 
	 * @throws JSONException 
	 * @throws LoginException 
	 */
	public static void main(String[] args) throws ParseException, JSONException, LoginException {
		// TODO Auto-generated method stub
		Authenticator at = Authenticator.getInstance();
		/*String jsonObjectStr1 = (String)(at.login("nrfaadmin@probasegroup.com", "password", "NRFA", Boolean.FALSE, "dasdadsadwe2322r2r2r2rr2rasda", "2342424222323243223442dd").getEntity());
		System.out.println(jsonObjectStr1);
		JSONObject userData1 = new JSONObject(jsonObjectStr1);
		String token1 = userData1.getString("token");
		System.out.println(token1);*/
		
		PaymentFunction pf = PaymentFunction.getInstance();
		//Response rs = pf.listTransactions("031", null, null, null, null, null, null, null, null, null, "2017-09-21", "2017-09-23", null, null, null, 0, 50, token1, "123443");
		
		//System.out.println((rs.getEntity().toString()));

	}

}
