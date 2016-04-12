package com.sharpdeep.assistant_android.model.dbModel;

import com.orm.SugarRecord;

/**
 * Created by bear on 16-1-13.
 */
public class AppInfo extends SugarRecord {
    private User currentUser;

    public AppInfo() {
    }


    public AppInfo(User currentUser) {
        this.currentUser = currentUser;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public void logout(){
        this.currentUser = null;
        this.save();
    }

}
