package br.com.titomilton.popularmovies.data;


import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import br.com.titomilton.popularmovies.Movie;
import br.com.titomilton.popularmovies.utils.TpMovieList;

public class FavoriteMovieAsyncTask extends AsyncTask<Void, Void, Movie[]> {
    private final Context context;
    private final TpMovieList.CallbackMovies callbackMovies;
    private Throwable error;

    public FavoriteMovieAsyncTask(Context context, TpMovieList.CallbackMovies callbackMovies) {
        this.context = context;
        this.callbackMovies = callbackMovies;
    }

    @Override
    protected Movie[] doInBackground(Void... voids) {
        Cursor cursor = null;
        Movie[] movies = null;
        error = null;
        try {
            cursor = context.getContentResolver().query(FavoriteMovieContract.FavoriteMovieEntry.CONTENT_URI,
                    FavoriteMovieContract.FavoriteMovieEntry.ALL_COLUMNS, null, null,
                    FavoriteMovieContract.FavoriteMovieEntry.COLUMN_MOVIE_NAME);

            movies = new Movie[cursor.getCount()];
            int i = 0;

            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_MOVIE_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_MOVIE_NAME));
                String posterUrl = cursor.getString(cursor.getColumnIndexOrThrow(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_MOVIE_POSTER_URL));
                Movie movie = new Movie(id, name, null, null, null, posterUrl, 0);
                movies[i] = movie;
                i++;
            }

        } catch (Exception e) {
            error = e;

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return movies;


    }

    @Override
    protected void onPostExecute(Movie[] movies) {
        if (error == null) {
            callbackMovies.get(movies);
        } else {
            callbackMovies.onFail(error);
        }
    }



}
