package com.example.android.myapplication2222222;


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
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public final class QueryUtils {

    /** Tag for the log messages */
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Query the USGS dataset and return a list of {@link News} objects.
     */
    public static List<News> fetchEarthquakeData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link News}s
        // List<News> news = extractFeatureFromJson(jsonResponse);
        List<News> news = extractFeatureFromJson2(jsonResponse);

        // Return the list of {@link News}s
        return news;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
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
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
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
        Log.e(LOG_TAG, "JSON response: >>>>>>>>>>>>>>>>>>>>>>>>>" + jsonResponse);
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
     * Return a list of {@link News} objects that has been built up from
     * parsing the given JSON response.
     */
   /* private static List<News> extractFeatureFromJson(String earthquakeJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(earthquakeJSON)) {
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

            // Extract “features” JSONArray
            JSONObject jsonBaseObject = new JSONObject(earthquakeJSON);
            JSONArray features = jsonBaseObject.getJSONArray("features");

            // Loop through each feature in the array
            for (int i = 0; i < features.length(); i++) {
                // Get news JSONObject at position i
                JSONObject feature = features.getJSONObject(i);
                // Get “properties” JSONObject
                JSONObject properties = feature.getJSONObject("properties");

                // Extract “mag” for magnitude
                double magnitude = properties.getDouble("mag");

                // Extract “place” for location
                String location = properties.getString("place");

                // Extract “time” for time
                Long time = properties.getLong("time");

                // Extract "url"
                String url = properties.getString("url");

                // Create News java object from magnitude, location, and time
                News news2 = new News(magnitude, location, time, url);
                // Add news to list of news

                news.add(news2);
            }
        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of news
        return news;
    }
*/
    private static List<News> extractFeatureFromJson2(String earthquakeJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(earthquakeJSON)) {
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

            // Extract “features” JSONArray
            JSONObject jsonBaseObject = new JSONObject(earthquakeJSON);
            JSONObject responseJSON = jsonBaseObject.getJSONObject("response");
            JSONArray featuresJSONArray = responseJSON.getJSONArray("results");

            // Loop through each feature in the array
            for (int i = 0; i < featuresJSONArray.length(); i++) {
                // Get news JSONObject at position i
                JSONObject feature = featuresJSONArray.getJSONObject(i);

                // Extract “place” for location
                String sectionName = feature.getString("sectionName");

                // Extract “place” for location
                String webPublicationDate = feature.getString("webPublicationDate");

                // Extract “place” for location
                String webTitle = feature.getString("webTitle");

                // Extract “place” for location
                String webUrl = feature.getString("webUrl");

                JSONObject fieldsJSON = feature.getJSONObject("fields");

                // Extract “place” for location
                String headline = fieldsJSON.getString("headline");

                // Extract “place” for location
                String thumbnail = fieldsJSON.getString("thumbnail");

                JSONArray tagsJSONArray = feature.getJSONArray("tags");
                JSONObject tag;
                String contributor = "UNAVAILABLE";
                if (tagsJSONArray.length() > 0) {
                    tag = tagsJSONArray.getJSONObject(0);
                    // Extract “place” for location
                    contributor = tag.getString("webTitle");
                }

                // Create News java object from magnitude, location, and time
                News news2 = new News(sectionName, headline, contributor, webPublicationDate, webUrl, thumbnail);
                // Add news to list of news

                news.add(news2);
            }
        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of news
        return news;
    }

    /**
     * Query the USGS dataset and return a list of {@link News} objects.
     */
    public static List<News> fetchImageData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link News}s
        // List<News> news = extractFeatureFromJson(jsonResponse);
        List<News> news = extractFeatureFromJson2(jsonResponse);

        // Return the list of {@link News}s
        return news;
    }


}
