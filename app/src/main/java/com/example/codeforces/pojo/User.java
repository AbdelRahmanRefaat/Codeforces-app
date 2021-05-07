package com.example.codeforces.pojo;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_table")
public class User {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String handle;

    private int rating;

    private int maxRating;

    private String rank;

    private String maxRank;

    private int userRatingChange;

    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    private byte[] image;

    public User(String handle, int rating, int maxRating, String rank, String maxRank, byte[] image, int userRatingChange) {
        this.handle = handle;
        this.rating = rating;
        this.maxRating = maxRating;
        this.rank = (rank == null ? "None" : rank); // rank could be absent
        this.maxRank = (maxRank == null ? "None" : maxRank); // max rank could b ee absent
        this.image = image;
        this.userRatingChange = userRatingChange;
    }


    public void setId(int id) {
        this.id = id;
    }

    public byte[] getImage() {
        return image;
    }

    public int getId() {
        return id;
    }

    public String getHandle() {
        return handle;
    }

    public int getRating() {
        return rating;
    }

    public String getRank() {
        return rank;
    }

    public int getMaxRating() {
        return maxRating;
    }

    public String getMaxRank() {
        return maxRank;
    }

    public int getUserRatingChange() {
        return userRatingChange;
    }
    public void setUserRatingChange(int ratingChange){
        this.userRatingChange = ratingChange;
    }
}
