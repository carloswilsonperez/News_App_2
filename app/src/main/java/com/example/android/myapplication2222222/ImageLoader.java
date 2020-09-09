package com.example.android.myapplication2222222;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.graphics.Bitmap;

import java.util.List;

/**
 * Loads a list of earthquakes by using an AsyncTask to perform the
 * network request to the given URL.
 */
public class ImageLoader extends AsyncTaskLoader<Bitmap> {

    /** Tag for log messages */
    private static final String LOG_TAG = ImageLoader.class.getName();

    /** Query URL */
    private String mUrl;

    /**
     * Constructs a new {@link ImageLoader}.
     *
     * @param context of the activity
     * @param url to load data from
     */
    public ImageLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     * This is on a background thread.
     */
    @Override
    public Bitmap loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of news.
        Bitmap bitmapData = QueryUtils.fetchImageData(mUrl);
        return bitmapData;
        // return null;
    }
}
