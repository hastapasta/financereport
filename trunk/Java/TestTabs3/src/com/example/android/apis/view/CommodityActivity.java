package com.example.android.apis.view;


import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;



public class CommodityActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView textview = new TextView(this);
        textview.setText("This is the commodities tab");
        setContentView(textview);
    }
}