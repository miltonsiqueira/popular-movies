package br.com.titomilton.popularmovies;


public class Trailer {
    private String youtubeId;
    private String name;

    public Trailer(String youtubeId, String name) {
        this.youtubeId = youtubeId;
        this.name = name;
    }

    public String getYoutubeId() {
        return youtubeId;
    }

    public void setYoutubeId(String youtubeId) {
        this.youtubeId = youtubeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
