package br.com.speracar.gpsservice;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import android.util.Log;

import com.red_folder.phonegap.plugin.backgroundservice.BackgroundService;

public class MyService extends BackgroundService implements LocationListener {
	
	private final static String TAG = MyService.class.getSimpleName();
	
	private String mHelloTo = "World";

	private LocationManager locationManager;
	private String provider;
	private Context mContext;

	public MyService(Context context) {
      this.mContext = context;
      doLocation();
  }

	@Override
	protected JSONObject doWork() {
		JSONObject result = new JSONObject();

		try {
			SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); 
			String now = df.format(new Date(System.currentTimeMillis())); 

			String msg = "Hello " + this.mHelloTo + " - its currently " + now;
			result.put("Message", msg);

			Log.d(TAG, msg);
		} catch (JSONException e) {
		}
		
		return result;	
	}

	public Location doLocation() {
		try {
			locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
			
			Criteria criteria = new Criteria();
			provider = locationManager.getBestProvider(criteria, false);
			
			Location location = locationManager.getLastKnownLocation(provider);
			if ( location != null ) {
				Log.d(TAG , " Provider " + provider + " foi selecionado . ");
				onLocationChanged(location);
			}
		} catch (Exception e) {
      e.printStackTrace();
    }

    return location;
	}

	@Override
	public void onLocationChanged ( Location location ) { }

	@Override
	protected JSONObject getConfig() {
		JSONObject result = new JSONObject();
		
		try {
			result.put("HelloTo", this.mHelloTo);
		} catch (JSONException e) {
		}
		
		return result;
	}

	@Override
	protected void setConfig(JSONObject config) {
		try {
			if (config.has("HelloTo"))
				this.mHelloTo = config.getString("HelloTo");
		} catch (JSONException e) {
		}
		
	}     

	@Override
	protected JSONObject initialiseLatestResult() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void onTimerEnabled() {
		// TODO Auto-generated method stub
	}

	@Override
	protected void onTimerDisabled() {
		// TODO Auto-generated method stub	
	}

	@Override
	public void onStatusChanged(String provider , int status , Bundle extras) { }

	@Override
	public void onProviderEnabled(String provider) { }

	@Override
	public void onProviderDisabled(String provider) { }
}