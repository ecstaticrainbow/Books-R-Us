package com.example.tomro.books_r_us;

import android.arch.persistence.room.*;

import java.util.List;

/**
 * Created by tomro on 09/12/2017.
 */

@Dao
public interface BookDao {
    @Query("SELECT * FROM Book")
    List<Book> getAll();

    @Query("SELECT * FROM Book ORDER BY uid DESC LIMIT 4")
    List<Book> getLatest();

    @Query("SELECT * FROM Book WHERE uid IN (:userIds)")
    List<Book> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM Book WHERE uid LIKE :ID LIMIT 1")
    Book findByID(int ID);

    @Insert
    void insertAll(List<Book> books);

    @Insert
    void insertOne(Book book);

    @Delete
    void delete(Book book);

    @Query("DElETE FROM Book WHERE uid LIKE :ID")
    void deleteByID(int ID);
}
