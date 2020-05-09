package com.ak.bakingtutorial.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.squareup.moshi.Json;


public class Recipe implements Parcelable {

    @Json(name = "id")
    private int id;

    @Json(name = "name")
    private String name;

    @Json(name = "servings")
    private int servings;

    @Json(name = "image")
    private String mImageUrl;

    public Recipe(int id, String name, int servings, String imageUrl) {
        this.id = id;
        this.name = name;
        this.servings = servings;
        this.mImageUrl = imageUrl;
    }


    private Recipe(Parcel in) {
        id = in.readInt();
        name = in.readString();
        servings = in.readInt();
        mImageUrl = in.readString();
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeInt(servings);
        parcel.writeString(mImageUrl);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getServings() {
        return servings;
    }

    public String getmImageUrl() {
        return mImageUrl;
    }
}
