package com.imax.ipt.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.imax.ipt.controller.eventbus.handler.Handler;

public class Movie extends Media implements Parcelable {
    private boolean favorite = false;
    private int time;
    private boolean bluray;
    private String synopsis = "";
    private Genre[] genres = new Genre[0];
    private Person[] actors = new Actor[0];
    private Person[] directors = new Director[0];
    private int year;
    private String rating = "";
    private boolean imax;
    private boolean threeD;


    public Movie() {
        setId(Handler.DEFAULT_GUID);
    }

    public boolean isThreeD() {
        return threeD;
    }

    public void setThreeD(boolean threeD) {
        this.threeD = threeD;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public boolean isBluray() {
        return bluray;
    }

    public void setBluray(boolean bluray) {
        this.bluray = bluray;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public Genre[] getGenre() {
        return genres;
    }

    public void setGenre(Genre[] genre) {
        this.genres = genre;
    }

    public Person[] getActors() {
        return actors;
    }

    public void setActors(Person[] actors) {
        this.actors = actors;
    }

    public Person[] getDirectors() {
        return directors;
    }

    public void setDirectors(Person[] directors) {
        this.directors = directors;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public Movie getValue() {
        return this;
    }

    public boolean isImax() {
        return imax;
    }

    public void setImax(boolean imax) {
        this.imax = imax;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getCoverArtPath());
        dest.writeParcelableArray(actors, flags);
        dest.writeParcelableArray(directors, flags);
        dest.writeByte((byte) (favorite ? 1 : 0));
        dest.writeParcelableArray(genres, flags);
        dest.writeString(id);
        dest.writeByte((byte) (loading ? 1 : 0));
        dest.writeString(rating);
        dest.writeString(synopsis);
        dest.writeInt(year);
        dest.writeString(getTitle());

    }

    public static final Parcelable.Creator<Movie> CREATOR = new Creator<Movie>() {

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }

        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }
    };

    private Movie(Parcel source) {
        setCoverArtPath(source.readString());
        Parcelable[] parcels = source.readParcelableArray(Person.class.getClassLoader());
        actors = new Person[parcels.length];
        for (int i = 0; i < parcels.length; i++) {
            actors[i] = (Person) parcels[i];
        }
        parcels = source.readParcelableArray(Person.class.getClassLoader());
        directors = new Person[parcels.length];
        for (int i = 0; i < parcels.length; i++) {
            directors[i] = (Person) parcels[i];
        }
        favorite = (source.readByte() == 1);

        parcels = source.readParcelableArray(Genre.class.getClassLoader());
        genres = new Genre[parcels.length];
        for (int i = 0; i < parcels.length; i++) {
            genres[i] = (Genre) parcels[i];
        }
        id = source.readString();
        loading = (source.readByte() == 1);
        rating = source.readString();
        synopsis = source.readString();
        year = source.readInt();
        setTitle(source.readString());
    }
}
