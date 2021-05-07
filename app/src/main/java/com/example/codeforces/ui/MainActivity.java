package com.example.codeforces.ui;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.codeforces.AppUtils;
import com.example.codeforces.R;
import com.example.codeforces.interfaces.FABContestControllerListener;
import com.example.codeforces.interfaces.FABProblemsControllerListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener
{

    private static final String TAG = "MainActivity";

    public static final int FRAGMENT_ID_USERS = R.id.navigation_users;
    public static final int FRAGMENT_ID_CONTESTS = R.id.navigation_contests;
    public static final int FRAGMENT_ID_ACTIONS = R.id.navigation_actions;
    public static final int FRAGMENT_ID_PROBLEMS = R.id.navigation_problems;

    private MutableLiveData<Fragment> mutableLiveData = new MutableLiveData<>();
    private String fragmentTag = null;
    private AddUserDialog dialog;


    // widgets
    private FloatingActionButton fab;
    private Fragment activeFragment;
    private UserFragment mUserFragment;
    private ContestsFragment mContestsFragment;
    private ProblemsFragment mProblemsFragment;

    private boolean fab_contests_isCLicked = false;
    private boolean fab_problems_isClicked = false;


    ///// TODO : remove this shit
    FABContestControllerListener mFABContestControllerListener;
    public void setContestsFABListener(FABContestControllerListener mFABContestControllerListener){
        this.mFABContestControllerListener = mFABContestControllerListener;
    }

    FABProblemsControllerListener mFabProblemsControllerListener;
    public void setProblemsFABListener(FABProblemsControllerListener mProblemsFABListener){
        this.mFabProblemsControllerListener = mProblemsFABListener;
    }

    public void updateProblemsFabState(){
        fab_problems_isClicked = !fab_problems_isClicked;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // init fragments
        mUserFragment = new UserFragment();
        mContestsFragment = new ContestsFragment();
        mProblemsFragment = new ProblemsFragment();
        activeFragment = mUserFragment;


        //loading the default fragment
        LoadFragment(mUserFragment);


        //getting bottom navigation view and attaching the listener
        BottomNavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
        navigationView.setOnNavigationItemSelectedListener(this);

        // Floating Action Bar
        fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: fab is clicked");

                String tag = activeFragment.getView().getTag().toString();

                switch (tag){
                    case UserFragment.FRAGMENT_TAG:
                        fab_contests_isCLicked = false;
                        // add user dialog
                        ((UserFragment) activeFragment).showAddUserDialog();
                        break;
                    case ContestsFragment.FRAGMENT_TAG:
                        if(!fab_contests_isCLicked) {
                            setTitle("Past Contests");
                            fab.setImageDrawable(getDrawable(R.drawable.ic_upcoming));
                        }
                        else {

                            setTitle("Upcoming Contests");
                            fab.setImageDrawable(getDrawable(R.drawable.ic_prev4));
                        }
                        mFABContestControllerListener.onContestsFABControllerClicked(fab_contests_isCLicked);
                        fab_contests_isCLicked = !fab_contests_isCLicked;
                        break;

                    case ProblemsFragment.FRAGMENT_TAG:
                        setTitle("Problems");
                        mFabProblemsControllerListener.onProblemFABControllerClicked(fab_problems_isClicked);
                        fab_problems_isClicked = !fab_problems_isClicked;
                        break;

                    case "Actions":

                        break;
                }
            }
        });


        mutableLiveData.observe(this, new Observer<Fragment>() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onChanged(Fragment fragment) {
               // activeFragment = fragment;

                fragmentTag = activeFragment.getView().getTag().toString();
                Log.d(TAG, "onChanged: We have a user fragment right now..." + fragmentTag);
                switch (fragmentTag){
                    case "Users":
                        setTitle("Users");
                        fab.setImageDrawable(getDrawable(R.drawable.ic_add));

                        break;
                    case "Contests":

                        if(!fab_contests_isCLicked) {

                            setTitle("Upcoming Contests");
                            fab.setImageDrawable(getDrawable(R.drawable.ic_prev4));

                        }else {

                            setTitle("Past Contests");
                            fab.setImageDrawable(getDrawable(R.drawable.ic_upcoming));
                        }
                        break;

                    case "Problems":
                        setTitle("Problems");
                        fab.setImageDrawable(getDrawable(R.drawable.ic_search));
                        break;

                    case "Actions":
                        fab.setImageDrawable(null);
                        break;
                }

            }
        });
    }
    private boolean LoadFragment(Fragment fragment){

        Log.i(TAG, "LoadFragment: Loading fragments, size = " + getSupportFragmentManager().getFragments().size());
        for(int i = 0; i < getSupportFragmentManager().getFragments().size(); ++i){

            if(getSupportFragmentManager().getFragments().get(i) != null  && getSupportFragmentManager().getFragments().get(i) == activeFragment) {
                mutableLiveData.postValue(fragment);
                return true;
            }
        }


        if(fragment != null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.nav_host_fragment,fragment)
                    .commit();

            mutableLiveData.postValue(fragment);
            return true;
        }
        return false;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){

            case  R.id.navigation_contests:
                Log.d(TAG, "onOptionsItemSelected: Contests Fragment Selected");
                return true;
            case  R.id.navigation_problems:
                Log.d(TAG, "onOptionsItemSelected: Problems Fragment Selected");
                return true;
            case R.id.navigation_users:
                Log.d(TAG, "onOptionsItemSelected: Users Fragment Selected");
        }
        return false;
    }

    // the return type is for highlighting the next button or not
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {


        FragmentManager fragmentManager = getSupportFragmentManager();
        switch (item.getItemId()){
            case R.id.navigation_users:
                fragmentManager.beginTransaction().hide(activeFragment).show(mUserFragment).commit();
                activeFragment = mUserFragment;
                LoadFragment(activeFragment);
                return true;

            case R.id.navigation_contests:
                fragmentManager.beginTransaction().hide(activeFragment).show(mContestsFragment).commit();
                activeFragment = mContestsFragment;
                LoadFragment(activeFragment);
                return true;

            case R.id.navigation_problems:
                fragmentManager.beginTransaction().hide(activeFragment).show(mProblemsFragment).commit();
                activeFragment = mProblemsFragment;
                LoadFragment(activeFragment);
                return true;
            default:
                return false;
        }
    }


}
