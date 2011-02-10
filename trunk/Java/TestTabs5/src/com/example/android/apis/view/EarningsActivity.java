package com.example.android.apis.view;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.apis.R;
import com.example.android.apis.view.CurrencyActivity.listRecordAdapter;

public class EarningsActivity extends ParentActivity {
	//TableLayout tl;
	//String strType;
	//private static final int DIALOG2_KEY = 1;	ProgressDialog mDialog2;
	//ProgressDialog mDialog2;
	/*AlertDialog alertDialog;
	JSONArray jArray;
	int nRowCount;
	String[] fieldsRow1;
	String[] fieldsRow2;
	String strErrorMessage;*/
	
	private ListView myListView=null;
	
	   /**
     * Safe to hold on to this.
     */
    private Menu mMenu;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        nRowCount = 30;
        fieldsRow1 = "ticker,Q1:2011,Q2:2011,Q3:2011,Q4:2011,total_eps".split(",");
        fieldsRow2 = "earnings,share_price".split(",");
        strActivityPHP = "pebymarketcap.php";
        setContentView(R.layout.earnings);
        strQueryString="";
        
        //tl = (TableLayout)findViewById(R.id.myTableLayout);
        
        myListView=(ListView)findViewById(R.id.earninglistView);
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent startNewActivity=new Intent(getBaseContext(),dummyClass.class);
				startActivity(startNewActivity);
				
			}
		});
        
        //tl.setShrinkAllColumns(true); 
        
        
        //strType = "weaker";
        
        //addPreferencesFromResource(R.xml.default_values1);
        
      /*  View loginButton = findViewById(R.id.button_id);
       
        loginButton.setOnClickListener(this);
        
        TextView tvButton = (TextView)findViewById(R.id.button_id);
        tvButton.setText("show stronger");*/
        
       /* try
        {
        	//jArray = MySQLConnect.connect(this.getString(R.string.json_url) + "pebymarketcap.php",strType);
        	//CommonFunctions.populate2(this,tl,this.fieldsRow1,this.fieldsRow2,jArray);
        }
        catch (CustomConnectionException cce)
        {*/
        	/*alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Alert");
            alertDialog.setMessage("Unable to retrieve data.");
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int which) {
                return;
              }
            });
            alertDialog.show();*/
        //}
        
        
        /*TextView textview = new TextView(this);
        textview.setText("This is the Albums tab");
        setContentView(textview);*/
    }
    
    public void onResume()
    {
    	//if (tl.getChildCount() > 0)
    	//	tl.removeViews(0, tl.getChildCount());
    	super.onResume();
    }
    
    public void populate2(Activity tmpActivity, JSONArray jArray)
    {
		    	String[] fieldNames = "ticker,Q1:2011,Q2:2011,Q3:2011,Q4:2011,total_eps".split(",");
		        String[] fieldNames2 = "earnings,share_price".split(",");
                JSONObject[] json_data=new JSONObject[jArray.length()];
                
                try
                {
                	for(int i=0;i<jArray.length();i++)
                	{
        	   	 		json_data[i] = jArray.getJSONObject(i);	//get all JSONObjects from JSONArray	   	 				
        	   	 	}
                	
                	myListView.setAdapter(new listRecordAdapter(tmpActivity, json_data));
                	Log.i("currency activity", "populate2: adapter set");
                }	//try block ends
                catch(JSONException e)
                {
               	 Log.e("log_tag", "Error parsing data "+e.toString());
                }
    }	//populate2 ends
    
  //artist event custom list adapter
	public class listRecordAdapter extends BaseAdapter{
		private JSONObject[] listOf_Records=null;
		private Context context;
		private TextView ticker,q1_2011,q2_2011,q3_2011,q4_2011,total_eps;

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
				convertView=LayoutInflater.from(context).inflate(R.layout.tablerow1, null);
			}
			try{
				
				ticker=(TextView)convertView.findViewById(R.id.eticker);
				ticker.setText(this.listOf_Records[position].getString("ticker"));
				Log.i("CurrentActivity", "listRecordAdapter: getView ticket->"+ticker.getText());
				
				q1_2011=(TextView)convertView.findViewById(R.id.q1_2011);			
				q1_2011.setText(this.listOf_Records[position].getString("Q1:2011"));
				Log.i("CurrentActivity", "listRecordAdapter: getView Q1_2011->"+q1_2011.getText());
				
				q2_2011=(TextView)convertView.findViewById(R.id.q2_2011);			
				q2_2011.setText(this.listOf_Records[position].getString("Q2:2011"));
				Log.i("CurrentActivity", "listRecordAdapter: getView Q2_2011->"+q2_2011.getText());
				
				q3_2011=(TextView)convertView.findViewById(R.id.q3_2011);			
				q3_2011.setText(this.listOf_Records[position].getString("Q3:2011"));
				Log.i("CurrentActivity", "listRecordAdapter: getView Q3_2011->"+q3_2011.getText());
				
				q4_2011=(TextView)convertView.findViewById(R.id.q4_2011);			
				q4_2011.setText(this.listOf_Records[position].getString("Q4:2011"));
				Log.i("CurrentActivity", "listRecordAdapter: getView Q4_2011->"+q4_2011.getText());
			}catch(Exception ex){
				Log.i("CurrencyActivity", "listRecordAdapter: getView exiting with exception message->"+ex.getMessage());
			}
			return convertView;
		}
		
	}
    
}


