package com.example.codeforces.pojo;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "contest_table")
public class Contest {

    @PrimaryKey
    private int id;

    private String name;

    private String type;

    private String phase;

    private long durationSeconds;

    private long startTimeSeconds;

    public Contest(int id, String name, String type, String phase, long durationSeconds, long startTimeSeconds) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.phase = phase;
        this.durationSeconds = durationSeconds;
        this.startTimeSeconds = startTimeSeconds;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getPhase() {
        return phase;
    }

    public long getDurationSeconds() {
        return durationSeconds;
    }

    public long getStartTimeSeconds() {
        return startTimeSeconds;
    }
}
