package com.example.android.newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks, SwipeRefreshLayout.OnRefreshListener {

    public static final String LOG_TAG = MainActivity.class.getName();
    List<News> mNews;

    private static final int NEWS_LOADER_ID = 1;
    private static final int BITMAP_LOADER_ID = 2;
    private static final int NUMBER_OF_NEWS_ITEMS = 12;
    SwipeRefreshLayout swipe;

    /**
     * URL for The Guardian News API
     */
    private static final String USGS_REQUEST_URL =
            "https://content.guardianapis.com/search?q=android&order-by=newest&show-tags=contributor&format=json&star-rating=5&page-size=12&show-fields=headline,thumbnail&show-tags=contributor&show-references=author&api-key=4065da3c-0bd0-41a2-9fc1-7cf1fd6fe178";

    /**
     * Adapter for the list of news
     */
    private NewsAdapter mAdapter;

    /**
     * TextView that is displayed when the list is empty
     */
    TextView mEmptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        swipe = findViewById(R.id.swiperefresh);
        swipe.setOnRefreshListener(this);
        swipe.setColorSchemeColors(getResources().getColor(R.color.colorAccent));

        // Find a reference to the {@link ListView} in the layout
        ListView newsListView = findViewById(R.id.list);

        mEmptyStateTextView = findViewById(R.id.empty_view);
        newsListView.setEmptyView(mEmptyStateTextView);

        // Create a new adapter that takes an empty list of news items as input
        mAdapter = new NewsAdapter(this, new ArrayList<News>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        newsListView.setAdapter(mAdapter);

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {

            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(NEWS_LOADER_ID, null, this);

        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

            // Update empty state with no connection error message
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }
    }

    // Then we need to override the three methods specified in the LoaderCallbacks interface.
    // We need onCreateLoader(), for when the LoaderManager has determined that the loader
    // with our specified ID isn't running, so we should create a new one.
    @Override
    public Loader onCreateLoader(int id, Bundle bundle) {
        // Create a new loader for the given URL
        if (id == NEWS_LOADER_ID) {
            return new NewsLoader(this, USGS_REQUEST_URL);
        } else {
            String[] urls = new String[12];
            // Get urls array from mNews
            for (int i = 0; i < NUMBER_OF_NEWS_ITEMS; i++) {
                urls[i] = mNews.get(i).getThumbnail();
            }

            return new ImageLoader(this, urls);
        }
    }

    // Second, We need onLoadFinished(), where we'll do exactly what we did in onPostExecute(), and
    // use the news data to update our UI - by updating the dataset in the adapter.
    @Override
    public void onLoadFinished(Loader loader, Object data) {
        int id = loader.getId();
        if (id == NEWS_LOADER_ID) {
            List<News> news = (List<News>) data;
            // Hide loading indicator because the data has been loaded
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);
            swipe.setRefreshing(false);
            mNews = news;

            // Set empty state text to display "No news found."
            mEmptyStateTextView.setText(R.string.no_news_found);

            // Clear the adapter of previous news data
            mAdapter.clear();

            // If there is a valid list of {@link News}s, then add them to the adapter's
            // data set. This will tr
            // igger the ListView to update.
            if (news != null && !news.isEmpty()) {
                mAdapter.addAll(news);
                getLoaderManager().initLoader(BITMAP_LOADER_ID, null, this);
            }
        } else {
            Bitmap[] result = (Bitmap[]) data;

            // Update the adapter with the new images
            for (int i = 0; i < NUMBER_OF_NEWS_ITEMS; i++) {
                mNews.get(i).setThumbnailBitmap(result[i]);
            }
            mAdapter.addAll(mNews);
            mAdapter.notifyDataSetChanged();
        }
    }

    // Third, we need onLoaderReset(), we're being informed that the data from our loader is
    // no longer valid. This isn't actually a case that's going to come up with our simple loader,
    // but the correct thing to do is to remove all the news data from our UI by clearing out
    // the adapter’s data set.
    @Override
    public void onLoaderReset(Loader loader) {
        int id = loader.getId();
        if (id == 1) {
            // Loader reset, so we can clear out our existing data.
            mAdapter.clear();
        }
    }

    @Override
    public void onRefresh() {
        getLoaderManager().restartLoader(NEWS_LOADER_ID, null, this);
    }
}