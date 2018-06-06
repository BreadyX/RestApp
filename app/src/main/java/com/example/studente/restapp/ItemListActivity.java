package com.example.studente.restapp;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;


import com.example.studente.restapp.Database.PostDao;
import com.example.studente.restapp.Database.PostDatabase;
import com.example.studente.restapp.adapter.PostRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a  representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class ItemListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mIsTablet;
    private PostRecyclerViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mIsTablet = true;
        }

        DummyPostContent dummyContent = new DummyPostContent();
        dummyContent.loadDummyPostIntoDatabase();

        View recyclerView = findViewById(R.id.item_list);
        setupRecyclerView((RecyclerView) recyclerView);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        mAdapter = new PostRecyclerViewAdapter(this, ITEMS, mIsTablet);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mAdapter.notifyDataSetChanged();
    }

    public  final List<Post> ITEMS = new ArrayList<Post>();

    private class DummyPostContent {

        private final int COUNT = 25;
        private PostDatabase db;

        public DummyPostContent() {
            this.db = Room.databaseBuilder(ItemListActivity.this, PostDatabase.class, PostDatabase.DATABASE_NAME).build();
        }

        public void loadDummyPostIntoDatabase() {
            LoadDatabase loadDatabase = new LoadDatabase();
            loadDatabase.execute();
        }

        private List<Post> addItemsToDatabase() {
            PostDao dao = db.postDao();
            Post[] postArray = new Post[COUNT];
            for (int i=0; i<COUNT; i++) {
                Post post = createPost(i);
                postArray[i] = post;
            }
            dao.insertAll(postArray);
            return Arrays.asList(postArray);
        }

        private void addItem(Post item) {
            ITEMS.add(item);
        }

        private Post createPost(int position) {
            return new Post(0, position, "Title " + position, "Body " + position, false);
        }

        private String makeDetails(int position) {
            StringBuilder builder = new StringBuilder();
            builder.append("Details about Item: ").append(position);
            for (int i = 0; i < position; i++) {
                builder.append("\nMore details information here.");
            }
            return builder.toString();
        }

        private class LoadDatabase extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                PostDao dao = db.postDao();
                List<Post> postList = dao.getAll();

                if (postList == null || postList.size() == 0) {
                    postList = addItemsToDatabase();
                }

                ITEMS.addAll(postList);
                mAdapter.notifyDataSetChanged();

                return null;
            }
        }

    }
}