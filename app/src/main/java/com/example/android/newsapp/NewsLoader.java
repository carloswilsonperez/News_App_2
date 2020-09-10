package com.example.android.newsapp;


import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Loads a list of news items by using an AsyncTask to perform the
 * network request to the given URL.
 */
public class NewsLoader extends AsyncTaskLoader<List<News>> {

    /** Tag for log messages */
    private static final String LOG_TAG = NewsLoader.class.getName();
    private Context mContext;

    /**
     * Constructs a new {@link NewsLoader}.
     *
     * @param context of the activity
     */
    public NewsLoader(Context context) {

        super(context);
        mContext = context;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     * This is on a background thread.
     */
    @Override
    public List<News> loadInBackground() {
        // Perform the network request, parse the response, and extract a list of news.
        List<News> news = QueryUtils.fetchNewsData(mContext);
        return news;
    }
}
