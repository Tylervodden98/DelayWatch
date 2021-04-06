package com.example.delaywatch;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;

public class TParserActivity extends ListActivity {
    final static String twitterScreenName = "TTCnotices";
    final static String TAG = "MainActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidNetworkUtility androidNetworkUtility = new AndroidNetworkUtility();
        if (androidNetworkUtility.isConnected(this)) {
            new TwitterAsyncTask().execute(twitterScreenName,this);
        } else {
            Log.v(TAG, "Network not Available!");
        }
    }
}