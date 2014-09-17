package pinetree.cra.bis.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.util.Log;

public class BusTimeListModel extends InformationModel{
	protected transient String destination;
	
	protected ArrayList<BusTimeModel> busTimeList;
	
	public BusTimeListModel(){
		busTimeList = new ArrayList<BusTimeModel>();
	}
	
	public ArrayList<BusTimeModel> getBusTimeList(){
		return busTimeList;
	}
	
	public BusTimeListModel setDestination(String dest){
		destination = dest;
		return this;
	}

	public List<NameValuePair> getParameters() {
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("act", "getIndecoxbusTimeList"));
		parameters.add(new BasicNameValuePair("destination", destination));
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
				//message_type = response.getString("message_type");
				
				if(error!=0)
					break;
				
				JSONArray jaBustime = new JSONArray(response.getString("bustime"));
				for(int j=0; j<jaBustime.length(); j++){
					JSONObject bustime = jaBustime.getJSONObject(j);
					BusTimeModel busTimeInfo = new BusTimeModel();
					busTimeInfo.setTimeSrl(bustime.getInt("time_srl"))
							.setPoint1(bustime.getString("point1"))
							.setPoint2(bustime.getString("point2"))
							.setPoint3(bustime.getString("point3"))
							.setPoint4(bustime.getString("point4"))
							.setPoint5(bustime.getString("point5"))
							.setStartTime(bustime.getString("start_time"))
							.setDestination(bustime.getString("destination"))
							.setEtc(bustime.getString("etc"));
					busTimeList.add(busTimeInfo);
				}
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.i("DebugPrint",e.getMessage());
		}
	}
}
