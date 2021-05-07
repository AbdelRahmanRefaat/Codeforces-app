package com.example.codeforces.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.codeforces.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


public class TagsListAdapter extends RecyclerView.Adapter<TagsListAdapter.TagViewHolder> {

    List<String> tagsList = new ArrayList<>();

    @NonNull
    @Override
    public TagViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TagViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_tags_listitem, parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull TagViewHolder holder, int position) {
        holder.tagNameTV.setText(tagsList.get(position));
        holder.removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                        tagsList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position,tagsList.size());
            }
        });
    }

    @Override
    public int getItemCount() {
        return tagsList.size();
    }

    public void setTagsList(List<String> usersList) {
        this.tagsList = usersList;
        notifyDataSetChanged();
    }

    public class TagViewHolder extends RecyclerView.ViewHolder {
        TextView tagNameTV;
        Button removeButton;
        public TagViewHolder(@NonNull View itemView) {
            super(itemView);
            tagNameTV = itemView.findViewById(R.id.tag_name_tv);
            removeButton = itemView.findViewById(R.id.button_remove_tag);

        }
    }
}
