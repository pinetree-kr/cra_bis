package pinetree.cra.bis;

import pinetree.cra.bis.model.BusStateModel;
import pinetree.cra.bis.model.CookieModel;
import pinetree.cra.bis.model.InformationModel;
import pinetree.cra.bis.model.LogInModel;
import pinetree.cra.bis.model.ServerStateModel;
import pinetree.cra.bis.subclass.HttpAsyncTask;
import pinetree.cra.bis.R;
import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

public class Cra_bisSplashActivity extends Cra_bisActivity{
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.splash_layout);
		
		checkServerState();
	}
	
	protected void checkServerState(){
		ServerStateModel serverStateModel = ServerStateModel.getInstance(); 
		serverStateModel.setUrl(sharedPrefSettings.getString("url", "http://cra16.handong.edu/i7/index.php"));
		
		sendRequest(serverStateModel);
	}
	
	/*
	 * 시나리오
	 * 로그인체크 : true - login skip
	 * 		    false - login
	 */
	protected boolean checkLogin(){
		
		CookieModel cookieModel = CookieModel.getInstance();
		cookieModel.setCookies(sharedPrefLogin.getString("session_key", ""), sharedPrefLogin.getLong("session_time", 0));
		
		// cookie check
		if(cookieModel.checkSession()){
			LogInModel logIn = LogInModel.getInstance()
					.setUserId(sharedPrefLogin.getString("user_id", ""))
					.setPassword(sharedPrefLogin.getString("user_pw", ""))
					.setBusSrl(sharedPrefLogin.getInt("bus_srl", 0));
			
			if(logIn.getBusSrl()>0){
				BusStateModel.getInstance()
						.setBusSrl(sharedPrefLogin.getInt("bus_srl", 0));				

				/*
				Log.i("DebugPrint",
						"id:" + logIn.getUserId() + 
						"\npw:" + logIn.getUserPw() + 
						"\nbus_srl:" + logIn.getBusSrl() + 
						"\nsession_key:" + cookieModel.getCookies());
				*/
				return true;
			}
		}else{
			// auto_login check
			if(sharedPrefLogin.getBoolean("auto_login", false)){
				LogInModel.getInstance()
						.setUserId(sharedPrefLogin.getString("user_id", ""))
						.setPassword(sharedPrefLogin.getString("user_pw", ""));
				
				return true;
			}
		}
		return false;
	}
	
	
	@Override
	public void postJob(InformationModel information) {
		//로그인시
		if(information.getClass().equals(LogInModel.class)){
			LogInModel logIn = (LogInModel) information;
			
			if(!logIn.isError() && logIn.isDriver()){
				BusStateModel.getInstance()
				.setBusSrl(logIn.getBusSrl());
				
				this.skipToLoginActivity();
			}else{
				this.moveToNextActivity();
			}			
		}

		//서버확인시
		if(information.getClass().equals(ServerStateModel.class)){
			ServerStateModel serverStateModel = (ServerStateModel) information;
			
			if(serverStateModel.isError()){
				this.moveToNextActivity();
				
				this.moveToSettingActivity();
				this.finish();
			}else{
				
				if(!checkLogin()){
					Handler handler = new Handler();
					handler.postDelayed(new splashHandler(), 2000);
				}else{
					sendRequest(LogInModel.getInstance());
				}
			}
		}
	}

	class splashHandler implements Runnable{

		public void run() {
			moveToNextActivity();
		}
	}
	
	@Override
	public void sendRequest(InformationModel information) {
		// TODO Auto-generated method stub
		HttpAsyncTask request = new HttpAsyncTask();
		request.setObject(Cra_bisSplashActivity.this);
		request.execute(information);
	}

	@Override
	public void moveToPreviousActivity() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void moveToNextActivity() {
		// TODO Auto-generated method stub
		Intent intentLoginActivity = new Intent(this, Cra_bisLoginActivity.class);
		startActivity(intentLoginActivity);
		
		this.finish();
	}
	
	public void skipToLoginActivity() {
		// TODO Auto-generated method stub
		Intent intentDestinationActivity = new Intent(this, Cra_bisDestinationActivity.class);
		startActivity(intentDestinationActivity);
		this.finish();
	}
}
