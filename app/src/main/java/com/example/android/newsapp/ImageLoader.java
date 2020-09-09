package com.example.android.newsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.graphics.Bitmap;

/**
 * Loads a list of bitmap images by using an AsyncTask to perform the
 * network request to the given URL.
 */
public class ImageLoader extends AsyncTaskLoader<Bitmap[]> {

    /** Tag for log messages */
    private static final String LOG_TAG = ImageLoader.class.getName();

    /** Query urls */
    private String[] mUrls;

    /**
     * Constructs a new {@link ImageLoader}.
     *
     * @param context of the activity
     * @param urls to load data from
     */
    public ImageLoader(Context context, String[] urls) {
        super(context);
        mUrls = urls;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     * This is on a background thread.
     */
    @Override
    public Bitmap[] loadInBackground() {
        if (mUrls == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of news.
        Bitmap[] bitmapData = QueryUtils.fetchImageData(mUrls);
        return bitmapData;
    }
}
