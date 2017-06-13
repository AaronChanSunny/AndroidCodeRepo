package com.aaron.servicecomponent;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017/6/13.
 */

public class Student implements Parcelable {

    private String mName;
    private int mAge;

    public Student(String name, int age) {
        mName = name;
        mAge = age;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public int getAge() {
        return mAge;
    }

    public void setAge(int age) {
        mAge = age;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mName);
        dest.writeInt(this.mAge);
    }

    public Student() {
    }

    protected Student(Parcel in) {
        this.mName = in.readString();
        this.mAge = in.readInt();
    }

    public static final Parcelable.Creator<Student> CREATOR = new Parcelable.Creator<Student>() {
        @Override
        public Student createFromParcel(Parcel source) {
            return new Student(source);
        }

        @Override
        public Student[] newArray(int size) {
            return new Student[size];
        }
    };

    @Override
    public String toString() {
        return "{name:" + mName + ", age:" + mAge + "}";
    }
}
