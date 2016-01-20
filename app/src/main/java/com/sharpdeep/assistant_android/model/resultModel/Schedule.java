
package com.sharpdeep.assistant_android.model.resultModel;

import android.content.Intent;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Schedule {

    @SerializedName("0")
    @Expose
    private String _0;
    @SerializedName("1")
    @Expose
    private String _1;
    @SerializedName("2")
    @Expose
    private String _2;
    @SerializedName("3")
    @Expose
    private String _3;
    @SerializedName("4")
    @Expose
    private String _4;
    @SerializedName("5")
    @Expose
    private String _5;
    @SerializedName("6")
    @Expose
    private String _6;

    /**
     * 
     * @return
     *     The _0
     */
    public String get0() {
        return _0;
    }

    /**
     * 
     * @param _0
     *     The 0
     */
    public void set0(String _0) {
        this._0 = _0;
    }

    /**
     * 
     * @return
     *     The _1
     */
    public String get1() {
        return _1;
    }

    /**
     * 
     * @param _1
     *     The 1
     */
    public void set1(String _1) {
        this._1 = _1;
    }

    /**
     * 
     * @return
     *     The _2
     */
    public String get2() {
        return _2;
    }

    /**
     * 
     * @param _2
     *     The 2
     */
    public void set2(String _2) {
        this._2 = _2;
    }

    /**
     * 
     * @return
     *     The _3
     */
    public String get3() {
        return _3;
    }

    /**
     * 
     * @param _3
     *     The 3
     */
    public void set3(String _3) {
        this._3 = _3;
    }

    /**
     * 
     * @return
     *     The _4
     */
    public String get4() {
        return _4;
    }

    /**
     * 
     * @param _4
     *     The 4
     */
    public void set4(String _4) {
        this._4 = _4;
    }

    /**
     * 
     * @return
     *     The _5
     */
    public String get5() {
        return _5;
    }

    /**
     * 
     * @param _5
     *     The 5
     */
    public void set5(String _5) {
        this._5 = _5;
    }

    /**
     * 
     * @return
     *     The _6
     */
    public String get6() {
        return _6;
    }

    /**
     * 
     * @param _6
     *     The 6
     */
    public void set6(String _6) {
        this._6 = _6;
    }

    public String get(int index){
        switch (index){
            case 0:
                return _0;
            case 1:
                return _1;
            case 2:
                return _2;
            case 3:
                return _3;
            case 4:
                return _4;
            case 5:
                return _5;
            case 6:
                return _6;
            default:
                return "";
        }
    }

    //一周几次,连续课程算一次
    public int getClassTimeEachWeek(){
        int count = 0;
        for (int i = 0; i < 7; i++){
            if (!"".equals(get(i))){
                count++;
            }
        }
        return count;
    }

    public List<Integer> getClassWeeks(){
        List<Integer> classWeeks = new ArrayList<>();
        for (int i = 0; i < 7; i++){
            if (!"".equals(get(i))){
                classWeeks.add(i);
            }
        }
        return classWeeks;
    }

    public List<Integer> getClassTimeByWeek(int week){
        List<Integer> classTime = new ArrayList<>();
        String strTime = get(week);
        for (int i = 0; i < strTime.length(); i++){
            String tempStr = String.valueOf(strTime.charAt(i));
            if (tempStr.equals("单") || tempStr.equals("双")){
                continue;
            }
            classTime.add(str2int(tempStr));
        }
        return classTime;
    }

    private int str2int(String time){
        if (!time.equals("A") && !time.equals("B") && !time.equals("C") &&! time.equals( "0")){
            return Integer.valueOf(time);
        }
        switch (time) {
            case "0":
                return 10;
            case "A":
                return 11;
            case "B":
                return 12;
            case "C":
                return 13;
            default:
                return 0;
        }
    }

    //该天上多少课时,不一定连续
    public int getClassCountByWeek(int week){
        return getClassTimeByWeek(week).size();
    }
}
