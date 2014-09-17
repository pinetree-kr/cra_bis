package pinetree.cra.bis.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;


public class DestinationListModel extends InformationModel{
	protected ArrayList<DestinationModel> destinationList;
	
	protected transient String text;
	
	public DestinationListModel(){
		destinationList = new ArrayList<DestinationModel>();
	}
	
	public ArrayList<DestinationModel> getDestinationList(){
		return destinationList;
	}
	
	public DestinationListModel setText(String str){
		text = str;
		return this;
	}
	
	public List<NameValuePair> getParameters() {
		// TODO Auto-generated method stub
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("act", "getIndecoxbusDestinationListAddon"));
		parameters.add(new BasicNameValuePair("response_type", "JSON"));
		return parameters;
	}

	public void setFields(String json) {
		// TODO Auto-generated method stub
		try {
			JSONArray jaResponse = new JSONArray(json);
			for(int i=0; i<jaResponse.length(); i++){
				JSONObject response = jaResponse.getJSONObject(i);
				
				error = response.getInt("error");
				message = response.getString("message");
				if(error!=0)
					break;
				
				JSONArray jaDestination = new JSONArray(response.getString("destination"));
				for(int j=0; j<jaDestination.length(); j++){
					JSONObject destination = jaDestination.getJSONObject(j);
					DestinationModel destinationInfo = new DestinationModel();
					destinationInfo.setPoint1(destination.getString("point1"))
							.setPoint2(destination.getString("point2"))
							.setPoint3(destination.getString("point3"))
							.setPoint4(destination.getString("point4"))
							.setPoint5(destination.getString("point5"))
							.setDestinationType(destination.getString("destination_type"))
							.setDestinationSubType(destination.getString("destination_subtype"))
							.setDestinationSubName(destination.getString("destination_subname"))
							.setDestinationName(destination.getString("destination_name"));
					destinationList.add(destinationInfo);
				}
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
			Log.i("DebugPrint",e.getMessage());
		}
	}
}
