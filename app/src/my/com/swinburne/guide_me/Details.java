package my.com.swinburne.guide_me;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class Details extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO: Fix this after configuring the details.xml
		super.onCreate(savedInstanceState);
		setContentView(R.layout.details);
		init();
	}
	private void init() {
		
		ImageView imageView = (ImageView) findViewById(R.id.imageView1);
		ImageButton map_btn = (ImageButton) findViewById(R.id.map_btn);

		Intent i = getIntent();
		ArrayList<String> info = i.getStringArrayListExtra("info");

		String name = info.get(0).trim();

		if (name.equals("RJ Ayam Bakar")) {
			imageView.setImageDrawable(getResources().getDrawable(R.drawable.rj_ayam));
		}
		else if (name.equals("John's place")) {
			imageView.setImageDrawable(getResources().getDrawable(R.drawable.johns_place));
		}
		else if (name.equals("Green Hill Cornor")) {
			imageView.setImageDrawable(getResources().getDrawable(R.drawable.green_hill));
		}
	}

	// Called when the map button is clicked on the detail page
	public void testMapping(View v)
	{
		GPSTracker gps = new GPSTracker(Details.this);
		double source_lat = 0;
		double source_long = 0;

		// check if GPS enabled     
        if(gps.canGetLocation())
        {
            source_lat = gps.getLatitude();
            source_long = gps.getLongitude();
        }
        else
        {
	        // can't get location
	        // GPS or Network is not enabled
	        // Ask user to enable GPS/network in settings
	        gps.showSettingsAlert();
	    }
	    // TODO: Make static destination routes dynamic
		String dest_lat = "1.556895";
		String dest_long = "110.35487";

		// TODO: Change the mode to WALKING mode from current driving mode
		String uri = "http://maps.google.com/maps?daddr="+dest_lat+","+dest_long+"&saddr="+source_lat+","+source_long;
		Intent i = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
		i.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
		startActivity(i);
	}

}
