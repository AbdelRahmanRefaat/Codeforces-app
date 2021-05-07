package com.example.codeforces.Models;

import com.example.codeforces.pojo.Problem;

import java.util.List;

public class UserSubmissionModel {

    private String status;
    private List<Result> result;

    public String getStatus() {
        return status;
    }

    public List<Result> getResult() {
        return result;
    }

    public class Result{
        private Problem problem;
        private String programmingLanguage;
        private String verdict;

        public Problem getProblem() {
            return problem;
        }

        public String getProgrammingLanguage() {
            return programmingLanguage;
        }

        public String getVerdict() {
            return verdict;
        }



    }
}
