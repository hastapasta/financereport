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
import android.view.View;
//import android.view.Window;
//import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TableRow.LayoutParams;





public class CurrencyActivity extends Activity implements View.OnClickListener{
	TableLayout tl;
	String strType;
	//private static final int DIALOG2_KEY = 1;
	ProgressDialog mDialog2;
	AlertDialog alertDialog;
	JSONArray jArray;
	int nRowCount;
	String[] staticFieldNames;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.forex);
        
        staticFieldNames = "ticker,value,value2,changeval".split(",");
        nRowCount = 10;
        
        tl = (TableLayout)findViewById(R.id.myTableLayout);
        
        //tl.setShrinkAllColumns(true); 
        
        strType = "weaker";
        
        View loginButton = findViewById(R.id.button_id);
       
        loginButton.setOnClickListener(this);
        
        TextView tvButton = (TextView)findViewById(R.id.button_id);
        tvButton.setText("show stronger");
        
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
    	super.onResume();
    	mDialog2 = ProgressDialog.show(this, "", "Updating...", true, false);
    	/*The button is in the first row. Don't want to remove that.*/
    	if (tl.getChildCount() > 1)
    		tl.removeViews(1, tl.getChildCount()-1);

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
    
    
    

    
    static public void populate(Activity tmpActivity,TableLayout tl,String[] fieldsRow1,JSONArray jArray)
    {
    	

                
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
			   	             tr2.addView(labelTV);
			   	             
		   	             }
		   	             
		   	      
		   	             
		   	      
		   	             
		   	           /*  tl.addView(tr2,new TableLayout.LayoutParams(
		   	                     LayoutParams.FILL_PARENT,
		   	                     LayoutParams.WRAP_CONTENT));*/
		   	             
	
		   	      
		   	             
		   	      
		   	             
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
                        	populate(CurrencyActivity.this,tl,CurrencyActivity.this.staticFieldNames,jArray);
                            break;
                        case 1:
                        	alertDialog = new AlertDialog.Builder(CurrencyActivity.this).create();
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
        		CurrencyActivity.this.jArray = MySQLConnect.connect(CurrencyActivity.this.getString(R.string.json_url) + "forexgainerslosers.php",strType);
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


