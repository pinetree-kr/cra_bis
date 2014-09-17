package pinetree.cra.bis;

import java.util.ArrayList;

import pinetree.cra.bis.model.ActivityListModel;
import pinetree.cra.bis.model.BusStateModel;
import pinetree.cra.bis.model.DestinationListModel;
import pinetree.cra.bis.model.DestinationModel;
import pinetree.cra.bis.model.InformationModel;
import pinetree.cra.bis.model.LogInModel;
import pinetree.cra.bis.subclass.HttpAsyncTask;
import pinetree.cra.bis.R;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Cra_bisDestinationActivity extends Cra_bisActivity{
	protected ListView destList;
	protected HttpAsyncTask request;
	protected ArrayList<DestinationModel> destinationListInfo;
	protected int bus_srl;
	protected String destinationType;
	protected DestinationModel destinationInfo;
	
	protected final int SEC = 1000;
	protected CountDownTimer timer;
	protected DestinationListModel destinationList;

	protected boolean isBackPressed = false;
	protected Handler myHandler;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Window window = getWindow();
		window.requestFeature(Window.FEATURE_CUSTOM_TITLE);
		
		setContentView(R.layout.destination_layout);
		window.setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_layout);
		
		textTitleBar = (TextView) findViewById(R.id.textTitleBar);
		textTitleBar.setText(R.string.titleDestination);
		
		bus_srl = BusStateModel.getInstance().getBusSrl();
		
		destinationListInfo = new ArrayList<DestinationModel>();
		
		destinationList = new DestinationListModel();
		
		/*
		// 2초간격으로 5초당안 Timer
		timer = new CountDownTimer(5 * SEC, 2 * SEC){

			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				Toast.makeText(Cra_bisDestinationActivity.this, (R.string.network_error), Toast.LENGTH_SHORT).show();
				
				moveToPreviousActivity();
			}

			@Override
			public void onTick(long millisUntilFinished) {
				// TODO Auto-generated method stub
				if(destinationList.isError())
					sendRequest(destinationList);
				else
					this.cancel();
			}
		};
		timer.start();
		*/
		
		// Handling When Pressed Back Button
		myHandler = new Handler(){
			@Override
			public void handleMessage(Message message){
				if(message.what == 0)
					isBackPressed = false;
			}
		};
		
		setSettingButton();
		
		ActivityListModel.getInstance()
			.push(this);
		
		sendRequest(destinationList);	
	}
	
	protected class DestinationAdapter extends ArrayAdapter<DestinationModel>{
		private ArrayList<DestinationModel> items;
		
		public DestinationAdapter(Context context, int textViewResourceId, ArrayList<DestinationModel> items){
			super(context, textViewResourceId, items);
			this.items = items;
		}
		
		public View getView(int position, View convertView, ViewGroup parent){
			View view = convertView;
			if(view==null){
				LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = vi.inflate(R.layout.destination_row, null);
			}
			DestinationModel dest = items.get(position);
			if(dest!=null){
				
				if(position%2==0){
					view.setBackgroundColor(getResources().getColor(R.color.columnStart));
				}else{
					view.setBackgroundColor(getResources().getColor(R.color.white));
				}
				TextView tt = (TextView)view.findViewById(R.id.toptext);
				TextView bt = (TextView)view.findViewById(R.id.bottomtext);
				if(tt!=null){
					tt.setText(dest.getDestinationName());
				}
				if(bt!=null){
					bt.setText(dest.getDestinationSubName());
				}
			}
			return view;
		}
	}
	
	public void refresh(){
		destList.notifyAll();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		//menu.add("menu");
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public void postJob(InformationModel information) {
		
		DestinationListModel destinationListModel = (DestinationListModel)information;
		
		if(destinationListModel.isError())
			return;
		
		//timer.cancel();
		
		destinationListInfo = destinationListModel.getDestinationList();
		
		DestinationAdapter destAdapter = new DestinationAdapter(this, R.layout.destination_row, destinationListInfo);
		
		destList = (ListView) findViewById(R.id.destList);
		destList.setAdapter(destAdapter);
		destList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		
		destList.setOnItemClickListener(
			new AdapterView.OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					destinationInfo = destinationListInfo.get(position);
					if(destinationInfo!=null)
						moveToNextActivity();
				}
			}
		);
		
		
	}
	
	@Override
	public void onBackPressed(){
		// TODO Auto-generated method stub
		
		if(!isBackPressed){
			Toast.makeText(Cra_bisDestinationActivity.this, "뒤로 버튼을 한 번 더 누르시면 종료됩니다", Toast.LENGTH_SHORT).show();
			isBackPressed = true;

			myHandler.sendEmptyMessageDelayed(0, 2 * SEC);
		}else{
			moveToPreviousActivity();
		}
	}
	
	@Override
	public void sendRequest(InformationModel information) {
		// TODO Auto-generated method stub
		request = new HttpAsyncTask();
		request.setObject(Cra_bisDestinationActivity.this);
		request.execute((DestinationListModel)information);
	}

	@Override
	public void moveToPreviousActivity() {
		// TODO Auto-generated method stub

		ActivityListModel.getInstance()
			.pop().finish();
	}

	@Override
	public void moveToNextActivity() {
		// TODO Auto-generated method stub
		Intent intentTimeListActivity = new Intent(this, Cra_bisTimeListActivity.class);
		intentTimeListActivity.putExtra("destinationInfo", destinationInfo);
		startActivity(intentTimeListActivity);
	}

}
