package com.uzitech.inventory_management_system.models;

import androidx.navigation.NavController;

import com.google.firebase.firestore.DocumentSnapshot;

public class MainModel {

    String Uid, device;
    int version;
    static int access_level;

    NavController navController;

    public void setUid(String Uid) {
        this.Uid = Uid;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public void setAccessLevel(DocumentSnapshot user) {
        access_level = Math.toIntExact(user.getLong("access_level"));

    }

    public void setNavController(NavController navController) {
        this.navController = navController;
    }

    public String getUid() {
        return Uid;
    }

    public String getDevice() {
        return device;
    }

    public int getVersion() {
        return version;
    }

    public int getAccessLevel() {
        return access_level;
    }

    public NavController getNavController() {
        return navController;
    }
}
