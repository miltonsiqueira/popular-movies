package br.com.titomilton.popularmovies;


public class Movie {
    private final String title;
    private final String posterUrl;

    public Movie(String title, String posterUrl) {
        this.title = title;
        this.posterUrl = posterUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getPosterUrl() {
        return posterUrl;
    }
}

