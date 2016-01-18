package com.sharpdeep.assistant_android.model.eventModel;

/**
 * Created by bear on 16-1-17.
 */
public class ImportDialogEvent {
    String selectYear = "";
    int selectSemester = 1;


    public String getSelectYear() {
        return selectYear;
    }

    public void setSelectYear(String selectYear) {
        this.selectYear = selectYear;
    }

    public int getSelectSemester() {
        return selectSemester;
    }

    public void setSelectSemester(int selectSemester) {
        this.selectSemester = selectSemester;
    }
}
