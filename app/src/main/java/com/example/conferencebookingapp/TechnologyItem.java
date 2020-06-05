package com.example.conferencebookingapp;

import android.os.Parcel;
import android.os.Parcelable;
/**
 * @author Karin
 */
public class TechnologyItem implements Parcelable {
    private String id;
    private String description;
    private String price;

    public TechnologyItem() {
        id = "";
        description = "";
        price = "";
    }

    protected TechnologyItem(Parcel in) {
        id = in.readString();
        description = in.readString();
        price = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(description);
        dest.writeString(price);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TechnologyItem> CREATOR = new Creator<TechnologyItem>() {
        @Override
        public TechnologyItem createFromParcel(Parcel in) {
            return new TechnologyItem(in);
        }

        @Override
        public TechnologyItem[] newArray(int size) {
            return new TechnologyItem[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
