package com.example.codeforces;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class ProblemTagsConverter {


    @TypeConverter
    public static  String fromTagsListToString(List<String> tags){
        if(tags == null)
            return null;
        Gson gson = new Gson();
        Type type= new TypeToken<List<String>>(){}.getType();
        String json = gson.toJson(tags,type);
        return json;
    }

    @TypeConverter
    public static  List<String> fromStringToTagsList(String json){
        if(json == null)
            return null;

        Gson gson = new Gson();
        Type type = new TypeToken<List<String>>(){}.getType();
        List<String> tags = gson.fromJson(json,type);
        return tags;
    }
}
