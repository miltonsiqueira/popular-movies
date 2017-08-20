package br.com.titomilton.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import br.com.titomilton.popularmovies.utils.NetworkUtils;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.MoviesAdapterOnClickHandler, AsyncTaskListener<Movie[]>  {

    private static final int GRID_SPAN_COUNT_ORIENTATION_PORTRAIT = 2;
    private static final int GRID_SPAN_COUNT_ORIENTATION_LANDSCAPE = GRID_SPAN_COUNT_ORIENTATION_PORTRAIT * 2;

    private RecyclerView mRecyclerView;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;
    private MoviesAdapter mMoviesAdapter;
    private TextView mOrderDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);

        mRecyclerView = findViewById(R.id.recycler_view_movies);

        mErrorMessageDisplay = findViewById(R.id.tv_error_message_display);

        mOrderDescription = findViewById(R.id.tv_order_description);

        StaggeredGridLayoutManager layoutManager = createStaggeredGridLayoutManager();

        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);

        mMoviesAdapter = new MoviesAdapter(this);

        mRecyclerView.setAdapter(mMoviesAdapter);

        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);

        loadMoviesData(NetworkUtils.TpMovieList.POPULAR);
    }

    @NonNull
    private StaggeredGridLayoutManager createStaggeredGridLayoutManager() {
        StaggeredGridLayoutManager layoutManager;
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            layoutManager = new StaggeredGridLayoutManager(GRID_SPAN_COUNT_ORIENTATION_PORTRAIT, StaggeredGridLayoutManager.VERTICAL);
        } else {
            layoutManager = new StaggeredGridLayoutManager(GRID_SPAN_COUNT_ORIENTATION_LANDSCAPE, StaggeredGridLayoutManager.VERTICAL);
        }
        return layoutManager;
    }

    private void loadMoviesData(NetworkUtils.TpMovieList tpMovieList) {
        adjustOrderDescription(tpMovieList);
        showMoviesDataView();
        new FetchMoviesTask(this).execute(tpMovieList);
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
    public void onClick(Movie movie) {
        Context context = this;
        Class destinationClass = DetailActivity.class;
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);
        intentToStartDetailActivity.putExtra (Intent.EXTRA_TEXT, movie);
        startActivity(intentToStartDetailActivity);
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


    @Override
    public void onPreExecute() {
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPostExecute(Movie[] moviesData) {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        if (moviesData != null) {
            showMoviesDataView();
            mMoviesAdapter.setMoviesData(moviesData);
        } else {
            showErrorMessage();
        }
    }

}
