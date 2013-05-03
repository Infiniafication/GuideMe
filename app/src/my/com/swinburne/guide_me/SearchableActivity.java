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
import android.widget.TextView;

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
	static final String URL = "http://diehardofdeath.net16.net/PlazaMadeka/url.xml";

	private String Name = "";
	private ArrayList<HashMap<String, String>> subcats;
	private String [] objects;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Intent intent = getIntent();
	    if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
	      String query = intent.getStringExtra(SearchManager.QUERY);
	      doMySearch(query);
	    }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.searchable, menu);
		return true;
	}
	
	private void doMySearch(String query)
	{
		init();
		new DownloadXMLTask().execute(URL);
	}
	
	private void init() {
		Intent i = getIntent();
		subcats = new ArrayList<HashMap<String,String>>();
		
	}
	
	public class SubCategoryList extends ArrayAdapter<String> {
		int rand;
		public SubCategoryList(Context context, int textViewResourceId,	String[] objects) {
			super(context, textViewResourceId, objects);
			rand = 0;
			
		}
		public View getView(int position, View convertView, ViewGroup parent) {
			
			final int icons [] = {
					R.drawable.green,
					R.drawable.red,
					R.drawable.yellow
			};
			
			rand = position % 3 ;
			LayoutInflater li = getLayoutInflater();
			View row = li.inflate(R.layout.main_cat_list_item, parent, false);
			TextView label = (TextView)row. findViewById(R.id.main_cat_list_item_label);
			ImageView icon = (ImageView)row. findViewById(R.id.main_cat_list_item_icon);
			
			
			label.setText(subcats.get(position).get(KEY_NAME));
			
			icon.setImageDrawable(getResources().getDrawable(icons[rand]));
			
			return row;
		}
	}
	
	private class DownloadXMLTask extends AsyncTask<String, Void, Document> {

		@Override
		protected Document doInBackground(String... params) {
			XMLParser parser = new XMLParser();
			String xml = parser.getXmlFromUrl(params[0]);
			Document doc = parser.getDomElement(xml);
			
			return doc;
		}
		
		@Override
		protected void onPostExecute(Document result)
		{
			XMLParser parser = new XMLParser();
			NodeList nl = result.getElementsByTagName(KEY_ITEM);
			
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
				Log.i("LOL", parser.getValue(e, KEY_NAME));
				subcats.add(map);
			}
			objects = new String[subcats.size()];
			for(int i = 0; i < subcats.size(); i++){
				objects[i] = subcats.get(i).get(KEY_NAME);
				Log.i("LOL", objects[i]);
			}
			
			setListAdapter(new SubCategoryList(getApplicationContext(), R.layout.category, objects));
		}
		
	}
}
