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





public class CurrencyActivity extends ParentActivity implements View.OnClickListener{
	/*TableLayout tl;
	String strType;
	//private static final int DIALOG2_KEY = 1;
	ProgressDialog mDialog2;
	AlertDialog alertDialog;
	JSONArray jArray;
	int nRowCount;*/

	String[] staticFieldNames;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.forex);
        
        strActivityPHP = "forexgainerslosers.php";
        
        nRowCount = 30;

        
        
        
        //staticFieldNames = "ticker,value,value2,changeval".split(",");
        
        
        
       
        
        tl = (TableLayout)findViewById(R.id.myTableLayout);
        
        //tl.setShrinkAllColumns(true); 
        
        strType = "weaker";
        
        String[] rows = {"rows",nRowCount+""};
        String[] type = {"type","weaker"};
        pairs = new ArrayList<String[]>();
        pairs.add(rows);
        pairs.add(type);
        
        strQueryString="?type="+strType+"&="+nRowCount;
        
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
    	if (tl.getChildCount() > 1)
    		tl.removeViews(1, tl.getChildCount()-1);
    	super.onResume();
    	
    }
    
  
    
    public void onClick(View v) {
    	//showDialog(DIALOG2_KEY);
    	mDialog2 = ProgressDialog.show(this, "", "Updating...", true, false);
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
    	
    	strQueryString="?type="+strType+"&="+nRowCount;
    	
    	
    	//populate(tl);
    	//dismissDialog(DIALOG2_KEY);
    	
    	//mDialog2.dismiss();
    	SearchThread searchThread = new SearchThread();
        searchThread.start();

     
    }
    
    
    

    
    public void populate2(Activity tmpActivity,TableLayout tl,JSONArray jArray)
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
                
         
    
    
   
}


