package pinetree.cra.bis.subclass;

import pinetree.cra.bis.R;
import pinetree.cra.bis.R.string;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class MyAlertDialog {
	protected boolean pressedButton;
	protected AlertDialog.Builder builder;
	
	MyAlertDialog(){
		builder = null;
	}
	
	MyAlertDialog(Activity activity){
		if(activity!=null){
			init(activity);
			
		}
	}
	
	MyAlertDialog(Activity activity, String message){
		if(activity!=null){
			this.init(activity)
				.setMessage(message);
		}
	}

	MyAlertDialog(Activity activity, String message, String title){
		if(activity!=null){
			this.init(activity)
				.setMessage(message)
				.setTitle(title);
		}
	}
	
	public MyAlertDialog init(Activity activity){
		builder = new AlertDialog.Builder(activity);
		builder.setCancelable(false)
			.setPositiveButton(R.string.button_yes,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						pressedButton = true;
					}
			})
			.setNegativeButton(R.string.button_no,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						pressedButton = false;
					}
			});
		pressedButton = false;
		return this;
	}
	
	public boolean hasBuilder(){
		if(builder!=null)
			return true;
		else return false;
	}
	
	public MyAlertDialog setMessage(String message){
		if(hasBuilder()){
			builder.setMessage(message);
		}
		//this.message = message;
		return this;
	}

	public MyAlertDialog setTitle(String title){
		if(hasBuilder()){
			builder.setTitle(title);
		}
		return this;
	}
	
	/*
	public MyAlertDialog setPositiveButton(){
		if(hasBuilder()){
			builder.setPositiveButton(R.string.button_yes,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						pressedButton = true;
					}
				});
		}
		return this;
	}
	
	public MyAlertDialog setNegativeButton(){
		if(hasBuilder()){
			builder.setNegativeButton(R.string.button_no,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						pressedButton = false;
					}
				});
		}
		return this;
	}
	*/
	
	public void show(){
		if(hasBuilder()){
			AlertDialog alertDialog = builder.create();
			alertDialog.show();
		}
		/*
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
		*/
	}
	
	public boolean getPressedButton(){
		return pressedButton;
	}
}
