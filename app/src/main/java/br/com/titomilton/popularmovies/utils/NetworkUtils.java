package br.com.titomilton.popularmovies.utils;


import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import br.com.titomilton.popularmovies.BuildConfig;

public final class NetworkUtils {
    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String API_KEY = BuildConfig.THE_MOVIE_DB_API_KEY;
    private static final String MOVIE_BASE_URL = "https://api.themoviedb.org/3";

    private static final String IMAGE_SIZE_PATH = "w185";
    public static final String MOVIE_IMAGE_WITH_SIZE_BASE_URL = "http://image.tmdb.org/t/p/" + IMAGE_SIZE_PATH;

    public enum TpMovieList {
        POPULAR(MOVIE_BASE_URL + "/movie/popular"),
        TOP_RATED(MOVIE_BASE_URL + "/movie/top_rated");
        private final String url;

        TpMovieList(String url) {
            this.url = url;
        }
    }

    public static URL buildUrl(TpMovieList tpMovieList) {
        final String API_KEY_PARAM = "api_key";
        Uri builtUri = Uri.parse(tpMovieList.url).buildUpon()
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

}
