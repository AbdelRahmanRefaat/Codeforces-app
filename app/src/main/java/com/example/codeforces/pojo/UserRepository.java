package com.example.codeforces.pojo;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class UserRepository {

    private UserDao userDao;
    private LiveData<List<User>> allUsers;


    public UserRepository(Application application) {
        UserDataBase database = UserDataBase.getInstance(application);
        userDao = database.userDao();
        allUsers = userDao.getAllUsers();
    }

    public void insert(User user) {
        new InsertUserAsyncTask(userDao).execute(user);
    }

    public void delete(User user) {
        new DeleteUserAsyncTask(userDao).execute(user);
    }

    public LiveData<List<User>> getAllUsers() {
        return this.allUsers;
    }

    public List<User> getAllUsersSync(){
        try {
            return new GetAllUsersAsyncTaskSync(userDao).execute().get();
        }catch (Exception e){
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    public Integer hasUser(String handle){
        try {
            return new HasUserAsyncTask(userDao).execute(handle).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 0;
    }
    public User getUserByHandle(String handle){
        try {
            return new GetUserByHandleAsyncTask(userDao).execute(handle).get();
        }catch (ExecutionException | InterruptedException E){
            E.printStackTrace();

        }
        return null;
    }

    public void update(User user){
            new UpdateUserAsyncTask(userDao).execute(user);
    }

    private static class InsertUserAsyncTask extends AsyncTask<User, Void, Void> {
        private UserDao userDao;

        public InsertUserAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground(User... users) {
            userDao.insert(users[0]);
            return null;
        }
    }


    private static class DeleteUserAsyncTask extends AsyncTask<User, Void, Void> {
        private UserDao userDao;

        public DeleteUserAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground(User... users) {
            userDao.delete(users[0]);
            return null;
        }
    }
    private static class GetUserByHandleAsyncTask extends AsyncTask<String, Void, User> {
        private UserDao userDao;

        public GetUserByHandleAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected User doInBackground(String... handles) {
            return userDao.getUserByHandle(handles[0]);
        }
    }

    private static class UpdateUserAsyncTask extends AsyncTask<User, Void, Void> {
        private UserDao userDao;

        public UpdateUserAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground(User... users) {
            userDao.update(users[0]);
            return null;
        }
    }
    private static class GetAllUsersAsyncTaskSync extends AsyncTask<Void , Void, List<User>>{
        UserDao userDao;
        public GetAllUsersAsyncTaskSync(UserDao userDao){
            this.userDao = userDao;
        }
        @Override
        protected List<User> doInBackground(Void... voids) {
            return userDao.getAllUsersSync();
        }
    }

    private class HasUserAsyncTask extends  AsyncTask<String,Void,Integer>{
        UserDao userDao;
        public HasUserAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected Integer doInBackground(String... handles) {
            return userDao.hasUser(handles[0]);
        }
    }



}
