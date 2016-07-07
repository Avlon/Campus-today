package com.example.zhao.top_school.Date;

/**
 * Created by zhao on 2016/3/27.
 */
public class ListItemClass {
    private String new_title;
    private String creat_time;
    private String watch_times;
    private String news_url;
    public void setNews_url(String a){this.news_url=a;}
    public void setNew_title(String a){
        this.new_title=a;
    }
    public void setCreat_time(String a){
        this.creat_time=a;
    }
    public void setWatch_times(String a){
        this.watch_times=a;
    }
    public String getNew_title(){
        return new_title;
    }
    public String getCreat_time(){
        return creat_time;
    }
    public String getWatch_times(){
        return watch_times;
    }
    public String getNews_url(){
        return news_url;
    }
}
