package com.example.studente.restapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.studente.restapp.ItemDetailActivity;
import com.example.studente.restapp.ItemDetailFragment;
import com.example.studente.restapp.Post;
import com.example.studente.restapp.R;

import java.util.List;

public class PostRecyclerViewAdapter
        extends RecyclerView.Adapter<PostRecyclerViewAdapter.ViewHolder> {

    private final FragmentActivity mParentActivity;
    private final List<Post> mValues;
    private final boolean mTwoPane;
    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Post post = (Post) view.getTag();
            if (mTwoPane) {
                Bundle arguments = new Bundle();
                arguments.putParcelable(ItemDetailFragment.ARG_ITEM, post);
                ItemDetailFragment fragment = new ItemDetailFragment();
                fragment.setArguments(arguments);
                mParentActivity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.item_detail_container, fragment)
                        .commit();
            } else {
                Context context = view.getContext();
                Intent intent = new Intent(context, ItemDetailActivity.class);
                intent.putExtra(ItemDetailFragment.ARG_ITEM, post);

                context.startActivity(intent);
            }
        }
    };

    public PostRecyclerViewAdapter(FragmentActivity parent,
                                         List<Post> items,
                                         boolean twoPane) {
        mValues = items;
        mParentActivity = parent;
        mTwoPane = twoPane;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_content, parent, false);
        return new ViewHolder(layout);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Post item  = mValues.get(position);
        holder.mIdView.setText(Integer.toString(item.getPostId()));
        holder.mContentView.setText(item.getTitle());
        showFavouriteIcon(item, holder.mFavImage);

        holder.itemView.setTag(mValues.get(position));
        holder.itemView.setOnClickListener(mOnClickListener);
    }

    @Override
    public int getItemCount() {return mValues.size();}

    private void showFavouriteIcon(Post item, ImageView icon){
        int res = item.isFavourite() ? R.drawable.ic_favorite_full_24dp : R.drawable.ic_favorite_empty_24dp;
        icon.setImageResource(res);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView mIdView;
        final TextView mContentView;
        final ImageView mFavImage;

        ViewHolder(View view) {
            super(view);
            mIdView = (TextView) view.findViewById(R.id.id_text);
            mContentView = (TextView) view.findViewById(R.id.content);
            mFavImage = (ImageView) view.findViewById(R.id.fav_image);
            mFavImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View icon) {
                    Post item = (Post) itemView.getTag();
                    item.setFavourite(!item.isFavourite());
                    showFavouriteIcon(item, mFavImage);
                }
            });
        }
    }
}