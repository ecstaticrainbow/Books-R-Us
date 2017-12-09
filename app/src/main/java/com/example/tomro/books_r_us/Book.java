package com.example.tomro.books_r_us;

import android.arch.persistence.room.*;

/**
 * Created by tomro on 08/12/2017.
 */
@Entity
public class Book {
    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "bookTitle")
    private String bookTitle;

    @ColumnInfo(name = "bookAuthor")
    private String bookAuthor;

    @ColumnInfo(name = "bookDesc")
    private String bookDesc;

    @ColumnInfo(name = "bookPrice")
    private double bookPrice;

    @ColumnInfo(name = "bookImageUrl")
    private String bookImageUrl;

    Book(String bookTitle, String bookAuthor, String bookDesc, double bookPrice, String bookImageUrl){
        this.bookTitle = bookTitle;
        this.bookAuthor = bookAuthor;
        this.bookDesc = bookDesc;
        this.bookPrice = bookPrice;
        this.bookImageUrl = bookImageUrl;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }

    public String getBookDesc() {
        return bookDesc;
    }

    public void setBookDesc(String bookDesc) {
        this.bookDesc = bookDesc;
    }

    public double getBookPrice() {
        return bookPrice;
    }

    public void setBookPrice(double bookPrice) {
        this.bookPrice = bookPrice;
    }

    public String getBookImageUrl() {
        return bookImageUrl;
    }

    public void setBookImageUrl(String bookImageUrl) {
        this.bookImageUrl = bookImageUrl;
    }
}
