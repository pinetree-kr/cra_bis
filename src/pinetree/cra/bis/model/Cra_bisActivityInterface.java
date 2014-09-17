package pinetree.cra.bis.model;

import android.os.Bundle;


public interface Cra_bisActivityInterface {
	public void onCreate(Bundle savedInstanceState);
	
	public abstract void postJob(InformationModel information);
	public abstract void sendRequest(InformationModel information);
	public abstract void moveToPreviousActivity();
	public abstract void moveToNextActivity();
	
	public void refreshActivity();
	public void setSettingButton();
	public void moveToSettingActivity();
	public void showProgressTitleBar(boolean show);
}
