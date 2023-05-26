package com.example.mybooks;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class BookReceiver implements Parcelable {
    String title;
    String content;
    String genre;
    String imgUrl;
    String author;
    String year;
    public BookReceiver(String title, String content,  String genre, String imgUrl, String author, String year) {
        this.title = title;
        this.content = content;
        this.genre = genre;
        this.imgUrl = imgUrl;
        this.author = author;
        this.year = year;
    }

    public BookReceiver(Parcel in) {
        this.title = in.readString();
        this.content = in.readString();
        this.genre = in.readString();
        this.imgUrl = in.readString();
        this.author = in.readString();
        this.year = in.readString();
    }



    public BookReceiver() {
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {

        parcel.writeString(title);
        parcel.writeString(content);
        parcel.writeString(genre);
        parcel.writeString(imgUrl);
        parcel.writeString(author);
        parcel.writeString(year);
    }
    public static final Parcelable.Creator<BookReceiver> CREATOR = new Parcelable.Creator<BookReceiver>() {
        public BookReceiver createFromParcel(Parcel in) {
            return new BookReceiver(in);
        }

        public BookReceiver[] newArray(int size) {
            return new BookReceiver[size];
        }
    };
}