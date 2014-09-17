package pinetree.cra.bis.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.location.Location;
import android.util.Log;

public class ServerStateModel extends InformationModel{
	
	protected String url;
	
	protected static ServerStateModel serverStateModel = new ServerStateModel();
	
	protected ServerStateModel(){}

	public static ServerStateModel getInstance(){
		return serverStateModel;
	}
	
	public ServerStateModel setUrl(String newUrl){
		url = newUrl;
		return this;
	}
	
	public String getUrl(){
		return url;
	}

	public List<NameValuePair> getParameters() {
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("act", "getIndecoxbusServerState"));
		parameters.add(new BasicNameValuePair("response_type", "JSON"));
		return parameters;
	}

	public void setFields(String json) {
		try {
			JSONArray jaResponse = new JSONArray(json);
			for(int i=0; i<jaResponse.length(); i++){
				JSONObject response = jaResponse.getJSONObject(i);
				
				error = response.getInt("error");
				//Log.i("DebugPrint","error:"+error);
				message = response.getString("message");
				//Log.i("DebugPrint","message:"+message);
				//message_type = response.getString("message_type");
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.i("DebugPrint",e.getMessage());
		}
	}
}
