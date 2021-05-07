package com.example.codeforces.Models;

import androidx.lifecycle.LiveData;

import com.example.codeforces.pojo.Problem;

import java.util.List;

import retrofit2.Response;

public class ProblemModel {

    private String status;
    private Result result;

    public String getStatus() {
        return status;
    }

    public Result getResult() {
        return result;
    }



    public class Result {
        private List<P> problems;
        private List<ProblemStatistics> problemStatistics;

        public List<ProblemStatistics> getProblemStatistics() {
            return problemStatistics;
        }

        public List<P> getProblems() {
            return problems;
        }

        public class ProblemStatistics {
            private int contestId;
            private String index;
            private int solvedCount;
            public int getContestId() {
                return contestId;
            }

            public String getIndex() {
                return index;
            }

            public int getSolvedCount() {
                return solvedCount;
            }
        }
        public class P{
            private int contestId;

            private String index;

            private String name;

            private int rating;
            private List<String >tags;

            public List<String> getTags() {
                return tags;
            }

            public String getIndex() {
                return index;
            }

            public String getName() {
                return name;
            }

            public int getRating() {
                return rating;
            }

            public int getContestId() {
                return contestId;
            }
        }
    }
}
