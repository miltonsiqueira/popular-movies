package br.com.titomilton.popularmovies.utils;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import br.com.titomilton.popularmovies.Movie;

public final class TheMovieDBJsonUtils {
    private static final String TAG = TheMovieDBJsonUtils.class.getSimpleName();
    private static final String JSON_STATUS_MESSAGE = "status_message";
    private static final String JSON_RESULTS = "results";
    private static final String JSON_TITLE = "title";
    private static final String JSON_VOTE_AVERAGE = "vote_average";
    private static final String JSON_SYNOPSIS = "overview";
    private static final String JSON_RELEASE_DATE = "release_date";
    private static final String JSON_POSTER_PATH = "poster_path";

    public static Movie[] getMoviesStringsFromJson(String jsonMoviesResponse) throws JSONException {
            Movie[] parsedMoviesData;

            JSONObject jsonMovies = new JSONObject(jsonMoviesResponse);
            if (jsonMovies.has(JSON_STATUS_MESSAGE)) {
                String statusMessage = jsonMovies.getString(JSON_STATUS_MESSAGE);
                throw new RuntimeException(statusMessage);
            }

            JSONArray jsonResults = jsonMovies.getJSONArray(JSON_RESULTS);
            int totalResults = jsonResults.length();
            parsedMoviesData = new Movie[totalResults];

            for (int i = 0; i < totalResults; i++) {
                JSONObject item = jsonResults.getJSONObject(i);
                String title = item.getString(JSON_TITLE);
                String posterPath = new StringBuilder()
                        .append(NetworkUtils.MOVIE_IMAGE_WITH_SIZE_BASE_URL)
                        .append(item.getString(JSON_POSTER_PATH))
                        .toString();
                String voteAverage = item.getString(JSON_VOTE_AVERAGE);
                String releaseDate = item.getString(JSON_RELEASE_DATE);
                String synopsis = item.getString(JSON_SYNOPSIS);
                parsedMoviesData[i] = new Movie(title, releaseDate, voteAverage, synopsis, posterPath);
            }

            return parsedMoviesData;

    }
}
