package pinetree.cra.bis;

import pinetree.cra.bis.model.ActivityListModel;
import pinetree.cra.bis.model.BusStateModel;
import pinetree.cra.bis.model.CookieModel;
import pinetree.cra.bis.model.InformationModel;
import pinetree.cra.bis.model.LogInModel;
import pinetree.cra.bis.subclass.HttpAsyncTask;
import pinetree.cra.bis.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class Cra_bisLoginActivity extends Cra_bisActivity {
	protected EditText inputId, inputPw;
	protected Button buttonLogin;
	protected String user_id, password;
	protected CountDownTimer timer;
	protected CheckBox checkAutoFill, checkAutoLogin;
	
	protected final int SEC = 1000;

	protected SharedPreferences privatePref;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Window window = getWindow();
		window.requestFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.login_layout);
		window.setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_layout);
		
		// private preferences 불러오기
		privatePref = getPreferences(Activity.MODE_PRIVATE);
		
		textTitleBar = (TextView) findViewById(R.id.textTitleBar);
		textTitleBar.setText(R.string.titleLogin);
		
		inputId = (EditText) findViewById(R.id.inputId);
		inputPw = (EditText) findViewById(R.id.inputPw);
		buttonLogin = (Button) findViewById(R.id.buttonLogin);
		
		checkAutoFill = (CheckBox) findViewById(R.id.checkAutoFill);
		checkAutoLogin = (CheckBox) findViewById(R.id.checkAutoLogin);
		
		checkAutoFill();
		
		buttonLogin.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				LogInModel.getInstance().setUserId(inputId.getText().toString())
					.setPassword(inputPw.getText().toString());
				
				if(!LogInModel.getInstance().isValid()){
					Toast.makeText(Cra_bisLoginActivity.this, R.string.empty_loginForm, Toast.LENGTH_SHORT).show();
					return;
				}
				
				sendRequest(LogInModel.getInstance());
			}
		});
		
		setSettingButton();
		
		ActivityListModel.getInstance()
			.push(this);
	}
	
	protected void checkAutoFill(){
		
		boolean isAutoFill, isAutoLogin;
		
		isAutoFill = sharedPrefLogin.getBoolean("auto_fill", true);
		isAutoLogin = sharedPrefLogin.getBoolean("auto_login", false);
		
		if(isAutoFill)
			inputId.setText(privatePref.getString("user_id", ""));
		
		/*
		 * 테스트용 아이디와 패스워드
		 */
		inputId.setText("testdriver2");
		inputPw.setText("testcra2");
		
		
		checkAutoFill.setChecked(isAutoFill);
		checkAutoLogin.setChecked(isAutoLogin);
		
		// Init()
		LogInModel.getInstance().init();
		BusStateModel.getInstance().init();
		CookieModel.getInstance().init();
		
		//로그인 Preferences 초기화
		SharedPreferences.Editor sharedEditor = sharedPrefLogin.edit();
		sharedEditor.clear();
		sharedEditor.commit();
		
		ActivityListModel.getInstance()
			.clear();
	}
	
	public void sendRequest(InformationModel information){
		HttpAsyncTask request = new HttpAsyncTask();
		request.setObject(Cra_bisLoginActivity.this);
		request.execute((LogInModel)information);
	}
	
	public void postJob(InformationModel information){
		LogInModel logIn = (LogInModel) information;
		
		if(!logIn.isError() && logIn.isDriver()){
			SharedPreferences.Editor sharedEditor = sharedPrefLogin.edit();
			SharedPreferences.Editor privateEditor = privatePref.edit();
			
			// auto_login check
			sharedEditor.putBoolean("auto_login", checkAutoLogin.isChecked());
			
			// auto_fill check
			sharedEditor.putBoolean("auto_fill", checkAutoFill.isChecked());
			
			privateEditor.clear();
			if(checkAutoFill.isChecked())
				privateEditor.putString("user_id", logIn.getUserId());
			privateEditor.commit();
			
			sharedEditor.putString("user_id", logIn.getUserId());
			sharedEditor.putString("user_pw", logIn.getUserPw());
			sharedEditor.putString("user_name", logIn.getUserName());
			sharedEditor.putInt("bus_srl", logIn.getBusSrl());
			sharedEditor.putString("session_key", CookieModel.getInstance().getCookies());
			sharedEditor.putLong("session_time", CookieModel.getInstance().getSessionTime());
			sharedEditor.commit();
			
			BusStateModel.getInstance()
				.setBusSrl(logIn.getBusSrl());
			moveToNextActivity();
		}else if(logIn.getResponseCode()>=400){
			//서버관련에러. setting activity로 이동
			this.moveToSettingActivity();
		}else{
			;
		}
	}
	
	@Override
	public void onBackPressed(){
		// TODO Auto-generated method stub
		
		moveToPreviousActivity();
	}
	
	@Override
	public void moveToPreviousActivity(){
		ActivityListModel.getInstance()
			.pop().finish();		
	}

	@Override
	public void moveToNextActivity() {
		// TODO Auto-generated method stub
		Intent intentDestinationActivity = new Intent(this, Cra_bisDestinationActivity.class);
		startActivity(intentDestinationActivity);
		
		ActivityListModel.getInstance()
			.pop().finish();
	}
}
