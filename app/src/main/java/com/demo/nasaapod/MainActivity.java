package com.demo.nasaapod;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.demo.nasaapod.adapter.APODAdapter;
import com.demo.nasaapod.constants.Constants;
import com.demo.nasaapod.repository.MainRepository;
import com.demo.nasaapod.main.MainViewModel;
import com.demo.nasaapod.model.APODUiData;

public class MainActivity extends AppCompatActivity {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private MainViewModel mViewModel;
    private RecyclerView mRecyclerView;
    private TextView mEmptyView;
    private APODAdapter mApodAdapter;
    private int count = 10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //1.
        mSwipeRefreshLayout = findViewById(R.id.container_swipe_layout);
        // Configure the refreshing colors
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        /*
         * Sets up a SwipeRefreshLayout.OnRefreshListener that is invoked when the user
         * performs a swipe-to-refresh gesture.
         */
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchOperation();
            }
        });

        //2.
        mRecyclerView = findViewById(R.id.main_view_rc_view);
        mEmptyView = findViewById(R.id.main_rc_empty_view);
        mApodAdapter = new APODAdapter();
        mRecyclerView.setAdapter(mApodAdapter);


        //3.
        // the factory and its dependencies
        MainViewModel.MainViewModelFactory factory =
                new MainViewModel.MainViewModelFactory(new MainRepository());

        mViewModel = ViewModelProviders.of(this, factory).get(MainViewModel.class);


        mViewModel.getApodModelLiveData().observe(this, new Observer<APODUiData>() {
            @Override
            public void onChanged(APODUiData response) {

                if (response.getErrorMsg() == null) {
                    mApodAdapter.clear();
                    mApodAdapter.addAll(response.getApodModels());

                    if (mApodAdapter.getItemCount() <= 0) {
                        showRcView(false);
                        mEmptyView.setText(R.string.no_results_found_swipe_to_refresh);
                    } else {
                        showRcView(true);
                        showToast(getString(R.string.data_loaded));
                    }
                } else {
                    if (response.getErrorMsg().contains(Constants.UNABLE_TO_RESOLVE_HOST)) {

                        if (mApodAdapter.getItemCount() <= 0) {
                            showRcView(false);
                            Drawable top = getResources().getDrawable(R.drawable.ic_no_internet);
                            mEmptyView.setCompoundDrawablesWithIntrinsicBounds(null ,top, null,null);
                            mEmptyView.setText(R.string.no_internet_connection_);
                        } else {
                            showRcView(true);
                            showToast(getString(R.string.no_internet_connection_));
                        }

                    } else {
                        showToast(response.getErrorMsg());
                    }
                }

                //calling this method instructs the SwipeRefreshLayout to
                // remove the progress indicator and update the view contents.
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        //4.
        // Init the fetch operation
        initFetch();

    }

    private void showRcView(boolean showRcv) {
        mRecyclerView.setVisibility(showRcv ? View.VISIBLE : View.GONE);
        mEmptyView.setVisibility(showRcv ? View.GONE : View.VISIBLE);
    }

    /*
     * Listen for option item selections so that we receive a notification
     * when the user requests a refresh by selecting the refresh action bar item.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Check if user triggered a refresh:
            case R.id.menu_refresh:
                Log.i("TEST", "Refresh menu item selected");

                initFetch();

                return true;
        }

        // User didn't trigger a refresh, let the superclass handle this action
        return super.onOptionsItemSelected(item);
    }

    private void initFetch() {
        if (mSwipeRefreshLayout == null) {
            return;
        }
        // Signal SwipeRefreshLayout to start the progress indicator
        mSwipeRefreshLayout.setRefreshing(true);

        // Start the refresh background task.
        fetchOperation();
    }

    //This method performs the actual data-refresh operation.
    //Start the refresh background task.
    //and observe by mViewModel's LiveData
    private void fetchOperation() {
        if (mViewModel != null) {
            mViewModel.fetchAPODData(count);
        }
    }

    //Show toast
    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
}

