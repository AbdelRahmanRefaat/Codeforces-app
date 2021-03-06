package com.example.codeforces.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.codeforces.pojo.User;
import com.example.codeforces.pojo.UserRepository;

import java.util.List;

public class UserViewModel extends AndroidViewModel {

    private UserRepository repository;
    private LiveData<List<User>> allUsers;

    public UserViewModel(@NonNull Application application) {
        super(application);
        repository = new UserRepository(application);
        allUsers = repository.getAllUsers();
    }
    public int hasUser(String handle){
        return repository.hasUser(handle);
    }
    public void update(User user){
        repository.update(user);
    }
    public void insert(User user){
        repository.insert(user);
    }
    public void delete(User user){
        repository.delete(user);
    }
    public LiveData<List<User>> getAllUsers(){
        return this.allUsers;
    }
    public User getUserByHandle(String handle){
        return repository.getUserByHandle(handle);
    }
    public List<User> getAllUsersSync(){
        return repository.getAllUsersSync();
    }
}

