package com.pikefin.test;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
//import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
//import android.view.Window;
//import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TableRow.LayoutParams;





public class EarningsActivity extends Activity implements View.OnClickListener{
	TableLayout tl;
	String strType;
	//private static final int DIALOG2_KEY = 1;
	ProgressDialog mDialog2;
	AlertDialog alertDialog;
	static JSONArray jArray;
	static int nRowCount=30;
	static String[] staticFieldNames = {"ticker","Q1:2011","Q2:2011","Q3:2011","Q4:2011","total_eps","shares_outstanding","earnings","share_price"};
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.earnings);
        
        tl = (TableLayout)findViewById(R.id.myTableLayout);
        
        strType = "weaker";
        
        View loginButton = findViewById(R.id.button_id);
       
        loginButton.setOnClickListener(this);
        
        TextView tvButton = (TextView)findViewById(R.id.button_id);
        tvButton.setText("show stronger");
        
        try
        {
        	jArray = MySQLConnect.connect(this.getString(R.string.json_url) + "pebymarketcap.php",strType);
        	populate(tl,EarningsActivity.staticFieldNames);
        }
        catch (CustomConnectionException cce)
        {
        	/*alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Alert");
            alertDialog.setMessage("Unable to retrieve data.");
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int which) {
                return;
              }
            });
            alertDialog.show();*/
        }
        
        
        /*TextView textview = new TextView(this);
        textview.setText("This is the Albums tab");
        setContentView(textview);*/
    }
    
    public void onResume()
    {
    	super.onResume();
    	mDialog2 = ProgressDialog.show(this, "", "Updating...", true, false);
    	tl.removeViews(1, nRowCount);

    	SearchThread searchThread = new SearchThread(strType);
        searchThread.start();
    	
    }
    
  
    
    public void onClick(View v) {
    	//showDialog(DIALOG2_KEY);
    	mDialog2 = ProgressDialog.show(this, "", "Updating...", true, false);
    	//mDialog2.requestWindowFeature(Window.FEATURE_NO_TITLE); 

    
    	TextView tvButton = (TextView)findViewById(R.id.button_id);
        tvButton.setText("show " + strType);
    	
    

    	tl.removeViews(1, nRowCount);
    	if (strType.equals("weaker"))
    		strType = "stronger";
    	else
    		strType="weaker";
    	
    	
    	//populate(tl);
    	//dismissDialog(DIALOG2_KEY);
    	
    	//mDialog2.dismiss();
    	SearchThread searchThread = new SearchThread(strType);
        searchThread.start();

     
    }
    
    
    

    
    public void populate(TableLayout tl,String[] fieldNames)
    {
    	
    	
    	
    	   //JSONArray jArray = MySQLConnect.connect(strType);
    	   
    	   
           
           /* Create a new row to be added. */
           /*TableRow tr = new TableRow(this);
           tr.setLayoutParams(new LayoutParams(
                          LayoutParams.FILL_PARENT,
                          LayoutParams.WRAP_CONTENT));
                #* Create a Button to be the row-content. *#
                Button b = new Button(this);
                b.setText("Dynamic Button");
                b.setLayoutParams(new LayoutParams(
                          LayoutParams.FILL_PARENT,
                          LayoutParams.WRAP_CONTENT));
                tr.addView(b);*/
                
                JSONObject json_data;
                TableRow tr2;
                TextView labelTV;
                
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
   	             tr2 = new TableRow(this);
   	             
   	             for(int j=0;j<fieldNames.length;j++)
   	             {
   	             
	   	             labelTV = new TextView(this);
	   	             labelTV.setId(3000 + (100*i) + j);
	   	             labelTV.setText(json_data.getString(fieldNames[j]));
	   	             labelTV.setTextColor(Color.WHITE);
	   	             labelTV.setLayoutParams(new LayoutParams(
	   	                     LayoutParams.FILL_PARENT,
	   	                     LayoutParams.WRAP_CONTENT));
	   	             tr2.addView(labelTV);
   	             }
   	             
   	             /*labelTV = new TextView(this);
   	             labelTV.setId(301+i);
   	             labelTV.setText(json_data.getString("Q1:2011"));
   	             labelTV.setTextColor(Color.WHITE);
   	             labelTV.setLayoutParams(new LayoutParams(
   	                     LayoutParams.FILL_PARENT,
   	                     LayoutParams.WRAP_CONTENT));
   	             tr2.addView(labelTV);
   	             
   	             labelTV = new TextView(this);
   	             labelTV.setId(302+i);
   	             labelTV.setText(json_data.getString("Q2:2011"));
   	             //labelTV.setText("This is a test of some really long text.");
   	             labelTV.setTextColor(Color.WHITE);
   	             labelTV.setLayoutParams(new LayoutParams(
   	                     LayoutParams.FILL_PARENT,
   	                     LayoutParams.WRAP_CONTENT));
   	             labelTV.setSingleLine(false);
   	             tr2.addView(labelTV);
   	             
   	             labelTV = new TextView(this);
   	             labelTV.setId(303+i);
   	             labelTV.setText(json_data.getString("Q3:"));
   	             labelTV.setTextColor(Color.WHITE);
   	             labelTV.setLayoutParams(new LayoutParams(
   	                     LayoutParams.FILL_PARENT,
   	                     LayoutParams.WRAP_CONTENT));
   	             tr2.addView(labelTV);*/
   	             
   	      
   	             
   	             tl.addView(tr2,new TableLayout.LayoutParams(
   	                     LayoutParams.FILL_PARENT,
   	                     LayoutParams.WRAP_CONTENT));
   	 			}
                }
                catch(JSONException e)
                {
               	 Log.e("log_tag", "Error parsing data "+e.toString());
                }
    }
                
                private Handler handler2 = new Handler() {

                    @Override
                    public void handleMessage(Message msg)
                    {
                   
                        //displaySearchResults();
                    	switch(msg.what){
                        case 0:
                               /*Refresh UI*/
                               //Log.i("blap","blap");
                               populate(tl,EarningsActivity.staticFieldNames);
                               break;
                        case 1:
                        	alertDialog = new AlertDialog.Builder(EarningsActivity.this).create();
                            alertDialog.setTitle("Alert");
                            alertDialog.setMessage("Unable to retrieve data.");
                            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                              public void onClick(DialogInterface dialog, int which) {
                                return;
                              }
                            });
                            alertDialog.show();
                        	
                        	break;
                      }
                   

                    	
                    }
                    
                };
    
    
    
    private class SearchThread extends Thread {

        //private Search search;
    	String strType;
    	//TableLayout tl;

        public SearchThread(String tmp) {
           this.strType=tmp;
           //this.tl = tmp2;
        }

        @Override
        public void run() {       
        	
        	//tl.removeViews(1, 20);
        	//retrieve(tl);
        	try
        	{
        		EarningsActivity.jArray = MySQLConnect.connect(EarningsActivity.this.getString(R.string.json_url) + "pebymarketcap.php",strType);
        		handler.sendEmptyMessage(0);
        	}
        	catch (CustomConnectionException cce)
        	{
        		handler.sendEmptyMessage(1);
        	}
        	

           
        }

       private Handler handler = new Handler() {

            @Override
            public void handleMessage(Message msg)
            {
            
                //displaySearchResults();
              	mDialog2.dismiss();
              	handler2.sendEmptyMessage(0);
            }
            
        };
    }
  
}


