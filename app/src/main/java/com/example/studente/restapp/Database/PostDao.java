package com.example.studente.restapp.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.database.Cursor;

import com.example.studente.restapp.Post;

import java.util.List;

@Dao
public interface PostDao {
    @Query("SELECT * FROM post")
    List<Post> getAll();

    @Query("SELECT * FROM post WHERE postId IN (:ids)")
    List<Post> loadAllByIds(int[] ids);

    @Query("SELECT * FROM post WHERE user_id = :userId LIMIT 1")
    Post findByUserId(int userId);

    @Query("SELECT * FROM post WHERE postId = :postId")
    Post findByPostId(int postId);

    @Insert
    void insertAll(Post... posts);

    @Update
    void update(Post post);

    @Delete
    void delete(Post post);

    @Query("DELETE FROM post")
    void truncate();

    @Query("SELECT * FROM post")
    Cursor getCursorAllPost();

    @Query("SELECT * FROM post WHERE postId = :id")
    Cursor loadPostById(int id);

}
