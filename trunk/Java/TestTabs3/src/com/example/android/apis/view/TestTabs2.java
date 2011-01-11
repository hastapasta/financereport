package com.example.android.apis.view;


//import android.app.Activity;
import com.example.android.apis.R;

import android.app.TabActivity;
import android.content.Intent;
//import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

public class TestTabs2 extends TabActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        //Resources res = getResources(); // Resource object to get Drawables
        TabHost tabHost = getTabHost();  // The activity TabHost
        TabHost.TabSpec spec;  // Resusable TabSpec for each tab
        Intent intent;  // Reusable Intent for each tab

        // Create an Intent to launch an Activity for the tab (to be reused)
        intent = new Intent().setClass(this, EarningsActivity.class);

        // Initialize a TabSpec for each tab and add it to the TabHost
        /*spec = tabHost.newTabSpec("artists").setIndicator("Artists",
                          res.getDrawable(R.drawable.ic_tab_artists))
                      .setContent(intent);*/
        spec = tabHost.newTabSpec("earnings").setIndicator("Earnings")
            .setContent(intent);
        
        tabHost.addTab(spec);
        
                     
   
      

        // Do the same for the other tabs
        intent = new Intent().setClass(this, CurrencyActivity.class);
        /*spec = tabHost.newTabSpec("albums").setIndicator("Albums",
                          res.getDrawable(R.drawable.ic_tab_albums))
                      .setContent(intent);*/
        spec = tabHost.newTabSpec("currency").setIndicator("Currency")
        .setContent(intent);
        tabHost.addTab(spec);
        
        

        intent = new Intent().setClass(this, CommodityActivity.class);
        /*spec = tabHost.newTabSpec("songs").setIndicator("Songs",
                          res.getDrawable(R.drawable.ic_tab_songs))
                      .setContent(intent);*/
        spec = tabHost.newTabSpec("commodity").setIndicator("Commodity")
        .setContent(intent);
        tabHost.addTab(spec);

        tabHost.setCurrentTab(0);
        
      
    }
    
}

