package com.hellofit.kidozone.entity;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/***
 *  The entity of waste in the project
 *  To store the waste data of user or school
 *
 *  Created by Mingzhe Liu on 08/30/19.
 *  Copyright @ 2019 Mingzhe Liu. All right reserved
 *
 *  @author Mingzhe Liu
 *  @version 2.4
 *
 *  Final modified date: 09/05/2019 by Mingzhe Liu
 */

public class WasteInfo implements Parcelable, java.io.Serializable {

    private String id;
    private String categoryName;
    private String wasteName;
    private String wasteImagePath;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getWasteName() {
        return wasteName;
    }

    public void setWasteName(String wasteName) {
        this.wasteName = wasteName;
    }

    public String getWasteImage() {
        return wasteImagePath;
    }

    public void setWasteImage(String wasteImagePath) {
        this.wasteImagePath = wasteImagePath;
    }

    public WasteInfo() { super(); }

    public WasteInfo(String id, String categoryName, String wasteName, String wasteImagePath) {
        this.id = id;
        this.categoryName = categoryName;
        this.wasteName = wasteName;
        this.wasteImagePath = wasteImagePath;
    }

    public WasteInfo(Parcel source) {
        this.id = source.readString();
        this.categoryName = source.readString();
        this.wasteName = source.readString();
        this.wasteImagePath = source.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(categoryName);
        parcel.writeString(wasteName);
        parcel.writeString(wasteImagePath);
    }

    public static final Creator<WasteInfo> CREATOR = new Creator<WasteInfo>() {
        @Override
        public WasteInfo createFromParcel(Parcel parcel) {
            return new WasteInfo(parcel);
        }

        @Override
        public WasteInfo[] newArray(int i) {
            return new WasteInfo[i];
        }
    };

}
