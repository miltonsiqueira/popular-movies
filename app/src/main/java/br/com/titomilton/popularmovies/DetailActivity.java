package br.com.titomilton.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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
    private ImageView mPoster;
    private Movie mMovie;
    private RecyclerView mRecyclerView;
    private TrailersAdapter mTrailersAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mTitleTextView = findViewById(R.id.tv_title_detail);
        mReleaseDateTextView = findViewById(R.id.tv_release_date_detail);
        mDurationTextView = findViewById(R.id.tv_duration);
        mVoteAverageTextView = findViewById(R.id.tv_vote_average);
        mSynopsis = findViewById(R.id.tv_plot_synopsis);
        mPoster = findViewById(R.id.iv_poster_detail);
        mRecyclerView = findViewById(R.id.recycler_view_trailers);

        mDurationTextView.setVisibility(View.GONE);

        mRecyclerView.setLayoutManager(createGridLayoutManager());

        mRecyclerView.setHasFixedSize(true);

        mTrailersAdapter = new TrailersAdapter(this);

        mRecyclerView.setAdapter(mTrailersAdapter);


        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra(Intent.EXTRA_TEXT)) {
                mMovie = intentThatStartedThisActivity.getParcelableExtra(Intent.EXTRA_TEXT);
                mTitleTextView.setText(mMovie.getTitle());
                setReleaseDate();
                mVoteAverageTextView.setText(mMovie.getVoteAverage());
                mSynopsis.setText(mMovie.getSynopsis());
                getAndSetDuration();
                Picasso.with(this)
                        .load(mMovie.getPosterUrl())
                        .into(mPoster);
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

    private void getAndSetDuration() {
        if (mMovie != null) {

            if (mMovie.getDuration() == 0) {
                updateDetailsFromREST();
            } else {
                setDurationTextView();
            }
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

    private void updateDetailsFromREST() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(TheMovieDBAPI.BASE_URL)
                .build();
        TheMovieDBAPI theMovieDBAPI = retrofit.create(TheMovieDBAPI.class);

        Call<ResponseBody> call = theMovieDBAPI.getMovieDetailAndTrailers(mMovie.getId());

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                int duration = 0;
                Trailer[] trailers = null;
                if (response.isSuccessful()) {
                    try {
                        String json = response.body().string();
                        duration = TheMovieDBJsonUtils.getMovieDuration(json);
                        trailers = TheMovieDBJsonUtils.getTrailers(json);
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage(), e);
                    }
                }
                onPostExecuteUpdateDetailFromREST(duration, trailers);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                onPostExecuteUpdateDetailFromREST(0, null);
            }

        });
    }

    private void onPostExecuteUpdateDetailFromREST(int duration, Trailer[] trailers) {
        mMovie.setDuration(duration);
        setDurationTextView();
        mTrailersAdapter.setTrailersData(trailers);

    }

    @Override
    public void onClick(Trailer trailer) {
        // TODO play trailer
    }
}
