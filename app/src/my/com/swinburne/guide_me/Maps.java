package my.com.swinburne.guide_me;

import java.util.ArrayList;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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
                getResources().getDrawable(R.drawable.btn_next),
                getResources().getDrawable(R.drawable.btn_next_pressed)) 
        {
            @Override
            protected void onClickConfirmed(View v, Marker marker) {
                // Here we can perform some action triggered after clicking the button
                Intent i = new Intent(getApplicationContext(), Details.class);
                ArrayList<String> info = new ArrayList<String>();
                info.add(infoTitle.getText().toString());
                i.putStringArrayListExtra("info", info);
                startActivity(i);
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
				
				// TODO: Remove static locations and use dynamic locations

				// map.addMarker(new MarkerOptions().position(new LatLng(1.558992, 110.344761)).title("Little Lebanon").snippet("Address: 49 Wayang Street 93000 Kuching, Sarawak"));
				// map.addMarker(new MarkerOptions().position(new LatLng(1.557496, 110.349356)).title("James Brooks").snippet("Address: Jalan Tunku Abdul Rahman, 93100 Kuching, Sarawak"));
				// map.addMarker(new MarkerOptions().position(new LatLng(1.558582, 110.34555)).title("Lau Ya Keng").snippet("Address: Carpenter St. 93300 Kuching, Sarawak"));
				// map.addMarker(new MarkerOptions().position(new LatLng(1.557624, 110.347819)).title("Life Café").snippet("Address: Brighton Square Kuching, Sarawak"));
				// map.addMarker(new MarkerOptions().position(new LatLng(1.559909, 110.345981)).title("On Top Lounge").snippet("Address: Level 4, Taman Letak Kereta, Jalan Bukit Mata Kucing, Kuching, Sarawak"));

				map.addMarker(new MarkerOptions().position(new LatLng(1.558992, 110.344761)).title("Little Lebanon").snippet("Restaurant").icon(BitmapDescriptorFactory.defaultMarker(270)));
				map.addMarker(new MarkerOptions().position(new LatLng(1.557496, 110.349356)).title("James Brooks").snippet("Restaurant").icon(BitmapDescriptorFactory.defaultMarker(270)));
				map.addMarker(new MarkerOptions().position(new LatLng(1.558582, 110.34555)).title("Lau Ya Keng").snippet("Restaurant").icon(BitmapDescriptorFactory.defaultMarker(270)));
				map.addMarker(new MarkerOptions().position(new LatLng(1.557624, 110.347819)).title("Life Café").snippet("Restaurant").icon(BitmapDescriptorFactory.defaultMarker(270)));
				map.addMarker(new MarkerOptions().position(new LatLng(1.559909, 110.345981)).title("On Top Lounge").snippet("Restaurant").icon(BitmapDescriptorFactory.defaultMarker(270)));
				map.addMarker(new MarkerOptions().position(new LatLng(1.556327, 110.348281)).title("Green Hill Cornor").snippet("Restaurant").icon(BitmapDescriptorFactory.defaultMarker(270)));
				map.addMarker(new MarkerOptions().position(new LatLng(1.556370, 110.349468)).title("John's place").snippet("Restaurant").icon(BitmapDescriptorFactory.defaultMarker(270)));
				map.addMarker(new MarkerOptions().position(new LatLng(1.556895, 110.354870)).title("RJ Ayam Bakar").snippet("Restaurant").icon(BitmapDescriptorFactory.defaultMarker(270)));
				

				map.addMarker(new MarkerOptions().position(new LatLng(1.556391, 110.343986)).title("Merdeka Palace Hotel").snippet("Accomodation").icon(BitmapDescriptorFactory.defaultMarker(328)));
				map.addMarker(new MarkerOptions().position(new LatLng(1.559276, 110.346153)).title("Kuching Waterfront Lodge").snippet("Accomodation").icon(BitmapDescriptorFactory.defaultMarker(328)));
				map.addMarker(new MarkerOptions().position(new LatLng(1.558246, 110.345922)).title("Mr. D's Bed & Breakfast").snippet("Accomodation").icon(BitmapDescriptorFactory.defaultMarker(328)));
				map.addMarker(new MarkerOptions().position(new LatLng(1.558568, 110.345611)).title("Backpacker's Stay").snippet("Accomodation").icon(BitmapDescriptorFactory.defaultMarker(328)));
				map.addMarker(new MarkerOptions().position(new LatLng(1.559373, 110.346024)).title("Rafflesia Lodge").snippet("Accomodation").icon(BitmapDescriptorFactory.defaultMarker(328)));

				map.addMarker(new MarkerOptions().position(new LatLng(1.558190, 110.343938)).title("Plaza Merdeka").snippet("Commercial Center").icon(BitmapDescriptorFactory.defaultMarker(192)));
				map.addMarker(new MarkerOptions().position(new LatLng(1.558086, 110.351570)).title("Riverside Shopping Complex").snippet("Commercial Center").icon(BitmapDescriptorFactory.defaultMarker(192)));
				map.addMarker(new MarkerOptions().position(new LatLng(1.555946, 110.351104)).title("Hills Shopping Mall").snippet("Commercial Center").icon(BitmapDescriptorFactory.defaultMarker(192)));
				map.addMarker(new MarkerOptions().position(new LatLng(1.557732, 110.353480)).title("Sarawak Plaza Shopping Complex").snippet("Commercial Center").icon(BitmapDescriptorFactory.defaultMarker(192)));
				map.addMarker(new MarkerOptions().position(new LatLng(1.557356, 110.353508)).title("Tun Jugah").snippet("Commercial Center").icon(BitmapDescriptorFactory.defaultMarker(192)));

				map.addMarker(new MarkerOptions().position(new LatLng(1.558000, 110.343567)).title("Hong Leong Bank").snippet("Finance").icon(BitmapDescriptorFactory.defaultMarker(42)));
				map.addMarker(new MarkerOptions().position(new LatLng(1.557978, 110.343491)).title("CIMB Bank").snippet("Finance").icon(BitmapDescriptorFactory.defaultMarker(42)));
				map.addMarker(new MarkerOptions().position(new LatLng(1.559598, 110.345680)).title("United Overseas Bank").snippet("Finance").icon(BitmapDescriptorFactory.defaultMarker(42)));

				map.addMarker(new MarkerOptions().position(new LatLng(1.560005, 110.345391)).title("Public Toilet").snippet("Toilet").icon(BitmapDescriptorFactory.defaultMarker(123)));
			}
		}
	}

	public static int getPixelsFromDp(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dp * scale + 0.5f);
    }

}
