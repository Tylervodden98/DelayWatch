package com.example.delaywatch;

import com.example.delaywatch.TweetCheck;

import java.util.ArrayList;

public class TwitterAsyncTask extends AsyncTask<Object, Void, ArrayList<TweetCheck>> {
    @Override
    protected ArrayList<TweetCheck> doInBackground(Object... objects) {
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<TweetCheck> twitterTweets) {
        super.onPostExecute(twitterTweets);
    }
}
