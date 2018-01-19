package br.com.titomilton.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
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
import br.com.titomilton.popularmovies.utils.TpMovieList;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.MoviesAdapterOnClickHandler{

    private static final int GRID_SPAN_COUNT_ORIENTATION_PORTRAIT = 2;
    private static final int GRID_SPAN_COUNT_ORIENTATION_LANDSCAPE = GRID_SPAN_COUNT_ORIENTATION_PORTRAIT * 2;
    private static final String TAG = MainActivity.class.getSimpleName();
    public static final String KEY_MOVIES_POSITION = "listState";
//    public static final String KEY_MOVIES_POSITION = "moviesPosition";

    private RecyclerView mRecyclerView;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;
    private MoviesAdapter mMoviesAdapter;
    private TextView mOrderDescription;
    private TpMovieList mTpMovieList;
    private Parcelable listState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);

        mRecyclerView = findViewById(R.id.recycler_view_movies);

        mErrorMessageDisplay = findViewById(R.id.tv_error_message_display);

        mOrderDescription = findViewById(R.id.tv_order_description);

        mRecyclerView.setLayoutManager(createStaggeredGridLayoutManager());

        mRecyclerView.setHasFixedSize(true);

        mMoviesAdapter = new MoviesAdapter(this);

        mRecyclerView.setAdapter(mMoviesAdapter);

        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mTpMovieList == null) {
            mTpMovieList = TpMovieList.POPULAR;
        }

        loadMoviesDataCheckIfConnected();



    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(TpMovieList.class.getSimpleName(), mTpMovieList.getId());
        outState.putParcelable(KEY_MOVIES_POSITION, mRecyclerView.getLayoutManager().onSaveInstanceState());

//        int position = 0;
//        StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) mRecyclerView.getLayoutManager();
//        layoutManager.getPosition()
//        RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();
//        if (layoutManager != null) {
//            position = layoutManager.
//        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        getTpMovieList(savedInstanceState);
        listState = savedInstanceState.getParcelable(KEY_MOVIES_POSITION);
        super.onRestoreInstanceState(savedInstanceState);
    }

    private void getTpMovieList(Bundle savedInstanceState) {
        int id = -1;
        if (savedInstanceState != null) {
           id = savedInstanceState.getInt(TpMovieList.class.getSimpleName());
        }
        mTpMovieList = TpMovieList.get(id);
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

    private void loadMoviesDataCheckIfConnected () {

        if (!mTpMovieList.isDataFromRest() || NetworkUtils.isConnected(this)) {
            loadMoviesData();
        } else {
            showErrorMessageNoInternet();
        }

    }

    private void loadMoviesData() {

        mOrderDescription.setText(mTpMovieList.getStringId());

        showMoviesDataView();
        mLoadingIndicator.setVisibility(View.VISIBLE);

        mTpMovieList.getMovies(this, new TpMovieList.CallbackMovies() {
            @Override
            public void get(Movie[] movies) {
                onPostExecute(movies);
            }

            @Override
            public void onFail(Throwable t) {
                showErrorMessage(t.getMessage());
            }
        });

    }


    private void showMoviesDataView() {
        mErrorMessageDisplay.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessageNoInternet() {
        showErrorMessage(R.string.error_message_no_internet);
    }

    private void showErrorMessage() {
        showErrorMessage(R.string.error_message);
    }

    private void showErrorMessage(int resStringId) {
        showErrorMessage(getResources().getString(resStringId));
    }

    private void showErrorMessage(String message) {
        mRecyclerView.setVisibility(View.GONE);
        mErrorMessageDisplay.setText(message);
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

        switch (item.getItemId()) {
            case R.id.action_most_popular:
                mTpMovieList = TpMovieList.POPULAR;
                break;
            case R.id.action_by_top_rated:
                mTpMovieList = TpMovieList.TOP_RATED;
                break;
            case R.id.action_by_favorited:
                mTpMovieList  = TpMovieList.FAVORITED;
                break;
            default:
                return super.onOptionsItemSelected(item);

        }

        loadMoviesDataCheckIfConnected();

        return true;

    }

    private void onPostExecute(Movie[] moviesData) {
        mLoadingIndicator.setVisibility(View.GONE);
        if (moviesData != null) {
            showMoviesDataView();
            mMoviesAdapter.setMoviesData(moviesData);
            if (listState != null) {
                mRecyclerView.getLayoutManager().onRestoreInstanceState(listState);
            }
        } else {
            showErrorMessage();
        }
    }

}
