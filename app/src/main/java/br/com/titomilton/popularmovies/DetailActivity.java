package br.com.titomilton.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Movie mMovie;
        TextView mTitleTextView;
        TextView mReleaseDateTextView;
        TextView mVoteAverageTextView;
        TextView mSynopsis;
        ImageView mPoster;

        mTitleTextView = findViewById(R.id.tv_title_detail);
        mReleaseDateTextView = findViewById(R.id.tv_release_date_detail);
        mVoteAverageTextView = findViewById(R.id.tv_vote_average);
        mSynopsis= findViewById(R.id.tv_plot_synopsis);
        mPoster= findViewById(R.id.iv_poster_detail);

        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra(Intent.EXTRA_TEXT)) {
                mMovie = intentThatStartedThisActivity.getParcelableExtra(Intent.EXTRA_TEXT);
                mTitleTextView.setText(mMovie.getTitle());
                mReleaseDateTextView.setText(mMovie.getReleaseDate());
                mVoteAverageTextView.setText(mMovie.getVoteAverage());
                mSynopsis.setText(mMovie.getSynopsis());
                Picasso.with(this)
                        .load(mMovie.getPosterUrl())
                        .into(mPoster);
            }
        }
    }
}
