package eshan.shafeeq.swinburne.guideme;

import java.util.ArrayList;
import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
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
		getDataFromInternet();
		Init();
		
	}
	private void Init() {
		String [] list = new String[categories.size()] ;
		for(int i =0 ; i < categories.size(); i++){
			list[i] = new String(categories.get(i).get(KEY_NAME).toString());
		}
		this.setListAdapter(new CategoryList(getApplicationContext(), R.layout.category, list));
		
	}

    @Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
    	Intent i = new Intent(getApplicationContext(), SubCategory.class);
    	i.putExtra("name", categories.get(position).get(KEY_NAME));
    	i.putExtra("url", categories.get(position).get(KEY_URL));
    	startActivity(i);
    }
	private void getDataFromInternet() {
		try{
			categories = new ArrayList<HashMap<String, String>>();
			XMLPraser praser = new XMLPraser();
			String xml = praser.getXmlFromUrl(URL);
			Document doc = praser.getDomElement(xml);
			NodeList nodes = doc.getElementsByTagName(KEY_ITEM);
			for(int i = 0; i < nodes.getLength(); i++){
			
				HashMap<String, String> urlObject = new HashMap<String, String>();
				Element el = (Element) nodes.item(i);
			
				urlObject.put(KEY_NAME, praser.getValue(el, KEY_NAME));
				urlObject.put(KEY_URL, praser.getValue(el, KEY_URL));
			
				categories.add(new HashMap<String, String>(urlObject));
			}
		}catch(Exception e){
			Log.d("LOL", e.toString());
		}
		
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
			//label.setText("Lol");
			else
				icon.setImageDrawable(getResources().getDrawable(icons[icons.length]));
			
			return row;
		}
	}
	
}
