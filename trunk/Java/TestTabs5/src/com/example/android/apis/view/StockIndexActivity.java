package com.example.android.apis.view;


import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;



public class StockIndexActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView textview = new TextView(this);
        textview.setText("This is the stock index tab");
        setContentView(textview);
    }
}