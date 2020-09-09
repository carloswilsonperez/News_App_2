package com.example.android.myapplication2222222;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks, SwipeRefreshLayout.OnRefreshListener {

    public static final String LOG_TAG = MainActivity.class.getName();
    Context myContext = this;


    /**
     * Constant value for the earthquake loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int EARTHQUAKE_LOADER_ID = 1;
    private static final int BITMAP_LOADER_ID = 2;
    SwipeRefreshLayout swipe;


    /** URL for earthquake data from the USGS dataset */
    private static final String USGS_REQUEST_URL =
            "https://content.guardianapis.com/search?order-by=newest&show-tags=contributor&format=json&q=ios&lang=en&star-rating=5&page-size=12&show-fields=headline,thumbnail&show-tags=contributor&show-references=author&api-key=4065da3c-0bd0-41a2-9fc1-7cf1fd6fe178";

    /** Adapter for the list of earthquakes */
    private NewsAdapter mAdapter;

    /** TextView that is displayed when the list is empty */
    TextView mEmptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        swipe = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        swipe.setOnRefreshListener(this);
        swipe.setColorSchemeColors(getResources().getColor(R.color.colorAccent));


        // Find a reference to the {@link ListView} in the layout
        ListView earthquakeListView = (ListView) findViewById(R.id.list);

        mEmptyStateTextView = findViewById(R.id.empty_view);
        earthquakeListView.setEmptyView(mEmptyStateTextView);

        // Create a new adapter that takes an empty list of earthquakes as input
        mAdapter = new NewsAdapter(this, new ArrayList<News>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(mAdapter);


        // Set an item click listener on the ListView, which sends an intent to a web browser
        // to open a website with more information about the selected earthquake.
        /*earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current earthquake that was clicked on
                News currentNews = mAdapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri newsWebUrl = Uri.parse(currentNews.getmWebUrl());

                // Create a new intent to view the earthquake URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsWebUrl);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });*/

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
            loaderManager.initLoader(EARTHQUAKE_LOADER_ID, null, this);
            loaderManager.initLoader(BITMAP_LOADER_ID, null, this);

        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

            // Update empty state with no connection error message
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }
    }

    String[] urls = {
            "https://media.guim.co.uk/fec806f8aab47a9de8caa2e6335fd466713e67e7/580_375_4392_2636/500.jpg",
            "https://media.guim.co.uk/6e43ca392bc23e56df3b67ac26f6371332501717/1110_542_3236_1943/500.jpg",
            "https://media.guim.co.uk/fec806f8aab47a9de8caa2e6335fd466713e67e7/580_375_4392_2636/500.jpg",
            "https://media.guim.co.uk/6e43ca392bc23e56df3b67ac26f6371332501717/1110_542_3236_1943/500.jpg",
            "https://media.guim.co.uk/fec806f8aab47a9de8caa2e6335fd466713e67e7/580_375_4392_2636/500.jpg",
            "https://media.guim.co.uk/6e43ca392bc23e56df3b67ac26f6371332501717/1110_542_3236_1943/500.jpg",
            "https://media.guim.co.uk/fec806f8aab47a9de8caa2e6335fd466713e67e7/580_375_4392_2636/500.jpg",
            "https://media.guim.co.uk/6e43ca392bc23e56df3b67ac26f6371332501717/1110_542_3236_1943/500.jpg",
            "https://media.guim.co.uk/fec806f8aab47a9de8caa2e6335fd466713e67e7/580_375_4392_2636/500.jpg",
            "https://media.guim.co.uk/6e43ca392bc23e56df3b67ac26f6371332501717/1110_542_3236_1943/500.jpg",
            "https://media.guim.co.uk/fec806f8aab47a9de8caa2e6335fd466713e67e7/580_375_4392_2636/500.jpg",
            "https://media.guim.co.uk/6e43ca392bc23e56df3b67ac26f6371332501717/1110_542_3236_1943/500.jpg",
};

    // Then we need to override the three methods specified in the LoaderCallbacks interface.
    // We need onCreateLoader(), for when the LoaderManager has determined that the loader
    // with our specified ID isn't running, so we should create a new one.
    @Override
    public Loader onCreateLoader(int i, Bundle bundle) {
        // Create a new loader for the given URL
        if (i == 1) {
            return new NewsLoader(this, USGS_REQUEST_URL);
        } else {
            return new ImageLoader(this, "https://media.guim.co.uk/fec806f8aab47a9de8caa2e6335fd466713e67e7/580_375_4392_2636/500.jpg");
        }
    }

    // Second, We need onLoadFinished(), where we'll do exactly what we did in onPostExecute(), and
    // use the earthquake data to update our UI - by updating the dataset in the adapter.
    @Override
    public void onLoadFinished(Loader loader, Object data) {
        int id = loader.getId();
        if (id == 1) {
            List<News> news = (List<News>) data;
            // Hide loading indicator because the data has been loaded
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);
            swipe.setRefreshing(false);

            // Set empty state text to display "No news found."
            mEmptyStateTextView.setText(R.string.no_earthquakes);

            // Clear the adapter of previous earthquake data
            mAdapter.clear();

            // If there is a valid list of {@link News}s, then add them to the adapter's
            // data set. This will tr
            // igger the ListView to update.
            if (news != null && !news.isEmpty()) {
                mAdapter.addAll(news);
            }
        } else {
            Bitmap result = (Bitmap) data;
            ImageView newsThumbnail = findViewById(R.id.news_thumbnail2);
            newsThumbnail.setImageBitmap(result);
        }


    }

    // Third, we need onLoaderReset(), we're being informed that the data from our loader is
    // no longer valid. This isn't actually a case that's going to come up with our simple loader,
    // but the correct thing to do is to remove all the earthquake data from our UI by clearing out
    // the adapter’s data set.
    @Override
    public void onLoaderReset(Loader loader) {
        int id = loader.getId();
        if (id == 1 ) {
            // Loader reset, so we can clear out our existing data.
            mAdapter.clear();
        } else {

        }
    }

    @Override
    public void onRefresh() {
        getLoaderManager().restartLoader(EARTHQUAKE_LOADER_ID, null, this);
    }
}