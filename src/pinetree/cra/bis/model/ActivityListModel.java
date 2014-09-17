package pinetree.cra.bis.model;

import java.util.ArrayList;

import pinetree.cra.bis.Cra_bisActivity;
import pinetree.cra.bis.Cra_bisGPSActivity;
import pinetree.cra.bis.Cra_bisMapActivity;

import android.app.Activity;
import android.util.Log;

public class ActivityListModel {
	protected static ActivityListModel activityListModel = new ActivityListModel();
	protected ArrayList<Activity> activityList;
	
	protected ActivityListModel(){
		activityList = new ArrayList<Activity>();
	}
	
	public static ActivityListModel getInstance(){
		return activityListModel;
	}
	
	public void push(Activity activity){
		activityList.add(activity);
	}
	
	public Activity pop(){
		Activity popAct = activityList.get(activityList.size()-1);
		activityList.remove(popAct);
		return popAct;
	}
	
	public void clear(){
		for(int i = activityList.size()-1; i>=0 && activityList.get(i)!=null; i--){
			if(activityList.get(i).getClass().equals(Cra_bisGPSActivity.class)){
				Cra_bisMapActivity activity = (Cra_bisMapActivity) activityList.get(i);
				activity.moveToPreviousActivity();
			}else{
				Cra_bisActivity activity = (Cra_bisActivity) activityList.get(i);
				activity.moveToPreviousActivity();
			}			
			
		}
		
	}
}
