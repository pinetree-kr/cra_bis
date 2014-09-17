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

public class BusStateModel extends InformationModel{
	protected int bus_srl;
	protected String name;
	protected String state;
	protected int volume;
	protected int max_volume;
	protected String position;
	protected int time_srl;
	protected String regtime;
	
	protected String destination;
	protected String thisTime;
	
	protected static BusStateModel busStateModel = new BusStateModel();
	
	protected BusStateModel(){
		bus_srl = 0;
		name = "";
		state = "I";
		volume = 0;
		max_volume = 0;
		position = "";
		time_srl = 0;
		regtime = "";
		destination = "";
	}

	public static BusStateModel getInstance(){
		return busStateModel;
	}
	
	public BusStateModel init(){
		busStateModel = new BusStateModel();
		return this;
	}
	
	public BusStateModel setBusSrl(int srl){
		bus_srl = srl;
		return this;
	}
		
	public BusStateModel setBusName(String busName){
		name = busName;
		return this;
	}
	
	public BusStateModel setBusState(String busState){
		state = busState;
		return this;
	}
	
	public BusStateModel setBusVolume(int busVolume){
		volume = busVolume;
		return this;
	}
	
	public BusStateModel setBusMaxVolume(int busMaxVolume){
		max_volume = busMaxVolume;
		return this;
	}
	
	public BusStateModel setBusPosition(String busPosition){
		position =  busPosition;
		return this;
	}
	
	/*
	public BusStateModel setGPGLL(Location location){
		position =  new GPGLLModel(location).toString();
		return this;
	}
	*/
	
	public BusStateModel setDestination(String dest){
		this.destination = dest;
		return this;
	}
	
	public String getDestinartion(){
		return destination;
	}
	
	public BusStateModel setThisTime(String thisTime){
		this.thisTime = thisTime;
		return this;
	}
	
	public String getThisTime(){
		return thisTime;
	}
	
	public BusStateModel setTimeSrl(int srl){
		time_srl = srl;
		return this;
	}
	
	public BusStateModel setRegTime(String time){
		regtime = time;
		return this;
	}
	
	
	public int getBusSrl(){
		return bus_srl;
	}
	
	public String getBusName(){
		return name;
	}
	
	public String getBusState(){
		return state;
	}
	
	public int getBusVolume(){
		return volume;
	}
	
	public int getBusMaxVolume(){
		return max_volume;
	}
	
	public String getPosition(){
		return position;
	}
	
	public int getTimeSrl(){
		return time_srl;
	}
	
	public String getRegTime(){
		return regtime;
	}
	
	public boolean isStatusOn(){
		if(position != "" && name != "" && state == "A")
			return true;
		else return false;
	}

	public List<NameValuePair> getParameters() {
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("act", "procIndecoxbusUpdateBusState"));
		parameters.add(new BasicNameValuePair("bus_srl", Integer.toString(bus_srl)));
		parameters.add(new BasicNameValuePair("time_srl", Integer.toString(time_srl)));
		parameters.add(new BasicNameValuePair("state", state));
		parameters.add(new BasicNameValuePair("name", name));
		parameters.add(new BasicNameValuePair("volume", Integer.toString(volume)));
		parameters.add(new BasicNameValuePair("position", position));
		parameters.add(new BasicNameValuePair("response_type", "JSON"));
		//Log.i("DebugPrint",parameters.toString());
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
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.i("DebugPrint",e.getMessage());
		}
	}
}
