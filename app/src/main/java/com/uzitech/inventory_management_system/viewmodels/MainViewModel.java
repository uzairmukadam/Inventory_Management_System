package com.uzitech.inventory_management_system.viewmodels;

import android.os.Build;

import androidx.lifecycle.ViewModel;

import com.uzitech.inventory_management_system.BuildConfig;
import com.uzitech.inventory_management_system.adapters.FirebaseAuthAdapter;
import com.uzitech.inventory_management_system.adapters.FirebaseFirestoreAdapter;
import com.uzitech.inventory_management_system.adapters.logMessages;
import com.uzitech.inventory_management_system.models.MainModel;

public class MainViewModel extends ViewModel {

    logMessages logMessages;
    FirebaseFirestoreAdapter firestoreAdapter;
    FirebaseAuthAdapter authAdapter;
    public MainModel model;

    public MainViewModel() {

        firestoreAdapter = new FirebaseFirestoreAdapter();
        authAdapter = new FirebaseAuthAdapter();

        model = new MainModel();

        model.setUid(authAdapter.getUID());
        model.setDevice(Build.DEVICE);
        model.setVersion(BuildConfig.VERSION_CODE);

        logMessages = new logMessages();
    }

    public void navigateTo(int id) {
        model.getNavController().navigate(id);
    }

    public void log(boolean critical, int id) {
        String message = logMessages.getMessage(id);

        firestoreAdapter.log(model.getUid(), model.getDevice(), model.getVersion(), critical, message)
                .addOnCompleteListener(task -> {

                });
    }
}

