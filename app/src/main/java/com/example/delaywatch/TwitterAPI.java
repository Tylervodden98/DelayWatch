package com.example.delaywatch;

import android.util.Base64;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.example.delaywatch.HTTPUtil;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class TwitterAPI {
    private String twitterApiKey;
    private String twitterAPISecret;
    final static String TWITTER_TOKEN_URL = "https://api.twitter.com/oauth2/token";
    final static String TWITTER_STREAM_URL = "https://api.twitter.com/1.1/statuses/user_timeline.json?screen_name=";

    public TwitterAPI(String twitterAPIKey, String twitterApiSecret){
        this.twitterApiKey = twitterAPIKey;
        this.twitterAPISecret = twitterApiSecret;
    }

    public ArrayList<TweetCheck> getTwitterTweets(String screenName) {
        ArrayList<TweetCheck> tweetCheckArrayList = null;
        try {
            String twitterUrlApiKey = URLEncoder.encode(twitterApiKey, "UTF-8");
            String twitterUrlApiSecret = URLEncoder.encode(twitterAPISecret, "UTF-8");
            String twitterKeySecret = twitterUrlApiKey + ":" + twitterUrlApiSecret;
            String twitterKeyBase64 = Base64.encodeToString(twitterKeySecret.getBytes(), Base64.NO_WRAP);
            TwitterAuthToken twitterAuthToken = getTwitterAuthToken(twitterKeyBase64);
            tweetCheckArrayList = getTwitterTweets(screenName, twitterAuthToken);
        } catch (UnsupportedEncodingException ex) {
        } catch (IllegalStateException ex1) {
        }
        return tweetCheckArrayList;
    }

    public ArrayList<TweetCheck> getTwitterTweets(String screenName,
                                                    TwitterAuthToken twitterAuthToken) {
        ArrayList<TweetCheck> tweetCheckArrayList = null;
        if (twitterAuthToken != null && twitterAuthToken.token_type.equals("bearer")) {
            HttpGet httpGet = new HttpGet(TWITTER_STREAM_URL + screenName);
            httpGet.setHeader("Authorization", "Bearer " + twitterAuthToken.access_token);
            httpGet.setHeader("Content-Type", "application/json");
            HTTPUtil httpUtil = new HTTPUtil();
            String tweetCheck = httpUtil.getHttpResponse(httpGet);
            tweetCheckArrayList = convertJsonToTwitterTweet(tweetCheck);
        }
        return tweetCheckArrayList;
    }

    public TwitterAuthToken getTwitterAuthToken(String twitterKeyBase64) throws UnsupportedEncodingException {
        HttpPost httpPost = new HttpPost(TWITTER_TOKEN_URL);
        httpPost.setHeader("Authorization", "Basic " + twitterKeyBase64);
        httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
        httpPost.setEntity(new StringEntity("grant_type=client_credentials"));
        HTTPUtil httpUtil = new HTTPUtil();
        String twitterJsonResponse = httpUtil.getHttpResponse(httpPost);
        return convertJsonToTwitterAuthToken(twitterJsonResponse);
    }

    private TwitterAuthToken convertJsonToTwitterAuthToken(String jsonAuth) {
        TwitterAuthToken twitterAuthToken = null;
        if (jsonAuth != null && jsonAuth.length() > 0) {
            try {
                Gson gson = new Gson();
                twitterAuthToken = gson.fromJson(jsonAuth, TwitterAuthToken.class);
            } catch (IllegalStateException ex) { }
        }
        return twitterAuthToken;
    }

    private ArrayList<TweetCheck> convertJsonToTwitterTweet(String tweetCheck) {
        ArrayList<TweetCheck> tweetCheckArrayList = null;
        if (tweetCheck != null && tweetCheck.length() > 0) {
            try {
                Gson gson = new Gson();
                tweetCheckArrayList =
                        gson.fromJson(tweetCheck, new TypeToken<ArrayList<TweetCheck>>(){}.getType());
            } catch (IllegalStateException e) {
            }
        }
        return tweetCheckArrayList;
    }
    private class TwitterAuthToken {
        String token_type;
        String access_token;
    }
}
