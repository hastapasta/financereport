package com.example.android.apis.view;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.android.apis.R;

public class CurrencyActivity extends ParentActivity implements View.OnClickListener,AdapterView.OnItemSelectedListener{
	/*TableLayout tl;
	String strType;
	//private static final int DIALOG2_KEY = 1;
	ProgressDialog mDialog2;
	AlertDialog alertDialog;
	JSONArray jArray;
	int nRowCount;*/

	String[] staticFieldNames;
	private ListView myListView;
	
	int firstpass=1;
	
	private static final String[] mStrings = {
	    "HOURLY", "DAILY", "MONTHLY", "YEARLY", "ALLTIME"
    };
	
    public void onCreate(Bundle savedInstanceState) {
    	Log.i("CurrencyActivity", "oncreate");
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.forex);
        
        Spinner s1 = (Spinner) findViewById(R.id.spinner1);
        
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, mStrings);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s1.setAdapter(adapter);
        s1.setOnItemSelectedListener(this);
        
        strActivityPHP = "forexgainerslosers.php";
        
        nRowCount = 30;

        //staticFieldNames = "ticker,value,value2,changeval".split(",");
        
        //tl = (TableLayout)findViewById(R.id.myTableLayout);
        
        //tl.setShrinkAllColumns(true); 
        
        strType = "weaker";
        
        String[] rows = {"rows",nRowCount+""};
        String[] type = {"type","weaker"};
        String[] frequency = {"freq","YEARLY"};
        pairs = new ArrayList<String[]>();
        pairs.add(rows);
        pairs.add(type);
        pairs.add(frequency);
        
        strQueryString="?type="+strType+"&="+nRowCount;
        
        View loginButton = findViewById(R.id.button_id);
       
        loginButton.setOnClickListener(this);
        
        TextView tvButton = (TextView)findViewById(R.id.button_id);
        tvButton.setText("show stronger");

        //connect variable to layout listView        
        myListView=(ListView)this.findViewById(R.id.listView);
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent startNewActivity=new Intent(getBaseContext(),dummyClass.class);
				startActivity(startNewActivity);
				
			}
		});

        
       // try
        //{
        	//jArray = MySQLConnect.connect(this.getString(R.string.json_url) + "forexgainerslosers.php",strType);
        	//CommonFunctions.populate(this,tl,this.staticFieldNames,jArray);
     //   }
        //catch (CustomConnectionException cce)
       // {
        	/*alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Alert");
            alertDialog.setMessage("Unable to retrieve data.");
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int which) {
                return;
              }
            });
            alertDialog.show();*/
     //   }
        
        
        /*TextView textview = new TextView(this);
        textview.setText("This is the Albums tab");
        setContentView(textview);*/
    }
    
    public void onResume()
    {
    	Log.i("CurrencyActivity", "onresume");
    	//if (tl.getChildCount() > 1)
    	//	tl.removeViews(1, tl.getChildCount()-1);
    	
    	super.onResume();
    	
    }
    
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
    	Log.i("CurrencyActivity", "onItemSelected");
        //selection.setText(items[position]);
    	/*
    	 * This event occurs when the activity created so I have to check for the first pass or
    	 * otherwise the data is loaded twice (and funky things start happening with the progress dialog).
    	 */
    	/*if (firstpass!=1)
    	{
	    	mDialog2 = ProgressDialog.show(this, "", "Updating...", true, false);
	    	
	    	tl.removeViews(1, nRowCount);
	    	
	    	String[] tmp = pairs.get(2);
	    	
	    	tmp[1] = mStrings[position];
	    	
	    	SearchThread searchThread = new SearchThread();
	        searchThread.start();
    	}
    	
    	firstpass=0;*/
    	
    	
    }
    
    public void onNothingSelected(AdapterView<?> parent) {
        //do nothing for now - may want to refresh the data for the existing selection.
    }

    public void onClick(View v) {
    	Log.i("CurrencyActivity", "onClick");
    	//showDialog(DIALOG2_KEY);
    	/*mDialog2 = ProgressDialog.show(this, "", "Updating...", true, false);
    	//mDialog2.requestWindowFeature(Window.FEATURE_NO_TITLE); 
    	
    	String[] tmp = pairs.get(1);

    
    	TextView tvButton = (TextView)findViewById(R.id.button_id);
        tvButton.setText("show " + tmp[1]);
    	
    

    	tl.removeViews(1, nRowCount);
    	
    	
    	if (tmp[1].equals("weaker"))
    	{
    		tmp[1] = "stronger";
    	}
    	else
    		tmp[1] = "weaker";
    	
    	/*if (strType.equals("weaker"))
    		strType = "stronger";
    	else
    		strType="weaker";*/
    	
    	//strQueryString="?type="+strType+"&="+nRowCount;
    	
    	
    	//populate(tl);
    	//dismissDialog(DIALOG2_KEY);
    	
    	//mDialog2.dismiss();
    	/*SearchThread searchThread = new SearchThread();
        searchThread.start();
        */
    }
        
    public void populate2(Activity tmpActivity, JSONArray jArray)
    {
    	Log.i("CurrencyActivity", "populate2");    	
        JSONObject[] json_data=new JSONObject[jArray.length()];
        
        try
        {
        	for(int i=0;i<jArray.length();i++)
        	{
	   	 		json_data[i] = jArray.getJSONObject(i);	//get all JSONObjects from JSONArray	   	 				
	   	 	}
        	
        	//set listview adapter
        	myListView.setAdapter(new listRecordAdapter(tmpActivity, json_data));
        	Log.i("currency activity", "populate2: adapter set");
        }
        catch(JSONException e)
        {
        	Log.e("currency activity", "populate2: Error parsing data "+e.toString());
        }
    }
    
  //artist event custom list adapter
	public class listRecordAdapter extends BaseAdapter{
		private JSONObject[] listOf_Records=null;
		private Context context;
		private TextView ticker,value,value2,changeval;

		public listRecordAdapter(Context context,JSONObject[] listOf_Records){
			Log.i("Concerts", "artistEventAdapter: constructor");
			this.listOf_Records=listOf_Records;
			this.context=context;
		}
		
		@Override
		public int getCount() {			
			return listOf_Records.length;
		}

		@Override
		public Object getItem(int position) {			
			return listOf_Records[position];
		}

		@Override
		public long getItemId(int position) {			
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Log.i("Concerts", "artistListAdapter: getView");
			if(convertView==null){
				convertView=LayoutInflater.from(context).inflate(R.layout.forex_listview_item, null);
			}
			try{
				
				ticker=(TextView)convertView.findViewById(R.id.ticker);
				ticker.setText(this.listOf_Records[position].getString("ticker"));
				Log.i("CurrentActivity", "listRecordAdapter: getView ticket->"+ticker.getText());
				
				value=(TextView)convertView.findViewById(R.id.value);			
				value.setText(this.listOf_Records[position].getString("value"));
				Log.i("CurrentActivity", "listRecordAdapter: getView value->"+value.getText());
				
				value2=(TextView)convertView.findViewById(R.id.value2);			
				value2.setText(this.listOf_Records[position].getString("value2"));
				Log.i("CurrentActivity", "listRecordAdapter: getView value2->"+value2.getText());
				
				changeval=(TextView)convertView.findViewById(R.id.changeval);			
				changeval.setText(this.listOf_Records[position].getString("changeval"));
				Log.i("CurrentActivity", "listRecordAdapter: getView changeval->"+changeval.getText());
			}catch(Exception ex){
				Log.i("CurrencyActivity", "listRecordAdapter: getView exiting with exception message->"+ex.getMessage());
			}
			return convertView;
		}
		
	}
        
}
