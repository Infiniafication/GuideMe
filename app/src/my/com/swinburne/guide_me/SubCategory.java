package my.com.swinburne.guide_me;

import java.util.ArrayList;
import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

public class SubCategory extends ListActivity{

	// The keys for the hashmap that stores the data of each item
	private static final String KEY_ITEM = "item"; // parent node
	private static final String KEY_NAME = "Name";
	private static final String KEY_URL = "url";
	private static final String KEY_LOC ="Location";
	private static final String KEY_DESC ="Description";
	private static final String KEY_PRICE ="Pricerange";
	private static final String KEY_OPEN ="Openinghours";
	private static final String KEY_CONT ="ContactNumber";
	private static final String KEY_LONG ="Longitude";
	private static final String KEY_LAT ="Latitude";
	private static final String KEY_WEB ="Website";
	private static final String KEY_EMAIL ="Email";

	private static String URL = "";

	private String name = "";
	private ArrayList<HashMap<String, String>> subcats;
	private ArrayList<String> info;
	private SearchView searchView;

	/** 
     * Class Static Method to get the URL passed from previous intent
     * Used by searchableActivity
     */
    public static String getURL()
    {
    	return SubCategory.URL;
    }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();    	
	}
	
	@Override
	protected void onResume() {
		super.onResume();

		// get URL depending on the category name from previous intent
		URL = getIntent().getStringExtra(KEY_URL);

		// Run network related task on a new thread
		new DownloadXMLTask().execute(SubCategory.URL);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		 // Inflate the options menu from XML
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.options_menu, menu);

	    // Get the SearchView and set the searchable configuration
	    SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
	    searchView = (SearchView) menu.findItem(R.id.search).getActionView();
	    // Assumes current activity is the searchable activity
	    searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
	    searchView.setSubmitButtonEnabled(true);
	    searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
	    
	    return true;
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// NOTE: Should use parcelable and pass a whole object with these attributes
    	Intent i = new Intent(getApplicationContext(), Details.class);
    	info = new ArrayList<String>();
		try{
			info.add(subcats.get(position).get(KEY_NAME).toString());
		}catch(NullPointerException e){
			Log.e("onListItemClick " + KEY_NAME, e.toString());
		}
    	try{
    		info.add(subcats.get(position).get(KEY_LOC).toString());
    	}catch(NullPointerException e){
			Log.e("onListItemClick " + KEY_LOC, e.toString());
    	}
    	try{
    		info.add(subcats.get(position).get(KEY_DESC).toString());
    	}catch(NullPointerException e){
    		Log.e("onListItemClick ", e.toString());
    	}
    	try{
    		info.add(subcats.get(position).get(KEY_PRICE).toString());
    	}catch(NullPointerException e){
    		Log.e("onListItemClick " + KEY_PRICE, e.toString());
    	}
    	try{
    		info.add(subcats.get(position).get(KEY_OPEN).toString());
    	}catch(NullPointerException e){
    		Log.e("onListItemClick " + KEY_OPEN, e.toString());
    	}
    	try{
    		info.add(subcats.get(position).get(KEY_CONT).toString());
    	}catch(NullPointerException e){
    		Log.e("onListItemClick " + KEY_CONT, e.toString());
    	}
    	try{
    		info.add(subcats.get(position).get(KEY_LONG).toString());
    	}catch(NullPointerException e){
    		Log.e("onListItemClick " + KEY_LONG, e.toString());
    	}
    	try{
    		info.add(subcats.get(position).get(KEY_LAT).toString());
    	}catch(NullPointerException e){
    		Log.e("onListItemClick " + KEY_LAT, e.toString());
    	}
    	try{
    		info.add(subcats.get(position).get(KEY_WEB).toString());
    	}catch(NullPointerException e){
    		Log.e("onListItemClick " + KEY_WEB, e.toString());
    	}
    	try{
    		info.add(subcats.get(position).get(KEY_EMAIL).toString());
    	}catch(NullPointerException e){
    		Log.e("onListItemClick " + KEY_EMAIL, e.toString());
    	}

    	i.putStringArrayListExtra("info", info);
    	startActivity(i);

    }

	private void init() {
		Intent i = getIntent();
		this.name = i.getStringExtra(KEY_NAME);
		setTitle(this.name);
		subcats = new ArrayList<HashMap<String,String>>();
		
	}

	/**
	 * SubCategoryList defines the customListAdapter for the listView
	 */
	public class SubCategoryList extends ArrayAdapter<String> {
		public SubCategoryList(Context context, int textViewResourceId,	String[] objects) {
			super(context, textViewResourceId, objects);
		}

		/**
		 * Defines the layout for each row in the listView
		 */
		public View getView(int position, View convertView, ViewGroup parent) {
			
			LayoutInflater li = getLayoutInflater();
			View row = li.inflate(R.layout.main_cat_list_item, parent, false);
			TextView label = (TextView)row. findViewById(R.id.main_cat_list_item_label);
			ImageView icon = (ImageView)row. findViewById(R.id.main_cat_list_item_icon);
			
			label.setText(subcats.get(position).get(KEY_NAME));
			
			icon.setImageDrawable(getResources().getDrawable(R.drawable.magnolia));
			
			return row;
		}
	}
	
	/**
	 * The Asynchronous Task that runs a new thread to retrieve data online
	 */ 
	private class DownloadXMLTask extends AsyncTask<String, Void, String[]> {
		private String[] result = null;

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
				subcats.add(map);
			}
			result = new String[subcats.size()];
			for(int i = 0; i < subcats.size(); i++){
				result[i] = subcats.get(i).get(KEY_NAME);
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
			// After doInBackground(), this is the only function that runs on the UI thread	
			setListAdapter(new SubCategoryList(getApplicationContext(), R.layout.category, result));
		}
		
	}
}
