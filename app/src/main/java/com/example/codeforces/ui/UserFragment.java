package com.example.codeforces.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.example.codeforces.Adapters.UserListAdapter;
import com.example.codeforces.Models.UserRatingModel;
import com.example.codeforces.R;
import com.example.codeforces.Models.UserModel;
import com.example.codeforces.ViewModels.UserViewModel;
import com.example.codeforces.interfaces.iUserAPI;
import com.example.codeforces.interfaces.iUserRatingAPI;
import com.example.codeforces.pojo.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserFragment extends Fragment implements AddUserDialog.OnInputSelected,
        UserListAdapter.OnUserListener , CompareUsersDialog.OnHandlesSelected {

    private static final String TAG = "UserFragment";
    public static final String FRAGMENT_TAG = "Users";

    private static final int ADD_USER_DIALOG_REQUEST_CODE = 1;
    private static final int COMPARE_USERS_DIALOG_REQUEST_CODE = 2;

    public static final String BASE_URL = "https://codeforces.com/api/";
    private List<User> usersList;
    private Retrofit retrofit;
    private UserModel mUserModel = null;
    private iUserAPI userAPI;
    private iUserRatingAPI userRatingAPI;

    private RecyclerView recyclerView;
    private UserListAdapter adapter;

    private AddUserDialog dialog;
    private CompareUsersDialog compareUsersDialog;

    private UserViewModel userViewModel;



    // swipe refresh
    private SwipeRefreshLayout refreshLayout;


    public void showAddUserDialog() {
        dialog.show(getActivity().getSupportFragmentManager(), FRAGMENT_TAG);
    }


    private void showCompareDialog(){

        compareUsersDialog.show(getActivity().getSupportFragmentManager(),FRAGMENT_TAG);
    }

    //TODO : 1
    /*
     * this is a listener for the compare dialog
     * will use the 2 handles to compare users statistics
     * */
    @Override
    public void sendHandles(String handle1, String handle2) {
        // TODO : add compare users function
        Log.d(TAG, "sendHandles: Handle1 = " + handle1 + ", Handle2 = " + handle2);

    }

    @Override
    public void onUserClick(User user) {
        Intent intent = new Intent(getActivity(), GraphsActivity.class);
        intent.putExtra(GraphsActivity.EXTRA_HANDLE, user.getHandle());
        intent.putExtra(GraphsActivity.EXTRA_RANK, user.getRank());
        intent.putExtra(GraphsActivity.EXTRA_MAX_RANK,user.getMaxRank());
        intent.putExtra(GraphsActivity.EXTRA_CURRENT_RATING,user.getRating());
        intent.putExtra(GraphsActivity.EXTRA_MAX_RATING,user.getMaxRating());
        intent.putExtra(GraphsActivity.EXTRA_IMAGE,user.getImage());
        startActivity(intent);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated: initializing fragment components...");

        //TODO : 1
        // compare dialog
        /*
         * setting the target fragment is important to make the dialog work
         * */
        compareUsersDialog = new CompareUsersDialog();
        compareUsersDialog.setTargetFragment(UserFragment.this, COMPARE_USERS_DIALOG_REQUEST_CODE);

        /*
        * setting the target fragment is important to make the dialog work
        * */
        dialog = new AddUserDialog();
        dialog.setTargetFragment(UserFragment.this, ADD_USER_DIALOG_REQUEST_CODE);


        // refresh layout
        refreshLayout = ((MainActivity)getActivity()).findViewById(R.id.refresh_layout_users);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                validateUsersList();
            }
        });

        // initing vars
        usersList = new ArrayList<>();
        recyclerView = ((MainActivity) getActivity()).findViewById(R.id.recycler_view_users);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new UserListAdapter(this, getContext());
        recyclerView.setAdapter(adapter);
        setupRetrofit();


        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);

        userViewModel.getAllUsers().observe(getViewLifecycleOwner(), new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                // TODO : add DiffUtil class
                Log.d(TAG, "onChanged: something has changed");
                adapter.submitList(users);

            }
        });

        // this is called first time the app is running
        // to make sure that the users list is up to date.
        validateUsersList();

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());

                dialog.setCancelable(false);

                dialog.setTitle("Watch Out!");
                dialog.setMessage("Are you sure you want to delete this user");


                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                            userViewModel.delete(adapter.getUserAt(viewHolder.getAdapterPosition()));
                        Toast.makeText(getContext(), "User Deleted", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        Toast.makeText(getContext(), "Canceled", Toast.LENGTH_SHORT).show();
                        adapter.notifyItemChanged(viewHolder.getAdapterPosition());
                    }
                }).create().show();

            }
        }).attachToRecyclerView(recyclerView);
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.main_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.compare_users:
                Log.d(TAG, "onOptionsItemSelected: comparing");
              showCompareDialog();
                return true;
            case  R.id.navigation_contests:
                Log.d(TAG, "onOptionsItemSelected: Contests Selected");
                return true;
        }
        return false;
    }

    /*
    * validate the user's list and keep it up to date
    *
    * */
    private void validateUsersList() {
        new ValidateUsersListAsyncTask().execute();
    }
    private class ValidateUsersListAsyncTask extends AsyncTask<Void, Void, Void>{

        private Response<UserRatingModel> response2;

        private List<User> allUsers;
        private List<Integer> allUsersRatingChanges = new ArrayList<>();
        private String handles = "";
        private final List<User> cfUsers = new ArrayList<>();
        private Response<UserModel> response;

        private boolean areEquals(User a, User b){

            return a.getHandle().equals(b.getHandle())
                    && Arrays.equals(a.getImage(),b.getImage())
                    && a.getMaxRank().equals(b.getMaxRank())
                    && a.getRank().equals(b.getRank())
                    && a.getMaxRating() == b.getMaxRating()
                    && a.getRating() == b.getRating()
                    && a.getUserRatingChange() == b.getUserRatingChange();
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d(TAG, "onPreExecute: preparing the handle query string for fetching data from cf");
            refreshLayout.setRefreshing(true);
            allUsers = userViewModel.getAllUsersSync();
            for(User user : allUsers){
                handles = handles + user.getHandle() + ";";
                allUsersRatingChanges.add(user.getUserRatingChange());
            }

        }



        @Override
        protected Void doInBackground(Void... voids) {

            if(allUsers.isEmpty()) {
                return null;
            }
            Log.d(TAG, "doInBackground: fetching the data from codeforces");
            userAPI = retrofit.create(iUserAPI.class);
            Call<UserModel> call = userAPI.getUserInfo(handles);
            try {
                response = call.execute();

                List<UserModel.Result>  result = response.body().getResult();

                for(int i = 0; i < result.size(); ++i){

                    UserModel.Result res = result.get(i);
                    String _handle = res.getHandle();
                    int rating = res.getRating();
                    int maxRating = res.getMaxRating();
                    String rank = res.getRank();
                    String maxRank = res.getMaxRank();
                    String avatar = res.getAvatar();
                    int ratingChange = allUsersRatingChanges.get(i);
                    byte[] image=null;

                    try {
                        // this way the downloading task is done sync
                        image = Glide.with(UserFragment.this)
                                .as(byte[].class)
                                .load("https://" + avatar)
                                .submit()
                                .get();

                    } catch (Exception e){

                        e.printStackTrace();

                    }

                    /*
                    * this call for getting user rating changes
                    * */
                    userRatingAPI = retrofit.create(iUserRatingAPI.class);
                    Call<UserRatingModel> call1 = userRatingAPI.getUserRatingChanges(_handle);
                    try {
                        response2 = call1.execute();

                        if(response2 != null && response2.body().getResult().size() >= 1){
                            UserRatingModel.Result reslt = response2.body().getResult()
                                    .get(response2.body().getResult().size()-1);
                            ratingChange = reslt.getNewRating()-reslt.getOldRating();
                            

                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //TODO : edit user
                    cfUsers.add( new User(
                            _handle, rating, maxRating, rank, maxRank, image,ratingChange));


                }
                Log.d(TAG, "doInBackground: Succeed");
            } catch (Exception E){
                E.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            super.onPostExecute(aVoid);

            Log.d(TAG, "onPostExecute: Executing commands + cfusers.size() = " + cfUsers.size());

            for(int i = 0; i < cfUsers.size(); ++i){

                if(!areEquals(allUsers.get(i), cfUsers.get(i))){

                    Log.d(TAG, "onPostExecute: there's some difference in data ");

                    User user = cfUsers.get(i);
                    user.setId(allUsers.get(i).getId());
                    userViewModel.update(user);
                }
            }

            refreshLayout.setRefreshing(false);
        }


    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        // this is important to show the menu icon on the current fragment
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView: creating....");
        return inflater.inflate(R.layout.fragment_user, container, false);
    }

    @Override
    public void sendInput(String handle) { // this comes from the add user dialog
        Log.d(TAG, "sendInput: adding new user with handle = " + handle);

        addNewUser(handle);
    }


    private void addNewUser(String handle) {
        new AddUserAsyncTask().execute(handle);
    }



    private class AddUserAsyncTask extends AsyncTask<String, Void, Void>{
        private Response<UserModel> response1;
        private Response<UserRatingModel> response2;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            refreshLayout.setRefreshing(true);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            UserModel result = response1.body();
            if (result == null) {
                Toast.makeText(getContext(), "Enter a valid handle", Toast.LENGTH_SHORT).show();
                refreshLayout.setRefreshing(false);
                return ;
            }
            if(response1.body() == null || response2.body() == null)return ;
            String _handle = result.getResult().get(0).getHandle();
            int rating = result.getResult().get(0).getRating();
            int maxRating = result.getResult().get(0).getMaxRating();
            String rank = result.getResult().get(0).getRank();
            String maxRank = result.getResult().get(0).getMaxRank();
            String avatar = result.getResult().get(0).getAvatar();
            byte[] image = null;

            try {

                image = new DownloadImageAsyncTask().execute(avatar).get();

            } catch (Exception e){

                e.printStackTrace();

            }

            // TODO : edit user
            User user = new User(
                    _handle, rating, maxRating, rank, maxRank, image,0);

            int hasUser = userViewModel.hasUser(_handle);
            Log.d(TAG, "sendInput - HasUser : " + _handle + " value = " + hasUser);
            if (hasUser == 1) {
                Toast.makeText(getContext(), "This user already exists", Toast.LENGTH_SHORT).show();
                //  userViewModel.update(user);
                refreshLayout.setRefreshing(false);
                return;
            }

            userViewModel.insert(user);
            refreshLayout.setRefreshing(false);
            Toast.makeText(getContext(), "User Added", Toast.LENGTH_SHORT).show();


            // this is for rating change
            UserRatingModel result1 = response2.body();
            if(result1 == null || result1.getResult().size() == 0) {
                refreshLayout.setRefreshing(false);
                return;
            }

            UserRatingModel.Result r = result1.getResult().get(result1.getResult().size()-1);
            int lst_rating = r.getOldRating();
            int new_rating = r.getNewRating();
            int current_change = new_rating - lst_rating;

            user = userViewModel.getUserByHandle(_handle);
            if(user != null){

                user.setUserRatingChange(current_change);
                Log.d(TAG, "onResponse: UserID = " + user.getId());
                userViewModel.update(user);
            }
            refreshLayout.setRefreshing(false);
        }

        @Override
        protected Void doInBackground(String... handles) {

            String handle = handles[0];

            userAPI = retrofit.create(iUserAPI.class);
            Call<UserModel> call = userAPI.getUserInfo(handle);
            try {
                response1 =  call.execute();


            } catch (IOException e) {
                e.printStackTrace();
            }

            userRatingAPI = retrofit.create(iUserRatingAPI.class);
            Call<UserRatingModel> call1 = userRatingAPI.getUserRatingChanges(handle);
            try {
               response2 = call1.execute();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private void setupRetrofit() {
        retrofit = new Retrofit
                .Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


    }

    private class DownloadImageAsyncTask extends AsyncTask<String, Void, byte[]> {
        @Override
        protected byte[] doInBackground(String... strings) {
            byte[] image = null;
            try {
                Log.d(TAG, "doInBackground: URLLLLL " + strings[0]);
                image = Glide.with(UserFragment.this)
                        .as(byte[].class)
                        .load("https://" + strings[0])
                        .submit()
                        .get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return image;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        adapter = null;
    }


}
