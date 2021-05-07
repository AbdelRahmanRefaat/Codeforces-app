package com.example.codeforces.ui;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.SearchView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.codeforces.Adapters.ProblemListAdapter;
import com.example.codeforces.Adapters.TagsListAdapter;
import com.example.codeforces.Models.ProblemModel;
import com.example.codeforces.PaginationScrollListener;
import com.example.codeforces.Paginator;
import com.example.codeforces.R;
import com.example.codeforces.ViewModels.ProblemViewModel;
import com.example.codeforces.interfaces.FABProblemsControllerListener;
import com.example.codeforces.interfaces.OnDataFetchingDoneListener;
import com.example.codeforces.interfaces.iProblemAPI;
import com.example.codeforces.pojo.Problem;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProblemsFragment extends Fragment implements ProblemListAdapter.OnProblemClickedListener,
      AdapterView.OnItemSelectedListener, FABProblemsControllerListener {

    private static final String TAG = "ProblemsFragment";
    public static final String FRAGMENT_TAG = "Problems";
    public static final String BASE_URL = "https://codeforces.com/api/";


    private LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());;
    private Retrofit retrofit;
    private iProblemAPI problemAPI;

    private ProblemViewModel problemViewModel;
    private RecyclerView recyclerView_problems;
    private ProblemListAdapter adapter;
    private Observer<List<Problem>> observer;

    // TODO : remove this shitt
    private int currentPage = PaginationScrollListener.PAGE_START;
    private boolean isLastPage = false;
    private int totalPage = 1;
    private boolean isLoading = false;
    int itemCount = 0;

    // todo : THIS IS FOR SEARCH VIEW, REMOVE IT LATER
    private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;

    //SwipeRefreshLayout
    private SwipeRefreshLayout refreshLayout;


    // tags spinner
    private Spinner tagsSpinner;


    // tags list view
    private List<String> tagsList = new ArrayList<>();
    private RecyclerView tagsRecyclerView;
    private TagsListAdapter tagsListAdapter = new TagsListAdapter();
    private EditText start_rating_editText;
    private EditText end_rating_editText;

    // problems & tags frames
    private FrameLayout problemsFrame;
    private FrameLayout tagsFrame;

    private Button goSearchButton;

    private List<Problem> allProblems = new ArrayList<>();

    // page Size
    private static final int PAGE_SIZE = 20;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // tags spinner
        tagsSpinner = (getActivity()).findViewById(R.id.spinner_tags);
        ArrayAdapter<CharSequence> tagsSpinnerAdapter
                = ArrayAdapter.createFromResource(getContext(), R.array.tags, android.R.layout.simple_spinner_item);
        tagsSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tagsSpinner.setAdapter(tagsSpinnerAdapter);
        tagsSpinner.setOnItemSelectedListener(this);




        // rating range edit text
        start_rating_editText = (getActivity()).findViewById(R.id.edit_text_start_rating);
        end_rating_editText = (getActivity()).findViewById(R.id.edit_text_end_rating);

        // tags List View
        tagsRecyclerView = getActivity().findViewById(R.id.recycler_view_tags);
        tagsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        tagsRecyclerView.setAdapter(tagsListAdapter);

        // problems &  tags frame
        problemsFrame = getActivity().findViewById(R.id.frame_problemsTab_problems);
        tagsFrame = getActivity().findViewById(R.id.frame_problemsTab_tags);

        // fab controller listener
        ((MainActivity) getActivity()).setProblemsFABListener(this);

        // recycler view initialization
        recyclerView_problems = ((MainActivity) getActivity()).findViewById(R.id.recycler_view_problems);
        recyclerView_problems.setLayoutManager(layoutManager);
        recyclerView_problems.setHasFixedSize(true);
        adapter = new ProblemListAdapter(this);
        recyclerView_problems.setAdapter(adapter);



        recyclerView_problems.addOnScrollListener(new PaginationScrollListener(layoutManager) {
            @Override
            protected void loadMoreItems() {
                Log.d(TAG, "loadMoreItems: loading more items");
                isLoading = true;
                currentPage++;
                loadProblems();

            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });

        // refresh layout
        refreshLayout = ((MainActivity) getActivity()).findViewById(R.id.refresh_layout_problems);
        SwipeRefreshLayout.OnRefreshListener swipeRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i(TAG, "onRefresh called from SwipeRefreshLayout");
                clearRecyclerView();
                // This method performs the actual data-refresh operation.
                // The method calls setRefreshing(false) when it's finished.
                fetchProblemsData();

            }
        };
        refreshLayout.setOnRefreshListener(swipeRefreshListener);

        problemViewModel = new ViewModelProvider(this).get(ProblemViewModel.class);

       // loadMoreProblems();

        Log.d(TAG, "onViewCreated: ProblemTable Size = " + problemViewModel.getSize());


        // setting up retrofit for online requests
        setupRetrofit();


        refreshLayout.post(new Runnable() {
            @Override
            public void run() {

                swipeRefreshListener.onRefresh();
            }
        });




    }

    private void loadProblems() {

        Log.d(TAG, "loadProblems: Loading Problems now and the size of the existed problems");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                List<Problem> page = new ArrayList<>();
                int curPosition = adapter.getItemCount();
                int endPosition = curPosition + PAGE_SIZE;


                if(endPosition > allProblems.size()){
                    endPosition = allProblems.size();
                }
                Log.d(TAG, "loadMoreProblems: all problem size = " + allProblems.size() + " paganation prboelms = " + page.size());
                Log.d(TAG, "loadMoreProblems: loading more problems where current position = "
                        +curPosition +" and end position = " + endPosition);

                for(int i = curPosition; i < endPosition; ++i){
                    itemCount++;
                    page.add(allProblems.get(i));
                }
                Log.d(TAG, "loadMoreProblems: Current Page Size = " + page.size());


                if(currentPage != PaginationScrollListener.PAGE_START || allProblems.size() <= PAGE_SIZE )adapter.removeLoading();

                adapter.addItems(page);
                Log.d(TAG, "run: Curr Page = " + currentPage  + " Total Pages = " + totalPage);
                if(currentPage < totalPage){
                    adapter.addLoading();
                }else{
                    isLastPage = true;
//                    if(allProblems.size() <= PAGE_SIZE)
//                        adapter.removeLoading();
                }
                isLoading = false;
            }
        },1000);


    }

    private void fetchProblemsData() {
        Log.d(TAG, "fetchProblemsData: Fetching data from api");
        //new FetchProblemsDataAsyncTask(this).execute();
       // OnDataFetchingDoneListener mOnDataFetchingDoneListener;
        refreshLayout.setRefreshing(true);
        Call<ProblemModel> call = problemAPI.getAllProblems();
        call.enqueue(new Callback<ProblemModel>() {
            @Override
            public void onResponse(Call<ProblemModel> call, Response<ProblemModel> response) {
                List<ProblemModel.Result.P>  mAllProblems = response.body().getResult().getProblems();
                List<ProblemModel.Result.ProblemStatistics>  problemStatistics = response.body().getResult().getProblemStatistics();


                Log.d(TAG, "onDataFetchCompleted: Updating Problems List or Inserting all problems");

                List<Problem> problemsList = new ArrayList<>();

                for (int i = 0; i < mAllProblems.size(); ++i) {
                    String index = mAllProblems.get(i).getIndex();
                    String name = mAllProblems.get(i).getName();
                    int contestId = mAllProblems.get(i).getContestId();
                    int rating = mAllProblems.get(i).getRating();
                    int solvedCount = problemStatistics.get(i).getSolvedCount();
                    List<String> tags = mAllProblems.get(i).getTags();
                    Problem problem = new Problem(index, name, contestId, rating, solvedCount, tags);
                    problemsList.add(problem);
                }
                problemViewModel.insertAll(problemsList).subscribeOn(Schedulers.computation());
                allProblems = problemsList;
                totalPage = ((int)allProblems.size() + PAGE_SIZE - 1) / PAGE_SIZE;
                loadProblems();
                refreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<ProblemModel> call, Throwable t) {

                 problemViewModel.getAllProblems().subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<Problem>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(List<Problem> problems) {
                        Log.d(TAG, "onSuccess: Fetching data from Database is a Success " + problems.size());
                            allProblems = problems;
                            totalPage = ((int)allProblems.size() + PAGE_SIZE - 1) / PAGE_SIZE;
                            loadProblems();
                            refreshLayout.setRefreshing(false);

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: Failed to fetch data from database");
                        refreshLayout.setRefreshing(false);
                    }
                });


            }
        });


    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        String tag = adapterView.getItemAtPosition(position).toString();
        if (tag.equals("choose tags")) {
            return;
        }
        if (!tagsList.contains(tag)) {
            tagsList.add(tag);
            tagsListAdapter.setTagsList(tagsList);

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    @Override
    public void onProblemFABControllerClicked(boolean isClicked) {
        Log.d(TAG, "onFabClicked: Problems fab button is clicked " + isClicked);
        if (!isClicked) {
            problemsFrame.setVisibility(View.GONE);
            tagsFrame.setVisibility(View.VISIBLE);
            searchView.setVisibility(View.GONE);


        } else {
            tagsFrame.setVisibility(View.GONE);
            problemsFrame.setVisibility(View.VISIBLE);
            searchView.setVisibility(View.VISIBLE);

        }
    }




    @Override
    public void onProblemClicked(Problem problem) {
        // TODO : add your code here

        int contestId = problem.getContestId();
        String index = problem.getIndex();
        String name = problem.getName();

        Log.d(TAG, "onProblemClicked: item clicked : ContestId = " + contestId
                + ", Problem Index = " + index);

        Intent i = new Intent();
        Intent intent = new Intent(getContext(), WebViewActivity.class);
        String url = "https://codeforces.com/problemset/problem/" + contestId + "/" + index;
        intent.putExtra("url", url);
        intent.putExtra(WebViewActivity.EXTRA_REQUEST_CODE, WebViewActivity.PROBLEM_REQUEST_CODE);
        intent.putExtra(WebViewActivity.EXTRA_PROBLEM_NAME, name);
        intent.putExtra(WebViewActivity.EXTRA_PROBLEM_INDEX, index);
        intent.putExtra(WebViewActivity.EXTRA_PROBLEM_CONTEST_ID, contestId);
        startActivity(intent);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_problems, container, false);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    // for search bar
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.searchable_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);

        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }

        if (searchView != null) {
            assert searchManager != null;
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
            queryTextListener = new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    Log.d(TAG, "onQueryTextSubmit: " + s);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    if(s.equals("")){
                        clearRecyclerView();
                        fetchProblemsData();
                        return true;
                    }

                    String query = "%" + s + "%";
                    Log.d(TAG, "onQueryTextChange: " + query);

                    String start_rating_text = start_rating_editText.getText().toString();
                    String end_rating_text = end_rating_editText.getText().toString();

                    if (start_rating_text.equals(""))
                        start_rating_text = "0";

                    if (end_rating_text.equals(""))
                        end_rating_text = "3500";

                    int start_rating_value = Integer.parseInt(start_rating_text);
                    int end_rating_value = Integer.parseInt(end_rating_text);

                    clearRecyclerView();
                    SingleObserver<List<Problem>> observer = new SingleObserver<List<Problem>>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onSuccess(List<Problem> problems) {
                            clearRecyclerView();
                            allProblems = problems;
                            totalPage = (allProblems.size() + PAGE_SIZE - 1) / PAGE_SIZE;
                            Log.d(TAG, "onSuccess: Total Pages now = " + totalPage + " " + allProblems.size());
                            loadProblems();
                        }

                        @Override
                        public void onError(Throwable e) {

                        }
                    };

                    problemViewModel.getProblemsByQuery(start_rating_value,end_rating_value,query)
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(observer);



                    return true;
                }
            };
            searchView.setOnQueryTextListener(queryTextListener);

        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                // Not implemented here
                return true;
            default:
                break;
        }
        searchView.setOnQueryTextListener(queryTextListener);
        return super.onOptionsItemSelected(item);
    }

    private void setupRetrofit() {
        retrofit = new Retrofit
                .Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        problemAPI = retrofit.create(iProblemAPI.class);
    }
    private void clearRecyclerView(){
        itemCount = 0;
        currentPage = PaginationScrollListener.PAGE_START;
        isLastPage = false;
        totalPage = 1;
        adapter.clear();
    }
}
