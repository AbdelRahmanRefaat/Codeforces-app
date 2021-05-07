package com.example.codeforces.Models;

import com.example.codeforces.pojo.Contest;

import java.util.List;

public class ContestModel {
    private String status;
    private List<Result> result;

    public String getStatus() {
        return status;
    }

    public List<Result> getResult() {
        return result;
    }

    public class Result
    {
        private Integer id;
        private String name;
        private String type;
        private String phase;
        private Long durationSeconds;
        private Long startTimeSeconds;

        public Integer getId() {
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

        public Long getDurationSeconds() {
            return durationSeconds;
        }

        public Long getStartTimeSeconds() {
            return startTimeSeconds;
        }


    }
}
