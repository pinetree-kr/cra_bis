package pinetree.cra.bis.model;

import android.util.Log;

public class CookieModel {
	protected static CookieModel cookieModel = new CookieModel();
	protected String cookies;
	protected boolean has_session;
	protected final long sessionLimitTime = 5 * 60 * 60 * 1000; //5시간
	protected long sessionTime;
	
	protected CookieModel(){
		cookies = "";
		has_session = false;
		sessionTime = 0;
	}
	
	public static CookieModel getInstance(){
		return cookieModel;
	}
	
	public CookieModel init(){
		cookieModel = new CookieModel();
		return this;
	}
	
	public String getCookies(){
		return cookies;
	}
	
	public long getSessionTime(){
		return sessionTime;
	}
	
	public CookieModel setCookies(String newCookies){
		cookies = newCookies;
		//sessionTime = System.currentTimeMillis();
		if(newCookies.equals(""))
			has_session = false;
		else
			has_session = true;
		
		return this;
	}

	public CookieModel setCookies(String newCookies, long newSessionTime){
		setCookies(newCookies);
		sessionTime = newSessionTime;
		return this;
	}
	
	public CookieModel removeCookies(){
		cookies = "";
		has_session = false;
		sessionTime = 0;
		return this;
	}
	
	// 세션 갱신
	public boolean checkSession(){
		if(!has_session)
			return false;
		//Log.i("DebugPrint",System.currentTimeMillis() + "\n"+ (sessionTime + sessionLimitTime));
		if(System.currentTimeMillis() < sessionTime + sessionLimitTime){
			sessionTime = System.currentTimeMillis();
			return true;
		}else{
			has_session = false;
			return false;
		}
	}
	
	public CookieModel hasSession(boolean flag){
		has_session = flag;
		return this;
	}
}
