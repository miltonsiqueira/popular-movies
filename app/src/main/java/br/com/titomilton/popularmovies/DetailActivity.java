package br.com.titomilton.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    private Movie mMovie;
    private TextView mTitleTextView;
    private TextView mReleaseDateTextView;
    private TextView mVoteAverageTextView;
    private TextView mSynopsis;
    private ImageView mPoster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mTitleTextView = (TextView) findViewById(R.id.tv_title_detail);
        mReleaseDateTextView = (TextView) findViewById(R.id.tv_release_date_detail);
        mVoteAverageTextView = (TextView) findViewById(R.id.tv_vote_average);
        mSynopsis= (TextView) findViewById(R.id.tv_plot_synopsis);
        mPoster= (ImageView) findViewById(R.id.iv_poster_detail);

        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra(Intent.EXTRA_TEXT)) {
                mMovie = intentThatStartedThisActivity.getParcelableExtra(Intent.EXTRA_TEXT);
                mTitleTextView.setText(mMovie.getTitle());
                mReleaseDateTextView.setText(mMovie.getReleaseDate());
                mVoteAverageTextView.setText(mMovie.getVoteAverage());
                mSynopsis.setText(mMovie.getSynopsis());
                //mPoster.setImage(mMovie.getSynopsis());
            }
        }
    }
}
