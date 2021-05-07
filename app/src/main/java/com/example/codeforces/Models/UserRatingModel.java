package com.example.codeforces.Models;

import java.util.List;

public class UserRatingModel {

    private String status;
    List<Result> result;

    public List<Result> getResult() {
        return result;
    }

    public void setResult(List<Result> result) {
        this.result = result;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public class Result{
        public Integer getContestId() {
            return contestId;
        }

        public void setContestId(Integer contestId) {
            this.contestId = contestId;
        }

        public String getContestName() {
            return contestName;
        }

        public void setContestName(String contestName) {
            this.contestName = contestName;
        }

        public String getHandle() {
            return handle;
        }

        public void setHandle(String handle) {
            this.handle = handle;
        }

        public Integer getRank() {
            return rank;
        }

        public void setRank(Integer rank) {
            this.rank = rank;
        }

        public Long getRatingUpdateTimeSeconds() {
            return ratingUpdateTimeSeconds;
        }

        public void setRatingUpdateTimeSeconds(Long ratingUpdateTimeSeconds) {
            this.ratingUpdateTimeSeconds = ratingUpdateTimeSeconds;
        }

        public Integer getOldRating() {
            return oldRating;
        }

        public void setOldRating(Integer oldRating) {
            this.oldRating = oldRating;
        }

        public Integer getNewRating() {
            return newRating;
        }

        public void setNewRating(Integer newRating) {
            this.newRating = newRating;
        }

        private Integer contestId;
        private String contestName;
        private String handle;
        private Integer rank;
        private Long ratingUpdateTimeSeconds;
        private Integer oldRating;
        private Integer newRating;
    }
}
