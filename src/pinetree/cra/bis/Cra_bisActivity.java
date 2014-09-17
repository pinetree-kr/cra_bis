package pinetree.cra.bis;

import pinetree.cra.bis.model.Cra_bisActivityInterface;
import pinetree.cra.bis.model.InformationModel;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public abstract class Cra_bisActivity extends Activity implements Cra_bisActivityInterface{
	protected SharedPreferences sharedPrefLogin, sharedPrefSettings;
	protected TextView textTitleBar;
	protected ProgressBar progressTitleBar;
	
	public void onCreate(Bundle savedInstanceState) {
		sharedPrefLogin = getSharedPreferences("BIS_Login",Activity.MODE_PRIVATE);
		sharedPrefSettings = getSharedPreferences("BIS_Settings",Activity.MODE_PRIVATE);
		
		overridePendingTransition(0,0);
		super.onCreate(savedInstanceState);
	}
	
	public abstract void postJob(InformationModel information);
	public abstract void sendRequest(InformationModel information);
	public abstract void moveToPreviousActivity();
	public abstract void moveToNextActivity();
	
	public void setSettingButton(){
		Button buttonSetting = (Button) findViewById(R.id.buttonSetting);
		buttonSetting.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				// TODO Auto-generated method stub
				moveToSettingActivity();
			}
		});
	}
	
	public void moveToSettingActivity() {
		Intent intentSettingActivity = new Intent(this, Cra_bisSettingActivity.class);
		startActivity(intentSettingActivity);
		overridePendingTransition(0,0);
	}
	public void showProgressTitleBar(boolean show){
		progressTitleBar = (ProgressBar) findViewById(R.id.progressTitleBar);
		if(show)
			progressTitleBar.setVisibility(View.VISIBLE);
		else
			progressTitleBar.setVisibility(View.INVISIBLE);
	}
	
	public void refreshActivity(){
		Intent thisActivity = this.getIntent();
		this.finish();
		this.startActivity(thisActivity);
		overridePendingTransition(0,0);
	}
}
