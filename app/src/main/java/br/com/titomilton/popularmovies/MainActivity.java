package br.com.titomilton.popularmovies;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.net.URL;

import br.com.titomilton.popularmovies.utils.NetworkUtils;
import br.com.titomilton.popularmovies.utils.TheMovieDBJsonUtils;

// TODO show poster (picasso library)
// TODO page list
// TODO detail

public class MainActivity extends AppCompatActivity implements MoviesAdapter.MoviesAdapterOnClickHandler {

    private static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;
    private MoviesAdapter mMoviesAdapter;
    private TextView mOrderDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_movies);

        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);

        mOrderDescription = (TextView) findViewById(R.id.tv_order_description);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);

        mMoviesAdapter = new MoviesAdapter(this);

        mRecyclerView.setAdapter(mMoviesAdapter);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        loadMoviesData(NetworkUtils.TpMovieList.POPULAR);
    }

    private void loadMoviesData(NetworkUtils.TpMovieList tpMovieList) {
        adjustOrderDescription(tpMovieList);
        showMoviesDataView();
        new FetchMoviesTask().execute(tpMovieList);
    }

    private void adjustOrderDescription(NetworkUtils.TpMovieList tpMovieList) {
        mOrderDescription.setText(
                tpMovieList.equals(NetworkUtils.TpMovieList.POPULAR) ?
                        R.string.by_most_popular :
                        R.string.by_top_rated
        );
    }

    private void showMoviesDataView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(String movie) {
//        Context context = this;
//        Class destinationClass = DetailActivity.class;
//        Intent intentToStartDetailActivity = new Intent(context, destinationClass);
//        intentToStartDetailActivity.putExtra(Intent.EXTRA_TEXT, movie);
//        startActivity(intentToStartDetailActivity);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.list_movies, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final NetworkUtils.TpMovieList tpMovieList;

        switch (item.getItemId()) {
            case R.id.action_most_popular:
                tpMovieList = NetworkUtils.TpMovieList.POPULAR;
                break;
            case R.id.action_by_top_rated:
                tpMovieList = NetworkUtils.TpMovieList.TOP_RATED;
                break;
            default:
                return super.onOptionsItemSelected(item);

        }

        loadMoviesData(tpMovieList);

        return true;

    }

    public class FetchMoviesTask extends AsyncTask<NetworkUtils.TpMovieList, Void, String[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected String[] doInBackground(NetworkUtils.TpMovieList... params) {
            NetworkUtils.TpMovieList tpMovieList = params[0];
            URL moviesRequestUrl = NetworkUtils.buildUrl(tpMovieList);

            String jsonMoviesResponse = null;
            try {
                jsonMoviesResponse = NetworkUtils
                        .getResponseFromHttpUrl(moviesRequestUrl);

                String[] simpleJsonMoviesData = TheMovieDBJsonUtils
                        .getMoviesStringsFromJson(jsonMoviesResponse);

                return simpleJsonMoviesData;

            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, e.getMessage() + " json:" + jsonMoviesResponse);
                return null;
            }
        }

        @Override
        protected void onPostExecute(String[] moviesData) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (moviesData != null) {
                showMoviesDataView();
                mMoviesAdapter.setMoviesData(moviesData);
            } else {
                showErrorMessage();
            }
        }
    }

}