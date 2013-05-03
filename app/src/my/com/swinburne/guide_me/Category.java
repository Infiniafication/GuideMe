package my.com.swinburne.guide_me;

import java.util.ArrayList;
import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;

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
import android.widget.*;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class Category extends ListActivity{
	// All static variables
	static final String URL = "http://diehardofdeath.net16.net/PlazaMadeka/url.xml";
	// XML node keys
	static final String KEY_ITEM = "item"; // parent node
	static final String KEY_NAME = "Name";
	static final String KEY_URL = "url";
	
	private ArrayList<HashMap<String, String>> categories;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		categories = new ArrayList<HashMap<String, String>>();
		new DownloadXMLTask().execute(URL);
		
		Intent intent = getIntent();
		
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			String query = intent.getStringExtra(SearchManager.QUERY);
			Log.i("Search Bar", query);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		 // Inflate the options menu from XML
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.options_menu, menu);

	    // Get the SearchView and set the searchable configuration
	    SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
	    SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
	    // Assumes current activity is the searchable activity
	    searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
	    searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default

	    return true;
	}

    @Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
    	Intent i = new Intent(getApplicationContext(), SubCategory.class);
    	i.putExtra("name", categories.get(position).get(KEY_NAME));
    	i.putExtra("url", categories.get(position).get(KEY_URL));
    	startActivity(i);
    }
	
	public class CategoryList extends ArrayAdapter<String> {
		
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
		public CategoryList(Context context, int textViewResourceId,
				String[] objects) {
			super(context, textViewResourceId, objects);
		}
		public View getView(int position, View convertView, ViewGroup parent) {

			LayoutInflater li = getLayoutInflater();
			View row = li.inflate(R.layout.main_cat_list_item, parent, false);
			TextView label = (TextView)row. findViewById(R.id.main_cat_list_item_label);
			ImageView icon = (ImageView)row. findViewById(R.id.main_cat_list_item_icon);
			
			String name = categories.get(position).get(KEY_NAME);
			label.setText(name);
			if(position <= icons.length-1)
				icon.setImageDrawable(getResources().getDrawable(icons[position]));
			else
				icon.setImageDrawable(getResources().getDrawable(icons[icons.length]));
			
			return row;
		}
	}
	
	private class DownloadXMLTask extends AsyncTask<String, Void, Document> {

		@Override
		protected Document doInBackground(String... params) {
			String url = params[0];
			String xml = "";
			Document doc = null;
			try {
			XMLParser parser = new XMLParser();
			xml = parser.getXmlFromUrl(url);
			doc = parser.getDomElement(xml);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			return doc;
		}
		
		@Override
		protected void onPostExecute(Document result)
		{
			XMLParser parser = new XMLParser();
			NodeList nodes = result.getElementsByTagName(KEY_ITEM);
			for(int i = 0; i < nodes.getLength(); i++){
				
				HashMap<String, String> urlObject = new HashMap<String, String>();
				Element el = (Element) nodes.item(i);
				urlObject.put(KEY_NAME, parser.getValue(el, KEY_NAME));
				urlObject.put(KEY_URL, parser.getValue(el, KEY_URL));
				
				categories.add(new HashMap<String, String>(urlObject));
				Init();
			}
		}
		
		private void Init() {
			String [] list = new String[categories.size()] ;
			for(int i =0 ; i < categories.size(); i++){
				list[i] = new String(categories.get(i).get(KEY_NAME).toString());
			}
			setListAdapter(new CategoryList(getApplicationContext(), R.layout.category, list));
		}
	}
	
}
