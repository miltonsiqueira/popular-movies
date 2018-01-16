package br.com.titomilton.popularmovies.utils;


import br.com.titomilton.popularmovies.BuildConfig;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface TheMovieDBAPI {
    String BASE_URL = "https://api.themoviedb.org/3/";
    String IMAGE_SIZE_PATH = "w185"; //"w342";//"w185";
    String MOVIE_IMAGE_WITH_SIZE_BASE_URL = "http://image.tmdb.org/t/p/" + IMAGE_SIZE_PATH;
    String API_KEY_PARAM_VALUE = "api_key=" + BuildConfig.THE_MOVIE_DB_API_KEY;

    @GET("movie/popular?" + API_KEY_PARAM_VALUE)
    Call<ResponseBody> getPopularMovies();

    @GET("movie/top_rated?" + API_KEY_PARAM_VALUE)
    Call<ResponseBody> getTopRatedMovies();

    @GET("movie/{id}/videos?" + API_KEY_PARAM_VALUE)
    Call<ResponseBody> getTrailers(@Path("id") int id);

    @GET("movie/{id}?" + API_KEY_PARAM_VALUE + "&append_to_response=videos,reviews")
    Call<ResponseBody> getMovieDetailAndTrailers(@Path("id") int id);
    //http://api.themoviedb.org/3/movie/157336?api_key=e8b866caa3a0041499e35a2d5c023384&append_to_response=videos


}
