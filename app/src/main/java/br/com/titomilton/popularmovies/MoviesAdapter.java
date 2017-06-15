package br.com.titomilton.popularmovies;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieAdapterViewHolder> {

    private Movie[] mMoviesData;

    private final MoviesAdapterOnClickHandler mClickHandler;

    public interface MoviesAdapterOnClickHandler {
        void onClick(Movie movie);
    }

    public MoviesAdapter(MoviesAdapterOnClickHandler mClickHandler) {
        this.mClickHandler = mClickHandler;
    }

    public void setMoviesData(Movie[] moviesData) {
        mMoviesData = moviesData;
        notifyDataSetChanged();
    }

    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final TextView mMovieTextView;
        public final ImageView mImageView;
        public final Context mContext;

        public MovieAdapterViewHolder(View view) {
            super(view);
            mMovieTextView = (TextView) view.findViewById(R.id.tv_movie_data);
            mImageView = (ImageView) view.findViewById(R.id.iv_poster);
            mContext = view.getContext();
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Movie movie = mMoviesData[adapterPosition];
            mClickHandler.onClick(movie);
        }
    }

    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.movie_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieAdapterViewHolder holder, int position) {
        Movie movie = mMoviesData[position];
        holder.mMovieTextView.setText(movie.getTitle());
        Picasso.with(holder.mContext)
                .load(movie.getPosterUrl())
                .into(holder.mImageView);
    }

    @Override
    public int getItemCount() {
        return (mMoviesData == null ? 0 : mMoviesData.length);
    }


}
