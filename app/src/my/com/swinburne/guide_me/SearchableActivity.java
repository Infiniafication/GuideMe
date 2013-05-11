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
// TODO: DO a real proper algorithm of searching 
public class SearchableActivity extends ListActivity {
	
	

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
	
	private String URL;

	private String Name = "";
	private ArrayList<HashMap<String, String>> subcats;
	private String [] result = null;
	private String query;
	private ArrayList<String> info;
	private static Toast toast;
	
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
		Intent i = getIntent();
		subcats = new ArrayList<HashMap<String,String>>();		
	}
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		
		super.onListItemClick(l, v, position, id);

		Intent i = new Intent(getApplicationContext(), Details.class);
		ArrayList<String> info = new ArrayList<String>();
		info.add(result[position]);
		i.putStringArrayListExtra("info", info);
		startActivity(i);
		
		
	}
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
			
			icon.setImageDrawable(getResources().getDrawable(R.drawable.magnolia));
			
			return row;
		}
	}
	
	private class DownloadXMLTask extends AsyncTask<String, Void, String[]> {

		@Override
		protected String[] doInBackground(String... params) {
			XMLParser parser = new XMLParser();
			String xml = parser.getXmlFromUrl(params[0]);

			if (xml == null) {		
				Toast toast = new Toast(getApplicationContext());
				toast = Toast.makeText(getApplicationContext(), "Something went wrong with your network.", Toast.LENGTH_LONG);
				toast.show();		
				cancel(true);
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
				subcats.add(map);
			}

			String searchList = "";
			ArrayList<String> objects = new ArrayList<String>();
			for(int i = 0; i < subcats.size(); i++){
				searchList += subcats.get(i).get(KEY_NAME) + " ";
				searchList += subcats.get(i).get(KEY_LOC) + " ";
				searchList += subcats.get(i).get(KEY_DESC) + " ";

				if (searchList.toLowerCase().trim().indexOf(query.toLowerCase().trim()) != -1) {
					objects.add(subcats.get(i).get(KEY_NAME));
					searchList = "";
				}
			}
			
			result = new String[objects.size()];

			for (int i = 0; i < objects.size(); i++) {
				result[i] = objects.get(i);
			}

			return result;
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
