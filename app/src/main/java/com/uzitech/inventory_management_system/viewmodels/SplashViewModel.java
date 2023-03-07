package com.uzitech.inventory_management_system.viewmodels;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.DocumentSnapshot;
import com.uzitech.inventory_management_system.R;

public class SplashViewModel extends MainViewModel {

    public MutableLiveData<Integer> toastMessage;

    public SplashViewModel() {
        toastMessage = new MutableLiveData<>();
    }

    public void initiateCheck() {
        firestoreAdapter.getMetadata().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot metadata = task.getResult();
                int version_code = model.getVersion();

                if (version_code >= metadata.getLong("supported_version")
                        && version_code <= metadata.getLong("current_version")) {

                    if (version_code > metadata.getLong("current_version")) {
                        toastMessage.setValue(R.string.update_available);
                    }

                    checkLogin();
                } else {
                    //deny and say reinstall the application
                    toastMessage.setValue(R.string.reinstall_app);
                    log(true, 0);
                }
            }
        });
    }

    void checkLogin() {
        if (model.getUid() != null) {
            checkUser();
        } else {
            navigateTo(R.id.action_splashFragment_to_loginFragment);
        }
    }

    void checkUser() {
        firestoreAdapter.getUser(model.getUid()).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot user = task.getResult();
                if (Boolean.TRUE.equals(user.getBoolean("is_active"))) {
                    if (Boolean.TRUE.equals(user.getBoolean("is_login"))) {
                        model.setUser(user);
                        navigateTo(R.id.action_splashFragment_to_dashboardFragment);
                    } else {
                        //signout and state something went wrong
                        toastMessage.setValue(R.string.contact_admin);
                        log(true, 1);
                    }
                } else {
                    //signout and toast no access
                    toastMessage.setValue(R.string.contact_admin);
                    log(true, 2);
                }
            }
        });
    }
}
