package com.example.zhao.top_school.Date;

/**
 * Created by zhao on 2016/3/25.
 */
public class HorItemClass {
    public int id;
    public String title;
    public String type;
    public boolean is_interest;
    public int getId(){
        return id;
    }
    public void setId(int a){
        this.id=a;
    }
    public String getTitle(){
        return title;
    }
    public void setTitle(String a){
        title=a;
    }
    public String getType(){
        return type;
    }
    public void setType(String a){
        this.type=a;
    }
    public boolean is_my_interest(){
        return is_interest;
    }
    public void setIs_interest(boolean a){
        this.is_interest=a;
    }
}

