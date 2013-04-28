package my.com.swinburne.guide_me;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class SubCategory extends ListActivity{
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
	
	private String URL = "";
	private String Name = "";
	private ArrayList<HashMap<String, String>> subcats;
	private String [] objects;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		init();
		new DownloadXMLTask().execute(URL);
	}
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
    	Intent i = new Intent(getApplicationContext(), Details.class);
    	ArrayList<String> info = new ArrayList<String>();
    	
		try{
			info.add(subcats.get(position).get(KEY_NAME).toString());
		}catch(NullPointerException e){

		}
    	try{
    		info.add(subcats.get(position).get(KEY_LOC).toString());
    	}catch(NullPointerException e){

    	}
    	try{
    		info.add(subcats.get(position).get(KEY_DESC).toString());
    	}catch(NullPointerException e){

    	}
    	try{
    		info.add(subcats.get(position).get(KEY_PRICE).toString());
    	}catch(NullPointerException e){

    	}
    	try{
    		info.add(subcats.get(position).get(KEY_OPEN).toString());
    	}catch(NullPointerException e){

    	}
    	try{
    		info.add(subcats.get(position).get(KEY_CONT).toString());
    	}catch(NullPointerException e){

    	}
    	try{
    		info.add(subcats.get(position).get(KEY_LONG).toString());
    	}catch(NullPointerException e){

    	}
    	try{
    		info.add(subcats.get(position).get(KEY_LAT).toString());
    	}catch(NullPointerException e){

    	}
    	try{
    		info.add(subcats.get(position).get(KEY_WEB).toString());
    	}catch(NullPointerException e){

    	}
    	try{
    		info.add(subcats.get(position).get(KEY_EMAIL).toString());
    	}catch(NullPointerException e){

    	}

    	
    	i.putStringArrayListExtra("info", info);
    	startActivity(i);

    }

	private void init() {
		Intent i = getIntent();
		this.Name = i.getStringExtra(KEY_NAME);
		URL = i.getStringExtra(KEY_URL);
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
