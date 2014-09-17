/*
 * class		: HttpPostRequest
 * author		: ?��???
 * email		: vanadate.kr@gmail.com
 * description	: HTTP protocol???�해 ?�으�?Asyncronous Task�?POST방식?�로 Request
 */

package pinetree.cra.bis.subclass;

import pinetree.cra.bis.Cra_bisActivity;
import pinetree.cra.bis.Cra_bisGPSActivity;
import pinetree.cra.bis.Cra_bisMapActivity;
import pinetree.cra.bis.Cra_bisSplashActivity;
import pinetree.cra.bis.model.InformationModel;

import android.R.attr;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

public class HttpAsyncTask extends AsyncTask<InformationModel, InformationModel, InformationModel>{
	protected Activity activity;
	//protected Cra_bisActivity activity;
	//protected Cra_bisMapActivity mapActivity;
	protected MyProgressDialog myProgressDialog;
	protected Dialog dialog;
	
	public void setObject(Activity srcActivity){
		activity = srcActivity;
	}
/*
	public void setObject(Cra_bisMapActivity srcActivity){
		mapActivity = srcActivity;
	}
*/
	@Override
	protected InformationModel doInBackground(InformationModel... objInfo) {

		// Activity?�서 Cancel()?�청??
		if(isCancelled())
			return null;
		
		// Object�?HttpPostRequest�??�청
		InformationModel information = HttpPostRequest.httpPostRequest(objInfo[0]);
		
		// UI쪽에 response 처리
		
		publishProgress(information);
		return information;
	}
	
	@Override
	protected void onCancelled(){
		Log.i("DebugTest","AsyncTask Cancelled");
		super.onCancelled();
	}
	
	@Override
	protected void onPreExecute() {
		
		if(!activity.getClass().equals(Cra_bisSplashActivity.class)){
			
			if(activity.getClass().equals(Cra_bisGPSActivity.class)){
				Cra_bisMapActivity bisMapActivity = (Cra_bisMapActivity) activity;
				bisMapActivity.showProgressTitleBar(true);				
			}else{
				Cra_bisActivity bisActivity = (Cra_bisActivity) activity;
				bisActivity.showProgressTitleBar(true);								
			}
		}
			
		super.onPreExecute();
	}

	@Override
	protected void onPostExecute(InformationModel information) {
		
		if(!activity.getClass().equals(Cra_bisSplashActivity.class)){
			if(activity.getClass().equals(Cra_bisGPSActivity.class)){
				Cra_bisMapActivity bisMapActivity = (Cra_bisMapActivity) activity;
				bisMapActivity.postJob(information);
				bisMapActivity.showProgressTitleBar(false);
			}else{
				Cra_bisActivity bisActivity = (Cra_bisActivity) activity;
				bisActivity.postJob(information);
				bisActivity.showProgressTitleBar(false);
			}
		}else{
			Cra_bisActivity bisActivity = (Cra_bisActivity) activity;
			bisActivity.postJob(information);
		}
		
		super.onPostExecute(information);
	}

	// UI 처리
	@Override
	protected void onProgressUpdate(InformationModel... objInfo){
		
		if(objInfo[0].isError()){
			if(!activity.getClass().equals(Cra_bisGPSActivity.class) && !activity.getClass().equals(Cra_bisSplashActivity.class))
				Toast.makeText(activity, objInfo[0].getMessage(), Toast.LENGTH_SHORT).show();
		}
	}
}
