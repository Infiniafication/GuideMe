package my.com.swinburne.guide_me;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class Details extends Activity{
	private TextView name, address, others;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.details);
		init();
	}
	private void init() {
		name = (TextView) findViewById(R.id.details_name);
		address = (TextView) findViewById(R.id.details_address);
		others = (TextView) findViewById(R.id.details_others);
		
		Intent i = getIntent();
		ArrayList<String> info = i.getStringArrayListExtra("info");
		name.setText(info.get(0));
		address.setText(info.get(1));
		String g = "";
		for(int j = 2; j < info.size(); j++){
			g += info.get(j) + "\n";
		}
		others.setText(g);
	}

}
