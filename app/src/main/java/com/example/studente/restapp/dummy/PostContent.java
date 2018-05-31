package com.example.studente.restapp.dummy;

import com.example.studente.restapp.Post;

import java.util.ArrayList;
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

    /**
     * An array of sample (dummy) items.
     */
    public static final List<Post> ITEMS = new ArrayList<Post>();

    private static final int COUNT = 25;

    static {//codice di inizializzazione viene eseguito prima del costruttore
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createPost(i));
        }
    }

    private static void addItem(Post item) {
        ITEMS.add(item);
    }

    private static Post createPost(int position) {
        return new Post(0, position, "Title " + position, "Body " + position, false);
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }
}
