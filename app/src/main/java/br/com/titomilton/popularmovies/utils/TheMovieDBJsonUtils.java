package br.com.titomilton.popularmovies.utils;


import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import br.com.titomilton.popularmovies.Movie;
import br.com.titomilton.popularmovies.Trailer;

public final class TheMovieDBJsonUtils {
    private static final String JSON_STATUS_MESSAGE = "status_message";
    private static final String JSON_ID = "id";
    private static final String JSON_RESULTS = "results";
    private static final String JSON_TITLE = "title";
    private static final String JSON_VOTE_AVERAGE = "vote_average";
    private static final String JSON_SYNOPSIS = "overview";
    private static final String JSON_RELEASE_DATE = "release_date";
    private static final String JSON_POSTER_PATH = "poster_path";

    private static final String JSON_TRAILER_VIDEOS = "videos";
    private static final String JSON_TRAILER_RESULTS = "results";
    private static final String JSON_TRAILER_YOUTUBE_ID = "key";
    private static final String JSON_TRAILER_NAME = "name";

    private static final String JSON_MOVIE_DURATION = "runtime";

    public static final String DATE_FORMAT = "yyyy-MM-dd";

    public static Movie[] getMoviesStringsFromJson(String jsonMoviesResponse) throws JSONException {
        Movie[] parsedMoviesData;

        JSONObject jsonMovies = new JSONObject(jsonMoviesResponse);

        checkErrorResponce(jsonMovies);

        JSONArray jsonResults = jsonMovies.getJSONArray(JSON_RESULTS);
        int totalResults = jsonResults.length();
        parsedMoviesData = new Movie[totalResults];

        for (int i = 0; i < totalResults; i++) {
            JSONObject item = jsonResults.getJSONObject(i);
            Movie movie = getMovie(item);
            parsedMoviesData[i] = movie;
        }

        return parsedMoviesData;

    }

    @NonNull
    private static Movie getMovie(JSONObject item) throws JSONException {
        String title = item.getString(JSON_TITLE);
        String posterPath = TheMovieDBAPI.MOVIE_IMAGE_WITH_SIZE_BASE_URL +
                item.getString(JSON_POSTER_PATH);
        String voteAverage = item.getString(JSON_VOTE_AVERAGE);
        String releaseDate = item.getString(JSON_RELEASE_DATE);
        String synopsis = item.getString(JSON_SYNOPSIS);
        int id = item.getInt(JSON_ID);

        int duration = 0;
        if (item.has(JSON_MOVIE_DURATION)) {
            duration = item.getInt(JSON_MOVIE_DURATION);
        }

        return new Movie(id, title, releaseDate, voteAverage, synopsis, posterPath, duration);
    }

    private static void checkErrorResponce(JSONObject jsonMovies) throws JSONException {
        if (jsonMovies.has(JSON_STATUS_MESSAGE)) {
            String statusMessage = jsonMovies.getString(JSON_STATUS_MESSAGE);
            throw new RuntimeException(statusMessage);
        }
    }

    public static Trailer[] getTrailers(String json) throws JSONException {
        Trailer[] parsedTrailerData;

        JSONObject jsonObjRoot = new JSONObject(json);

        checkErrorResponce(jsonObjRoot);
        JSONObject jsonVideos = jsonObjRoot.getJSONObject(JSON_TRAILER_VIDEOS);
        JSONArray jsonResults = jsonVideos.getJSONArray(JSON_TRAILER_RESULTS);
        int totalResults = jsonResults.length();
        parsedTrailerData = new Trailer[totalResults];

        for (int i = 0; i < totalResults; i++) {
            JSONObject item = jsonResults.getJSONObject(i);
            String youtubeId = item.getString(JSON_TRAILER_YOUTUBE_ID);
            String name = item.getString(JSON_TRAILER_NAME);
            parsedTrailerData[i] = new Trailer(youtubeId, name);
        }

        return parsedTrailerData;
    }

    public static Movie getMovieDetail(String json) throws JSONException {
        JSONObject jsonObjRoot = new JSONObject(json);

        checkErrorResponce(jsonObjRoot);

        return getMovie(jsonObjRoot);

    }

}
