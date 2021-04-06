package com.example.delaywatch;

import android.app.ListActivity;
import android.text.method.LinkMovementMethod;
import android.widget.ArrayAdapter;
import android.os.AsyncTask;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class TwitterAsyncTask extends AsyncTask<Object, Void, ArrayList<TweetCheck>> {
    ListActivity callerActivity;

    final static String TWITTER_API_KEY = "DXt2abzp45nxMovM4LJYrqn5q";
    final static String TWITTER_API_SECRET = "xuQsVGprEXkZ86xhVmOziHliqxbzV6sEtTkp9rROeRi0oXdhBX";

    @Override
    protected ArrayList<TweetCheck> doInBackground(Object... params) {
        ArrayList<TweetCheck> tweetCheck = null;
        callerActivity = (ListActivity) params[1];
        if (params.length > 0) {
            TwitterAPI twitterAPI = new TwitterAPI(TWITTER_API_KEY,TWITTER_API_SECRET);
            tweetCheck = twitterAPI.getTwitterTweets(params[0].toString());
        }
        return tweetCheck;
    }

    @Override
    protected void onPostExecute(ArrayList<TweetCheck> tweetCheck) {
        ArrayAdapter<TweetCheck> adapter = new ArrayAdapter<TweetCheck>(callerActivity, R.layout.list_item_card, R.id.card_listView, tweetCheck);
        callerActivity.setListAdapter(adapter);
        ListView lv = callerActivity.getListView();
        lv.setDividerHeight(0);
        //lv.setDivider(this.getResources().getDrawable(android.R.color.transparent));
        lv.setBackgroundColor(callerActivity.getResources().getColor(R.color.blue));
    }
}
