package com.example.zhao.top_school.Tool;

import com.example.zhao.top_school.Date.HorItemClass;
import com.example.zhao.top_school.Date.InterestItemClass;
import com.example.zhao.top_school.Date.ListItemClass;

import java.util.ArrayList;

/**
 * Created by zhao on 2016/3/25.
 */
public class test_get_date {
    public ArrayList<HorItemClass> getDate(){
        ArrayList<HorItemClass> newArry=new ArrayList<HorItemClass>();
        HorItemClass newclass =new HorItemClass();
        newclass.setId(0);
        newclass.setTitle("推荐");
        newArry.add(newclass);
        newclass =new HorItemClass();
        newclass.setId(1);
        newclass.setTitle("教务处");
        newArry.add(newclass);
        newclass =new HorItemClass();
        newclass.setId(2);
        newclass.setTitle("人事处");
        newArry.add(newclass);
        newclass =new HorItemClass();
        newclass.setId(3);
        newclass.setTitle("软件学院");
        newArry.add(newclass);
        newclass =new HorItemClass();
        newclass.setId(4);
        newclass.setTitle("新闻部");
        newArry.add(newclass);
        newclass =new HorItemClass();
        newclass.setId(5);
        newclass.setTitle("理学院");
        newArry.add(newclass);
        return newArry;
    }
    static public ArrayList<ListItemClass> getList(){
        ArrayList<ListItemClass> newsList=new ArrayList<ListItemClass>();
        for(int i=0;i<8;i++){
            ListItemClass news=new ListItemClass();
            news.setCreat_time("发布时间：2016-3-27");
            news.setWatch_times("点击数：0");
            news.setNew_title("测试");
            newsList.add(news);
        }
        return newsList;
    }
    static public ArrayList<InterestItemClass> get_interest_list(){
        ArrayList<InterestItemClass> newsList=new ArrayList<InterestItemClass>();
        ArrayList<String> test_list=new ArrayList<String>();
        for(int i=0;i<4;i++){
            test_list.add("测试");
        }
        for(int i=0;i<13;i++){
            InterestItemClass news=new InterestItemClass();
            news.setName("测试","测试","测试");
            for(int j=1;j<4;j++){
                news.setBUT(j,test_list);
            }
            newsList.add(news);
        }
        InterestItemClass news=new InterestItemClass();
        news.setName("测试","测试",null);
        news.setBUT(1,test_list);
        news.setBUT(2,test_list);
        newsList.add(news);
        return newsList;
    }
}
