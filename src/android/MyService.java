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
import android.os.Looper;
import android.os.Bundle;

import android.util.Log;

import com.red_folder.phonegap.plugin.backgroundservice.BackgroundService;

public class MyService extends BackgroundService {
	
	private final static String TAG = MyService.class.getSimpleName();
	
	private String mHelloTo = "World";
	private Looper mLooper;
	private String provider;

	@Override
	protected JSONObject doWork() {
		JSONObject result = new JSONObject();

		try {
			if (mLooper != null) {
				Log.d(TAG, "Exiting of Looper.");
				mLooper.quit();
			}

    	doLocation();

			SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); 
			String now = df.format(new Date(System.currentTimeMillis())); 

			String msg = "Hello " + this.mHelloTo + " - its currently " + now;
			result.put("Message", msg);

			Log.i(TAG, msg);
		} catch (JSONException e) {
		}
		
		return result;	
	}

	public void doLocation() {
		new Thread(new Runnable() {
			
			@Override
      public void run() {
				// Acquire a reference to the system Location Manager
				LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

				Criteria criteria = new Criteria();
				provider = locationManager.getBestProvider(criteria, false);

				// Define a listener that responds to location updates
				LocationListener locationListener = new LocationListener() {
			    public void onLocationChanged(Location location) {
			    	Log.d(TAG, "Coordenadas: " + location.getLatitude() + ", " + location.getLongitude());
			      
			      // Called when a new location is found by the network location provider.
			      makeUseOfNewLocation(location);
			    }

			    public void onStatusChanged(String provider, int status, Bundle extras) {}

			    public void onProviderEnabled(String provider) {}

			    public void onProviderDisabled(String provider) {}
				};

				// Prepare the Looper
				Log.d(TAG, "Preparing Looper");
				Looper.prepare();
				mLooper = Looper.myLooper();

				Log.d(TAG, "Registering location updates!");

				// Register the listener with the Location Manager to receive location updates
				locationManager.requestLocationUpdates(provider, 0, 0, locationListener);
				
				Log.d(TAG, "Location Updates Registered!");

				// Get the most recent location
				makeUseOfNewLocation(locationManager.getLastKnownLocation(provider));

				Looper.loop();

				// If we have come out of the loop, then we need to free everything
				Log.d(TAG, "Looper quit, time to destroy");
				Log.d(TAG, "Removing listener for GPS provider");
				locationManager.removeUpdates(locationListener);

				Log.d(TAG, "Setting location to null");
				locationListener = null;
				
				Log.d(TAG, "Setting location manager to null");
				locationManager = null;

				Log.d(TAG, "Run finished");
      }

		}).start();
	}

	public void makeUseOfNewLocation(Location location) {
		Log.d(TAG, "Make Use of new Location!");
	}

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
}