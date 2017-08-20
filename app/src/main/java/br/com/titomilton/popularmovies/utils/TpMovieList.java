package br.com.titomilton.popularmovies.utils;


import okhttp3.ResponseBody;
import retrofit2.Call;

public enum TpMovieList {
    POPULAR {
        @Override
        public Call<ResponseBody> endpoint(TheMovieDBAPI theMovieDBAPI) {
            return theMovieDBAPI.getPopularMovies();
        }
    },
    TOP_RATED {
        @Override
        public Call<ResponseBody> endpoint(TheMovieDBAPI theMovieDBAPI) {
            return theMovieDBAPI.getTopRatedMovies();
        }
    };
    public abstract Call<ResponseBody> endpoint(TheMovieDBAPI theMovieDBAPI);

}