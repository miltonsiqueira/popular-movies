package br.com.titomilton.popularmovies;


import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {

    private int id;
    private String title;
    private String releaseDate;
    private String voteAverage;
    private String synopsis;
    private String posterUrl;
    private int duration;

    public Movie(int id, String title, String releaseDate, String voteAverage, String synopsis, String posterUrl, int duration) {
        this.id = id;
        this.title = title;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
        this.synopsis = synopsis;
        this.posterUrl = posterUrl;
        this.duration = duration;
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
        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeString(releaseDate);
        parcel.writeString(voteAverage);
        parcel.writeString(synopsis);
        parcel.writeString(posterUrl);
        parcel.writeInt(duration);
    }

    private void readFromParcel(Parcel in) {
        id = in.readInt();
        title = in.readString();
        releaseDate = in.readString();
        voteAverage = in.readString();
        synopsis = in.readString();
        posterUrl = in.readString();
        duration = in.readInt();
    }

    public int getId() {
        return id;
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

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}

