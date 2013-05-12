package my.com.swinburne.guide_me;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends Activity {
	private ImageButton cat, map;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setOrientation();
		map = (ImageButton) findViewById(R.id.actMain_imBtn_map);
		cat = (ImageButton) findViewById(R.id.actMain_imBtn_cat);
		
		cat.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext(),Category.class);
				startActivity(i);
				
			}
		});
		map.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getApplicationContext(),Maps.class);
				startActivity(i);
			}
		});
	}

	/**
	 * Fix the orientation to be on potrait mode
	 */
	protected void setOrientation() {
	    int current = getRequestedOrientation();
	    // only switch the orientation if not in portrait
	    if ( current != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT ) {
	        setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT );
	    }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
