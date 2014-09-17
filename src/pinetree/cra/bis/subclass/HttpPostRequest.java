package pinetree.cra.bis.subclass;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import pinetree.cra.bis.model.BusStateModel;
import pinetree.cra.bis.model.CookieModel;
import pinetree.cra.bis.model.InformationModel;
import pinetree.cra.bis.model.ServerStateModel;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

public class HttpPostRequest{
	protected static HttpURLConnection connection;
	
	
	public static InformationModel httpPostRequest(InformationModel objInfo){
		//SharedPreferences sharedPref;
		//sharedPref = getSharedPreferences("BIS_AutoLogin",Activity.MODE_PRIVATE);
		
		UrlEncodedFormEntity entity;
		List<NameValuePair> parameters = objInfo.getParameters();	

		//HttpURLConnection connection;
		
		try {
			connection = (HttpURLConnection) new URL(ServerStateModel.getInstance().getUrl()).openConnection();
			
			//Log.i("DebugPrint", "connection:"+connection.toString());
			
			System.setProperty("http.keepAlive", "false");
			connection.setDoInput(true);
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);
			
			// for JDK 1.5 or Higher
			if(objInfo.getClass().equals(BusStateModel.class)){
				connection.setConnectTimeout(2000);
				connection.setReadTimeout(2000);
			}else{
				connection.setConnectTimeout(8000);
				connection.setReadTimeout(8000);				
			}
			
			if(CookieModel.getInstance().checkSession() && !objInfo.getClass().equals(ServerStateModel.class)){
				Log.i("DebugPrint","Update Cookie:"+CookieModel.getInstance().getCookies());
				// set cookie on request
				connection.setRequestProperty("cookie", CookieModel.getInstance().getCookies());
			}
			
			OutputStreamWriter streamWriter = new OutputStreamWriter(connection.getOutputStream());
			
			entity = new UrlEncodedFormEntity(parameters,HTTP.UTF_8);
			
			streamWriter.write(EntityUtils.toString(entity));
			streamWriter.flush();
			
			// Get Response Code
			objInfo.setResponseCode(connection.getResponseCode());
			//Log.i("DebugPrint","ResponseCode:"+connection.getResponseCode());
			
			// Check Session and Expired
			if(!CookieModel.getInstance().checkSession() && !objInfo.getClass().equals(ServerStateModel.class)){
				// Get Cookies by Header
				Map<String, List<String>> iMap = connection.getHeaderFields();
				if(iMap.containsKey("Set-Cookie")){
					List<String> lString = iMap.get("Set-Cookie");
					String newCookies = "";
					for(int i = 0; i<lString.size(); i++)
						newCookies += lString.get(i);
					
					// new cookie
					CookieModel.getInstance()
						.setCookies(newCookies, System.currentTimeMillis());
					
					Log.i("DebugPrint","New Cookies:"+CookieModel.getInstance().getCookies());
					
				}
			}
			
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream(),"UTF-8"));
			
			String lineBuffer = null;
			String response = "";
			
			while((lineBuffer = bufferedReader.readLine())!=null){
				response += lineBuffer;
			}
			
			//Log.i("DebugPrint","response:"+response);
			objInfo.setFields("["+response+"]");
			
			streamWriter.close();
			bufferedReader.close();
			
			connection.disconnect();
			
			//objInfo.setFields(response);
			return objInfo;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			//Log.i("DebugTest", e.getMessage());
			objInfo.setError(-1);
			objInfo.setMessage("MalformedURLException\n" + e.getMessage());
			return objInfo;
		} catch (IOException e) {
			e.printStackTrace();
			//Log.i("DebugTest",e.getMessage());
			objInfo.setError(-1);
			objInfo.setMessage("IOException\n" + "네트워크 연결이 실패했습니다\n네트워크 상태를 확인후 다시 시도해주세요");
			
			return objInfo;
		}
	}
}
