package br.com.titomilton.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import br.com.titomilton.popularmovies.utils.NetworkUtils;
import br.com.titomilton.popularmovies.utils.TheMovieDBAPI;
import br.com.titomilton.popularmovies.utils.TheMovieDBJsonUtils;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DetailActivity extends AppCompatActivity implements TrailersAdapter.TrailersAdapterOnClickHandler {

    private static final String TAG = DetailActivity.class.getSimpleName();
    private static final int GRID_SPAN_COUNT_ORIENTATION_PORTRAIT = 1;
    private TextView mTitleTextView;
    private TextView mReleaseDateTextView;
    private TextView mDurationTextView;
    private TextView mVoteAverageTextView;
    private TextView mSynopsis;
    private TextView mErrorTextView;
    private ImageView mPoster;
    private View mMovieDetailsView;
    private Movie mMovie;
    private RecyclerView mRecyclerView;
    private TrailersAdapter mTrailersAdapter;
    private ProgressBar mLoadingIndicator;
    private static final String YOUTUBE_URL_TEMPLATE = "http://www.youtube.com/watch?v=%s";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);
        mErrorTextView = findViewById(R.id.tv_error_message_display);
        mMovieDetailsView = findViewById(R.id.cl_movie_detail);
        mTitleTextView = findViewById(R.id.tv_title_detail);
        mReleaseDateTextView = findViewById(R.id.tv_release_date_detail);
        mDurationTextView = findViewById(R.id.tv_duration);
        mVoteAverageTextView = findViewById(R.id.tv_vote_average);
        mSynopsis = findViewById(R.id.tv_plot_synopsis);
        mPoster = findViewById(R.id.iv_poster_detail);
        mRecyclerView = findViewById(R.id.recycler_view_trailers);

        mLoadingIndicator.setVisibility(View.GONE);
        mDurationTextView.setVisibility(View.GONE);
        mErrorTextView.setVisibility(View.GONE);
        mRecyclerView.setLayoutManager(createGridLayoutManager());

        mRecyclerView.setHasFixedSize(true);

        mTrailersAdapter = new TrailersAdapter(this);

        mRecyclerView.setAdapter(mTrailersAdapter);


        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra(Intent.EXTRA_TEXT)) {

                mMovieDetailsView.setVisibility(View.GONE);
                mMovie = intentThatStartedThisActivity.getParcelableExtra(Intent.EXTRA_TEXT);

                if (mMovie == null) {
                    showErrorMessage();
                } else {
                    loadMovieDetailsCheckIfConnected();
                }


            }
        }
    }

    @NonNull
    private GridLayoutManager createGridLayoutManager() {
        GridLayoutManager layoutManager = new GridLayoutManager(this, GRID_SPAN_COUNT_ORIENTATION_PORTRAIT);
        return layoutManager;
    }

    private void setReleaseDate() {
        try {
            String year = mMovie.getReleaseDate().substring(0, 4);
            mReleaseDateTextView.setText(year);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }


    private void setDurationTextView() {
        if (mMovie.getDuration() != 0) {
            mDurationTextView.setVisibility(View.VISIBLE);
            mDurationTextView.setText(mMovie.getDuration() + "min");
        } else {
            mDurationTextView.setVisibility(View.GONE);
        }
    }

    private void loadMovieDetailsCheckIfConnected () {

        if (NetworkUtils.isConnected(this)) {
            loadMovieDetails();
        } else {
            showErrorMessageNoInternet();
        }
    }

    private void loadMovieDetails() {

        mLoadingIndicator.setVisibility(View.VISIBLE);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(TheMovieDBAPI.BASE_URL)
                .build();
        TheMovieDBAPI theMovieDBAPI = retrofit.create(TheMovieDBAPI.class);

        Call<ResponseBody> call = theMovieDBAPI.getMovieDetailAndTrailers(mMovie.getId());

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String json = response.body().string();
                        mMovie = TheMovieDBJsonUtils.getMovieDetail(json);
                        Trailer[] trailers = trailers = TheMovieDBJsonUtils.getTrailers(json);
                        showMovieDetails(trailers);
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage(), e);
                        onFailure(call, e);
                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                mLoadingIndicator.setVisibility(View.GONE);
                showErrorMessage();
            }

        });
    }


    private void showErrorMessage(int resStringId) {
        mMovieDetailsView.setVisibility(View.GONE);
        mErrorTextView.setText(resStringId);
        mErrorTextView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        showErrorMessage(R.string.error_message);
    }

    private void showErrorMessageNoInternet() {
        showErrorMessage(R.string.error_message_no_internet);
    }

    private void showMovieDetails(Trailer[] trailers) {

        Picasso.with(this)
                .load(mMovie.getPosterUrl())
                .into(mPoster);

        mTitleTextView.setText(mMovie.getTitle());
        setReleaseDate();
        mVoteAverageTextView.setText(mMovie.getVoteAverage());
        mSynopsis.setText(mMovie.getSynopsis());
        setDurationTextView();

        mTrailersAdapter.setTrailersData(trailers);
        mLoadingIndicator.setVisibility(View.GONE);
        mMovieDetailsView.setVisibility(View.VISIBLE);

    }

    @Override
    public void onClick(Trailer trailer) {
        try {
            String youtubeUrl = String.format(YOUTUBE_URL_TEMPLATE, trailer.getYoutubeId());
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(youtubeUrl)));
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
            String errorMessage = "Error - " +  e.getMessage();
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT)
                    .show();
        }
    }
}
