package my.com.swinburne.guide_me;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Maps extends Activity{

	private GoogleMap map;
	private static final LatLng PADANG_MERDEKA = new LatLng(1.55794,110.34434);

	private ViewGroup infoWindow;
    private TextView infoTitle;
    private TextView infoSnippet;
    private Button infoButton;
    private OnInfoWindowElemTouchListener infoButtonListener;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_relative_layout);
		// GoogleMap map = ((SupportMapFragment)  getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
		
		final MapWrapperLayout mapWrapperLayout = (MapWrapperLayout)findViewById(R.id.map_relative_layout);
		initMap();

		// MapWrapperLayout initialization
        // 39 - default marker height
        // 20 - offset between the default InfoWindow bottom edge and it's content bottom edge 
        mapWrapperLayout.init(map, getPixelsFromDp(this, 39 + 20));

        // We want to reuse the info window for all the markers, 
        // so let's create only one class member instance
        this.infoWindow = (ViewGroup)getLayoutInflater().inflate(R.layout.info_window, null);
        this.infoTitle = (TextView)infoWindow.findViewById(R.id.title);
        this.infoSnippet = (TextView)infoWindow.findViewById(R.id.snippet);
        this.infoButton = (Button)infoWindow.findViewById(R.id.button);

        // Setting custom OnTouchListener which deals with the pressed state
        // so it shows up 
        this.infoButtonListener = new OnInfoWindowElemTouchListener(infoButton,
                getResources().getDrawable(R.drawable.btn_default_normal_holo_light),
                getResources().getDrawable(R.drawable.btn_default_pressed_holo_light)) 
        {
            @Override
            protected void onClickConfirmed(View v, Marker marker) {
                // Here we can perform some action triggered after clicking the button
                Toast.makeText(Maps.this, marker.getTitle() + "'s button clicked!", Toast.LENGTH_SHORT).show();
            }
        }; 
        this.infoButton.setOnTouchListener(infoButtonListener);


        map.setInfoWindowAdapter(new InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                // Setting up the infoWindow with current's marker info
                infoTitle.setText(marker.getTitle());
                infoSnippet.setText(marker.getSnippet());
                infoButtonListener.setMarker(marker);

                // We must call this to set the current marker and infoWindow references
                // to the MapWrapperLayout
                mapWrapperLayout.setMarkerWithInfoWindow(marker, infoWindow);
                return infoWindow;
            }
        });

        // Start GPS code
		GPSTracker gps = new GPSTracker(this);
        // check if GPS location can get
        if (gps.canGetLocation()) {
            Log.d("Your Location", "latitude:" + gps.getLatitude() + ", longitude: " + gps.getLongitude());
        } else {
        	Log.d("Your Location", "Unavailable");
        }
        gps.getLocation();
		map.addMarker(new MarkerOptions().position(new LatLng(gps.getLatitude() , gps.getLongitude())).title("Marker"));
		
		map.setMyLocationEnabled(true);
	}

	private void initMap() {
		if (map == null) {
			map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
			if(map != null) {
				map.moveCamera(CameraUpdateFactory.newLatLngZoom(PADANG_MERDEKA, 14));
				map.animateCamera(CameraUpdateFactory.zoomIn());
				
				map.addMarker(new MarkerOptions().position(new LatLng(1.558992, 110.344761)).title("Little Lebanon").snippet("Address: 49 Wayang Street 93000 Kuching, Sarawak"));
				map.addMarker(new MarkerOptions().position(new LatLng(1.557496, 110.349356)).title("James Brooks").snippet("Address: Jalan Tunku Abdul Rahman, 93100 Kuching, Sarawak"));
				map.addMarker(new MarkerOptions().position(new LatLng(1.558582, 110.34555)).title("Lau Ya Keng").snippet("Address: Carpenter St. 93300 Kuching, Sarawak"));
				map.addMarker(new MarkerOptions().position(new LatLng(1.557624, 110.347819)).title("Life Café").snippet("Address: Brighton Square Kuching, Sarawak"));
				map.addMarker(new MarkerOptions().position(new LatLng(1.559909, 110.345981)).title("On Top Lounge").snippet("Address: Level 4, Taman Letak Kereta, Jalan Bukit Mata Kucing, Kuching, Sarawak"));
			}
		}
	}

	public static int getPixelsFromDp(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dp * scale + 0.5f);
    }

	public final class GPSTracker implements LocationListener {

		private final Context mContext;

		// flag for GPS status
		public boolean isGPSEnabled = false;

		// flag for network status
		boolean isNetworkEnabled = false;

		// flag for GPS status
		boolean canGetLocation = false;

		Location location; // location
		double latitude; // latitude
		double longitude; // longitude

		// The minimum distance to change Updates in meters
		private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

		// The minimum time between updates in milliseconds
		private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

		// Declaring a Location Manager
		protected LocationManager locationManager;

		public GPSTracker(Context context) {
		    this.mContext = context;
		    getLocation();
		}

		public Location getLocation() {
		    try {
		        locationManager = (LocationManager) mContext
		                .getSystemService(Context.LOCATION_SERVICE);

		        // getting GPS status
		        isGPSEnabled = locationManager
		                .isProviderEnabled(LocationManager.GPS_PROVIDER);

		        Log.v("isGPSEnabled", "=" + isGPSEnabled);

		        // getting network status
		        isNetworkEnabled = locationManager
		                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

		        Log.v("isNetworkEnabled", "=" + isNetworkEnabled);

		        if (isGPSEnabled == false && isNetworkEnabled == false) {
		            // no network provider is enabled
		        } else {
		            this.canGetLocation = true;
		            if (isNetworkEnabled) {
		                locationManager.requestLocationUpdates(
		                        LocationManager.NETWORK_PROVIDER,
		                        MIN_TIME_BW_UPDATES,
		                        MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
		                Log.d("Network", "Network");
		                if (locationManager != null) {
		                    location = locationManager
		                            .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		                    if (location != null) {
		                        latitude = location.getLatitude();
		                        longitude = location.getLongitude();
		                    }
		                }
		            }
		            // if GPS Enabled get lat/long using GPS Services
		            if (isGPSEnabled) {
		                if (location == null) {
		                    locationManager.requestLocationUpdates(
		                            LocationManager.GPS_PROVIDER,
		                            MIN_TIME_BW_UPDATES,
		                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
		                    Log.d("GPS Enabled", "GPS Enabled");
		                    if (locationManager != null) {
		                        location = locationManager
		                                .getLastKnownLocation(LocationManager.GPS_PROVIDER);
		                        if (location != null) {
		                            latitude = location.getLatitude();
		                            longitude = location.getLongitude();
		                        }
		                    }
		                }
		            }
		        }

		    } catch (Exception e) {
		        e.printStackTrace();
		    }

		    return location;
		}

		public void stopUsingGPS() {
		    if (locationManager != null) {
		        locationManager.removeUpdates(GPSTracker.this);
		    }
		}

		public double getLatitude() {
		    if (location != null) {
		        latitude = location.getLatitude();
		    }

		    return latitude;
		}

		public double getLongitude() {
		    if (location != null) {
		        longitude = location.getLongitude();
		    }

		    return longitude;
		}

		public boolean canGetLocation() {
		    return this.canGetLocation;
		}

		public void showSettingsAlert() {
		    AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

		    // Setting Dialog Title
		    alertDialog.setTitle("GPS Settings");

		    // Setting Dialog Message
		    alertDialog
		            .setMessage("GPS is not enabled. Do you want to go to settings menu?");

		    // On pressing Settings button
		    alertDialog.setPositiveButton("Settings",
		            new DialogInterface.OnClickListener() {
		                public void onClick(DialogInterface dialog, int which) {
		                    Intent intent = new Intent(
		                            Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		                    mContext.startActivity(intent);
		                }
		            });

		    // on pressing cancel button
		    alertDialog.setNegativeButton("Cancel",
		            new DialogInterface.OnClickListener() {
		                public void onClick(DialogInterface dialog, int which) {
		                    dialog.cancel();
		                }
		            });

		    // Showing Alert Message
		    alertDialog.show();
		}

		@Override
		public void onLocationChanged(Location location) {
		}

		@Override
		public void onProviderDisabled(String provider) {
		}

		@Override
		public void onProviderEnabled(String provider) {
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	}

}
