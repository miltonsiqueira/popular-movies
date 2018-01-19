package br.com.titomilton.popularmovies.utils;


import android.content.Context;
import android.os.AsyncTask;

import br.com.titomilton.popularmovies.Movie;
import br.com.titomilton.popularmovies.R;
import br.com.titomilton.popularmovies.data.FavoriteMovieAsyncTask;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public enum TpMovieList {

    POPULAR(1, R.string.by_most_popular, true) {
        @Override
        public void getMovies(Context context, final CallbackMovies callbackMovies) {
            callRest(callbackMovies, this);
        }

        @Override
        public Call<ResponseBody> endpoint(TheMovieDBAPI theMovieDBAPI) {
            return theMovieDBAPI.getPopularMovies();
        }
    },

    TOP_RATED(2, R.string.by_most_popular, true) {
        @Override
        public void getMovies(Context context, CallbackMovies callbackMovies) {
            callRest(callbackMovies, this);
        }

        @Override
        public Call<ResponseBody> endpoint(TheMovieDBAPI theMovieDBAPI) {
            return theMovieDBAPI.getTopRatedMovies();
        }
    },

    FAVORITED(3, R.string.by_favorited, false) {
        @Override
        public void getMovies(final Context context, final CallbackMovies callbackMovies) {
            AsyncTask<Void, Void, Movie[]> task = new FavoriteMovieAsyncTask(context, callbackMovies);
            task.execute();

        }

        @Override
        public Call<ResponseBody> endpoint(TheMovieDBAPI theMovieDBAPI) {
            return null;
        }
    };

    private final int id;
    private final int stringId;
    private final boolean isDataFromRest;

    TpMovieList(int id, int stringId, boolean isDataFromRest) {
        this.id = id;
        this.stringId = stringId;
        this.isDataFromRest = isDataFromRest;
    }

    private static void callRest(final CallbackMovies callbackMovies, TpMovieList tpMovieList) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(TheMovieDBAPI.BASE_URL)
                .build();
        TheMovieDBAPI theMovieDBAPI = retrofit.create(TheMovieDBAPI.class);

        Call<ResponseBody> call = tpMovieList.endpoint(theMovieDBAPI);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Movie[] movies = null;
                if (response.isSuccessful()) {
                    try {
                        String json = response.body().string();
                        movies = TheMovieDBJsonUtils
                                .getMoviesStringsFromJson(json);
                    } catch (Exception e) {
                        callbackMovies.onFail(e);
                    }
                }
                callbackMovies.get(movies);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                callbackMovies.onFail(t);
            }
        });

    }

    public int getId() {
        return id;
    }

    public int getStringId() {
        return stringId;
    }

    public boolean isDataFromRest() {
        return isDataFromRest;
    }

    public static TpMovieList get(int id) {
        for(TpMovieList tpMovie : TpMovieList.values()) {
            if (tpMovie.getId() == id) {
                return tpMovie;
            }
        }
        return null;
    }

    public abstract Call<ResponseBody> endpoint(TheMovieDBAPI theMovieDBAPI);

    public abstract void getMovies(Context context, CallbackMovies callbackMovies);

    public interface CallbackMovies {
        void get(Movie[] movies);

        void onFail(Throwable t);
    }

}