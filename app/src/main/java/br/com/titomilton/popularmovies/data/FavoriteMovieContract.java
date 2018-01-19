
package br.com.titomilton.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;


public class FavoriteMovieContract {

    public static final String CONTENT_AUTHORITY = "br.com.titomilton.popularmovies";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_FAVORITE_MOVIE = "favorite";

    /* Inner class that defines the table contents of the weather table */
    public static final class FavoriteMovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_FAVORITE_MOVIE)
                .build();

        public static final String TABLE_NAME = "favorite";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_MOVIE_NAME = "movie_name";
        public static final String COLUMN_MOVIE_POSTER_URL = "poster_url";
        public static final String[] ALL_COLUMNS = new String[] {
            COLUMN_MOVIE_ID, COLUMN_MOVIE_NAME, COLUMN_MOVIE_POSTER_URL
        };
        public static Uri getContentUriWithId(int id) {
            String stringId = Integer.toString(id);
            return CONTENT_URI.buildUpon().appendPath(stringId).build();
        }
    }
}