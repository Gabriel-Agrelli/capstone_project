package com.example.android.mymovies.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

@Entity(tableName = "favorites")
public class Movie implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int idFromApi;
    private String title;
    private String releaseDate;
    private String overview;
    private Long voteAverage;
    private String posterPath;
    @ColumnInfo(name = "updated_at")
    private Date updatedAt;
    private boolean favorite;

    @Ignore
    public Movie(int idFromApi, String title, String releaseDate, String overview, Long voteAverage, String posterPath, Date updatedAt, boolean favorite) {
        this.idFromApi = idFromApi;
        this.title = title;
        this.releaseDate = releaseDate;
        this.overview = overview;
        this.voteAverage = voteAverage;
        this.posterPath = posterPath;
        this.updatedAt = updatedAt;
        this.favorite = favorite;
    }

    public Movie(int id, int idFromApi, String title, String releaseDate, String overview, Long voteAverage, String posterPath, Date updatedAt, boolean favorite) {
        this.id = id;
        this.idFromApi = idFromApi;
        this.title = title;
        this.releaseDate = releaseDate;
        this.overview = overview;
        this.voteAverage = voteAverage;
        this.posterPath = posterPath;
        this.updatedAt = updatedAt;
        this.favorite = favorite;
    }

    @Ignore
    public Movie() {
    }

    private Movie(Parcel in) {
        id = in.readInt();
        idFromApi = in.readInt();
        title = in.readString();
        releaseDate = in.readString();
        overview = in.readString();
        voteAverage = in.readLong();
        posterPath = in.readString();
        updatedAt = (Date) in.readSerializable();
        favorite = in.readByte() != 0;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getIdFromApi() {
        return idFromApi;
    }

    public void setIdFromApi(int idFromApi) {
        this.idFromApi = idFromApi;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Long getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(Long voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeInt(idFromApi);
        parcel.writeString(title);
        parcel.writeString(releaseDate);
        parcel.writeString(overview);
        parcel.writeLong(voteAverage);
        parcel.writeString(posterPath);
        parcel.writeSerializable(updatedAt);
        parcel.writeByte((byte) (favorite ? 1 : 0));
    }
}


