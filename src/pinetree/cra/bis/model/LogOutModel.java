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

public class LogOutModel extends InformationModel{
	protected static LogOutModel logOutModel = new LogOutModel();
	
	protected LogOutModel(){}
	
	public static LogOutModel getInstance(){
		return logOutModel;
	}
	
	public List<NameValuePair> getParameters() {
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("act", "procIndecoxbusLogOut"));
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
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
			Log.i("DebugPrint",e.getMessage());
		}
	}
}
