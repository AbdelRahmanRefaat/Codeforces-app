package com.example.codeforces.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.codeforces.Adapters.ContestListAdapter;
import com.example.codeforces.AppUtils;
import com.example.codeforces.Models.ContestModel;
import com.example.codeforces.R;
import com.example.codeforces.ViewModels.ContestViewModel;
import com.example.codeforces.interfaces.FABContestControllerListener;
import com.example.codeforces.interfaces.iContestAPI;
import com.example.codeforces.pojo.Contest;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ContestsFragment extends Fragment implements ContestListAdapter.OnContestListener
        , FABContestControllerListener {

    private static final String TAG = "ContestsFragment";
    public static final String BASE_URL = "https://codeforces.com/api/";
    public static final String FRAGMENT_TAG = "Contests";

    private Retrofit retrofit;
    private iContestAPI contestAPI;

    private RecyclerView recyclerView;
    private ContestListAdapter adapter;



    private ToggleButton contestToggleButton;




    // TODO : Temp
    ContestViewModel contestViewModel;
    private Observer<PagedList<Contest>> observer;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // TODO : Remove this shit
        ((MainActivity) getActivity()).setContestsFABListener(this);

        recyclerView = getActivity().findViewById(R.id.recycler_view_contests);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ContestListAdapter(getContext(),this);
        recyclerView.setAdapter(adapter);

        setupRetrofit();


        observer = new Observer<PagedList<Contest>>() {
            @Override
            public void onChanged(PagedList<Contest> contests) {
                adapter.submitList(contests);
            }
        };

        long currentTime = System.currentTimeMillis()/1000;
        contestViewModel = new ViewModelProvider(this).get(ContestViewModel.class);
        contestViewModel.getAllUpComingContests(currentTime).observe(getViewLifecycleOwner(),observer);



        fetchContestData();


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contests, container, false);
    }

    private void fetchContestData() {
        new FetchContestDataAsyncTask().execute();
    }



    private class FetchContestDataAsyncTask extends AsyncTask<Void, Void,Void>{
        private List<Contest> allContests = new ArrayList<>();
        @Override
        protected Void doInBackground(Void... voids) {
            Response<ContestModel> response;
            Call<ContestModel> call = contestAPI.getContestsList(false);
            try {
                response = call.execute();
                ContestModel result = response.body();
                for(ContestModel.Result res: result.getResult()){
                    Contest contest = new Contest(res.getId(),res.getName(),res.getType(),
                            res.getPhase(),res.getDurationSeconds(),res.getStartTimeSeconds());

                    allContests.add(contest);
                }
            } catch (Exception e) {

                e.printStackTrace();
            }

            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.d(TAG, "onPostExecute: all Contests List Size = " + allContests.size());
            contestViewModel.insertAll(allContests);
        }
    }

    private void setupRetrofit() {
        retrofit = new Retrofit
                .Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        contestAPI = retrofit.create(iContestAPI.class);
    }

    @Override
    public void onUserClick(Contest contest) {

        int contestId = contest.getId();
        String contestName = contest.getName();
        Log.d(TAG, "onUserClick: Clicked Contest ID = " + contestId);

        String url = "https://codeforces.com/contest/" + contestId;

        Intent intent = new Intent(getContext(), WebViewActivity.class);

        intent.putExtra("url", url);
        intent.putExtra(WebViewActivity.EXTRA_CONTEST_NAME, contestName);
        intent.putExtra(WebViewActivity.EXTRA_REQUEST_CODE,WebViewActivity.CONTEST_REQUEST_CODE);

        startActivity(intent);
    }


    @Override
    public void onContestsFABControllerClicked(boolean isClicked) {

        Log.d(TAG, "onFabClicked: Contests fab button is clicked " + isClicked);
        long currentTime = System.currentTimeMillis()/1000;
        if (!isClicked) { // show the previous contests

            Log.d(TAG, "onContestFabClicked: Fetching Previous Contests");
            contestViewModel.getAllPastContests(currentTime).observe(getViewLifecycleOwner(),observer);

        } else { // show the up coming contests

            Log.d(TAG, "onContestFabClicked: Fetching up coming Contests");
            contestViewModel.getAllUpComingContests(currentTime).observe(getViewLifecycleOwner(),observer);
        }
    }
}
