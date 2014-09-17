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

public class BusStateListModel extends InformationModel{
	//private transient String state;
	//private transient int time_srl;
	
	private List<BusStateModel> busStateList;
	
	public BusStateListModel(){
		busStateList = new ArrayList<BusStateModel>();
	}

	public List<BusStateModel> getBusStateList(){
		return busStateList;
	}
	
	/*
	public BusStateListInfo setBusState(String busState){
		state = busState;
		return this;
	}
	
	public BusStateListInfo setTimeSrl(int srl){
		time_srl = srl;
		return this;
	}

	public String getBusState(){
		return state;
	}
	
	public int getTimeSrl(){
		return time_srl;
	}
	*/
	public List<NameValuePair> getParameters() {
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("act", "getIndecoxbusStateList"));
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
				//message_type = response.getString("message_type");
				
				if(error!=0)
					break;
				
				JSONArray jaBus= new JSONArray(response.getString("bus"));
				for(int j=0; j<jaBus.length(); j++){
					JSONObject busState = jaBus.getJSONObject(j);
					BusStateModel busStateInfo = new BusStateModel();
					busStateInfo.setBusSrl(busState.getInt("bus_srl"))
							.setBusName(busState.getString("name"))
							.setBusState(busState.getString("state"))
							.setBusVolume(busState.getInt("volume"))
							.setBusMaxVolume(busState.getInt("max_volume"))
							.setBusPosition(busState.getString("position"))
							//.setTimeSrl(Integer.parseInt((busState.getString("time_srl"))))
							.setRegTime(busState.getString("regtime"));
					busStateList.add(busStateInfo);
				}
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.i("DebugPrint",e.getMessage());
		}
	}
}
