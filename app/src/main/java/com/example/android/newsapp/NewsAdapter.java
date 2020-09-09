package com.example.android.newsapp;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class NewsAdapter extends ArrayAdapter<News> {
    private Context mContext;

    /**
     * Constructs a new {@Link NewsAdapter}.
     *
     * @param context of the app
     * @param news    is the list of news, which is the data source of the adapter
     */
    public NewsAdapter(Context context, List<News> news) {
        super(context, 0, news);
        mContext = context;
    }

    /**
     * Returns a list item view that displays information about the earthquake at the given
     * position in the list of earthquakes.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int mPosition = position;

        // Check if there is an existing list item view (called convertView) that we can reuse,
        // otherwise, inflate a new list item layout.
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.news_list_item, parent, false);
        }

        // Find the earthquake at the given position in the list of earthquakes
        News currentNews = getItem(position);

        // Find the child text views by ID
        TextView magnitudeView = listItemView.findViewById(R.id.magnitude);
        TextView offsetLocationView = listItemView.findViewById(R.id.location_offset);
        TextView primaryLocationView = listItemView.findViewById(R.id.primary_location);
        TextView newsTitleView = listItemView.findViewById(R.id.news_title);
        ImageView newsThumbnailView = listItemView.findViewById(R.id.news_thumbnail);
        Button buttonViewMoreView = listItemView.findViewById(R.id.news_view_more);

        // Initial "Loading..." image
        Bitmap thumbnailBitmap = currentNews.getmThumbnailBitmap();
        if (thumbnailBitmap == null) {
            newsThumbnailView.setImageResource(R.drawable.loading);
        } else {
            newsThumbnailView.setImageBitmap(thumbnailBitmap);
        }

        buttonViewMoreView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri newsWebUrl = Uri.parse( getItem(mPosition).getmWebUrl());

                // Create a new intent to view the earthquake URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsWebUrl);

                // Send the intent to launch a new activity
                mContext.startActivity(websiteIntent);
            }
        });

        // ToDo: add letter to circle
        // Format the magnitude to show 1 decimal place
        // String formattedMagnitude = formatMagnitude(currentNews.getMagnitude());
        // Display the magnitude of the current earthquake in that TextView
        // magnitudeView.setText(formattedMagnitude);

        // Set the values for primary & offset locations
        offsetLocationView.setText(currentNews.getmSectionName());
        primaryLocationView.setText(currentNews.getmContributor());
        newsTitleView.setText(currentNews.getmWebTitle());

        // Create a new Date object from the time in milliseconds of the earthquake
        Date dateObject= null;
        String dtStart = currentNews.getmWebPublicationDate();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        try {
            dateObject = format.parse(dtStart);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Find the TextView with view ID date
        TextView dateView = (TextView) listItemView.findViewById(R.id.date);
        // Format the date string (i.e. "Mar 3, 1984")
        String formattedDate = formatDate(dateObject);
        // Display the date of the current earthquake in that TextView
        dateView.setText(formattedDate);

        // Find the TextView with view ID time
        TextView timeView = (TextView) listItemView.findViewById(R.id.time);
        // Format the time string (i.e. "4:30PM")
        String formattedTime = formatTime(dateObject);
        // Display the time of the current earthquake in that TextView
        timeView.setText(formattedTime);

        // Set the proper background color on the magnitude circle.
        // Fetch the background from the TextView, which is a GradientDrawable.
        GradientDrawable magnitudeCircle = (GradientDrawable) magnitudeView.getBackground();

        // Get the appropriate background color based on the current earthquake magnitude
        int magnitudeColor = getMagnitudeColor(currentNews.getmSectionName());

        // Set the color on the magnitude circle
        magnitudeCircle.setColor(magnitudeColor);

        // Return the list item view that is now showing the appropriate data
        return listItemView;
    }

    /**
     * Return the formatted date string (i.e. "Mar 3, 1984") from a Date object.
     */
    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObject);
    }

    /**
     * Return the formatted date string (i.e. "4:30 PM") from a Date object.
     */
    private String formatTime(Date dateObject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObject);
    }

    /**
     * Return the formatted magnitude string showing 1 decimal place (i.e. "3.2")
     * from a decimal magnitude value.
     */
    private String formatMagnitude(double magnitude) {
        DecimalFormat magnitudeFormat = new DecimalFormat("0.0");
        return magnitudeFormat.format(magnitude);
    }

    /**
     * Return the correct color for the given magnitude
     * from a decimal magnitude value.
     */
    private int getMagnitudeColor(String sectionName) {
        int magnitudeColorResourceId;

        // In Java code, you can refer to the colors that you defined in the colors.xml file using
        // the color resource ID
        // ToDO: add more section names
        // ToDO: add README
        switch (sectionName) {
            case "GAMES":
                magnitudeColorResourceId = R.color.magnitude1;
                break;
            case "TECHNOLOGY":
                magnitudeColorResourceId = R.color.magnitude1;
                break;
            case "TRAVEL":
                magnitudeColorResourceId = R.color.magnitude2;
                break;
            case "US news":
                magnitudeColorResourceId = R.color.magnitude3;
                break;
            case "US news2":
                magnitudeColorResourceId = R.color.magnitude4;
                break;
            case "US news3":
                magnitudeColorResourceId = R.color.magnitude5;
                break;
            case "US news4":
                magnitudeColorResourceId = R.color.magnitude6;
                break;
            case "US news5":
                magnitudeColorResourceId = R.color.magnitude7;
                break;
            case "US news6":
                magnitudeColorResourceId = R.color.magnitude8;
                break;
            case "US news7":
                magnitudeColorResourceId = R.color.magnitude9;
                break;
            default:
                magnitudeColorResourceId = R.color.magnitude10plus;
                break;
        }

        // You still need to convert the color resource ID into a color integer value
        return ContextCompat.getColor(getContext(), magnitudeColorResourceId);
    }
}

