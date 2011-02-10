/*
 * 
 * NOTE: DO NOT PUT A LISTVIEW INSIDE OF A SCROLLVIEW.
 * 
 * 
 */


package com.example.android.apis.view;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.android.apis.R;

import android.app.Activity;
import android.app.AlertDialog;
//import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
//import android.view.Window;
//import android.widget.Button;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TableRow.LayoutParams;
import android.widget.AdapterView;
import android.widget.ListView;








public class CurrencyActivity2 extends ParentActivity2 implements View.OnClickListener,AdapterView.OnItemSelectedListener{
	/*TableLayout tl;
	String strType;
	//private static final int DIALOG2_KEY = 1;
	ProgressDialog mDialog2;
	AlertDialog alertDialog;
	JSONArray jArray;
	int nRowCount;*/

	String[] staticFieldNames;
	
	int firstpass=1;
	
	private static final String[] mStrings = {
	    "HOURLY", "DAILY", "MONTHLY", "YEARLY", "ALLTIME"
    };
	
	String[] items={"lorem", "ipsum", "dolor", "sit", "amet",
			"consectetuer", "adipiscing", "elit", "morbi", "vel",
			"ligula", "vitae", "arcu", "aliquet", "mollis",
			"etiam", "vel", "erat", "placerat", "ante",
			"porttitor", "sodales", "pellentesque", "augue",
			"purus"};
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        
        /*
         * Temporarily commented out.
         */
        setContentView(R.layout.forex2);
        
        /*
        
        Spinner s1 = (Spinner) findViewById(R.id.spinner1);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, mStrings);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s1.setAdapter(adapter);
        s1.setOnItemSelectedListener(this);*/

        
        strActivityPHP = "forexgainerslosers.php";
        
        //nRowCount = 30;
        nRowCount = items.length;

        
        
        
        //staticFieldNames = "ticker,value,value2,changeval".split(",");
        
        
        
       
        
        //tl = (TableLayout)findViewById(R.id.myTableLayout);
        
        /*OFP
        lv = (ListView)findViewById(android.R.id.list);
        */

        //tl.setShrinkAllColumns(true); 
        
        strType = "weaker";
        
        String[] rows = {"rows",nRowCount+""};
        String[] type = {"type","weaker"};
        String[] frequency = {"freq","YEARLY"};
        pairs = new ArrayList<String[]>();
        pairs.add(rows);
        pairs.add(type);
        pairs.add(frequency);
        
        //strQueryString="?type="+strType+"&="+nRowCount;
        
        
        /*OFP
        View loginButton = findViewById(R.id.button_id);
       
        loginButton.setOnClickListener(this);
        
        TextView tvButton = (TextView)findViewById(R.id.button_id);
        tvButton.setText("show stronger");
        */
        
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
    
    	/*if (lv.getChildCount() > 1)
    		lv.removeViews(1, lv.getChildCount()-1);*/
    	super.onResume();
    	
    }
    
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        //selection.setText(items[position]);
    	/*
    	 * This event occurs when the activity created so I have to check for the first pass or
    	 * otherwise the data is loaded twice (and funky things start happening with the progress dialog).
    	 */
    	if (firstpass!=1)
    	{
	    	mDialog2 = ProgressDialog.show(this, "", "Updating...", true, false);
	    	
	    	lv.removeViews(1, nRowCount);
	    	
	    	String[] tmp = pairs.get(2);
	    	
	    	tmp[1] = mStrings[position];
	    	
	    	SearchThread searchThread = new SearchThread();
	        searchThread.start();
    	}
    	
    	firstpass=0;
    	
    	
    }
    
    public void onNothingSelected(AdapterView<?> parent) {
        //do nothing for now - may want to refresh the data for the existing selection.
    }

    
  
    
    public void onClick(View v) {
    	//showDialog(DIALOG2_KEY);
    	mDialog2 = ProgressDialog.show(this, "", "Updating...", true, false);
    	//mDialog2.requestWindowFeature(Window.FEATURE_NO_TITLE); 
    	
    	String[] tmp = pairs.get(1);

    
    	TextView tvButton = (TextView)findViewById(R.id.button_id);
        tvButton.setText("show " + tmp[1]);
    	
    

    	lv.removeViews(1, nRowCount);
    	
    	
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
    	SearchThread searchThread = new SearchThread();
        searchThread.start();

     
    }
    
    public void populate2(Activity tmpActivity,ListView lv,JSONArray jArray)
    {
    	
    			/*
    			 * Have to create and adapter (list of objects) and then use that
    			 * to populate the listview.
    			 */
    			
    			/*for(int i=0;i<nRowCount;i++)
    			{
    				TextView result=new TextView(this);

        			result.setText("Hello, world!");
        			
        			lv.addView(result);
    				
    			}*/
    			
    			/*setListAdapter(new ArrayAdapter(this,
    	        		R.layout.tablerow3, android.R.id.text1 ,
    	        		items));*/
    			
		    	/* ListAdapter adapter = new SimpleCursorAdapter(this,
		                 android.R.layout.simple_list_item_1, // Use a template
		                                                      // that displays a
		                                                      // text view
		                 c, // Give the cursor to the list adapter
		                 new String[] { ContactsContract.Contacts.DISPLAY_NAME }, // Map the NAME column in the
		                                                                          // people database to...
		                 new int[] { android.R.id.text1 }); // The "text1" view defined in
		                                                    // the XML template
		         setListAdapter(adapter);*/
    	
    	
    	
    			setListAdapter(new ArrayAdapter<String>(this,
    		                android.R.layout.simple_list_item_1, items));
    			 getListView().setTextFilterEnabled(true);
    }
    

    
    public void populate3(Activity tmpActivity,ListView lv,JSONArray jArray)
    {
    	
    			
    	
    			
                JSONObject json_data;
                TableRow tr2;
                TextView labelTV;
                String[] fieldsRow1 = "ticker,value,value2,changeval".split(",");
                
                try
                {
                
	   	             for(int i=0;i<jArray.length();i++)
	   	 			{
	   	 				json_data = jArray.getJSONObject(i);
	   	 				/*Log.i("log_tag","ticker: "+json_data.getString("ticker")+
	   	 						", value2: "+json_data.getDouble("value2")+
	   	 						", value: "+json_data.getDouble("value")+
	   	 						", frequency: "+json_data.getString("frequency"));*/
	   	 			
	   	             
	   	             /*OFP - create a text view */
		   	             tr2 = new TableRow(tmpActivity);
		   	             //tr2.setHorizontalScrollBarEnabled(false);
		   	             
		   	             
		   	             for(int j=0;j<fieldsRow1.length;j++)
		   	             {
		   	             
			   	             labelTV = new TextView(tmpActivity);
			   	             labelTV.setId(3000 + (100*i) + j);
			   	             labelTV.setText(json_data.getString(fieldsRow1[j]));
			   	             labelTV.setTextColor(Color.WHITE);
			   	             labelTV.setLayoutParams(new LayoutParams(
			   	                     LayoutParams.FILL_PARENT,
			   	                     LayoutParams.WRAP_CONTENT));
			   	             labelTV.setHorizontallyScrolling(false);
			   	             labelTV.setSingleLine(false);
			   	             labelTV.setPadding(3,3,3,3);
			   	           
			   	             tr2.addView(labelTV);
			   	             
		   	             }
		   	             
		   	             if (i%2==0)
		 	            	 tr2.setBackgroundResource(R.color.translucent_blue_green);
		   	             
		   	      
		   	             
		   	      
		   	             
		   	           /*  tl.addView(tr2,new TableLayout.LayoutParams(
		   	                     LayoutParams.FILL_PARENT,
		   	                     LayoutParams.WRAP_CONTENT));*/
		   	             
	
		   	      
		   	             
		   	      
		   	             
		   	             lv.addView(tr2,new ListView.LayoutParams(
		   	                     LayoutParams.FILL_PARENT,
		   	                     LayoutParams.WRAP_CONTENT));
	   	 			}
                }
                catch(JSONException e)
                {
               	 Log.e("log_tag", "Error parsing data "+e.toString());
                }
    }
                
    
   
         
    
    
   
}


