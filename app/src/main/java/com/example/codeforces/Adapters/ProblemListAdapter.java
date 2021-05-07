package com.example.codeforces.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.codeforces.Models.ProblemModel;
import com.example.codeforces.R;
import com.example.codeforces.ViewHolders.ProblemBaseViewHolder;
import com.example.codeforces.pojo.Problem;

import java.util.ArrayList;
import java.util.List;


public class ProblemListAdapter extends RecyclerView.Adapter<ProblemBaseViewHolder> {

    private static final String TAG = "ProblemListAdapter";
    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_NORMAL = 1;
    boolean isLoadingVisible = false;

    private List<Problem> allProblems;


    public interface OnProblemClickedListener {
        void onProblemClicked(Problem problem);
    }

    OnProblemClickedListener mOnProblemClickedListener;

    public ProblemListAdapter(OnProblemClickedListener onProblemClickedListener) {
        allProblems = new ArrayList<>();
        this.mOnProblemClickedListener = onProblemClickedListener;
    }



    @NonNull
    @Override
    public ProblemBaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Log.d(TAG, "onCreateViewHolder: View Type Now = " + viewType);
        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new ProblemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_problems_listitem, parent, false));
            case VIEW_TYPE_LOADING:
                return new ProgressViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false));
            default:
                return null;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isLoadingVisible) {
            return position == allProblems.size() - 1 ? VIEW_TYPE_LOADING : VIEW_TYPE_NORMAL;
        } else {
            return VIEW_TYPE_NORMAL;
        }
    }

    @Override
    public int getItemCount() {
        return this.allProblems.size();
    }

    public void addLoading() {
        Log.d(TAG, "addLoading: ProblemSetSize = " + allProblems.size());
        isLoadingVisible = true;
        allProblems.add(new Problem());
        notifyItemInserted(allProblems.size() - 1);
    }

    public void removeLoading() {
        Log.d(TAG, "removeLoading: ProblemSetSize = " + allProblems.size());
        isLoadingVisible = false;
        int position = allProblems.size() - 1;
        Problem item = allProblems.get(position);
        Log.d(TAG, "removeLoading: Position = " + position + " item = " + item);
        if (item != null) {
            allProblems.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void addItems(List<Problem> allProblems) {
        int pos = allProblems.size();
        this.allProblems.addAll(allProblems);
        notifyDataSetChanged();
        //notifyItemRangeInserted(pos , 10);
    }

    public void clear(){
        allProblems.clear();
        notifyDataSetChanged();
    }


    @Override
    public void onBindViewHolder(@NonNull ProblemBaseViewHolder holder, int position) {
        holder.onBind(position);
    }

    public class ProblemViewHolder extends ProblemBaseViewHolder implements View.OnClickListener {
        TextView problemName_TV;
        TextView contestName_TV;
        TextView problemRating_TV;

        public ProblemViewHolder(@NonNull View itemView) {
            super(itemView);
            problemName_TV = itemView.findViewById(R.id.problem_name_tv);
            contestName_TV = itemView.findViewById(R.id.contest_name_tv);
            problemRating_TV = itemView.findViewById(R.id.problem_rating_tv);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onBind(int position) {
            super.onBind(position);
            Problem problem = allProblems.get(position);
            if (problem != null) {
                problemRating_TV.setText(String.valueOf(problem.getRating()));
                problemName_TV.setText(String.valueOf(problem.getContestId()) + problem.getIndex() + ". " + problem.getName());
                contestName_TV.setText(String.valueOf(problem.getSolvedCount()));
            }
        }

        @Override
        protected void clear() {

        }

        @Override
        public void onClick(View view) {
            mOnProblemClickedListener.onProblemClicked(allProblems.get(getAbsoluteAdapterPosition()));
        }
    }

    public class ProgressViewHolder extends ProblemBaseViewHolder {
        ProgressBar loadingProgress;

        public ProgressViewHolder(@NonNull View itemView) {
            super(itemView);
            loadingProgress = itemView.findViewById(R.id.progress_bar_loading);
        }

        @Override
        public void onBind(int position) {
            super.onBind(position);

        }

        @Override
        protected void clear() {

        }
    }
}
