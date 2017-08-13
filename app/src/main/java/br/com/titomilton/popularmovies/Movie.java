package br.com.titomilton.popularmovies;


import android.os.Parcel;
import android.os.Parcelable;

// Movie details layout contains title, release date, movie poster, vote average, and plot synopsis
public class Movie implements Parcelable
{
    private String title;
    private String releaseDate;
    private String voteAverage;
    private String synopsis;
    private String posterUrl;

    public Movie(String title, String releaseDate, String voteAverage, String synopsis, String posterUrl) {
        this.title = title;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
        this.synopsis = synopsis;
        this.posterUrl = posterUrl;
    }

    private Movie(Parcel in) {
        readFromParcel(in);

    }


    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(releaseDate);
        parcel.writeString(voteAverage);
        parcel.writeString(synopsis);
        parcel.writeString(posterUrl);
    }

    private void readFromParcel(Parcel in) {
        title = in.readString();
        releaseDate = in.readString();
        voteAverage = in.readString();
        synopsis = in.readString();
        posterUrl = in.readString();
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public String getSynopsis() {
        return synopsis;
    }
}

