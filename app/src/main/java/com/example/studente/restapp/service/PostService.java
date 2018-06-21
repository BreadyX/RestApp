package com.example.studente.restapp.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.studente.restapp.ItemListActivity;
import com.example.studente.restapp.Post;
import com.example.studente.restapp.R;
import com.example.studente.restapp.database.PostDatabase;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PostService extends Service {

    private static final String TAG = PostService.class.getName();
    private NotificationManager mNotificationManager;
    private int NOTIFICATION = R.string.app_name;
    private Retrofit mRetroFit;
    private PostDatabase postDb;
    private JsonPlaceHolderCalls mCalls;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate(){

        mRetroFit = new Retrofit.Builder()
                .baseUrl("http://jsonplaceholder.typicode.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        postDb = Room.databaseBuilder(PostService.this, PostDatabase.class, PostDatabase.DATABASE_NAME).build();
        mNotificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        mCalls = mRetroFit.create(JsonPlaceHolderCalls.class);
    }

    public int OnStartCommand(Intent intent, int flags, int startId){
        JsonPlaceHolderLoader loader = new JsonPlaceHolderLoader();
        loader.execute(JsonPlaceHolderLoader.POST_LIST);
        return START_NOT_STICKY;
    }

    private void showNotification() {
        CharSequence text = getText(R.string.post_downloaded);
        Intent futureIntent = new Intent(this, ItemListActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(
                this, 0, futureIntent, 0);
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_room_service_black_24dp)
                .setTicker(text)
                .setWhen(System.currentTimeMillis())
                .setContentTitle(getText(R.string.app_name))
                .setContentText(text)
                .setContentIntent(contentIntent)
                .build();
        mNotificationManager.notify(NOTIFICATION, notification);
    }

    private class JsonPlaceHolderLoader extends AsyncTask<Integer, Void, Boolean>{
        public static final int POST_LIST = 1;
        public static final int COMMENT_LIST = 2;
        public static final int ALBUM_LIST = 3;

        @Override
        protected Boolean doInBackground(Integer... code) {
            if (code[0] < 4 && code[0] > 0){

                switch(code[0]){
                    case POST_LIST:
                        Call<List<Post>> call = mCalls.getPosts();
                        try {
                            Response<List<Post>> response = call.execute();
                            List<Post> posts = response.body();
                            for(Post post: posts){
                                Post old = postDb.postDao().findByPostId(post.getId());
                                if(old == null)
                                    postDb.postDao().insert(post);
                            }
                            postDb.postDao().insertAll(posts);
                        } catch (IOException e) {
                            e.printStackTrace();
                            return false;
                        }
                        break;
                    default:
                        return false;
                }
                return true;
            }
            return false;
        }

        public void onPostExecute(Boolean input){
            if (input){
                Intent broadcastReceiverIntent = new Intent(
                        ItemListActivity.ACTION_DATA_SET_CHANGED);
                Log.i(TAG, "Launching broadcast");
                LocalBroadcastManager.getInstance(PostService.this).
                        sendBroadcast(broadcastReceiverIntent);
                showNotification();
            }
            stopSelf();
        }
    }
}
