package pinetree.cra.bis.subclass;

import java.util.ArrayList;

import android.content.Context;

import android.graphics.drawable.Drawable;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class OverlayItems extends ItemizedOverlay<OverlayItem> implements OnGestureListener{

	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	Context mContext;
	public int mTouchLat = 0, mTouchLon = 0;
	private MapView view;
	private Drawable marker;
	private boolean longTouch = false;
	private GestureDetector mGestureDetector;
	
	/*
	 * Constructor
	 */
	@SuppressWarnings("deprecation")
	public OverlayItems(Drawable defaultMarker, Context context, MapView mapView){
		super(boundCenterBottom(defaultMarker));
		marker = defaultMarker;
		mContext = context;
		mGestureDetector = new GestureDetector(this);
		view = mapView;
	}

	public void addOverlay(OverlayItem overlay){
		mOverlays.add(overlay);
		populate();
		view.invalidate();
	}
	
	public GeoPoint getTouchedPoint(){
		return (new GeoPoint(mTouchLat, mTouchLon));
	}
	
	@Override
	protected OverlayItem createItem(int i) {
		// TODO Auto-generated method stub
		return mOverlays.get(i);
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return mOverlays.size();
	}

	@Override
	public boolean onTap(int index) {
		// TODO Auto-generated method stub
		mOverlays.remove(getItem(index));
		populate();
		return true;
	}

	@Override
	public boolean onTap(GeoPoint p, MapView mapView) {
		// TODO Auto-generated method stub
		boolean tapped = super.onTap(p, mapView);
		if(!tapped){
			if(longTouch == true){
				//Drable logo = mContext.getResources().getDrawable(R.drawable.icon);
				longTouch = false;
			}
		}
		
		return true;
	}

	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		return false;
	}

	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		longTouch = true;
	}

	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}
	public boolean onTouchEvent(MotionEvent event, MapView mapView){
		return mGestureDetector.onTouchEvent(event);
	}
}
