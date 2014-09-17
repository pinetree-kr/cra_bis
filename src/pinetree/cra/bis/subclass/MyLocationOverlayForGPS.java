package pinetree.cra.bis.subclass;

import pinetree.cra.bis.Cra_bisGPSActivity;
import pinetree.cra.bis.model.BusStateModel;
import pinetree.cra.bis.model.GPGLLModel;
import android.content.Context;
import android.location.Location;

import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;

public class MyLocationOverlayForGPS extends MyLocationOverlay{
	protected float accuracy;
	protected double latitude, longitude;
	
	// �������� ��
	protected final float ACCURACY_LIMIT = 50;
		
	public MyLocationOverlayForGPS(Context context, MapView mapView) {
		super(context, mapView);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void onLocationChanged(Location location){
		super.onLocationChanged(location);
		String textLocation;
		String textColor;
		if(location!=null){
			accuracy = this.getLastFix().getAccuracy();
			latitude = this.getLastFix().getLatitude();
			longitude = this.getLastFix().getLongitude();
			GPGLLModel.getInstance().setGPGLL(latitude,longitude);
			
			//BusStateModel.getInstance().setGPGLL(location);
			/*
			textLocation = "Latitude : " + latitude + "\n"
					+ "Longitude : " + longitude + "\n"
					+ "Accuracy : " + accuracy;
			*/
			textLocation = "�������� : " + accuracy +"m";
			
			if(accuracy>ACCURACY_LIMIT){
				//Cra_bisGPSActivity.activateGpsToggleButton(false);				
				textLocation += "\n���������� �о�("+ACCURACY_LIMIT+"m�̻�) ��ġ������ ���� �� �� �����ϴ�";
				// ���� �۾�
				textColor = "#FFFF0000";
			}else{
				//Cra_bisGPSActivity.activateGpsToggleButton(true);
				textColor = "#FF000000";				
			}
			
		}else{
			textLocation = "��ġ ������ ������ �� �����ϴ�";
			textColor = "#FFFF0000";
			
		}
		Cra_bisGPSActivity.setTextForGPS(textLocation, textColor);		
	}
	
	public float getAccuracy(){
		return accuracy;
	} 
	
	public float getAccuracyLimit(){
		return ACCURACY_LIMIT;
	}
}
