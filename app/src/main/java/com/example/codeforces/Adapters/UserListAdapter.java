package com.example.codeforces.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.codeforces.AppUtils;
import com.example.codeforces.R;
import com.example.codeforces.pojo.User;

import de.hdodenhof.circleimageview.CircleImageView;


public class UserListAdapter extends ListAdapter<User,UserListAdapter.UserViewHolder> {

    private static final String TAG = "UserListAdapter";
    private OnUserListener mOnUserListener;
    private Context mContext;
    public UserListAdapter(OnUserListener onUserListener,Context context){
        super(DIFF_CALLBACK);
        this.mOnUserListener = onUserListener;
        this.mContext = context;
    }
    private static DiffUtil.ItemCallback<User> DIFF_CALLBACK = new DiffUtil.ItemCallback<User>() {
        @Override
        public boolean areItemsTheSame(@NonNull User oldItem, @NonNull User newItem) {
            return oldItem.getHandle().equals(newItem.getHandle());
        }

        @Override
        public boolean areContentsTheSame(@NonNull User oldItem, @NonNull User newItem) {
            return oldItem.getHandle().equals(newItem.getHandle());
        }
    };

    @NonNull
    @Override
    public UserListAdapter.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_users_listitem,parent,false);
        return new UserViewHolder(view,mOnUserListener);

    }


    // right the changes that will happen to your recycler view item e.g(TV,IMG,etc)
    @Override
    public void onBindViewHolder(@NonNull UserListAdapter.UserViewHolder holder, int position) {
            User user = getItem(position);
            int rating = user.getRating();
            if(user.getUserRatingChange() > 0){
                holder.userRatingChangeTV.setTextColor(Color.parseColor("#1a8212"));
            }else if(user.getUserRatingChange() == 0){
                holder.userRatingChangeTV.setTextColor(Color.parseColor("#000000"));
            }else{
                holder.userRatingChangeTV.setTextColor(Color.parseColor("#eb4334"));
            }

            int colorRating = AppUtils.getRatingColor(rating);
            holder.userRatingTV.setTextColor(colorRating);
            holder.userNameTV.setTextColor(colorRating);

            boolean up = (user.getUserRatingChange() > 0);
            holder.userNameTV.setText(user.getHandle());
            holder.userRatingTV.setText(String.valueOf(user.getRating()));
            holder.userRatingChangeTV.setText((up ? "+" : "") + String.valueOf(user.getUserRatingChange()));
            Bitmap bmp = null;
            if(user.getImage() != null)
            bmp = BitmapFactory.decodeByteArray(user.getImage(),0,
                    user.getImage().length);
            holder.userIMGV.setImageBitmap(bmp);

            holder.parentLayout.setAnimation(AnimationUtils.loadAnimation(mContext,R.anim.animation_fade_swipe_right));
//            Glide.with(mContext)
//                    .asBitmap()
//                    .load(usersList.get(position).getImage())
//                    .into(holder.userIMGV);
    }


    public User getUserAt(int position){
        return getItem(position);
    }

    public class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView userNameTV;
        CircleImageView userIMGV;
        View parentLayout;
        TextView userRatingTV;
        TextView userRatingChangeTV;

        OnUserListener onUserListener;
        public UserViewHolder(@NonNull View itemView, OnUserListener onUserListener) {
            super(itemView);
            userNameTV = itemView.findViewById(R.id.user_name);
            userIMGV = itemView.findViewById(R.id.user_img);
            userRatingTV = itemView.findViewById(R.id.user_rating);
            userRatingChangeTV = itemView.findViewById(R.id.user_rating_change);
            parentLayout = itemView.findViewById(R.id.parent_layout_users);
            this.onUserListener = onUserListener;
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            onUserListener.onUserClick(getItem(getAdapterPosition()));
        }
    }
    public interface OnUserListener{
        void onUserClick(User user);
    }

}
