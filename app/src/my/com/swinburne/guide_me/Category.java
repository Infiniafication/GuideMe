package my.com.swinburne.guide_me;

import java.util.ArrayList;
import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.*;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class Category extends ListActivity{
	// All static variables
	// NOTE: This URL will change during actual production
	private static final String URL = "http://diehardofdeath.net16.net/PlazaMadeka/url.xml";
	// XML node keys
	private static final String KEY_ITEM = "item"; // parent node
	private static final String KEY_NAME = "Name";
	private static final String KEY_URL = "url";
	
	// An arrayList of hashmap. The keys are the category name and values are category xml URL
	private ArrayList<HashMap<String, String>> categories;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		categories = new ArrayList<HashMap<String, String>>();

		// This runs a new thread for network related task
		new DownloadXMLTask().execute(URL);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		 // Inflate the options menu from XML
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main, menu);

	    return true;
	}


	/**
	 * When a list item is clicked, it passes in the category name and URL to the next activity
	 */
    @Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
    	Intent i = new Intent(getApplicationContext(), SubCategory.class);
    	i.putExtra(KEY_NAME, categories.get(position).get(KEY_NAME));
    	i.putExtra(KEY_URL, categories.get(position).get(KEY_URL));
    	startActivity(i);
    }
	
	/**
	 * CategoryList defines the CustomListAdapter.
	 */
	public class CategoryList extends ArrayAdapter<String> {
		
		// NOTE: Static icons for the categories. Might change to dynamic in production
		private final int icons[] = {
			R.drawable.food,
			R.drawable.accomd,
			R.drawable.center,
			R.drawable.finance,
			R.drawable.store,
			R.drawable.laundry,
			R.drawable.pray,
			R.drawable.site,
			R.drawable.categories
		};

		public CategoryList(Context context, int textViewResourceId, String[] objects) {
			super(context, textViewResourceId, objects);
		}

		/**
		 * Gets each row and displays the necessary data accordingly
		 */
		public View getView(int position, View convertView, ViewGroup parent) {

			LayoutInflater li = getLayoutInflater();
			// Each row is a view from main_cat_list_item.xml
			View row = li.inflate(R.layout.main_cat_list_item, parent, false);

			TextView label = (TextView)row. findViewById(R.id.main_cat_list_item_label);
			ImageView icon = (ImageView)row. findViewById(R.id.main_cat_list_item_icon);
			
			label.setText(categories.get(position).get(KEY_NAME));

			// NOTE: Static icons are set here
			if(position <= icons.length-1)
				icon.setImageDrawable(getResources().getDrawable(icons[position]));
			else // set default icon
				icon.setImageDrawable(getResources().getDrawable(icons[icons.length]));
			
			return row;
		}
	}
	
	/**
	 * Defines the Asynchronous Task to handle a new thread for network access
	 */
	private class DownloadXMLTask extends AsyncTask<String, Void, String[]> {
		private String[] result = null;

		@Override
		protected void onPreExecute()
		{
			// TODO: Show progress bar while loading data online
			Log.i("onPreExucute", "Show progress bar");
		}

		@Override
		protected String[] doInBackground(String... params) {
			Log.i("doInBackground", "Getting data");
			String url = params[0];
			String xml = null;
			Document doc = null;
			
			XMLParser parser = new XMLParser();
			xml = parser.getXmlFromUrl(url);

			if (xml == null) { // Most probably because of a network fault
				Log.e("doInBackground", "XML Error");
				cancel(false);
				return result;
			}
			
			Log.i("doInBackground", "Got XML");
			doc = parser.getDomElement(xml);
			
			NodeList nodes = doc.getElementsByTagName(KEY_ITEM);
			for(int i = 0; i < nodes.getLength(); i++){
				
				HashMap<String, String> urlObject = new HashMap<String, String>();
				Element el = (Element) nodes.item(i);
				urlObject.put(KEY_NAME, parser.getValue(el, KEY_NAME));
				urlObject.put(KEY_URL, parser.getValue(el, KEY_URL));
				Log.i(KEY_NAME, parser.getValue(el, KEY_NAME));
				Log.i(KEY_URL, parser.getValue(el, KEY_URL));
				categories.add(new HashMap<String, String>(urlObject));
			}

			result = new String[categories.size()] ;
			for(int i =0 ; i < categories.size(); i++){
				result[i] = new String(categories.get(i).get(KEY_NAME).toString());
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
			Log.i("onPostExecute", "Hide progress bar");
			// hide the progress bar
			setListAdapter(new CategoryList(getApplicationContext(), R.layout.category, result));
		}
	}
	
}
