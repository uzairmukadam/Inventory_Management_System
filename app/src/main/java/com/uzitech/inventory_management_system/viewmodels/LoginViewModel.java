package com.uzitech.inventory_management_system.viewmodels;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.DocumentSnapshot;
import com.uzitech.inventory_management_system.R;

public class LoginViewModel extends MainViewModel {

    public MutableLiveData<Integer> toastMessage;
    public MutableLiveData<Boolean> resetField, inProgress;

    public LoginViewModel() {
        toastMessage = new MutableLiveData<>();
        resetField = new MutableLiveData<>();
        inProgress = new MutableLiveData<>();
    }

    public void login(String email, String password) {
        inProgress.setValue(true);

        if (checkInput(email, password)) {
            authLogin(email, password);
        } else {
            toastMessage.setValue(R.string.empty_input);
            inProgress.setValue(false);
        }
    }

    boolean checkInput(String email, String password) {
        return email != null && !email.trim().isEmpty() && password != null && !password.trim().isEmpty();
    }

    void authLogin(String email, String password) {
        authAdapter.login(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                model.setUid(authAdapter.getUID());
                checkUser();
            } else {
                toastMessage.setValue(R.string.incorrect_login);
                resetField.setValue(true);
                inProgress.setValue(false);
            }
        });
    }

    void checkUser() {
        firestoreAdapter.getUser(model.getUid()).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot user = task.getResult();
                if (Boolean.TRUE.equals(user.getBoolean("is_active"))) {
                    if (Boolean.FALSE.equals(user.getBoolean("is_login"))) {
                        model.setAccessLevel(user);
                        updateUser();
                    } else {
                        //signout and state something went wrong
                        toastMessage.setValue(R.string.contact_admin);
                        log(true, 3);
                        resetField.setValue(true);
                        inProgress.setValue(false);
                    }
                } else {
                    //signout and toast no access
                    toastMessage.setValue(R.string.contact_admin);
                    log(true, 4);
                    resetField.setValue(true);
                    inProgress.setValue(false);
                }
            }
        });
    }

    void updateUser() {
        firestoreAdapter.updateUser(model.getUid(), true).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                log(false, 5);
                navigateTo(R.id.action_loginFragment_to_dashboardFragment);
            }
        });
    }
}
