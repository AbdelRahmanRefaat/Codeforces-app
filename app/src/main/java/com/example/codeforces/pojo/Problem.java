package com.example.codeforces.pojo;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.example.codeforces.ProblemTagsConverter;

import java.util.List;

//@Entity(tableName = "problem_table")
@Entity(tableName = "problem_table", primaryKeys  = {"contestId" , "index"})
public class Problem {


    //private int id;

    @NonNull
    private String index;

    @NonNull
    private String name;

    private int contestId;

    private int rating;

    private int solvedCount;

    @TypeConverters(ProblemTagsConverter.class)
    private List<String> tags;
    public Problem(){
//        index = "dummy";
//        name = "#dummy";
//        contestId=-1;
//        solvedCount=-1;
//        rating = -1;
    }
    public Problem(String index, String name,int contestId, int rating, int solvedCount, List<String> tags) {
        this.index = index;
        this.name = name;
        this.rating = rating;
        this.solvedCount = solvedCount;
        this.tags = tags;
        this.contestId = contestId;
    }




    public int getSolvedCount() {
        return solvedCount;
    }

    public void setSolvedCount(int solvedCount) {
        this.solvedCount = solvedCount;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    @TypeConverters(ProblemTagsConverter.class)
    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public int getContestId() {
        return contestId;
    }

    public void setContestId(int contestId) {
        this.contestId = contestId;
    }
}
