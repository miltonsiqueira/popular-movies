package br.com.titomilton.popularmovies.utils;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class TheMovieDBJsonUtils {
    private static final String TAG = TheMovieDBJsonUtils.class.getSimpleName();
    private static final String JSON_STATUS_MESSAGE = "status_message";
    private static final String JSON_RESULTS = "results";
    private static final String JSON_TITLE = "title";

    public static String[] getMoviesStringsFromJson(String jsonMoviesResponse) throws JSONException {
            String[] parsedMoviesData;

            JSONObject jsonMovies = new JSONObject(jsonMoviesResponse);
            if (jsonMovies.has(JSON_STATUS_MESSAGE)) {
                String statusMessage = jsonMovies.getString(JSON_STATUS_MESSAGE);
                throw new RuntimeException(statusMessage);
            }

            JSONArray jsonResults = jsonMovies.getJSONArray(JSON_RESULTS);
            int totalResults = jsonResults.length();
            parsedMoviesData = new String[totalResults];

            for (int i = 0; i < totalResults; i++) {
                JSONObject item = jsonResults.getJSONObject(i);
                String title = item.getString(JSON_TITLE);
                parsedMoviesData[i] = title;
            }

            return parsedMoviesData;

    }
}
