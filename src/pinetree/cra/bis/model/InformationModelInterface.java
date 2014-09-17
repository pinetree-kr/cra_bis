package pinetree.cra.bis.model;

import java.util.List;

import org.apache.http.NameValuePair;


public interface InformationModelInterface {
	
	public List<NameValuePair> getParameters();
	//public Information setResponseData(String data);
	public InformationModel setResponseCode(int code);
	//public String getResponseData();
	public int getResponseCode();
	
	public void setFields(String json);
}
