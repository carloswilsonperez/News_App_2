package com.example.android.newsapp;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
     * Returns a list item view that displays information about the news item at the given
     * position in the list of news items.
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

        // Find the news item at the given position in the news list
        News currentNews = getItem(position);

        // Find the child text views by ID
        TextView newsCategoryView = listItemView.findViewById(R.id.news_category_circle);
        TextView offsetLocationView = listItemView.findViewById(R.id.news_category);
        TextView primaryLocationView = listItemView.findViewById(R.id.news_contributor);
        TextView newsTitleView = listItemView.findViewById(R.id.news_title);
        ImageView newsThumbnailView = listItemView.findViewById(R.id.news_thumbnail);
        Button buttonViewMoreView = listItemView.findViewById(R.id.news_view_more);
        TextView dateView = listItemView.findViewById(R.id.date);
        TextView timeView = listItemView.findViewById(R.id.time);

        // Initial "Loading..." image
        Bitmap thumbnailBitmap = currentNews.getThumbnailBitmap();
        if (thumbnailBitmap == null) {
            newsThumbnailView.setImageResource(R.drawable.loading);
        } else {
            newsThumbnailView.setImageBitmap(thumbnailBitmap);
        }

        buttonViewMoreView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            // Convert the String URL into a URI object (to pass into the Intent constructor)
            Uri newsWebUrl = Uri.parse( getItem(mPosition).getWebUrl());

            // Create a new intent to view the news item URI
            Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsWebUrl);

            // Send the intent to launch a new activity
            mContext.startActivity(websiteIntent);
            }
        });

        // Display the category of the current news item in that TextView
        newsCategoryView.setText(currentNews.getSectionName());

        // Set the values for primary & offset locations
        offsetLocationView.setText(currentNews.getSectionName());
        primaryLocationView.setText(currentNews.getContributor());
        newsTitleView.setText(currentNews.getWebTitle());

        // Format the webPublicationDate for the news item
        Date dateObject= null;
        String dtStart = currentNews.getWebPublicationDate();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        try {
            dateObject = format.parse(dtStart);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Format the date string (i.e. "Mar 3, 1984")
        String formattedDate = formatDate(dateObject);
        dateView.setText(formattedDate);

        // Format the time string (i.e. "4:30PM")
        String formattedTime = formatTime(dateObject);
        timeView.setText(formattedTime);

        // Set the proper background color on the categoryNewsCircle circle.
        // Fetch the background from the TextView, which is a GradientDrawable.
        GradientDrawable categoryNewsCircle = (GradientDrawable) newsCategoryView.getBackground();

        // Get the appropriate background color based on the category of the news item
        int categoryNewsColor = getMagnitudeColor();

        // Set the color on the  circle
        categoryNewsCircle.setColor(categoryNewsColor);

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
     * Return the correct color for the categories circle
     */
    private int getMagnitudeColor() {
        int magnitudeColorResourceId = R.color.circle_color;

        // You still need to convert the color resource ID into a color integer value
        return ContextCompat.getColor(getContext(), magnitudeColorResourceId);
    }
}

