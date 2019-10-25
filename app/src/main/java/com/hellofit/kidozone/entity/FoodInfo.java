package com.hellofit.kidozone.entity;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/***
 *  The entity of food in the project
 *  To store the food data of user or school
 *
 *  Created by Mingzhe Liu on 08/30/19.
 *  Copyright @ 2019 Mingzhe Liu. All right reserved
 *
 *  @author Mingzhe Liu
 *  @version 2.4
 *
 *  Final modified date: 09/05/2019 by Mingzhe Liu
 */

public class FoodInfo implements Parcelable, java.io.Serializable {

    private String id;
    private String foodName;
    private String categoryName;
    private String foodImagePath;

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getFoodName() { return foodName; }

    public void setFoodName(String foodName) { this.foodName = foodName; }

    public String getCategoryName() { return categoryName; }

    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public String getFoodImage() { return foodImagePath; }

    public void setFoodImage(String foodImagePath) { this.foodImagePath = foodImagePath; }

    public FoodInfo() { super(); }

    public FoodInfo(String id, String foodName, String categoryName, String foodImagePath) {
        this.id = id;
        this.foodName = foodName;
        this.categoryName = categoryName;
        this.foodImagePath = foodImagePath;
    }

    public FoodInfo(Parcel source) {
        this.id = source.readString();
        this.foodName = source.readString();
        this.categoryName = source.readString();
        this.foodImagePath = source.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * write the value into the Parcel
     * @param parcel
     * @param i
     */
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.id);
        parcel.writeString(this.foodName);
        parcel.writeString(this.categoryName);
        parcel.writeString(this.foodImagePath);
    }

    public static final Creator<FoodInfo> CREATOR = new Creator<FoodInfo>() {

        /**
         * For read the data from Parcel Obj
         * @param parcel
         * @return FoodInfo entity
         */
        @Override
        public FoodInfo createFromParcel(Parcel parcel) {
            return new FoodInfo(parcel);
        }

        @Override
        public FoodInfo[] newArray(int i) {
            return new FoodInfo[i];
        }
    };
}
