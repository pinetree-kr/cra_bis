package pinetree.cra.bis;

import pinetree.cra.bis.model.ActivityListModel;
import pinetree.cra.bis.model.BusStateModel;
import pinetree.cra.bis.model.GPGLLModel;
import pinetree.cra.bis.model.InformationModel;
import pinetree.cra.bis.subclass.HttpAsyncTask;
import pinetree.cra.bis.subclass.MyLocationOverlayForGPS;
import pinetree.cra.bis.subclass.OverlayItems;
import pinetree.cra.bis.R;

import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

@SuppressLint("HandlerLeak")
public class Cra_bisGPSActivity extends Cra_bisMapActivity{
	protected MapView mapView;
	protected LocationManager myLocationManager;
	protected Location myLocation = null;
	//protected double mLat = 0, mLon = 0, mAcc = 2500;
	public OverlayItems mOverlayItems;
	protected HttpAsyncTask request;
	protected MyLocationOverlayForGPS myLocationOverlay;
	protected int errorCount = 0;
	
	protected final int SEC = 1000;
	protected final int MIN = 60;
	protected final int HOUR = 60;

	protected CountDownTimer timer;
	protected Criteria criteria;
	protected String provider;
	protected int time_srl;
	
	protected static ToggleButton toggleButton;
	protected static TextView textView;
	protected RadioGroup volumeButton;
	
	protected boolean isBackPressed = false;
	protected boolean isTimerOn = false;
	//protected Handler myHandler;
	//protected MyLocationListener myLocationListener;
	
	protected PowerManager.WakeLock wakeLock;
	protected boolean isToggleOn;
	
	
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Window window = getWindow();
		window.requestFeature(Window.FEATURE_CUSTOM_TITLE);
		
		setContentView(R.layout.gps_layout);
		
		window.setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_layout);
		
		textTitleBar = (TextView) findViewById(R.id.textTitleBar);
		textTitleBar.setText(R.string.titleGPS);
		
		TextView textBusInfo = (TextView) findViewById(R.id.textBusInfo); 
		String strBusInfo = BusStateModel.getInstance().getDestinartion()+" "+BusStateModel.getInstance().getThisTime()+" 출발 버스입니다";
		textBusInfo.setText(strBusInfo);
		
		textView = (TextView) findViewById(R.id.textStatus);
		
		//myLocationListener = new MyLocationListener();
		request = null;
		
		// backup the time_srl
		time_srl = BusStateModel.getInstance().getTimeSrl();
		
		isToggleOn = false;
		toggleButton = (ToggleButton) findViewById(R.id.toggleButton1);
		toggleButton.setOnClickListener(new MyClickListener());
		
		volumeButton = (RadioGroup) findViewById(R.id.radioVolumeGroup);
		volumeButton.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch(checkedId){
				case R.id.radioEmptyBus:
					BusStateModel.getInstance().setBusVolume(0);
					break;
				case R.id.radioHalfBus:
					BusStateModel.getInstance().setBusVolume(45);
					break;
				case R.id.radioFullBus:
					BusStateModel.getInstance().setBusVolume(65);
					break;
				}
			}
		});
		
		/*
		// Handling When Pressed Back Button
		myHandler = new Handler(){
			@Override
			public void handleMessage(Message message){
				if(message.what == 0)
					isBackPressed = false;
			}
		};
		*/
		
		myLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		
		/*
		 * Get any GPS provider
		 */
		
		criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setPowerRequirement(Criteria.NO_REQUIREMENT);
		
		provider = myLocationManager.getBestProvider(criteria, true);
		
		Log.i("DebugPrint","provider:"+provider);
		if(provider==null || provider.equals(""))
			provider = LocationManager.GPS_PROVIDER;
		
		//activateGpsToggleButton(false);
		
		myLocation = myLocationManager.getLastKnownLocation(provider);
		
		timer = new CountDownTimer(2 * HOUR * MIN * SEC, 2 * SEC){

			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				if(toggleButton.isChecked())
					toggleButton.setChecked(false);
				
				toggleGps(false);
			}

			@Override
			public void onTick(long millisUntilFinished) {
				// TODO Auto-generated method stub
				if(myLocationOverlay.getAccuracy()<myLocationOverlay.getAccuracyLimit()){
					//BusStateModel.getInstance().setBusPosition(GPGLLModel.getInstance().getGPGLL());
					sendRequest(BusStateModel.getInstance().setBusPosition(GPGLLModel.getInstance().getGPGLL()));
					errorCount = 0;
				}else if(errorCount>30){
					Toast.makeText(Cra_bisGPSActivity.this, "30초 이상 전송이 이루어지지 않았습니다\nGPS나 네트워크 상태를 확인해주세요", Toast.LENGTH_LONG).show();
					errorCount = 0;
				}else{
					errorCount+=2;
				}
			}
		};
		
		/*
		 * Map View
		 */
		mapView = (MapView) findViewById(R.id.mapView);
		//mapView.getController().setCenter(getGeoPoint(myLocation));
		//mapView.getController().setZoom(18);
		mapView.setBuiltInZoomControls(true);
		
		/*
		 * My Overlay
		 */
		
		myLocationOverlay = new MyLocationOverlayForGPS(this, mapView);
		myLocationOverlay.enableMyLocation();
		myLocationOverlay.enableCompass();
		
		if(myLocationOverlay.isMyLocationEnabled()){
			myLocationOverlay.runOnFirstFix(new Runnable(){
				public void run() {
					// TODO Auto-generated method stub
					mapView.getController().setZoom(18);
					mapView.getController().animateTo(myLocationOverlay.getMyLocation());
				}
			});
			
		}else{
			Toast.makeText(Cra_bisGPSActivity.this, "위치정보를 확인 할 수 없습니다\nGPS나 네트워크 상태를 확인해주세요", Toast.LENGTH_SHORT).show();
			moveToPreviousActivity();
		}
		
		mapView.getOverlays().add(myLocationOverlay);
		
		//myLocationManager.requestLocationUpdates(provider, 0, 0, myLocationListener);
		//myLocationManager.requestLocationUpdates(provider, 1000, 0.05f, myLocationListener);
		//myLocationManager.requestLocationUpdates(provider, 1000, 2, myLocationListener);
		
		PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
		wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "PowerManager.WakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK)");
		
		setSettingButton();
		ActivityListModel.getInstance()
			.push(this);
	}
	
	@Override
	public void onResume(){
		super.onResume();
		wakeLock.acquire();
	}
	
	@Override
	public void onPause(){
		super.onPause();
		wakeLock.release();
	}
	
	protected void setRequestForStart(){
		
		//String position = GPGLLModel.getInstance().getGPGLL();
		//Log.i("DebugPrint",position);
		BusStateModel.getInstance()
			.setBusState("A")
			.setTimeSrl(time_srl)
			.setBusPosition(GPGLLModel.getInstance().getGPGLL());
	}

	protected void setRequestForStop(){
		BusStateModel.getInstance()
			.setBusState("I")
			.setTimeSrl(0)
			.setBusPosition("")
			.setBusVolume(0);		
	}
	
	public void postJob(InformationModel information){
		//Log.i("DebugPrint","GPS Test");
	}

	public void sendRequest(InformationModel information){
		request = new HttpAsyncTask();
		request.setObject(Cra_bisGPSActivity.this);
		request.execute((BusStateModel)information);
	}
	
	static public void setTextForGPS(String text, String color){
		textView.setText(text);
		textView.setTextColor(Color.parseColor(color));
	}
	
	static public void activateGpsToggleButton(boolean activate){
		toggleButton.setEnabled(activate);
		toggleButton.setFocusable(activate);
	}
	
	/*
	 * get new Location
	 */
	/*
	protected void updateWithNewLocation(Location location){
		
		if(location != null){
			Log.i("DebugPrint","NewLocation");
			mLat = location.getLatitude();
			mLon = location.getLongitude();
			mAcc = location.getAccuracy();
			
			if(mAcc<50){
				BusStateModel.getInstance().setGPGLL(location);
				
				toggleButton.setEnabled(true);
				toggleButton.setFocusable(true);
			}
			
			String latLon = "Latitude : " + mLat + "\n"
						+ "Longitude : " + mLon + "\n"
						+ "Accuracy : " + mAcc;
			textView.setText(latLon);
		}else{
			//Log.i("DebugPrint","NewProvider");
			String disabled = "위치 정보를 수집할 수 없습니다";
			textView.setText(disabled);
		}
		
		
	}
	*/
	@Override
	public void onBackPressed(){
		// TODO Auto-generated method stub
		
		/*
		if(!isBackPressed){
			Toast.makeText(Cra_bisGPSActivity.this, "뒤로 버튼을 한 번 더 누르시면\n현재 작업을 중단하고 이전으로 돌아갑니다", Toast.LENGTH_SHORT).show();
			isBackPressed = true;
			
			// delay 2sec
			myHandler.sendEmptyMessageDelayed(0, 2 * SEC);
		}else{
			moveToPreviousActivity();
		}
		*/
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		String message;
		if(isToggleOn)
			message = "현재 작업을 중단하고 이전으로 돌아갑니다";
		else
			message = "이전으로 돌아갑니다";
		
		builder.setMessage(message)
			.setCancelable(false)
			.setPositiveButton(R.string.button_yes,
				new DialogInterface.OnClickListener(){
					public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					//pressedButton = true;
						moveToPreviousActivity();
					}
			})
			.setNegativeButton(R.string.button_no,
				new DialogInterface.OnClickListener(){	
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						
					}
			});
		AlertDialog alertDialog = builder.create();
		alertDialog.show();
	}
	
	public void setTextView(String text){
		textView.setText(text);
	}

	@Override
	public void moveToPreviousActivity() {
		// TODO Auto-generated method stub
		//myLocationManager.removeUpdates(myLocationListener);
		
		// GPS 수신 종료
		myLocationOverlay.disableMyLocation();
		
		toggleGps(false);
		
		/*
		 * todo : 마지막 송신이 제대로 안이루어졌을 경우의 처리(postjob에서 처리하도록 한다)
		 * - howto
		 * 	1. 종료를 취소한다.
		 * 	2. 반복한다.
		 * 	3. 메시지를 띄운후  1~2를 선택하도록 한다.
		 */
		ActivityListModel.getInstance()
			.pop().finish();
		overridePendingTransition(0,0);
	}

	@Override
	public void moveToNextActivity() {
		// TODO Auto-generated method stub
		
	}
	
	/*
	public class MyLocationListener implements LocationListener{
		
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			Log.i("DebugPrint","location changed");
			updateWithNewLocation(location);
		}
		
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			Log.i("DebugPrint","provider disabled");
			updateWithNewLocation(null);
		}
		
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			Log.i("DebugPrint","provider enabled");
		}
		
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			Log.i("DebugPrint","status changed");
		}
	}
	*/
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	
	/*
	private GeoPoint getGeoPoint(Location location){
		if(location!=null)
			return (new GeoPoint((int)(location.getLatitude() * 1000000.0),(int)(location.getLongitude() * 1000000.0)));
		return null;
	}
	*/
	
	protected void toggleGps(boolean start){
		if(start){
			setRequestForStart();
			timer.start();
			isTimerOn = true;
		}else{
			
			setRequestForStop();
			
			if(isTimerOn)
				timer.cancel();
			
			
			volumeButton.check(R.id.radioEmptyBus);
			errorCount = 0;
			
			if(request!=null && isTimerOn){
				sendRequest(BusStateModel.getInstance());
			}

			isTimerOn = false;
		}
	}
	
	
	public void alertGpsMessage(String message){
		AlertDialog.Builder builder = new AlertDialog.Builder(Cra_bisGPSActivity.this);
		
		/*
		String message;
		if(!togegle){
			message = "위치정보 송신을 시작하겠습니까?";
		}else{
			message = "위치정보 송신을 중단하겠습니까?";
		}
		*/
		builder.setCancelable(false)
			.setMessage(message)
			.setPositiveButton(R.string.button_yes,
				new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					isToggleOn = !isToggleOn;
					toggleGps(isToggleOn);
					//message;
					toggleButton.setChecked(isToggleOn);
				}
			})
			.setNegativeButton(R.string.button_no,
				new DialogInterface.OnClickListener() {		
				public void onClick(DialogInterface dialog, int which) {}
			});
		AlertDialog alertDialog = builder.create();
		alertDialog.show();
		//toggleButton.setChecked(isToggleOn);
	}
	
	class MyClickListener implements OnClickListener{
		public void onClick(View v) {
			// TODO Auto-generated method stub
			String message;
			toggleButton.setChecked(isToggleOn);
			
			if(isToggleOn){
				message = "위치정보 송신을 중단하겠습니까?";
			}else{
				message = "위치정보 송신을 시작하겠습니까?";
			}
			//alertGpsToggle(isToggleOn);
			alertGpsMessage(message);
			//MyAlertDialog myAlertDialog = new MyAlertDialog(Cra_bisGPSActivity.this, message);
			//myAlertDialog.setMessage(message);
			//	.setNegativeButton();
		}
	}
}