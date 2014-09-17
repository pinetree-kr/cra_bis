package pinetree.cra.bis;

import pinetree.cra.bis.model.ActivityListModel;
import pinetree.cra.bis.model.BusStateModel;
import pinetree.cra.bis.model.CookieModel;
import pinetree.cra.bis.model.InformationModel;
import pinetree.cra.bis.model.LogInModel;
import pinetree.cra.bis.model.LogOutModel;
import pinetree.cra.bis.model.ServerStateModel;
import pinetree.cra.bis.subclass.HttpAsyncTask;
import pinetree.cra.bis.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
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

public class Cra_bisSettingActivity extends Cra_bisActivity {
	protected EditText inputServerUrl;
	protected Button buttonLogin, buttonConnect;
	protected String user_id, password;
	protected CountDownTimer timer;
	protected CheckBox checkAutoFill, checkAutoLogin;
	protected TextView userName, programVer; 
	protected final int SEC = 1000;

	protected SharedPreferences privatePref;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Window window = getWindow();
		window.requestFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.setting_layout);
		window.setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_layout);
		
		// private preferences �ҷ�����
		privatePref = getPreferences(Activity.MODE_PRIVATE);
		
		textTitleBar = (TextView) findViewById(R.id.textTitleBar);
		textTitleBar.setText(R.string.titleSetting);
		
		// ����
		inputServerUrl = (EditText) findViewById(R.id.inputServerAddr);
		userName = (TextView) findViewById(R.id.textLoginId);
		programVer = (TextView) findViewById(R.id.textProgramVer);
		buttonConnect = (Button) findViewById(R.id.buttonConnect);
		buttonLogin = (Button) findViewById(R.id.buttonLogin);
		
		// ���� URL �ҷ�����
		inputServerUrl.setText(ServerStateModel.getInstance().getUrl());
		
		LogInModel logIn = LogInModel.getInstance();
		
		// �α��� ���� Ȯ��
		if(CookieModel.getInstance().checkSession()){
			userName.setText(logIn.getUserName()+"��");
			buttonLogin.setText(R.string.setting_logoff);
		}else{
			userName.setText(R.string.setting_loginIdNull);			
			buttonLogin.setText(R.string.setting_logon);
		}
		
		// �α��� ��ư�� ������
		buttonLogin.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				
				if(CookieModel.getInstance().checkSession()){
					//�α׾ƿ�
					sendRequest(LogOutModel.getInstance());
				}else{
					//�α������� �̵�
					moveToPreviousActivity();
				}
			}
		});
		
		buttonConnect.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				
				ServerStateModel serverStateModel = ServerStateModel.getInstance();
				serverStateModel.setUrl(inputServerUrl.getText().toString());
				Log.i("DebugPrint","Set Server:"+serverStateModel.getUrl());
				sendRequest(serverStateModel);
			}
		});
		
		programVer.setText(getVersionName());
	}
	
	protected String getVersionName(){
		PackageInfo packageInfo;
		try {
			packageInfo = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
			return packageInfo.versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public void moveToPreviousActivity() {
		// TODO Auto-generated method stub
		Intent intentLoginActivity = new Intent(this, Cra_bisLoginActivity.class);
		startActivity(intentLoginActivity);
		this.finish();
	}

	@Override
	public void moveToNextActivity() {
	}

	@Override
	public void postJob(InformationModel information) {
		// TODO Auto-generated method stub
		if(!information.isError()){
			SharedPreferences.Editor sharedEditorSettings = sharedPrefSettings.edit();
			//SharedPreferences.Editor sharedEditorLogin = sharedPrefLogin.edit();
			
			// ���� �ּ� ����
			sharedEditorSettings.putString("url", ServerStateModel.getInstance().getUrl());
			sharedEditorSettings.commit();
			
			// �α׾ƿ���
			if(information.getClass().equals(LogOutModel.class)){
				//LogOutModel logOut = (LogOutModel) information;
				// �����ʱ�ȭ
				//CookieModel.getInstance().removeCookies();
				//sharedEditorLogin.clear();
				moveToPreviousActivity();
			}else if(information.getClass().equals(ServerStateModel.class)){
				//ServerStateModel serverStateModel = (ServerStateModel) information;
				refreshActivity();
			}
		}
	}
	
	@Override
	public void sendRequest(InformationModel information) {
		// TODO Auto-generated method stub
		
		HttpAsyncTask request = new HttpAsyncTask();
		request.setObject(Cra_bisSettingActivity.this);
		request.execute(information);
	}

	@Override
	public void moveToSettingActivity() {
		// TODO Auto-generated method stub
		
	}
}
