package my.com.swinburne.guide_me;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
		// name = (TextView) findViewById(R.id.details_name);
		// address = (TextView) findViewById(R.id.details_address);
		// others = (TextView) findViewById(R.id.details_others);
		
		ImageView imageView = (ImageView) findViewById(R.id.imageView1);

		Intent i = getIntent();
		ArrayList<String> info = i.getStringArrayListExtra("info");

		String name = info.get(0).trim();

		if (name.equals("RJ Ayam Bakar")) {
			imageView.setImageDrawable(getResources().getDrawable(R.drawable.ayam_bakar));
		}
		else if (name.equals("John's place")) {
			imageView.setImageDrawable(getResources().getDrawable(R.drawable.laksa1));
		}
		else if (name.equals("Green Hill Cornor")) {
			imageView.setImageDrawable(getResources().getDrawable(R.drawable.laksa2));
		}

		// address.setText(info.get(1));
		// String g = "";
		// for(int j = 2; j < info.size(); j++){
		// 	g += info.get(j) + "\n";
		// }
		// others.setText(g);
	}

}
