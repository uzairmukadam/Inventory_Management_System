package com.uzitech.inventory_management_system.viewmodels;

import androidx.lifecycle.MutableLiveData;

import com.uzitech.inventory_management_system.R;

import java.util.Map;

public class SplashViewModel extends MainViewModel {

    public MutableLiveData<Integer> toastMessage;

    public SplashViewModel() {
        toastMessage = new MutableLiveData<>();
    }

    public void initiateCheck() {
        firestoreAdapter.getMetadata().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Map<String, Object> metadata = task.getResult().getData();
                int version_code = model.getVersion();

                // check application version
                if (version_code >= (long) metadata.get("supported_version")
                        && version_code <= (long) metadata.get("current_version")) {

                    // outdated application version
                    if (version_code > (long) metadata.get("current_version")) {
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
                Map<String, Object> user = task.getResult().getData();

                // check user account status
                if ((boolean) user.get("is_active")) {

                    // check if user already logged in
                    if ((boolean) user.get("is_login")) {
                        model.setAccessLevel((long) user.get("access_level"));
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
