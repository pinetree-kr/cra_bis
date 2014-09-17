/*
 * class		: GPGLL
 * author		: ����ȣ
 * email		: vanadate.kr@gmail.com
 * description	: Location���κ��� �޾ƿ� �������� GPGLL �������� ��ȯ�� String ��ü�� ����
 */

package pinetree.cra.bis.model;

import android.location.Location;
import android.util.Log;

public class GPGLLModel {
	protected String gpgll;
	protected final String messageID = "$GPGLL";
	protected String indicatorNS;
	protected String indicatorEW;
	protected String positionUTC;
	protected String status;
	protected String checkSum;
	protected final String CRLF = "\r\n";
	protected static GPGLLModel gpgllModel = new GPGLLModel();
	
	protected GPGLLModel(){
		gpgll = "";
		indicatorNS = "N";
		indicatorEW = "W";
		positionUTC = "";
		status = "";
		checkSum = "";
	};
	
	public static GPGLLModel getInstance(){
		return gpgllModel;
	}
	
	/*
	protected GPGLLModel(Location location){
		setGPGLL(location);
	}
	*/
	
	public GPGLLModel setGPGLL(double latitude, double longitude){
		/*
		gpgll = messageID + "," +
				location.getLatitude() + "," +
				indicatorNS + "," +
				location.getLongitude() + "," +
				indicatorEW + "," +
				positionUTC + "," +
				status + "," +
				checkSum + "," + 
				CRLF;
			*/
		gpgll = messageID + "," +
				latitude + "," +
				indicatorNS + "," +
				longitude + "," +
				indicatorEW + "," +
				positionUTC + "," +
				status + "," +
				checkSum + "," + 
				CRLF;
		//Log.i("DebugPrint",gpgll);
		return this;
	}
	
	public String getGPGLL(){
		return gpgll;
	}
}
