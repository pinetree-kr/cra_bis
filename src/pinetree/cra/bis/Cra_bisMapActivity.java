package pinetree.cra.bis;

import pinetree.cra.bis.model.Cra_bisActivityInterface;
import pinetree.cra.bis.model.InformationModel;

import android.app.Activity;
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

import com.google.android.maps.MapActivity;

public abstract class Cra_bisMapActivity extends MapActivity implements Cra_bisActivityInterface{
	protected SharedPreferences sharedPrefAutoLogin, sharedPrefSettings;
	protected TextView textTitleBar;
	protected ProgressBar progressTitleBar;
	
	public void onCreate(Bundle savedInstanceState) {
		sharedPrefAutoLogin = getSharedPreferences("BIS_AutoLogin",Activity.MODE_PRIVATE);
		sharedPrefSettings = getSharedPreferences("BIS_Settings",Activity.MODE_PRIVATE);
		
		overridePendingTransition(0,0);
		super.onCreate(savedInstanceState);
	}
	
	/*
	 * GPS 상태를 보고 Toggle
	 */
	/*
	public void GPSStatusToggle(boolean toggle){
		LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		
		if(toggle){
			if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
				Toast.makeText(Cra_bisMapActivity.this, R.string.gps_on, Toast.LENGTH_SHORT).show();
				Intent intent=new Intent("android.location.GPS_ENABLED_CHANGE");
				intent.putExtra("enabled", toggle);
				sendBroadcast(intent);
			}
		}else{
			if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
				Toast.makeText(Cra_bisMapActivity.this, R.string.gps_off, Toast.LENGTH_SHORT).show();
				Intent intent=new Intent("android.location.GPS_ENABLED_CHANGE");
				intent.putExtra("enabled", toggle);
				sendBroadcast(intent);
			}
		}
	}
	*/
	
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
