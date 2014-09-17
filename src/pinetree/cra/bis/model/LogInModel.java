package pinetree.cra.bis.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pinetree.cra.bis.Cra_bisGPSActivity;


import android.util.Log;
import android.widget.Toast;

public class LogInModel extends InformationModel{
	protected String user_id;
	protected String password;
	protected String user_name;
	protected int bus_srl;
	//protected String session_key;
	protected static LogInModel logInModel = new LogInModel();
	
	protected LogInModel(){
		user_id = "";
		user_name = "";
		password = "";
		bus_srl = 0;
	}
	
	public static LogInModel getInstance(){
		return logInModel;
	}
	
	public LogInModel init(){
		logInModel = new LogInModel();
		return this;
	}
	
	public LogInModel setUserName(String name){
		user_name = name;
		return this;
	}
	
	public String getUserName(){
		return user_name;
	}
	
	public LogInModel setUserId(String id){
		user_id = id;
		return this;
	}

	public LogInModel setPassword(String pw){
		password = pw;
		return this;
	}
	
	public String getUserId(){
		return user_id;
	}

	public String getUserPw(){
		return password;
	}
	
	public LogInModel setBusSrl(int srl){
		bus_srl = srl;
		return this;
	}
	
	public int getBusSrl(){
		return bus_srl;
	}
	public boolean isDriver(){
		if(bus_srl>0)
			return true;
		else return false;
	}
	
	public boolean isValid(){
		if(user_id.trim().equals("") || password.trim().equals(""))
			return false;
		else return true;
	}
	
	public List<NameValuePair> getParameters() {
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("act", "procIndecoxbusLogIn"));
		parameters.add(new BasicNameValuePair("user_id", user_id));
		parameters.add(new BasicNameValuePair("password", password));
		parameters.add(new BasicNameValuePair("response_type", "JSON"));
		return parameters;
	}

	public void setFields(String json) {
		try {
			JSONArray jaResponse = new JSONArray(json);
			for(int i=0; i<jaResponse.length(); i++){
				JSONObject response = jaResponse.getJSONObject(i);
				
				error = response.getInt("error");
				message = response.getString("message");
				if(error!=0)
					break;
				
				
				JSONArray jaDriver = new JSONArray("["+response.getString("driver")+"]");
				for(int j=0; j<jaDriver.length(); j++){
					JSONObject driver = jaDriver.getJSONObject(j);
					this.setBusSrl(driver.getInt("bus_srl"));
					this.setUserName(driver.getString("name"));
				}
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
			Log.i("DebugPrint",e.getMessage());
		}
	}
}
