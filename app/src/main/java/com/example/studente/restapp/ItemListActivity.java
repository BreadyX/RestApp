package com.example.studente.restapp;

import android.arch.persistence.room.Room;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;


import com.example.studente.restapp.database.PostDao;
import com.example.studente.restapp.database.PostDatabase;
import com.example.studente.restapp.adapter.PostRecyclerViewAdapter;
import com.example.studente.restapp.service.PostService;

import java.util.ArrayList;
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
    public static final String TAG = ItemListActivity.class.getName();
    private boolean mIsTablet;
    private PostRecyclerViewAdapter mAdapter;
    public  final List<Post> ITEMS = new ArrayList<Post>();
    private BroadcastReceiver mReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent){
            loadPostFromDatabase();
        }
    };
    public static final String ACTION_DATA_SET_CHANGED = "com.example.studente.restapp.POST_SET_CHANGED";

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

        View recyclerView = findViewById(R.id.item_list);
        setupRecyclerView((RecyclerView) recyclerView);

        loadPostFromDatabase();

        Intent downloadPostIntent = new Intent(this, PostService.class);
        startService(downloadPostIntent);
    }

    @Override
    protected void onStop() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
        super.onStop();
    }

    @Override
    protected void onStart() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_DATA_SET_CHANGED);
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, filter);
        super.onStart();
    }

    public void loadPostFromDatabase() {
        ReadDatabase loadDatabase = new ReadDatabase(this);
        loadDatabase.execute();
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

    private void notifyDataSetChanged(List<Post> items){
        ITEMS.addAll(items);
        mAdapter.notifyDataSetChanged();
    }

    private class ReadDatabase extends AsyncTask<Void, Void, List<Post>> {

        private PostDatabase db;
        ReadDatabase(Context context) {
            this.db = Room.databaseBuilder(context, PostDatabase.class, PostDatabase.DATABASE_NAME).build();
        }

        @Override
        protected List<Post> doInBackground(Void... voids) {
            PostDao dao = db.postDao();
            List<Post> postList = dao.getAll();
            return postList;
        }
        @Override
        public void onPostExecute(List<Post> postList){
            notifyDataSetChanged(postList);
        }
    }
}