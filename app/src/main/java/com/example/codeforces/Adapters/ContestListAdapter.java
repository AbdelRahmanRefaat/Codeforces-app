package com.example.codeforces.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.codeforces.AppUtils;
import com.example.codeforces.R;
import com.example.codeforces.pojo.Contest;

import de.hdodenhof.circleimageview.CircleImageView;


public class ContestListAdapter extends PagedListAdapter<Contest,ContestListAdapter.ContestViewHolder> {

    private Context mContext;
    private OnContestListener mOnContestListener;

    public ContestListAdapter(Context context,OnContestListener onContestListener) {
        super(DIFF_CALLBACK);
        mOnContestListener = onContestListener;
        this.mContext = context;
    }

    private static DiffUtil.ItemCallback<Contest> DIFF_CALLBACK = new DiffUtil.ItemCallback<Contest>() {
        @Override
        public boolean areItemsTheSame(@NonNull Contest oldItem, @NonNull Contest newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Contest oldItem, @NonNull Contest newItem) {
            return oldItem.getId() == newItem.getId();
        }
    };

    @NonNull
    @Override
    public ContestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_contests_listitem,parent , false);
        return new ContestViewHolder(view , mOnContestListener);
    }


    // right the changes that will happen to your recycler view item e.g(TV,IMG,etc)
    @Override
    public void onBindViewHolder(@NonNull ContestViewHolder holder, int position) {


            holder.name.setText(getItem(position).getName());
            holder.type.setText("Type: " + getItem(position).getType());
            holder.duration.setText("Duration: " + AppUtils.secondToHours(getItem(position).getDurationSeconds()));
            holder.startTime.setText("Start Time: " + AppUtils.unixToDateHM(getItem(position).getStartTimeSeconds()));
            holder.phase.setText(getItem(position).getPhase());
            if((position & 1) == 0) {
                holder.parentLayout.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.animation_fade_swipe_right));
            }else{
                holder.parentLayout.setAnimation(AnimationUtils.loadAnimation(mContext,R.anim.animation_fade_swipe_left));
            }
    }

    public class ContestViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private CircleImageView cf_icon;
        private TextView name;
        private TextView type;
        private TextView duration;
        private TextView phase;
        private TextView startTime;
        private View parentLayout;

        private OnContestListener onContestListener;

        public ContestViewHolder(@NonNull View itemView, OnContestListener onContestListener) {
            super(itemView);
            parentLayout = itemView.findViewById(R.id.parent_layout_contests);
            cf_icon = itemView.findViewById(R.id.judge_icon);
            name = itemView.findViewById(R.id.contest_nameTV);
            type = itemView.findViewById(R.id.contest_typeTV);
            duration = itemView.findViewById(R.id.contest_durationTV);
            startTime = itemView.findViewById(R.id.contest_start_time_TV);
            phase = itemView.findViewById(R.id.contest_phaseTV);
            this.onContestListener = onContestListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onContestListener.onUserClick(getItem(getAdapterPosition()));
        }
    }
    public interface OnContestListener{
        void onUserClick(Contest contest);
    }
}
