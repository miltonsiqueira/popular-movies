package br.com.titomilton.popularmovies;


interface AsyncTaskListener<T>
{
    void onPreExecute();
    void onPostExecute(T result);
}
