package com.example.zhao.top_school.Date;

import java.util.ArrayList;

/**
 * Created by zhao on 2016/5/18.
 */
public class InterestItemClass {
    private String one_but,sec_but,tree_but;
    private ArrayList<String> one_list,two_list,three_list,answer_list;
    public boolean[] vis_one,vis_two,vis_three;
    private int one=2,two=2,three=2;//2-无数据 1-选中 0-未选中

    public ArrayList<String> getButList(int num){
        switch (num){
            case 1:
                return one_list;
            case 2:
                return two_list;
            case 3:
                return three_list;
            default:
                return null;
        }
    }
    public boolean[] getBool(int num){
        switch (num){
            case 1:
                return vis_one;
            case 2:
                return vis_two;
            case 3:
                return vis_three;
            default:
                return null;
        }
    }
    public InterestItemClass(){
        vis_one=new boolean[10];
        vis_two=new boolean[10];
        vis_three=new boolean[10];
        for(int i=0;i<vis_three.length;i++){
            vis_three[i]=false;
            vis_two[i]=false;
            vis_one[i]=false;
        }
        one_but=null;
        tree_but=null;
        sec_but=null;
        one_list=null;
        two_list=null;
        three_list=null;
        answer_list=new ArrayList<String>();
    }
    public void set_name(int num,String name){
        switch (num%3){
            case 1:
                this.one_but=name;
                one=0;
                break;
            case 2:
                this.sec_but=name;
                two=0;
                break;
            case 0:
                this.tree_but=name;
                three=0;
                break;
        }
    }
    public void setName(String one_but,String two_but,String three_but){
        this.one_but=one_but;
        this.tree_but=three_but;
        this.sec_but=two_but;
        if (this.one_but !=null) {
            one=0;
        }
        if(this.sec_but!=null){
            two=0;
        }
        if(this.tree_but!=null){
            three=0;
        }
    }
    public String getButName(int num){
        switch (num){
            case 1:
                return one_but;
            case 2:
                return sec_but;
            case 3:
                return tree_but;
            default:
                return null;
        }
    }
    public void setBUT(int num,ArrayList<String> but_list){
        switch (num%3) {
            case 1:
                one = 0;
                one_list=but_list;
                break;
            case 2:
                two = 0;
                two_list=but_list;
                break;
            case 0:
                three = 0;
                three_list=but_list;
                break;
            default:
                break;
        }
    }
    public int checkBut(int num){
        switch (num){
            case 1:
                if(one_but!=null) {
                    if (check_select(num)) {
                        one = 1;
                    } else
                        one = 0;
                    return one;
                }
            case 2:
                if(sec_but!=null) {
                    if (check_select(num)) {
                        two = 1;
                    } else
                        two = 0;
                    return two;
                }
            case 3:
                if(tree_but!=null) {
                    if (check_select(num)) {
                        three = 1;
                    } else
                        three = 0;
                    return three;
                }
            default:
                return 5;
        }
    }
    private boolean check_select(int num){
        switch (num){
            case 1:
                if(one_list!=null){
                    for (int i = 0; i < one_list.size(); i++) {
                        if (vis_one[i]) {
                            return true;
                        }
                    }
                }
                return false;
            case 2:
                if(two_list!=null) {
                    for (int i = 0; i < two_list.size(); i++) {
                        if (vis_two[i]) {
                            return true;
                        }
                    }
                }
                return false;
            case 3:
                if(three_list!=null) {
                    for (int i = 0; i < three_list.size(); i++) {
                        if (vis_three[i]) {
                            return true;
                        }
                    }
                }
                return false;
            default:
                return false;
        }
    }
    public ArrayList<String> checkBut_list(int num){
        switch (num) {
            case 1:
                isSelect(num);
                return answer_list;
            case 2:
                isSelect(num);
                return answer_list;
            case 3:
                isSelect(num);
                return answer_list;
            default:
                return null;
        }
    }
    private void isSelect(int num){
        switch (num){
            case 1:
                answer_list.clear();
                if(one_list!=null) {
                    for (int i = 0; i < one_list.size(); i++) {
                        if (vis_one[i]) {
                            answer_list.add(one_list.get(i));
                        }
                    }
                }
                break;
            case 2:
                answer_list.clear();
                if(two_list!=null) {
                    for (int i = 0; i < two_list.size(); i++) {
                        if (vis_two[i]) {
                            answer_list.add(two_list.get(i));
                        }
                    }
                }
                break;
            case 3:
                answer_list.clear();
                if(three_list!=null) {
                    for (int i = 0; i < three_list.size(); i++) {
                        if (vis_three[i]) {
                            answer_list.add(three_list.get(i));
                        }
                    }
                }
                break;
            default:
                break;
        }
    }
    public void ClickBut(int num,boolean state){
        switch (num){
            case 1:
                if(state){
                    one=1;
                }
                else
                    one=0;
                break;
            case 2:
                if(state){
                    two=1;
                }
                else
                    two=0;
                break;
            case 3:
                if(state){
                    three=1;
                }
                else
                    three=0;
                break;
        }
    }
}
