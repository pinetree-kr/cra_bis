package pinetree.cra.bis.subclass;

public class XmlResponse {
	private boolean error;
	private String message;
	
	public XmlResponse(){
		error = false;
		message = "";
	}
	
	public XmlResponse setError(boolean arg0){
		error = arg0;
		return this;
	}

	public XmlResponse setMessage(String arg0){
		message = arg0;
		return this;
	}
	
	public boolean isError(){
		return error;
	}
	
	public String getMessage(){
		return message;
	}
}
