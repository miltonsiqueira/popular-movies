package br.com.titomilton.popularmovies;


import android.os.AsyncTask;
import android.util.Log;

import java.net.URL;

import br.com.titomilton.popularmovies.utils.NetworkUtils;
import br.com.titomilton.popularmovies.utils.TheMovieDBJsonUtils;

class FetchMoviesTask  extends AsyncTask<NetworkUtils.TpMovieList, Void, Movie[]> {

    private static final String TAG = FetchMoviesTask.class.getSimpleName();

    private final AsyncTaskListener<Movie[]> asyncTaskListener;

    public FetchMoviesTask (AsyncTaskListener<Movie[]> asyncTaskListener) {
        this.asyncTaskListener = asyncTaskListener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        asyncTaskListener.onPreExecute();
    }

    @Override
    protected Movie[] doInBackground(NetworkUtils.TpMovieList... params) {
        NetworkUtils.TpMovieList tpMovieList = params[0];
        URL moviesRequestUrl = NetworkUtils.buildUrl(tpMovieList);

        String jsonMoviesResponse = null;
        try {
            jsonMoviesResponse = NetworkUtils
                    .getResponseFromHttpUrl(moviesRequestUrl);

            return TheMovieDBJsonUtils
                    .getMoviesStringsFromJson(jsonMoviesResponse);


        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage() + " json:" + jsonMoviesResponse);
            return null;
        }
    }

    @Override
    protected void onPostExecute(Movie[] moviesData) {
        asyncTaskListener.onPostExecute(moviesData);
    }
}
