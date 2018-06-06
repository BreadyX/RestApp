package com.example.studente.restapp.dummy;

import com.example.studente.restapp.Database.PostDao;
import com.example.studente.restapp.Database.PostDatabase;
import com.example.studente.restapp.Post;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class PostContent {

    public static final List<Post> ITEMS = new ArrayList<Post>();

    protected  PostDatabase db;

    private static final int COUNT = 25;

    public void loadDummyPostInDatabase(){
        PostDao dao = db.postDao();
        List<Post> postList = dao.getAll();
        if (postList == null || postList.size() == 0){
            postList = addItemToDatabase();
        }
            PostContent.ITEMS.addAll(postList);
    }

    private List<Post> addItemToDatabase() {
        PostDao dao = db.postDao();
        Post[] postArray = new Post[COUNT];
        for (int i = 1; i <= COUNT; i++) {
            Post post = createPost(i);
            postArray[i] = post;
        }
        dao.insertAll(postArray);
        return Arrays.asList(postArray);
    }

    public PostContent(){}

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
}
