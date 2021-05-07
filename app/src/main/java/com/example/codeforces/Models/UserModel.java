package com.example.codeforces.Models;

import java.util.List;

public class UserModel {

    private String status;
    private List<Result> result;
    public class Result {
        private String handle;
        private int rating;
        private String rank="None";
        private int maxRating;
        private String avatar;
        private String maxRank="None";
        private String titlePhoto;


        public String getTitlePhoto() {
            return titlePhoto;
        }

        public String getHandle() {
            return handle;
        }

        public void setHandle(String handle) {
            this.handle = handle;
        }

        public Integer getRating() {
            return rating;
        }

        public void setRating(Integer rating) {
            this.rating = rating;
        }

        public String getRank() {
            return rank;
        }

        public void setRank(String rank) {
            this.rank = rank;
        }

        public Integer getMaxRating() {
            return maxRating;
        }

        public void setMaxRating(Integer maxRating) {
            this.maxRating = maxRating;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getMaxRank() {
            return maxRank;
        }

        public void setMaxRank(String maxRank) {
            this.maxRank = maxRank;
        }
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Result> getResult() {
        return result;
    }

    public void setResult(List<Result> result) {
        this.result = result;
    }





}
