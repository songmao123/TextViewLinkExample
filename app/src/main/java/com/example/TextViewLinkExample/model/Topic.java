package com.example.TextViewLinkExample.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 青松 on 2016/10/10.
 */

public class Topic implements Parcelable {

    private int topicId;
    private String topicName;

    public Topic() {
    }

    public Topic(int topicId, String topicName) {
        this.topicId = topicId;
        this.topicName = topicName;
    }

    protected Topic(Parcel in) {
        topicId = in.readInt();
        topicName = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(topicId);
        dest.writeString(topicName);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Topic> CREATOR = new Creator<Topic>() {
        @Override
        public Topic createFromParcel(Parcel in) {
            return new Topic(in);
        }

        @Override
        public Topic[] newArray(int size) {
            return new Topic[size];
        }
    };

    public int getTopicId() {
        return topicId;
    }

    public void setTopicId(int topicId) {
        this.topicId = topicId;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }
}
