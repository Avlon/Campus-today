package com.example.zhao.top_school.Date;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by zhao on 2016/6/5.
 */
public class SetUpDateClass implements Parcelable {
    private ArrayList<String> department;
    private HashMap<String,ArrayList<String>> maps;
    public void setDepartment(ArrayList<String> department){
        this.department=department;
    }
    public void setMaps(HashMap<String,ArrayList<String>> maps){
        this.maps=maps;
    }
    public  HashMap<String,ArrayList<String>> getMaps(){
        return this.maps;
    }
    public ArrayList<String> getDepartment(){
        return this.department;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags){
        try {
            dest.writeList(department);
            dest.writeMap(maps);
        }catch (Exception e){
            Log.e("1","1"+e.toString());
        }
    }
    @Override
    public int describeContents(){
        return 0;
    }
    public static final Parcelable.Creator<SetUpDateClass> CREATOR =new Creator<SetUpDateClass>() {
        public SetUpDateClass createFromParcel(Parcel source) {
            SetUpDateClass setUpDateClass = new SetUpDateClass();
            try {
                setUpDateClass.department = source.readArrayList(ArrayList.class.getClassLoader());
                setUpDateClass.maps = source.readHashMap(HashMap.class.getClassLoader());
                Log.e("1","maps"+setUpDateClass.maps.toString());
                Log.e("1","department"+setUpDateClass.department.toString());
            }catch (Exception e){
                Log.e("1","2"+e.toString());
            }
            return setUpDateClass;
        }
        public SetUpDateClass[] newArray(int size) {
            return new SetUpDateClass[size];
        }
    };
}
