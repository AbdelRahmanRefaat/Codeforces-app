package com.example.codeforces.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.codeforces.R;
import com.example.codeforces.pojo.Problem;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


public class ProblemListAdapterZbi extends RecyclerView.Adapter<ProblemListAdapterZbi.ProblemViewHolder> {

    List<Problem> problemsList = new ArrayList<>();

    @NonNull
    @Override
    public ProblemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProblemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_problems_listitem, parent, false));
    }


    // right the changes that will happen to your recycler view item e.g(TV,IMG,etc)
    @Override
    public void onBindViewHolder(@NonNull ProblemViewHolder holder, int position) {
        Problem problem = problemsList.get(position);
        if(problem != null) {
            holder.problemRating_TV.setText(String.valueOf(problem.getRating()));
            holder.problemName_TV.setText(String.valueOf(problem.getContestId()) + problem.getIndex() + ". " + problem.getName());
            holder.contestName_TV.setText(String.valueOf(problem.getSolvedCount()));
        }

    }

    @Override
    public int getItemCount() {
        return problemsList.size();
    }

    public void setUsersList(List<Problem> usersList) {
        this.problemsList = usersList;
        notifyDataSetChanged();
    }

    public class ProblemViewHolder extends RecyclerView.ViewHolder {
        TextView problemName_TV;
        TextView contestName_TV;
        TextView problemRating_TV;
        public ProblemViewHolder(@NonNull View itemView) {
            super(itemView);
            problemName_TV = itemView.findViewById(R.id.problem_name_tv);
            contestName_TV = itemView.findViewById(R.id.contest_name_tv);
            problemRating_TV = itemView.findViewById(R.id.problem_rating_tv);


        }
    }
}
