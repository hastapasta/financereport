package com.example.android.apis.view;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.android.apis.R;

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
import android.view.LayoutInflater;
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
	//private static final int DIALOG2_KEY = 1;	ProgressDialog mDialog2;
	ProgressDialog mDialog2;
	AlertDialog alertDialog;
	JSONArray jArray;
	int nRowCount;
	String[] fieldsRow1;
	String[] fieldsRow2;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        nRowCount = 30;
        fieldsRow1 = "ticker,Q1:2011,Q2:2011,Q3:2011,Q4:2011,total_eps".split(",");
        fieldsRow2 = "earnings,share_price".split(",");
        
        setContentView(R.layout.earnings);
        
        tl = (TableLayout)findViewById(R.id.myTableLayout);
        
        //tl.setShrinkAllColumns(true); 
        
        
        strType = "weaker";
        
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
    	super.onResume();
    	mDialog2 = ProgressDialog.show(this, "", "Updating...", true, false);
    	//tl.removeViews(1, nRowCount);
    	if (tl.getChildCount() > 0)
    		tl.removeViews(0, tl.getChildCount());
    

    	SearchThread searchThread = new SearchThread(strType);
        searchThread.start();
    	
    }
    
  
    
    public void onClick(View v) {
    	//showDialog(DIALOG2_KEY);
    	mDialog2 = ProgressDialog.show(this, "", "Updating...", true, false);
    	//mDialog2.requestWindowFeature(Window.FEATURE_NO_TITLE); 

    
    	TextView tvButton = (TextView)findViewById(R.id.button_id);
        tvButton.setText("show " + strType);
    	
    

    	//tl.removeViews(1, nRowCount);
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
    
    public void populate2(Activity tmpActivity,TableLayout tl,String[] fieldNames,String[] fieldNames2,JSONArray jArray)
    {
    	

                
                JSONObject json_data=null;
                TableRow tr2;
                TableRow row;
                TextView labelTV;
                LayoutInflater inflater = tmpActivity.getLayoutInflater(); 
                
                try
                {
                
   	             //for(int i=0;i<jArray.length();i++)
   	            	for(int i=0;i<nRowCount+1;i++)
   	 			{
   	 				
   	 				/*Log.i("log_tag","ticker: "+json_data.getString("ticker")+
   	 						", value2: "+json_data.getDouble("value2")+
   	 						", value: "+json_data.getDouble("value")+
   	 						", frequency: "+json_data.getString("frequency"));*/
   	 			
   	          
   	             row = (TableRow)inflater.inflate(R.layout.tablerow1, tl, false);
   	     
   	             /*OFP - create a text view */
   	             //tr2 = new TableRow(tmpActivity);
   	 			/*	if (i==0)
   	 				{
   	 					row = (TableRow)tmpActivity.findViewById(R.id.tablerow1);
   	 				}
   	 				else
   	 				{
   	 					inflater = tmpActivity.getLayoutInflater();
   	 					row = (TableRow)inflater.inflate(R.layout.tablerow, tl, false);
   	 				}*/
   	             //TableRow row2 = (TableRow)inflater.inflate(R.id.tablerow, tl, false);
   	             	TextView content;
   	             	if (i!=0)
   	             	{
   	             
	   	 				
   	             		json_data = jArray.getJSONObject(i-1);
	   	 				 
	   	 			
	   	            	content = (TextView)row.findViewById(R.id.content0);
	   	            	content.setText(json_data.getString(fieldNames[0]));
	   	            	content.setTextColor(Color.WHITE);
	   	            	//tl.addView(row);
	   	            	content = (TextView)row.findViewById(R.id.content01);
	   	            	content.setText(json_data.getString(fieldNames[1]));
	   	            	content.setTextColor(Color.WHITE);
	   	            	
	   	            	content = (TextView)row.findViewById(R.id.content02);
	   	            	content.setText(json_data.getString(fieldNames[2]));
	   	            	content.setTextColor(Color.WHITE);
	   	            	
	   	            	content = (TextView)row.findViewById(R.id.content03);
	   	            	content.setText(json_data.getString(fieldNames[3]));
	   	            	content.setTextColor(Color.WHITE);
	   	            	
	   	            	content = (TextView)row.findViewById(R.id.content04);
	   	            	content.setText(json_data.getString(fieldNames[4]));
	   	            	content.setTextColor(Color.WHITE);
   	             	}
   	             	
	   	             tl.addView(row,new TableLayout.LayoutParams(
	   	                     LayoutParams.FILL_PARENT,
	   	                     LayoutParams.WRAP_CONTENT));
	   	             	
	   	          row = (TableRow)inflater.inflate(R.layout.tablerow2, tl, false);
   	            
   	            	/*if (i==0)
   	 				{
   	 					row = (TableRow)tmpActivity.findViewById(R.id.tablerow2);
   	 				}
   	 				else
   	 				{
   	 					row = (TableRow)inflater.inflate(R.id.tablerow2, tl, false);
   	 				}*/
   	            	//row = (TableRow)tmpActivity.findViewById(R.id.tablerow2);
	   	            if (i!=0)
	   	            {
		   	         	content = (TextView)row.findViewById(R.id.content05);
	   	            	content.setText(json_data.getString(fieldNames[5]));
	   	            	content.setTextColor(Color.WHITE);
	   	            	
	   	            	
	   	            	content = (TextView)row.findViewById(R.id.content020);
	   	            	content.setText(json_data.getString(fieldNames2[0]));
	   	            	content.setTextColor(Color.WHITE);
	   	            	
	   	            	content = (TextView)row.findViewById(R.id.content021);
	   	            	content.setText(json_data.getString(fieldNames2[1]));
	   	            	content.setTextColor(Color.WHITE);
	   	            }
	   	            
		   	         tl.addView(row,new TableLayout.LayoutParams(
	   	                     LayoutParams.FILL_PARENT,
	   	                     LayoutParams.WRAP_CONTENT));
   	 				
   	            	
   	            	
   	          
   	             
   	      
   	             
   	      
   	             /*if (i!=0)
   	             {
	   	            tl.addView(row,new TableLayout.LayoutParams(
	   	                     LayoutParams.FILL_PARENT,
	   	                     LayoutParams.WRAP_CONTENT));
   	             }*/
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
                               populate2(EarningsActivity.this,tl,EarningsActivity.this.fieldsRow1,EarningsActivity.this.fieldsRow2,jArray);
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
        		EarningsActivity.this.jArray = MySQLConnect.connect(EarningsActivity.this.getString(R.string.json_url) + "pebymarketcap.php",strType);
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


