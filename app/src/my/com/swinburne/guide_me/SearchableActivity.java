package my.com.swinburne.guide_me;

import java.util.ArrayList;
import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SearchableActivity extends ListActivity {

	// The keys for the hashmap that stores the data of each item
	private static final String KEY_ITEM = "item"; // parent node
	private static final String KEY_NAME = "Name";
	private static final String KEY_LOC ="Location";
	private static final String KEY_DESC ="Description";
	private static final String KEY_PRICE ="Pricerange";
	private static final String KEY_OPEN ="Openinghours";
	private static final String KEY_CONT ="ContactNumber";
	private static final String KEY_LONG ="Longitude";
	private static final String KEY_LAT ="Latitude";
	private static final String KEY_WEB ="Website";
	private static final String KEY_EMAIL ="Email";
	
	// URL is unique to each category. 
	private String URL;

	// The sub-category arraylist of hashmaps.
	// Each hashmap stores each sub-category and it's details.
	private ArrayList<HashMap<String, String>> subcats;
	
	// Array of String of the list
	private String[] result = null;
	
	private String query;
	private ArrayList<String> info;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		handleIntent(getIntent());	    
	}

	@Override
	protected void onNewIntent(Intent intent) {
		setIntent(intent);
		handleIntent(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.searchable, menu);
		return true;
	}

	private void handleIntent(Intent intent)
	{
		URL = SubCategory.getURL();
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
	      this.query = intent.getStringExtra(SearchManager.QUERY);
	      doMySearch();
	    }
	}
	
	private void doMySearch()
	{
		init();
		new DownloadXMLTask().execute(URL);
	}
	
	private void init() {
		subcats = new ArrayList<HashMap<String,String>>();		
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		
		super.onListItemClick(l, v, position, id);

		Intent i = new Intent(getApplicationContext(), Details.class);
		info = new ArrayList<String>();
		info.add(result[position]);
		i.putStringArrayListExtra("info", info);
		startActivity(i);
		
		
	}

	/**
	 * SubCategoryList defines the customListAdapter
	 */
	public class SubCategoryList extends ArrayAdapter<String> {
		int rand;
		public SubCategoryList(Context context, int textViewResourceId,	String[] objects) {
			super(context, textViewResourceId, objects);
			rand = 0;
			
		}
		public View getView(int position, View convertView, ViewGroup parent) {
			
			LayoutInflater li = getLayoutInflater();
			View row = li.inflate(R.layout.main_cat_list_item, parent, false);
			TextView label = (TextView)row. findViewById(R.id.main_cat_list_item_label);
			ImageView icon = (ImageView)row. findViewById(R.id.main_cat_list_item_icon);
			
			label.setText(result[position]);
			
			
			// NOTE: Static icons to be changed in the future to dynamic
			if (URL.indexOf("resturants.xml") != -1) {
				icon.setImageDrawable(getResources().getDrawable(R.drawable.icon_food2));				
			}
			else if (URL.indexOf("accomodation.xml") != -1) {
				icon.setImageDrawable(getResources().getDrawable(R.drawable.icon_hotel));
			}
			else if (URL.indexOf("commercial%20centers.xml") != -1) {
				icon.setImageDrawable(getResources().getDrawable(R.drawable.icon_hotel));
			}
			else if (URL.indexOf("Financial%20Institutions.xml") != -1) {
				icon.setImageDrawable(getResources().getDrawable(R.drawable.icon_money));
			}
			else if (URL.indexOf("convenience%20store.xml") != -1) {
				icon.setImageDrawable(getResources().getDrawable(R.drawable.icon_store));
			}
			else if (URL.indexOf("Laundry%20Service.xml") != -1) {
				icon.setImageDrawable(getResources().getDrawable(R.drawable.icon_laundry));
			}
			else if (URL.indexOf("nightspots.xml") != -1) {
				icon.setImageDrawable(getResources().getDrawable(R.drawable.icon_night));
			}
			else if (URL.indexOf("tourist%20attraction.xml") != -1) {
				icon.setImageDrawable(getResources().getDrawable(R.drawable.icon_site));
			}
			else {
				icon.setImageDrawable(getResources().getDrawable(R.drawable.icon_food));
			}
			
			return row;
		}
	}
	
	private class DownloadXMLTask extends AsyncTask<String, Void, String[]> {

		@Override
		protected String[] doInBackground(String... params) {
			XMLParser parser = new XMLParser();
			String xml = parser.getXmlFromUrl(params[0]);

			if (xml == null) { // Most probably because of a network fault
				cancel(false);
				return result;
			}

			Document doc = parser.getDomElement(xml);
			
			NodeList nl = doc.getElementsByTagName(KEY_ITEM);
			
			for(int i = 0; i < nl.getLength(); i++){
				HashMap<String, String> map = new HashMap<String, String>();
				Element e = (Element) nl.item(i);
				map.put(KEY_NAME, parser.getValue(e, KEY_NAME));
				map.put(KEY_LOC, parser.getValue(e, KEY_LOC));
				map.put(KEY_DESC, parser.getValue(e, KEY_DESC));
				map.put(KEY_PRICE, parser.getValue(e, KEY_PRICE));
				map.put(KEY_OPEN, parser.getValue(e, KEY_OPEN));
				map.put(KEY_CONT, parser.getValue(e, KEY_CONT));
				map.put(KEY_LONG, parser.getValue(e, KEY_LONG));
				map.put(KEY_LAT, parser.getValue(e, KEY_LAT));
				map.put(KEY_WEB, parser.getValue(e, KEY_WEB));
				map.put(KEY_EMAIL, parser.getValue(e, KEY_EMAIL));
				subcats.add(map);
			}

			String searchList = "";
			ArrayList<String> objects = new ArrayList<String>();
			for(int i = 0; i < subcats.size(); i++){
				searchList += subcats.get(i).get(KEY_NAME) + " ";
				searchList += subcats.get(i).get(KEY_LOC) + " ";
				searchList += subcats.get(i).get(KEY_DESC) + " ";

				if (searchList.toLowerCase().replaceAll("\\s","").indexOf(query.toLowerCase().replaceAll("\\s","")) != -1) {
					objects.add(subcats.get(i).get(KEY_NAME));
				}

				searchList = "";
			}
			
			result = new String[objects.size()];

			for (int i = 0; i < objects.size(); i++) {
				result[i] = objects.get(i);
			}

			return result;
		}

		@Override
		protected void onCancelled(String[] result) {
			Log.w("XMLParsing", "Failed to parse on ");
			if (result == null)
			{
				Toast toast = new Toast(getApplicationContext());
				toast = Toast.makeText(getApplicationContext(), "Something went wrong with your network.", Toast.LENGTH_LONG);
				toast.show();
			}
		}

		@Override
		protected void onPostExecute(String[] result)
		{
			if (result == null) {
				return;		
			}		
			
			setListAdapter(new SubCategoryList(getApplicationContext(), R.layout.category, result));
		}
		
	}
}
