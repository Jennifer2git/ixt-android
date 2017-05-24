package com.imax.ipt.model;

public class MusicTrack extends Media {
    private String albumId;
    private Person artist;
    private Genre genre;

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public Person getArtist() {
        return artist;
    }

    public void setArtist(Person artist) {
        this.artist = artist;
    }



    public Genre getGenre(){
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

}
