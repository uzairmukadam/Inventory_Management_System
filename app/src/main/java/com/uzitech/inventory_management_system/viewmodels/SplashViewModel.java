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

                // check application version
                if (version_code >= metadata.getLong("supported_version")
                        && version_code <= metadata.getLong("current_version")) {

                    // outdated application version
                    if (version_code > metadata.getLong("current_version")) {
                        toastMessage.setValue(R.string.update_available);
                    }

                    checkLogin();
                } else {
                    // unsupported application version detected
                    toastMessage.setValue(R.string.reinstall_app);
                    log(true, 0);
                    if (model.getUid() != null) {
                        authAdapter.signout();
                    }
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

                // check user account status
                if (Boolean.TRUE.equals(user.getBoolean("is_active"))) {

                    // check if user already logged in
                    if (Boolean.TRUE.equals(user.getBoolean("is_login"))) {
                        model.setAccessLevel(user);
                        navigateTo(R.id.action_splashFragment_to_dashboardFragment);
                    } else {
                        toastMessage.setValue(R.string.contact_admin);
                        log(true, 1);
                        authAdapter.signout();
                        navigateTo(R.id.action_splashFragment_to_loginFragment);
                    }
                } else {
                    toastMessage.setValue(R.string.contact_admin);
                    log(true, 2);
                    authAdapter.signout();
                    navigateTo(R.id.action_splashFragment_to_loginFragment);
                }
            }
        });
    }
}
