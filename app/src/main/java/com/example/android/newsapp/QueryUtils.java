package com.example.android.newsapp;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper methods related to requesting and receiving news data from The Guardian API.
 */
public final class QueryUtils {

    /** Tag for the log messages */
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();
    private static Context mContext;

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Returns new URL string for the main API request (without thumbnails)
     */
    static String createStringUrlForNewsQuery() {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .encodedAuthority("content.guardianapis.com")
                .appendPath("search")
                .appendQueryParameter("q", "android")
                .appendQueryParameter("order-by", "newest")
                .appendQueryParameter("show-references", "author")
                .appendQueryParameter("show-tags", "contributor")
                .appendQueryParameter("tag", "technology/android")
                .appendQueryParameter("format", "json")
                .appendQueryParameter("star-rating", "5")
                .appendQueryParameter("page-size", "12")
                .appendQueryParameter("show-fields", "headline,thumbnail")
                .appendQueryParameter("show-tags", "contributor")
                .appendQueryParameter("show-references", "author")
                .appendQueryParameter("api-key", "4065da3c-0bd0-41a2-9fc1-7cf1fd6fe178");
        String urlString = builder.build().toString();
        return urlString;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrlForNewsQuery() {
        URL url = null;
        String stringUrl = createStringUrlForNewsQuery();
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrlForBitmapQuery(String stringUrl) {
        URL url = null;
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .encodedAuthority(stringUrl.substring(8)); // I am not including the https:// part
        String urlString = builder.build().toString();

        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the news item JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Returns a list of news items
     */
    private static List<News> extractFeatureFromJson(String newsJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(newsJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding news to
        ArrayList<News> news = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            // Parse the response given by the SAMPLE_JSON_RESPONSE string and
            // build up a list of News objects with the corresponding data.

            // Extract “results” JSONArray
            JSONObject jsonBaseObject = new JSONObject(newsJSON);
            JSONObject responseJSON = jsonBaseObject.getJSONObject("response");
            JSONArray featuresJSONArray = responseJSON.getJSONArray("results");

            // Loop through each feature in the array
            for (int i = 0; i < featuresJSONArray.length(); i++) {
                // Get news JSONObject at position i
                JSONObject feature = featuresJSONArray.getJSONObject(i);

                // Extract “sectionName” of news item
                String sectionName = feature.getString("sectionName");

                // Extract “webPublicationDate” of news item
                String webPublicationDate = feature.getString("webPublicationDate");

                // Extract “webTitle” of news item
                String webTitle = feature.getString("webTitle");

                // Extract “webUrl” of news item
                String webUrl = feature.getString("webUrl");

                // Extract "fields" values of news item
                JSONObject fieldsJSON = feature.getJSONObject("fields");

                // Extract “thumbnaila” for news item
                String thumbnail = fieldsJSON.getString("thumbnail");

                JSONArray tagsJSONArray = feature.getJSONArray("tags");
                JSONObject tag;
                String contributor = mContext.getResources().getString(R.string.unavailable_contributor);
                if (tagsJSONArray.length() > 0) {
                    tag = tagsJSONArray.getJSONObject(0);
                    // Extract “webTitle” of news item
                    contributor = tag.getString("webTitle");
                }

                // Create News java object
                News news2 = new News(sectionName, webTitle, contributor, webPublicationDate, webUrl, thumbnail, null);

                // Add news to list of news
                news.add(news2);
            }
        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e(LOG_TAG, "Problem parsing the news JSON results", e);
        }

        // Return the list of news
        return news;
    }

    /**
     * Query The Guardian API and return a list of {@link News} objects.
     */
    public static List<News> fetchNewsData(Context context) {
        // Create URL object
        URL url = createUrlForNewsQuery();
        mContext = context;

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link News}s
        // List<News> news = extractFeatureFromJson(jsonResponse);
        List<News> news = extractFeatureFromJson(jsonResponse);

        // Return the list of {@link News}s
        return news;
    }

    /**
     * Returns an array of Bitmap objects for the news items.
     */
    public static Bitmap[] fetchImageData(String[] urls) {
        int n = urls.length;
        Bitmap[] bitmapNewsArray = new Bitmap[12];

        for (int i = 0; i < n; i++) {
            // Create URL object
            URL url = createUrlForBitmapQuery(urls[i]);

            // Perform HTTP request to the URL and receive a JSON response back
            Bitmap bitmapNews;
            try {
                bitmapNews = makeHttpRequestForBitmap(url);
                bitmapNewsArray[i] = bitmapNews;
            } catch (IOException e) {
                Log.e(LOG_TAG, "Problem making the HTTP request.", e);
            }
        }

        // Return the array of {@link Bitmap}s
        return bitmapNewsArray;
    }


    /**
     * Makes HTTP response to get a Bitmap object
     */
    private static Bitmap makeHttpRequestForBitmap(URL url) throws IOException {
        Bitmap bitmap = null;
        InputStream in = null;
        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.connect();
            in = conn.getInputStream();

            // Download and decode the bitmap using BitmapFactory
            bitmap = BitmapFactory.decodeStream(in);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null)
                try {
                    in.close();
                } catch (IOException e) {
                    Log.e(LOG_TAG, "Exception while closing inputstream" + e);
                }
        }

        return bitmap;
    }
}
