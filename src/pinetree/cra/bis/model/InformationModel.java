package pinetree.cra.bis.model;

import java.util.List;


public abstract class InformationModel implements InformationModelInterface{
	protected String message;
	//protected String message_type;
	protected int error = -1;
	
	//protected final transient String url = "http://cra16.handong.edu/i7/index.php";
	protected transient int responseCode;
	//protected transient String responseData;
	
	public boolean isError(){
		if(error!=0)
			return true;
		else return false;
	}
	
	public InformationModel setError(int isError){
		error = isError;
		return this;
	}
	
	public InformationModel setMessage(String getMessage){
		message = getMessage;
		return this;
	}
	
	/*
	public String getMessageType(){
		return message_type;
	}
	
	public String getUrl(){
		return url;
	}
	*/
	public String getMessage(){
		return message;
	}
	
	/*
	public Information setResponseData(String data){
		responseData = data;
		return this;
	}
	*/
	public InformationModel setResponseCode(int code){
		responseCode = code;
		return this;
	}
	
	/*
	public String getResponseData(){
		return responseData;
	}
	*/
	
	public int getResponseCode(){
		return responseCode;
	}
	
}
