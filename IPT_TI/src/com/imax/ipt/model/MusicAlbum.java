package com.imax.ipt.model;

public class MusicAlbum extends Media {
    private String name;
    private MusicTrack[] tracks;
    private boolean favorite;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MusicTrack[] getTracks() {
        return tracks;
    }

    public void setTracks(MusicTrack[] tracks) {
        this.tracks = tracks;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }
}
