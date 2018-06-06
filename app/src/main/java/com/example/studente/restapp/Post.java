package com.example.studente.restapp;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

@Entity()
public class Post implements Parcelable {

    @PrimaryKey
    private int postId;
    @ColumnInfo(name = "user_id")
    private int userId;
    @ColumnInfo(name = "title")
    private String title;
    @ColumnInfo(name = "body")
    private String body;
    @ColumnInfo(name = "favourite")
    private boolean favourite;
    @ColumnInfo(name = "image_uri")
    private Uri imageUri;

    public Post(){}

    public Post(int userId, int postId, String title, String body, boolean favourite, Uri imageUri) {
        this.userId = userId;
        this.postId = postId;
        this.title = title;
        this.body = body;
        this.favourite = favourite;
        this.imageUri = imageUri;
    }

    public Post(int userId, int postId, String title, String body, boolean favourite) {
        this(userId, postId, title, body, favourite, null);
    }

    protected Post(Parcel in) {
        userId = in.readInt();
        postId = in.readInt();
        title = in.readString();
        body = in.readString();
        favourite = in.readInt() == 0 ? false : true;
        String uriString = in.readString();
        if (uriString != null)
            imageUri = Uri.parse(uriString);
    }

    public static final Creator<Post> CREATOR = new Creator<Post>() {
        @Override
        public Post createFromParcel(Parcel in) {
            return new Post(in);
        }

        @Override
        public Post[] newArray(int size) {
            return new Post[size];
        }
    };

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(userId);
        dest.writeInt(postId);
        dest.writeString(title);
        dest.writeString(body);
        dest.writeInt(favourite ? 1:0);
        if (imageUri != null)
            dest.writeString(imageUri.toString());
    }
}
