package com.example.studente.restapp.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import com.example.studente.restapp.Post;

@Database(entities = { Post.class }, version = 1)
@TypeConverters({UriConverter.class})
public abstract class PostDatabase extends RoomDatabase {
    public static final String DATABASE_NAME = "database-post";
    public abstract PostDao postDao();
}
