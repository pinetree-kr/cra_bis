package pinetree.cra.bis;

import java.util.ArrayList;
import java.util.Calendar;

import pinetree.cra.bis.model.ActivityListModel;
import pinetree.cra.bis.model.BusStateModel;
import pinetree.cra.bis.model.BusTimeListModel;
import pinetree.cra.bis.model.BusTimeModel;
import pinetree.cra.bis.model.DestinationModel;
import pinetree.cra.bis.model.InformationModel;
import pinetree.cra.bis.subclass.HttpAsyncTask;
import pinetree.cra.bis.R;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Cra_bisTimeListActivity extends Cra_bisActivity{
	protected BusTimeListModel busTimeList;
	protected HttpAsyncTask request;
	protected ListView timeList;
	protected ArrayList<BusTimeModel> timeListInfo;
	//protected int bus_srl, time_srl;
	protected CountDownTimer timer;
	protected DestinationModel destinationInfo;
	
	protected final int SEC = 1000;
	protected final int MIN = 60;
	
	protected Calendar currentTime;
	protected int selectedTimeList;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		Window window = getWindow();
		window.requestFeature(Window.FEATURE_CUSTOM_TITLE);
		
		setContentView(R.layout.timelist_layout);
		
		window.setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_layout);
		
		textTitleBar = (TextView) findViewById(R.id.textTitleBar);
		textTitleBar.setText(R.string.titleTimeList);
		
		currentTime = Calendar.getInstance();
		currentTime.set(Calendar.SECOND, 0);
		currentTime.set(Calendar.MILLISECOND, 0);
		
		Intent intent = getIntent();
		
		destinationInfo = (DestinationModel) intent.getSerializableExtra("destinationInfo");
		
		busTimeList = new BusTimeListModel();
		busTimeList.setDestination(destinationInfo.getDestinationType()+destinationInfo.getDestinationSubType());
		
		BusStateModel.getInstance()
			.setDestination(destinationInfo.getDestinationName()+"("+destinationInfo.getDestinationSubName()+")");
		
		TextView pointName1 = (TextView) findViewById(R.id.pointName1);
		TextView pointName2 = (TextView) findViewById(R.id.pointName2);
		TextView pointName3 = (TextView) findViewById(R.id.pointName3);
		TextView pointName4 = (TextView) findViewById(R.id.pointName4);
		TextView pointName5 = (TextView) findViewById(R.id.pointName5);
		
		pointName1.setText(destinationInfo.getPoint1());
		pointName2.setText(destinationInfo.getPoint2());
		pointName3.setText(destinationInfo.getPoint3());
		pointName4.setText(destinationInfo.getPoint4());
		pointName5.setText(destinationInfo.getPoint5());
		
		
		setSettingButton();
		ActivityListModel.getInstance()
			.push(this);
		
		/*
		timer = new CountDownTimer(5 * SEC, 2 * SEC){

			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				Toast.makeText(Cra_bisTimeListActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
				moveToPreviousActivity();
			}

			@Override
			public void onTick(long millisUntilFinished) {
				// TODO Auto-generated method stub
				if(busTimeList.isError())
					sendRequest(busTimeList);
				else
					this.cancel();
			}
		};
		timer.start();
		*/
		
		sendRequest(busTimeList);
	}

	protected class TimeAdapter extends ArrayAdapter<BusTimeModel>{
		protected ArrayList<BusTimeModel> items;
		 
		public TimeAdapter(Context context, int textViewResourceId, ArrayList<BusTimeModel> items){
			super(context, textViewResourceId, items);
			this.items = items;
		}
		
		public View getView(int position, View convertView, ViewGroup parent){
			// 각 ConvertView마다 view로 assign
			View view = convertView;
			BusTimeModel dest = items.get(position);
			// View안에 또 다른 View를 Inflate하기 위해 사용
			LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			int etcCase = 0;
			
			if(dest!=null){
			
				if(dest.getTimeSrl()==0){
					// 리스트가 비어있을때
					view = vi.inflate(R.layout.timelist_empty_row, null);
				}else if(!dest.getPoint1().equals("") && !dest.getPoint2().equals("") && !dest.getPoint3().equals("") && !dest.getPoint4().equals("") && !dest.getPoint5().equals("")){
					view = vi.inflate(R.layout.timelist_row, null);
					etcCase = 0;
				}else if(dest.getPoint1().equals("") && dest.getPoint2().equals("") && !dest.getPoint3().equals("") && !dest.getPoint4().equals("") && !dest.getPoint5().equals("")){
					view = vi.inflate(R.layout.timelist_2_1x3_row, null);
					etcCase = 1;
				}else if(dest.getPoint1().equals("") && dest.getPoint2().equals("") && dest.getPoint3().equals("") && !dest.getPoint4().equals("") && !dest.getPoint5().equals("")){
					view = vi.inflate(R.layout.timelist_3_1x2_row, null);
					etcCase = 1;
				}else if(!dest.getPoint1().equals("") && !dest.getPoint2().equals("") && !dest.getPoint3().equals("") && dest.getPoint4().equals("") && dest.getPoint5().equals("")){
					view = vi.inflate(R.layout.timelist_1x3_2_row, null);
					etcCase = 5;
				}else if(!dest.getPoint1().equals("") && !dest.getPoint2().equals("") && dest.getPoint3().equals("") && dest.getPoint4().equals("") && dest.getPoint5().equals("")){
					view = vi.inflate(R.layout.timelist_1x2_3_row, null);
					
					etcCase = 5;
				}
				
				// 각 Record별 데이터
				if(dest.getTimeSrl()>0){
					
					if(position==selectedTimeList){
						view.setBackgroundColor(getResources().getColor(R.color.selectedTimeList));
					}else if(position%2==0){
						// Record의 Row마다 다른 색상
						view.setBackgroundColor(getResources().getColor(R.color.columnStart));
					}else{
						view.setBackgroundColor(getResources().getColor(R.color.white));
					}
					
					
					TextView point1 = (TextView)view.findViewById(R.id.point1);
					TextView point2 = (TextView)view.findViewById(R.id.point2);
					TextView point3 = (TextView)view.findViewById(R.id.point3);
					TextView point4 = (TextView)view.findViewById(R.id.point4);
					TextView point5 = (TextView)view.findViewById(R.id.point5);
					
					// 이미 지나간 Record
					if(selectedTimeList!=-1 && position<selectedTimeList){
						point1.setTextColor(Color.argb(100, 50, 50, 50));
						point2.setTextColor(Color.argb(100, 50, 50, 50));
						point3.setTextColor(Color.argb(100, 50, 50, 50));
						point4.setTextColor(Color.argb(100, 50, 50, 50));
						point5.setTextColor(Color.argb(100, 50, 50, 50));
					}
					
					if(!dest.getPoint1().equals("")){
						point1.setText(dest.getPoint1());
					}else if(!dest.getEtc().equals("") && etcCase == 1){
						point1.setText(dest.getEtc());
					}
					
					if(!dest.getPoint2().equals("")){
						point2.setText(dest.getPoint2());
					}
					
					if(!dest.getPoint3().equals("")){
						point3.setText(dest.getPoint3());
					}
					
					if(!dest.getPoint4().equals("")){
						point4.setText(dest.getPoint4());
					}
					
					if(!dest.getPoint5().equals("")){
						point5.setText(dest.getPoint5());
					}else if(!dest.getEtc().equals("") && etcCase == 5){
						point5.setText(dest.getEtc());
					}
				}
			}
			
			//view.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, parent.getMeasuredHeight()/10));
			view.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, 90));
			return view;
		}
	}
	
	/*
	 * Load된 TimeList에서 현재 시간과 근접한 Index
	 */
	public int getIndexOfStartTime(ArrayList<BusTimeModel> busTimeListInfo){

		Calendar thisTime = Calendar.getInstance();
		int timeIndex = 0;
		
		// 첫차의 시간
		String[] getFirstTime = busTimeListInfo.get(0).getStartTime().split(":");
		int firstHourOfDay = Integer.parseInt(getFirstTime[0]);
		int firstMinute = Integer.parseInt(getFirstTime[1]);

		// 막차의 시간
		String[] getLastTime = busTimeListInfo.get(busTimeListInfo.size()-1).getStartTime().split(":");
		int lastHourOfDay = Integer.parseInt(getLastTime[0]);
		int lastMinute = Integer.parseInt(getLastTime[1]);
		
		// 현재 시간
		int currentHour = currentTime.get(Calendar.HOUR_OF_DAY);
		int currentMinute = currentTime.get(Calendar.MINUTE);
		
		/*
		 * 현재 시간이 첫차보다 이전이고 막차보다 이전이면 day+1
		 */
		if( (currentHour < firstHourOfDay || (currentHour == firstHourOfDay && currentMinute < firstMinute))
			&& (currentHour < lastHourOfDay || (currentHour == lastHourOfDay && currentMinute <= lastMinute)) ){
			currentTime.add(Calendar.DATE, 1);
		}
		
		for(BusTimeModel busTime : busTimeListInfo){
			
			String[] getTime = busTime.getStartTime().split(":");
			
			int hourOfDay = Integer.parseInt(getTime[0])%24;
			int minute = Integer.parseInt(getTime[1]);
			

			// 현재시간과 비교. 첫차의 시간보다 빠르거나 시간은 같으나 분이 빠를때
			if(hourOfDay < firstHourOfDay || (hourOfDay == firstHourOfDay && minute < firstMinute)){
				// 익일
				thisTime.add(Calendar.DATE, 1);
			}
			
			thisTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
			thisTime.set(Calendar.MINUTE, minute);
			thisTime.set(Calendar.SECOND, 0);
			thisTime.set(Calendar.MILLISECOND, 0);
			
			if(currentTime.compareTo(thisTime)>0){
				timeIndex++;
			}
		}
		
		return timeIndex;
	}
	
	@Override
	public void postJob(InformationModel information) {
		// TODO Auto-generated method stub
		
		BusTimeListModel busTimeListInfo = (BusTimeListModel)information;
		
		if(busTimeListInfo.isError())
			return;
		
		// 수신이 제대로 이루어졌으면 Timer종료
		//timer.cancel();
		
		timeListInfo = busTimeListInfo.getBusTimeList();
		
		if(timeListInfo.size()==0){
			LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			LinearLayout emptyLayout = (LinearLayout) layoutInflater.inflate(R.layout.timelist_empty_row, null);
			LinearLayout contentLayout = (LinearLayout) findViewById(R.id.emptyLayout);
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
			contentLayout.addView(emptyLayout, layoutParams);
		}else{
			selectedTimeList = getIndexOfStartTime(timeListInfo);

			TimeAdapter timeAdapter = new TimeAdapter(this, R.layout.timelist_row, timeListInfo);
			
			timeList = (ListView) findViewById(R.id.timeList);
			timeList.setAdapter(timeAdapter);
			timeList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
			
			
			timeList.setOnItemClickListener(
				new AdapterView.OnItemClickListener() {
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						if(isGpsOn()){
							int time_srl = timeListInfo.get(position).getTimeSrl();
							
							if(time_srl>0){
								BusStateModel.getInstance()
									.setTimeSrl(time_srl)
									.setThisTime(timeListInfo.get(position).getStartTime());
								
								moveToNextActivity();
							}
						}else{
							alertGpsStatus();
						}
					}
				}
			);
			
			timeList.setSelection(selectedTimeList);
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
	public void moveToNextActivity(){
		Intent intentGPSActivity = new Intent(this, Cra_bisGPSActivity.class);
		startActivity(intentGPSActivity);
	}
	
	@Override
	public void sendRequest(InformationModel information) {
		// TODO Auto-generated method stub
		
		request = new HttpAsyncTask();
		request.setObject(Cra_bisTimeListActivity.this);
		request.execute((BusTimeListModel)information);

	}
	
	public boolean isGpsOn(){
		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
			return true;
		else return false;
	}
	
	public void moveSetGps(){
		Intent intentGpsOption = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intentGpsOption);
	}
	
	public void alertGpsStatus(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(R.string.needGpsOn)
			.setCancelable(false)
			.setPositiveButton(R.string.button_yes,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							moveSetGps();
						}
					})
			.setNegativeButton(R.string.button_no,
					new DialogInterface.OnClickListener() {		
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
						}
					});
		
		AlertDialog alertDialog = builder.create();
		alertDialog.show();
	}
	
}
